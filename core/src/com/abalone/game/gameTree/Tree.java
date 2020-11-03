package com.abalone.game.gameTree;

import com.abalone.game.objects.Board;
import com.abalone.game.objects.Turn;

import java.util.List;

public class Tree {

    private final Node root;

    public Tree(Board board, byte depth) {
        root = new Node(board, depth);
    }

    public Node getRoot() {
        return root;
    }
}