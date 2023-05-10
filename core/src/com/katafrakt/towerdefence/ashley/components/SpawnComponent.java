package com.katafrakt.towerdefence.ashley.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.gdx.ai.GdxAI;
import com.katafrakt.towerdefence.Main;
import com.katafrakt.towerdefence.entities.MinionType;
import com.katafrakt.towerdefence.screens.GameManager;

public class SpawnComponent implements Component {
    public static final ComponentMapper<SpawnComponent> MAPPER = ComponentMapper.getFor(SpawnComponent.class);
    public MinionType type;
    public float lastSpawnTime;
    public float spawnRate;
    public float maxSpawn;

    public SpawnComponent init(MinionType minionType, float spawnRate, float maxSpawn) {
        this.type = minionType;
        this.lastSpawnTime = GdxAI.getTimepiece().getTime();
        this.spawnRate = spawnRate;
        this.maxSpawn = maxSpawn;
        return this;
    }
}
