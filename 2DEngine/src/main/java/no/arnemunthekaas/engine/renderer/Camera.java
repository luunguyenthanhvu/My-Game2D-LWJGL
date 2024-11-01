package no.arnemunthekaas.engine.renderer;

import no.arnemunthekaas.utils.GameConstants;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class Camera {

    public Vector4f clearColor = new Vector4f(1, 1, 1, 1);
    private Matrix4f projectionMat, viewMat, inverseProjectionMat, inverseViewMat;
    public Vector2f position;

    private float projectionWidth = 6;
    private float projectionHeight = 6 / GameConstants.ASPECT_RATIO;
    private Vector2f projectionSize = new Vector2f(projectionWidth, projectionHeight);
    private float zoom = 1.0f;

    /**
     * Create a Camera object
     *
     * @param position Position vector
     */
    public Camera(Vector2f position) {
        this.position = position;
        this.projectionMat = new Matrix4f();
        this.viewMat = new Matrix4f();
        this.inverseProjectionMat = new Matrix4f();
        this.inverseViewMat = new Matrix4f();
        adjustProjection();
    }

    /**
     *  Adjusts current projection
     */
    public void adjustProjection() {
        projectionMat.identity();
        projectionMat.ortho(0.0f, projectionSize.x * zoom, 0.0f, projectionSize.y * zoom, 0.0f, 100.0f);
        projectionMat.invert(inverseProjectionMat);
        viewMat.invert(inverseViewMat);
    }

    /**
     * Returns the view matrix
     *
     * @return Matrix4f view matrix
     */
    public Matrix4f getViewMat() {
        Vector3f cameraFront = new Vector3f(0.0f, 0.0f, -1.0f);
        Vector3f cameraUp = new Vector3f(0.0f, 1.0f, 0.0f);
        this.viewMat.identity();
        viewMat.lookAt(new Vector3f(position.x, position.y, 20.0f), cameraFront.add(position.x, position.y, 0.0f), cameraUp);
        this.viewMat.invert(inverseViewMat);

        return this.viewMat;
    }

    /**
     * Get the projection matrix
     * @return Matrix4f for projection
     */
    public Matrix4f getProjectionMat() {
        return this.projectionMat;
    }

    /**
     * Get inverse projection matrix
     * @return inverse projection matrix
     */
    public Matrix4f getInverseProjectionMat() {
        return inverseProjectionMat;
    }

    /**
     * Get inverse view matrix
     * @return inverse view matrix
     */
    public Matrix4f getInverseViewMat() {
        return inverseViewMat;
    }

    /**
     * Get projection size
     * @return projection size
     */
    public Vector2f getProjectionSize() {
        return projectionSize;
    }

    /**
     *
     * @return
     */
    public float getZoom() {
        return zoom;
    }

    /**
     *
     * @param zoom
     */
    public void setZoom(float zoom) {
        this.zoom = zoom;
    }

    /**
     *
     * @param value
     */
    public void addZoom(float value) {
        this.zoom += value;
    }
}
