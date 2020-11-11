package com.abalone.game.gameTree;

import com.abalone.game.objects.Board;
import com.abalone.game.objects.Turn;
import com.abalone.game.players.MiniMax;
import com.abalone.game.utils.Color;

import java.util.List;

public class Tree {

    private final Node root;
    private MiniMax miniMax;
    public float miniMaxValue;

    public Tree(Board board, int depthTree) {
        root = new Node(board, depthTree, 0);
        //miniMaxValue = miniMax.minimax(root, depthTree, Color.BLUE);
    }

    public Node getRoot() {
        return root;
    }
}
