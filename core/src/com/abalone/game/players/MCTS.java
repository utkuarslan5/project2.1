package com.abalone.game.players;

import com.abalone.game.gameTree.Heuristics;
import com.abalone.game.gameTree.Node;
import com.abalone.game.gameTree.Tree;

import java.util.*;

/**
 * Now following https://www.baeldung.com/java-monte-carlo-tree-search
 *
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
    private int maxRuntimeMilliSec = 5000;
    private int maxDepth;

    public MCTS(Tree tree, int maxDepth){
        this.tree = tree;
        this.root = tree.getRoot();
        this.maxDepth = maxDepth;
    }

    public Node findNextMove(){

        int endTime = (int) (System.currentTimeMillis() + maxRuntimeMilliSec);

        while(((int) System.currentTimeMillis() < endTime)) {
            Node leaf = select();
            Node rollOutResult = simulation(leaf);
            backpropagate(leaf, rollOutResult);
        }

        return bestChild(root);
    }

    // STEP 1
    private Node select() {
        Node node = root;
        while (node.getChildren().size() != 0) {
            node = findBestUCT(node, node.getParent().getVisitCount());
        }
        // STEP 2
        if(node.getChildren().size() == 0) {
            return node;
        } else {
            List<Node> unvisitedChilderen = new ArrayList<>();
            for(Node n : node.getChildren()){
                if(n.getVisitCount() <= 0){
                    unvisitedChilderen.add(n);
                }
            }
            int selected = (int)(Math.random() * unvisitedChilderen.size());
            return unvisitedChilderen.get(selected);
        }
    }

    // STEP 3
    private Node simulation(Node leaf) {
        for(int i = 0; i <= maxDepth; i++){
            if(leaf.getChildren().size() > 0) {
                leaf = simulationPolicy(leaf);
            }
            else{continue;}
        }
        return leaf;
    }

    private Node simulationPolicy(Node node){
        List<Node> theChildren = node.getChildren();
        int rnd = new Random().nextInt(theChildren.size());
        Node randomChild = theChildren.get(rnd);

        return randomChild;
    }

    // STEP 4
    private void backpropagate(Node leaf, Node rollOutResult) {

        Heuristics HLeaf = new Heuristics(rollOutResult.getBoard(), root.getPlayerColorToPlay(), root.getWeights()[0], root.getWeights()[1], root.getWeights()[2]);
        HLeaf.valueFunction(rollOutResult.getBoard());

        Node tempNode = leaf;
        while (tempNode != null) {
            tempNode.incrementVisit();
            if (tempNode.getPlayerColorToPlay().equals(root.getPlayerColorToPlay())) {
                tempNode.addScore(HLeaf.getValue());
            }
            tempNode = tempNode.getParent();
        }
    }


    public static double uctValue(int totalVisitOfParent, double winScore, int nodeVisit) {
        if (nodeVisit == 0) {
            return Integer.MAX_VALUE;
        }
        return ((double) winScore / (double) nodeVisit) + 1.41 * Math.sqrt(Math.log(totalVisitOfParent) / (double) nodeVisit);
    }

    public static Node findBestUCT(Node node, int parentVisit) {

        double bestValue = uctValue(parentVisit, node.getChildren().get(0).getWinScore(), node.getChildren().get(0).getVisitCount());
        Node bestNode = node.getChildren().get(0);

        for(Node c : node.getChildren()){
            double uctval = uctValue(parentVisit, c.getWinScore(), c.getVisitCount());
            if(uctval >= bestValue){
                bestNode = c;
                bestValue = uctval;
            }
        }

        return bestNode;
    }

    private Node bestChild(Node root) {
        double bestValue = 0;
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
