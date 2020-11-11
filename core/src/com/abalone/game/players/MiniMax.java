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

    private Node currentNode;
    private int depth;
    private Color color;
    private Tree tree;
    private float val;

    public MiniMax(Node currentNode, int depth, Color color, Tree tree){
        this.currentNode = currentNode;
        this.depth = depth;
        this.color = color;
        this.tree = tree;
        val = minimax(this.currentNode, this.depth, this.color);
    }

    public float minimax(Node currentNode, int depth, Color color){
        if(depth==0){
            return tree.getRoot().getHeuristicsValue();
        }
        //Assuming the AI is purple player
        if(color.isBlue()){
            float value = -10000000;
            for(Node child: currentNode.getChildren()){
                value = Math.max(value, minimax(child, depth -1, Color.BLUE));
            }
            return value;

        } else {
            float value = 10000000;
            for(Node child: currentNode.getChildren()){
                value = Math.min(value, minimax(child, depth -1, Color.PURPLE));
            }
            return value;
        }
    }

    public float getVal() {
        return val;
    }
}
