package pkgMinesweeperBackend;

import java.util.Random;

public class MSPingPong {
    private boolean[][] liveArray;
    private boolean[][] nextArray;

    private final int ROWS;
    private final int COLS;
    private final Random rand = new Random();

    // Constructor
    public MSPingPong(int rows, int cols) {
        this.ROWS = rows;
        this.COLS = cols;
        this.liveArray = new boolean[rows][cols];
        this.nextArray = new boolean[rows][cols];
        resetBoard();
    }

    // Set the chosen element to a passed in value
    private void setNext(int row, int col, boolean val) {
        nextArray[row][col] = val;
    }

    // Gets a specific element from the Live Array
    public boolean getLive(int row, int col) {
        return liveArray[row][col];
    }

    // Swap the live and next array
    public void swapArray() {
        boolean[][] temp = liveArray;
        liveArray = nextArray;
        nextArray = temp;
    }

    // Count the Next Nearest Neighbors of a cell
    public void countNNN() {
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                int count = 0;
                int prevC = (COLS + j - 1) % COLS;
                int nextC = (j + 1) % COLS;
                int nextR = (i + 1) % ROWS;
                int prevR = (ROWS + i - 1) % ROWS;

                // Nearest Neighbors
                count += getLive(i, prevC) ? 1 : 0;
                count += getLive(prevR, j) ? 1 : 0;
                count += getLive(i, nextC) ? 1 : 0;
                count += getLive(nextR, j) ? 1 : 0;
                // Next Nearest Neighbors
                count += getLive(nextR, prevC) ? 1 : 0;
                count += getLive(nextR, nextC) ? 1 : 0;
                count += getLive(prevR, nextC) ? 1 : 0;
                count += getLive(prevR, prevC) ? 1 : 0;

                if (getLive(i, j)) {
                    if (count == 2 || count == 3) {
                        setNext(i, j, true);
                    } else {
                        setNext(i, j, false);
                    }
                } else {
                    if (count == 3) {
                        setNext(i, j, true);
                    } else {
                        setNext(i, j, false);
                    }
                }
            }
        }
    }

    // Method used to reset board to 0 or 1
    public void resetBoard() {
        for (int i = 0; i < liveArray.length; i++) {
            for (int j = 0; j < liveArray[i].length; j++) {
                liveArray[i][j] = rand.nextBoolean();
            }
        }
    }

    // Method to count all NNN and swap the arrays
    public void nextArray() {
        countNNN();
        swapArray();
    }
}