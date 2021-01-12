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
    private final Color maximizerColor;
    public final double value;
    private double w1,w2,w3;
    private double[] weights;

    public Heuristics(Board current, Color maximizerColor, double w1, double w2, double w3) {
        this.current = current;
        this.timestamp = new Timestamp(System.currentTimeMillis());
        this.maximizerColor = maximizerColor;
        this.w1 = w1;
        this.w2 = w2;
        this.w3 = w3;
        this.weights = new double[] {w1, w2, w3};
        this.value = valueFunction(current);
    }

    //Heuristics
    public double valueFunction(Board board) {
        // H1
        int countPurple = board.getPurpleHex().size();
        int countBlue = board.getBlueHex().size();
        // H2
        int totalDistancePurple = 0;
        int totalDistanceBlue = 0;
        // H3
        int totalNeighborsPurple = 0;
        int totalNeighborsBlue = 0;

        List<Hex> hexlist = board.getHexGrid().getHexList();
        for (Hex hex : hexlist) {
            if (hex.isOccupied()) {
                if(hex.getBall().getColor().equals(Color.PURPLE)) {
                    totalDistancePurple += Math.abs(hex.getX()) + Math.abs(hex.getZ());

                    if(!hex.getNeighbors().isEmpty()) {
                        for (Hex h : hex.getNeighbors()) {
                            h = current.getHexGrid().getMatchedHex(h);
                            if(h != null && h.getBall() != null && h.getBall().getColor().equals(Color.PURPLE)) {
                                totalNeighborsPurple++;
                            }
                        }
                    }
                }
                else if (hex.getBall().getColor().equals(Color.BLUE)){
                    totalDistanceBlue += Math.abs(hex.getX()) + Math.abs(hex.getZ());

                    if(!hex.getNeighbors().isEmpty()) {
                        for (Hex h : hex.getNeighbors()) {
                            h = current.getHexGrid().getMatchedHex(h);
                            if(h != null && h.getBall() != null && h.getBall().getColor().equals(Color.BLUE)) {
                                totalNeighborsBlue++;
                            }
                        }
                    }
                }
            }
        }

        double h1;
        double h2;
        double h3;

        if(maximizerColor == Color.PURPLE) {
            // Number of balls  (the bigger the better, so + weight)
            h1 = w1 * (countPurple - countBlue);
            // Total distance to the center (the smaller the better, so - weight)
            h2 = w2 * ((double)totalDistancePurple / (countPurple * 8)); // 8 corresponds to the maximum distance to the center
            // Neighbors of the same color  (the bigger the better, so + weight)
            h3 = w3 * ((double)totalNeighborsPurple / (6 * 14));
        }
        else {
            // Number of balls  (the bigger the better, so + weight)
            h1 = w1 * (countBlue - countPurple);
            // Total distance to the center (the smaller the better, so - weight)
            h2 = w2 * ((double)totalDistanceBlue / (countBlue * 8)); // 8 corresponds to the maximum distance to the center
            // Neighbors of the same color  (the bigger the better, so + weight)
            h3 = w3 * ((double)totalNeighborsBlue / (6 * 14));
        }

        double value = h1 + h2 + h3;
        //System.out.printf("h1: %.2f   h2: %.2f   h3: %.2f   h4: %.2f   =   %.2f\n", h1, h2, h3, h4, value);

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
                ", value=" + value +
                '}';
    }
}

