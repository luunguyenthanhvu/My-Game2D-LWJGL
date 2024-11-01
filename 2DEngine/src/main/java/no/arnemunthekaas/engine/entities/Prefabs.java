package no.arnemunthekaas.engine.entities;

import no.arnemunthekaas.engine.Window;
import no.arnemunthekaas.engine.entities.components.*;
import no.arnemunthekaas.engine.entities.components.animation.AnimationState;
import no.arnemunthekaas.engine.entities.components.animation.StateMachine;
import no.arnemunthekaas.engine.entities.components.physics2d.BodyType;
import no.arnemunthekaas.engine.entities.components.physics2d.Physics2D;
import no.arnemunthekaas.engine.entities.components.physics2d.components.Box2DCollider;
import no.arnemunthekaas.engine.entities.components.physics2d.components.CircleCollider;
import no.arnemunthekaas.engine.entities.components.physics2d.components.PillboxCollider;
import no.arnemunthekaas.engine.entities.components.physics2d.components.Rigidbody2D;
import no.arnemunthekaas.utils.AssetPool;
import no.arnemunthekaas.utils.GameConstants;
import org.joml.Vector2f;

public class Prefabs {

    // TODO: remove paths from here, instantiate static variables or something
    private static Spritesheet items = AssetPool.getSpritesheet("assets/images/spritesheets/oryx_16bit_fantasy_items_trans.png");
    private static Spritesheet tiles = AssetPool.getSpritesheet("assets/images/spritesheets/oryx_16bit_fantasy_tiles.png");
    private static Spritesheet sprites = AssetPool.getSpritesheet("assets/images/spritesheet.png");
    private static Spritesheet playerSprites = AssetPool.getSpritesheet("assets/images/spritesheet.png");
    private static Spritesheet bigPlayerSprites = AssetPool.getSpritesheet("assets/images/bigSpritesheet.png");
    private static Spritesheet pipes = AssetPool.getSpritesheet("assets/images/img_1.png");
    private static Spritesheet turtles = AssetPool.getSpritesheet("assets/images/turtle.png");
    private static Spritesheet items2 = AssetPool.getSpritesheet("assets/images/items.png");

    /**
     *
     * @param sprite
     * @param sizeX
     * @param sizeY
     * @return
     */
    public static GameObject generateSpriteObject(Sprite sprite, float sizeX, float sizeY) {
        return generateSpriteObject(sprite, sizeX, sizeY, "Sprite_Object_Gen");
    }

    /**
     *
     * @param sprite
     * @param sizeX
     * @param sizeY
     * @param name
     * @return
     */
    public static GameObject generateSpriteObject(Sprite sprite, float sizeX, float sizeY, String name) {
        GameObject gameObject = Window.getScene().createGameObject(name);
        gameObject.transform.scale.x = sizeX;
        gameObject.transform.scale.y = sizeY;

        SpriteRenderer spriteRenderer = new SpriteRenderer();
        spriteRenderer.setSprite(sprite);
        gameObject.addComponent(spriteRenderer);
        return gameObject;
    }

