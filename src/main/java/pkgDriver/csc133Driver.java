package pkgDriver;

import pkgSlRenderer.SLPolygonRenderer;
import pkgSlRenderer.SLRenderEngine;

import static pkgDriver.SLSpot.*;
import pkgSlUtils.SLWindowManager;

public class csc133Driver {

    public static void main(String[] my_args) {
        SLRenderEngine my_re = new SLPolygonRenderer();
        SLWindowManager.get().initGLFWWindow(WIN_WIDTH, WIN_HEIGHT, "CSUS CSC133");
        my_re.initOpenGL(SLWindowManager.get());

        final int FRAME_DELAY = 700, NUM_ROWS = 30, NUM_COLS = 30;
        my_re.render(30.0f);
    }  //  public static void main(String[] my_args)

}  // public class csc133Driver(...)
