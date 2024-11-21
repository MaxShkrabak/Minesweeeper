package pkgSlRenderer;

import org.joml.Matrix4f;
import org.joml.Vector4f;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.lwjgl.opengl.GL20.*;

public class MSShaderObject {
    private String vsFile, fsFile;
    private int programId;
    public MSShaderObject(String vs, String fs) {
        try {
            vsFile = new String(Files.readAllBytes(Paths.get(vs)));
            fsFile = new String(Files.readAllBytes(Paths.get(fs)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void compile_shader() {
        programId = glCreateProgram();

        int vs = glCreateShader(GL_VERTEX_SHADER);
        glShaderSource(vs, vsFile);
        glCompileShader(vs);
        glAttachShader(programId, vs);

        int fs = glCreateShader(GL_FRAGMENT_SHADER);
        glShaderSource(fs, fsFile);
        glCompileShader(fs);
        glAttachShader(programId, fs);
        glLinkProgram(programId);
    }

    public void set_shader_program() {
        glLinkProgram(programId);
    }

    public void loadMatrix4f(String strMatrixName, Matrix4f my_mat4) {
        int var_location = glGetUniformLocation(programId, strMatrixName);
        final int OGL_MATRIX_SIZE = 16;
        FloatBuffer matrixBuffer = BufferUtils.createFloatBuffer(OGL_MATRIX_SIZE);
        my_mat4.get(matrixBuffer);
        glUniformMatrix4fv(var_location, false, matrixBuffer);
    } // public void loadMatrix4f(...)

    public void loadVector4f(String strVec4Name, Vector4f my_vec4) {
        int var_location = glGetUniformLocation(programId, strVec4Name);
        final int OGL_VEC4_SIZE = 4;
        FloatBuffer vec4Buffer = BufferUtils.createFloatBuffer(OGL_VEC4_SIZE);
        my_vec4.get(vec4Buffer);
        glUniform4fv(var_location, vec4Buffer);
    } // public void loadVec4f(...)

    public int getProgID() {
        return programId;
    }
}
