package com.alevat.spaceinvaders.game;

import com.alevat.spaceinvaders.io.InputListener;

public class GameOverState extends AbstractGameState {

    private GameOverInputListener inputListener = new GameOverInputListener(this);

    public GameOverState(Game game) {
        super(game);
    }

    @Override
    public InputListener getInputListener() {
        return inputListener;
    }

}
