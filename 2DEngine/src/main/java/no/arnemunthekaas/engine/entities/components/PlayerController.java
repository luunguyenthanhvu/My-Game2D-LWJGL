package no.arnemunthekaas.engine.entities.components;

import no.arnemunthekaas.engine.Window;
import no.arnemunthekaas.engine.entities.GameObject;
import no.arnemunthekaas.engine.entities.components.animation.StateMachine;
import no.arnemunthekaas.engine.entities.components.physics2d.BodyType;
import no.arnemunthekaas.engine.entities.components.physics2d.Physics2D;
import no.arnemunthekaas.engine.entities.components.physics2d.components.PillboxCollider;
import no.arnemunthekaas.engine.entities.components.physics2d.components.Rigidbody2D;
import no.arnemunthekaas.engine.events.listeners.KeyListener;
import no.arnemunthekaas.scenes.LevelEditorSceneInitializer;
import no.arnemunthekaas.scenes.LevelSceneInitializer;
import no.arnemunthekaas.utils.AssetPool;
import no.arnemunthekaas.utils.GameConstants;
import org.jbox2d.dynamics.contacts.Contact;
import org.joml.Vector2f;
import org.joml.Vector4f;

import static org.lwjgl.glfw.GLFW.*;


public class PlayerController extends Component {

    private transient boolean playWinAnimation;
    private transient float timeToCastle = 4.5f;
    private transient float walkTime = 2.2f;

    /**
     *
     * @param flagpole
     */
    public void playWinAnimation(GameObject flagpole) {
        if (!playWinAnimation) {
            playWinAnimation = true;
            velocity.set(0.0f, 0.0f);
            acceleration.set(0.0f, 0.0f);
            rigidbody2D.setVelocity(velocity);
            rigidbody2D.setIsSensor();
            rigidbody2D.setBodyType(BodyType.Static);
            gameObject.transform.position.x = flagpole.transform.position.x;
            AssetPool.getSound("assets/audio/flagpole.ogg").play();
        }
    }

    private enum PlayerState {
        Small,
        Big,
        Fire,
        Invincible
    }

    public float walkSpeed = 1.9f;
    public float jumpBoost = 1.0f;
    public float jumpImpulse = 3.0f;
    public float slowDownForce = 0.05f;
    public Vector2f terminalVelocity = new Vector2f(2.1f, 3.1f);
    private PlayerState playerState = PlayerState.Small;


    public transient boolean onGround;
    private transient float groundDebounce = 0.0f;
    private transient float groundDebounceTime = 0.1f;
    private transient Rigidbody2D rigidbody2D;
    private transient StateMachine stateMachine;
    private transient float bigJumpBoostFactor = 1.05f;
    private transient float playerWidth = GameConstants.GRID_WIDTH;
    private transient int jumpTime = 0;
    private transient Vector2f acceleration = new Vector2f();
    private transient Vector2f velocity = new Vector2f();
    private transient boolean isDead;
    private transient int enemyBounce = 0;

    private transient float invincibilityTimeLeft = 0;
    private transient float invincibilityTime = 1.4f;
    private transient float deadMaxHeight = 0;
    private transient float deadMinHeight = 0;
    private transient boolean deadGoingUp = true;
    private transient float blinkTime = 0.0f;
    private transient SpriteRenderer spriteRenderer;


    @Override
    public void start() {
        this.rigidbody2D = gameObject.getComponent(Rigidbody2D.class);
        this.stateMachine = gameObject.getComponent(StateMachine.class);
        this.rigidbody2D.setGravityScale(0.0f);
        this.spriteRenderer = gameObject.getComponent(SpriteRenderer.class);
    }

