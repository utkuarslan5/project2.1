package com.abalone.game.gameTree;

import com.abalone.game.objects.Board;
import com.abalone.game.objects.Hex;
import com.abalone.game.objects.Turn;
import com.abalone.game.utils.Color;
import com.abalone.game.utils.TurnsFinder;

import java.util.ArrayList;
import java.util.List;

import static com.abalone.game.utils.Color.BLUE;
import static com.abalone.game.utils.Color.PURPLE;

public class Node {

    private final BoardState boardState;
    private final List<Node> children;
    private Node parent;
    private int depth;

    public Node(Board board, int depthTree, int depth) {
        this.depth = depth;

        Color player;//temporary solution
        if (depth % 2 == 0) player = PURPLE;
        else player = BLUE;

        boardState = constructBoardState(board, player);
        this.children = new ArrayList();
        TurnsFinder turnsFinder = new TurnsFinder(boardState.getBoard().getHexGrid());

        if (depthTree > depth) {
            // If the depth of this node is even, this mean it's the state of the board after a move of the human player
            // So we get the purple balls (balls of the AI) because it is the turn of the AI to play
            // Otherwise we take the blue balls because it is the turn of the human to play
            List<Hex> hexes = (depth % 2 == 0) ? boardState.getBoard().getPurpleHex() : boardState.getBoard().getBlueHex(); //define which color to select by the color of the player in BoardState

            turnsFinder.clearTurns();
            for(Hex hex : hexes) {
                // calculte all turns for each hex
                turnsFinder.findTurns(hex);
            }
            // get every calculated turns for all hexes
            List<List<Turn>> allTurns = turnsFinder.getTurns();
            for(List<Turn> ts : allTurns) {
                for (Turn t : ts) {
                    Board newBoard = new Board();// board.move(availableMoves[iMove]);
                    // TODO: apply the turn to the board (by creating a new board, attention to reference)
                    this.addChild(new Node(newBoard, depthTree, depth + 1));
                }
            }
        }
        this.depth = depth;
    }

    private BoardState constructBoardState(Board board, Color player) {
        return new BoardState(board, player);
    }

    public void addChild(Node child) {
        child.setParent(this);
        this.children.add(child);
    }

    public boolean isChildOf(Node child) {
        return children.contains(child);
    }

    public Node getChild(int i) {
        return children.get(i);
    }

    public List<Node> getChildren() {
        return children;
    }


    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }
}
