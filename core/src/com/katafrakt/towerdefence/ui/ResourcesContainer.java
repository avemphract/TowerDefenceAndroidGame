package com.katafrakt.towerdefence.ui;

import com.badlogic.ashley.signals.Listener;
import com.badlogic.ashley.signals.Signal;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.katafrakt.towerdefence.core.resources.Energy;
import com.katafrakt.towerdefence.core.resources.Resource;
import com.katafrakt.towerdefence.utility.Renderable;

import java.text.DecimalFormat;

public class ResourcesContainer extends Container<Table> implements Renderable {
    private static final String TAG = ResourcesContainer.class.getSimpleName();
    private static final int SCREEN_WIDTH = Gdx.graphics.getWidth();
    private static final int SCREEN_HEIGHT = Gdx.graphics.getHeight();

    public static final Rectangle TRANSFORM = new Rectangle(SCREEN_WIDTH / 4 * 3, SCREEN_HEIGHT / 8 * 7, SCREEN_WIDTH / 4, SCREEN_HEIGHT / 8);

    private final DecimalFormat format = new DecimalFormat("#.#");
    {
        format.setMinimumFractionDigits(1);
    }
    private final PlayerHud playerHud;
    private final Skin skin;
    private final Energy energy;
    private final Resource resource;

    private final Label energyLabel;
    private final Label resourceLabel;

    public ResourcesContainer(PlayerHud playerHud, Skin skin, Energy energy, Resource resource) {
        super(new Table());
        this.playerHud = playerHud;
        this.skin = skin;
        this.energy = energy;
        this.resource = resource;

        energyLabel = new Label("E: ", skin);
        resourceLabel = new Label("R: ", skin);
        getActor().add(energyLabel);
        getActor().row();
        getActor().add(resourceLabel);
        setSize(TRANSFORM.width, TRANSFORM.height);
        setPosition(TRANSFORM.x, TRANSFORM.y);
        energy.add(new EnergyListener());
        energy.add(new ResourceListener());
    }

    @Override
    public void render(float deltaTime) {

    }

    public class EnergyListener implements Listener<Float> {

        @Override
        public void receive(Signal<Float> signal, Float object) {
            energyLabel.setText("E:" + format.format(energy.getCurrentValue()) + "/" + energy.capacity);
        }
    }

    public class ResourceListener implements Listener<Float> {

        @Override
        public void receive(Signal<Float> signal, Float object) {
            resourceLabel.setText("R: " + resource.getCurrentValue());
        }
    }
}
