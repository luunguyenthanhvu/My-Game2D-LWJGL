package no.arnemunthekaas.engine.entities.components;

import no.arnemunthekaas.engine.renderer.Texture;
import org.joml.Vector2f;

public class Sprite {

    private float width, height;

    private Texture texture = null;
    private Vector2f[] texCoords = {
                new Vector2f(1, 1),
                new Vector2f(1, 0),
                new Vector2f(0, 0),
                new Vector2f(0, 1)
        };

    /**
     * Get sprite texture
     * @return
     */
    public Texture getTexture() {
        return texture;
    }

    /**
     * Get texture coordinates
     * @return
     */
    public Vector2f[] getTexCoords() {
        return texCoords;
    }

    /**
     * Set texture
     * @param texture
     */
    public void setTexture(Texture texture) {
        this.texture = texture;
    }

    /**
     * Set texture coordinates
     * @param texCoords
     */
    public void setTexCoords(Vector2f[] texCoords) {
        this.texCoords = texCoords;
    }

    /**
     * Set sprite width
     * @param width width
     */
    public void setWidth(float width) {
        this.width = width;
    }

    /**
     * Set sprite height
     * @param height height
     */
    public void setHeight(float height) {
        this.height = height;
    }

    /**
     * Get sprite width
     * @return width
     */
    public float getWidth() {
        return width;
    }

    /**
     * Get sprite height
     * @return height
     */
    public float getHeight() {
        return height;
    }

    /**
     * Get sprites texture ID, if no texture returns -1
     * @return ID
     */
    public int getTexID() {
        return texture == null ? -1 : texture.getID();
    }
}
