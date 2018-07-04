package com.alevat.spaceinvaders.game;

import java.awt.image.BufferedImage;

class Alien extends AbstractCombatSprite {

    static final int WIDTH = 16;
    static final int HEIGHT = 8;

    private final AlienWave alienWave;
    private final AlienType type;
    private int x;
    private int y;

    Alien(CombatState combatState, AlienWave alienWave, AlienType type, int x, int y) {
        super(combatState);
        this.alienWave = alienWave;
        this.type = type;
        this.x = x;
        this.y = y;
    }

    @Override
    public int getX() {
        return 0;
    }

    @Override
    public int getY() {
        return 0;
    }

    @Override
    public BufferedImage getBufferedImage() {
        return null;
    }
}
