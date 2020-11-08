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
                hexGrid.getHexList().get(hexGrid.getBallAt(destinationBall)).setBall(startBall);
                hexGrid.getHexList().get(hexGrid.getBallAt(startBall)).setBall(destinationBall);
                break;
            case 1:
                moveList = move.getMovesList();
                start = moveList.get(0).getStart();
                Hex start2 = moveList.get(1).getStart();
                destination = moveList.get(0).getDestination();
                Hex destination2 = moveList.get(1).getDestination();
                startBall = start.getBall();
                Ball start2Ball = start2.getBall();
                destinationBall = destination.getBall();
                Ball destination2Ball = destination2.getBall();
                if ((startBall.getColor().isBlue() && destinationBall.getColor().isPurple()) ||
                        startBall.getColor().isPurple() && destinationBall.getColor().isBlue()) {
                    selected.clear();
                    selected.add(startBall);
                    selected.add(start2Ball);
                    pushBalls(destinationBall);
                } else if (destination2 == start) {
                    hexGrid.getHexList().get(hexGrid.getBallAt(destinationBall)).setBall(startBall);
                    hexGrid.getHexList().get(hexGrid.getBallAt(startBall)).setBall(start2Ball);
                    hexGrid.getHexList().get(hexGrid.getBallAt(start2Ball)).setBall(destinationBall);
                } else {
                    hexGrid.getHexList().get(hexGrid.getBallAt(destinationBall)).setBall(startBall);
                    hexGrid.getHexList().get(hexGrid.getBallAt(destination2Ball)).setBall(start2Ball);
                    hexGrid.getHexList().get(hexGrid.getBallAt(startBall)).setBall(destinationBall);
                    hexGrid.getHexList().get(hexGrid.getBallAt(start2Ball)).setBall(destination2Ball);
                }
                break;

            case 2:
                moveList = move.getMovesList();
                start = moveList.get(0).getStart();
                start2 = moveList.get(1).getStart();
                Hex start3 = moveList.get(2).getStart();
                destination = moveList.get(0).getDestination();
                destination2 = moveList.get(1).getDestination();
                Hex destination3 = moveList.get(2).getDestination();
                startBall = start.getBall();
                start2Ball = start2.getBall();
                Ball start3Ball = start3.getBall();
                destinationBall = destination.getBall();
                destination2Ball = destination2.getBall();
                Ball destination3Ball = destination3.getBall();
                if ((startBall.getColor().isBlue() && destinationBall.getColor().isPurple()) ||
                        startBall.getColor().isPurple() && destinationBall.getColor().isBlue()) {
                    selected.clear();
                    selected.add(startBall);
                    selected.add(start2Ball);
                    selected.add(start3Ball);
                    pushBalls(destinationBall);
                } else if (destination2 == start && destination3 == start2) {
                    hexGrid.getHexList().get(hexGrid.getBallAt(destinationBall)).setBall(startBall);
                    hexGrid.getHexList().get(hexGrid.getBallAt(startBall)).setBall(start2Ball);
                    hexGrid.getHexList().get(hexGrid.getBallAt(start2Ball)).setBall(start3Ball);
                    hexGrid.getHexList().get(hexGrid.getBallAt(start3Ball)).setBall(destinationBall);
                } else {
                    hexGrid.getHexList().get(hexGrid.getBallAt(destinationBall)).setBall(startBall);
                    hexGrid.getHexList().get(hexGrid.getBallAt(destination2Ball)).setBall(start2Ball);
                    hexGrid.getHexList().get(hexGrid.getBallAt(destination3Ball)).setBall(start3Ball);
                    hexGrid.getHexList().get(hexGrid.getBallAt(startBall)).setBall(destinationBall);
                    hexGrid.getHexList().get(hexGrid.getBallAt(start2Ball)).setBall(destination2Ball);
                    hexGrid.getHexList().get(hexGrid.getBallAt(start3Ball)).setBall(destination3Ball);

                }

                break;
        }
    }

    public void requestMove(Ball ballTo) {
        organizeSelected(ballTo);
        int from = hexGrid.getBallAt(selected.get(0));
        int to = hexGrid.getBallAt(ballTo);
        System.out.println(from + " to " + to);

        switch (selected.size()) {
            case 1:
                if (isLegal(from, to)) {
                    hexGrid.getHexList().get(to).setBall(selected.get(0));
                    hexGrid.getHexList().get(from).setBall(ballTo);
                    movePerformed = true;
                    selected.clear();
                }
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
                    System.out.println("Move performed!");
                    selected.clear();
                } else {
                    System.out.println("Illegal move!");
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
                    System.out.println("Move performed!");
                    selected.clear();
                } else {
                    System.out.println("Illegal move!");
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
                    //System.out.println(emptyNeighbor);
                }
                try {
                    Ball tempBall2 = emptyNeighbor.getBall();
                    if (tempBall2.getColor().isBlank()) {
                        int from2 = hexGrid.getBallAt(tempBall2);
                        hexGrid.getHexList().get(to).setBall(tempBall2);
                        hexGrid.getHexList().get(from2).setBall(ballTo);
                        System.out.println("PUSHED");
                        requestMove(tempBall2);
                    }
                } catch (Exception e) {
                    System.out.println("OUT OF BOARD");
                    ballTo.setColor(Color.BLANK);

                    if (selected.get(0).getColor().isBlue()) {
                        PlayState.lostP += 1;
                    } else {
                        PlayState.lostB += 1;
                    }
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

                //System.out.println(nonEmptyNeighbor);

                try {
                    if (!nonEmptyNeighbor.getBall().getColor().isBlank()) {
                        Ball tempBall = emptyNeighbor.getBall();
                        int fromTemp = hexGrid.getBallAt(tempBall);
                        Ball tempBall2 = nonEmptyNeighbor.getBall();
                        int fromTemp2 = hexGrid.getBallAt(tempBall2);
                        if (tempBall.getColor().isBlank() && !tempBall2.getColor().isBlank()) {
                            hexGrid.getHexList().get(to).setBall(tempBall);
                            hexGrid.getHexList().get(fromTemp).setBall(tempBall2);
                            hexGrid.getHexList().get(fromTemp2).setBall(ballTo);
                            System.out.println("PUSHED");
                            requestMove(tempBall);
                        }
                    } else {

                        //tempball is blank so nonempty is empty lol
                        Ball tempBall = nonEmptyNeighbor.getBall();
                        int fromTemp = hexGrid.getBallAt(tempBall);
                        if (tempBall.getColor().isBlank()) {
                            hexGrid.getHexList().get(to).setBall(tempBall);
                            hexGrid.getHexList().get(fromTemp).setBall(ballTo);
                            System.out.println("PUSHED");
                            requestMove(tempBall);
                        }
                    }
                } catch (Exception e) {
                    System.out.println("OUT OF BOARD");
                    ballTo.setColor(Color.BLANK);

                    if (selected.get(0).getColor().isBlue()) {
                        PlayState.lostP += 1;
                    } else {
                        PlayState.lostB += 1;
                    }
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

    public Object clone() throws CloneNotSupportedException {
        Board clonedBoard = (Board) super.clone();
        clonedBoard.selected = new ArrayList<>();
        clonedBoard.hexGrid = (HexGrid) this.hexGrid.clone();
        return clonedBoard;
    }
}