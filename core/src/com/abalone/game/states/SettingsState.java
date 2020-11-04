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
    private BitmapFont gameFontSetting;
    private BitmapFont gameFontMusic;
    private BitmapFont gameFontSubPoints;
    private static ImageButton returnButton;
    private static ImageButton music1Checked;
    private static ImageButton music2Checked;
    private static ImageButton musicOnOff;
    private static ImageButton aiChecked;

    protected SettingsState(GameStateManager ourGsm) {
        super(ourGsm);
    }

    @Override
    public void init() {
        spriteBatch = new SpriteBatch();
        Viewport viewport = new FitViewport(AbaloneGame.width, AbaloneGame.height, AbaloneGame.cam);
        viewport.apply();
        FreeTypeFontGenerator gen = new FreeTypeFontGenerator(Gdx.files.internal("Prime-Regular.ttf"));
        stage = new Stage(viewport, spriteBatch);
        Gdx.input.setInputProcessor(stage);

        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 120;
        gameFontSetting = gen.generateFont(parameter);
        gameFontSetting.setColor(Color.LIGHT_GRAY);

        FreeTypeFontGenerator.FreeTypeFontParameter parameter2 = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter2.size = 50;
        gameFontMusic = gen.generateFont(parameter2);
        gameFontMusic.setColor(Color.WHITE);

        FreeTypeFontGenerator.FreeTypeFontParameter parameter3 = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter3.size = 25;
        gameFontSubPoints = gen.generateFont(parameter3);
        gameFontSubPoints.setColor(Color.WHITE);

        skin = new Skin(Gdx.files.internal("cloud-form/skin/cloud-form-ui.json"));
        stage = new Stage(viewport, spriteBatch);
        Texture img = new Texture("aurora.jpg");
        background = new Image(img);
        Gdx.input.setInputProcessor(stage);

        returnButton = new ImageButton(new TextureRegionDrawable(new Texture(Gdx.files.internal("returnArrow.png"))),
                new TextureRegionDrawable(new Texture(Gdx.files.internal("returnArrowPressed.png"))));
        returnButton.getImage().setScale(1 / 18f);
        returnButton.setScale(0.5f);
        returnButton.setPosition(15, 730);

        music1Checked = new ImageButton(new TextureRegionDrawable(new Texture(Gdx.files.internal("checkedCheckBox.png"))),
                new TextureRegionDrawable(new Texture(Gdx.files.internal("checkedCheckBox.png"))),
                new TextureRegionDrawable(new Texture(Gdx.files.internal("uncheckedCheckBox.png"))));
        music1Checked.getImage().setScale(1 / 20f);
        music1Checked.setScale(0.1f);
        music1Checked.setPosition(300, 380);

        music2Checked = new ImageButton(new TextureRegionDrawable(new Texture(Gdx.files.internal("checkedCheckBox.png"))),
                new TextureRegionDrawable(new Texture(Gdx.files.internal("checkedCheckBox.png"))),
                new TextureRegionDrawable(new Texture(Gdx.files.internal("uncheckedCheckBox.png"))));
        music2Checked.getImage().setScale(1 / 20f);
        music2Checked.setScale(0.1f);
        music2Checked.setPosition(300, 310);

        musicOnOff = new ImageButton(new TextureRegionDrawable(new Texture(Gdx.files.internal("checkedCheckBox.png"))),
                new TextureRegionDrawable(new Texture(Gdx.files.internal("checkedCheckBox.png"))),
                new TextureRegionDrawable(new Texture(Gdx.files.internal("uncheckedCheckBox.png"))));
        musicOnOff.getImage().setScale(1 / 20f);
        musicOnOff.setScale(0.1f);
        musicOnOff.setPosition(300, 240);

        if(AbaloneGame.music1.isPlaying() || AbaloneGame.music2.isPlaying()){
            musicOnOff.setChecked(true);
            music1Checked.setChecked(!AbaloneGame.music1.isPlaying());
            music2Checked.setChecked(!AbaloneGame.music2.isPlaying());
        }else if(!AbaloneGame.music1.isPlaying() && !AbaloneGame.music2.isPlaying()){
            musicOnOff.setChecked(false);
            music1Checked.setChecked(!musicOnOff.isChecked());
            music2Checked.setChecked(!musicOnOff.isChecked());
        }


        aiChecked = new ImageButton(new TextureRegionDrawable(new Texture(Gdx.files.internal("checkedCheckBox.png"))),
                new TextureRegionDrawable(new Texture(Gdx.files.internal("checkedCheckBox.png"))),
                new TextureRegionDrawable(new Texture(Gdx.files.internal("uncheckedCheckBox.png"))));
        aiChecked.getImage().setScale(1 / 20f);
        aiChecked.setScale(0.1f);
        aiChecked.setPosition(790, 380);

    }

    @Override
    public void update(float dt) {
        handleInput();
        if (returnButton.isChecked()) {
            State MenuState = new MenuState(gsm);
            gsm.pop();
            gsm.push(MenuState);
        }
    }

    @Override
    public void draw() {

        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.addActor(background);
        stage.addActor(returnButton);
        stage.addActor(music1Checked);
        stage.addActor(music2Checked);
        stage.addActor(musicOnOff);
        stage.addActor(aiChecked);

        stage.draw();
        spriteBatch.begin();
        gameFontSetting.draw(spriteBatch, "Settings", 390, 750);
        gameFontMusic.draw(spriteBatch, "Music:", 280, 490);
        gameFontMusic.draw(spriteBatch, "AI:", 740, 490);
        gameFontSubPoints.draw(spriteBatch, "Theme 1", 360, 405);
        gameFontSubPoints.draw(spriteBatch, "Theme 2", 360, 335);
        gameFontSubPoints.draw(spriteBatch, "On/Off", 850, 405);
        gameFontSubPoints.draw(spriteBatch, "Mute", 360, 265);
        spriteBatch.end();
    }

    @Override
    public void handleInput() {

        if (!musicOnOff.isChecked() && (!music1Checked.isChecked() || !music2Checked.isChecked()) &&
                (AbaloneGame.music1.isPlaying() || AbaloneGame.music2.isPlaying())) {
            AbaloneGame.music1.pause();
            AbaloneGame.music2.pause();
            music2Checked.setChecked(true);
            music1Checked.setChecked(true);
        }

        if (music1Checked.isChecked() && AbaloneGame.music1.isPlaying()) {
            AbaloneGame.music1.pause();
            music2Checked.setChecked(false);
            musicOnOff.setChecked(true);
        } else if (!music1Checked.isChecked() && !AbaloneGame.music1.isPlaying()) {
            AbaloneGame.music1.play();
            music2Checked.setChecked(true);
            musicOnOff.setChecked(true);
        } else if (music2Checked.isChecked() && AbaloneGame.music2.isPlaying()) {
            AbaloneGame.music2.pause();
            music1Checked.setChecked(false);
            musicOnOff.setChecked(true);
        } else if (!music2Checked.isChecked() && !AbaloneGame.music2.isPlaying() ) {
            AbaloneGame.music2.play();
            music1Checked.setChecked(true);
            musicOnOff.setChecked(true);
        }

    }

    @Override
    public void dispose() {

    }
}
