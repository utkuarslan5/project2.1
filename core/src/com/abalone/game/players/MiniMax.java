package com.abalone.game.players;

import com.abalone.game.gameTree.NodeDynamic;
import com.abalone.game.objects.Board;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MiniMax extends PlayerDynamic {

    private NodeDynamic bestNode;
    private int depth;
    private boolean maximizingPlayer;
    private boolean iterativeDeepening;
    double infinity = Double.POSITIVE_INFINITY;
    private boolean timeOut;
    private long endTime;
    private NodeDynamic root;

    public MiniMax(NodeDynamic currentNode, int depth, boolean maximizingPlayer, boolean iterativeDeepening, int endTime, Board currentBoard) {
        this.depth = depth;
        this.maximizingPlayer = maximizingPlayer;
        this.iterativeDeepening = iterativeDeepening;
        this.root = currentNode;
        if (!iterativeDeepening) {
            bestNode = minimax(currentNode, this.depth, -infinity, infinity, this.maximizingPlayer);
        } else {
            this.endTime = (endTime + System.currentTimeMillis()); // transfer to milliseconds
            bestNode = iterativeDeepening(currentBoard, this.maximizingPlayer, this.endTime);
        }
    }

    public NodeDynamic minimax(NodeDynamic currentNode, int depth, double alpha, double beta, boolean maximizingPlayer) {
        long timeNow = System.currentTimeMillis();
        if (timeNow > endTime && this.iterativeDeepening) {
            this.timeOut = true;
            return bestNode;
        }

        List<NodeDynamic> theChildren = new ArrayList<NodeDynamic>();
        if (currentNode.getDepthTree() > currentNode.getDepth()) {
            theChildren = currentNode.getChildren();
        }


        if (depth == 0) {
            return currentNode;
        }

        NodeDynamic bestNode = null;
        if (maximizingPlayer) {
            Collections.sort(theChildren, Collections.<NodeDynamic>reverseOrder());

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
            Collections.sort(theChildren);

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


    public NodeDynamic iterativeDeepening(Board currentBoard, boolean maximizingPlayer, long endTime) {
        int currentDepth = 1;
        this.timeOut = false;
        while (System.currentTimeMillis() < endTime) {
            System.out.println("blalal");
            NodeDynamic root = new NodeDynamic(currentBoard, currentDepth, 0, null, this.root.getMaximizerColor(), this.root.getHeuristics());
            NodeDynamic currentNode = minimax(root, currentDepth, -infinity, infinity, maximizingPlayer);
            currentDepth++;
            System.out.println("blalal33");
            if (timeOut) {
                System.out.println("timeout");
                break;
            } else {
                bestNode = currentNode;
            }
        }

        return bestNode;
    }
}