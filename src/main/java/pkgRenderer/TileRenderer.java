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
    private int totalMines = MineBoard.getNumMines();

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

            if (resetIsClicked()) {
                resetGame();
            }

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
                    totalMines--;

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

    private boolean resetIsClicked() {
        float xm = MouseListener.getX();
        float ym = MouseListener.getY();

        ym = Spot.WIN_HEIGHT - ym;

        float resetXMin = 320f;
        float resetXMax = 360f;
        float resetYMin = 683f;
        float resetYMax = 723f;

        boolean insideButton = xm >= resetXMin && xm <= resetXMax && ym >= resetYMin && ym <= resetYMax;

        if (insideButton && MouseListener.mouseButtonDown(0)) {
            return true;
        }
        return false;
    }


    private void renderUI() {
        long currentTime = gameTimer.getElapsedTime();

        int hundreds = (int) (currentTime / 100);
        int tens = (int) (currentTime / 10) % 10;
        int ones = (int) (currentTime % 10);

        int mineHundreds = (totalMines / 100);
        int mineTens = (totalMines / 10) % 10;
        int mineOnes = (totalMines % 10);

        Vector4f rectangleColor = new Vector4f(0.26f, 0.29f, 0.32f, 1.0f);
        shaderObj0.loadVector4f("rectangleColor", rectangleColor);

        glBindVertexArray(rectangleVaoIDs[0]);
        glDrawArrays(GL_TRIANGLES, 0, 6);
        glBindVertexArray(0);


        // Ones timer rectangle
        Vector4f OnesRectangleColor = new Vector4f(0.0f, 0.0f, 0.0f, 1.0f);
        shaderObj0.loadVector4f("rectangleColor", OnesRectangleColor);
        glUseProgram(shaderObj0.getProgID());

        switchCounter(ones); // Updates ones counter

        glBindVertexArray(rectangleVaoIDs[1]);
        glDrawArrays(GL_TRIANGLES, 0, 6);
        glBindVertexArray(0);

        // Tenths timer counter
        Vector4f TensRectangleColor = new Vector4f(0.0f, 0.0f, 0.0f, 1.0f);
        shaderObj0.loadVector4f("rectangleColor", TensRectangleColor);
        glUseProgram(shaderObj0.getProgID());

        switchCounter(tens); // Updates tens counter

        glBindVertexArray(rectangleVaoIDs[2]);
        glDrawArrays(GL_TRIANGLES, 0, 6);
        glBindVertexArray(0);

        // Hundreds timer counter
        Vector4f HundredsRectangleColor = new Vector4f(0.0f, 0.0f, 0.0f, 1.0f);
        shaderObj0.loadVector4f("rectangleColor", HundredsRectangleColor);
        glUseProgram(shaderObj0.getProgID());

        switchCounter(hundreds); // Updates tens counter

        glBindVertexArray(rectangleVaoIDs[3]);
        glDrawArrays(GL_TRIANGLES, 0, 6);
        glBindVertexArray(0);

        // Hundreds Mine counter
        Vector4f HMineRectangleColor = new Vector4f(0.0f, 0.0f, 0.0f, 1.0f);
        shaderObj0.loadVector4f("rectangleColor", HMineRectangleColor);
        glUseProgram(shaderObj0.getProgID());

        switchCounter(mineHundreds);

        glBindVertexArray(rectangleVaoIDs[4]);
        glDrawArrays(GL_TRIANGLES, 0, 6);
        glBindVertexArray(0);

        // Tens Mine counter
        Vector4f TMineRectangleColor = new Vector4f(0.0f, 0.0f, 0.0f, 1.0f);
        shaderObj0.loadVector4f("rectangleColor", TMineRectangleColor);
        glUseProgram(shaderObj0.getProgID());
        switchCounter(mineTens);
        glBindVertexArray(rectangleVaoIDs[5]);
        glDrawArrays(GL_TRIANGLES, 0, 6);
        glBindVertexArray(0);

        // Ones Mine counter
        Vector4f OMineRectangleColor = new Vector4f(0.0f, 0.0f, 0.0f, 1.0f);
        shaderObj0.loadVector4f("rectangleColor", OMineRectangleColor);
        glUseProgram(shaderObj0.getProgID());
        switchCounter(mineOnes);
        glBindVertexArray(rectangleVaoIDs[6]);
        glDrawArrays(GL_TRIANGLES, 0, 6);
        glBindVertexArray(0);

        // Reset button
        Vector4f ResetRectangleColor = new Vector4f(0.0f, 0.0f, 0.0f, 1.0f);
        shaderObj0.loadVector4f("rectangleColor", ResetRectangleColor);
        glUseProgram(shaderObj0.getProgID());

        texture_array[22].bind_texture();

        glBindVertexArray(rectangleVaoIDs[7]);
        glDrawArrays(GL_TRIANGLES, 0, 6);
        glBindVertexArray(0);
    }

    public void switchCounter(int value) {
        switch (value) {
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
            case 5:
                texture_array[17].bind_texture();
                break;
            case 6:
                texture_array[18].bind_texture();
                break;
            case 7:
                texture_array[19].bind_texture();
                break;
            case 8:
                texture_array[20].bind_texture();
                break;
            case 9:
                texture_array[21].bind_texture();
                break;
            default:
                texture_array[12].bind_texture();
                break;
        }
    }

    private void resetGame() {
        my_board = new MineBoard(rows,cols);
        flaggedTile = new boolean[rows][cols];
        gameTimer.resetTimer();
        totalMines = MineBoard.getNumMines();
        startTimer = false;
        clickHandled = false;
    }
}
