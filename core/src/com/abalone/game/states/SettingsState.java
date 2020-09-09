package com.abalone.game.states;

import com.abalone.game.AbaloneGame;
import com.abalone.game.managers.GameStateManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class SettingsState extends State {
    private SpriteBatch spriteBatch;
    private Stage stage;
    private Image background;
    private BitmapFont gameFont;
    protected SettingsState(GameStateManager ourGsm) {
        super(ourGsm);
    }

    @Override
    public void init() {
        spriteBatch = new SpriteBatch();
        Viewport viewport = new FitViewport(AbaloneGame.width, AbaloneGame.height, AbaloneGame.cam);
        viewport.apply();
        FreeTypeFontGenerator gen = new FreeTypeFontGenerator(Gdx.files.internal("Prime-Regular.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 110;
        gameFont = gen.generateFont(parameter);
        gameFont.setColor(Color.LIGHT_GRAY);
        stage = new Stage(viewport, spriteBatch);
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void update(float dt) {

    }

    @Override
    public void draw() {
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
        spriteBatch.begin();
        gameFont.draw(spriteBatch,"Settings", 390, 750);
        spriteBatch.end();
    }

    @Override
    public void handleInput() {

    }

    @Override
    public void dispose() {

    }
}
