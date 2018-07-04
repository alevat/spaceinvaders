package com.alevat.spaceinvaders.game;

import com.alevat.spaceinvaders.io.Sprite;

public interface CombatSprite extends Sprite {

    boolean detectCollision(CombatSprite sprite);

    void handleShotCollision(PlayerShot playerShot);

    boolean isPixelOn(int screenX, int screenY);
}
