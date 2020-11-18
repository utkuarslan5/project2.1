package com.abalone.game.objects;

import com.abalone.game.gameTree.Node;
import com.abalone.game.gameTree.Tree;

public interface TreeSearch {
    int Depth = 0;

    Node search();

    Tree getTree();
    int getDepth();
}
