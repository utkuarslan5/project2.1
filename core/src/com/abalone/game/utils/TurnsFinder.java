package com.abalone.game.utils;

import com.abalone.game.objects.Hex;
import com.abalone.game.objects.HexGrid;
import com.abalone.game.objects.Turn;

import java.util.ArrayList;
import java.util.List;

public class TurnsFinder {
    private HexGrid grid;
    private List<Hex> hexes = new ArrayList<>();
    private final List<List<Turn>> turns = new ArrayList<>();
    private int holdForceDouble;
    private int holdForceTriple;
    private Color currentColor;

    public TurnsFinder(HexGrid grid){
        this.grid = grid;
        this.hexes = grid.getHexList();
    }

    public TurnsFinder(Hex hex){
        this.hexes.add(hex);
    }

    public void findTurns(Hex hex){

        if(hex.getBall().getColor().isBlank()){
            return;
        }

        currentColor = hex.getBall().getColor();
        List<Hex> neighbors = hex.getNeighbors();
        List<Turn> foundTurns = new ArrayList<>();
        int neighborId = -1;

        // Loop though all directions the ball can move in.
        for (Hex nh : neighbors) {
            neighborId++;

            /** All turns **/
            if (!grid.onBoard(nh)) {
                //System.out.println("Not on board");
                continue;
            }

            Hex h = grid.getMatchedHex(nh);

            /** Onestep Moves **/
            if(h.getBall().getColor().isBlank()){
                foundTurns.add(new Turn(hex));
                foundTurns.get(foundTurns.size()-1).addMove(hex,h);
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
                                foundTurns.get(foundTurns.size() - 1).addMove(hex, layerOneTarget);
                                foundTurns.get(foundTurns.size() - 1).addMove(candidates.get(1), layerTwoTarget);

                                // Check third layer
                                Hex nhhh = candidates.get(1).getNeighbors().get(neighborId);
                                if (grid.onBoard(nhhh)) {
                                    Hex hhh = grid.getMatchedHex(nhhh);
                                    Hex nlayerThreeTarget = hhh.getNeighbors().get(pos);
                                    // Check if third layer target is on board, and the third layer isn't blank
                                    if(grid.onBoard(nlayerThreeTarget) && !hhh.getBall().getColor().isBlank()){
                                        Hex layerThreeTarget = grid.getMatchedHex(nlayerThreeTarget);
                                        // Check if third layer ball isn't  blocked
                                        if (layerThreeTarget.getBall().getColor().isBlank()) {
                                            foundTurns.add(new Turn(hex));
                                            foundTurns.get(foundTurns.size() - 1).addMove(hex, layerOneTarget);
                                            foundTurns.get(foundTurns.size() - 1).addMove(candidates.get(1), layerTwoTarget);
                                            foundTurns.get(foundTurns.size() - 1).addMove(hhh, layerThreeTarget);
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
            holdForceDouble = 0;
            holdForceTriple = 0;

            findForce(1, hex, findInverseNeighborId(neighborId), 2, 2, true, false);
            findForce(0, hex, neighborId,3, 2, true, true);
            findForce(1, hex, findInverseNeighborId(neighborId), 3, 2, false, false);
            findForce(0, hex, neighborId,4, 2, false, true);

            // Get in-line turns from forces
            if (holdForceDouble > 0 && grid.onBoard(neighbors.get(findInverseNeighborId(neighborId)))) {
                if(!grid.getMatchedHex(neighbors.get(findInverseNeighborId(neighborId))).getBall().getColor().isBlank()) {
                    foundTurns.add(new Turn(hex)); // i h n = 1 1 0
                    foundTurns.get(foundTurns.size() - 1).addMove(hex, grid.getMatchedHex(neighbors.get(neighborId))); // i h n = 1 0 1
                    foundTurns.get(foundTurns.size() - 1).addMove(grid.getMatchedHex(neighbors.get(findInverseNeighborId(neighborId))), hex); // i h n = 0 1 1
                }
            }

            if (holdForceTriple > 0 && grid.onBoard(neighbors.get(findInverseNeighborId(neighborId))) && grid.onBoard(neighbors.get(findInverseNeighborId(neighborId)).getNeighbors().get(findInverseNeighborId(neighborId)))) {
                if(!grid.getMatchedHex(neighbors.get(findInverseNeighborId(neighborId))).getBall().getColor().isBlank() && !grid.getMatchedHex(neighbors.get(findInverseNeighborId(neighborId)).getNeighbors().get(findInverseNeighborId(neighborId))).getBall().getColor().isBlank()) {
                    foundTurns.add(new Turn(hex)); // ii i h n = 1 1 1 0
                    foundTurns.get(foundTurns.size() - 1).addMove(hex, grid.getMatchedHex(neighbors.get(neighborId))); // ii i h n = 1 1 0 1
                    foundTurns.get(foundTurns.size() - 1).addMove(grid.getMatchedHex(neighbors.get(findInverseNeighborId(neighborId))), hex); // ii i h n = 1 0 1 1
                    foundTurns.get(foundTurns.size() - 1).addMove(grid.getMatchedHex(neighbors.get(findInverseNeighborId(neighborId)).getNeighbors().get(findInverseNeighborId(neighborId))), grid.getMatchedHex(neighbors.get(findInverseNeighborId(neighborId)))); // ii i h n = 0 1 1 1
                }
            }

        }

        turns.add(foundTurns);

    }

    private boolean findForce(int force, Hex hex, int neighborId, int maxDepth, int depth, boolean doub, boolean inverse) {
        if(depth > maxDepth){
            if(doub){
                if(inverse){
                    holdForceDouble -= force;
                }
                else{
                    holdForceDouble += force;
                }
            }
            else{
                if(inverse){
                    holdForceTriple -= force;
                }
                else{
                    holdForceTriple += force;
                }
            }
            return false;
        }
        Hex nHex = hex.getNeighbors().get(neighborId);
        if(!grid.onBoard(nHex)){
            if(doub){
                if(inverse){
                    holdForceDouble -= force;
                }
                else{
                    holdForceDouble += force;
                }
            }
            else{
                if(inverse){
                    holdForceTriple -= force;
                }
                else{
                    holdForceTriple += force;
                }
            }
            return false;
        }
        Hex useMe = grid.getMatchedHex(nHex);
        if(useMe != null) {
            if (useMe.getBall().getColor().isBlank()) { // If the next space is empty
                if (doub) {
                    if (inverse) {
                        holdForceDouble -= force;
                    } else {
                        holdForceDouble += force;
                    }
                } else {
                    if (inverse) {
                        holdForceTriple -= force;
                    } else {
                        holdForceTriple += force;
                    }
                }
                return false;
            }
            if (inverse) { // If searching for enemy balls
                if (!useMe.getBall().getColor().equals(currentColor)) { // If another enemy ball is found
                    findForce(force + 1, nHex, neighborId, maxDepth, depth + 1, doub, inverse);
                } else { // If no more enemy balls are found
                    if (doub) {
                        if (inverse) {
                            holdForceDouble -= force;
                        } else {
                            holdForceDouble += force;
                        }
                    } else {
                        if (inverse) {
                            holdForceTriple -= force;
                        } else {
                            holdForceTriple += force;
                        }
                    }
                    return false;
                }
            } else { // If searching for friendly balls
                if (useMe.getBall().getColor().equals(currentColor)) { // If another friendly ball is found
                    findForce(force + 1, nHex, neighborId, maxDepth, depth + 1, doub, inverse);
                } else { // If no more friendly balls are found
                    if (doub) {
                        if (inverse) {
                            holdForceDouble -= force;
                        } else {
                            holdForceDouble += force;
                        }
                    } else {
                        if (inverse) {
                            holdForceTriple -= force;
                        } else {
                            holdForceTriple += force;
                        }
                    }
                    return false;
                }
            }

        }
        return false;
    }

    private int findInverseNeighborId(int id){
        if(id-3 >= 0){
            return id - 3;
        } else {
            return id + 3;
        }
    }

    public void addHex(Hex hex) {
        this.hexes.add(hex);
    }

    public List<List<Turn>> getTurns() {
        return turns;
    }
}
