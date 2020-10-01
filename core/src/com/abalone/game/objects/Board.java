package com.abalone.game.objects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

import java.util.ArrayList;

public class Board {
    private Image board;
    private Boolean isModified;
    private final HexGrid hexGrid;
    private final ArrayList<Ball> selected;

    public Board() {
        // isModified initiated to true, so will be display in the UI
        this.isModified = true;

        Texture img = new Texture("abalone.png");
        this.board = new Image(img);

        hexGrid = new HexGrid();
        selected = new ArrayList<>();
    }
        //For TESTING
    public Board(int x) {
        // isModified initiated to true, so will be display in the UI
        this.isModified = true;
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
        if(selected.contains(ball)) {
            removeBall(ball);
        }
        else {
            selected.add(ball);
        }

        String log = "Selected: ";
        for(Ball b: selected) {
            log += b.getId() + " ";
        }
        System.out.println(log);
    }

    public void removeBall(Ball ball) {
        selected.remove(ball);
    }
    public ArrayList<Ball> getSelected() {
        return selected;
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

    //TODO: make the first ball in the selected list the one in front
    public void organizeSelected(){
        if(selected.size() > 1 && selected.get(0).getColor().isPurple()){
            Ball tempBall;

            }
        }


    //TODO: push other balls if they get hit
    public void pushBall(){

    }

    //TODO: rewrite the move ball so it takes in account the whole list of selected balls
    public void move(Ball ballTo) {
        organizeSelected();
        int from = hexGrid.getBallAt(selected.get(0));
        int to = hexGrid.getBallAt(ballTo);
        System.out.println(from + " to " + to);

        hexGrid.getHexList().get(to).setBall(selected.get(0));
        hexGrid.getHexList().get(from).setBall(ballTo);

        selected.clear();

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