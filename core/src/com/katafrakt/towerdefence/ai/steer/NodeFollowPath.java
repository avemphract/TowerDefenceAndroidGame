package com.katafrakt.towerdefence.ai.steer;

import com.badlogic.gdx.ai.steer.Steerable;
import com.badlogic.gdx.ai.steer.behaviors.FollowPath;
import com.badlogic.gdx.ai.steer.utils.Path;
import com.badlogic.gdx.ai.steer.utils.paths.LinePath;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.katafrakt.towerdefence.ashley.components.ai.AiComponent;
import com.katafrakt.towerdefence.pfa.Node;

public class NodeFollowPath extends FollowPath<Vector2, LinePath.LinePathParam> {
    public NodeFollowPath(Steerable<Vector2> steerable, Array<Node> waypoints) {
        super(steerable, new LinePath<>(toVector(waypoints), true));
        setArrivalTolerance(0.2f).setDecelerationRadius(2).setPathOffset(1).setPredictionTime(0.4f);
    }

    public static Array<Vector2> toVector(Array<Node> nodes) {
        Array<Vector2> vector2Array = new Array<>();
        for (Node node : nodes) {
            vector2Array.add(node.cpy().add(MathUtils.random(-Node.LENGTH / 3, Node.LENGTH / 3), MathUtils.random(-Node.LENGTH / 3, Node.LENGTH / 3)));
        }
        return vector2Array;
    }
}
