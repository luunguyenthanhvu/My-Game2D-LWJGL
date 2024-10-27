package vuluu.nlu.engine;


import static org.lwjgl.glfw.GLFW.GLFW_FALSE;
import static org.lwjgl.glfw.GLFW.GLFW_MAXIMIZED;
import static org.lwjgl.glfw.GLFW.GLFW_RESIZABLE;
import static org.lwjgl.glfw.GLFW.GLFW_TRUE;
import static org.lwjgl.glfw.GLFW.GLFW_VISIBLE;
import static org.lwjgl.glfw.GLFW.glfwCreateWindow;
import static org.lwjgl.glfw.GLFW.glfwDefaultWindowHints;
import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwShowWindow;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.glfw.GLFW.glfwSwapInterval;
import static org.lwjgl.glfw.GLFW.glfwWindowHint;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.system.MemoryUtil.NULL;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.Observable;
import java.util.Observer;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;
import vuluu.nlu.constant.GameConstants;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class Window implements Observer {

  int width, height;
  String title;
  long glfwWindow;
  static Window window = null;

  public Window() {
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    this.width = (int) screenSize.getWidth();
    this.height = (int) screenSize.getHeight();
    this.title = GameConstants.GAME_NAME;
  }

  public static Window get() {
    if (Window.window == null) {
      Window.window = new Window();
    }
    return Window.window;
  }

  public void run() {
    System.out.println("LWGL " + Version.getVersion());

    init();
    loop();
  }

  private void init() {
    // Setup an error callback
    GLFWErrorCallback.createPrint(System.err).set();

    // Initialize GLFW
    if (!glfwInit()) {
      throw new IllegalStateException("Unable to initialize GLFW.");
    }

    // config GLFW
    glfwDefaultWindowHints();
    glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
    glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
    glfwWindowHint(GLFW_MAXIMIZED, GLFW_TRUE);

    // Create the window
    glfwWindow = glfwCreateWindow(this.width, this.height, this.title, NULL, NULL);

    if (glfwWindow == NULL) {
      throw new IllegalStateException("Failed to create the GLFW window");
    }

    // make the open GL context current
    glfwMakeContextCurrent(glfwWindow);

    // enable v-sync
    glfwSwapInterval(1);

    // make the window visible
    glfwShowWindow(glfwWindow);

    GL.createCapabilities();
  }

  private void loop() {
    while (!glfwWindowShouldClose(glfwWindow)) {
      glfwPollEvents();

      glClearColor(1.0f, 0.0f, 0.0f, 1.0f);
      glClear(GL_COLOR_BUFFER_BIT);

      glfwSwapBuffers(glfwWindow);
    }
  }

  @Override
  public void update(Observable o, Object arg) {

  }
}
