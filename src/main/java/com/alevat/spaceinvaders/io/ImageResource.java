package com.alevat.spaceinvaders.io;

import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;

public enum ImageResource {

    FLOOR("floor.png"),

    PLAYER_CANNON("player_cannon.png"),
    PLAYER_CANNON_EXPLODING_1("player_cannon_exploding_1.png"),
    PLAYER_CANNON_EXPLODING_2("player_cannon_exploding_2.png"),
    PLAYER_SHOT("player_shot.png"),
    PLAYER_SHOT_EXPLODING("player_shot_exploding.png"),

    SHIELD("shield.png"),

    ALIEN_1A("alien-1a.png"),
    ALIEN_1B("alien-1b.png"),
    ALIEN_2A("alien-2a.png"),
    ALIEN_2B("alien-2b.png"),
    ALIEN_3A("alien-3a.png"),
    ALIEN_3B("alien-3b.png");

    private final BufferedImage bufferedImage;

    ImageResource(String imageName) {
        this.bufferedImage = loadImage(imageName);
    }

    private BufferedImage loadImage(String imageName) {
        URL url = getClass().getResource("/sprites/" + imageName);
        try {
            return ImageIO.read(url);
        } catch (IOException e) {
            throw new IllegalStateException("Image not found at URL: " + url);
        }
    }

    public BufferedImage getBufferedImage() {
        return bufferedImage;
    }

    public BufferedImage copyBufferedImage() {
        ColorModel cm = getBufferedImage().getColorModel();
        boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
        WritableRaster raster = getBufferedImage().copyData(null);
        return new BufferedImage(cm, raster, isAlphaPremultiplied, null)
                .getSubimage(0, 0, getBufferedImage().getWidth(), getBufferedImage().getHeight());
    }

    public int getWidth() {
        return getBufferedImage().getWidth();
    }

    public int getHeight() {
        return getBufferedImage().getHeight();
    }
}
