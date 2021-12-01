package com.katafrakt.towerdefence.enemy.abilities.controller;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.signals.Listener;
import com.badlogic.ashley.signals.Signal;
import com.badlogic.gdx.Gdx;
import com.katafrakt.towerdefence.ashley.components.HealthComponent;
import com.katafrakt.towerdefence.enemy.abilities.AbilityController;
import com.katafrakt.towerdefence.enemy.abilities.Cloneable;
import com.katafrakt.towerdefence.enemy.abilities.Effect;

public class GetDamagedController extends AbilityController implements Listener<Float> {
    private static final String TAG = GetDamagedController.class.getSimpleName();
    private boolean onlyOneTime;

    public GetDamagedController(Effect effect, Entity owner, boolean onlyOneTime) {
        super(effect,owner);
        this.effect = effect;
        this.onlyOneTime = onlyOneTime;
        HealthComponent.MAPPER.get(owner).healthChange.add(this);
    }

    public GetDamagedController(Effect effect, boolean onlyOneTime) {
        super(effect);
        this.onlyOneTime = onlyOneTime;
    }

    @Override
    public void setOwners(Entity owner) {
        super.setOwners(owner);
        HealthComponent.MAPPER.get(owner).healthChange.add(this);
    }

    @Override
    public void receive(Signal<Float> signal, Float object) {
        if (object<0) {
            enter(owner);
            if (onlyOneTime) {
                HealthComponent.MAPPER.get(owner).healthChange.remove(this);
            }
        }
    }

    @Override
    public void enter(Entity entity) {
        if (effect instanceof Cloneable)
            ((Cloneable<?>) effect).clone(entity).enter(entity);
        else
            effect.enter(entity);
    }
}
