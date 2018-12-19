package tetrisGame;

import javafx.util.Pair;

enum Figure {
    O(0),
    J(1),
    L(2),
    Z(3),
    S(4),
    T(5),
    I(6);

    private int index;

    Figure(int index) {
        this.index = index;
    }

    int getIndex() {
        return index;
    }
}

class Tetromino {
    int[][] cells;
    int size;
    int row;
    int column;

    private Tetromino(int[][] cells) {
        this.cells = cells;
        this.size = this.cells.length;
        this.row = 0;
        this.column = 0;
    }

    private static Figure getFigure(int index) {
        Figure result = null;
        for (Figure f : Figure.values()) {
            if (index == f.getIndex()) {
                result = f;
            }
        }
        return result;
    }

    static Tetromino fromIndex(int index) {
        Tetromino tetromino = null;

        switch (getFigure(index)) {
            case O:
                int[][] cells0 = {
                        {0xdee02c, 0xdee02c},
                        {0xdee02c, 0xdee02c}
                };
                tetromino = new Tetromino(cells0);
                break;
            case J:
                int[][] cells1 = {
                        {0x609ee3, 0x000000, 0x000000},
                        {0x609ee3, 0x609ee3, 0x609ee3},
                        {0x000000, 0x000000, 0x000000}
                };
                tetromino = new Tetromino(cells1);
                break;
            case L:
                int[][] cells2 = {
                        {0x000000, 0x000000, 0xf3b03a},
                        {0xf3b03a, 0xf3b03a, 0xf3b03a},
                        {0x000000, 0x000000, 0x000000}
                };
                tetromino = new Tetromino(cells2);
                break;
            case Z:
                int[][] cells3 = {
                        {0xf33a3a, 0xf33a3a, 0x000000},
                        {0x000000, 0xf33a3a, 0xf33a3a},
                        {0x000000, 0x000000, 0x000000}
                };
                tetromino = new Tetromino(cells3);
                break;
            case S:
                int[][] cells4 = {
                        {0x000000, 0x56ce30, 0x56ce30},
                        {0x56ce30, 0x56ce30, 0x00000},
                        {0x000000, 0x000000, 0x000000}
                };
                tetromino = new Tetromino(cells4);
                break;
            case T:
                int[][] cells5 = {
                        {0x000000, 0xe360b0, 0x000000},
                        {0xe360b0, 0xe360b0, 0xe360b0},
                        {0x000000, 0x000000, 0x000000}
                };
                tetromino = new Tetromino(cells5);
                break;
            case I:
                int[][] cells6 = {
                        {0x000000, 0x000000, 0x000000, 0x000000},
                        {0x3ecfcd, 0x3ecfcd, 0x3ecfcd, 0x3ecfcd},
                        {0x000000, 0x000000, 0x000000, 0x000000},
                        {0x000000, 0x000000, 0x000000, 0x000000}
                };
                tetromino = new Tetromino(cells6);
                break;
        }

        tetromino.row = 0;
        tetromino.column = (int) Math.floor((10 - tetromino.size) / 2.0); // выравнивание по центру

        return tetromino;
    }

    protected Tetromino clone() {
        int[][] cells = new int[this.size][this.size];
        for (int r = 0; r < this.size; r++) {
            System.arraycopy(this.cells[r], 0, cells[r], 0, this.size);
        }

        Tetromino piece = new Tetromino(cells);
        piece.row = this.row;
        piece.column = this.column;
        return piece;
    }

