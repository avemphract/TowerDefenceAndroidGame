package com.katafrakt.towerdefence.enemy.abilities.effects;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.katafrakt.towerdefence.ashley.components.ai.SteeringComponent;
import com.katafrakt.towerdefence.enemy.abilities.Amplifier;
import com.katafrakt.towerdefence.enemy.abilities.UpdateEffect;

import java.util.Set;
import java.util.function.Function;

public class SpeedUpdateEffect implements UpdateEffect {
    private static final String TAG = SpeedUpdateEffect.class.getSimpleName();
    private Function<Entity, Set<Entity>> amplifier;
    private float maxSpeedUp;
    private float maxAccUp;

    public SpeedUpdateEffect(Function<Entity, Set<Entity>> amplifier, float maxSpeedUp, float maxAccUp) {
        this.amplifier = amplifier;
        this.maxSpeedUp = maxSpeedUp;
        this.maxAccUp = maxAccUp;
    }

    public SpeedUpdateEffect(float maxSpeedUp, float maxAccUp) {
        this(Amplifier.OWN, maxSpeedUp, maxAccUp);
    }

    @Override
    public void enter(Entity entity) {
        for (Entity target : amplifier.apply(entity)) {
            SteeringComponent steeringComponent = SteeringComponent.MAPPER.get(target);
            steeringComponent.maxLinearSpeed.addAddition(maxSpeedUp);
            steeringComponent.maxLinearAcceleration.addAddition(maxAccUp);
            //steeringComponent.setMaxLinearSpeed(steeringComponent.getMaxLinearSpeed() + maxSpeedUp);
            //steeringComponent.setMaxLinearAcceleration(steeringComponent.getMaxLinearAcceleration() + maxSpeedUp);

        }
    }

    @Override
    public void update(Entity entity, float delta) {

    }

    @Override
    public void leave(Entity entity) {
        for (Entity target : amplifier.apply(entity)) {
            SteeringComponent steeringComponent = SteeringComponent.MAPPER.get(target);
            steeringComponent.maxLinearSpeed.removeAddition(maxSpeedUp);
            steeringComponent.maxLinearAcceleration.removeAddition(maxAccUp);
        }
    }
}
