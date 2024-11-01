package no.arnemunthekaas.engine.entities.components.gizmos;

import no.arnemunthekaas.engine.Window;
import no.arnemunthekaas.editor.PropertiesWindow;
import no.arnemunthekaas.engine.entities.GameObject;
import no.arnemunthekaas.engine.entities.components.Component;
import no.arnemunthekaas.engine.entities.components.Sprite;
import no.arnemunthekaas.engine.entities.components.SpriteRenderer;
import no.arnemunthekaas.engine.entities.components.UnPickable;
import no.arnemunthekaas.engine.events.listeners.MouseListener;
import no.arnemunthekaas.engine.entities.Prefabs;
import no.arnemunthekaas.utils.GameConstants;
import org.joml.Vector2f;
import org.joml.Vector4f;

import static org.lwjgl.glfw.GLFW.*;

public abstract class Gizmo extends Component {
    private Vector4f xAxisColor = new Vector4f(1, 0.3f, 0.3f, 1);
    private Vector4f xAxisColorHover = new Vector4f(1, 0, 0, 1);
    private Vector4f yAxisColor = new Vector4f(0.3f, 1, 0.3f, 1);
    private Vector4f yAxisColorHover = new Vector4f(0, 1, 0, 1);

    private GameObject xAxisObject;
    private GameObject yAxisObject;
    private SpriteRenderer xAxisSprite;
    private SpriteRenderer yAxisSprite;
    protected GameObject activeGameObject;

    private Vector2f xAxisOffset = new Vector2f( GameConstants.GRID_WIDTH, 0);
    private Vector2f yAxisOffset = new Vector2f(0, GameConstants.GRID_HEIGHT);

    private float gizmoWidth = GameConstants.GRID_WIDTH / 2;
    private float gizmoHeight =  GameConstants.GRID_HEIGHT * 2;

    protected boolean xAxisActive, yAxisActive;

    private PropertiesWindow propertiesWindow;
    private boolean using;

    /**
     *
     * @param arrowSprite
     * @param propertiesWindow
     */
    public Gizmo(Sprite arrowSprite, PropertiesWindow propertiesWindow) {
        this.xAxisObject = Prefabs.generateSpriteObject(arrowSprite, gizmoWidth, gizmoHeight);
        this.yAxisObject = Prefabs.generateSpriteObject(arrowSprite, gizmoWidth, gizmoHeight);
        this.xAxisSprite = this.xAxisObject.getComponent(SpriteRenderer.class);
        this.yAxisSprite = this.yAxisObject.getComponent(SpriteRenderer.class);
        this.propertiesWindow = propertiesWindow;

        this.xAxisObject.addComponent(new UnPickable());
        this.yAxisObject.addComponent(new UnPickable());

        Window.getScene().addGameObject(this.xAxisObject);
        Window.getScene().addGameObject(this.yAxisObject);
    }

    @Override
    public void start() {
        this.xAxisObject.transform.rotation = 90;
        this.yAxisObject.transform.rotation = 180;
        this.xAxisObject.transform.zIndex = GameConstants.MAX_Z_INDEX;
        this.yAxisObject.transform.zIndex = GameConstants.MAX_Z_INDEX;

        this.xAxisObject.setNoSerialization();
        this.yAxisObject.setNoSerialization();
    }

    @Override
    public void update(float dt) {
        if (using)
            this.setInActive();
        xAxisObject.getComponent(SpriteRenderer.class).setColor(new Vector4f(0,0,0,0));
        yAxisObject.getComponent(SpriteRenderer.class).setColor(new Vector4f(0,0,0,0));
    }

    @Override
    public void editorUpdate(float dt) {
        if(!using) return;

        this.activeGameObject = this.propertiesWindow.getActiveGameObject();

        if(this.activeGameObject != null) {
            this.setActive();
        } else {
            this.setInActive();
            return;
        }

        boolean xAxisHot = checkXHoverState();
        boolean yAxisHot = checkYHoverState();

        if((xAxisHot || xAxisActive)&& MouseListener.isDragging() && MouseListener.mouseButtonDown(GLFW_MOUSE_BUTTON_LEFT)) {
            xAxisActive = true;
            yAxisActive = false;
        } else if((yAxisHot || yAxisActive)&& MouseListener.isDragging() && MouseListener.mouseButtonDown(GLFW_MOUSE_BUTTON_LEFT)) {
            yAxisActive = true;
            xAxisActive = false;
        } else {
            xAxisActive = false;
            yAxisActive = false;
        }

        if(this.activeGameObject != null) {
            this.xAxisObject.transform.position.set(this.activeGameObject.transform.position);
            this.yAxisObject.transform.position.set(this.activeGameObject.transform.position);
            this.xAxisObject.transform.position.add(this.xAxisOffset);
            this.yAxisObject.transform.position.add(this.yAxisOffset);
        }
    }

    private boolean checkYHoverState() {
        Vector2f mousePos = MouseListener.getWorld();

        if (mousePos.x <= yAxisObject.transform.position.x + (gizmoWidth / 2.0f) &&
                mousePos.x >= yAxisObject.transform.position.x - (gizmoWidth / 2.0f) &&
                mousePos.y <= yAxisObject.transform.position.y + (gizmoHeight / 2.0f) &&
                mousePos.y >= yAxisObject.transform.position.y - (gizmoHeight / 2.0f)) {
            yAxisSprite.setColor(yAxisColorHover);
            return true;
        }

        yAxisSprite.setColor(yAxisColor);
        return false;
    }

    private boolean checkXHoverState() {
        Vector2f mousePos = MouseListener.getWorld();

        if (mousePos.x <= xAxisObject.transform.position.x + (gizmoHeight / 2.0f) &&
                mousePos.x >= xAxisObject.transform.position.x - (gizmoWidth / 2.0f) &&
                mousePos.y >= xAxisObject.transform.position.y - (gizmoHeight / 2.0f) &&
                mousePos.y <= xAxisObject.transform.position.y + (gizmoWidth / 2.0f)) {
            xAxisSprite.setColor(xAxisColorHover);
            return true;
        }

        xAxisSprite.setColor(xAxisColor);
        return false;
    }

    private void setActive() {
        this.xAxisSprite.setColor(xAxisColor);
        this.yAxisSprite.setColor(yAxisColor);
    }

    private void setInActive() {
        this.activeGameObject = null;
        this.xAxisSprite.setColor(new Vector4f(0,0,0,0));
        this.yAxisSprite.setColor(new Vector4f(0,0,0,0));
    }

    /**
     *
     */
    public void setUsing() {
        this.using = true;
    }

    /**
     *
     */
    public void setNotUsing() {
        this.using = false;
        this.setInActive();
    }
}