    /**
     *
     * @return
     */
    public static GameObject generatePlayerCharacter() {
        GameObject mario = generateSpriteObject(playerSprites.getSprite(0), GameConstants.GRID_WIDTH, GameConstants.GRID_HEIGHT);

        // Little mario animations
        AnimationState run = new AnimationState();
        run.title = "Run";
        float defaultFrameTime = 0.2f;
        run.addFrame(playerSprites.getSprite(0), defaultFrameTime);
        run.addFrame(playerSprites.getSprite(2), defaultFrameTime);
        run.addFrame(playerSprites.getSprite(3), defaultFrameTime);
        run.addFrame(playerSprites.getSprite(2), defaultFrameTime);
        run.setLoop(true);

        AnimationState switchDirection = new AnimationState();
        switchDirection.title = "Switch Direction";
        switchDirection.addFrame(playerSprites.getSprite(4), 0.1f);
        switchDirection.setLoop(false);

        AnimationState idle = new AnimationState();
        idle.title = "Idle";
        idle.addFrame(playerSprites.getSprite(0), 0.1f);
        idle.setLoop(false);

        AnimationState jump = new AnimationState();
        jump.title = "Jump";
        jump.addFrame(playerSprites.getSprite(5), 0.1f);
        jump.setLoop(false);

        // Big mario animations
        AnimationState bigRun = new AnimationState();
        bigRun.title = "BigRun";
        bigRun.addFrame(bigPlayerSprites.getSprite(0), defaultFrameTime);
        bigRun.addFrame(bigPlayerSprites.getSprite(1), defaultFrameTime);
        bigRun.addFrame(bigPlayerSprites.getSprite(2), defaultFrameTime);
        bigRun.addFrame(bigPlayerSprites.getSprite(3), defaultFrameTime);
        bigRun.addFrame(bigPlayerSprites.getSprite(2), defaultFrameTime);
        bigRun.addFrame(bigPlayerSprites.getSprite(1), defaultFrameTime);
        bigRun.setLoop(true);

        AnimationState bigSwitchDirection = new AnimationState();
        bigSwitchDirection.title = "Big Switch Direction";
        bigSwitchDirection.addFrame(bigPlayerSprites.getSprite(4), 0.1f);
        bigSwitchDirection.setLoop(false);

        AnimationState bigIdle = new AnimationState();
        bigIdle.title = "BigIdle";
        bigIdle.addFrame(bigPlayerSprites.getSprite(0), 0.1f);
        bigIdle.setLoop(false);

        AnimationState bigJump = new AnimationState();
        bigJump.title = "BigJump";
        bigJump.addFrame(bigPlayerSprites.getSprite(5), 0.1f);
        bigJump.setLoop(false);

        // Fire mario animations
        int fireOffset = 21;
        AnimationState fireRun = new AnimationState();
        fireRun.title = "FireRun";
        fireRun.addFrame(bigPlayerSprites.getSprite(fireOffset + 0), defaultFrameTime);
        fireRun.addFrame(bigPlayerSprites.getSprite(fireOffset + 1), defaultFrameTime);
        fireRun.addFrame(bigPlayerSprites.getSprite(fireOffset + 2), defaultFrameTime);
        fireRun.addFrame(bigPlayerSprites.getSprite(fireOffset + 3), defaultFrameTime);
        fireRun.addFrame(bigPlayerSprites.getSprite(fireOffset + 2), defaultFrameTime);
        fireRun.addFrame(bigPlayerSprites.getSprite(fireOffset + 1), defaultFrameTime);
        fireRun.setLoop(true);

        AnimationState fireSwitchDirection = new AnimationState();
        fireSwitchDirection.title = "Fire Switch Direction";
        fireSwitchDirection.addFrame(bigPlayerSprites.getSprite(fireOffset + 4), 0.1f);
        fireSwitchDirection.setLoop(false);

        AnimationState fireIdle = new AnimationState();
        fireIdle.title = "FireIdle";
        fireIdle.addFrame(bigPlayerSprites.getSprite(fireOffset + 0), 0.1f);
        fireIdle.setLoop(false);

        AnimationState fireJump = new AnimationState();
        fireJump.title = "FireJump";
        fireJump.addFrame(bigPlayerSprites.getSprite(fireOffset + 5), 0.1f);
        fireJump.setLoop(false);

        AnimationState die = new AnimationState();
        die.title = "Die";
        die.addFrame(playerSprites.getSprite(6), 0.1f);
        die.setLoop(false);

        StateMachine stateMachine = new StateMachine();
        stateMachine.addState(run);
        stateMachine.addState(idle);
        stateMachine.addState(switchDirection);
        stateMachine.addState(jump);
        stateMachine.addState(die);

        stateMachine.addState(bigRun);
        stateMachine.addState(bigIdle);
        stateMachine.addState(bigSwitchDirection);
        stateMachine.addState(bigJump);

        stateMachine.addState(fireRun);
        stateMachine.addState(fireIdle);
        stateMachine.addState(fireSwitchDirection);
        stateMachine.addState(fireJump);

        stateMachine.setDefaultState(idle.title);
        stateMachine.addTrigger(run.title, switchDirection.title, "switchDirection");
        stateMachine.addTrigger(run.title, idle.title, "stopRunning");
        stateMachine.addTrigger(run.title, jump.title, "jump");
        stateMachine.addTrigger(switchDirection.title, idle.title, "stopRunning");
        stateMachine.addTrigger(switchDirection.title, run.title, "startRunning");
        stateMachine.addTrigger(switchDirection.title, jump.title, "jump");
        stateMachine.addTrigger(idle.title, run.title, "startRunning");
        stateMachine.addTrigger(idle.title, jump.title, "jump");
        stateMachine.addTrigger(jump.title, idle.title, "stopJumping");

        stateMachine.addTrigger(bigRun.title, bigSwitchDirection.title, "switchDirection");
        stateMachine.addTrigger(bigRun.title, bigIdle.title, "stopRunning");
        stateMachine.addTrigger(bigRun.title, bigJump.title, "jump");
        stateMachine.addTrigger(bigSwitchDirection.title, bigIdle.title, "stopRunning");
        stateMachine.addTrigger(bigSwitchDirection.title, bigRun.title, "startRunning");
        stateMachine.addTrigger(bigSwitchDirection.title, bigJump.title, "jump");
        stateMachine.addTrigger(bigIdle.title, bigRun.title, "startRunning");
        stateMachine.addTrigger(bigIdle.title, bigJump.title, "jump");
        stateMachine.addTrigger(bigJump.title, bigIdle.title, "stopJumping");

        stateMachine.addTrigger(fireRun.title, fireSwitchDirection.title, "switchDirection");
        stateMachine.addTrigger(fireRun.title, fireIdle.title, "stopRunning");
        stateMachine.addTrigger(fireRun.title, fireJump.title, "jump");
        stateMachine.addTrigger(fireSwitchDirection.title, fireIdle.title, "stopRunning");
        stateMachine.addTrigger(fireSwitchDirection.title, fireRun.title, "startRunning");
        stateMachine.addTrigger(fireSwitchDirection.title, fireJump.title, "jump");
        stateMachine.addTrigger(fireIdle.title, fireRun.title, "startRunning");
        stateMachine.addTrigger(fireIdle.title, fireJump.title, "jump");
        stateMachine.addTrigger(fireJump.title, fireIdle.title, "stopJumping");

        stateMachine.addTrigger(run.title, bigRun.title, "powerup");
        stateMachine.addTrigger(idle.title, bigIdle.title, "powerup");
        stateMachine.addTrigger(switchDirection.title, bigSwitchDirection.title, "powerup");
        stateMachine.addTrigger(jump.title, bigJump.title, "powerup");
        stateMachine.addTrigger(bigRun.title, fireRun.title, "powerup");
        stateMachine.addTrigger(bigIdle.title, fireIdle.title, "powerup");
        stateMachine.addTrigger(bigSwitchDirection.title, fireSwitchDirection.title, "powerup");
        stateMachine.addTrigger(bigJump.title, fireJump.title, "powerup");

        stateMachine.addTrigger(bigRun.title, run.title, "damage");
        stateMachine.addTrigger(bigIdle.title, idle.title, "damage");
        stateMachine.addTrigger(bigSwitchDirection.title, switchDirection.title, "damage");
        stateMachine.addTrigger(bigJump.title, jump.title, "damage");
        stateMachine.addTrigger(fireRun.title, bigRun.title, "damage");
        stateMachine.addTrigger(fireIdle.title, bigIdle.title, "damage");
        stateMachine.addTrigger(fireSwitchDirection.title, bigSwitchDirection.title, "damage");
        stateMachine.addTrigger(fireJump.title, bigJump.title, "damage");

        stateMachine.addTrigger(run.title, die.title, "die");
        stateMachine.addTrigger(switchDirection.title, die.title, "die");
        stateMachine.addTrigger(idle.title, die.title, "die");
        stateMachine.addTrigger(jump.title, die.title, "die");
        stateMachine.addTrigger(bigRun.title, run.title, "die");
        stateMachine.addTrigger(bigSwitchDirection.title, switchDirection.title, "die");
        stateMachine.addTrigger(bigIdle.title, idle.title, "die");
        stateMachine.addTrigger(bigJump.title, jump.title, "die");
        stateMachine.addTrigger(fireRun.title, bigRun.title, "die");
        stateMachine.addTrigger(fireSwitchDirection.title, bigSwitchDirection.title, "die");
        stateMachine.addTrigger(fireIdle.title, bigIdle.title, "die");
        stateMachine.addTrigger(fireJump.title, bigJump.title, "die");
        mario.addComponent(stateMachine);

        PillboxCollider pillboxCollider = new PillboxCollider();
        pillboxCollider.width = GameConstants.GRID_WIDTH;
        pillboxCollider.height = GameConstants.GRID_HEIGHT;
        Rigidbody2D rigidbody2D = new Rigidbody2D();
        rigidbody2D.setBodyType(BodyType.Dynamic);
        rigidbody2D.setContinuousCollision(false);
        rigidbody2D.setFixedRotation(true);
        rigidbody2D.setMass(25.0f);

        mario.addComponent(rigidbody2D);
        mario.addComponent(pillboxCollider);
        mario.addComponent(new PlayerController());

        return mario;
    }

