package pkgSlUtils;

import org.lwjgl.glfw.GLFWErrorCallback;

import static org.lwjgl.glfw.GLFW.*;

public class slWindowManager {

    private static long win_id = 0;
    private static slWindowManager my_window = null;

    private slWindowManager() {}

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
        if (win_id != 0) {
            glfwDestroyWindow(win_id);
            win_id = 0;
        }
    }

    public void swapBuffers() {
        glfwSwapBuffers(win_id);
    }

    public boolean isGlfwWindowClosed() {
        return glfwWindowShouldClose(win_id);
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

        win_id = glfwCreateWindow(w,h,title,0, 0);

        if (win_id == 0) {
            throw new RuntimeException("Failed to create the GLFW window");
        }

        glfwMakeContextCurrent(win_id);

        return win_id;
    }

    public int[] getWindowSize() {
        return new int[0];
    }

    public void updateContextToThis() {
        glfwMakeContextCurrent(win_id);
    }
}
