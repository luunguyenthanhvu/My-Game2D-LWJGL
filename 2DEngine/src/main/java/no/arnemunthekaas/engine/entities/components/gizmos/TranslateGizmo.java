package no.arnemunthekaas.engine.entities.components.gizmos;

import no.arnemunthekaas.editor.PropertiesWindow;
import no.arnemunthekaas.engine.entities.components.Sprite;
import no.arnemunthekaas.engine.events.listeners.MouseListener;

public class TranslateGizmo extends Gizmo {

    /**
     *
     * @param arrowSprite
     * @param propertiesWindow
     */
    public TranslateGizmo(Sprite arrowSprite, PropertiesWindow propertiesWindow) {
        super(arrowSprite, propertiesWindow);
    }

    @Override
    public void editorUpdate(float dt) {

        if(activeGameObject != null) {
            if (xAxisActive && !yAxisActive) {
                activeGameObject.transform.position.x -= MouseListener.getWorldX();
            } else if (yAxisActive) {
                activeGameObject.transform.position.y -= MouseListener.getWorldY();
            }
        }

        super.editorUpdate(dt);
    }


}
