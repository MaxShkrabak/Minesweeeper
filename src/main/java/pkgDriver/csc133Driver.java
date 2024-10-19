package pkgDriver;

import pkgSlRenderer.MSPolygonRenderer;
import pkgSlRenderer.MSRenderEngine;

import static pkgDriver.MSSpot.*;
import pkgSlUtils.MSWindowManager;

public class csc133Driver {

    public static void main(String[] my_args) {
        MSRenderEngine my_re = new MSPolygonRenderer();
        MSWindowManager.get().initGLFWWindow(WIN_WIDTH, WIN_HEIGHT, "CSUS CSC133");
        my_re.initOpenGL(MSWindowManager.get());

        final int FRAME_DELAY = 700, NUM_ROWS = 30, NUM_COLS = 30;
        my_re.render(FRAME_DELAY, NUM_ROWS, NUM_COLS);
    }  //  public static void main(String[] my_args)

}  // public class csc133Driver(...)
