package com.alevat.spaceinvaders.game;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    private static final int ALIEN_ROW_OFFSET_PIXELS = 8;
    private static final int HORIZONTAL_PIXELS_MOVED_PER_TURN = 4;

    private static final int SLOWEST_CADENCE_FRAMES = 50;
    private static final int FASTEST_CADENCE_FRAMES = 2;

    private final CombatState state;
    private final int waveStartY;
    private List<Alien> aliens = new ArrayList<>();

    private int leftX = WAVE_START_X;
    private int topY;
    private HorizontalDirection direction = RIGHT;

    private int currentNoteIndex = 0;
    private boolean alienExploding;

    AlienWave(CombatState state, int waveStartY) {
        this.state = state;
        this.waveStartY = waveStartY;
        topY = waveStartY;
    }

    void initialize() {
        for (int row = 0; row < ROWS; row++) {
            for (int column = 0; column < COLUMNS; column++) {
                AlienType type = getType(row);
                aliens.add(buildAlien(type, row, column));
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
        if (state.getPlayState() == GamePlayState.COMBAT) {
            checkForExplodingAlien();
            updateAliens();
            if (state.getFrameCount() % getCadence() == 0) {
                if (!alienExploding) {
                    playNote();
                    moveWave();
                }
            }
        }
    }

    private void moveWave() {
        if (direction == RIGHT) {
            leftX += HORIZONTAL_PIXELS_MOVED_PER_TURN;
        } else if (direction == LEFT) {
            leftX -= HORIZONTAL_PIXELS_MOVED_PER_TURN;
        }
        if (getOccupiedLeftX() <= LEFT_X_BOUNDARY) {
            direction = RIGHT;
            dropRow();
        } else if (getRightX() >= RIGHT_X_BOUNDARY) {
            direction = LEFT;
            dropRow();
        }
        if (!alienExploding) {
            for (Alien alien : aliens) {
                alien.move();
            }
        }
    }

    private int getOccupiedLeftX() {
        int leftMostX = getRightX();
        for (Alien alien : getAliens()) {
            if (alien.getColumn() < leftMostX) {
                leftMostX = alien.getX();
            }
        }
        return leftMostX;
    }

    private int getRightX() {
        int maxOccupiedColumn = 0;
        for (Alien alien : aliens) {
            if (alien.getColumn() > maxOccupiedColumn) {
                maxOccupiedColumn = alien.getColumn();
            }
        }
        return getX(maxOccupiedColumn) + Alien.WIDTH;
    }

    private int getBottomY() {
        int maxOccupiedRow = 0;
        for (Alien alien : aliens) {
            if (alien.getRow() > maxOccupiedRow) {
                maxOccupiedRow = alien.getRow();
            }
        }
        return getY(maxOccupiedRow) + Alien.HEIGHT;
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
        getCurrentAliens().stream().forEach(Alien::update);
    }

    // To protect against concurrent modification
    private Set<Alien> getCurrentAliens() {
        return new HashSet<>(aliens);
    }

    private void checkForExplodingAlien() {
        for (Alien alien : aliens) {
            if (alien.isExploding()) {
                alienExploding = true;
                return;
            }
        }
        alienExploding = false;
    }

    private Game getGame() {
        return state.getGame();
    }

    private int getCadence() {
        float percentAliensRemaining = (float) aliens.size() / MAX_NUMBER_OF_ALIENS;
        return (int) (percentAliensRemaining * (SLOWEST_CADENCE_FRAMES - FASTEST_CADENCE_FRAMES)) + FASTEST_CADENCE_FRAMES;
    }

    void remove(Alien alien) {
        getGame().getScreen().removeSprite(alien);
        aliens.remove(alien);
        if (aliens.isEmpty()) {
            state.handleWaveCleared();
        }
    }

    List<Alien> getAliens() {
        return aliens;
    }

    int getWaveStartY() {
        return waveStartY;
    }
}
