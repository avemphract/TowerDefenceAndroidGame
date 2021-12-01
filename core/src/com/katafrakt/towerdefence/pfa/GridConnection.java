package com.katafrakt.towerdefence.pfa;

import com.badlogic.gdx.ai.pfa.Connection;
import com.badlogic.gdx.math.MathUtils;
import com.katafrakt.towerdefence.map.Map;

public class GridConnection implements Connection<Node> {
    Node startNode, endNode;

    public GridConnection(Node startNode, Node endNode) {
        this.startNode = startNode;
        this.endNode = endNode;
    }

    @Override
    public float getCost() {
        return startNode.dst(endNode) + MathUtils.random();
    }

    @Override
    public Node getFromNode() {
        return startNode;
    }

    @Override
    public Node getToNode() {
        return endNode;
    }
}
