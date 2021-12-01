package com.katafrakt.towerdefence.core.weapons;

import com.badlogic.ashley.core.Entity;
import com.katafrakt.towerdefence.Main;
import com.katafrakt.towerdefence.ashley.components.TransformComponent;
import com.katafrakt.towerdefence.core.AttackType;

public abstract class Weapon {
    protected AttackType attackType;
    protected Entity owner;
    protected TransformComponent ownerTransform;

    protected float attackAmount;
    //per second
    protected float attackRate;
    protected float range;

    protected float lastAttackTime;

    public Weapon(Builder<?, ?> builder) {
        this.attackType = builder.attackType;
        this.attackAmount = builder.attackAmount;
        this.attackRate = builder.attackRate;
        this.range = builder.range;
    }

    public void setOwner(Entity entity) {
        this.owner = entity;
        this.ownerTransform = TransformComponent.MAPPER.get(entity);
    }

    public Entity getOwner() {
        return owner;
    }

    public TransformComponent getOwnerTransform() {
        return ownerTransform;
    }

    public abstract void attack(Entity target);

    public float remainPercent() {
        return (Main.getMain().getTotalTime() - lastAttackTime) * attackRate;
    }

    public float getAttackAmount() {
        return attackAmount;
    }

    public float getAttackRate() {
        return attackRate;
    }

    public float getRange() {
        return range;
    }

    @Override
    public String toString() {
        return "Weapon{" +
                "attackType=" + attackType +
                ", owner=" + owner +
                ", ownerTransform=" + ownerTransform +
                ", attackAmount=" + attackAmount +
                ", attackRate=" + attackRate +
                ", range=" + range +
                ", lastAttackTime=" + lastAttackTime +
                '}';
    }

    @SuppressWarnings("unchecked")
    public abstract static class Builder<B extends Builder<B, T>, T extends Weapon> implements javafx.util.Builder<Weapon> {
        protected AttackType attackType;
        protected float attackAmount;
        protected float attackRate;
        protected float range;

        public B setAttackType(AttackType attackType) {
            this.attackType = attackType;
            return (B) this;
        }

        public B setAttackAmount(float attackAmount) {
            this.attackAmount = attackAmount;
            return (B) this;
        }

        public B setAttackRate(float attackRate) {
            this.attackRate = attackRate;
            return (B) this;
        }

        public B setRange(float range) {
            this.range = range;
            return (B) this;
        }
    }
}
