package QuickMaths;

        import org.joml.Matrix4f;
        import org.joml.Vector3f;

public class QuickMaths {
    public static Matrix4f createTransformationMatrix(Vector3f translation,
                                                      float scale)
    {
        Matrix4f matrix = new Matrix4f();
        matrix.identity();
        matrix.setTranslation(translation.x, translation.y, translation.z);
        matrix.scale(scale,scale,scale);
        return matrix;
    }
}
