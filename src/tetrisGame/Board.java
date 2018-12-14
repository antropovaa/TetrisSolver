package tetrisGame;

public class Board {
    int rows;
    int columns;
    int[][] cells;

    Board(int rows, int columns) {
        this.rows = rows;
        this.columns = columns;

        this.cells = new int[rows][columns];
        for (int r = 0; r < this.rows; r++) {
            for (int c = 0; c < this.columns; c++) {
                this.cells[r][c] = 0;
            }
        }
    }

    protected Board clone() {
        Board board = new Board(this.rows, this.columns);
        for (int r = 0; r < this.rows; r++) {
            for (int c = 0; c < this.columns; c++) {
                board.cells[r][c] = this.cells[r][c];
            }
        }
        return board;
    }

    int clearLines() {
        int distance = 0;
        int[] row = new int[this.columns];
        for (int r = this.rows - 1; r >= 0; r--) {
            if (this.isLine(r)) {
                distance++;
                for (int c = 0; c < this.columns; c++) {
                    this.cells[r][c] = 0;
                }
            } else if (distance > 0) {
                for (int c = 0; c < this.columns; c++) {
                    this.cells[r + distance][c] = this.cells[r][c];
                    this.cells[r][c] = 0;
                }
            }
        }
        return distance;
    }

    private boolean isLine(int row) {
        for (int c = 0; c < this.columns; c++) {
            if (this.cells[row][c] == 0) {
                return false;
            }
        }
        return true;
    }

    private boolean isEmptyRow(int row) {
        for (int c = 0; c < this.columns; c++) {
            if (this.cells[row][c] != 0)
                return false;
        }
        return true;
    }

    boolean isFull() {
        return !this.isEmptyRow(0) || !this.isEmptyRow(1);
    }

    int fullLines() {
        int count = 0;
        for (int r = 0; r < this.rows; r++) {
            if (this.isLine(r)){
                count++;
            }
        }
        return count;
    }

    int holes() {
        int count = 0;
        for (int c = 0; c < this.columns; c++) {
            boolean isGroundBegins = false;
            for (int r = 0; r < this.rows; r++) {
                if (this.cells[r][c] != 0) {
                    isGroundBegins = true;
                } else if (this.cells[r][c] == 0 && isGroundBegins) {
                    count++;
                }
            }
        }
        return count;
    }

    private int columnHeight(int column) {
        int r = 0;
        while (r < this.rows && this.cells[r][column] == 0) {
            r++;
        }
        return this.rows - r;
    }

    int summaryHeight() {
        int total = 0;
        for (int c = 0; c < this.columns - 1; c++) {
            total += this.columnHeight(c);
        }
        return total;
    }

    int drops() {
        int total = 0;
        for (int c = 0; c < this.columns - 1; c++) {
            total += Math.abs(this.columnHeight(c) - this.columnHeight(c + 1));
        }
        return total;
    }

    void addTetromino(Tetromino tetromino) {
        for (int r = 0; r < tetromino.cells.length; r++) {
            for (int c = 0; c < tetromino.cells[r].length; c++) {
                int _r = tetromino.row + r;
                int _c = tetromino.column + c;
                if (tetromino.cells[r][c] != 0 && _r >= 0) {
                    this.cells[_r][_c] = tetromino.cells[r][c];
                }
            }
        }
    }

    boolean valid(Tetromino tetromino) {
        for(int r = 0; r < tetromino.cells.length; r++){
            for(int c = 0; c < tetromino.cells[r].length; c++){
                int _r = tetromino.row + r;
                int _c = tetromino.column + c;
                if (tetromino.cells[r][c] != 0){
                    if(_r < 0 || _r >= this.rows){
                        return false;
                    }
                    if(_c < 0 || _c >= this.columns){
                        return false;
                    }
                    if (this.cells[_r][_c] != 0){
                        return false;
                    }
                }
            }
        }
        return true;
    }
}
