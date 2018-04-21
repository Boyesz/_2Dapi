package core;

import QuickMaths.QuickMaths;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

public class Renderer {

    public void render(GameObject gameObject,ShaderOpenGL shaderOpenGL){
        TexturedModel model = gameObject.getModel();
        RawModel rawModel = model.getRawModel();
        GL30.glBindVertexArray(rawModel.getVaoID());
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);
        Matrix4f debug = QuickMaths.createTransformationMatrix(gameObject.getPosition(),gameObject.getScale());
        shaderOpenGL.loadTransformationMatrix(debug);
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D,model.getTextureModel().getID());
        GL11.glDrawElements(GL11.GL_TRIANGLES,rawModel.getVertexCount(),GL11.GL_UNSIGNED_INT,0);
        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);
        GL30.glBindVertexArray(0);
    }

}
