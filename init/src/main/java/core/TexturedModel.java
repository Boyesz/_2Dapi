package core;

public class TexturedModel {

    private RawModel rawModel;
    private TextureModel textureModel;

    public TexturedModel(RawModel rawModel, TextureModel textureModel) {
        this.rawModel = rawModel;
        this.textureModel = textureModel;
    }

    public RawModel getRawModel() {
        return rawModel;
    }

    public TextureModel getTextureModel() {
        return textureModel;
    }
}
