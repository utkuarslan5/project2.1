package com.abalone.game.players;

import com.abalone.game.gameTree.Node;
import com.abalone.game.gameTree.Tree;
import com.abalone.game.utils.Color;

public class NegaMax {
    /*
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
    private Color color;
    private Tree tree;

    public NegaMax(Node currentNode, int depth, Color color, Tree tree) {
        this.depth = depth;
        this.color = color;
        this.tree = tree;
        bestNode = negamax(currentNode, this.depth, this.color);
    }

    private Node negamax(Node currentNode, int depth, Color color) {
        if (depth == 0) {
            return tree.getRoot();
        }
        Node bestNode = null;
        float value = -10000000;
        //Assuming the AI is purple player
        for (Node child : currentNode.getChildren()) {
            if (child != null) {
                float nodeValue = -negamax(child, depth - 1, Color.BLUE).getHeuristicsValue();
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
