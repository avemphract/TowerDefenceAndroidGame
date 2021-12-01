package com.katafrakt.towerdefence.pfa;

import com.badlogic.gdx.ai.pfa.Connection;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ArrayMap;
import com.badlogic.gdx.utils.ObjectSet;
import com.katafrakt.towerdefence.map.Map;

public class GraphEnemy extends AbstractGridGraph {
    private static final String TAG = GraphEnemy.class.getSimpleName();

    public GraphEnemy(Map map) {
        super(map);
    }

    @Override
    protected void createConnections() {
        for (Node node : map.nodes) {
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
                        result.put(node, Math.min(result.get(currentNode, Integer.MAX_VALUE), i+1));
                    }
                }
                result.put(currentNode, Math.min(result.get(currentNode, Integer.MAX_VALUE), i));
            }
            i++;
            nextNodes = tempNodes;
        }
        return result;

    }
}
