package pkgSlRenderer;

import org.lwjgl.opengl.GL;
import pkgSlUtils.MSWindowManager;

import java.util.Random;

import static org.lwjgl.opengl.GL11.glColor4f;
import static org.lwjgl.opengl.GL11.glVertex3f;
import static org.lwjgl.opengl.GL11C.glClearColor;

public abstract class MSRenderEngine {
    protected static final float DEFAULT_POLYGON_RADIUS = 0.5f;
    protected static final int MAX_POLYGON_SIDES = 4;
    protected static final int DEFAULT_POLYGON_SIDES = 4;
    protected static final int DEF_TIME_DELAY = 500;
    protected static final int DEF_ROWS = 30, DEF_COLS = 30;

    Random rand = new Random();
    protected MSWindowManager my_wm;

    public void initOpenGL(MSWindowManager wm) {
        my_wm = wm;
        my_wm.updateContextToThis();

        GL.createCapabilities();
        float CC_RED = 0.0f, CC_GREEN = 0.0f, CC_BLUE = 0.0f, CC_ALPHA = 1.0f; // Window background color (BLACK)
        glClearColor(CC_RED, CC_GREEN, CC_BLUE, CC_ALPHA);
    }

    // Method that can be used in other derived classes (ex. generating hexagons)
    protected void generatePolygon(int sides, float radius, float[] center) {
        float theta = 0.0f, delT = (float) (2.0f * Math.PI) / sides;

        for (int i = 0; i < sides; i++) {
            float x = (float) Math.cos(theta) * radius + center[0];
            float y = (float) Math.sin(theta) * radius + center[1];

            glVertex3f(x, y, 0.0f);
            theta += delT;
        }
    }

    protected void randomColor() {
        glColor4f(rand.nextFloat(), rand.nextFloat(), rand.nextFloat(), 1.0f); // Random polygon color
    }

    public abstract void render(int frameDelay, int rows, int cols);
    public abstract void render(float radius);
    public abstract void render();
}