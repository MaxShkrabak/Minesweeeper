package pkgSlRenderer;

import pkgSlUtils.slWindowManager;

import java.util.Random;

import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.opengl.GL11C.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11C.glClear;

public class slRenderEngine {

    private final int NUM_RGBA = 4;
    private final int NUM_3D_COORDS = 3;
    private final int TRIANGLES_PER_CIRCLE = 40;
    private final float C_RADIUS = 0.05f;
    private final int MAX_CIRCLES = 1;
    private final int UPDATE_INTERVAL = 30;

    private float[][] rand_colors;
    private float[][] rand_coords;

    private slWindowManager my_wm;
    Random rand = new Random();

    public void initOpenGL(slWindowManager wm) {
        my_wm = wm;
        my_wm.updateContextToThis();

        while (!my_wm.isGlfwWindowClosed()) {
            glfwPollEvents();
            glClear(GL_COLOR_BUFFER_BIT);

            my_wm.swapBuffers();
        }
    }

    private void generateCircleSegmentVertices(float[] vertices, float radius, float[] center, float na, float na2, int segments) {

    }

    private void updateRandVertices() {

    }

    public void render() {

    }

}
