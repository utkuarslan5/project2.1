package com.abalone.game.players;

import com.abalone.game.gameTree.Node;
import com.abalone.game.gameTree.Tree;

import java.util.Collections;
import java.util.List;

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

    public Node minimax(Node currentNode, int depth, double alpha, double beta, boolean maximizingPlayer) {

        /**
        List<Node> theChildren = currentNode.getChildren();

        if(theChildren.size() != 0) {
            for (int i = 0; i < theChildren.size()-1; i++) {
                if (theChildren.get(i).getHeuristicsValue() < theChildren.get(i + 1).getHeuristicsValue()) { //or the other way when it is > and not <
                    Collections.swap(theChildren, i, i + 1);
                }
            }
        }
         // !!!!!!!!!! Replace currentNode.getChildren() in the if and else with theChildren to test it
         */

        if (depth == 0) {
            return tree.getRoot();
        }
        Node bestNode = null;
        if (maximizingPlayer) {
            double value = -10000000;
            for (Node child : currentNode.getChildren()  /**insert theChildren here*/) {
                if (child != null) {
                    double nodeValue = minimax(child, depth - 1, alpha, beta, false).getHeuristicsValue();
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
            double value = 10000000;
            for (Node child : currentNode.getChildren()  /**insert theChildren here*/) {
                if (child != null) {
                    double nodeValue = minimax(child, depth - 1, alpha, beta, true).getHeuristicsValue();
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
