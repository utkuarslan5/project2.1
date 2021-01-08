package com.abalone.game.gameTree;

import com.abalone.game.objects.Board;
import com.abalone.game.objects.Hex;
import com.abalone.game.utils.Color;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class Heuristics {

    private final Board current;
    private final Timestamp timestamp;
    private final Color playerColorToPlay;
    public final double value;
    private double w1,w2,w3,w4;
    private double[] weights;

    public Heuristics(Board current, Color playerColorToPlay, double w1, double w2, double w3, double w4) {
        this.current = current;
        this.timestamp = new Timestamp(System.currentTimeMillis());
        this.playerColorToPlay = playerColorToPlay;
        this.w1 = w1;
        this.w2 = w2;
        this.w3 = w3;
        this.w4 = w4;
        this.weights = new double[] {w1, w2, w3, w4};
        this.value = valueFunction(current);
    }

    //Heuristics
    public double valueFunction(Board board) {

        List<Hex> hexlist = board.getHexGrid().getHexList();
        int count = 0;
        int enemyCount = 0;
        double totalDistance = 0;
        int countNeighboursOfEachBall = 0;

        Color otherColor = (playerColorToPlay.isBlue())?Color.PURPLE:Color.BLUE;
        for (Hex hex : hexlist) {
            if (hex.isOccupied()) {
                if(hex.getBall().getColor().equals(playerColorToPlay)) {
                    count++;
                    // Manhattan distance, might be a good idea to add number of ply to get to the center
                    int distance = Math.abs(hex.getX()) + Math.abs(hex.getZ());
                    totalDistance += distance;

                    //For the cohesion we count every neighbour of each ball, and add these up
                    if(!hex.getNeighbors().isEmpty()) {
                        for (Hex h : hex.getNeighbors()) {
                            countNeighboursOfEachBall += 1;
                        }
                    } else {
                        countNeighboursOfEachBall++;
                    }
                }
                else if (hex.getBall().getColor().equals(otherColor)){
                    enemyCount++;
                }
            }
        }

        // Number of balls  (the bigger the better, so + weight)
        double h1 = w1 * count / 14;
        // Total distance to the center (the smaller the better, so - weight)
        double h2 = w2 * (totalDistance / (count * 8)); // 8 corresponds to the maximum distance to the center
        // Neighbors of the same color  (the bigger the better, so + weight)
        double h3 = w3 * countNeighboursOfEachBall / (6 * 14);
        // Enemy count (the smaller the better, so - weight)
        double h4 = w4 * enemyCount / 14;

        double value = h1 + h2 + h3 + h4;
        // System.out.printf("h1: %.2f   h2: %.2f   h3: %.2f   h4: %.2f   =   %.2f\n", h1, h2, h3, h4, value);

        return value;
    }

    public Board getBoard() {
        return current;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public double getValue() {
        return value;
    }

    public Board getCurrent() {
        return current;
    }

    public double[] getWeights(){
        return this.weights;
    }

    @Override
    public String toString() {
        return "Heuristics{" +
                "current=" + current +
                ", timestamp=" + timestamp +
                ", player=" + playerColorToPlay.toString() +
                ", value=" + value +
                '}';
    }
}

