package com.abalone.game.states;

import com.abalone.game.AbaloneGame;
import com.abalone.game.managers.GameStateManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class ExperimentsState extends State {
    private SpriteBatch spriteBatch;
    private Stage stage;
    private Skin skin;
    private ImageButton returnButton;
    private Image background;
    private BitmapFont experimentsFont;
    private BitmapFont aiNameFont;
    private boolean error = true;

    protected ExperimentsState(GameStateManager ourGsm) {
        super(ourGsm);
    }

    @Override
    public void init() {
        spriteBatch = new SpriteBatch();
        Viewport viewport = new FitViewport(AbaloneGame.width, AbaloneGame.height, AbaloneGame.cam);
        viewport.apply();
        FreeTypeFontGenerator gen = new FreeTypeFontGenerator(Gdx.files.internal("Prime-Regular.ttf"));
        Texture img = new Texture("aurora.jpg");
        background = new Image(img);
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 70;
        experimentsFont = gen.generateFont(parameter);
        experimentsFont.setColor(Color.LIGHT_GRAY);
        stage = new Stage(viewport, spriteBatch);
        skin = new Skin(Gdx.files.internal("cloud-form/skin/cloud-form-ui.json"));
        Gdx.input.setInputProcessor(stage);

        returnButton = new ImageButton(new TextureRegionDrawable(new Texture(Gdx.files.internal("returnArrow.png"))),
                new TextureRegionDrawable(new Texture(Gdx.files.internal("returnArrowPressed.png"))));
        returnButton.getImage().setScale(1 / 18f);
        returnButton.setScale(0.5f);
        returnButton.setPosition(15, 730);

        FreeTypeFontGenerator.FreeTypeFontParameter aiName = new FreeTypeFontGenerator.FreeTypeFontParameter();
        aiName.size = 35;
        aiNameFont = gen.generateFont(aiName);
        aiNameFont.setColor(Color.WHITE);

        Table playerOne = new Table();
        Table playerTwo = new Table();
        Table simulations = new Table();
        Table mainTable = new Table();

        Label value_1 = new Label("Heuristics 1: ", skin);
        final TextField playerOneField_1 = new TextField("0", skin);
        Label value_2 = new Label("Heuristics 2: ", skin);
        final TextField playerOneField_2 = new TextField("0", skin);
        Label value_3 = new Label("Heuristics 3: ", skin);
        final TextField playerOneField_3 = new TextField("0", skin);
        Label value_4 = new Label("Heuristics 4: ", skin);
        final TextField playerOneField_4 = new TextField("0", skin);
        Label value_5 = new Label("Heuristics 5: ", skin);
        final TextField playerOneField_5 = new TextField("0", skin);

        Label valueTwo_1 = new Label("Heuristics 1: ", skin);
        Label valueTwo_2 = new Label("Heuristics 2: ", skin);
        Label valueTwo_3 = new Label("Heuristics 3: ", skin);
        Label valueTwo_4 = new Label("Heuristics 4: ", skin);
        Label valueTwo_5 = new Label("Heuristics 5: ", skin);

        final TextField playerTwoField_1 = new TextField("0", skin);
        final TextField playerTwoField_2 = new TextField("0", skin);
        final TextField playerTwoField_3 = new TextField("0", skin);
        final TextField playerTwoField_4 = new TextField("0", skin);
        final TextField playerTwoField_5 = new TextField("0", skin);

        playerOne.row().pad(10,0,10,0);
        playerOne.add(value_1);
        playerOne.add(playerOneField_1).padRight(20);
        playerOne.row().pad(10,0,10,0);
        playerOne.add(value_2);
        playerOne.add(playerOneField_2).padRight(20);
        playerOne.row().pad(10,0,10,0);
        playerOne.add(value_3);
        playerOne.add(playerOneField_3).padRight(20);
        playerOne.row().pad(10,0,10,0);
        playerOne.add(value_4);
        playerOne.add(playerOneField_4).padRight(20);
        playerOne.row().pad(10,0,10,0);
        playerOne.add(value_5);
        playerOne.add(playerOneField_5).padRight(20);

        playerTwo.row().pad(10,0,10,0);
        playerTwo.add(valueTwo_1);
        playerTwo.add(playerTwoField_1).padRight(20);
        playerTwo.row().pad(10,0,10,0);
        playerTwo.add(valueTwo_2);
        playerTwo.add(playerTwoField_2).padRight(20);
        playerTwo.row().pad(10,0,10,0);
        playerTwo.add(valueTwo_3);
        playerTwo.add(playerTwoField_3).padRight(20);
        playerTwo.row().pad(10,0,10,0);
        playerTwo.add(valueTwo_4);
        playerTwo.add(playerTwoField_4).padRight(20);
        playerTwo.row().pad(10,0,10,0);
        playerTwo.add(valueTwo_5);
        playerTwo.add(playerTwoField_5).padRight(20);

        Label numberOfGames = new Label("Number of games : ", skin);
        final TextField nrOfGames = new TextField("0", skin);

        simulations.row().pad(10,0,10,0);
        simulations.add(numberOfGames);
        simulations.row();
        simulations.add(nrOfGames);


        TextButton startButton = new TextButton("Run Experiments", skin);
        ChangeListener listener = new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                float valueOne_1 = 0, valueOne_2 = 0, valueOne_3 = 0, valueOne_4 = 0, valueOne_5 = 0;
                float valueTwo_1 = 0, valueTwo_2 = 0, valueTwo_3 = 0, valueTwo_4 = 0, valueTwo_5 = 0;
                int nrOfSimulations = 0;

                error = playerOneField_1.toString().isEmpty() || playerOneField_2.toString().isEmpty() ||
                        playerOneField_3.toString().isEmpty() || playerOneField_4.toString().isEmpty() ||
                        playerOneField_5.toString().isEmpty() || playerTwoField_1.toString().isEmpty() ||
                        playerTwoField_2.toString().isEmpty() || playerTwoField_3.toString().isEmpty() ||
                        playerTwoField_4.toString().isEmpty() || playerTwoField_5.toString().isEmpty() ||
                        nrOfGames.toString().isEmpty();

                try {
                    //Heuristics values for player 1
                    valueOne_1 = Float.parseFloat(playerOneField_1.getText().replaceAll(" ", ""));
                    valueOne_2 = Float.parseFloat(playerOneField_2.getText().replaceAll(" ", ""));
                    valueOne_3 = Float.parseFloat(playerOneField_3.getText().replaceAll(" ", ""));
                    valueOne_4 = Float.parseFloat(playerOneField_4.getText().replaceAll(" ", ""));
                    valueOne_5 = Float.parseFloat(playerOneField_5.getText().replaceAll(" ", ""));
                    //Heuristics values for player 2
                    valueTwo_1 = Float.parseFloat(playerTwoField_1.getText().replaceAll(" ", ""));
                    valueTwo_2 = Float.parseFloat(playerTwoField_2.getText().replaceAll(" ", ""));
                    valueTwo_3 = Float.parseFloat(playerTwoField_3.getText().replaceAll(" ", ""));
                    valueTwo_4 = Float.parseFloat(playerTwoField_4.getText().replaceAll(" ", ""));
                    valueTwo_5 = Float.parseFloat(playerTwoField_5.getText().replaceAll(" ", ""));
                    //Collect nr of games
                    nrOfSimulations = Integer.parseInt(nrOfGames.getText().replaceAll(" ", ""));

                } catch (Exception e) {
                    System.out.println("Not all values are real!");
                }
                //TODO: Run games here
                if (!error) {
                    System.out.println();
                    System.out.println("MiniMax VALUES ARE :  " + valueOne_1 + " " + valueOne_2 + " " + valueOne_3 + " " +
                            valueOne_4 + " " + valueOne_5);
                    System.out.println();
                    System.out.println("NegaMax VALUES ARE :  " + valueTwo_1 + " " + valueTwo_2 + " " + valueTwo_3 + " " +
                            valueTwo_4 + " " + valueTwo_5);
                    System.out.println();
                    System.out.println("Nr of games wanted : " + nrOfSimulations);
                }
            }
        };
        startButton.addListener(listener);
        mainTable.add(playerOne).fillY().align(Align.left).padRight(300);
        mainTable.add(playerTwo).fillY().align(Align.right);
        mainTable.row();
        mainTable.add(simulations).align(Align.center).padLeft(270);
        mainTable.row().padBottom(-100);
        mainTable.add(startButton).align(Align.center).padLeft(270);
        mainTable.setY(mainTable.getY());
        mainTable.setFillParent(true);

        stage.addActor(background);
        stage.addActor(mainTable);
        stage.addActor(returnButton);

    }

    @Override
    public void update(float dt) {
        handleInput();

    }

    @Override
    public void draw() {
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();

        spriteBatch.begin();
        experimentsFont.draw(spriteBatch,"Experiments Room",330,750);
        aiNameFont.draw(spriteBatch,"MiniMax",295,570);
        aiNameFont.draw(spriteBatch,"NegaMax",838,570);
        spriteBatch.end();
    }

    @Override
    public void handleInput() {
        if (returnButton.isChecked()) {
            State MenuState = new MenuState(gsm);
            gsm.pop();
            gsm.push(MenuState);
        }
    }

    @Override
    public void dispose() {

    }
}
