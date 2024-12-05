package pkgDriver;

import pkgMSRenderer.MSTileRenderer;
import pkgMSRenderer.MSRenderEngine;

import static pkgMinesweeperBackend.MSSpot.*;

import pkgSlUtils.MSWindowManager;

public class csc133Driver {

    public static void main(String[] my_args) {
        MSRenderEngine my_re = new MSTileRenderer();
        MSWindowManager.get().initGLFWWindow(WIN_WIDTH, WIN_HEIGHT, WINDOW_TITLE);
        my_re.initOpenGL(MSWindowManager.get(), NUM_POLY_ROWS, NUM_POLY_COLS);

        my_re.render();
    }  //  public static void main(String[] my_args)
}  // public class csc133Driver(...)
