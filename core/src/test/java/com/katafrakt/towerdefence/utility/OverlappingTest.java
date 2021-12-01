package com.katafrakt.towerdefence.utility;

import static org.junit.jupiter.api.Assertions.*;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import org.junit.jupiter.api.Test;

class OverlappingTest {

    @Test
    void overlapsRectangle() {
        Vector2 pos1 = new Vector2(0, 0);
        Vector2 dim1 = new Vector2(1, 1);

        Vector2 pos2 = new Vector2(0, 1);
        Vector2 dim2 = new Vector2(1, 1);

        assertTrue(Overlapping.overlapsRectangle(pos1, dim1, pos2, dim2));

    }

    @Test
    void intersectionLineLine() {
        Vector2 b1 = new Vector2(1,-2);
        Vector2 e1 = new Vector2(1,+1);

        Vector2 b2 = new Vector2(-1,0);
        Vector2 e2 = new Vector2(+1,0);

        Vector2 result=new Vector2(0,0);
        System.out.println(Overlapping.intersectionLineLine(b1,e1,b2,e2));
        assertEquals(Overlapping.intersectionLineLine(b1,e1,b2,e2),result);
    }

    @Test
    void intersectionRectangleLine() {
        Rectangle rectangle=new Rectangle(0,0,1,1);

        Vector2 b=new Vector2(-1,-1);
        Vector2 e=new Vector2(1,1);

        System.out.println(Overlapping.intersectionRectangleLine(rectangle.x,rectangle.y,rectangle.width,rectangle.height,
                b.x, b.y, e.x, e.y));
        System.out.println(Overlapping.overlapRectangleLine(rectangle.x,rectangle.y,rectangle.width,rectangle.height,
                b.x, b.y, e.x, e.y));
    }
}