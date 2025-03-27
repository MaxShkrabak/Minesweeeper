package pkgRenderer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL;
import pkgUtils.WindowManager;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.ARBVertexArrayObject.glBindVertexArray;
import static org.lwjgl.opengl.ARBVertexArrayObject.glGenVertexArrays;
import static org.lwjgl.opengl.GL11C.glClearColor;
import static org.lwjgl.opengl.GL15C.*;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static pkgMinesweeperBackend.Spot.*;

public abstract class RenderEngine {
    protected static final int FLOAT_PER_SQUARE = 5;
    protected static final int VPT = 4;

    protected int[] rectangleVaoIDs = new int[10];
    protected int[] rectangleVboIDs = new int[10];
    protected int vaoID, vboID;
    protected int rows, cols;
    protected int currentRectangleIndex = 0;

    protected WindowManager my_wm;
    protected ShaderObject shaderObj0;
    protected TextureObject[] game_array = new TextureObject[14];
    protected TextureObject[] timer_array = new TextureObject[10];

    public void initOpenGL(WindowManager wm, int row, int col) {
        my_wm = wm;
        my_wm.updateContextToThis();
        rows = row;
        cols = col;

        GL.createCapabilities();
        float CC_RED = 0.5f, CC_GREEN = 0.5f, CC_BLUE = 0.5f, CC_ALPHA = 1.0f; // Window background color (BLACK)
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

        shaderObj0 = new ShaderObject("assets/shaders/vs_texture_1.glsl", "assets/shaders/fs_texture_1.glsl");
        shaderObj0.compile_shader();
        shaderObj0.set_shader_program();

        game_array = TextureLoader.loadGameTextures();
        timer_array = TextureLoader.loadTimerTextures();


        initRectangle(2f,2f,670f, 58f); // UI background

        // In-game timer
        initRectangle(640f, 7f, 27f, 49f); // Ones counter
        initRectangle(613f, 7f, 27f, 49f); // Tens counter
        initRectangle(586, 7f, 27f, 49f); // Hundreds counter

        // Mine counter
        initRectangle(6, 7f, 27f, 49f); // Hundreds
        initRectangle(33, 7f, 27f, 49f); // Tens
        initRectangle(60, 7f, 27f, 49f); // Ones

        // Reset button
        initRectangle(320, 11f, 40f, 40f);
    }

    // Method to generate tile vertices starting from bottom left of window
    private float[] generateVertices(int rows, int cols) {
        int totalTiles = rows * cols;
        float[] vertices = new float[totalTiles * VPT * FLOAT_PER_SQUARE];

        float height = WIN_HEIGHT - 2 * POLY_OFFSET - (rows - 1) * POLY_PADDING;
        float width = WIN_WIDTH - 2 * POLY_OFFSET - (cols - 1) * POLY_PADDING;

        float tileSize = Math.min(height / rows, width / cols);
        float z = 0.0f;

        int index = 0;
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                float x = col * (tileSize + POLY_PADDING) + POLY_OFFSET;
                float y = row * (tileSize + POLY_PADDING) + POLY_OFFSET + TOP_HUD_HEIGHT;

                // Bottom left
                vertices[index++] = x;
                vertices[index++] = y;
                vertices[index++] = z;
                vertices[index++] = 0.0f;     // Texture x
                vertices[index++] = 0.0f;     // Texture y

                // Bottom right
                vertices[index++] = x + tileSize;
                vertices[index++] = y;
                vertices[index++] = z;
                vertices[index++] = 1.0f;
                vertices[index++] = 0.0f;

                // Top right
                vertices[index++] = x + tileSize;
                vertices[index++] = y + tileSize;
                vertices[index++] = z;
                vertices[index++] = 1.0f;
                vertices[index++] = 1.0f;

                // Top left
                vertices[index++] = x;
                vertices[index++] = y + tileSize;
                vertices[index++] = z;
                vertices[index++] = 0.0f;
                vertices[index++] = 1.0f;

            }
        }
        return vertices;
    }

    // Used to create rectangles
    private void initRectangle(float x, float y, float width, float height) {
        float[] rectangleVertices = {
                x, y + height, 0.0f, 0.0f, 1.0f,
                x, y, 0.0f, 0.0f, 0.0f,
                x + width, y + height, 0.0f, 1.0f, 1.0f,

                x, y, 0.0f, 0.0f, 0.0f,
                x + width, y, 0.0f, 1.0f, 0.0f,
                x + width, y + height, 0.0f, 1.0f, 1.0f,
        };

        FloatBuffer rectangleBuffer = BufferUtils.createFloatBuffer(rectangleVertices.length);
        rectangleBuffer.put(rectangleVertices).flip();

        rectangleVaoIDs[currentRectangleIndex] = glGenVertexArrays();
        glBindVertexArray(rectangleVaoIDs[currentRectangleIndex]);

        rectangleVboIDs[currentRectangleIndex] = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, rectangleVboIDs[currentRectangleIndex]);
        glBufferData(GL_ARRAY_BUFFER, rectangleBuffer, GL_STATIC_DRAW);

        glVertexAttribPointer(0, 3, GL_FLOAT, false, 5 * Float.BYTES, 0);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1,2,GL_FLOAT, false, 5 * Float.BYTES, 3 * Float.BYTES);
        glEnableVertexAttribArray(1);

        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindVertexArray(0);

        currentRectangleIndex++;
    }


    public abstract void render();
}