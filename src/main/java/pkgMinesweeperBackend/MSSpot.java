package pkgMinesweeperBackend;

public class MSSpot {
    public static String WINDOW_TITLE = "CSC 133: Minesweeper";
    // Use LOWER length the more rows and cols being generated
    public static int POLY_OFFSET = 4, POLYGON_LENGTH = 80, POLY_PADDING = 4;
    public static int NUM_POLY_ROWS = 9, NUM_POLY_COLS = 7;

    public enum TILE_STATUS {NOT_EXPOSED, EXPOSED};
    public enum TILE_TYPE {MINE,GOLD}

    public static int WIN_WIDTH =
            2*POLY_OFFSET + (NUM_POLY_COLS-1)*POLY_PADDING + NUM_POLY_COLS*POLYGON_LENGTH;
    public static int WIN_HEIGHT =
            2*POLY_OFFSET + (NUM_POLY_ROWS-1)*POLY_PADDING + NUM_POLY_ROWS*POLYGON_LENGTH;
    public static final float FRUSTUM_LEFT = 0.0f,   FRUSTUM_RIGHT = (float)WIN_WIDTH,
            FRUSTUM_BOTTOM = 0.0f, FRUSTUM_TOP = (float)WIN_HEIGHT,
            Z_NEAR = 0.0f, Z_FAR = 100.0f;
}
