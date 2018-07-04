package com.alevat.spaceinvaders.game;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.alevat.spaceinvaders.io.InputListener;

class CombatState extends AbstractGameState {

    static final int LEFT_X_BOUNDARY = 20;
    static final int RIGHT_X_BOUNDARY = Screen.WIDTH - 20;
    static final int TOP_Y_BOUNDARY = 34;

    private static final int SHIELD_COUNT = 4;
    private static final int FIRST_SHIELD_OFFSET = 32;
    private static final int SHIELD_SPACING = 24;
    private static final int MAX_PLAYER_SHOTS = 3;

    private PlayerCannon playerCannon = new PlayerCannon(this);
    private List<PlayerShot> playerShots = new ArrayList<>();
    private List<Shield> shields = new ArrayList<>();

    private CombatInputListener inputListener = new CombatInputListener(this);

    CombatState(Game game) {
        super(game);
        initialize();
    }

    private void initialize() {
        initializeShields();
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
        playerCannon.update();
        updatePlayerShots();
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
        return targets;
    }

    boolean canPlayerCannonFire() {
        return playerShots.size() < MAX_PLAYER_SHOTS;
    }

    void addPlayerShot(PlayerShot shot) {
        playerShots.add(shot);
    }

    void removePlayerShot(PlayerShot shot) {
        playerShots.remove(shot);
        getScreen().removeSprite(shot);
    }
}

