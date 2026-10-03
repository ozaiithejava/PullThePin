package com.game.pullthepin.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.game.pullthepin.PullThePinGame;
import com.game.pullthepin.daily.DailyChallengeManager;
import com.game.pullthepin.daily.DailyChallengeManager.DailyChallenge;
import com.game.pullthepin.entities.*;
import com.game.pullthepin.haptics.HapticsService.HapticType;
import com.game.pullthepin.levels.LevelLoader;
import com.game.pullthepin.theme.WorldTheme;

/**
 * Üst Düzey Görsel ve Ses Kalitesi ile Geliştirilmiş Oyun Ekranı:
 * - Günlük Meydan Okuma Desteği (Daily Challenge System & Custom Physics Mutators)
 * - Ağır Çekim Sinematik Zafer Efekti (Cinematic Slow-Mo Victory Finish & Letterbox Bars)
 * - Evrensel Hassas Haptik Titreşim Motoru (Multi-Device Android & iOS Haptic Feedback)
 * - Gerçek Zamanlı Yıldız Kilidi Açma ve HUD İlerleme Çubuğu Rozetleri (Live 1⭐, 2⭐, 3⭐ Milestones)
 * - 3D Elastik Bouncy Zafer Yıldızı Töreni & Kusursuzluk Tacı (Juiced Star Ceremony)
 * - 6 Farklı Dünya Temasına Göre Değişen Dinamik Arka Plan ve Prosedürel Müzik
 * - Kombo Puan Sistemi & Yüzen Pop-up Metinleri ("+1", "MÜKEMMEL!", "KOMBO x3")
 * - Box2D Sub-stepping, CCD ve Akışkan Sıvı Tilt Simülasyonu.
 */
public class GameScreen extends InputAdapter implements Screen {

    public static final float WORLD_WIDTH = 9.0f;
    public static final float WORLD_HEIGHT = 16.0f;
    public static final float UI_WIDTH = 720f;
    public static final float UI_HEIGHT = 1280f;

    private PullThePinGame game;
    private int currentLevel;
    private DailyChallenge dailyChallenge = null;

    private OrthographicCamera worldCamera;
    private FitViewport worldViewport;

    private OrthographicCamera uiCamera;
    private FitViewport uiViewport;

    private World world;
    private LevelLoader.LevelData levelData;
    private ParticleSystem particleSystem;
    private WorldTheme worldTheme;

    private Pin selectedPin = null;
    private Vector3 touchWorldPos = new Vector3();
    private Vector3 touchUIPos = new Vector3();
    private GlyphLayout glyphLayout = new GlyphLayout();

    // Ekran Sarsıntısı (Screen Shake)
    private float shakeTime = 0f;
    private float shakeIntensity = 0f;
    private float lastClinkTime = 0f;
    private float victoryRayRotation = 0f;

    // Sinematik Ağır Çekim (Cinematic Slow-Mo Finish)
    private float timeScale = 1.0f;
    private boolean isSlowMoActive = false;
    private float slowMoTimer = 0f;
    private static final float SLOW_MO_DURATION = 1.35f;
    private float cinematicBarHeight = 0f;

    // Seviye Başlangıç Banner Animasyonu
    private float introBannerTimer = 0f;
    private static final float INTRO_BANNER_DURATION = 2.4f;

    // Kombo Takip Sistemi
    private int comboCount = 0;
    private float lastBallCollectTime = 0f;

    // Canlı Oyun İçi Yıldız Eşik Takibi (Live In-Game Milestones)
    private boolean milestone1Triggered = false;
    private boolean milestone2Triggered = false;
    private boolean milestone3Triggered = false;

    // Oyun Durumları
    private enum GameState { PLAYING, WON, LOST }
    private enum LostReason { NONE, LASER, BOMB, SPIKE, NOT_ENOUGH, STUCK, ACID }
    private GameState state = GameState.PLAYING;
    private LostReason lostReason = LostReason.NONE;
    private float failFlashTimer = 0f;   // Kırmızı flaş efekti için
    private float stateTimer = 0f;
    private float lostCountdown = 0f;
    private float idleCountdown = 0f;
    private boolean soundPlayed = false;
    private int earnedStars = 0;

    // Altın Kazanım Sistemi Alanları
    private int earnedCoinsThisLevel = 0;
    private int baseCoinsEarned = 0;
    private int surplusBallCoins = 0;
    private int speedBonusCoins = 0;
    private int jackpotBonusCoins = 0;
    private boolean isFirstTimeClear = false;
    private int bombsDestroyedCount = 0;
    private float animatedCoinCount = 0f;
    private String unlockedAchievementName = null;
    private float achievementToastTimer = 0f;

    // Zafer Yıldız Töreni Animasyon Zamanlayıcıları
    private float[] starAnimTimers = new float[]{0f, 0f, 0f};
    private boolean[] starHasPopped = new boolean[]{false, false, false};

    private Array<Body> bodiesToDestroy = new Array<>();
    private Array<Portal.TeleportRequest> pendingTeleports = new Array<>();
    private Array<CloneSpawnRequest> pendingClones = new Array<>();

    private static class CloneSpawnRequest {
        float x, y;
        Ball.BallType type;
        Color color;
        float velX, velY;
        MultiplierGate gate;

        CloneSpawnRequest(float x, float y, Ball.BallType type, Color color, float velX, float velY, MultiplierGate gate) {
            this.x = x;
            this.y = y;
            this.type = type;
            this.color = color;
            this.velX = velX;
            this.velY = velY;
            this.gate = gate;
        }
    }

    // Sıvı / İvmeölçer Dinamik Yerçekimi (Liquid Tilt Physics)
    private float currentGravityX = 0f;
    private float currentGravityY = -22.0f;

    public GameScreen(PullThePinGame game, int level) {
        this(game, level, null);
    }

    public GameScreen(PullThePinGame game, int level, DailyChallenge dailyChallenge) {
        this.game = game;
        this.currentLevel = level;
        this.dailyChallenge = dailyChallenge;

        worldCamera = new OrthographicCamera();
        worldViewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT, worldCamera);
        worldCamera.position.set(WORLD_WIDTH / 2f, WORLD_HEIGHT / 2f, 0);

        uiCamera = new OrthographicCamera();
        uiViewport = new FitViewport(UI_WIDTH, UI_HEIGHT, uiCamera);
        uiCamera.position.set(UI_WIDTH / 2f, UI_HEIGHT / 2f, 0);

        particleSystem = new ParticleSystem();
        worldTheme = new WorldTheme(WorldTheme.getThemeForLevel(currentLevel));

        game.soundEngine.setTheme(worldTheme.getThemeType());

