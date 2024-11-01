package no.arnemunthekaas.engine;

import no.arnemunthekaas.engine.entities.GameObject;
import no.arnemunthekaas.engine.entities.components.physics2d.Physics2D;
import no.arnemunthekaas.engine.events.listeners.KeyListener;
import no.arnemunthekaas.engine.events.listeners.MouseListener;
import no.arnemunthekaas.editor.imgui.ImGuiLayer;
import no.arnemunthekaas.engine.renderer.*;
import no.arnemunthekaas.engine.events.observers.Observer;
import no.arnemunthekaas.engine.events.observers.events.Event;
import no.arnemunthekaas.engine.events.observers.events.EventSystem;
import no.arnemunthekaas.scenes.LevelEditorSceneInitializer;
import no.arnemunthekaas.scenes.LevelSceneInitializer;
import no.arnemunthekaas.scenes.Scene;
import no.arnemunthekaas.scenes.SceneInitializer;
import no.arnemunthekaas.utils.AssetPool;
import no.arnemunthekaas.utils.GameConstants;
import org.joml.Vector4f;
import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.openal.AL;
import org.lwjgl.openal.ALC;
import org.lwjgl.openal.ALCCapabilities;
import org.lwjgl.openal.ALCapabilities;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.openal.ALC10.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

/**
 *  https://www.lwjgl.org/guide
 */
public class Window implements Observer {
    private int width, height;
    private String title = "2D Engine";
    public long glfwWindow;
    private ImGuiLayer imGuiLayer;
    private Framebuffer framebuffer;
    private PickingTexture pickingTexture;

    // Color to clear screen
    public float r = 1, g = 1, b = 1, a = 1;

    private static Window window = null;

    private long audioContext;
    private long audioDevice;

    private static Scene currentScene;
    private boolean runtimePlaying;

    private Window() {
        // Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.width = 1440;
        this.height = 900;
        this.title = GameConstants.GAME_NAME;

        EventSystem.addObserver(this);
    }

    /**
     *
     * @param sceneInitializer
     */
    public static void changeScene(SceneInitializer sceneInitializer) {
        if (currentScene != null) {
            currentScene.destroy();
        }

        getImGuiLayer().getPropertiesWindow().setActiveGameObject(null);
        currentScene = new Scene(sceneInitializer);
        currentScene.load();
        currentScene.init();
        currentScene.start();
    }

    /**
     * Returns the window object, if the window = null, it creates a new window.
     *
     * @return Window object
     */
    public static Window get() {
        if (Window.window == null)
            Window.window = new Window();

        return Window.window;
    }

    /**
     * Get current window Scene.
     * @return Scene object
     */
    public static Scene getScene() {
        return currentScene;
    }

    /**
     * Get window width
     * @return width
     */
    public static int getWidth() {
        return get().framebuffer.width;
    }

    /**
     * Get window height
     * @return height
     */
    public static int getHeight() {
        return get().framebuffer.height;
    }

    /**
     * Set new width
     * @param width new width
     */
    public static void setWidth(int width) {
        get().width = width;
    }

    /**
     * Set new height
     * @param height new height
     */
    public static void setHeight(int height) {
        get().height = height;
    }

    /**
     * Returns the windows framebuffer object
     * @return framebuffer
     */
    public static Framebuffer getFramebuffer() {
        return get().framebuffer;
    }

    /**
     * Returns target aspect ratio specified in GameConstants
     * @return
     */
    public static float getTargetAspectRatio() {
        return GameConstants.ASPECT_RATIO;
    }

    /**
     *
     * @return
     */
    public static ImGuiLayer getImguiLayer() {
        return get().imGuiLayer;
    }

    /**
     *
     * @return
     */
    public static Physics2D getPhysics() {
        return currentScene.getPhysics();
    }

