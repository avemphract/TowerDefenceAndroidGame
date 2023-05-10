package com.katafrakt.towerdefence.pfa;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.pfa.Connection;
import com.badlogic.gdx.ai.utils.Collision;
import com.badlogic.gdx.ai.utils.Ray;
import com.badlogic.gdx.ai.utils.RaycastCollisionDetector;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ArrayMap;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectSet;
import com.katafrakt.towerdefence.map.Map;
import com.katafrakt.towerdefence.utility.LineRay;

import java.awt.geom.Line2D;
import java.util.Arrays;
import java.util.HashMap;

public class GraphEnemy extends AbstractGridGraph<GraphEnemy.WalkableRaycastCollisionDetector> {
    private static final String TAG = GraphEnemy.class.getSimpleName();
    public Array<Node> enemyNodes;

    public GraphEnemy(Map map) {
        super(map);
        raycastCollisionDetector = new WalkableRaycastCollisionDetector();
    }

    @Override
    protected void createConnections() {
        enemyNodes = new Array<>();
        for (Node node : map.nodes) {
            if (node.type == Node.Type.ENEMY_PATH) {
                enemyNodes.add(node);
            }

            if (!isConnected(node))
                continue;

            if (map.nodeMap.get(node.tableY + 1).get(node.tableX) != null && isConnected(map.nodeMap.get(node.tableY + 1).get(node.tableX))) {
                node.enemyConnection.add(new GridConnection(node, map.nodeMap.get(node.tableY + 1).get(node.tableX)));
            }
            if (map.nodeMap.get(node.tableY).get(node.tableX + 1) != null && isConnected(map.nodeMap.get(node.tableY).get(node.tableX + 1))) {
                node.enemyConnection.add(new GridConnection(node, map.nodeMap.get(node.tableY).get(node.tableX + 1)));
            }
            if (map.nodeMap.get(node.tableY - 1).get(node.tableX + 1) != null && isConnected(map.nodeMap.get(node.tableY - 1).get(node.tableX + 1))) {
                node.enemyConnection.add(new GridConnection(node, map.nodeMap.get(node.tableY - 1).get(node.tableX + 1)));
            }
            if (map.nodeMap.get(node.tableY - 1).get(node.tableX) != null && isConnected(map.nodeMap.get(node.tableY - 1).get(node.tableX))) {
                node.enemyConnection.add(new GridConnection(node, map.nodeMap.get(node.tableY - 1).get(node.tableX)));
            }
            if (map.nodeMap.get(node.tableY).get(node.tableX - 1) != null && isConnected(map.nodeMap.get(node.tableY).get(node.tableX - 1))) {
                node.enemyConnection.add(new GridConnection(node, map.nodeMap.get(node.tableY).get(node.tableX - 1)));
            }
            if (map.nodeMap.get(node.tableY + 1).get(node.tableX - 1) != null && isConnected(map.nodeMap.get(node.tableY + 1).get(node.tableX - 1))) {
                node.enemyConnection.add(new GridConnection(node, map.nodeMap.get(node.tableY + 1).get(node.tableX - 1)));
            }
        }
    }

    private static boolean isConnected(Node node) {
        return node.type == Node.Type.ENEMY_PATH;
    }

    @Override
    public Array<Connection<Node>> getConnections(Node fromNode) {
        return fromNode.enemyConnection;
    }

    @Override
    public void render(ShapeRenderer shapeRenderer, SpriteBatch spriteBatch) {
        super.render(shapeRenderer, spriteBatch);
    }

    public ArrayMap<Node, Integer> getNodeRemain() {
        ArrayMap<Node, Integer> result = new ArrayMap<>();
        ObjectSet<Node> nextNodes = new ObjectSet<>();
        nextNodes.add(map.endNode);

        int i = 0;
        while (!nextNodes.isEmpty()) {
            ObjectSet<Node> tempNodes = new ObjectSet<>();
            for (Node currentNode : nextNodes) {
                for (Node node : map.getAllNodeInRange(currentNode, 1)) {
                    if (result.containsKey(node)) {
                        continue;
                    } else if ((Node.Type.ENEMY_PATH.equals(node.type)))
                        tempNodes.add(node);
                    else {
                        result.put(node, Math.min(result.get(currentNode, Integer.MAX_VALUE), i + 1));
                    }
                }
                result.put(currentNode, Math.min(result.get(currentNode, Integer.MAX_VALUE), i));
            }
            i++;
            nextNodes = tempNodes;
        }
        return result;

    }

    protected class WalkableRaycastCollisionDetector implements RaycastCollisionDetector<Vector2> {
        ArrayMap<Node, Array<LineRay>> colliderMap = new ArrayMap<>();
        private Collision<Vector2> collision = new Collision<>(new Vector2(), new Vector2());
        private Array<LineRay> emptyArray = Array.of(LineRay.class);
        private LineRay tempLineRay = new LineRay(new Vector2(), new Vector2());


        public WalkableRaycastCollisionDetector() {
            ArrayMap<Node, Array<Vector2>> vectorMap = new ArrayMap<>();
            for (Node node : enemyNodes) {
                vectorMap.put(node, new Array<>());
                for (int i = 0; i < node.vertices.length; i += 2) {
                    vectorMap.get(node).add(new Vector2(node.vertices[i], node.vertices[i + 1]));
                }
            }
            for (ObjectMap.Entry<Node, Array<Vector2>> entry : vectorMap.entries()) {
                if (!colliderMap.containsKey(entry.key)) {
                    colliderMap.put(entry.key, new Array<>());
                }
                for (Vector2 vector2 : entry.value) {
                    ObjectSet<Node> allNode = map.findAllNode(vector2);

                    Array<Node> enemyNode = new Array<>();
                    Array<Node> otherNode = new Array<>();
                    for (Node node : allNode) {
                        if (Node.Type.ENEMY_PATH == node.type) {
                            enemyNode.add(node);
                        } else {
                            otherNode.add(node);
                        }
                    }
                    if (enemyNode.isEmpty() || otherNode.isEmpty()) {
                        continue;
                    } else if (enemyNode.size == 2) {
                        colliderMap.get(entry.key).add(new LineRay(
                                new Vector2((enemyNode.get(0).x + otherNode.get(0).x) / 2, (enemyNode.get(0).y + otherNode.get(0).y) / 2),
                                new Vector2((enemyNode.get(1).x + otherNode.get(0).x) / 2, (enemyNode.get(1).y + otherNode.get(0).y) / 2)
                        ));
                    } else {
                        colliderMap.get(entry.key).add(new LineRay(
                                new Vector2((enemyNode.get(0).x + otherNode.get(0).x) / 2, (enemyNode.get(0).y + otherNode.get(0).y) / 2),
                                new Vector2((enemyNode.get(0).x + otherNode.get(1).x) / 2, (enemyNode.get(0).y + otherNode.get(1).y) / 2)
                        ));
                    }
                }

            }
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

        @Override
        public boolean collides(Ray<Vector2> ray) {
            for (Node node : map.getAllNodeFromInsideRange(map.findNode(ray.start), 1)) {
                for (LineRay lineRay : colliderMap.get(node, emptyArray)) {
                    Gdx.app.log(TAG, lineRay.toString());
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
                        collision = outputCollision;
                        return true;
                    }
                }

            }
            return false;
        }
    }
}
