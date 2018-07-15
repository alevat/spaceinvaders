package com.alevat.spaceinvaders.game;

import java.awt.image.BufferedImage;

import com.alevat.spaceinvaders.io.ImageResource;
import com.alevat.spaceinvaders.io.SoundResource;

class PlayerCannon extends AbstractCombatSprite {

    private static final int LEFT_X_BOUNDARY = CombatState.LEFT_X_BOUNDARY + 16;
    private static final int RIGHT_X_BOUNDARY = CombatState.RIGHT_X_BOUNDARY - 16;
    private static final int STARTING_X_POSITION = LEFT_X_BOUNDARY;

    private static final double VELOCITY_PIXELS_PER_FRAME = 1.0;

    static final int Y_POSITION = Screen.HEIGHT - 32;
    static final int WIDTH = ImageResource.PLAYER_CANNON.getWidth();
    static final int HEIGHT = ImageResource.PLAYER_CANNON.getHeight();
    static final int BARREL_X_OFFSET = 6;

    static final BufferedImage CANNON_IMAGE = ImageResource.PLAYER_CANNON.getBufferedImage();

    static final ImageResource[] EXPLOSION_IMAGE_RESOURCES = new ImageResource[] {
            ImageResource.PLAYER_CANNON_EXPLODING_1,
            ImageResource.PLAYER_CANNON_EXPLODING_2,
    };

    private HorizontalDirection direction = HorizontalDirection.STILL;

    double x = STARTING_X_POSITION;

    PlayerCannon(CombatState state) {
        super(state);
    }

    public void update() {
        if (getCombatState().getPlayState() == GamePlayState.COMBAT) {
            handlePlayerMovement();
        }
    }

    private void handlePlayerMovement() {
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
        if (x > LEFT_X_BOUNDARY) {
            x -= VELOCITY_PIXELS_PER_FRAME;
        }
    }

    private void moveRight() {
        if (x < RIGHT_X_BOUNDARY) {
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
        if (getCombatState().getPlayState() == GamePlayState.COMBAT) {
            return CANNON_IMAGE;
        } else {
            return EXPLOSION_IMAGE_RESOURCES[0].getBufferedImage();
        }
    }

}
