package com.alevat.spaceinvaders.io;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * A renderable independent game element.
 */
public interface Sprite {

    /**
     * @return the x position of the left-most pixel of the sprite.
     */
    int getX();

    /**
     * @return the y position of the top-most pixel of the sprite.
     */
    int getY();

    int getWidth();

    int getHeight();

    Rectangle getBounds();

    BufferedImage getBufferedImage();

}
