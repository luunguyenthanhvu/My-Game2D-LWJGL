package no.arnemunthekaas.editor;

import imgui.ImGui;
import no.arnemunthekaas.engine.entities.GameObject;
import no.arnemunthekaas.engine.entities.components.SpriteRenderer;
import no.arnemunthekaas.engine.entities.components.physics2d.components.Box2DCollider;
import no.arnemunthekaas.engine.entities.components.physics2d.components.CircleCollider;
import no.arnemunthekaas.engine.entities.components.physics2d.components.Rigidbody2D;
import no.arnemunthekaas.engine.renderer.PickingTexture;
import org.joml.Vector4f;

import java.util.ArrayList;
import java.util.List;

public class PropertiesWindow {

    private List<GameObject> activeGameObjects;
    private List<Vector4f> activeGameObjectsOriginalColor;
    private GameObject activeGameObject;
    private PickingTexture pickingTexture;

    /**
     *
     * @param pickingTexture
     */
    public PropertiesWindow(PickingTexture pickingTexture) {
        this.activeGameObjects = new ArrayList<>();
        this.activeGameObjectsOriginalColor = new ArrayList<>();
        this.pickingTexture = pickingTexture;
    }

    /**
     *
     */
    public void imgui() {
        if(activeGameObjects.size() == 1 && activeGameObjects.get(0) != null) {
            activeGameObject = activeGameObjects.get(0);
            ImGui.begin("Properties");

            if (ImGui.beginPopupContextWindow("ComponentAdder")) {
                if(ImGui.menuItem("Add Rigidbody")) {
                    if (activeGameObject.getComponent(Rigidbody2D.class) == null) {
                        activeGameObject.addComponent(new Rigidbody2D());
                    }
                }

                if (ImGui.menuItem("Add Box collider")) {
                    if (activeGameObject.getComponent(Box2DCollider.class) == null &&
                            activeGameObject.getComponent(CircleCollider.class)  == null) {
                        activeGameObject.addComponent(new Box2DCollider());
                    }
                }

                if (ImGui.menuItem("Add Circle collider")) {
                    if (activeGameObject.getComponent(CircleCollider.class) == null &&
                            activeGameObject.getComponent(Box2DCollider.class) == null) {
                        activeGameObject.addComponent(new CircleCollider());
                    }
                }

                ImGui.endPopup();
            }

            activeGameObject.imgui();
            ImGui.end();
        }
    }

    /**
     * Returns active game object
     * @return
     */
    public GameObject getActiveGameObject() {
        return this.activeGameObjects.size() == 1 ? this.activeGameObjects.get(0) : null;
    }

    /**
     * Set active game object
     * @param go
     */
    public void setActiveGameObject(GameObject go) {
        if (go != null) {
            clearSelected();
            this.activeGameObjects.add(go);
        }
    }

    /**
     * Returns all active game objects
     * @return
     */
    public List<GameObject> getActiveGameObjects() {
        return this.activeGameObjects;
    }

    /**
     *
     */
    public void clearSelected() {
        if(activeGameObjectsOriginalColor.size() > 0) {
            for(int i = 0; i < activeGameObjects.size(); i++) {
                SpriteRenderer spr = activeGameObjects.get(i).getComponent(SpriteRenderer.class);
                if(spr != null)
                    spr.setColor(activeGameObjectsOriginalColor.get(i));
            }
        }
        this.activeGameObjects.clear();
        this.activeGameObjectsOriginalColor.clear();
    }

    /**
     *
     * @param go
     */
    public void addActiveGameObject(GameObject go) {
        SpriteRenderer spr = go.getComponent(SpriteRenderer.class);
        if (spr != null) {
            this.activeGameObjectsOriginalColor.add(new Vector4f(spr.getColor()));
            spr.setColor(new Vector4f(0.8f, 0.8f, 0.0f, 0.8f));
        } else {
            this.activeGameObjectsOriginalColor.add(new Vector4f());
        }

        this.activeGameObjects.add(go);

    }

    /**
     *
     * @return
     */
    public PickingTexture getPickingTexture() {
        return this.pickingTexture;
    }
}
