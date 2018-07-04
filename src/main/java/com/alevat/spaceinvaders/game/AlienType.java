package com.alevat.spaceinvaders.game;

import com.alevat.spaceinvaders.io.ImageResource;

enum AlienType {

    ONE(10, new ImageResource[] {ImageResource.ALIEN_1A, ImageResource.ALIEN_1B}),
    TWO(10, new ImageResource[] {ImageResource.ALIEN_2A, ImageResource.ALIEN_2B}),
    THREE(10, new ImageResource[] {ImageResource.ALIEN_3A, ImageResource.ALIEN_3B});

    private final int points;
    private final ImageResource[] images;

    AlienType(int points, ImageResource[] images) {
        this.points = points;
        this.images = images;
    }

    int getPoints() {
        return points;
    }

    ImageResource[] getImages() {
        return images;
    }
}
