package no.arnemunthekaas.engine.entities.components.physics2d;

import no.arnemunthekaas.engine.entities.GameObject;
import no.arnemunthekaas.engine.entities.components.Component;
import org.jbox2d.callbacks.ContactImpulse;
import org.jbox2d.callbacks.ContactListener;
import org.jbox2d.collision.Manifold;
import org.jbox2d.collision.WorldManifold;
import org.jbox2d.dynamics.contacts.Contact;
import org.joml.Vector2f;

public class ContactListenerImpl implements ContactListener {

    @Override
    public void beginContact(Contact contact) {
        GameObject objectA = (GameObject) contact.getFixtureA().getUserData();
        GameObject objectB = (GameObject) contact.getFixtureB().getUserData();
        WorldManifold worldManifold = new WorldManifold();
        contact.getWorldManifold(worldManifold);
        Vector2f aNormal = new Vector2f(worldManifold.normal.x, worldManifold.normal.y);
        Vector2f bNormal = new Vector2f(aNormal).negate();

        for (Component component : objectA.getComponents()) {
            component.beginContact(objectB, contact, aNormal);
        }

        for (Component component : objectB.getComponents()) {
            component.beginContact(objectA, contact, bNormal);
        }
    }

    @Override
    public void endContact(Contact contact) {
        GameObject objectA = (GameObject) contact.getFixtureA().getUserData();
        GameObject objectB = (GameObject) contact.getFixtureB().getUserData();
        WorldManifold worldManifold = new WorldManifold();
        contact.getWorldManifold(worldManifold);
        Vector2f aNormal = new Vector2f(worldManifold.normal.x, worldManifold.normal.y);
        Vector2f bNormal = new Vector2f(aNormal).negate();

        for (Component component : objectA.getComponents()) {
            component.endContact(objectB, contact, aNormal);
        }

        for (Component component : objectB.getComponents()) {
            component.endContact(objectA, contact, bNormal);
        }
    }

    @Override
    public void preSolve(Contact contact, Manifold manifold) {
        GameObject objectA = (GameObject) contact.getFixtureA().getUserData();
        GameObject objectB = (GameObject) contact.getFixtureB().getUserData();
        WorldManifold worldManifold = new WorldManifold();
        contact.getWorldManifold(worldManifold);
        Vector2f aNormal = new Vector2f(worldManifold.normal.x, worldManifold.normal.y);
        Vector2f bNormal = new Vector2f(aNormal).negate();

        for (Component component : objectA.getComponents()) {
            component.preSolve(objectB, contact, aNormal);
        }

        for (Component component : objectB.getComponents()) {
            component.preSolve(objectA, contact, bNormal);
        }
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse contactImpulse) {
        GameObject objectA = (GameObject) contact.getFixtureA().getUserData();
        GameObject objectB = (GameObject) contact.getFixtureB().getUserData();
        WorldManifold worldManifold = new WorldManifold();
        contact.getWorldManifold(worldManifold);
        Vector2f aNormal = new Vector2f(worldManifold.normal.x, worldManifold.normal.y);
        Vector2f bNormal = new Vector2f(aNormal).negate();

        for (Component component : objectA.getComponents()) {
            component.postSolve(objectB, contact, aNormal);
        }

        for (Component component : objectB.getComponents()) {
            component.postSolve(objectA, contact, bNormal);
        }
    }
}
