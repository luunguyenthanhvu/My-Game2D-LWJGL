package vuluu.nlu.engine;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.Observable;
import java.util.Observer;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import vuluu.nlu.constant.GameConstants;
import vuluu.nlu.scenes.Scene;
import vuluu.nlu.scenes.SceneInitializer;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class Window implements Observer {

  double width, height;
  String title;

  static Window window = null;
  static Scene currentScene;

  public Window() {
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    this.width = screenSize.getWidth();
    this.height = screenSize.getHeight();
    this.title = GameConstants.GAME_NAME;

    // EventSystem.addObserver(this);
  }

  public static void changScene(SceneInitializer sceneInitializer) {
    if (currentScene != null) {
      // currentScene.destroy();
    }
    // getImGui
  }


  public void run() {

  }

  @Override
  public void update(Observable o, Object arg) {

  }
}
