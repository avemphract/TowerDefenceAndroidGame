package com.katafrakt.towerdefence.ashley.system;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.katafrakt.towerdefence.ashley.components.DebugGraphicComponent;
import com.katafrakt.towerdefence.ashley.components.TransformComponent;
import com.katafrakt.towerdefence.pfa.Node;
import com.katafrakt.towerdefence.utility.DebugRender;
import com.katafrakt.towerdefence.utility.DebugShapes;
import com.katafrakt.towerdefence.utility.DoubleArray;
import com.katafrakt.towerdefence.utility.Pair;

public class DebugGraphicSystem extends IteratingSystem {
    private static final String TAG = DebugGraphicSystem.class.getSimpleName();
    private final ShapeRenderer shapeRenderer;
    private final OrthographicCamera camera;

    //private final ObjectMapArray<ShapeRenderer.ShapeType, DoubleArray<DebugShapes, Vector2>> shapeTransform = new ObjectMapArray<>();

    private final DoubleArray<DebugShapes, Vector2> lineShapes = new DoubleArray<>();
    private final DoubleArray<DebugShapes, Vector2> filledShapes = new DoubleArray<>();
    private final DoubleArray<DebugRender, Entity> debugRenderArray = new DoubleArray<>();

    public DebugGraphicSystem(OrthographicCamera camera, ShapeRenderer shapeRenderer) {
        super(Family.all(TransformComponent.class, DebugGraphicComponent.class).get());
        this.camera = camera;
        this.shapeRenderer = shapeRenderer;

    }

    @Override
    public void update(float deltaTime) {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.GRAY);
        for (float i = (camera.position.x - camera.viewportWidth * camera.zoom / 2) - (camera.position.x - camera.viewportWidth * camera.zoom / 2) % Node.WIDTH; i < camera.position.x + camera.viewportWidth * camera.zoom / 2; i += Node.WIDTH) {
            shapeRenderer.line(i, camera.position.y - camera.viewportHeight * camera.zoom / 2, i, camera.position.y + camera.viewportHeight * camera.zoom / 2);
        }
        for (float i = (camera.position.y - camera.viewportHeight * camera.zoom / 2) - (camera.position.y - camera.viewportHeight * camera.zoom / 2) % (Node.HEIGHT * 1.5f); i < camera.position.y + camera.viewportHeight * camera.zoom / 2; i += Node.HEIGHT * 1.5f) {
            shapeRenderer.line(camera.position.x - camera.viewportWidth * camera.zoom / 2, i, camera.position.x + camera.viewportWidth * camera.zoom / 2, i);
        }
        super.update(deltaTime);
        shapeRenderer.end();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        for (Pair<DebugShapes, Vector2> pair : lineShapes) {
            if (pair.getValue() instanceof TransformComponent) {
                pair.getKey().render(shapeRenderer, (TransformComponent) pair.getValue());
            } else {
                pair.getKey().render(shapeRenderer, pair.getValue(), 0);
            }
        }
        for (Pair<DebugRender, Entity> pair : debugRenderArray) {
            pair.getKey().render(shapeRenderer, pair.getValue());
        }
        shapeRenderer.end();
        lineShapes.clear();
        debugRenderArray.clear();


        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        for (Pair<DebugShapes, Vector2> pair : filledShapes) {
            if (pair.getValue() instanceof TransformComponent) {
                pair.getKey().render(shapeRenderer, (TransformComponent) pair.getValue());
            } else {
                pair.getKey().render(shapeRenderer, pair.getValue(), 0);
            }
        }
        shapeRenderer.end();
        filledShapes.clear();
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        DebugGraphicComponent debugGraphicComponent = DebugGraphicComponent.MAPPER.get(entity);
        TransformComponent transformComponent = TransformComponent.MAPPER.get(entity);

        if (debugGraphicComponent.debugShapes==null)
            ;
        else if (ShapeRenderer.ShapeType.Line.equals(debugGraphicComponent.debugShapes.shapeType))
            lineShapes.addPair(debugGraphicComponent.debugShapes, transformComponent);
        else if (ShapeRenderer.ShapeType.Filled.equals(debugGraphicComponent.debugShapes.shapeType))
            filledShapes.addPair(debugGraphicComponent.debugShapes, transformComponent);

