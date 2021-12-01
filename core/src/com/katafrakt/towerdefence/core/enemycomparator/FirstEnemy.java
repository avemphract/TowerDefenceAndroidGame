package com.katafrakt.towerdefence.core.enemycomparator;

import com.badlogic.ashley.core.Entity;
import com.katafrakt.towerdefence.ashley.components.entities.EnemyComponent;

import java.util.Comparator;

public class FirstEnemy implements Comparator<Entity> {
    @Override
    public int compare(Entity o1, Entity o2) {
        EnemyComponent e1=EnemyComponent.MAPPER.get(o1);
        EnemyComponent e2=EnemyComponent.MAPPER.get(o2);
        return Float.compare(e2.progress, e1.progress);
    }
}
