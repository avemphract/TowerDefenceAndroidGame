package com.katafrakt.towerdefence.ashley.components.entities.bullets;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.gdx.utils.Pool;

public class BulletComponent implements Component, Pool.Poolable {
    public static final ComponentMapper<BulletComponent> MAPPER = ComponentMapper.getFor(BulletComponent.class);
    public float attackAmount;
    public float bulletRemainSecond;

    public BulletComponent init(float attackAmount, float bulletRemainSecond){
        this.attackAmount=attackAmount;
        this.bulletRemainSecond=bulletRemainSecond;
        return this;
    }

    @Override
    public void reset() {
        attackAmount=0;
        bulletRemainSecond =0;
    }

}
