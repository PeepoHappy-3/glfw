package DreamEngine;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import components.RigidBody;
import components.Sprite;
import components.SpriteRenderer;
import components.SpriteSheet;
import imgui.ImGui;
import org.joml.Vector2f;
import org.joml.Vector4f;
import utils.AssetPool;

public class LevelEditorScene extends Scene{
    private  GameObject obj1;
    private SpriteSheet sprites;
    public LevelEditorScene(){

    }
    @Override
    public void init(){
        loadResources();
        this.camera = new Camera(new Vector2f());
        if(levelLoaded){
            this.activeGameObject = gameObjects.get(0);
            return;
        }
       sprites = AssetPool.getSpriteSheet("assets/images/spritesheet.png");
       obj1 = new GameObject("Object 1", new Transform(new Vector2f(200,100),
               new Vector2f(256,256)), -1);
       //obj1.addComponent(new SpriteRenderer(sprites.getSprite(0)));
        SpriteRenderer obj1spriteRenderer = new SpriteRenderer();
        obj1spriteRenderer.setColor(new Vector4f(1,0,0,1));
        obj1.addComponent(obj1spriteRenderer);
        obj1.addComponent(new RigidBody());
       // obj1.addComponent(new SpriteRenderer(new Vector4f(1,0,0,1)));
                //new Sprite(AssetPool.getTexture("assets/images/blendImage1.png"))));
       this.addGameObjectToScene(obj1);

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

    }

    private void loadResources(){
        AssetPool.getShader("assets/shaders/default.glsl");
        AssetPool.addSpriteSheet("assets/images/spritesheet.png",
                new SpriteSheet(AssetPool.getTexture("assets/images/spritesheet.png"),16,16,26,0));
    }
    @Override
    public void update(float dt) {
        for(GameObject go : gameObjects){
            go.update(dt);
        }
        this.renderer.render();
    }
    @Override
    public void imgui(){
//        ImGui.begin("Test window");
//        ImGui.text("sdfsdf");
//        ImGui.end();
    }
}
