package othellotrainer;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class ConsoleGameS1 extends ConsoleGameAuto {
    protected BoardS1 mainBoard;
    protected static final String DATA_FILE_PATH = "../../../../data/performance_S_v1.csv";
    protected int wins;
    protected long totalScore;
    protected ArrayList<Integer> winsList;
    protected ArrayList<Long> scoreList;

    ConsoleGameS1(BoardS1 board, int totalRuns) {
        super();
        mainBoard = board;
        repetitions = totalRuns;
        batchRepetitions = repetitions / RUNS_PER_UPDATE;
        if (batchRepetitions > 59) {
            throw new Error("Total runs / RUNS_PER_UPDATE must be less than 60.");
        }
        wins = 0;
        totalScore = 0;
        winsList = new ArrayList<>(batchRepetitions);
        scoreList = new ArrayList<>(batchRepetitions);
    }

    @Override
    void run() {
        System.out.println("Press Ctrl+C to exit early. Showing updates every " + RUNS_PER_UPDATE + " games.");
        int playerMovedFor;
        int opponent;
        int scoreDifference;
        int tempWins;
        long tempScore;
        startTime = System.currentTimeMillis();

        for (runsCompleted = 0; runsCompleted < batchRepetitions; runsCompleted++) {
            tempWins = 0;
            tempScore = 0;
            scoreDifference = 0;
            for (batchedRuns = 0; batchedRuns < RUNS_PER_UPDATE; batchedRuns++) {
                for (int i = 0; i < runsCompleted; i++) {
                    if (!mainBoard.makeRandomMove(mainBoard.getActivePlayer())) {
                        break;
                    }
                }
                playerMovedFor = mainBoard.getActivePlayer();
                opponent = playerMovedFor == 0 ? 1 : 0;
                mainBoard.moveS1(playerMovedFor);
                for (int j = 0; j < 60 - runsCompleted; j++) {
                    if (!mainBoard.makeRandomMove(mainBoard.getActivePlayer())) {
                        break;
                    }
                }
                scoreDifference = mainBoard.getScore(playerMovedFor) - mainBoard.getScore(opponent);
                tempWins += scoreDifference > 0 ? 1 : 0;
                tempScore += scoreDifference;

                mainBoard = new BoardS1();
            }
            winsList.add(tempWins);
            scoreList.add(tempScore);
            wins += tempWins;
            totalScore += tempScore;
            System.out.println("Runs completed: " + (runsCompleted + 1) * RUNS_PER_UPDATE + "   Wins: " + tempWins
                    + "   Score Difference: " + tempScore);
        }

        endTime = System.currentTimeMillis();
        double timeDifference = endTime - startTime;
        System.out.println("---\nFinished. " + repetitions + " total runs completed." + "\n---\nTotal time (ms): "
                + timeDifference + "\nTime per " + RUNS_PER_UPDATE + " runs (ms): "
                + timeDifference * RUNS_PER_UPDATE / repetitions + "\n");

        try (FileWriter fileWriter = new FileWriter(DATA_FILE_PATH, true)) {
            String data = VERSION + ","
                    + wins + "."
                    + totalScore + ","
                    + timeDifference * 1000 / repetitions + ","
                    + repetitions + ","
                    + timeDifference + ","
                    + RUNS_PER_UPDATE;
            for (int win : winsList) {
                data += "," + win;
            }
            for (int i = 0; i < 60 - batchRepetitions; i++) {
                data += ",";
            }
            for (long score : scoreList) {
                data += "," + score;
            }
            for (int i = 0; i < 60 - batchRepetitions; i++) {
                data += ",";
            }
            data += "\n";
            fileWriter.write(data);
            System.out.println("Successfully appended to data file.");
        } catch (IOException e) {
            System.out.println("Could not append to data file: " + e.getMessage());
        }
    }
}
