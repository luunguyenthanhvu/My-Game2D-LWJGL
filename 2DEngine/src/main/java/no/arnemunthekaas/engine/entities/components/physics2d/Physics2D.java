package no.arnemunthekaas.engine.entities.components.physics2d;

import no.arnemunthekaas.engine.Window;
import no.arnemunthekaas.engine.entities.GameObject;
import no.arnemunthekaas.engine.entities.components.Ground;
import no.arnemunthekaas.engine.entities.components.Transform;
import no.arnemunthekaas.engine.entities.components.physics2d.components.*;
import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.*;
import org.joml.Vector2f;

import static no.arnemunthekaas.engine.entities.components.physics2d.BodyType.*;
import static org.jbox2d.dynamics.BodyType.*;


public class Physics2D {



    private Vec2 gravity = new Vec2(0, -10.0f);
    private World world = new World(gravity);

    private float physicsTime = 0.0f;
    private float physicsTimeStep = 1.0f / 60.0f;
    private int velocityIterations = 8;
    private int positionIterations = 3;

    public Physics2D() {
        world.setContactListener(new ContactListenerImpl());
    }

    /**
     *
     * @param go
     */
    public void add(GameObject go) {
        Rigidbody2D rb = go.getComponent(Rigidbody2D.class);
        if(rb != null && rb.getRawBody() == null) {
            Transform transform = go.transform;

            BodyDef bodyDef = new BodyDef();
            bodyDef.angle = (float) Math.toRadians(transform.rotation);
            bodyDef.position.set(transform.position.x, transform.position.y);
            bodyDef.angularDamping = rb.getAngularDamping();
            bodyDef.linearDamping = rb.getLinearDamping();
            bodyDef.fixedRotation = rb.isFixedRotation();
            bodyDef.bullet = rb.isContinuousCollision();
            bodyDef.gravityScale = rb.gravityScale;
            bodyDef.angularVelocity = rb.angularVelocity;
            bodyDef.userData = rb.gameObject;


            switch (rb.getBodyType()) {
                case Kinematic: bodyDef.type = KINEMATIC; break;
                case Static: bodyDef.type = STATIC; break;
                case Dynamic: bodyDef.type = DYNAMIC; break;
            }

            Body body = this.world.createBody(bodyDef);
            body.m_mass = rb.getMass();
            rb.setRawBody(body);

            CircleCollider circleCollider;
            Box2DCollider boxCollider;
            PillboxCollider pillboxCollider;

            if((circleCollider = go.getComponent(CircleCollider.class)) != null) {
                addCircleCollider(rb, circleCollider);
            }

            if ((boxCollider = go.getComponent(Box2DCollider.class)) != null) {
                addBox2dCollider(rb, boxCollider);
            }

            if ((pillboxCollider = go.getComponent(PillboxCollider.class)) != null) {
                addPillboxCollider(rb, pillboxCollider);
            }

        }
    }



    /**
     *
     * @param go
     */
    public void destroyGameObject(GameObject go) {
        Rigidbody2D rb = go.getComponent(Rigidbody2D.class);
        if (rb != null) {
            if (rb.getRawBody() != null) {
                world.destroyBody(rb.getRawBody());
                rb.setRawBody(null);
            }
        }
    }

    /**
     *
     * @param dt
     */
    public void update(float dt) {
        physicsTime += dt;

        if (physicsTime >= 0.0f) {
            physicsTime -= physicsTimeStep;
            world.step(physicsTimeStep, velocityIterations, positionIterations);
        }
    }

    /**
     *
     * @param rigidbody2D
     * @param box2DCollider
     */
    public void resetBox2DCollider(Rigidbody2D rigidbody2D, Box2DCollider box2DCollider) {
        Body body = rigidbody2D.getRawBody();
        if(body == null)
            return;

        int size = fixtureListSize(body);
        for(int i = 0; i < size; i++) {
            body.destroyFixture(body.getFixtureList());
        }

        addBox2dCollider(rigidbody2D, box2DCollider);
        body.resetMassData();
    }

    /**
     *
     * @param rigidbody2D
     * @param box2DCollider
     */
    public void addBox2dCollider(Rigidbody2D rigidbody2D, Box2DCollider box2DCollider) {
        Body body = rigidbody2D.getRawBody();
        assert body != null : "Raw body must not be null";

        PolygonShape shape = new PolygonShape();
        Vector2f halfSize = new Vector2f(box2DCollider.getHalfSize()).mul(0.5f);
        Vector2f offset = box2DCollider.getOffset();
        Vector2f origin = new Vector2f(box2DCollider.getOrigin());
        shape.setAsBox(halfSize.x, halfSize.y, new Vec2(offset.x, offset.y), 0);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1.0f; // TODO: implement customizable physics density?
        fixtureDef.friction = rigidbody2D.getFriction();
        fixtureDef.userData = box2DCollider.gameObject;
        fixtureDef.isSensor = rigidbody2D.isSensor();
        body.createFixture(fixtureDef);
    }

