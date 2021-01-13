package com.abalone.game.players;

import com.abalone.game.gameTree.Heuristics;
import com.abalone.game.gameTree.Node;
import com.abalone.game.gameTree.Tree;
import com.badlogic.gdx.math.Interpolation;

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

public class MCTS extends Player {

    private Tree tree;
    private Node root;
    private int maxRuntimeMilliSec = 1000;
    private int maxDepth;
    private double WIN_VALUE = 1;
    private int totalVisit = 0;

    public MCTS(Tree tree, int maxDepth){
        this.tree = tree;
        this.root = tree.getRoot();
        this.maxDepth = maxDepth;
    }

    public Node getBestNode(){
        int endTime = (int) (System.currentTimeMillis() + maxRuntimeMilliSec);
        while(((int) System.currentTimeMillis() < endTime)) {
            Node leaf = select();
            Node rollOutResult = simulation(leaf);
            double value = 0;
            if(rollOutResult.getHeuristicsValue() >= root.getHeuristicsValue()){
                WIN_VALUE = Math.abs(rollOutResult.getHeuristicsValue() - root.getHeuristicsValue());
                value += WIN_VALUE;
            }
            totalVisit++;
            backpropagate(leaf, value, 0);
        }

        //System.out.println(bestChild(root).getWinScore() + "/" + bestChild(root).getVisitCount());

        return bestChild(root);
    }

    // STEP 1
    private Node select() {
        Node node = root;
        while (node.getChildren().size() != 0) {
            node = findBestUCT(node, totalVisit, root);
        }
        // STEP 2
        if(node.getChildrenMCTS(root.getMaximizerColor()).size() == 0) {
            return node;
        } else {
            List<Node> unvisitedChilderen = new ArrayList<>();
            for(Node n : node.getChildrenMCTS(root.getMaximizerColor())){
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
            if(leaf.getChildrenMCTS(root.getMaximizerColor()).size() > 0) {
                leaf = simulationPolicy(leaf);
            }
            else{continue;}
        }
        return leaf;
    }

    private Node simulationPolicy(Node node){
        List<Node> theChildren = node.getChildrenMCTS(root.getMaximizerColor());
        int rnd = new Random().nextInt(theChildren.size());
        Node randomChild = theChildren.get(rnd);

        return randomChild;
    }

    // STEP 4
    private void backpropagate(Node leaf, double value, int depth) {
        if(leaf.getParent() != null){
            leaf.incrementVisit();
            if((leaf.getMaximizerColor().equals(root.getMaximizerColor()) && (value > 0)) || (!leaf.getMaximizerColor().equals(root.getMaximizerColor()) && (value <= 0))){
                leaf.addScore(WIN_VALUE);
            }
            backpropagate(leaf.getParent(), value, depth+1);
        }

        return;
    }


    public static double uctValue(int totalVisitOfParent, double winScore, int nodeVisit) {
        if (nodeVisit == 0) {
            return Integer.MAX_VALUE;
        }
        return ((double) winScore / (double) nodeVisit) + 2 * Math.sqrt(Math.log(totalVisitOfParent) / (double) nodeVisit);
    }

    public static Node findBestUCT(Node node, int parentVisit, Node root) {

        double bestValue = -Double.MAX_VALUE;
        Node bestNode = null;

        for(Node c : node.getChildrenMCTS(root.getMaximizerColor())){
            double uctval = uctValue(parentVisit, c.getWinScore(), c.getVisitCount());
            if(uctval >= bestValue){
                bestNode = c;
                bestValue = uctval;
            }
        }

        return bestNode;
    }

    private Node bestChild(Node root) {
        double bestValue = -Double.MAX_VALUE;
        Node bestNode = null;
        for(Node n : root.getChildren()){
            if(n.getVisitCount() != 0) {
                if ((n.getWinScore() / n.getVisitCount()) >= bestValue) {
                    bestValue = n.getWinScore() / n.getVisitCount();
                    bestNode = n;
                }
            }
        }
        return bestNode;
    }

}
