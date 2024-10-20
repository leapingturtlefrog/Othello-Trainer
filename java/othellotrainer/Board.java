package othellotrainer;

import java.util.Random;
import java.util.ArrayList;


@FunctionalInterface
interface searchFunction {
    boolean isValid(int tempPos, int pos);
}

public class Board {
    // Black pieces packed as a bitboard
    // A bit of 0 represents not present, 1 present, going along columns and then rows
    private long black;
    // White pieces packed as a bitboard
    // A bit of 0 represents not present, 1 present, going along columns and then rows
    private long white;
    // Move order as an array of bytes, going by column and then row, arranged
    // The positive bytes 1-60 inclusive represent the move number of the black player
    // The positive bytes (61-121, inclusive) minus 60 represent the move number of the white player
    // -128 represents the black player starting position
    // -127 represents the white player starting position
    // 0 represents an empty square
    private byte[] moves;
    // Number of moves completed
    private byte move;
    // The score with black first and then white
    private byte[] score;
    // The current active player
    // 0 is black, 1 is white
    private int activePlayer;

    // Temporary position holder
    private int pos;
    // Used for random moves
    private final Random random;

    // Used for determining valid moves by adding these values each time to view another square
    private final int[] searchAddList = new int[]{-9, -8, -7, -1, 1, 7, 8, 9};
    // Used to ensure position being compared is on the board and is valid
    private final searchFunction[] searchFunctionList = new searchFunction[]{
            (int tempPos, int pos) -> tempPos / 8 + 1 == pos / 8 && tempPos > -1, // top left
            (int tempPos, int pos) -> tempPos > -1, // up
            (int tempPos, int pos) -> tempPos / 8 != pos / 8 && tempPos > -1, // top right
            (int tempPos, int pos) -> tempPos / 8 == pos / 8, // left
            (int tempPos, int pos) -> tempPos / 8 == pos / 8, // right
            (int tempPos, int pos) -> tempPos / 8 != pos / 8 && tempPos < 64, // bottom left
            (int tempPos, int pos) -> tempPos < 64, // down
            (int tempPos, int pos) -> tempPos / 8 == pos / 8 + 1 && tempPos < 64, // bottom right
    };

    // Nonstandard preset boards for testing. 0 is empty, 1 black, 2 white
    private final int[][][] EASY_BOARDS = new int[][][]{
            {
                    {0, 0, 0, 0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0, 0, 0, 0}
            },
            {
                    {2, 2, 2, 2, 2, 0, 0, 0},
                    {2, 1, 1, 1, 2, 0, 0, 0},
                    {2, 1, 0, 1, 2, 0, 0, 0},
                    {2, 1, 1, 1, 2, 0, 0, 0},
                    {2, 2, 2, 2, 2, 0, 0, 0},
                    {0, 0, 0, 0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0, 0, 0, 0}
            },
            {
                    {0, 0, 0, 0, 0, 0, 0, 0},
                    {0, 1, 1, 1, 0, 0, 0, 0},
                    {0, 1, 0, 1, 0, 0, 0, 0},
                    {0, 1, 1, 1, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0, 0, 0, 2}
            }
    };

    Board(int boardNumber) {
        if (boardNumber == -1) {
            black = (long) (Math.pow(2, 28) + Math.pow(2, 35));
            white = (long) (Math.pow(2, 27) + Math.pow(2, 36));
        } else {
            long[] tempLong = createBoard(EASY_BOARDS[boardNumber]);
            black = tempLong[0];
            white = tempLong[1];
        }
        moves = new byte[]{
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, -127, -128, 0, 0, 0,
                0, 0, 0, -128, -127, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
        };
        score = new byte[]{2, 2};
        move = (byte) (score[0] + score[1] - 4);
        activePlayer = 0;
        random = new Random();
    }

