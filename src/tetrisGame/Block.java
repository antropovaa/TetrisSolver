package tetrisGame;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import static tetrisGame.Tetris.BLOCK_SIZE;
import static tetrisGame.Tetris.gc;

/**
 * Class for building element for Tetromino
 */
class Block {
    private int x;
    private int y;
    private Color color;

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

    void paint(Color color) {
        gc.setFill(color);
        gc.fillRect(x * BLOCK_SIZE + 1, y * BLOCK_SIZE + 1, BLOCK_SIZE - 2, BLOCK_SIZE - 2);
    }

    void erase() {
        gc.clearRect(x * BLOCK_SIZE + 1, y * BLOCK_SIZE + 1, BLOCK_SIZE - 2, BLOCK_SIZE - 2);
    }
}