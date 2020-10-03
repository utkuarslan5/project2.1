package com.abalone.game.states;

import com.abalone.game.AbaloneGame;
import com.abalone.game.managers.GameStateManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
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
    private ImageButton returnButton;
    private ImageButton Music1Checked;
    private ImageButton Music2Checked;
    private ImageButton MusicOnOff;
    private ImageButton AiChecked;

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
        returnButton.getImage().setScale(1/18f);
        returnButton.setScale(0.5f);
        returnButton.setPosition(15, 730);

        Music1Checked = new ImageButton(new TextureRegionDrawable(new Texture(Gdx.files.internal("checkedCheckBox.png"))),
                new TextureRegionDrawable(new Texture(Gdx.files.internal("checkedCheckBox.png"))),
                new TextureRegionDrawable(new Texture(Gdx.files.internal("uncheckedCheckBox.png"))));
        Music1Checked.getImage().setScale(1/20f);
        Music1Checked.setScale(0.1f);
        Music1Checked.setPosition(300, 380);

        Music2Checked = new ImageButton(new TextureRegionDrawable(new Texture(Gdx.files.internal("checkedCheckBox.png"))),
                new TextureRegionDrawable(new Texture(Gdx.files.internal("checkedCheckBox.png"))),
                new TextureRegionDrawable(new Texture(Gdx.files.internal("uncheckedCheckBox.png"))));
        Music2Checked.getImage().setScale(1/20f);
        Music2Checked.setScale(0.1f);
        Music2Checked.setPosition(300, 310);
        Music2Checked.setChecked(true);

        MusicOnOff = new ImageButton(new TextureRegionDrawable(new Texture(Gdx.files.internal("checkedCheckBox.png"))),
                new TextureRegionDrawable(new Texture(Gdx.files.internal("checkedCheckBox.png"))),
                new TextureRegionDrawable(new Texture(Gdx.files.internal("uncheckedCheckBox.png"))));
        MusicOnOff.getImage().setScale(1/20f);
        MusicOnOff.setScale(0.1f);
        MusicOnOff.setPosition(300, 240);
        MusicOnOff.setChecked(true);

        AiChecked = new ImageButton(new TextureRegionDrawable(new Texture(Gdx.files.internal("checkedCheckBox.png"))),
                new TextureRegionDrawable(new Texture(Gdx.files.internal("checkedCheckBox.png"))),
                new TextureRegionDrawable(new Texture(Gdx.files.internal("uncheckedCheckBox.png"))));
        AiChecked.getImage().setScale(1/20f);
        AiChecked.setScale(0.1f);
        AiChecked.setPosition(790, 380);

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
        stage.addActor(Music1Checked);
        stage.addActor(Music2Checked);
        stage.addActor(MusicOnOff);
        stage.addActor(AiChecked);

        stage.draw();
        spriteBatch.begin();
        gameFontSetting.draw(spriteBatch,"Settings", 390, 750);
        gameFontMusic.draw(spriteBatch,"Music:",280,490);
        gameFontMusic.draw(spriteBatch,"AI:",740,490);
        gameFontSubPoints.draw(spriteBatch, "Theme 1", 360, 405);
        gameFontSubPoints.draw(spriteBatch, "Theme 2", 360, 335);
        gameFontSubPoints.draw(spriteBatch, "On/Off", 850, 405);
        gameFontSubPoints.draw(spriteBatch, "On/Off", 360, 265);
        spriteBatch.end();
    }

    @Override
    public void handleInput() {

        if(!MusicOnOff.isChecked() && (!Music1Checked.isChecked() || !Music2Checked.isChecked()) &&
                (AbaloneGame.music1.isPlaying() || AbaloneGame.music2.isPlaying())){
            AbaloneGame.music1.pause();
            AbaloneGame.music2.pause();
            Music2Checked.setChecked(true);
            Music1Checked.setChecked(true);
        }

        if(Music1Checked.isChecked() && AbaloneGame.music1.isPlaying()){
            AbaloneGame.music1.pause();
            Music2Checked.setChecked(false);
            MusicOnOff.setChecked(true);
        }else if(!Music1Checked.isChecked() && !AbaloneGame.music1.isPlaying()){
            AbaloneGame.music1.play();
            Music2Checked.setChecked(true);
            MusicOnOff.setChecked(true);
        }

        if(Music2Checked.isChecked() && AbaloneGame.music2.isPlaying()){
            AbaloneGame.music2.pause();
            Music1Checked.setChecked(false);
            MusicOnOff.setChecked(true);
        } else if(!Music2Checked.isChecked() && !AbaloneGame.music2.isPlaying()){
            AbaloneGame.music2.play();
            Music1Checked.setChecked(true);
            MusicOnOff.setChecked(true);
        }

    }

    @Override
    public void dispose() {

    }
}
