package no.arnemunthekaas.engine.entities.components;

import no.arnemunthekaas.engine.Window;
import no.arnemunthekaas.engine.entities.GameObject;
import no.arnemunthekaas.engine.entities.Prefabs;
import no.arnemunthekaas.engine.entities.components.animation.StateMachine;
import no.arnemunthekaas.utils.GameConstants;

public class MoonBlock extends Block {

    private enum BlockType {
        Heart,
        Potion,
        Invincibility
    }

    public BlockType blockType = BlockType.Heart;

    @Override
    void playerHit(PlayerController playerController) {
        switch(blockType) {
            case Heart:
                doHeart(playerController);
                break;
            case Potion:
                doPotion(playerController);
                break;
            case Invincibility:
                doInvincibility(playerController);
                break;
        }

        StateMachine stateMachine = gameObject.getComponent(StateMachine.class);
        if (stateMachine != null) {
            stateMachine.trigger("setInactive");
            this.setInactive();
        }
    }

    private void doInvincibility(PlayerController playerController) {
    }

    private void doPotion(PlayerController playerController) {
        if (playerController.isSmall())
            doBluePotion();
        else if (playerController.isBig())
            doRedPotion();

    }

    private void doBluePotion() {
        GameObject bluePotion = Prefabs.generateBluePotion();
        bluePotion.transform.position.set(this.gameObject.transform.position);
        bluePotion.transform.position.y += GameConstants.GRID_HEIGHT / 1.2f;
        Window.getScene().addGameObject(bluePotion);
    }

    private void doRedPotion() {
        GameObject redPotion = Prefabs.generateRedPotion();
        redPotion.transform.position.set(this.gameObject.transform.position);
        redPotion.transform.position.y += GameConstants.GRID_HEIGHT / 1.2f;
        Window.getScene().addGameObject(redPotion);
    }

    private void doHeart(PlayerController playerController) {
        GameObject heart = Prefabs.generateHeart();
        heart.transform.position.set(this.gameObject.transform.position);
        heart.transform.position.y += GameConstants.GRID_HEIGHT;
        Window.getScene().addGameObject(heart);
    }


}
