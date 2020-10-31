package com.abalone.game.objects;

import com.abalone.game.utils.Color;

import java.util.ArrayList;
import java.util.List;

public class Turn {
    private final List<Move> movesList = new ArrayList<>();
    private int turnType;
    private Hex baseHex;
    private List<Hex> baseHexList;
    private Color color;

    public Turn(Hex baseHex) {
        this.baseHex = baseHex;
        this.color = baseHex.getBall().getColor();
    }

    public void addMove(int moveType, Hex start, Hex destination) {
        movesList.add(new Move(moveType, start, destination));
        turnType = moveType;
    }

    @Override
    public String toString() {
        String r;
        if (this.turnType == 0) {
            r = "Moves (One ball) ";
        } else if (this.turnType == 1) {
            r = "Moves (Two balls) ";
        } else {
            r = "Moves (Three balls) ";
        }

        for (Move m : movesList) {
            r += ";" + m.toString() + " ";
        }
        return r;
    }

    public Color getColor() {
        return color;
    }

    public List<Move> getMovesList() {
        return movesList;
    }

    public int getTurnType() {
        return turnType;
    }
}
