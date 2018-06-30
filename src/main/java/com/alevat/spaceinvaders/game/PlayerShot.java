package com.alevat.spaceinvaders.game;

import java.awt.image.BufferedImage;

import com.alevat.spaceinvaders.io.ImageResource;

import static com.alevat.spaceinvaders.game.PlayerShotState.IN_FLIGHT;
import static com.alevat.spaceinvaders.game.PlayerShotState.MISSED;

class PlayerShot extends AbstractCombatSprite {

    private static final double VELOCITY_PIXELS_PER_FRAME = 4;
    private static final double STARTING_Y_POSITION =
            PlayerCannon.Y_POSITION + PlayerCannon.HEIGHT - VELOCITY_PIXELS_PER_FRAME;
    private static final int MISSED_SHOT_EXPLOSION_FRAMES = 10;
    public static final BufferedImage SHOT_IMAGE = ImageResource.PLAYER_SHOT.getBufferedImage();
    public static final BufferedImage SHOT_EXPLODING_IMAGE = ImageResource.PLAYER_SHOT_EXPLODING.getBufferedImage();

    private int x;
    private double y = STARTING_Y_POSITION;
    private PlayerShotState shotState = IN_FLIGHT;
    private int missedShotExplosionFrameCount = 0;

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
        if (shotState == IN_FLIGHT) {
            return SHOT_IMAGE;
        } else {
            return SHOT_EXPLODING_IMAGE;
        }
    }

    void update() {
        if (shotState == IN_FLIGHT) {
            move();
            handlePossibleCollision();
        } else if (shotState == MISSED && missedShotExplosionFrameCount++ == MISSED_SHOT_EXPLOSION_FRAMES)
        {
            getScreen().removeSprite(this);
            getCombatState().setPlayerShot(null);
        }
    }

    private void move() {
        y += VELOCITY_PIXELS_PER_FRAME;
        if (y >= CombatState.TOP_Y_BOUNDARY) {
            shotState = MISSED;
            x = (int) (x - (SHOT_EXPLODING_IMAGE.getWidth() / 2.0));
        }
    }

    private void handlePossibleCollision() {
        Collision collision = getCombatState().getCollision(this);
        if (collision != null) {
            shotState = MISSED;
            x = (int) (x - (SHOT_EXPLODING_IMAGE.getWidth() / 2.0));
        }
    }

}
