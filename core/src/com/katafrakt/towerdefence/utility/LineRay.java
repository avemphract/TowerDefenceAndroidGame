package com.katafrakt.towerdefence.utility;

import com.badlogic.gdx.ai.utils.Collision;
import com.badlogic.gdx.ai.utils.Ray;
import com.badlogic.gdx.math.Vector2;

public class LineRay extends Ray<Vector2> {
    public float a;
    public float b;
    public float c;

    public LineRay(Vector2 start, Vector2 end) {
        super(start, end);
        a = end.y - start.y;
        b = start.x - end.x;
        c = -end.x * a - end.y * b;
        //a * x + b * y + k     = 0
    }

    public LineRay set(Ray<Vector2> ray) {
        start.set(ray.start);
        end.set(ray.end);
        a = end.y - start.y;
        b = start.x - end.x;
        c = -end.x * a - end.y * b;
        return this;
    }

    public Collision<Vector2> intersectionPoint(Collision<Vector2> collision, LineRay ray) {
        float x = (b * ray.c - ray.b * c) / (a * ray.b - ray.a * b);
        float y = (ray.a * c - a * ray.c) / (a * ray.b - ray.a * b);
        collision.point.set(x, y);
        collision.normal.set(a, b).scl(Vector2.dot(ray.start.x - x, ray.start.y - y, a, b) / (Vector2.len2(a, b))).setLength(2);
        return collision;
    }
}
