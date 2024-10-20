package othellotrainer;

import java.util.Scanner;


public class ConsoleGame {
    Board board;

    ConsoleGame(int boardNumber) {
        board = new Board(boardNumber);

        printIntro();
        System.out.println(board.toPrint());

        Scanner input = new Scanner(System.in);
        int colInt;
        int rowInt;

        System.out.print("Input square: ");
        String inputString = input.nextLine().strip().toLowerCase();
        while (!inputString.equals("exit")) {
            if (inputString.length() != 2) {
                printInvalidInput();
            } else {
                try {
                    colInt = (int) inputString.charAt(0) - 97;
                    rowInt = Integer.parseInt(inputString.substring(1, 2)) - 1;

                    if (colInt > -1 && colInt < 8 && rowInt > -1 && rowInt < 8) {
                        ;
                    } else {
                        printInvalidInput();
                    }
                } catch (Exception e) {
                    printInvalidInput();
                }
            }

            System.out.print("Input square: ");
            inputString = input.nextLine().strip().toLowerCase();
        }
    }

    void printIntro() {
        System.out.println("""
Game started in console! Human vs human currently supported.
     
      Layout:
      B: Black disk
      W: White disk
      .: Blank Square
     
      Controls:
      When prompted, input a valid square through a lowercase letter and then a number with no spaces (ie. 'a4')
     
      Input 'exit' to close or press the 'x' in the top right of the console
     
      Have fun!
""");
    }

    void printInvalidInput() {
        System.out.println("Invalid input. Must be a letter a through h followed by a number 1 through 8, "
                + "inclusive. For example: 'h8'.");
    }
}
