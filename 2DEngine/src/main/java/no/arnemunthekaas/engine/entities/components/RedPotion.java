package no.arnemunthekaas.engine.entities.components;

import no.arnemunthekaas.engine.entities.GameObject;
import no.arnemunthekaas.engine.entities.components.physics2d.components.Rigidbody2D;
import no.arnemunthekaas.utils.AssetPool;
import org.jbox2d.dynamics.contacts.Contact;
import org.joml.Vector2f;

public class RedPotion extends Potion {

    private transient Rigidbody2D rigidbody2D;

    @Override
    public void start() {
        this.rigidbody2D = gameObject.getComponent(Rigidbody2D.class);
        this.color = Color.Red;
        AssetPool.getSound("assets/audio/button_2.ogg").play();
        this.rigidbody2D.setIsSensor();
    }

    @Override
    public void beginContact(GameObject collidingObject, Contact contact, Vector2f contactNormal) {
        PlayerController playerController = collidingObject.getComponent(PlayerController.class);
        if (playerController != null) {
            playerController.consume(this);
            this.gameObject.destroy();
        }
    }
}
