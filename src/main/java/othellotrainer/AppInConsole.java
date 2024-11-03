package othellotrainer;

/**
 *
 *
 */
public class AppInConsole {
    protected static int RUNS = 1;
    public static void main(String[] args) throws CloneNotSupportedException {
        for (int i = 0; i < RUNS; i++) {
            ConsoleGameS1_c c = new ConsoleGameS1_c(new BoardS1_1(), 59);
            c.run();
        }
        /*
        if (args.length == 0) {
            ConsoleGame consoleGame = new ConsoleGame(new Board());
            consoleGame.run();
        } else if (args[0].equals("S1") && args.length == 3) {
            ConsoleGameS1 consoleGameS1;
            for (int i = 0; i < RUNS; i++) {
                consoleGameS1 = new ConsoleGameS1(new BoardS1(), Integer.parseInt(args[1]));
                consoleGameS1.run();
            }
        } else if (args.length == 1) {
            ConsoleGameAuto consoleGameAuto = new ConsoleGameAuto(new Board(), Integer.parseInt(args[0]));
            consoleGameAuto.run();
        }
         */
    }
}
