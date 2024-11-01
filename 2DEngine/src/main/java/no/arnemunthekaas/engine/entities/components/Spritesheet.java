package no.arnemunthekaas.engine.entities.components;

import no.arnemunthekaas.engine.renderer.Texture;
import org.joml.Vector2f;

import java.util.ArrayList;
import java.util.List;

public class Spritesheet {

    private Texture texture;
    private List<Sprite> sprites;

    /**
     *
     * @param texture
     * @param spriteWidth
     * @param spriteHeight
     * @param spriteAmount
     * @param spacing
     */
    public Spritesheet(Texture texture, int spriteWidth, int spriteHeight, int spriteAmount, int spacing) {
        this.sprites = new ArrayList<>();
        this.texture = texture;
        int currentX = 0;
        int currentY = texture.getHeight()-spriteHeight;

        for (int i = 0; i < spriteAmount; i++) {
            float topY= (currentY + spriteHeight) / (float)texture.getHeight();
            float rightX = (currentX + spriteWidth) / (float)texture.getWidth();
            float leftX = currentX / (float)texture.getWidth();
            float bottomY = currentY / (float)texture.getHeight();

            Vector2f[] texCoords = {
                    new Vector2f(rightX, topY),
                    new Vector2f(rightX, bottomY),
                    new Vector2f(leftX, bottomY),
                    new Vector2f(leftX, topY)
            };
            Sprite sprite = new Sprite();
            sprite.setTexture(this.texture);
            sprite.setTexCoords(texCoords);
            sprite.setWidth(spriteWidth);
            sprite.setHeight(spriteHeight);
            this.sprites.add(sprite);

            currentX += spriteWidth + spacing;
            if(currentX >= texture.getWidth()) {
                currentX = 0;
                currentY -= spriteHeight + spacing;
            }

        }
    }

    /**
     *
     * @param index
     * @return
     */
    public Sprite getSprite(int index) {
        return sprites.get(index);
    }

    /**
     * Get amount of sprites
     * @return
     */
    public int size() {
        return sprites.size();
    }
}
