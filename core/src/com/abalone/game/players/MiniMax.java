package com.abalone.game.players;

import com.abalone.game.gameTree.Node;
import com.abalone.game.gameTree.NodeDynamic;
import com.abalone.game.gameTree.Tree;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MiniMax extends PlayerDynamic {

    private NodeDynamic bestNode;
    private int depth;
    private boolean maximizingPlayer;
    double infinity = Double.POSITIVE_INFINITY;

    public MiniMax(NodeDynamic currentNode, int depth, boolean maximizingPlayer) {
        this.depth = depth;
        this.maximizingPlayer = maximizingPlayer;
        bestNode = minimax(currentNode, this.depth, -infinity, infinity, this.maximizingPlayer);
    }

    public NodeDynamic minimax(NodeDynamic currentNode, int depth, double alpha, double beta, boolean maximizingPlayer) {
        List<NodeDynamic> theChildren = new ArrayList<NodeDynamic>();
        if (currentNode.getDepthTree() > currentNode.getDepth()) {
            theChildren = currentNode.getChildren();
        }

        if (depth == 0) {
            return currentNode;
        }

        NodeDynamic bestNode = null;
        if (maximizingPlayer) {
            Collections.sort(theChildren);
            double value = -infinity;
            for (NodeDynamic child : theChildren) {
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
            Collections.sort(theChildren,Collections.<NodeDynamic>reverseOrder());

            double value = infinity;
            for (NodeDynamic child : theChildren) {
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

    public NodeDynamic getBestNode() {
        return bestNode;
    }
}