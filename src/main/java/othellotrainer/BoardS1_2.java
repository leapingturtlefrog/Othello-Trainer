package othellotrainer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

/**
 *
 */
public class BoardS1_2 {
    // In bits: 0 = not present, 1 = present. For empty squares this means 0 = occupied, 1 = empty
    // [Black squares, White squares, empty squares]
    protected long[] squares;
    // The squares that disks can be places on for the active player
    protected long moveableSquares;

    protected int activePlayer; // 0 = Black, 1 = White
    protected int[] score; // {Black score, White score}
    protected int move; // Current move number
    protected boolean gameOver;

    protected final Random random; // Used for making random moves
    // In bits: 0 = not valid, 1 = valid
    // For a given square, the "options" next to the square that are valid
    // Bit order (LSB to MSB): top left, top, top right, left, right, bottom left, bottom, bottom right
    protected final byte[] validNextSquares = new byte[]{
            (byte) 0b11010000, (byte) 0b11111000, (byte) 0b11111000, (byte) 0b11111000,
            (byte) 0b11111000, (byte) 0b11111000, (byte) 0b11111000, (byte) 0b01101000,

            (byte) 0b11010110, (byte) 0b11111111, (byte) 0b11111111, (byte) 0b11111111,
            (byte) 0b11111111, (byte) 0b11111111, (byte) 0b11111111, (byte) 0b01101011,

            (byte) 0b11010110, (byte) 0b11111111, (byte) 0b11111111, (byte) 0b11111111,
            (byte) 0b11111111, (byte) 0b11111111, (byte) 0b11111111, (byte) 0b01101011,

            (byte) 0b11010110, (byte) 0b11111111, (byte) 0b11111111, (byte) 0b11111111,
            (byte) 0b11111111, (byte) 0b11111111, (byte) 0b11111111, (byte) 0b01101011,

            (byte) 0b11010110, (byte) 0b11111111, (byte) 0b11111111, (byte) 0b11111111,
            (byte) 0b11111111, (byte) 0b11111111, (byte) 0b11111111, (byte) 0b01101011,

            (byte) 0b11010110, (byte) 0b11111111, (byte) 0b11111111, (byte) 0b11111111,
            (byte) 0b11111111, (byte) 0b11111111, (byte) 0b11111111, (byte) 0b01101011,

            (byte) 0b11010110, (byte) 0b11111111, (byte) 0b11111111, (byte) 0b11111111,
            (byte) 0b11111111, (byte) 0b11111111, (byte) 0b11111111, (byte) 0b01101011,

            (byte) 0b00010110, (byte) 0b00011111, (byte) 0b00011111, (byte) 0b00011111,
            (byte) 0b00011111, (byte) 0b00011111, (byte) 0b00011111, (byte) 0b00001011
    };
    protected final int[] compAddList = new int[]{-9, -8, -7, -1, 1, 7, 8, 9};

    protected final int LOOK_AHEADS = 1;
    protected final int ITERATIONS_PER_LOOK_AHEAD = 100;
    protected long[] savedSquares;
    protected int[] savedScore;
    protected int savedActivePlayer;
    protected int savedMove;
    protected boolean savedGameOver;

    public boolean moveS1(int player) {
        save();
        long moveableSquaresTemp = getMoveableSquares(player);
        int opponent = player == 0 ? 1 : 0;

        if (moveableSquaresTemp == 0) {
            return false;
        } else {
            int[] moveableSquaresList = new int[64];
            int[] totalPointsDifferenceList = new int[64];
            int highestDifferenceIndex = 0;
            int idx = 0;

            for (int i = 0; i < 64; i++) {
                if ((moveableSquaresTemp & (1L << i)) != 0) {
                    moveableSquaresList[idx] = i;
                    totalPointsDifferenceList[idx] = 0;
                    for (int iters = 0; iters < ITERATIONS_PER_LOOK_AHEAD; iters++) {
                        for (int j = 0; j < 61; j++) {
                            if (!makeRandomMove(getActivePlayer())) {
                                totalPointsDifferenceList[idx] += score[player] - score[opponent];
                                break;
                            }
                        }
                        load();
                    }
                    if (totalPointsDifferenceList[idx] > totalPointsDifferenceList[highestDifferenceIndex]) {
                        highestDifferenceIndex = idx;
                    }
                    idx++;
                }
            }
            int pos = moveableSquaresList[highestDifferenceIndex];

            return move_print(player, pos);
        }
    }

