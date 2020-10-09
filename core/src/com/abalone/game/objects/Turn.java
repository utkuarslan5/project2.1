package com.abalone.game.objects;

import com.abalone.game.utils.Color;

import java.util.ArrayList;
import java.util.List;

public class Turn {
    private final List<Move> movesList = new ArrayList<>();
    private Hex baseHex;
    private List<Hex> baseHexList;
    private Color color;

    public Turn(Hex baseHex) {
        this.baseHex = baseHex;
        this.color = baseHex.getBall().getColor();
    }

    //constructor to play multiple balls at the same turn
    public Turn(List<Hex> baseHexList) {
        if (baseHexList.size() > 3) {
            System.out.println("cannot make a move more than 3 balls per turn");
        } else {
            this.baseHexList = baseHexList;
            this.color = baseHexList.get(0).getBall().getColor();
            for (Hex h : baseHexList) {
                if (h.getBall().getColor() != color) System.out.println("Cannot play two color balls in one turn!");
            }
        }
    }

    public void addMove(Hex start, Hex destination) {
        movesList.add(new Move(start, destination));
    }

    public boolean removeMove(Hex start, Hex destination) {
        return movesList.remove(new Move(start, destination));
    }

    @Override
    public String toString() {
        String r = "Moves ";
        for(Move m : movesList){
            r += ";"+m.toString()+" ";
        }
        return r;
    }

    public Color getColor() {
        return color;
    }

    public List<Move> getMovesList() {
        return movesList;
    }
}
