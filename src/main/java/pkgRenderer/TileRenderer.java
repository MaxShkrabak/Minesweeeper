package pkgRenderer;

import org.joml.Vector4f;
import org.lwjgl.BufferUtils;
import pkgMinesweeperBackend.GameTimer;
import pkgMinesweeperBackend.MineBoard;
import pkgMinesweeperBackend.Spot;
import pkgUtils.MouseListener;

import java.nio.IntBuffer;

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
                    // Right-clicked tile
                    if (tileIsClicked(row, col) == 1 && my_board.isGameActive()) {
                        if (my_board.getTileStatus(row,col) != TILE_STATUS.EXPOSED) {
                            flaggedTile[row][col] = !flaggedTile[row][col];
                            my_board.clickedTileStatus(row,col,true);
                        }
                    }

                    // Left-clicked tile
                    if (tileIsClicked(row, col) == 0 && my_board.isGameActive() && !flaggedTile[row][col]) {
                        my_board.clickedTileStatus(row, col, false);
                    }

                    if (flaggedTile[row][col] && my_board.getTileStatus(row,col) == TILE_STATUS.CLOSEDMINE) {
                        game_array[13].bind_texture();
                    } else if (flaggedTile[row][col] && my_board.getTileStatus(row,col) == TILE_STATUS.EXPOSED) {
                        flaggedTile[row][col] = false;
                    } else if (flaggedTile[row][col]) {
                        game_array[10].bind_texture();
                    } else if (my_board.getTileStatus(row, col) != TILE_STATUS.EXPOSED) {
                        game_array[11].bind_texture();
                    } else if (my_board.getTileType(row, col) == TILE_TYPE.MINE) {
                        gameTimer.stopTimer();
                        game_array[9].bind_texture();
                    } else {
                        // Bind texture based on tile score
                        int score = my_board.getTileScore(row,col);
                        int textureIndex = (score - 40) / 5;
                        if (textureIndex < 0 || textureIndex > 8) {textureIndex = 1;}
                        game_array[textureIndex].bind_texture();
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

            // Right Click
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

        return insideButton && MouseListener.mouseButtonDown(0);
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

        game_array[12].bind_texture();

        glBindVertexArray(rectangleVaoIDs[7]);
        glDrawArrays(GL_TRIANGLES, 0, 6);
        glBindVertexArray(0);
    }

    public void switchCounter(int value) {
        if (value >= 0 && value <= 9) {
            timer_array[value].bind_texture();
        } else {
            timer_array[0].bind_texture();
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
