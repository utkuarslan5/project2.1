package com.abalone.game.players;

import com.abalone.game.gameTree.Heuristics;
import com.abalone.game.gameTree.Node;
import com.abalone.game.gameTree.Tree;
import com.abalone.game.objects.Board;
import com.abalone.game.objects.Hex;
import com.abalone.game.objects.Turn;
import com.abalone.game.utils.Color;
import com.abalone.game.utils.TurnsFinder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MiniMaxv2 extends Player {

    private Node bestNode;
    private int depth;
    private boolean maximizingPlayer;
    private Board initBoard;
    private Turn bestMove;
    private double value;
    private Heuristics heuristics;
    double infinity = Double.POSITIVE_INFINITY;
    private Board finalBoard;

    public MiniMaxv2(Board currentBoard, int depth, boolean maximizingPlayer) throws CloneNotSupportedException {
        this.depth = depth;
        this.maximizingPlayer = maximizingPlayer;
        this.initBoard = (Board) currentBoard.clone();
        this.heuristics = new Heuristics(initBoard, Color.BLUE, 100, -10, 5);
        this.finalBoard = minimax(initBoard, this.depth, -infinity, infinity, this.maximizingPlayer);
    }

    public Board minimax(Board currentBoard, int depth, double alpha, double beta, boolean maximizingPlayer) throws CloneNotSupportedException {
        System.out.println("USEDDDD");
        if (depth == 0) {
            return initBoard;
        }

        if (maximizingPlayer) {
            double value = -infinity;
            TurnsFinder turnsFinder = new TurnsFinder(currentBoard.getHexGrid());
            List<Hex> hexes = currentBoard.getBlueHex();
            turnsFinder.clearTurns();
            for (Hex hex : hexes) {
                // calculate all turns for each hex
                turnsFinder.findTurns(hex);
            }
            // get every calculated turns for all hexes
            List<List<Turn>> allTurnsByHex = turnsFinder.getTurns();
            List<Turn> allTurns = transformToSingleList(allTurnsByHex);
            for (Turn child : allTurns) {
                if (child != null) {
                    Board newBoard = (Board) currentBoard.clone();
                    newBoard.move(child);
                    this.heuristics = new Heuristics(newBoard, Color.BLUE, 100, -10, 5);
                        double nodeValue = heuristics.valueFunction(minimax(newBoard, depth - 1, alpha, beta, false));
                        if (nodeValue > value) {
                            value = nodeValue;
                            bestMove = child;
                            currentBoard = newBoard;
                        }
                        alpha = Math.max(alpha, value);
                        if (alpha >= beta) {
                            break;
                        }
                }
            }
            return currentBoard;

        } else {
            double value = infinity;
            TurnsFinder turnsFinder = new TurnsFinder(currentBoard.getHexGrid());
            List<Hex> hexes = currentBoard.getPurpleHex();
            turnsFinder.clearTurns();
            for (Hex hex : hexes) {
                // calculate all turns for each hex
                turnsFinder.findTurns(hex);
            }
            // get every calculated turns for all hexes
            List<List<Turn>> allTurnsByHex = turnsFinder.getTurns();
            List<Turn> allTurns = transformToSingleList(allTurnsByHex);

            for (Turn child : allTurns) {
                if (child != null) {
                    Board newBoard = (Board) currentBoard.clone();
                    newBoard.move(child);
                    this.heuristics = new Heuristics(newBoard, Color.BLUE, 100, -10, 5);
                        double nodeValue = heuristics.valueFunction(minimax(newBoard, depth - 1, alpha, beta, true));
                        if (nodeValue < value) {
                            value = nodeValue;
                            bestMove = child;
                            currentBoard = newBoard;
                        }
                        beta = Math.min(beta, value);
                        if (beta <= alpha) {
                            break;
                        }
                }
            }
            return currentBoard;
        }

    }

    @Override
    public Node getBestNode() {
        return null;
    }

    public Turn getBestMove() {
        return bestMove;
    }

    public List<Turn> transformToSingleList(List<List<Turn>> transformMe) {
        List<Turn> returnMe = new ArrayList<>();

        for (int i = 0; i < transformMe.size(); i++) {
            returnMe.addAll(transformMe.get(i));
        }

        return returnMe;
    }
}
