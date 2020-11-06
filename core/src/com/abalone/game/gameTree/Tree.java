package com.abalone.game.gameTree;

import com.abalone.game.objects.Board;
import com.abalone.game.objects.Turn;

import java.util.List;

public class Tree {

    private final Node root;

    public Tree(Board board, int depthTree) {
        root = new Node(board, depthTree, 0);
    }

    public Node getRoot() {
        return root;
    }
}
