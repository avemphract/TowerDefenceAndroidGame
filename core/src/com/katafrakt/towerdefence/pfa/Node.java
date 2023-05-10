package com.katafrakt.towerdefence.pfa;

import static com.katafrakt.towerdefence.utility.ConstValues.ROOT3DIV2;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.pfa.Connection;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectSet;
import com.katafrakt.towerdefence.ashley.components.buildings.BuildingComponent;
import com.katafrakt.towerdefence.map.Map;
import com.katafrakt.towerdefence.screens.GameManager;

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

    public Type type;

    private Entity building;
    public float connectionMultiplier = 1;
    public final ObjectSet<Entity> enemyEntities = new ObjectSet<>();

    public Node(int tableX, int tableY, int typeIndex) {
        index = Indexer.getIndex();
        this.tableX = tableX;
        this.tableY = tableY;
        type = Type.getByIndex(typeIndex);

        x = (LENGTH * 2 * ROOT3DIV2) * (tableX + tableY / 2f);
        y = (LENGTH * 3 * 0.5f) * tableY;

        vertices = new float[]{
                x, (y + LENGTH),
                (x + LENGTH * ROOT3DIV2), (y + LENGTH * 0.5f),
                (x + LENGTH * ROOT3DIV2), (y - LENGTH * 0.5f),
                x, (y - LENGTH),
                (x - LENGTH * ROOT3DIV2), (y - LENGTH * 0.5f),
                (x - LENGTH * ROOT3DIV2), (y + LENGTH * 0.5f),
        };


    }

    public Entity getBuilding() {
        return building;
    }

    public Node setBuilding(Entity building) {
        this.building = building;
        if (building != null) {
            connectionMultiplier = 1000;
        } else {
            connectionMultiplier = 1;
        }
        GameManager.getInstance().getMap().allyGridGraph.raycastCollisionDetector.changeNode(this);
        return this;
    }

    public boolean isInside(Vector3 vector3) {
        return isInside(vector3.x, vector3.y);
    }

    public boolean isInside(Vector2 vector2) {
        return isInside(vector2.x, vector2.y);
    }

    public boolean isInside(float x, float y) {
        if (this.y - HEIGHT / 2 <= y && y <= this.y - HEIGHT / 4) {
            if (this.x - WIDTH / 2 <= x && x <= this.x + WIDTH / 2) {
                float lengthX = Math.abs(this.x - x);
                float lengthY = Math.abs(this.y - LENGTH * 0.5f - y) * ROOT3DIV2 * 2;
                return lengthX + lengthY <= LENGTH * ROOT3DIV2;
            }
            return false;
        } else if (this.y - HEIGHT / 4 <= y && y <= this.y + HEIGHT / 4) {
            return (this.x - WIDTH / 2 <= x && x <= this.x + WIDTH / 2);
        } else if (this.y + HEIGHT / 4 <= y && y <= this.y + HEIGHT / 2) {
            float lengthX = Math.abs(this.x - x);
            float lengthY = Math.abs(this.y + LENGTH * 0.5f - y) * ROOT3DIV2 * 2;
            return lengthX + lengthY <= LENGTH * ROOT3DIV2;
        }
        return false;
    }

    public Vector2 getBorderAtAngle(float rad, float length) {
        return Vector2.X.rotateRad(rad).setLength(length).add(this);
    }

    public boolean isAllyWalkable() {
        return (type == Type.PLAIN_TILE || type == Type.ENEMY_PATH) && (building == null || BuildingComponent.getComponent(building).walkable);
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
