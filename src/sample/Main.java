package sample;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;


public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("TetrisGame");
        primaryStage.setResizable(false);

        Tetris tetris = new Tetris();
        Scene scene = new Scene(tetris.createContent());
        scene.setFill(Color.BLACK);

        scene.setOnKeyPressed(event -> {
            if (!tetris.isGameOver()) {
                tetris.makeMove(event.getCode());
                tetris.repaint();
            }
        });

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}