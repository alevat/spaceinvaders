package com.alevat.spaceinvaders.game;

import static com.alevat.spaceinvaders.game.HorizontalDirection.LEFT;
import static com.alevat.spaceinvaders.game.HorizontalDirection.RIGHT;
import static com.alevat.spaceinvaders.io.SoundResource.WAVE_NOTES;

class AlienWave {

    private static final int ROWS = 5;
    private static final int COLUMNS = 11;
    private static final int MAX_NUMBER_OF_ALIENS = ROWS * COLUMNS;

    private static final int LEFT_X_BOUNDARY = CombatState.LEFT_X_BOUNDARY;
    private static final int RIGHT_X_BOUNDARY = CombatState.RIGHT_X_BOUNDARY;

    private static final int WAVE_START_X = CombatState.LEFT_X_BOUNDARY;
    private static final int WAVE_START_Y = CombatState.TOP_Y_BOUNDARY + 64;

    private static final int ALIEN_ROW_OFFSET_PIXELS = 8;
    private static final int HORIZONTAL_PIXELS_MOVED_PER_TURN = 4;

    private final CombatState state;
    private Alien[][] aliens;

    private int leftX = WAVE_START_X;
    private int topY = WAVE_START_Y;
    private HorizontalDirection direction = RIGHT;

    private int currentNoteIndex = 0;

    private int frameCount;

    AlienWave(CombatState state) {
        this.state = state;
    }

    void initialize() {
        aliens = new Alien[ROWS][COLUMNS];
        for (int row = 0; row < ROWS; row++) {
            for (int column = 0; column < COLUMNS; column++) {
                AlienType type = getType(row);
                aliens[row][column] = buildAlien(type, row, column);
            }
        }
    }

    private AlienType getType(int row) {
        if (row == 0) {
            return AlienType.THREE;
        } else if (row < 3) {
            return AlienType.TWO;
        } else {
            return AlienType.ONE;
        }
    }

    private Alien buildAlien(AlienType type, int row, int column) {
        return new Alien(state, this, type, row, column);
    }

    int getX(Alien alien) {
        return getX(alien.getColumn());
    }

    private int getX(int column) {
        return this.leftX + (column * Alien.WIDTH);
    }

    int getY(Alien alien) {
        return getY(alien.getRow());
    }

    private int getY(int row) {
        return this.topY + (row * (Alien.HEIGHT + ALIEN_ROW_OFFSET_PIXELS));
    }

    void update() {
        frameCount++;
        if (state.getPlayState() == GamePlayState.COMBAT) {
            if (frameCount % getCadence() == 0) {
                playNote();
                moveWave();
                updateAliens();
            }
        }
    }

    private void moveWave() {
        if (direction == RIGHT) {
            leftX += HORIZONTAL_PIXELS_MOVED_PER_TURN;
        } else if (direction == LEFT) {
            leftX -= HORIZONTAL_PIXELS_MOVED_PER_TURN;
        }
        if (leftX <= LEFT_X_BOUNDARY) {
            direction = RIGHT;
            dropRow();
        } else if (getRightX() >= RIGHT_X_BOUNDARY) {
            direction = LEFT;
            dropRow();
        }
    }

    private int getRightX() {
        int maxOccupiedColumn = 0;
        for (int row = 0; row < ROWS; row++) {
            for (int column = 0; column < COLUMNS; column++) {
                Alien alien = aliens[row][column];
                if (alien != null && column > maxOccupiedColumn) {
                    maxOccupiedColumn = column;
                }
            }
        }
        return getX(maxOccupiedColumn) + Alien.WIDTH;
    }

    private int getBottomY() {
        for (int row = ROWS - 1; row >= 0; row--) {
            for (int column = 0; column < COLUMNS; column++) {
                Alien alien = aliens[row][column];
                if (alien != null) {
                    return getY(row) + Alien.HEIGHT;
                }
            }
        }
        throw new IllegalStateException("No aliens");
    }

    private void dropRow() {
        topY = topY + ((Alien.HEIGHT + ALIEN_ROW_OFFSET_PIXELS) / 2);
        if (aliensHaveLanded()) {
            state.handleAlienConquest();
        }
    }

    private boolean aliensHaveLanded() {
        return getBottomY() >= state.getPlayerCannon().getY() - PlayerCannon.HEIGHT;
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
//        return getAlienCount();
        return 4;
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
