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
        currentColor = hex.getBall().getColor();
        List<Hex> neighbors = hex.getNeighbors();
        System.out.println("secure size of neighbors "+hex.getNeighbors().size());
        List<Turn> foundTurns = new ArrayList<>();
        int neighborId = -1;

        System.out.println("neighbors size:"+neighbors.size());

        // Loop though all directions the ball can move in.
        for (Hex nh : neighbors) {
            Hex h = grid.getMatchedHex(nh);
            neighborId++;

            System.out.println(neighborId+"/i"+findInverseNeighborId(neighborId));

            boolean inlineAllowed = true;

            /** All turns **/
            if (!grid.onBoard(h)) {
                System.out.println("Not on board");
                continue;
            }

            /** Broadside turns **/
            // TODO: Broadside turns, see below (in progress)

            /** In-line turns **/

            // If the movement is blocked by a friendly ball
            if(h.isOccupied() && h.getBall().getColor().equals(hex.getBall().getColor())){
                System.out.println("Friendly ball block");
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
            if (holdForceDouble > 0) {
                foundTurns.add(new Turn(hex)); // i h n = 1 1 0
                foundTurns.get(foundTurns.size() - 1).addMove(hex, neighbors.get(neighborId)); // i h n = 1 0 1
                foundTurns.get(foundTurns.size() - 1).addMove(neighbors.get(findInverseNeighborId(neighborId)), hex); // i h n = 0 1 1
            }
            if (holdForceTriple > 0) {
                foundTurns.add(new Turn(hex)); // ii i h n = 1 1 1 0
                foundTurns.get(foundTurns.size() - 1).addMove(hex, neighbors.get(neighborId)); // ii i h n = 1 1 0 1
                foundTurns.get(foundTurns.size() - 1).addMove(neighbors.get(findInverseNeighborId(neighborId)), hex); // ii i h n = 1 0 1 1
                foundTurns.get(foundTurns.size() - 1).addMove(neighbors.get(findInverseNeighborId(neighborId)).getNeighbors().get(findInverseNeighborId(neighborId)), neighbors.get(findInverseNeighborId(neighborId))); // ii i h n = 0 1 1 1
            }

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
                    holdForceTriple -= force;
                }
                else{
                    holdForceTriple += force;
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

    //TODO: add a method to check if selected balls are inline

    //TODO: complete broadsideMove method, return broadsideTurn containing all legal broadside moves from given hexes
    public void broadsideMove(List<Hex> hexes) {
        Turn broadsideTurn = new Turn(hexes);
        //split to 3 cases: single ball, two balls, three balls
        Hex h1, h2, h3;
        switch (hexes.size()) {
            case 1:
                h1 = hexes.get(0);
                for (Hex h : h1.getNeighbors()) {
                    if (!h.isOccupied()) broadsideTurn.addMove(h1, h);
                }
                //return broadsideTurn;
            case 2:
                h1 = hexes.get(0);
                h2 = hexes.get(1);
                //need to check if h1,h2 are neighbors
                //after the check, find their line segment
                //
            case 3:
                h1 = hexes.get(0);
                h2 = hexes.get(1);
                h3 = hexes.get(2);
                //check h1,h2 and h2,h3 to be inline
                //find line segment of two subgroups
                //iterate over 4 possible line segments
                //add moves if segment is not occupied
        }

    }

    public List<List<Turn>> getTurns() {
        return turns;
    }
}
