package Game;

import QuickMaths.Vector3D;
import core.GameObject;
import core.*;
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
        Renderer renderer = new Renderer();

        int[] indicies = {
                0,1,3,
                3,1,2
        };

        float[] textureCoords = {
                0,0,0,1,1,1,1,0
        };

        float[] vertices = {
                -0.5f, 0.5f, 0f,
                -0.5f, -0.5f, 0f,
                0.5f, -0.5f, 0f,
                0.5f, 0.5f, 0f,
        };

        //ha DirectX akkor térjen vissza egy Singleton DirectX ablak kezelő példányal ami megvílósítja a IDisplayManagert, különben OpenGL.
        if( useD3D)
            return; //TODO
        else
            displayManager = DisplayManagerOpenGL.getInstance();

        //Ez inicializál mindent szóval csak utána tudunk hívogatni opengl es dolgokat
        displayManager.createDisplay();

        ShaderOpenGL shader = new ShaderOpenGL();

        RawModel model = loader.loadToVAO(vertices,textureCoords,indicies);

        TexturedModel staticModel = new TexturedModel(model,new TextureModel(loader.loadTexture("kek")));

        Vector3D asd = new Vector3D();
        asd.x = 0;
        asd.y = 0;
        asd.z = 0;
        GameObject uto01 = new GameObject(staticModel,asd,0);

        while (!glfwWindowShouldClose(displayManager.getWindow())){

            displayManager.updateDisplay();
            //Ki rendereljük a modelt
            shader.start();
            renderer.render(uto01,shader);
            shader.stop();
            //uto02.renderObject();

        }
        shader.cleanUp();
        loader.cleanUP();
        displayManager.closeDisplay();

    }

}
