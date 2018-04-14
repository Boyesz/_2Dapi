package core;

import core.coreTypes.ShaderProgram;
import org.joml.Matrix4f;

public class ShaderOpenGL extends ShaderProgram{

    private static final String VERTEX_FILE = ".\\init\\src\\main\\java\\core\\shaders\\vertexShader";
    private static final String FRAGMENT_FILE = ".\\init\\src\\main\\java\\core\\shaders\\fragmentShader";

    private int location_transformationMatrix;

    public ShaderOpenGL() {
        super(VERTEX_FILE, FRAGMENT_FILE);
    }

    @Override
    protected void getAllUniformLocations() {
        location_transformationMatrix = super.getUniformLocation("transformationMatrix");
    }

    @Override
    protected void bindAttributes() {
        super.bindAttribute(0,"position");
        super.bindAttribute(1,"textureCoords");
    }

    public void loadTransformationMatrix(Matrix4f matrix){
        super.loadMatrix(location_transformationMatrix,matrix);
    }
}