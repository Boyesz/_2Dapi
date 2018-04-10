package Game.GameComponents;

import core.*;

public class GameObject {

    ShaderOpenGL shader;
    Renderer renderer;
    Loader loader;
    String textureName;
    TexturedModel texturedModel;
    TextureModel texture;
    RawModel model;

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

    public GameObject(ShaderOpenGL shader, Renderer renderer, Loader loader, String textureName){
        this.shader = shader;
        this.renderer = renderer;
        this.loader = loader;
        this.textureName = textureName;


        //Betöltjük a modelt a VAO-ba
        model = loader.loadToVAO(vertices,textureCoords,indicies);
        texture = new TextureModel(loader.loadTexture(textureName));
        texturedModel = new TexturedModel(model,texture);
        texturedModel.setPosition(0.0f,0.0f,0.0f);
        texturedModel.setScale(1.0f);
    }

    public void renderObject(){
        renderer.render(texturedModel,shader);
    }
}
