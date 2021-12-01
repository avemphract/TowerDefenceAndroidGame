package com.katafrakt.towerdefence.enemy.abilities.effects;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.katafrakt.towerdefence.ashley.components.HealthComponent;
import com.katafrakt.towerdefence.ashley.components.entities.EnemyComponent;
import com.katafrakt.towerdefence.enemy.abilities.Amplifier;
import com.katafrakt.towerdefence.enemy.abilities.Effect;

import java.util.Set;
import java.util.function.Function;

public class HealInstantEffect implements Effect {
    private static final String TAG = HealInstantEffect.class.getSimpleName();
    private Function<Entity, Set<Entity>> amplifier;
    private float healAmount;

    public HealInstantEffect(float healAmount) {
        this(Amplifier.OWN,healAmount);
    }
    public HealInstantEffect(Function<Entity, Set<Entity>> amplifier, float healAmount) {
        this.amplifier = amplifier;
        this.healAmount = healAmount;
    }


    @Override
    public void enter(Entity entity) {
        for (Entity amp: amplifier.apply(entity)){
            HealthComponent healthComponent = HealthComponent.MAPPER.get(amp);
            if (healthComponent.isAlive()) {
                healthComponent.addCurrentHealth(healAmount);
            }
        }
    }
}
