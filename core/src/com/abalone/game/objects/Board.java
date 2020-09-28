package com.abalone.game.objects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

import java.util.ArrayList;
import java.util.List;

public class Board {
    private final Image board;
    private Boolean isModified;
    private HexGrid hexGrid;
    private ArrayList<Ball> selected;

    public Board() {
        // isModified initiated to true, so will be display in the UI
        this.isModified = true;

        Texture img = new Texture("abalone.png");
        this.board = new Image(img);

        hexGrid = new HexGrid();
        selected = new ArrayList<>();
    }

    public Image getBoardImage() {
        return board;
    }

    public HexGrid getHexGrid() {
        return hexGrid;
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

    // a method to map Balls to Hexgrid
    public void mapBallsToHexGrid(Ball[] balls) {
        List<Hex> hexList = hexGrid.getHexList();
        //check for Pigeon Hole Principle
        if (balls.length == hexList.size()) {
            for (int i = 0; i < balls.length; i++) {
                hexList.get(i).setBall(balls[i]);
            }
            hexGrid.setHexList(hexList);
        } else {
            throw new IllegalArgumentException("balls.length != temp.size()");
        }
    }

    //  a method to return all occupied Hex's
    public ArrayList<Hex> getOccupiedHex() {
        ArrayList<Hex> temp = new ArrayList<>();
        for (Hex h : hexGrid.getHexList()) {
            if (h.isOccupied()) temp.add(h);
        }
        return temp;
    }

    // a method to return all unoccupied Hex's
    public ArrayList<Hex> getUnoccupiedHex() {
        ArrayList<Hex> temp = new ArrayList<>();
        for (Hex h : hexGrid.getHexList()) {
            if (!h.isOccupied()) temp.add(h);
        }
        return temp;
    }

    public void move(Ball ballFrom,Ball ballTo) {
        int from = hexGrid.getBallAt(ballFrom);
        int to = hexGrid.getBallAt(ballTo);
        System.out.println(from + " to " + to);

        hexGrid.getHexList().get(to).setBall(ballFrom);
        hexGrid.getHexList().get(from).setBall(ballTo);

        String log = "";
        for(Hex hex: hexGrid.getHexList()) {
            Ball ball = hex.getBall();
            switch (ball.getColor()) {
                case BLUE: log += "1"; break;
                case BLANK: log += "0"; break;
                case PURPLE: log += "2"; break;
            }
        }
        System.out.println(log);
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