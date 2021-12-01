package com.katafrakt.towerdefence.entities;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.katafrakt.towerdefence.ashley.components.BoundComponent;
import com.katafrakt.towerdefence.ashley.components.DebugGraphicComponent;
import com.katafrakt.towerdefence.ashley.components.FocusableComponent;
import com.katafrakt.towerdefence.ashley.components.TransformComponent;
import com.katafrakt.towerdefence.ashley.components.ai.TowerAiComponent;
import com.katafrakt.towerdefence.ashley.components.buildings.CollectorComponent;
import com.katafrakt.towerdefence.ashley.components.buildings.MinerComponent;
import com.katafrakt.towerdefence.ashley.components.entities.NameComponent;
import com.katafrakt.towerdefence.ashley.components.buildings.TowerComponent;
import com.katafrakt.towerdefence.core.AttackType;
import com.katafrakt.towerdefence.core.actions.RemovePlayerAction;
import com.katafrakt.towerdefence.core.actions.UpgradePlayerAction;
import com.katafrakt.towerdefence.core.enemycomparator.FirstEnemy;
import com.katafrakt.towerdefence.core.weapons.bullet.BulletWeapon;
import com.katafrakt.towerdefence.core.weapons.bullet.MultiParallelBulletWeapon;
import com.katafrakt.towerdefence.core.weapons.bullet.ShotgunBulletWeapon;
import com.katafrakt.towerdefence.core.weapons.bullet.SprayWeapon;
import com.katafrakt.towerdefence.core.weapons.Weapon;
import com.katafrakt.towerdefence.core.weapons.circle.AreaExpanderWeapon;
import com.katafrakt.towerdefence.core.weapons.laser.LaserWeapon;
import com.katafrakt.towerdefence.core.weapons.mortar.MortarWeapon;
import com.katafrakt.towerdefence.core.weapons.mortar.MultiMortarWeapon;
import com.katafrakt.towerdefence.core.weapons.rocket.RocketWeapon;
import com.katafrakt.towerdefence.core.weapons.rocket.RotatedRocketWeapon;
import com.katafrakt.towerdefence.pfa.Node;
import com.katafrakt.towerdefence.screens.GameManager;
import com.katafrakt.towerdefence.utility.DebugShapes;

import javafx.util.Builder;

