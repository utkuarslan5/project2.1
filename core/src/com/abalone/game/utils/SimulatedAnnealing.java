package com.abalone.game.utils;

import com.abalone.game.gameTree.Heuristics;
import com.abalone.game.gameTree.Node;
import com.abalone.game.gameTree.NodeDynamic;
import com.abalone.game.gameTree.Tree;
import com.abalone.game.objects.Board;
import com.abalone.game.objects.Turn;
import com.abalone.game.players.MCTS;
import com.abalone.game.players.MiniMax;
import com.abalone.game.players.Player;

import java.util.ArrayList;

public class SimulatedAnnealing {
    public SimulatedAnnealing() {
        run();
    }

    public void run() {
        int tempMax = 300;
        Board initialState = new Board();
        Heuristics start = new Heuristics(initialState, com.abalone.game.utils.Color.BLUE, 100, -0.878, 0.1435);
        Tree MCTSTree = new Tree(initialState, 1, Color.BLUE, start);
        MCTS bestPlayer = generateRandom(MCTSTree);
        double bestConstant = bestPlayer.getConstant();
        int bestDepth = bestPlayer.getMaxDepth();

        System.out.println("---- SIMULATED ANNEALING START ----");
        System.out.println("---- INITIAL BOT -----");
        printBot(bestPlayer);
        double coolingRatio = 0.03;
        double currentTemp = tempMax;

        while (currentTemp > 1) {
            System.out.println("---- CURRENT BEST BOT ----");
            printBot(bestPlayer);
            MCTS contender = alternateBest(initialState, bestPlayer);
            double contenderConstant = contender.getConstant();
            int contenderDepth = contender.getMaxDepth();
            int turnsCounter = 0;
            Heuristics contenderH = new Heuristics(initialState, Color.PURPLE, 100, -0.878, 0.1435);
            while (initialState.getBlueHex().size() > 8 && initialState.getPurpleHex().size() > 8 && turnsCounter < 120) {
                start = new Heuristics(initialState, com.abalone.game.utils.Color.BLUE, 100, -0.878, 0.1435);
                MCTSTree = new Tree(initialState, 1, Color.BLUE, start);
                Player bestNow = new MCTS(MCTSTree, bestDepth, 1000, bestConstant);
                Turn firstMove = bestNow.getBestNode().getTurn();
                Node.theListRemember.add(firstMove);
                initialState.move(firstMove);
                turnsCounter++;
                initialState.setMovePerformed(false);
                contenderH = new Heuristics(initialState, Color.PURPLE, 100, -0.878, 0.1435);
                Tree contenderTree = new Tree(initialState, 1, Color.PURPLE, contenderH);
                Player contenderNow = new MCTS(contenderTree, contenderDepth, 1000, contenderConstant);
                Turn secondMove = contenderNow.getBestNode().getTurn();
                Node.theListRemember.add(secondMove);
                initialState.move(secondMove);
                turnsCounter++;
                initialState.setMovePerformed(false);
            }
            Node.theListRemember = new ArrayList<>();
            double bestScore = start.valueFunction(initialState);
            double opponentScore = contenderH.valueFunction(initialState);
            double delta = (opponentScore - bestScore) * 6;
            System.out.println("delta" + delta);
            initialState = new Board();

            if (delta > 0) {
                System.out.println("The new bot was better !");
                bestDepth = contenderDepth;
                bestConstant = contenderConstant;
                bestPlayer = new MCTS(MCTSTree,bestDepth,1000,bestConstant);
            } else {
                if (Math.exp(delta / currentTemp) > Math.random()) {
                    System.out.println(Math.exp(delta / currentTemp));
                    System.out.println("Worse,but accept!");
                    bestDepth = contenderDepth;
                    bestConstant = contenderConstant;
                }
                System.out.println("Keep the old one!");
                bestPlayer = new MCTS(MCTSTree,bestDepth,1000,bestConstant);
            }
            currentTemp *= 1 - coolingRatio;
        }
        System.out.println("---- BEST Bot ----");
        printBot(bestPlayer);
    }

    public void printBot(MCTS bot) {
        System.out.println();
        System.out.println("Tree Depth: " + bot.getMaxDepth());
        System.out.println("Constant Value: " + bot.getConstant());
        System.out.println();
    }

    public MCTS generateRandom(Tree tree) {
        double treeDepth = (Math.random() * 5) + 1;
        double constantValue = (Math.random() * 1.8);

        return new MCTS(tree, (int) treeDepth, 10000, constantValue);
    }

    public MCTS alternateBest(Board board, MCTS bestBot) {
        int oldTreeDepth = bestBot.getMaxDepth();
        double constant = bestBot.getConstant();
        int newDepth = oldTreeDepth;
        double newConstant = constant;
        if (Math.random() < 0.50) {
            int change;
            if (Math.random() < 0.50) {
                change = -1;
            } else {
                change = 1;
            }
            newDepth = oldTreeDepth + change;
        } else {
            double change = (Math.random() * 0.5) - 0.25;
            newConstant = constant + change;
        }
        Heuristics newH = new Heuristics(board, Color.PURPLE, 100, -0.878, 0.1435);
        Tree tree = new Tree(board, 1, Color.PURPLE, newH);

        return new MCTS(tree, newDepth, 10000, newConstant);
    }
}
