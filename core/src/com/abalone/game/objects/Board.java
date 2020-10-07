package com.abalone.game.objects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

import java.util.ArrayList;
import java.util.List;

public class Board {
    private Image board;
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
        if (selected.contains(ball)) {
            removeBall(ball);
        } else {
            selected.add(ball);
        }

        String log = "Selected: ";
        for (Ball b : selected) {
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

    //TODO: optimize
    public void organizeSelected(Ball ballTo) {
        int ballToPos = hexGrid.getBallAt(ballTo);
        for(int i = 0 ; i < selected.size(); i++)
        {
            for(int j = i+1 ; j < selected.size();j++)
            {
                if(ballToPos < hexGrid.getBallAt(selected.get(j)) && hexGrid.getBallAt(selected.get(j)) > hexGrid.getBallAt(selected.get(i))
                        || ballToPos > hexGrid.getBallAt(selected.get(j)) && hexGrid.getBallAt(selected.get(j)) < hexGrid.getBallAt(selected.get(i)))
                {
                    Ball temp = selected.get(i);
                    selected.set(i, selected.get(j));
                    selected.set(j, temp);
                }
            }
        }
    }


    //TODO: push other balls if they get hit
    public void pushBall() {

    }

    //TODO: rewrite the move ball so it takes in account the whole list of selected balls
    public void move(Ball ballTo) {
        organizeSelected(ballTo);
        int from = hexGrid.getBallAt(selected.get(0));
        int to = hexGrid.getBallAt(ballTo);
        System.out.println(from + " to " + to);

        if (selected.size() == 1) {
            hexGrid.getHexList().get(to).setBall(selected.get(0));
            hexGrid.getHexList().get(from).setBall(ballTo);
        } else if (selected.size() == 2) {
            int from2 = hexGrid.getBallAt(selected.get(1));
                if(hexGrid.getHexList().get(to).getZ() > hexGrid.getHexList().get(from).getZ() &&
                    hexGrid.getHexList().get(from).getZ() == hexGrid.getHexList().get(from2).getZ()){
                        Hex tempHex = hexGrid.getHexList().get(to);
                        Hex tempHex2 = hexGrid.getMatchedHex(new Hex(tempHex.getX() + 1,tempHex.getZ()));
                        int to2 = hexGrid.getBallAt(tempHex2.getBall());
                        Ball tempBall = tempHex2.getBall();
                        hexGrid.getHexList().get(to).setBall(selected.get(0));
                        hexGrid.getHexList().get(to2).setBall(selected.get(1));
                        hexGrid.getHexList().get(from).setBall(ballTo);
                        hexGrid.getHexList().get(from2).setBall(tempBall);
                }
                else if(hexGrid.getHexList().get(to).getZ() < hexGrid.getHexList().get(from).getZ() &&
                        hexGrid.getHexList().get(from).getZ() == hexGrid.getHexList().get(from2).getZ()){
                    Hex tempHex = hexGrid.getHexList().get(to);
                    Hex tempHex2 = hexGrid.getMatchedHex(new Hex(tempHex.getX() - 1,tempHex.getZ()));
                    int to2 = hexGrid.getBallAt(tempHex2.getBall());
                    Ball tempBall = tempHex2.getBall();
                    hexGrid.getHexList().get(to).setBall(selected.get(0));
                    hexGrid.getHexList().get(to2).setBall(selected.get(1));
                    hexGrid.getHexList().get(from).setBall(ballTo);
                    hexGrid.getHexList().get(from2).setBall(tempBall);
                }
                else {
                    hexGrid.getHexList().get(to).setBall(selected.get(0));
                    hexGrid.getHexList().get(from2).setBall(selected.get(1));
                    hexGrid.getHexList().get(from).setBall(ballTo);
                }
        } else if (selected.size() == 3){
            int from2 = hexGrid.getBallAt(selected.get(1));
            int from3 = hexGrid.getBallAt(selected.get(2));
                hexGrid.getHexList().get(to).setBall(selected.get(0));
                hexGrid.getHexList().get(from3).setBall(selected.get(1));
                hexGrid.getHexList().get(from2).setBall(selected.get(2));
                hexGrid.getHexList().get(from).setBall(ballTo);

        }

        selected.clear();

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