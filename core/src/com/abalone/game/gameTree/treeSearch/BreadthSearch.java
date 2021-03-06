package com.abalone.game.gameTree.treeSearch;

import com.abalone.game.gameTree.Node;
import com.abalone.game.gameTree.Tree;

import java.util.ArrayDeque;
import java.util.List;
import java.util.Queue;

public class BreadthSearch implements TreeSearch {
    private int maxDepth = 0;
    private Tree tree;
    private List<Node> finalPath;
    private Queue<Node> q = new ArrayDeque();

    public BreadthSearch(int maxDepth, Tree tree){
        this.maxDepth = maxDepth;
        this.tree = tree;
    }

    @Override
    public boolean search(Node target, Node current, List<Node> path, int depth) {
        // If on first depth
        if(depth == 0){
            q.add(tree.getRoot());
        }

        // Set current
        current = q.remove();
        path.add(current);
        depth = current.getDepth();

        // If target node reached
        if(current.equals(target)){
            this.finalPath = path;
            return true;
        }
        // If maximal depth reached
        if(depth > maxDepth){
            return false;
        }
        // If q is empty
        if(q.isEmpty()){
            return false;
        }

        q.addAll(current.getChildren());
        return search(target, current, path, depth+1);
    }

    @Override
    public Tree getTree() {
        return this.tree;
    }

    @Override
    public int getDepth() {
        return this.maxDepth;
    }

    @Override
    public List<Node> getPath() {
        return finalPath;
    }

    @Override
    public void setDepth(int maxDepth) {
        this.maxDepth = maxDepth;
    }
}
