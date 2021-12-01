package com.katafrakt.towerdefence.ashley.system.bullets;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.katafrakt.towerdefence.ashley.components.BoundComponent;
import com.katafrakt.towerdefence.ashley.components.HealthComponent;
import com.katafrakt.towerdefence.ashley.components.TransformComponent;
import com.katafrakt.towerdefence.ashley.components.entities.EnemyComponent;
import com.katafrakt.towerdefence.ashley.components.entities.bullets.LaserComponent;
import com.katafrakt.towerdefence.screens.GameManager;
import com.katafrakt.towerdefence.utility.Overlapping;

import java.lang.annotation.Target;
import java.util.Comparator;

public class LaserSystem extends IteratingSystem {
    private static final String TAG = LaserSystem.class.getSimpleName();
    private final PooledEngine engine;
    private final Array<Entity> possibleTargets = new Array<>();
    private final NearestComparator comparator = new NearestComparator();

    public LaserSystem() {
        super(Family.all(LaserComponent.class).get());
        engine = GameManager.getInstance().getEngine();
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        TransformComponent transformComponent = TransformComponent.MAPPER.get(entity);
        LaserComponent laserComponent = LaserComponent.MAPPER.get(entity);

        for (Entity target : engine.getEntitiesFor(EnemyComponent.FAMILY)) {
            if (HealthComponent.MAPPER.get(target).isAlive()) {
                TransformComponent targetTransform = TransformComponent.MAPPER.get(target);
                BoundComponent targetBound = BoundComponent.MAPPER.get(target);
                if (Overlapping.overlapRectangleLine(targetTransform, targetBound, transformComponent, laserComponent.endPoint)) {
                    possibleTargets.add(target);
                }
            }
        }
        comparator.setPoint(transformComponent, laserComponent.endPoint);
        possibleTargets.sort(comparator);
        int i;
        for (i = 0; i < laserComponent.penetration && i < possibleTargets.size; i++) {
            HealthComponent.MAPPER.get(possibleTargets.get(i)).addCurrentHealth(-(laserComponent.totalDamage / laserComponent.time) * deltaTime);
        }
        if (i != 0) {
            //i--;
            //BoundComponent enemyBound = BoundComponent.MAPPER.get(possibleTargets.get(i));
            //TransformComponent enemyTransform = TransformComponent.MAPPER.get(possibleTargets.get(i));
            //laserComponent.realEnd = getNearest(transformComponent, Overlapping.intersectionRectangleLine(enemyTransform, enemyBound, transformComponent, laserComponent.endPoint));
        } else {
        }            laserComponent.realEnd = laserComponent.endPoint;

        possibleTargets.clear();

        laserComponent.remainTime -= deltaTime;
        if (laserComponent.remainTime < 0) {
            engine.removeEntity(entity);
        }
    }

    private class NearestComparator implements Comparator<Entity> {
        public Vector2 begin;
        public Vector2 end;

        public void setPoint(Vector2 begin, Vector2 end) {
            this.begin = begin;
            this.end = end;
        }

        @Override
        public int compare(Entity o1, Entity o2) {
            TransformComponent transform1 = TransformComponent.MAPPER.get(o1);
            BoundComponent bound1 = BoundComponent.MAPPER.get(o1);

            TransformComponent transform2 = TransformComponent.MAPPER.get(o2);
            BoundComponent bound2 = BoundComponent.MAPPER.get(o2);

            Vector2 nearest1 = getNearest(begin, Overlapping.intersectionRectangleLine(transform1, bound1, begin, end));
            Vector2 nearest2 = getNearest(begin, Overlapping.intersectionRectangleLine(transform2, bound2, begin, end));

            return Float.compare(begin.dst2(nearest1), begin.dst2(nearest2));
        }


    }

    public Vector2 getNearest(Vector2 vec, Array<Vector2> vectors) {
        if (vectors.size==2)
            return getNearest(vec, vectors.get(0), vectors.get(1));
        else
            return vectors.get(0);
    }

    public Vector2 getNearest(Vector2 vec, Vector2 v1, Vector2 v2) {
        return (vec.dst2(v1) < vec.dst2(v2)) ? v1 : v2;
    }
}
