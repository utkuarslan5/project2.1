package com.abalone.game.objects;

public class Hex {
    private int[] coords;

    public Hex(int x,int y,int z){
        this.coords = new int[]{x,y,z};
    }

    public int getY() {
        return coords[1];
    }
    public int getX(){
        return coords[0];
    }
    public int getZ(){
        return coords[2];
    }
    public int[] getCoords() {
        return coords;
    }
}
