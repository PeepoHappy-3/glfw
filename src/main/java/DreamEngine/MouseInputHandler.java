package DreamEngine;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

public class MouseInputHandler {
    private static MouseInputHandler instance;
    private double scrollX, scrollY;
    private double xPos, yPos, xPosLast, yPosLast;
    private boolean[] mouseButtonPressed = new boolean[3];
    private boolean isDragging;

    private MouseInputHandler(){
        this.scrollX = 0.0;
        this.scrollY = 0.0;
        this.xPos = 0.0;
        this.yPos =0.0;
        this.xPosLast =0.0;
        this.yPosLast = 0.0;
    }

    public static MouseInputHandler get(){
        if(MouseInputHandler.instance==null){
            MouseInputHandler.instance = new MouseInputHandler();
        }
        return MouseInputHandler.instance;
    }

    public static void mousePosCallback(long window, double xpos, double ypos){
        get().xPosLast = get().xPos;
        get().yPosLast = get().yPos;
        get().xPos = xpos;
        get().yPos = ypos;
        get().isDragging = get().mouseButtonPressed[0] || get().mouseButtonPressed[1]||get().mouseButtonPressed[2];
    }

    public static void mouseButtonCallback(long window, int button, int action, int mods){
        if(action==GLFW_PRESS){
            if (button < get().mouseButtonPressed.length) {
                get().mouseButtonPressed[button] = true;
            }
        } else if(action==GLFW_RELEASE){
            if (button < get().mouseButtonPressed.length){
                get().mouseButtonPressed[button] = false;
                get().isDragging = false;
            }
        }
    }
    public static void mouseScrollCallback(long window, double xOffset, double yOffset){
        get().scrollY = yOffset;
        get().scrollX = xOffset;
    }
    public static void endFrame(){
        get().scrollX = 0;
        get().scrollY = 0;
        get().yPosLast = get().yPos;
        get().xPosLast = get().xPos;
    }
    public static float getX(){
        return (float)get().xPos;
    }
    public static float getY(){
        return (float)get().yPos;
    }
    public static float getDx(){
        return (float) (get().xPosLast - get().xPos);
    }
    public static float getDy(){
        return (float) (get().yPosLast - get().yPos);
    }

    public static float getScrollX(){
        return (float)get().scrollX;
    }
    public static float getScrollY(){
        return (float)get().scrollY;
    }
    public static boolean getDragging(){
        return get().isDragging;
    }
    public static boolean getButtonPressed(int button){
        if (button < get().mouseButtonPressed.length){
            return get().mouseButtonPressed[button];
        }
        return false;
    }
}
