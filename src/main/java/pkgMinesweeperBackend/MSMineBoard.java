package pkgMinesweeperBackend;

import java.util.Random;

public class MSMineBoard {
    private int current_score = 0;
    private final CellData[][] board;
    private boolean gameActive = false;
    private static int ROWS;
    private static int COLS;
    private static final int NUM_MINES = 14;

    public MSMineBoard(int rows, int cols) {
        ROWS = rows;
        COLS = cols;
        board = new CellData[rows][cols];
        int goldScore = 40;
        int mineScore = 0;

        // All tiles will start as gold and not exposed
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                board[row][col] = new CellData();
                board[row][col].status = MSSpot.TILE_STATUS.NOT_EXPOSED;
                board[row][col].type = MSSpot.TILE_TYPE.GOLD;
                board[row][col].tile_score = goldScore;
            }
        }

        // Randomly place 14 mines
        Random rand = new Random();
        int mineCount = 0;
        while (mineCount < NUM_MINES) {
            int row = rand.nextInt(ROWS);
            int col = rand.nextInt(COLS);

            if (board[row][col].type != MSSpot.TILE_TYPE.MINE) {
                board[row][col].type = MSSpot.TILE_TYPE.MINE;
                board[row][col].tile_score = mineScore;
                mineCount++;
            }
        }
        countNNN();
        gameActive = true;
    }

    public void printBoard() {
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                if (board[row][col].type == MSSpot.TILE_TYPE.GOLD) {
                    System.out.print("G ");
                } else { // Mine
                    System.out.print("M ");
                }
            }
            System.out.println();
        }
        System.out.println();
    }

    public boolean isGameActive() {
        return gameActive;
    }

    public void printTileScores() {
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                System.out.printf("%2d ", board[row][col].tile_score);
            }
            System.out.println();
        }
    }

    public MSSpot.TILE_STATUS getTileStatus(int row, int col) {
        return board[row][col].status;
    }

    public void changeTileStatus(int row, int col) {
        board[row][col].status = MSSpot.TILE_STATUS.EXPOSED;
        current_score += board[row][col].tile_score;
    }

    public MSSpot.TILE_TYPE getTileType(int row, int col) {
        return board[row][col].type;
    }

    public int getCurrentScore() {
        return current_score;
    }

    // Count the Next Nearest Neighbors of a tile
    public void countNNN() {
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                // Skip over mines they are always 0
                if (getTileType(i, j) == MSSpot.TILE_TYPE.MINE) {
                    continue;
                }

                MSSpot.TILE_TYPE mine = MSSpot.TILE_TYPE.MINE;
                int numMines = 0;
                int prevC = (COLS + j - 1) % COLS;
                int nextC = (j + 1) % COLS;
                int nextR = (i + 1) % ROWS;
                int prevR = (ROWS + i - 1) % ROWS;

                numMines += getTileType(i, prevC) == mine ? 1 : 0;
                numMines += getTileType(prevR, j) == mine ? 1 : 0;
                numMines += getTileType(i, nextC) == mine ? 1 : 0;
                numMines += getTileType(nextR, j) == mine ? 1 : 0;

                numMines += getTileType(nextR, prevC) == mine ? 1 : 0;
                numMines += getTileType(nextR, nextC) == mine ? 1 : 0;
                numMines += getTileType(prevR, nextC) == mine ? 1 : 0;
                numMines += getTileType(prevR, prevC) == mine ? 1 : 0;

                int nnn_tiles = 8; // There are 8 surrounding tiles
                int numGold = nnn_tiles - numMines;  // This will give total gold tiles surrounding the cell
                board[i][j].tile_score = (10 * numMines) + (5 * numGold); // Sets the new score
            }
        }
    }

    public void clickedTileStatus(int row, int col) {
        if (getTileStatus(row, col) == MSSpot.TILE_STATUS.NOT_EXPOSED) {
            if (getTileType(row, col) == MSSpot.TILE_TYPE.GOLD) {
                changeTileStatus(row, col);
            } else {
                revealBoard();
                gameActive = false;
            }
            System.out.printf("Mouse Clicked: (%d, %d)\t score: %d\n", row, col, getCurrentScore());  // Prints which tile was clicked
        }
    }

    private void revealBoard() {
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                board[row][col].status = MSSpot.TILE_STATUS.EXPOSED;
            }
        }
    }

    private static class CellData {
        private MSSpot.TILE_STATUS status;
        private MSSpot.TILE_TYPE type;
        private int tile_score;
    }
}
