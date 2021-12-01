package com.katafrakt.towerdefence.enemy.abilities;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.signals.Listener;
import com.badlogic.ashley.signals.Signal;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectSet;

public abstract class AbilityController implements Effect {
    protected Entity owner;
    protected Effect effect;

    public AbilityController(Effect effect, Entity owner) {
        this.owner = owner;
        this.effect = effect;
        if (effect instanceof AbilityController){
            ((AbilityController) effect).setOwners(owner);
        }
    }

    public AbilityController(Effect effect) {
        this.effect = effect;
    }

    public void setOwners(Entity owner) {
        this.owner=owner;
    }

    public void effectsUpdated(ObjectSet<AbilityController> effects){

    }
}
