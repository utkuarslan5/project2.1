package com.abalone.game.utils;

import com.abalone.game.objects.Hex;
import com.abalone.game.objects.HexGrid;
import com.abalone.game.objects.Move;
import com.abalone.game.objects.Turn;
import java.util.ArrayList;
import java.util.List;

public class TurnsFinder {
    private HexGrid grid;
    private List<Hex> hexes = new ArrayList<>();
    private List<List<Turn>> turns = new ArrayList<>();
    private Color currentColor;

    public TurnsFinder(HexGrid grid){
        this.grid = grid;
        this.hexes = grid.getHexList();
    }

    public TurnsFinder(Hex hex){
        this.hexes.add(hex);
    }

    public List<Turn> findTurns(Hex hex){

        // System.out.println("BASE HEX " + hex.toString());

        if(hex.getBall().getColor().isBlank()){
            return null;
        }

        currentColor = hex.getBall().getColor();
        List<Hex> neighbors = hex.getNeighbors();
        List<Turn> foundTurns = new ArrayList<>();
        int neighborId = -1;

        // Loop though all directions the ball can move in.
        for (Hex nh : neighbors) {
            neighborId++;
            int iNeighborId = findInverseNeighborId(neighborId);

            /** All turns **/
            if (!grid.onBoard(nh)) {
                //System.out.println("Not on board");
                continue;
            }

            Hex h = grid.getMatchedHex(nh);

            /** Onestep Moves **/
            if(h.getBall().getColor().isBlank()){
                foundTurns.add(new Turn(hex));
                foundTurns.get(foundTurns.size()-1).addMove(0,1,hex,h);
            }

            /** Broadside turns **/

            // Find balls that qualify for broadside move
            List<Hex> candidates = new ArrayList<>();
            candidates.add(hex);
            if(h.getBall().getColor().equals(hex.getBall().getColor())){
                candidates.add(h);
                Hex nhh = h.getNeighbors().get(neighborId);
                if(grid.onBoard(nhh)){
                    Hex hh = grid.getMatchedHex(nhh);
                    if(hh.getBall().getColor().equals(hex.getBall().getColor())){
                        candidates.add(hh);
                    }
                }
            }

            // Check if candidates aren't blocked
            if(candidates.size() > 1){
                List<Hex> secondLayerNeigbors = candidates.get(1).getNeighbors();
                // For all legal positions
                for(int pos = 0; pos <= 5; pos++) {
                    // If the position is not reserverd for an in-line move
                    if(pos != neighborId && pos != findInverseNeighborId(neighborId)) {
                        Hex nlayerOneTarget = neighbors.get(pos);
                        Hex nlayerTwoTarget = secondLayerNeigbors.get(pos);
                        // Check if targets are on board
                        if(grid.onBoard(nlayerOneTarget) && grid.onBoard(nlayerTwoTarget)){
                            Hex layerOneTarget = grid.getMatchedHex(nlayerOneTarget);
                            Hex layerTwoTarget = grid.getMatchedHex(nlayerTwoTarget);
                            // Check if targets are empty
                            if (layerOneTarget.getBall().getColor().isBlank() && layerTwoTarget.getBall().getColor().isBlank()) {
                                foundTurns.add(new Turn(hex));
                                foundTurns.get(foundTurns.size() - 1).addMove(1,1,hex, layerOneTarget);
                                foundTurns.get(foundTurns.size() - 1).addMove(1,1,candidates.get(1), layerTwoTarget);

                                // Check third layer
                                Hex nhhh = candidates.get(1).getNeighbors().get(neighborId);
                                if (grid.onBoard(nhhh)) {
                                    Hex hhh = grid.getMatchedHex(nhhh);
                                    Hex nlayerThreeTarget = hhh.getNeighbors().get(pos);
                                    // Check if third layer target is on board, and the third layer isn't blank
                                    if(grid.onBoard(nlayerThreeTarget) && !hhh.getBall().getColor().isBlank()){
                                        Hex layerThreeTarget = grid.getMatchedHex(nlayerThreeTarget);
                                        // Check if third layer ball isn't  blocked
                                        if (layerThreeTarget.getBall().getColor().isBlank() && candidates.size() >= 3) {
                                            foundTurns.add(new Turn(hex));
                                            foundTurns.get(foundTurns.size() - 1).addMove(2,1,hex, layerOneTarget);
                                            foundTurns.get(foundTurns.size() - 1).addMove(2,1,candidates.get(1), layerTwoTarget);
                                            foundTurns.get(foundTurns.size() - 1).addMove(2,1,candidates.get(2), layerThreeTarget);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }




            /** In-line turns **/

            // If the movement is blocked by a friendly ball
            if(h.isOccupied() && h.getBall().getColor().equals(hex.getBall().getColor())){
                //System.out.println("Friendly ball block");
                continue;
            }

            // Find forces for two and three consecutive balls
            int forceTypeTwo = 1;
            int forceTypeThree = 1;

            Hex nhh = h.getNeighbors().get(neighborId);
            Hex hh = null;
            if(grid.onBoard(nhh)){
                hh = grid.getMatchedHex(nhh);
            }
            Hex nhhh = null;
            if(hh != null){
                nhhh = hh.getNeighbors().get(neighborId);
            }
            Hex hhh = null;
            if(nhhh != null && grid.onBoard(nhhh)){
                hhh = grid.getMatchedHex(nhhh);
            }
            Hex nih = hex.getNeighbors().get(iNeighborId);
            Hex ih = null;
            if(grid.onBoard(nih)){
                ih = grid.getMatchedHex(nih);
            }
            Hex nihh = null;
            if(ih != null){
                nihh = ih.getNeighbors().get(iNeighborId);
            }
            Hex ihh = null;
            if(nihh != null && grid.onBoard(nihh)){
                ihh = grid.getMatchedHex(nihh);
            }

            boolean firstBlock = false;
            boolean secondBlock = false;
            boolean firstPush = false;

            if(h != null){ // First block
                if(!h.getBall().getColor().isBlank() && !h.getBall().getColor().equals(hex.getBall().getColor())){
                    forceTypeTwo--;
                    forceTypeThree--;
                    firstBlock = true;
                }
            }
            if(hh != null){ // Second block - First block must succeed
                if(!hh.getBall().getColor().isBlank() && !hh.getBall().getColor().equals(hex.getBall().getColor()) && firstBlock){
                    forceTypeTwo--;
                    forceTypeThree--;
                    secondBlock = true;
                }
            }
            if(hhh != null){ // Third block - Second block must succeed
                if(!hhh.getBall().getColor().isBlank() && !hhh.getBall().getColor().equals(hex.getBall().getColor()) && secondBlock){
                    forceTypeTwo--;
                    forceTypeThree--;
                }
            }
            if(ih != null){ // First push
                if(ih.getBall().getColor().equals(hex.getBall().getColor())){
                    forceTypeTwo++;
                    forceTypeThree++;
                    firstPush = true;
                }
            }
            if(ihh != null){ // Second push - First push must succeed
                if(ihh.getBall().getColor().equals(hex.getBall().getColor()) && firstPush){
                    forceTypeThree++;
                }
            }


            // Get in-line turns from forces
            if(forceTypeTwo > 1 && ih != null){
                foundTurns.add(new Turn(hex));
                foundTurns.get(foundTurns.size()-1).addMove(1,0,hex,h);
                foundTurns.get(foundTurns.size()-1).addMove(1,0,ih,hex);
                System.out.println(foundTurns.get(foundTurns.size()-1).toString());
            }
            if(forceTypeThree > 2 && ih != null && ihh != null){
                foundTurns.add(new Turn(hex));
                foundTurns.get(foundTurns.size()-1).addMove(2,0,hex,h);
                foundTurns.get(foundTurns.size()-1).addMove(2,0,ih,hex);
                foundTurns.get(foundTurns.size()-1).addMove(2,0,ihh,ih);
                System.out.println(foundTurns.get(foundTurns.size()-1).toString());
            }

        }

        turns.add(foundTurns);
        return foundTurns;

    }


    private int findInverseNeighborId(int id){
        switch(id){
            case 0:
                return 1;
            case 1:
                return 0;
            case 2:
                return 3;
            case 3:
                return 2;
            case 4:
                return 5;
            case 5:
                return 4;
            default:
                return 6;
        }
    }

    public void addHex(Hex hex) {
        this.hexes.add(hex);
    }

    public List<List<Turn>> getTurns() {
        return turns;
    }

    public void clearTurns(){
        turns = new ArrayList<>();
    }
}