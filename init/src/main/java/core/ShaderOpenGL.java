package core;

import core.coreTypes.ShaderProgram;

public class ShaderOpenGL extends ShaderProgram{

    private static final String VERTEX_FILE = ".\\init\\src\\main\\java\\core\\shaders\\vertexShader";
    private static final String FRAGMENT_FILE = ".\\init\\src\\main\\java\\core\\shaders\\fragmentShader";

    public ShaderOpenGL() {
        super(VERTEX_FILE, FRAGMENT_FILE);
    }

    @Override
    protected void bindAttributes() {
        super.bindAttribute(0,"position");
    }
}
