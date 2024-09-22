package pkgSlUtils;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11C.glClearColor;

public class slWindowManager {

    private long glfw_win;
    private static slWindowManager my_window;

    public int getCurrentWindowSize() {
        return 0;
    }

    public static slWindowManager get() {
        if (my_window == null) {
            my_window = new slWindowManager();
        }
        return my_window;
    }

    public void destroyGlfwWindow() {

    }

    public void swapBuffers() {
        glfwSwapBuffers(glfw_win);
    }

    public boolean isGlfwWindowClosed() {
        return glfwWindowShouldClose(glfw_win);
    }

    public long initGLFWWindow(int h, int w, String title) {
        GLFWErrorCallback.createPrint(System.err).set();

        if (!glfwInit()) {
            throw new IllegalStateException("Could not initialize GLFW");
        }

        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_TRUE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
        glfwWindowHint(GLFW_MAXIMIZED, GLFW_FALSE);

        glfw_win = glfwCreateWindow(w,h,title,0, 0);

        if (glfw_win == 0) {
            throw new RuntimeException("Failed to create the GLFW window");
        }

        glfwMakeContextCurrent(glfw_win);
        updateContextToThis();

        GL.createCapabilities();
        float CC_RED = 0.0f, CC_GREEN = 0.0f, CC_BLUE = 1.0f, CC_ALPHA = 1.0f;
        glClearColor(CC_RED, CC_GREEN, CC_BLUE, CC_ALPHA);

        return glfw_win;
    }

    public int[] getWindowSize() {
        return new int[0];
    }

    public void updateContextToThis() {
        glfwMakeContextCurrent(glfw_win);
    }

}
