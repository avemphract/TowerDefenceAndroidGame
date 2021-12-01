package com.katafrakt.towerdefence.enemy.abilities.effects;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.utils.Array;
import com.katafrakt.towerdefence.ashley.components.HealthComponent;
import com.katafrakt.towerdefence.ashley.components.entities.EnemyComponent;
import com.katafrakt.towerdefence.enemy.abilities.Amplifier;
import com.katafrakt.towerdefence.enemy.abilities.Effect;
import com.katafrakt.towerdefence.enemy.abilities.UpdateEffect;

import java.util.Set;
import java.util.function.Function;

public class HealUpdateEffect implements UpdateEffect {
    private static final String TAG = HealUpdateEffect.class.getSimpleName();
    private Function<Entity, Set<Entity>> amplifier;
    private float healPerSecond;


    public HealUpdateEffect(float healPerSecond, Function<Entity, Set<Entity>> amplifier) {
        this.amplifier = amplifier;
        this.healPerSecond = healPerSecond;
    }

    public HealUpdateEffect(float healPerSecond) {
        this(healPerSecond, Amplifier.OWN);
    }

    @Override
    public void enter(Entity entity) {

    }

    @Override
    public void update(Entity entity, float delta) {
        for (Entity amp : amplifier.apply(entity)) {
            HealthComponent healthComponent = HealthComponent.MAPPER.get(amp);
            if (healthComponent.isAlive()) {
                healthComponent.addCurrentHealth(healPerSecond * delta);
            }
        }
    }

    @Override
    public void leave(Entity entity) {

    }
}
