package com.abalone.game.utils;

public enum Color {

    WHITE{
        public boolean isWhite() {
            return true;
        }

        public boolean isBlack() {
            return false;
        }
    },
    BLACK{
        public boolean isWhite() {
            return false;
        }

        public boolean isBlack() {
            return true;
        }
    };

    public abstract boolean isWhite();

    public abstract boolean isBlack();
}
