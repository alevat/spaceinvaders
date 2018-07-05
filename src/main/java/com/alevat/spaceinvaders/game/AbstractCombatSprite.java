package com.alevat.spaceinvaders.game;

import java.awt.*;

import com.alevat.spaceinvaders.io.AudioEngine;

abstract class AbstractCombatSprite extends AbstractSprite implements CombatSprite {

    private final CombatState combatState;

    AbstractCombatSprite(CombatState combatState){
        this.combatState = combatState;
        this.combatState.getScreen().addSprite(this);
    }

    @Override
    public boolean detectCollision(CombatSprite source) {
        return detectBoundsOverlap(source) && detectPixelCollision(source);
    }

    private boolean detectBoundsOverlap(CombatSprite source) {
        return getBounds().intersects(source.getBounds());
    }

    private boolean detectPixelCollision(CombatSprite source) {
        Rectangle intersection = getBounds().intersection(source.getBounds());
        for (int y = (int) intersection.getMinY(); y < intersection.getMaxY(); y++) {
            for (int x = (int) intersection.getMinX(); x < intersection.getMaxX(); x++) {
                if (detectPixelCollision(source, x, y)) {
                    return true;
                }
            }
        }
        return false;
    }

    boolean detectPixelCollision(CombatSprite source, int screenX, int screenY) {
        return this.isPixelOn(screenX, screenY) && source.isPixelOn(screenX, screenY);
    }

    @Override
    public boolean isPixelOn(int screenX, int screenY) {
        int imageX = screenX - this.getX();
        int imageY = screenY - this.getY();
        if (imageX >= 0 && imageX < getWidth() && imageY >=0 && imageY < getHeight()) {
            int rgb = this.getBufferedImage().getRGB(imageX, imageY);
            int alpha = rgb >> 24;
            return alpha != 0;
        } else {
            return false;
        }
    }

    CombatState getCombatState() {
        return combatState;
    }

    Screen getScreen() {
        return getCombatState().getScreen();
    }

    Console getConsole() {
        return getCombatState().getConsole();
    }

    AudioEngine getAudioEngine() {
        return getCombatState().getGame().getIOResources().getAudioEngine();
    }

    @Override
    public void handleShotCollision(PlayerShot playerShot) {
        handleIllegalCollision(playerShot); // default, override where allowed
    }

    private void handleIllegalCollision(CombatSprite sprite) {
        throw new UnsupportedOperationException("Illegal collision between " + getClass().getSimpleName()
                + " + and " + sprite.getClass().getSimpleName());
    }
}
