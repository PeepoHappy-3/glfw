package DreamEngine;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window {
    private static Window window = null;
    private int width, height;
    private String title;
    private long glfwWindow;
    public float r,g,b,a;
    private boolean fadeToBlack = false;
    private static Scene currentScene;
    private ImGuiLayer imGuiLayer;
    private Window(){
        this.width = 640;
        this.height = 480;
        this.title = "Игра мечты";
        this.r=1;
        this.g=1;
        this.b=1;
        this.a=1;
    }
    public static Window get(){
        if(Window.window == null){
            Window.window = new Window();
        }
       return Window.window;
    }


    public static Scene getScene(){
        return get().currentScene;
    }
    public static void changeScene(int newScene){
        switch (newScene){
            case 0:
                currentScene = new LevelEditorScene();
                currentScene.init();
                currentScene.start();
                break;
            case 1:
                currentScene = new LevelScene();
                currentScene.init();
                currentScene.start();
                break;
            default:
                assert false: "Unknown scene'" + newScene + "'";
        }
    }

    public void run(){
        init();
        loop();

        //free memory
        glfwFreeCallbacks(glfwWindow);
        glfwDestroyWindow(glfwWindow);
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    public void init(){
        //setup err callback
        GLFWErrorCallback.createPrint(System.err).set();

        //init glfw
        if(!glfwInit()){
            throw  new IllegalStateException("Unable to initialize GLFW");
        }
        //config
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
        glfwWindowHint(GLFW_MAXIMIZED, GLFW_FALSE);

        glfwWindow = glfwCreateWindow(this.width, this.height, this.title,NULL,NULL);

        if(glfwWindow == NULL){
            throw new IllegalStateException("Failed to create window");
        }

        glfwSetCursorPosCallback(glfwWindow, MouseInputHandler::mousePosCallback);
        glfwSetMouseButtonCallback(glfwWindow, MouseInputHandler::mouseButtonCallback);
        glfwSetScrollCallback(glfwWindow, MouseInputHandler::mouseScrollCallback);
        glfwSetKeyCallback(glfwWindow, KeyInputHandler::keyCallback);
        //make openGL context current
        glfwMakeContextCurrent(glfwWindow);
        //v-sync
        glfwSwapInterval(1);

        glfwShowWindow(glfwWindow);

        // This line is critical for LWJGL's interoperation with GLFW's
        // OpenGL context, or any context that is managed externally.
        // LWJGL detects the context that is current in the current thread,
        // creates the GLCapabilities instance and makes the OpenGL
        // bindings available for use.
        GL.createCapabilities();
        glEnable(GL_BLEND);
        glBlendFunc(GL_ONE, GL_ONE_MINUS_SRC_ALPHA);
        this.imGuiLayer = new ImGuiLayer(glfwWindow);
        this.imGuiLayer.initImGui();
        Window.changeScene(0);
    }
    public void loop(){
        float startTime = (float) glfwGetTime();
        float endTime;
        float dt = -1.0f;
        while ( !glfwWindowShouldClose(glfwWindow) ) {
            glClearColor(r, g, b, a);
            glClear(GL_COLOR_BUFFER_BIT); // clear the framebuffer
            if(dt >= 0)
                currentScene.update(dt);

            if(fadeToBlack){
                r = Math.max(r - 0.01f,0);
                g = Math.max(g - 0.01f,0);
                b = Math.max(b - 0.01f,0);
            }

            if(KeyInputHandler.isKeyPressed(GLFW_KEY_SPACE)){
                fadeToBlack=true;
            }
            this.imGuiLayer.update(dt, currentScene);
            glfwSwapBuffers(glfwWindow); // swap the color buffers
            glfwPollEvents();
            endTime = (float)glfwGetTime();
            dt = endTime - startTime;
            startTime = endTime;
        }
    }

    public static int getWidth(){
        return get().width;
    }

    public static int getHeight(){
        return get().height;
    }
}
