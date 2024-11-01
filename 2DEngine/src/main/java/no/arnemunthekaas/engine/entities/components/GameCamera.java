package no.arnemunthekaas.engine.entities.components;

import no.arnemunthekaas.engine.Window;
import no.arnemunthekaas.engine.entities.GameObject;
import no.arnemunthekaas.engine.renderer.Camera;
import no.arnemunthekaas.utils.GameConstants;
import org.joml.Vector4f;

public class GameCamera extends Component{

    private transient GameObject player;
    private transient Camera sceneCamera;
    private transient float largestX = Float.MIN_VALUE;
    private transient float undergroundYLevel = 0.0f;
    private transient float cameraBuffer = 1.5f;
    private transient float playerBuffer = GameConstants.GRID_HEIGHT;

    private Vector4f skyColor = new Vector4f(92.0f / 255.0f, 148.0f / 255.0f, 252.0f / 255.0f, 1);
    private Vector4f undergroundColor = new Vector4f(0, 0, 0, 1);

    /**
     *
     * @param sceneCamera
     */
    public GameCamera(Camera sceneCamera) {
        this.sceneCamera = sceneCamera;
    }

    @Override
    public void start() {
        // TODO: refactor so that there can be several game controllers?
        // TODO: support for movement all ways
        this.player = Window.getScene().getGameObjectWith(PlayerController.class);
        this.sceneCamera.clearColor.set(skyColor);
        this.sceneCamera.position.set(this.player.transform.position.x - this.sceneCamera.getProjectionSize().x / 2,
                this.player.transform.position.y - this.sceneCamera.getProjectionSize().y / 2);

        this.undergroundYLevel = this.sceneCamera.position.y - this.sceneCamera.getProjectionSize().y
                - this.cameraBuffer;
    }

    @Override
    public void update(float dt) {
        if(player != null && !player.getComponent(PlayerController.class).hasWon()) {
//            sceneCamera.position.x = Math.max(player.transform.position.x - 2.5f, largestX);
//            largestX = Math.max(largestX, sceneCamera.position.x);

            sceneCamera.position.set(player.transform.position.x - sceneCamera.getProjectionSize().x / 2.0f, 0);

            if (player.transform.position.y < -playerBuffer) {
                this.sceneCamera.position.y = undergroundYLevel;
                this.sceneCamera.clearColor.set(undergroundColor);
            } else if (player.transform.position.y > 0) {
                this.sceneCamera.clearColor.set(skyColor);
            }



        }
    }
}
