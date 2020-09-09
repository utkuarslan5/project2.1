package com.abalone.game.states;

import com.abalone.game.managers.GameStateManager;

public abstract class State {
    protected GameStateManager gsm;

    protected State(GameStateManager ourGsm) {
        this.gsm = ourGsm;
        init();
    }

    public abstract void init();
    public abstract void update(float dt);
    public abstract void draw();
    public abstract void handleInput();
    public abstract void dispose();

}
