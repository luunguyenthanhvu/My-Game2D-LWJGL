package no.arnemunthekaas.engine.entities.components;

import no.arnemunthekaas.engine.entities.GameObject;
import no.arnemunthekaas.engine.entities.components.physics2d.components.Rigidbody2D;
import no.arnemunthekaas.utils.AssetPool;
import org.jbox2d.dynamics.contacts.Contact;
import org.joml.Vector2f;

public class BluePotion extends Potion {

    private transient boolean goingRight = true;
    private transient Rigidbody2D rigidbody2D;
    private transient Vector2f speed = new Vector2f(0.1f, 0.0f);
    private transient float maxSpeed = 0.8f;
    private transient boolean hitPlayer = false;

    @Override
    public void start() {
        this.rigidbody2D = gameObject.getComponent(Rigidbody2D.class);
        this.color = Color.Blue;
        AssetPool.getSound("assets/audio/button_2.ogg").play();
    }

    @Override
    public void update(float dt) {
        if (goingRight && Math.abs(rigidbody2D.getVelocity().x) < maxSpeed)
            rigidbody2D.addForce(speed);
        else if (!goingRight && Math.abs(rigidbody2D.getVelocity().x) < maxSpeed)
            rigidbody2D.addForce(new Vector2f(-speed.x, speed.y));
    }

    @Override
    public void preSolve(GameObject collidingObject, Contact contact, Vector2f contactNormal) {
        PlayerController playerController = collidingObject.getComponent(PlayerController.class);
        if (playerController != null) {
            contact.setEnabled(false);
            if (!hitPlayer) {
                playerController.consume(this);
                this.gameObject.destroy();
                hitPlayer = true;
            }
        }

        if(Math.abs(contactNormal.y) < 0.1f)
            goingRight = contactNormal.x < 0;
    }

}
