package com.katafrakt.towerdefence.screens;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.katafrakt.towerdefence.Main;
import com.katafrakt.towerdefence.ashley.EntityListener;
import com.katafrakt.towerdefence.ashley.system.AiSystem;
import com.katafrakt.towerdefence.ashley.system.EffectSystem;
import com.katafrakt.towerdefence.ashley.system.bullets.AreaExpanderSystem;
import com.katafrakt.towerdefence.ashley.system.bullets.BulletSystem;
import com.katafrakt.towerdefence.ashley.system.DebugGraphicSystem;
import com.katafrakt.towerdefence.ashley.system.DebugLogSystem;
import com.katafrakt.towerdefence.ashley.system.EnemySystem;
import com.katafrakt.towerdefence.ashley.system.HealthDebugSystem;
import com.katafrakt.towerdefence.ashley.system.HealthSystem;
import com.katafrakt.towerdefence.ashley.system.bullets.LaserSystem;
import com.katafrakt.towerdefence.ashley.system.bullets.MortarSystem;
import com.katafrakt.towerdefence.ashley.system.bullets.RocketSystem;
import com.katafrakt.towerdefence.ashley.system.SteeringSystem;
import com.katafrakt.towerdefence.ashley.system.VelocitySystem;
import com.katafrakt.towerdefence.core.resources.Energy;
import com.katafrakt.towerdefence.core.resources.Resource;
import com.katafrakt.towerdefence.entities.spawner.EnemySpawner;
import com.katafrakt.towerdefence.entities.EntityInput;
import com.katafrakt.towerdefence.entities.BuildingType;
import com.katafrakt.towerdefence.map.Map;
import com.katafrakt.towerdefence.map.MapLoader;
import com.katafrakt.towerdefence.pfa.Node;
import com.katafrakt.towerdefence.ui.PlayerHud;
import com.katafrakt.towerdefence.utility.AfterEngine;
import com.katafrakt.towerdefence.utility.BeforeEngine;

public class GameScreen implements Screen {
    private static final String TAG = GameScreen.class.getSimpleName();

    private final PlayerHud playerHud;

    private final SpriteBatch spriteBatch;
    private final PooledEngine pooledEngine;
    private EnemySpawner enemySpawner;
    private final EntityInput entityInput;
    private final InputProcessor desktopInput;
    private final Map map;

    private final OrthographicCamera camera;
    private final Viewport viewport;
    private final ShapeRenderer shapeRenderer;

    public final Energy energy = new Energy();
    public final Resource resource = new Resource();

    public GameScreen(ShapeRenderer shapeRenderer, SpriteBatch spriteBatch) {
        this.shapeRenderer = shapeRenderer;
        this.spriteBatch = spriteBatch;
        camera = new OrthographicCamera();
        viewport = new FillViewport(80, 45, camera);
        camera.position.set(0, 0, 0);

        playerHud = new PlayerHud(this,camera, viewport);

        pooledEngine = new PooledEngine() {
            @Override
            public void addEntity(Entity entity) {
                super.addEntity(entity);
                for (Component component : entity.getComponents())
                    if (component instanceof BeforeEngine)
                        ((BeforeEngine) component).beforeEngine(this, entity);
            }

            @Override
            public void removeEntity(Entity entity) {
                for (Component component : entity.getComponents())
                    if (component instanceof AfterEngine)
                        ((AfterEngine) component).afterEngine(this,entity);
                super.removeEntity(entity);
            }
        };
        entityInput = new EntityInput(camera, pooledEngine);
        desktopInput = Main.getMain().getInputProcessor().init(camera);

        map = MapLoader.mapLoader(1);
        GameManager.getInstance().setMap(map).setEngine(pooledEngine).setCamera(camera);

        GameManager.getInstance().getInputMultiplexer().addProcessor(desktopInput);
        //Gdx.input.setInputProcessor(new InputMultiplexer(desktopInput, entityInput.listener));


        pooledEngine.addSystem(new DebugGraphicSystem(camera, shapeRenderer));
        pooledEngine.addSystem(new DebugLogSystem());
        pooledEngine.addSystem(new HealthDebugSystem(shapeRenderer));

        pooledEngine.addSystem(new AiSystem());
        pooledEngine.addSystem(new EnemySystem());

        pooledEngine.addSystem(new BulletSystem());
        pooledEngine.addSystem(new MortarSystem());
        pooledEngine.addSystem(new RocketSystem());
        pooledEngine.addSystem(new AreaExpanderSystem());
        pooledEngine.addSystem(new LaserSystem());

        pooledEngine.addSystem(new HealthSystem());
        pooledEngine.addSystem(new SteeringSystem());
        pooledEngine.addSystem(new VelocitySystem());
        pooledEngine.addSystem(new EffectSystem());
        pooledEngine.addEntityListener(new EntityListener());

    }

    @Override
    public void show() {
        enemySpawner = new EnemySpawner();
        for (int i = 0; i < 51; i++) {
            enemySpawner.spawn(map.startNode.x - Node.LENGTH / 2 + MathUtils.random(Node.LENGTH), map.startNode.y - Node.LENGTH / 2 + MathUtils.random(Node.LENGTH));
        }
        BuildingType.BASIC.spawn(map.findFromTable(3, 2));
        //TowerType.SNIPER.spawn(map.findFromTable(3, 3));

    }

    @Override
    public void render(float delta) {
        spriteBatch.setProjectionMatrix(camera.combined);
        shapeRenderer.setProjectionMatrix(camera.combined);
        viewport.apply();

        map.render(shapeRenderer, spriteBatch);
        spriteBatch.begin();
        pooledEngine.update(delta);
        spriteBatch.end();

        playerHud.render(delta);

        energy.addCurrentValue(delta/1);
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
         GameManager.getInstance().getInputMultiplexer().removeProcessor(desktopInput);
    }
}
