package pkgSlRenderer;

import java.util.Random;

import static org.lwjgl.glfw.GLFW.glfwGetTime;
import static org.lwjgl.opengl.GL11.*;

public class SLPolygonRenderer extends SLRenderEngine {
    private static final float DEFAULT_POLYGON_RADIUS = 0.10f;
    private static final int MAX_POLYGON_SIDES = 20;
    private static final int DEFAULT_POLYGON_SIDES = 3;
    private static final int DEF_TIME_DELAY = 1000;      // Not sure if needed yet

    Random rand = new Random();
    private float polygonRadius;
    private int currNumSides;
    private double lastTime = 0;

    // Constructor
    public SLPolygonRenderer() {
        this.polygonRadius = DEFAULT_POLYGON_RADIUS;    // Starts with 0.05f radius
        this.currNumSides = DEFAULT_POLYGON_SIDES;      // Starts with 3 sides
    }

    public void setRadius(float radius) {
        this.polygonRadius = radius;
    }

    public void setMaxSides(int sides) {
        this.currNumSides = sides;
    }

    @Override
    protected void renderPolygons(int frameDelay, int rows, int cols) {
        double currTime = glfwGetTime();
        final float begin_angle = 0.0f, end_angle = (float) (2.0f * Math.PI) / currNumSides;
        float[] center = {0.0f, 0.0f}; // For now center of screen

        glClear(GL_COLOR_BUFFER_BIT);
        // Increase sides after each render
        if (currTime - lastTime > (DEF_TIME_DELAY / 1000)) {
            lastTime = currTime;
            setMaxSides(++currNumSides);

            // If currSides is more than maxSides, reset back to 3
            if (currNumSides > MAX_POLYGON_SIDES) {
                currNumSides = DEFAULT_POLYGON_SIDES;
            }
        }

        glColor4f(rand.nextFloat(), rand.nextFloat(), rand.nextFloat(), 1.0f); // Random colors

        glBegin(GL_TRIANGLE_FAN);
        generatePolygonVertices(polygonRadius, center, begin_angle, end_angle, currNumSides);
        glEnd();
    }

    /*
    // Method to Render Circle(s)
    public void render(int frameDelay, int rows, int cols) {
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
     */

    private void generatePolygonVertices(float radius, float[] center, float theta, float delT, int sides) {
        for (int i = 0; i < sides; i++) {
            float x = (float) Math.cos(theta) * radius + center[0];
            float y = (float) Math.sin(theta) * radius + center[1];

            glVertex3f(x, y, 0.0f);
            theta += delT;
        }
    }

    /*
    private void generateCircleSegmentVertices(float radius, float[] center, float theta, float delT, int maxTriangles) {
        for (int num_tri = 0; num_tri <= maxTriangles; ++num_tri) {
            // Calculate vertices
            float x = (float) Math.cos(theta) * radius + center[0];
            float y = (float) Math.sin(theta) * radius + center[1];

            glVertex3f(x, y, 0.0f);
            theta += delT;
        }
    }
     */



}
