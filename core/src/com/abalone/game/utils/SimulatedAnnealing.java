package com.abalone.game.utils;

import com.abalone.game.gameTree.Heuristics;
import com.abalone.game.gameTree.Node;
import com.abalone.game.gameTree.NodeDynamic;
import com.abalone.game.objects.Board;
import com.abalone.game.objects.Turn;
import com.abalone.game.players.MiniMax;

import java.util.ArrayList;

public class SimulatedAnnealing {
    public SimulatedAnnealing() {
        run();
    }

    public void run() {
        int tempMax = 1000;
        Board initialState = new Board();
        Heuristics start = generateRandom(initialState);
        NodeDynamic rootNode = new NodeDynamic(initialState, 2, 0, null, Color.BLUE, start);
        MiniMax bestPlayer = new MiniMax(rootNode, 2, true, false, 0, initialState);

        System.out.println("---- SIMULATED ANNEALING START ----");
        System.out.println("---- INITIAL WEIGHTS -----");
        printWeights(start);
        double coolingRatio = 0.03;
        double currentTemp = tempMax;

        while (currentTemp > 1) {
            System.out.println("---- CURRENT BEST ----");
            printWeights(start);
            Heuristics contender = alternateBest(initialState, start);
            double[] weights = contender.getWeights();
            while (initialState.getBlueHex().size() > 8 && initialState.getPurpleHex().size() > 8) {
                rootNode = new NodeDynamic(initialState, 2, 0, null, Color.BLUE, start);
                bestPlayer = new MiniMax(rootNode, 2, true, false, 0, initialState);
                Turn firstMove = bestPlayer.getBestNode().getTurn();
                Node.theListRemember.add(firstMove);
                initialState.move(firstMove);
                initialState.setMovePerformed(false);
                contender = new Heuristics(initialState, Color.PURPLE, weights[0], weights[1], weights[2]);
                NodeDynamic randomRootNode = new NodeDynamic(initialState, 2, 0, null, com.abalone.game.utils.Color.PURPLE, contender);
                MiniMax randomPlayer = new MiniMax(randomRootNode, 2, true, false, 0, initialState);
                Turn secondMove = randomPlayer.getBestNode().getTurn();
                Node.theListRemember.add(secondMove);
                initialState.move(secondMove);
                initialState.setMovePerformed(false);
            }
            Node.theListRemember = new ArrayList<>();
            double bestScore = 14 - initialState.getPurpleHex().size();
            double opponentScore = 14 - initialState.getBlueHex().size();
            double delta = opponentScore - bestScore;

            initialState = new Board();

            if (delta > 0) {
                double[] weightsNew = contender.getWeights();
                start = new Heuristics(initialState, Color.BLUE, weightsNew[0], weightsNew[1], weightsNew[2]);
                rootNode = new NodeDynamic(initialState, 2, 0, null, Color.BLUE, start);
                bestPlayer = new MiniMax(rootNode, 2, true, false, 0, initialState);
            } else {
                if (Math.exp(delta / currentTemp) > Math.random()) {
                    double[] weightsNew = contender.getWeights();
                    start = new Heuristics(initialState, Color.BLUE, weightsNew[0], weightsNew[1], weightsNew[2]);
                } else {
                    double[] weightsNew = start.getWeights();
                    start = new Heuristics(initialState, Color.BLUE, weightsNew[0], weightsNew[1], weightsNew[2]);
                }
                rootNode = new NodeDynamic(initialState, 2, 0, null, Color.BLUE, start);
                bestPlayer = new MiniMax(rootNode, 2, true, false, 0, initialState);
            }
            currentTemp *= 1 - coolingRatio;
        }
        System.out.println("---- BEST WEIGHTS ----");
        printWeights(start);
    }

    public void printWeights(Heuristics temp) {
        double[] weights = temp.getWeights();
        System.out.println();
        System.out.println("Heuristics value 1 : " + weights[0]);
        System.out.println("Heuristics value 2 : " + weights[1]);
        System.out.println("Heuristics value 3 : " + weights[2]);
        System.out.println("Heuristics value 4 : " + weights[3]);
        System.out.println();
    }

    public Heuristics generateRandom(Board board) {
        double valueOne = 100;
        double valueTwo = (Math.random() * 2) - 1;
        double valueThree = (Math.random() * 2) - 1;

        return new Heuristics(board, Color.BLUE, valueOne, valueTwo, valueThree);
    }

    public Heuristics alternateBest(Board board, Heuristics oldHeuristics) {
        int dimension = (int) (Math.random() * 2);
        double valueOneNew = 100;
        double valueTwoNew = oldHeuristics.getWeights()[1];
        double valueThreeNew = oldHeuristics.getWeights()[2];
        double change = (Math.random() * 0.45) - 0.225;

        if (dimension == 0) {
            valueTwoNew += change;
        } else if (dimension == 1) {
            valueThreeNew += change;
        }

        return new Heuristics(board, Color.PURPLE, valueOneNew, valueTwoNew, valueThreeNew);
    }
}
