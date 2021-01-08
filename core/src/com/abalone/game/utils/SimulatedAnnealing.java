package com.abalone.game.utils;

import com.abalone.game.gameTree.Heuristics;
import com.abalone.game.gameTree.Node;
import com.abalone.game.gameTree.Tree;
import com.abalone.game.objects.Board;
import com.abalone.game.objects.Turn;
import com.abalone.game.players.MiniMax;
import com.abalone.game.players.NegaMax;
import com.abalone.game.players.Player;

import java.util.ArrayList;

public class SimulatedAnnealing {
    public SimulatedAnnealing() {
        run();
    }

    public void run() {
        int tempMax = 1000;
        Board initialState = new Board();
        Heuristics start = generateRandom(initialState);
        Tree initialTree = new Tree(initialState, 2, Color.BLUE, start);
        MiniMax bestPlayer = new MiniMax(initialTree.getRoot(), 2, true, initialTree);

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
                initialTree = new Tree(initialState, 2, Color.BLUE, start);
                bestPlayer = new MiniMax(initialTree.getRoot(), 2, true, initialTree);
                Turn firstMove = bestPlayer.getBestNode().getTurn();
                Node.theListRemember.add(firstMove);
                initialState.move(firstMove);
                initialState.setMovePerformed(false);
                contender = new Heuristics(initialState, Color.PURPLE, weights[0], weights[1], weights[2], weights[3]);
                Tree randomTree = new Tree(initialState, 2, com.abalone.game.utils.Color.PURPLE, contender);
                MiniMax randomPlayer = new MiniMax(randomTree.getRoot(), 2, true, randomTree);
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
                start = new Heuristics(initialState, Color.BLUE, weightsNew[0], weightsNew[1], weightsNew[2], weightsNew[3]);
                initialTree = new Tree(initialState, 2, Color.BLUE, start);
                bestPlayer = new MiniMax(initialTree.getRoot(), 2, true, initialTree);
            } else {
                if (Math.exp(delta / currentTemp) > Math.random()) {
                    double[] weightsNew = contender.getWeights();
                    start = new Heuristics(initialState, Color.BLUE, weightsNew[0], weightsNew[1], weightsNew[2], weightsNew[3]);
                } else {
                    double[] weightsNew = start.getWeights();
                    start = new Heuristics(initialState, Color.BLUE, weightsNew[0], weightsNew[1], weightsNew[2], weightsNew[3]);
                }
                initialTree = new Tree(initialState, 2, Color.BLUE, start);
                bestPlayer = new MiniMax(initialTree.getRoot(), 2, true, initialTree);
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
        double valueOne = (Math.random() * 2) - 1;
        double valueTwo = (Math.random() * 2) - 1;
        double valueThree = (Math.random() * 2) - 1;
        double valueFour = (Math.random() * 5) - 6;

        return new Heuristics(board, Color.BLUE, valueOne, valueTwo, valueThree, valueFour);
    }

    public Heuristics alternateBest(Board board, Heuristics oldHeuristics) {
        int dimension = (int) (Math.random() * 4);
        double valueOneNew = oldHeuristics.getWeights()[0];
        double valueTwoNew = oldHeuristics.getWeights()[1];
        double valueThreeNew = oldHeuristics.getWeights()[2];
        double valueFourNew = oldHeuristics.getWeights()[3];
        double change = (Math.random() * 0.45) - 0.225;

        if (dimension == 0) {
            valueOneNew += change;
        } else if (dimension == 1) {
            valueTwoNew += change;
        } else if (dimension == 2) {
            valueThreeNew += change;
        } else {
            valueFourNew += change;
        }

        return new Heuristics(board, Color.PURPLE, valueOneNew, valueTwoNew, valueThreeNew, valueFourNew);
    }
}
