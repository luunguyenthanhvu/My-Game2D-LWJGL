package no.arnemunthekaas.engine.entities.components;

import no.arnemunthekaas.editor.PropertiesWindow;
import no.arnemunthekaas.engine.entities.components.animation.StateMachine;
import no.arnemunthekaas.engine.events.listeners.KeyListener;
import no.arnemunthekaas.engine.renderer.DebugDraw;
import no.arnemunthekaas.engine.renderer.PickingTexture;
import no.arnemunthekaas.scenes.Scene;
import no.arnemunthekaas.utils.GameConstants;
import no.arnemunthekaas.engine.Window;
import no.arnemunthekaas.engine.entities.GameObject;
import no.arnemunthekaas.engine.events.listeners.MouseListener;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.util.HashSet;
import java.util.Set;

import static org.lwjgl.glfw.GLFW.*;

public class MouseControls extends Component{

    private GameObject holdingObject;
    private float debounceTime = 0.2f;
    private float debounce = debounceTime;

    private boolean boxSelectSet;
    private Vector2f boxSelectStart = new Vector2f();
    private Vector2f boxSelectEnd = new Vector2f();

    // TODO: Fix object duplication on place
    /**
     * Change holding object
     * @param gameObject
     */
    public void pickUpObject(GameObject gameObject) {
        if(this.holdingObject != null)
            this.holdingObject.destroy();
        this.holdingObject = gameObject;
        this.holdingObject.getComponent(SpriteRenderer.class).setColor(new Vector4f(0.8f, 0.8f, 0.8f, 0.5f));
        this.holdingObject.addComponent(new UnPickable());
        Window.getScene().addGameObject(gameObject);
    }

    /**
     *
     */
    public void place() {
        GameObject newObj = this.holdingObject.copy();
        newObj.getComponent(SpriteRenderer.class).setColor(new Vector4f(1, 1, 1, 1));
        if (newObj.getComponent(StateMachine.class) != null)
            newObj.getComponent(StateMachine.class).refreshTextures();

        newObj.removeComponent(UnPickable.class);
        Window.getScene().addGameObject(newObj);
    }

