package com.abalone.game.gameTree;

import com.abalone.game.objects.*;
import com.abalone.game.utils.Color;
import com.abalone.game.utils.TurnsFinder;

import java.util.ArrayList;
import java.util.List;

public class Node {
    public static ArrayList<Turn> theListRemember = new ArrayList<>();
    private Board board;
    private List<Node> children;
    private Node parent;
    private int depth;
    private double value;
    private Heuristics heuristics;
    private Turn turn;
    private Color playerColorToPlay;
    private int numberBlueBalls;
    private int numberPurpleBalls;
    private double[] weights;
    // MCTS Stuff
    private double winScore = 0;
    private int visitCount = 0;


    public Node(Board board, int depthTree, int depth, Turn turn, Color playerColorToPlay, Heuristics heuristics) {
        this.board = board;
        this.depth = depth;
        this.turn = turn;
        this.playerColorToPlay = playerColorToPlay;
        this.children = new ArrayList();
        this.numberBlueBalls = board.getBlueHex().size();
        this.numberPurpleBalls = board.getPurpleHex().size();
        this.weights = heuristics.getWeights();
        this.heuristics = new Heuristics(this.board, playerColorToPlay, weights[0], weights[1], weights[2]);
        this.calculateHeuristicsValue();


        // setHeuristicsValue(heuristics.getValue());
        if (depthTree > depth) {
            // If the depth of this node is even, this means it's the state of the board after a move of the human player
            // So we get the purple balls (balls of the AI) because it is the turn of the AI to play
            // Otherwise we take the blue balls because it is the turn of the human to play
            List<Hex> hexes;
            TurnsFinder turnsFinder = new TurnsFinder(board.getHexGrid());
            if (playerColorToPlay == Color.PURPLE) {
                hexes = board.getPurpleHex();
            } else {
                hexes = board.getBlueHex();
            }

            turnsFinder.clearTurns();
            for (Hex hex : hexes) {
                // calculate all turns for each hex
                turnsFinder.findTurns(hex);
            }
            // get every calculated turns for all hexes
            List<List<Turn>> allTurns = turnsFinder.getTurns();

            if (theListRemember.size() > 100) {
                theListRemember.remove(0);
            }
            try {
                Color nextColor = (playerColorToPlay == Color.BLUE) ? Color.PURPLE : Color.BLUE;
                for (List<Turn> ts : allTurns) {
                    for (Turn t : ts) {
                        if (!doneBefore(t)) {
                            Board newBoard = (Board) board.clone();
                            newBoard.move(t);
                            if (newBoard.getPushPossible()) {
                                Node newNode = new Node(newBoard, depthTree, depth + 1, t, nextColor, heuristics);
                                this.addChild(newNode);
                            }
                        }
                    }
                }
            } catch (CloneNotSupportedException e) {
                System.out.println("Clone exception");
            }
        }
    }

    public void addChild(Node child) {
        child.setParent(this);
        this.children.add(child);
    }

    public boolean legalTurn(Turn currentTurn) {
        boolean legal = true;
        if (currentTurn != null) {
            List<Move> moveList = currentTurn.getMovesList();
            if (currentTurn.getTurnType() == 0) {
                if (moveList.get(0).getStart().getBall().getColor().isBlank() || !moveList.get(0).getDestination().getBall().getColor().isBlank()) {
                    legal = false;
                }
            } else if (currentTurn.getTurnType() == 1) {
                Ball start1 = moveList.get(0).getStart().getBall();
                Ball start2 = moveList.get(1).getStart().getBall();
                if (!(start1.getColor() == start2.getColor())) {
                    legal = false;
                }
            } else {
                Ball start1 = moveList.get(0).getStart().getBall();
                Ball start2 = moveList.get(1).getStart().getBall();
                Ball start3 = moveList.get(2).getStart().getBall();
                if (!(start1.getColor() == start2.getColor()) || !(start2.getColor() == start3.getColor())) {
                    legal = false;
                }
            }
        }
        return legal;
    }

