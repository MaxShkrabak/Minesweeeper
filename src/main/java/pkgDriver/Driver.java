package pkgDriver;

import pkgRenderer.TileRenderer;
import pkgRenderer.RenderEngine;

import static pkgMinesweeperBackend.Spot.*;

import pkgUtils.WindowManager;

public class Driver {

    public static void main(String[] my_args) {
        RenderEngine my_re = new TileRenderer();
        WindowManager.get().initGLFWWindow(WIN_WIDTH, WIN_HEIGHT, WINDOW_TITLE);
        my_re.initOpenGL(WindowManager.get(), NUM_POLY_ROWS, NUM_POLY_COLS);
        System.out.println("Width " + WIN_WIDTH + " Height " + WIN_HEIGHT);
        my_re.render();
    }  //  public static void main(String[] my_args)
}
