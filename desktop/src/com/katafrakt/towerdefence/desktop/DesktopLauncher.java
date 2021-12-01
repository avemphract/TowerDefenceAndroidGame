package com.katafrakt.towerdefence.desktop;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.katafrakt.towerdefence.Main;
import com.katafrakt.towerdefence.desktop.input.DesktopInput;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.height=450;
		config.width=800;
		new LwjglApplication(new Main(new DesktopInput()), config);
		Gdx.app.setLogLevel(Application.LOG_INFO);
	}
}
