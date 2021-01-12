package com.abalone.game.gameTree;
import com.abalone.game.objects.Board;
import com.abalone.game.utils.Color;

public class Tree {

    private final Color maximizerColor;
    private final Node root;

    public Tree(Board board, int depthTree, Color maximizerColor, Heuristics heuristics) {
        this.maximizerColor = maximizerColor;
        this.root = new Node(board, depthTree, 0, null, maximizerColor, heuristics);
    }

    public Node getRoot() {
        return root;
    }
}