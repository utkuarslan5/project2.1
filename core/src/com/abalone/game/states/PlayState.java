package com.abalone.game.states;

import com.abalone.game.AbaloneGame;
import com.abalone.game.managers.GameStateManager;
import com.abalone.game.objects.Ball;
import com.abalone.game.objects.Board;
import com.abalone.game.objects.Hex;
import com.abalone.game.objects.HexGrid;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;


import java.util.ArrayList;

public class PlayState extends State {
    private SpriteBatch spriteBatch;
    private Stage stage;
    private Skin skin;
    private Image background;
    private BitmapFont gameFont;
    private Board board;
    private ImageButton[] ballButtons;
    private CheckBox bluePlayer;
    private CheckBox purplePlayer;
    private ButtonGroup<CheckBox> colorSelectPlayer;
    private Label purpleLostBalls;
    private Label blueLostBalls;
    public static int lostP;
    public static int lostB;

    private TextureRegionDrawable ballTextureRegionDrawableBlue;
    private TextureRegionDrawable ballTexturePressedRegionDrawableBlue;
    private TextureRegionDrawable ballTextureRegionDrawablePurple;
    private TextureRegionDrawable ballTexturePressedRegionDrawablePurple;
    private TextureRegionDrawable ballTextureRegionDrawableBlank;
    private TextureRegionDrawable ballTexturePressedRegionDrawableBlank;

    private ImageButton returnButton;

    protected PlayState(GameStateManager ourGsm) {
        super(ourGsm);
    }

