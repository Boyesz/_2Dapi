package Game;

import core.GameObject;
import core.*;
import org.joml.Vector3f;
import renderEngine.DisplayManagerOpenGL;
import renderEngine.rendererTypes.IDisplayManager;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.xml.transform.Source;
import java.io.File;

import static org.lwjgl.glfw.GLFW.*;

public class MainGameLoop {
    //Ablak kezelő megvalósítása.
    static IDisplayManager displayManager;
    static GameObject kek;
    static GameObject piros;
    static GameObject palyaObj;
    static GameObject labdaObj;

    //ezt bele égettük more DONT TOUCH
    static float mouseSensiX = 0.003f;
    static float mouseSensiY = -0.005f;

    static double[] mousePos =  new double[2];
    static double[] mousePosCurrent;
    static double[] mousePosOld = { 0.0, 0.0};
    private static float labdaVelocityX;
    private static float labdaVelocityY;
    private static float mainSpeed = 0.01f;

    /*static SoundSource  mainMusic;
    static SoundSource hit1;
    static SoundSource hit2;*/

    static Clip hit1;
    static Clip hit2;

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
        labdaObj = new GameObject(labda,new Vector3f(0,0,0),0.1f, 0.03f);

        kek.setScore(0);
        piros.setScore(0);

        //SoundSource mainMusic = new SoundSource("./game/src/main/java/Game/mainMusic.mp3");
        /*Media hit = new Media(new File("./game/src/main/java/Game/mainMusic.mp3").toURI().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(hit);
        mediaPlayer.play();*/

        String executionPath = System.getProperty("user.dir");
        System.out.print("Executing at =>"+executionPath);

        /*mainMusic = new SoundSource();
        mainMusic.LoadFile("game/src/main/java/Game/mainMusic.wav");*/
        /*hit1 = new SoundSource();
        hit1.LoadFile("game/src/main/java/Game/hit1.wav");*/
        /*hit2 = new SoundSource();
        hit2.LoadFile("game/src/main/java/Game/hit2.wav");*/

        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("game/src/main/java/Game/mainMusic.wav"));
            AudioInputStream hit1AudioStream = AudioSystem.getAudioInputStream(new File("game/src/main/java/Game/hit1.wav"));
            AudioInputStream hit2AudioStream = AudioSystem.getAudioInputStream(new File("game/src/main/java/Game/hit2.wav"));
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
             hit1 = AudioSystem.getClip();
            hit1.open(hit1AudioStream);
             hit2 = AudioSystem.getClip();
            hit2.open(hit2AudioStream );

            clip.start();
            // If you want the sound to loop infinitely, then put: clip.loop(Clip.LOOP_CONTINUOUSLY);
            // If you want to stop the sound, then use clip.stop();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        //mainMusic.Play();


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
        float oldPirosMoveX = piros.getPosition().x;
        float oldPirosMoveY = piros.getPosition().y;
        inputHandler();
        checkGoal();
        checkWalls();
        makeGamePhysicsByHitbox();
        mainSpeed =(float)Math.sqrt(Math.pow(oldPirosMoveX - piros.getPosition().x,2) + Math.pow(oldPirosMoveY - piros.getPosition().y,2));
    }

    static void makeGamePhysicsByHitbox(){

        if(mainSpeed > 0.15)
            mainSpeed = 0.15f;


        //if(labdaVelocityX == 0 && labdaVelocityY == 0)
        labdaObj.setPosition(new Vector3f(labdaObj.getPosition().x + labdaVelocityX,labdaObj.getPosition().y + labdaVelocityY,0.0f));
        /*else
            labdaObj.setPosition(new Vector3f(labdaObj.getPosition().x + labdaVelocityX -0.01f,labdaObj.getPosition().y + labdaVelocityY -0.01f,0.0f));

*/
        //Ütközik a labda a piros playerrel
        if(piros.getHitCircleTransformed().checkOverlapping(labdaObj.getHitCircleTransformed()))
        {
            hit2.stop();
            hit2.start();

            double alpha = Math.abs(labdaObj.getPosition().y - piros.getPosition().y) / Math.sqrt(Math.abs((labdaObj.getPosition().x-piros.getPosition().x)*(labdaObj.getPosition().x-piros.getPosition().x)+(labdaObj.getPosition().y-piros.getPosition().y)*(labdaObj.getPosition().y-piros.getPosition().y)));
            double gammaSugarzas =  Math.toRadians(90 - Math.toDegrees(alpha));

            //Jobb lent*/
            if(labdaObj.getPosition().x >= piros.getPosition().x && labdaObj.getPosition().y <= piros.getPosition().y)
            {
                labdaVelocityX = mainSpeed * (float) gammaSugarzas;
                labdaVelocityY = -mainSpeed * (float)alpha;
            }
            //Bal fent
            if(labdaObj.getPosition().x <= piros.getPosition().x && labdaObj.getPosition().y >= piros.getPosition().y){
                labdaVelocityX = -mainSpeed * (float) gammaSugarzas;
                labdaVelocityY = mainSpeed * (float)alpha;
            }
            //Bal lent
            if(labdaObj.getPosition().x <= piros.getPosition().x && labdaObj.getPosition().y <= piros.getPosition().y){
                labdaVelocityX = -mainSpeed * (float) gammaSugarzas;
                labdaVelocityY = -mainSpeed * (float)alpha;
            }
            //Jobb fent
            if(labdaObj.getPosition().x >= piros.getPosition().x && labdaObj.getPosition().y >= piros.getPosition().y){
                labdaVelocityX = mainSpeed * (float) gammaSugarzas;
                labdaVelocityY = mainSpeed * (float)alpha;
            }

        }
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
        //check right side.
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
           labdaVelocityX *= -1;
        //check up side.
        if(labdaObj.getPosition().y <= palyaObj.getPosition().y - 0.87f)
            labdaVelocityY *= -1;
        //check down side.
        if(labdaObj.getPosition().y >= palyaObj.getPosition().y + 0.87f)
            labdaVelocityY *= -1;
        //check left side.
        if(labdaObj.getPosition().x <= palyaObj.getPosition().x - 0.9f){
            labdaVelocityX *= -1;
        }

    }

    static void checkGoal(){
        if(labdaObj.getPosition().x <= palyaObj.getPosition().x -0.89f && labdaObj.getPosition().y <= palyaObj.getPosition().y + 0.38f && labdaObj.getPosition().y >= palyaObj.getPosition().y - 0.38f)
        {
            //hit2.Play();
            hit1.stop();
            hit1.start();
            kek.setScore(kek.getScore() + 1);
            resetBall();
            System.out.println("Kes Pontszama: " + kek.getScore());
        }

        if(labdaObj.getPosition().x >= palyaObj.getPosition().x + 0.89f && labdaObj.getPosition().y <= palyaObj.getPosition().y + 0.38f && labdaObj.getPosition().y >= palyaObj.getPosition().y - 0.38f)
        {
            //hit2.Play();
            hit1.stop();
            hit1.start();
            piros.setScore(piros.getScore() + 1);
            resetBall();
            System.out.println("Piros Pontszama: " + piros.getScore());
        }

    }

    private static void resetBall() {
        labdaVelocityX = 0.0f;
        labdaVelocityY = 0.0f;
        labdaObj.setPosition(new Vector3f(0.0f,0.0f,0.0f));
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
