package com.alevat.spaceinvaders.game;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.alevat.spaceinvaders.io.InputListener;

class CombatState extends AbstractGameState {

    static final int LEFT_X_BOUNDARY = 4;
    static final int RIGHT_X_BOUNDARY = Screen.WIDTH - 4;

    static final int TOP_Y_BOUNDARY = 4;
    static final int BOTTOM_Y_BOUNDARY = Screen.HEIGHT - 4;

    private static final int GAME_OVER_MESSAGE_TOP_Y = 48;

    private static final int SHIELD_COUNT = 4;
    private static final int FIRST_SHIELD_OFFSET = 32;
    private static final int SHIELD_SPACING = 24;
    private static final int MAX_PLAYER_SHOTS = 1;

    private static final int WAVE_ONE_START_Y = CombatState.TOP_Y_BOUNDARY + 64;
    private static final int WAVE_Y_OFFSET = Alien.HEIGHT + AlienWave.ALIEN_ROW_OFFSET_PIXELS; // correct?
    private static final int WAVE_START_Y_MAXIMUM = WAVE_ONE_START_Y + 4 * WAVE_Y_OFFSET; // correct?

    private PlayerCannon playerCannon = new PlayerCannon(this);
    private List<PlayerShot> playerShots = new ArrayList<>();
    private List<Shield> shields = new ArrayList<>();
    private AlienWave alienWave = new AlienWave(this, WAVE_ONE_START_Y);
    private Floor floor = new Floor(this);
    private GamePlayState playState = GamePlayState.COMBAT;

    private CombatInputListener inputListener = new CombatInputListener(this);
    private AnimatedText gameOverText;

    CombatState(Game game) {
        super(game);
        initialize();
    }

    private void initialize() {
        initializeShields();
        alienWave.initialize();
    }

    private void initializeShields() {
        int shieldX = FIRST_SHIELD_OFFSET;
        for (int i = 0; i < SHIELD_COUNT; i++) {
            Shield shield = new Shield(this, shieldX);
            shields.add(shield);
            shieldX += shield.WIDTH + SHIELD_SPACING;
        }
    }

    @Override
    public InputListener getInputListener() {
        return inputListener;
    }

    @Override
    public void update() {
        super.update();
        if (playState == GamePlayState.COMBAT) {
            playerCannon.update();
            updatePlayerShots();
            alienWave.update();
        } else if (playState == GamePlayState.WAVE_CLEARED) {
            nextWave();
        } else if (playState == GamePlayState.ALIEN_CONQUEST) {
            playerCannon.update();
        } else if (playState == GamePlayState.GAME_OVER) {
            gameOverText.update();
        }
    }

    private void nextWave() {
        shields.stream().forEach(shield -> getScreen().removeSprite(shield));
        int alienWaveStartY = alienWave.getWaveStartY() + WAVE_Y_OFFSET;
        if (alienWaveStartY >= WAVE_START_Y_MAXIMUM) {
            alienWaveStartY = WAVE_START_Y_MAXIMUM;
        }
        alienWave = new AlienWave(this, alienWaveStartY);
        playState = GamePlayState.COMBAT;
        initialize();
    }

    private void updatePlayerShots() {
        List<PlayerShot> currentShots = new ArrayList<>(playerShots);
        for (PlayerShot shot : currentShots) {
            shot.update();
        }
    }

    PlayerCannon getPlayerCannon() {
        return playerCannon;
    }

    Collision getCollision(CombatSprite source) {
        for (CombatSprite target : getTargetSprites()) {
            if (target == source) {
                continue;
            }
            if (target.detectCollision(source)) {
                return new Collision(source, target);
            }
        }
        return null;
    }

    private Set<CombatSprite> getTargetSprites() {
        Set<CombatSprite> targets = new HashSet<>();
        targets.add(playerCannon);
        targets.addAll(playerShots);
        targets.addAll(shields);
        targets.addAll(alienWave.getAliens());
        return targets;
    }

    boolean canPlayerCannonFire() {
        return playState == GamePlayState.COMBAT && playerShots.size() < MAX_PLAYER_SHOTS;
    }

    void addPlayerShot(PlayerShot shot) {
        playerShots.add(shot);
    }

    void removePlayerShot(PlayerShot shot) {
        playerShots.remove(shot);
        getScreen().removeSprite(shot);
    }

    void handleAlienConquest() {
        playerCannon.startExplosion();
        playState = GamePlayState.ALIEN_CONQUEST;
    }

    GamePlayState getPlayState() {
        return playState;
    }

    void handleGameOver() {
        playState = GamePlayState.GAME_OVER;
        gameOverText = new AnimatedText("GAME OVER", this);
        int textWidth = gameOverText.getWidth();
        int leftX = (Screen.WIDTH - textWidth) / 2;
        gameOverText.display(leftX, GAME_OVER_MESSAGE_TOP_Y);
    }

    void handleWaveCleared() {
        playState = GamePlayState.WAVE_CLEARED;
    }

}

