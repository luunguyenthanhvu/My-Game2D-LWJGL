package no.arnemunthekaas.scenes;

import no.arnemunthekaas.engine.entities.components.*;
import no.arnemunthekaas.engine.entities.GameObject;
import no.arnemunthekaas.engine.entities.components.animation.StateMachine;
import no.arnemunthekaas.utils.AssetPool;



public class LevelSceneInitializer extends SceneInitializer {

    private GameObject cameraObject;

    public LevelSceneInitializer() {
    }

    @Override
    public void init(Scene scene) {
        cameraObject = scene.createGameObject("GameCamera");
        cameraObject.addComponent(new GameCamera(scene.getCamera()));
        cameraObject.start();
        scene.addGameObject(cameraObject);
    }

    @Override
    public void loadResources(Scene scene) {

        // TODO: Have all resource paths specified in a file, refactor and cleanup here and make adding easier / less tedious

        // Load shaders
        AssetPool.getShader("assets/shaders/default.glsl");
        AssetPool.getShader("assets/shaders/pickingShader.glsl");
        AssetPool.getShader("assets/shaders/debugLine2D.glsl");

        // Load Images
        AssetPool.addSpriteSheet("assets/images/gizmos.png", new Spritesheet(AssetPool.getTexture("assets/images/gizmos.png"),24,48,3,0));
        AssetPool.addSpriteSheet("assets/images/missingtex.png", new Spritesheet(AssetPool.getTexture("assets/images/missingtex.png"),32,32,1,0));

        // Load Spritesheets
        AssetPool.addSpriteSheet("assets/images/spritesheets/oryx_16bit_fantasy_tiles.png", new Spritesheet(AssetPool.getTexture("assets/images/spritesheets/oryx_16bit_fantasy_tiles.png"),
                24, 24, 204, 0));
        AssetPool.addSpriteSheet("assets/images/spritesheets/oryx_16bit_fantasy_creatures_trans.png", new Spritesheet(AssetPool.getTexture("assets/images/spritesheets/oryx_16bit_fantasy_creatures_trans.png"),
                24, 24, 300, 0));

        AssetPool.addSpriteSheet("assets/images/spritesheet.png", new Spritesheet(AssetPool.getTexture("assets/images/spritesheet.png"),
                16, 16, 20, 0));
        AssetPool.addSpriteSheet("assets/images/bigSpritesheet.png", new Spritesheet(AssetPool.getTexture("assets/images/bigSpritesheet.png"),
                16, 32, 42, 0));

        AssetPool.addSpriteSheet("assets/images/spritesheets/oryx_16bit_fantasy_items_trans.png", new Spritesheet(AssetPool.getTexture("assets/images/spritesheets/oryx_16bit_fantasy_items_trans.png"),
                16, 16, 300, 0));

        AssetPool.addSpriteSheet("assets/images/img.png", new Spritesheet(AssetPool.getTexture("assets/images/img.png"),
                16, 16, 64, 0));

        AssetPool.addSpriteSheet("assets/images/img_1.png", new Spritesheet(AssetPool.getTexture("assets/images/img_1.png"),
                32, 32, 4, 0));

        // Load Sounds
        AssetPool.addSound("assets/audio/Powerup6.ogg", false);
        AssetPool.addSound("assets/audio/button_2.ogg", false);
        AssetPool.addSound("assets/audio/mario1up.ogg", false);
        AssetPool.addSound("assets/audio/assets_sounds_jump-small.ogg", false);
        AssetPool.addSound("assets/audio/assets_sounds_jump-super.ogg", false);
        AssetPool.addSound("assets/audio/assets_sounds_bump.ogg", false);
        AssetPool.addSound("assets/audio/assets_sounds_break_block.ogg", false);
        AssetPool.addSound("assets/audio/assets_sounds_mario_die.ogg", false);
        AssetPool.addSound("assets/audio/assets_sounds_pipe.ogg", false);

        for(GameObject go : scene.getGameObjects()) {
            if(go.getComponent(SpriteRenderer.class) != null) {
                SpriteRenderer spr = go.getComponent(SpriteRenderer.class);
                if (spr.getTexture() != null) {
                    spr.setTexture(AssetPool.getTexture(spr.getTexture().getFilepath()));
                }
            }

            if(go.getComponent(StateMachine.class) != null) {
                StateMachine stateMachine = go.getComponent(StateMachine.class);
                stateMachine.refreshTextures();
            }
        }

    }

    @Override
    public void imgui() {
        
    }
}
