package com.abalone.game.objects;

public class Move {
    private int moveType; // 0 - one ball, 1 - two balls, 2 - three balls
    private int moveRole; // 0 - in-line, 1 - broadside
    private Hex start;
    private Hex destination;

    public Move(int moveType, int moveRole, Hex start, Hex destination) {
        this.moveType = moveType;
        this.moveRole = moveRole;
        this.start = start;
        this.destination = destination;
    }

    @Override
    public String toString() {
        String r = "From " + start.toString() + " to " + destination.toString();
        return r;
    }

    public Hex getStart() {
        return this.start;
    }

    public Hex getDestination() {
        return this.destination;
    }

    public int getMoveType() {
        return this.moveType;
    }

    public int getMoveRole(){
        return this.moveRole;
    }
}