    public static GameObject generateMoonBlock() {
        GameObject questionBlock = generateSpriteObject(tiles.getSprite(3), GameConstants.GRID_WIDTH, GameConstants.GRID_HEIGHT);

        AnimationState flicker = new AnimationState();
        flicker.title = "Question";
        float defaultFrameTime = 0.23f;
        flicker.addFrame(tiles.getSprite(3), 0.57f);
        flicker.addFrame(tiles.getSprite(4), defaultFrameTime);
        flicker.setLoop(true);

        AnimationState inactive = new AnimationState();
        inactive.title = "Inactive";
        inactive.addFrame(tiles.getSprite(3), 0.1f);
        inactive.setLoop(false);

        StateMachine stateMachine = new StateMachine();
        stateMachine.addState(flicker);
        stateMachine.addState(inactive);
        stateMachine.setDefaultState(flicker.title);
        stateMachine.addTrigger(flicker.title, inactive.title, "setInactive");
        questionBlock.addComponent(stateMachine);
        questionBlock.addComponent(new MoonBlock());

        Rigidbody2D rb = new Rigidbody2D();
        rb.setBodyType(BodyType.Static);
        questionBlock.addComponent(rb);
        Box2DCollider b2d = new Box2DCollider();
        b2d.setHalfSize(new Vector2f(GameConstants.GRID_WIDTH, GameConstants.GRID_HEIGHT));
        questionBlock.addComponent(b2d);
        questionBlock.addComponent(new Ground());

        return questionBlock;
    }

