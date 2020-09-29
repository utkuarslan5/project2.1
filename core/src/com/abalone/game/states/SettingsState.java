package com.abalone.game.states;

import com.abalone.game.AbaloneGame;
import com.abalone.game.managers.GameStateManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class SettingsState extends State {
    private SpriteBatch spriteBatch;
    private Stage stage;
    private Image background;
    private BitmapFont gameFont;
    private BitmapFont gameFontMusic;
    private ImageButton returnButton;
    private ImageButton MusicChecked;
    protected SettingsState(GameStateManager ourGsm) {
        super(ourGsm);
    }

    private AbaloneGame abaloneGame;

    @Override
    public void init() {
        spriteBatch = new SpriteBatch();
        Viewport viewport = new FitViewport(AbaloneGame.width, AbaloneGame.height, AbaloneGame.cam);
        viewport.apply();
        FreeTypeFontGenerator gen = new FreeTypeFontGenerator(Gdx.files.internal("Prime-Regular.ttf"));

        Texture img = new Texture("aurora.jpg");
        background = new Image(img);
        background.setWidth(AbaloneGame.width);
        background.setHeight(AbaloneGame.height);

        returnButton = new ImageButton(new TextureRegionDrawable(new Texture(Gdx.files.internal("returnArrow.png"))),
                new TextureRegionDrawable(new Texture(Gdx.files.internal("returnArrowPressed.png"))));
        returnButton.getImage().setScale(1/18f);
        returnButton.setScale(0.5f);
        returnButton.setPosition(15, 730);

        MusicChecked = new ImageButton(new TextureRegionDrawable(new Texture(Gdx.files.internal("checkedCheckBox.png"))),
                new TextureRegionDrawable(new Texture(Gdx.files.internal("checkedCheckBox.png"))),
                new TextureRegionDrawable(new Texture(Gdx.files.internal("uncheckedCheckBox.png"))));
        MusicChecked.getImage().setScale(1/18f);
        MusicChecked.setScale(0.1f);
        MusicChecked.setPosition(500, 472);

        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 110;
        gameFont = gen.generateFont(parameter);
        gameFont.setColor(Color.LIGHT_GRAY);
        stage = new Stage(viewport, spriteBatch);
        Gdx.input.setInputProcessor(stage);

        FreeTypeFontGenerator.FreeTypeFontParameter parameterMusic = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameterMusic.size = 30;
        gameFontMusic = gen.generateFont(parameterMusic);
        gameFontMusic.setColor(Color.WHITE);

    }

    @Override
    public void update(float dt) {
        if (returnButton.isChecked()){
            State MenuState = new MenuState(gsm);
            gsm.pop();
            gsm.push(MenuState);
        }

       /* if(MusicChecked.isChecked()){
            abaloneGame.music.pause();
        }*/
    }

    @Override
    public void draw() {
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
        spriteBatch.begin();
        gameFont.draw(spriteBatch,"Settings", 390, 750);
        gameFontMusic.draw(spriteBatch, "Music", 390, 500);
        spriteBatch.end();
        stage.addActor(background);
        stage.addActor(returnButton);
        stage.addActor(MusicChecked);
    }

    @Override
    public void handleInput() {

    }

    @Override
    public void dispose() {

    }
}
