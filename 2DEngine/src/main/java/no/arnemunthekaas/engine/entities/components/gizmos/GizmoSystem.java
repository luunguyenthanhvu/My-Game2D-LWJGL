package no.arnemunthekaas.engine.entities.components.gizmos;

import no.arnemunthekaas.engine.Window;
import no.arnemunthekaas.engine.entities.components.Component;
import no.arnemunthekaas.engine.entities.components.Spritesheet;
import no.arnemunthekaas.engine.events.listeners.KeyListener;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_E;

public class GizmoSystem extends Component {
    private Spritesheet gizmos;
    private transient int usingGizmo = 1;
    private transient int gizmoAmount;
    private transient float updateCooldown = 0;

    /**
     *
     * @param gizmos
     */
    public GizmoSystem(Spritesheet gizmos) {
        this.gizmos = gizmos;
    }

    @Override
    public void start() {
        gameObject.addComponent(new TranslateGizmo(gizmos.getSprite(1), Window.getImGuiLayer().getPropertiesWindow()));
        gameObject.addComponent(new ScaleGizmo(gizmos.getSprite(2), Window.getImGuiLayer().getPropertiesWindow()));
            gizmoAmount = 2;
    }

    @Override
    public void editorUpdate(float dt) {
        if(usingGizmo == 0) {

            gameObject.getComponent(TranslateGizmo.class).setNotUsing();
            gameObject.getComponent(ScaleGizmo.class).setNotUsing();

        } else if(usingGizmo == 1) {

            gameObject.getComponent(TranslateGizmo.class).setUsing();
            gameObject.getComponent(ScaleGizmo.class).setNotUsing();

        } else if(usingGizmo == 2) {

            gameObject.getComponent(TranslateGizmo.class).setNotUsing();
            gameObject.getComponent(ScaleGizmo.class).setUsing();

        }

        if (KeyListener.isKeyPressed(GLFW_KEY_E) && updateCooldown <= 0) {
            usingGizmo++;
            usingGizmo %= gizmoAmount + 1;
            updateCooldown = 0.25f;
        } else {
            updateCooldown -= dt;
        }
    }
}
