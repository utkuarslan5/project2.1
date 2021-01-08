package com.abalone.game.gameTree;
import com.abalone.game.objects.Board;
import com.abalone.game.utils.Color;

public class Tree {

    private final Color playerColorToPlay;
    private final Node root;

    public Tree(Board board, int depthTree, Color playerColorToPlay,Heuristics heuristics) {
        this.playerColorToPlay = playerColorToPlay;
        this.root = new Node(board, depthTree, 0, null, playerColorToPlay, heuristics);
    }

    public Node getRoot() {
        return root;
    }
}