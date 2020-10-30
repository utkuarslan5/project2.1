package com.abalone.game.gameTree;

import java.util.ArrayList;
import java.util.List;

public class Node<T> {

    private T stateData = null;
    private final List<Node<T>> children = new ArrayList<>();
    private Node<T> parent = null;
    private int depth;

    public Node(T stateData) {
        this.stateData = stateData;
    }

    public void addChild(Node<T> child){
        child.setParent(this);
        this.children.add(child);
    }

    public Node<T> getChild(int i){
        return children.get(i);
    }

    public List<Node<T>> getChildren() {
        return children;
    }

    public T getStateData() {
        return stateData;
    }

    public void setStateData(T stateData){
        this.stateData = stateData;
    }

    public void setParent(Node<T> parent){
        this.parent = parent;
    }

    public Node<T> getParent(){
        return parent;
    }
}
