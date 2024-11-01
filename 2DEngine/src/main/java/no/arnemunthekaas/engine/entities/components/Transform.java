package no.arnemunthekaas.engine.entities.components;

import no.arnemunthekaas.editor.imgui.ImGuiUtils;
import no.arnemunthekaas.utils.GameConstants;
import org.joml.Vector2f;

public class Transform extends Component{

    public Vector2f position;
    public Vector2f scale;
    public float rotation = 0.0f;
    public int zIndex;

    /**
     * Create new transform, with new position and scale vectors
     */
    public Transform() {
        this.position = new Vector2f();
        this.scale = new Vector2f();
        this.zIndex = 0;
    }

    /**
     * Create new transform, with given position vector
     * @param position position vector
     */
    public Transform(Vector2f position) {
        this.position = position;
        this.scale = new Vector2f();
        this.zIndex = 0;
    }

    /**
     * Create new transform with both position and scale vectors
     *
     * @param position position vector
     * @param scale scale vector
     */
    public Transform(Vector2f position, Vector2f scale) {
        this.position = position;
        this.scale = scale;
        this.zIndex = 0;
    }

    /**
     * Return a new copy of this transform
     * @return New Transform copy
     */
    public Transform copy() {
        return new Transform(new Vector2f(this.position), new Vector2f(this.scale));
    }

    /**
     * Copy contents of this transform to transform parameter
     * @param to Transform to be copied to
     */
    public void copy(Transform to) {
        to.position.set(this.position);
        to.scale.set(this.scale);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null)
            return false;
        if (!(o instanceof Transform))
            return false;

        Transform t = (Transform) o;
        return t.position.equals(this.position) && t.scale.equals(this.scale) && t.rotation == this.rotation
                && t.zIndex == this.zIndex;

    }

    @Override
    public void imgui() {
        gameObject.name = ImGuiUtils.inputText("Name: ", gameObject.name);
        ImGuiUtils.drawVec2Control("Positon", this.position);
        ImGuiUtils.drawVec2Control("Scale", this.scale, GameConstants.GRID_WIDTH);
        this.rotation = ImGuiUtils.dragFloat("Rotation", this.rotation);
        this.zIndex = ImGuiUtils.dragInt("Z-Index", this.zIndex);
    }

}
