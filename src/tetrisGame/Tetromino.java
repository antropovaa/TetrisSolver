package tetrisGame;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;

import java.util.*;

import static tetrisGame.Tetris.*;

/**
 * Class for building figure named Tetromino from multiple Blocks.
 */
class Tetromino {
    static Map<Integer, Color> colors = new HashMap<Integer, Color>(){{
        put(0, Color.BLACK);
        put(1, Color.BLUE);
        put(2, Color.YELLOW);
        put(3, Color.ORANGE);
        put(4, Color.DARKBLUE);
        put(5, Color.GREEN);
        put(6, Color.MAGENTA);
        put(7, Color.RED);
    }};

    private final int [][][] shapes = {
            {{0,0,0,0}, {1,1,1,1}, {0,0,0,0}, {0,0,0,0}, {4, 1}}, // I
            {{1,1}, {1,1}, {2, 2}}, // O
            {{1,0,0}, {1,1,1}, {0,0,0}, {3, 3}}, // J
            {{0,0,1}, {1,1,1}, {0,0,0}, {3, 4}}, // L
            {{0,1,1}, {1,1,0}, {0,0,0}, {3, 5}}, // S
            {{1,1,1}, {0,1,0}, {0,0,0}, {3, 6}}, // T
            {{1,1,0}, {0,1,1}, {0,0,0}, {3, 7}} // Z
        };

    private List<Block> tetromino = new ArrayList<>();
    private int[][] shape = new int[Tetris.WIDTH][Tetris.HEIGHT];
    private int type, size, keyColor;
    private Color color;
    private String figure;
    private int x;
    private int y;
    private Random random = new Random();
    private boolean clockwise = true;

    Tetromino() {
        type = random.nextInt(shapes.length);
        size = shapes[type][shapes[type].length - 1][0];
        keyColor = shapes[type][shapes[type].length - 1][1];

        color = colors.get(keyColor);

        switch(type) {
            case 0:
                figure = "I";
                break;
            case 1:
                figure = "O";
                break;
            case 2:
                figure = "J";
                break;
            case 3:
                figure = "L";
                break;
            case 4:
                figure = "S";
                break;
            case 5:
                figure = "T";
                break;
            case 6:
                figure = "Z";
                break;
        }

        x = 3;
        y = 0;
        for (int i = 0; i < size; i++) {
            System.arraycopy(shapes[type][i], 0, shape[i], 0, shapes[type][i].length);
        }
        createFromShape();
    }



    void setLocation(int x, int y) {
        updateLocation(x - this.x, y - this.y);
    }

    int getHeight() {
        if (keyColor == 1)
            return 1;
        else if (keyColor > 1 && keyColor < 8)
            return 2;
        else
            return 0;
    }

    int getWidth() {
        Set<Integer> fulledColumns = new HashSet<>();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (shape[j][i] == 1)
                    fulledColumns.add(i);
            }
        }
        return fulledColumns.size();
    }

    private void createFromShape() {
        tetromino.clear();
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                Block block = new Block(x + this.x, y + this.y);
                if (shape[y][x] == 1)
                    tetromino.add(block);
            }
        }
    }

    void paint() {
        for (Block block : tetromino) {
            block.paint(color);
        }
    }

    void erase() {
        for (Block block : tetromino) {
            block.paint(Color.BLACK);
        }
    }

    boolean isTouchWall(KeyCode keyCode) {
        for (Block block : tetromino) {
            if (keyCode == KeyCode.LEFT &&
                    (block.getX() - 1 < 0 || mine[block.getY()][block.getX() - 1] > 0)) return true;
            if (keyCode == KeyCode.RIGHT &&
                    (block.getX() + 1 > Tetris.WIDTH - 1 || mine[block.getY()][block.getX() + 1] > 0)) return true;
        }
        return false;
    }

    boolean isTouchGround() {
        for (Block block : tetromino) {
            if (mine[block.getY() + 1][block.getX()] > 0)
                return true;
        }
        return false;
    }

//    boolean isCrossGround() {
//        for (Block block : tetromino)
//            if (block.getY() > HEIGHT - 1) return true;
//        return false;
//    }

    void leaveOnTheGround() {
        for (Block block : tetromino) {
            mine[block.getY()][block.getX()] = keyColor;
            block.paint(colors.get(keyColor));
        }
    }

    void drop() {
        for (Block block : tetromino) {
            mine[block.getY()][block.getX()] = keyColor;
        }
    }

    void clearFromMine() {
        for (Block block : tetromino) {
            mine[block.getY()][block.getX()] = 0;
        }
    }

    boolean isWrongPosition() {
        for (int x = 0; x < size; x++)
            for (int y = 0; y < size; y++)
                if (shape[y][x] == 1) {
                    if (y + this.y < 0) return true;
                    if (y + this.y > HEIGHT - 1) return true;
                    if (x + this.x < 0 || x + this.x > WIDTH - 1) return true;
                    if (mine[y + this.y][x + this.x] > 0) return true;
                }
        return false;
    }

    private void rotateShape(boolean clockwise) {
        int[][] newShape = new int[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++)
                if (clockwise) {
//                        Block block = getBlock(x + j, y + i);
//                        block.erase();
//                        tetromino.remove(block);
                        newShape[j][size - 1 - i] = shape[i][j];
//                        Block newBlock = new Block(x + size - 1 - i, y + j);
//                        newBlock.paint(color);
                } else {
                    newShape[size - 1 - j][i] = shape[i][j];
                }
        }
        shape = newShape;
    }

//    Block getBlock(int x, int y) {
//        Block result = null;
//        for (Block block : tetromino) {
//            if (block.getX() == x && block.getY() == y) {
//                result = block;
//            }
//        }
//        return result;
//    }

    void rotate() {
        rotateShape(clockwise);
        if (!isWrongPosition()) {
            erase();
            createFromShape();
        } else {
            rotateShape(!clockwise);
        }
    }

    void move(KeyCode direction) {
        erase();
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
        paint();
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