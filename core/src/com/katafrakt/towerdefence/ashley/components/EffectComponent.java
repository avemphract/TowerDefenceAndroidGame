package com.katafrakt.towerdefence.ashley.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.ObjectSet;
import com.badlogic.gdx.utils.Pool;
import com.katafrakt.towerdefence.enemy.abilities.AbilityController;
import com.katafrakt.towerdefence.enemy.abilities.Effect;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class EffectComponent implements Component, Pool.Poolable {
    public static final ComponentMapper<EffectComponent> MAPPER = ComponentMapper.getFor(EffectComponent.class);
    private static final String TAG = EffectComponent.class.getSimpleName();

    public ObjectSet<AbilityController> effects = new ObjectSet<>();
    public void addEffect(AbilityController abilityController){

    }


    @Override
    public void reset() {
        effects.clear();
    }
}
