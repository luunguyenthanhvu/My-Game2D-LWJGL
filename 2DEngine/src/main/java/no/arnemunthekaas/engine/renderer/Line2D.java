package no.arnemunthekaas.engine.renderer;

import org.joml.Vector2f;
import org.joml.Vector3f;

public class Line2D {

    private Vector2f start;
    private Vector2f end;
    private Vector3f color;
    private int lifetime; // TODO change to seconds?

    /**
     * Create new line
     * @param start start vector
     * @param end end vector
     * @param color color (rgb) vector
     * @param lifetime lifetime inf frames
     */
    public Line2D(Vector2f start, Vector2f end, Vector3f color, int lifetime) {
        this.start = start;
        this.end = end;
        this.color = color;
        this.lifetime = lifetime;
    }

    /**
     *
     * @return
     */
    public int beginFrame() {
        lifetime--;
        return lifetime;
    }

    /**
     * Get line's start position
     * @return start position
     */
    public Vector2f getStart() {
        return start;
    }

    /**
     * Get line's end position
     * @return end position
     */
    public Vector2f getEnd() {
        return end;
    }

    /**
     * Get line's color vector
     * @return color vector
     */
    public Vector3f getColor() {
        return color;
    }
}
