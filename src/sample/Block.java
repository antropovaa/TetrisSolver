package sample;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import static sample.Tetris.*;


/**
 * Class for building element for Tetromino
 */
class Block {
    private int x;
    private int y;

    Block(int x, int y) {
        this.x = x;
        this.y = y;
    }

    void setX(int x) {
        this.x = x;
    }
    void setY(int y) {
        this.y = y;
    }

    int getX() {
        return x;
    }
    int getY() {
        return y;
    }

    void paint(GraphicsContext gc, Color color) {
        gc.setFill(color);
        gc.fillRect(x * BLOCK_SIZE + 1, y * BLOCK_SIZE + 1, BLOCK_SIZE - 2, BLOCK_SIZE - 2);
    }
}