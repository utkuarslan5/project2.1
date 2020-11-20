package com.abalone.game.objects;

import com.abalone.game.gameTree.Node;
import com.abalone.game.gameTree.Tree;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DepthSearch implements TreeSearch {
    private int maxDepth = 0;
    private Tree tree;
    private List<Node> finalPath;

    public DepthSearch(int maxDepth, Tree tree){
        this.maxDepth = maxDepth;
        this.tree = tree;
    }

    @Override
    public boolean search(Node target, Node current, List<Node> path, int depth) {
        path.add(current);
        // If target node reached
        if(current.equals(target)){
            this.finalPath = path;
            return true;
        }
        // If maximal depth reached
        if(depth > maxDepth){
            return false;
        }

        List<Node> children = current.getChildren();
        for(Node child : children){
            if(search(target, child, path, depth+1)){
                return true;
            }
        }
        return false;
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