    boolean canMoveLeft(Board board) {
        for (int r = 0; r < this.cells.length; r++) {
            for (int c = 0; c < this.cells[r].length; c++) {
                int _r = this.row + r;
                int _c = this.column + c - 1;
                if (this.cells[r][c] != 0) {
                    if (!(_c >= 0 && board.cells[_r][_c] == 0)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    boolean canMoveRight(Board board) {
        for (int r = 0; r < this.cells.length; r++) {
            for (int c = 0; c < this.cells[r].length; c++) {
                int _r = this.row + r;
                int _c = this.column + c + 1;
                if (this.cells[r][c] != 0) {
                    if (!(_c >= 0 && board.cells[_r][_c] == 0)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    boolean canMoveDown(Board board) {
        for (int r = 0; r < this.cells.length; r++) {
            for (int c = 0; c < this.cells[r].length; c++) {
                int _r = this.row + r + 1;
                int _c = this.column + c;
                if (this.cells[r][c] != 0) {
                    if (!(_r < board.rows && board.cells[_r][_c] == 0)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    void moveLeft() {
        this.column--;
    }

    void moveRight() {
        this.column++;
    }

    void moveDown() {
        this.row++;
    }

    private void rotateCells() {
        int[][] _cells = new int[this.size][this.size];

//        for (int i = 0; i < this.size / 2; i++) {
//            for (int j = i; j < this.size - 1 - i; j++) {
//                _cells[i][j] = this.cells[this.size - 1 - j][i];
//                _cells[j][this.size - 1 - i] = this.cells[i][j];
//                _cells[this.size - 1 - j][i] = this.cells[this.size - 1 - i][this.size - 1 - j];
//                _cells[this.size - 1 - i][this.size - 1 - j] = this.cells[j][this.size - 1 - i];
//            }
//        }

        switch (this.size) {
            case 2:
                _cells[0][0] = this.cells[1][0];
                _cells[0][1] = this.cells[0][0];
                _cells[1][0] = this.cells[1][1];
                _cells[1][1] = this.cells[0][1];
                break;
            case 3:
                _cells[0][0] = this.cells[2][0];
                _cells[0][1] = this.cells[1][0];
                _cells[0][2] = this.cells[0][0];
                _cells[1][0] = this.cells[2][1];
                _cells[1][1] = this.cells[1][1];
                _cells[1][2] = this.cells[0][1];
                _cells[2][0] = this.cells[2][2];
                _cells[2][1] = this.cells[1][2];
                _cells[2][2] = this.cells[0][2];
                break;
            case 4:
                _cells[0][0] = this.cells[3][0];
                _cells[0][1] = this.cells[2][0];
                _cells[0][2] = this.cells[1][0];
                _cells[0][3] = this.cells[0][0];
                _cells[1][3] = this.cells[0][1];
                _cells[2][3] = this.cells[0][2];
                _cells[3][3] = this.cells[0][3];
                _cells[3][2] = this.cells[1][3];
                _cells[3][1] = this.cells[2][3];
                _cells[3][0] = this.cells[3][3];
                _cells[2][0] = this.cells[3][2];
                _cells[1][0] = this.cells[3][1];
                _cells[1][1] = this.cells[2][1];
                _cells[1][2] = this.cells[1][1];
                _cells[2][2] = this.cells[1][2];
                _cells[2][1] = this.cells[2][2];
                break;
        }

        this.cells = _cells;
    }

    // метод для "коррекции" положения фигурки, которая могла выйти за пределы доски после поворота
    private Pair<Integer, Integer> computeRotateOffset(Board board) {
        Tetromino current = this.clone();
        current.rotateCells();

        if (board.valid(current)) {
            return new Pair<>(current.row - this.row, current.column - this.column);
        }

        // если вышла за пределы доски
        int initialRow = current.row;
        int initialCol = current.column;

        for (int i = 0; i < current.size - 1; i++) {
            current.column = initialCol + i;
            if (board.valid(current)) {
                return new Pair<>(current.row - this.row, current.column - this.column);
            }

            for (int j = 0; j < current.size - 1; j++) {
                current.row = initialRow - j;
                if (board.valid(current)) {
                    return new Pair<>(current.row - this.row, current.column - this.column);
                }
            }
            current.row = initialRow;
        }
        current.column = initialCol;

        for (int i = 0; i < current.size - 1; i++) {
            current.column = initialCol - i;
            if (board.valid(current)) {
                return new Pair<>(current.row - this.row, current.column - this.column);
            }

            for (int j = 0; j < current.size - 1; j++) {
                current.row = initialRow - j;
                if (board.valid(current)) {
                    return new Pair<>(current.row - this.row, current.column - this.column);
                }
            }
            current.row = initialRow;
        }
        current.column = initialCol;

        return null;
    }

    void rotate(Board board) {
        Pair<Integer, Integer> offset = this.computeRotateOffset(board);
        if (offset != null) {
            this.rotateCells();
            this.row += offset.getKey();
            this.column += offset.getValue();
        }
    }
}