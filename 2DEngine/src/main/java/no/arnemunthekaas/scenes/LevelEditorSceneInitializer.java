package no.arnemunthekaas.scenes;

import imgui.ImGui;
import imgui.ImVec2;
import no.arnemunthekaas.engine.audio.Sound;
import no.arnemunthekaas.engine.entities.Direction;
import no.arnemunthekaas.engine.entities.components.*;
import no.arnemunthekaas.engine.entities.GameObject;
import no.arnemunthekaas.engine.entities.components.animation.StateMachine;
import no.arnemunthekaas.engine.entities.components.gizmos.GizmoSystem;
import no.arnemunthekaas.engine.entities.Prefabs;
import no.arnemunthekaas.engine.entities.components.physics2d.BodyType;
import no.arnemunthekaas.engine.entities.components.physics2d.components.Box2DCollider;
import no.arnemunthekaas.engine.entities.components.physics2d.components.Rigidbody2D;
import no.arnemunthekaas.utils.AssetPool;
import no.arnemunthekaas.utils.GameConstants;
import org.joml.Vector2f;

import java.io.File;
import java.util.Collection;


public class LevelEditorSceneInitializer extends SceneInitializer {

    private GameObject levelEditorComponents;

    public LevelEditorSceneInitializer() {
    }

    @Override
    public void init(Scene scene) {
        Spritesheet gizmos = AssetPool.getSpritesheet("assets/images/gizmos.png");

        levelEditorComponents = scene.createGameObject("LevelEditor");
        levelEditorComponents.setNoSerialization();

        levelEditorComponents.addComponent(new MouseControls());
        levelEditorComponents.addComponent(new KeyControls());
        levelEditorComponents.addComponent(new GridLines());
        levelEditorComponents.addComponent(new EditorCamera(scene.getCamera()));
        levelEditorComponents.addComponent(new GizmoSystem(gizmos));

        scene.addGameObject(levelEditorComponents);
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

        AssetPool.addSpriteSheet("assets/images/turtle.png", new Spritesheet(AssetPool.getTexture("assets/images/turtle.png"),
                16, 24, 4, 0));

        AssetPool.addSpriteSheet("assets/images/items.png", new Spritesheet(AssetPool.getTexture("assets/images/items.png"),
                16, 16, 41, 0));

        // Load Sounds
        AssetPool.addSound("assets/audio/Powerup6.ogg", false);
        AssetPool.addSound("assets/audio/button_2.ogg", false);
        AssetPool.addSound("assets/audio/mario1up.ogg", false);
        AssetPool.addSound("assets/audio/assets_sounds_jump-small.ogg", false);
        AssetPool.addSound("assets/audio/assets_sounds_jump-super.ogg", false);
        AssetPool.addSound("assets/audio/assets_sounds_bump.ogg", false);
        AssetPool.addSound("assets/audio/assets_sounds_break_block.ogg", false);
        AssetPool.addSound("assets/audio/assets_sounds_mario_die.ogg", false);
        AssetPool.addSound("assets/audio/assets_sounds_kick.ogg", false);
        AssetPool.addSound("assets/audio/assets_sounds_pipe.ogg", false);
        AssetPool.addSound("assets/audio/flagpole.ogg", false);
        AssetPool.addSound("assets/audio/stage_clear.ogg", false);

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
        // TODO: cleanup here, create helper methods for tab items
        Spritesheet solidTiles = AssetPool.getSpritesheet("assets/images/spritesheets/oryx_16bit_fantasy_tiles.png");
        Spritesheet decorativeTiles = AssetPool.getSpritesheet("assets/images/img.png");
        Spritesheet playerSprites = AssetPool.getSpritesheet("assets/images/spritesheet.png");
        Spritesheet items = AssetPool.getSpritesheet("assets/images/spritesheets/oryx_16bit_fantasy_tiles.png");
        Spritesheet pipes = AssetPool.getSpritesheet("assets/images/img_1.png");
        Spritesheet turtles = AssetPool.getSpritesheet("assets/images/turtle.png");
        Spritesheet items2 = AssetPool.getSpritesheet("assets/images/items.png");

        ImGui.begin("Level Editor Components");
        levelEditorComponents.imgui();
        ImGui.end();

        ImGui.begin("Objects");

        if (ImGui.beginTabBar("WindowTabBar")) {
            if (ImGui.beginTabItem("Solid Tiles")) {
                ImVec2 windowPos = new ImVec2();
                ImGui.getWindowPos(windowPos);
                ImVec2 windowSize = new ImVec2();
                ImGui.getWindowSize(windowSize);
                ImVec2 itemSpacing = new ImVec2();
                ImGui.getStyle().getItemSpacing(itemSpacing);

                float windowX2 = windowPos.x + windowSize.x;
                for (int i = 0; i < solidTiles.size(); i++) {
                    Sprite sprite = solidTiles.getSprite(i);
                    float spriteWidth = sprite.getWidth() * 2;
                    float spriteHeight = sprite.getHeight() * 2;
                    int id = sprite.getTexID();
                    Vector2f[] texCoords = sprite.getTexCoords();

                    ImGui.pushID(i);
                    if (ImGui.imageButton(id, spriteWidth, spriteHeight, texCoords[2].x, texCoords[0].y, texCoords[0].x, texCoords[2].y)) {
                        GameObject gameObject = Prefabs.generateSpriteObject(sprite, GameConstants.GRID_WIDTH, GameConstants.GRID_HEIGHT);
                        Rigidbody2D rigidbody2D = new Rigidbody2D();
                        rigidbody2D.setBodyType(BodyType.Static);
                        gameObject.addComponent(rigidbody2D);
                        Box2DCollider box2DCollider = new Box2DCollider();
                        box2DCollider.setHalfSize(new Vector2f(GameConstants.GRID_WIDTH, GameConstants.GRID_HEIGHT));
                        gameObject.addComponent(box2DCollider);
                        gameObject.addComponent(new Ground());

                        if(i == 1 || i == 2)
                            gameObject.addComponent(new Breakable());

                        levelEditorComponents.getComponent(MouseControls.class).pickUpObject(gameObject);

                    }
                    ImGui.popID();

                    ImVec2 lastButtonPos = new ImVec2();
                    ImGui.getItemRectMax(lastButtonPos);
                    float lastButtonX2 = lastButtonPos.x;
                    float nextButtonX2 = lastButtonX2 + itemSpacing.x + spriteWidth;

                    if (i + 1 < solidTiles.size() && nextButtonX2 < windowX2)
                        ImGui.sameLine();

                }
                ImGui.endTabItem();
            }

            if (ImGui.beginTabItem("Decoration")) {

                ImVec2 windowPos = new ImVec2();
                ImGui.getWindowPos(windowPos);
                ImVec2 windowSize = new ImVec2();
                ImGui.getWindowSize(windowSize);
                ImVec2 itemSpacing = new ImVec2();
                ImGui.getStyle().getItemSpacing(itemSpacing);

                float windowX2 = windowPos.x + windowSize.x;
                for (int i = 0; i < decorativeTiles.size(); i++) {
                    Sprite sprite = decorativeTiles.getSprite(i);
                    float spriteWidth = sprite.getWidth() * 2;
                    float spriteHeight = sprite.getHeight() * 2;
                    int id = sprite.getTexID();
                    Vector2f[] texCoords = sprite.getTexCoords();

                    ImGui.pushID(i);
                    if (ImGui.imageButton(id, spriteWidth, spriteHeight, texCoords[2].x, texCoords[0].y, texCoords[0].x, texCoords[2].y)) {
                        GameObject gameObject = Prefabs.generateSpriteObject(sprite, GameConstants.GRID_WIDTH, GameConstants.GRID_HEIGHT);
                        gameObject.transform.zIndex = Integer.MIN_VALUE;
                        levelEditorComponents.getComponent(MouseControls.class).pickUpObject(gameObject);
                    }
                    ImGui.popID();

                    ImVec2 lastButtonPos = new ImVec2();
                    ImGui.getItemRectMax(lastButtonPos);
                    float lastButtonX2 = lastButtonPos.x;
                    float nextButtonX2 = lastButtonX2 + itemSpacing.x + spriteWidth;

                    if (i + 1 < decorativeTiles.size() && nextButtonX2 < windowX2)
                        ImGui.sameLine();

                }
                ImGui.endTabItem();
            }

            if (ImGui.beginTabItem("Prefabs")) {
                int uid = 0;

                // Player Character
                Sprite sprite = playerSprites.getSprite(0);
                float spriteWidth = sprite.getWidth() * 2;
                float spriteHeight = sprite.getHeight() * 2;
                int id = sprite.getTexID();
                Vector2f[] texCoords = sprite.getTexCoords();

                ImGui.pushID(uid++);
                if (ImGui.imageButton(id, spriteWidth, spriteHeight, texCoords[2].x, texCoords[0].y, texCoords[0].x, texCoords[2].y)) {
                    GameObject gameObject = Prefabs.generatePlayerCharacter();
                    levelEditorComponents.getComponent(MouseControls.class).pickUpObject(gameObject);
                }
                ImGui.popID();
                ImGui.sameLine();

                // Moonblock

                sprite = items.getSprite(4);
                id = sprite.getTexID();
                texCoords = sprite.getTexCoords();

                ImGui.pushID(uid++);
                if (ImGui.imageButton(id, spriteWidth, spriteHeight, texCoords[2].x, texCoords[0].y, texCoords[0].x, texCoords[2].y)) {
                    GameObject object = Prefabs.generateMoonBlock();
                    levelEditorComponents.getComponent(MouseControls.class).pickUpObject(object);
                }
                ImGui.popID();
                ImGui.sameLine();

                // Goomba

                sprite = playerSprites.getSprite(14);
                id = sprite.getTexID();
                texCoords = sprite.getTexCoords();

                ImGui.pushID(uid++);
                if (ImGui.imageButton(id, spriteWidth, spriteHeight, texCoords[2].x, texCoords[0].y, texCoords[0].x, texCoords[2].y)) {
                    GameObject gameObject = Prefabs.generateGoomba();
                    levelEditorComponents.getComponent(MouseControls.class).pickUpObject(gameObject);
                }
                ImGui.popID();
                ImGui.sameLine();

                // Turtles

                sprite = turtles.getSprite(0);
                id = sprite.getTexID();
                texCoords = sprite.getTexCoords();

                ImGui.pushID(uid++);
                if (ImGui.imageButton(id, spriteWidth, spriteHeight, texCoords[2].x, texCoords[0].y, texCoords[0].x, texCoords[2].y)) {
                    GameObject gameObject = Prefabs.generateTurtle();
                    levelEditorComponents.getComponent(MouseControls.class).pickUpObject(gameObject);
                }
                ImGui.popID();
                ImGui.sameLine();

                // Flagpole

                sprite = items2.getSprite(6);
                id = sprite.getTexID();
                texCoords = sprite.getTexCoords();

                ImGui.pushID(uid++);
                if (ImGui.imageButton(id, spriteWidth, spriteHeight, texCoords[2].x, texCoords[0].y, texCoords[0].x, texCoords[2].y)) {
                    GameObject gameObject = Prefabs.generateFlagTop();
                    levelEditorComponents.getComponent(MouseControls.class).pickUpObject(gameObject);
                }
                ImGui.popID();
                ImGui.sameLine();

                sprite = items2.getSprite(33);
                id = sprite.getTexID();
                texCoords = sprite.getTexCoords();

                ImGui.pushID(uid++);
                if (ImGui.imageButton(id, spriteWidth, spriteHeight, texCoords[2].x, texCoords[0].y, texCoords[0].x, texCoords[2].y)) {
                    GameObject gameObject = Prefabs.generateFlagPole();
                    levelEditorComponents.getComponent(MouseControls.class).pickUpObject(gameObject);
                }
                ImGui.popID();
                ImGui.sameLine();

                // Pipes

                sprite = pipes.getSprite(0);
                id = sprite.getTexID();
                texCoords = sprite.getTexCoords();

                ImGui.pushID(uid++);
                if (ImGui.imageButton(id, spriteWidth, spriteHeight, texCoords[2].x, texCoords[0].y, texCoords[0].x, texCoords[2].y)) {
                    GameObject gameObject = Prefabs.generatePipe(Direction.Down);
                    levelEditorComponents.getComponent(MouseControls.class).pickUpObject(gameObject);
                }
                ImGui.popID();
                ImGui.sameLine();

                sprite = pipes.getSprite(1);
                id = sprite.getTexID();
                texCoords = sprite.getTexCoords();

                ImGui.pushID(uid++);
                if (ImGui.imageButton(id, spriteWidth, spriteHeight, texCoords[2].x, texCoords[0].y, texCoords[0].x, texCoords[2].y)) {
                    GameObject gameObject = Prefabs.generatePipe(Direction.Up);
                    levelEditorComponents.getComponent(MouseControls.class).pickUpObject(gameObject);
                }
                ImGui.popID();
                ImGui.sameLine();

                sprite = pipes.getSprite(2);
                id = sprite.getTexID();
                texCoords = sprite.getTexCoords();

                ImGui.pushID(uid++);
                if (ImGui.imageButton(id, spriteWidth, spriteHeight, texCoords[2].x, texCoords[0].y, texCoords[0].x, texCoords[2].y)) {
                    GameObject gameObject = Prefabs.generatePipe(Direction.Right);
                    levelEditorComponents.getComponent(MouseControls.class).pickUpObject(gameObject);
                }
                ImGui.popID();
                ImGui.sameLine();

                sprite = pipes.getSprite(3);
                id = sprite.getTexID();
                texCoords = sprite.getTexCoords();

                ImGui.pushID(uid++);
                if (ImGui.imageButton(id, spriteWidth, spriteHeight, texCoords[2].x, texCoords[0].y, texCoords[0].x, texCoords[2].y)) {
                    GameObject gameObject = Prefabs.generatePipe(Direction.Left);
                    levelEditorComponents.getComponent(MouseControls.class).pickUpObject(gameObject);
                }
                ImGui.popID();
                ImGui.sameLine();



                ImGui.endTabItem();
            }

            if (ImGui.beginTabItem("Sounds")) {
                Collection<Sound> sounds = AssetPool.getAllSounds();
                for (Sound sound : sounds) {
                    File tmp = new File(sound.getFilepath());
                    if (ImGui.button(tmp.getName())) {
                        if (!sound.isPlaying())
                            sound.play();
                        else
                            sound.stop();
                    }

                    if (ImGui.getContentRegionAvailX() > 100)
                        ImGui.sameLine();
                }

                ImGui.sameLine();

                ImGui.endTabItem();
            }

            ImGui.endTabBar();
        }


        ImGui.end();
    }
}
