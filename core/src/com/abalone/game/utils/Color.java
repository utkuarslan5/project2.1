package com.abalone.game.utils;

public enum Color {

    BLANK {
        public boolean isBlank() {
            return true;
        }

        public boolean isPurple() {
            return false;
        }

        public boolean isBlue() {
            return false;
        }

        @Override
        public boolean equals(Color c) {
            return c.isBlank();
        }
    },
    PURPLE {
        public boolean isBlank() {
            return false;
        }

        public boolean isPurple() {
            return true;
        }

        public boolean isBlue() {
            return false;
        }

        @Override
        public boolean equals(Color c) {
            return c.isPurple();
        }
    },
    BLUE {
        public boolean isBlank() {
            return false;
        }

        public boolean isPurple() {
            return false;
        }

        public boolean isBlue() {
            return true;
        }

        @Override
        public boolean equals(Color c) {
            return c.isBlue();
        }
    };

    public abstract boolean isBlank();

    public abstract boolean isPurple();

    public abstract boolean isBlue();

    public abstract boolean equals(Color c);
}
