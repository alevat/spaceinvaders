package com.alevat.spaceinvaders.game;

class AlienWave {

    private static final int ROWS = 5;
    private static final int COLUMNS = 11;

    private final CombatState state;
    private Alien[][] aliens;

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
        int x = column * Alien.WIDTH;
        int y = row * Alien.HEIGHT;
        return new Alien(state, this, type, x, y);
    }

}
