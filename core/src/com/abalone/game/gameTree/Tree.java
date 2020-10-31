package com.abalone.game.gameTree;

import com.abalone.game.objects.Turn;

import java.util.List;

public class Tree {

    private final List<Node<BoardState>> tree;

    public Tree(List<Node<BoardState>> tree) {
        this.tree = tree;
    }

    public void constructTree(BoardState currBoardState) {
        //may not be a good way to set root to final
        final Node<BoardState> root = new Node<>(currBoardState);
        tree.add(root);

        List<List<Turn>> legal = root.getStateData().getLegalMoves(); //Supposed to find all legal moves from the root

        for (List<Turn> i : legal) { //For every legal move, add it as a child from the root
            List<Turn> hi = i;
            for (Turn j : hi) {
                root.addChild(new Node<>(root.getStateData().getState(j)));
            }
        }

        List<Node<BoardState>> currChildren = root.getChildren();
        // this recursive call could be improved
        if (currChildren != null) { //will stop at a leaf
            for (Node<BoardState> child : currChildren) {
                constructTree(child.getStateData()); //children will become new root
            }
        }
        // add a method to pass-by-value children of current root
    }

    public List<Node<BoardState>> getTree() {
        return tree;
    }
}