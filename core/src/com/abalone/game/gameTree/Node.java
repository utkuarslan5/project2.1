package com.abalone.game.gameTree;

import com.abalone.game.objects.*;
import com.abalone.game.utils.Color;
import com.abalone.game.utils.TurnsFinder;
import com.badlogic.gdx.ai.pfa.Heuristic;

import java.util.ArrayList;
import java.util.List;

public class Node implements Comparable<Node>{
    public static ArrayList<Turn> theListRemember = new ArrayList<>();
    private Board board;
    private List<Node> children;
    private Node parent;
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

    public Node(Board board, int depthTree, int depth, Turn turn, Color maximizerColor, Heuristics parentHeuristics) {
        this.board = board;
        this.depth = depth;
        this.depthTree = depthTree;
        this.turn = turn;
        this.maximizerColor = maximizerColor;
        this.children = new ArrayList();
        this.weights = parentHeuristics.getWeights();
        this.heuristics = new Heuristics(this.board, maximizerColor, weights[0], weights[1], weights[2]);
        this.setHeuristicsValue(heuristics.getValue());

        if (depthTree > depth) {
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

            if (theListRemember.size() > 100) {
                theListRemember.remove(0);
            }

            try {
                for (List<Turn> ts : allTurns) {
                    for (Turn t : ts) {
                        if (!doneBefore(t)) {
                            Board newBoard = (Board) board.clone();
                            newBoard.move(t);
                            if (newBoard.getPushPossible()) {
                                Node newNode = new Node(newBoard, depthTree, depth + 1, t, maximizerColor, heuristics);
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
                child.setParent(this);
            }
        }
        return children;
    }

    public Node getParent() {
        return parent;
    }

    public Heuristics getHeuristics() {
        return heuristics;
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
        theListRemember = new ArrayList<>();
    }

    public double[] getWeights() {
        return weights;
    }

    @Override
    public int compareTo(Node node) {
        return Double.compare(this.value, node.value);

    }
}