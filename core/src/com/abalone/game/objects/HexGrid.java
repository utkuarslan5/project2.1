package com.abalone.game.objects;

import java.util.ArrayList;

// TODO: add a configuration file of the board and game's initial conditions
public class HexGrid {
    private ArrayList<Hex> hexList = new ArrayList<>();

    public HexGrid() {
        hexList = generateHex();
        System.out.println("hexGrid called");
    }

    public HexGrid(ArrayList<Hex> hexList) {
        //debugging
        if (hexList == this.hexList) System.out.println("*Grids are same");
        this.hexList = hexList;
    }

    private ArrayList<Hex> generateHex() {
        ArrayList<Hex> temp = new ArrayList<>();
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
        System.out.println(temp.size());
        return temp;
    }

    public ArrayList<Hex> getHexList() {
        return hexList;
    }

    public void setHexList(ArrayList<Hex> hexList) {
        this.hexList = hexList;
    }
}
