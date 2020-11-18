package com.abalone.game.gameTree;

import com.abalone.game.objects.Board;
import com.abalone.game.objects.Hex;
import com.abalone.game.objects.Turn;
import com.abalone.game.utils.Color;
import com.abalone.game.utils.TurnsFinder;

import java.util.ArrayList;
import java.util.List;

public class Node {

    private List<Node> children;
    private Node parent;
    private int depth;
    private float value;
    private Heuristics heuristics;
    private Turn turn;

    public Node(Board board, int depthTree, int depth, Turn turn) {
        TurnsFinder turnsFinder = new TurnsFinder(board.getHexGrid());
        this.children = new ArrayList();

        heuristics = new Heuristics(board, Color.PURPLE);
        setHeuristicsValue(heuristics.getValue());
        if (depthTree > depth) {
            // If the depth of this node is even, this means it's the state of the board after a move of the human player
            // So we get the purple balls (balls of the AI) because it is the turn of the AI to play
            // Otherwise we take the blue balls because it is the turn of the human to play
            List<Hex> hexes;
            if (depth % 2 == 0) hexes = board.getPurpleHex();
            else hexes = board.getBlueHex();

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
                        this.addChild(new Node(newBoard, depthTree, depth+1, t));
                    }
                }
            }
            catch (CloneNotSupportedException e) {
                System.out.println("Clone exception");
            }
        }

        this.depth = depth;
        this.turn = turn;
        // System.out.printf("d=%d   Value=%.2f\n", depth, value);
    }

    public void addChild(Node child) {
        child.setParent(this);
        this.children.add(child);
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

    public void setHeuristicsValue(float value) {
        this.value = value;
    }

    public float getHeuristicsValue(){
        return this.value;
    }
}