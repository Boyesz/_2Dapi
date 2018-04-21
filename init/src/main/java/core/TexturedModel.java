package core;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class TexturedModel {


    // Matrix for transformation
    private Matrix4f mTranformation = new Matrix4f();

    // Texture position
    private Vector3f position = new Vector3f();

    private int width;
    private int height;
    private Matrix4f transformationMatrix = new Matrix4f();
    private float scale;

    private RawModel rawModel;
    private TextureModel textureModel;

    private HitBox mAABBOriginal;
    private HitBox mAABBTransformed;

    public TexturedModel(RawModel rawModel, TextureModel textureModel) {
        this.rawModel = rawModel;
        this.textureModel = textureModel;
        Vector2f minPoint = new Vector2f(0.0F, 0.0F);
        Vector2f maxPoint = new Vector2f((float)this.width, (float)this.height);

        this.mAABBOriginal = new HitBox(minPoint, maxPoint);
        this.mAABBTransformed = new HitBox(minPoint, maxPoint);
    }

    public RawModel getRawModel() {
        return rawModel;
    }

    public TextureModel getTextureModel() {
        return textureModel;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void setScale(float scale)
    {
        transformationMatrix.scale(scale, scale, scale);
    }
    public float getScale()
    {
        return scale;
    }

    public Vector3f getPosition() {
        return position;
    }




    public void setPosition(float x, float y, float z) {
        this.position.x = x;
        this.position.y = y;
        this.position.z = z;
    }

    public Matrix4f getWorldMatrix() {

        // First identity
        mTranformation.identity();

        // Translate
        mTranformation.translate(position);

        mTranformation.scale(scale, scale, 1);

        return mTranformation;
    }

    public HitBox getTransformedBoundingBox() {

        mAABBTransformed.SetPoints(mAABBOriginal.GetMinPoint(), mAABBOriginal.GetMaxPoint());
        //this.mAABBTransformed.transformByScale(new Vector2f(this.scale, this.scale));
        mAABBTransformed.transformByScale(new Vector2f(scale,scale));
        mAABBTransformed.transformByTranslate(new Vector2f(position.x, position.y));
        return this.mAABBTransformed;
    }

    public HitBox getmAABBOriginal()
    {
        return mAABBOriginal;
    }

    public HitBox getmAABBTransformed()
    {
        return mAABBTransformed;
    }

}
