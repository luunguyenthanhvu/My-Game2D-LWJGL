package no.arnemunthekaas.engine.entities.components.animation;

import no.arnemunthekaas.engine.entities.components.Sprite;

public class Frame {

    public Sprite sprite;
    public  float frameTime;

    /**
     *
     */
    public Frame() {

    }

    /**
     *
     * @param sprite
     * @param frameTime
     */
    public Frame(Sprite sprite, float frameTime) {
        this.sprite = sprite;
        this.frameTime = frameTime;
    }
}
