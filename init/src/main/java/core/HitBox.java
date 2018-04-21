package core;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class HitBox {

    private final int AABB_POINTS_2D = 4;

    /// Minimum point of the Bounding Box
    private Vector2f minpoint;

    /// Maximum point of the Bounding Box
    private Vector2f maxpoint;

    /// Bounding box points
    private Vector2f[] bbPoints;

    /// Half width of the Box
    private float boxHalfWidth;

    /// Half height of the Box
    private float boxHalfHeight;

    /// Transformation matrix
    private Matrix4f m_mTransformationMatrix;

    /// BB is usable or not
    private boolean mEnabled;

    public HitBox (){
        boxHalfWidth = 0;
        boxHalfHeight = 0;

        mEnabled = true;

        minpoint = new Vector2f();
        maxpoint = new Vector2f();

        bbPoints = new Vector2f[AABB_POINTS_2D];
        for (int i = 0; i < bbPoints.length; i++){
            bbPoints[i] = new Vector2f();
        }
        m_mTransformationMatrix = new Matrix4f();
    }

    ///
    /// Constructor
    ///
    public HitBox(Vector2f min, Vector2f max) {

        boxHalfWidth = 0;
        boxHalfHeight = 0;

        bbPoints = new Vector2f[AABB_POINTS_2D];
        for (int i = 0; i < bbPoints.length; i++) {
            bbPoints[i] = new Vector2f();
        }

        m_mTransformationMatrix = new Matrix4f();

        mEnabled = true;

        minpoint = new Vector2f(min.x, min.y);
        maxpoint = new Vector2f(max.x, max.y);

        setUpBBPoints();
        searchMinMax();

        boxHalfWidth = (maxpoint.x - minpoint.x) / 2.0f;
        boxHalfHeight = (maxpoint.y - minpoint.y) / 2.0f;
    }
    ///
    /// Set AABB points and calculates center
    ///
    public void SetPoints(Vector2f min, Vector2f max) {
        minpoint.set(min.x, min.y);
        maxpoint.set(max.x, max.y);

        setUpBBPoints();
        searchMinMax();

        boxHalfWidth = (maxpoint.x - minpoint.x) / 2.0f;
        boxHalfHeight = (maxpoint.y - minpoint.y) / 2.0f;
    }

    ///
    /// Set up the BB points from the min and max point
    ///
    public void setUpBBPoints() {
        // 1. pont
        bbPoints[0].x = minpoint.x;
        bbPoints[0].y = minpoint.y;

        // 2. pont
        bbPoints[1].x = maxpoint.x;
        bbPoints[1].y = minpoint.y;

        // 3. pont
        bbPoints[2].x = maxpoint.x;
        bbPoints[2].y = maxpoint.y;

        // 4. pont
        bbPoints[3].x = minpoint.x;
        bbPoints[3].y = maxpoint.y;
    }
    ///
    /// Search min and max point of the bounding box
    ///
    public void searchMinMax() {
        Vector2f min = new Vector2f(bbPoints[0].x, bbPoints[0].y);
        Vector2f max = new Vector2f(bbPoints[0].x, bbPoints[0].y);

        // loop each 4 points
        for (int i = 0; i < AABB_POINTS_2D; i++) {
            if (bbPoints[i].x < min.x) {
                min.x = bbPoints[i].x;
            }
            if (bbPoints[i].y < min.y) {
                min.y = bbPoints[i].y;
            }

            if (bbPoints[i].x > max.x) {
                max.x = bbPoints[i].x;
            }
            if (bbPoints[i].y > max.y) {
                max.y = bbPoints[i].y;
            }
        }

        minpoint.x = min.x;
        minpoint.y = min.y;

        maxpoint.x = max.x;
        maxpoint.y = max.y;
    }

    ///
    /// Scale transformation on BB
    ///
    public void transformByScale(Vector2f scale_vector) {

        // load scale vector into the matrix
        m_mTransformationMatrix.scale(new Vector3f(scale_vector.x, scale_vector.y, 0.0f));

        // loop all the 8 points
        for (int i = 0; i < AABB_POINTS_2D; i++) {

            // transform the points
            Vector3f point = new Vector3f(bbPoints[i].x, bbPoints[i].y, 0.0f);
            m_mTransformationMatrix.setTranslation(point);
            bbPoints[i].set(point.x, point.y);
        }

        // Search min and max points
        searchMinMax();

        // Setup AABB box
        setUpBBPoints();

    }
    ///
    /// Translate transformation on BB
    ///
    public void transformByTranslate(Vector2f translate_vector) {
        // load translate vector into the matrix
        m_mTransformationMatrix.identity();
        m_mTransformationMatrix.translate(new Vector3f(translate_vector.x, translate_vector.y, 0.0f));

        // loop all the 8 points
        for (int i = 0; i < AABB_POINTS_2D; i++) {
            // transform the points
            Vector3f point = new Vector3f(bbPoints[i].x, bbPoints[i].y, 0.0f);
            m_mTransformationMatrix.setTranslation(point);
            bbPoints[i].set(point.x, point.y);
        }

        // Search min and max points
        searchMinMax();

        // setup AABB box
        setUpBBPoints();
    }


    ///
    /// Get AABB max point
    ///
    public Vector2f GetMaxPoint() {
        return maxpoint;
    }

    ///
    /// Get AABB min point
    ///
    public Vector2f GetMinPoint() {
        return minpoint;
    }

    ///
    /// Check two BB overlapping
    ///
    public boolean CheckOverlapping(HitBox box) {

        // Checking x axis overlapping
        if (maxpoint.x < box.GetMinPoint().x || minpoint.x > box.GetMaxPoint().x) {
            // No collision
            return false;
        }

        // Checking y axis overlapping
        if (maxpoint.y < box.GetMinPoint().y || minpoint.y > box.GetMaxPoint().y) {
            // No collision
            return false;
        }

        return true;
    }

    ///
    /// Get all the points of the BB
    ///
    public Vector2f[] GetBBPoints() {
        return bbPoints;
    }

    ///
    /// Set Box status
    ///
    public void SetEnabled(boolean flag) {
        mEnabled = flag;
    }

    ///
    /// Get Box status
    ///
    public boolean isEnabled() {
        return mEnabled;
    }




}
