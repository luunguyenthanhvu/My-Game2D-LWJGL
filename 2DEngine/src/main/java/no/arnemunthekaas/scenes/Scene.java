package no.arnemunthekaas.scenes;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import no.arnemunthekaas.engine.entities.components.PlayerController;
import no.arnemunthekaas.engine.entities.components.physics2d.Physics2D;
import no.arnemunthekaas.engine.renderer.Camera;
import no.arnemunthekaas.engine.entities.GameObject;
import no.arnemunthekaas.engine.entities.components.Component;
import no.arnemunthekaas.engine.entities.components.Transform;
import no.arnemunthekaas.engine.entities.deserializers.ComponentDeserializer;
import no.arnemunthekaas.engine.entities.deserializers.GameObjectDeserializer;
import no.arnemunthekaas.engine.renderer.Renderer;
import org.joml.Vector2f;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Scene {

    private Renderer renderer;
    private Camera camera;
    private boolean running;
    private List<GameObject> gameObjects;
    private List<GameObject> pendingObjects;
    private Physics2D physics2D;

    private SceneInitializer sceneInitializer;

    public Scene(SceneInitializer sceneInitializer) {
        this.sceneInitializer = sceneInitializer;
        this.physics2D = new Physics2D();
        this.renderer = new Renderer();
        this.gameObjects = new ArrayList<>();
        this.pendingObjects = new ArrayList<>();
        this.running = false;
    }

    /**
     *
     */
    public void init() {
        this.camera = new Camera(new Vector2f(0, 0));
        this.sceneInitializer.loadResources(this);
        this.sceneInitializer.init(this);
    }

    /**
     * Starts scene.
     * Starts all Game Objects and their components.
     */
    public void start() {
        for (int i = 0; i < gameObjects.size(); i++) {
            GameObject go = gameObjects.get(i);
            go.start();
            this.renderer.add(go);
            this.physics2D.add(go);
        }
        this.running = true;
    }

    /**
     * Destroy all objects currently in scene
     */
    public void destroy() {
        for (GameObject go : gameObjects)
            go.destroy();
    }

    /**
     * Update the scene in game loop using delta time
     *
     * Adjusts scene's camera's projection matrix and updates all game objects in scene and
     * destroys dead game objects
     *
     * @param dt delta time
     */
    public void update(float dt) {
        this.camera.adjustProjection();
        this.physics2D.update(dt);

        for (int i = 0; i < gameObjects.size(); i++) {

            GameObject go = gameObjects.get(i);
            go.update(dt);

            if(go.isDead()) {
                gameObjects.remove(i);
                this.renderer.destroyGameObject(go);
                this.physics2D.destroyGameObject(go);
                i--;
            }


        }

        for (GameObject go : pendingObjects) {
            this.gameObjects.add(go);
            go.start();
            this.renderer.add(go);
            this.physics2D.add(go);
        }
        pendingObjects.clear();
    }

    /**
     *
     * @param dt
     */
    public void editorUpdate(float dt) {
        this.camera.adjustProjection();

        for (int i = 0; i < gameObjects.size(); i++) {

            GameObject go = gameObjects.get(i);
            go.editorUpdate(dt);

            if(go.isDead()) {
                gameObjects.remove(i);
                this.renderer.destroyGameObject(go);
                this.physics2D.destroyGameObject(go);
                i--;
            }


        }

        for (GameObject go : pendingObjects) {
            this.gameObjects.add(go);
            go.start();
            this.renderer.add(go);
            this.physics2D.add(go);
        }
        pendingObjects.clear();
    }

    /**
     * Render scene in game loop
     */
    public void render() {
        this.renderer.render();
    }

    /**
     * Add Game Object to scene
     *
     * @param go Game Object to add
     */
    public void addGameObject(GameObject go) {
        if(!running)
            this.gameObjects.add(go);
        else {
            pendingObjects.add(go);

        }
    }

    /**
     *
     * @return
     */
    public List<GameObject> getGameObjects() {
        return this.gameObjects;
    }

    /**
     * Finds and returns a game object in scene from its UID
     * @param UID UID of object to be found
     * @return Object if found, else null
     */
    public GameObject getGameObject(int UID) {
        Optional<GameObject> result = gameObjects.stream().filter(g -> g.getUid() == UID).findFirst();
        return result.orElse(null);
    }

    /**
     * Finds and returns a game object in scene from its name
     * @param name Name of object to be found
     * @return Object if found, else null
     */
    public GameObject getGameObject(String name) {
        Optional<GameObject> result = gameObjects.stream().filter(g -> g.name.equals(name)).findFirst();
        return result.orElse(null);
    }

    /**
     * Get Scene Camera
     *
     * @return Camera
     */
    public Camera getCamera() {
        return camera;
    }

    /**
     *
     */
    public void imgui() {
        this.sceneInitializer.imgui();
    }

    /**
     * Create a new gameObject
     * @param name
     * @return
     */
    public GameObject createGameObject(String name) {
        GameObject go = new GameObject(name);
        go.addComponent(new Transform());
        go.transform = go.getComponent(Transform.class);
        return go;
    }

    /**
     *
     */
    public void save() {
        Gson gson = new GsonBuilder().setPrettyPrinting().registerTypeAdapter(Component.class, new ComponentDeserializer()).registerTypeAdapter(GameObject.class, new GameObjectDeserializer()).enableComplexMapKeySerialization().create();

        try {
            FileWriter writer = new FileWriter("level.txt");

            List<GameObject> objsToSerialize = gameObjects.stream().filter(go -> go.doSerialization()).collect(Collectors.toList());

            writer.write(gson.toJson(objsToSerialize));
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     */
    public void load() {
        Gson gson = new GsonBuilder().setPrettyPrinting().registerTypeAdapter(Component.class, new ComponentDeserializer()).registerTypeAdapter(GameObject.class, new GameObjectDeserializer()).enableComplexMapKeySerialization().create();
        String inFile = null;

        try {
            inFile = new String(Files.readAllBytes(Paths.get("level.txt")));
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(inFile != null) {
            int maxGameObjectID = -1;
            int maxComponentId = -1;
            GameObject[] objs = gson.fromJson(inFile, GameObject[].class);

            for (GameObject o : objs) {
                addGameObject(o);

                for(Component c : o.getComponents()) {
                    if(c.getUid() > maxComponentId)
                        maxComponentId = c.getUid();

                }

                if(o.getUid() > maxGameObjectID)
                    maxGameObjectID = o.getUid();
            }

            maxGameObjectID++;
            maxComponentId++;
            GameObject.init(maxGameObjectID);
            Component.init(maxComponentId);

        }
    }

    /**
     *
     * @return
     */
    public Physics2D getPhysics() {
        return this.physics2D;
    }

    /**
     * Returns first instance of a GameObject that contains a specific component
     * @param clazz
     * @return
     */
    public <T extends Component> GameObject getGameObjectWith(Class<T> clazz) {
        for (GameObject gameObject : gameObjects) {
            if (gameObject.getComponent(clazz) != null)
                return  gameObject;
        }

        return null;
    }
}
