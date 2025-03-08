package pkgMinesweeperBackend;

import java.util.ArrayList;
import java.util.Random;

public class MineBoard {
    private int current_score = 0;
    private final CellData[][] board;
    private boolean gameActive = false;
    private static int ROWS;
    private static int COLS;
    private static final int NUM_MINES = 40;
    private boolean firstPlay = true;
    private boolean[][] visited;

    public MineBoard(int rows, int cols) {
        ROWS = rows;
        COLS = cols;
        board = new CellData[rows][cols];
        int goldScore = 40;

        // All tiles will start as gold and not exposed
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                board[row][col] = new CellData();
                board[row][col].status = Spot.TILE_STATUS.NOT_EXPOSED;
                board[row][col].type = Spot.TILE_TYPE.GOLD;
                board[row][col].tile_score = goldScore;
            }
        }

        gameActive = true;
    }

    private void scatterMines(int sRow, int sCol) {
        int mineScore = 0;

        // Stores all available coordinates in an ArrayList
        ArrayList<int[]> coordinates = new ArrayList<>();
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                // Ensures initial tile and surrounding tiles are safe
                if (Math.abs(row - sRow) <= 1 && Math.abs(col - sCol) <= 1) {
                    continue;
                }
                coordinates.add(new int[]{row,col});
            }
        }

        // Get random coordinates for mines
        Random rand = new Random();
        for (int i = 0; i < NUM_MINES; i++) {
            int randIndex = rand.nextInt(coordinates.size()); // Gets random index 0 to arrayList size
            int[] coord = coordinates.get(randIndex);
            coordinates.remove(randIndex);     // Remove that coordinate so that it can't be picked again

            int row = coord[0];
            int col = coord[1];

            board[row][col].type = Spot.TILE_TYPE.MINE;
            board[row][col].tile_score = mineScore;
        }
    }

    public void printBoard() {
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                if (board[row][col].type == Spot.TILE_TYPE.GOLD) {
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

    public int getTileScore(int row, int col) {
        return board[row][col].tile_score;
    }

    public Spot.TILE_STATUS getTileStatus(int row, int col) {
        return board[row][col].status;
    }

    public static int getNumMines() {
        return NUM_MINES;
    }

    public void changeTileStatus(int row, int col) {
        board[row][col].status = Spot.TILE_STATUS.EXPOSED;
        current_score += board[row][col].tile_score;
    }

    public Spot.TILE_TYPE getTileType(int row, int col) {
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
                if (getTileType(i, j) == Spot.TILE_TYPE.MINE) {
                    continue;
                }

                Spot.TILE_TYPE mine = Spot.TILE_TYPE.MINE;
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

    public void clickedTileStatus(int row, int col, boolean rightClick) {
        // First play is always safe
        if (firstPlay) {
            scatterMines(row,col);
            countNNN();
            firstPlay = false;
            printBoard();
            printTileScores();
        }

        if (rightClick) {
            if (getTileStatus(row,col) == Spot.TILE_STATUS.NOT_EXPOSED) {
                board[row][col].status = Spot.TILE_STATUS.CLOSED;
                System.out.println("Tile at row " + row + " col " + col + " was marked");
            } else if (getTileStatus(row,col) == Spot.TILE_STATUS.CLOSED) {
                board[row][col].status = Spot.TILE_STATUS.NOT_EXPOSED;
            }
        } else if (getTileStatus(row, col) == Spot.TILE_STATUS.NOT_EXPOSED) {
            if (getTileType(row, col) == Spot.TILE_TYPE.GOLD) {
                changeTileStatus(row, col);
                if (getTileScore(row,col) == 40) {
                    revealSurrounding(row,col);
                }
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
                board[row][col].status = Spot.TILE_STATUS.EXPOSED;
            }
        }
    }

    // Will reveal 3x3 area around tiles with 0 mines near them recursively
    private void revealSurrounding(int row, int col) {
        if (visited == null) {
            visited = new boolean[ROWS][COLS];
        }

        for (int deltaRow = -1; deltaRow <= 1; deltaRow++) {
            for (int deltaCol = -1; deltaCol <= 1; deltaCol++) {
                int newRow = row + deltaRow;
                int newCol = col + deltaCol;

                if (newRow >= 0 && newRow < ROWS && newCol >= 0 && newCol < COLS) {
                    if (!visited[newRow][newCol]) {
                        changeTileStatus(newRow, newCol);
                        visited[newRow][newCol] = true;

                        if (getTileScore(newRow, newCol) == 40) {
                            revealSurrounding(newRow, newCol);
                        }
                    }
                }
            }
        }
    }

    private static class CellData {
        private Spot.TILE_STATUS status;
        private Spot.TILE_TYPE type;
        private int tile_score;
    }
}
