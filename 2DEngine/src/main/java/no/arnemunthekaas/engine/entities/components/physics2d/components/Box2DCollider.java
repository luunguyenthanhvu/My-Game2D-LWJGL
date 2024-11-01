package no.arnemunthekaas.engine.entities.components.physics2d.components;

import no.arnemunthekaas.engine.entities.components.Component;
import no.arnemunthekaas.engine.renderer.DebugDraw;
import org.joml.Vector2f;

public class Box2DCollider extends Component {
    private Vector2f offset = new Vector2f();
    private Vector2f halfSize = new Vector2f(1);
    private Vector2f origin = new Vector2f();

    /**
     *
     * @return
     */
    public Vector2f getHalfSize() {
        return halfSize;
    }

    /**
     *
     * @param halfSize
     */
    public void setHalfSize(Vector2f halfSize) {
        this.halfSize = halfSize;
    }

    /**
     *
     * @return
     */
    public Vector2f getOrigin() {
        return origin;
    }

    /**
     *
     * @return
     */
    public Vector2f getOffset() {
        return offset;
    }

    @Override
    public void editorUpdate(float dt) {
        Vector2f center = new Vector2f(this.gameObject.transform.position).add(this.offset);
        DebugDraw.addBox2D(center, this.halfSize, this.gameObject.transform.rotation);
    }

    /**
     * Set box offset
     * @param offset
     */
    public void setOffset(Vector2f offset) {
        this.offset.set(offset);
    }
}