        initLevel();
    }

    public boolean isJackpotLevel() {
        return (dailyChallenge == null && currentLevel % 5 == 0);
    }

    private void initLevel() {
        if (world != null) {
            world.dispose();
        }

        float initialGravY = (dailyChallenge != null) ? dailyChallenge.modifier.gravityY : -22.0f;
        currentGravityX = 0f;
        currentGravityY = initialGravY;

        world = new World(new Vector2(0, initialGravY), true);
        setupCollisionHandling();

        levelData = LevelLoader.loadLevel(currentLevel, world);

        // Seçili bilye, kuyruk izi ve pim skinlerini uygula
        if (game.skinManager != null) {
            com.game.pullthepin.shop.SkinManager.BallSkin ballSkin = game.skinManager.getEquippedSkin();
            com.game.pullthepin.shop.SkinManager.TrailSkin trailSkin = game.skinManager.getEquippedTrailSkin();
            for (int i = 0; i < levelData.balls.size; i++) {
                levelData.balls.get(i).applySkin(ballSkin);
                levelData.balls.get(i).applyTrailSkin(trailSkin);
            }

            com.game.pullthepin.shop.SkinManager.PinSkin pinSkin = game.skinManager.getEquippedPinSkin();
            for (int i = 0; i < levelData.pins.size; i++) {
                levelData.pins.get(i).applySkin(pinSkin);
            }
        }

        if (dailyChallenge != null) {
            if (dailyChallenge.modifier.requireAllBalls) {
                int validBalls = 0;
                for (int i = 0; i < levelData.balls.size; i++) {
                    Ball b = levelData.balls.get(i);
                    if (b.getType() != Ball.BallType.BOMB) {
                        validBalls++;
                    }
                }
                levelData.jar.setRequiredBalls(validBalls);
            }
            if (dailyChallenge.modifier == DailyChallengeManager.ChallengeModifier.MEGA_BOUNCER) {
                for (int i = 0; i < levelData.balls.size; i++) {
                    levelData.balls.get(i).setRestitution(0.85f);
                }
            }
        }
        state = GameState.PLAYING;
        lostReason = LostReason.NONE;
        failFlashTimer = 0f;
        stateTimer = 0f;
        lostCountdown = 0f;
        idleCountdown = 0f;
        soundPlayed = false;
        earnedStars = 0;
        earnedCoinsThisLevel = 0;
        baseCoinsEarned = 0;
        surplusBallCoins = 0;
        speedBonusCoins = 0;
        isFirstTimeClear = false;
        bombsDestroyedCount = 0;
        animatedCoinCount = 0f;
        unlockedAchievementName = null;
        achievementToastTimer = 0f;
        shakeTime = 0f;
        victoryRayRotation = 0f;
        introBannerTimer = INTRO_BANNER_DURATION;
        comboCount = 0;
        lastBallCollectTime = 0f;

        timeScale = 1.0f;
        isSlowMoActive = false;
        slowMoTimer = 0f;
        cinematicBarHeight = 0f;

        milestone1Triggered = false;
        milestone2Triggered = false;
        milestone3Triggered = false;

        for (int i = 0; i < 3; i++) {
            starAnimTimers[i] = 0f;
            starHasPopped[i] = false;
        }

        particleSystem.clear();
        bodiesToDestroy.clear();
        pendingTeleports.clear();
        pendingClones.clear();

        worldTheme.setTheme(WorldTheme.getThemeForLevel(currentLevel));
        game.soundEngine.setTheme(worldTheme.getThemeType());

        Gdx.input.setInputProcessor(this);
    }

    private void setupCollisionHandling() {
        world.setContactListener(new ContactListener() {
            @Override
            public void beginContact(Contact contact) {
                Fixture fA = contact.getFixtureA();
                Fixture fB = contact.getFixtureB();
                Object uA = fA.getBody().getUserData();
                Object uB = fB.getBody().getUserData();

                // 1. Top + Top
                if (uA instanceof Ball && uB instanceof Ball) {
                    handleBallBallContact((Ball) uA, (Ball) uB);
                }

                // 2. Top + Hedef Kavanoz Sensörü
                if (uA instanceof Ball && "JAR_SENSOR".equals(uB)) {
                    handleBallJarContact((Ball) uA);
                } else if (uB instanceof Ball && "JAR_SENSOR".equals(uA)) {
                    handleBallJarContact((Ball) uB);
                }

                // 3. Top + Portal
                if (uA instanceof Ball && uB instanceof Portal) {
                    Portal.TeleportRequest req = ((Portal) uB).createTeleportRequest((Ball) uA);
                    if (req != null) {
                        pendingTeleports.add(req);
                        particleSystem.spawnColorSparks(((Ball) uA).getPosition().x, ((Ball) uA).getPosition().y, ((Ball) uA).getColor());
                        game.soundEngine.playPortalWarp();
                        game.triggerHaptic(HapticType.PORTAL_WARP);
                    }
                } else if (uB instanceof Ball && uA instanceof Portal) {
                    Portal.TeleportRequest req = ((Portal) uA).createTeleportRequest((Ball) uB);
                    if (req != null) {
                        pendingTeleports.add(req);
                        particleSystem.spawnColorSparks(((Ball) uB).getPosition().x, ((Ball) uB).getPosition().y, ((Ball) uB).getColor());
                        game.soundEngine.playPortalWarp();
                        game.triggerHaptic(HapticType.PORTAL_WARP);
                    }
                }

                // 4. Top + Bouncer
                if (uA instanceof Ball && uB instanceof Bouncer) {
                    ((Bouncer) uB).applyImpulse((Ball) uA);
                    particleSystem.spawnBouncerShockwave(((Ball) uA).getPosition().x, ((Ball) uA).getPosition().y);
                    game.soundEngine.playBouncerBoing();
                    game.triggerHaptic(HapticType.BOUNCER_HIT);
                } else if (uB instanceof Ball && uA instanceof Bouncer) {
                    ((Bouncer) uA).applyImpulse((Ball) uB);
                    particleSystem.spawnBouncerShockwave(((Ball) uB).getPosition().x, ((Ball) uB).getPosition().y);
                    game.soundEngine.playBouncerBoing();
                    game.triggerHaptic(HapticType.BOUNCER_HIT);
                }

                // 5. Top + Diken
                if (uA instanceof Ball && uB instanceof SpikeObstacle) {
                    popBallOnSpike((Ball) uA);
                } else if (uB instanceof Ball && uA instanceof SpikeObstacle) {
                    popBallOnSpike((Ball) uB);
                }

                // 6. Top + Çoğaltıcı Kapı (Multiplier Gate)
                if (uA instanceof Ball && uB instanceof MultiplierGate) {
                    handleBallMultiplierContact((Ball) uA, (MultiplierGate) uB);
                } else if (uB instanceof Ball && uA instanceof MultiplierGate) {
                    handleBallMultiplierContact((Ball) uB, (MultiplierGate) uA);
                }

                // 7. Top + Renk Filtresi (Color Gate)
                if (uA instanceof Ball && uB instanceof ColorGate) {
                    handleBallColorGateContact((Ball) uA, (ColorGate) uB);
                } else if (uB instanceof Ball && uA instanceof ColorGate) {
                    handleBallColorGateContact((Ball) uB, (ColorGate) uA);
                }

                // 8. Anahtar + Kilitli Pim (Key + Locked Pin)
                if (uA instanceof KeyEntity && uB instanceof Pin) {
                    handleKeyPinContact((KeyEntity) uA, (Pin) uB);
                } else if (uB instanceof KeyEntity && uA instanceof Pin) {
                    handleKeyPinContact((KeyEntity) uB, (Pin) uA);
                }

                // 9. Top + Dönen Çark (Ball + Rotating Obstacle)
                if (uA instanceof Ball && uB instanceof RotatingObstacle) {
                    handleBallRotatingObstacleContact((Ball) uA, (RotatingObstacle) uB);
                } else if (uB instanceof Ball && uA instanceof RotatingObstacle) {
                    handleBallRotatingObstacleContact((Ball) uB, (RotatingObstacle) uA);
                }

                // 10. Top + Lazer Bariyeri (Ball + Laser Obstacle)
                if (uA instanceof Ball && uB instanceof LaserObstacle) {
                    handleBallLaserContact((Ball) uA, (LaserObstacle) uB);
                } else if (uB instanceof Ball && uA instanceof LaserObstacle) {
                    handleBallLaserContact((Ball) uB, (LaserObstacle) uA);
                }

                // 11. Top + Asit Havuzu (Ball + Acid Pool)
                if (uA instanceof Ball && uB instanceof AcidPool) {
                    handleBallAcidContact((Ball) uA, (AcidPool) uB);
                } else if (uB instanceof Ball && uA instanceof AcidPool) {
                    handleBallAcidContact((Ball) uB, (AcidPool) uA);
                }

                // 12. Top + Rüzgar Türbini (Ball + Wind Turbine Stream)
                if (uA instanceof Ball && uB instanceof WindTurbine) {
                    ((WindTurbine) uB).addBall((Ball) uA);
                    game.soundEngine.playWindWhoosh();
                } else if (uB instanceof Ball && uA instanceof WindTurbine) {
                    ((WindTurbine) uA).addBall((Ball) uB);
                    game.soundEngine.playWindWhoosh();
                }
            }

            @Override
            public void endContact(Contact contact) {
                Fixture fA = contact.getFixtureA();
                Fixture fB = contact.getFixtureB();
                if (fA == null || fB == null || fA.getBody() == null || fB.getBody() == null) return;
                Object uA = fA.getBody().getUserData();
                Object uB = fB.getBody().getUserData();

                if (uA instanceof Ball && uB instanceof WindTurbine) {
                    ((WindTurbine) uB).removeBall((Ball) uA);
                } else if (uB instanceof Ball && uA instanceof WindTurbine) {
                    ((WindTurbine) uA).removeBall((Ball) uB);
                }
            }
            @Override public void preSolve(Contact contact, Manifold oldManifold) {}
            @Override public void postSolve(Contact contact, ContactImpulse impulse) {}
        });
    }

    private void handleBallAcidContact(Ball ball, AcidPool acidPool) {
        if (ball.isDead()) return;
        if (ball.getType() == Ball.BallType.WATER) {
            if (!acidPool.isNeutralized()) {
                acidPool.neutralize();
                ball.setDead(true);
                bodiesToDestroy.add(ball.getBody());
                particleSystem.spawnSteamBurst(ball.getPosition().x, ball.getPosition().y);
                game.soundEngine.playSteamHiss();
                game.triggerHaptic(HapticType.VICTORY_PULSE);
                String nText = com.game.pullthepin.i18n.I18N.isTurkish() ? "✨ NOTRALIZE EDILDI!" : "✨ NEUTRALIZED!";
                particleSystem.spawnFloatingText(nText, acidPool.getX(), acidPool.getY() + acidPool.getHeight() / 2f + 0.35f, new Color(0.35f, 0.95f, 1.0f, 1f));
            }
        } else if (!acidPool.isNeutralized()) {
            ball.setDead(true);
            bodiesToDestroy.add(ball.getBody());
            particleSystem.spawnAcidFizz(ball.getPosition().x, ball.getPosition().y);
            game.soundEngine.playAcidSizzle();
            game.triggerHaptic(HapticType.FAILURE_BUZZ);
            String meltText = com.game.pullthepin.i18n.I18N.isTurkish() ? "ERIDI!" : "MELTED!";
            particleSystem.spawnFloatingText(meltText, ball.getPosition().x, ball.getPosition().y + 0.35f, new Color(0.35f, 1.0f, 0.35f, 1f));
            checkInstantFail(LostReason.ACID);
        }
    }

    private void handleBallMultiplierContact(Ball ball, MultiplierGate gate) {
        if (!gate.canProcess(ball)) return;
        gate.markProcessed(ball);

        int count = gate.getCloneCount();
        Vector2 bpos = ball.getPosition();
        Vector2 bvel = ball.getBody() != null ? ball.getBody().getLinearVelocity() : Vector2.Zero;

        for (int i = 0; i < count; i++) {
            float spreadX = MathUtils.random(-0.25f, 0.25f);
            float spreadY = MathUtils.random(-0.15f, 0.15f);
            float velX = bvel.x + MathUtils.random(-1.5f, 1.5f);
            float velY = bvel.y + MathUtils.random(-0.5f, 0.5f);
            Color cloneCol = (ball.getColor() != null) ? ball.getColor().cpy() : null;
            pendingClones.add(new CloneSpawnRequest(bpos.x + spreadX, bpos.y + spreadY, ball.getType(), cloneCol, velX, velY, gate));
        }

        particleSystem.spawnMultiplierBurst(gate.getPosition().x, gate.getPosition().y, gate.getType().accentColor);
        game.soundEngine.playMultiplier();
        game.triggerHaptic(HapticType.MULTIPLIER_SURGE);
        particleSystem.spawnFloatingText(gate.getType().label, gate.getPosition().x, gate.getPosition().y + 0.4f, gate.getType().accentColor);
    }

    private void handleBallColorGateContact(Ball ball, ColorGate gate) {
        if (gate.canColorize(ball)) {
            if (ball.colorize(gate.getGateColor())) {
                if (ball.isCollected()) {
                    levelData.jar.incrementBalls();
                }
                particleSystem.spawnColorGateSplash(ball.getPosition().x, ball.getPosition().y, gate.getGateColor());
                game.soundEngine.playColorGate();
                game.triggerHaptic(HapticType.BALL_COLORIZE);
                particleSystem.spawnFloatingText("✨ " + gate.getColorName(), ball.getPosition().x, ball.getPosition().y + 0.35f, gate.getGateColor());
            }
        }
    }

    private void handleKeyPinContact(KeyEntity key, Pin pin) {
        if (key.isDead() || !pin.isLocked()) return;
        if (pin.getKeyId() == 0 || pin.getKeyId() == key.getKeyId()) {
            if (pin.unlock()) {
                key.setDead(true);
                bodiesToDestroy.add(key.getBody());
                particleSystem.spawnKeyUnlockSparks(pin.getHandlePosition().x, pin.getHandlePosition().y);
                game.soundEngine.playKeyUnlock();
                game.triggerHaptic(HapticType.KEY_UNLOCK);
                String msg = com.game.pullthepin.i18n.I18N.isTurkish() ? "🔓 KİLİT AÇILDI!" : "🔓 UNLOCKED!";
                particleSystem.spawnFloatingText(msg, pin.getHandlePosition().x, pin.getHandlePosition().y + 0.65f, new Color(1.0f, 0.88f, 0.25f, 1f));
            }
        }
    }

    private void handleBallRotatingObstacleContact(Ball ball, RotatingObstacle rotor) {
        if (ball.isDead()) return;
        float now = stateTimer;
        if (now - lastClinkTime > 0.04f) {
            game.soundEngine.playGearTick();
            lastClinkTime = now;
        }
        particleSystem.spawnGearSparks(ball.getPosition().x, ball.getPosition().y);
        game.triggerHaptic(HapticType.LIGHT_TICK);
    }

    private void handleBallLaserContact(Ball ball, LaserObstacle laser) {
        if (ball.isDead() || !laser.isActive()) return;
        ball.setDead(true);
        bodiesToDestroy.add(ball.getBody());
        particleSystem.spawnLaserVaporize(ball.getPosition().x, ball.getPosition().y, laser.getLaserColor());
        game.soundEngine.playLaserZap();
        game.triggerHaptic(HapticType.LIGHT_TICK);
        String zapText = com.game.pullthepin.i18n.I18N.isTurkish() ? "BUHARASLASTI!" : "ZAPPED!";
        particleSystem.spawnFloatingText(zapText, ball.getPosition().x, ball.getPosition().y + 0.35f, laser.getLaserColor());
        checkInstantFail(LostReason.LASER);
    }

    private void handleBallBallContact(Ball b1, Ball b2) {
        float now = stateTimer;
        if (now - lastClinkTime > 0.05f) {
            game.soundEngine.playMarbleClink();
            lastClinkTime = now;
        }

        if (b1.getType() == Ball.BallType.BOMB || b2.getType() == Ball.BallType.BOMB) {
            triggerBombExplosion(b1.getType() == Ball.BallType.BOMB ? b1 : b2);
            return;
        }

        if (b1.getType() == Ball.BallType.COLORED && b2.getType() == Ball.BallType.GRAY) {
            if (b2.colorize(b1.getColor())) {
                if (b2.isCollected()) {
                    levelData.jar.incrementBalls();
                }
                particleSystem.spawnColorSparks(b2.getPosition().x, b2.getPosition().y, b2.getColor());
                game.soundEngine.playZenChime();
                game.triggerHaptic(HapticType.BALL_COLORIZE);
            }
        } else if (b2.getType() == Ball.BallType.COLORED && b1.getType() == Ball.BallType.GRAY) {
            if (b1.colorize(b2.getColor())) {
                if (b1.isCollected()) {
                    levelData.jar.incrementBalls();
                }
                particleSystem.spawnColorSparks(b1.getPosition().x, b1.getPosition().y, b1.getColor());
                game.soundEngine.playZenChime();
                game.triggerHaptic(HapticType.BALL_COLORIZE);
            }
        }
    }

    private void handleBallJarContact(Ball ball) {
        if (ball.isCollected() || ball.isDead()) return;

        if (ball.getType() == Ball.BallType.COLORED) {
            ball.setCollected(true);
            levelData.jar.incrementBalls();
            particleSystem.spawnLiquidSplash(ball.getPosition().x, ball.getPosition().y, ball.getColor());
            game.triggerHaptic(HapticType.BALL_COLLECT);

            // Kavanozdaki gri topları renkli sıvı ile renklendir
            for (int i = 0; i < levelData.balls.size; i++) {
                Ball b = levelData.balls.get(i);
                if (b.isCollected() && b.getType() == Ball.BallType.GRAY && !b.isDead()) {
                    if (b.colorize(ball.getColor())) {
                        levelData.jar.incrementBalls();
                        particleSystem.spawnColorSparks(b.getPosition().x, b.getPosition().y, b.getColor());
                        game.triggerHaptic(HapticType.BALL_COLORIZE);
                    }
                }
            }

            int collected = levelData.jar.getCollectedBalls();
            int req = levelData.jar.getRequiredBalls();

            // Oyun İçi Canlı Yıldız Eşik Kontrolleri (Live Star Milestones)
            if (req > 0) {
                float progress = (float) collected / (float) req;
                if (progress >= 0.33f && !milestone1Triggered) {
                    milestone1Triggered = true;
                    particleSystem.spawnStarBurst(4.5f, 15.0f);
                    particleSystem.spawnFloatingText(com.game.pullthepin.i18n.I18N.get("STAR_MILESTONE_1"), 4.5f, 14.5f, new Color(1.0f, 0.85f, 0.25f, 1f));
                }
                if (progress >= 0.66f && !milestone2Triggered) {
                    milestone2Triggered = true;
                    particleSystem.spawnStarBurst(4.5f, 15.0f);
                    particleSystem.spawnFloatingText(com.game.pullthepin.i18n.I18N.get("STAR_MILESTONE_2"), 4.5f, 14.5f, new Color(1.0f, 0.85f, 0.25f, 1f));
                }
                if (progress >= 1.00f && !milestone3Triggered) {
                    milestone3Triggered = true;
                    particleSystem.spawnStarBurst(4.5f, 15.0f);
                    particleSystem.spawnFloatingText(com.game.pullthepin.i18n.I18N.get("STAR_MILESTONE_3"), 4.5f, 14.5f, new Color(1.0f, 0.90f, 0.35f, 1f));
                }
            }

            // Kombo Artışı ve Melodik Çan Sesi (ASMR Ses + Görsel İlerleme)
            if (stateTimer - lastBallCollectTime < 0.45f) {
                comboCount++;
            } else {
                comboCount = 0;
            }
            lastBallCollectTime = stateTimer;

            game.soundEngine.playComboChime(comboCount);

            if (comboCount >= 14) {
                // Kademeli ASMR Efekti: ULTRA FEVER (Pembe/Altın Ateş + Konfeti + Titreşim)
                String comboText = String.format(com.game.pullthepin.i18n.I18N.get("COMBO_FEVER"), comboCount + 1);
                particleSystem.spawnFloatingText(comboText, ball.getPosition().x, ball.getPosition().y, new Color(1.0f, 0.25f, 0.65f, 1f));
                particleSystem.spawnStarBurst(ball.getPosition().x, ball.getPosition().y);
                particleSystem.spawnConfetti(ball.getPosition().x, ball.getPosition().y);
                game.triggerHaptic(HapticType.VICTORY_PULSE);
                shakeTime = 0.12f;
                shakeIntensity = 0.08f;
            } else if (comboCount >= 7) {
                // Kademeli ASMR Efekti: SUPER KOMBO (Turuncu Parlama + Yıldız Patlaması)
                String comboText = String.format(com.game.pullthepin.i18n.I18N.get("COMBO_SUPER"), comboCount + 1);
                particleSystem.spawnFloatingText(comboText, ball.getPosition().x, ball.getPosition().y, new Color(1.0f, 0.70f, 0.15f, 1f));
                particleSystem.spawnStarBurst(ball.getPosition().x, ball.getPosition().y);
                game.triggerHaptic(HapticType.MULTIPLIER_SURGE);
            } else if (comboCount > 2) {
                // Normal Kombo Serisi
                String comboText = String.format(com.game.pullthepin.i18n.I18N.get("COMBO_STREAK"), comboCount + 1);
                particleSystem.spawnFloatingText(comboText, ball.getPosition().x, ball.getPosition().y, new Color(1.0f, 0.85f, 0.25f, 1f));
            } else {
                particleSystem.spawnFloatingText("+1", ball.getPosition().x, ball.getPosition().y, ball.getColor());
            }

        } else if (ball.getType() == Ball.BallType.GRAY) {
            ball.setCollected(true);
            // Eğer kavanozda zaten renkli bilye/sıvı varsa bu gri topu da renklendir ve say
            if (levelData.jar.getCollectedBalls() > 0) {
                ball.colorize(new Color(0.25f, 0.85f, 0.65f, 1f));
                levelData.jar.incrementBalls();
                particleSystem.spawnLiquidSplash(ball.getPosition().x, ball.getPosition().y, ball.getColor());
                game.soundEngine.playZenChime();
                game.triggerHaptic(HapticType.BALL_COLORIZE);
            } else {
                particleSystem.spawnLiquidSplash(ball.getPosition().x, ball.getPosition().y, Color.GRAY);
                game.triggerHaptic(HapticType.BALL_COLLECT);
            }
        } else if (ball.getType() == Ball.BallType.BOMB) {
            triggerBombExplosion(ball);
        }
    }

    private void popBallOnSpike(Ball ball) {
        if (ball.isDead()) return;
        ball.setDead(true);
        particleSystem.spawnColorSparks(ball.getPosition().x, ball.getPosition().y, Color.RED);
        game.soundEngine.playBubblePop();
        game.triggerHaptic(HapticType.LIGHT_TICK);
        bodiesToDestroy.add(ball.getBody());
        checkInstantFail(LostReason.SPIKE);
    }

    private void triggerBombExplosion(Ball bomb) {
        if (bomb.isDead()) return;
        bomb.setDead(true);
        bombsDestroyedCount++;
        Vector2 bombPos = bomb.getPosition().cpy();
        particleSystem.spawnExplosion(bombPos.x, bombPos.y);
        game.soundEngine.playExplosion();
        game.triggerHaptic(HapticType.EXPLOSION);

        shakeTime = 0.45f;
        shakeIntensity = 0.25f;

        for (int i = 0; i < levelData.balls.size; i++) {
            Ball b = levelData.balls.get(i);
            if (b.isDead() || b == bomb) continue;
            float dist = b.getPosition().dst(bombPos);
            if (dist < 2.6f) {
                b.setDead(true);
                bodiesToDestroy.add(b.getBody());
            }
        }
        bodiesToDestroy.add(bomb.getBody());
        checkInstantFail(LostReason.BOMB);
    }

    /**
     * Her top ölümünden sonra çağrılır.
     * Eğer kalan toplar + kavanoza girmiş toplar hedefe yetmiyorsa → anında başarısız.
     */
    private void checkInstantFail(LostReason reason) {
        if (state != GameState.PLAYING) return;

        boolean hasColorSource = levelData.jar.getCollectedBalls() > 0 
                || (levelData.colorGates != null && levelData.colorGates.size > 0);
        int coloredAlive = 0;
        int uncollected = 0;

        for (int i = 0; i < levelData.balls.size; i++) {
            Ball b = levelData.balls.get(i);
            if (!b.isDead() && !b.isCollected()) {
                uncollected++;
                if (b.getType() == Ball.BallType.COLORED) {
                    coloredAlive++;
                }
            }
        }

        // 1. Hiç renk kaynağı yoksa ve sahada hiç renkli bilye kalmadıysa -> gri toplar asla renklenemez
        if (!hasColorSource && coloredAlive == 0 && uncollected > 0) {
            lostReason = (reason != LostReason.NONE) ? reason : LostReason.NOT_ENOUGH;
            if (lostCountdown < 9999f) {
                failFlashTimer = 1.0f;
                lostCountdown = 9999f;
                game.triggerHaptic(HapticType.FAILURE_BUZZ);
            }
            return;
        }

        // 2. Sahadaki tüm canlı bilyeler toplansa bile hedef sayıya ulaşmak imkansızsa
        int maxCanReach = levelData.jar.getCollectedBalls() + uncollected;
        if (maxCanReach < levelData.jar.getRequiredBalls()) {
            lostReason = (reason != LostReason.NONE) ? reason : LostReason.NOT_ENOUGH;
            if (lostCountdown < 9999f) {
                failFlashTimer = 1.0f;
                lostCountdown = 9999f;
                game.triggerHaptic(HapticType.FAILURE_BUZZ);
            }
        }
    }

    private int countCollectedAlive() {
        int c = 0;
        for (int i = 0; i < levelData.balls.size; i++) {
            Ball b = levelData.balls.get(i);
            if (!b.isDead() && b.isCollected()) c++;
        }
        return c;
    }

    @Override
    public void render(float delta) {
        update(delta);

        Gdx.gl.glClearColor(0.04f, 0.05f, 0.10f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // ==========================================
        // 1. FİZİK & 2.5D DÜNYA ÇİZİMİ
        // ==========================================
        worldViewport.apply();

        if (shakeTime > 0) {
            worldCamera.position.set(
                    WORLD_WIDTH / 2f + MathUtils.random(-shakeIntensity, shakeIntensity),
                    WORLD_HEIGHT / 2f + MathUtils.random(-shakeIntensity, shakeIntensity),
                    0
            );
        } else {
            worldCamera.position.set(WORLD_WIDTH / 2f, WORLD_HEIGHT / 2f, 0);
        }
        worldCamera.update();

        ShapeRenderer sr = game.shapeRenderer;
        sr.setProjectionMatrix(worldCamera.combined);

        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        sr.begin(ShapeRenderer.ShapeType.Filled);

        // 1. DİNAMİK DÜNYA ARKA PLANI
        worldTheme.renderBackground(sr, WORLD_WIDTH, WORLD_HEIGHT);

        // 2. Parçacıklar
        particleSystem.render(sr);

        // 3. Portallar
        for (int i = 0; i < levelData.portals.size; i++) levelData.portals.get(i).render(sr);

        // 3B. Çoğaltıcı Kapılar
        for (int i = 0; i < levelData.multiplierGates.size; i++) levelData.multiplierGates.get(i).render(sr);

        // 3C. Renk Filtreleri
        for (int i = 0; i < levelData.colorGates.size; i++) levelData.colorGates.get(i).render(sr);

        // 3D. Dönen Çarklar & Pervaneler (Rotating Obstacles)
        for (int i = 0; i < levelData.rotatingObstacles.size; i++) levelData.rotatingObstacles.get(i).render(sr);

        // 3E. Lazer Bariyerleri (Laser Obstacles)
        for (int i = 0; i < levelData.lasers.size; i++) levelData.lasers.get(i).render(sr);

        // 3F. Zehirli Asit Havuzları (Acid Pools)
        for (int i = 0; i < levelData.acidPools.size; i++) levelData.acidPools.get(i).render(sr);

        // 3G. Rüzgar Türbinleri (Wind Turbines)
        for (int i = 0; i < levelData.windTurbines.size; i++) levelData.windTurbines.get(i).render(sr);

        // 4. Trambolinler
        for (int i = 0; i < levelData.bouncers.size; i++) levelData.bouncers.get(i).render(sr);

        // 5. Dikenler
        for (int i = 0; i < levelData.spikes.size; i++) levelData.spikes.get(i).render(sr);

        // 5B. Altın Anahtarlar
        for (int i = 0; i < levelData.keys.size; i++) levelData.keys.get(i).render(sr);

        // 6. 2.5D Oluklu Duvarlar
        for (int i = 0; i < levelData.walls.size; i++) levelData.walls.get(i).render(sr);

        // 7. 2.5D Cam Kavanoz
        levelData.jar.render(sr);

        // 8. 2.5D Metalik Pimler
        for (int i = 0; i < levelData.pins.size; i++) levelData.pins.get(i).render(sr);

        // 9. 2.5D Cam Bilyeler
        for (int i = 0; i < levelData.balls.size; i++) levelData.balls.get(i).render(sr);

        sr.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);

        // ==========================================
        // 2. YÜKSEK ÇÖZÜNÜRLÜKLÜ UI / HUD (720x1280)
        // ==========================================
        uiViewport.apply();
        uiCamera.update();

        sr.setProjectionMatrix(uiCamera.combined);
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        sr.begin(ShapeRenderer.ShapeType.Filled);
        renderModernHUD(sr);
        sr.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);

        // Net Tipografi & Yüzen Pop-up Skor Yazıları
        game.batch.setProjectionMatrix(uiCamera.combined);
        game.batch.begin();
        particleSystem.renderFloatingTexts(game.batch, game.smallFont);
        renderCleanTypography();
        game.batch.end();
    }

    private void update(float delta) {
        worldTheme.update(delta);
        if (introBannerTimer > 0) introBannerTimer -= delta;

        // --- DİNAMİK SIVI & İVMEÖLÇER EĞİM MEKANİĞİ ---
        float baseGravityY = (dailyChallenge != null) ? dailyChallenge.modifier.gravityY : -22.0f;
        float targetGravityX = 0f;
        float targetGravityY = baseGravityY;

        // Rüzgar Akıntısı Mutasyonu (Dynamic Wind Mutator)
        if (dailyChallenge != null && dailyChallenge.modifier == DailyChallengeManager.ChallengeModifier.DYNAMIC_WIND) {
            targetGravityX += (float) Math.sin(stateTimer * 2.2f) * 12.0f;
        }

        if (Gdx.input.isPeripheralAvailable(com.badlogic.gdx.Input.Peripheral.Accelerometer)) {
            float accelX = Gdx.input.getAccelerometerX();
            float accelY = Gdx.input.getAccelerometerY();
            targetGravityX += -accelX * 3.4f;
            if (dailyChallenge == null) {
                targetGravityY = -Math.max(14.0f, accelY * 2.4f);
            } else {
                float normRatio = Math.abs(baseGravityY) / 22.0f;
                targetGravityY = baseGravityY - (accelY / 9.8f) * 4.0f * normRatio;
            }
        }

        if (Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.A) || Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.LEFT)) {
            targetGravityX -= 16.0f;
        }
        if (Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.D) || Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.RIGHT)) {
            targetGravityX += 16.0f;
        }

        currentGravityX += (targetGravityX - currentGravityX) * 12.0f * delta;
        currentGravityY += (targetGravityY - currentGravityY) * 12.0f * delta;

        world.setGravity(new Vector2(currentGravityX, currentGravityY));
        if (levelData != null && levelData.jar != null) {
            levelData.jar.setFluidTilt(currentGravityX);
        }

        for (int i = 0; i < levelData.portals.size; i++) {
            Portal portal = levelData.portals.get(i);
            for (int j = 0; j < levelData.balls.size; j++) {
                Ball ball = levelData.balls.get(j);
                if (!ball.isDead() && !ball.isCollected() && ball.canTeleport()) {
                    if (ball.getPosition().dst(portal.getInPos()) < portal.getRadius() * 1.15f) {
                        Portal.TeleportRequest req = portal.createTeleportRequest(ball);
                        if (req != null) {
                            pendingTeleports.add(req);
                            particleSystem.spawnColorSparks(ball.getPosition().x, ball.getPosition().y, ball.getColor());
                            game.soundEngine.playPortalWarp();
                        }
                    }
                }
            }
        }

        float speedMultiplier = (dailyChallenge != null && dailyChallenge.modifier == DailyChallengeManager.ChallengeModifier.TURBO_SPEED) ? 1.65f : 1.0f;
        float fixedTimeStep = Math.min(delta, 1 / 30f) * timeScale * speedMultiplier;
        world.step(fixedTimeStep, 10, 8);

        if (pendingClones.size > 0) {
            for (int i = 0; i < pendingClones.size; i++) {
                CloneSpawnRequest req = pendingClones.get(i);
                Ball clone = new Ball(world, req.x, req.y, req.type, req.color);
                if (game.skinManager != null) {
                    clone.applySkin(game.skinManager.getEquippedSkin());
                }
                if (dailyChallenge != null && dailyChallenge.modifier == DailyChallengeManager.ChallengeModifier.MEGA_BOUNCER) {
                    clone.setRestitution(0.85f);
                }
                if (clone.getBody() != null) {
                    clone.getBody().setLinearVelocity(req.velX, req.velY);
                }
                if (req.gate != null) {
                    req.gate.markProcessed(clone);
                }
                levelData.balls.add(clone);
            }
            pendingClones.clear();
        }

        if (pendingTeleports.size > 0) {
            for (int i = 0; i < pendingTeleports.size; i++) {
                pendingTeleports.get(i).execute();
            }
            pendingTeleports.clear();
        }

        if (shakeTime > 0) {
            shakeTime -= delta;
            if (shakeTime <= 0) shakeIntensity = 0f;
        }

        if (bodiesToDestroy.size > 0) {
            for (int i = 0; i < bodiesToDestroy.size; i++) {
                Body b = bodiesToDestroy.get(i);
                if (b != null) world.destroyBody(b);
            }
            bodiesToDestroy.clear();
        }

        for (int i = 0; i < levelData.balls.size; i++) {
            Ball ball = levelData.balls.get(i);
            ball.update(delta * timeScale);
            // Ekranın altına düşen (kavanozu kaçıran) topları kontrol et
            if (!ball.isDead() && !ball.isCollected() && ball.getPosition().y < -0.5f) {
                ball.setDead(true);
                bodiesToDestroy.add(ball.getBody());
                checkInstantFail(LostReason.NOT_ENOUGH);
            }
        }
        for (int i = 0; i < levelData.pins.size; i++) levelData.pins.get(i).update(delta * (isSlowMoActive ? 0.6f : 1.0f));
        for (int i = 0; i < levelData.portals.size; i++) levelData.portals.get(i).update(delta * timeScale);
        for (int i = 0; i < levelData.multiplierGates.size; i++) levelData.multiplierGates.get(i).update(delta * timeScale);
        for (int i = 0; i < levelData.colorGates.size; i++) levelData.colorGates.get(i).update(delta * timeScale);
        for (int i = 0; i < levelData.keys.size; i++) levelData.keys.get(i).update(delta * timeScale);
        for (int i = 0; i < levelData.rotatingObstacles.size; i++) levelData.rotatingObstacles.get(i).update(delta * timeScale);
        for (int i = 0; i < levelData.lasers.size; i++) levelData.lasers.get(i).update(delta * timeScale);
        for (int i = 0; i < levelData.acidPools.size; i++) levelData.acidPools.get(i).update(delta * timeScale);
        for (int i = 0; i < levelData.windTurbines.size; i++) levelData.windTurbines.get(i).update(delta * timeScale);
        particleSystem.update(delta * (isSlowMoActive ? 0.35f : 1.0f));

        // Lazer Sürekli Işın Kesişimi Kontrolü (Continuous Laser Beam Intersection)
        for (int i = 0; i < levelData.lasers.size; i++) {
            LaserObstacle laser = levelData.lasers.get(i);
            if (!laser.isActive()) continue;
            for (int j = 0; j < levelData.balls.size; j++) {
                Ball ball = levelData.balls.get(j);
                if (!ball.isDead() && !ball.isCollected()) {
                    if (laser.checkBallIntersection(ball)) {
                        handleBallLaserContact(ball, laser);
                    }
                }
            }
        }

        // Anahtar ve Kilitli Pim Yakınlık Tetikleyicisi (Proximity Key Trigger)
        for (int i = 0; i < levelData.keys.size; i++) {
            KeyEntity key = levelData.keys.get(i);
            if (key.isDead()) continue;
            for (int j = 0; j < levelData.pins.size; j++) {
                Pin pin = levelData.pins.get(j);
                if (pin.isLocked() && (pin.getKeyId() == 0 || pin.getKeyId() == key.getKeyId())) {
                    if (key.getPosition().dst(pin.getHandlePosition()) < 0.75f || pin.containsPin(key.getPosition().x, key.getPosition().y)) {
                        handleKeyPinContact(key, pin);
                    }
                }
            }
        }

        // Ağır Çekim (Slow-Motion) Zaman Yönetimi
        if (isSlowMoActive) {
            slowMoTimer -= delta;
            timeScale = MathUtils.clamp(0.20f + (1.0f - (slowMoTimer / SLOW_MO_DURATION)) * 0.15f, 0.20f, 1.0f);
            cinematicBarHeight += (95f - cinematicBarHeight) * 9.0f * delta;

            if (slowMoTimer <= 0f) {
                isSlowMoActive = false;
                timeScale = 1.0f;
                state = GameState.WON;
                stateTimer = 0f;

                if (!soundPlayed) {
                    particleSystem.spawnConfetti(WORLD_WIDTH / 2f, WORLD_HEIGHT - 3f);
                    if (isJackpotLevel()) {
                        particleSystem.spawnStarBurst(WORLD_WIDTH / 2f, WORLD_HEIGHT - 4f);
                    }
                    game.soundEngine.playWin();
                    game.triggerHaptic(HapticType.VICTORY_PULSE);
                    soundPlayed = true;
                }
            }
        } else if (state != GameState.WON) {
            cinematicBarHeight += (0f - cinematicBarHeight) * 8.0f * delta;
            timeScale = 1.0f;
        }

        // Kazanma / Kaybetme Değerlendirmesi
        stateTimer += delta;
        if (state == GameState.PLAYING) {
            if (levelData.jar.isTargetReached()) {
                if (!isSlowMoActive) {
                    isSlowMoActive = true;
                    slowMoTimer = SLOW_MO_DURATION;
                    timeScale = 0.20f;

                    int collected = levelData.jar.getCollectedBalls();
                    int required = levelData.jar.getRequiredBalls();
                    if (collected >= required + 2) earnedStars = 3;
                    else if (collected >= required) earnedStars = 2;
                    else earnedStars = 1;

                    if (dailyChallenge != null) {
                        earnedCoinsThisLevel = dailyChallenge.rewardCoins;
                        game.saveManager.completeDailyChallenge(dailyChallenge.id, DailyChallengeManager.getTodayKey(), dailyChallenge.rewardCoins);
                    } else {
                        int previousStars = game.saveManager.getStarsForLevel(currentLevel);
                        isFirstTimeClear = (previousStars == 0);
                        game.saveManager.setStarsForLevel(currentLevel, earnedStars);
                        if (currentLevel < LevelLoader.TOTAL_LEVELS) {
                            game.saveManager.unlockLevel(currentLevel + 1);
                        }

                        // ── KAPSAMLI ALTIN KAZANIM MOTORU ──────────────────────
                        // 1. Yıldız Bazlı Temel Altın
                        baseCoinsEarned = (earnedStars == 3) ? 40 : (earnedStars == 2 ? 25 : 15);

                        // 2. Fazla Top Bonusu (hedefin üstündeki her sağlam bilye için +2 altın)
                        int surplus = Math.max(0, collected - required);
                        surplusBallCoins = surplus * 2;

                        // 3. Hızlı Bitirme / Hız Bonusu
                        if (stateTimer < 5.0f) {
                            speedBonusCoins = 25;
                        } else if (stateTimer < 8.0f) {
                            speedBonusCoins = 15;
                        } else {
                            speedBonusCoins = 0;
                        }

                        // 4. İlk Kez Geçiş Bonusu
                        int firstClearBonus = isFirstTimeClear ? 50 : 0;

                        // 5. Jackpot Bonusu (Her 5 Seviyede bir Altın Yağmuru: +250 Altın)
                        jackpotBonusCoins = isJackpotLevel() ? 250 : 0;

                        // Toplam Altın
                        earnedCoinsThisLevel = baseCoinsEarned + surplusBallCoins + speedBonusCoins + firstClearBonus + jackpotBonusCoins;
                        game.saveManager.addCoins(earnedCoinsThisLevel);

                        // Başarım Sistemi Entegrasyonu
                        if (game.achievementManager != null) {
                            game.achievementManager.onLevelComplete(currentLevel, earnedStars, stateTimer, collected, bombsDestroyedCount);
                            game.achievementManager.onCoinsChanged(game.saveManager.getCoins());
                            String notif = game.achievementManager.getPendingNotification();
                            if (notif != null && !notif.isEmpty()) {
                                unlockedAchievementName = notif;
                                achievementToastTimer = 4.0f;
                            }
                        }
                    }

                    game.triggerHaptic(HapticType.BALL_COLLECT);
                    particleSystem.spawnStarBurst(WORLD_WIDTH / 2f, WORLD_HEIGHT - 5f);
                }
            } else {
                int uncollectedBalls = 0;
                boolean anyMoving = false;
                boolean hasUnpulledPins = false;

                for (int i = 0; i < levelData.pins.size; i++) {
                    if (!levelData.pins.get(i).isPulled()) {
                        hasUnpulledPins = true;
                        break;
                    }
                }

                for (int i = 0; i < levelData.balls.size; i++) {
                    Ball b = levelData.balls.get(i);
                    if (!b.isDead()) {
                        if (!b.isCollected()) {
                            uncollectedBalls++;
                        }
                        if (b.getBody() != null && b.getBody().getLinearVelocity().len2() > 0.05f) {
                            anyMoving = true;
                        }
                    }
                }

                // Oyuncu şu anda pim çekiyorsa veya bomba patlama sarsıntısı varsa ASLA kaybetme sayacı başlatma
                if (selectedPin != null || shakeTime > 0) {
                    if (lostCountdown < 9999f) { lostCountdown = 0f; }  // fast-track varsa dokunma
                    idleCountdown = 0f;
                } else {
                    // Sahadaki ve kavanozdaki toplam yaşayan bilyeler
                    int maxPossibleBalls = levelData.jar.getCollectedBalls() + uncollectedBalls;

                    // ── FAST-TRACK: checkInstantFail() tarafından tetiklendi ──────────────
                    if (lostCountdown >= 9999f) {
                        failFlashTimer -= delta;
                        if (failFlashTimer <= 0f) {
                            state = GameState.LOST;
                            stateTimer = 0f;
                            if (!soundPlayed) {
                                game.soundEngine.playFail();
                                game.triggerHaptic(HapticType.FAILURE_BUZZ);
                                soundPlayed = true;
                            }
                        }
                    }
                    // 1. Durum: Bomba/lazer yüzünden toplam bilye sayısı hedefin altına düştü
                    else if (maxPossibleBalls < levelData.jar.getRequiredBalls()) {
                        if (!anyMoving && stateTimer > 2.5f) {
                            lostCountdown += delta;
                            if (lostCountdown > 1.5f) {  // 2.5 → 1.5s (daha hızlı)
                                lostReason = LostReason.NOT_ENOUGH;
                                state = GameState.LOST;
                                stateTimer = 0f;
                                if (!soundPlayed) {
                                    game.soundEngine.playFail();
                                    game.triggerHaptic(HapticType.FAILURE_BUZZ);
                                    soundPlayed = true;
                                }
                            }
                        } else {
                            lostCountdown = 0f;
                        }
                    }
                    // 2. Durum: Tüm pimler çekildi, bilyeler durdu veya girdi ve hedef tutmadı
                    else if (!anyMoving && !hasUnpulledPins && uncollectedBalls == 0 && stateTimer > 2.5f) {
                        idleCountdown += delta;
                        if (idleCountdown > 2.2f) {
                            lostReason = LostReason.STUCK;
                            state = GameState.LOST;
                            stateTimer = 0f;
                            if (!soundPlayed) {
                                game.soundEngine.playFail();
                                game.triggerHaptic(HapticType.FAILURE_BUZZ);
                                soundPlayed = true;
                            }
                        }
                    } else {
                        lostCountdown = 0f;
                        idleCountdown = 0f;
                    }
                }
            }
        } else if (state == GameState.WON) {
            victoryRayRotation += delta * 25f;

            // 3 Kademeli Juiced Yıldız Giriş Animasyonu
            for (int s = 0; s < earnedStars; s++) {
                float delay = 0.35f + s * 0.40f;
                if (stateTimer >= delay) {
                    starAnimTimers[s] += delta;
                    if (!starHasPopped[s]) {
                        starHasPopped[s] = true;
                        shakeTime = 0.12f;
                        shakeIntensity = 0.10f;
                        float starWorldX = 2.0f + s * 2.5f;
                        particleSystem.spawnStarBurst(starWorldX, 8.5f);
                        game.soundEngine.playStarEarned(s + 1);
                        game.triggerHaptic(HapticType.PIN_RELEASE);
                    }
                }
            }

            // Altın sayacının akıcı artışı (Rolling counter animation)
            if (animatedCoinCount < earnedCoinsThisLevel) {
                animatedCoinCount += delta * (earnedCoinsThisLevel / 0.8f + 8f);
                if (animatedCoinCount >= earnedCoinsThisLevel) {
                    animatedCoinCount = earnedCoinsThisLevel;
                    particleSystem.spawnColorSparks(WORLD_WIDTH / 2f, 7.5f, new Color(1f, 0.84f, 0.22f, 1f));
                }
            }

            if (achievementToastTimer > 0) {
                achievementToastTimer -= delta;
            }
        }
    }

    private void renderModernHUD(ShapeRenderer sr) {
        // Sinematik Ağır Çekim Siyah Bantları
        if (cinematicBarHeight > 0.5f) {
            sr.setColor(0f, 0f, 0f, 0.96f);
            sr.rect(0, UI_HEIGHT - cinematicBarHeight, UI_WIDTH, cinematicBarHeight);
            sr.rect(0, 0, UI_WIDTH, cinematicBarHeight);
            sr.setColor(1.0f, 0.84f, 0.22f, 0.85f);
            sr.rect(0, UI_HEIGHT - cinematicBarHeight, UI_WIDTH, 4);
            sr.rect(0, cinematicBarHeight - 4, UI_WIDTH, 4);
        }

        Color accent = worldTheme.getAccentColor();
        float progress = Math.min(1.0f, (float) levelData.jar.getCollectedBalls() / (float) levelData.jar.getRequiredBalls());

        // ── İNCE PROGRESS BAR (Ekran Üstünde İnce Şerit) ────────────────────
        // Arka plan kanalı
        sr.setColor(0.04f, 0.06f, 0.10f, 0.88f);
        sr.rect(0, UI_HEIGHT - 6, UI_WIDTH, 6);
        // Dolgu
        sr.setColor(accent);
        sr.rect(0, UI_HEIGHT - 6, UI_WIDTH * progress, 6);
        // Parlama ucu
        if (progress > 0.02f && progress < 1f) {
            sr.setColor(1f, 1f, 1f, 0.7f);
            sr.rect(UI_WIDTH * progress - 3, UI_HEIGHT - 8, 6, 10);
        }

        // ── MİNİMAL ÜSTTE ORTALI BİLGİ HAPSÜLü ─────────────────────────────
        // Küçük yarı saydam bilgi kapsülü (sadece seviye adı + skor)
        float capsW = 340f, capsH = 46f;
        float capsX = (UI_WIDTH - capsW) / 2f;
        float capsY = UI_HEIGHT - 62f;
        // Gölge
        sr.setColor(0f, 0f, 0f, 0.30f);
        drawPill(sr, capsX + 2, capsY - 2, capsW, capsH);
        // Ana kapsül
        sr.setColor(0.06f, 0.09f, 0.16f, 0.88f);
        drawPill(sr, capsX, capsY, capsW, capsH);
        // Üst accent çizgisi
        if (isJackpotLevel()) {
            float pulse = 0.5f + 0.5f * (float) Math.sin(stateTimer * 4.0f);
            sr.setColor(1.0f, 0.84f, 0.22f, 0.70f + pulse * 0.30f);
            sr.rect(capsX + 10, capsY + capsH - 3, capsW - 20, 3);
        } else {
            sr.setColor(accent.r, accent.g, accent.b, 0.80f);
            sr.rect(capsX + 20, capsY + capsH - 2, capsW - 40, 2);
        }

        // ── ÜST SAĞ: CANLI ALTIN BAKİYESİ KAPSÜLÜ ───────────────────────────
        float coinCapsW = 120f, coinCapsH = 38f;
        float coinCapsX = UI_WIDTH - coinCapsW - 20f;
        float coinCapsY = UI_HEIGHT - 56f;
        sr.setColor(0f, 0f, 0f, 0.25f);
        drawPill(sr, coinCapsX + 2, coinCapsY - 2, coinCapsW, coinCapsH);
        sr.setColor(0.06f, 0.09f, 0.16f, 0.88f);
        drawPill(sr, coinCapsX, coinCapsY, coinCapsW, coinCapsH);
        sr.setColor(1.0f, 0.84f, 0.22f, 0.70f);
        sr.rect(coinCapsX + 10, coinCapsY + coinCapsH - 2, coinCapsW - 20, 2);
        // Altın sikke simgesi
        sr.setColor(1.0f, 0.84f, 0.22f, 1.0f);
        sr.circle(coinCapsX + 18f, coinCapsY + coinCapsH / 2f, 9f, 16);
        sr.setColor(1.0f, 0.95f, 0.60f, 1.0f);
        sr.circle(coinCapsX + 16f, coinCapsY + coinCapsH / 2f + 2f, 3.5f, 12);

        // ── YILDIZ MİLESTONE NOKTACIKLARI (Progress Bar Üstünde) ─────────────
        float[] milestonePercents = {0.33f, 0.66f, 1.00f};
        for (int m = 0; m < 3; m++) {
            float mx = UI_WIDTH * milestonePercents[m];
            boolean reached = (progress >= milestonePercents[m]);
            if (reached) {
                sr.setColor(1.0f, 0.88f, 0.22f, 0.9f);
                sr.circle(mx, UI_HEIGHT - 3, 5f, 10);
            } else {
                sr.setColor(0.25f, 0.30f, 0.42f, 0.8f);
                sr.circle(mx, UI_HEIGHT - 3, 4f, 10);
            }
        }

        // ── GERİ BUTONU — Sol Alt, Küçük Yuvarlak Pill ───────────────────────
        float btnR = 36f; // yarıçap
        float backX = 55f, backY = 88f;
        // Gölge
        sr.setColor(0f, 0f, 0f, 0.35f);
        sr.circle(backX + 2, backY - 3, btnR + 2, 20);
        // Arka plan
        sr.setColor(0.08f, 0.11f, 0.20f, 0.92f);
        sr.circle(backX, backY, btnR, 20);
        // Accent ring
        sr.setColor(accent.r, accent.g, accent.b, 0.55f);
        sr.circle(backX, backY, btnR, 20);
        sr.setColor(0.08f, 0.11f, 0.20f, 0.92f);
        sr.circle(backX, backY, btnR - 2.5f, 20);
        // Ok ikonu (chevron <)
        sr.setColor(1f, 1f, 1f, 0.90f);
        sr.rectLine(backX - 10, backY, backX + 5, backY + 14, 3f);
        sr.rectLine(backX - 10, backY, backX + 5, backY - 14, 3f);

        // ── RESET BUTONU — Sağ Alt, Küçük Yuvarlak Pill ─────────────────────
        float resetX = UI_WIDTH - 55f, resetY = 88f;
        // Gölge
        sr.setColor(0f, 0f, 0f, 0.35f);
        sr.circle(resetX + 2, resetY - 3, btnR + 2, 20);
        // Arka plan
        sr.setColor(0.08f, 0.11f, 0.20f, 0.92f);
        sr.circle(resetX, resetY, btnR, 20);
        // Accent ring
        sr.setColor(accent.r, accent.g, accent.b, 0.55f);
        sr.circle(resetX, resetY, btnR, 20);
        sr.setColor(0.08f, 0.11f, 0.20f, 0.92f);
        sr.circle(resetX, resetY, btnR - 2.5f, 20);
        // Döngü ikonu (basit ⟳ — üç çizgi)
        float globalT = stateTimer; // reset animasyon için kullanılabilir
        sr.setColor(1f, 1f, 1f, 0.90f);
        sr.arc(resetX, resetY, 14f, 30f, 280f, 18);
        sr.rectLine(resetX + 12, resetY + 5, resetX + 18, resetY + 12, 3f);
        sr.rectLine(resetX + 12, resetY + 5, resetX + 5, resetY + 11, 3f);

        // ── SEVIYE BAŞLANGIÇ BANNER ─────────────────────────────────────────
        if (introBannerTimer > 0) {
            float bannerAlpha = Math.min(1.0f, introBannerTimer * 1.5f);
            float bW = 500f, bH = 80f, bX = (UI_WIDTH - bW) / 2f, bY = 580f;
            sr.setColor(0f, 0f, 0f, 0.30f * bannerAlpha);
            drawPill(sr, bX + 2, bY - 2, bW, bH);
            sr.setColor(0.07f, 0.10f, 0.20f, 0.92f * bannerAlpha);
            drawPill(sr, bX, bY, bW, bH);
            sr.setColor(accent.r, accent.g, accent.b, 0.85f * bannerAlpha);
            sr.rect(bX + 18, bY + bH - 2.5f, bW - 36, 2.5f);
        }

        // --- ZAFER / MAĞLUBİYET POPUP DİYALOGLARI ---
        if (state == GameState.WON) {
            sr.setColor(0f, 0f, 0f, 0.78f);
            sr.rect(0, 0, UI_WIDTH, UI_HEIGHT);

            // Dönen Zafer Güneş Işınları
            for (int i = 0; i < 14; i++) {
                float angle = victoryRayRotation + i * (360f / 14f);
                sr.setColor(1.0f, 0.85f, 0.3f, 0.045f);
                float rad1 = (float) Math.toRadians(angle - 5f);
                float rad2 = (float) Math.toRadians(angle + 5f);
                sr.triangle(
                        UI_WIDTH / 2f, 680f,
                        UI_WIDTH / 2f + (float) Math.cos(rad1) * 650f, 680f + (float) Math.sin(rad1) * 650f,
                        UI_WIDTH / 2f + (float) Math.cos(rad2) * 650f, 680f + (float) Math.sin(rad2) * 650f
                );
            }

            // ── ZAFER KARTI (2.5D Lüks Cam Modal) ───────────────────────────
            float cx = 60f, cy = 350f, cw = 600f, ch = 575f;
            drawRoundedBox(sr, cx + 6f, cy - 8f, cw, ch, 28f, new Color(0f, 0f, 0f, 0.45f));

            float cardPulse = 0.5f + 0.5f * (float) Math.sin(stateTimer * 3f);
            drawRoundedBox(sr, cx - 3f, cy - 3f, cw + 6f, ch + 6f, 30f,
                    isJackpotLevel() ? new Color(1.0f, 0.85f, 0.20f, 0.35f + cardPulse * 0.25f)
                                     : new Color(0.18f, 0.82f, 1.0f, 0.22f + cardPulse * 0.16f));

            drawRoundedBox(sr, cx, cy, cw, ch, 28f, new Color(0.08f, 0.12f, 0.22f, 0.98f));

            sr.setColor(1.0f, 0.84f, 0.25f, 0.95f);
            sr.rect(cx + 25f, cy + ch - 4f, cw - 50f, 4f);

            // 3D ELASTİK ZAFER YILDIZLARI (Bouncy Juiced Star Drop)
            Color goldStar = new Color(1.0f, 0.82f, 0.22f, 1.0f);
            Color darkStar = new Color(0.18f, 0.22f, 0.32f, 1.0f);

            for (int s = 0; s < 3; s++) {
                float sx = 230 + s * 130;
                float sy = 680;
                boolean isEarned = (s < earnedStars);

                if (isEarned && starHasPopped[s]) {
                    // Elastik Yaylanma (Elastic Overshoot: Zoom 1.4 -> 0.9 -> 1.0)
                    float t = Math.min(1.0f, starAnimTimers[s] / 0.45f);
                    float scale = 1.0f + 0.5f * (float) Math.sin(t * Math.PI) * (1.0f - t);
                    float baseRadius = 45f * scale;

                    // Yıldız Arkası Işıma Aurası (Golden Halo Aura)
                    float pulse = 0.5f + 0.5f * (float) Math.sin(Gdx.graphics.getFrameId() * 0.1f + s);
                    sr.setColor(1.0f, 0.85f, 0.2f, 0.32f + pulse * 0.18f);
                    sr.circle(sx, sy, baseRadius * 1.5f, 24);

                    // Ana Altın Yıldız
                    drawStar(sr, sx, sy, baseRadius, goldStar);
                    // İç Parıltı & Elmas Çekirdek
                    drawStar(sr, sx, sy, baseRadius * 0.48f, Color.WHITE);
                } else {
                    // Henüz düşmemiş veya kazanılmamış karanlık yuva
                    drawStar(sr, sx, sy, 38f, darkStar);
                }
            }

            // 3 Yıldız Kusursuz Rozeti (Perfect 3-Star Badge)
            if (earnedStars == 3 && starHasPopped[2]) {
                drawRoundedBox(sr, 140f, 558f, 440f, 52f, 14f, new Color(1.0f, 0.84f, 0.22f, 0.22f));
                sr.setColor(1.0f, 0.84f, 0.22f, 0.85f);
                sr.rect(155f, 558f, 410f, 2f);
                sr.rect(155f, 608f, 410f, 2f);
            }

            // Sonraki Seviye Butonu (Lüks 2.5D Buton & Işık Parıltısı)
            float btnX = 110f, btnY = 385f, btnW = 500f, btnH = 92f;
            float btnPulse = 0.5f + 0.5f * (float) Math.sin(stateTimer * 4f);

            drawRoundedBox(sr, btnX - 4f, btnY - 4f, btnW + 8f, btnH + 8f, 22f,
                    new Color(0.18f, 0.88f, 0.62f, 0.18f + btnPulse * 0.16f));

            renderModernButton(sr, btnX, btnY, btnW, btnH, 20f,
                    new Color(0.18f, 0.88f, 0.62f, 1.0f),
                    new Color(0.09f, 0.56f, 0.38f, 1.0f),
                    new Color(0.65f, 1.00f, 0.88f, 0.70f),
                    false);

            renderButtonShimmer(sr, btnX, btnY, btnW, btnH, 20f);
        } else if (state == GameState.LOST) {
            sr.setColor(0f, 0f, 0f, 0.78f);
            sr.rect(0, 0, UI_WIDTH, UI_HEIGHT);

            // ── MAĞLUBİYET KARTI (2.5D Yakut Cam Modal) ───────────────
            float cx = 60f, cy = 405f, cw = 600f, ch = 475f;
            drawRoundedBox(sr, cx + 6f, cy - 8f, cw, ch, 28f, new Color(0f, 0f, 0f, 0.45f));

            float failPulse = 0.5f + 0.5f * (float) Math.sin(stateTimer * 3.5f);
            drawRoundedBox(sr, cx - 3f, cy - 3f, cw + 6f, ch + 6f, 30f,
                    new Color(0.98f, 0.32f, 0.38f, 0.22f + failPulse * 0.16f));

            drawRoundedBox(sr, cx, cy, cw, ch, 28f, new Color(0.08f, 0.12f, 0.22f, 0.98f));

            sr.setColor(0.98f, 0.38f, 0.45f, 0.95f);
            sr.rect(cx + 25f, cy + ch - 4f, cw - 50f, 4f);

            // Yeniden Dene Butonu (Lüks 2.5D Buton)
            float btnX = 110f, btnY = 445f, btnW = 500f, btnH = 92f;
            float btnPulse = 0.5f + 0.5f * (float) Math.sin(stateTimer * 4f);

            drawRoundedBox(sr, btnX - 4f, btnY - 4f, btnW + 8f, btnH + 8f, 22f,
                    new Color(0.98f, 0.38f, 0.45f, 0.18f + btnPulse * 0.16f));

            renderModernButton(sr, btnX, btnY, btnW, btnH, 20f,
                    new Color(0.98f, 0.38f, 0.45f, 1.0f),
                    new Color(0.72f, 0.22f, 0.28f, 1.0f),
                    new Color(1.00f, 0.75f, 0.80f, 0.70f),
                    false);

            renderButtonShimmer(sr, btnX, btnY, btnW, btnH, 20f);
        }

        // ── TEHLİKE / KIRMIZI FLAŞ EKRAN KAPLAMASI (Yetersiz top kaldığında) ──
        if (failFlashTimer > 0f && state == GameState.PLAYING) {
            float alpha = MathUtils.clamp(failFlashTimer * 0.45f, 0f, 0.42f);
            sr.setColor(0.95f, 0.15f, 0.20f, alpha);
            sr.rect(0, 0, UI_WIDTH, UI_HEIGHT);
        }
    }

    /** Yuvarlak uçlu pill (kapsül) çizer — sol/sağ uçlar yarım daire */
    private void drawPill(ShapeRenderer sr, float x, float y, float w, float h, Color color) {
        sr.setColor(color);
        drawPill(sr, x, y, w, h);
    }

    private void drawPill(ShapeRenderer sr, float x, float y, float w, float h) {
        float r = h / 2f;
        sr.rect(x + r, y, w - 2 * r, h);
        sr.circle(x + r, y + r, r, 18);
        sr.circle(x + w - r, y + r, r, 18);
    }

    private void drawRoundedBox(ShapeRenderer sr, float x, float y, float w, float h, float r, Color color) {
        if (r <= 0f || h <= 4f || w <= 4f) {
            sr.setColor(color);
            sr.rect(x, y, w, h);
            return;
        }
        r = Math.min(r, Math.min(w / 2f, h / 2f));
        sr.setColor(color);
        sr.rect(x + r, y, w - 2 * r, h);
        sr.rect(x, y + r, r, h - 2 * r);
        sr.rect(x + w - r, y + r, r, h - 2 * r);
        sr.arc(x + r, y + r, r, 180, 90, 14);
        sr.arc(x + w - r, y + r, r, 270, 90, 14);
        sr.arc(x + w - r, y + h - r, r, 0, 90, 14);
        sr.arc(x + r, y + h - r, r, 90, 90, 14);
    }

    private void renderModernButton(ShapeRenderer sr, float x, float y, float w, float h, float r,
                                    Color topColor, Color bottomColor, Color rimColor, boolean isPressed) {
        float shadowOffset = isPressed ? 2f : 6f;
        float faceY = isPressed ? y - 4f : y;

        // 1. Zemin Yumuşak Derinlik Gölgesi
        drawRoundedBox(sr, x + 3f, y - shadowOffset, w, h, r, new Color(0f, 0f, 0f, 0.40f));

        // 2. Alt Eğim 3D Gövdesi (Bevel)
        drawRoundedBox(sr, x, y - 5f, w, h, r, bottomColor);

        // 3. Buton Yüzeyi
        drawRoundedBox(sr, x, faceY, w, h - 2f, r, topColor);

        // 4. Üst İnce Işık Hattı (Rim Highlight)
        if (rimColor != null && !isPressed) {
            sr.setColor(rimColor);
            sr.rect(x + r * 0.8f, faceY + h - 5f, w - r * 1.6f, 3f);
        }
    }

    private void renderButtonShimmer(ShapeRenderer sr, float x, float y, float w, float h, float r) {
        float cycleTime = 2.4f;
        float phase = (stateTimer % cycleTime);
        if (phase < 0.75f) {
            float progress = phase / 0.75f;
            float shimmerW = 55f;
            float shimmerX = x - shimmerW + progress * (w + shimmerW * 2f);
            float clampedLeft = Math.max(x + r * 0.5f, shimmerX);
            float clampedRight = Math.min(x + w - r * 0.5f, shimmerX + shimmerW);
            if (clampedRight > clampedLeft) {
                sr.setColor(1f, 1f, 1f, 0.24f * (float) Math.sin(progress * Math.PI));
                sr.rect(clampedLeft, y + 4f, clampedRight - clampedLeft, h - 10f);
            }
        }
    }

    private void drawStar(ShapeRenderer sr, float cx, float cy, float r, Color color) {
        sr.setColor(color);
        int points = 5;
        float innerR = r * 0.42f;
        for (int i = 0; i < points; i++) {
            float a1 = (float) (i * 2 * Math.PI / points - Math.PI / 2);
            float a2 = (float) ((i + 0.5f) * 2 * Math.PI / points - Math.PI / 2);
            float a3 = (float) ((i + 1) * 2 * Math.PI / points - Math.PI / 2);

            float x1 = cx + (float) Math.cos(a1) * r;
            float y1 = cy + (float) Math.sin(a1) * r;
            float x2 = cx + (float) Math.cos(a2) * innerR;
            float y2 = cy + (float) Math.sin(a2) * innerR;
            float x3 = cx + (float) Math.cos(a3) * r;
            float y3 = cy + (float) Math.sin(a3) * r;

            sr.triangle(cx, cy, x1, y1, x2, y2);
            sr.triangle(cx, cy, x2, y2, x3, y3);
        }
    }

    private void renderCleanTypography() {
        // ── BUTON İKON ETİKETLERİ (Alt köşe daireleri altında küçük etiket) ──
        // Geri butonu etiketi
        drawShadowText(game.smallFont, "<", 37f, 102f, new Color(0.7f, 0.85f, 1f, 0.75f));
        // Reset butonu etiketi
        drawShadowText(game.smallFont, "R", UI_WIDTH - 73f, 102f, new Color(0.7f, 0.85f, 1f, 0.75f));

        // ── MERKEZDEKİ BİLGİ KAPSÜLü METİNLERİ ─────────────────────────────
        float capsY = UI_HEIGHT - 62f;
        float capsH = 46f;

        String titleText;
        if (dailyChallenge != null) {
            titleText = String.format("%s: %s", dailyChallenge.getDifficultyLabel(com.game.pullthepin.i18n.I18N.isTurkish()), dailyChallenge.modifier.getTitle(com.game.pullthepin.i18n.I18N.isTurkish()));
        } else if (isJackpotLevel()) {
            titleText = com.game.pullthepin.i18n.I18N.get("JACKPOT_TITLE") + " (" + levelData.levelTitle + ")";
        } else {
            titleText = levelData.levelTitle;
        }
        glyphLayout.setText(game.smallFont, titleText);
        drawShadowText(game.smallFont, titleText, (UI_WIDTH - glyphLayout.width) / 2f,
                capsY + capsH - 6,
                (dailyChallenge != null || isJackpotLevel() ? new Color(1.0f, 0.85f, 0.3f, 1f) : Color.WHITE));

        int collected = levelData.jar.getCollectedBalls();
        int req = levelData.jar.getRequiredBalls();
        int pct = req > 0 ? Math.min(100, (collected * 100) / req) : 0;
        String scoreText = com.game.pullthepin.i18n.I18N.get("HUD_COLLECTED", collected, req, pct);
        glyphLayout.setText(game.smallFont, scoreText);
        drawShadowText(game.smallFont, scoreText, (UI_WIDTH - glyphLayout.width) / 2f,
                capsY + 19f, new Color(0.75f, 0.90f, 1.0f, 0.95f));

        // ── ÜST SAĞ: CANLI ALTIN MİKTARI ──────────────────────────────────
        float coinCapsX = UI_WIDTH - 120f - 20f;
        float coinCapsY = UI_HEIGHT - 56f;
        String balanceStr = String.valueOf(game.saveManager.getCoins());
        game.smallFont.setColor(new Color(1.0f, 0.88f, 0.25f, 1.0f));
        game.smallFont.draw(game.batch, balanceStr, coinCapsX + 34f, coinCapsY + 26f);

        // ── AĞIR ÇEKİM BANNER ────────────────────────────────────────────────
        if (isSlowMoActive && cinematicBarHeight > 35f) {
            String slowMoTitle = com.game.pullthepin.i18n.I18N.isTurkish() ? "AGIR CEKIM ZAFER" : "SLOW-MOTION FINISH";
            drawCenteredShadowText(game.regularFont, slowMoTitle, UI_HEIGHT - (cinematicBarHeight / 2f) + 12f, new Color(1.0f, 0.88f, 0.25f, 1.0f));
        }

        // ── SEVIYE BAŞLANGIÇ BANNER METİNLERİ ────────────────────────────────
        if (introBannerTimer > 0) {
            float alpha = Math.min(1.0f, introBannerTimer * 1.5f);
            float bY = 580f, bH = 80f;
            String worldTitle = (dailyChallenge != null)
                    ? String.format("- %s -", dailyChallenge.getDifficultyLabel(com.game.pullthepin.i18n.I18N.isTurkish()))
                    : String.format("- %s -", worldTheme.getThemeType().getName(com.game.pullthepin.i18n.I18N.isTurkish()));
            drawCenteredShadowText(game.smallFont, worldTitle, bY + bH - 12,
                    new Color(worldTheme.getAccentColor().r, worldTheme.getAccentColor().g, worldTheme.getAccentColor().b, alpha));
            drawCenteredShadowText(game.regularFont,
                    (dailyChallenge != null ? dailyChallenge.modifier.getDesc(com.game.pullthepin.i18n.I18N.isTurkish()) : levelData.levelTitle),
                    bY + 34f, new Color(1f, 1f, 1f, alpha));
        }

        if (state == GameState.WON) {
            String winTitle = isJackpotLevel() ? com.game.pullthepin.i18n.I18N.get("JACKPOT_TITLE") : com.game.pullthepin.i18n.I18N.get("WIN_TITLE");
            drawCenteredShadowText(game.titleFont, winTitle, 865, new Color(1.0f, 0.82f, 0.22f, 1f));

            if (dailyChallenge != null) {
                String winSub = com.game.pullthepin.i18n.I18N.isTurkish() ? "GÖREV BAŞARIYLA TAMAMLANDI!" : "MISSION ACCOMPLISHED!";
                drawCenteredShadowText(game.regularFont, winSub, 795, Color.WHITE);
                String rewardStr = String.format(com.game.pullthepin.i18n.I18N.get("DAILY_VICTORY_REWARD", dailyChallenge.rewardCoins));
                drawCenteredShadowText(game.regularFont, rewardStr, 745, new Color(1.0f, 0.88f, 0.25f, 1f));
                drawCenteredShadowText(game.regularFont, com.game.pullthepin.i18n.I18N.get("BTN_BACK_CHALLENGES"), 443, Color.WHITE);
            } else {
                drawCenteredShadowText(game.regularFont, com.game.pullthepin.i18n.I18N.get("WIN_LEVEL_DONE", currentLevel), 795, Color.WHITE);

                // ── KAZANILAN ALTIN MİKTARI ──
                int displayCoin = Math.min(earnedCoinsThisLevel, (int) animatedCoinCount);
                String coinStr = "+ " + displayCoin + " " + (com.game.pullthepin.i18n.I18N.isTurkish() ? "ALTIN" : "COINS");
                drawCenteredShadowText(game.regularFont, coinStr, 600, new Color(1.0f, 0.88f, 0.22f, 1.0f));

                // Detay dökümü / Bonus rozetleri
                StringBuilder bonuses = new StringBuilder();
                boolean isTR = com.game.pullthepin.i18n.I18N.isTurkish();
                if (jackpotBonusCoins > 0) bonuses.append(isTR ? "JACKPOT (+250) " : "JACKPOT (+250) ");
                if (isFirstTimeClear) bonuses.append(isTR ? "Ilk Gecis (+50) " : "First Clear (+50) ");
                if (speedBonusCoins > 0) bonuses.append(isTR ? "Hiz (+" + speedBonusCoins + ") " : "Speed (+" + speedBonusCoins + ") ");
                if (surplusBallCoins > 0) bonuses.append(isTR ? "Ekstra Top (+" + surplusBallCoins + ")" : "Extra (+" + surplusBallCoins + ")");

                if (bonuses.length() > 0) {
                    drawCenteredShadowText(game.smallFont, bonuses.toString().trim(), 550, new Color(0.85f, 0.95f, 0.50f, 0.95f));
                } else if (earnedStars == 3 && starHasPopped[2]) {
                    drawCenteredShadowText(game.smallFont, com.game.pullthepin.i18n.I18N.get("PERFECT_STAR_BADGE"), 550, new Color(1.0f, 0.90f, 0.35f, 1f));
                }

                drawCenteredShadowText(game.regularFont, com.game.pullthepin.i18n.I18N.get("BTN_NEXT_LEVEL"), 443, Color.WHITE);
            }

            // ── BAŞARIM AÇILDI BİLDİRİMİ ──
            if (achievementToastTimer > 0 && unlockedAchievementName != null) {
                String achMsg = (com.game.pullthepin.i18n.I18N.isTurkish() ? "BASARIM ACILDI: " : "ACHIEVEMENT: ") + unlockedAchievementName;
                drawCenteredShadowText(game.smallFont, achMsg, 905, new Color(1.0f, 0.90f, 0.30f, 1.0f));
            }
        } else if (state == GameState.LOST) {
            // Başarısız Başlığı
            String loseTitle = com.game.pullthepin.i18n.I18N.isTurkish() ? "BASARISIZ!" : "FAILED!";
            drawCenteredShadowText(game.titleFont, loseTitle, 815, new Color(0.98f, 0.35f, 0.40f, 1f));

            // Sebebe göre açıklama metni
            String loseReason;
            String loseHint;
            boolean isTR = com.game.pullthepin.i18n.I18N.isTurkish();
            switch (lostReason) {
                case ACID:
                    loseReason = isTR ? "Toplar asitte eridi!" : "Balls melted in toxic acid!";
                    loseHint   = isTR ? "Once su bilyesiyle asidi notralize et" : "Neutralize acid with water first";
                    break;
                case LASER:
                    loseReason = isTR ? "Lazer toplarini yok etti!" : "Laser destroyed your balls!";
                    loseHint   = isTR ? "Pimi dogru zamanda cek" : "Pull the pin at the right time";
                    break;
                case BOMB:
                    loseReason = isTR ? "Bomba patlamasi cok top yok etti!" : "Explosion wiped out too many balls!";
                    loseHint   = isTR ? "Bombadan uzak dur" : "Keep balls away from bombs";
                    break;
                case SPIKE:
                    loseReason = isTR ? "Dikenlere cok top dustU!" : "Too many balls hit the spikes!";
                    loseHint   = isTR ? "Farkli sirada dene" : "Try a different pin order";
                    break;
                case NOT_ENOUGH:
                    loseReason = isTR ? "Yeterli top kalmadi!" : "Not enough balls left!";
                    loseHint   = isTR ? "Engelleri dikkatli gecir" : "Navigate obstacles carefully";
                    break;
                case STUCK:
                    loseReason = isTR ? "Toplar kavanozu dolduramadi!" : "Balls couldn't fill the jar!";
                    loseHint   = isTR ? "Tum pimleri dogru sirada cek" : "Pull all pins in the right order";
                    break;
                default:
                    loseReason = isTR ? "Hedef doldurulamadi!" : "Target not reached!";
                    loseHint   = isTR ? "Tekrar dene!" : "Try again!";
            }

            drawCenteredShadowText(game.regularFont, loseReason, 750, new Color(1f, 0.7f, 0.3f, 1f));
            drawCenteredShadowText(game.smallFont,   loseHint,   708, new Color(0.75f, 0.88f, 1f, 0.90f));
            drawCenteredShadowText(game.regularFont, isTR ? "TEKRAR DENE" : "TRY AGAIN", 503, Color.WHITE);
        }

        // ── FAST-TRACK FAIL UYARI BİLDİRİMİ ──────────────────────────────
        if (failFlashTimer > 0 && state == GameState.PLAYING) {
            boolean isTR = com.game.pullthepin.i18n.I18N.isTurkish();
            String warnTitle = isTR ? "YETERLI TOP KALMADI!" : "NOT ENOUGH BALLS!";
            String warnSub = isTR ? "Seviye basarisiz oluyor..." : "Level failed...";
            drawCenteredShadowText(game.regularFont, warnTitle, 660, new Color(1.0f, 0.35f, 0.35f, 1.0f));
            drawCenteredShadowText(game.smallFont, warnSub, 615, new Color(1.0f, 0.85f, 0.85f, 0.90f));
        }
    }

    private void drawShadowText(BitmapFont font, String text, float x, float y, Color color) {
        font.setColor(0f, 0f, 0f, 0.85f);
        font.draw(game.batch, text, x + 2f, y - 2f);
        font.setColor(color);
        font.draw(game.batch, text, x, y);
    }

    private void drawCenteredShadowText(BitmapFont font, String text, float y, Color color) {
        glyphLayout.setText(font, text);
        float x = (UI_WIDTH - glyphLayout.width) / 2f;
        drawShadowText(font, text, x, y, color);
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        touchUIPos.set(screenX, screenY, 0);
        uiViewport.unproject(touchUIPos);

        // ← Geri butonu (sol alt daire: cx=55, cy=88, r=36)
        if (touchUIPos.x >= 19 && touchUIPos.x <= 91 && touchUIPos.y >= 52 && touchUIPos.y <= 124) {
            game.soundEngine.playClick();
            game.triggerHaptic(HapticType.BUTTON_CLICK);
            if (dailyChallenge != null) {
                game.openDailyChallenge();
            } else {
                game.openLevelSelect();
            }
            return true;
        }

        // ⟳ Reset butonu (sağ alt daire: cx=665, cy=88, r=36)
        if (touchUIPos.x >= 629 && touchUIPos.x <= 701 && touchUIPos.y >= 52 && touchUIPos.y <= 124) {
            game.soundEngine.playClick();
            game.triggerHaptic(HapticType.BUTTON_CLICK);
            initLevel();
            return true;
        }

        if (state == GameState.WON) {
            if (touchUIPos.x >= 110 && touchUIPos.x <= 610 && touchUIPos.y >= 380 && touchUIPos.y <= 490) {
                game.soundEngine.playClick();
                game.triggerHaptic(HapticType.BUTTON_CLICK);
                if (dailyChallenge != null) {
                    game.openDailyChallenge();
                } else {
                    int next = currentLevel < LevelLoader.TOTAL_LEVELS ? currentLevel + 1 : 1;
                    game.startLevel(next);
                }
                return true;
            }
            return true;
        }

        if (state == GameState.LOST) {
            // Butona veya karta dokunulduğunda anında yeniden başlat
            if (touchUIPos.x >= 60 && touchUIPos.x <= 660 && touchUIPos.y >= 380 && touchUIPos.y <= 880) {
                game.soundEngine.playClick();
                game.triggerHaptic(HapticType.BUTTON_CLICK);
                initLevel();
                return true;
            }
            return true;
        }

        touchWorldPos.set(screenX, screenY, 0);
        worldViewport.unproject(touchWorldPos);

        for (int i = 0; i < levelData.pins.size; i++) {
            Pin pin = levelData.pins.get(i);
            if (pin.containsHandle(touchWorldPos.x, touchWorldPos.y) || pin.containsPin(touchWorldPos.x, touchWorldPos.y)) {
                if (pin.isLocked()) {
                    pin.triggerLockedFeedback();
                    game.soundEngine.playFail();
                    game.triggerHaptic(HapticType.FAILURE_BUZZ);
                    String lockMsg = com.game.pullthepin.i18n.I18N.isTurkish() ? "🔒 ÖNCE ANAHTARI BUL!" : "🔒 FIND KEY FIRST!";
                    particleSystem.spawnFloatingText(lockMsg, pin.getHandlePosition().x, pin.getHandlePosition().y + 0.6f, new Color(0.98f, 0.35f, 0.40f, 1f));
                    return true;
                }
                selectedPin = pin;
                selectedPin.setSelected(true);
                particleSystem.spawnPinShockwave(touchWorldPos.x, touchWorldPos.y);
                game.soundEngine.playPinPull();
                game.triggerHaptic(HapticType.LIGHT_TICK);
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        if (selectedPin != null) {
            touchWorldPos.set(screenX, screenY, 0);
            worldViewport.unproject(touchWorldPos);
            boolean justPulled = selectedPin.onDrag(touchWorldPos.x, touchWorldPos.y);
            if (justPulled) {
                game.triggerHaptic(HapticType.PIN_RELEASE);
                particleSystem.spawnColorSparks(selectedPin.getCurrentX(), selectedPin.getCurrentY(), selectedPin.getSparkColor());
                game.soundEngine.playPinPull();
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if (selectedPin != null) {
            selectedPin.setSelected(false);
            selectedPin = null;
            return true;
        }
        return false;
    }

    @Override
    public void resize(int width, int height) {
        worldViewport.update(width, height, true);
        uiViewport.update(width, height, true);
    }

    @Override public void show() {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}

    @Override
    public void dispose() {
        if (world != null) world.dispose();
    }
}
