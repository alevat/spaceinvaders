package com.alevat.spaceinvaders.io;

import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
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
    ALIEN_3B("alien-3b.png"),

    ALPHA_A("alpha/a.png"), ALPHA_B("alpha/b.png"), ALPHA_C("alpha/c.png"), ALPHA_D("alpha/d.png"),
    ALPHA_E("alpha/e.png"), ALPHA_F("alpha/f.png"), ALPHA_G("alpha/g.png"), ALPHA_H("alpha/h.png"),
    ALPHA_I("alpha/i.png"), ALPHA_J("alpha/j.png"), ALPHA_K("alpha/k.png"), ALPHA_L("alpha/l.png"),
    ALPHA_M("alpha/m.png"), ALPHA_N("alpha/n.png"), ALPHA_O("alpha/o.png"), ALPHA_P("alpha/p.png"),
    ALPHA_Q("alpha/q.png"), ALPHA_R("alpha/r.png"), ALPHA_S("alpha/s.png"), ALPHA_T("alpha/t.png"),
    ALPHA_U("alpha/u.png"), ALPHA_V("alpha/v.png"), ALPHA_W("alpha/w.png"), ALPHA_X("alpha/x.png"),
    ALPHA_Y("alpha/y.png"), ALPHA_Z("alpha/z.png");

    private final BufferedImage bufferedImage;

    ImageResource(String imageName) {
        this.bufferedImage = loadImage(imageName);
    }

    public static List<ImageResource> getForString(String string) {
        List<ImageResource> resources = new ArrayList<>(string.length());
        for (Character character: string.toUpperCase().toCharArray()) {
            ImageResource characterResource = ImageResource.valueOf("ALPHA_" + character);
            resources.add(characterResource);
        }
        return resources;
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
