package tetrisGame;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

class Randomizer {
    private List<Integer> bag;
    private int index;

    Randomizer() {
        this.bag = Arrays.asList(0, 1, 2, 3, 4, 5, 6);

        Collections.shuffle(this.bag);
        this.index = -1;
    }

    Tetromino nextTetromino() {
        this.index++;
        if (this.index >= this.bag.size()) {
            Collections.shuffle(this.bag);
            this.index = 0;
        }
        return Tetromino.fromIndex(this.bag.get(this.index));
    }
}