package com.alevat.spaceinvaders;

import java.awt.image.BufferedImage;

import com.alevat.spaceinvaders.io.ImageResource;

public class BitmapTest {

    public static void main(String[] args) {
        try {
            printImageInfo(ImageResource.SHIELD);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void printImageInfo(ImageResource resource) {
        BufferedImage image = resource.getBufferedImage();
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                int rgb = image.getRGB(x, y);
                byte alpha = (byte) (rgb >> 24);
                System.out.print(alpha + "\t");
            }
            System.out.println();
        }
    }

}