    public boolean doneBefore(Turn turn) {
        if (turn != null) {
            List<Move> moveList = turn.getMovesList();
            for (Turn t : theListRemember) {
                if (turn.getTurnType() == 0 && t.getTurnType() == 0) {
                    int startt1 = t.getMovesList().get(0).getStart().getBall().getId();
                    int turns1 = moveList.get(0).getStart().getBall().getId();
                    int destt1 = t.getMovesList().get(0).getDestination().getBall().getId();
                    int turnd1 = moveList.get(0).getDestination().getBall().getId();

                    if (startt1 == turns1 && destt1 == turnd1) {
                        return true;
                    }
                } else if (turn.getTurnType() == 1 && t.getTurnType() == 1) {
                    int startt1 = t.getMovesList().get(0).getStart().getBall().getId();
                    int turns1 = moveList.get(0).getStart().getBall().getId();
                    int destt1 = t.getMovesList().get(0).getDestination().getBall().getId();
                    int turnd1 = moveList.get(0).getDestination().getBall().getId();

                    int startt2 = t.getMovesList().get(1).getStart().getBall().getId();
                    int turns2 = moveList.get(1).getStart().getBall().getId();
                    int destt2 = t.getMovesList().get(1).getDestination().getBall().getId();
                    int turnd2 = moveList.get(1).getDestination().getBall().getId();

                    if (startt1 == turns1 && destt1 == turnd1 && startt2 == turns2 && turnd2 == destt2) {
                        return true;
                    }
                } else if (turn.getTurnType() == 2 && t.getTurnType() == 2) {
                    int startt1 = t.getMovesList().get(0).getStart().getBall().getId();
                    int turns1 = moveList.get(0).getStart().getBall().getId();
                    int destt1 = t.getMovesList().get(0).getDestination().getBall().getId();
                    int turnd1 = moveList.get(0).getDestination().getBall().getId();

                    int startt2 = t.getMovesList().get(1).getStart().getBall().getId();
                    int turns2 = moveList.get(1).getStart().getBall().getId();
                    int destt2 = t.getMovesList().get(1).getDestination().getBall().getId();
                    int turnd2 = moveList.get(1).getDestination().getBall().getId();

                    int startt3 = t.getMovesList().get(2).getStart().getBall().getId();
                    int turns3 = moveList.get(2).getStart().getBall().getId();
                    int destt3 = t.getMovesList().get(2).getDestination().getBall().getId();
                    int turnd3 = moveList.get(2).getDestination().getBall().getId();

                    if (startt1 == turns1 && destt1 == turnd1 && startt2 == turns2 && turnd2 == destt2 && startt3 == turns3 && turnd3 == destt3) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean isChildOf(Node child) {
        return children.contains(child);
    }

    public Node getChild(int i) {
        return children.get(i);
    }

    public List<Node> getChildren() {
        return children;
    }

    public List<Node> getChildrenMCTS(Color c) {
        if(children.size() == 0){
            Tree newChildrenTree = new Tree(board, 1, c, heuristics);
            List<Node> newChildren = newChildrenTree.getRoot().getChildren();
            for(Node child : newChildren){
                children.add(child);
            }
        }
        return children;
    }

    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public Turn getTurn() {
        return turn;
    }

    public int getDepth() {
        return depth;
    }

    public void calculateHeuristicsValue() {
        heuristics.valueFunction(this.board);
        this.setHeuristicsValue(heuristics.getValue());
    }

    public double getHeuristicsValue() {
        return this.value;
    }

    public void setHeuristicsValue(double value) {
        this.value = value;
    }

    public int getNumberBlueBalls() {
        return numberBlueBalls;
    }

    public int getNumberPurpleBalls() {
        return numberPurpleBalls;
    }

    // MCTS Stuff

    public double getWinScore() {
        return winScore;
    }

    public int getVisitCount() {
        return visitCount;
    }

    public void incrementVisit() {
        this.visitCount++;
    }

    public void addScore(double score) {
        this.winScore += score;
    }

    public Color getPlayerColorToPlay() {
        return playerColorToPlay;
    }

    public Board getBoard() {
        return board;
    }

    public void clearRememberedList() {
        theListRemember = new ArrayList<>();
    }

    public double[] getWeights() {
        return weights;
    }
}