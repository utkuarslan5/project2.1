package com.abalone.game.gameTree;

import com.abalone.game.objects.Board;
import com.abalone.game.objects.Hex;
import com.abalone.game.utils.Color;

import java.sql.Timestamp;
import java.util.List;

public class Heuristics {


    private final Board current;
    private final Timestamp timestamp;
    private final Color player;
    public final float value;

    public Heuristics(Board current, Color player) {
        timestamp = new Timestamp(System.currentTimeMillis());
        this.current = current;
        this.player = player;
        this.value = valueFunction(current, 0.5f, 1, 0.05f);
    }

    //Heuristics
    private float valueFunction(Board board, float w1, float w2, float w3) {
        List<Hex> hexlist = board.getHexGrid().getHexList();
        int count = 0;
        float totalDistance = 0;
        int countNeighboursOfEachBall = 0;

        for (Hex hex : hexlist) {
            if (hex.isOccupied() && hex.getBall().getColor().equals(player)) {
                count++;
                // Manhattan distance, might be a good idea to add number of ply to get to the center
                int distance = Math.abs(hex.getX()) + Math.abs(hex.getZ());
                totalDistance += distance;
            }
        }

        //For the cohesion we count every neighbour of each ball, and add these up
        for (Hex hex : hexlist){
            if(hex.isOccupied() && hex.getBall().getColor().equals(player)){
                if(!hex.getNeighbors().isEmpty()) {
                    for (int i = 0; i <= hex.getNeighbors().size(); i++) {
                        countNeighboursOfEachBall += 1;
                    }
                } else {
                    countNeighboursOfEachBall++;
                }
            }
        }

        float h1 = w1 * count;
        float h2 = w2 * (totalDistance / count);
        float h3 = w3 * countNeighboursOfEachBall;
        float value = h1 + h2 + h3;
        // System.out.printf("h1: %.2f   h2: %.2f   h3: %.2f   =   %.2f\n", h1, h2, h3, value);

        return value;
    }

    public Board getBoard() {
        return current;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public float getValue() {
        return value;
    }

    public Board getCurrent() {
        return current;
    }

    @Override
    public String toString() {
        return "BoardState{" +
                "current=" + current +
                ", timestamp=" + timestamp +
                ", player=" + player.toString() +
                ", value=" + value +
                '}';
    }
}

