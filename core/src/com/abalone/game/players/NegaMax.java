package com.abalone.game.players;

import com.abalone.game.gameTree.Node;
import com.abalone.game.gameTree.Tree;
import com.abalone.game.utils.Color;

public class NegaMax extends Player {
    /*
    PSEUDO CODE FOR NORMAL:
    function negamax(node, depth, color):
        if depth = 0 or node is a terminal node then
            return color & the heuristic value of node
        value := −∞
        for each child of node do
            value := max(value, −negamax(child, depth − 1, −color))
        return value
    */
    private Node bestNode;
    private int depth;
    private boolean maximizingPlayer;
    private Tree tree;

    public NegaMax(Node currentNode, int depth, boolean maximizingPlayer, Tree tree) {
        this.depth = depth;
        this.maximizingPlayer = maximizingPlayer;
        this.tree = tree;
        bestNode = negamax(currentNode, this.depth, this.maximizingPlayer);
    }

    private Node negamax(Node currentNode, int depth, boolean maximizingPlayer) {
        int counter = 0;
        if (depth == 0) {
            return tree.getRoot();
        }
        Node bestNode = null;
        double value = -10000000;

        for (Node child : currentNode.getChildren()) {
            if (child != null) {
                double nodeValue = negamax(child, depth - 1, false).getHeuristicsValue();
                counter++;
                if (nodeValue > value) {
                    value = nodeValue;
                    bestNode = child;
                }
            }
        }
        return bestNode;
    }

    public Node getBestNode() {
        return bestNode;
    }
}
