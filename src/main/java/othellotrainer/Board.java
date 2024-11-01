package othellotrainer;

import java.util.Random;
import java.util.ArrayList;

/**
 *
 */
public class Board {
    // In bits: 0 = not present, 1 = present
    // [Black squares, White squares, empty squares]
    private long[] squares;
    // The squares that disks can be places on for the active player
    private long moveableSquares;

    private int activePlayer; // 0 = Black, 1 = White
    private int[] score; // {Black score, White score}
    private int move; // Current move number
    private boolean gameOver = false;

    private int pos; // Used for calculations
    private final Random random;
    // In bits: 0 = not valid, 1 = valid
    // For a given square, the "options" next to the square that are valid
    // Order: top left, top, top right, left, right, bottom left, bottom, bottom right
    private final byte[] validNextSquares = new byte[]{
            (byte) 0b00001011, (byte) 0b00011111, (byte) 0b00011111, (byte) 0b00011111,
            (byte) 0b00011111, (byte) 0b00011111, (byte) 0b00011111, (byte) 0b00010110,

            (byte) 0b01101011, (byte) 0b11111111, (byte) 0b11111111, (byte) 0b11111111,
            (byte) 0b11111111, (byte) 0b11111111, (byte) 0b11111111, (byte) 0b11010110,

            (byte) 0b01101011, (byte) 0b11111111, (byte) 0b11111111, (byte) 0b11111111,
            (byte) 0b11111111, (byte) 0b11111111, (byte) 0b11111111, (byte) 0b11010110,

            (byte) 0b01101011, (byte) 0b11111111, (byte) 0b11111111, (byte) 0b11111111,
            (byte) 0b11111111, (byte) 0b11111111, (byte) 0b11111111, (byte) 0b11010110,

            (byte) 0b01101011, (byte) 0b11111111, (byte) 0b11111111, (byte) 0b11111111,
            (byte) 0b11111111, (byte) 0b11111111, (byte) 0b11111111, (byte) 0b11010110,

            (byte) 0b01101011, (byte) 0b11111111, (byte) 0b11111111, (byte) 0b11111111,
            (byte) 0b11111111, (byte) 0b11111111, (byte) 0b11111111, (byte) 0b11010110,

            (byte) 0b01101011, (byte) 0b11111111, (byte) 0b11111111, (byte) 0b11111111,
            (byte) 0b11111111, (byte) 0b11111111, (byte) 0b11111111, (byte) 0b11010110,

            (byte) 0b01101000, (byte) 0b11111000, (byte) 0b11111000, (byte) 0b11111000,
            (byte) 0b11111000, (byte) 0b11111000, (byte) 0b11111000, (byte) 0b11010000
    };
    private final int[] compAddList = {-9, -8, -7, -1, 1, 7, 8, 9};

    private final String boardString = """
            0b
            00000000
            00000000
            00010000
            00100000
            00000100
            00001000
            00000000
            00000000L
    """;

    private final String boardOppString = """
            0b
            11111111
            11111111
            11111111
            11100111
            11100111
            11111111
            11111111
            11111111L
    """;

    public Board() {
        // Standard board setup
        squares = new long[]{
                0b0000000000000000000000000000100000010000000000000000000000000000L,
                0b0000000000000000000000000001000000001000000000000000000000000000L,
                0b1111111111111111111111111110011111100111111111111111111111111111L
        };
        moveableSquares = 0b0000000000000000000100000010000000000100000010000000000000000000L;

        activePlayer = 0;
        score = new int[]{2, 2};
        move = 0;

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
            squares[player] += disksToFlip + (1L << pos);
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

    public String toConsoleString() {
        StringBuilder output = new StringBuilder(387);

        output.append(("        Black   White     %1s\nScore:  %-2d      %-2d        Move:  %-2d\n\n    a   b   c   d   e   f")
                        .formatted(getActivePlayer() == 0 ? "B" : "W", score[0], score[1], move))
                .append("   g   h");

        for (int i = 0; i < 64; i++) {
            // Row text
            if (i % 8 == 0) {
                output.append("\n").append(i / 8 + 1).append(" |");
            }
            if ((squares[2] & (1L << i)) != 0) {
                output.append(" |");
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
            ArrayList<Integer> moveableSquaresList = new ArrayList<Integer>();

            for (int i = 0; i < 64; i++) {
                if ((moveableSquaresTemp & (1L << i)) != 0) {
                    moveableSquaresList.add(i);
                }
            }

            return move(
                    player,
                    moveableSquaresList.get(
                            random.nextInt(moveableSquaresList.size())
                    )
            );
        }
    }

    public boolean isGameOver() { return gameOver; }

    public int getActivePlayer() { return activePlayer; }

    public int getBlackScore() { return score[0]; }

    public int getWhiteScore() { return score[1]; }

    public String getWinner() {
        if (score[0] > score[1]) {
            return "Black";
        } else if (score[1] > score[0]) {
            return "White";
        } else {
            return "Tie";
        }
    }

    private long getDisksToFlip(int player, int pos) {
        int compPos;
        long tempDisksToFlip;
        long disksToFlip = 0L;

        if ((squares[2] & (1L << pos)) == 0) {
            for (int i = 7; i > -1; i--) {
                tempDisksToFlip = 0L;
                compPos = pos + compAddList[i];

                while ((validNextSquares[compPos] & (1L << i)) != 0) {
                    if ((squares[2] & (1L << compPos)) != 0) { // Square is empty
                        break;
                    } else if ((squares[player] & (1L << i)) != 0) { // Current player is on square
                        disksToFlip += tempDisksToFlip;
                        break;
                    } else { // Opponent is on square
                        tempDisksToFlip |= 1L << i;
                    }

                    compPos += compAddList[i];
                }
            }

            return disksToFlip;
        } else {
            return 0L;
        }
    }

    private boolean canFlipDisks(int player, int pos) {
        int compPos;
        boolean opponentSeen; // Does the opponent have disks on the path being examined?

        if ((squares[2] & (1L << pos)) == 0) {
            for (int i = 7; i > -1; i--) {
                compPos = pos + compAddList[i];
                opponentSeen = false;

                while ((validNextSquares[compPos] & (1L << i)) != 0) {
                    if ((squares[2] & (1L << compPos)) != 0) { // Square is empty
                        break;
                    } else if ((squares[player] & (1L << i)) != 0) { // Current player is on square
                        if (opponentSeen) {
                            return true;
                        } else {
                            break;
                        }
                    } else { // Opponent is on square
                        opponentSeen = true;
                    }

                    compPos += compAddList[i];
                }
            }
        }

        return false;
    }

    private long getMoveableSquares(int player) {
        long moveableSquares = 0L;

        for (int i = 0; i < 64; i++) {
            if ((squares[2] & (1L << i)) != 0 && canFlipDisks(player, i)) {
                moveableSquares |= 1L << i;
            }
        }

        return moveableSquares;
    }
}