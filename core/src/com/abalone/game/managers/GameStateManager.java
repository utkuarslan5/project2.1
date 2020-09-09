package com.abalone.game.managers;

import com.abalone.game.states.MenuState;
import com.abalone.game.states.State;
import com.badlogic.gdx.InputAdapter;

import java.util.Stack;

public class GameStateManager extends InputAdapter {
    private Stack<State> currentState;

    public GameStateManager(){
        currentState = new Stack<>();
        init();
    }

    public void init(){
        State initialScreen = new MenuState(this);
        initialScreen.init();
        currentState.push(initialScreen);
    }
    public void update(float dt){
        currentState.peek().update(dt);
    }

    public void draw(){
        currentState.peek().draw();
    }

    public GameStateManager push(State state){
        state.init();
        currentState.push(state);
        return this;
    }

    public GameStateManager pop() {
        currentState.peek().dispose();
        currentState.pop();
        return this;
    }

}
