package com.abalone.game.objects;

import com.abalone.game.states.PlayState;
import com.abalone.game.utils.Color;
import com.abalone.game.utils.TurnsFinder;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

import java.util.ArrayList;
import java.util.List;

public class Board {
    private Image board;
    private Boolean isModified;
    private HexGrid hexGrid;
    private ArrayList<Ball> selected;
    private TurnsFinder turnsFinder;
    private boolean movePerformed;

    public Board() {
        // isModified initiated to true, so will be display in the UI
        this.isModified = true;
        this.movePerformed = false;
        Texture img = new Texture("abalone.png");
        this.board = new Image(img);

        hexGrid = new HexGrid();
        selected = new ArrayList<>();
        turnsFinder = new TurnsFinder(hexGrid);
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
        for (int i = 0; i < selected.size(); i++) {
            for (int j = i + 1; j < selected.size(); j++) {
                if (ballToPos < hexGrid.getBallAt(selected.get(j)) && hexGrid.getBallAt(selected.get(j)) > hexGrid.getBallAt(selected.get(i))
                        || ballToPos > hexGrid.getBallAt(selected.get(j)) && hexGrid.getBallAt(selected.get(j)) < hexGrid.getBallAt(selected.get(i))) {
                    Ball temp = selected.get(i);
                    selected.set(i, selected.get(j));
                    selected.set(j, temp);
                }
            }
        }
        for (int i = 0; i < selected.size(); i++) {
            System.out.println(selected.get(i).getId());
        }
    }

