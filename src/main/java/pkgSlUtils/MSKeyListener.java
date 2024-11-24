package pkgSlUtils;

import pkgMinesweeperBackend.MSPingPong;

import static org.lwjgl.glfw.GLFW.*;

public class MSKeyListener {
    private static MSKeyListener my_key = null;
    private final boolean[] keyPressed = new boolean[400];
    private static int frameDelay = 0;
    private static MSPingPong myPingPong;

    // Private constructor
    private MSKeyListener() {}

    public static MSKeyListener get() {
        if (my_key == null) {
            my_key = new MSKeyListener();
        }
        return my_key;
    }

    // Helper method to set frame delay here to what ever was passed into render
    public static void initFrameDelay(int delay) {
        frameDelay = delay;
    }

    public static void setPPInstance(MSPingPong my_pingPong) {
        myPingPong = my_pingPong;
    }

    public static void keyCallback(long my_window, int key, int scancode, int action, int modifier_key) {
        if (action == GLFW_PRESS) {
            get().keyPressed[key] = true;
        } else if (action == GLFW_RELEASE){
            get().keyPressed[key] = false;
        }

        if (MSKeyListener.isKeyPressed(GLFW_KEY_I)) {
            frameDelay += 500;
            System.out.println("Delay increased - Delay is now: " + frameDelay);
            resetKeypressEvent(GLFW_KEY_I);
        }
        if (MSKeyListener.isKeyPressed(GLFW_KEY_D)) {
            frameDelay -= 500;
            if (frameDelay < 0) {
                frameDelay = 0;
            }
            System.out.println("Delay decreased - Delay is now: " + frameDelay);
            resetKeypressEvent(GLFW_KEY_D);
        }
        if (MSKeyListener.isKeyPressed(GLFW_KEY_R)) {
            myPingPong.resetBoard();
            System.out.println("The 'Game of Life' board was reset");
        }
        if (MSKeyListener.isKeyPressed(GLFW_KEY_ESCAPE)) {
            glfwSetWindowShouldClose(my_window,true); // Terminates application
            System.out.println("Window closed - Good bye!");
            resetKeypressEvent(GLFW_KEY_ESCAPE);
        }
    }

    public static boolean isKeyPressed(int keyCode){
        if (keyCode < get().keyPressed.length) {
            return get().keyPressed[keyCode];
        } else {
            return false;
        }
    }

    // Call this function to receive one event for repeated presses:
    public static void resetKeypressEvent(int keyCode) {
        if (my_key != null && keyCode < get().keyPressed.length) {
            my_key.keyPressed[keyCode] = false;
        }
    }

    public static int getFrameDelay() {
        return frameDelay;
    }

    // Method used to display all possible commands
    public static void commandMenu() {
        System.out.println("<ESC> --> Terminate the application");
        System.out.println("'i' --> Increase Frame delay by 500 ms");
        System.out.println("'d' --> Decrease Frame delay by 500 ms");
        System.out.println("'r' --> Reset board");

        System.out.println("----------------------------------------");
    }
}
