package com.abalone.game.gameTree;

import java.util.List;

public class Node<BoardState> {

    private final BoardState stateData;
    private List<Node<BoardState>> children;
    private Node<BoardState> parent;
    private byte depth;


    public Node(BoardState stateData) {
        this.stateData = stateData;
    }

    public Node(BoardState stateData, byte depth) {
        this.stateData = stateData;
        this.depth = depth;
    }

    public Node(BoardState stateData, Node<BoardState> parent, byte depth) {
        this.stateData = stateData;
        this.parent = parent;
        this.depth = depth;
    }

    public Node(BoardState stateData, List<Node<BoardState>> children, Node<BoardState> parent, byte depth) {
        this.stateData = stateData;
        this.children = children;
        this.parent = parent;
        this.depth = depth;
    }

    public void addChild(Node<BoardState> child) {
        child.setParent(this);
        this.children.add(child);
    }

    public void addChildren(List<Node<BoardState>> children) {
        this.children = children;
        for (Node<BoardState> child : this.children) {
            child.setParent(this);
        }
    }

    public boolean isChildOf(Node<BoardState> child) {
        return children.contains(child);
    }

    public Node<BoardState> getChild(int i) {
        return children.get(i);
    }

    public List<Node<BoardState>> getChildren() {
        return children;
    }

    public BoardState getStateData() {
        return stateData;
    }

    public Node<BoardState> getParent() {
        return parent;
    }

    public void setParent(Node<BoardState> parent) {
        this.parent = parent;
    }
}
