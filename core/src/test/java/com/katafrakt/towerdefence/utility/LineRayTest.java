package com.katafrakt.towerdefence.utility;

import static org.junit.jupiter.api.Assertions.*;

import com.badlogic.gdx.ai.utils.Collision;
import com.badlogic.gdx.math.Vector2;

import org.junit.jupiter.api.Test;

class LineRayTest {

    @Test
    void intersectionPoint() {
        LineRay l1 = new LineRay(new Vector2(1, -1), new Vector2(0, 1));
        LineRay l2 = new LineRay(new Vector2(-1, 0), new Vector2(1, 0));
        Collision<Vector2> collision=new Collision<>(new Vector2(),new Vector2());
        assertEquals(new Vector2(0.5f, 0), l1.intersectionPoint(collision,l2).point);
        System.out.println(collision.normal);

    }
}