package pkgSlRenderer;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public class MSCamera {
    private Matrix4f projectionMatrix, viewMatrix;

    public MSCamera() {
        projectionMatrix = new Matrix4f();
        viewMatrix = new Matrix4f();

        projectionMatrix.identity();
        projectionMatrix.ortho(-1f,1f,-1f,1f,0.0f,100f);
    }

    public Matrix4f getViewingMatrix() {
        Vector3f lookFrom = new Vector3f(0.0f,0.0f,5f);
        Vector3f cameraUp = new Vector3f(0.0f,1.0f,0.0f);
        Vector3f lookAt = new Vector3f(0.0f,0.0f,0.0f);
        viewMatrix.identity();
        viewMatrix.lookAt(lookFrom, lookAt, cameraUp);
        return viewMatrix;
    }

    public Matrix4f getProjectionMatrix() {
        return projectionMatrix;
    }
}
