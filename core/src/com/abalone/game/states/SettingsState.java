package com.abalone.game.states;

import com.abalone.game.AbaloneGame;
import com.abalone.game.managers.GameStateManager;
import com.abalone.game.utils.AI;
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
    public static Table playerOne;
    public static Image blueBall;
    public static Image purpleBall;
    public static SelectBox bluePlayerTypeSelectBox;
    public static SelectBox blueAISelectBox;
    public static SelectBox purplePlayerTypeSelectBox;
    public static SelectBox purpleAISelectBox;

    protected SettingsState(GameStateManager ourGsm) {
        super(ourGsm);
    }

    @Override
    public void init() {
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

        playerOne = new Table();

        String[] playerTypeChoices = new String[2];
        playerTypeChoices[0] = "Human";
        playerTypeChoices[1] = "AI";

        String[] AIChoices = new String[3];
        AIChoices[0] = "Minimax";
        AIChoices[1] = "Negamax";
        AIChoices[2] = "MCTS";

        // Blue Player
        blueBall = new Image(new Texture("blue.png"));
        blueBall.setSize(30, 30);
        blueBall.setPosition(740, 380);

        Label bluePlayerTypeLabel = new Label("Human/AI: ", skin);
        bluePlayerTypeSelectBox = new SelectBox<String>(skin);
        bluePlayerTypeSelectBox.setItems(playerTypeChoices);

        Label blueAILabel = new Label("AI: ", skin);
        blueAISelectBox = new SelectBox<String>(skin);
        blueAISelectBox.setItems(AIChoices);
        blueAISelectBox.setDisabled(true);
        blueAISelectBox.setColor(Color.GRAY);
        if(AbaloneGame.isBluePlayerAI) {
            bluePlayerTypeSelectBox.setSelected("AI");
            blueAISelectBox.setDisabled(false);
        }
        else {
            bluePlayerTypeSelectBox.setSelected("Human");
            blueAISelectBox.setDisabled(true);
        }
        switch(AbaloneGame.bluePlayerAI) {
            case MINIMAX:
                blueAISelectBox.setSelected("Minimax");
                break;
            case NEGAMAX:
                blueAISelectBox.setSelected("Negamax");
                break;
            case MCTS:
                blueAISelectBox.setSelected("MCTS");
                break;
        }

        // Purple Player
        purpleBall = new Image(new Texture("purple.png"));
        purpleBall.setSize(30, 30);
        purpleBall.setPosition(740, 310);

        Label purplePlayerTypeLabel = new Label("Human/AI: ", skin);
        purplePlayerTypeSelectBox = new SelectBox<String>(skin);
        purplePlayerTypeSelectBox.setItems(playerTypeChoices);

        Label purpleAILabel = new Label("AI: ", skin);
        purpleAISelectBox = new SelectBox<String>(skin);
        purpleAISelectBox.setItems(AIChoices);
        purpleAISelectBox.setDisabled(true);
        purpleAISelectBox.setColor(Color.GRAY);
        if(AbaloneGame.isPurplePlayerAI) {
            purplePlayerTypeSelectBox.setSelected("AI");
            purpleAISelectBox.setDisabled(false);
        }
        else {
            purplePlayerTypeSelectBox.setSelected("Human");
            purpleAISelectBox.setDisabled(true);
        }
        switch(AbaloneGame.purplePlayerAI) {
            case MINIMAX:
                purpleAISelectBox.setSelected("Minimax");
                break;
            case NEGAMAX:
                purpleAISelectBox.setSelected("Negamax");
                break;
            case MCTS:
                purpleAISelectBox.setSelected("MCTS");
                break;
        }

        // Blue Player
        playerOne.row().pad(25,0,25,0);
        playerOne.add(bluePlayerTypeLabel);
        playerOne.add(bluePlayerTypeSelectBox).padRight(20);
        playerOne.add(blueAILabel);
        playerOne.add(blueAISelectBox).padRight(20);
        // Purple Player
        playerOne.row().pad(25,0,25,0);
        playerOne.add(purplePlayerTypeLabel);
        playerOne.add(purplePlayerTypeSelectBox).padRight(20);
        playerOne.add(purpleAILabel);
        playerOne.add(purpleAISelectBox).padRight(20);

        playerOne.setPosition(930, 360);
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
        spriteBatch.end();
    }

    @Override
    public void handleInput() {
        if(bluePlayerIsAI()) {
            AbaloneGame.isBluePlayerAI = true;
            blueAISelectBox.setColor(Color.WHITE);
            blueAISelectBox.setDisabled(false);
            switch(getBlueAI()) {
                case MINIMAX:
                    AbaloneGame.bluePlayerAI = AI.MINIMAX;
                    break;
                case NEGAMAX:
                    AbaloneGame.bluePlayerAI = AI.NEGAMAX;
                    break;
            }
        }
        else {
            AbaloneGame.isBluePlayerAI = false;
            blueAISelectBox.setColor(Color.GRAY);
            blueAISelectBox.setDisabled(true);
        }

        if(purplePlayerIsAI()) {
            AbaloneGame.isPurplePlayerAI = true;
            purpleAISelectBox.setColor(Color.WHITE);
            purpleAISelectBox.setDisabled(false);
            switch(getPurpleAI()) {
                case MINIMAX:
                    AbaloneGame.purplePlayerAI = AI.MINIMAX;
                    break;
                case NEGAMAX:
                    AbaloneGame.purplePlayerAI = AI.NEGAMAX;
                    break;
            }
        }
        else {
            AbaloneGame.isPurplePlayerAI = false;
            purpleAISelectBox.setColor(Color.GRAY);
            purpleAISelectBox.setDisabled(true);
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
    }

    @Override
    public void dispose() {
    }

    public static boolean bluePlayerIsAI() {
        return (bluePlayerTypeSelectBox.getSelected() == "AI");
    }

    public static boolean purplePlayerIsAI() {
        return (purplePlayerTypeSelectBox.getSelected() == "AI");
    }

    public static AI getBlueAI() {
        AI value;
        switch ((String) blueAISelectBox.getSelected()) {
            case "Minimax":
                value = AI.MINIMAX;
            break;
            case "Negamax":
                value = AI.NEGAMAX;
            break;
            default:
                value = AI.MINIMAX;
        }
        return value;
    }

    public static AI getPurpleAI() {
        AI value;
        switch ((String) purpleAISelectBox.getSelected()) {
            case "Minimax":
                value = AI.MINIMAX;
                break;
            case "Negamax":
                value = AI.NEGAMAX;
                break;
            default:
                value = AI.MINIMAX;
        }
        return value;
    }
}