package pkgSlRenderer;

import org.lwjgl.opengl.GL;
import pkgSlUtils.slWindowManager;

import java.util.Random;

import static org.lwjgl.glfw.GLFW.glfwGetTime;
import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11C.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11C.glClear;
import static org.lwjgl.opengl.GL11C.glClearColor;

public class slRenderEngine {
    private final int NUM_RGBA = 4;
    private final int NUM_3D_COORDS = 3;
    private final int TRIANGLES_PER_CIRCLE = 40;
    private final float C_RADIUS = 0.05f;
    private final int MAX_CIRCLES = 5000;
    private final int UPDATE_INTERVAL = 500;

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
        rand_coords = new float[MAX_CIRCLES][NUM_3D_COORDS];
    }

    private void generateCircleSegmentVertices(float radius, float[] center, float theta, float delT, int maxTriangles) {
        glVertex3f(center[0], center[1], 0.0f); // Center point
        for (int num_tri = 0; num_tri <= maxTriangles; ++num_tri) {
            // Calculates vertices
            float x = (float) Math.cos(theta) * radius + center[0];
            float y = (float) Math.sin(theta) * radius + center[1];

            glVertex3f(x, y, 0.0f);

            theta += delT;
        }
    }

    // Function used to generate random colors and coords for circles
    private void updateRandVertices() {
        for (int i = 0; i < MAX_CIRCLES; i++) {
            // Random colors
            rand_colors[i][0] = rand.nextFloat(); // RED
            rand_colors[i][1] = rand.nextFloat(); // BLUE
            rand_colors[i][2] = rand.nextFloat(); // GREEN
            rand_colors[i][3] = 1.0f; // ALPHA

            // Random coords for 2D space
            rand_coords[i][0] = rand.nextFloat() * (2.0f - 2 * C_RADIUS) - 1.0f + C_RADIUS; // Random X
            rand_coords[i][1] = rand.nextFloat() * (2.0f - 2 * C_RADIUS) - 1.0f + C_RADIUS; // Random Y
            rand_coords[i][2] = 0.0f; // Z is always 0
        }
    }

    public void render() {
        double lastUpdate = glfwGetTime();

        final float begin_angle = 0.0f, end_angle = (float) (2.0f * Math.PI) / TRIANGLES_PER_CIRCLE;

        updateRandVertices();

        while (!my_wm.isGlfwWindowClosed()) {
            double currTime = glfwGetTime();
            glfwPollEvents();
            glClear(GL_COLOR_BUFFER_BIT);

            // Render new vertices and colors
            if (currTime - lastUpdate >= (UPDATE_INTERVAL / 1000.0)) {
                lastUpdate = currTime;
                updateRandVertices();
            }

            for (int i = 0; i < MAX_CIRCLES; i++) {
                // Random color for the circle(s)
                glColor4f(rand_colors[i][0], rand_colors[i][1], rand_colors[i][2], rand_colors[i][3]);

                // Generate circle(s)
                glBegin(GL_TRIANGLE_FAN);
                generateCircleSegmentVertices(C_RADIUS, rand_coords[i], begin_angle, end_angle, TRIANGLES_PER_CIRCLE);
                glEnd();
            }

            my_wm.swapBuffers();
        }
        my_wm.destroyGlfwWindow();
    }
}