    //TODO: rewrite the move ball so it takes in account the whole list of selected balls
    public void move(Ball ballTo) {
        organizeSelected(ballTo);
        int from = hexGrid.getBallAt(selected.get(0));
        int to = hexGrid.getBallAt(ballTo);
        boolean move = false;
        System.out.println(from + " to " + to);


        if (selected.size() == 1) {
            if (isLegal(from, to)) {
                hexGrid.getHexList().get(to).setBall(selected.get(0));
                hexGrid.getHexList().get(from).setBall(ballTo);
                movePerformed = true;
                selected.clear();
            } else {
                System.out.println("Illegal!!!!!!!!!!!!");
            }

        } else if (selected.size() == 2) {
            int from2 = hexGrid.getBallAt(selected.get(1));

            if (hexGrid.getHexList().get(to).getZ() > hexGrid.getHexList().get(from).getZ() &&
                    hexGrid.getHexList().get(from).getZ() == hexGrid.getHexList().get(from2).getZ()) {
                Hex tempHex = hexGrid.getHexList().get(to);
                Hex tempHex2 = hexGrid.getMatchedHex(new Hex(tempHex.getX() + 1, tempHex.getZ()));
                int to2 = hexGrid.getBallAt(tempHex2.getBall());
                Ball tempBall = tempHex2.getBall();
                if (isLegal(from2, to2)) {
                    hexGrid.getHexList().get(to).setBall(selected.get(1));
                    hexGrid.getHexList().get(to2).setBall(selected.get(0));
                    hexGrid.getHexList().get(from).setBall(ballTo);
                    hexGrid.getHexList().get(from2).setBall(tempBall);
                    movePerformed = true;
                    selected.clear();
                }
            } else if (hexGrid.getHexList().get(to).getZ() < hexGrid.getHexList().get(from).getZ() &&
                    hexGrid.getHexList().get(from).getZ() == hexGrid.getHexList().get(from2).getZ()) {
                Hex tempHex = hexGrid.getHexList().get(to);
                Hex tempHex2 = hexGrid.getMatchedHex(new Hex(tempHex.getX() - 1, tempHex.getZ()));
                int to2 = hexGrid.getBallAt(tempHex2.getBall());
                Ball tempBall = tempHex2.getBall();
                if (isLegal(from2, to2)) {

                    hexGrid.getHexList().get(to).setBall(selected.get(1));
                    hexGrid.getHexList().get(to2).setBall(selected.get(0));
                    hexGrid.getHexList().get(from).setBall(ballTo);
                    hexGrid.getHexList().get(from2).setBall(tempBall);
                    movePerformed = true;
                    selected.clear();
                }
            }
            else if(hexGrid.getHexList().get(from).getZ() == hexGrid.getHexList().get(from2).getZ() &&
                    hexGrid.getHexList().get(to).getZ() == hexGrid.getHexList().get(from2).getZ()) {
                if(isLegal(from2,to)){
                    hexGrid.getHexList().get(to).setBall(selected.get(1));
                    hexGrid.getHexList().get(from2).setBall(selected.get(0));
                    hexGrid.getHexList().get(from).setBall(ballTo);
                    movePerformed = true;
                    selected.clear();
                }
            }
            else {
                if (isLegal(from2, to)) {
                    if(hexGrid.getHexList().get(from).getX() == hexGrid.getHexList().get(from2).getX()) {
                        if (hexGrid.getHexList().get(from2).getX() == hexGrid.getHexList().get(to).getX()) {
                            hexGrid.getHexList().get(to).setBall(selected.get(1));
                            hexGrid.getHexList().get(from2).setBall(selected.get(0));
                            hexGrid.getHexList().get(from).setBall(ballTo);
                            movePerformed = true;
                            selected.clear();
                        }
                        else{
                            System.out.println("Snake MOVE!");
                        }
                    }
                    else{
                        if (hexGrid.getHexList().get(from2).getY() == hexGrid.getHexList().get(to).getY()) {
                            hexGrid.getHexList().get(to).setBall(selected.get(1));
                            hexGrid.getHexList().get(from2).setBall(selected.get(0));
                            hexGrid.getHexList().get(from).setBall(ballTo);
                            movePerformed = true;
                            selected.clear();
                        }
                        else{
                            System.out.println("Snake MOVE!");
                        }
                    }

                }
            }

        } else if (selected.size() == 3) {
            int from2 = hexGrid.getBallAt(selected.get(1));
            int from3 = hexGrid.getBallAt(selected.get(2));
            if (hexGrid.getHexList().get(to).getZ() > hexGrid.getHexList().get(from).getZ() &&
                    hexGrid.getHexList().get(from).getZ() == hexGrid.getHexList().get(from2).getZ()) {
                Hex tempHex = hexGrid.getHexList().get(to);
                Hex tempHex2 = hexGrid.getMatchedHex(new Hex(tempHex.getX() + 1, tempHex.getZ()));
                Hex tempHex3 = hexGrid.getMatchedHex(new Hex(tempHex.getX() + 2, tempHex.getZ()));
                int to2 = hexGrid.getBallAt(tempHex2.getBall());
                int to3 = hexGrid.getBallAt(tempHex3.getBall());
                Ball tempBall = tempHex2.getBall();
                Ball tempBall2 = tempHex3.getBall();
                if (isLegal(from3, to3)) {
                    hexGrid.getHexList().get(to).setBall(selected.get(2));
                    hexGrid.getHexList().get(to2).setBall(selected.get(1));
                    hexGrid.getHexList().get(to3).setBall(selected.get(0));
                    hexGrid.getHexList().get(from).setBall(ballTo);
                    hexGrid.getHexList().get(from2).setBall(tempBall);
                    hexGrid.getHexList().get(from3).setBall(tempBall2);
                    movePerformed = true;
                    selected.clear();
                }
            } else if (hexGrid.getHexList().get(to).getZ() < hexGrid.getHexList().get(from).getZ() &&
                    hexGrid.getHexList().get(from).getZ() == hexGrid.getHexList().get(from2).getZ()) {
                Hex tempHex = hexGrid.getHexList().get(to);
                Hex tempHex2 = hexGrid.getMatchedHex(new Hex(tempHex.getX() - 1, tempHex.getZ()));
                Hex tempHex3 = hexGrid.getMatchedHex(new Hex(tempHex.getX() - 2, tempHex.getZ()));
                int to2 = hexGrid.getBallAt(tempHex2.getBall());
                int to3 = hexGrid.getBallAt(tempHex3.getBall());
                Ball tempBall = tempHex2.getBall();
                Ball tempBall2 = tempHex3.getBall();
                if (isLegal(from3, to3)) {
                    hexGrid.getHexList().get(to).setBall(selected.get(2));
                    hexGrid.getHexList().get(to2).setBall(selected.get(1));
                    hexGrid.getHexList().get(to3).setBall(selected.get(0));
                    hexGrid.getHexList().get(from).setBall(ballTo);
                    hexGrid.getHexList().get(from2).setBall(tempBall);
                    hexGrid.getHexList().get(from3).setBall(tempBall2);
                    movePerformed = true;
                    selected.clear();
                }
            }
            else if(hexGrid.getHexList().get(from).getZ() == hexGrid.getHexList().get(from3).getZ() &&
                    hexGrid.getHexList().get(to).getZ() == hexGrid.getHexList().get(from3).getZ()) {
                if(isLegal(from3,to)){
                    moveThree(ballTo, from, to, from2, from3);
                    selected.clear();
                }
            }
            else{
                if (isLegal(from3, to)) {
                    if(hexGrid.getHexList().get(from3).getX() == hexGrid.getHexList().get(from2).getX()){
                        if (hexGrid.getHexList().get(from3).getX() == hexGrid.getHexList().get(to).getX()) {
                            moveThree(ballTo, from, to, from2, from3);
                            selected.clear();
                        }
                        else{
                            System.out.println("Snake MOVE!");
                        }
                    }
                    else {
                        if (hexGrid.getHexList().get(from3).getY() == hexGrid.getHexList().get(to).getY()) {
                            moveThree(ballTo, from, to, from2, from3);
                            selected.clear();
                        } else {
                            System.out.println("Snake MOVE!");
                        }
                    }
                }
            }
        }

    }

