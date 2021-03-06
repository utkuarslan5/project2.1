package com.abalone.game.gameTree;

import com.abalone.game.objects.*;
import com.abalone.game.utils.Color;
import com.abalone.game.utils.TurnsFinder;

import java.util.ArrayList;
import java.util.List;

public class NodeDynamic implements Comparable<NodeDynamic>{
    public static ArrayList<Turn> theListRemember2 = new ArrayList<>();
    private Board board;
    private List<NodeDynamic> children;
    private NodeDynamic parent;
    private int depth;
    private int depthTree;
    private double value;
    private Heuristics heuristics;
    private Turn turn;
    private Color maximizerColor;
    private double[] weights;
    // MCTS Stuff
    private double winScore = 0;
    private int visitCount = 0;

    public NodeDynamic(Board board, int depthTree, int depth, Turn turn, Color maximizerColor, Heuristics parentHeuristics) {
        this.board = board;
        this.depth = depth;
        this.depthTree = depthTree;
        this.turn = turn;
        this.maximizerColor = maximizerColor;
        this.children = new ArrayList();
        this.weights = parentHeuristics.getWeights();
        this.heuristics = new Heuristics(this.board, maximizerColor, weights[0], weights[1], weights[2]);
        this.setHeuristicsValue(heuristics.getValue());
    }

    public void addChild(NodeDynamic child) {
        child.setParent(this);
        this.children.add(child);
    }

    public boolean doneBefore(Turn turn) {
        if (turn != null) {
            List<Move> moveList = turn.getMovesList();
            for (Turn t : theListRemember2) {
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


    public List<NodeDynamic> getChildren() {
        List<NodeDynamic> children = new ArrayList();;

        TurnsFinder turnsFinder = new TurnsFinder(board.getHexGrid());
        List<Hex> hexes;

        if(getPlayerColorToPlay() == Color.PURPLE) {
            hexes = board.getPurpleHex();
        }
        else {
            hexes = board.getBlueHex();
        }

        turnsFinder.clearTurns();
        for (Hex hex : hexes) {
            // calculate all turns for each hex
            turnsFinder.findTurns(hex);
        }
        // get every calculated turns for all hexes
        List<List<Turn>> allTurns = turnsFinder.getTurns();

        if (theListRemember2.size() > 100) {
            theListRemember2.remove(0);
        }

        try {
            for (List<Turn> ts : allTurns) {
                for (Turn t : ts) {
                    if (!doneBefore(t)) {
                        Board newBoard = (Board) board.clone();
                        newBoard.move(t);
                        if (newBoard.getPushPossible()) {
                            NodeDynamic newNode = new NodeDynamic(newBoard, depthTree, depth + 1, t, maximizerColor, heuristics);
                            children.add(newNode);
                        }
                    }
                }
            }
        } catch (CloneNotSupportedException e) {
            System.out.println("Clone exception");
        }
        return children;
    }

    public NodeDynamic getParent() {
        return parent;
    }

    public Heuristics getHeuristics() {
        return heuristics;
    }

    public void setParent(NodeDynamic parent) {
        this.parent = parent;
    }

    public Turn getTurn() {
        return turn;
    }

    public int getDepth() {
        return depth;
    }

    public int getDepthTree() {
        return depthTree;
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

    public Color getMaximizerColor() {
        return maximizerColor;
    }

    public Color getPlayerColorToPlay() {
        if (depth%2 == 0) {
            if(maximizerColor == Color.PURPLE) {
                return Color.PURPLE;
            }
            else {
                return Color.BLUE;
            }
        } else {
            if(maximizerColor == Color.PURPLE) {
                return Color.BLUE;

            }
            else {
                return Color.PURPLE;
            }
        }
    }

    public Board getBoard() {
        return board;
    }

    public void clearRememberedList() {
        theListRemember2 = new ArrayList<>();
    }

    public double[] getWeights() {
        return weights;
    }

    @Override
    public int compareTo(NodeDynamic node) {
        return Double.compare(this.value, node.value);

    }
}