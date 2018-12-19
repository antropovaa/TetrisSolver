package tetrisGame;

import javafx.animation.AnimationTimer;
import javafx.scene.Parent;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

class Tetris {
    private Board board;
    private Canvas canvas;
    private Canvas nextCanvas;
    private GraphicsContext context;
    private GraphicsContext nextContext;

    private Solver solver = new Solver(0.510066, 0.760666, 0.35663, 0.184483);
    private Randomizer randomizer = new Randomizer();
    private Tetromino[] workingTetrominos;
    private Tetromino workingTetromino;
    private boolean isSolverActive = false;
    private double time;
    private AnimationTimer timer;

    Tetris(int numberOfNextTetromino) {
        workingTetrominos = new Tetromino[numberOfNextTetromino];

        if (numberOfNextTetromino > 0) {
            workingTetrominos[0] = null;

            for (int i = 1; i < workingTetrominos.length; i++) {
                workingTetrominos[i] = randomizer.nextTetromino();
            }
        }

        workingTetromino = null;
    }

    Parent createContent() {
        Pane root = new Pane();
        root.setPrefSize(350, 400);
        root.setStyle("-fx-background-color: #e2eff0;");

        Pane settingsPane = new Pane();
        settingsPane.setLayoutX(200);
        settingsPane.setLayoutY(0);
        settingsPane.setPrefSize(150, 400);
        settingsPane.setStyle("-fx-background-color: white");

        canvas = new Canvas(200, 400);
        context = canvas.getGraphicsContext2D();

        nextCanvas = new Canvas(130, 100);
        nextCanvas.setLayoutX(240);
        nextCanvas.setLayoutY(10);
        nextContext = nextCanvas.getGraphicsContext2D();

        Button solverButton = new Button("Solve");
        solverButton.setPrefSize(130, 40);
        solverButton.setLayoutX(210);
        solverButton.setLayoutY(155);

        Rectangle solveButton = new Rectangle(130, 40, Color.web("#999999"));
        solveButton.setLayoutX(210);
        solveButton.setLayoutY(155);

        Text solve = new Text(250, 180, "Solve");
        solve.setFill(Color.BLACK);
        solve.setStyle("-fx-font: 18 tahoma");
        solve.setOnMouseEntered(e -> solve.setFill(Color.WHITE));
        solve.setOnMouseExited(e -> solve.setFill(Color.BLACK));

        solveButton.setOnMouseClicked(e -> {
            if (!isSolverActive) {
                solveButton.setFill(Color.web("#cc9999"));
                isSolverActive = true;
                timer.stop();
                startWorkingPieceDropAnimation();
            }
        });

        Rectangle resetButton = new Rectangle(130, 40,Color.web("#999999"));
        resetButton.setLayoutX(210);
        resetButton.setLayoutY(205);

        Text reset = new Text(253, 230, "Reset");
        reset.setFill(Color.BLACK);
        reset.setStyle("-fx-font: 18 tahoma");
        reset.setOnMouseEntered(e -> reset.setFill(Color.WHITE));
        reset.setOnMouseExited(e -> reset.setFill(Color.BLACK));

        resetButton.setOnMouseClicked(e -> {
            timer.stop();
            clearBoard();
            board = new Board(22, 10);
            randomizer = new Randomizer();
            workingTetrominos = new Tetromino[]{null, randomizer.nextTetromino()};
            workingTetromino = null;
            isSolverActive = false;
            solveButton.setFill(Color.web("#999999"));
            startTurn();
        });

        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {

//                while (workingTetromino.canMoveDown(board)) {
//                    time += 500;
//                    if (time >= 750000000) {
//                        workingTetromino.moveDown();
//                        repaintCanvas();
//                        time = 0;
//                    }
//                }

                while (workingTetromino.canMoveDown(board)) {
                    time += 500;
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

        root.getChildren().addAll(settingsPane, canvas, nextCanvas, solveButton, solve, resetButton, reset);

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
                    context.fillRoundRect(20 * (c),
                            20 * (r - 2), 20, 20, 10, 10);
                    context.setStroke(Paint.valueOf(intToRGB(0xe2eff0)));
                    context.strokeRect(20 * (c),
                            20 * (r - 2), 20, 20);
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
                    nextContext.fillRoundRect(xOffSet + 20 * c, yOffSet + 20 * r, 20, 20, 10, 10);
                    //nextContext.strokeStyle(Paint.valueOf(intToRGB(0xe2eff0)));
                    nextContext.setStroke(Color.web("#ffffff"));
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
            workingTetromino = solver.best(board, workingTetrominos);
        }
    }

    private void startWorkingPieceDropAnimation() {
        time = 0;
        timer.start();
    }

    private boolean endTurn() {
        board.addTetromino(workingTetromino);
        repaintCanvas();
        board.clearLines();
        repaintCanvas();
        repaintNextCanvas();

        return !board.isFull();
    }

    private void clearBoard() {
        context.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }
}