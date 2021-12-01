package com.katafrakt.towerdefence.ui;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.signals.Listener;
import com.badlogic.ashley.signals.Signal;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.katafrakt.towerdefence.Main;
import com.katafrakt.towerdefence.ashley.components.FocusableComponent;
import com.katafrakt.towerdefence.ashley.components.TransformComponent;
import com.katafrakt.towerdefence.core.actions.PlayerAction;
import com.katafrakt.towerdefence.pfa.Node;

import java.util.Arrays;

public class SelectedEntityContainer implements Listener<OrthographicCamera> {
    private static final String TAG = SelectedEntityContainer.class.getSimpleName();

    Array<EntityAction> entityActions = new Array<EntityAction>() {
        @Override
        public void add(EntityAction value) {
            playerHud.getStage().addActor(value);
            super.add(value);
        }

        @Override
        public boolean removeValue(EntityAction value, boolean identity) {
            playerHud.getStage().getActors().removeValue(value, true);
            return super.removeValue(value, identity);
        }

        @Override
        public void clear() {
            for (EntityAction entityAction : this) {
                playerHud.getStage().getActors().removeValue(entityAction, true);
            }
            super.clear();
        }
    };
    private final Skin skin;
    private final PlayerHud playerHud;
    private float x, y;
    private Entity entity;

    public SelectedEntityContainer(PlayerHud playerHud, Skin skin) {
        this.playerHud = playerHud;
        this.skin = skin;
    }

    Vector3 screenPos = new Vector3();

    public SelectedEntityContainer init(OrthographicCamera camera, Entity entity) {
        TransformComponent transformComponent = TransformComponent.MAPPER.get(entity);
        FocusableComponent focusableComponent = FocusableComponent.MAPPER.get(entity);
        this.x = transformComponent.x;
        this.y = transformComponent.y;
        this.entity = entity;

        camera.project(screenPos.set(x, y, 0));
        Main.getMain().getInputProcessor().cameraReceiver.add(this);
        for (int i = 0; i < focusableComponent.actions.size; i++) {
            float rad = MathUtils.HALF_PI - ((focusableComponent.actions.size - 1) / 2f - i) * MathUtils.HALF_PI / 2;
            EntityAction entityAction = new EntityAction(focusableComponent.actions.get(i), x, y);
            entityAction.setPosition(screenPos.x + MathUtils.cos(rad) * 80, screenPos.y + MathUtils.sin(rad) * 80);
            //Gdx.app.log(TAG, entityAction.getX() + "," + entityAction.getY());
            entityActions.add(entityAction);
        }
        return this;
    }

    public void setVisible(boolean b) {
        for (EntityAction entityAction : entityActions) {
            entityAction.setVisible(b);
        }
    }

    @Override
    public void receive(Signal<OrthographicCamera> signal, OrthographicCamera object) {
        object.project(screenPos.set(x, y, 0));
        for (int i = 0; i < entityActions.size; i++) {
            float rad = MathUtils.HALF_PI - ((entityActions.size - 1) / 2f - i) * MathUtils.HALF_PI / 2;
            entityActions.get(i).setPosition(screenPos.x + MathUtils.cos(rad) * 80, screenPos.y + MathUtils.sin(rad) * 80);
        }
    }

    public void dispose() {
        entityActions.clear();
        Main.getMain().getInputProcessor().cameraReceiver.remove(this);
    }


    public class EntityAction extends Container<Table> {
        private final ClickListener clickListener;
        private final Label label;

        public EntityAction(PlayerAction action, float x, float y) {
            super(new Table());
            clickListener = new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                }

                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    action.act();
                    return true;
                }
            };
            setPosition(x, y);
            size(30, 30);
            addListener(clickListener);
            label = new Label(action.getClass().getSimpleName().substring(0, 1), skin);
            label.setAlignment(Align.center);
            getActor().add(label).grow();
        }


    }
}
