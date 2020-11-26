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
    private Image background;
    private BitmapFont gameFontSetting;
    private BitmapFont gameFontMusic;
    private BitmapFont gameFontSubPoints;
    private static ImageButton returnButton;
    private static ImageButton music1Checked;
    private static ImageButton music2Checked;
    private static ImageButton musicOnOff;
    public static ImageButton miniMaxChecked;
    public static ImageButton negaMaxChecked;
    public static Table playerOne;
    public static Image blueBall;
    public static Image purpleBall;
    public static SelectBox playerTypeSelectBox1;
    public static SelectBox AISelectBox1;
    public static SelectBox playerTypeSelectBox2;
    public static SelectBox AISelectBox2;
    private boolean help;

    protected SettingsState(GameStateManager ourGsm) {
        super(ourGsm);
    }

    @Override
    public void init() {
        help = true;
        Texture unchecked = new Texture(Gdx.files.internal("checkedCheckBox.png"));
        Texture checked = new Texture(Gdx.files.internal("uncheckedCheckBox.png"));
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

        Skin skin = new Skin(Gdx.files.internal("cloud-form/skin/cloud-form-ui.json"));
        stage = new Stage(viewport, spriteBatch);
        Texture img = new Texture("aurora.jpg");
        background = new Image(img);
        Gdx.input.setInputProcessor(stage);

        returnButton = new ImageButton(new TextureRegionDrawable(new Texture(Gdx.files.internal("returnArrow.png"))),
                new TextureRegionDrawable(new Texture(Gdx.files.internal("returnArrowPressed.png"))));
        returnButton.getImage().setScale(1 / 18f);
        returnButton.setScale(0.5f);
        returnButton.setPosition(15, 730);

        music1Checked = new ImageButton(new TextureRegionDrawable(unchecked),
                new TextureRegionDrawable(unchecked),
                new TextureRegionDrawable(checked));
        music1Checked.getImage().setScale(1 / 20f);
        music1Checked.setScale(0.1f);
        music1Checked.setPosition(280, 380);

        music2Checked = new ImageButton(new TextureRegionDrawable(unchecked),
                new TextureRegionDrawable(unchecked),
                new TextureRegionDrawable(checked));
        music2Checked.getImage().setScale(1 / 20f);
        music2Checked.setScale(0.1f);
        music2Checked.setPosition(280, 310);

        musicOnOff = new ImageButton(new TextureRegionDrawable(unchecked),
                new TextureRegionDrawable(unchecked),
                new TextureRegionDrawable(checked));
        musicOnOff.getImage().setScale(1 / 20f);
        musicOnOff.setScale(0.1f);
        musicOnOff.setPosition(280, 240);

        if(AbaloneGame.music1.isPlaying() || AbaloneGame.music2.isPlaying()){
            musicOnOff.setChecked(true);
            music1Checked.setChecked(!AbaloneGame.music1.isPlaying());
            music2Checked.setChecked(!AbaloneGame.music2.isPlaying());
        }else if(!AbaloneGame.music1.isPlaying() && !AbaloneGame.music2.isPlaying()){
            musicOnOff.setChecked(false);
            music1Checked.setChecked(!musicOnOff.isChecked());
            music2Checked.setChecked(!musicOnOff.isChecked());
        }

        miniMaxChecked = new ImageButton(new TextureRegionDrawable(unchecked),
                new TextureRegionDrawable(unchecked),
                new TextureRegionDrawable(checked));
        miniMaxChecked.getImage().setScale(1 / 20f);
        miniMaxChecked.setScale(0.1f);
        miniMaxChecked.setPosition(790, 180);
        miniMaxChecked.setChecked(true);

        negaMaxChecked = new ImageButton(new TextureRegionDrawable(unchecked),
                new TextureRegionDrawable(unchecked),
                new TextureRegionDrawable(checked));
        negaMaxChecked.getImage().setScale(1 / 20f);
        negaMaxChecked.setScale(0.1f);
        negaMaxChecked.setPosition(790, 110);
        negaMaxChecked.setChecked(true);

        playerOne = new Table();

        String[] playerTypeChoices = new String[2];
        playerTypeChoices[0] = "Human";
        playerTypeChoices[1] = "AI";

        String[] AIChoices = new String[2];
        AIChoices[0] = "Minimax";
        AIChoices[1] = "Negamax";

        // Player 1 / Blue
        blueBall = new Image(new Texture("blue.png"));
        blueBall.setSize(30, 30);
        blueBall.setPosition(740, 380);

        Label playerTypeLabel1 = new Label("Human/AI: ", skin);
        playerTypeSelectBox1 = new SelectBox<String>(skin);
        playerTypeSelectBox1.setItems(playerTypeChoices);

        Label AILabel1 = new Label("AI: ", skin);
        AISelectBox1 = new SelectBox<String>(skin);
        AISelectBox1.setItems(AIChoices);
        AISelectBox1.setDisabled(true);
        AISelectBox1.setColor(Color.GRAY);

        // Player 2 / Purple
        purpleBall = new Image(new Texture("purple.png"));
        purpleBall.setSize(30, 30);
        purpleBall.setPosition(740, 310);

        Label playerTypeLabel2 = new Label("Human/AI: ", skin);
        playerTypeSelectBox2 = new SelectBox<String>(skin);
        playerTypeSelectBox2.setItems(playerTypeChoices);

        Label AILabel2 = new Label("AI: ", skin);
        AISelectBox2 = new SelectBox<String>(skin);
        AISelectBox2.setItems(AIChoices);
        AISelectBox2.setDisabled(true);
        AISelectBox2.setColor(Color.GRAY);


        // Player 1 / Blue
        playerOne.row().pad(25,0,25,0);
        playerOne.add(playerTypeLabel1);
        playerOne.add(playerTypeSelectBox1).padRight(20);
        playerOne.add(AILabel1);
        playerOne.add(AISelectBox1).padRight(20);
        // Player 2 / Purple
        playerOne.row().pad(25,0,25,0);
        playerOne.add(playerTypeLabel2);
        playerOne.add(playerTypeSelectBox2).padRight(20);
        playerOne.add(AILabel2);
        playerOne.add(AISelectBox2).padRight(20);

        playerOne.setPosition(930, 360);

        System.out.println(negaMaxChecked.isChecked());
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
        stage.addActor(miniMaxChecked);
        stage.addActor(negaMaxChecked);
        stage.addActor(playerOne);
        stage.addActor(blueBall);
        stage.addActor(purpleBall);

        stage.draw();
        spriteBatch.begin();
        gameFontSetting.draw(spriteBatch, "Settings", 390, 750);
        gameFontMusic.draw(spriteBatch, "Music:", 280, 490);
        gameFontMusic.draw(spriteBatch, "Players:", 740, 490);
        gameFontSubPoints.draw(spriteBatch, "Theme 1", 340, 405);
        gameFontSubPoints.draw(spriteBatch, "Theme 2", 340, 335);
        gameFontSubPoints.draw(spriteBatch, "Mute", 340, 265);
        gameFontSubPoints.draw(spriteBatch, "MiniMax", 850, 205);
        gameFontSubPoints.draw(spriteBatch, "NegaMax", 850, 135);
        spriteBatch.end();
    }

    @Override
    public void handleInput() {
        switch((String)playerTypeSelectBox1.getSelected()) {
            case "Human":
                AISelectBox1.setDisabled(true);
                AISelectBox1.setColor(Color.GRAY);
                break;
            case "AI":
                AISelectBox1.setDisabled(false);
                AISelectBox1.setColor(Color.WHITE);
                break;
        }

        switch((String)playerTypeSelectBox2.getSelected()) {
            case "Human":
                AISelectBox2.setDisabled(true);
                AISelectBox2.setColor(Color.GRAY);
                break;
            case "AI":
                AISelectBox2.setDisabled(false);
                AISelectBox2.setColor(Color.WHITE);
                break;
        }

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
        aiHandleInput(this.help);
    }

    public void aiHandleInput(boolean help) {
        if(!negaMaxChecked.isChecked() && help){
            miniMaxChecked.setChecked(true);
            this.help = false;
        }
        if(!miniMaxChecked.isChecked() && !help) {
            negaMaxChecked.setChecked(true);
            this.help = true;
        }
    }

    @Override
    public void dispose() {
    }
}