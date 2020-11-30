package com.abalone.game.objects;

import com.abalone.game.states.PlayState;
import com.abalone.game.utils.Color;
import com.abalone.game.utils.TurnsFinder;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

import java.util.ArrayList;
import java.util.List;

public class Board implements Cloneable {
    private Image board;
    private HexGrid hexGrid;
    private ArrayList<Ball> selected;
    private TurnsFinder turnsFinder;
    private boolean movePerformed;

    public Board() {
        this.movePerformed = false;
        Texture img = new Texture("abalone.png");
        this.board = new Image(img);

        hexGrid = new HexGrid();
        selected = new ArrayList<>();
        turnsFinder = new TurnsFinder(hexGrid);
    }

    //For TESTING
    public Board(int x) {
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

    public ArrayList<Hex> getBlueHex() {
        ArrayList<Hex> temp = new ArrayList<>();
        for (Hex h : hexGrid.getHexList()) {
            if (h.isOccupied() && h.getBall().getColor() == Color.BLUE) temp.add(h);
        }
        return temp;
    }

    public ArrayList<Hex> getPurpleHex() {
        ArrayList<Hex> temp = new ArrayList<>();
        for (Hex h : hexGrid.getHexList()) {
            if (h.isOccupied() && h.getBall().getColor() == Color.PURPLE) temp.add(h);
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
    }

    public void move(Turn move) {
        switch (move.getTurnType()) {
            case 0:
                List<Move> moveList = move.getMovesList();
                Hex start = moveList.get(0).getStart();
                Hex destination = moveList.get(0).getDestination();
                Ball startBall = start.getBall();
                Ball destinationBall = destination.getBall();
                selected.clear();
                selected.add(hexGrid.getMatchedHex(start).getBall());
                requestMove(hexGrid.getMatchedHex(destination).getBall());
                break;
            case 1:
                moveList = move.getMovesList();
                start = moveList.get(0).getStart();
                Hex start2 = moveList.get(1).getStart();
                destination = moveList.get(0).getDestination();
                startBall = start.getBall();
                destinationBall = destination.getBall();
                if ((startBall.getColor().isBlue() && destinationBall.getColor().isPurple()) ||
                        startBall.getColor().isPurple() && destinationBall.getColor().isBlue()) {
                    if((start.getBall().getColor().isPurple() && start2.getBall().getColor().isPurple()) ||
                            (start.getBall().getColor().isBlue() && start2.getBall().getColor().isBlue())) {
                        selected.clear();
                        selected.add(hexGrid.getMatchedHex(start).getBall());
                        selected.add(hexGrid.getMatchedHex(start2).getBall());
                        pushBalls(hexGrid.getMatchedHex(destination).getBall());
                    }
                    else{
                        System.out.println("fuck off");
                        System.out.println("The move was : ");
                        System.out.println("Start 1 X : " + start.getX() + " Z:" + start.getZ() + " COLOR :" + start.getBall().getColor());
                        System.out.println("Start 2 X : " + start2.getX() + " Z:" + start2.getZ() + " COLOR : " + start2.getBall().getColor());
                        System.out.println("Destination  X : " + destination.getX() + " Z:" + destination.getZ() + " COLOR : " + destination.getBall().getColor());
                    }
                } else {
                    selected.clear();
                    selected.add(hexGrid.getMatchedHex(start).getBall());
                    selected.add(hexGrid.getMatchedHex(start2).getBall());
                    requestMove(hexGrid.getMatchedHex(destination).getBall());
                }
                break;

            case 2:
                moveList = move.getMovesList();
                start = moveList.get(0).getStart();
                start2 = moveList.get(1).getStart();
                Hex start3 = moveList.get(2).getStart();
                destination = moveList.get(0).getDestination();
                startBall = start.getBall();
                destinationBall = destination.getBall();
                if ((startBall.getColor().isBlue() && destinationBall.getColor().isPurple()) ||
                        startBall.getColor().isPurple() && destinationBall.getColor().isBlue()) {
                    if((start.getBall().getColor().isPurple() && start2.getBall().getColor().isPurple() && start3.getBall().getColor().isPurple()) ||
                            (start.getBall().getColor().isBlue() && start2.getBall().getColor().isBlue() && start3.getBall().getColor().isBlue())) {
                        selected.clear();
                        selected.add(hexGrid.getMatchedHex(start).getBall());
                        selected.add(hexGrid.getMatchedHex(start2).getBall());
                        selected.add(hexGrid.getMatchedHex(start3).getBall());
                        pushBalls(hexGrid.getMatchedHex(destination).getBall());
                    }
                    else{
                        System.out.println("fuck off");
                        System.out.println("The move was : ");
                        System.out.println("Start 1 X : " + start.getX() + " Z:" + start.getZ() + " COLOR :" + start.getBall().getColor());
                        System.out.println("Start 2 X : " + start2.getX() + " Z:" + start2.getZ() + " COLOR : " + start2.getBall().getColor());
                        System.out.println("Start 3 X : " + start3.getX() + " Z:" + start3.getZ() + " COLOR : " + start3.getBall().getColor());
                        System.out.println("Destination  X : " + destination.getX() + " Z:" + destination.getZ() + " COLOR : " + destination.getBall().getColor());
                    }
                } else {
                    selected.clear();
                    selected.add(hexGrid.getMatchedHex(start).getBall());
                    selected.add(hexGrid.getMatchedHex(start2).getBall());
                    selected.add(hexGrid.getMatchedHex(start3).getBall());
                    requestMove(hexGrid.getMatchedHex(destination).getBall());
                }
                break;
        }
    }

    public void requestMove(Ball ballTo) {
        organizeSelected(ballTo);
        int from = hexGrid.getBallAt(selected.get(0));
        int to = hexGrid.getBallAt(ballTo);
        //System.out.println(from + " to " + to);

        switch (selected.size()) {
            case 1:
                if (isLegal(from, to)) {
                    hexGrid.getHexList().get(to).setBall(selected.get(0));
                    hexGrid.getHexList().get(from).setBall(ballTo);
                    movePerformed = true;
                }
                selected.clear();
                break;
            case 2:
                int from2 = hexGrid.getBallAt(selected.get(1));
                Turn decidedMove = findDoubleMove(from2, to, from);
                if (decidedMove != null) {
                    List<Move> moveList = decidedMove.getMovesList();
                    Hex secondDestination = moveList.get(1).getDestination();
                    Ball secondBall = secondDestination.getBall();
                    if (secondDestination == hexGrid.getHexList().get(from2)) {
                        hexGrid.getHexList().get(to).setBall(selected.get(1));
                        hexGrid.getHexList().get(from2).setBall(selected.get(0));
                        hexGrid.getHexList().get(from).setBall(ballTo);
                    } else {
                        hexGrid.getHexList().get(to).setBall(selected.get(1));
                        hexGrid.getHexList().get(hexGrid.getBallAt(secondBall)).setBall(selected.get(0));
                        hexGrid.getHexList().get(from).setBall(ballTo);
                        hexGrid.getHexList().get(from2).setBall(secondBall);
                    }

                    movePerformed = true;
                    //System.out.println("Move performed!");
                    selected.clear();
                } else {
                    //System.out.println("Illegal move!");
                    selected.clear();
                }

                break;
            case 3:
                from2 = hexGrid.getBallAt(selected.get(1));
                int from3 = hexGrid.getBallAt(selected.get(2));
                Turn decidedTripleMove = findTripleMove(from3, to, from2, from);
                if (decidedTripleMove != null) {
                    List<Move> moveList = decidedTripleMove.getMovesList();
                    Hex secondDestination = moveList.get(1).getDestination();
                    Hex thirdDestination = moveList.get(2).getDestination();
                    Ball secondBall = secondDestination.getBall();
                    Ball thirdBall = thirdDestination.getBall();
                    if (secondDestination == hexGrid.getHexList().get(from3) && thirdDestination == hexGrid.getHexList().get(from2)) {
                        hexGrid.getHexList().get(to).setBall(selected.get(2));
                        hexGrid.getHexList().get(from3).setBall(selected.get(1));
                        hexGrid.getHexList().get(from2).setBall(selected.get(0));
                        hexGrid.getHexList().get(from).setBall(ballTo);
                    } else {
                        hexGrid.getHexList().get(to).setBall(selected.get(2));
                        hexGrid.getHexList().get(hexGrid.getBallAt(secondBall)).setBall(selected.get(1));
                        hexGrid.getHexList().get(hexGrid.getBallAt(thirdBall)).setBall(selected.get(0));
                        hexGrid.getHexList().get(from).setBall(ballTo);
                        hexGrid.getHexList().get(from2).setBall(secondBall);
                        hexGrid.getHexList().get(from3).setBall(thirdBall);
                    }

                    movePerformed = true;
                    //System.out.println("Move performed!");
                    selected.clear();
                } else {
                    //System.out.println("Illegal move!");
                    selected.clear();
                }
                break;
        }
    }


    public Turn findDoubleMove(int from, int to, int from2) {
        Turn foundTurn = null;
        List<Turn> tempList = turnsFinder.findTurns(hexGrid.getHexList().get(from));
        for (Turn turn : tempList) {
            if (turn.getTurnType() == 1) {
                List<Move> tempMoveList = turn.getMovesList();
                if (tempMoveList.get(0).getStart() == hexGrid.getHexList().get(from) &&
                        tempMoveList.get(0).getDestination() == hexGrid.getHexList().get(to) &&
                        tempMoveList.get(1).getStart() == hexGrid.getHexList().get(from2)) {
                    foundTurn = turn;
                    break;
                }
            }
        }
        turnsFinder.clearTurns();
        return foundTurn;
    }

    public Turn findTripleMove(int from, int to, int from2, int from3) {
        Turn foundTurn = null;
        List<Turn> tempList = turnsFinder.findTurns(hexGrid.getHexList().get(from));
        for (Turn turn : tempList) {
            if (turn.getTurnType() == 2) {
                List<Move> tempMoveList = turn.getMovesList();
                if (tempMoveList.get(0).getStart() == hexGrid.getHexList().get(from) &&
                        tempMoveList.get(0).getDestination() == hexGrid.getHexList().get(to) &&
                        tempMoveList.get(1).getStart() == hexGrid.getHexList().get(from2) &&
                        tempMoveList.get(2).getStart() == hexGrid.getHexList().get(from3)) {
                    foundTurn = turn;
                    break;
                }
            }
        }
        turnsFinder.clearTurns();
        return foundTurn;
    }

    public boolean isLegal(int hexFrom, int hexTo) {
        boolean legal = false;
        List<Turn> tempList = turnsFinder.findTurns(hexGrid.getHexList().get(hexFrom));
        for (Turn turn : tempList) {
            if (turn.getTurnType() == 0) {
                List<Move> tempMoveList = turn.getMovesList();
                if (tempMoveList.get(0).getDestination() == hexGrid.getHexList().get(hexTo)
                        && tempMoveList.get(0).getStart() == hexGrid.getHexList().get(hexFrom)) {
                    legal = true;
                    break;
                }
            }
        }
        turnsFinder.clearTurns();
        return legal;
    }


    public void pushBalls(Ball ballTo) {
        organizeSelected(ballTo);
        int sel1 = hexGrid.getBallAt(selected.get(0));
        Hex hex1 = hexGrid.getHexList().get(sel1);
        int sel2 = hexGrid.getBallAt(selected.get(1));
        Hex hex2 = hexGrid.getHexList().get(sel2);

        int to = hexGrid.getBallAt(ballTo);
        Hex hexBallTo = hexGrid.getHexList().get(to);
        if (hex1.getX() == hexBallTo.getX() || hex1.getY() == hexBallTo.getY() || hex1.getZ() == hexBallTo.getZ()) {

            if (selected.size() == 2) {
                int dif = hex1.getZ() - hex2.getZ();
                Hex emptyNeighbor = null;
                if (hex1.getX() == hexBallTo.getX()) {
                    emptyNeighbor = hexGrid.getMatchedHex(new Hex(hexBallTo.getX(), hexBallTo.getZ() - dif));
                } else if (hex1.getY() == hexBallTo.getY()) {
                    emptyNeighbor = hexGrid.getMatchedHex(new Hex(hexBallTo.getX() + dif, hexBallTo.getZ() - dif));
                } else if (hex1.getZ() == hexBallTo.getZ()) {
                    int dif2 = hex1.getX() - hex2.getX();
                    emptyNeighbor = hexGrid.getMatchedHex(new Hex(hexBallTo.getX() - dif2, hexBallTo.getZ()));
                }
                if(emptyNeighbor!= null && emptyNeighbor.getBall() != null){
                    Ball tempBall2 = emptyNeighbor.getBall();
                    if (tempBall2.getColor().isBlank()) {
                        int from2 = hexGrid.getBallAt(tempBall2);
                        hexGrid.getHexList().get(to).setBall(tempBall2);
                        hexGrid.getHexList().get(from2).setBall(ballTo);
                        requestMove(tempBall2);
                    }
                } else if(emptyNeighbor == null) {
                    //System.out.println("OUT OF BOARD");
                    //ballTo = new Ball(Color.BLANK, ballTo.getId());
                    ballTo = hexBallTo.getBall();
                    ballTo.setColor(Color.BLANK);
                    requestMove(ballTo);
                }
            }
            if (selected.size() == 3) {
                int dif = hex1.getZ() - hex2.getZ();
                Hex emptyNeighbor = null;
                Hex nonEmptyNeighbor = null;
                if (hex1.getX() == hexBallTo.getX()) {
                    emptyNeighbor = hexGrid.getMatchedHex(new Hex(hexBallTo.getX(), hexBallTo.getZ() - 2 * dif));
                    nonEmptyNeighbor = hexGrid.getMatchedHex(new Hex(hexBallTo.getX(), hexBallTo.getZ() - dif));
                } else if (hex1.getY() == hexBallTo.getY()) {
                    emptyNeighbor = hexGrid.getMatchedHex(new Hex(hexBallTo.getX() + 2 * dif, hexBallTo.getZ() - 2 * dif));
                    nonEmptyNeighbor = hexGrid.getMatchedHex(new Hex(hexBallTo.getX() + dif, hexBallTo.getZ() - dif));
                } else if (hex1.getZ() == hexBallTo.getZ()) {
                    int dif2 = hex1.getX() - hex2.getX();
                    emptyNeighbor = hexGrid.getMatchedHex(new Hex(hexBallTo.getX() - 2 * dif2, hexBallTo.getZ()));
                    nonEmptyNeighbor = hexGrid.getMatchedHex(new Hex(hexBallTo.getX() - dif2, hexBallTo.getZ()));
                }

                if(nonEmptyNeighbor!= null && emptyNeighbor!=null
                        && emptyNeighbor.getBall()!= null && nonEmptyNeighbor.getBall()!=null){
                    if(!nonEmptyNeighbor.getBall().getColor().isBlank() && emptyNeighbor.getBall().getColor().isBlank()) {
                        Ball tempBall = emptyNeighbor.getBall();
                        int fromTemp = hexGrid.getBallAt(tempBall);
                        Ball tempBall2 = nonEmptyNeighbor.getBall();
                        int fromTemp2 = hexGrid.getBallAt(tempBall2);
                        hexGrid.getHexList().get(to).setBall(tempBall);
                        hexGrid.getHexList().get(fromTemp).setBall(tempBall2);
                        hexGrid.getHexList().get(fromTemp2).setBall(ballTo);
                        requestMove(tempBall);
                    }else{
                        Ball tempBall2 = nonEmptyNeighbor.getBall();
                        int fromTemp2 = hexGrid.getBallAt(tempBall2);
                        hexGrid.getHexList().get(to).setBall(tempBall2);
                        hexGrid.getHexList().get(fromTemp2).setBall(ballTo);
                        requestMove(tempBall2);
                    }

                }
                else if(nonEmptyNeighbor!= null && emptyNeighbor == null && nonEmptyNeighbor.getBall()!=null) {
                    if (nonEmptyNeighbor.getBall().getColor().isBlank()) {

                        Ball tempBall = nonEmptyNeighbor.getBall();
                        int fromTemp = hexGrid.getBallAt(tempBall);
                        if (tempBall.getColor().isBlank()) {
                            hexGrid.getHexList().get(to).setBall(tempBall);
                            hexGrid.getHexList().get(fromTemp).setBall(ballTo);
                            requestMove(tempBall);
                        }
                    }else{
                        ballTo = hexBallTo.getBall();
                        ballTo.setColor(Color.BLANK);
                        requestMove(ballTo);
                    }
                }
                else if((nonEmptyNeighbor == null && emptyNeighbor == null)) {

                    //ballTo = new Ball(Color.BLANK, ballTo.getId());
                    ballTo = hexBallTo.getBall();
                    ballTo.setColor(Color.BLANK);
                    requestMove(ballTo);
                }
            }
        }
    }

    public void setMovePerformed(boolean movePerformed) {
        this.movePerformed = movePerformed;
    }

    public boolean getMovePerformed() {
        return movePerformed;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        Board clonedBoard = (Board) super.clone();
        clonedBoard.selected = new ArrayList<>();
        clonedBoard.hexGrid = (HexGrid) this.hexGrid.clone();
        clonedBoard.turnsFinder = new TurnsFinder(clonedBoard.hexGrid);
        return clonedBoard;
    }
}