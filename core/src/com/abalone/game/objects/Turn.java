package com.abalone.game.objects;

import com.abalone.game.utils.Color;

import java.util.ArrayList;
import java.util.List;

public class Turn {
    private List<Move> movesList = new ArrayList<>();
    private Hex baseHex;
    private Color color;

    public Turn(Hex baseHex){
        this.baseHex = baseHex;
        this.color = baseHex.getBall().getColor();
    }

    public void addMove(Hex start, Hex destination){
        movesList.add(new Move(start,destination));
    }

    public Color getColor() {
        return color;
    }
}
