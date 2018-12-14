package tetrisGame;

import javafx.animation.AnimationTimer;
import javafx.scene.Parent;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;

/**
 * Contains all the logic of the TetrisGame
 */
class Tetris {
    private Board board;
    private Canvas canvas;
    private Canvas nextCanvas;
    private GraphicsContext context;
    private GraphicsContext nextContext;

    private Solver solver = new Solver(0.510066, 0.760666, 0.35663, 0.184483);
    private Randomizer randomizer = new Randomizer();
    private Tetromino[] workingTetrominos = {null, randomizer.nextTetromino()};
    private Tetromino workingTetromino = null;
    private boolean isSolverActive = false;
    private boolean isKeyEnabled = false;
    private double score = 0;
    double time;
    AnimationTimer timer;

    Parent createContent() {
        Pane root = new Pane();
        root.setPrefSize(350, 400);
        root.setStyle("-fx-background-color: black;");

        canvas = new Canvas(200, 400);
        context = canvas.getGraphicsContext2D();

        nextCanvas = new Canvas(130, 100);
        nextCanvas.setLayoutX(210);
        nextCanvas.setLayoutY(10);
        nextContext = canvas.getGraphicsContext2D();

        Button solverButton = new Button("Solve");
        solverButton.setPrefSize(130, 40);
        solverButton.setLayoutX(210);
        solverButton.setLayoutY(155);

        solverButton.setOnAction(e -> {
            if (isSolverActive) {
                isSolverActive = false;
            } else {
                isSolverActive = true;
                isKeyEnabled = false;
                timer.stop();
                startWorkingPieceDropAnimation();
            }
        });

        Button resetButton = new Button("Reset");
        resetButton.setPrefSize(130, 40);
        resetButton.setLayoutX(210);
        resetButton.setLayoutY(205);

        resetButton.setOnAction(e -> {
            timer.stop();
            clearBoard();
            board = new Board(22, 10);
            randomizer = new Randomizer();
            workingTetrominos = new Tetromino[]{null, randomizer.nextTetromino()};
            workingTetromino = null;
            score = 0;
            isKeyEnabled = true;
            isSolverActive = false;
            startTurn();
        });

        root.setOnKeyPressed(e -> {
            if (!isKeyEnabled)
                return;
            switch (e.getCode()) {
                case DOWN:
                    if (workingTetromino.canMoveDown(board)) {
                        workingTetromino.moveDown();
                        repaintCanvas();
                    }
                    break;
                case LEFT:
                    if (workingTetromino.canMoveLeft(board)) {
                        workingTetromino.moveLeft();
                        repaintCanvas();
                    }
                    break;
                case RIGHT:
                    if (workingTetromino.canMoveRight(board)) {
                        workingTetromino.moveRight();
                        repaintCanvas();
                    }
                    break;
                case UP:
                    workingTetromino.rotate(board);
                    repaintCanvas();
                    break;
            }
        });

        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                while (workingTetromino.canMoveDown(board)) {
                    time += 250;
                    if (time >= 750000000) {
                        workingTetromino.moveDown();
                        repaintCanvas();
                        time = 0;
                    }
                }
                if (!endTurn()) {
                    return;
                }
                startTurn();
            }
        };

        board = new Board(22, 10);
        startTurn();

        root.getChildren().addAll(canvas, nextCanvas, solverButton, resetButton);

        return root;
    }

    private String intToRGB(int value) {
        return "rgb(" + ((value >> 16) & 0xFF) + "," + ((value >> 8) & 0xFF) + "," + (value & 0xFF) + ")";
    }

    private void repaintCanvas() {
        context.save();
        context.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

        for (int r = 2; r < board.rows; r++) {
            for (int c = 0; c < board.columns; c++) {
                if (board.cells[r][c] != 0) {
                    context.setFill(Paint.valueOf(intToRGB(board.cells[r][c])));
                    context.fillRect(20 * (c),
                            20 * ((r) - 2), 20, 20);
                    context.setFill(Paint.valueOf(intToRGB(0xFFFFFF)));
                    context.strokeRect(20 * (c),
                            20 * ((r) - 2), 20, 20);
                }
            }
        }
        context.restore();
    }

    private void repaintNextCanvas() {
        nextContext.save();
        nextContext.clearRect(0, 0, nextCanvas.getWidth(), nextCanvas.getHeight());
        Tetromino next = workingTetrominos[1];

        int xOffSet = next.size == 2 ? 20 : next.size == 3 ? 10 : next.size == 4 ? 0 : null;
        int yOffSet = next.size == 2 ? 20 : next.size == 3 ? 20 : next.size == 4 ? 10 : null;
        for (int r = 0; r < next.size; r++) {
            for (int c = 0; c < next.size; c++) {
                if (next.cells[r][c] != 0) {
                    nextContext.setFill(Paint.valueOf(intToRGB(next.cells[r][c])));
                    nextContext.fillRect( xOffSet + 20 * c, yOffSet + 20 * r, 20, 20);
                    nextContext.setFill(Paint.valueOf(intToRGB(0xFFFFFF)));
                    nextContext.strokeRect(xOffSet + 20 * c, yOffSet + 20 * r, 20, 20);
                }
            }
        }
        nextContext.restore();
    }

    private void startTurn() {
        // смена фигурок
        for (int i = 0; i < workingTetrominos.length - 1; i++) {
            workingTetrominos[i] = workingTetrominos[i + 1];
        }
        workingTetrominos[workingTetrominos.length - 1] = randomizer.nextTetromino();
        workingTetromino = workingTetrominos[0];

        repaintCanvas();
        repaintNextCanvas();

        if (isSolverActive) {
            isKeyEnabled = false;
            workingTetromino = solver.best(board, workingTetrominos);
            startWorkingPieceDropAnimation();
        } else {
            isKeyEnabled = true;
        }
    }

    private void startWorkingPieceDropAnimation() {
        time = 0;
        timer.start();
    }

    private boolean endTurn() {
        board.addTetromino(workingTetromino);
        score += board.clearLines();
        repaintCanvas();
        repaintNextCanvas();

        return !board.isFull();
    }

    private void clearBoard() {
        context.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }
}