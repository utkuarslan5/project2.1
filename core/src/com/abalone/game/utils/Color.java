package com.abalone.game.utils;

public enum Color {

    BLANK{
        public  boolean isBlank() { return true;}

        public boolean isPurple() {
            return false;
        }

        public boolean isBlue() {
            return false;
        }
    },
    PURPLE{
        public  boolean isBlank() { return false;}

        public boolean isPurple() {
            return true;
        }

        public boolean isBlue() {
            return false;
        }
    },
    BLUE{
        public  boolean isBlank() { return false;}

        public boolean isPurple() {
            return false;
        }

        public boolean isBlue() {
            return true;
        }
    };

    public abstract boolean isBlank();

    public abstract boolean isPurple();

    public abstract boolean isBlue();
}
