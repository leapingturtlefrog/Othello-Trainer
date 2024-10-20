package othellotrainer;

public class OthelloTrainer {
    boolean consoleGame = true;

    public static void main(String[] args) {
        if (args.length == 0) {
            ConsoleGame consoleGame = new ConsoleGame(0);
        } else if (args.length == 1) {
            ConsoleGame consoleGame = new ConsoleGame(Integer.parseInt(args[0]));
        }
    }
}
