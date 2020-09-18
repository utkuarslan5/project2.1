package com.abalone.game.objects;

import com.abalone.game.AbaloneGame;
import com.abalone.game.utils.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;

public class Board {
    private Image board;
    private HexGrid grid;
    private Ball [] balls;

    public Board(){
        Texture img = new Texture("abalone.png");
        this.board = new Image(img);

        // TODO: adapt to the hexgrid
        this.balls = new Ball[61];
        for(int iBall = 0; iBall < 61; iBall++) {
            if(iBall < 11 || (iBall >= 13 && iBall <= 15)) {
                this.balls[iBall] = new Ball(Color.BLUE);
            }
            else if(iBall >= 50 || (iBall >= 45 && iBall <= 47)) {
                this.balls[iBall] = new Ball(Color.PURPLE);
            }
        }
    }
    public Image getBoardImage(){
        return board;
    }

    // TODO: change and return Hexgrid with balls
    public Ball[] getGrid() {
        return this.balls;
    }

    public void selectBall(Ball ball) {

    }
}
