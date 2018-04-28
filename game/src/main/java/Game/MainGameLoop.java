package Game;

import core.GameObject;
import core.*;
import org.joml.Vector3f;
import renderEngine.DisplayManagerOpenGL;
import renderEngine.rendererTypes.IDisplayManager;

import static org.lwjgl.glfw.GLFW.*;

public class MainGameLoop {
    //Ablak kezelő megvalósítása.
    static IDisplayManager displayManager;
    static GameObject kek;
    static GameObject piros;
    static GameObject palyaObj;
    static GameObject labdaObj;

    static float mouseSensiX = 0.003f;
    static float mouseSensiY = -0.005f;

    static double[] mousePos =  new double[2];
    static double[] mousePosCurrent;
    static double[] mousePosOld = { 0.0, 0.0};


    public static void main(String[] args){

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

        RawModel kekModel = loader.loadToVAO(vertices,textureCoords,indicies);
        RawModel pirosModel = loader.loadToVAO(vertices,textureCoords,indicies);
        RawModel palyaModel = loader.loadToVAO(vertices,textureCoords,indicies);
        RawModel labdaModel = loader.loadToVAO(vertices,textureCoords,indicies);
        TexturedModel kekUto = new TexturedModel(kekModel,new TextureModel(loader.loadTexture("kek")));
        TexturedModel pirosUto = new TexturedModel(pirosModel,new TextureModel(loader.loadTexture("piros")));
        TexturedModel palya = new TexturedModel(palyaModel,new TextureModel(loader.loadTexture("palya")));
        TexturedModel labda = new TexturedModel(labdaModel,new TextureModel(loader.loadTexture("korong")));

        kek = new GameObject(kekUto,new Vector3f(0.5f,0,0),0.15f , 0.08f);
        piros = new GameObject(pirosUto,new Vector3f(-0.5f,0,0),0.15f, 0.08f);
        palyaObj = new GameObject(palya,new Vector3f(0,0,0),2f, 0.0f);
        labdaObj = new GameObject(labda,new Vector3f(0.0f,0,0),0.1f, 0.03f);

        while (!glfwWindowShouldClose(displayManager.getWindow())){
            displayManager.updateDisplay();
            gameLogic();
            //Ki rendereljük a modelt
            shader.start();
            renderer.render(palyaObj,shader);
            renderer.render(piros,shader);
            renderer.render(kek,shader);
            renderer.render(labdaObj,shader);
            shader.stop();
        }
        shader.cleanUp();
        loader.cleanUP();
        displayManager.closeDisplay();

    }

    static void gameLogic(){
        inputHandler();
        checkWalls();
        makeGamePhysicsByHitbox();
    }

    static void makeGamePhysicsByHitbox(){
        //Ütközik a labda a piros playerrel
        if(piros.getHitCircleTransformed().checkOverlapping(labdaObj.getHitCircleTransformed()))
            System.out.println("Hit");
        //Ütközik a labda a kek playerrel
        if(kek.getHitCircleTransformed().checkOverlapping(labdaObj.getHitCircleTransformed()));
            //TODO

    }

    static void checkWalls(){
        //Piros Player

        //check mid line.
        if(piros.getPosition().x >= palyaObj.getPosition().x -0.05f)
            piros.setPosition(new Vector3f(palyaObj.getPosition().x -0.05f,piros.getPosition().y,0.0f));
        //check left side.
        if(piros.getPosition().x <= palyaObj.getPosition().x - 0.9f)
            piros.setPosition(new Vector3f( palyaObj.getPosition().x - 0.9f ,piros.getPosition().y,0.0f));
        //check up side.
        if(piros.getPosition().y >= palyaObj.getPosition().y + 0.87f)
            piros.setPosition(new Vector3f( piros.getPosition().x,palyaObj.getPosition().y +0.87f,0.0f));
        //check down side.
        if(piros.getPosition().y <= palyaObj.getPosition().y - 0.87f)
            piros.setPosition(new Vector3f( piros.getPosition().x,palyaObj.getPosition().y - 0.87f,0.0f));

        //Kek Player

        //check mid line.
        if(kek.getPosition().x <= palyaObj.getPosition().x +0.05f)
            kek.setPosition(new Vector3f(palyaObj.getPosition().x + 0.05f,kek.getPosition().y,0.0f));
        //check left side.
        if(kek.getPosition().x >= palyaObj.getPosition().x + 0.9f)
            kek.setPosition(new Vector3f( palyaObj.getPosition().x + 0.9f ,kek.getPosition().y,0.0f));
        //check up side.
        if(kek.getPosition().y <= palyaObj.getPosition().y - 0.87f)
            kek.setPosition(new Vector3f( kek.getPosition().x,palyaObj.getPosition().y -0.87f,0.0f));
        //check down side.
        if(kek.getPosition().y >= palyaObj.getPosition().y + 0.87f)
            kek.setPosition(new Vector3f( kek.getPosition().x,palyaObj.getPosition().y + 0.87f,0.0f));

        //Labda Object

        //check right side.
        if(labdaObj.getPosition().x >= palyaObj.getPosition().x + 0.9f)
            //TODO
        //check up side.
        if(labdaObj.getPosition().y <= palyaObj.getPosition().y - 0.87f)
            //TODO
        //check down side.
        if(labdaObj.getPosition().y >= palyaObj.getPosition().y + 0.87f)
            //TODO
        //check left side.
        if(labdaObj.getPosition().x >= palyaObj.getPosition().x + 0.9f){
            //TODO
        }

    }

    static void inputHandler(){
            mousePosCurrent = displayManager.getCursorPos();
            mousePos[0] = mousePosCurrent[0] - mousePosOld[0];
            mousePos[1] = mousePosCurrent[1] - mousePosOld[1];
            Vector3f newPlayerPos = new Vector3f( piros.getPosition().x + ((float)mousePos[0]*mouseSensiX) ,piros.getPosition().y + ((float)mousePos[1]*mouseSensiY),0.0f);
            piros.setPosition(newPlayerPos);
            mousePosOld = mousePosCurrent;
    }

}
