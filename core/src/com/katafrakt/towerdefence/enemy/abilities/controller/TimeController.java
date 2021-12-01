package com.katafrakt.towerdefence.enemy.abilities.controller;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.signals.Listener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.ObjectSet;
import com.katafrakt.towerdefence.ashley.components.EffectComponent;
import com.katafrakt.towerdefence.ashley.components.HealthComponent;
import com.katafrakt.towerdefence.ashley.components.entities.EnemyComponent;
import com.katafrakt.towerdefence.enemy.abilities.AbilityController;
import com.katafrakt.towerdefence.enemy.abilities.Cloneable;
import com.katafrakt.towerdefence.enemy.abilities.Effect;
import com.katafrakt.towerdefence.enemy.abilities.UpdateAbilityController;
import com.katafrakt.towerdefence.enemy.abilities.UpdateEffect;

public class TimeController extends UpdateAbilityController implements Cloneable<TimeController> {
    private static final String TAG = TimeController.class.getSimpleName();
    private Listener<Entity> aliveListener;
    private float time;
    private float remainTime;

    public TimeController(Effect effect, Entity owner, float time) {
        super(effect, owner);
        this.time = time;
        aliveListener = (signal, object) -> leave(owner);
        HealthComponent.MAPPER.get(owner).aliveSignal.add(aliveListener);
    }

    public TimeController(Effect effect, float time) {
        super(effect);
        this.time = time;
    }

    @Override
    public void setOwners(Entity owner) {
        super.setOwners(owner);
        aliveListener = (signal, object) -> leave(owner);
        HealthComponent.MAPPER.get(owner).aliveSignal.add(aliveListener);
    }

    @Override
    public void enter(Entity entity) {
        remainTime = time;
        EffectComponent.MAPPER.get(entity).effects.add(this);
        effect.enter(entity);
    }

    @Override
    public void update(Entity entity, float delta) {
        if (remainTime < 0) {
            leave(entity);
        } else {
            if (effect instanceof UpdateEffect) {
                ((UpdateEffect) effect).update(entity, delta);
            }
        }
        //Gdx.app.log(TAG, "Remain Time: " + remainTime);
        remainTime -= delta;
    }

    @Override
    public void leave(Entity entity) {
        if (effect instanceof UpdateEffect) {
            ((UpdateEffect) effect).leave(entity);
        }
        HealthComponent.MAPPER.get(owner).aliveSignal.remove(aliveListener);
        EffectComponent.MAPPER.get(entity).effects.remove(this);
    }

    @Override
    public TimeController clone(Entity entity) {
        return new TimeController(effect, entity, time);
    }
}
