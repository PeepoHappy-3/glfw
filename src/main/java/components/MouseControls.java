package components;

import DreamEngine.GameObject;
import DreamEngine.MouseInputHandler;
import DreamEngine.Window;
import utils.Settings;

import java.awt.event.MouseListener;

import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;

public class MouseControls extends Component {
    GameObject holdingObj = null;
    public void pickUpObject(GameObject go){
        this.holdingObj = go;
        Window.getScene().addGameObjectToScene(go);
    }

    public void place(){
        this.holdingObj=null;
    }

    @Override
    public void update(float dt) {
        if(holdingObj!=null){
            holdingObj.transform.position.x = MouseInputHandler.getOrthoX() - 16;
            holdingObj.transform.position.y = MouseInputHandler.getOrthoY() - 16;

            holdingObj.transform.position.x = (int)(holdingObj.transform.position.x / Settings.GRID_WIDTH)
                    * Settings.GRID_WIDTH;
            holdingObj.transform.position.y = (int)(holdingObj.transform.position.y / Settings.GRID_HEIGHT)
                    * Settings.GRID_HEIGHT;

            System.out.println("x obj: " + holdingObj.transform.position.x + " x mouse: "
                    + (MouseInputHandler.getOrthoX()-16));
            if(MouseInputHandler.getButtonPressed(GLFW_MOUSE_BUTTON_LEFT)){
                place();
            }
        }
    }
}
