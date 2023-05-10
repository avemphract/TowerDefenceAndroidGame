package com.katafrakt.towerdefence.input;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector3;
import com.katafrakt.towerdefence.Main;
import com.katafrakt.towerdefence.ashley.components.BoundComponent;
import com.katafrakt.towerdefence.ashley.components.FocusableComponent;
import com.katafrakt.towerdefence.ashley.components.TransformComponent;
import com.katafrakt.towerdefence.ashley.components.ai.PlayerAiComponent;
import com.katafrakt.towerdefence.ashley.components.entities.NameComponent;
import com.katafrakt.towerdefence.screens.GameManager;
import com.katafrakt.towerdefence.utility.Overlapping;

public class SelectedTabType extends TabType{
    private final static String TAG = SelectedTabType.class.getSimpleName();
    private Entity entity;

    public SelectedTabType(Entity entity) {
        this.entity = entity;
    }

    public Entity getEntity() {
        return entity;
    }

    @Override
    public String getName() {
        return "Selected "+ (NameComponent.MAPPER.get(entity).name);
    }

    @Override
    public void tab(int screenX, int screenY) {
        Vector3 gamePos=new Vector3();
        GameManager.getInstance().getCamera().unproject(gamePos.set(screenX,screenY,0));
        for (Entity entity : GameManager.getInstance().getEngine().getEntitiesFor(Family.all(FocusableComponent.class).get())) {
            if (TransformComponent.MAPPER.has(entity) && BoundComponent.MAPPER.has(entity)) {
                TransformComponent transformComponent = TransformComponent.MAPPER.get(entity);
                BoundComponent boundComponent = BoundComponent.MAPPER.get(entity);
                if (Overlapping.overlapsRectanglePoint(transformComponent, boundComponent, gamePos)) {
                    Gdx.app.log(TAG,entity.toString());
                    if (PlayerAiComponent.MAPPER.has(entity))
                        Main.getMain().getInputProcessor().setTabType(new PlayerSelectedTabType(entity));
                    else
                        Main.getMain().getInputProcessor().setTabType(new SelectedTabType(entity));
                }
            }
        }
    }

    @Override
    public void drag(int screenX, int screenY) {

    }
}
