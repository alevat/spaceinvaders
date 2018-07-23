package com.alevat.spaceinvaders.game;

import java.awt.image.BufferedImage;

import com.alevat.spaceinvaders.io.ImageResource;

class Alien extends AbstractCombatSprite {

    static final int WIDTH = 16;
    static final int HEIGHT = 8;

    private final AlienWave wave;
    private final AlienType type;

    private int row;
    private int column;
    private boolean hit;

    private final ImageResource[] imageResources;
    private int imageFrameIndex = 0;

    Alien(CombatState combatState, AlienWave wave, AlienType type, int row, int column) {
        super(combatState);
        this.wave = wave;
        this.type = type;
        this.row = row;
        this.column = column;
        this.imageResources = type.getImageResources();
    }

    @Override
    public int getX() {
        return wave.getX(this);
    }

    @Override
    public int getY() {
        return wave.getY(this);
    }

    int getRow() {
        return row;
    }

    int getColumn() {
        return column;
    }

    @Override
    public BufferedImage getBufferedImage() {
        if (!hit) {
            return imageResources[imageFrameIndex].getBufferedImage();
        } else {
            return ImageResource.ALIEN_EXPLODING.getBufferedImage();
        }
    }

    public void update() {
        imageFrameIndex++;
        if (imageFrameIndex == imageResources.length) {
            imageFrameIndex = 0;
        }
        handlePossibleCollision();
    }

    private void handlePossibleCollision() {
        Collision collision = getCombatState().getCollision(this);
        if (collision != null) {
            collision.getTarget().handleAlienCollision(this);
        }
    }

    @Override
    public void handleShotCollision(PlayerShot playerShot) {
        this.hit = true;
        playerShot.handleAlienCollision(this);
    }
}
