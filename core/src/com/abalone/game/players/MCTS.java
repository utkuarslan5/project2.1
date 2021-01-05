package com.abalone.game.players;

import com.abalone.game.gameTree.Node;
import com.abalone.game.gameTree.Tree;

import java.util.*;

/**
 * MCTS has 4 steps: Selection / Traversal,  Expansion,  Random Simulation / RollOut,  Backpropagation
 *
 * while within some weird time         ->   see: https://www.geeksforgeeks.org/ml-monte-carlo-tree-search-mcts/
 *      leaf = select(root)
 *      rollOutResult = simulation(leaf)
 *      backpropagate(leaf, rollOutResult)
 *      return bestChild(root)
 *
 */

public class MCTS {

    private Tree tree;
    private Node root;

    public MCTS(Node root){
        root = tree.getRoot();
        this.root = root;
    }

    public Node mainFunction(Node root){

        //TODO while within some weird time
        Node leaf = select(root);
        Node rollOutResult = simulation(leaf);
        backpropagate(leaf, rollOutResult);

        return bestChild(root);
    }

    // STEP 1
    private Node select(Node root) {
        Node node = root;
        while (node.getChildren().size() != 0) {
            node = findBestUCT(node, node.getParent().getVisitCount());
        }
        // STEP 2
        if(node.getChildren() == null) {
            return node;
        } else {
          //TODO  return node.getChildren().unvisitedOnes;
            return null; //for sake of no errors
        }
    }

    // STEP 3
    private Node simulation(Node leaf) {
        while (!(leaf.getChildren() == null)){
            leaf = simulationPolicy(leaf);
        }
        return leaf;
    }

    private Node simulationPolicy(Node node){
        List<Node> theChildren = node.getChildren();
        int rnd = new Random().nextInt(theChildren.size());
        Node randomChild = theChildren.get(rnd);

        return randomChild;
    }

    // TODO
    // STEP 4
    private void backpropagate(Node leaf, Node rollOutResult) {
    }





    public static double uctValue(int totalVisitOfParent, double winScore, int nodeVisit) {
        if (nodeVisit == 0) {
            return Integer.MAX_VALUE;
        }
        return ((double) winScore / (double) nodeVisit) + 1.41 * Math.sqrt(Math.log(totalVisitOfParent) / (double) nodeVisit);
    }

    public static Node findBestUCT(Node node, int parentVisit) {
        /*
        return Collections.max(
                node.getChildren(),
                Comparator.comparing(c -> uctValue(parentVisit, c.getWinScore(), c.getVisitCount())));

         */
        return null;
    }

    private Node bestChild(Node root) {
        int bestValue = 0;
        Node bestNode = root.getChildren().get(0);
        for(Node n : root.getChildren()){
           if((n.getWinScore()/n.getVisitCount()) >= bestValue) {
               bestValue = n.getWinScore()/n.getVisitCount();
               bestNode = n;
           }
        }
        return bestNode;
    }

}
