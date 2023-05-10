package com.katafrakt.towerdefence.pfa;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.pfa.Connection;
import com.badlogic.gdx.ai.pfa.PathSmoother;
import com.badlogic.gdx.ai.steer.utils.RayConfiguration;
import com.badlogic.gdx.ai.utils.Collision;
import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.ai.utils.Ray;
import com.badlogic.gdx.ai.utils.RaycastCollisionDetector;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ArrayMap;
import com.badlogic.gdx.utils.ObjectMap;
import com.katafrakt.towerdefence.map.Map;
import com.katafrakt.towerdefence.utility.LineRay;

import java.awt.geom.Line2D;
import java.util.Comparator;

public class GraphAlly extends AbstractGridGraph<GraphAlly.WalkableRaycastCollisionDetector> {
    private static final String TAG = GraphAlly.class.getSimpleName();

    public GraphAlly(Map map) {
        super(map);
        raycastCollisionDetector = new WalkableRaycastCollisionDetector();
        pathSmoother = new PathSmoother<>(raycastCollisionDetector);
    }

    @Override
    protected void createConnections() {

        for (Node node : map.nodes) {
            if (!isConnected(node))
                continue;

            if (map.nodeMap.get(node.tableY + 1).get(node.tableX) != null && isConnected(map.nodeMap.get(node.tableY + 1).get(node.tableX))) {
                node.allyConnection.add(new GridConnection(node, map.nodeMap.get(node.tableY + 1).get(node.tableX)));
            }
            if (map.nodeMap.get(node.tableY).get(node.tableX + 1) != null && isConnected(map.nodeMap.get(node.tableY).get(node.tableX + 1))) {
                node.allyConnection.add(new GridConnection(node, map.nodeMap.get(node.tableY).get(node.tableX + 1)));
            }
            if (map.nodeMap.get(node.tableY - 1).get(node.tableX + 1) != null && isConnected(map.nodeMap.get(node.tableY - 1).get(node.tableX + 1))) {
                node.allyConnection.add(new GridConnection(node, map.nodeMap.get(node.tableY - 1).get(node.tableX + 1)));
            }
            if (map.nodeMap.get(node.tableY - 1).get(node.tableX) != null && isConnected(map.nodeMap.get(node.tableY - 1).get(node.tableX))) {
                node.allyConnection.add(new GridConnection(node, map.nodeMap.get(node.tableY - 1).get(node.tableX)));
            }
            if (map.nodeMap.get(node.tableY).get(node.tableX - 1) != null && isConnected(map.nodeMap.get(node.tableY).get(node.tableX - 1))) {
                node.allyConnection.add(new GridConnection(node, map.nodeMap.get(node.tableY).get(node.tableX - 1)));
            }
            if (map.nodeMap.get(node.tableY + 1).get(node.tableX - 1) != null && isConnected(map.nodeMap.get(node.tableY + 1).get(node.tableX - 1))) {
                node.allyConnection.add(new GridConnection(node, map.nodeMap.get(node.tableY + 1).get(node.tableX - 1)));
            }
        }
    }

    private static boolean isConnected(Node node) {
        return node.type == Node.Type.ENEMY_PATH || node.type == Node.Type.PLAIN_TILE;
    }

    public void render(ShapeRenderer shapeRenderer, SpriteBatch spriteBatch) {
        ((WalkableRaycastCollisionDetector) raycastCollisionDetector).render(shapeRenderer);
    }

    @Override
    public Array<Connection<Node>> getConnections(Node fromNode) {
        return fromNode.allyConnection;
    }

    private Collision<Vector2> tempCollision = new Collision<>(Vector2.Zero.cpy(), Vector2.Zero.cpy());

    Array<Node> nodeArray = new Array<>();

