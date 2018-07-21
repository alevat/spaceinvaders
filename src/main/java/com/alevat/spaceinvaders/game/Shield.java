package com.alevat.spaceinvaders.game;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;

import com.alevat.spaceinvaders.io.ImageResource;

class Shield extends AbstractCombatSprite {

    static final int Y_POSITION = Screen.HEIGHT - 56;
    static final int WIDTH = ImageResource.SHIELD.getWidth();

    private final int x;
    private final BufferedImage image;

    Shield(CombatState state, int x) {
        super(state);
        this.x = x;
        this.image = ImageResource.SHIELD.copyBufferedImage();
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getY() {
        return Y_POSITION;
    }

    @Override
    public BufferedImage getBufferedImage() {
        return image;
    }

    @Override
    public void handleShotCollision(PlayerShot playerShot) {
        playerShot.handleShieldCollision(this);
    }

    @Override
    public void handleAlienCollision(Alien alien) {
        eraseDamage(alien);
    }

    void eraseDamage(CombatSprite damagingSprite) {
        Rectangle damagingSpriteBounds = damagingSprite.getBounds();
        WritableRaster raster = getBufferedImage().getRaster();
        for (int screenY = (int) damagingSpriteBounds.getMinY(); screenY < damagingSpriteBounds.getMaxY(); screenY++) {
            for (int screenX = (int) damagingSpriteBounds.getMinX(); screenX < damagingSpriteBounds.getMaxX(); screenX++) {
                if (detectPixelCollision(damagingSprite, screenX, screenY)) {
                    int shieldImageX = screenX - getX();
                    int shieldImageY = screenY - getY();
                    raster.setPixel(shieldImageX, shieldImageY, new int[] {0, 0, 0, 0});
                }
            }
        }
    }

}
