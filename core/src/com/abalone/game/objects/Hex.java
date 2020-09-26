package com.abalone.game.objects;

import com.abalone.game.utils.Color;

import java.util.ArrayList;
import java.util.List;

public class Hex {
    private int x;
    private int z;
    private List<Hex> directions = new ArrayList<>();
    private Ball ball = null;
    private List<Turn> broadsideTurns = new ArrayList<>();
    private List<Turn> inlineTurns = new ArrayList<>();


    public Hex(int x, int z) {
        this.x = x;
        this.z = z;
    }

    public void generateDirections() {
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

    public List<Hex> getNeighbors() {
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

    public void findTurns(){
        Color color = ball.getColor();
        List<Hex> neigbors = getNeighbors();
        int neighborId = 0;

        for(Hex h : neigbors) {

            // All turns
            if (!onBoard(h)){
                continue;
            }

            // Broadside turns
            boolean broadsideAllowed = true;

            if(h.isOccupied()){
                broadsideAllowed = false;
            }
            else{
                broadsideTurns.add(new Turn(this));
                broadsideTurns.get(broadsideTurns.size()-1).addMove(this,h);
            }

            // TODO: Broadside turn with more then one ball

            // In-line turns
            boolean inlineAllowed = true;


            neighborId++;
        }

    }

    private boolean onBoard(Hex h) {
        // TODO: Check if a hex is on the board.
        return true;
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
}