    public void save() {
        savedSquares = Arrays.copyOf(squares, squares.length);
        savedScore = Arrays.copyOf(score, score.length);
        savedActivePlayer = activePlayer;
        savedMove = move;
        savedGameOver = gameOver;
    }

    public void load() {
        squares = Arrays.copyOf(savedSquares, savedSquares.length);
        score = Arrays.copyOf(savedScore, savedScore.length);
        activePlayer = savedActivePlayer;
        move = savedMove;
        gameOver = savedGameOver;
    }

    /**
     * Standard board setup
     */
    public BoardS1_2() {
        squares = new long[]{
                0b0000000000000000000000000000100000010000000000000000000000000000L,
                0b0000000000000000000000000001000000001000000000000000000000000000L,
                0b1111111111111111111111111110011111100111111111111111111111111111L
        };
        moveableSquares = 0b0000000000000000000100000010000000000100000010000000000000000000L;

        activePlayer = 0;
        score = new int[]{2, 2};
        move = 0;
        gameOver = false;

        random = new Random();
    }

    public boolean move(int player, int pos) {
        int opponent = player ^ 1;
        // Return opponent's disks that can be flipped based on current position
        long disksToFlip = getDisksToFlip(player, pos);
        int disksFlipped = 0;

        if (disksToFlip == 0) { // If this is not a valid move
            return false;
        } else { // Valid move
            // Update the player's squares, opponent's, and the empty squares
            squares[player] |= disksToFlip | (1L << pos);
            squares[opponent] ^= disksToFlip;
            squares[2] ^= 1L << pos;

            // Use Brain Kernighan's Algorithm to find the number of disks flipped
            while (disksToFlip > 0) {
                disksToFlip &= disksToFlip - 1;
                disksFlipped++;
            }

            // Set new score and update move number
            score[player] += disksFlipped + 1;
            score[opponent] -= disksFlipped;
            move++;

            if (move == 60) {
                gameOver = true;
            } else {
                // Update the moveable squares and active player
                moveableSquares = getMoveableSquares(opponent);
                if (moveableSquares != 0) {
                    activePlayer = opponent;
                } else {
                    moveableSquares = getMoveableSquares(player);

                    if (moveableSquares == 0) { // Neither player can move
                        gameOver = true;
                    }
                }
            }

            return true;
        }
    }

    public boolean move_print(int player, int pos) {
        int opponent = player ^ 1;
        // Return opponent's disks that can be flipped based on current position
        long disksToFlip = getDisksToFlip(player, pos);
        int disksFlipped = 0;

        if (disksToFlip == 0) { // If this is not a valid move
            return false;
        } else { // Valid move
            // Update the player's squares, opponent's, and the empty squares
            squares[player] |= disksToFlip | (1L << pos);
            squares[opponent] ^= disksToFlip;
            squares[2] ^= 1L << pos;

            // Use Brain Kernighan's Algorithm to find the number of disks flipped
            while (disksToFlip > 0) {
                disksToFlip &= disksToFlip - 1;
                disksFlipped++;
            }

            // Set new score and update move number
            score[player] += disksFlipped + 1;
            score[opponent] -= disksFlipped;
            move++;
            /*
            int col = (pos % 8) + 1;
            String[] rows = new String[]{"a", "b", "c", "d", "e", "f", "g", "h"};
            String row = rows[pos / 8];
            System.out.print(row + col);
            */

            if (move == 60) {
                gameOver = true;
            } else {
                // Update the moveable squares and active player
                moveableSquares = getMoveableSquares(opponent);
                if (moveableSquares != 0) {
                    activePlayer = opponent;
                } else {
                    moveableSquares = getMoveableSquares(player);

                    if (moveableSquares == 0) { // Neither player can move
                        gameOver = true;
                    }
                }
            }

            return true;
        }
    }

    public String getBoardInConsoleString() {
        StringBuilder output = new StringBuilder(387);

        output.append(("""
                                Black   White     %1s
                        Score:  %-2d      %-2d        Move:  %-2d
                        
                            a   b   c   d   e   f   g   h""")
                .formatted(getActivePlayer() == 0 ? "B" : "W", score[0], score[1], move));

        for (int i = 0; i < 64; i++) {
            // Row text
            if (i % 8 == 0) {
                output.append("\n").append(i / 8 + 1).append(" |");
            }
            if ((squares[2] & (1L << i)) != 0) {
                output.append(" . |");
            } else if ((squares[0] & (1L << i)) != 0) {
                output.append(" B |");
            } else {
                output.append(" W |");
            }
        }
        output.append("\n");

        return output.toString();
    }

