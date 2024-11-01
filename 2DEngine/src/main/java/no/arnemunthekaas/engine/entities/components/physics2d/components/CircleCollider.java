package no.arnemunthekaas.engine.entities.components.physics2d.components;

import no.arnemunthekaas.engine.entities.components.Component;
import no.arnemunthekaas.engine.renderer.DebugDraw;
import org.joml.Vector2f;

public class CircleCollider extends Component {
    private Vector2f offset = new Vector2f();
    private float radius = 1.0f;

    @Override
    public void editorUpdate(float dt) {
        Vector2f center = new Vector2f(this.gameObject.transform.position).add(this.offset);
        DebugDraw.addCircle(center, this.radius);
    }

    /**
     *
     * @return
     */
    public float getRadius() {
        return radius;
    }

    /**
     *
     * @param radius
     */
    public void setRadius(float radius) {
        this.radius = radius;
    }

    /**
     * Returns offset
     * @return Vector2f containing offset
     */
    public Vector2f getOffset() {
        return offset;
    }

    /**
     * Set new offset
     */
    public void setOffset(Vector2f offset) {
        this.offset.set(offset);
    }
}