    @Override
    public void editorUpdate(float dt) {
        debounce -= dt;
        PickingTexture pickingTexture = Window.getImGuiLayer().getPropertiesWindow().getPickingTexture();
        Scene currentScene = Window.getScene();

        if(holdingObject != null) {
            float x = MouseListener.getWorldX();
            float y = MouseListener.getWorldY();
            holdingObject.transform.position.x = ((int)Math.floor(x / GameConstants.GRID_WIDTH) * GameConstants.GRID_WIDTH) + GameConstants.GRID_WIDTH / 2.0f;
            holdingObject.transform.position.y = ((int)Math.floor(y / GameConstants.GRID_HEIGHT) * GameConstants.GRID_HEIGHT) + GameConstants.GRID_HEIGHT / 2.0f;


            if(MouseListener.mouseButtonDown(GLFW_MOUSE_BUTTON_LEFT)) {
                float halfWidth = GameConstants.GRID_WIDTH / 2.0f;
                float halfHeight = GameConstants.GRID_HEIGHT / 2.0f;
                if ( MouseListener.isDragging() && !blockInSquare(holdingObject.transform.position.x - halfHeight, holdingObject.transform.position.y - halfHeight)) {
                    place();
                } else if (!MouseListener.isDragging() && debounce < 0) {
                    place();
                    debounce = debounceTime;
                }
            }

            if(KeyListener.isKeyPressed(GLFW_KEY_ESCAPE) || MouseListener.mouseButtonDown(GLFW_MOUSE_BUTTON_RIGHT)) {
                holdingObject.destroy();
                holdingObject = null;
            }

        } else if (!MouseListener.isDragging() && MouseListener.mouseButtonDown(GLFW_MOUSE_BUTTON_LEFT) && debounce < 0) {
            int x = (int) MouseListener.getScreenX();
            int y = (int) MouseListener.getScreenY();
            int gameObjectID = pickingTexture.readPixel(x, y);
            GameObject go = currentScene.getGameObject(gameObjectID);

            if( go != null && go.getComponent(UnPickable.class) == null)
                Window.getImGuiLayer().getPropertiesWindow().setActiveGameObject(go);
            else if( go == null && !MouseListener.isDragging())
                Window.getImGuiLayer().getPropertiesWindow().clearSelected();
            this.debounce = 0.2f;
        } else if (MouseListener.isDragging() && MouseListener.mouseButtonDown(GLFW_MOUSE_BUTTON_LEFT)) {
            if(!boxSelectSet) {
                Window.getImGuiLayer().getPropertiesWindow().clearSelected();
                boxSelectStart = MouseListener.getScreen();
                boxSelectSet = true;
            }
            boxSelectEnd = MouseListener.getScreen();
            Vector2f boxSelectStartWorld = MouseListener.screenToWorld(boxSelectStart);
            Vector2f boxSelectEndWorld = MouseListener.screenToWorld(boxSelectEnd);
            Vector2f halfSize = (new Vector2f(boxSelectEndWorld).sub(boxSelectStartWorld)).mul(0.5f);
            DebugDraw.addBox2D((new Vector2f(boxSelectStartWorld)).add(halfSize), new Vector2f(halfSize).mul(2.0f), 0, new Vector3f(0, 0, 1));
        } else if (boxSelectSet) {
            boxSelectSet = false;
            int screenStartX = (int) boxSelectStart.x;
            int screenStartY = (int) boxSelectStart.y;
            int screenEndX = (int) boxSelectEnd.x;
            int screenEndY = (int) boxSelectEnd.y;
            boxSelectStart.zero();
            boxSelectEnd.zero();

            if (screenEndX < screenStartX) {
                int tmp = screenStartX;
                screenStartX = screenEndX;
                screenEndX = tmp;
            }

            if (screenEndY < screenStartY) {
                int tmp = screenStartY;
                screenStartY = screenEndY;
                screenEndY = tmp;
            }

            float[] gameObjectIds = pickingTexture.readPixels(new Vector2i(screenStartX, screenStartY),
                    new Vector2i(screenEndX, screenEndY));
            Set<Integer> uniqueGOIDs = new HashSet<>();

            for (float ID : gameObjectIds)
                uniqueGOIDs.add((int) ID);

            for (Integer ID: uniqueGOIDs) {
                GameObject obj = Window.getScene().getGameObject(ID);
                if (obj != null && obj.getComponent(UnPickable.class) == null)
                    Window.getImguiLayer().getPropertiesWindow().addActiveGameObject(obj);

            }
        }
    }

    private boolean blockInSquare(float x, float y) {

        PropertiesWindow propertiesWindow = Window.getImguiLayer().getPropertiesWindow();
        Vector2f start = new Vector2f(x, y);
        Vector2f end = new Vector2f(start).add(new Vector2f(GameConstants.GRID_WIDTH, GameConstants.GRID_HEIGHT));
        Vector2f startScreenf = MouseListener.worldToScreen(start);
        Vector2f endScreenf = MouseListener.worldToScreen(end);
        Vector2i startScreen = new Vector2i((int) startScreenf.x + 2, (int) startScreenf.y - 2);
        Vector2i endScreen = new Vector2i((int) endScreenf.x - 2, (int) endScreenf.y - 2);
        float[] gameObjectIds = propertiesWindow.getPickingTexture().readPixels(startScreen, endScreen);

        for (int i = 0; i < gameObjectIds.length; i++) {
            if (gameObjectIds[i] >= 0) {
                GameObject pickedObject = Window.getScene().getGameObject((int)gameObjectIds[i]);
                if(pickedObject.getComponent(UnPickable.class) == null)
                    return true;
            }
        }

        return false;

    }
}
