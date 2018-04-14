package core;

import QuickMaths.Vector3D;

public class GameObject {

    private TexturedModel model;
    private Vector3D position;
    private float scale;

    public GameObject(TexturedModel model, Vector3D position, float scale) {
        this.model = model;
        this.position = position;
        this.scale = scale;
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

    public Vector3D getPosition() {
        return position;
    }

    public void setPosition(Vector3D position) {
        this.position = position;
    }

    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }
}
