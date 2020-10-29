package com.abalone.game.gameTree;

import com.abalone.game.objects.Board;

import java.util.ArrayList;
import java.util.List;

public class Tree {

    private ArrayList<Node<Board>> tree = null;

    public void createBranch(Board currBoard){

        Node<Board> root = new Node<>(currBoard);
        tree.add(root);
      //  ArrayList<TurnsFinder> available = root.getData().getLegalMoves(); //Supposed to find all legal moves from the root
        // There is no method getLegalMoves() in board yet

      /*  for(Move i : available){ //For every legal move, add it as a child from the root
            root.addChild(new Node<>(root.getData().getState()));
        }**/

        List<Node<Board>> currChildren = root.getChildren();
        if(currChildren != null){ //will stop at a leaf
            for(Node<Board> child : currChildren){
                createBranch(child.getStateData()); //children will become new root
            }
        }
    }

    public ArrayList<Node<Board>> getTree() {
        return tree;
    }
}
