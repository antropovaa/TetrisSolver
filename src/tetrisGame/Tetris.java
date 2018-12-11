package tetrisGame;

import javafx.animation.AnimationTimer;
import javafx.scene.Parent;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

import java.security.Key;
import java.util.Arrays;

import static tetrisGame.Tetromino.colors;

/**
 * Contains all the logic of the TetrisGame
 */
class Tetris {
    static final int BLOCK_SIZE = 25;
    static final int WIDTH = 10; // in blocks
    static final int HEIGHT = 18;
    static int[][] mine = new int[HEIGHT + 1][WIDTH];

    private Tetromino currentTetromino;
    static double time;
    static GraphicsContext gc;
    private boolean gameOver = false;
    int gameScore;
    private final int[] SCORES = {100, 300, 700, 1500};

    Parent createContent() {
        Pane root = new Pane();
        root.setPrefSize(BLOCK_SIZE * WIDTH + 100, BLOCK_SIZE * HEIGHT);
        root.setStyle("-fx-background-color: black;");

        Canvas canvas = new Canvas(BLOCK_SIZE * WIDTH, BLOCK_SIZE * HEIGHT);
        gc = canvas.getGraphicsContext2D();

        Line line = new Line(BLOCK_SIZE * WIDTH + 1, 0, BLOCK_SIZE * WIDTH + 2, BLOCK_SIZE * HEIGHT);
        line.setFill(Color.WHITE);

        Button player = new Button("Player");
        player.setLayoutX(BLOCK_SIZE * WIDTH + 10);
        player.setLayoutY(BLOCK_SIZE * HEIGHT / 2.0 - 50);

        Button solver = new Button("Solver");
        solver.setLayoutX(BLOCK_SIZE * WIDTH + 10);
        solver.setLayoutY(BLOCK_SIZE * HEIGHT / 2.0);

        player.setOnAction(e -> {
            createTetromino();
            AnimationTimer timer = new AnimationTimer() {
                @Override
                public void handle(long now) {
                    time += 0.017;

                    if (time >= 0.5) {
                        makeMove(KeyCode.DOWN);
                        time = 0;
                    }
                }
            };
            timer.start();
        });

        solver.setOnAction(e -> {
            Solver solve = new Solver();
            AnimationTimer timer = new AnimationTimer() {
                @Override
                public void handle(long now) {
                    time += 2;
                    if (time >= 16) {
                        solve.makeMove();
                        time = 0;
                    }
                }
            };
            timer.start();
        });

        root.getChildren().addAll(canvas, line, player, solver);
        Arrays.fill(mine[HEIGHT], 1);

        return root;
    }

    boolean isGameOver() {
        return gameOver;
    }

    void createTetromino() {
        if (!gameOver) {
            currentTetromino = new Tetromino();
            gameOver = currentTetromino.isTouchGround();
            if (!gameOver)
                currentTetromino.paint();
        }
    }

    void makeMove(KeyCode direction) {
        if (!gameOver) {
            clearRows();
            if (currentTetromino.isTouchGround()) {
                currentTetromino.leaveOnTheGround();
                createTetromino();
            } else {
                if (!currentTetromino.isTouchWall(direction))
                    currentTetromino.move(direction);
            }
            if (direction == KeyCode.UP) {
                currentTetromino.rotate();
            }
        }
    }

    void repaintMine(int row){
        for (int i = row; i > 0; i--) {
            for (int j = 0; j < WIDTH; j++) {
                gc.setFill(colors.get(mine[i][j]));
                gc.fillRect(j * BLOCK_SIZE + 1, i * BLOCK_SIZE + 1, BLOCK_SIZE - 2, BLOCK_SIZE - 2);
            }
        }
    }

    void erase() {
        currentTetromino.erase();
    }

    void clearRows() {
        int clearRows = 0;
        int filled = 1;
        int currentRow = HEIGHT - 1;
        while (currentRow > 0) {
            for (int column = 0; column < WIDTH; column++) {
                filled = mine[currentRow][column] * filled;
                if (filled < 0) break;
            }
            if (filled > 0) {
                clearRows++;
                for (int i = currentRow; i > 0; i--) {
                    System.arraycopy(mine[i - 1], 0, mine[i], 0, WIDTH);
                    repaintMine(i);
                }
            } else
                currentRow--;
            filled = 1;
        }
        if (clearRows > 0) {
            gameScore += SCORES[clearRows - 1];
        }
    }
}