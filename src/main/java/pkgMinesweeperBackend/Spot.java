package pkgMinesweeperBackend;

public class Spot {
    public static String WINDOW_TITLE = "CSC 133: Minesweeper";
    // Use LOWER length the more rows and cols being generated
    public static int POLY_OFFSET = 2, POLYGON_LENGTH = 40, POLY_PADDING = 2;
    public static int NUM_POLY_ROWS = 16, NUM_POLY_COLS = 16;

    public static final int TOP_UI_HEIGHT = 60;

    public enum TILE_STATUS {NOT_EXPOSED, EXPOSED, CLOSED};
    public enum TILE_TYPE {MINE,GOLD}

    public static int WIN_WIDTH =
            2*POLY_OFFSET + (NUM_POLY_COLS-1)*POLY_PADDING + NUM_POLY_COLS*POLYGON_LENGTH;
    public static int WIN_HEIGHT =
            TOP_UI_HEIGHT +
            2*POLY_OFFSET + (NUM_POLY_ROWS-1)*POLY_PADDING + NUM_POLY_ROWS*POLYGON_LENGTH;
    public static final float FRUSTUM_LEFT = 0.0f,   FRUSTUM_RIGHT = (float)WIN_WIDTH,
            FRUSTUM_BOTTOM = 0.0f, FRUSTUM_TOP = (float)WIN_HEIGHT,
            Z_NEAR = 0.0f, Z_FAR = 100.0f;

}
