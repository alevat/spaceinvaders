package com.alevat.spaceinvaders.game;

import java.awt.image.BufferedImage;

import com.alevat.spaceinvaders.io.AudioEngine;
import com.alevat.spaceinvaders.io.ImageResource;
import com.alevat.spaceinvaders.io.SoundResource;

class PlayerCannon extends AbstractCombatSprite {

    private static final double VELOCITY_PIXELS_PER_FRAME = 1.0;
    private static final int STARTING_X_POSITION = 20;
    static final int Y_POSITION = Screen.HEIGHT - 32;
    static final int WIDTH = ImageResource.PLAYER_CANNON.getWidth();
    static final int HEIGHT = ImageResource.PLAYER_CANNON.getHeight();
    static final int BARREL_X_OFFSET = 6;
    public static final BufferedImage CANNON_IMAGE = ImageResource.PLAYER_CANNON.getBufferedImage();

    private HorizontalDirection direction = HorizontalDirection.STILL;

    double x = STARTING_X_POSITION;

    PlayerCannon(CombatState state) {
        super(state);
    }

    public void update() {
        switch (direction) {
            case LEFT:
                moveLeft();
                break;
            case RIGHT:
                moveRight();
                break;
            default:
                // do nothing
        }
    }

    private void moveLeft() {
        if (x > CombatState.LEFT_X_BOUNDARY) {
            x -= VELOCITY_PIXELS_PER_FRAME;
        }
    }

    private void moveRight() {
        if (x < CombatState.RIGHT_X_BOUNDARY - WIDTH) {
            x += VELOCITY_PIXELS_PER_FRAME;
        }
    }

    HorizontalDirection getDirection() {
        return direction;
    }

    void setDirection(HorizontalDirection direction) {
        getConsole().info("PlayerCannon direction set to " + direction);
        this.direction = direction;
    }

    void fire() {
        if (getCombatState().canPlayerCannonFire()) {
            getCombatState().addPlayerShot(new PlayerShot(getCombatState(), this));
            getAudioEngine().play(SoundResource.FIRE_SHOT);
        }
    }

    @Override
    public int getX() {
        return (int) Math.round(x);
    }

    @Override
    public int getY() {
        return Y_POSITION;
    }

    @Override
    public BufferedImage getBufferedImage() {
        return CANNON_IMAGE;
    }

}
