package Game;

import Game.GameComponents.GameObject;
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


        //ha DirectX akkor térjen vissza egy Singleton DirectX ablak kezelő példányal ami megvílósítja a IDisplayManagert, különben OpenGL.
        if( useD3D)
            return; //TODO
        else
            displayManager = DisplayManagerOpenGL.getInstance();

        //Ez inicializál mindent szóval csak utána tudunk hívogatni opengl es dolgokat
        displayManager.createDisplay();

        ShaderOpenGL shader = new ShaderOpenGL();
        try {
            shader.createUniform("worldMatrix");
            shader.createUniform("textureSampler");
        } catch (Exception e) {
            e.printStackTrace();
        }


        GameObject uto01 = new GameObject(shader,renderer,loader,"kek");
        //GameObject uto02 = new GameObject(shader,renderer,loader,"piros.png",10,10,132,132);




        while (!glfwWindowShouldClose(displayManager.getWindow())){

            displayManager.updateDisplay();
            //Ki rendereljük a modelt
            uto01.renderObject();
            //uto02.renderObject();

        }
        shader.cleanUp();
        loader.cleanUP();
        displayManager.closeDisplay();

    }

}
