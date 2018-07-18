package com.alevat.spaceinvaders.game;

import java.awt.image.BufferedImage;

class GenericSprite extends AbstractSprite {

    private final int x;
    private final int y;
    private final BufferedImage image;

    GenericSprite(int x, int y, BufferedImage image) {
        this.x = x;
        this.y = y;
        this.image = image;
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getY() {
        return y;
    }

    @Override
    public BufferedImage getBufferedImage() {
        return image;
    }
}
