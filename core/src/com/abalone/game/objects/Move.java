package com.abalone.game.objects;

public class Move {
    private int moveType; // 0 - one ball, 1 - two balls, 2 - three balls
    private Hex start;
    private Hex destination;

    public Move(int moveType, Hex start, Hex destination) {
        this.moveType = moveType;
        this.start = start;
        this.destination = destination;
    }

    @Override
    public String toString() {
        String r = "From " + start.toString();
        r += " to " + destination.toString();
        return r;
    }

    public Hex getStart() {
        return start;
    }

    public Hex getDestination() {
        return destination;
    }

    public int getMoveType() {
        return moveType;
    }
}
