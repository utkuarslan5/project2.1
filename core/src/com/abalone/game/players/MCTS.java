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
            node = findBestUCT(node);
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
        return ((double) winScore / (double) nodeVisit) + 2 * Math.sqrt(Math.log(totalVisitOfParent) / (double) nodeVisit);
    }

    //TODO main objective: find the Node with the highest UCT value
    public static Node findBestUCT(Node node) {

       /*     ### This is from the internet:

        int parentVisit = node.getState().getVisitCount();
        return Collections.max(
                node.getChildArray(),
                Comparator.comparing(c -> uctValue(parentVisit, c.getState().getWinScore(), c.getState().getVisitCount())));
        **/
        return null;
    }

    //TODO return child with highest number of visits
    private Node bestChild(Node root) {
        return null; //for the sake of no errors
    }

}
