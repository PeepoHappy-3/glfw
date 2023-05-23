package renderer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL20.glGetShaderInfoLog;

public class Shader {
    private int shaderProgramID;
    private String vertexSrc;
    private String fragmentSrc;
    private String path;
    public Shader(String path){
        this.path = path;
        try {
        String src = new String(Files.readAllBytes(Paths.get(path)));
        String[] splitString = src.split("(#type)( )+([a-zA-Z]+)");

        int index = src.indexOf("#type") + 6;
        int eol = src.indexOf("\r\n", index);
        String firstPattern = src.substring(index, eol).trim();
        index = src.indexOf("#type", eol) +6;
        eol = src.indexOf("\r\n", index);
        String secondPattern = src.substring(index,eol).trim();
        if(firstPattern.equals("vertex")){
            vertexSrc = splitString[1];
        } else if (firstPattern.equals("fragment")){
            fragmentSrc = splitString[1];
        } else {
            throw new IOException("Unexpected token '" + firstPattern + "' in " + path);
        }
        if(secondPattern.equals("vertex")){
            vertexSrc = splitString[2];
        } else if (secondPattern.equals("fragment")){
            fragmentSrc = splitString[2];
        } else {
            throw new IOException("Unexpected token '" + secondPattern + "' in " + path);
        }
        } catch(IOException e){
            e.printStackTrace();
            assert false: "Error: Could not open shader file: '" + path + "'";
        }
    }
    public void compile(){
        //compile and link shaders
        int vertexID, fragmentID;
        //load and compile vertex shader
        vertexID = glCreateShader(GL_VERTEX_SHADER);
        glShaderSource(vertexID, vertexSrc);
        glCompileShader(vertexID);
        //errors
        int succ = glGetShaderi(vertexID, GL_COMPILE_STATUS);
        if(succ == GL_FALSE){
            int len = glGetShaderi(vertexID, GL_INFO_LOG_LENGTH);
            System.out.println("ERROR: " + path + " '\n\tVertex compilation failed");
            System.out.println(glGetShaderInfoLog(vertexID, len));
            assert false: "";
        }
        //load and compile fragment shader
        fragmentID = glCreateShader(GL_FRAGMENT_SHADER);
        glShaderSource(fragmentID, fragmentSrc);
        glCompileShader(fragmentID);
        //errors
        succ = glGetShaderi(fragmentID, GL_COMPILE_STATUS);
        if(succ == GL_FALSE){
            int len = glGetShaderi(fragmentID, GL_INFO_LOG_LENGTH);
            System.out.println("ERROR: " + path + " '\n\tFragment compilation failed");
            System.out.println(glGetShaderInfoLog(fragmentID, len));
            assert false: "";
        }
        //link shaders and errors
        shaderProgramID = glCreateProgram();
        glAttachShader(shaderProgramID,vertexID);
        glAttachShader(shaderProgramID, fragmentID);
        glLinkProgram(shaderProgramID);
        succ = glGetProgrami(shaderProgramID, GL_LINK_STATUS);
        if(succ == GL_FALSE){
            int len = glGetProgrami(shaderProgramID, GL_INFO_LOG_LENGTH);
            System.out.println("ERROR: " + path + " '\n\tLinking shaders failed");
            System.out.println(glGetProgramInfoLog(shaderProgramID, len));
            assert false: "";
        }

    }
    public void use(){
        glUseProgram(shaderProgramID);
    }
    public void detach(){
        glUseProgram(0);
    }
}
