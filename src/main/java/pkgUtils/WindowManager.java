package pkgUtils;

import org.lwjgl.glfw.GLFWErrorCallback;

import static org.lwjgl.glfw.GLFW.*;

public class WindowManager {

    private static long win_id = 0;
    private static WindowManager my_window = null;

    private WindowManager() {}

    public static WindowManager get() {
        if (my_window == null) {
            my_window = new WindowManager();
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

    public long initGLFWWindow(int w, int h, String label) {
        String gameIcon = "assets/icon/Mine.png";
        GLFWErrorCallback.createPrint(System.err).set();

        if (!glfwInit()) {
            throw new IllegalStateException("Could not initialize GLFW");
        }

        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_TRUE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
        glfwWindowHint(GLFW_MAXIMIZED, GLFW_FALSE);

        win_id = glfwCreateWindow(w,h,label,0, 0);

        if (win_id == 0) {
            throw new RuntimeException("Failed to create the GLFW window");
        }

        WindowIcon.setIcon(win_id, gameIcon);

        glfwMakeContextCurrent(win_id);
        glfwSetMouseButtonCallback(win_id, MouseListener::mouseButtonCallback);
        glfwSetCursorPosCallback(win_id, MouseListener::mousePosCallback);

        return win_id;
    }

    public void updateContextToThis() {
        glfwMakeContextCurrent(win_id);
    }
}
