package pkgDriver;

import pkgSlRenderer.MSPolygonRenderer;
import pkgSlRenderer.MSRenderEngine;

import static pkgDriver.MSSpot.*;

import pkgSlUtils.MSWindowManager;

public class csc133Driver {

    public static void main(String[] my_args) {
        final int FRAME_DELAY = 300;
        MSRenderEngine my_re = new MSPolygonRenderer();
        MSWindowManager.get().initGLFWWindow(WIN_WIDTH, WIN_HEIGHT, WINDOW_TITLE);
        my_re.initOpenGL(MSWindowManager.get(), FRAME_DELAY, NUM_POLY_ROWS, NUM_POLY_COLS);

        my_re.render();
    }  //  public static void main(String[] my_args)

}  // public class csc133Driver(...)
