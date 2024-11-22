package pkgSlRenderer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL;
import pkgSlUtils.MSWindowManager;

import java.nio.FloatBuffer;
import java.util.Random;

import static org.lwjgl.opengl.ARBVertexArrayObject.glBindVertexArray;
import static org.lwjgl.opengl.ARBVertexArrayObject.glGenVertexArrays;
import static org.lwjgl.opengl.GL11.glColor4f;
import static org.lwjgl.opengl.GL11.glVertex3f;
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

    Random rand = new Random();
    protected MSWindowManager my_wm;
    protected MSShaderObject shaderObj0;

    public void initOpenGL(MSWindowManager wm, int frameD, int row, int col) {
        my_wm = wm;
        my_wm.updateContextToThis();
        frameDelay = frameD;
        rows = row;
        cols = col;

        GL.createCapabilities();
        float CC_RED = 0.0f, CC_GREEN = 0.0f, CC_BLUE = 0.0f, CC_ALPHA = 1.0f; // Window background color (BLACK)
        glClearColor(CC_RED, CC_GREEN, CC_BLUE, CC_ALPHA);

        float[] mv = new float[rows * cols * FLOAT_PER_SQUARE];

        FloatBuffer fb = BufferUtils.createFloatBuffer(mv.length);
        fb.put(mv).flip();

        vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);

        vboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferData(GL_ARRAY_BUFFER, fb, GL_STATIC_DRAW);

        int posStride = 3;
        int textStride = 2;
        int vertexStride = posStride + textStride;
        int index0 = 0, index1 = 1, startIndex = 0;

        glVertexAttribPointer(index0, posStride, GL_FLOAT, false, vertexStride, startIndex);
        glEnableVertexAttribArray(index0);

        startIndex = 3;
        glVertexAttribPointer(index1, textStride, GL_FLOAT, false, vertexStride, startIndex);
        glEnableVertexAttribArray(index1);

        shaderObj0 = new MSShaderObject("assets/shaders/vs_texture_1.glsl", "assets/shaders/fs_texture_1.glsl");
        shaderObj0.compile_shader();
        shaderObj0.set_shader_program();
    }

    public abstract void render();
}