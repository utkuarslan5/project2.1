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
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class SettingsState extends State {
    private SpriteBatch spriteBatch;
    private Stage stage;
    private Skin skin;
    private Image background;
    private BitmapFont gameFont;
    private BitmapFont gameFontMusic;
    private ImageButton returnButton;
    private ImageButton MusicChecked;
    private ImageButton AiChecked;
    private static boolean musicBoolean = true;

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
        parameter.size = 120;
        gameFont = gen.generateFont(parameter);
        gameFont.setColor(Color.LIGHT_GRAY);
        stage = new Stage(viewport, spriteBatch);
        Gdx.input.setInputProcessor(stage);

        FreeTypeFontGenerator.FreeTypeFontParameter parameter2 = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter2.size = 50;
        gameFontMusic = gen.generateFont(parameter2);
        gameFontMusic.setColor(Color.WHITE);

        skin = new Skin(Gdx.files.internal("cloud-form/skin/cloud-form-ui.json"));
        stage = new Stage(viewport, spriteBatch);
        Texture img = new Texture("aurora.jpg");
        background = new Image(img);
        Gdx.input.setInputProcessor(stage);

        returnButton = new ImageButton(new TextureRegionDrawable(new Texture(Gdx.files.internal("returnArrow.png"))),
                new TextureRegionDrawable(new Texture(Gdx.files.internal("returnArrowPressed.png"))));
        returnButton.getImage().setScale(1/18f);
        returnButton.setScale(0.5f);
        returnButton.setPosition(15, 730);

        MusicChecked = new ImageButton(new TextureRegionDrawable(new Texture(Gdx.files.internal("checkedCheckBox.png"))),
                new TextureRegionDrawable(new Texture(Gdx.files.internal("checkedCheckBox.png"))),
                new TextureRegionDrawable(new Texture(Gdx.files.internal("uncheckedCheckBox.png"))));
        MusicChecked.getImage().setScale(1/10f);
        MusicChecked.setScale(0.1f);
        MusicChecked.setPosition(700, 450);

        AiChecked = new ImageButton(new TextureRegionDrawable(new Texture(Gdx.files.internal("checkedCheckBox.png"))),
                new TextureRegionDrawable(new Texture(Gdx.files.internal("checkedCheckBox.png"))),
                new TextureRegionDrawable(new Texture(Gdx.files.internal("uncheckedCheckBox.png"))));
        AiChecked.getImage().setScale(1/10f);
        AiChecked.setScale(0.1f);
        AiChecked.setPosition(700, 280);

    }

    @Override
    public void update(float dt) {
        if (returnButton.isChecked()){
            State MenuState = new MenuState(gsm);
            gsm.pop();
            gsm.push(MenuState);
        }
        handleInput();
    }

    @Override
    public void draw() {

        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.addActor(background);
        stage.addActor(returnButton);
        stage.addActor(MusicChecked);
        stage.addActor(AiChecked);

        stage.draw();
        spriteBatch.begin();
        gameFont.draw(spriteBatch,"Settings", 430, 750);
        gameFontMusic.draw(spriteBatch,"Music:",470,490);
        gameFontMusic.draw(spriteBatch,"AI:",470,320);
        spriteBatch.end();
    }

    @Override
    public void handleInput() {
        if(MusicChecked.isChecked() && AbaloneGame.music.isPlaying()){
            musicBoolean = !musicBoolean;
            System.out.println(musicBoolean);
            AbaloneGame.music.pause();
        }else if(!MusicChecked.isChecked() && !AbaloneGame.music.isPlaying()){
            musicBoolean = !musicBoolean;
            System.out.println(musicBoolean);
            AbaloneGame.music.play();
        }
    }

    @Override
    public void dispose() {

    }
}
