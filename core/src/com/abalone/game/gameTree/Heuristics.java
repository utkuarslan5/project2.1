package com.abalone.game.gameTree;

import com.abalone.game.objects.Board;
import com.abalone.game.objects.Hex;
import com.abalone.game.utils.Color;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class Heuristics {

    private final Board current;
    private final Timestamp timestamp;
    private final Color playerColorToPlay;
    private final Node parentNode;
    public final double value;

    public Heuristics(Board current, Color playerColorToPlay, Node parentNode) {
        this.current = current;
        this.timestamp = new Timestamp(System.currentTimeMillis());
        this.playerColorToPlay = playerColorToPlay;
        this.parentNode = parentNode;
        this.value = valueFunction(current, 5, -10, 1,1,1, 500);
    }

    //Heuristics
    private double valueFunction(Board board, double w1, double w2, double w3, double w4, double w5, double w6) {

        List<Hex> hexlist = board.getHexGrid().getHexList();
        int count = 0;
        int enemyCount = 0;
        int countAttacks = 0;
        double totalDistance = 0;
        int countNeighboursOfEachBall = 0;

        Color otherColor = (playerColorToPlay.isBlue())?Color.PURPLE:Color.BLUE;
        for (Hex hex : hexlist) {
            if (hex.isOccupied()) {
                if(hex.getBall().getColor().equals(playerColorToPlay)) {
                    count++;
                    // Manhattan distance, might be a good idea to add number of ply to get to the center
                    int distance = Math.abs(hex.getX()) + Math.abs(hex.getZ());
                    totalDistance += distance;

                    //For the cohesion we count every neighbour of each ball, and add these up
                    if(!hex.getNeighbors().isEmpty()) {
                        for (int i = 0; i <= hex.getNeighbors().size(); i++) {
                            countNeighboursOfEachBall += 1;
                        }
                    } else {
                        countNeighboursOfEachBall++;
                    }
                }
                else if (hex.getBall().getColor().equals(otherColor)){
                    enemyCount++;
                }
            }
        }

        /*
        //Attacking
        for(int i = -4 ;i<=4 ; i++){
            ArrayList<Hex> grouped1 = new ArrayList<>();
            ArrayList<Hex> grouped2 = new ArrayList<>();
            ArrayList<Hex> grouped3 = new ArrayList<>();
            for(Hex hexes : hexlist){
                if(hexes.getX() == i){
                    grouped1.add(hexes);
                }else if(hexes.getY() == i ){
                    grouped2.add(hexes);
                }else if(hexes.getZ() == i){
                    grouped3.add(hexes);
                }
            }


            //finding attacks for 2vs1
            for(int j = 4;j<grouped1.size();j++){
                ArrayList<Hex> sublist = new ArrayList<>(grouped1.subList(j-4,j));
                Color first = sublist.get(0).getBall().getColor();
                Color second = sublist.get(1).getBall().getColor();
                Color third = sublist.get(2).getBall().getColor();
                Color fourth = sublist.get(3).getBall().getColor();
                if(first.equals(player) && first.equals(second) && !first.equals(third) && !third.equals(Color.BLANK)
                        && fourth.equals(Color.BLANK)){
                    countAttacks++;
                }
            }
            for(int j = 4;j<grouped2.size();j++){
                ArrayList<Hex> sublist = new ArrayList<>(grouped2.subList(j-4,j));
                Color first = sublist.get(0).getBall().getColor();
                Color second = sublist.get(1).getBall().getColor();
                Color third = sublist.get(2).getBall().getColor();
                Color fourth = sublist.get(3).getBall().getColor();
                if(first.equals(player) && first.equals(second) && !first.equals(third) && !third.equals(Color.BLANK)
                        && fourth.equals(Color.BLANK)){
                    countAttacks++;
                }
            }
            for(int j = 4;j<grouped3.size();j++){
                ArrayList<Hex> sublist = new ArrayList<>(grouped3.subList(j-4,j));
                Color first = sublist.get(0).getBall().getColor();
                Color second = sublist.get(1).getBall().getColor();
                Color third = sublist.get(2).getBall().getColor();
                Color fourth = sublist.get(3).getBall().getColor();
                if(first.equals(player) && first.equals(second) && !first.equals(third) && !third.equals(Color.BLANK)
                        && fourth.equals(Color.BLANK)){
                    countAttacks++;
                }
            }

            //finding attacks for 3vs1
            for(int j = 5;j<grouped1.size();j++){
                ArrayList<Hex> sublist = new ArrayList<>(grouped1.subList(j-5,j));
                Color first = sublist.get(0).getBall().getColor();
                Color second = sublist.get(1).getBall().getColor();
                Color third = sublist.get(2).getBall().getColor();
                Color fourth = sublist.get(3).getBall().getColor();
                Color fifth = sublist.get(4).getBall().getColor();
                if(first.equals(player) && first.equals(second) && first.equals(third)  && !first.equals(fourth)
                        && !fourth.equals(Color.BLANK) && fifth.equals(Color.BLANK)){
                    countAttacks++;
                }
            }

            for(int j = 5;j<grouped2.size();j++){
                ArrayList<Hex> sublist = new ArrayList<>(grouped2.subList(j-5,j));
                Color first = sublist.get(0).getBall().getColor();
                Color second = sublist.get(1).getBall().getColor();
                Color third = sublist.get(2).getBall().getColor();
                Color fourth = sublist.get(3).getBall().getColor();
                Color fifth = sublist.get(4).getBall().getColor();
                if(first.equals(player) && first.equals(second) && first.equals(third) && !first.equals(fourth)
                        && !fourth.equals(Color.BLANK) && fifth.equals(Color.BLANK)){
                    countAttacks++;
                }
            }

            for(int j = 5;j<grouped3.size();j++){
                ArrayList<Hex> sublist = new ArrayList<>(grouped3.subList(j-5,j));
                Color first = sublist.get(0).getBall().getColor();
                Color second = sublist.get(1).getBall().getColor();
                Color third = sublist.get(2).getBall().getColor();
                Color fourth = sublist.get(3).getBall().getColor();
                Color fifth = sublist.get(4).getBall().getColor();
                if(first.equals(player) && first.equals(second) && first.equals(third) && !first.equals(fourth)
                        && !fourth.equals(Color.BLANK) && fifth.equals(Color.BLANK)){
                    countAttacks++;
                }
            }

            //finding attacks 3vs2
            for(int j = 6;j<grouped1.size();j++){
                ArrayList<Hex> sublist = new ArrayList<>(grouped1.subList(j-6,j));
                Color first = sublist.get(0).getBall().getColor();
                Color second = sublist.get(1).getBall().getColor();
                Color third = sublist.get(2).getBall().getColor();
                Color fourth = sublist.get(3).getBall().getColor();
                Color fifth = sublist.get(4).getBall().getColor();
                Color sixth = sublist.get(5).getBall().getColor();
                if(first.equals(player) && first.equals(second) && first.equals(third)
                        && !first.equals(fourth) && !first.equals(fifth)
                        && !fourth.equals(Color.BLANK) && !fifth.equals(Color.BLANK)
                        && sixth.equals(Color.BLANK)){
                    countAttacks++;
                }
            }

            for(int j = 6;j<grouped2.size();j++){
                ArrayList<Hex> sublist = new ArrayList<>(grouped2.subList(j-6,j));
                Color first = sublist.get(0).getBall().getColor();
                Color second = sublist.get(1).getBall().getColor();
                Color third = sublist.get(2).getBall().getColor();
                Color fourth = sublist.get(3).getBall().getColor();
                Color fifth = sublist.get(4).getBall().getColor();
                Color sixth = sublist.get(5).getBall().getColor();
                if(first.equals(player) && first.equals(second) && first.equals(third)
                        && !first.equals(fourth) && !first.equals(fifth)
                        && !fourth.equals(Color.BLANK) && !fifth.equals(Color.BLANK)
                        && sixth.equals(Color.BLANK)){
                    countAttacks++;
                }
            }

            for(int j = 6;j<grouped3.size();j++){
                ArrayList<Hex> sublist = new ArrayList<>(grouped3.subList(j-6,j));
                Color first = sublist.get(0).getBall().getColor();
                Color second = sublist.get(1).getBall().getColor();
                Color third = sublist.get(2).getBall().getColor();
                Color fourth = sublist.get(3).getBall().getColor();
                Color fifth = sublist.get(4).getBall().getColor();
                Color sixth = sublist.get(5).getBall().getColor();
                if(first.equals(player) && first.equals(second) && first.equals(third)
                        && !first.equals(fourth) && !first.equals(fifth)
                        && !fourth.equals(Color.BLANK) && !fifth.equals(Color.BLANK)
                        && sixth.equals(Color.BLANK)){
                    countAttacks++;
                }
            }

            grouped1.clear();
            grouped2.clear();
            grouped3.clear();
        }
        //System.out.println("ATTACK VALUE IS: " + countAttacks);
        */

        // Number of balls  (the more the better, so + weight)
        double h1 = w1 * count;
        // Total distance from  (the less the better, so - weight)
        double h2 = w2 * (totalDistance / count);
        // Neighbors of the same color  (the more the better, so + weight)
        double h3 = w3 * countNeighboursOfEachBall;
        // double h4 = w4 * countAttacks;
        // double h5 = -w5 * enemycount;

        // Heuristic 6
        double h6 = w6 * -enemyCount / 14;
        double value = h1 + h2 + h3 + h6;
        System.out.printf("h1: %.2f   h2: %.2f   h3: %.2f   pp: %.2f   =   %.2f\n", h1, h2, h3, h6, value);

        return value;
    }

    public Board getBoard() {
        return current;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public double getValue() {
        return value;
    }

    public Board getCurrent() {
        return current;
    }

    @Override
    public String toString() {
        return "Heuristics{" +
                "current=" + current +
                ", timestamp=" + timestamp +
                ", player=" + playerColorToPlay.toString() +
                ", value=" + value +
                '}';
    }
}

