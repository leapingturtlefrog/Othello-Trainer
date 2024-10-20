package othellotrainer;


public class OthelloTrainer {
    private static boolean consoleGame = true;

    public static void main(String[] args) {
        if (consoleGame) {
            if (args.length == 0) {
                ConsoleGame consoleGame = new ConsoleGame(-1); // default starting board
                consoleGame.run();
            } else if (args.length == 1) {
                ConsoleGame consoleGame = new ConsoleGame(Integer.parseInt(args[0])); // non-default starting board
                consoleGame.run();
            }
        }
    }
}
