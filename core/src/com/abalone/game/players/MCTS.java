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
    private int maxRuntimeMilliSec = 5000;
    private int maxDepth;
    private final double WIN_VALUE = 1;

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
            Heuristics HLeaf = new Heuristics(rollOutResult.getBoard(), root.getPlayerColorToPlay(), root.getWeights()[0], root.getWeights()[1], root.getWeights()[2]);
            HLeaf.valueFunction(rollOutResult.getBoard());
            Heuristics HRoot = new Heuristics(root.getBoard(), root.getPlayerColorToPlay(), root.getWeights()[0], root.getWeights()[1], root.getWeights()[2]);
            HRoot.valueFunction(rollOutResult.getBoard());
            double value = 0;
            if(HLeaf.getValue() > HRoot.getValue()){
                value += WIN_VALUE;
            }
            backpropagate(leaf, value, 0);
        }

        System.out.println(bestChild(root).getWinScore() + "/" + bestChild(root).getVisitCount());

        return bestChild(root);
    }

    // STEP 1
    private Node select() {
        Node node = root;
        while (node.getChildren().size() != 0) {
            node = findBestUCT(node, root.getVisitCount(), root);
        }
        // STEP 2
        if(node.getChildrenMCTS(root.getPlayerColorToPlay()).size() == 0) {
            return node;
        } else {
            List<Node> unvisitedChilderen = new ArrayList<>();
            for(Node n : node.getChildrenMCTS(root.getPlayerColorToPlay())){
                if(n.getVisitCount() <= 0){
                    unvisitedChilderen.add(n);
                }
            }
            int selected = (int)(Math.random() * unvisitedChilderen.size());
            System.out.println(unvisitedChilderen.size());
            return unvisitedChilderen.get(selected);
        }
    }

    // STEP 3
    private Node simulation(Node leaf) {
        for(int i = 0; i <= maxDepth; i++){
            if(leaf.getChildrenMCTS(root.getPlayerColorToPlay()).size() > 0) {
                leaf = simulationPolicy(leaf);
            }
            else{continue;}
        }
        return leaf;
    }

    private Node simulationPolicy(Node node){
        List<Node> theChildren = node.getChildrenMCTS(root.getPlayerColorToPlay());
        int rnd = new Random().nextInt(theChildren.size());
        Node randomChild = theChildren.get(rnd);

        return randomChild;
    }

    // STEP 4
    private void backpropagate(Node leaf, double value, int depth) {

        /*
        for(Node c : leaf.getChildren()){
            if(c.getVisitCount() > 0){
                System.out.println(depth + " " + c.getWinScore() + "/" + c.getVisitCount());
            }
        }
        */

        if(leaf.getParent() != null){
            leaf.incrementVisit();
            if((leaf.getPlayerColorToPlay().equals(root.getPlayerColorToPlay()) && (value > 0)) || (!leaf.getPlayerColorToPlay().equals(root.getPlayerColorToPlay()) && (value <= 0))){
                leaf.addScore(WIN_VALUE);
                //System.out.println(depth + " ADDED VALUE");
            }
            backpropagate(leaf.getParent(), value, depth+1);
        }

        //System.out.println("RETURN DEPTH = " + depth);
        return;
    }


    public static double uctValue(int totalVisitOfParent, double winScore, int nodeVisit) {
        if (nodeVisit == 0) {
            return Integer.MAX_VALUE;
        }
        return ((double) winScore / (double) nodeVisit) + 2 * Math.sqrt(Math.log(totalVisitOfParent) / (double) nodeVisit);
    }

    public static Node findBestUCT(Node node, int parentVisit, Node root) {

        double bestValue = uctValue(parentVisit, node.getChildrenMCTS(root.getPlayerColorToPlay()).get(0).getWinScore(), node.getChildrenMCTS(root.getPlayerColorToPlay()).get(0).getVisitCount());
        Node bestNode = node.getChildrenMCTS(root.getPlayerColorToPlay()).get(0);

        for(Node c : node.getChildrenMCTS(root.getPlayerColorToPlay())){
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
        Node bestNode = root.getChildren().get(0);
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
