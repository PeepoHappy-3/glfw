package renderer;

import DreamEngine.Window;
import org.joml.Vector2d;
import org.joml.Vector2f;
import org.joml.Vector3f;
import utils.AssetPool;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class DebugDraw {
    private static int MAX_LINES = 1000;
    private static List<Line2D> lines = new ArrayList<>();

    public static float[] vertexArray = new float[MAX_LINES];

    private static Shader shader = AssetPool.getShader("assets/shaders/debugLine2D.glsl");

    private static int vaoID, vboID;

    private static boolean started = false;

    public static void start(){
        vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);

        vboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER,vboID);
        glBufferData(GL_ARRAY_BUFFER, vertexArray.length * Float.BYTES, GL_DYNAMIC_DRAW);

        glVertexAttribPointer(0, 3, GL_FLOAT, false, 6 * Float.BYTES, 0);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1, 3, GL_FLOAT, false, 6 * Float.BYTES, 3 * Float.BYTES);
        glEnableVertexAttribArray(1);

        glLineWidth(2.0f);
    }

    public static void beginFrame(){
        if(!started){
            start();
            started = true;
        }

        //Remove dead lines
        for(int i = 0; i < lines.size();i++){
            if(lines.get(i).beginFrame() < 0){
                lines.remove(i);
                i--;
            }
        }
    }

    public static void draw(){
        if(lines.size() < 0) return;

        int idx = 0;
        for(Line2D line : lines){
            for(int i =0; i < 2;i++){
                Vector2f pos = i ==0 ? line.getFrom() : line.getTo();
                Vector3f color = line.getColor();

                //load pos
                vertexArray[idx] = pos.x;
                vertexArray[idx + 1] = pos.y;
                vertexArray[idx + 2] = -10.f;

                //load color
                vertexArray[idx + 3] = color.x;
                vertexArray[idx + 4] = color.y;
                vertexArray[idx + 5] = color.z;
                idx += 6;
            }
        }
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        //glBufferData(GL_ARRAY_BUFFER, vertexArray,GL_DYNAMIC_DRAW);
        glBufferSubData(GL_ARRAY_BUFFER, 0,
                Arrays.copyOfRange(vertexArray, 0, lines.size() * 6 * 2));
        DebugDraw.shader.use();
        DebugDraw.shader.uploadMat4f("uProjection", Window.getScene().camera().getProjectionMatrix());
        DebugDraw.shader.uploadMat4f("uView", Window.getScene().camera().getViewMatrix());

        glBindVertexArray(vaoID);
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        glDrawArrays(GL_LINES, 0, lines.size() * 6 * 2);

        //unbind
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glBindVertexArray(0);

        shader.detach();
    }

    //===============================================
    //add line2d methods
    //===============================================

    public static void addLine2D(Vector2f from, Vector2f to){
        addLine2D(from, to, new Vector3f(0,1,0), 1);
    }
    public static void addLine2D(Vector2f from, Vector2f to, Vector3f color){
        addLine2D(from, to,color, 1);
    }
    public static void addLine2D(Vector2f from, Vector2f to, Vector3f color, int lifetime){
        if(lines.size() >= MAX_LINES) return;
        DebugDraw.lines.add(new Line2D(from, to, color, lifetime));
    }

    public static void print(){
        for(Line2D l : lines){
            System.out.println(l.getFrom() + " " + l.getTo() + " " + l.getColor());
        }
        System.out.println("====================================================");
    }
}
