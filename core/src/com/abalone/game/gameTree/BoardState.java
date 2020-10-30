package com.abalone.game.gameTree;
import com.abalone.game.objects.Board;
import com.abalone.game.objects.Hex;
import com.abalone.game.utils.Color;

import java.sql.Timestamp;
import java.util.List;

// This class is supposed to create states of a board after a move is made, first state will be the initial position,
// where no moves are made yet (the root of the tree)
// a certain BoardState is the data of a certain node (so kinda connect with Node.setData() etc)
// take into consideration: Position of balls, cohesion and center proximity

public class BoardState {

    private final Board current;
    private final Timestamp timestamp;
    private final Color player;
    private final float value;

    public BoardState(Board current, Color player) {
        timestamp = new Timestamp(System.currentTimeMillis());
        this.current = current;
        this.player = player;
        this.value = valueFunction(current, 1, 1);
    }

    //Heuristics
    private float valueFunction(Board board, float w1, float w2) {
        List<Hex> hexlist = board.getHexGrid().getHexList();
        int count = 0;
        float totalDistance = 0;

        for (Hex hex : hexlist) {
            if (hex.isOccupied() && hex.getBall().getColor().equals(player)) {
                count++;
                // Manhattan distance, might be a good idea to add number of ply to get to the center
                int distance = Math.abs(hex.getX()) + Math.abs(hex.getZ());
                totalDistance += distance;
            }
        }

        float h1 = w1 * count;
        float h2 = w2 * (totalDistance / count);
        System.out.println("h1:" + h1 + "h2: " + h2);
        float value = h1 + h2;

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
