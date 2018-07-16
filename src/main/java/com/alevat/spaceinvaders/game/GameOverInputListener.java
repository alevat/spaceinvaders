package com.alevat.spaceinvaders.game;

class GameOverInputListener extends GameStateInputListenerAdapter{

    private final GameOverState gameOverState;

    GameOverInputListener(GameOverState gameOverState) {
        this.gameOverState = gameOverState;
    }

    @Override
    GameState getGameState() {
        return gameOverState;
    }

}
