package com.abalone.game.gameTree.treeSearch;

import com.abalone.game.gameTree.Node;
import com.abalone.game.gameTree.Tree;

import java.util.ArrayList;
import java.util.List;

public class IterativeDeepening implements TreeSearch {
    private int depth;
    private int step;
    private List<Node> path;
    private TreeSearch searcher;

    public IterativeDeepening(int depth, int step, TreeSearch searcher){
        this.depth = depth;
        this.step = step;
        this.searcher = searcher;
    }


    @Override
    public boolean search(Node target, Node current, List<Node> path, int depth) {
        boolean succes = false;

        for(int i=1; i <= depth; i+=step){
            List<Node> i_path = new ArrayList<>();
            searcher.setDepth(i);
            succes = searcher.search(target, searcher.getTree().getRoot(), i_path, 1);
            this.path = searcher.getPath();
        }

        return succes;
    }

    @Override
    public Tree getTree() {
        return this.searcher.getTree();
    }

    @Override
    public int getDepth() {
        return this.depth;
    }

    @Override
    public List<Node> getPath() {
        return this.path;
    }

    @Override
    public void setDepth(int maxDepth) {
        this.depth = maxDepth;
    }
}
