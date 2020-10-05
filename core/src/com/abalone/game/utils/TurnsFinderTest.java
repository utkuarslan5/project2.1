package com.abalone.game.utils;

import com.abalone.game.objects.Board;
import com.abalone.game.objects.Hex;
import com.abalone.game.objects.HexGrid;
import com.abalone.game.objects.Turn;
import com.badlogic.gdx.math.Interpolation;

import java.util.ArrayList;
import java.util.List;

public class TurnsFinderTest {
    private Board board;
    private HexGrid hexGrid;
    private TurnsFinder turnsFinder;

    public TurnsFinderTest(){
        this.board = new Board(0);
        this.hexGrid = board.getHexGrid();
        this.turnsFinder = new TurnsFinder(hexGrid);
    }

    public void test() {
        //getNeighbors check
        for (int i = 0; i < hexGrid.getHexList().size(); i++) {
            Hex tempHex = hexGrid.getHexList().get(i);
            List<Hex> neighbors = tempHex.getNeighbors();
            for (Hex neighbor : neighbors) {
                //System.out.println("X: " + neighbor.getX() + " /// " + " Z: " + neighbor.getZ());
                //System.out.println(neighbor.isOccupied());
            }
        }

        // Find all turns
        for(int i = 0; i < hexGrid.getHexList().size();i++){
            //no return for findTurns ?
            turnsFinder.findTurns(hexGrid.getHexList().get(i));
            }

        // Print all turns
        System.out.println();
        System.out.println("=== All Legal Turns for every Hex ===");
        List<List<Turn>> turns = turnsFinder.getTurns();
        int i = 1;
        List<Integer> noTurns = new ArrayList<>();
        for(List<Turn> h : turns) {
            if(h.size()>0) {
                System.out.println("Turns for hex " + (i + 1));
                for (Turn t : h) {
                    System.out.println(t.toString());
                }
            }
            else{
                noTurns.add(i+1);
            }

            i++;
        }
        System.out.print("Hexes without turns: ");
        for(Integer n : noTurns){
            System.out.print(n+" ");
        }
        System.out.println();
    }

}
