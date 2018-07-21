package com.alevat.spaceinvaders.game;

import java.util.List;

import com.alevat.spaceinvaders.io.ImageResource;

class AnimatedText {


    private static final int FRAMES_PER_CHARACTER = 8;

    private final List<ImageResource> characterResources;
    private final AbstractGameState gameState;

    private int leftX;
    private int topY;

    private int characterIndex = 0;

    AnimatedText(String message, AbstractGameState gameState) {
        this.characterResources = ImageResource.getForString(message);
        this.gameState = gameState;
    }

    int getWidth() {
        return characterResources.stream().mapToInt(r -> r.getWidth()).sum();
    }

    void display(int leftX, int topY) {
        this.leftX = leftX;
        this.topY = topY;
    }

    void update() {
        if (gameState.getFrameCount() % FRAMES_PER_CHARACTER == 0 && characterIndex < characterResources.size()) {
            ImageResource characterResource = characterResources.get(characterIndex++);
            GenericSprite characterSprite = new GenericSprite(leftX, topY, characterResource.getBufferedImage());
            gameState.getScreen().addSprite(characterSprite);
            leftX += characterSprite.getWidth();
        }
    }

}
