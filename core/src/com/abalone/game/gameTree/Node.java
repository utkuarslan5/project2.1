package com.abalone.game.gameTree;

import com.abalone.game.objects.Board;
import com.abalone.game.objects.Hex;
import com.abalone.game.objects.Turn;
import com.abalone.game.utils.Color;
import com.abalone.game.utils.TurnsFinder;

import java.util.ArrayList;
import java.util.List;

public class Node {

    //private final BoardState stateData;
    private List<Node> children;
    private Node parent;
    private int depth;
    private float value;
    private Heuristics heuristics;

    public Node(Board board, int depthTree, int depth) {
        TurnsFinder turnsFinder = new TurnsFinder(board.getHexGrid());
        this.children = new ArrayList();

        BoardState boardState = new BoardState(board);
        heuristics = new Heuristics(board, Color.PURPLE);
        setHeuristicsValue(heuristics.getValue());
        if (depthTree > depth) {
            // If the depth of this node is even, this means it's the state of the board after a move of the human player
            // So we get the purple balls (balls of the AI) because it is the turn of the AI to play
            // Otherwise we take the blue balls because it is the turn of the human to play
            List<Hex> hexes = (depth%2 == 0) ? board.getPurpleHex() : board.getBlueHex();

            turnsFinder.clearTurns();
            for(Hex hex : hexes) {
                // calculate all turns for each hex
                turnsFinder.findTurns(hex);
            }
            // get every calculated turns for all hexes
            List<List<Turn>> allTurns = turnsFinder.getTurns();
            try {
                for(List<Turn> ts : allTurns) {
                    for(Turn t : ts) {
                        Board newBoard = (Board)board.clone();
                        newBoard.move(t);
                        this.addChild(new Node(newBoard, depthTree, depth+1));
                    }
                }
            }
            catch (CloneNotSupportedException e) {
                System.out.println("Clone exception");
            }
        }
        this.depth = depth;

    }

    /*
    public Node(BoardState stateData) {
        this.stateData = stateData;
    }
    public Node(BoardState stateData, List<Node<BoardState>> children, Node<BoardState> parent, int depth) {
        this.stateData = stateData;
        this.children = children;
        this.parent = parent;
        this.depth = depth;
    }
    */

    public void addChild(Node child) {
        child.setParent(this);
        this.children.add(child);
    }

    /*
    public void addChildren(List<Node> children) {
        this.children = children;
        for (Node child : this.children) {
            child.setParent(this);
        }
    }
    */

    public boolean isChildOf(Node child) {
        return children.contains(child);
    }

    public Node getChild(int i) {
        return children.get(i);
    }

    public List<Node> getChildren() {
        return children;
    }

    /*
    public BoardState getStateData() {
        return stateData;
    }

    */

    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public void setHeuristicsValue(float value) {
        this.value = value;
    }

    public float getHeuristicsValue(){
        return this.value;
    }
}