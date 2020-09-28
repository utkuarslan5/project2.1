package com.abalone.game.utils;

import com.abalone.game.objects.Board;
import com.abalone.game.objects.Hex;
import com.abalone.game.objects.HexGrid;

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
            for (Hex neighbor : neighbors) {
                System.out.println("X: " + neighbor.getX() + " /// " + " Z: " + neighbor.getZ());
            }
        }

        for(int i = 0; i < hexGrid.getHexList().size();i++){
            //no return for findTurns ?
            //turnsFinder.findTurns(hexGrid.getHexList().get(i));
        }
    }

}
