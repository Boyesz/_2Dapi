package Game;

import core.Loader;
import core.RawModel;
import core.ShaderOpenGL;
import core.VertexRenderer;
import renderEngine.DisplayManagerOpenGL;
import renderEngine.rendererTypes.IDisplayManager;

import static org.lwjgl.glfw.GLFW.*;

public class MainGameLoop {

    public static void main(String[] args){
        //Ablak kezelő megvalósítása.
        IDisplayManager displayManager;
        //OpenGL vagy DirectX.
        boolean useD3D = false;

        Loader loader = new Loader();
        VertexRenderer renderer = new VertexRenderer();

        float[] vertices = {
                -0.5f, 0.5f, 0f,
                -0.5f, -0.5f, 0f,
                0.5f, -0.5f, 0f,
                0.5f, -0.5f, 0f,
                0.5f, 0.5f, 0f,
                -0.5f, 0.5f, 0f
        };
        //ha DirectX akkor térjen vissza egy Singleton DirectX ablak kezelő példányal ami megvílósítja a IDisplayManagert, különben OpenGL.
        if( useD3D)
            return; //TODO
        else
            displayManager = DisplayManagerOpenGL.getInstance();

        //Ez inicializál mindent szóval csak utána tudunk hívogatni opengl es dolgokat
        displayManager.createDisplay();
        //Betöltjük a modelt a VAO-ba
        RawModel model = loader.loadToVAO(vertices);
        ShaderOpenGL shader = new ShaderOpenGL();
        while (!glfwWindowShouldClose(displayManager.getWindow())){

            displayManager.updateDisplay();
            //Ki rendereljük a modelt
            shader.start();
            renderer.render(model);
            shader.stop();

        }
        shader.cleanUp();
        loader.cleanUP();
        displayManager.closeDisplay();

    }

}
