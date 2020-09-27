package com.abalone.game.states;

import com.abalone.game.AbaloneGame;
import com.abalone.game.managers.GameStateManager;
import com.abalone.game.objects.Ball;
import com.abalone.game.objects.Board;
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


import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.EventListener;

public class PlayState extends State {
    private SpriteBatch spriteBatch;
    private Stage stage;
    private Skin skin;
    private Image background;
    private BitmapFont gameFont;
    private Board board;
    private ImageButton[] balls;
    private CheckBox bluePlayer;
    private CheckBox purplePlayer;
    private ButtonGroup<CheckBox> colorSelectPlayer;

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

        // TODO: adapt the loop and conditions to the the hexgrid (and not the temporary array)
        Ball[] grid = board.getBalls();
        balls = new ImageButton[61];
        for(int iBall = 0; iBall < grid.length; iBall++) {
            final int index = iBall;

            Texture ballTexture;
            Texture ballTexturePressed;
            TextureRegionDrawable ballTextureRegionDrawable;
            TextureRegionDrawable ballTexturePressedRegionDrawable;

            if(grid[iBall] != null && grid[iBall].getColor().isBlue()) {
                ballTexture = new Texture(Gdx.files.internal("purple.png"));
                ballTexturePressed = new Texture(Gdx.files.internal("purpleClicked.png"));
            }
            else if(grid[iBall] != null && grid[iBall].getColor().isPurple()) {
                ballTexture = new Texture(Gdx.files.internal("blue.png"));
                ballTexturePressed = new Texture(Gdx.files.internal("blueClicked.png"));
            }
            else {
                ballTexture = new Texture(Gdx.files.internal("blank.png"));
                ballTexturePressed = new Texture(Gdx.files.internal("blank.png"));
            }

            ballTextureRegionDrawable = new TextureRegionDrawable(new TextureRegion(ballTexture));
            ballTexturePressedRegionDrawable = new TextureRegionDrawable(new TextureRegion(ballTexturePressed));
            ImageButton ball = new ImageButton(
                    ballTextureRegionDrawable,
                    ballTextureRegionDrawable,
                    ballTexturePressedRegionDrawable
            );

            ball.addListener(new ClickListener() {
                public void clicked(InputEvent event, float x, float y){
                    System.out.println("Ball " + index + " clicked");
                    // doesn't allow empty balls to be selected
                    if(!board.getBalls()[index].getColor().isBlank()) {
                        if(board.getBalls()[index].getColor().isBlue() == (colorSelectPlayer.getCheckedIndex() == 1) ||
                                (board.getBalls()[index].getColor().isPurple() == (colorSelectPlayer.getCheckedIndex() == 0)) ) {
                            board.selectBall(board.getBalls()[index]);
                        }
                        else {
                            balls[index].setChecked(false);
                        }
                    }
                    else {
                        board.setIsModified();
                        if(board.getSelected().size() > 0) {
                            board.move(board.getSelected().get(0), board.getBalls()[index]);
                        }
                    }
                }
            });

            ballTextureRegionDrawable.setMinSize(50, 50);
            ballTexturePressedRegionDrawable.setMinSize(50, 50);
            balls[iBall] = ball;
        }
    }

    @Override
    public void update(float dt) {
        System.out.println("PlayState: Update");
        ArrayList<Ball> selectedList = board.getSelected();
        //removes any ball that is not checked from the selected list
        for(Ball ball : board.getBalls()) {
            if (ball != null && !balls[ball.getId()].isChecked()) {
                board.removeBall(board.getBalls()[ball.getId()]);
            }
        }
        assert selectedList!= null;

        // doesn't allow more than 3 balls to be selected
        if(selectedList.size()>3){
            for (Ball ball : selectedList) {
                balls[ball.getId()].setChecked(false);
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
                    balls[ball.getId()].setChecked(false);
                }
                selectedList.clear();
            }
        }

    }

    @Override
    public void draw() {
        stage.addActor(background);

        stage.addActor(board.getBoardImage());
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));

        /*
        int iBall = 0;
        for(int iLine = 0; iLine < 9; iLine++) {
            Table table = new Table();
            // table.debug();

            table.row().pad(5);
            int iColumnMax = (iLine < 5 ? 5 + iLine : 9 - iLine + 4);
            for(int iColumn = 0; iColumn < iColumnMax; iColumn++) {
                table.add(balls[iBall]).width(43).height(43);
                iBall++;
            }
            table.setFillParent(true);
            table.setTransform(true);
            table.padTop(iLine * 96 - 386);
            table.padRight(3.4f);

            stage.addActor(table);
        }

         */
        board.mapBallsToHexGrid(board.getBalls());
        HexGrid gridOfBalls = board.getHexGrid();
        int c =1;
        float temp = -4;

            for(int i =0;i<61; i++){
                float x = gridOfBalls.getHexList().get(i).getX();
                float y = gridOfBalls.getHexList().get(i).getZ();
                if(temp!=y){
                    c++;
                }
                x+= c*0.5;
                balls[i].setBounds((x*53 +445),y*48 +380,43,43);
                stage.addActor(balls[i]);
                temp = y;
            }
            board.setIsModified(false);


        stage.addActor(bluePlayer);
        stage.addActor(purplePlayer);

        stage.draw();
    }

    @Override
    public void handleInput() {

    }

    @Override
    public void dispose() {

    }
}
