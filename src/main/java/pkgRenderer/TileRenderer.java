package pkgRenderer;

import org.joml.Vector4f;
import org.lwjgl.BufferUtils;
import pkgMinesweeperBackend.GameTimer;
import pkgMinesweeperBackend.MineBoard;
import pkgMinesweeperBackend.Spot;
import pkgUtils.MouseListener;

import java.nio.IntBuffer;
import java.util.Vector;

import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11C.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11C.glClear;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL15C.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL20.glUseProgram;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static pkgMinesweeperBackend.Spot.*;

public class TileRenderer extends RenderEngine {
    private MineBoard my_board;
    private boolean[][] flaggedTile;
    private boolean clickHandled = false;
    boolean startTimer = false;
    private GameTimer gameTimer = new GameTimer();



    // Constructor
    public TileRenderer() {
    }

    @Override
    public void render() {
        my_board = new MineBoard(rows, cols);
        flaggedTile = new boolean[rows][cols];  // Ensure it is initialized when board size is known

        renderTiles(rows, cols);
        my_wm.destroyGlfwWindow();
    }


    // Method used to render 'grid' of square tiles
    private void renderTiles(int rows, int cols) {
        Vector4f COLOR_FACTOR = new Vector4f(0.4f, 0.1f, 0.9f,1.0f); // Color of tiles
        Camera my_cam = new Camera();

        while (!my_wm.isGlfwWindowClosed()) {
            glfwPollEvents();
            glClear(GL_COLOR_BUFFER_BIT);

            shaderObj0.loadMatrix4f("uProjMatrix", my_cam.getProjectionMatrix());
            shaderObj0.loadMatrix4f("uViewMatrix", my_cam.getViewingMatrix());

            glBindVertexArray(vaoID);
            glUseProgram(shaderObj0.getProgID());

            for (int row = 0; row < rows; row++) {
                for (int col = 0; col < cols; col++) {
                    if (tileIsClicked(row, col) == 1 && my_board.isGameActive()) {
                        if (my_board.getTileStatus(row,col) != TILE_STATUS.EXPOSED) {
                            flaggedTile[row][col] = !flaggedTile[row][col];
                            my_board.clickedTileStatus(row,col,true);
                        }
                    }
                    if (tileIsClicked(row, col) == 0 && my_board.isGameActive() && !flaggedTile[row][col]) {
                        my_board.clickedTileStatus(row, col, false);
                    }

                    if (flaggedTile[row][col]) {
                        texture_array[10].bind_texture();
                    } else if (my_board.getTileStatus(row, col) != TILE_STATUS.EXPOSED) {
                        texture_array[11].bind_texture();
                    } else if (my_board.getTileType(row, col) == TILE_TYPE.MINE) {
                        texture_array[9].bind_texture();
                    } else {
                        if (my_board.getTileScore(row, col) == 40) {
                            texture_array[0].bind_texture();
                        } else if (my_board.getTileScore(row, col) == 45) {
                            texture_array[1].bind_texture();
                        } else if (my_board.getTileScore(row, col) == 50) {
                            texture_array[2].bind_texture();
                        } else if (my_board.getTileScore(row,col) == 55) {
                            texture_array[3].bind_texture();
                        } else if (my_board.getTileScore(row,col) == 60) {
                            texture_array[4].bind_texture();
                        } else if (my_board.getTileScore(row,col) == 65) {
                            texture_array[5].bind_texture();
                        } else if (my_board.getTileScore(row,col) == 70) {
                            texture_array[6].bind_texture();
                        } else if (my_board.getTileScore(row,col) == 75) {
                            texture_array[7].bind_texture();
                        } else if (my_board.getTileScore(row,col) == 80) {
                            texture_array[8].bind_texture();
                        } else {
                            texture_array[1].bind_texture();
                        }
                    }

                    shaderObj0.loadVector4f("COLOR_FACTOR", COLOR_FACTOR);
                    renderTile(row, col);
                }
            }
            renderUI();

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

    private int tileIsClicked(int row, int col) {
        float xm = MouseListener.getX();
        float ym = MouseListener.getY();

        ym = Spot.WIN_HEIGHT - ym; // Invert y-axis since origin is bottom left

        float xMin = POLY_OFFSET + col * (POLYGON_LENGTH + POLY_PADDING);
        float xMax = xMin + POLYGON_LENGTH;

        row = (rows - 1) - row;  // Flips the rows
        float yMin = POLY_OFFSET + row * (POLYGON_LENGTH + POLY_PADDING);
        float yMax = yMin + POLYGON_LENGTH;
        boolean insideTile = xm >= xMin && xm <= xMax && ym >= yMin && ym <= yMax;

        if (insideTile) {
            if (MouseListener.mouseButtonDown(0)) {

                if (!clickHandled) {  // Ensures it only prints once per click
                    System.out.println("Left-click at row: " + row + ", col: " + col);
                    clickHandled = true;
                }

                if (!startTimer) {
                    gameTimer.startTimer();
                    startTimer = true;
                }

                return 0;
            }

            if (MouseListener.mouseButtonDown(1)) {
                if (!clickHandled) {
                    System.out.println("Right-click at row: " + row + ", col: " + col);
                    clickHandled = true;

                    if (!startTimer) {
                        gameTimer.startTimer();
                        startTimer = true;
                    }

                    return 1;
                }
            } else {
                clickHandled = false;
            }
        }

        return -1;
    }


    private void renderUI() {
        long currentTime = gameTimer.getElapsedTime();

        Vector4f rectangleColor = new Vector4f(0.26f, 0.29f, 0.32f, 1.0f);
        shaderObj0.loadVector4f("rectangleColor", rectangleColor);

        for (int i = 0; i < currentRectangleIndex; i++) {
            glBindVertexArray(rectangleVaoIDs[i]);
            glDrawArrays(GL_TRIANGLES, 0, 6);
            glBindVertexArray(0);
        }

        // Timer rectangle
        Vector4f secondRectangleColor = new Vector4f(0.0f, 0.0f, 0.0f, 1.0f);
        shaderObj0.loadVector4f("rectangleColor", secondRectangleColor);
        glUseProgram(shaderObj0.getProgID());

        switch ((int) currentTime) {
            case 1:
                texture_array[13].bind_texture();
                break;
            case 2:
                texture_array[14].bind_texture();
                break;
            case 3:
                texture_array[15].bind_texture();
                break;
            case 4:
                texture_array[16].bind_texture();
                break;
            default:
                texture_array[12].bind_texture();
                break;
        }

        glBindVertexArray(rectangleVaoIDs[1]);
        glDrawArrays(GL_TRIANGLES, 0, 6);
        glBindVertexArray(0);
    }

}