    /**
     *
     * @return
     */
    public static GameObject generateHeart() {
        GameObject heart = generateSpriteObject(items.getSprite(115), GameConstants.GRID_WIDTH, GameConstants.GRID_HEIGHT);

        AnimationState flip = new AnimationState();
        flip.title = "Flip";
        float defaultFrameTime = 0.23f;
        flip.addFrame(items.getSprite(115), 0.57f);
        flip.addFrame(items.getSprite(118), defaultFrameTime);
        flip.addFrame(items.getSprite(117), defaultFrameTime);
        flip.setLoop(true);

        StateMachine stateMachine = new StateMachine();
        stateMachine.addState(flip);
        stateMachine.setDefaultState(flip.title);
        heart.addComponent(stateMachine);
        heart.addComponent(new MoonBlock());

        heart.addComponent(new Heart());

        return heart;
    }

    /**
     *
     * @return
     */
    public static GameObject generateBluePotion() {
        GameObject bluePotion = generateSpriteObject(items.getSprite(25), GameConstants.GRID_WIDTH / 1.5f, GameConstants.GRID_HEIGHT / 1.5f);

        Rigidbody2D rigidbody2D = new Rigidbody2D();
        rigidbody2D.setBodyType(BodyType.Dynamic);
        rigidbody2D.setFixedRotation(true);
        rigidbody2D.setContinuousCollision(false);
        bluePotion.addComponent(rigidbody2D);

        PillboxCollider pillboxCollider = new PillboxCollider();
        pillboxCollider.setHeight(GameConstants.GRID_HEIGHT / 1.1f);
        pillboxCollider.setWidth(GameConstants.GRID_HEIGHT / 2.5f);
        bluePotion.addComponent(pillboxCollider);

        bluePotion.addComponent(new BluePotion());

        return bluePotion;
    }

