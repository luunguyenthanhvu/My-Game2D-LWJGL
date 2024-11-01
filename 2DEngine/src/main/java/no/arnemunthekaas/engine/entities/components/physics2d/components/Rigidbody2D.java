package no.arnemunthekaas.engine.entities.components.physics2d.components;

import no.arnemunthekaas.engine.Window;
import no.arnemunthekaas.engine.entities.components.Component;
import no.arnemunthekaas.engine.entities.components.physics2d.BodyType;
import no.arnemunthekaas.engine.entities.components.physics2d.Physics2D;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.joml.Vector2f;

public class Rigidbody2D extends Component {
    private Vector2f velocity = new Vector2f();
    private float angularDamping = 0.8f;
    private float linearDamping = 0.9f;
    private float mass = 0;
    private BodyType bodyType = BodyType.Dynamic;
    private float friction = 0.1f;
    public float angularVelocity = 0.0f;
    public float gravityScale = 1.0f;
    private boolean isSensor = false;

    private boolean fixedRotation = false;
    private boolean continuousCollision = true;

    private transient Body rawBody = null;

    @Override
    public void update(float dt) {
        if(rawBody != null) {
            if (this.bodyType == BodyType.Dynamic || this.bodyType == BodyType.Kinematic) {
                this.gameObject.transform.position.set(rawBody.getPosition().x, rawBody.getPosition().y);
                this.gameObject.transform.rotation = (float) Math.toDegrees(rawBody.getAngle());
                Vec2 vel = rawBody.getLinearVelocity();
                this.velocity.set(vel.x, vel.y);
            } else if (this.bodyType == BodyType.Static) {
                this.rawBody.setTransform(new Vec2(this.gameObject.transform.position.x, this.gameObject.transform.position.y), gameObject.transform.rotation);
            }
        }
    }

    /**
     *
     * @return
     */
    public Vector2f getVelocity() {
        return velocity;
    }

    /**
     *
     * @param velocity
     */
    public void setVelocity(Vector2f velocity) {
        this.velocity.set(velocity);
        if(rawBody != null) {
            this.rawBody.setLinearVelocity(new Vec2(velocity.x, velocity.y));
        }
    }

    /**
     *
     * @param angularVelocity
     */
    public void setAngularVelocity(float angularVelocity) {
        this.angularVelocity = angularVelocity;
        if(rawBody != null) {
            this.rawBody.setAngularVelocity(angularVelocity);
        }
    }

    /**
     *
     * @param gravityScale
     */
    public void setGravityScale(float gravityScale) {
        this.gravityScale = gravityScale;
        if(rawBody != null) {
            this.rawBody.setGravityScale(gravityScale);
        }
    }

    /**
     *
     */
    public void setIsSensor() {
        this.isSensor = true;
        if(rawBody != null) {
            Window.getPhysics().setIsSensor(this);
        }
    }

    /**
     *
     */
    public void setNotSensor() {
        this.isSensor = false;
        if(rawBody != null) {
            Window.getPhysics().setNotSensor(this);
        }
    }

    /**
     *
     * @return
     */
    public boolean isSensor() {
        return this.isSensor;
    }



    /**
     *
     * @return
     */
    public float getAngularDamping() {
        return angularDamping;
    }

    /**
     *
     */
    public void setAngularDamping(float angularDamping) {
        this.angularDamping = angularDamping;
    }

    /**
     *
     * @return
     */
    public float getLinearDamping() {
        return linearDamping;
    }

    /**
     *
     * @param linearDamping
     */
    public void setLinearDamping(float linearDamping) {
        this.linearDamping = linearDamping;
    }

    /**
     *
     * @return
     */
    public float getMass() {
        return mass;
    }

    /**
     *
     * @param mass
     */
    public void setMass(float mass) {
        this.mass = mass;
    }

    /**
     *
     * @return
     */
    public BodyType getBodyType() {
        return bodyType;
    }

    /**
     *
     * @param bodyType
     */
    public void setBodyType(BodyType bodyType) {
        this.bodyType = bodyType;
    }

    /**
     *
     * @return
     */
    public boolean isFixedRotation() {
        return fixedRotation;
    }

    /**
     *
     * @param fixedRotation
     */
    public void setFixedRotation(boolean fixedRotation) {
        this.fixedRotation = fixedRotation;
    }

    /**
     *
     * @return
     */
    public boolean isContinuousCollision() {
        return continuousCollision;
    }

    /**
     *
     * @param continuousCollision
     */
    public void setContinuousCollision(boolean continuousCollision) {
        this.continuousCollision = continuousCollision;
    }

    /**
     *
     * @return
     */
    public Body getRawBody() {
        return rawBody;
    }

    /**
     *
     * @param rawBody
     */
    public void setRawBody(Body rawBody) {
        this.rawBody = rawBody;
    }

    /**
     *
     * @return
     */
    public float getFriction() {
        return this.friction;
    }

    /**
     *
     * @param force
     */
    public void addForce(Vector2f force) {
        if(rawBody != null)
            rawBody.applyForceToCenter(new Vec2(force.x, force.y));
    }

    /**
     *
     * @param force
     */
    public void addImpulse(Vector2f force) {
        if(rawBody != null)
            rawBody.applyLinearImpulse(new Vec2(force.x, force.y), rawBody.getWorldCenter());
    }

    /**
     * Change position
     * @param newPosition
     */
    public void setPosition(Vector2f newPosition) {
        if (rawBody != null) {
            rawBody.setTransform(new Vec2(newPosition.x, newPosition.y), gameObject.transform.rotation);
        }
    }
}
