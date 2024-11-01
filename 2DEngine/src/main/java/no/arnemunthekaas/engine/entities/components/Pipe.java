package no.arnemunthekaas.engine.entities.components;

import no.arnemunthekaas.engine.Window;
import no.arnemunthekaas.engine.entities.Direction;
import no.arnemunthekaas.engine.entities.GameObject;
import no.arnemunthekaas.engine.events.listeners.KeyListener;
import no.arnemunthekaas.utils.AssetPool;
import no.arnemunthekaas.utils.GameConstants;
import org.jbox2d.dynamics.contacts.Contact;
import org.joml.Vector2f;

import static org.lwjgl.glfw.GLFW.*;

public class Pipe extends Component {

    private Direction direction;
    private String connectingPipeName = "";
    private boolean isEntrance = false;
    private transient GameObject connectingPipe;
    private transient float entranceVectorTolerance = 0.6f;
    private transient PlayerController collidingPlayer;

    public Pipe(Direction direction) {
        this.direction = direction;
    }

    @Override
    public void start() {
        connectingPipe = Window.getScene().getGameObject(connectingPipeName);
    }

    @Override
    public void update(float dt) {
        if (connectingPipe == null)
            return;


        if (collidingPlayer != null && isEntrance) {
            boolean enter = false;
            switch (direction) {
                case Up -> {
                    // TODO, controls settings file
                    if (KeyListener.isKeyPressed(GLFW_KEY_DOWN) || KeyListener.isKeyPressed(GLFW_KEY_S)) {
                        enter = true;
                    }
                    break;
                }

                case Down -> {
                    if (KeyListener.isKeyPressed(GLFW_KEY_UP) || KeyListener.isKeyPressed(GLFW_KEY_W)) {
                        enter = true;
                    }
                    break;
                }

                case Left -> {
                    if (KeyListener.isKeyPressed(GLFW_KEY_RIGHT) || KeyListener.isKeyPressed(GLFW_KEY_D)) {
                        enter = true;
                    }
                    break;
                }

                case Right -> {
                    if (KeyListener.isKeyPressed(GLFW_KEY_LEFT) || KeyListener.isKeyPressed(GLFW_KEY_A)) {
                        enter = true;
                    }
                    break;
                }
            }

            if (enter) {
                collidingPlayer.setPosition(getPlayerPosition(connectingPipe));
                AssetPool.getSound("assets/audio/assets_sounds_pipe.ogg").play();
            }
        }
    }

    @Override
    public void beginContact(GameObject collidingObject, Contact contact, Vector2f contactNormal) {
        PlayerController playerController = collidingObject.getComponent(PlayerController.class);

        if(playerController != null) {
            switch (direction) {
                case Up -> {
                    if (contactNormal.y < entranceVectorTolerance)
                        return;
                    break;
                }

                case Down -> {
                    if (contactNormal.y > -entranceVectorTolerance)
                        return;
                    break;
                }

                case Left -> {
                    if (contactNormal.x > -entranceVectorTolerance)
                        return;
                    break;
                }

                case Right -> {
                    if (contactNormal.x < entranceVectorTolerance)
                        return;
                    break;
                }
            }

            collidingPlayer = playerController;


        }
    }

    @Override
    public void endContact(GameObject collidingObject, Contact contact, Vector2f contactNormal) {
        PlayerController playerController = collidingObject.getComponent(PlayerController.class);

        if(playerController != null) {
            collidingPlayer = null;
        }
    }

    private Vector2f getPlayerPosition(GameObject pipe) {
        Pipe pipeComponent = pipe.getComponent(Pipe.class);

        switch (pipeComponent.direction) {
            case Up -> {
                return new Vector2f(pipe.transform.position).add(0.0f, GameConstants.GRID_HEIGHT * 2);
            }

            case Down -> {
                return new Vector2f(pipe.transform.position).add(0.0f, -GameConstants.GRID_HEIGHT * 2);
            }

            case Left -> {
                return new Vector2f(pipe.transform.position).add( -GameConstants.SCREEN_WIDTH * 2, 0.0f);
            }

            case Right -> {
                return new Vector2f(pipe.transform.position).add(GameConstants.SCREEN_WIDTH * 2, 0.0f);
            }


        }
        return new Vector2f();
    }
}
