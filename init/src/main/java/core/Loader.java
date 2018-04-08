package core;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.stb.STBImage;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

public class Loader {

    private List<Integer> vaos = new ArrayList<Integer>();
    private List<Integer> vbos = new ArrayList<Integer>();
    private List<Integer> textures = new ArrayList<Integer>();
    //Vissza add egy modelt.
    public RawModel loadToVAO(float[] positions,float[] texutreCoordinates,int[] indices){
        //Hozzá rendeljük a modelt egy új Vertex Array Object - hez
        int vaoID = createVAO();
        bindIndicesBuffer(indices);
        //Az adatokat eltároljuk egy VBO ban
        storeDataInAttributeList(0,3,positions);
        storeDataInAttributeList(1,2,texutreCoordinates);
        unbindVAO();
        //Vissza térünk a model adataival
        return new RawModel(vaoID,indices.length);
    }

    public int loadTexture(String fileName){
        Texture texture = new Texture("sprite");;
        int textureID = texture.getId();
        textures.add(textureID);
        return textureID;
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
        for(int texture:textures){
            GL11.glDeleteTextures(texture);
        }
    }

    private void bindIndicesBuffer(int[] indices) {
        int vboId = GL15.glGenBuffers();
        vbos.add(vboId);
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboId);
        IntBuffer buffer = storeDataInIntBuffer(indices);
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
    }

    private void storeDataInAttributeList(int attributeNumber, int coordinateSize ,float[] data){
        int vboID = GL15.glGenBuffers();
        vbos.add(vboID);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
        FloatBuffer buffer = storeDataInFloatBuffer(data);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
        GL20.glVertexAttribPointer(attributeNumber, coordinateSize, GL11.GL_FLOAT, false, 0, 0);
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

    private IntBuffer storeDataInIntBuffer(int[] data) {
        IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
        buffer.put(data);
        buffer.flip();
        return buffer;
    }

}
