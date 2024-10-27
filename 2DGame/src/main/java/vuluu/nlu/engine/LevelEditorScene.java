package vuluu.nlu.engine;

import java.awt.event.KeyEvent;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class LevelEditorScene extends Scene {

  boolean changingScene = false;
  float timeTimeChangScene = 2;

  public LevelEditorScene() {
    System.out.println("in side editor scene");
  }

  @Override
  public void update(float dt) {

    System.out.println((1.0f / dt) + "FPS");

    if (!changingScene && KeyListener.isKeyPressed(KeyEvent.VK_SPACE)) {
      changingScene = true;
    }

    if (changingScene && timeTimeChangScene > 0) {
      timeTimeChangScene -= dt;
      Window.get().r -= dt * 5.0f;
      Window.get().g -= dt * 5.0f;
      Window.get().b -= dt * 5.0f;
    } else if (changingScene) {
      Window.changeScene(1);
    }
  }


}
