package com.alevat.spaceinvaders.game;

import java.awt.image.BufferedImage;

import com.alevat.spaceinvaders.io.ImageResource;

public class Floor extends AbstractCombatSprite {

    private static final int X = CombatState.LEFT_X_BOUNDARY;
    private static final int Y = CombatState.BOTTOM_Y_BOUNDARY - 16;

    private BufferedImage image = ImageResource.FLOOR.copyBufferedImage();

    Floor(CombatState combatState) {
        super(combatState);
    }

    @Override
    public int getX() {
        return X;
    }

    @Override
    public int getY() {
        return Y;
    }

    @Override
    public BufferedImage getBufferedImage() {
        return image;
    }
}
