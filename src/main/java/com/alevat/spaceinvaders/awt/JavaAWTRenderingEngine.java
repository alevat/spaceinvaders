package com.alevat.spaceinvaders.awt;

import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.HashSet;
import java.util.Set;

import com.alevat.spaceinvaders.game.Console;
import com.alevat.spaceinvaders.game.Screen;
import com.alevat.spaceinvaders.io.RenderingEngine;
import com.alevat.spaceinvaders.io.Sprite;

class JavaAWTRenderingEngine implements RenderingEngine {

    private static final int DISPLAY_MODE_WIDTH = 400;
    private static final int DISPLAY_MODE_HEIGHT = 300;
    public static final int CENTERING_OFFSET_Y = (DISPLAY_MODE_HEIGHT - Screen.HEIGHT) / 2;
    public static final int CENTERING_OFFSET_X = (DISPLAY_MODE_WIDTH - Screen.WIDTH) / 2;

    private final Console console;
    private GraphicsEnvironment graphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
    private GraphicsDevice graphicsDevice = graphicsEnvironment.getScreenDevices()[0];
    private Frame window;

    private final Set<Sprite> sprites = new HashSet<>();
    private BufferStrategy bufferStrategy;
    private Rectangle windowBounds;

    JavaAWTRenderingEngine(Console console) {
        this.console = console;
    }

    @Override
    public void initializeScreen() {
        window = new Frame(graphicsDevice.getDefaultConfiguration());
        window.setUndecorated(true);
        window.setIgnoreRepaint(true);
        window.requestFocus();
        if (graphicsDevice.isFullScreenSupported()) {
            console.info("Setting to Full screen exclusive");
            graphicsDevice.setFullScreenWindow(window);
        } else {
            console.info("Full screen exclusive unavailable");
        }
        if (graphicsDevice.isDisplayChangeSupported()) {
            selectDisplayMode();
        }
        console.info("Current mode " + getDisplayModeDescription(graphicsDevice.getDisplayMode()));
        windowBounds = window.getBounds();
        window.createBufferStrategy(3);
        bufferStrategy = window.getBufferStrategy();
    }

    private void selectDisplayMode() {
        DisplayMode[] displayModes = graphicsDevice.getDisplayModes();
        for (DisplayMode mode : displayModes) {
            if (isRequiredDisplayMode(mode)) {
                console.info("Changed to mode " + getDisplayModeDescription(mode));
                graphicsDevice.setDisplayMode(mode);
                return;
            }
        }
        throw new IllegalStateException("Couldn't find required display mode");
    }

    private boolean isRequiredDisplayMode(DisplayMode mode) {
        return mode.getHeight() == DISPLAY_MODE_HEIGHT
                && mode.getWidth() == DISPLAY_MODE_WIDTH;
    }

    private String getDisplayModeDescription(DisplayMode displayMode) {
        return displayMode.getWidth() + " x "
                + displayMode.getHeight() + ", "
                + displayMode.getBitDepth() + " bits, "
                + displayMode.getRefreshRate() + " Hz";
    }

    @Override
    public void addSprite(Sprite sprite) {
        sprites.add(sprite);
    }

    @Override
    public void removeSprite(Sprite sprite) {
        sprites.remove(sprite);
    }

    @Override
    public void renderScreen() {
        do {
            do {
                Graphics graphics = bufferStrategy.getDrawGraphics();
                graphics.setColor(Color.black);
                graphics.fillRect(0, 0, (int) windowBounds.getWidth(), (int) windowBounds.getHeight());
                renderScreenImage(graphics);
                graphics.dispose();
            } while (bufferStrategy.contentsRestored());
            bufferStrategy.show();
        } while (bufferStrategy.contentsLost());
    }

    private void renderScreenImage(Graphics graphics) {
        renderSprites(graphics);
    }

    private void renderSprites(Graphics graphics) {
        for (Sprite sprite : getCurrentSprites()) {
            renderSprite(sprite, graphics);
        }
    }

    // To protect against concurrent modification
    private Set<Sprite> getCurrentSprites() {
        return new HashSet<>(sprites);
    }

    private void renderSprite(Sprite sprite, Graphics graphics) {
        BufferedImage bufferedImage = sprite.getBufferedImage();
        int x = getActualX(sprite);
        int y = getActualY(sprite);
        graphics.drawImage(bufferedImage, x, y, null);
    }

    private int getActualX(Sprite sprite) {
        return sprite.getX() + CENTERING_OFFSET_X;
    }

    private int getActualY(Sprite sprite) {
        return sprite.getY() + CENTERING_OFFSET_Y;
    }
    void registerKeyListener(JavaAWTKeyListener keyListener) {
        window.addKeyListener(keyListener);
    }

    void close() {
        window.setVisible(false);
        window.dispose();
    }

}
