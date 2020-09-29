package com.abalone.game.states;

import com.abalone.game.AbaloneGame;
import com.abalone.game.managers.GameStateManager;
import com.abalone.game.objects.Ball;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class SettingsState extends State {
    private SpriteBatch spriteBatch;
    private Stage stage;
    private Skin skin;
    private Image background;
    private BitmapFont gameFont;
    private BitmapFont settingFont;
    private ImageButton goBack;
    private CheckBox musicOn;
    private CheckBox musicOff;
    private CheckBox aiOn;
    private CheckBox aiOff;
    private Image settingBack;
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
        parameter.size = 90;
        FreeTypeFontGenerator.FreeTypeFontParameter parameter2 = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter2.size = 60;
        gameFont = gen.generateFont(parameter);
        gameFont.setColor(Color.LIGHT_GRAY);
        settingFont = gen.generateFont(parameter2);
        settingFont.setColor(Color.DARK_GRAY);

        skin = new Skin(Gdx.files.internal("cloud-form/skin/cloud-form-ui.json"));
        stage = new Stage(viewport, spriteBatch);
        Texture img = new Texture("background.jpg");
        background = new Image(img);
        Gdx.input.setInputProcessor(stage);
        TextureRegionDrawable drawable1 = new TextureRegionDrawable(new Texture(Gdx.files.internal("goback1.png")));
        TextureRegionDrawable drawable2 = new TextureRegionDrawable(new Texture(Gdx.files.internal("goback2.png")));
        goBack = new ImageButton(drawable1);
        goBack.getStyle().imageOver = drawable2;

        goBack.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                State goBackMenuState = new MenuState(gsm);
                gsm.pop();
                gsm.push(goBackMenuState);

            }
        });
        goBack.setBounds(0,0,100,100);

        musicOn = new CheckBox(" on", skin,"radio");
        musicOff = new CheckBox(" off", skin,"radio");
        ButtonGroup<CheckBox> musicGroup = new ButtonGroup<>(musicOn, musicOff);
        musicGroup.setMaxCheckCount(1);
        musicGroup.setMinCheckCount(1);
        musicOn.setTransform(true);
        musicOff.setTransform(true);
        musicOn.setScale(1.3f);
        musicOff.setScale(1.3f);
        musicOn.setChecked(musicBoolean);
        musicOff.setChecked(!musicBoolean);
        musicOn.setPosition(400,470);
        musicOff.setPosition(700,470);

        aiOn = new CheckBox(" on", skin,"radio");
        aiOff = new CheckBox(" off", skin,"radio");
        ButtonGroup<CheckBox> aiGroup = new ButtonGroup<>(aiOn, aiOff);
        aiGroup.setMaxCheckCount(1);
        aiGroup.setMinCheckCount(1);
        aiOn.setTransform(true);
        aiOff.setTransform(true);
        aiOn.setScale(1.3f);
        aiOff.setScale(1.3f);
        aiOff.setChecked(true);
        aiOn.setPosition(400,270);
        aiOff.setPosition(700,270);

        Texture img2 = new Texture(Gdx.files.internal("settingBack.png"));
        settingBack = new Image(img2);
        settingBack.setPosition(Gdx.graphics.getWidth()/2f - settingBack.getWidth()/2,
                Gdx.graphics.getHeight()/2f - settingBack.getHeight()/2);
    }

    @Override
    public void update(float dt) {
        handleInput();
    }

    @Override
    public void draw() {

        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.addActor(background);
        stage.addActor(settingBack);
        stage.addActor(goBack);
        stage.addActor(musicOn);
        stage.addActor(musicOff);
        stage.addActor(aiOn);
        stage.addActor(aiOff);

        stage.draw();
        spriteBatch.begin();
        gameFont.draw(spriteBatch,"Settings", 450, 750);
        settingFont.draw(spriteBatch,"Music",530,570);
        settingFont.draw(spriteBatch,"AI",570,370);
        spriteBatch.end();
    }

    @Override
    public void handleInput() {
        if(musicOff.isChecked() && AbaloneGame.music.isPlaying()){
            musicBoolean = !musicBoolean;
            System.out.println(musicBoolean);
            AbaloneGame.music.pause();
        }else if(musicOn.isChecked() && !AbaloneGame.music.isPlaying()){
            musicBoolean = !musicBoolean;
            System.out.println(musicBoolean);
            AbaloneGame.music.play();
        }
    }

    @Override
    public void dispose() {

    }
}
