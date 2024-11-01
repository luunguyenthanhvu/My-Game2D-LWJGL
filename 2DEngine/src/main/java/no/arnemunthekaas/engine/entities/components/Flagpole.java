package no.arnemunthekaas.engine.entities.components;

import no.arnemunthekaas.engine.entities.GameObject;
import org.jbox2d.dynamics.contacts.Contact;
import org.joml.Vector2f;

public class Flagpole extends Component {
    private boolean isTop;

    public Flagpole(boolean isTop) {
        this.isTop = isTop;
    }

    @Override
    public void beginContact(GameObject collidingObject, Contact contact, Vector2f contactNormal) {
        PlayerController playerController = collidingObject.getComponent(PlayerController.class);
        if (playerController != null) {
            playerController.playWinAnimation(this.gameObject);
        }
    }
}
