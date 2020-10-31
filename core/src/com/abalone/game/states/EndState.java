package com.abalone.game.states;

import com.abalone.game.AbaloneGame;
import com.abalone.game.managers.GameStateManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import org.w3c.dom.Text;

public class EndState extends State {
    private Stage stage;
    private SpriteBatch spriteBatch;
    private Skin skin;
    private Image background;
    private TextButton mainMenu;
    private TextButton retry;

    protected EndState(GameStateManager ourGsm) {
        super(ourGsm);
    }

    @Override
    public void init() {
        spriteBatch = new SpriteBatch();
        Viewport viewport = new FitViewport(AbaloneGame.width, AbaloneGame.height, AbaloneGame.cam);
        viewport.apply();
        stage = new Stage(viewport, spriteBatch);
        Gdx.input.setInputProcessor(stage);

        skin = new Skin(Gdx.files.internal("cloud-form/skin/cloud-form-ui.json"));
        stage = new Stage(viewport, spriteBatch);
        mainMenu = new TextButton("Return to Main Menu", skin);
        mainMenu.getColor().set(Color.LIGHT_GRAY);
        retry = new TextButton("Retry", skin);
        retry.getColor().set(Color.LIGHT_GRAY);
        Texture img1 = new Texture("PurpleWon.png");
        Texture img2 = new Texture("BlueWon.png");
        if (PlayState.lostB == 6) {
            background = new Image(img1);
            background.setWidth(AbaloneGame.width);
            background.setHeight(AbaloneGame.height);
        } else if (PlayState.lostP == 6) {
            background = new Image(img2);
            background.setWidth(AbaloneGame.width);
            background.setHeight(AbaloneGame.height);
        }
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void update(float dt) {
        handleInput();
    }

    @Override
    public void draw() {
        Table table = new Table();

        table.add(mainMenu).width(200);
        table.setTransform(true);
        table.bottom();
        table.setPosition(1 / 8, 1 / 300);
        table.row().pad(0, 0, 300, 0);
        table.add(retry).fillX().uniformX();
        table.row();
        table.setFillParent(true);

        stage.addActor(table);
        stage.addActor(background);
        Gdx.gl.glClearColor(.1f, .12f, .16f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }

    @Override
    public void handleInput() {
        if (mainMenu.isPressed()) {
            State mainMenu = new MenuState(gsm);
            gsm.pop();
            gsm.push(mainMenu);
        } else if (retry.isPressed()) {
            State playState = new PlayState(gsm);
            gsm.pop();
            gsm.push(playState);
        }
    }

    @Override
    public void dispose() {

    }
}
