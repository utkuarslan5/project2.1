package com.abalone.game.utils;

import com.abalone.game.gameTree.Heuristics;
import com.abalone.game.gameTree.Tree;
import com.abalone.game.objects.Board;
import com.abalone.game.players.MiniMax;

public class SimulatedAnnealing {
    public SimulatedAnnealing(){
        run();
    }
    public void run(){
        Board initialState = new Board();
        Heuristics start = generateRandom(initialState);
        Tree initialTree = new Tree(initialState,2,Color.BLUE,start);
        MiniMax bestPlayer = new MiniMax(initialTree.getRoot(),2,true,initialTree);


    }
    public Heuristics generateRandom(Board board){
        double valueOne = (Math.random() * 2) - 1;
        double valueTwo = (Math.random() * 2 ) - 1;
        double valueThree = (Math.random() * 2 ) - 1;
        double valueFour = -100;

        return new Heuristics(board,Color.BLUE,valueOne,valueTwo,valueThree,valueFour);
    }
    public Heuristics alternateBest(Board board,Heuristics oldHeuristics){
        int dimension = (int) (Math.random() * 3);
        double valueOneNew = oldHeuristics.getWeights()[0];
        double valueTwoNew = oldHeuristics.getWeights()[1];
        double valueThreeNew = oldHeuristics.getWeights()[2];
        double change = (Math.random() * 0.3) - 0.15;

        if(dimension == 0){
            valueOneNew += change;
        }
        else if(dimension == 1){
            valueTwoNew += change;
        }
        else{
            valueThreeNew += change;
        }

        return new Heuristics(board,Color.PURPLE,valueOneNew,valueTwoNew,valueThreeNew,-100);
    }
}
