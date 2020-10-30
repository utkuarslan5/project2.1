package com.abalone.game.gameTree;
import com.abalone.game.objects.HexGrid;
import com.abalone.game.objects.Turn;
import com.abalone.game.utils.TurnsFinder;
import java.util.ArrayList;
import java.util.List;

// This class is supposed to create states of a board after a move is made, first state will be the initial position,
// where no moves are made yet (the root of the tree)
// a certain BoardState is the data of a certain node (so kinda connect with Node.setData() etc)
// take into consideration: Position of balls, cohesion and center proximity

public class BoardState {

    private TurnsFinder turnsFinder;
    private HexGrid hexGrid;
    private List<List<Turn>> legalMoves;
    private Heuristics heuristics;

    public BoardState(List<List<Turn>> availableMoves) {
        hexGrid = new HexGrid();
        turnsFinder = new TurnsFinder(hexGrid);
        this.legalMoves = getLegalMoves();
    }

    public List<List<Turn>> getLegalMoves() {
        return turnsFinder.getTurns();
    }

    // creates a new board state after a move is made
    // is called in line 24 in Tree class
    // if you run this first time, "newMoves" will be all legal moves from the beginning
    public BoardState getState(Turn newMove){

        List<List<Turn>> newLegalMoves = new ArrayList<>();
        List<Turn> movesMade = new ArrayList<>();

        movesMade.add(newMove);

        // Since the legal moves are stored in a list of lists we have to loop through it twice to get each legal move
        // each legal move is stored in a list available, each list available is stored in the list legalMoves
        for(int i = 0; i < legalMoves.size(); i++) {
            List<Turn> available = legalMoves.get(i);

            for (int j = 0; j < available.size(); j++) {
                //if each legal move (in available) is not equal to the new move that is made, create newAvail and add it to newLegalMoves
                //if one legal move does equal to the move that is made, add only the moves to newAvail, that are not equal to the newMove
                if (available.get(j) != newMove) {
                    List<Turn> newAvail = new ArrayList<>();
                    newAvail.add(available.get(j));
                    newLegalMoves.add(newAvail);
                }
            }
        }

        //create further branches build on every move
        return new BoardState(newLegalMoves);
    }
}