    /**
     *
     * @param rigidbody2D
     * @param circleCollider
     */
    public void resetCircleCollider(Rigidbody2D rigidbody2D, CircleCollider circleCollider) {
        Body body = rigidbody2D.getRawBody();
        if(body == null)
            return;

        int size = fixtureListSize(body);
        for(int i = 0; i < size; i++) {
            body.destroyFixture(body.getFixtureList());
        }

        addCircleCollider(rigidbody2D, circleCollider);
        body.resetMassData();
    }

    /**
     *
     * @param rigidbody2D
     * @param circleCollider
     */
    public void addCircleCollider(Rigidbody2D rigidbody2D, CircleCollider circleCollider) {
        Body body = rigidbody2D.getRawBody();
        assert body != null : "Raw body must not be null";

        CircleShape shape = new CircleShape();
        shape.setRadius(circleCollider.getRadius());
        shape.m_p.set(circleCollider.getOffset().x, circleCollider.getOffset().y);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1.0f; // TODO: implement customizable physics density?
        fixtureDef.friction = rigidbody2D.getFriction();
        fixtureDef.userData = circleCollider.gameObject;
        fixtureDef.isSensor = rigidbody2D.isSensor();
        body.createFixture(fixtureDef);
    }

    /**
     *
     * @param requestingObject
     * @param start
     * @param end
     * @return
     */
    public RaycastInfo raycast(GameObject requestingObject, Vector2f start, Vector2f end) {
        RaycastInfo callback = new RaycastInfo(requestingObject);
        world.raycast(callback, new Vec2(start.x, start.y), new Vec2(end.x, end.y));
        return callback;
    }


    private int fixtureListSize(Body body) {
        int size = 0;
        Fixture fixture = body.getFixtureList();

        while(fixture != null) {
            size++;
            fixture = fixture.m_next;
        }

        return size;
    }

    /**
     *
     * @param rigidbody2D
     */
    public void setNotSensor(Rigidbody2D rigidbody2D) {
        Body body = rigidbody2D.getRawBody();
        if (body == null) return;

        Fixture fixture = body.getFixtureList();
        while (fixture != null) {
            fixture.m_isSensor = false;
            fixture = fixture.m_next;
        }
    }

    /**
     *
     * @param rigidbody2D
     */
    public void setIsSensor(Rigidbody2D rigidbody2D) {
        Body body = rigidbody2D.getRawBody();
        if (body == null) return;

        Fixture fixture = body.getFixtureList();
        while (fixture != null) {
            fixture.m_isSensor = true;
            fixture = fixture.m_next;
        }
    }

    /**
     *
     * @return
     */
    public boolean isLocked() {
        return world.isLocked();
    }

    /**
     *
     * @param rigidbody2D
     * @param pillboxCollider
     */
    public void addPillboxCollider(Rigidbody2D rigidbody2D, PillboxCollider pillboxCollider) {
        Body body = rigidbody2D.getRawBody();
        assert body != null : "Raw body must not be null";

        addBox2dCollider(rigidbody2D, pillboxCollider.getBox());
        addCircleCollider(rigidbody2D, pillboxCollider.getTopCircle());
        addCircleCollider(rigidbody2D, pillboxCollider.getBottomCircle());
    }

    /**
     *
     * @param rigidbody2D
     * @param pillboxCollider
     */
    public void resetPillboxCollider(Rigidbody2D rigidbody2D, PillboxCollider pillboxCollider) {
        Body body = rigidbody2D.getRawBody();
        if(body == null)
            return;

        int size = fixtureListSize(body);
        for(int i = 0; i < size; i++) {
            body.destroyFixture(body.getFixtureList());
        }

        addPillboxCollider(rigidbody2D, pillboxCollider);
        body.resetMassData();
    }

    /**
     *
     * @return
     */
    public Vector2f getGravity() {
        return new Vector2f(this.gravity.x, this.gravity.y);
    }

    /**
     *
     * @param gameObject
     * @param width
     * @param height
     * @return
     */
    public static boolean checkOnGround(GameObject gameObject, float width, float height) {
        Vector2f raycastBegin = new Vector2f(gameObject.transform.position);
        raycastBegin.sub(width / 2.0f, 0.0f);

        float yVal = height;
        Vector2f raycastEnd = new Vector2f(raycastBegin).add(0.0f, yVal);

        RaycastInfo info = Window.getPhysics().raycast(gameObject, raycastBegin, raycastEnd);

        Vector2f raycast2Begin = new Vector2f(raycastBegin).add(width, 0.0f);
        Vector2f raycast2End = new Vector2f(raycastEnd).add(width, 0.0f);
        RaycastInfo info2 = Window.getPhysics().raycast(gameObject, raycast2Begin, raycast2End);

        boolean onGround = (info.hit && info.hitObject != null && info.hitObject.getComponent(Ground.class) != null) ||
                (info2.hit && info2.hitObject != null && info2.hitObject.getComponent(Ground.class) != null);

//        DebugDraw.addLine2D(raycastBegin, raycastEnd, new Vector3f(1, 0, 0));
//        DebugDraw.addLine2D(raycast2Begin, raycast2End, new Vector3f(1, 0, 0));

        return onGround;
    }
}
