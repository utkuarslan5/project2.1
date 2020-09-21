package com.abalone.game.objects;

import java.util.ArrayList;
import java.util.List;

public class Hex {
    private int x;
    private int z;
    private static List<Hex> directions = new ArrayList<>();
    private Ball ball = null;


    public Hex(int x, int z) {
        this.x = x;
        this.z = z;
    }

    public static void generateDirections() {
        directions.add(new Hex(0, 1));
        directions.add(new Hex(0, -1));
        directions.add(new Hex(1, 0));
        directions.add(new Hex(-1, 0));
        directions.add(new Hex(-1, 1));
        directions.add(new Hex(1, -1));
    }

    public void add(Hex b) {
        this.x += b.x;
        this.z += b.z;
    }

    public void subtract(Hex b){
        this.x -= b.x;
        this.z -= b.z;
    }

    public List<Hex> getDirections() {
        generateDirections();
        return directions;
    }

    private List<Hex> getNeighbors() {
        generateDirections();
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
}
