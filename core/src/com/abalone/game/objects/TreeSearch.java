package com.abalone.game.objects;

import com.abalone.game.gameTree.Node;
import com.abalone.game.gameTree.Tree;

import java.util.List;

public interface TreeSearch {
    int maxDepth = 0;

    boolean search(Node target, Node current, List<Node> path, int depth);

    Tree getTree();
    int getDepth();
    List<Node> getPath();
}
