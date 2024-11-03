package pkgSlRenderer;

import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11C.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11C.glClear;

public class MSPolygonRenderer extends MSRenderEngine {
    private float polygonRadius;
    private int currNumSides;
    private int numbPolygons;

    // Constructor
    public MSPolygonRenderer() {
        this.polygonRadius = DEFAULT_POLYGON_RADIUS;    // Starts with 0.5f radius
        this.currNumSides = DEFAULT_POLYGON_SIDES;      // Starts with 3 sides
    }

    public void setRadius(float radius) {
        this.polygonRadius = radius;
    }

    public void setMaxSides(int sides) {
        this.currNumSides = sides;
    }

    public void setNumPolygons(int numPolygons) {
        this.numbPolygons = numPolygons;
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
            if (frameDelay > 0) {
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

    // Render randomly placed polygons
    @Override
    public void render(float radius, int sides, int numPolygons) {
        setRadius(radius);
        setMaxSides(sides);
        setNumPolygons(numPolygons);

        while (!my_wm.isGlfwWindowClosed()) {
            glfwPollEvents();
            glClear(GL_COLOR_BUFFER_BIT);

            renderRandomPolygons();

            my_wm.swapBuffers();
            if (DEF_TIME_DELAY > 0) {
                try{
                    Thread.sleep(DEF_TIME_DELAY);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
        my_wm.destroyGlfwWindow();
    }

    // Method used to render 'grid' of polygons based on rows and cols
    private void renderPolygons(int rows, int cols) {
        int[] windowSize = my_wm.getWindowSize();
        int width = windowSize[0], height = windowSize[1];

        glClear(GL_COLOR_BUFFER_BIT);

        randomColor(); // Each frame will have newly colorized polygons

        //System.out.println(currNumSides); // Testing purposes

        float w_c = (float) width / cols; // Width for each column
        float h_r = (float) height / rows; // Height for each row

        float maxDimension = Math.max(cols, rows); // Finds larger of two arguments
        setRadius(Math.min(width, height) / (maxDimension * 2)); // New scaled radius

        //System.out.println(polygonRadius); // Testing purposes

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                // Center position for each polygon
                float[] center = {
                        (col + DEFAULT_POLYGON_RADIUS) * w_c,  // Center x-coordinates
                        (row + DEFAULT_POLYGON_RADIUS) * h_r   // Center y-coordinates
                };

                glBegin(GL_TRIANGLE_FAN);
                generatePolygon(currNumSides, polygonRadius, center);
                glEnd();
            }
        }
        setMaxSides(++currNumSides); // Increment number of sides
    }

    // Generate vertices for polygon given sides and radius at given center coords
    @Override
    protected void generatePolygon(int sides, float radius, float[] center) {
        int[] window_size = my_wm.getWindowSize();
        int height = window_size[0];
        int width = window_size[1];
        float theta = (float) (Math.PI / 4), delT = (float) (2.0f * Math.PI) / sides;

        for (int i = 0; i < sides; i++) {
            float x = (float) Math.cos(theta) * radius + center[0];
            float y = (float) Math.sin(theta) * radius + center[1];

            glVertex3f(x / ((float) width / 2) - 1, y / ((float) height / 2) -1, 0.0f);
            theta += delT;
        }
    }

    private void renderRandomPolygons() {
        for (int i = 0; i < numbPolygons; i++) {
            // Random shapes 3-sides
            int randomSides = rand.nextInt(currNumSides - 2) + 3;
            float[] center = {
                    rand.nextFloat() * (2.0f - 2 * polygonRadius) - 1.0f + polygonRadius, // Random X
                    rand.nextFloat() * (2.0f - 2 * polygonRadius) - 1.0f + polygonRadius, // Random Y
            };

            randomColor();  // Each polygon will have a random color
            glBegin(GL_TRIANGLE_FAN);
            super.generatePolygon(randomSides, polygonRadius, center);
            glEnd();
        }
    }
}
