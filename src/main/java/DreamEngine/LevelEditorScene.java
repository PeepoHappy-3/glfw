package DreamEngine;

import components.Sprite;
import components.SpriteRenderer;
import components.SpriteSheet;
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

       sprites = AssetPool.getSpriteSheet("assets/images/spritesheet.png");
       obj1 = new GameObject("Object 1", new Transform(new Vector2f(200,100),
               new Vector2f(256,256)), 2);
       //obj1.addComponent(new SpriteRenderer(sprites.getSprite(0)));
        obj1.addComponent(new SpriteRenderer(
                new Sprite(AssetPool.getTexture("assets/images/blendImage1.png"))));
       this.addGameObjectToScene(obj1);
        GameObject obj2 = new GameObject("Object 2", new Transform(new Vector2f(400,100),
                new Vector2f(256,256)),-1);
       // obj2.addComponent(new SpriteRenderer(sprites.getSprite(10)));
       // obj1.addComponent(new SpriteRenderer(sprites.getSprite(0)));
        obj2.addComponent(new SpriteRenderer(
                new Sprite(AssetPool.getTexture("assets/images/blendImage2.png"))));
        this.addGameObjectToScene(obj2);

    }

    private void loadResources(){
        AssetPool.getShader("assets/shaders/default.glsl");
        AssetPool.addSpriteSheet("assets/images/spritesheet.png",
                new SpriteSheet(AssetPool.getTexture("assets/images/spritesheet.png"),16,16,26,0));
    }

    private int sprIdx = 0;
    private float flipTime = 0.2f;
    private  float flipTimeLeft = 0.0f;
    @Override
    public void update(float dt) {
        for(GameObject go : gameObjects){
            go.update(dt);
        }
        this.renderer.render();
    }
}
