package com.alevat.spaceinvaders.game;

import static com.alevat.spaceinvaders.io.SoundResource.WAVE_NOTES;

class AlienWave {

    private static final int ROWS = 5;
    private static final int COLUMNS = 11;
    private static final int MAX_NUMBER_OF_ALIENS = ROWS * COLUMNS;

    private static final int WAVE_START_X = CombatState.LEFT_X_BOUNDARY;
    private static final int WAVE_START_Y = CombatState.TOP_Y_BOUNDARY;
    private static final int ALIEN_ROW_OFFSET_PIXELS = 8;

    private final CombatState state;
    private Alien[][] aliens;
    private int leftX = WAVE_START_X;
    private int topY = WAVE_START_Y;
    private int currentNoteIndex = 0;

    private int frameCount;

    AlienWave(CombatState state) {
        this.state = state;
    }

    void initialize() {
        aliens = new Alien[ROWS][COLUMNS];
        for (int row = 0; row < ROWS; row++) {
            for (int column = 0; column < COLUMNS; column++) {
                AlienType type = AlienType.ONE;
                aliens[row][column] = buildAlien(type, row, column);
            }
        }
    }

    private Alien buildAlien(AlienType type, int row, int column) {
        return new Alien(state, this, type, row, column);
    }

    int getX(Alien alien) {
        return this.leftX + (alien.getColumn() * Alien.WIDTH);
    }

    int getY(Alien alien) {
        return this.topY + (alien.getRow() * (Alien.HEIGHT + ALIEN_ROW_OFFSET_PIXELS));
    }

    void update() {
        frameCount++;
        if (frameCount % getCadence() == 0) {
            playNote();
            updateAliens();
        }
    }

    private void playNote() {
        getGame().getIOResources().getAudioEngine().play(WAVE_NOTES[currentNoteIndex++]);
        if (currentNoteIndex == WAVE_NOTES.length) {
            currentNoteIndex = 0;
        }
    }

    private void updateAliens() {
        for (int row = 0; row < ROWS; row++) {
            for (int column = 0; column < COLUMNS; column++) {
                Alien alien = aliens[row][column];
                if (alien != null) {
                    alien.update();
                }
            }
        }
    }

    private Game getGame() {
        return state.getGame();
    }

    private int getCadence() {
        return getAlienCount();
    }

    private int getAlienCount() {
        int count = 0;
        for (int row = 0; row < ROWS; row++) {
            for (int column = 0; column < COLUMNS; column++) {
                Alien alien = aliens[row][column];
                if (alien != null) {
                    count++;
                }
            }
        }
        return count;
    }

    int getFrameCount() {
        return frameCount;
    }
}
