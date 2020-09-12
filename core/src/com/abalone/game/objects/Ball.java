package com.abalone.game.objects;

import com.abalone.game.utils.Color;

public class Ball {

    private Color color;

    public Ball(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return this.color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}
