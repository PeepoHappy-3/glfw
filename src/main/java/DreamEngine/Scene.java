package DreamEngine;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import components.SpriteRenderer;
import imgui.ImGui;
import renderer.Renderer;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public abstract class Scene {
    protected Renderer renderer = new Renderer();
    protected Camera camera;
    private boolean isRunning = false;
    protected List<GameObject> gameObjects = new ArrayList<>();
    protected GameObject activeGameObject = null;
    protected boolean levelLoaded = false;
    public Scene(){}
    public  void init(){

    };
    public void start(){
        for(GameObject go : gameObjects){
            go.start();
            this.renderer.add(go);
        }
        isRunning = true;
    }
    public void addGameObjectToScene(GameObject go){
        if(!isRunning){
            gameObjects.add(go);
        } else {
            gameObjects.add(go);
            go.start();
            this.renderer.add(go);
        }
    }
    public abstract void update(float dt);

    public Camera camera(){
        return this.camera;
    }
    public void sceneImgui(){
        if (activeGameObject != null) {
            ImGui.begin("Inspector");
            activeGameObject.imgui();
            ImGui.end();
        }
        imgui();
    }
    public void imgui(){

    }

    public void saveExit(){
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(Component.class, new ComponentDeserializer())
                .registerTypeAdapter(GameObject.class, new GameObjectDeserializer())
                .create();
       try {
           FileWriter fw = new FileWriter("level.txt");
           fw.write(gson.toJson(this.gameObjects));
           fw.close();
       } catch(IOException e){
           e.printStackTrace();
       }

    }
    public void load(){
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(Component.class, new ComponentDeserializer())
                .registerTypeAdapter(GameObject.class, new GameObjectDeserializer())
                .create();
          String path = "";
            try{
                path  = new String(Files.readAllBytes(Paths.get("level.txt")));
            } catch(IOException e){
                e.printStackTrace();
            }
            if(!path.equals("")){
                GameObject[] objs = gson.fromJson(path, GameObject[].class);
                for(int i =0; i < objs.length;i++){
                    addGameObjectToScene(objs[i]);
                }
                this.levelLoaded = true;
            }
//        String ser = gson.toJson(obj1);
//        gson.fromJson(ser, SpriteRenderer.class);
//        System.out.println(ser);
//        GameObject o = gson.fromJson(ser, GameObject.class);
//        System.out.println(o);

    }
}
