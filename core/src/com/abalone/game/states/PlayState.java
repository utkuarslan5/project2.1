package com.abalone.game.states;

import com.abalone.game.AbaloneGame;
import com.abalone.game.managers.GameStateManager;
import com.abalone.game.objects.Ball;
import com.abalone.game.objects.Board;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.EventListener;

public class PlayState extends State {
    private SpriteBatch spriteBatch;
    private Stage stage;
    private Image background;
    private BitmapFont gameFont;
    private Board board;
    private ImageButton[] balls;

    protected PlayState(GameStateManager ourGsm) {
        super(ourGsm);
    }

    @Override
    public void init() {
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

        // TODO: adapt the loop and conditions to the the hexgrid (and not the temporary array)
        Ball[] grid = board.getGrid();
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
            ballTextureRegionDrawable = (new TextureRegionDrawable(new TextureRegion(ballTexture)));
            ballTexturePressedRegionDrawable = new TextureRegionDrawable(new TextureRegion(ballTexturePressed));
            ImageButton ball = new ImageButton(
                    ballTextureRegionDrawable,
                    ballTexturePressedRegionDrawable
            );

            ball.addListener(new ClickListener() {
                public void clicked(InputEvent event, float x, float y){
                    System.out.println("Ball " + index + " clicked");
                    board.selectBall(board.getGrid()[index]);
                }
            });

            ballTextureRegionDrawable.setMinSize(50, 50);
            ballTexturePressedRegionDrawable.setMinSize(50, 50);
            balls[iBall] = ball;
        }
    }

    @Override
    public void update(float dt) {

    }

    @Override
    public void draw() {
        stage.addActor(background);
        stage.addActor(board.getBoardImage());
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));

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

        stage.draw();
    }

    @Override
    public void handleInput() {

    }

    @Override
    public void dispose() {

    }
}