    /**
     *
     * @return
     */
    public static GameObject generateRedPotion() {
        GameObject redPotion = generateSpriteObject(items.getSprite(27), GameConstants.GRID_WIDTH / 1.5f, GameConstants.GRID_HEIGHT / 1.5f);

        Rigidbody2D rigidbody2D = new Rigidbody2D();
        rigidbody2D.setBodyType(BodyType.Static);
        rigidbody2D.setFixedRotation(true);
        rigidbody2D.setContinuousCollision(false);
        redPotion.addComponent(rigidbody2D);

        PillboxCollider pillboxCollider = new PillboxCollider();
        pillboxCollider.setHeight(GameConstants.GRID_HEIGHT / 1.1f);
        pillboxCollider.setWidth(GameConstants.GRID_HEIGHT / 2.5f);
        redPotion.addComponent(pillboxCollider);

        redPotion.addComponent(new RedPotion());

        return redPotion;
    }

    /**
     *
     * @return
     */
    public static GameObject generateGoomba() {
        // TODO: create rigidbody creation in helper class and use static variables from game objects class
        GameObject goomba = generateSpriteObject(playerSprites.getSprite(14), GameConstants.GRID_WIDTH, GameConstants.GRID_HEIGHT);

        AnimationState walk = new AnimationState();
        walk.title = "walk";
        float defaultFrameTime = 0.23f;
        walk.addFrame(playerSprites.getSprite(14), defaultFrameTime);
        walk.addFrame(playerSprites.getSprite(15), defaultFrameTime);
        walk.setLoop(true);

        AnimationState squashed = new AnimationState();
        squashed.title = "stomped";
        squashed.addFrame(playerSprites.getSprite(16), 0.1f);
        squashed.setLoop(false);

        StateMachine stateMachine = new StateMachine();
        stateMachine.addState(walk);
        stateMachine.addState(squashed);
        stateMachine.setDefaultState(walk.title);
        stateMachine.addTrigger(walk.title, squashed.title, "stomp");
        goomba.addComponent(stateMachine);

        Rigidbody2D rigidbody2D = new Rigidbody2D();
        rigidbody2D.setBodyType(BodyType.Dynamic);
        rigidbody2D.setMass(0.1f);
        rigidbody2D.setFixedRotation(true);
        goomba.addComponent(rigidbody2D);

        CircleCollider circleCollider = new CircleCollider();
        circleCollider.setRadius(0.12f);
        goomba.addComponent(circleCollider);

        goomba.addComponent(new GoombaAI());

        return goomba;
    }

    /**
     *
     * @return
     */
    public static GameObject generatePipe(Direction direction) {
        int index = direction == Direction.Down ? 0 :
                direction == Direction.Up ? 1:
                        direction == Direction.Right ? 2:
                                direction == Direction.Left ? 3: -1;
//        assert index == -1 : "Invalid pipe direction: " + direction;

        GameObject pipe = generateSpriteObject(pipes.getSprite(index), GameConstants.GRID_WIDTH * 2, GameConstants.GRID_HEIGHT * 2);

        Rigidbody2D rigidbody2D = new Rigidbody2D();
        rigidbody2D.setBodyType(BodyType.Static);
        rigidbody2D.setFixedRotation(true);
        rigidbody2D.setContinuousCollision(false);
        pipe.addComponent(rigidbody2D);

        Box2DCollider box2DCollider = new Box2DCollider();
        box2DCollider.setHalfSize(new Vector2f(GameConstants.GRID_WIDTH * 2, GameConstants.GRID_HEIGHT * 2));
        pipe.addComponent(box2DCollider);
        pipe.addComponent(new Pipe(direction));
        pipe.addComponent(new Ground());

        return pipe;
    }

