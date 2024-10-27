package vuluu.nlu.engine;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class KeyListener {

  static KeyListener keyListener;
  boolean keyPressed[] = new boolean[350];

  private KeyListener() {

  }

  public static KeyListener get() {
    if (keyListener == null) {
      keyListener = new KeyListener();
    }
    return keyListener;
  }

  public static void keyCallBack(long window, int key, int scanCode, int action, int mods) {
    if (action == GLFW_PRESS) {
      get().keyPressed[key] = true;
    } else if (action == GLFW_RELEASE) {
      get().keyPressed[key] = false;
    }
  }

  public static boolean isKeyPressed(int keyCode) {
    if (keyCode < get().keyPressed.length) {
      return get().keyPressed[keyCode];
    }

    return false;
  }


}
