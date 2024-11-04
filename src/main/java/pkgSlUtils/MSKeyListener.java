package pkgSlUtils;

import static org.lwjgl.glfw.GLFW.*;

public class MSKeyListener {
    private static MSKeyListener my_key = null;
    private final boolean[] keyPressed = new boolean[400];
    public static int frameDelay = 0;

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

    public static void keyCallback(long my_window, int key, int scancode, int action, int modifier_key) {
        if (action == GLFW_PRESS) {
            get().keyPressed[key] = true;
        } else if (action == GLFW_RELEASE){
            get().keyPressed[key] = false;
        }

        if (MSKeyListener.isKeyPressed(GLFW_KEY_RIGHT_SHIFT)) {
            System.out.println("Shift is pressed");
            MSKeyListener.resetKeypressEvent(GLFW_KEY_RIGHT_SHIFT);
        }
        if (MSKeyListener.isKeyPressed(GLFW_KEY_I)) {
            frameDelay += 500;
            System.out.println("Delay increased by 500 new delay is now: " + frameDelay);

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
}
