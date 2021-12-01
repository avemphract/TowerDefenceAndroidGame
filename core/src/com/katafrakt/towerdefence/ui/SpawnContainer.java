package com.katafrakt.towerdefence.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.katafrakt.towerdefence.Main;
import com.katafrakt.towerdefence.entities.BuildingType;
import com.katafrakt.towerdefence.input.SpawnTabType;
import com.katafrakt.towerdefence.input.TabType;

public class SpawnContainer extends Container<Table> {
    private static final String TAG = SpawnContainer.class.getSimpleName();
    private static final int SCREEN_WIDTH = Gdx.graphics.getWidth();
    private static final int SCREEN_HEIGHT = Gdx.graphics.getHeight();

    public static final Rectangle TRANSFORM = new Rectangle(0, SCREEN_HEIGHT / 3, SCREEN_WIDTH / 10, SCREEN_HEIGHT / 2);

    private final PlayerHud playerHud;
    private final Skin skin;

    public SpawnContainer(PlayerHud playerHud, Skin skin) {
        super(new Table());
        this.skin = skin;
        this.playerHud = playerHud;

        setSize(TRANSFORM.width, TRANSFORM.height);
        setPosition(TRANSFORM.x, TRANSFORM.y);
        for (BuildingType tower : BuildingType.values()) {
            SpawnObject spawnObject = new SpawnObject(tower);
            getActor().add(spawnObject).size(TRANSFORM.width, TRANSFORM.width);
            getActor().row();
        }
    }

    private static final Vector2 SPAWN_OBJECT_DIMENSION = new Vector2(SCREEN_WIDTH / 10, SCREEN_WIDTH / 10);

    public class SpawnObject extends Table {
        private final BuildingType buildingType;
        Label label;

        public SpawnObject(BuildingType buildingType) {
            this.buildingType = buildingType;
            label = new Label(buildingType.name(), skin);
            label.setAlignment(Align.center);
            add(label).grow();
            addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    super.clicked(event, x, y);
                    TabType tabType = new SpawnTabType(buildingType.name(), buildingType::spawn);
                    Main.getMain().getInputProcessor().setTabType(tabType);
                }
            });
        }
    }


}
