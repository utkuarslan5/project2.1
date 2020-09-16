package com.abalone.game.objects;

public class Hex {
    private int x;
    private int z;

    public Hex(int x,int z){
        this.x = x;
        this.z = z;
    }

    public int getY() {
        return -x-z;
    }
    public int getX(){
        return x;
    }
    public int getZ(){
        return z;
    }
}
