package pkgSlRenderer;

import pkgSlUtils.slWindowManager;

import java.util.Random;

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

    }

    private void generateCircleSegmentVertices(float[] vertices, float radius, float[] center, float na, float na2, int segments) {

    }

    private void updateRandVertices() {

    }

    public void render() {

    }

}
