package src.othellotrainer;

import java.util.Scanner;


public class ConsoleGame {
    Board board;

    ConsoleGame(int boardNumber) {
        board = new Board(boardNumber);
    }

    void run() {
        boolean continuePlaying = true;
        while (continuePlaying) {
            System.out.println(getIntroText() + board.toPrint());

            Scanner input = new Scanner(System.in);
            int colInt;
            int rowInt;

            System.out.print("Input square: ");
            String inputString = input.nextLine().strip().toLowerCase();
            while (true) {
                if (inputString.startsWith("exit")) {
                    continuePlaying = false;
                    break;
                } else if (inputString.startsWith("r")) {
                    if (inputString.length() == 1) {
                        board.randomMove(board.getActivePlayer());
                        System.out.println(board.toPrint());
                    } else {
                        try {
                            int repeats = Integer.parseInt(inputString.substring(1));
                            if (repeats > 0 && repeats < 61) {
                                for (int i = 0; i < repeats; i++) {
                                    if (!board.randomMove(board.getActivePlayer())) {
                                        break;
                                    }
                                }
                                System.out.println(board.toPrint());
                            } else {
                                throw new Exception();
                            }
                        } catch (Exception e) {
                            System.out.println("If you are doing a random move. The format to enter is either 'r' for one "
                                    + "random move or 'r' followed immediately by a number of how many times to "
                                    + "repeat. To complete an entire game, input 'r60', for example (as there are "
                                    + "at most 60 moves in a game.");
                        }
                    }
                } else if (inputString.length() != 2) {
                    printInvalidInput();
                } else {
                    try {
                        colInt = (int) inputString.charAt(0) - 97;
                        rowInt = Integer.parseInt(inputString.substring(1, 2)) - 1;
                        if (colInt > -1 && colInt < 8 && rowInt > -1 && rowInt < 8) {
                            if (!board.move(board.getActivePlayer(), colInt, rowInt)) {
                                System.out.println("Square " + inputString + " is not a valid move.");
                            } else {
                                System.out.println(board.toPrint());
                            }
                        } else {
                            printInvalidInput();
                        }
                    } catch (Exception e) {
                        printInvalidInput();
                    }
                }

                if (board.moveableSquares(0) == 0L && board.moveableSquares(1) == 0L) {
                    System.out.println("Game ended! Final score: " + board.getScore()[0] + " to " + board.getScore()[1]);
                    if (board.getWinner().equals("Black")) {
                        System.out.println("Black won.");
                    } else if (board.getWinner().equals("White")) {
                        System.out.println("White won.");
                    } else {
                        System.out.println("Draw.");
                    }
                    System.out.println("\nThank you for playing! Enter any key to continue. Enter 'exit' to exit.");
                    if (!input.nextLine().strip().equalsIgnoreCase("exit")) {
                        board = new Board(-1);
                    } else {
                        continuePlaying = false;
                    }
                    break;
                }
                System.out.print("Input square: ");
                //System.out.print("Input square: ");
                inputString = input.nextLine().strip().toLowerCase();
            }
        }
        System.out.println("Exited.");
    }

    String getIntroText() {
        return """
Game started in console! Human vs human currently supported.
     
      Layout:
      B: Black disk
      W: White disk
      .: Blank Square
     
      Controls:
      When prompted, input a valid square through a lowercase letter and then a number with no spaces (ie. 'a4').
      Or enter 'r' for a random selection.
     
      Input 'exit' or press the 'x' in the top right of the console to close.
     
      Have fun!
""";
    }

    void printInvalidInput() {
        System.out.println("Invalid input. Must be a letter a through h followed by a number 1 through 8, "
                + "inclusive. For example: 'h8'.");
    }
}
