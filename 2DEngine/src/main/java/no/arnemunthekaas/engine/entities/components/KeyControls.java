package no.arnemunthekaas.engine.entities.components;

import no.arnemunthekaas.editor.PropertiesWindow;
import no.arnemunthekaas.engine.Window;
import no.arnemunthekaas.engine.entities.GameObject;
import no.arnemunthekaas.engine.entities.components.animation.StateMachine;
import no.arnemunthekaas.engine.events.listeners.KeyListener;
import no.arnemunthekaas.utils.GameConstants;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.glfw.GLFW.*;

public class KeyControls extends Component{

    private float debounce = 0;
    private float debounceTime = 0.2f;

    @Override
    public void editorUpdate(float dt) {
        debounce -= dt;
        PropertiesWindow propertiesWindow = Window.getImGuiLayer().getPropertiesWindow();
        GameObject activeGameObject = propertiesWindow.getActiveGameObject();

        List<GameObject> activeGameObjects = propertiesWindow.getActiveGameObjects();

        float multiplier = KeyListener.isKeyPressed(GLFW_KEY_LEFT_SHIFT) ? 0.1f : 1.0f;

        if (KeyListener.isKeyPressed(GLFW_KEY_LEFT_CONTROL) && KeyListener.isKeyBeginPress(GLFW_KEY_D) && activeGameObject != null) {
            GameObject newObj = activeGameObject.copy();
            Window.getScene().addGameObject(newObj);
            newObj.transform.position.add(GameConstants.GRID_WIDTH, 0);
            propertiesWindow.setActiveGameObject(newObj);
            if (newObj.getComponent(StateMachine.class) != null)
                newObj.getComponent(StateMachine.class).refreshTextures();
        } else if (KeyListener.isKeyPressed(GLFW_KEY_LEFT_CONTROL) && KeyListener.isKeyBeginPress(GLFW_KEY_D) && activeGameObjects.size() > 1) {
            List<GameObject> gameObjects = new ArrayList<>(activeGameObjects);
            propertiesWindow.clearSelected();
            for (GameObject go : gameObjects) {
                GameObject copy = go.copy();
                Window.getScene().addGameObject(copy);
                propertiesWindow.addActiveGameObject(copy);

                if (copy.getComponent(StateMachine.class) != null)
                    copy.getComponent(StateMachine.class).refreshTextures();
            }
        } else if ((KeyListener.isKeyBeginPress(GLFW_KEY_DELETE) || KeyListener.isKeyBeginPress(GLFW_KEY_BACKSPACE))) {
            activeGameObjects.forEach(go -> go.destroy());
            propertiesWindow.clearSelected();
        } else if (KeyListener.isKeyPressed(GLFW_KEY_PAGE_DOWN) && debounce < 0) {
            debounce = debounceTime;
            for (GameObject go : activeGameObjects)
                go.transform.zIndex --;
        } else if (KeyListener.isKeyPressed(GLFW_KEY_PAGE_UP) && debounce < 0) {
            debounce = debounceTime;
            for (GameObject go : activeGameObjects)
                go.transform.zIndex ++;
        } else if (KeyListener.isKeyPressed(GLFW_KEY_UP) && debounce < 0) {
            debounce = debounceTime;
            for (GameObject go : activeGameObjects)
                go.transform.position.y += GameConstants.GRID_HEIGHT * multiplier;
        } else if (KeyListener.isKeyPressed(GLFW_KEY_DOWN) && debounce < 0) {
            debounce = debounceTime;
            for (GameObject go : activeGameObjects)
                go.transform.position.y -= GameConstants.GRID_HEIGHT * multiplier;
        } else if (KeyListener.isKeyPressed(GLFW_KEY_RIGHT) && debounce < 0) {
            debounce = debounceTime;
            for (GameObject go : activeGameObjects)
                go.transform.position.x += GameConstants.GRID_WIDTH * multiplier;
        } else if (KeyListener.isKeyPressed(GLFW_KEY_LEFT) && debounce < 0) {
            debounce = debounceTime;
            for (GameObject go : activeGameObjects)
                go.transform.position.x -= GameConstants.GRID_WIDTH * multiplier;
        }
    }
}
