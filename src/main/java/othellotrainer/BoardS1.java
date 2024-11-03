package othellotrainer;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Random;

/**
 *
 */
public class BoardS1 extends Board implements Cloneable {
    protected final int LOOK_AHEADS = 1;
    protected final int ITERATIONS_PER_LOOK_AHEAD = 100;

    public boolean moveS1(int player, BoardS1 board) throws CloneNotSupportedException {
        long moveableSquaresTemp = getMoveableSquares(player);
        int opponent = player == 0 ? 1 : 0;
        BoardS1 b;

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
                    b = (BoardS1) board.clone();
                    for (int iters = 0; iters < ITERATIONS_PER_LOOK_AHEAD; iters++) {
                        for (int j = 0; j < 61; j++) {
                            if (!b.makeRandomMove(getActivePlayer())) {
                                totalPointsDifferenceList.set(idx,
                                        totalPointsDifferenceList.get(idx) + b.score[player] - b.score[opponent]);
                                break;
                            }
                        }
                    }
                    if (totalPointsDifferenceList.get(idx) > totalPointsDifferenceList.get(highestDifferenceIndex)) {
                        highestDifferenceIndex = idx;
                    }
                    idx++;
                }
            }

            return move(player, moveableSquaresList.get(highestDifferenceIndex));
        }
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
