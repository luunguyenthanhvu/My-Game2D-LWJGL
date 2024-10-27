package vuluu.nlu.engine;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class MouseListener {

  static MouseListener instance;
  double scrollX, scrollY;
  double xPos, yPos, lastY, lastX;
  boolean mouseButtonPressed[] = new boolean[3];
  boolean isDragging;

  private MouseListener() {
    this.scrollX = 0.0;
    this.scrollY = 0.0;
    this.xPos = 0.0;
    this.yPos = 0.0;
    this.lastX = 0.0;
    this.lastY = 0.0;
  }

  public static MouseListener get() {
    if (instance == null) {
      instance = new MouseListener();
    }

    return instance;
  }

  // create call back
  public static void mousePosCallback(long window, double xPos, double yPos) {
    get().lastX = get().xPos;
    get().lastY = get().yPos;
    get().xPos = xPos;
    get().yPos = yPos;
    get().isDragging =
        get().mouseButtonPressed[0] || get().mouseButtonPressed[1] || get().mouseButtonPressed[2];
  }

  public static void mouseButtonCallback(long window, int button, int action, int mods) {
    if (action == GLFW_PRESS) {
      // check mouse (i just check for gaming mouse which mouse has > 3 button just in case)
      if (button < get().mouseButtonPressed.length) {
        get().mouseButtonPressed[button] = true;
      }
    } else if (action == GLFW_RELEASE) {
      if (button < get().mouseButtonPressed.length) {
        get().mouseButtonPressed[button] = false;
        get().isDragging = false;
      }
    }
  }

  public static void mouseScrollCallBack(long window, double xOffset, double yOffset) {
    get().scrollX = xOffset;
    get().scrollY = yOffset;
  }

  public static void endFrame() {
    get().scrollX = 0;
    get().scrollY = 0;
    get().lastX = get().xPos;
    get().lastY = get().yPos;
  }

  public static float getX() {
    return (float) get().xPos;
  }

  public static float getY() {
    return (float) get().yPos;
  }

  public static float getDx() {
    return (float) (get().lastX - get().xPos);
  }

  public static float getDy() {
    return (float) (get().lastY - get().yPos);
  }

  public static float getScrollX() {
    return (float) get().scrollX;
  }

  public static float getScrollY() {
    return (float) get().scrollY;
  }

  public static boolean isDragging() {
    return get().isDragging;
  }

  public static boolean mouseButtonDown(int button) {
    if (button < get().mouseButtonPressed.length) {
      return get().mouseButtonPressed[button];
    } else {
      return false;
    }
  }

}
