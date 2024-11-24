package pkgMSRenderer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL;
import pkgMinesweeperBackend.MSSpot;
import pkgSlUtils.MSWindowManager;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.ARBVertexArrayObject.glBindVertexArray;
import static org.lwjgl.opengl.ARBVertexArrayObject.glGenVertexArrays;
import static org.lwjgl.opengl.GL11C.glClearColor;
import static org.lwjgl.opengl.GL15C.*;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;

public abstract class MSRenderEngine {
    protected static final int FLOAT_PER_SQUARE = 5;
    protected static final int VPT = 4;

    protected int vaoID, vboID;
    protected int frameDelay;
    protected int rows, cols;

    protected MSWindowManager my_wm;
    protected MSShaderObject shaderObj0;
    protected MSTextureObject texObj0;

    public void initOpenGL(MSWindowManager wm, int frameD, int row, int col) {
        my_wm = wm;
        my_wm.updateContextToThis();
        frameDelay = frameD;
        rows = row;
        cols = col;

        GL.createCapabilities();
        float CC_RED = 0.0f, CC_GREEN = 0.2f, CC_BLUE = 0.2f, CC_ALPHA = 1.0f; // Window background color (BLACK)
        glClearColor(CC_RED, CC_GREEN, CC_BLUE, CC_ALPHA);

        float[] mv = generateVertices(rows, cols);

        //for (int i = 0; i < mv.length; i++) { System.out.print(mv[i] + " "); }

        FloatBuffer fb = BufferUtils.createFloatBuffer(mv.length);
        fb.put(mv).flip();

        vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);

        vboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferData(GL_ARRAY_BUFFER, fb, GL_STATIC_DRAW);

        int posStride = 3;
        int textStride = 2;
        int vertexStride = (posStride + textStride) * Float.BYTES;
        int index0 = 0, index1 = 1, startIndex = 0;

        glVertexAttribPointer(index0, posStride, GL_FLOAT, false, vertexStride, startIndex);
        glEnableVertexAttribArray(index0);

        startIndex = 3 * Float.BYTES;
        glVertexAttribPointer(index1, textStride, GL_FLOAT, false, vertexStride, startIndex);
        glEnableVertexAttribArray(index1);

        shaderObj0 = new MSShaderObject("assets/shaders/vs_texture_1.glsl", "assets/shaders/fs_texture_1.glsl");
        shaderObj0.compile_shader();
        shaderObj0.set_shader_program();

        texObj0 = new MSTextureObject("assets/images/MarioWithGun.PNG");
        texObj0.bind_texture();
    }

    // Method to generate tile vertices starting from bottom left of window
    private float[] generateVertices(int rows, int cols) {
        int totalTiles = rows * cols;
        float[] vertices = new float[totalTiles * VPT * FLOAT_PER_SQUARE];

        float padding = MSSpot.POLY_PADDING;
        float offset = MSSpot.POLY_OFFSET;
        float height = MSSpot.WIN_HEIGHT - 2 * offset - (rows - 1) * padding;
        float width = MSSpot.WIN_WIDTH - 2 * offset - (cols - 1) * padding;

        float tileSize = Math.min(height / rows, width / cols);
        float z = 0.0f;

        int index = 0;
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                float x = col * (tileSize + padding) + offset;
                float y = row * (tileSize + padding) + offset;

                // Bottom left
                vertices[index++] = x;
                vertices[index++] = y;
                vertices[index++] = z;
                vertices[index++] = 1.0f;     // Texture x
                vertices[index++] = 1.0f;     // Texture y

                // Bottom right
                vertices[index++] = x + tileSize;
                vertices[index++] = y;
                vertices[index++] = z;
                vertices[index++] = 0.0f;
                vertices[index++] = 1.0f;

                // Top right
                vertices[index++] = x + tileSize;
                vertices[index++] = y + tileSize;
                vertices[index++] = z;
                vertices[index++] = 0.0f;
                vertices[index++] = 0.0f;

                // Top left
                vertices[index++] = x;
                vertices[index++] = y + tileSize;
                vertices[index++] = z;
                vertices[index++] = 1.0f;
                vertices[index++] = 0.0f;

            }
        }
        return vertices;
    }

    public abstract void render();
}