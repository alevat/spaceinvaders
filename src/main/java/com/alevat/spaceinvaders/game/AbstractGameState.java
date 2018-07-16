package com.alevat.spaceinvaders.game;

abstract class AbstractGameState implements GameState {

    private final Game game;
    private int frameCount;

    AbstractGameState(Game game) {
        this.game = game;
    }

    @Override
    public Game getGame() {
        return game;
    }

    @Override
    public Console getConsole() {
        return game.getConsole();
    }

    Screen getScreen() {
        return getGame().getScreen();
    }

    @Override
    public void update() {
        frameCount++;
    }

    int getFrameCount() {
        return frameCount;
    }
}
