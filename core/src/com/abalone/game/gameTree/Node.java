package com.abalone.game.gameTree;

import com.abalone.game.objects.Board;
import com.abalone.game.objects.Move;

import java.util.ArrayList;
import java.util.List;

public class Node {

    //private final BoardState stateData;
    private List<Node> children;
    private Node parent;
    private int depth;

    public Node(Board board, int depthTree, int depth) {
        this.children = new ArrayList();

        BoardState boardState = new BoardState(board);
        if (depthTree > depth) {
            // every combinations of balls of the player playing
            for(int i = 0; i < 4; i++) {
                // every move
                for(int iMove = 0; iMove < 2; iMove++) {
                    Board newBoard = new Board(); // board.move(availableMoves[iMove]);
                    this.addChild(new Node(newBoard, depthTree, depth+1));
                }
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
}
