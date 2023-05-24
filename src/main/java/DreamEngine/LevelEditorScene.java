package DreamEngine;

import components.FontRenderer;
import components.SpriteRenderer;
import org.joml.Vector2f;
import org.lwjgl.BufferUtils;
import renderer.Shader;
import renderer.Texture;
import utils.Time;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class LevelEditorScene extends Scene{
    private boolean changingScene = false;

    private float timeToChangeScene = 2.0f;

    private float[] vertexArray ={
         //position                  //color                //UV coordinates
        100.5f, 0.5f, 0.0f,          1.0f, 0.0f,0.0f,1.0f,  1,1,               //bottom right
        0.5f,100.5f, 0.0f,          0.0f,1.0f, 0.0f, 1.0f,  0,0,               //top left
        100.5f, 100.5f, 0.0f,       0.0f,0.0f, 1.0f, 1.0f,  1,0,               //top right
        -0.5f, -0.5f, 0.0f,         1.0f,1.0f, 0.0f, 1.0f,  0,1,               //top right
    };

    //counter-clockwise
    private int[] elementArray ={
            2,1,0,//top right triangle
            0,1,3 //bottom left triangle
    };

    private int vaoID, vboID, eboID;
    Shader defaultShader;
    private Texture testTexture;
    GameObject testObj;
    private boolean firstTime = false;
    public LevelEditorScene(){

    }
    @Override
    public void init(){
        System.out.println("Creating test obj");

        this.testObj = new GameObject("test obj");
        this.testObj.addComponent(new SpriteRenderer());
        this.testObj.addComponent(new FontRenderer());

        this.addGameObjectToScene(this.testObj);

        this.camera = new Camera(new Vector2f(-200,-300));

        defaultShader = new Shader("assets/shaders/default.glsl");
        defaultShader.compile();

        this.testTexture = new Texture("assets/images/testImage.png");
        //generate vao vbo ebo
        vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);
        //create a float buffer of vertices
        FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(vertexArray.length);
        vertexBuffer.put(vertexArray).flip();
        //create vbo upload vertex buffer
        vboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW);

        //create indices
        IntBuffer elementBuffer  = BufferUtils.createIntBuffer(elementArray.length);
        elementBuffer.put(elementArray).flip();

        eboID = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, elementBuffer, GL_STATIC_DRAW);

        //add vertex att pointers
        int positionsSize = 3;
        int colorSize = 4;
        int uvSize = 2;
        int vertexSizeBytes = (positionsSize + colorSize + uvSize) * Float.BYTES;
        glVertexAttribPointer(0,positionsSize, GL_FLOAT, false, vertexSizeBytes, 0);
        glEnableVertexAttribArray(0);
        glVertexAttribPointer(1, colorSize, GL_FLOAT, false, vertexSizeBytes,
                positionsSize * Float.BYTES);
        glEnableVertexAttribArray(1);

        glVertexAttribPointer(2, uvSize,GL_FLOAT, false, vertexSizeBytes,
                (positionsSize + colorSize ) * Float.BYTES);
        glEnableVertexAttribArray(2);


    }
    @Override
    public void update(float dt) {
        //bind shader program
        camera.position.x -=20*dt;
        camera.position.y -=20*dt;
        defaultShader.use();

        //upload texture to shader

        defaultShader.uploadTexture("TEX_SAMPLER", 0);
        glActiveTexture(GL_TEXTURE0);
        testTexture.bind();

        defaultShader.uploadMat4f("uProjection", camera.getProjectionMatrix());
        defaultShader.uploadMat4f("uView", camera.getViewMatrix());
        defaultShader.uploadFloat("uTime", Time.getTime());

        glBindVertexArray(vaoID);

        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        glDrawElements(GL_TRIANGLES, elementArray.length, GL_UNSIGNED_INT,0);

        //unbind
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glBindVertexArray(0);
        defaultShader.detach();
        if(!firstTime){
            System.out.println("Creating gameObject");
            GameObject go = new GameObject("game test 2");
            go.addComponent(new SpriteRenderer());
            this.addGameObjectToScene(go);
            firstTime = true;
        }
        for(GameObject go : gameObjects){
            go.update(dt);
        }
    }
}
