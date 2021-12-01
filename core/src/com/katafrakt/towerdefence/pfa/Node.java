package com.katafrakt.towerdefence.pfa;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.pfa.Connection;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectSet;

public class Node extends Vector2 {
    private static final String TAG = Node.class.getSimpleName();

    public enum Type {
        PLAIN_TILE(Color.LIME, 0),
        ENEMY_PATH(Color.GOLDENROD, 1),
        OBSTACLE_TILE(Color.BROWN, 2),
        SEA_TILE(Color.NAVY, 4);

        public static Type getByIndex(int index) {
            for (Type type : Type.values()) {
                if (type.index == index) return type;
            }
            throw new RuntimeException("Invalid index: " + index);
        }

        public final Color color;
        public final int index;

        Type(Color color, int index) {
            this.color = color;
            this.index = index;
        }
    }

    public static final float ROOT3DIV2 = MathUtils.sinDeg(60);
    public static final int LENGTH = 8;
    public static final float WIDTH = LENGTH * ROOT3DIV2 * 2;
    public static final float HEIGHT = LENGTH * 2;
    public static final float TEMPLATE_HEIGHT = LENGTH * 3;

    public final int index;

    public final Array<Connection<Node>> allyConnection = new Array<>();
    public final Array<Connection<Node>> enemyConnection = new Array<>();
    public final float[] vertices;
    public final int tableX;
    public final int tableY;
    public final Vector2 posVector;

    public Type type;

    public Entity building;
    public final ObjectSet<Entity> enemyEntities = new ObjectSet<>();

    public Node(int tableX, int tableY, int typeIndex) {
        index = Indexer.getIndex();
        this.tableX = tableX;
        this.tableY = tableY;
        type = Type.getByIndex(typeIndex);

        x = (LENGTH * (float) Math.pow(3, 0.5f) * (tableX + tableY / 2f));
        y = (LENGTH * (3 / 2f) * tableY);

        posVector = new Vector2(x, y);

        vertices = new float[]{
                x, (y + LENGTH),
                (x + LENGTH * ROOT3DIV2), (y + LENGTH * 0.5f),
                (x + LENGTH * ROOT3DIV2), (y - LENGTH * 0.5f),
                x, (y - LENGTH),
                (x - LENGTH * ROOT3DIV2), (y - LENGTH * 0.5f),
                (x - LENGTH * ROOT3DIV2), (y + LENGTH * 0.5f),
        };


    }

    public boolean isInside(Vector3 vector3) {
        return isInside(vector3.x, vector3.y);
    }

    public boolean isInside(Vector2 vector2) {
        return isInside(vector2.x, vector2.y);
    }

    public boolean isInside(float x, float y) {
        if (posVector.y - HEIGHT / 2 <= y && y <= posVector.y - HEIGHT / 4) {
            if (posVector.x - WIDTH / 2 <= x && x <= posVector.x + WIDTH / 2) {
                float lengthX = Math.abs(posVector.x - x);
                float lengthY = Math.abs(posVector.y - LENGTH * 0.5f - y) * ROOT3DIV2 * 2;
                return lengthX + lengthY <= LENGTH * ROOT3DIV2;
            }
            return false;
        } else if (posVector.y - HEIGHT / 4 <= y && y <= posVector.y + HEIGHT / 4) {
            return (posVector.x - WIDTH / 2 <= x && x <= posVector.x + WIDTH / 2);
        } else if (posVector.y + HEIGHT / 4 <= y && y <= posVector.y + HEIGHT / 2) {
            float lengthX = Math.abs(posVector.x - x);
            float lengthY = Math.abs(posVector.y + LENGTH * 0.5f - y) * ROOT3DIV2 * 2;
            return lengthX + lengthY <= LENGTH * ROOT3DIV2;
        }
        return false;
    }

    public int nodeDistance(Node node) {
        return Math.abs(tableX - node.tableX) + Math.abs(tableX + tableY - node.tableX - node.tableY) + Math.abs(tableY - node.tableY) / 2;
    }

    @Override
    public String toString() {
        return "Node(" + tableX + "," + tableY + ")";
    }

    public static class Indexer {
        private static int index;

        public static int getIndex() {
            return index++;
        }

        public static void reset() {
            index = 0;
        }
    }
}
