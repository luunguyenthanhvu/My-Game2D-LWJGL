package no.arnemunthekaas.engine.entities.components;

import no.arnemunthekaas.engine.Window;
import no.arnemunthekaas.engine.entities.GameObject;
import no.arnemunthekaas.engine.entities.components.animation.StateMachine;
import no.arnemunthekaas.engine.entities.components.physics2d.Physics2D;
import no.arnemunthekaas.engine.entities.components.physics2d.components.Rigidbody2D;
import no.arnemunthekaas.engine.renderer.Camera;
import no.arnemunthekaas.utils.AssetPool;
import no.arnemunthekaas.utils.GameConstants;
import org.jbox2d.dynamics.contacts.Contact;
import org.joml.Vector2f;

public class GoombaAI extends EnemyAI {

    private transient boolean goingRight;
    private transient boolean onGround;
    private transient Rigidbody2D rigidbody2D;
    private transient float walkSpeed = 0.6f;
    private transient Vector2f velocity = new Vector2f();
    private transient Vector2f acceleration = new Vector2f();
    private transient Vector2f terminalVelocity = new Vector2f();
    private transient boolean isDead;
    private transient float timeToKill = 0.5f;
    private transient StateMachine stateMachine;

    @Override
    public void start() {
        this.stateMachine = gameObject.getComponent(StateMachine.class);
        this.rigidbody2D = gameObject.getComponent(Rigidbody2D.class);
        this.acceleration.y = Window.getPhysics().getGravity().y;
    }

    @Override
    public void update(float dt) {
        Camera camera = Window.getScene().getCamera();
        if (this.gameObject.transform.position.x > camera.position.x + camera.getProjectionSize().x * camera.getZoom())
            return; // If goomba outside of camera view, do not update

        if (isDead) {
            timeToKill -= dt;
            if (timeToKill <= 0)
                this.gameObject.destroy();
            this.rigidbody2D.setVelocity(new Vector2f());
            return;
        }

        if (goingRight)
            velocity.x = walkSpeed;
        else
            velocity.x = -walkSpeed;

        checkOnGround();
        if (onGround) {
            this.acceleration.y = 0;
            this.velocity.y = 0;
        } else {
            this.acceleration.y = Window.getPhysics().getGravity().y;
        }

        this.velocity.y += this.acceleration.y * dt;
        this.velocity.y = Math.max(Math.min(this.velocity.y, this.terminalVelocity.y), -terminalVelocity.y);
        this.rigidbody2D.setVelocity(velocity);
    }

    public void checkOnGround() {
        float width = GameConstants.GRID_WIDTH * 0.7f;
        float yVal = -0.14f;
        onGround = Physics2D.checkOnGround(this.gameObject, width, yVal);
    }

    @Override
    public void beginContact(GameObject collidingObject, Contact contact, Vector2f contactNormal) {
        if (isDead)
            return;

        PlayerController playerController = collidingObject.getComponent(PlayerController.class);
        if (playerController != null) {
            if (!playerController.isDead() && !playerController.isInvincible() && contactNormal.y > 0.58f) {
                playerController.enemyBounce();
                stomp();
            } else if (!playerController.isDead() && !playerController.isInvincible()) {
                playerController.damage();
            }
        } else if (Math.abs(contactNormal.y) < 0.58f)
            goingRight = contactNormal.x < 0;
    }

    public void stomp() {
        stomp(true);
    }

    private void stomp(boolean playSound) {
        this.isDead = true;
        this.velocity.zero();
        this.rigidbody2D.setVelocity(new Vector2f());
        this.rigidbody2D.setAngularVelocity(0);
        this.rigidbody2D.setGravityScale(0.0f);
        this.stateMachine.trigger("stomp");
        this.rigidbody2D.setIsSensor();

        if (playSound)
            AssetPool.getSound("assets/audio/assets_sounds_bump.ogg").play();

    }
}