    @Override
    public void update(float dt) {

        if (playWinAnimation) {
            checkOnGround();
            if (!onGround) {
                gameObject.transform.scale.x = -0.25f;
                gameObject.transform.position.y -= dt;
                stateMachine.trigger("stopRunning");
                stateMachine.trigger("stopJumping");
            } else {
                if (this.walkTime > 0) {
                    gameObject.transform.scale.x = 0.25f;
                    gameObject.transform.position.x += dt;
                    stateMachine.trigger("startRunning");
                }
                if (!AssetPool.getSound("assets/audio/stage_clear.ogg").isPlaying()) {
                    AssetPool.getSound("assets/audio/stage_clear.ogg").play();
                }
                timeToCastle -= dt;
                walkTime -= dt;

                if (timeToCastle <= 0) {
                    Window.changeScene(new LevelEditorSceneInitializer());
                }
            }

            return;
        }

        if (isDead) {
            if (this.gameObject.transform.position.y < deadMaxHeight && deadGoingUp) {
                this.gameObject.transform.position.y += dt * walkSpeed / 2.0f;
            } else if (this.gameObject.transform.position.y >= deadMaxHeight && deadGoingUp) {
                deadGoingUp = false;
            } else if (!deadGoingUp && gameObject.transform.position.y > deadMinHeight) {
                this.rigidbody2D.setBodyType(BodyType.Kinematic);
                this.acceleration.y = Window.getPhysics().getGravity().y * 0.7f;
                this.velocity.y += this.acceleration.y * dt;
                this.velocity.y = Math.max(Math.min(this.velocity.y, this.terminalVelocity.y), -this.terminalVelocity.y);
                this.rigidbody2D.setVelocity(this.velocity);
                this.rigidbody2D.setAngularVelocity(0);
            } else if (!deadGoingUp && gameObject.transform.position.y <= deadMinHeight) {
                Window.changeScene(new LevelSceneInitializer());
            }
            return;
        }

        if (invincibilityTimeLeft > 0) {
            invincibilityTimeLeft -= dt;
            blinkTime -= dt;

            if (blinkTime <= 0) {
                blinkTime = 0.2f;
                if(spriteRenderer.getColor().w == 1)
                    spriteRenderer.setColor(new Vector4f(1, 1, 1, 0));
                else
                    spriteRenderer.setColor(new Vector4f(1, 1, 1, 1));
            } else {
                if(spriteRenderer.getColor().w == 0)
                    spriteRenderer.setColor(new Vector4f(1, 1, 1, 1));
            }
        }

        if (KeyListener.isKeyPressed(GLFW_KEY_RIGHT) || KeyListener.isKeyPressed(GLFW_KEY_D)) {
            this.gameObject.transform.scale.x = playerWidth;
            this.acceleration.x = walkSpeed;

            if (this.velocity.x < 0) {
                this.stateMachine.trigger("switchDirection");
                this.velocity.x += slowDownForce;
            } else {
                this.stateMachine.trigger("startRunning");
            }
    // TODO : Add keybinds
        } else if (KeyListener.isKeyPressed(GLFW_KEY_LEFT) || KeyListener.isKeyPressed(GLFW_KEY_A)) {
            this.gameObject.transform.scale.x = - playerWidth;
            this.acceleration.x = - walkSpeed;

            if (this.velocity.x > 0) {
                this.stateMachine.trigger("switchDirection");
                this.velocity.x -= slowDownForce;
            } else {
                this.stateMachine.trigger("startRunning");
            }

        } else {
            this.acceleration.x = 0;
            if (this.velocity.x > 0)
                this.velocity.x = Math.max(0, this.velocity.x - slowDownForce);
            else if (this.velocity.x < 0)
                this.velocity.x = Math.min(0, this.velocity.x + slowDownForce);
        }

        if(this.velocity.x == 0)
            this.stateMachine.trigger("stopRunning");


        checkOnGround();
        if(KeyListener.isKeyPressed(GLFW_KEY_SPACE) && (jumpTime > 0 || onGround || groundDebounce > 0)) {
            if ((onGround || groundDebounce > 0) && jumpTime == 0) {
                AssetPool.getSound("assets/audio/assets_sounds_jump-small.ogg").play();
                jumpTime = 64;
                this.velocity.y = jumpImpulse;
            } else if (jumpTime > 0) {
                jumpTime--;
                this.velocity.y = (jumpTime /2.2f) * jumpBoost;
            } else {
                this.velocity.y = 0;
            }
            groundDebounce = 0;
        } else if (enemyBounce > 0) {
            enemyBounce--;
            this.velocity.y = (enemyBounce/2.2f) * jumpBoost;
        } else if (!onGround) {
            if (this.jumpTime > 0) {
                this.velocity.y *= 0.35f;
                this.jumpTime = 0;
            }
            groundDebounce -= dt;
            this.acceleration.y = Window.getPhysics().getGravity().y;
        } else {
            this.velocity.y = 0;
            this.acceleration.y = 0;
            groundDebounce = groundDebounceTime;
        }


        this.acceleration.y = Window.getPhysics().getGravity().y;

        this.velocity.x += this.acceleration.x * dt;
        this.velocity.y += this.acceleration.y * dt;
        this.velocity.x = Math.max(Math.min(this.velocity.x, this.terminalVelocity.x), -this.terminalVelocity.x);
        this.velocity.y = Math.max(Math.min(this.velocity.y, this.terminalVelocity.y), -this.terminalVelocity.y);

        this.rigidbody2D.setVelocity(this.velocity);
        this.rigidbody2D.setAngularVelocity(0);

        if (!onGround)
            stateMachine.trigger("jump");
        else
            stateMachine.trigger("stopJumping");
    }

