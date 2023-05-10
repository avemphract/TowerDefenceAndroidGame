package com.katafrakt.towerdefence;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.GdxAI;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.katafrakt.towerdefence.input.InputProcessorModified;
import com.katafrakt.towerdefence.screens.LoadingScreen;
import com.katafrakt.towerdefence.screens.ScreenManager;

public class Main extends Game {
	private static Main main;
	public static Main getMain(){return main;}

	private SpriteBatch batch;
	private ShapeRenderer shapeRenderer;
	private InputProcessorModified inputProcessor;

	public Main(InputProcessorModified inputProcessor) {
		this.inputProcessor = inputProcessor;
	}

	@Override
	public void create () {
		main =this;
		batch = new SpriteBatch();
		shapeRenderer=new ShapeRenderer();
		ScreenManager.getInstance().push(new LoadingScreen(this,shapeRenderer,batch));

	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 0, 0, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		GdxAI.getTimepiece().update(Gdx.graphics.getDeltaTime());
		super.render();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}

	public InputProcessorModified getInputProcessor() {
		return inputProcessor;
	}
}
