package no.arnemunthekaas.engine.entities.components.gizmos;

import no.arnemunthekaas.editor.PropertiesWindow;
import no.arnemunthekaas.engine.entities.components.Sprite;
import no.arnemunthekaas.engine.events.listeners.MouseListener;

public class ScaleGizmo extends Gizmo {

    /**
     *
     * @param scaleSprite
     * @param propertiesWindow
     */
    public ScaleGizmo(Sprite scaleSprite, PropertiesWindow propertiesWindow) {
        super(scaleSprite, propertiesWindow);
    }

    @Override
    public void editorUpdate(float dt) {


        if(activeGameObject != null) {
            if (xAxisActive && !yAxisActive) {
                activeGameObject.transform.scale.x -= MouseListener.getWorldX();
            } else if (yAxisActive) {
                activeGameObject.transform.scale.y -= MouseListener.getWorldY();
            }
        }

        super.editorUpdate(dt);
    }
}
