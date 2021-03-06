package com.abalone.game.objects;

import com.abalone.game.utils.Color;

import java.util.ArrayList;
import java.util.List;

public class Hex implements Cloneable {
    private int x;
    private int z;
    private static final List<Hex> directions = new ArrayList<Hex>() {{
        add(new Hex(0, 1));
        add(new Hex(0, -1));
        add(new Hex(1, 0));
        add(new Hex(-1, 0));
        add(new Hex(-1, 1));
        add(new Hex(1, -1));
    }};
    private Ball ball = null;

    public Hex(int x, int z) {
        this.x = x;
        this.z = z;
    }

    public void add(Hex b) {
        this.x += b.x;
        this.z += b.z;
    }

    public void subtract(Hex b) {
        this.x -= b.x;
        this.z -= b.z;
    }

    public List<Hex> getDirections() {
        return directions;
    }

    public List<Hex> getNeighbors() {
        List<Hex> neighbors = new ArrayList<>();
        for (Hex neighbor : directions) {
            neighbors.add(new Hex(this.x + neighbor.x, this.z + neighbor.z));
        }
        return neighbors;
    }

    private boolean isNeighbor(Hex b) {
        List<Hex> neighbors = getNeighbors();
        for (Hex neighbor : neighbors) {
            if (neighbor.x == b.x && neighbor.z == b.z) {
                return true;
            }
        }
        return false;
    }

    private float calculateDistance(Hex b) {
        return (Math.abs(this.x - b.x) + Math.abs(getY() - b.getY()) + Math.abs(this.z - b.z)) / 2;
    }


    public void setBall(Ball ball) {
        this.ball = ball;
    }

    public Ball popBall() {
        Ball b = this.ball;
        this.ball = null;
        return b;
    }

    public Ball getBall() {
        return ball;
    }

    public boolean isOccupied() {
        return ball != null;
    }

    public int getY() {
        return -x - z;
    }

    public int getX() {
        return x;
    }

    public int getZ() {
        return z;
    }

    @Override
    public String toString() {
        return "Hex{" +
                "x" + x +
                " z" + z +
                " " + ball.getColor() +
                '}';
    }
    @Override
    public Object clone() throws CloneNotSupportedException {
        Hex clonedHex = (Hex)super.clone();
        if(clonedHex.ball != null) {
            clonedHex.ball = (Ball)this.ball.clone();
        }
        return clonedHex;
    }
}
