package pkgDriver;

import pkgSlRenderer.slRenderEngine;
import static pkgDriver.slSpot.*;
import pkgSlUtils.slWindowManager;

public class csc133Driver {

    public static void main(String[] my_args) {
        slRenderEngine my_re = new slRenderEngine();
        slWindowManager.get().initGLFWWindow(WIN_WIDTH, WIN_HEIGHT, "CSUS CSC133");
        my_re.initOpenGL(slWindowManager.get());

        my_re.render();
    }  //  public static void main(String[] my_args)

}  // public class csc133Driver(...)
