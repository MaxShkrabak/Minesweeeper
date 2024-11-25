package pkgMSRenderer;

import org.joml.Vector4f;
import org.lwjgl.BufferUtils;
import pkgMinesweeperBackend.MSPingPong;
import pkgMinesweeperBackend.MSSpot;
import pkgSlUtils.MSKeyListener;
import pkgSlUtils.MSMouseListener;

import java.nio.IntBuffer;

import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11C.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11C.glClear;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL15C.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL20.glUseProgram;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

public class MSTileRenderer extends MSRenderEngine {

    // Constructor
    public MSTileRenderer() {
    }

    @Override
    public void render() {
        MSPingPong myPingPong = new MSPingPong(rows, cols);
        MSKeyListener.setPPInstance(myPingPong); // Set instance of pingPong in keyListener

        MSKeyListener.commandMenu(); // Display commands from keyListener

        // Gets the passed in frame delay and sends it to keyListener
        MSKeyListener.initFrameDelay(frameDelay);

        renderTiles(rows, cols);
        my_wm.destroyGlfwWindow();
    }

    // Method used to render 'grid' of square tiles
    private void renderTiles(int rows, int cols) {
        Vector4f COLOR_FACTOR = new Vector4f(0.4f, 0.1f, 0.7f,1.0f); // Color of tiles
        MSCamera my_cam = new MSCamera();

        while (!my_wm.isGlfwWindowClosed()) {
            glfwPollEvents();
            glClear(GL_COLOR_BUFFER_BIT);

            shaderObj0.loadMatrix4f("uProjMatrix", my_cam.getProjectionMatrix());
            shaderObj0.loadMatrix4f("uViewMatrix", my_cam.getViewingMatrix());

            glBindVertexArray(vaoID);
            glUseProgram(shaderObj0.getProgID());

            for (int row = 0; row < rows; row++) {
                for (int col = 0; col < cols; col++) {
                    shaderObj0.loadVector4f("COLOR_FACTOR", COLOR_FACTOR);
                    texObj0.bind_texture();
                    renderTile(row,col);
                }
            }
            mouseClick();
            my_wm.swapBuffers();
        }
    }

    // Render the particular tile
    public void renderTile(int row, int col) {
        // Compute the vertexArray offset
        int va_offset = getVAVIndex(row, col); // vertex array offset of tile
        int[] rgVertexIndices = new int[] {va_offset, va_offset+1, va_offset+2,
                va_offset, va_offset+2, va_offset+3};
        IntBuffer VertexIndicesBuffer = BufferUtils.createIntBuffer(rgVertexIndices.length);
        VertexIndicesBuffer.put(rgVertexIndices).flip();
        int eboID = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, VertexIndicesBuffer, GL_STATIC_DRAW);
        glDrawElements(GL_TRIANGLES, rgVertexIndices.length, GL_UNSIGNED_INT, 0);
    } // public void renderTile(...)

    private int getVAVIndex(int row, int col) {
        return (row * cols + col) * VPT;
    }

    // Method to display where mouse is being clicked on window
    public void mouseClick() {
        if (MSMouseListener.mouseButtonDown(0)) {
            float xm = MSMouseListener.getX();
            float ym = MSMouseListener.getY();
            ym = MSSpot.WIN_HEIGHT - ym;      // Invert y-axis since origin is bottom left
            System.out.println(xm + " " + ym);
        }
    }
}
