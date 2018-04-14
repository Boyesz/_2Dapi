package QuickMaths;

public class QuickMaths {
    public static MATRIX4X4 createTransformationMatrix(Vector3D translation, float rx, float ry, float rz, float scale)
    {
        MATRIX4X4 matrix = new MATRIX4X4();
        matrix.identity();
        matrix.translate(translation);
        matrix.scale(scale);
        return null;
    }
}
