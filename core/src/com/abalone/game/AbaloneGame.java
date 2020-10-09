package com.abalone.game;

import com.abalone.game.managers.GameStateManager;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class AbaloneGame extends ApplicationAdapter {
	SpriteBatch batch;
	public static int width;
	public static int height;
	public static OrthographicCamera cam;
	public static GameStateManager gsm;
	public static Music music1;
	public static Music music2;


	@Override
	public void create () {
		music1 = Gdx.audio.newMusic(Gdx.files.internal("WiiRemix.ogg"));
		music1.setLooping(true);
		music1.setVolume(0.18f);
		music1.play();
		music2 = Gdx.audio.newMusic(Gdx.files.internal("ElectroAbalone.ogg"));
		music2.setLooping(true);
		music2.setVolume(0.18f);
		batch = new SpriteBatch();
		width = Gdx.graphics.getWidth();
		height = Gdx.graphics.getHeight();
		cam = new OrthographicCamera(width, height);
		cam.translate(width / 2f, height / 2f);
		cam.update();
		gsm = new GameStateManager();

	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(255, 255, 255, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		gsm.update(Gdx.graphics.getDeltaTime());
		gsm.draw();
		batch.begin();
		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}
}
