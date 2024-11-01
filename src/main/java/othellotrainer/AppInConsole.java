package othellotrainer;

/**
 *
 *
 */
public class AppInConsole {
    public static void main( String[] args ) {
        ConsoleGame consoleGame = new ConsoleGame(new Board());
        consoleGame.run();
    }
}
