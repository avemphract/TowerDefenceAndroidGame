package com.katafrakt.towerdefence.core.weapons.mortar;

import com.badlogic.ashley.core.Entity;
import com.katafrakt.towerdefence.core.weapons.Weapon;

public class MultiMortarWeapon extends MortarWeapon {
    protected int mortarCount;

    public MultiMortarWeapon(MultiMortarWeapon.Builder<?, ?> builder) {
        super(builder);
        this.mortarCount=builder.mortarCount;
    }

    @Override
    public void attack(Entity target) {
        for (int i=0;i<mortarCount;i++)
            super.attack(target);
    }

    @SuppressWarnings("unchecked")
    public static class Builder<B extends MultiMortarWeapon.Builder<B, T>, T extends MultiMortarWeapon> extends MortarWeapon.Builder<B, T> {
        private int mortarCount;

        public B setMortarCount(int mortarCount) {
            this.mortarCount = mortarCount;
            return (B) this;
        }


        @Override
        public Weapon build() {
            return new MultiMortarWeapon(this);
        }
    }
}