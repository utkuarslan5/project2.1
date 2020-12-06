package com.abalone.game.states;

import com.abalone.game.AbaloneGame;
import com.abalone.game.gameTree.Heuristics;
import com.abalone.game.gameTree.Tree;
import com.abalone.game.managers.GameStateManager;
import com.abalone.game.objects.*;
import com.abalone.game.players.MiniMax;
import com.abalone.game.players.NegaMax;
import com.abalone.game.players.Player;
import com.abalone.game.utils.AI;
import com.abalone.game.utils.TurnsFinder;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import java.util.ArrayList;
import java.util.List;

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
    private CheckBox highlight;
    private ButtonGroup<CheckBox> colorSelectPlayer;
    private Label purpleLostBalls;
    private Label blueLostBalls;
    public static int lostP;
    public static int lostB;
    private List<Hex> allDestinations = new ArrayList<>();
    private ImageButton[] lostBalls;
    private ImageButton[] circles;
    private MiniMax miniMax;
    private NegaMax negaMax;
    private Tree tree;
    private boolean blueFinished;
    private boolean purpleFinished;

    private TextureRegionDrawable ballTextureRegionDrawableBlue;
    private TextureRegionDrawable ballTexturePressedRegionDrawableBlue;
    private TextureRegionDrawable ballTextureRegionDrawablePurple;
    private TextureRegionDrawable ballTexturePressedRegionDrawablePurple;
    private TextureRegionDrawable ballTextureRegionDrawableBlank;
    private TextureRegionDrawable ballTexturePressedRegionDrawableBlank;
    private TextureRegionDrawable ballTextureRegionDrawableBlankDark;
    private TextureRegionDrawable ballTexturePressedRegionDrawableBlankDark;
    private ImageButton returnButton;

    protected PlayState(GameStateManager ourGsm) {
        super(ourGsm);
    }

    @Override
    public void init() {
        blueFinished = true;
        purpleFinished = true;
        skin = new Skin(Gdx.files.internal("cloud-form/skin/cloud-form-ui.json"));
        spriteBatch = new SpriteBatch();
        board = new Board();

        board.getBoardImage().setPosition(AbaloneGame.width / 2f - (board.getBoardImage().getWidth() / 2), AbaloneGame.height / 2f - (board.getBoardImage().getHeight() / 2));
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
        returnButton.getImage().setScale(1 / 18f);
        returnButton.setScale(0.5f);
        returnButton.setPosition(15, 730);

        Texture img = new Texture("pbg.jpg");
        background = new Image(img);
        background.setWidth(AbaloneGame.width);
        background.setHeight(AbaloneGame.height);

        bluePlayer = new CheckBox("Blue", skin, "radio");
        bluePlayer.setPosition(1000, 600);
        bluePlayer.setTransform(true);
        bluePlayer.setScale(1.5f);
        bluePlayer.getLabel().setFontScale(0.5f);
        bluePlayer.getStyle().fontColor = Color.WHITE;
        bluePlayer.setChecked(true);

        purplePlayer = new CheckBox("Purple", skin, "radio");
        purplePlayer.setPosition(991, 550);
        purplePlayer.setTransform(true);
        purplePlayer.setScale(1.5f);
        purplePlayer.getLabel().setFontScale(0.5f);
        //purplePlayer.setChecked(true);

        colorSelectPlayer = new ButtonGroup<>(bluePlayer, purplePlayer);
        colorSelectPlayer.setMaxCheckCount(1);
        colorSelectPlayer.setMinCheckCount(1);

        highlight = new CheckBox("Highlight Possible Moves",skin);
        highlight.setPosition(830,650);
        highlight.setTransform(true);
        highlight.setScale(1.5f);
        highlight.getLabel().setFontScale(0.5f);
        highlight.getStyle().fontColor = Color.WHITE;
        highlight.setChecked(true);

        lostP = 0;
        lostB = 0;
        purpleLostBalls = new Label("Purple Lost: " + lostP, skin);
        purpleLostBalls.setFontScale(1.5f);
        purpleLostBalls.getStyle().fontColor = Color.WHITE;
        purpleLostBalls.setPosition(530, 50);
        blueLostBalls = new Label("Blue Lost: " + lostB, skin);
        blueLostBalls.setFontScale(1.5f);
        blueLostBalls.setPosition(550, 700);

        ballTextureRegionDrawableBlue = new TextureRegionDrawable(new Texture(Gdx.files.internal("blue.png")));
        ballTexturePressedRegionDrawableBlue = new TextureRegionDrawable(new Texture(Gdx.files.internal("blueClicked.png")));

        ballTextureRegionDrawablePurple = new TextureRegionDrawable(new Texture(Gdx.files.internal("purple.png")));
        ballTexturePressedRegionDrawablePurple = new TextureRegionDrawable(new Texture(Gdx.files.internal("purpleClicked.png")));

        ballTextureRegionDrawableBlank = new TextureRegionDrawable(new Texture(Gdx.files.internal("blank.png")));
        ballTexturePressedRegionDrawableBlank = new TextureRegionDrawable(new Texture(Gdx.files.internal("blank.png")));

        ballTextureRegionDrawableBlankDark = new TextureRegionDrawable(new Texture(Gdx.files.internal("blankdark.png")));
        ballTexturePressedRegionDrawableBlankDark = new TextureRegionDrawable(new Texture(Gdx.files.internal("blankdark.png")));

        // TODO: adapt the loop and conditions to the the hexgrid (and not the temporary array)
        ArrayList<Hex> hexList = (ArrayList<Hex>) board.getHexGrid().getHexList();
        ballButtons = new ImageButton[61];
        for (int iHex = 0; iHex < hexList.size(); iHex++) {
            final int index = iHex;
            final Hex hex = hexList.get(iHex);

            TextureRegionDrawable ballTextureRegionDrawable;
            TextureRegionDrawable ballTexturePressedRegionDrawable;

            if (hexList.get(iHex).getBall() != null && hexList.get(iHex).getBall().getColor().isBlue()) {
                ballTextureRegionDrawable = ballTextureRegionDrawableBlue;
                ballTexturePressedRegionDrawable = ballTexturePressedRegionDrawableBlue;
            } else if (hexList.get(iHex).getBall() != null && hexList.get(iHex).getBall().getColor().isPurple()) {
                ballTextureRegionDrawable = ballTextureRegionDrawablePurple;
                ballTexturePressedRegionDrawable = ballTexturePressedRegionDrawablePurple;
            } else {
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
                    System.out.println("Ball " + ball.getId() + " clicked " + ball.getColor() + " x:" + hex.getX() + " y:" + hex.getY() + " z:" + hex.getZ());
                    // doesn't allow empty balls to be selected
                    if (!ball.getColor().isBlank()) {
                        if (ball.getColor().isBlue() == (colorSelectPlayer.getCheckedIndex() == 0) ||
                                (ball.getColor().isPurple() == (colorSelectPlayer.getCheckedIndex() == 1))) {

                            //ONLY ALLOWS NEIGHBOURS TO BE SELECTED IN A LINE
                            allDestinations.clear();
                            alignSelection(hex, ball, index);

                            highlightMoves();

                        } else if (board.getSelected().size() > 1) {
                            ballButtons[index].setChecked(false);
                            System.out.println("PUSH NEEDED");
                            board.pushBalls(ball);
                            if(board.getPushPossible()) {
                                allDestinations.clear();
                                if (board.getMovePerformed()) {
                                    switchTurnPlayer();
                                    board.setMovePerformed(false);
                                }
                            }else{
                                for (Ball aha : board.getSelected()) {
                                    int ffs = board.getHexGrid().getBallAt(aha);
                                    ballButtons[ffs].setChecked(false);
                                }
                                board.getSelected().clear();
                                board.setPushPossible(true);
                                allDestinations.clear();
                            }
                        } else {
                            ballButtons[index].setChecked(false);
                        }
                    } else if (ball.getColor().isBlank()) {
                        if (board.getSelected().size() > 0) {
                            board.requestMove(ball);
                            if (board.getMovePerformed()) {
                                switchTurnPlayer();
                                allDestinations.clear();
                                board.setMovePerformed(false);
                            }
                            else{
                                allDestinations.clear();
                                board.getSelected().clear();
                                switchTurnPlayer();
                                switchTurnPlayer();
                            }
                        }
                    }
                }
            });
            ballTextureRegionDrawable.setMinSize(50, 50);
            ballTexturePressedRegionDrawable.setMinSize(50, 50);
            ballButtons[iHex] = ballButton;
        }

        lostBalls = new ImageButton[12];
        circles = new ImageButton[12];
        int tempy= 0;
        int tempy2 = 0;
        float t = 1;
        float t2= 1;
        TextureRegionDrawable circle = new TextureRegionDrawable(new Texture(Gdx.files.internal("uncheckedCheckBox.png")));
        circle.setMinSize(52,52);
        for (int h = 0; h < lostBalls.length; h++) {
            circles[h] = new ImageButton(
                    circle,
                    circle
            );
            if(h < 6) {
                lostBalls[h] = new ImageButton(
                        ballTextureRegionDrawableBlue,
                        ballTextureRegionDrawableBlue,
                        ballTexturePressedRegionDrawableBlue
                );
                lostBalls[h].setPosition(240 + t,418 + tempy);
                circles[h].setPosition(240 + t,418 + tempy);
                t += 31;
                tempy += 55;
            } else {
                lostBalls[h] = new ImageButton(
                        ballTextureRegionDrawablePurple,
                        ballTextureRegionDrawablePurple,
                        ballTexturePressedRegionDrawablePurple
                );
                lostBalls[h].setPosition(240 + t2,328 - tempy2);
                circles[h].setPosition(240 + t2, 328 - tempy2);
                t2 += 31;
                tempy2 += 55;
            }
            lostBalls[h].setVisible(false);
        }
    }

    @Override
    public void update(float dt) {
        lostB = 14-board.getBlueHex().size();
        lostP = 14-board.getPurpleHex().size();

        ArrayList<Ball> selectedList = board.getSelected();
        //assert selectedList != null;

        // doesn't allow more than 3 balls to be selected
        if (selectedList.size() > 3) {
            for (Ball ball : selectedList) {
                int ffs = board.getHexGrid().getBallAt(ball);
                ballButtons[ffs].setChecked(false);
            }
            selectedList.clear();
            board.getSelected().clear();
        }

        Ball curr = null;
        Ball first = null;
        if (selectedList.size() != 0) {
            curr = selectedList.get(selectedList.size() - 1);
            first = selectedList.get(0);
        }

        if (curr != first) {
            if (curr.getColor() != first.getColor()) {
                for (Ball ball : selectedList) {
                    ballButtons[ball.getId()].setChecked(false);
                }
                selectedList.clear();
            }
        }

        if (returnButton.isChecked()) {
            State MenuState = new MenuState(gsm);
            gsm.pop();
            gsm.push(MenuState);
        }

        purpleLostBalls.setText("Purple Lost: " + lostP);
        blueLostBalls.setText("Blue Lost: " + lostB);

        if(lostB!=0) {
            lostBalls[lostB-1].setVisible(true);
        }
        if(lostP!=0){
            lostBalls[6 + lostP-1].setVisible(true);
        }
        if (lostP == 6 || lostB == 6) {
            State endState = new EndState(gsm);
            gsm.pop();
            gsm.push(endState);
        }

        if(bluePlayer.isChecked() && AbaloneGame.isBluePlayerAI && blueFinished) {
            blueAIplays();
        }
        else if(purplePlayer.isChecked() && AbaloneGame.isPurplePlayerAI && purpleFinished) {
            purpleAIplays();
        }
    }

    @Override
    public void draw() {
        stage.addActor(background);

        stage.addActor(board.getBoardImage());
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));

        ArrayList<Hex> hexList = (ArrayList<Hex>) board.getHexGrid().getHexList();
        int c = 1;
        float temp = -4;

        for (int i = 0; i < 61; i++) {
            Hex hex = hexList.get(i);
            float x = hex.getX();
            float y = hex.getZ();
            if (temp != y) {
                c++;
            }
            x += c * 0.5;
            ballButtons[i].setBounds((x * 53 + 445), y * 48 + 380, 43, 43);

            // Update ballButton color
            if (hex.getBall() != null && hex.getBall().getColor().isBlue()) {
                ballButtons[i].getStyle().imageUp = ballTextureRegionDrawableBlue;
                ballButtons[i].getStyle().imageDown = ballTextureRegionDrawableBlue;
                ballButtons[i].getStyle().imageChecked = ballTexturePressedRegionDrawableBlue;
            } else if (hex.getBall() != null && hex.getBall().getColor().isPurple()) {
                ballButtons[i].getStyle().imageUp = ballTextureRegionDrawablePurple;
                ballButtons[i].getStyle().imageDown = ballTextureRegionDrawablePurple;
                ballButtons[i].getStyle().imageChecked = ballTexturePressedRegionDrawablePurple;
            } else {

                if (allDestinations.contains(hex) && highlight.isChecked()) {
                    ballButtons[i].getStyle().imageUp = ballTextureRegionDrawableBlankDark;
                    ballButtons[i].getStyle().imageDown = ballTextureRegionDrawableBlankDark;
                    ballButtons[i].getStyle().imageChecked = ballTextureRegionDrawableBlankDark;

                } else {
                    ballButtons[i].getStyle().imageUp = ballTextureRegionDrawableBlank;
                    ballButtons[i].getStyle().imageDown = ballTextureRegionDrawableBlank;
                    ballButtons[i].getStyle().imageChecked = ballTextureRegionDrawableBlank;
                }
            }
            stage.addActor(ballButtons[i]);
            temp = y;
        }
        for(ImageButton lostBalls : lostBalls){
            stage.addActor(lostBalls);
        }
        for(ImageButton circles : circles){
            stage.addActor(circles);
        }
        stage.addActor(bluePlayer);
        stage.addActor(purplePlayer);
        stage.addActor(highlight);
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
        for (int i = 0; i < 61; i++) {
            ballButtons[i].setChecked(false);
        }
        if (purplePlayer.isChecked()) {
            bluePlayer.setChecked(true);
        } else {
            purplePlayer.setChecked(true);
        }
    }

    public void blueAIplays() {
        blueFinished = false;
        System.out.println("blue playing");
        int depthTree = 2;
        Player player;
        Heuristics heuristics = new Heuristics(this.board,com.abalone.game.utils.Color.BLUE,0.5,-1,0.5,0,0,1000);
        tree = new Tree(this.board, depthTree, com.abalone.game.utils.Color.BLUE,heuristics);
        if(AbaloneGame.bluePlayerAI == AI.MINIMAX) {
            player = new MiniMax(tree.getRoot(), depthTree, true, tree);
            System.out.println(player.getBestNode().getTurn());
            Turn turn =player.getBestNode().getTurn();
            board.move(turn);
        }
        else if(AbaloneGame.bluePlayerAI == AI.NEGAMAX) {
            player = new NegaMax(tree.getRoot(), depthTree, true, tree);
            Turn turn =player.getBestNode().getTurn();
            board.move(turn);
        }

        if(board.getMovePerformed()) {
            switchTurnPlayer();
            allDestinations.clear();
            board.setMovePerformed(false);
            blueFinished = true;
        }else{
            Heuristics heuristics2 = new Heuristics(this.board,com.abalone.game.utils.Color.BLUE,0,100,1,1,1,10000);
            tree = new Tree(this.board, depthTree, com.abalone.game.utils.Color.BLUE,heuristics2);
            if(AbaloneGame.bluePlayerAI == AI.MINIMAX) {
                player = new MiniMax(tree.getRoot(), depthTree, true, tree);
                //System.out.println(player.getBestNode().getTurn());
                Turn turn =player.getBestNode().getTurn();
                board.move(turn);
            }
            else if(AbaloneGame.bluePlayerAI == AI.NEGAMAX) {
                player = new NegaMax(tree.getRoot(), depthTree, true, tree);
                Turn turn =player.getBestNode().getTurn();
                board.move(turn);
            }

            if(board.getMovePerformed()) {
                switchTurnPlayer();
                allDestinations.clear();
                board.setMovePerformed(false);
                blueFinished = true;
            }else{
                //System.out.println("ffs1");
            }
        }
    }

    public void purpleAIplays() {
        purpleFinished = false;
        System.out.println("purple playing");
        int depthTree = 2;
        Player player;
        Heuristics heuristics = new Heuristics(this.board,com.abalone.game.utils.Color.PURPLE,0.5,-1,0.5,0,0,1000);
        tree = new Tree(this.board, depthTree, com.abalone.game.utils.Color.PURPLE,heuristics);
        if(AbaloneGame.purplePlayerAI == AI.MINIMAX) {
            player = new MiniMax(tree.getRoot(), depthTree, true, tree);
            System.out.println(player.getBestNode().getTurn().getTurnType());
            Turn turn =player.getBestNode().getTurn();
            board.move(turn);
        }
        else if(AbaloneGame.purplePlayerAI == AI.NEGAMAX) {
            player = new NegaMax(tree.getRoot(), depthTree, true, tree);
            Turn turn =player.getBestNode().getTurn();
            board.move(turn);
        }
        if(board.getMovePerformed()) {
            switchTurnPlayer();
            allDestinations.clear();
            board.setMovePerformed(false);
            purpleFinished = true;
        }else{
            Heuristics heuristics2 = new Heuristics(this.board,com.abalone.game.utils.Color.PURPLE,0,100,1,1,1,10000);
            tree = new Tree(this.board, depthTree, com.abalone.game.utils.Color.PURPLE,heuristics2);
            if(AbaloneGame.purplePlayerAI == AI.MINIMAX) {
                player = new MiniMax(tree.getRoot(), depthTree, true, tree);
                //System.out.println(player.getBestNode().getTurn());
                Turn turn =player.getBestNode().getTurn();
                board.move(turn);
            }
            else if(AbaloneGame.purplePlayerAI == AI.NEGAMAX) {
                player = new NegaMax(tree.getRoot(), depthTree, true, tree);
                Turn turn =player.getBestNode().getTurn();
                board.move(turn);
            }

            if(board.getMovePerformed()) {
                switchTurnPlayer();
                allDestinations.clear();
                board.setMovePerformed(false);
                purpleFinished = true;
            }else{
                //System.out.println("ffs2");
            }
        }
    }

    public void alignSelection(Hex hex, Ball ball, int index) {
        board.selectBall(ball);
        if (board.getSelected().size() > 1 && board.getSelected().size() < 4) {
            int theBall = board.getHexGrid().getBallAt(board.getSelected().get(board.getSelected().size() - 2));
            Hex tempHex = board.getHexGrid().getHexList().get(theBall);

            boolean added = false;
            //current hex that's selected
            int x1 = hex.getX();
            int y1 = hex.getY();
            int z1 = hex.getZ();
            int x2, y2, z2;

            //the last selected hex
            int tempx1 = tempHex.getX();
            int tempy1 = tempHex.getY();
            int tempz1 = tempHex.getZ();

            //first selected hex
            Ball first = board.getSelected().get(0);
            int firstint = board.getHexGrid().getBallAt(first);
            Hex firstHex = board.getHexGrid().getHexList().get(firstint);
            int fx1 = firstHex.getX();
            int fy1 = firstHex.getY();
            int fz1 = firstHex.getZ();

            List<Hex> neighb = tempHex.getNeighbors();
            for (Hex theHex : neighb) {
                x2 = theHex.getX();
                y2 = theHex.getY();
                z2 = theHex.getZ();
                if (x1 == x2 && y1 == y2 && z1 == z2) { //if it's a neighbour
                    if (board.getSelected().size() == 3) {
                        // check if they are in a line
                        if ((x2 == fx1 && x2 == tempx1) || (y2 == fy1 && y2 == tempy1) || (z2 == fz1 && z2 == tempz1)) {
                            added = true;
                            break;
                        }
                    } else {
                        //add directly if size 2
                        added = true;
                        break;
                    }
                }
            }
            //if nothing found from neighbours skip to the other (first) selected ball's neighbours
            if (board.getSelected().size() == 3 && !added) {
                List<Hex> neighb2 = firstHex.getNeighbors();
                for (Hex theHex : neighb2) {
                    int xs3 = theHex.getX();
                    int ys3 = theHex.getY();
                    int zs3 = theHex.getZ();
                    if (x1 == xs3 && y1 == ys3 && z1 == zs3 && // if neighbour and one of the following is true
                            ((tempx1 == xs3 && xs3 == fx1)
                                    || (tempy1 == ys3 && ys3 == fy1)
                                    || (tempz1 == zs3 && zs3 == fz1))) {
                        added = true;
                        break;
                    }
                }
            }
            if (!added) { //if it can't be added remove it
                board.removeBall(ball);
                ballButtons[index].setChecked(false);
            }
        }
    }

    public void highlightMoves() {
        TurnsFinder turnsFinder = new TurnsFinder(board.getHexGrid());
        List<List<Turn>> tempList = new ArrayList<>();
        List<Hex> startHexes = new ArrayList<>();
        List<Hex> tempdest = new ArrayList<>();
        List<Hex> inselected = new ArrayList<>();
        for (Ball allBall : board.getSelected()) {
            int tired = board.getHexGrid().getBallAt(allBall);
            tempList.add(turnsFinder.findTurns(board.getHexGrid().getHexList().get(tired)));
            int temp = board.getHexGrid().getBallAt(allBall);
            inselected.add(board.getHexGrid().getHexList().get(temp));
        }

        for (List<Turn> mainturns : tempList) {
            assert mainturns != null;
            if (mainturns != null) {
                for (Turn turn : mainturns) {
                    if (turn.getTurnType() + 1 == board.getSelected().size()) {
                        List<Move> tempMoveList = turn.getMovesList();
                        for (int j = 0; j < turn.getMovesList().size(); j++) {
                            startHexes.add(tempMoveList.get(j).getStart());
                            tempdest.add(tempMoveList.get(j).getDestination());
                        }
                        if (inselected.containsAll(startHexes)) {
                            allDestinations.addAll(tempdest);
                        } else {
                            startHexes.clear();
                            tempdest.clear();
                        }
                    }
                }
            }
        }
    }
}