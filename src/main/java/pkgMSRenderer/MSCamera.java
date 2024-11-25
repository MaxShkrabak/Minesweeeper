package pkgMSRenderer;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import static pkgMinesweeperBackend.MSSpot.*;

public class MSCamera {
    private final Matrix4f projectionMatrix, viewMatrix;

    public MSCamera() {
        projectionMatrix = new Matrix4f();
        viewMatrix = new Matrix4f();

        projectionMatrix.identity();
        projectionMatrix.ortho(FRUSTUM_LEFT,FRUSTUM_RIGHT,FRUSTUM_TOP,FRUSTUM_BOTTOM,Z_NEAR,Z_FAR);
    }

    public Matrix4f getViewingMatrix() {
        Vector3f lookFrom = new Vector3f(0.0f,0.0f,100f);
        Vector3f cameraUp = new Vector3f(0.0f,1.0f,0.0f);
        Vector3f lookAt = new Vector3f(0.0f,0.0f,-1.0f);
        viewMatrix.identity();
        viewMatrix.lookAt(lookFrom, lookAt, cameraUp);
        return viewMatrix;
    }

    public Matrix4f getProjectionMatrix() {
        return projectionMatrix;
    }
}
