package com.katafrakt.towerdefence.map;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.pfa.GraphPath;
import com.badlogic.gdx.ai.pfa.SmoothableGraphPath;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ArrayMap;
import com.badlogic.gdx.utils.Collections;
import com.badlogic.gdx.utils.IntMap;
import com.badlogic.gdx.utils.ObjectSet;
import com.badlogic.gdx.utils.StreamUtils;
import com.katafrakt.towerdefence.pfa.AbstractGridGraph;
import com.katafrakt.towerdefence.pfa.GraphAlly;
import com.katafrakt.towerdefence.pfa.GraphEnemy;
import com.katafrakt.towerdefence.pfa.Node;

import org.junit.jupiter.api.Test;
import org.w3c.dom.traversal.NodeFilter;

import java.awt.image.ColorConvertOp;
import java.util.Arrays;

public class Map {
    private static final String TAG = Map.class.getSimpleName();
    public GraphAlly allyGridGraph;
    public GraphEnemy enemyGridGraph;

    public IntMap<IntMap<Node>> nodeMap = new IntMap<IntMap<Node>>() {
        @Override
        public IntMap<Node> get(int key) {
            if (super.get(key) == null)
                nodeMap.put(key, new IntMap<Node>());
            return super.get(key);
        }
    };
    public ArrayMap<Node, Integer> endRemain;
    public Array<Node> nodes;
    public Node startNode;
    public Node endNode;

    GraphPath<Node> path;
    Vector2 baseVector = new Vector2();

    public Map(Array<Node> nodes, Node startNode, Node endNode) {
        for (Node node : nodes) {
            nodeMap.get(node.tableY).put(node.tableX, node);
        }
        this.startNode = startNode;
        this.endNode = endNode;
        this.nodes = nodes;

        allyGridGraph = new GraphAlly(this);
        enemyGridGraph = new GraphEnemy(this);

        endRemain = enemyGridGraph.getNodeRemain();
        path = allyGridGraph.getSmoothPath(startNode, endNode);
    }

    public void render(ShapeRenderer shapeRenderer, SpriteBatch spriteBatch) {
        allyGridGraph.render(shapeRenderer, spriteBatch);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.VIOLET);
        shapeRenderer.rect(startNode.x - 3, startNode.y - 3, 6, 6);

