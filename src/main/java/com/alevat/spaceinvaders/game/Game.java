package com.alevat.spaceinvaders.game;

import com.alevat.spaceinvaders.io.InputOutputResources;

public class Game {

    private final InputOutputResources inputOutputResources;
    private final Console console;
    private final GameInputDispatcher inputListener;
    private final Screen screen;
    private final GameLoop gameLoop;

    private GameState state;

    public Game(InputOutputResources inputOutputResources, Console console) {
        this.inputOutputResources = inputOutputResources;
        this.console = console;
        this.inputListener = new GameInputDispatcher(this);
        this.screen = new Screen(this, inputOutputResources.getRenderingEngine());
        this.gameLoop = new GameLoop(this);
    }

    public void start() {
        getConsole().info("Starting...");
        inputOutputResources.getRenderingEngine().initializeScreen();
        inputOutputResources.registerInputListener(inputListener);
        state = new CombatState(this);
        gameLoop.start();
    }

    void quit() {
        gameLoop.quit();
        inputOutputResources.close();
    }

    public InputOutputResources getIOResources() {
        return inputOutputResources;
    }

    GameState getState() {
        return state;
    }

    void setState(GameState state) {
        this.state = state;
    }

    Console getConsole() {
        return console;
    }

    Screen getScreen() {
        return screen;
    }
}
