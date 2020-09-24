package com.abalone.game.objects;

import com.abalone.game.utils.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

import java.util.ArrayList;
import java.util.List;

public class Board {
    private final Image board;
    private Boolean isModified;
    private HexGrid grid;
    private final Ball[] balls;
    private ArrayList<Ball> selected;

    public Board() {
        Texture img = new Texture("abalone.png");
        // isModified initiated to true, so will be display in the UI
        this.isModified = true;
        this.board = new Image(img);
        grid = new HexGrid();
        // TODO: adapt to the hexgrid
        this.balls = new Ball[61];
        for (int iBall = 0; iBall < 61; iBall++) {
            if (iBall < 11 || (iBall >= 13 && iBall <= 15)) {
                this.balls[iBall] = new Ball(Color.BLUE,iBall);
            } else if (iBall >= 50 || (iBall >= 45 && iBall <= 47)) {
                this.balls[iBall] = new Ball(Color.PURPLE,iBall);
            }
        }
        selected = new ArrayList<>();
    }

    public Image getBoardImage() {
        return board;
    }

    // TODO: change and return Hexgrid with balls
    public Ball[] getGrid() {
        return this.balls;
    }

    // a method to return HexGrid (see getGrid();)
    public HexGrid getGrid(Ball[] balls) {
        // TODO: we need to keep track of which time-step the grid and balls are at
        this.grid = mapBallsToHexgrid(balls);
        return this.grid;
    }

    public void selectBall(Ball ball) {
        selected.add(ball);
    }
    public void removeBall(Ball ball){
        selected.remove(ball);
    }
    public ArrayList<Ball> getSelected() {
        return selected;
    }


    public Ball[] getBalls() {
        return balls;
    }

    // a method to map Balls to Hexgrid
    public HexGrid mapBallsToHexgrid(Ball[] balls) {
        List<Hex> temp = grid.getHexList();
        //check for Pigeon Hole Principle
        if (balls.length == temp.size()) {
            for (int i = 0; i < balls.length; i++) {
                temp.get(i).setBall(balls[i]);
            }
            grid.setHexList(temp);
        } else {
            throw new IllegalArgumentException("balls.length != temp.size()");
        }
        return grid;
    }

    //  a method to return all occupied Hex's
    public ArrayList<Hex> getOccupiedHex() {
        ArrayList<Hex> temp = new ArrayList<>();
        for (Hex h : grid.getHexList()) {
            if (h.isOccupied()) temp.add(h);
        }
        return temp;
    }

    // a method to return all unoccupied Hex's
    public ArrayList<Hex> getUnoccupiedHex() {
        ArrayList<Hex> temp = new ArrayList<>();
        for (Hex h : grid.getHexList()) {
            if (!h.isOccupied()) temp.add(h);
        }
        return temp;
    }

    public Boolean isModified() {
        return this.isModified;
    }

    public void setIsModified(Boolean isModified) {
        this.isModified = isModified;
    }

    public void setIsModified() {
        this.setIsModified(true);
    }
}
