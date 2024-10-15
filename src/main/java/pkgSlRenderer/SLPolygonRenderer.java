package pkgSlRenderer;

import java.util.Random;

public class SLPolygonRenderer extends SLRenderEngine {
    private static final float DEFAULT_POLYGON_RADIUS = 0.05f;
    private static final int MAX_POLYGON_SIDES = 20;
    private static final int DEFAULT_POLYGON_SIDES = 3;
    private static final int DEF_TIME_DELAY = 1000;      // Not sure if needed yet

    Random rand = new Random();
    private float polygonRadius;
    private int currNumSides;

    // Constructor
    public SLPolygonRenderer() {
        this.polygonRadius = DEFAULT_POLYGON_RADIUS;    // Starts with 0.05f radius
        this.currNumSides = DEFAULT_POLYGON_SIDES;      // Starts with 3 sides
    }

    // TODO: Create all required setters


    @Override
    protected void renderPolygons(int frameDelay, int rows, int cols) {

    }


    /*
    // Method used to generate random colors and coords for circles
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
     */

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


}
