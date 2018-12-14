package tetrisGame;

import javafx.util.Pair;

class Solver {
    private double heightWeight;
    private double linesWeight;
    private double holesWeight;
    private double dropsWeight;

    Solver(double heightWeight, double linesWeight, double holesWeight, double dropsWeight) {
        this.heightWeight = heightWeight;
        this.linesWeight = linesWeight;
        this.holesWeight = holesWeight;
        this.dropsWeight = dropsWeight;
    }

    private Pair<Tetromino, Double> best(Board board, Tetromino[] workingTetromino, int workingTetrominoIndex) {
        Tetromino best = null;
        double bestScore = 0;
        Tetromino workingPiece = workingTetromino[workingTetrominoIndex];

        for (int rotation = 0; rotation < 4; rotation++) {
            Tetromino _piece = workingPiece.clone();
            for (int i = 0; i < rotation; i++) {
                _piece.rotate(board);
            }

            while(_piece.canMoveLeft(board)) {
                _piece.moveLeft();
            }

            while (board.valid(_piece)) {
                Tetromino _pieceSet = _piece.clone();
                while (_pieceSet.canMoveDown(board)) {
                    _pieceSet.moveDown();
                }

                Board _board = board.clone();
                _board.addTetromino(_pieceSet);

                double score;
                if (workingTetrominoIndex == (workingTetromino.length - 1)) {
                    score = - this.heightWeight * _board.summaryHeight() + this.linesWeight * _board.fullLines()
                            - this.holesWeight * _board.holes() - this.dropsWeight * _board.drops();
                } else {
                    score = this.best(_board, workingTetromino, workingTetrominoIndex + 1).getValue();
                }

                if (score > bestScore || bestScore == 0) {
                    bestScore = score;
                    best = _piece.clone();
                }

                _piece.column++;
            }
        }
        return new Pair<>(best, bestScore);
    }

    Tetromino best(Board board, Tetromino[] workingPieces) {
        return best(board, workingPieces, 0).getKey();
    }
}
