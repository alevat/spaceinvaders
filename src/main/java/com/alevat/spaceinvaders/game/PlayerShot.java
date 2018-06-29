package com.alevat.spaceinvaders.game;

import com.alevat.spaceinvaders.io.ImageResource;
import com.alevat.spaceinvaders.io.Sprite;

import static com.alevat.spaceinvaders.game.PlayerShotState.IN_FLIGHT;
import static com.alevat.spaceinvaders.game.PlayerShotState.MISSED;

class PlayerShot implements Sprite {

    private static final double VELOCITY_PIXELS_PER_FRAME = 4;
    private static final double STARTING_Y_POSITION =
            PlayerCannon.Y_POSITION + PlayerCannon.HEIGHT - VELOCITY_PIXELS_PER_FRAME;
    private static final int MISSED_SHOT_EXPLOSION_FRAMES = 10;

    private final CombatState combatState;
    private int x;
    private double y = STARTING_Y_POSITION;
    private PlayerShotState shotState = IN_FLIGHT;
    private int missedShotExplosionFrameCount = 0;

    PlayerShot(CombatState combatState, PlayerCannon cannon) {
        this.combatState = combatState;
        this.x = cannon.getX() + PlayerCannon.BARREL_X_OFFSET;
        combatState.getScreen().addSprite(this);
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
    public ImageResource getImageResource() {
        if (shotState == IN_FLIGHT) {
            return ImageResource.PLAYER_SHOT;
        } else {
            return ImageResource.PLAYER_SHOT_EXPLODING;
        }
    }

    void update() {
        if (shotState == IN_FLIGHT) {
            y += VELOCITY_PIXELS_PER_FRAME;
            if (y >= CombatState.TOP_Y_BOUNDARY) {
                shotState = MISSED;
                x = (int) (x - (ImageResource.PLAYER_SHOT_EXPLODING.getWidth() / 2.0));
            }
        } else if (shotState == MISSED
                && missedShotExplosionFrameCount++ == MISSED_SHOT_EXPLOSION_FRAMES)
        {
            combatState.getScreen().removeSprite(this);
            combatState.setPlayerShot(null);
        }
    }

}
