package com.abalone.game.gameTree;

import com.abalone.game.objects.Board;

public class Tree {

    private final Node root;

    public Tree(Board board, int maxTreeDepth) {

        root = new Node(board, maxTreeDepth, 0);
    }

    public Node getRoot() {
        return root;
    }


}
