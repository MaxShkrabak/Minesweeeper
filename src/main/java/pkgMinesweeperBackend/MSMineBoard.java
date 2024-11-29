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

        // All tiles will start as gold and not exposed
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                board[row][col] = new CellData();
                board[row][col].status = MSSpot.TILE_STATUS.NOT_EXPOSED;
                board[row][col].type = MSSpot.TILE_TYPE.GOLD;
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
                mineCount++;
            }
        }

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
    }

    public boolean isGameActive() {
        return gameActive;
    }

    public void printTileScores() {

    }

    public MSSpot.TILE_STATUS getTileStatus(int row, int col) {
        return null;
    }

    public MSSpot.TILE_TYPE changeTileStatus(int row, int col) {
        return null;
    }

    public MSSpot.TILE_TYPE getTileType(int row, int col) {
        return null;
    }

    public int getCurrentScore() {
        return current_score;
    }

    private static class CellData {
        private MSSpot.TILE_STATUS status;
        private MSSpot.TILE_TYPE type;
        private int tile_score;
    }
}
