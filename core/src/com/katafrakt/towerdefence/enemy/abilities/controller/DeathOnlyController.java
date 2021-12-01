package com.katafrakt.towerdefence.enemy.abilities.controller;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.signals.Listener;
import com.badlogic.ashley.signals.Signal;
import com.katafrakt.towerdefence.ashley.components.HealthComponent;
import com.katafrakt.towerdefence.ashley.components.TransformComponent;
import com.katafrakt.towerdefence.ashley.components.entities.EnemyComponent;
import com.katafrakt.towerdefence.enemy.abilities.AbilityController;
import com.katafrakt.towerdefence.enemy.abilities.Effect;
import com.katafrakt.towerdefence.screens.GameManager;

public class DeathOnlyController extends AbilityController implements Listener<Entity> {

    public DeathOnlyController(Effect effect, Entity owner) {
        super(effect,owner);
        HealthComponent.MAPPER.get(owner).aliveSignal.add(this);
    }

    public DeathOnlyController(Effect effect) {
        super(effect);
    }

    @Override
    public void setOwners(Entity owner) {
        super.setOwners(owner);
        HealthComponent.MAPPER.get(owner).aliveSignal.add(this);
    }

    @Override
    public void receive(Signal<Entity> signal, Entity object) {
        enter(owner);
    }

    @Override
    public void enter(Entity entity) {
        effect.enter(entity);
    }
}
