package pkgSlRenderer;

import java.util.Random;

public class MSPingPong {
    private int[][] liveArray;
    private int[][] nextArray;

    private final int ROWS;
    private final int COLS;
    private final Random rand = new Random();

    public MSPingPong(int rows, int cols) {
        this(rows, cols, 0,1); //random 0 - 1
    }

    public MSPingPong(int rows, int cols, int val) {
        this(rows, cols, val, val);
    }

    public MSPingPong(int rows, int cols, int lowerBound, int upperBound) {
        this.ROWS = rows;
        this.COLS = cols;
        this.liveArray = new int[rows][cols];
        this.nextArray = new int[rows][cols];

        boundResetBoard(lowerBound, upperBound);
    }

    // Set the chosen element to a passed in value
    private void setNext(int row, int col, int val) {
        nextArray[row][col] = val;
    }

    // Gets a specific element from the Live Array
    private int getLive(int row, int col) {
        return liveArray[row][col];
    }

    // Swap the live and next array
    public void swapArray() {
        int[][] temp = liveArray;
        liveArray = nextArray;
        nextArray = temp;
    }

    // Count the Nearest Neighbors of a cell
    public void countNN() {
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                int count = 0;
                int prevC = (COLS + j - 1) % COLS;
                int nextC = (j + 1) % COLS;
                int nextR = (i + 1) % COLS;
                int prevR = (ROWS + i - 1) % ROWS;

                count += getLive(i, prevC) + getLive(prevR, j) + getLive(i, nextC) + getLive(nextR, j);

                setNext(i, j, count);
            }
        }
    }

    // Count the Next Nearest Neighbors of a cell
    public void countNNN() {
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                int count = 0;
                int prevC = (COLS + j - 1) % COLS;
                int nextC = (j + 1) % COLS;
                int nextR = (i + 1) % COLS;
                int prevR = (ROWS + i - 1) % COLS;

                count += getLive(nextR, prevC) + getLive(nextR, nextC) + getLive(prevR, nextC) + getLive(prevR, prevC);

                setNext(i, j, count);
            }
        }
    }

    // Display contents in array
    public void printArray() {
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                System.out.print(liveArray[i][j] + " ");
            }
            System.out.println();
        }
    }
    // Method to reset board between two values
    public void boundResetBoard(int lower, int upper) {
        for (int i = 0; i < liveArray.length; i++) {
            for (int j = 0; j < liveArray[i].length; j++) {
                liveArray[i][j] = rand.nextInt(upper - lower + 1) + lower;
            }
        }
    }
    // Method used to reset board to 0 or 1
    public void resetBoard() {
        boundResetBoard(0, 1);
    }
}
