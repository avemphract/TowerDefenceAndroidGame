package com.katafrakt.towerdefence.utility;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.katafrakt.towerdefence.ashley.components.TransformComponent;

public class Overlapping {
    private static final String TAG = Overlapping.class.getSimpleName();

    //Rectangle-Rectangle
    public static boolean overlapsRectangle(Vector2 position1, Vector2 dimension1,
                                            Vector2 position2, Vector2 dimension2) {
        return overlapsRectangle(position1.x, position1.y, dimension1.x, dimension1.y,
                position2.x, position2.y, dimension2.x, dimension2.y);
    }

    public static boolean overlapsRectangle(float x1, float y1, float w1, float h1,
                                            float x2, float y2, float w2, float h2) {
        if (x1 + w1 / 2 >= x2 - w2 / 2 && x2 + w2 / 2 >= x1 - w1 / 2) {
            return y1 + h1 / 2 >= y2 - h2 / 2 && y2 + h2 / 2 >= y1 - h1 / 2;
        }
        return false;
    }

    //Rectangle-Point
    public static boolean overlapsRectanglePoint(Vector2 position1, Vector2 dimension1, Vector3 position2) {
        return overlapsRectanglePoint(position1.x, position1.y, dimension1.x, dimension1.y,
                position2.x, position2.y);
    }

    public static boolean overlapsRectanglePoint(float x1, float y1, float w1, float h1, float x2, float y2) {
        if (x1 + w1 / 2 >= x2 && x2 >= x1 - w1 / 2) {
            return y1 + h1 / 2 >= y2 && y2 >= y1 - h1 / 2;
        }
        return false;
    }

    //Rectangle-Line
    public static boolean overlapRectangleLine(Vector2 rectCenter, Vector2 rectDim,
                                               Vector2 beginPos, Vector2 endPos) {
        return overlapRectangleLine(rectCenter.x, rectCenter.y, rectDim.x, rectDim.y,
                beginPos.x, beginPos.y, endPos.x, endPos.y);
    }

    public static boolean overlapRectangleLine(float x1, float y1, float w1, float h1,
                                               float lx1, float ly1, float lx2, float ly2) {
        boolean left = overlapLineLine(x1 - w1 / 2, y1 - h1 / 2, x1 - w1 / 2, y1 + h1 / 2,
                lx1, ly1, lx2, ly2);
        boolean right = overlapLineLine(x1 + w1 / 2, y1 - h1 / 2, x1 + w1 / 2, y1 + h1 / 2,
                lx1, ly1, lx2, ly2);
        boolean top = overlapLineLine(x1 - w1 / 2, y1 - h1 / 2, x1 + w1 / 2, y1 - h1 / 2,
                lx1, ly1, lx2, ly2);
        boolean bottom = overlapLineLine(x1 - w1 / 2, y1 + h1 / 2, x1 + w1 / 2, y1 + h1 / 2,
                lx1, ly1, lx2, ly2);

        // if ANY of the above are true, the line
        // has hit the rectangle
        return left || right || top || bottom;
    }

    public static Array<Vector2> intersectionRectangleLine(Vector2 rectCenter, Vector2 rectDim,
                                                           Vector2 beginPos, Vector2 endPos) {
        return intersectionRectangleLine(rectCenter.x, rectCenter.y, rectDim.x, rectDim.y,
                beginPos.x, beginPos.y, endPos.x, endPos.y);
    }

    public static Array<Vector2> intersectionRectangleLine(float x1, float y1, float w1, float h1,
                                                           float lx1, float ly1, float lx2, float ly2) {
        Vector2 left = intersectionLineLine(x1 - w1 / 2, y1 - h1 / 2, x1 - w1 / 2, y1 + h1 / 2, lx1, ly1, lx2, ly2);
        Vector2 right = intersectionLineLine(x1 + w1 / 2, y1 - h1 / 2, x1 + w1 / 2, y1 + h1 / 2, lx1, ly1, lx2, ly2);
        Vector2 top = intersectionLineLine(x1 - w1 / 2, y1 - h1 / 2, x1 + w1 / 2, y1 - h1 / 2, lx1, ly1, lx2, ly2);
        Vector2 bottom = intersectionLineLine(x1 - w1 / 2, y1 + h1 / 2, x1 + w1 / 2, y1 + h1 / 2, lx1, ly1, lx2, ly2);
        Array<Vector2> intersections = new Array<>();
        if (left != null)
            intersections.add(left);
        if (right != null)
            intersections.add(right);
        if (top != null)
            intersections.add(top);
        if (bottom != null)
            intersections.add(bottom);
        return intersections;
    }

    public static boolean overlapLineLine(Vector2 begin1, Vector2 end1,
                                          Vector2 begin2, Vector2 end2) {
        return overlapLineLine(begin1.x, begin1.y, end1.x, end1.y,
                begin2.x, begin2.y, end2.x, end2.y);
    }

    public static boolean overlapLineLine(float xb1, float yb1, float xe1, float ye1,
                                          float xb2, float yb2, float xe2, float ye2) {
        float uA = ((xe2 - xb2) * (yb1 - yb2) - (ye2 - yb2) * (xb1 - xb2)) / ((ye2 - yb2) * (xe1 - xb1) - (xe2 - xb2) * (ye1 - yb1));
        float uB = ((xe1 - xb1) * (yb1 - yb2) - (ye1 - yb1) * (xb1 - xb2)) / ((ye2 - yb2) * (xe1 - xb1) - (xe2 - xb2) * (ye1 - yb1));
        if (uA >= 0 && uA <= 1 && uB >= 0 && uB <= 1) {
            float intersectionX = xb1 + (uA * (xe1 - xb1));
            float intersectionY = yb1 + (uA * (ye1 - yb1));
            return true;
        }
        return false;
    }

    public static Vector2 intersectionLineLine(Vector2 b1, Vector2 e1,
                                               Vector2 b2, Vector2 e2) {
        return intersectionLineLine(b1.x, b1.y, e1.x, e1.y, b2.x, b2.y, e2.x, e2.y);
    }

    public static Vector2 intersectionLineLine(float xb1, float yb1, float xe1, float ye1,
                                               float xb2, float yb2, float xe2, float ye2) {
        float uA = ((xe2 - xb2) * (yb1 - yb2) - (ye2 - yb2) * (xb1 - xb2)) / ((ye2 - yb2) * (xe1 - xb1) - (xe2 - xb2) * (ye1 - yb1));
        float uB = ((xe1 - xb1) * (yb1 - yb2) - (ye1 - yb1) * (xb1 - xb2)) / ((ye2 - yb2) * (xe1 - xb1) - (xe2 - xb2) * (ye1 - yb1));
        if (uA >= 0 && uA <= 1 && uB >= 0 && uB <= 1) {
            float intersectionX = xb1 + (uA * (xe1 - xb1));
            float intersectionY = yb1 + (uA * (ye1 - yb1));
            return new Vector2(intersectionX, intersectionY);
        }
        return null;
    }

    //Circle-Point
    public static boolean overlapCirclePoint(Vector2 v1, float r1, Vector2 v2) {
        return overlapCirclePoint(v1.x, v1.y, r1, v2.x, v2.y);
    }

    public static boolean overlapCirclePoint(float x1, float y1, float r1,
                                             float x2, float y2) {
        return Vector2.dst2(x1, y1, x2, y2) < Math.pow(r1, 2);
    }

}
