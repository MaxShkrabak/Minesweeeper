package pkgSlRenderer;

import org.lwjgl.opengl.GL;
import pkgSlUtils.SLWindowManager;

import static org.lwjgl.opengl.GL11C.glClearColor;

public abstract class SLRenderEngine {

    protected SLWindowManager my_wm;

    public void initOpenGL(SLWindowManager wm) {
        my_wm = wm;
        my_wm.updateContextToThis();

        GL.createCapabilities();
        float CC_RED = 0.0f, CC_GREEN = 0.0f, CC_BLUE = 0.0f, CC_ALPHA = 1.0f; // Window background color (BLACK)
        glClearColor(CC_RED, CC_GREEN, CC_BLUE, CC_ALPHA);
    }

    public abstract void render(int frameDelay, int rows, int cols);
    public abstract void render(float radius);
    public abstract void render();

}