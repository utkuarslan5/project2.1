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
        // isModified initiated to true, so will be display in the UI
        this.isModified = true;

        Texture img = new Texture("abalone.png");
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
            else{
                this.balls[iBall] = new Ball(Color.BLANK,iBall);
            }
        }
        selected = new ArrayList<>();
    }

    public Image getBoardImage() {
        return board;
    }

    public HexGrid getHexGrid() {
        return grid;
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

    // TODO: change and return Hexgrid with balls
    public Ball[] getBalls() {
        return balls;
    }

    // a method to map Balls to Hexgrid
    public void mapBallsToHexGrid(Ball[] balls) {
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

    public void move(Ball ballFrom,Ball ballTo){
        int from = grid.getBallAt(ballFrom);
        int to = grid.getBallAt(ballTo);

        ballFrom.getColor();
        System.out.println(from + " to " + to);

        Ball tempBall;
        tempBall = balls[ballFrom.getId()];
        balls[ballFrom.getId()] = balls[ballTo.getId()];
        balls[ballTo.getId()] = tempBall;
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