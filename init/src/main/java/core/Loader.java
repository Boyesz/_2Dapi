package core;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

public class Loader {

    private List<Integer> vaos = new ArrayList<Integer>();
    private List<Integer> vbos = new ArrayList<Integer>();
    //Vissza add egy modelt.
    public RawModel loadToVAO(float[] positions){
        //Hozzá rendeljük a modelt egy új Vertex Array Object - hez
        int vaoID = createVAO();
        //Az adatokat eltároljuk egy VBO ban
        storeDataInAttributeList(0,positions);
        unbindVAO();
        //Vissza térünk a model adataival
        return new RawModel(vaoID,positions.length/3);
    }
    //Elkészítjük a vaot
    private int createVAO(){
        //Kérünk egy új Buffer létrehozását aminek megkapjuk az ID-jat.
        int vaoID = GL30.glGenVertexArrays();
        //Hozzá adjuk a listához, hogy tudjuk később kezelni
        vaos.add(vaoID);
        //Mindig csak egy Buffer lehet Bindolva, azaz csak egyel tudunk műveleteket elvégezni
        GL30.glBindVertexArray(vaoID);
        return vaoID;
    }


    public void cleanUP(){
        for(int vao:vaos){
            GL30.glDeleteVertexArrays(vao);
        }
        for(int vbo:vbos){
            GL15.glDeleteBuffers(vbo);
        }
    }

    private void storeDataInAttributeList(int attributeNumber, float[] data){
        int vboID = GL15.glGenBuffers();
        vbos.add(vboID);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
        FloatBuffer buffer = storeDataInFloatBuffer(data);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
        GL20.glVertexAttribPointer(attributeNumber, 3, GL11.GL_FLOAT, false, 0, 0);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
    }

    private void unbindVAO(){
        GL30.glBindVertexArray(0);
    }

    private FloatBuffer storeDataInFloatBuffer(float[] data){
        FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
        buffer.put(data);
        buffer.flip();
        return buffer;
    }
}