public enum BuildingType {
    BASIC() {
        final BulletWeapon.Builder<?, BulletWeapon> builder1 = new BulletWeapon.Builder<>()
                .setAttackType(AttackType.MECHANIC)
                .setAttackAmount(10)
                .setAttackRate(4)
                .setRange(40)
                .setBulletSpeed(80)
                .setAccuracy(0.0f)
                .setBulletRadius(1);

        final MultiParallelBulletWeapon.Builder<?, MultiParallelBulletWeapon> builder2 = new MultiParallelBulletWeapon.Builder<>()
                .setAttackAmount(10)
                .setAttackRate(8)
                .setRange(35)
                .setAccuracy(0.0f)
                .setBulletSpeed(50)
                .setBulletRadius(1)
                .setBulletDistance(5)
                .setBulletAmount(3);

        final ShotgunBulletWeapon.Builder<?, ShotgunBulletWeapon> builder3 = new ShotgunBulletWeapon.Builder<>()
                .setAttackAmount(10)
                .setAttackRate(1)
                .setRange(35)
                .setAccuracy(0.0f)
                .setBulletSpeed(50)
                .setBulletRadius(1)
                .setBulletAmount(4)
                .setTotalAngle(MathUtils.degreesToRadians * 60f);

        final SprayWeapon.Builder<?, SprayWeapon> builder4 = new SprayWeapon.Builder<>()
                .setAttackAmount(10)
                .setAttackRate(1)
                .setRange(35)
                .setAccuracy(0.95f)
                .setBulletSpeed(50)
                .setBulletRadius(0.5f)
                .setBulletAmount(20)
                .setTotalAngle(MathUtils.degreesToRadians * 30f);

        final SprayWeapon.Builder<?, SprayWeapon> builder5 = new SprayWeapon.Builder<>()
                .setAttackAmount(10)
                .setAttackRate(5)
                .setRange(35)
                .setAccuracy(0.95f)
                .setBulletSpeed(50)
                .setBulletRadius(0.5f)
                .setBulletAmount(4)
                .setTotalAngle(MathUtils.degreesToRadians * 30f);

        final MortarWeapon.Builder<?, MortarWeapon> builder6 = new MortarWeapon.Builder<>()
                .setAttackAmount(10)
                .setAttackRate(5)
                .setRange(35)
                .setAccuracyRadius(10)
                .setTime(1)
                .setBulletRadius(1.5f);

        final MultiMortarWeapon.Builder<?, MultiMortarWeapon> builder7 = new MultiMortarWeapon.Builder<>()
                .setAttackAmount(10)
                .setAttackRate(1)
                .setRange(65)
                .setAccuracyRadius(8)
                .setTime(1)
                .setBulletRadius(1.5f)
                .setMortarCount(10);

        final RocketWeapon.Builder<?, RocketWeapon> builder8 = new RocketWeapon.Builder<>()
                .setAttackAmount(5)
                .setAttackRate(1f)
                .setRange(35)
                .setMaxLinearSpeed(15f)
                .setMaxLinearAcceleration(50f)
                .setMaxAngularSpeed(200)
                .setMaxAngularAcceleration(200)
                .setWidth(0.5f)
                .setHeight(1)
                .setAutoAiming(true);

        final RotatedRocketWeapon.Builder<?, RotatedRocketWeapon> builder9 = new RotatedRocketWeapon.Builder<>()
                .setAttackAmount(1)
                .setAttackRate(4f)
                .setRange(35)
                .setMaxLinearSpeed(15f)
                .setMaxLinearAcceleration(50f)
                .setMaxAngularSpeed(200)
                .setMaxAngularAcceleration(200)
                .setWidth(0.5f)
                .setHeight(1)
                .setAutoAiming(true)
                .setAccuracy(0.0f);

        final AreaExpanderWeapon.Builder<?, AreaExpanderWeapon> builder10 = new AreaExpanderWeapon.Builder<>()
                .setAttackAmount(25)
                .setAttackRate(0.4f)
                .setRange(40)
                .setRadiusSpeed(20);

        final LaserWeapon.Builder<?, LaserWeapon> builder11 = new LaserWeapon.Builder<>()
                .setAttackAmount(125)
                .setAttackRate(0.6f)
                .setRange(40)
                .setPenetration(10)
                .setTime(1);

        public Builder<Weapon> weaponBuilder() {
            return builder11;
        }

        @Override
        public Entity spawn(Node node) {
            PooledEngine engine = GameManager.getInstance().getEngine();

            Entity entity = engine.createEntity();
            entity.add(engine.createComponent(TransformComponent.class).init(node.x, node.y));
            entity.add(engine.createComponent(BoundComponent.class).init(Node.WIDTH, Node.HEIGHT));
            entity.add(engine.createComponent(FocusableComponent.class).init(FocusableComponent.Type.TOWER,
                    new RemovePlayerAction(entity), new UpgradePlayerAction(entity)));

            entity.add(engine.createComponent(TowerComponent.class).init(entity, weaponBuilder().build()));
            entity.add(engine.createComponent(TowerAiComponent.class).init(entity, new FirstEnemy()));
            entity.add(engine.createComponent(DebugGraphicComponent.class).init(new DebugShapes.LineHexagon(Color.BLACK, 5)));
            entity.add(engine.createComponent(NameComponent.class).init("Basic"));

            engine.addEntity(entity);
            return entity;
        }
    },
    SNIPER() {
        final BulletWeapon.Builder<?, BulletWeapon> builder = new BulletWeapon.Builder<>()
                .setAttackType(AttackType.MECHANIC)
                .setAttackAmount(40)
                .setAttackRate(0.5f)
                .setRange(90)
                .setBulletSpeed(180)
                .setAccuracy(0.9f)
                .setBulletRadius(1.5f);

        public Builder<Weapon> weaponBuilder() {
            return builder;
        }

        @Override
        public Entity spawn(Node node) {
            PooledEngine engine = GameManager.getInstance().getEngine();

            Entity entity = engine.createEntity();
            entity.add(engine.createComponent(TransformComponent.class).init(node.x, node.y));
            entity.add(engine.createComponent(BoundComponent.class).init(Node.WIDTH, Node.HEIGHT));
            entity.add(engine.createComponent(FocusableComponent.class).init(FocusableComponent.Type.TOWER,
                    new RemovePlayerAction(entity), new UpgradePlayerAction(entity)));

            entity.add(engine.createComponent(TowerComponent.class).init(entity, builder.build()));
            entity.add(engine.createComponent(TowerAiComponent.class).init(entity, new FirstEnemy()));
            entity.add(engine.createComponent(DebugGraphicComponent.class).init(new DebugShapes.LineHexagon(Color.LIGHT_GRAY, 6)));
            entity.add(engine.createComponent(NameComponent.class).init("Sniper"));

            engine.addEntity(entity);
            return entity;
        }
    },
    COLLECTOR() {
        @Override
        public Entity spawn(Node node) {
            PooledEngine engine = GameManager.getInstance().getEngine();

            Entity entity = engine.createEntity();
            entity.add(engine.createComponent(TransformComponent.class).init(node.x, node.y));
            entity.add(engine.createComponent(BoundComponent.class).init(Node.WIDTH, Node.HEIGHT));
            entity.add(engine.createComponent(FocusableComponent.class).init(FocusableComponent.Type.TOWER,
                    new RemovePlayerAction(entity), new UpgradePlayerAction(entity)));

            entity.add(engine.createComponent(DebugGraphicComponent.class).init(new DebugShapes.LineHexagon(Color.MAROON, 6)));
            entity.add(engine.createComponent(CollectorComponent.class).init(3, 1));
            entity.add(engine.createComponent(NameComponent.class).init("Collector"));

            engine.addEntity(entity);
            return entity;
        }
    },
    MINER(){
        @Override
        public Entity spawn(Node node) {
            PooledEngine engine = GameManager.getInstance().getEngine();

            Entity entity = engine.createEntity();
            entity.add(engine.createComponent(TransformComponent.class).init(node.x, node.y));
            entity.add(engine.createComponent(BoundComponent.class).init(Node.WIDTH, Node.HEIGHT));
            entity.add(engine.createComponent(FocusableComponent.class).init(FocusableComponent.Type.TOWER,
                    new RemovePlayerAction(entity), new UpgradePlayerAction(entity)));

            entity.add(engine.createComponent(DebugGraphicComponent.class).init(new DebugShapes.LineHexagon(Color.MAROON, 6)));
            entity.add(engine.createComponent(MinerComponent.class));
            entity.add(engine.createComponent(NameComponent.class).init("Collector"));

            engine.addEntity(entity);
            return entity;
        }
    };




    public abstract Entity spawn(Node node);

    public Entity spawn(float x, float y) {
        return spawn(GameManager.getInstance().getMap().findNode(x, y));
    }
}