    /**
     * Runs the LWJGL window
     */
    public void run() {
        System.out.println("Hello LWJGL " + Version.getVersion() + "!");

        init();
        loop();

        // Destroy audio context
        alcDestroyContext(audioContext);
        alcCloseDevice(audioDevice);

        // Free memory
        glfwFreeCallbacks(glfwWindow);
        glfwDestroyWindow(glfwWindow);

        // Terminate GLFW and free the error callback
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    /**
     * Initializes game
     */
    private void init() {
        // Setup an error callback
        GLFWErrorCallback.createPrint(System.err).set();

        // Initialize GLFW
        if (!glfwInit())
            throw new IllegalStateException("Unable to initialize GLFW.");

        // Configure GLFW
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
        glfwWindowHint(GLFW_MAXIMIZED, GLFW_TRUE);

        // macOS
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);

        // Antialiasing
//        glfwWindowHint(GLFW_SAMPLES, 4);

        // Create the window
        glfwWindow = glfwCreateWindow(this.width, this.height, this.title, NULL, NULL);
        if (glfwWindow == NULL)
            throw new IllegalStateException("Failed to create GLFW window.");

        glfwSetCursorPosCallback(glfwWindow, MouseListener::mousePosCallback);
        glfwSetMouseButtonCallback(glfwWindow, MouseListener::mouseButtonCallback);
        glfwSetScrollCallback(glfwWindow, MouseListener::mouseScrollCallback);
        glfwSetKeyCallback(glfwWindow, KeyListener::keyCallBack);

        glfwSetWindowSizeCallback(glfwWindow, (w, newWidth, newHeight) -> {
            Window.setWidth(newWidth);
            Window.setHeight(newHeight);
        });

        // Make the OpenGL context current
        glfwMakeContextCurrent(glfwWindow);
        // Enable v-sync
        glfwSwapInterval(1);

        // Make the window visible
        glfwShowWindow(glfwWindow);

        // Init audio device
        String defaultDeviceName = alcGetString(0, ALC_DEFAULT_DEVICE_SPECIFIER);
        audioDevice = alcOpenDevice(defaultDeviceName);

        int[] attributes = {0};
        audioContext = alcCreateContext(audioDevice, attributes);
        alcMakeContextCurrent(audioContext);

        ALCCapabilities alcCapabilities = ALC.createCapabilities(audioDevice);
        ALCapabilities alCapabilities = AL.createCapabilities(alcCapabilities);

        if (!alCapabilities.OpenAL10)
            assert false : "Audio library not supported.";

        // This line is critical for LWJGL's interoperation with GLFW's
        // OpenGL context, or any context that is managed externally.
        // LWJGL detects the context that is current in the current thread,
        // creates the GLCapabilities instance and makes the OpenGL
        // bindings available for use.
        GL.createCapabilities();

        // Transparency and blending
        glEnable(GL_BLEND);
        glBlendFunc(GL_ONE, GL_ONE_MINUS_SRC_ALPHA);

        // Antialiasing
//        glEnable(GL_MULTISAMPLE);



        this.framebuffer = new Framebuffer(GameConstants.SCREEN_WIDTH, GameConstants.SCREEN_HEIGHT);
        this.pickingTexture = new PickingTexture(GameConstants.SCREEN_WIDTH, GameConstants.SCREEN_HEIGHT);
        glViewport(0,0, GameConstants.SCREEN_WIDTH, GameConstants.SCREEN_HEIGHT);

        this.imGuiLayer = new ImGuiLayer(glfwWindow, pickingTexture);
        this.imGuiLayer.initImGui();

        Window.changeScene(new LevelEditorSceneInitializer());
    }

    /**
     * Core game loop
     */
    private void loop() {
        float beginTime = (float) glfwGetTime();
        float endTime;
        float dt = - 1.0f;
        Shader defaultShader = AssetPool.getShader("assets/shaders/default.glsl");
        Shader pickingShader = AssetPool.getShader("assets/shaders/pickingShader.glsl");

        while (!glfwWindowShouldClose(glfwWindow)) {
            System.out.println("FPS: " + 1 / dt);

            // Poll events
            glfwPollEvents();

            // Render pass 1. Render picking texture
            glDisable(GL_BLEND);
            pickingTexture.enableWriting();

            glViewport(0, 0, GameConstants.SCREEN_WIDTH, GameConstants.SCREEN_HEIGHT);
            glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            Renderer.bindShader(pickingShader);
            currentScene.render();

            pickingTexture.disableWriting();

            glEnable(GL_BLEND);

            // Render pass 2. Render game
            DebugDraw.beginFrame();

            this.framebuffer.bind();

            Vector4f clearColor = getScene().getCamera().clearColor;
            glClearColor(clearColor.x, clearColor.y, clearColor.z, clearColor.w);
            glClear(GL_COLOR_BUFFER_BIT);


            if(dt >= 0) {
                Renderer.bindShader(defaultShader);

                if(runtimePlaying)
                    currentScene.update(dt);
                else
                    currentScene.editorUpdate(dt);

                currentScene.render();
                DebugDraw.draw();
            }
            this.framebuffer.unBind();

            this.imGuiLayer.update(dt, currentScene);
            glfwSwapBuffers(glfwWindow);

            KeyListener.endFrame();
            MouseListener.endFrame();

            endTime = (float) glfwGetTime();
            dt = endTime - beginTime;
            beginTime = endTime;
        }


    }

    /**
     *
     * @return
     */
    public static ImGuiLayer getImGuiLayer() {
        return get().imGuiLayer;
    }


    @Override
    public void onNotify(GameObject object, Event event) {
        switch (event.type) {
            case GameEnginePlay:
                this.runtimePlaying = true;
                currentScene.save();
                Window.changeScene(new LevelSceneInitializer());
                break;
            case GameEngineStop:
                this.runtimePlaying = false;
                Window.changeScene(new LevelEditorSceneInitializer());
                break;
            case LoadLevel:
                Window.changeScene(new LevelEditorSceneInitializer());
                break;
            case SaveLevel:
                currentScene.save();
                break;
        }
    }
}
