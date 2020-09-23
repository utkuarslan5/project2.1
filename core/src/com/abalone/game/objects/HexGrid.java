package com.abalone.game.objects;

import java.awt.image.AreaAveragingScaleFilter;
import java.util.ArrayList;
import java.util.List;

// TODO: add a configuration file of the board and game's initial conditions
public class HexGrid {
    private List<Hex> hexList = new ArrayList<>();

    public HexGrid() {
        hexList = generateHex();
        System.out.println("hexGrid called");
    }

    public HexGrid(ArrayList<Hex> hexList) {
        //debugging
        if (hexList == this.hexList) System.out.println("*Grids are same");
        this.hexList = hexList;
    }

    private List<Hex> generateHex() {
        List<Hex> temp = new ArrayList<>();
        //61 balls +4,-4 on both axis, center being 0,0, radius 5
        int size = 9;
        int half = size / 2;
        for (int row = 0; row < size; row++) {
            int cols = size - Math.abs(row - half);
            for (int col = 0; col < cols; col++) {
                int x;
                if (row < half) {
                    x = col - row;
                } else {
                    x = col - half;
                }
                int z = row - half;
                System.out.println("Hex created with x " + x + " and z " + z);
                temp.add(new Hex(x, z));
            }
        }

        //Neighbors functionality check
        System.out.println(temp.size());
        List<Hex> neighbors = temp.get(30).getNeighbors();
        for(int i = 0; i<neighbors.size();i++){
            Hex tempHex = temp.get(30).getNeighbors().get(i);
            System.out.println("The hex 30 has neighbors " + tempHex.getX() +
                    tempHex.getZ() );
        }

        return temp;
    }

    public List<Hex> getHexList() {
        return hexList;
    }

    public void setHexList(List<Hex> hexList) {
        this.hexList = hexList;
    }
}
