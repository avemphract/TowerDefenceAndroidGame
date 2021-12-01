package com.katafrakt.towerdefence.ashley.components.entities.bullets;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.ashley.signals.Listener;
import com.badlogic.ashley.signals.Signal;
import com.badlogic.gdx.utils.Pool;
import com.katafrakt.towerdefence.ai.steer.FaceThrust;
import com.katafrakt.towerdefence.ashley.components.HealthComponent;
import com.katafrakt.towerdefence.ashley.components.TransformComponent;
import com.katafrakt.towerdefence.ashley.components.VelocityComponent;
import com.katafrakt.towerdefence.ashley.components.ai.SteeringComponent;
import com.katafrakt.towerdefence.ashley.components.entities.EnemyComponent;
import com.katafrakt.towerdefence.map.Map;
import com.katafrakt.towerdefence.pfa.Node;
import com.katafrakt.towerdefence.screens.GameManager;
import com.katafrakt.towerdefence.utility.AfterEngine;
import com.katafrakt.towerdefence.utility.LocationAdapter;

public class RocketComponent implements Component, Pool.Poolable, AfterEngine {
    private static final String TAG = RocketComponent.class.getSimpleName();
    public static final ComponentMapper<RocketComponent> MAPPER = ComponentMapper.getFor(RocketComponent.class);
    public Entity own;
    public Listener<Entity> aliveListener;
    public float remainSecond;
    public float attackAmount;
    private Entity target;

    public RocketComponent init(Entity own, float remainSecond, float attackAmount) {
        this.own = own;
        this.remainSecond = remainSecond;
        this.attackAmount = attackAmount;
        return this;
    }

    @Override
    public void reset() {
        remainSecond = 0;
        attackAmount = 0;
    }

    public Entity getTarget() {
        return target;
    }

    public void setTarget(Entity target) {
        if (this.target!=null)
            HealthComponent.MAPPER.get(this.target).aliveSignal.remove(aliveListener);
        this.target = target;
        if (this.target!=null)
            HealthComponent.MAPPER.get(this.target).aliveSignal.add(aliveListener);

    }

    @Override
    public void afterEngine(PooledEngine engine, Entity own) {
        setTarget(null);
    }

    public static void targetless(Entity entity) {
        RocketComponent rocketComponent = RocketComponent.MAPPER.get(entity);
        SteeringComponent steeringComponent = SteeringComponent.MAPPER.get(entity);
        VelocityComponent velocityComponent = VelocityComponent.MAPPER.get(entity);

        ((FaceThrust) steeringComponent.behavior).isFaceActive(false);
        ((FaceThrust) steeringComponent.behavior).setTarget(null);
        velocityComponent.angular = 0;
        steeringComponent.steeringOutput.setZero();
        rocketComponent.setTarget(null);
        //rocketComponent.target = null;


    }

    public static void scanNewTarget(Entity entity) {
        Map map = GameManager.getInstance().getMap();
        TransformComponent transformComponent = TransformComponent.MAPPER.get(entity);
        SteeringComponent steeringComponent = SteeringComponent.MAPPER.get(entity);
        RocketComponent rocketComponent = RocketComponent.MAPPER.get(entity);
        for (int i = 0; i < 3; i++) {
            for (Node node : map.getAllNodeInRange(map.findNode(transformComponent), i)) {
                if (node == null)
                    continue;
                for (Entity enemy : node.enemyEntities) {
                    rocketComponent.setTarget(enemy);
                    ((FaceThrust) steeringComponent.behavior).setTarget(new LocationAdapter(TransformComponent.MAPPER.get(enemy)));
                    return;
                }
            }
        }

        targetless(entity);
    }


    public class AutoScanTrue implements Listener<Entity> {

        @Override
        public void receive(Signal<Entity> signal, Entity entity) {

            Map map = GameManager.getInstance().getMap();
            TransformComponent transformComponent = TransformComponent.MAPPER.get(entity);
            SteeringComponent steeringComponent = SteeringComponent.MAPPER.get(entity);
            for (int i = 0; i < 3; i++) {
                for (Node node : map.getAllNodeInRange(map.findNode(transformComponent), i)) {
                    if (node == null)
                        continue;
                    for (Entity enemy : node.enemyEntities) {
                        ((FaceThrust) steeringComponent.behavior).setTarget(new LocationAdapter(TransformComponent.MAPPER.get(enemy)));
                        return;
                    }
                }
            }

            targetless(entity);
        }
    }

    public class AutoScanFalse implements Listener<Entity> {

        @Override
        public void receive(Signal<Entity> signal, Entity entity) {
            RocketComponent rocketComponent = RocketComponent.MAPPER.get(entity);
            SteeringComponent steeringComponent = SteeringComponent.MAPPER.get(entity);
            VelocityComponent velocityComponent = VelocityComponent.MAPPER.get(entity);


            ((FaceThrust) steeringComponent.behavior).isFaceActive(false);
            ((FaceThrust) steeringComponent.behavior).setTarget(null);
            velocityComponent.angular = 0;
            steeringComponent.steeringOutput.setZero();
            rocketComponent.target = null;
        }
    }
}
