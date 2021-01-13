package com.abalone.game.players;

import com.abalone.game.gameTree.Node;
import com.abalone.game.gameTree.Tree;
import com.abalone.game.utils.Color;

import java.util.Collections;
import java.util.List;

public class NegaMax extends Player {

    private Node bestNode;
    private int depth;
    private boolean maximizingPlayer;
    private Tree tree;
    double infinity = Double.POSITIVE_INFINITY;

    public NegaMax(Node currentNode, int depth, boolean maximizingPlayer, Tree tree) {
        this.depth = depth;
        this.maximizingPlayer = maximizingPlayer;
        this.tree = tree;
        bestNode = negamax(currentNode, this.depth, this.maximizingPlayer, -infinity, infinity);
    }

    private Node negamax(Node currentNode, int depth, boolean maximizingPlayer, double alpha, double beta) {

        List<Node> theChildren = currentNode.getChildren();
        if(maximizingPlayer) {
            Collections.sort(theChildren);
        }else{
            Collections.sort(theChildren, Collections.<Node>reverseOrder());

        }

        if (depth == 0) {
            return tree.getRoot();
        }

        Node bestNode = null;
        double value = -infinity;

        for (Node child : theChildren) {
            if (child != null) {
                double nodeValue = negamax(child, depth - 1, !maximizingPlayer, beta, alpha).getHeuristicsValue();
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
    }

    public Node getBestNode() {
        return bestNode;
    }
}
