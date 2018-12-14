package tetrisGame;

class Randomizer {
    private int[] bag;
    private int index;

    Randomizer() {
        this.bag = new int[]{0, 1, 2, 3, 4, 5, 6};
        this.shuffleBag();
        this.index = -1;
    }

    Tetromino nextTetromino() {
        this.index++;
        if (this.index >= this.bag.length) {
            this.shuffleBag();
            this.index = 0;
        }
        return Tetromino.fromIndex(this.bag[this.index]);
    }

    private void shuffleBag() {
        int currentIndex = this.bag.length;
        int temporaryValue;
        int randomIndex;

        while (currentIndex != 0) {
            randomIndex = (int) Math.floor(Math.random() * currentIndex);
            currentIndex--;

            temporaryValue = this.bag[currentIndex];
            this.bag[currentIndex] = this.bag[randomIndex];
            this.bag[randomIndex] = temporaryValue;
        }
    }
}