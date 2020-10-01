package com.abalone.game.utils;

import com.abalone.game.objects.Board;
import com.abalone.game.objects.Hex;
import com.abalone.game.objects.HexGrid;
import com.abalone.game.objects.Turn;
import com.badlogic.gdx.math.Interpolation;

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
            System.out.println("Hex " + (i+1) + " has neighbors");
            System.out.println(neighbors.size());
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
        List<List<Turn>> turns = turnsFinder.getTurns();
        int i = 1;
        for(List<Turn> h : turns) {
            System.out.println("Turns for hex " + (i + 1));
            for (Turn t : h) {
                System.out.println(t.toString());
            }
            i++;
        }
    }

}
