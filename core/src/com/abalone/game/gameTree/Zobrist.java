package com.abalone.game.gameTree;

import com.abalone.game.objects.Ball;
import com.abalone.game.objects.Board;

import java.util.ArrayList;

public class Zobrist {
    public static int[][] ZOBRISTTABLE = new int[61][28];
    public static ArrayList<Integer> lookupTable = new ArrayList<>();

    public Zobrist(){

    }

    public static void tableInit(){

        for (int i = 0; i < 61; i++) {
            for (int j = 0; j < 28; j++) {
                ZOBRISTTABLE[i][j] = (int) (Math.random() * 999999999);
            }
        }
    }

    public int findIndex(Ball ball){
        int index = 0;
        switch (ball.getId()){
            case 56:
                index = 0;
                break;
            case 57:
                index = 1;
                break;
            case 58:
                index = 2;
                break;
            case 59:
                index = 3;
                break;
            case 60:
                index = 4;
                break;
            case 50:
                index = 5;
                break;
            case 51:
                index = 6;
                break;
            case 52:
                index = 7;
                break;
            case 53:
                index = 8;
                break;
            case 54:
                index = 9;
                break;
            case 55:
                index = 10;
                break;
            case 45:
                index = 11;
                break;
            case 46:
                index = 12;
                break;
            case 47:
                index = 13;
                break;
            case 13:
                index = 14;
                break;
            case 14:
                index = 15;
                break;
            case 15:
                index = 16;
                break;
            case 5:
                index = 17;
                break;
            case 6:
                index = 18;
                break;
            case 7:
                index = 19;
                break;
            case 8:
                index = 20;
                break;
            case 9:
                index = 21;
                break;
            case 10:
                index = 22;
                break;
            case 0:
                index = 23;
                break;
            case 1:
                index = 24;
                break;
            case 2:
                index = 25;
                break;
            case 3:
                index = 26;
                break;
            case 4:
                index = 27;
                break;
            default:
                index = -1;

        }
        return index;
    }
    public int computeHash(Board currentBoard){
        int h = 0;
        for(int i = 0; i<61;i++){
            if(!currentBoard.getHexGrid().getHexList().get(i).getBall().getColor().isBlank()) {
                for (int j = 0; j < 28; j++) {
                    if(j == findIndex(currentBoard.getHexGrid().getHexList().get(i).getBall())) {
                        h ^= ZOBRISTTABLE[i][j];
                    }
                }
            }
        }
        return h;
    }
}
