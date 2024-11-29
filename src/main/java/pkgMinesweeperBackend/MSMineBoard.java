package pkgMinesweeperBackend;

public class MSMineBoard {
    private int current_score = 0;
    private CellData[][] board;
    private boolean gameActive = false;
    private static final int ROWS = 9;
    private static final int COLS = 7;
    private static final int NUM_MINES = 14;

    public MSMineBoard(int rows, int cols) {

    }

    public void printBoard() {
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {

            }
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
