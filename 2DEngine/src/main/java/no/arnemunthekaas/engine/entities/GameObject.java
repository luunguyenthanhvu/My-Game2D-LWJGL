package no.arnemunthekaas.engine.entities;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import imgui.ImGui;
import no.arnemunthekaas.engine.entities.components.Component;
import no.arnemunthekaas.engine.entities.components.SpriteRenderer;
import no.arnemunthekaas.engine.entities.components.Transform;
import no.arnemunthekaas.engine.entities.deserializers.ComponentDeserializer;
import no.arnemunthekaas.engine.entities.deserializers.GameObjectDeserializer;
import no.arnemunthekaas.utils.AssetPool;

import java.util.ArrayList;
import java.util.List;

public class GameObject {
    private static int ID_COUNTER = 0;
    private int uid = -1;

    public String name;
    private List<Component> components;
    public transient Transform transform;
    private boolean doSerialization = true;
    private boolean dead;

    public GameObject(String name) {
        this.name = name;
        this.components = new ArrayList<>();

        this.uid = ID_COUNTER++;
    }

    /**
     * Return a reference to a Component in Game Object, specified by given components class.
     *
     * @param componentClass Class for component in question
     * @param <T>
     * @return Component reference in Game Object
     */
    public <T extends Component> T getComponent(Class<T> componentClass) {
        for (Component c : components) {
            if (componentClass.isAssignableFrom(c.getClass())) {
                try {
                    return componentClass.cast(c);
                } catch ( ClassCastException e) {
                    e.printStackTrace();
                    assert false : "Error: Casting component.";
                }
            }
        }

        return null;
    }

    /**
     * Removes a given component in Game Object given its class.
     *
     * @param componentClass Class for component in question
     * @param <T>
     */
    public <T extends Component> void removeComponent(Class<T> componentClass) {
        for (int i = 0; i < components.size(); i++) {
            Component c = components.get(i);
            if(componentClass.isAssignableFrom(c.getClass())) {
                components.remove(i);
                return;
            }
        }
    }

    /**
     * Add component to game object
     *
     * @param c component to add
     */
    public void addComponent(Component c) {
        c.generateID();
        this.components.add(c);
        c.gameObject = this;
    }

    /**
     * Update all components
     *
     * @param dt delta time
     */
    public void update(float dt) {
        components.forEach(c -> c.update(dt));
    }

    /**
     *
     * @param dt
     */
    public void editorUpdate(float dt) {
        components.forEach(c -> c.editorUpdate(dt));
    }
    /**
     * Start all components
     */
    public void start() {
        for (int i=0; i < components.size(); i++) {
            components.get(i).start();
        }
    }

    /**
     * Destroy game object and all its components
     */
    public void destroy() {
        this.dead = true;
        for(int i = 0; i < components.size(); i++)
            components.get(i).destroy();
    }

    /**
     * Check if game object is set to be destroyed (dead)
     */
    public boolean isDead(){
        return this.dead;
    }

    /**
     *
     */
    public void imgui() {
        for(Component c : components) {
            if(ImGui.collapsingHeader(c.getClass().getSimpleName()))
                c.imgui();
        }
    }

    /**
     * Get unique ID
     * @return
     */
    public int getUid() {
        return uid;
    }

    /**
     * Generate new UID
     */
    public void generateUid() {
        this.uid = ID_COUNTER++;
    }

    /**
     *
     * @param maxID
     */
    public static void init(int maxID) {
        ID_COUNTER = maxID;
    }

    /**
     * Get components
     * @return
     */
    public List<Component> getComponents() {
        return components;
    }

    /**
     * Disable serialization for this game object. Default = true
     */
    public void setNoSerialization() {
        this.doSerialization = false;
    }

    /**
     * Returns if game object is serialized or not
     * @return
     */
    public boolean doSerialization() {
        return doSerialization;
    }

    /**
     * Creates a copy of this game object
     * @return
     */
    public GameObject copy() {
        // TODO find better solution
        Gson gson = new GsonBuilder().setPrettyPrinting().registerTypeAdapter(Component.class, new ComponentDeserializer()).registerTypeAdapter(GameObject.class, new GameObjectDeserializer()).enableComplexMapKeySerialization().create();
        String objAsJson = gson.toJson(this);
        GameObject obj = gson.fromJson(objAsJson, GameObject.class);

        obj.generateUid();
        for (Component c : obj.components)
            c.generateID();

        SpriteRenderer sprite = obj.getComponent(SpriteRenderer.class);

        if (sprite != null && sprite.getTexture() != null) {
            sprite.setTexture(AssetPool.getTexture(sprite.getTexture().getFilepath()));
        }

        return obj;
    }

}
