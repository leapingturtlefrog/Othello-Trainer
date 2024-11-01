package othellotrainer;

/**
 *
 *
 */
public class App {
    private static boolean gameInConsole = true;

    public static void main( String[] args ) {
        if (gameInConsole) {
            ConsoleGame consoleGame = new ConsoleGame(new Board());
            consoleGame.run();
        }
    }
}