    @Override
    public void init() {
        skin = new Skin(Gdx.files.internal("cloud-form/skin/cloud-form-ui.json"));
        spriteBatch = new SpriteBatch();
        board = new Board();
        board.getBoardImage().setPosition(AbaloneGame.width/2f-(board.getBoardImage().getWidth()/2),AbaloneGame.height/2f- (board.getBoardImage().getHeight()/2));
        Viewport viewport = new FitViewport(AbaloneGame.width, AbaloneGame.height, AbaloneGame.cam);
        viewport.apply();
        FreeTypeFontGenerator gen = new FreeTypeFontGenerator(Gdx.files.internal("Prime-Regular.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 80;
        gameFont = gen.generateFont(parameter);
        gameFont.setColor(Color.LIGHT_GRAY);
        stage = new Stage(viewport, spriteBatch);
        Gdx.input.setInputProcessor(stage);

        returnButton = new ImageButton(new TextureRegionDrawable(new Texture(Gdx.files.internal("returnArrow.png"))),
                new TextureRegionDrawable(new Texture(Gdx.files.internal("returnArrowPressed.png"))));
        returnButton.getImage().setScale(1/18f);
        returnButton.setScale(0.5f);
        returnButton.setPosition(15, 730);

        Texture img = new Texture("pbg.jpg");
        background = new Image(img);
        background.setWidth(AbaloneGame.width);
        background.setHeight(AbaloneGame.height);

        bluePlayer = new CheckBox("Blue", skin,"radio");
        bluePlayer.setPosition(1000,600);
        bluePlayer.setTransform(true);
        bluePlayer.setScale(1.5f);
        bluePlayer.getLabel().setFontScale(0.5f);
        bluePlayer.getStyle().fontColor = Color.WHITE;
        bluePlayer.setChecked(true);

        purplePlayer = new CheckBox("Purple", skin,"radio");
        purplePlayer.setPosition(991,550);
        purplePlayer.setTransform(true);
        purplePlayer.setScale(1.5f);
        purplePlayer.getLabel().setFontScale(0.5f);

        colorSelectPlayer = new ButtonGroup<>(bluePlayer, purplePlayer);
        colorSelectPlayer.setMaxCheckCount(1);
        colorSelectPlayer.setMinCheckCount(1);

        lostP = 0;
        lostB = 0;
        purpleLostBalls = new Label("Purple Lost: " + lostP, skin);
        purpleLostBalls.setFontScale(1.5f);
        purpleLostBalls.getStyle().fontColor = Color.WHITE;
        purpleLostBalls.setPosition(530,50);
        blueLostBalls = new Label("Blue Lost: " + lostB, skin);
        blueLostBalls.setFontScale(1.5f);
        blueLostBalls.setPosition(550,700);

        ballTextureRegionDrawableBlue = new TextureRegionDrawable(new Texture(Gdx.files.internal("blue.png")));
        ballTexturePressedRegionDrawableBlue = new TextureRegionDrawable(new Texture(Gdx.files.internal("blueClicked.png")));

        ballTextureRegionDrawablePurple = new TextureRegionDrawable(new Texture(Gdx.files.internal("purple.png")));
        ballTexturePressedRegionDrawablePurple = new TextureRegionDrawable(new Texture(Gdx.files.internal("purpleClicked.png")));

        ballTextureRegionDrawableBlank = new TextureRegionDrawable(new Texture(Gdx.files.internal("blank.png")));
        ballTexturePressedRegionDrawableBlank = new TextureRegionDrawable(new Texture(Gdx.files.internal("blank.png")));

        // TODO: adapt the loop and conditions to the the hexgrid (and not the temporary array)
        ArrayList<Hex> hexList = (ArrayList<Hex>) board.getHexGrid().getHexList();
        ballButtons = new ImageButton[61];
        for(int iHex = 0; iHex < hexList.size(); iHex++) {
            final int index = iHex;
            final Hex hex = hexList.get(iHex);

            TextureRegionDrawable ballTextureRegionDrawable;
            TextureRegionDrawable ballTexturePressedRegionDrawable;

            if(hexList.get(iHex).getBall() != null && hexList.get(iHex).getBall().getColor().isBlue()) {
                ballTextureRegionDrawable = ballTextureRegionDrawableBlue;
                ballTexturePressedRegionDrawable = ballTexturePressedRegionDrawableBlue;
            }
            else if(hexList.get(iHex).getBall() != null && hexList.get(iHex).getBall().getColor().isPurple()) {
                ballTextureRegionDrawable = ballTextureRegionDrawablePurple;
                ballTexturePressedRegionDrawable = ballTexturePressedRegionDrawablePurple;
            }
            else {
                ballTextureRegionDrawable = ballTextureRegionDrawableBlank;
                ballTexturePressedRegionDrawable = ballTexturePressedRegionDrawableBlank;
            }

            ImageButton ballButton = new ImageButton(
                ballTextureRegionDrawable,
                ballTextureRegionDrawable,
                ballTexturePressedRegionDrawable
            );

            ballButton.addListener(new ClickListener() {
                public void clicked(InputEvent event, float x, float y) {
                    Ball ball = board.getHexGrid().getHexList().get(index).getBall();
                    System.out.println("Ball " + index + " clicked " + ball.getColor() + " x:" + hex.getX() + " y:" + hex.getY() + " z:" + hex.getZ());
                    // doesn't allow empty balls to be selected
                    if(!ball.getColor().isBlank()) {
                        if(ball.getColor().isBlue() == (colorSelectPlayer.getCheckedIndex() == 0) ||
                                (ball.getColor().isPurple() == (colorSelectPlayer.getCheckedIndex() == 1)) ) {
                            board.selectBall(ball);
                        }
                        else if(board.getSelected().size()>1){
                            ballButtons[index].setChecked(false);
                            System.out.println("PUSH NEEDED");
                            board.pushBalls(ball);
                            if(board.getMovePerformed()) {
                                switchTurnPlayer();
                                board.setMovePerformed(false);
                            }
                        }
                        else {
                            ballButtons[index].setChecked(false);

                        }
                    }
                    else if(ball.getColor().isBlank()) {
                        board.setIsModified();
                        if(board.getSelected().size() > 0) {
                            board.move(ball);
                            if(board.getMovePerformed()) {
                                switchTurnPlayer();
                                board.setMovePerformed(false);
                            }
                        }
                    }
                }
            });

            ballTextureRegionDrawable.setMinSize(50, 50);
            ballTexturePressedRegionDrawable.setMinSize(50, 50);
            ballButtons[iHex] = ballButton;
        }
    }

    @Override
    public void update(float dt) {
        ArrayList<Ball> selectedList = board.getSelected();
        assert selectedList != null;

        // doesn't allow more than 3 balls to be selected
        if(selectedList.size() > 3) {
            for (Ball ball : selectedList) {
                ballButtons[ball.getId()].setChecked(false);
            }
            selectedList.clear();
        }

        Ball curr = null;
        Ball first = null;
        if(selectedList.size()!=0){
            curr = selectedList.get(selectedList.size() - 1);
            first = selectedList.get(0);
        }

        if(curr != first){
            if (curr.getColor() != first.getColor()){
                for (Ball ball : selectedList){
                    ballButtons[ball.getId()].setChecked(false);
                }
                selectedList.clear();
            }
        }

        if (returnButton.isChecked()){
            State MenuState = new MenuState(gsm);
            gsm.pop();
            gsm.push(MenuState);
        }
        purpleLostBalls.setText("Purple Lost: " + lostP);
        blueLostBalls.setText("Blue Lost: " + lostB);

    }

    @Override
    public void draw() {
        stage.addActor(background);

        stage.addActor(board.getBoardImage());
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));

        ArrayList<Hex> hexList = (ArrayList<Hex>) board.getHexGrid().getHexList();
        int c = 1;
        float temp = -4;

        for(int i = 0; i < 61; i++) {
            Hex hex = hexList.get(i);
            float x = hex.getX();
            float y = hex.getZ();
            if(temp != y) {
                c++;
            }
            x += c * 0.5;
            ballButtons[i].setBounds((x*53 +445),y*48 +380,43,43);

            // Update ballButton color
            if(hex.getBall() != null && hex.getBall().getColor().isBlue()) {
                ballButtons[i].getStyle().imageUp = ballTextureRegionDrawableBlue;
                ballButtons[i].getStyle().imageDown = ballTextureRegionDrawableBlue;
                ballButtons[i].getStyle().imageChecked = ballTexturePressedRegionDrawableBlue;
            }
            else if(hex.getBall() != null && hex.getBall().getColor().isPurple()) {
                ballButtons[i].getStyle().imageUp = ballTextureRegionDrawablePurple;
                ballButtons[i].getStyle().imageDown = ballTextureRegionDrawablePurple;
                ballButtons[i].getStyle().imageChecked = ballTexturePressedRegionDrawablePurple;
            }
            else {
                ballButtons[i].getStyle().imageUp = ballTextureRegionDrawableBlank;
                ballButtons[i].getStyle().imageDown = ballTextureRegionDrawableBlank;
                ballButtons[i].getStyle().imageChecked = ballTextureRegionDrawableBlank;
            }
            stage.addActor(ballButtons[i]);
            temp = y;
        }
        board.setIsModified(false);

        stage.addActor(bluePlayer);
        stage.addActor(purplePlayer);
        stage.addActor(returnButton);
        stage.addActor(purpleLostBalls);
        stage.addActor(blueLostBalls);
        stage.draw();
    }

    @Override
    public void handleInput() {

    }

    @Override
    public void dispose() {

    }

    public void switchTurnPlayer() {
        for(int i = 0; i < 61 ; i++) {
            ballButtons[i].setChecked(false);
        }
        if(purplePlayer.isChecked()) {
            bluePlayer.setChecked(true);
        }
        else {
            purplePlayer.setChecked(true);
        }
    }
}
