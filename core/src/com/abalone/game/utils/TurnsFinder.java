package com.abalone.game.utils;

import com.abalone.game.objects.Hex;
import com.abalone.game.objects.HexGrid;
import com.abalone.game.objects.Turn;

import java.util.ArrayList;
import java.util.List;

public class TurnsFinder {
    private HexGrid grid;
    private List<Hex> hexes = new ArrayList<>();
    private List<List<Turn>> turns = new ArrayList<>();
    private int holdForceDouble;
    private int holdForceTripple;
    private Color currentColor;

    public TurnsFinder(HexGrid grid){
        this.grid = grid;
        this.hexes = grid.getHexList();
    }

    public TurnsFinder(Hex hex){
        this.hexes.add(hex);
    }

    public void findTurns(Hex hex){
        currentColor = hex.getBall().getColor();
        List<Hex> neigbors = hex.getNeighbors();
        List<Turn> foundTurns = new ArrayList<>();
        int neighborId = 0;

        // Loop though all directions the ball can move in.
        for(Hex h : neigbors) {
            boolean inlineAllowed = true;

            /** All turns **/
            if (!grid.onBoard(h)){
                continue;
            }

            /** Broadside turns **/
            // TODO: Broadside turns

            /** In-line turns **/

            // If the movement is blocked by a friendly ball
            if(h.isOccupied() && h.getBall().getColor().equals(hex.getBall().getColor())){
                continue;
            }

            // Find forces for two and three consecutive balls
            holdForceDouble = 0;
            holdForceTripple = 0;
            findForce(1, hex, findInverseNeighborId(neighborId), 2, 2, true, false);
            findForce(0, hex, neighborId,3, 2, true, true);
            findForce(1, hex, findInverseNeighborId(neighborId), 3, 2, false, false);
            findForce(0, hex, neighborId,4, 2, false, true);

            // Get in-line turns from forces
            if(holdForceDouble > 0){
                foundTurns.add(new Turn(hex)); // i h n = 1 1 0
                foundTurns.get(foundTurns.size()-1).addMove(hex, neigbors.get(neighborId)); // i h n = 1 0 1
                foundTurns.get(foundTurns.size()-1).addMove(neigbors.get(findInverseNeighborId(neighborId)), hex); // i h n = 0 1 1
            }
            if(holdForceTripple > 0){
                foundTurns.add(new Turn(hex)); // ii i h n = 1 1 1 0
                foundTurns.get(foundTurns.size()-1).addMove(hex, neigbors.get(neighborId)); // ii i h n = 1 1 0 1
                foundTurns.get(foundTurns.size()-1).addMove(neigbors.get(findInverseNeighborId(neighborId)), hex); // ii i h n = 1 0 1 1
                foundTurns.get(foundTurns.size()-1).addMove(neigbors.get(findInverseNeighborId(neighborId)).getNeighbors().get(findInverseNeighborId(neighborId)), neigbors.get(findInverseNeighborId(neighborId))); // ii i h n = 0 1 1 1
            }

            neighborId++;
        }

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
                    holdForceTripple -= force;
                }
                else{
                    holdForceTripple += force;
                }
            }
            return false;
        }
        Hex nHex = hex.getNeighbors().get(neighborId);
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
                        holdForceTripple -= force;
                    } else {
                        holdForceTripple += force;
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
                            holdForceTripple -= force;
                        } else {
                            holdForceTripple += force;
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
                            holdForceTripple -= force;
                        } else {
                            holdForceTripple += force;
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
            return id-3;
        }
        else{
            return id+3;
        }
    }

    public void addHex(Hex hex){
        this.hexes.add(hex);
    }
}
