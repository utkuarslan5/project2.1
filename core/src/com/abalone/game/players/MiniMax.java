package com.abalone.game.players;

import com.abalone.game.gameTree.Node;
import com.abalone.game.gameTree.Tree;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MiniMax extends Player {

    private Node bestNode;
    private int depth;
    private boolean maximizingPlayer;
    private Tree tree;
    double infinity = Double.POSITIVE_INFINITY;

    public MiniMax(Node currentNode, int depth, boolean maximizingPlayer, Tree tree) {
        this.depth = depth;
        this.maximizingPlayer = maximizingPlayer;
        this.tree = tree;
        bestNode = minimax(currentNode, this.depth, -infinity, infinity, this.maximizingPlayer);
    }

    public Node minimax(Node currentNode, int depth, double alpha, double beta, boolean maximizingPlayer) {
        List<Node> theChildren = currentNode.getChildren();

        if (depth == 0) {
            return currentNode;
        }

        Node bestNode = null;
        if (maximizingPlayer) {
            Collections.sort(theChildren);
            double value = -infinity;
            for (Node child : theChildren) {
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
            Collections.sort(theChildren,Collections.<Node>reverseOrder());

            double value = infinity;
            for (Node child : theChildren) {
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
