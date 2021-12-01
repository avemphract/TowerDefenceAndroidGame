package com.katafrakt.towerdefence.ui;

import com.badlogic.ashley.signals.Listener;
import com.badlogic.ashley.signals.Signal;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.katafrakt.towerdefence.Main;
import com.katafrakt.towerdefence.ashley.components.TransformComponent;
import com.katafrakt.towerdefence.input.MoveTabType;
import com.katafrakt.towerdefence.input.SelectedTabType;
import com.katafrakt.towerdefence.input.TabType;
import com.katafrakt.towerdefence.screens.GameManager;
import com.katafrakt.towerdefence.screens.GameScreen;
import com.katafrakt.towerdefence.utility.AssetOrganizer;
import com.katafrakt.towerdefence.utility.Renderable;

public class PlayerHud implements Renderable {
    private static final String TAG = PlayerHud.class.getSimpleName();
    private static final boolean ALL_DEBUG = true;
    private static final int SCREEN_WIDTH = Gdx.graphics.getWidth();
    private static final int SCREEN_HEIGHT = Gdx.graphics.getHeight();

    private final GameScreen gameScreen;
    private final OrthographicCamera camera;
    private final Viewport viewport;
    private final Skin skin;

    private final InputTypeContainer inputTypeContainer;
    private final SpawnContainer spawnContainer;
    public final Container<TextButton> cancelButtonContainer;
    public SelectedEntityContainer entityContainer;
    private final ResourcesContainer resourcesContainer;

    private final Listener<TabType> tabTypeListener = new TabTypeListener();
    //private final Listener<OrthographicCamera> cameraListener = new CameraListener();

    private final Stage stage = new Stage();

    public PlayerHud(GameScreen gameScreen, OrthographicCamera camera, Viewport viewport) {
        this.gameScreen = gameScreen;
        this.camera = camera;
        this.viewport = viewport;
        this.skin = AssetOrganizer.getInstance().get("skins/neutralizer/skin.json", Skin.class);


        Container<Table> container = new Container<>();
        container.setPosition(1, 1);
        container.setSize(SCREEN_WIDTH - 2, SCREEN_HEIGHT - 2);
        stage.addActor(container);

        //InputTypeContainer
        inputTypeContainer = new InputTypeContainer(skin);
        Main.getMain().getInputProcessor().tabTypeReceiver.add(inputTypeContainer);
        stage.addActor(inputTypeContainer);

        //SpawnContainer
        spawnContainer = new SpawnContainer(this, skin);
        stage.addActor(spawnContainer);

        //CancelContainer
        cancelButtonContainer = new Container<>();
        cancelButtonContainer.setActor(new TextButton("Cancel", skin));
        cancelButtonContainer.setPosition(0, SpawnContainer.TRANSFORM.y - 30);
        cancelButtonContainer.setSize(SCREEN_WIDTH / 10, 30);
        cancelButtonContainer.getActor().addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Main.getMain().getInputProcessor().setTabType(MoveTabType.MOVE_TAB_TYPE);
            }
        });
        stage.addActor(cancelButtonContainer);

        //EntityContainer
        entityContainer = new SelectedEntityContainer(this, skin);

        //ResourcesContainer
        resourcesContainer = new ResourcesContainer(this, skin, gameScreen.energy, gameScreen.resource);
        stage.addActor(resourcesContainer);

        stage.setDebugAll(ALL_DEBUG);
        Main.getMain().getInputProcessor().tabTypeReceiver.add(tabTypeListener);
        GameManager.getInstance().getInputMultiplexer().addProcessor(stage);
    }


    @Override
    public void render(float deltaTime) {
        stage.act(deltaTime);
        stage.draw();
    }

    public Stage getStage() {
        return stage;
    }

    private class TabTypeListener implements Listener<TabType> {

        Vector3 screePos = new Vector3();

        @Override
        public void receive(Signal<TabType> signal, TabType object) {
            cancelButtonContainer.setVisible(!(object instanceof MoveTabType));
            entityContainer.setVisible(object instanceof SelectedTabType);
            if (object instanceof SelectedTabType) {
                TransformComponent transformComponent = TransformComponent.MAPPER.get(((SelectedTabType) object).getEntity());
                camera.project(screePos.set(transformComponent.x, transformComponent.y, 0));
                entityContainer.dispose();
                entityContainer.init(camera, ((SelectedTabType) object).getEntity());
            } else {
                entityContainer.dispose();
            }
        }
    }
}