    @Override
    public void beginContact(GameObject collidingObject, Contact contact, Vector2f contactNormal) {
        if (isDead) return;

        if (collidingObject.getComponent(Ground.class) != null) {
            if (Math.abs(contactNormal.x) > 0.8f) {
                this.velocity.x = 0;
            } else if (contactNormal.y > 0.8f) {
                this.velocity.y = 0;
                this.acceleration.y = 0;
                this.jumpTime = 0;
            }
        }
    }

    /**
     *
     * @return
     */
    public boolean isSmall() {
        return this.playerState == PlayerState.Small;
    }

    /**
     *
     * @return
     */
    public boolean isBig() {
        return this.playerState == PlayerState.Big;
    }

    /**
     *
     * @return
     */
    public boolean isDead() {
        return this.isDead;
    }

    /**
     *
     * @return
     */
    public boolean isInvincible() {
        return this.playerState == PlayerState.Invincible || invincibilityTimeLeft > 0 || playWinAnimation;
    }


    /**
     *
     * @param consumable
     */
    public void consume(Consumable consumable) {
        if (consumable instanceof Potion) {

                if (playerState == PlayerState.Small) {
                    playerState = PlayerState.Big;
                    AssetPool.getSound("assets/audio/mario1up.ogg").play();
                    gameObject.transform.scale.y = 0.42f;
                    PillboxCollider pillboxCollider = gameObject.getComponent(PillboxCollider.class);

                    if(pillboxCollider != null) {
                        jumpBoost *= bigJumpBoostFactor;
                        walkSpeed *= bigJumpBoostFactor;
                        pillboxCollider.setHeight(0.63f);
                    }
                } else if (playerState == PlayerState.Big) {
                    AssetPool.getSound("assets/audio/mario1up.ogg").play();
                    // TODO: inventory system
                }

                stateMachine.trigger("powerup");

        }
    }

    private void checkOnGround() {
        float innerPlayerWidth = this.playerWidth * 0.6f;
        float yVal = playerState == PlayerState.Small ? -0.14f : -0.24f;
        onGround = Physics2D.checkOnGround(this.gameObject, innerPlayerWidth, yVal);
    }

    /**
     *
     */
    public void enemyBounce() {
        this.enemyBounce = 8;
    }

    /**
     *
     */
    public void damage() {
        // TODO: refactor
        // TODO: fix bug where fire mario shrinks when damaged
        this.stateMachine.trigger("die");
        if (isSmall()) {
            killPlayer();
        } else if (isBig()) {
            this.playerState = PlayerState.Small;
            this.gameObject.transform.scale.y = GameConstants.GRID_HEIGHT;
            PillboxCollider pillboxCollider = this.gameObject.getComponent(PillboxCollider.class);
            if (pillboxCollider != null) {
                jumpBoost /= bigJumpBoostFactor;
                walkSpeed /= bigJumpBoostFactor;
                pillboxCollider.setHeight(GameConstants.GRID_HEIGHT);
            }
            invincibilityTimeLeft = invincibilityTime;
            AssetPool.getSound("assets/audio/assets_sounds_pipe.ogg").play();

        } else if (this.playerState == PlayerState.Fire) {
            this.playerState = PlayerState.Big;
            invincibilityTimeLeft = invincibilityTime;
            AssetPool.getSound("assets/audio/assets_sounds_pipe.ogg").play();
        }
    }

    private void killPlayer() {
        this.velocity.zero();
        this.acceleration.zero();
        this.rigidbody2D.setVelocity(new Vector2f());
        this.isDead = true;
        this.rigidbody2D.setIsSensor();
        AssetPool.getSound("assets/audio/assets_sounds_mario_die.ogg").play();
        deadMaxHeight = this.gameObject.transform.position.y + 0.3f;
        this.rigidbody2D.setBodyType(BodyType.Static);
        if (gameObject.transform.position.y > 0) {
            deadMinHeight = -GameConstants.GRID_HEIGHT;
        }
    }

    /**
     * Set new player position
     * @param newPosition
     */
    public void setPosition(Vector2f newPosition) {
        this.gameObject.transform.position.set(newPosition);
        this.rigidbody2D.setPosition(newPosition);
    }

    /**
     * Check if player has won
     * @return
     */
    public boolean hasWon() {
        return false;
    }

}
