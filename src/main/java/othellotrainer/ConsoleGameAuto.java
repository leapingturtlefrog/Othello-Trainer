package othellotrainer;

import java.io.FileWriter;
import java.io.IOException;

public class ConsoleGameAuto {
    private Board mainBoard;
    private final int repetitions;
    private long startTime;
    private long endTime;

    private static final int RUNS_PER_UPDATE = 1000; // How often stats should be displayed, every _ games
    private static final String DATA_FILE_PATH = "./data/performanceTimes.csv";
    private static final String VERSION = "0.1.11-11.1.24-10:49";

    ConsoleGameAuto(Board board, int totalRuns) {
        mainBoard = board;
        repetitions = totalRuns;
    }

    void run() {
        System.out.println("Press Ctrl+C to exit early. Showing stats every " + RUNS_PER_UPDATE + " games.");
        startTime = System.currentTimeMillis();

        for (int runsCompleted = 0; runsCompleted < repetitions; runsCompleted++) {
            if (runsCompleted % RUNS_PER_UPDATE == 0) {
                System.out.println("Runs completed: " + runsCompleted);
            }
            for (int i = 0; i < 60; i++) {
                if (!mainBoard.makeRandomMove(mainBoard.getActivePlayer())) {
                    break;
                }
            } mainBoard = new Board();
        }

        endTime = System.currentTimeMillis();
        double timeDifference = endTime - startTime;
        System.out.println("---\nFinished. " + repetitions + " total runs completed." + "\n---\nTotal time (ms): "
                + timeDifference + "\nTime per " + RUNS_PER_UPDATE + " runs (ms): "
                + timeDifference * RUNS_PER_UPDATE / repetitions + "\n");

        try (FileWriter fileWriter = new FileWriter(DATA_FILE_PATH, true)) {
            fileWriter.write(VERSION + "," + repetitions + "," + timeDifference + "," + RUNS_PER_UPDATE + "\n");
            System.out.println("Successfully appended to data file.");
        } catch (IOException e) {
            System.out.println("Could not append to data file: " + e.getMessage());
        }
    }
}
