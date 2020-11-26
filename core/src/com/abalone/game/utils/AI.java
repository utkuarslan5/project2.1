package com.abalone.game.utils;

public enum AI {
    MINIMAX {
        public boolean isMinimax() {
            return true;
        }
        public boolean isNegamax() {
            return false;
        }

    },
    NEGAMAX {
        public boolean isNegamax() {
            return true;
        }
        public boolean isMinimax() {
            return false;
        }

    },
}