    public Vector2 setLocation(Vector2 vector2Location) {
        Node node = map.findNode(vector2Location);
        if (!node.isAllyWalkable()) {
            for (int i = 1; true; i++) {
                nodeArray.addAll(map.getAllNodeInRange(node, i));
                nodeArray.sort((o1, o2) -> Float.compare(vector2Location.dst2(o1), vector2Location.dst2(o2)));
                for (Node temp : nodeArray) {
                    if (temp.isAllyWalkable()) {
                        System.out.println("Node: " + temp.x + "," + temp.y + " Target: " + vector2Location.x + "," + vector2Location.y);
                        Vector2 vector2 = new Vector2(node).mulAdd(vector2Location, -1).setLength(Node.WIDTH / 3);
                        vector2Location.set(temp.x, temp.y).add(vector2);
                        return vector2Location;
                    }
                }
                nodeArray.clear();
            }
        }
        return vector2Location;
    }


    protected class WalkableRaycastCollisionDetector implements RaycastCollisionDetector<Vector2> {
        ArrayMap<Node, Array<LineRay>> colliderMap = new ArrayMap<Node, Array<LineRay>>() {
            @Override
            public Array<LineRay> get(Node key) {
                if (super.get(key) == null) {
                    super.put(key, new Array<>());
                }
                return super.get(key);
            }
        };
        Array<LineRay> emptyArray = new Array<>();
        LineRay tempLineRay = new LineRay(new Vector2(), new Vector2());
        Collision<Vector2> collision = new Collision<>(new Vector2(), new Vector2());

        public WalkableRaycastCollisionDetector() {
            for (Node node : map.nodes) {
                if (!node.isAllyWalkable()) {
                    for (int i = 0; i < node.vertices.length; i += 2) {
                        colliderMap.get(node).add(new LineRay(
                                new Vector2((node.vertices[(i + 0) % node.vertices.length] + node.vertices[(i + 2) % node.vertices.length]) / 2, (node.vertices[(i + 1) % node.vertices.length] + node.vertices[(i + 3) % node.vertices.length]) / 2),
                                new Vector2((node.vertices[(i + 2) % node.vertices.length] + node.vertices[(i + 4) % node.vertices.length]) / 2, (node.vertices[(i + 3) % node.vertices.length] + node.vertices[(i + 5) % node.vertices.length]) / 2)));
                    }
                }
            }

        }

        public void changeNode(Node node) {
            raycastCollisionDetector.colliderMap.get(node).clear();
            if (!node.isAllyWalkable()) {
                for (int i = 0; i < node.vertices.length; i += 2) {
                    colliderMap.get(node).add(new LineRay(
                            new Vector2(node.vertices[i], node.vertices[i + 1]),
                            new Vector2(node.vertices[(i + 2) % node.vertices.length], node.vertices[(i + 3) % node.vertices.length])));
                }
            }
        }

        @Override
        public boolean collides(Ray<Vector2> ray) {
            for (Array<LineRay> lineRays : colliderMap.values()) {
                for (LineRay lineRay : lineRays) {
                    if (Line2D.linesIntersect(ray.start.x, ray.start.y, ray.end.x, ray.end.y,
                            lineRay.start.x, lineRay.start.y, lineRay.end.x, lineRay.end.y
                    ))
                        return true;
                }

            }
            return false;
        }

        @Override
        public boolean findCollision(Collision<Vector2> outputCollision, Ray<Vector2> ray) {
            for (Node node : map.getAllNodeFromInsideRange(map.findNode(ray.start), 1)) {
                for (LineRay lineRay : colliderMap.get(node, emptyArray)) {
                    if (Line2D.linesIntersect(ray.start.x, ray.start.y, ray.end.x, ray.end.y,
                            lineRay.start.x, lineRay.start.y, lineRay.end.x, lineRay.end.y
                    )) {
                        lineRay.intersectionPoint(outputCollision, tempLineRay.set(ray));
                        return true;
                    }
                }

            }
            return false;
        }

        public void render(ShapeRenderer shapeRenderer) {
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            for (ObjectMap.Entry<Node, Array<LineRay>> array : colliderMap.entries()) {
                for (LineRay lineRay : array.value) {
                    shapeRenderer.line(lineRay.start.x, lineRay.start.y, lineRay.end.x, lineRay.end.y);
                }
            }
            shapeRenderer.circle(collision.point.x, collision.point.y, 1);
            shapeRenderer.circle(collision.point.x + collision.normal.x, collision.point.y + collision.normal.y, 2);
            shapeRenderer.end();
        }
    }
}
