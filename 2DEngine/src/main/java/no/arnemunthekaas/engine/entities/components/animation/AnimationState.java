package no.arnemunthekaas.engine.entities.components.animation;

import no.arnemunthekaas.engine.entities.components.Sprite;
import no.arnemunthekaas.utils.AssetPool;

import java.util.ArrayList;
import java.util.List;

public class AnimationState {
    public String title;
    public List<Frame> animationFrames = new ArrayList<>();

    private static Sprite defaultSprite = new Sprite();
    private transient float timeTracker = 0.0f;
    private transient int currentSprite = 0;
    public boolean doesLoop;

    /**
     * Add a new frame to the antimation frames
     * @param sprite
     * @param frameTime
     */
    public void addFrame(Sprite sprite, float frameTime) {
        this.animationFrames.add(new Frame(sprite, frameTime));
    }

    /**
     * Set if animation loops
     */
    public void setLoop(boolean doesLoop) {
        this.doesLoop = doesLoop;
    }

    /**
     * Updates the animation state
     * @param dt
     */
    public void update(float dt) {
        if(currentSprite < animationFrames.size()) {
            timeTracker -= dt;
            if (timeTracker <= 0) {
                if(!(currentSprite == animationFrames.size() - 1 && !doesLoop))
                    currentSprite = (currentSprite+1) % animationFrames.size();
                timeTracker = animationFrames.get(currentSprite).frameTime;
            }
        }
    }

    /**
     * Get current sprite
     * @return
     */
    public Sprite getCurrentSprite() {
        if(currentSprite < animationFrames.size())
            return animationFrames.get(currentSprite).sprite;
        return defaultSprite;
    }

    /**
     *
     */
    public void refreshTextures() {
        for (Frame frame : animationFrames) {
            frame.sprite.setTexture(AssetPool.getTexture(frame.sprite.getTexture().getFilepath()));
        }
    }
}