        shapeRenderer.setColor(Color.MAGENTA);
        shapeRenderer.rect(endNode.x - 3, endNode.y - 3, 6, 6);
        shapeRenderer.end();

    }

    public Node findNode(Vector3 vector) {
        return findNode(vector.x, vector.y);
    }

    public Node findNode(Vector2 vector) {
        return findNode(vector.x, vector.y);
    }

    public Node findNode(float x, float y) {
        int refY = y > 0 ? (int) (y / Node.TEMPLATE_HEIGHT) : (int) (y / Node.TEMPLATE_HEIGHT) - 1;
        int refX = x > 0 ? (int) (x / Node.WIDTH) : (int) (x / Node.WIDTH) - 1;

        float modY = y > 0 ? (y % Node.TEMPLATE_HEIGHT) : (y % Node.TEMPLATE_HEIGHT) + Node.TEMPLATE_HEIGHT;
        float modX = x > 0 ? (x % Node.WIDTH) : (x % Node.WIDTH) + Node.WIDTH;

        //Gdx.app.debug(TAG, "refX: " + refX + "  refY: " + refY + "  modX: " + modX + "  modY: " + modY);

        if (modY <= Node.TEMPLATE_HEIGHT / 6) {
            if (modX < Node.WIDTH / 2) {
                return nodeMap.get(refY * 2).get(refX - refY);
            } else {
                return nodeMap.get(refY * 2).get(refX - refY + 1);
            }
        } else if (modY <= Node.TEMPLATE_HEIGHT * 2 / 6) {
            float deg = MathUtils.atan2(modY - Node.TEMPLATE_HEIGHT / 6, Math.abs(Node.WIDTH / 2 - modX));
            if (deg > 0.5f) {
                return nodeMap.get(refY * 2 + 1).get(refX - refY);
            } else {
                if (0 < modX && modX < Node.WIDTH / 2) {
                    return nodeMap.get(refY * 2).get(refX - refY);
                } else {
                    return nodeMap.get(refY * 2).get(refX - refY + 1);
                }
            }
        } else if (modY <= Node.TEMPLATE_HEIGHT * 4 / 6) {
            return nodeMap.get(refY * 2 + 1).get(refX - refY);
        } else if (modY <= Node.TEMPLATE_HEIGHT * 5 / 6) {
            float deg = MathUtils.atan2(Node.TEMPLATE_HEIGHT * 5 / 6 - modY, Math.abs(Node.WIDTH / 2 - modX));
            if (deg > 0.5f) {
                return nodeMap.get(refY * 2 + 1).get(refX - refY);
            } else {
                if (0 < modX && modX < Node.WIDTH / 2) {
                    return nodeMap.get(refY * 2 + 2).get(refX - refY - 1);
                } else {
                    return nodeMap.get(refY * 2 + 2).get(refX - refY);
                }
            }
        } else if (modY <= Node.TEMPLATE_HEIGHT * 6 / 6) {
            if (0 < modX && modX < Node.WIDTH / 2) {
                return nodeMap.get(refY * 2 + 2).get(refX - refY - 1);
            } else {
                return nodeMap.get(refY * 2 + 2).get(refX - refY);
            }
        }
        //Gdx.app.error(TAG, "X: " + x + "  Y: " + y + "  refX: " + refX + "  refY: " + refY + "  modX: " + modX + "  modY: " + modY);
        return null;
    }

    public Node findFromTable(int x, int y) {
        return nodeMap.get(y).get(x);
    }

    public Array<Node> getAllNodeFromInsideRange(Node node, int range) {
        Array<Node> nodes = new Array<Node>() {
            @Override
            public void add(Node value) {
                if (value != null)
                    super.add(value);
            }
        };
        for (int y = +range; y >= 0; y--) {
            for (int x = -range; x + y <= range; x++) {
                nodes.add(nodeMap.get(node.tableY + y).get(node.tableX + x));
            }
        }
        for (int y = -range; y < 0; y++) {
            for (int x = -range - y; x <= range; x++) {
                nodes.add(nodeMap.get(node.tableY + y).get(node.tableX + x));
            }
        }
        return nodes;
    }

    public Array<Node> getAllNodeInRange(Node node, int range) {
        Array<Node> nodes = new Array<>();
        if (range == 0) {
            nodes.add(node);
            return nodes;
        }

        for (int i = 0; i < range; i++) {
            if (nodeMap.get(node.tableY + range - i) != null || nodeMap.get(node.tableY + range - i).get(node.tableX + i) != null) {
                nodes.add(nodeMap.get(node.tableY + range - i).get(node.tableX + i));
            }
        }

        for (int i = 0; i < range; i++) {
            //System.out.print("("+(-i)+","+(range)+")");
            nodes.add(nodeMap.get(node.tableY - i).get(node.tableX + range));
        }

        for (int i = 0; i < range; i++) {
            //System.out.print("("+(-range)+","+(range-i)+")");
            nodes.add(nodeMap.get(node.tableY - range).get(node.tableX + range - i));
        }

        for (int i = 0; i < range; i++) {
            //System.out.print("("+(-range+i)+","+(-i)+")");
            nodes.add(nodeMap.get(node.tableY - range + i).get(node.tableX - i));
        }

        for (int i = 0; i < range; i++) {
            //System.out.print("("+(+i)+","+(-range)+")");
            nodes.add(nodeMap.get(node.tableY + i).get(node.tableX - range));
        }
        for (int i = 0; i < range; i++) {
            //System.out.print("("+(+range)+","+(-range+i)+")");
            nodes.add(nodeMap.get(node.tableY + range).get(node.tableX - range + i));
        }
        return nodes;
    }

    public ObjectSet<Entity> enemyInNodes(Array<Node> nodes) {
        ObjectSet<Entity> entities = new ObjectSet<>();
        nodes.forEach((node) -> entities.addAll(node.enemyEntities));
        return entities;
    }

    public ObjectSet<Node> viewedNodes(Vector2 pos, int range, Node.Type... types) {
        return viewedNodes(findNode(pos), range, types);
    }

    public ObjectSet<Node> viewedNodes(Node center, int range, Node.Type... types) {
        ObjectSet<Node> nodes = new ObjectSet<>();
        for (Node node : getAllNodeFromInsideRange(center, range)) {
            int dist = center.nodeDistance(node);
            boolean result = true;
            for (int i = 1; i <= dist; i++) {
                float x = center.x + (node.x - center.x) * i / dist;
                float y = center.y + (node.y - center.y) * i / dist;
                if (Arrays.stream(types).anyMatch(type ->
                        findNode(x + 1, y + 1).type == type ||
                                findNode(x - 1, y + 1).type == type ||
                                findNode(x - 1, y - 1).type == type ||
                                findNode(x + 1, y - 1).type == type)) {
                    result = false;
                    break;
                }
            }
            if (result) {
                nodes.add(node);
            }
        }
        return nodes;
    }

    public ObjectSet<Node> viewedConnectedNode(Vector2 pos, int range, Node.Type... types) {
        return viewedConnectedNode(findNode(pos), range, types);
    }

    public ObjectSet<Node> viewedConnectedNode(Node center, int range, Node.Type... types) {
        ObjectSet<Node> nodes = new ObjectSet<>();
        nodes.add(center);
        for (int i = 1; i <= range; i++) {
            for (Node node : getAllNodeFromInsideRange(center, i)) {
                int dist = center.nodeDistance(node);
                boolean result = true;
                for (int j = 1; j <= dist; j++) {
                    float x = center.x + (node.x - center.x) * j / dist;
                    float y = center.y + (node.y - center.y) * j / dist;
                    if (Arrays.stream(types).anyMatch(type -> findNode(x + 1, y + 1).type == type || findNode(x - 1, y + 1).type == type || findNode(x - 1, y - 1).type == type || findNode(x + 1, y - 1).type == type) ||
                            !getAllNodeFromInsideRange(node, 1).containsAny(nodes.iterator().toArray(), true)) {
                        result = false;
                        break;
                    }
                }
                if (result) {
                    nodes.add(node);
                }
            }
        }
        return nodes;
    }
}
