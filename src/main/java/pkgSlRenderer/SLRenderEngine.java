package pkgSlRenderer;

import org.lwjgl.opengl.GL;
import pkgSlUtils.SLWindowManager;

import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.opengl.GL11C.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11C.glClear;
import static org.lwjgl.opengl.GL11C.glClearColor;

public abstract class SLRenderEngine {

    private SLWindowManager my_wm;

    public void initOpenGL(SLWindowManager wm) {
        my_wm = wm;
        my_wm.updateContextToThis();

        GL.createCapabilities();
        float CC_RED = 0.0f, CC_GREEN = 0.0f, CC_BLUE = 0.0f, CC_ALPHA = 1.0f; // Window background color (BLACK)
        glClearColor(CC_RED, CC_GREEN, CC_BLUE, CC_ALPHA);
    }

    // Method to Render Circle(s)
    public void render(int frameDelay, int rows, int cols) {
        while (!my_wm.isGlfwWindowClosed()) {
            glfwPollEvents();
            glClear(GL_COLOR_BUFFER_BIT);

            renderPolygons(frameDelay, rows, cols);

            my_wm.swapBuffers();
        }
        my_wm.destroyGlfwWindow();
    }

    protected abstract void renderPolygons(int frameDelay, int rows, int cols);
}