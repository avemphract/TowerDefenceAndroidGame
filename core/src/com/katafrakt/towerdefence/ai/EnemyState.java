package com.katafrakt.towerdefence.ai;

import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.ai.steer.behaviors.CollisionAvoidance;
import com.badlogic.gdx.ai.steer.behaviors.PrioritySteering;
import com.badlogic.gdx.ai.steer.behaviors.RaycastObstacleAvoidance;
import com.badlogic.gdx.ai.steer.utils.rays.RayConfigurationBase;
import com.badlogic.gdx.ai.steer.utils.rays.SingleRayConfiguration;
import com.badlogic.gdx.ai.utils.Ray;
import com.badlogic.gdx.math.Vector2;
import com.katafrakt.towerdefence.ai.steer.NodeFollowPath;
import com.katafrakt.towerdefence.ashley.components.ai.EnemyAiComponent;
import com.katafrakt.towerdefence.ashley.components.ai.SteeringComponent;
import com.katafrakt.towerdefence.screens.GameManager;

public enum EnemyState implements State<EnemyAiComponent> {
    FLOW_PATH() {
        @Override
        public void enter(EnemyAiComponent entity) {
            entity.path = GameManager.getInstance().getMap().enemyGridGraph.getPath(GameManager.getInstance().getMap().startNode, GameManager.getInstance().getMap().endNode);
            SteeringComponent steeringComponent = SteeringComponent.MAPPER.get(entity.entity);

            steeringComponent.behavior = null;
            NodeFollowPath n = new NodeFollowPath(steeringComponent, entity.path.nodes);
            RaycastObstacleAvoidance<Vector2> obstacleAvoidance =
                    new RaycastObstacleAvoidance<Vector2>(steeringComponent,
                            new SingleRayConfiguration<>(steeringComponent, 8),
                            GameManager.getInstance().getMap().enemyGridGraph.raycastCollisionDetector);
            PrioritySteering<Vector2> p = new PrioritySteering<Vector2>(steeringComponent);
            p.add(obstacleAvoidance);
            p.add(n);
            steeringComponent.behavior=p;
            steeringComponent.unUsedBehaviors.add(obstacleAvoidance);
            steeringComponent.unUsedBehaviors.add(n);

        }

        @Override
        public void update(EnemyAiComponent entity) {

        }

        @Override
        public void exit(EnemyAiComponent entity) {

        }

        @Override
        public boolean onMessage(EnemyAiComponent entity, Telegram telegram) {
            return false;
        }
    },
    EVADE_BULLET() {
        @Override
        public void enter(EnemyAiComponent entity) {

        }

        @Override
        public void update(EnemyAiComponent entity) {

        }

        @Override
        public void exit(EnemyAiComponent entity) {

        }

        @Override
        public boolean onMessage(EnemyAiComponent entity, Telegram telegram) {
            return false;
        }
    };

    protected static final String TAG = EnemyState.class.getSimpleName();

    @Override
    public String toString() {
        return super.toString();
    }
}
