package com.alevat.spaceinvaders.game;

import java.awt.image.BufferedImage;

import com.alevat.spaceinvaders.io.ImageResource;

import static com.alevat.spaceinvaders.game.PlayerShotState.HIT_SHIELD;
import static com.alevat.spaceinvaders.game.PlayerShotState.IN_FLIGHT;
import static com.alevat.spaceinvaders.game.PlayerShotState.MISSED;

class PlayerShot extends AbstractCombatSprite {

    private static final double VELOCITY_PIXELS_PER_FRAME = 4;
    private static final double STARTING_Y_POSITION =
            PlayerCannon.Y_POSITION - PlayerCannon.HEIGHT + VELOCITY_PIXELS_PER_FRAME;
    private static final int EXPLOSION_FRAMES = 10;

    public static final BufferedImage IMAGE = ImageResource.PLAYER_SHOT.getBufferedImage();
    public static final BufferedImage EXPLODING_IMAGE = ImageResource.PLAYER_SHOT_EXPLODING.getBufferedImage();

    private int x;
    private double y = STARTING_Y_POSITION;
    private PlayerShotState state = IN_FLIGHT;

    PlayerShot(CombatState combatState, PlayerCannon cannon) {
        super(combatState);
        this.x = cannon.getX() + PlayerCannon.BARREL_X_OFFSET;
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getY() {
        return (int) Math.round(y);
    }

    @Override
    public BufferedImage getBufferedImage() {
        if (state == IN_FLIGHT) {
            return IMAGE;
        } else {
            return EXPLODING_IMAGE;
        }
    }

    void update() {
        switch (state) {
            case IN_FLIGHT:
                move();
                break;
            case HIT_SHIELD:
                handleExploding();
                break;
            case MISSED:
                handleExploding();
                break;
            default:
                throw new IllegalStateException("State not handled " + state);
        }
    }

    private void move() {
        y -= VELOCITY_PIXELS_PER_FRAME;
        if (y <= CombatState.TOP_Y_BOUNDARY) {
            miss();
        }
        handlePossibleCollision();
    }

    private void miss() {
        state = MISSED;
        resetFrameCount();
        repositionForExplosion();
    }

    private void repositionForExplosion() {
        x = (int) (x - (EXPLODING_IMAGE.getWidth() / 2.0));
        y = (int) (y - (EXPLODING_IMAGE.getHeight() / 2.0));
    }

    private void handlePossibleCollision() {
        Collision collision = getCombatState().getCollision(this);
        if (collision != null) {
            collision.getTarget().handleShotCollision(this);
        }
    }

    private void handleExploding() {
        if (getFrameCount() == EXPLOSION_FRAMES) {
            getCombatState().removePlayerShot(this);
        }
    }

    void handleShieldCollision(Shield shield) {
        state = HIT_SHIELD;
        repositionForExplosion();
        shield.eraseDamage(this);
    }

    @Override
    public void handleAlienCollision(Alien alien) {
        getCombatState().removePlayerShot(this);
    }

}
