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
    private boolean pushPossible;

    public Board() {
        this.movePerformed = false;
        this.pushPossible = true;
        Texture img = new Texture("abalone.png");
        this.board = new Image(img);

        hexGrid = new HexGrid();
        selected = new ArrayList<>();
        turnsFinder = new TurnsFinder(hexGrid);
    }

    public Board(HexGrid grid) {
        this.movePerformed = false;
        this.hexGrid = grid;
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
                if (!start.getBall().getColor().isBlank() && destination.getBall().getColor().isBlank()) {
                    selected.add(hexGrid.getMatchedHex(start).getBall());
                    requestMove(hexGrid.getMatchedHex(destination).getBall());
                }
                break;
            case 1:
                moveList = move.getMovesList();
                start = moveList.get(0).getStart();
                Hex start2 = moveList.get(1).getStart();
                destination = moveList.get(0).getDestination();
                startBall = start.getBall();
                destinationBall = destination.getBall();
                selected.clear();
                if ((startBall.getColor().isBlue() && destinationBall.getColor().isPurple()) ||
                        startBall.getColor().isPurple() && destinationBall.getColor().isBlue()) {

                    selected.add(hexGrid.getMatchedHex(start).getBall());
                    selected.add(hexGrid.getMatchedHex(start2).getBall());
                    pushBalls(hexGrid.getMatchedHex(destination).getBall());

                } else {
                    selected.clear();
                    if ((start.getBall().getColor() == start2.getBall().getColor()) && destination.getBall().getColor().isBlank()) {
                        selected.add(hexGrid.getMatchedHex(start).getBall());
                        selected.add(hexGrid.getMatchedHex(start2).getBall());
                        Hex dest2 = moveList.get(1).getDestination();
                        if ((destination.getZ() == dest2.getZ() && destination.getZ() - start.getZ() == 1 && start.getX() < start2.getX())
                                || (destination.getZ() == dest2.getZ() && destination.getZ() - start.getZ() == -1 && start.getX() > start2.getX())
                                || (destination.getY() == dest2.getY() && destination.getZ() - start.getZ() == 1 && start.getX() > start2.getX())
                                || (destination.getY() == dest2.getY() && destination.getZ() - start.getZ() == -1 && start.getX() < start2.getX())) {
                            requestMove(hexGrid.getMatchedHex(dest2).getBall());
                        } else {
                            requestMove(hexGrid.getMatchedHex(destination).getBall());
                        }

                    }
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
                selected.clear();
                if ((startBall.getColor().isBlue() && destinationBall.getColor().isPurple()) ||
                        startBall.getColor().isPurple() && destinationBall.getColor().isBlue()) {
                    selected.add(hexGrid.getMatchedHex(start).getBall());
                    selected.add(hexGrid.getMatchedHex(start2).getBall());
                    selected.add(hexGrid.getMatchedHex(start3).getBall());
                    pushBalls(hexGrid.getMatchedHex(destination).getBall());
                } else {
                    selected.clear();
                    if ((start.getBall().getColor() == start2.getBall().getColor() && start2.getBall().getColor()
                            == start3.getBall().getColor()) && destination.getBall().getColor().isBlank()) {
                        selected.add(hexGrid.getMatchedHex(start).getBall());
                        selected.add(hexGrid.getMatchedHex(start2).getBall());
                        selected.add(hexGrid.getMatchedHex(start3).getBall());
                        Hex dest2 = moveList.get(1).getDestination();
                        Hex dest3 = moveList.get(2).getDestination();
                        if ((destination.getZ() == dest2.getZ() && destination.getZ() - start.getZ() == 1 && start.getX() < start2.getX())
                                || (destination.getZ() == dest2.getZ() && destination.getZ() - start.getZ() == -1 && start.getX() > start2.getX())
                                || (destination.getY() == dest2.getY() && destination.getZ() - start.getZ() == 1 && start.getX() > start2.getX())
                                || (destination.getY() == dest2.getY() && destination.getZ() - start.getZ() == -1 && start.getX() < start2.getX())) {
                            requestMove(hexGrid.getMatchedHex(dest3).getBall());
                        } else {
                            requestMove(hexGrid.getMatchedHex(destination).getBall());
                        }
                    }
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

        int to = hexGrid.getBallAt(ballTo);
        Hex hexBallTo = hexGrid.getHexList().get(to);

        organizeSelected(ballTo);

        int sel1 = hexGrid.getBallAt(selected.get(0));
        Hex hex1 = hexGrid.getHexList().get(sel1);

        int sel2 = hexGrid.getBallAt(selected.get(1));
        Hex hex2 = hexGrid.getHexList().get(sel2);


        if (hex1.getX() == hexBallTo.getX() || hex1.getY() == hexBallTo.getY() || hex1.getZ() == hexBallTo.getZ()) {

            if (selected.size() == 2) {
                int dif = hex1.getZ() - hex2.getZ();
                //System.out.println(dif);
                Hex emptyNeighbor = null;
                if (hex1.getX() == hexBallTo.getX()) {

                    emptyNeighbor = hexGrid.getMatchedHex(new Hex(hexBallTo.getX(), hexBallTo.getZ() - dif));
                } else if (hex1.getY() == hexBallTo.getY()) {
                    emptyNeighbor = hexGrid.getMatchedHex(new Hex(hexBallTo.getX() + dif, hexBallTo.getZ() - dif));
                } else if (hex1.getZ() == hexBallTo.getZ()) {
                    int dif2 = hex1.getX() - hex2.getX();
                    emptyNeighbor = hexGrid.getMatchedHex(new Hex(hexBallTo.getX() - dif2, hexBallTo.getZ()));
                }
                if (emptyNeighbor != null && emptyNeighbor.getBall() != null) {
                    Ball tempBall2 = emptyNeighbor.getBall();
                    //System.out.println(emptyNeighbor.toString());
                    if (tempBall2.getColor().isBlank()) {
                        int from2 = hexGrid.getBallAt(tempBall2);
                        hexGrid.getHexList().get(from2).setBall(hexBallTo.getBall());
                        hexGrid.getHexList().get(to).setBall(tempBall2);
                        //System.out.println("moved");
                        requestMove(tempBall2);
                    } else {
                        pushPossible = false;
                        //System.out.println("not possible");
                        //selected.clear();
                    }
                } else if (emptyNeighbor == null) {

                    Ball ballTo2 = hexBallTo.getBall();
                    ballTo2.setColor(Color.BLANK);
                    requestMove(ballTo2);
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

                if (nonEmptyNeighbor != null && emptyNeighbor != null
                        && emptyNeighbor.getBall() != null && nonEmptyNeighbor.getBall() != null) {
                    if (!nonEmptyNeighbor.getBall().getColor().isBlank() && emptyNeighbor.getBall().getColor().isBlank()
                            && !selected.get(0).getColor().equals(nonEmptyNeighbor.getBall().getColor())) {
                        Ball tempBall = emptyNeighbor.getBall();
                        int fromTemp = hexGrid.getBallAt(tempBall);
                        Ball tempBall2 = nonEmptyNeighbor.getBall();
                        int fromTemp2 = hexGrid.getBallAt(tempBall2);

                        hexGrid.getHexList().get(fromTemp2).setBall(hexBallTo.getBall());
                        hexGrid.getHexList().get(fromTemp).setBall(tempBall2);
                        hexGrid.getHexList().get(to).setBall(tempBall);

                        requestMove(tempBall);
                        //SOLVED SANDWICH MOVE
                    } else if (nonEmptyNeighbor.getBall().getColor().isBlank()) {
                        Ball tempBall2 = nonEmptyNeighbor.getBall();
                        int fromTemp2 = hexGrid.getBallAt(tempBall2);
                        hexGrid.getHexList().get(fromTemp2).setBall(hexBallTo.getBall());
                        hexGrid.getHexList().get(to).setBall(tempBall2);
                        requestMove(tempBall2);
                    } else {
                        pushPossible = false;
                        //selected.clear();
                    }

                } else if (nonEmptyNeighbor != null && emptyNeighbor == null && nonEmptyNeighbor.getBall() != null) {
                    if (nonEmptyNeighbor.getBall().getColor().isBlank()) {

                        Ball tempBall = nonEmptyNeighbor.getBall();
                        int fromTemp = hexGrid.getBallAt(tempBall);
                        if (tempBall.getColor().isBlank()) {
                            hexGrid.getHexList().get(fromTemp).setBall(hexBallTo.getBall());
                            hexGrid.getHexList().get(to).setBall(tempBall);
                            requestMove(tempBall);
                        }
                    } else if (nonEmptyNeighbor.getBall().getColor().equals(ballTo.getColor())) {
                        //ballTo = new Ball(Color.BLANK, to);
                        Ball ballTo2 = hexBallTo.getBall();
                        ballTo2.setColor(Color.BLANK);
                        requestMove(ballTo2);
                    } else {
                        pushPossible = false;
                    }
                } else if ((nonEmptyNeighbor == null && emptyNeighbor == null)) {

                    //ballTo = new Ball(Color.BLANK, to);
                    Ball ballTo2 = hexBallTo.getBall();
                    ballTo2.setColor(Color.BLANK);
                    requestMove(ballTo2);
                }
            }
        }
    }

    public boolean getMovePerformed() {
        return movePerformed;
    }

    public void setMovePerformed(boolean movePerformed) {
        this.movePerformed = movePerformed;
    }

    public boolean getPushPossible() {
        return pushPossible;
    }

    public void setPushPossible(boolean pushPossible) {
        this.pushPossible = pushPossible;
    }

    public String getKey() {
        List<Hex> list = this.getHexGrid().getHexList();
        String key = "";
        Character c = ' ';
        for(Hex h : list) {
            Ball ball = h.getBall();
            switch (ball.getColor()) {
                case BLANK:
                    c = 'W';
                    break;
                case BLUE:
                    c = 'B';
                    break;
                case PURPLE:
                    c = 'P';
                    break;

            }
            key = key + c;
        }
        return key;
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