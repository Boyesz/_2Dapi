package core;

import core.hitBoxes.HitCircle;
import org.joml.Vector3f;

public class GameObject {

    Vector3f position;
    private TexturedModel model;
    private float scale;

    private float sugar;


    HitCircle hitCircleOriginal;

    HitCircle hitCircleTransformed;

    public GameObject(TexturedModel model, Vector3f position, float scale , float r) {
        this.model = model;
        this.position = position;
        this.scale = scale;

        sugar = r;

        hitCircleOriginal = new HitCircle(position.x,position.y,r);
    }

    public void increasePosition(float dx,float dy,float dz){
        this.position.x += dx;
        this.position.y += dy;
        this.position.z += dz;
    }

    public TexturedModel getModel() {
        return model;
    }

    public void setModel(TexturedModel model) {
        this.model = model;
    }

    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }

    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    public HitCircle getHitCircleOriginal() {
        return hitCircleOriginal;
    }

    public HitCircle getHitCircleTransformed() {
        hitCircleTransformed = new HitCircle(position.x,position.y,sugar);
        return hitCircleTransformed;
    }
}
