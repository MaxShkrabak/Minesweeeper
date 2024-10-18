package pkgSlRenderer;

import java.util.Random;

import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11C.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11C.glClear;

public class SLPolygonRenderer extends SLRenderEngine {
    private static final float DEFAULT_POLYGON_RADIUS = 0.5f;
    private static final int MAX_POLYGON_SIDES = 20;
    private static final int DEFAULT_POLYGON_SIDES = 3;
    private static final int DEF_TIME_DELAY = 500;
    private static final int DEF_ROWS = 30, DEF_COLS = 30;

    Random rand = new Random();
    private float polygonRadius;
    private int currNumSides;

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
    public void render(int frameDelay, int cols, int rows) {
        while (!my_wm.isGlfwWindowClosed()) {
            glfwPollEvents();
            glClear(GL_COLOR_BUFFER_BIT);

            // If currSides is more than maxSides, reset back to 3
            if (currNumSides > MAX_POLYGON_SIDES) {
                currNumSides = DEFAULT_POLYGON_SIDES;
            }

            renderPolygons(cols, rows);
            my_wm.swapBuffers();
            if (frameDelay != 0) {
                try{
                    Thread.sleep(frameDelay);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
        my_wm.destroyGlfwWindow();
    }

    // Renders as many rows and cols as radius allows to fit in window
    @Override
    public void render(float polygonRadius) {
        int[] windowSize = my_wm.getWindowSize();
        int width = windowSize[0];
        int height = windowSize[1];

        int cols = (int) (width / (polygonRadius * 2));
        int rows = (int) (height / (polygonRadius * 2));

        render(DEF_TIME_DELAY, cols, rows);
    }

    // Default Render Method
    @Override
    public void render(){
        render(DEF_TIME_DELAY, DEF_COLS, DEF_ROWS);
    }

    private void renderPolygons(int rows, int cols) {
        final float begin_angle = 0.0f, end_angle = (float) (2.0f * Math.PI) / currNumSides;
        int[] windowSize = my_wm.getWindowSize();
        int width = windowSize[0], height = windowSize[1];

        glClear(GL_COLOR_BUFFER_BIT);

        setMaxSides(++currNumSides); // Increment number of sides
        glColor4f(rand.nextFloat(), rand.nextFloat(), rand.nextFloat(), 1.0f); // Random polygon color

        System.out.println(currNumSides); // Testing purposes

        float w_c = (float) width / cols; // Width for each column
        float h_r = (float) height / rows; // Height for each row

        float maxDimension = Math.max(cols, rows); // Finds larger of two arguments
        setRadius(Math.min(width, height) / (maxDimension * 2)); // New scaled radius

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                // Center position for each polygon
                float[] center = {
                        (col + DEFAULT_POLYGON_RADIUS) * w_c,  // Center x-coord
                        (row + DEFAULT_POLYGON_RADIUS) * h_r   // Center y-coord
                };

                glBegin(GL_TRIANGLE_FAN);
                generatePolygonVertices(polygonRadius, center, begin_angle, end_angle, currNumSides, width, height);
                glEnd();
            }
        }
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

    private void generatePolygonVertices(float radius, float[] center, float theta, float delT, int sides, int width, int height) {
        for (int i = 0; i < sides; i++) {
            float x = (float) Math.cos(theta) * radius + center[0];
            float y = (float) Math.sin(theta) * radius + center[1];

            glVertex3f(x / ((float) width / 2) - 1, y / ((float) height / 2) -1, 0.0f);
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
