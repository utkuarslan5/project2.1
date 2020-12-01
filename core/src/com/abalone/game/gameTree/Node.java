package com.abalone.game.gameTree;

import com.abalone.game.objects.Board;
import com.abalone.game.objects.Hex;
import com.abalone.game.objects.Turn;
import com.abalone.game.utils.Color;
import com.abalone.game.utils.TurnsFinder;
import java.util.ArrayList;
import java.util.List;

public class Node {
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

    public Node(Board board, int depthTree, int depth, Turn turn, Color playerColorToPlay) {
        this.board = board;
        this.depth = depth;
        this.turn = turn;
        this.playerColorToPlay = playerColorToPlay;
        this.children = new ArrayList();
        this.numberBlueBalls = board.getBlueHex().size();
        this.numberPurpleBalls = board.getPurpleHex().size();

        TurnsFinder turnsFinder = new TurnsFinder(board.getHexGrid());
        // setHeuristicsValue(heuristics.getValue());

        if (depthTree > depth) {
            // If the depth of this node is even, this means it's the state of the board after a move of the human player
            // So we get the purple balls (balls of the AI) because it is the turn of the AI to play
            // Otherwise we take the blue balls because it is the turn of the human to play
            List<Hex> hexes;
            if(playerColorToPlay == Color.PURPLE) {
                hexes = board.getPurpleHex();
            }
            else {
                hexes = board.getBlueHex();
            }

            turnsFinder.clearTurns();
            for(Hex hex : hexes) {
                // calculate all turns for each hex
                turnsFinder.findTurns(hex);
            }
            // get every calculated turns for all hexes
            List<List<Turn>> allTurns = turnsFinder.getTurns();
            try {
                Color nextColor = (playerColorToPlay == Color.BLUE)?Color.PURPLE:Color.BLUE;
                for(List<Turn> ts : allTurns) {
                    for(Turn t : ts) {
                        if(t.getTurnType() >= 1) {
                            Board newBoard = (Board)board.clone();
                            newBoard.move(t);
                            Node newNode = new Node(newBoard, depthTree, depth+1, t, nextColor);
                            this.addChild(newNode);
                            newNode.calculateHeuristicsValue();
                        }
                    }
                }
            }
            catch (CloneNotSupportedException e) {
                System.out.println("Clone exception");
            }
        }
        System.out.printf("d=%d   Value=%.2f\n", depth, value);
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

    public void calculateHeuristicsValue() {
        this.heuristics = new Heuristics(board, Color.PURPLE, parent);
        this.setHeuristicsValue(heuristics.getValue());
    }

    public void setHeuristicsValue(double value) {
        this.value = value;
    }

    public double getHeuristicsValue(){
        return this.value;
    }

    public int getNumberBlueBalls() {
        return numberBlueBalls;
    }

    public int getNumberPurpleBalls() {
        return numberPurpleBalls;
    }
}