package no.arnemunthekaas.engine.entities.components;

import no.arnemunthekaas.editor.imgui.ImGuiUtils;
import no.arnemunthekaas.engine.renderer.Texture;
import org.joml.Vector2f;
import org.joml.Vector4f;

public class SpriteRenderer extends Component {

    private Vector4f color = new Vector4f(1,1,1,1);
    private Sprite sprite = new Sprite();

    private transient Transform lastTransform;
    private transient boolean isDirty = true;

    @Override
    public void start() {
        this.lastTransform = gameObject.transform.copy();
    }

    @Override
    public void update(float dt) {
        if(!this.lastTransform.equals(this.gameObject.transform)) {
            this.gameObject.transform.copy(this.lastTransform);
            isDirty = true;
        }
    }

    @Override
    public void editorUpdate(float dt) {
        if(!this.lastTransform.equals(this.gameObject.transform)) {
            this.gameObject.transform.copy(this.lastTransform);
            isDirty = true;
        }
    }

    @Override
    public void imgui() {
        if (ImGuiUtils.colorPicker4("Color Picker", this.color))
            this.isDirty = true;

    }

    /**
     * Get color vector
     *
     * @return Color vector
     */
    public Vector4f getColor() {
        return color;
    }

    /**
     * Get Texture
     * @return Texture
     */
    public Texture getTexture() {
        return sprite.getTexture();
    }

    /**
     * Get Texture coordinates
     * @return Vector with texture coordinates
     */
    public Vector2f[] getTextureCoordinates() {
        return sprite.getTexCoords();
    }

    /**
     * Change color vector
     * @param color
     */
    public void setColor(Vector4f color) {
        if(!this.color.equals(color)) {
            this.isDirty = true;
            this.color = color;
        }
    }

    /**
     * Change sprite
     * @param sprite
     */
    public void setSprite(Sprite sprite) {
        this.sprite = sprite;
        this.isDirty = true;
    }

    /**
     * Dirty flag, returns true if sprite has changed in any way
     * @return isDirty
     */
    public boolean isDirty() {
        return isDirty;
    }

    /**
     * Cleans the dirty flag.
     */
    public void setClean() {
        this.isDirty = false;
    }

    /**
     * Set texture
     * @param tex
     */
    public void setTexture(Texture tex) {
        this.sprite.setTexture(tex);
    }

    /**
     * Sets dirty flag to true
     */
    public void setDirty() {
        this.isDirty = true;
    }
}
