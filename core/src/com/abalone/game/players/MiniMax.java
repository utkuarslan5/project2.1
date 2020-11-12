package com.abalone.game.players;

import com.abalone.game.gameTree.Node;
import com.abalone.game.gameTree.Tree;
import com.abalone.game.utils.Color;

public class MiniMax extends Player {

    /**
     function minimax(node, depth, maximizingPlayer):
        if depth = 0 or node is a terminal node then
            return the heuristic value of node
        if maximizingPlayer then
            value := −∞
            for each child of node do
                value := max(value, minimax(child, depth − 1, FALSE))
            return value
        else (* minimizing player *)
            value := +∞
            for each child of node do
                value := min(value, minimax(child, depth − 1, TRUE))
            return value
     **/

    private Node bestNode;
    private int depth;
    private Color color;
    private Tree tree;

    public MiniMax(Node currentNode, int depth, Color color, Tree tree){
        this.depth = depth;
        this.color = color;
        this.tree = tree;
        bestNode = minimax(currentNode, this.depth, this.color);
    }

    public Node minimax(Node currentNode, int depth, Color color){
        if(depth==0){
            return tree.getRoot();
        }
        //Assuming the AI is purple player
        Node bestNode = null;
        if(color.isBlue()){
            float value = -10000000;
            for(Node child: currentNode.getChildren()) {
                if(child != null) {
                    float nodeValue = minimax(child, depth -1, Color.BLUE).getHeuristicsValue();
                    if(nodeValue > value) {
                        value = nodeValue;
                        bestNode = child;
                    }
                }
            }

        } else {
            float value = 10000000;
            for(Node child: currentNode.getChildren()){
                if(child != null) {
                    float nodeValue = minimax(child, depth -1, Color.BLUE).getHeuristicsValue();
                    if(nodeValue < value) {
                        value = nodeValue;
                        bestNode = child;
                    }
                }
            }
        }
        return bestNode;
    }

    public Node getBestNode() {
        return bestNode;
    }
}