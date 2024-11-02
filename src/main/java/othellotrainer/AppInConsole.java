package othellotrainer;

/**
 *
 *
 */
public class AppInConsole {
    public static void main( String[] args ) {
        if (args.length == 0) {
            ConsoleGame consoleGame = new ConsoleGame(new Board());
            consoleGame.run();
        } else if (args.length == 1) {
            ConsoleGameAuto consoleGameAuto = new ConsoleGameAuto(new Board(), Integer.parseInt(args[0]));
            consoleGameAuto.run();
        }
    }
}
