package tetrisGame;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.stage.Stage;


public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("TetrisGame");
        primaryStage.setResizable(false);

        Tetris tetris = new Tetris();
        Solver solver = new Solver();
        Scene scene = new Scene(tetris.createContent());
        scene.setFill(Color.BLACK);

        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                solver.makeMove();
            } else {
                if (!tetris.isGameOver()) {
                    tetris.erase();
                    tetris.makeMove(event.getCode());
                    primaryStage.setTitle("TetrisGame | SCORE: " + tetris.gameScore);
                }
            }
        });

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}