package tetrisGame;

import javafx.scene.input.KeyCode;
import javafx.util.Pair;

class Solver extends Tetris{
    private static double A = -0.510066;
    private static double B = 0.760666;
    private static double C = -0.35663;
    private static double D = -0.184483;

    private Tetromino tetromino;
    private double bestResult;
    private int bestRotation;
    private Pair<Integer, Integer> bestLocation;

    void makeMove() {
        tetromino = new Tetromino();
        bestResult = -Integer.MAX_VALUE;
        bestRotation = 0;
        bestLocation = new Pair<>(0, 0);

        findBestLocation();

        for (int k = 0; k < bestRotation; k++) {
            tetromino.rotate();
        }
        tetromino.setLocation(bestLocation.getValue(), bestLocation.getKey());
        tetromino.leaveOnTheGround();
        clearRows();
    }

    private void findBestLocation() {
        double result;
        int maxHeight = maxHeightOfMine();
        Tetromino current = tetromino;

        for (int rotation = 0; rotation < 4; rotation++) {
            current.paint();
            int cases = WIDTH + 1 - current.getWidth();
            int counter = 0;
            for (int i = HEIGHT - 2 - maxHeight; i < HEIGHT; i++) {
                for (int j = 0; j < WIDTH; j++) {
                    current.setLocation(j, i);
                    if (!current.isWrongPosition() && current.isTouchGround()) {
                        current.drop();
                        int sumHeight = sumHeight();
                        int fullLines = fullLines();
                        int holes = holes();
                        int drops = drops();
                        result = A * sumHeight + B * fullLines + C * holes + D * drops;

                        if (result > bestResult) {
                            bestResult = result;
                            bestLocation = new Pair<>(i, j);
                            bestRotation = rotation;
                        }

                        current.clearFromMine();
                        counter++;
                    }
                }
                if (counter == cases) break;
            }
            current.erase();
            current.setLocation(3, 0);
            current.rotate();
        }
    }

    private int sumHeight() {
        int aggregateHeight = 0;
        for (int j = 0; j < WIDTH; j++) {
            aggregateHeight += maxHeightOfColumn(j);
        }
        return aggregateHeight;
    }

    private int fullLines() {
        int completeLines = 0;
        int filled = 1;
        int currentRow = HEIGHT - 1;
        while (currentRow > 0) {
            for (int column = 0; column < WIDTH; column++) {
                filled = mine[currentRow][column] * filled;
                if (filled < 0) break;
            }
            if (filled > 0) {
                completeLines++;
            } else
                currentRow--;
            filled = 1;
        }
        return completeLines;
    }

    private int holes() {
        int holes = 0;
        boolean isGroundBegins = false;
        for (int j = 0; j < WIDTH; j++) {
            isGroundBegins = false;
            for (int i = 1; i < HEIGHT; i++) {
                if (mine[i][j] > 0)
                    isGroundBegins = true;
                if (mine[i][j] == 0 && isGroundBegins)
                    holes++;
            }
        }
        return holes;
    }

    private int drops() {
        int drops = 0;

        for (int j = 0; j < WIDTH - 1; j++) {
            drops += Math.abs(maxHeightOfColumn(j) - maxHeightOfColumn(j + 1));
        }

        return drops;
    }

    private int maxHeightOfColumn(int column) {
        int maxHeight = HEIGHT;
        for (int row = 0; row < HEIGHT; row++) {
            if (mine[row][column] == 0)
                maxHeight--;
            else break;
        }
        return maxHeight;
    }

    private int maxHeightOfMine() {
        int maxHeight = 0;
        for (int column = 0; column < WIDTH; column++) {
            for (int row = 0; row < HEIGHT; row++) {
                if (row == maxHeightOfColumn(column))
                    if (row > maxHeight)
                        maxHeight = row;
            }
        }
        return maxHeight;
    }

    void clearRows() {
        int filled = 1;
        int currentRow = HEIGHT - 1;
        while (currentRow > 0) {
            for (int column = 0; column < WIDTH; column++) {
                filled = mine[currentRow][column] * filled;
                if (filled < 0) break;
            }
            if (filled > 0) {
                for (int i = currentRow; i > 0; i--) {
                    System.arraycopy(mine[i - 1], 0, mine[i], 0, WIDTH);
                    repaintMine(i);
                }
            } else
                currentRow--;
            filled = 1;
        }
    }
}

