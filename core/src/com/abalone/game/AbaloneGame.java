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
import com.badlogic.gdx.audio.Music;

public class AbaloneGame extends ApplicationAdapter {
	SpriteBatch batch;
	public static int width;
	public static int height;
	public static OrthographicCamera cam;
	public static GameStateManager gsm;
	private Music music;


	@Override
	public void create () {
		music = Gdx.audio.newMusic(Gdx.files.internal("AbaloneMusic.ogg"));
		music.setLooping(true);
		music.setVolume(0.25f);
		music.play();
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
