package com.abalone.game.objects;

import com.abalone.game.utils.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class Ball implements Cloneable {
    private Color color;
    private int id;

    public Ball(Color color, int id) {
        this.color = color;
        this.id = id;
    }

    public Color getColor() {
        return this.color;
    }

    public int getId() {
        return id;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Object clone() throws CloneNotSupportedException {
        Ball clonedBall = (Ball)super.clone();
        return clonedBall;
    }
}
