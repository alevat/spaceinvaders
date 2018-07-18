package com.alevat.spaceinvaders.game;

import java.util.List;

import com.alevat.spaceinvaders.io.ImageResource;

class AnimatedText {

    private final List<ImageResource> characterResources;
    private final AbstractGameState gameState;

    private int leftX;
    private int topY;

    AnimatedText(String message, AbstractGameState gameState) {
        this.characterResources = ImageResource.getForString(message);
        this.gameState = gameState;
    }

    int getWidth() {
        return characterResources.stream().mapToInt(r -> r.getWidth()).sum();
    }

    void display(int leftX, int topY) {
        this.leftX = leftX;
        int currentX = leftX;
        this.topY = topY;
        for (ImageResource characterResource : characterResources) {
            GenericSprite characterSprite = new GenericSprite(currentX, topY, characterResource.getBufferedImage());
            gameState.getScreen().addSprite(characterSprite);
            currentX += characterSprite.getWidth();
        }
    }
}
