package pkgSlRenderer;

import org.lwjgl.opengl.GL;
import pkgSlUtils.slWindowManager;

import java.util.Random;

import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11C.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11C.glClear;
import static org.lwjgl.opengl.GL11C.glClearColor;

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

        GL.createCapabilities();
        float CC_RED = 0.0f, CC_GREEN = 0.0f, CC_BLUE = 1.0f, CC_ALPHA = 1.0f;
        glClearColor(CC_RED, CC_GREEN, CC_BLUE, CC_ALPHA);

        rand_colors = new float[MAX_CIRCLES][NUM_RGBA];

        for (int i = 0; i < MAX_CIRCLES; i++) {
            rand_colors[i][0] = rand.nextFloat();
            rand_colors[i][1] = rand.nextFloat();
            rand_colors[i][2] = rand.nextFloat();
            rand_colors[i][3] = 1.0f;
        }
    }

    private void generateCircleSegmentVertices(float[] vertices, float radius, float[] center, float na, float na2, int segments) {

    }

    private void updateRandVertices() {

    }

    public void render() {

        final float begin_angle = 0.0f, end_angle = (float) (2.0f * Math.PI);
        while (!my_wm.isGlfwWindowClosed()) {
            glfwPollEvents();
            glClear(GL_COLOR_BUFFER_BIT);
            glBegin(GL_TRIANGLES);

            glColor4f(rand_colors[0][0],rand_colors[0][1], rand_colors[0][2],rand_colors[0][3]);
            glVertex3f(0.0f, 0.0f, 0.0f);
            glVertex3f(0.1f, 0.0f, 0.0f);
            glVertex3f(0.0f, 0.1f, 0.0f);

            glEnd();
            my_wm.swapBuffers();
        }
        my_wm.destroyGlfwWindow();
    }

}
