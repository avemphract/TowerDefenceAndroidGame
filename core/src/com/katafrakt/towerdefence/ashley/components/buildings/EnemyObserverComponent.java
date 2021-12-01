package com.katafrakt.towerdefence.ashley.components.buildings;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;

public class EnemyObserverComponent implements Component {
    public static final ComponentMapper<EnemyObserverComponent> MAPPER = ComponentMapper.getFor(EnemyObserverComponent.class);
}