    private void moveThree(Ball ballTo, int from, int to, int from2, int from3) {
        hexGrid.getHexList().get(to).setBall(selected.get(2));
        hexGrid.getHexList().get(from3).setBall(selected.get(1));
        hexGrid.getHexList().get(from2).setBall(selected.get(0));
        hexGrid.getHexList().get(from).setBall(ballTo);
        movePerformed = true;
    }

    public Boolean isModified() {
        return this.isModified;
    }

    public boolean isLegal(int hexFrom, int hexTo) {
        boolean legal = false;
        List<Turn> tempList = turnsFinder.findTurns(hexGrid.getHexList().get(hexFrom));
        outerloop:
        for (Turn turn : tempList) {
            for (int j = 0; j < turn.getMovesList().size(); j++) {
                List<Move> tempMoveList = turn.getMovesList();
                if (tempMoveList.get(j).getDestination() == hexGrid.getHexList().get(hexTo)
                        && tempMoveList.get(j).getStart() == hexGrid.getHexList().get(hexFrom)) {
                    legal = true;
                    break outerloop;
                }
            }
        }
        return legal;
    }

    public void pushBalls(Ball ballTo){
        organizeSelected(ballTo);
        int sel1 = hexGrid.getBallAt(selected.get(0));
        Hex hex1 = hexGrid.getHexList().get(sel1);
        int sel2 = hexGrid.getBallAt(selected.get(1));
        Hex hex2 = hexGrid.getHexList().get(sel2);

        int to = hexGrid.getBallAt(ballTo);
        Hex hexBallTo = hexGrid.getHexList().get(to);
        if(hex1.getX() == hexBallTo.getX() || hex1.getY() == hexBallTo.getY() || hex1.getZ() == hexBallTo.getZ() ){

            if(selected.size() == 2){
                int dif = hex1.getZ()- hex2.getZ();
                Hex emptyNeighbor = null;
                if(hex1.getX() == hexBallTo.getX()) {
                    emptyNeighbor = hexGrid.getMatchedHex(new Hex(hexBallTo.getX(), hexBallTo.getZ() - dif));
                }else if(hex1.getY() == hexBallTo.getY()){
                    emptyNeighbor = hexGrid.getMatchedHex(new Hex(hexBallTo.getX() + dif, hexBallTo.getZ() - dif));
                }else if(hex1.getZ() == hexBallTo.getZ()){
                    int dif2 = hex1.getX()- hex2.getX();
                    emptyNeighbor = hexGrid.getMatchedHex(new Hex(hexBallTo.getX() - dif2, hexBallTo.getZ()));
                    System.out.println(emptyNeighbor);
                }
                try {
                    Ball tempBall2 = emptyNeighbor.getBall();
                    if (tempBall2.getColor().isBlank()) {
                        int from2 = hexGrid.getBallAt(tempBall2);
                        hexGrid.getHexList().get(to).setBall(tempBall2);
                        hexGrid.getHexList().get(from2).setBall(ballTo);
                        System.out.println("PUSHED");
                        move(tempBall2);
                    }
                }catch (Exception e){
                    System.out.println("OUT OF BOARD");
                    ballTo.setColor(Color.BLANK);

                    if(selected.get(0).getColor().isBlue()){
                        PlayState.lostP += 1;
                    }else{
                        PlayState.lostB += 1;
                    }
                    move(ballTo);

                }
            }

            if(selected.size() == 3){
                int dif = hex1.getZ()- hex2.getZ();
                Hex emptyNeighbor = null;
                Hex nonEmptyNeighbor = null;
                if(hex1.getX() == hexBallTo.getX()) {

                    emptyNeighbor = hexGrid.getMatchedHex(new Hex(hexBallTo.getX(), hexBallTo.getZ() - 2*dif));
                    nonEmptyNeighbor = hexGrid.getMatchedHex(new Hex(hexBallTo.getX(), hexBallTo.getZ() - dif));
                }else if(hex1.getY() == hexBallTo.getY()){

                    emptyNeighbor = hexGrid.getMatchedHex(new Hex(hexBallTo.getX() + 2*dif, hexBallTo.getZ() - 2*dif));
                    nonEmptyNeighbor = hexGrid.getMatchedHex(new Hex(hexBallTo.getX() + dif, hexBallTo.getZ() - dif));
                }else if(hex1.getZ() == hexBallTo.getZ()){
                    int dif2 = hex1.getX()- hex2.getX();
                    emptyNeighbor = hexGrid.getMatchedHex(new Hex(hexBallTo.getX() - 2*dif2, hexBallTo.getZ()));
                    nonEmptyNeighbor = hexGrid.getMatchedHex(new Hex(hexBallTo.getX() - dif2, hexBallTo.getZ()));
                }

                System.out.println(nonEmptyNeighbor);

                try {
                    if(!nonEmptyNeighbor.getBall().getColor().isBlank()) {
                        Ball tempBall = emptyNeighbor.getBall();
                        int fromTemp = hexGrid.getBallAt(tempBall);
                        Ball tempBall2 = nonEmptyNeighbor.getBall();
                        int fromTemp2 = hexGrid.getBallAt(tempBall2);
                        if (tempBall.getColor().isBlank() && !tempBall2.getColor().isBlank()) {
                            hexGrid.getHexList().get(to).setBall(tempBall);
                            hexGrid.getHexList().get(fromTemp).setBall(tempBall2);
                            hexGrid.getHexList().get(fromTemp2).setBall(ballTo);
                            System.out.println("PUSHED");
                            move(tempBall);
                        }
                    }else {

                        //tempball is blank so nonempty is empty lol
                        Ball tempBall = nonEmptyNeighbor.getBall();
                        int fromTemp = hexGrid.getBallAt(tempBall);
                        if (tempBall.getColor().isBlank()) {
                            hexGrid.getHexList().get(to).setBall(tempBall);
                            hexGrid.getHexList().get(fromTemp).setBall(ballTo);
                            System.out.println("PUSHED");
                            move(tempBall);
                        }
                    }
                } catch (Exception e) {
                    System.out.println("OUT OF BOARD");
                    ballTo.setColor(Color.BLANK);

                    if(selected.get(0).getColor().isBlue()){
                        PlayState.lostP += 1;
                    }else{
                        PlayState.lostB += 1;
                    }
                    move(ballTo);
                }
            }
        }
    }


    public void setIsModified(Boolean isModified) {
        this.isModified = isModified;
    }

    public void setIsModified() {
        this.setIsModified(true);
    }

    public void setMovePerformed(boolean movePerformed){
        this.movePerformed = movePerformed;
    }

    public boolean getMovePerformed(){
        return movePerformed;
    }
}