    public boolean makeRandomMove(int player) {
        long moveableSquaresTemp = getMoveableSquares(player);

        if (moveableSquaresTemp == 0) {
            return false;
        } else {
            ArrayList<Integer> moveableSquaresList = new ArrayList<>();

            for (int i = 0; i < 64; i++) {
                if ((moveableSquaresTemp & (1L << i)) != 0) {
                    moveableSquaresList.add(i);
                }
            }
            int pos = moveableSquaresList.get(random.nextInt(moveableSquaresList.size()));

            return move(player, pos);
        }
    }

    public boolean makeRandomMove_print(int player) {
        long moveableSquaresTemp = getMoveableSquares(player);

        if (moveableSquaresTemp == 0) {
            return false;
        } else {
            ArrayList<Integer> moveableSquaresList = new ArrayList<>();

            for (int i = 0; i < 64; i++) {
                if ((moveableSquaresTemp & (1L << i)) != 0) {
                    moveableSquaresList.add(i);
                }
            }
            int pos = moveableSquaresList.get(random.nextInt(moveableSquaresList.size()));

            return move_print(player, pos);
        }
    }

    public String getWinnerString() {
        if (score[0] > score[1]) {
            return "Black won.";
        } else if (score[1] > score[0]) {
            return "White won.";
        } else {
            return "Draw.";
        }
    }

    protected int getWinnerNumber() {
        if (score[0] > score[1]) {
            return 0;
        } else if (score[1] > score[0]) {
            return 1;
        } else {
            return -1;
        }
    }

    protected int getScore(int player) {
        return score[player];
    }

    public boolean isGameOver() { return gameOver; }

    public int getActivePlayer() { return activePlayer; }

    public int getBlackScore() { return score[0]; }

    public int getWhiteScore() { return score[1]; }

    protected long getDisksToFlip(int player, int pos) {
        int currPos;
        int nextPos;
        long tempDisksToFlip;
        long disksToFlip = 0L;

        if ((squares[2] & (1L << pos)) != 0) { // If the square is empty so a disk may be placed
            for (int i = 0; i < 8; i++) {
                tempDisksToFlip = 0L;
                currPos = pos;
                nextPos = currPos + compAddList[i];

                while ((validNextSquares[currPos] & (1L << i)) != 0) { // While next square is a valid square
                    if ((squares[2] & (1L << nextPos)) != 0) { // Square is empty
                        break;
                    } else if ((squares[player] & (1L << nextPos)) != 0) { // Current player is on square
                        disksToFlip |= tempDisksToFlip;
                        break;
                    } else { // Opponent is on square
                        tempDisksToFlip |= 1L << nextPos;
                    }

                    currPos = nextPos;
                    nextPos += compAddList[i];
                }
            }

            return disksToFlip;
        } else {
            return 0L;
        }
    }

    protected boolean canFlipDisks(int player, int pos) {
        int currPos;
        int nextPos;
        boolean opponentSeen; // Does the opponent have disks on the path being examined?

        if ((squares[2] & (1L << pos)) != 0) {
            for (int i = 0; i < 8; i++) {
                currPos = pos;
                nextPos = currPos + compAddList[i];
                opponentSeen = false;

                while ((validNextSquares[currPos] & (1L << i)) != 0) { // While next square is a valid square
                    if ((squares[2] & (1L << nextPos)) != 0) { // Square is empty
                        break;
                    } else if ((squares[player] & (1L << nextPos)) != 0) { // Current player is on square
                        if (opponentSeen) {
                            return true;
                        } else {
                            break;
                        }
                    } else { // Opponent is on square
                        opponentSeen = true;
                    }

                    currPos = nextPos;
                    nextPos += compAddList[i];
                }
            }
        }

        return false;
    }

    protected long getMoveableSquares(int player) {
        long moveableSquares = 0L;

        for (int i = 0; i < 64; i++) {
            if ((squares[2] & (1L << i)) != 0 && canFlipDisks(player, i)) {
                moveableSquares |= 1L << i;
            }
        }

        return moveableSquares;
    }
}
