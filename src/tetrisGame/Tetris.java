package tetrisGame;

import javafx.animation.AnimationTimer;
import javafx.scene.Parent;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Contains all the logic of the TetrisGame
 */
class Tetris {
    static final int BLOCK_SIZE = 25;
    static final int WIDTH = 10; // in blocks
    static final int HEIGHT = 18;
    static int[][] mine = new int[HEIGHT + 1][WIDTH];

    private Tetromino currentTetromino;
    private int time;
    private List<Tetromino> tetrominos = new ArrayList<>();
    private GraphicsContext gc;
    private boolean gameOver = false;

    Parent createContent() {
        Pane root = new Pane();
        root.setPrefSize(BLOCK_SIZE * WIDTH, BLOCK_SIZE * HEIGHT);

        Canvas canvas = new Canvas(BLOCK_SIZE * WIDTH, BLOCK_SIZE * HEIGHT);
        gc = canvas.getGraphicsContext2D();

        root.getChildren().addAll(canvas);

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                time += 0.017;

                if (time >= 0.5) {
                    move(KeyCode.DOWN);
                    repaint();
                    time = 0;
                }
            }
        };
        timer.start();
        Arrays.fill(mine[HEIGHT], 1);
        createTetromino();

        return root;
    }

    boolean isGameOver() {
        return gameOver;
    }

    private void createTetromino() {
        if (!gameOver) {
            Tetromino tetromino = new Tetromino();
            tetrominos.add(tetromino);
            currentTetromino = tetromino;
            gameOver = currentTetromino.isCrossGround();
            if (!gameOver)
                tetromino.paint(gc);
        }
    }

    private void move(KeyCode direction) {
        int dx = 0;
        int dy = 0;

        switch (direction) {
            case DOWN:
                dy = 1;
                break;
            case LEFT:
                dx = -1;
                break;
            case RIGHT:
                dx = 1;
                break;
        }

        currentTetromino.updateLocation(dx, dy);
    }

    void makeMove(KeyCode direction) {
        if (!gameOver) {
            if (currentTetromino.isTouchGround()) {
                currentTetromino.leaveOnTheGround();
                createTetromino();
            } else {
                if (!currentTetromino.isTouchWall(direction))
                    move(direction);
            }
            if (direction == KeyCode.UP) {
                currentTetromino.rotate();
            }
        }
    }

    void repaint() {
        gc.clearRect(0, 0, WIDTH * BLOCK_SIZE, HEIGHT * BLOCK_SIZE);

        for (Tetromino tetromino : tetrominos) {
            tetromino.paint(gc);
        }
    }
}