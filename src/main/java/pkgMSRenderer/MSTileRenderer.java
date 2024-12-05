package pkgMSRenderer;

import org.joml.Vector4f;
import org.lwjgl.BufferUtils;
import pkgMinesweeperBackend.MSMineBoard;
import pkgMinesweeperBackend.MSSpot;
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
import static pkgMinesweeperBackend.MSSpot.*;

public class MSTileRenderer extends MSRenderEngine {
    private MSMineBoard my_board;

    // Constructor
    public MSTileRenderer() {
    }

    @Override
    public void render() {
        my_board = new MSMineBoard(rows, cols);
        my_board.printBoard();
        my_board.printTileScores();

        renderTiles(rows, cols);
        my_wm.destroyGlfwWindow();
    }

    // Method used to render 'grid' of square tiles
    private void renderTiles(int rows, int cols) {
        Vector4f COLOR_FACTOR = new Vector4f(0.4f, 0.1f, 0.9f,1.0f); // Color of tiles
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
                    if (tileIsClicked(row, col) && my_board.isGameActive()) {
                        my_board.clickedTileStatus(row, col);
                    }

                    if (my_board.getTileStatus(row, col) != TILE_STATUS.EXPOSED) {
                        texture_array[2].bind_texture();
                    } else if (my_board.getTileType(row, col) == TILE_TYPE.MINE) {
                        texture_array[1].bind_texture();
                    } else {
                        texture_array[0].bind_texture();
                    }

                    shaderObj0.loadVector4f("COLOR_FACTOR", COLOR_FACTOR);
                    renderTile(row, col);
                }
            }
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

    // Method to check if a tile was clicked
    private boolean tileIsClicked(int row, int col) {
        float xm = MSMouseListener.getX();
        float ym = MSMouseListener.getY();

        ym = MSSpot.WIN_HEIGHT - ym;      // Invert y-axis since origin is bottom left

        float xMin = POLY_OFFSET + col * (POLYGON_LENGTH + POLY_PADDING);
        float xMax = xMin + POLYGON_LENGTH;

        row = (rows - 1) - row;  // Flips the rows
        float yMin = POLY_OFFSET + row * (POLYGON_LENGTH + POLY_PADDING);
        float yMax = yMin + POLYGON_LENGTH;

        // Checks if click was within a tile
        return MSMouseListener.mouseButtonDown(0) && xm >= xMin && xm <= xMax && ym >= yMin && ym <= yMax;
    }
}
