package othellotrainer;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.Random;

/**
 *
 */
public class BoardS1 extends Board {
    protected final int LOOK_AHEADS = 1;
    protected final int ITERATIONS_PER_LOOK_AHEAD = 10;
    protected long[] savedSquares;
    protected int[] savedScore;

    public boolean moveS1(int player) {
        save();
        long moveableSquaresTemp = getMoveableSquares(player);
        int opponent = player == 0 ? 1 : 0;

        if (moveableSquaresTemp == 0) {
            return false;
        } else {
            ArrayList<Integer> moveableSquaresList = new ArrayList<>();
            ArrayList<Integer> totalPointsDifferenceList = new ArrayList<>();
            int highestDifferenceIndex = 0;
            int idx = 0;

            for (int i = 0; i < 64; i++) {
                if ((moveableSquaresTemp & (1L << i)) != 0) {
                    moveableSquaresList.add(i);
                    totalPointsDifferenceList.add(0);
                    for (int iters = 0; iters < ITERATIONS_PER_LOOK_AHEAD; iters++) {
                        save();
                        for (int j = 0; j < 61; j++) {
                            if (!makeRandomMove(getActivePlayer())) {
                                totalPointsDifferenceList.set(idx,
                                        totalPointsDifferenceList.get(idx) + score[player] - score[opponent]);
                                break;
                            }
                        }
                        load();
                    }
                    if (totalPointsDifferenceList.get(idx) > totalPointsDifferenceList.get(highestDifferenceIndex)) {
                        highestDifferenceIndex = idx;
                    }
                    idx++;
                }
            }
            load();
            return move(player, moveableSquaresList.get(highestDifferenceIndex));
        }
    }

    public void save() {
        savedSquares = Arrays.copyOf(squares, squares.length);
        savedScore = Arrays.copyOf(score, score.length);
    }

    public void load() {
        squares = Arrays.copyOf(savedSquares, savedSquares.length);
        score = Arrays.copyOf(savedScore, savedScore.length);
    }
}
