package com.abalone.game.players;

import com.abalone.game.gameTree.Node;
import com.abalone.game.gameTree.Tree;
import com.abalone.game.utils.Color;

public class MiniMax extends Player {

    /**
     * function minimax(node, depth, maximizingPlayer):
     * if depth = 0 or node is a terminal node then
     * return the heuristic value of node
     * if maximizingPlayer then
     * value := −∞
     * for each child of node do
     * value := max(value, minimax(child, depth − 1, FALSE))
     * return value
     * else (* minimizing player *)
     * value := +∞
     * for each child of node do
     * value := min(value, minimax(child, depth − 1, TRUE))
     * return value
     **/

    private Node bestNode;
    private int depth;
    private boolean maximizingPlayer;
    private Tree tree;

    public MiniMax(Node currentNode, int depth, boolean maximizingPlayer, Tree tree) {
        this.depth = depth;
        this.maximizingPlayer = maximizingPlayer;
        this.tree = tree;
        bestNode = minimax(currentNode, this.depth, -10000000, 10000000, this.maximizingPlayer);
    }

    public Node minimax(Node currentNode, int depth, float alpha, float beta, boolean maximizingPlayer) {
        if (depth == 0) {
            return tree.getRoot();
        }
        //Assuming the AI is purple player
        Node bestNode = null;
        if (maximizingPlayer) {
            float value = -10000000;
            for (Node child : currentNode.getChildren()) {
                if (child != null) {
                    float nodeValue = minimax(child, depth - 1, alpha, beta, false).getHeuristicsValue();
                    if (nodeValue > value) {
                        value = nodeValue;
                        bestNode = child;
                    }
                    alpha = Math.max(alpha, value);
                    if (alpha >= beta) {
                        break;
                    }
                }
            }
            return bestNode;

        } else {
            float value = 10000000;
            for (Node child : currentNode.getChildren()) {
                if (child != null) {
                    float nodeValue = minimax(child, depth - 1, alpha, beta, true).getHeuristicsValue();
                    if (nodeValue < value) {
                        value = nodeValue;
                        bestNode = child;
                    }
                    beta = Math.min(beta, value);
                    if (beta <= alpha) {
                        break;
                    }
                }
            }
            return bestNode;
        }

    }

    public Node getBestNode() {
        return bestNode;
    }
}
