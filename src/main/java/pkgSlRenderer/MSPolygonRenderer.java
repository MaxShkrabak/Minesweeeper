package pkgSlRenderer;

import pkgSlUtils.MSKeyListener;

import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11C.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11C.glClear;

public class MSPolygonRenderer extends MSRenderEngine {
    private float polygonRadius;
    private int currNumSides;
    private MSPingPong myPingPong;


    // Constructor
    public MSPolygonRenderer() {
        this.polygonRadius = DEFAULT_POLYGON_RADIUS;    // Starts with 0.5f radius
        this.currNumSides = DEFAULT_POLYGON_SIDES;      // Starts with 3 sides
    }

    public void setRadius(float radius) {
        this.polygonRadius = radius;
    }

    @Override
    public void render(int frameDelay, int cols, int rows) {
        this.myPingPong = new MSPingPong(rows,cols);

        // Gets the passed in frame delay and sends it to keyListener
        MSKeyListener.initFrameDelay(frameDelay);

        while (!my_wm.isGlfwWindowClosed()) {
            glfwPollEvents();
            glClear(GL_COLOR_BUFFER_BIT);

            // If currSides is more than maxSides, reset back to 3
            if (currNumSides > MAX_POLYGON_SIDES) {
                currNumSides = DEFAULT_POLYGON_SIDES;
            }

            renderPolygons(cols, rows);

            myPingPong.nextArray();

            my_wm.swapBuffers();

            frameDelay = MSKeyListener.getFrameDelay();

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

    // Method used to render 'grid' of polygons based on rows and cols
    private void renderPolygons(int rows, int cols) {
        int[] windowSize = my_wm.getWindowSize();
        int width = windowSize[0], height = windowSize[1];

        glClear(GL_COLOR_BUFFER_BIT);

        float w_c = (float) width / cols; // Width for each column
        float h_r = (float) height / rows; // Height for each row

        float maxDimension = Math.max(cols, rows); // Finds larger of two arguments
        setRadius(Math.min(width, height) / (maxDimension * 2)); // New scaled radius

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {

                boolean isAlive = myPingPong.getLive(row, col);

                // Colors for live and dead
                if (isAlive) {
                    glColor3f(0.6f,1.0f,0.6f); // Alive color
                } else {
                    glColor3f(0.0f,0.0f,0.0f);    // Dead color
                }

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
}
