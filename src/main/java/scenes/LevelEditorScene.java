package scenes;

import DreamEngine.*;
import components.*;
import imgui.ImGui;
import imgui.ImVec2;
import org.joml.Vector2f;
import org.joml.Vector4f;
import scenes.Scene;
import utils.AssetPool;

public class LevelEditorScene extends Scene {
    private GameObject obj1;
    private SpriteSheet sprites;
    MouseControls mouseControls = new MouseControls();
    public LevelEditorScene(){

    }
    @Override
    public void init(){
        loadResources();
        this.camera = new Camera(new Vector2f(-250,0));
        sprites = AssetPool.getSpriteSheet("assets/images/spritesheets/decorationsAndBlocks.png");
        if(levelLoaded){
            this.activeGameObject = gameObjects.get(0);
            return;
        }

       obj1 = new GameObject("Object 1", new Transform(new Vector2f(200,100),
               new Vector2f(256,256)), -1);
       //obj1.addComponent(new SpriteRenderer(sprites.getSprite(0)));
        SpriteRenderer obj1spriteRenderer = new SpriteRenderer();
        obj1spriteRenderer.setColor(new Vector4f(1,0,0,1));
        obj1.addComponent(obj1spriteRenderer);

        //obj1.addComponent(new RigidBody());
       // obj1.addComponent(new SpriteRenderer(new Vector4f(1,0,0,1)));
                //new Sprite(AssetPool.getTexture("assets/images/blendImage1.png"))));this.addGameObjectToScene(obj1);
        //obj1.addComponent(new RigidBody());
        GameObject obj2 = new GameObject("Object 2", new Transform(new Vector2f(400,100),
                new Vector2f(256,256)),3);
       // obj2.addComponent(new SpriteRenderer(sprites.getSprite(10)));
       // obj1.addComponent(new SpriteRenderer(sprites.getSprite(0)));
        SpriteRenderer obj2spriteRenderer = new SpriteRenderer();
        Sprite obj2sprite = new Sprite();
        obj2sprite.setTexture(AssetPool.getTexture("assets/images/blendImage2.png"));
        obj2spriteRenderer.setSprite(obj2sprite);
        obj2.addComponent(obj2spriteRenderer);

        this.addGameObjectToScene(obj2);
        this.addGameObjectToScene(obj1);

    }

    private void loadResources(){

        AssetPool.getShader("assets/shaders/default.glsl");
        AssetPool.addSpriteSheet("assets/images/spritesheets/decorationsAndBlocks.png",
                new SpriteSheet(AssetPool.getTexture("assets/images/spritesheets/decorationsAndBlocks.png"),32,32,48,0));

    }
    @Override
    public void update(float dt) {
        mouseControls.update(dt);
        //MouseInputHandler.getOrthoX();
        for(GameObject go : gameObjects){
            go.update(dt);
        }
        this.renderer.render();
    }
    @Override
    public void imgui(){
        ImGui.begin("Test window");
        ImVec2 windowPos = new ImVec2();
        ImGui.getWindowPos(windowPos);
        ImVec2 windowSize = new ImVec2();
        ImGui.getWindowSize(windowSize);
        ImVec2 itemSpacing = new ImVec2();
        ImGui.getStyle().getItemSpacing(itemSpacing);
        float windowsX2 = windowPos.x + windowSize.x;
        for(int i = 0; i < sprites.size(); i++){
            Sprite sprite = sprites.getSprite(i);
            float spriteWidth = sprite.getWidth();
            float spriteHeight = sprite.getHeight();
            int id = sprite.getTexId();
            Vector2f[] texCoords = sprite.getTexCoords();

            ImGui.pushID(i);
            if(ImGui.imageButton(id, spriteHeight, spriteHeight,
                    texCoords[0].x, texCoords[0].y,
                    texCoords[2].x, texCoords[2].y)){
                GameObject o = Prefabs.generateSpriteObject(sprite,spriteWidth,spriteHeight);
                //attach to the mouse cursor
                mouseControls.pickUpObject(o);

            }
            ImGui.popID();

            ImVec2 lastButtonPos = new ImVec2();
            ImGui.getItemRectMax(lastButtonPos);
            float lastButtonX2 = lastButtonPos.x;
            float nextButtonX2 = lastButtonX2 + itemSpacing.x + spriteWidth;
            if(i + 1 < sprites.size() && nextButtonX2 < windowsX2){
                ImGui.sameLine();
            }
        }
        ImGui.end();
    }
}
