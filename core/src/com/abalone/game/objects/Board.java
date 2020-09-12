package com.abalone.game.objects;

import com.abalone.game.AbaloneGame;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class Board {
    private Image board;
    public Board(){
        Texture img = new Texture("abalone.png");
        board = new Image(img);
    }
    public Image getBoardImage(){
        return board;
    }
}