        for (Component component : entity.getComponents()) {
            if (component instanceof DebugRender) {
                debugRenderArray.addPair((DebugRender) component, entity);
            }
        }


        /*
        AiComponent<?> aiComponent = AiComponent.getComponent(entity);
        if (aiComponent != null && aiComponent.path != null) {
            for (int i = 1; i < aiComponent.path.getCount(); i++) {
                Node prev = aiComponent.path.nodes.get(i - 1);
                Node curr = aiComponent.path.nodes.get(i);
                shapeRenderer.rectLine(prev.x, prev.y, curr.x, curr.y, 0.2f);
            }
        }

        if (aiComponent instanceof TowerAiComponent) {
            if (((TowerAiComponent) aiComponent).getTarget() != null) {
                if (TowerComponent.MAPPER.get(entity).weapon instanceof BulletWeapon) {
                    BulletWeapon bulletWeapon = (BulletWeapon) TowerComponent.MAPPER.get(entity).weapon;
                    shapeRenderer.rectLine(transformComponent, TransformComponent.MAPPER.get(((TowerAiComponent) aiComponent).getTarget()).cpy().rotateAroundRad(transformComponent, (1 - bulletWeapon.getAccuracy()) * BulletWeapon.MIN_ACCURACY_RAD), 0.1f);
                    shapeRenderer.rectLine(transformComponent, TransformComponent.MAPPER.get(((TowerAiComponent) aiComponent).getTarget()).cpy().rotateAroundRad(transformComponent, -(1 - bulletWeapon.getAccuracy()) * BulletWeapon.MIN_ACCURACY_RAD), 0.1f);
                    shapeRenderer.rectLine(transformComponent, TransformComponent.MAPPER.get(((TowerAiComponent) aiComponent).getTarget()), 0.2f);
                } else if (TowerComponent.MAPPER.get(entity).weapon instanceof MortarWeapon) {
                    MortarWeapon mortarWeapon = (MortarWeapon) TowerComponent.MAPPER.get(entity).weapon;
                    shapeRenderer.circle(TransformComponent.MAPPER.get(((TowerAiComponent) aiComponent).getTarget()).x, TransformComponent.MAPPER.get(((TowerAiComponent) aiComponent).getTarget()).y, mortarWeapon.getAccuracyRadius());
                } else if (TowerComponent.MAPPER.get(entity).weapon instanceof RocketWeapon) {
                    TransformComponent targetTransform = TowerAiComponent.MAPPER.get(entity).getTarget().getComponent(TransformComponent.class);
                    shapeRenderer.rectLine(targetTransform.x - 5, targetTransform.y, targetTransform.x + 5, targetTransform.y, 0.2f);
                    shapeRenderer.rectLine(targetTransform.x, targetTransform.y - 5, targetTransform.x, targetTransform.y + 5, 0.2f);
                }
            }
        }

        SteeringComponent steeringComponent = SteeringComponent.MAPPER.get(entity);
        if (steeringComponent != null && steeringComponent.behavior != null) {
            if (steeringComponent.behavior instanceof NodeFollowPath) {
                Vector2 vector2 = ((NodeFollowPath) steeringComponent.behavior).getInternalTargetPosition();
                shapeTransform.get(steeringCircle.getShapeType()).addPair(steeringCircle, vector2);
            }
            if (steeringComponent.behavior instanceof FaceThrust){
            }
        }

        TowerComponent towerComponent = TowerComponent.MAPPER.get(entity);
        if (towerComponent != null && towerComponent.weapon != null) {
            shapeTransform.get(ShapeRenderer.ShapeType.Line).addPair(new DebugShapes.LineCircle(Color.BLACK, towerComponent.weapon.getRange()), transformComponent);
        }

        AreaExpanderComponent areaExpanderComponent = AreaExpanderComponent.MAPPER.get(entity);
        if (areaExpanderComponent !=null){
            shapeTransform.get(ShapeRenderer.ShapeType.Line).addPair(new DebugShapes.LineCircle(Color.FOREST,areaExpanderComponent.radius),transformComponent);
        }
        */
    }
}
