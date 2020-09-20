package com.abalone.game.objects;

import java.util.ArrayList;

// TODO: add a configuration file of the board and game's initial conditions
public class HexGrid {
    private ArrayList<Hex> hexList = new ArrayList<>();

    public HexGrid() {
        //hexList = generateHex();
        System.out.println("hexGrid called");
    }

    public HexGrid(ArrayList<Hex> hexList) {
        //debugging
        if (hexList == this.hexList) System.out.println("*Grids are same");
        this.hexList = hexList;
    }

    private ArrayList<Hex> generateHex() {
        ArrayList<Hex> temp = new ArrayList<>();
        //TODO: write a for loop to create coordinate tuples of Hex'es (x,z)
        //61 balls +4,-4 on both axis, center being 0,0, radius 5
//        int y;
//        for(int x = -5; x < 5; x++){
//            int x_temp = x;
//            y = -x;
//            for (int z = y; z > -5; z--){
//               Hex hex = new Hex(x,z);
//               System.out.println("Hex created. x:" + x + " z:" + z);
//               temp.add(hex);
//            }
//        }
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