    /**
     *
     * @return
     */
    public static GameObject generateTurtle() {
        GameObject turtle = generateSpriteObject(turtles.getSprite(0), GameConstants.GRID_WIDTH, GameConstants.GRID_HEIGHT * 1.5f);

        AnimationState walk = new AnimationState();
        walk.title = "walk";
        float defaultFrameTime = 0.23f;
        walk.addFrame(turtles.getSprite(0), defaultFrameTime);
        walk.addFrame(turtles.getSprite(1), defaultFrameTime);
        walk.setLoop(true);

        AnimationState squashed = new AnimationState();
        squashed.title = "TurtleShellSpin";
        squashed.addFrame(turtles.getSprite(2), 0.1f);
        squashed.setLoop(false);

        StateMachine stateMachine = new StateMachine();
        stateMachine.addState(walk);
        stateMachine.addState(squashed);
        stateMachine.setDefaultState(walk.title);
        stateMachine.addTrigger(walk.title, squashed.title, "squashMe");
        turtle.addComponent(stateMachine);

        Rigidbody2D rigidbody2D = new Rigidbody2D();
        rigidbody2D.setBodyType(BodyType.Dynamic);
        rigidbody2D.setMass(0.1f);
        rigidbody2D.setFixedRotation(true);
        turtle.addComponent(rigidbody2D);

        CircleCollider circleCollider = new CircleCollider();
        circleCollider.setRadius(0.13f);
        circleCollider.setOffset(new Vector2f(0, -0.05f));
        turtle.addComponent(circleCollider);

        turtle.addComponent(new TurtleAI());

        return turtle;
    }

    /**
     *
     * @return
     */
    public static GameObject generateFlagTop() {
        GameObject flagTop = generateSpriteObject(items2.getSprite(6), GameConstants.GRID_WIDTH, GameConstants.GRID_HEIGHT);


        Rigidbody2D rigidbody2D = new Rigidbody2D();
        rigidbody2D.setBodyType(BodyType.Dynamic);
        rigidbody2D.setFixedRotation(true);
        rigidbody2D.setContinuousCollision(false);
        flagTop.addComponent(rigidbody2D);

        Box2DCollider box2DCollider = new Box2DCollider();
        box2DCollider.setHalfSize(new Vector2f(0.1f, 0.2355f));
        box2DCollider.setOffset(new Vector2f(-0.075f, 0.0f));
        flagTop.addComponent(box2DCollider);
        flagTop.addComponent(new Flagpole(true));

        return flagTop;
    }

    /**
     *
     * @return
     */
    public static GameObject generateFlagPole() {
        GameObject flagTop = generateSpriteObject(items2.getSprite(33), GameConstants.GRID_WIDTH, GameConstants.GRID_HEIGHT);


        Rigidbody2D rigidbody2D = new Rigidbody2D();
        rigidbody2D.setBodyType(BodyType.Dynamic);
        rigidbody2D.setFixedRotation(true);
        rigidbody2D.setContinuousCollision(false);
        flagTop.addComponent(rigidbody2D);

        Box2DCollider box2DCollider = new Box2DCollider();
        box2DCollider.setHalfSize(new Vector2f(0.1f, 0.2355f));
        box2DCollider.setOffset(new Vector2f(-0.075f, 0.0f));
        flagTop.addComponent(box2DCollider);
        flagTop.addComponent(new Flagpole(false));

        return flagTop;
    }
}
