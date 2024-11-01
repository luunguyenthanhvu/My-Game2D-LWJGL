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

public class TurtleAI extends Component {

    // TODO: AI component to recycle all this? or physicscontroller
    private transient boolean goingRight;
    private transient Rigidbody2D rigidbody2D;
    private transient float walkSpeed = 0.6f;
    private transient Vector2f velocity = new Vector2f();
    private transient Vector2f acceleration = new Vector2f();
    private transient Vector2f terminalVelocity = new Vector2f(2.1f, 3.1f);
    private transient boolean onGround;
    private transient boolean isDead;
    private transient boolean isMoving;
    private transient StateMachine stateMachine;
    private float movingDebounce = 0.32f;

    @Override
    public void start() {
        this.stateMachine = this.gameObject.getComponent(StateMachine.class);
        this.rigidbody2D = this.gameObject.getComponent(Rigidbody2D.class);
        this.acceleration.y = Window.getPhysics().getGravity().y;
    }

    @Override
    public void update(float dt) {
        movingDebounce -= dt;
        Camera camera = Window.getScene().getCamera();

        // If turtle is right of camera, return (does not update)
        if (this.gameObject.transform.position.x > camera.position.x + camera.getProjectionSize().x +
        camera.getZoom()) {
            return;
        }

        // Check states and update accordingly
        if (!isDead || isMoving) {
            if (goingRight) {
                velocity.x = walkSpeed;
                gameObject.transform.scale.x = -GameConstants.GRID_WIDTH;
            } else {
                velocity.x = -walkSpeed;
                gameObject.transform.scale.x = GameConstants.GRID_WIDTH;
            }
        } else
            velocity.x = 0;

        checkOnGround();
        if (onGround) {
            this.acceleration.y = 0;
            this.velocity.y = 0;
        } else
            this.acceleration.y = Window.getPhysics().getGravity().y;

        this.velocity.y += dt * this.acceleration.y;

        this.velocity.y = Math.max(Math.min(this.velocity.y, this.terminalVelocity.y), -terminalVelocity.y);
        this.rigidbody2D.setVelocity(velocity);

        // Destroy turtle if left or beneath camera
        if (this.gameObject.transform.position.x < Window.getScene().getCamera().position.x -0.5f ||
        this.gameObject.transform.position.y < 0.0f) {
            this.gameObject.destroy();
        }
    }

    public void checkOnGround() {
        float width = GameConstants.GRID_WIDTH * 0.7f;
        float yVal = -0.2f;
        onGround = Physics2D.checkOnGround(this.gameObject, width, yVal);
    }

    public void stomp() {
        this.isDead = true;
        this.isMoving = false;
        this.velocity.zero();
        this.rigidbody2D.setVelocity(this.velocity);
        this.rigidbody2D.setAngularVelocity(0);
        this.rigidbody2D.setGravityScale(0);
        stateMachine.trigger("squashMe");
        AssetPool.getSound("assets/audio/assets_sounds_bump.ogg").play();
    }

    @Override
    public void beginContact(GameObject collidingObject, Contact contact, Vector2f contactNormal) {
        PlayerController playerController = collidingObject.getComponent(PlayerController.class);

        if (playerController != null) {
            if (!isDead && !playerController.isDead() &&
                    !playerController.isInvincible() && contactNormal.y > 0.58f) {
                playerController.enemyBounce();
                stomp();
                walkSpeed *= 3.0f;
            } else if (movingDebounce < 0 && !playerController.isDead()
            && !playerController.isInvincible() && (isMoving || !isDead) && contactNormal.y < 0.58f) {
                playerController.damage();
            } else if (!playerController.isDead() && !playerController.isInvincible()) {
                if (isDead && contactNormal.y > 0.58f) {
                    playerController.enemyBounce();
                    isMoving = isMoving;
                    goingRight = contactNormal.x < 0;
                } else if (isDead && !isMoving) {
                    isMoving = true;
                    goingRight = contactNormal.x < 0;
                    movingDebounce = 0.32f;
                }
            }
        } else if (Math.abs(contactNormal.y) < 0.1f && !gameObject.isDead()) {
            goingRight = contactNormal.x < 0;
            if(isMoving && isDead)
                AssetPool.getSound("assets/audio/assets_sounds_bump.ogg").play();
        }
    }

    @Override
    public void preSolve(GameObject collidingObject, Contact contact, Vector2f contactNormal) {
        GoombaAI goombaAI = collidingObject.getComponent(GoombaAI.class);

        if (isDead && isMoving && goombaAI != null) {
            goombaAI.stomp();
            contact.setEnabled(false);
            AssetPool.getSound("assets/audio/assets_sounds_kick.ogg").play();
        }
    }

}
