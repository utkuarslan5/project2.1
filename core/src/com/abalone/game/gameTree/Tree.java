package com.abalone.game.gameTree;
import com.abalone.game.objects.Board;

public class Tree {

    private final Node root;

    public Tree(Board board, int depthTree) {
        root = new Node(board, depthTree, 0, null);
    }

    public Node getRoot() {
        return root;
    }
}