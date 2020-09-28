package com.abalone.game.states;

import com.abalone.game.AbaloneGame;
import com.abalone.game.managers.GameStateManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class MenuState extends State {
    private SpriteBatch spriteBatch;
    private Stage stage;
    private Skin skin;
    private Image background;
    private BitmapFont gameFont;
    private TextButton playGame;
    private TextButton settingsMenu;
    private TextButton exit;


    public MenuState(GameStateManager gsm) {
        super(gsm);
    }

    @Override
    public void init() {
        spriteBatch = new SpriteBatch();
        Viewport viewport = new FitViewport(AbaloneGame.width, AbaloneGame.height, AbaloneGame.cam);
        viewport.apply();
        FreeTypeFontGenerator gen = new FreeTypeFontGenerator(Gdx.files.internal("Prime-Regular.ttf"));
        Texture img = new Texture("background.jpg");
        background = new Image(img);
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 110;
        gameFont = gen.generateFont(parameter);
        gameFont.setColor(Color.LIGHT_GRAY);
        stage = new Stage(viewport, spriteBatch);
        skin = new Skin(Gdx.files.internal("cloud-form/skin/cloud-form-ui.json"));
        Gdx.input.setInputProcessor(stage);
        playGame = new TextButton("Play", skin);
        settingsMenu = new TextButton("Settings", skin);
        exit = new TextButton("Exit", skin);
    }

    @Override
    public void update(float dt) {
        handleInput();
    }

    @Override
    public void draw() {
        Table table = new Table();

        table.add(playGame).width(250);
        table.row().pad(20, 0, 20, 0);
        table.add(settingsMenu).fillX().uniformX();
        table.row();
        table.add(exit).fillX().uniformX();

        table.setFillParent(true);
        table.setTransform(true);
        stage.addActor(background);

        stage.addActor(table);

        stage.addActor(background);
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
        spriteBatch.begin();
        gameFont.draw(spriteBatch,"Abalone", 400, 750);
        spriteBatch.end();
    }

    @Override
    public void handleInput() {
        if(playGame.isPressed()){
            State gameState = new PlayState(gsm);
            gsm.pop();
            gsm.push(gameState);
        }
        else if(settingsMenu.isPressed()){
            State settingsState = new SettingsState(gsm);
            gsm.pop();
            gsm.push(settingsState);
        }
        else if(exit.isPressed()){
            Gdx.app.exit();
        }
    }


    @Override
    public void dispose() {

    }
}
