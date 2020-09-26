package com.abalone.game.objects;

public class Move {
    private Hex start;
    private Hex destination;

    public Move(Hex start, Hex destination){
        this.start = start;
        this.destination = destination;
    }

    public Hex getStart() {
        return start;
    }

    public Hex getDestination() {
        return destination;
    }
}
