package com.katafrakt.towerdefence.ui;

import com.badlogic.ashley.signals.Listener;
import com.badlogic.ashley.signals.Signal;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.katafrakt.towerdefence.input.MoveTabType;
import com.katafrakt.towerdefence.input.TabType;

public class InputTypeContainer extends Container<Table> implements Listener<TabType> {
    private static final String TAG = InputTypeContainer.class.getSimpleName();
    private static final int SCREEN_WIDTH = Gdx.graphics.getWidth();
    private static final int SCREEN_HEIGHT = Gdx.graphics.getHeight();

    private static final Rectangle TRANSFORM = new Rectangle(0, 0, SCREEN_WIDTH / 5, SCREEN_HEIGHT / 20);

    private final Skin skin;

    private final Label label;

    public InputTypeContainer(Skin skin) {
        super(new Table());
        this.skin = skin;
        setSize(TRANSFORM.width, TRANSFORM.height);
        setPosition(TRANSFORM.x, TRANSFORM.y);

        label = new Label("Move", skin);
        getActor().add(label);
    }

    @Override
    public void receive(Signal<TabType> signal, TabType object) {
        label.setText(object.getName());
    }
}
