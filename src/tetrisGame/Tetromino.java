package tetrisGame;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;

import java.util.*;

/**
 * Class for building figure named Tetromino from multiple Blocks.
 */
class Tetromino {
    private Map<Integer, Color> colors = new HashMap<Integer, Color>(){{
        put(1, Color.BLUE);
        put(2, Color.YELLOW);
        put(3, Color.ORANGE);
        put(4, Color.DARKBLUE);
        put(5, Color.GREEN);
        put(6, Color.MAGENTA);
        put(7, Color.RED);
    }};

    private final int [][][] shapes = {
            {{1,1,1,1}, {0,0,0,0}, {0,0,0,0}, {0,0,0,0}, {4, 1}}, // I
            {{0,1,1,0}, {0,1,1,0}, {0,0,0,0}, {0,0,0,0}, {4, 2}}, // O
            {{1,0,0,0}, {1,1,1,0}, {0,0,0,0}, {0,0,0,0}, {4, 3}}, // J
            {{0,0,1,0}, {1,1,1,0}, {0,0,0,0}, {0,0,0,0}, {4, 4}}, // L
            {{0,1,1,0}, {1,1,0,0}, {0,0,0,0}, {0,0,0,0}, {3, 5}}, // S
            {{1,1,1,0}, {0,1,0,0}, {0,0,0,0}, {0,0,0,0}, {3, 6}}, // T
            {{1,1,0,0}, {0,1,1,0}, {0,0,0,0}, {0,0,0,0}, {3, 7}} // Z
        };

    private List<Block> tetromino = new ArrayList<>();
    private int[][] shape = new int[Tetris.WIDTH][Tetris.HEIGHT];
    private int type, size, keyColor;
    private Color color;
    private int x;
    private int y;
    private Random random = new Random();
    private boolean clockwise = true;

    Tetromino() {
        type = random.nextInt(shapes.length);
        size = shapes[type][4][0];
        keyColor = shapes[type][4][1];
        color = colors.get(shapes[type][4][1]);
        x = 3;
        y = 0;
        for (int i = 0; i < size; i++) {
            System.arraycopy(shapes[type][i], 0, shape[i], 0, shapes[type][i].length);
        }
        createFromShape();
    }

    private void createFromShape() {
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                Block block = new Block(x + this.x, y + this.y);
                if (shape[y][x] == 1)
                    tetromino.add(block);
            }
        }
    }

    void paint(GraphicsContext gc) {
        for (Block block : tetromino) {
            block.paint(gc, color);
        }
    }

    boolean isTouchWall(KeyCode keyCode) {
        for (Block block : tetromino) {
            if (keyCode == KeyCode.LEFT && block.getX() - 1 < 0) return true;
            if (keyCode == KeyCode.RIGHT && block.getX() + 1 > Tetris.WIDTH - 1) return true;
        }
        return false;
    }

    boolean isTouchGround() {
        for (Block block : tetromino) {
            if (Tetris.mine[block.getY() + 1][block.getX()] > 0)
                return true;
        }
        return false;
    }

    boolean isCrossGround() {
        for (Block block : tetromino)
            if (Tetris.mine[block.getY() + 1][block.getX()] > 0)
                return true;
        return false;
    }

    void leaveOnTheGround() {
        for (Block block : tetromino) {
            Tetris.mine[block.getY()][block.getX()] = keyColor;
        }
    }

    private boolean isWrongPosition() {
        for (int x = 0; x < size; x++)
            for (int y = 0; y < size; y++)
                if (shape[y][x] == 1) {
                    if (y + this.y < 0) return true;
                    if (x + this.x < 0 || x + this.x > Tetris.WIDTH - 1) return true;
                    if (Tetris.mine[y + this.y][x + this.x] > 0) return true;
                }
        return false;
    }

    private void rotateShape(boolean clockwise) {
        for (int i = 0; i < size/2; i++)
            for (int j = i; j < size - 1 - i; j++)
                if (clockwise) {
                    int temp = shape[size - 1 - j][i];
                    shape[size - 1 - j][i] = shape[size - 1 - i][size - 1 - j];
                    shape[size - 1 - i][size - 1 - j] = shape[j][size - 1 - i];
                    shape[j][size - 1 - i] = shape[i][j];
                    shape[i][j] = temp;
                } else {
                    int temp = shape[i][j];
                    shape[i][j] = shape[j][size - 1 - i];
                    shape[j][size - 1 - i] = shape[size - 1 - i][size - 1 - j];
                    shape[size - 1 - i][size - 1 - j] = shape[size - 1 - j][i];
                    shape[size - 1 - j][i] = temp;
                }
    }

    void rotate() {
        rotateShape(clockwise);
        if (!isWrongPosition()) {
            tetromino.clear();
            createFromShape();
        } else
            rotateShape(!clockwise);
    }

    void move(KeyCode direction) {
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

        updateLocation(dx, dy);
    }

    private void updateLocation(int dx, int dy) {
        for (Block block : tetromino) {
            block.setX(block.getX() + dx);
            block.setY(block.getY() + dy);
        }
        x = x + dx;
        y = y + dy;
    }
}