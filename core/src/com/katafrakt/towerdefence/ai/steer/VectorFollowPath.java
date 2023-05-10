package com.katafrakt.towerdefence.ai.steer;

import com.badlogic.gdx.ai.steer.Steerable;
import com.badlogic.gdx.ai.steer.behaviors.FollowPath;
import com.badlogic.gdx.ai.steer.utils.Path;
import com.badlogic.gdx.ai.steer.utils.paths.LinePath;
import com.badlogic.gdx.math.Vector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class VectorFollowPath extends FollowPath<Vector2, LinePath.LinePathParam> {

    public VectorFollowPath(Steerable<Vector2> owner, Array<Vector2> path) {
        super(owner, new LinePath<>(path, true));
        setArrivalTolerance(0.5f).setDecelerationRadius(2).setPathOffset(2f).setPredictionTime(0).setArriveEnabled(true);
    }
}
