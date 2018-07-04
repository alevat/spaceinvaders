package com.alevat.spaceinvaders.game;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

import static java.lang.System.nanoTime;

class GameLoop implements Runnable {

    private final static int TARGET_FPS = 60;
    private final static long TARGET_NANOSECONDS_PER_FRAME = TimeUnit.SECONDS.toNanos(1) / TARGET_FPS;

    private final Game game;
    private boolean running;
    private long frameCount = 0;
    private long startTimeNanos;
    private long cycleStartTimeNanos;

    GameLoop(Game game) {
        this.game = game;
    }

    void start() {
        Thread thread = new Thread(this);
        thread.setPriority(Thread.MAX_PRIORITY);
        thread.start();
    }

    @Override
    public void run() {
        running = true;
        startTimeNanos = nanoTime();
        try {
            while (running) {
                executeGameCycle();
            }
        } catch (Exception e) {
            getConsole().error("Unexpected error", e);
            System.exit(1);
        }
    }

    private void executeGameCycle() {
        cycleStartTimeNanos = nanoTime();
        update();
        render();
        sleep();
        frameCount++;
        if (frameCount % 100 == 0) {
            getConsole().info("Average FPS: " + getAverageFPS());
        }
    }

    private double getAverageFPS() {
        final long currentTimeNanos = nanoTime();
        final long durationNanos = currentTimeNanos - startTimeNanos;
        double averageFPS = (double) frameCount / TimeUnit.NANOSECONDS.toSeconds(durationNanos);
        frameCount = 0;
        startTimeNanos = nanoTime();
        return averageFPS;
    }

    private void update() {
        getGameState().update();
    }

    private void render() {
        getScreen().render();
    }

    private void sleep() {
        final long currentTimeNanos = nanoTime();
        final long elapsedCycleTimeNanos = cycleStartTimeNanos - currentTimeNanos;
        final long requestedSleepTimeNanos = TARGET_NANOSECONDS_PER_FRAME - elapsedCycleTimeNanos;
        LockSupport.parkNanos(requestedSleepTimeNanos);
    }

    void quit() {
        running = false;
    }

    private GameState getGameState() {
        return game.getState();
    }

    private Screen getScreen() {
        return game.getScreen();
    }

    private Console getConsole() {
        return game.getConsole();
    }
}
