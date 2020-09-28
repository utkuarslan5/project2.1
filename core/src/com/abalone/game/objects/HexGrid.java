package com.abalone.game.objects;

import com.abalone.game.utils.Color;

import java.awt.image.AreaAveragingScaleFilter;
import java.util.ArrayList;
import java.util.List;

// TODO: add a configuration file of the board and game's initial conditions
public class HexGrid {
    private List<Hex> hexList = new ArrayList<>();

    public HexGrid() {
        hexList = generateHex();
        String log = "";
        for(Hex hex: getHexList()) {
            Ball ball = hex.getBall();
            switch (ball.getColor()) {
                case BLUE: log += "1"; break;
                case BLANK: log += "0"; break;
                case PURPLE: log += "2"; break;
            }
        }
        System.out.println(log);
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
        int iBall = 0;
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
                Hex newHex = new Hex(x, z);
                if (iBall < 11 || (iBall >= 13 && iBall <= 15)) {
                    newHex.setBall(new Ball(Color.PURPLE,iBall));
                } else if (iBall >= 50 || (iBall >= 45 && iBall <= 47)) {
                    newHex.setBall(new Ball(Color.BLUE,iBall));
                }
                else{
                    newHex.setBall(new Ball(Color.BLANK,iBall));
                }
                temp.add(newHex);
                iBall++;
            }
        }

        //Neighbors functionality check
        System.out.println(temp.size());
        List<Hex> neighbors = temp.get(30).getNeighbors();
        for(int i = 0; i < neighbors.size(); i++){
            Hex tempHex = temp.get(30).getNeighbors().get(i);
            System.out.println("The hex 30 has neighbors " + tempHex.getX() +
                    tempHex.getZ() );
        }

        return temp;
    }

    public int getBallAt(Ball ball){
        int hexPos = 0;
        for(int i = 0 ; i < hexList.size(); i++){
            if(ball.getId() == hexList.get(i).getBall().getId()){
                hexPos = i;
            }
        }
        return hexPos;
    }

    public List<Hex> getHexList() {
        return hexList;
    }

    public void setHexList(List<Hex> hexList) {
        this.hexList = hexList;
    }
}
