package DreamEngine;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

public class KeyInputHandler {
    private static KeyInputHandler instance = null;

    private boolean keyPressed[] = new boolean[350];

    private KeyInputHandler(){

    }

    public static KeyInputHandler get(){
        if(KeyInputHandler.instance == null){
            KeyInputHandler.instance = new KeyInputHandler();
        }
        return KeyInputHandler.instance;
    }
    public static void keyCallback(long window, int key, int scancode, int action, int mods){
        if(action==GLFW_PRESS){
            get().keyPressed[key] = true;
        } else if((action==GLFW_RELEASE)){
            get().keyPressed[key] = false;
        }
    }
    public static boolean isKeyPressed(int key){
        if(key < get().keyPressed.length)
            return get().keyPressed[key];
        return false;
    }
}

