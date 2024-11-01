package no.arnemunthekaas.engine.entities.components;

import no.arnemunthekaas.engine.entities.GameObject;
import no.arnemunthekaas.utils.AssetPool;
import org.jbox2d.dynamics.contacts.Contact;
import org.joml.Vector2f;

public abstract class Block extends Component {

    private transient boolean bopGoingUp = true;
    private transient boolean doBopAnimation = false;
    private transient Vector2f bopStart;
    private transient Vector2f topBoplocation;
    private transient boolean active = true;

    public float bopSpeed = 0.8f;

    @Override
    public void start() {
        this.bopStart = new Vector2f(this.gameObject.transform.position);
        this.topBoplocation = new Vector2f(bopStart).add(0.0f, 0.02f);
    }

    @Override
    public void update(float dt) {
        if (doBopAnimation) {
            if(bopGoingUp) {
                if(this.gameObject.transform.position.y < topBoplocation.y)
                    this.gameObject.transform.position.y += bopSpeed * dt;
                else
                    bopGoingUp = false;
            } else {
                if (this.gameObject.transform.position.y > bopStart.y)
                    this.gameObject.transform.position.y -= bopSpeed * dt;
                else {
                    this.gameObject.transform.position.y = this.bopStart.y;
                    bopGoingUp = true;
                    doBopAnimation = false;
                }
            }
        }
    }

    @Override
    public void beginContact(GameObject collidingObject, Contact contact, Vector2f contactNormal) {
        PlayerController playerController = collidingObject.getComponent(PlayerController.class);
        if (active && playerController != null && contactNormal.y < -0.8f) {
            doBopAnimation = true; //TODO: move sound playing to subclass?
            AssetPool.getSound("assets/audio/assets_sounds_bump.ogg").play();
            playerHit(playerController);
        }
    }

    /**
     *
     */
    public void setInactive() {
        this.active = false;
    }

    /**
     *
     * @param playerController
     */
    abstract void playerHit(PlayerController playerController);
}