    long[] createBoard(int[][] iboard) {
        long[] outLong = new long[]{0L, 0L};
        int curr;
        long toAdd;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                curr = iboard[i][j];
                toAdd = (long) Math.pow(2, 8 * i + j);
                if (i * j != 49) {
                    if (curr == 1) {
                        outLong[0] += toAdd;
                    } else if (curr == 2) {
                        outLong[1] += toAdd;
                    }
                } else {
                    if (curr == 1) {
                        outLong[0] += -toAdd;
                    } else if (curr == 2) {
                        outLong[1] += -toAdd;
                    }
                }
            }
        }
        return outLong;
    }

    int getPiece(int col, int row) {
        pos = 8 * row + col;
        if ((black >>> pos) % 2 == 1) {
            return 0; // black
        } else if ((white >>> pos) % 2 == 1) {
            return 1; // white
        }
        return -1; // empty square
    }

    String toPrint() {
        StringBuilder output = new StringBuilder(387);

        output.append(("        Black   White     %1s\nScore:  %-2d      %-2d        Move:  %-2d\n\n    a   b   c   d   e   f")
                .formatted(getActivePlayer() == 0 ? "B" : "W", score[0], score[1], move))
                .append("   g   h");

        for (int i = 0; i < 64; i++) {
            // row text
            if (i % 8 == 0) {
                output.append("\n").append(i / 8 + 1).append(" |");
            }
            if ((black >>> i) % 2 == 1) {
                output.append(" B |");
            } else if ((white >>> i) % 2 == 1) {
                output.append(" W |");
            } else {
                output.append(" . |");
            }
        }

        output.append("\n");

        return output.toString();
    } // print

    boolean move(int player, int col, int row) {
        long piecesToFlip = getPiecesToFlip(player, col, row);
        long temp;
        if (piecesToFlip != 0) {
            if (player == 0) {
                if (col * row != 49) {
                    black += (long) Math.pow(2, 8 * row + col);
                } else {
                    white += (long) -Math.pow(2, 63);
                }
                score[0] += 1;
            } else if (player == 1) {
                if (col * row != 49) {
                    white += (long) Math.pow(2, 8 * row + col);
                } else {
                    white += (long) -Math.pow(2, 63);
                }
                score[1] += 1;
            }
            for (int i = 0; i < 64; i++) {
                temp = piecesToFlip >>> i;
                if (temp % 2 == 1) {
                    if ((black >>> i) % 2 == 1 || (white >>> i) % 2 == 1) {
                        if (player == 0) {
                            white -= (long) Math.pow(2, i);
                            black += (long) Math.pow(2, i);
                            score[0] += 1;
                            score[1] -= 1;
                        } else if (player == 1) {
                            white += (long) Math.pow(2, i);
                            black -= (long) Math.pow(2, i);
                            score[0] -= 1;
                            score[1] += 1;
                        }
                    }
                }
            }
            move += 1;
            moves[8 * row + col] = player == 0 ? move : (byte) (move + 60);
            if (moveableSquares(player == 0 ? 1 : 0) != 0L) {
                activePlayer = player == 0 ? 1 : 0;
            }
            return true;
        } else {
            return false;
        }
    }

    long getPiecesToFlip(int player, int col, int row) {
        long opponent = player == 0 ? white : black;
        long currPlayer = player == 0 ? black : white;
        pos = 8 * row + col;
        if ((black >>> pos) % 2 != 1 && (white >>> pos) % 2 != 1) {
            long disksToFlip = 0L;
            long tempDisksToFlip;
            int tempPos;
            int prevPos;

            for (int i = 0; i < 8; i++) {
                tempDisksToFlip = 0L;
                tempPos = pos + searchAddList[i];
                prevPos = pos;

                while (searchFunctionList[i].isValid(tempPos, prevPos)) {
                    if ((opponent >>> tempPos) % 2 == 1) {
                        tempDisksToFlip += (long) Math.pow(2, tempPos);
                    } else if ((currPlayer >>> tempPos) % 2 == 1) {
                        disksToFlip += tempDisksToFlip;
                        break;
                    } else {
                        break;
                    }

                    prevPos = tempPos;
                    tempPos += searchAddList[i];
                }
            }

            return disksToFlip;
        } else {
            return 0L;
        }
    }

    long moveableSquares(int player) {
        long availableSquares = white + black; //all 0's in the binary representation are empty
        long moveableSquares = 0L;
        for (int i = 0; i < 64; i++) {
            if ((availableSquares >>> i) % 2 == 0 && getPiecesToFlip(player, i % 8, i / 8) != 0L) {
                if (i != 63) {
                    moveableSquares += (long) Math.pow(2, i);
                } else {
                    moveableSquares += (long) -Math.pow(2, 63);
                }
            }
        }
        return moveableSquares;
    }

    int getActivePlayer() {
        return activePlayer;
    }

    byte getMove() {
        return move;
    }

    String getWinner() {
        if (score[0] > score[1]) {
            return "Black";
        } else if (score[1] > score[0]) {
            return "White";
        } else {
            return "Tie";
        }
    }

    byte[] getScore() {
        return score;
    }

    String randomMove(int player) {
        long moveableSquares = moveableSquares(player);
        if (moveableSquares != 0L) {
            ArrayList<Integer> moveableSquaresList = new ArrayList<Integer>();
            for (int i = 0; i < 64; i++) {
                if ((moveableSquares >>> i) % 2 == 1) {
                    moveableSquaresList.add(i);
                }
            }
            int len = moveableSquaresList.size();
            if (len > 0) {
                pos = moveableSquaresList.get(random.nextInt(len));
                return "Moving at c" + pos % 8 + ", r" + pos / 8 + " returned "
                        + move(player, pos % 8, pos / 8) + ".";
            } else {
                return "moveableSquaresList is empty.";
            }
        } else {
            return "MoveableSquares returned 0L.";
        }
    }
} // Board
