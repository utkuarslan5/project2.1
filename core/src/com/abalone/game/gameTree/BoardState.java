package com.abalone.game.gameTree;

import com.abalone.game.objects.Board;
import com.abalone.game.utils.Color;

import java.sql.Timestamp;

public class BoardState {


    private final Heuristics heuristics;
    private final Board current;
    private final Timestamp timestamp;
    private final Color player;

    public BoardState(Board current, Color player) {
        timestamp = new Timestamp(System.currentTimeMillis());
        this.current = current;
        this.player = player;
        this.heuristics = new Heuristics(current, player);
    }

    public float getValue() {
        return heuristics.getValue();
    }

    public Board getBoard() {
        return current;
    }

}