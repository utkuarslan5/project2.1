package com.abalone.game.objects;

import com.abalone.game.gameTree.Node;
import com.abalone.game.gameTree.Tree;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
        if(depth == 1){
            q.add(tree.getRoot());
        }

        // Set current
        current = q.remove();

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
        List<Node> currentListed = new ArrayList<>();
        currentListed.add(current);
        return search(target, current, Stream.concat(path.stream(), currentListed.stream()).collect(Collectors.toList()), depth+1);
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
