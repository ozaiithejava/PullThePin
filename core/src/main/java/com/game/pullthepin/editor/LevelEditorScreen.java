package com.game.pullthepin.editor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
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
import com.game.pullthepin.entities.*;

/**
 * Profesyonel, Kapsamlı ve Hatasız 2.5D İzometrik Bölüm Editörü (Level Designer).
 * - Klavye ve Ctrl Kısayolları (Ctrl+S, Ctrl+Z, Ctrl+Y, Space, Tab, R, 1-0 vb.)
 * - Geri Al / Yinele (Undo / Redo Stack) Sistemi
 * - Seç & Taşı Aracı (Selection & Drag-and-Drop Repositioning)
 * - Hayalet İmleç Önizlemesi (Ghost Preview & Rubber-banding)
 * - Canlı Fizik Testi & Anında Yeniden Başlatma
 * - 10 Seviye Yuvası & JSON Kayıt / Yükleme
 * - Kısayol Yardım Penceresi (H / F1)
 */
public class LevelEditorScreen extends InputAdapter implements Screen {

    public static final float WORLD_WIDTH = 9.0f;
    public static final float WORLD_HEIGHT = 16.0f;
    public static final float UI_WIDTH = 720f;
    public static final float UI_HEIGHT = 1280f;

    public enum Tool {
        SELECT, WALL, PIN, COLOR_BALL, GRAY_BALL, BOMB, JAR, PORTAL, BOUNCER, SPIKE, DELETE
    }

    private PullThePinGame game;
    private OrthographicCamera worldCamera;
    private FitViewport worldViewport;
    private OrthographicCamera uiCamera;
    private FitViewport uiViewport;

    private GlyphLayout glyphLayout = new GlyphLayout();
    private Vector3 touchWorld = new Vector3();
    private Vector3 touchUI = new Vector3();
    private Vector3 mouseWorld = new Vector3();

    // Editör Durumu
    private EditorData editorData;
    private Tool activeTool = Tool.PIN;
    private int currentSlot = 1;
    private boolean isGridSnap = true;
    private boolean isPlaytesting = false;
    private boolean showHelpModal = false;
    private String toastMessage = "";
    private float toastTimer = 0f;

    // Geri Al / Yinele (Undo / Redo Stack)
    private Array<EditorData> undoStack = new Array<>();
    private Array<EditorData> redoStack = new Array<>();
    private static final int MAX_UNDO_STATES = 30;

    // Seç & Taşı (Select & Drag) Durumu
    private enum SelectedType { NONE, PIN, BALL, JAR, PORTAL_IN, PORTAL_OUT, BOUNCER, SPIKE, WALL_POINT }
    private SelectedType selectedType = SelectedType.NONE;
    private int selectedIndex = -1;
    private int selectedPointIndex = -1;
    private boolean isDraggingObject = false;

    // Pim & Trambolin Seçenekleri
    private float selectedPinLength = 4.5f;
    private Pin.Direction selectedPinDir = Pin.Direction.RIGHT;
    private float selectedBouncerAngle = 45f;

    // Duvar Çizim Noktaları
    private Array<Vector2> currentWallPoints = new Array<>();

    // Portal Çizim Durumu
    private Vector2 pendingPortalIn = null;

    // --- TEST MODU (PLAYTEST) ---
    private World testWorld;
    private Array<Ball> testBalls = new Array<>();
    private Array<Pin> testPins = new Array<>();
    private Array<LevelWall> testWalls = new Array<>();
    private Array<Portal> testPortals = new Array<>();
    private Array<Bouncer> testBouncers = new Array<>();
    private Array<SpikeObstacle> testSpikes = new Array<>();
    private Jar testJar;
    private ParticleSystem testParticles;
    private Pin testSelectedPin = null;
    private Array<Body> bodiesToDestroy = new Array<>();
    private Array<Portal.TeleportRequest> pendingTeleports = new Array<>();
    private float testGravityX = 0f;
    private float testGravityY = -22.0f;

    public LevelEditorScreen(PullThePinGame game) {
        this.game = game;

        worldCamera = new OrthographicCamera();
        worldViewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT, worldCamera);
        worldCamera.position.set(WORLD_WIDTH / 2f, WORLD_HEIGHT / 2f, 0);

        uiCamera = new OrthographicCamera();
        uiViewport = new FitViewport(UI_WIDTH, UI_HEIGHT, uiCamera);
        uiCamera.position.set(UI_WIDTH / 2f, UI_HEIGHT / 2f, 0);

        editorData = EditorData.loadFromSlot(currentSlot);
        testParticles = new ParticleSystem();
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(this);
    }

    // =========================================================================
    // GERİ AL / YİNELE (UNDO / REDO) YÖNETİMİ
    // =========================================================================

    private void pushUndoState() {
        undoStack.add(editorData.copy());
        if (undoStack.size > MAX_UNDO_STATES) {
            undoStack.removeIndex(0);
        }
        redoStack.clear();
    }

    public void undo() {
        if (undoStack.size == 0) {
            showToast("ℹ️ Geri alinacak islem yok.");
            return;
        }
        if (isPlaytesting) stopPlaytest();
        redoStack.add(editorData.copy());
        editorData = undoStack.pop();
        currentWallPoints.clear();
        pendingPortalIn = null;
        selectedType = SelectedType.NONE;
        game.soundEngine.playClick();
        showToast("↩️ Geri Alindi (Undo)");
    }

    public void redo() {
        if (redoStack.size == 0) {
            showToast("ℹ️ Yinelenecek islem yok.");
            return;
        }
        if (isPlaytesting) stopPlaytest();
        undoStack.add(editorData.copy());
        editorData = redoStack.pop();
        currentWallPoints.clear();
        pendingPortalIn = null;
        selectedType = SelectedType.NONE;
        game.soundEngine.playClick();
        showToast("↪️ Yinelendi (Redo)");
    }

    // =========================================================================
    // CANLI TEST MODU (PLAYTEST)
    // =========================================================================

    private void startPlaytest() {
        isPlaytesting = true;
        pendingPortalIn = null;
        currentWallPoints.clear();
        selectedType = SelectedType.NONE;

        if (testWorld != null) testWorld.dispose();
        testWorld = new World(new Vector2(0, -22.0f), true);
        testParticles.clear();
        bodiesToDestroy.clear();
        pendingTeleports.clear();

        testBalls.clear();
        testPins.clear();
        testWalls.clear();
        testPortals.clear();
        testBouncers.clear();
        testSpikes.clear();

        // 1. Duvarlar
        for (EditorData.WallDef w : editorData.walls) {
            if (w.points.size >= 2) {
                Vector2[] pts = new Vector2[w.points.size];
                for (int i = 0; i < w.points.size; i++) pts[i] = w.points.get(i).cpy();
                testWalls.add(new LevelWall(testWorld, pts));
            }
        }

        // 2. Pimler
        for (EditorData.PinDef p : editorData.pins) {
            testPins.add(new Pin(testWorld, p.x, p.y, p.length, p.dir));
        }

        // 3. Portallar
        for (EditorData.PortalDef port : editorData.portals) {
            testPortals.add(new Portal(testWorld, port.inX, port.inY, port.outX, port.outY));
        }

        // 4. Trambolinler
        for (EditorData.BouncerDef b : editorData.bouncers) {
            testBouncers.add(new Bouncer(testWorld, b.x, b.y, b.width, b.height, b.angle, b.force));
        }

        // 5. Dikenler
        for (EditorData.SpikeDef s : editorData.spikes) {
            testSpikes.add(new SpikeObstacle(testWorld, s.x, s.y, s.width, s.height));
        }

        // 6. Kavanoz
        testJar = new Jar(testWorld, editorData.jar.x, editorData.jar.y, editorData.jar.width, editorData.jar.height, editorData.jar.requiredBalls);

        // 7. Toplar
        for (EditorData.BallDef b : editorData.balls) {
            testBalls.add(new Ball(testWorld, b.x, b.y, b.type));
        }

        setupPlaytestCollisions();
        showToast("▶ Test Modu Baslatildi! (Durdurmak icin SPACE / F5)");
    }

    private void stopPlaytest() {
        isPlaytesting = false;
        if (testWorld != null) {
            testWorld.dispose();
            testWorld = null;
        }
        showToast("✏️ Duzenleme Moduna Donuldu.");
    }

    private void restartPlaytest() {
        if (!isPlaytesting) {
            startPlaytest();
        } else {
            startPlaytest();
            showToast("🔄 Test Yeniden Baslatildi.");
        }
    }

    private void setupPlaytestCollisions() {
        testWorld.setContactListener(new ContactListener() {
            @Override
            public void beginContact(Contact contact) {
                Fixture fA = contact.getFixtureA();
                Fixture fB = contact.getFixtureB();
                Object uA = fA.getBody().getUserData();
                Object uB = fB.getBody().getUserData();

                if (uA instanceof Ball && uB instanceof Ball) {
                    Ball b1 = (Ball) uA; Ball b2 = (Ball) uB;
                    game.soundEngine.playMarbleClink();
                    if (b1.getType() == Ball.BallType.BOMB || b2.getType() == Ball.BallType.BOMB) {
                        triggerBomb(b1.getType() == Ball.BallType.BOMB ? b1 : b2);
                    } else if (b1.getType() == Ball.BallType.COLORED && b2.getType() == Ball.BallType.GRAY) {
                        if (b2.colorize(b1.getColor())) {
                            if (b2.isCollected()) {
                                testJar.incrementBalls();
                            }
                            testParticles.spawnColorSparks(b2.getPosition().x, b2.getPosition().y, b2.getColor());
                            game.soundEngine.playZenChime();
                        }
                    } else if (b2.getType() == Ball.BallType.COLORED && b1.getType() == Ball.BallType.GRAY) {
                        if (b1.colorize(b2.getColor())) {
                            if (b1.isCollected()) {
                                testJar.incrementBalls();
                            }
                            testParticles.spawnColorSparks(b1.getPosition().x, b1.getPosition().y, b1.getColor());
                            game.soundEngine.playZenChime();
                        }
                    }
                }

                if (uA instanceof Ball && "JAR_SENSOR".equals(uB)) {
                    collectTestBall((Ball) uA);
                } else if (uB instanceof Ball && "JAR_SENSOR".equals(uA)) {
                    collectTestBall((Ball) uB);
                }

                if (uA instanceof Ball && uB instanceof Portal) {
                    Portal.TeleportRequest req = ((Portal) uB).createTeleportRequest((Ball) uA);
                    if (req != null) {
                        pendingTeleports.add(req);
                        game.soundEngine.playMarbleClink();
                    }
                } else if (uB instanceof Ball && uA instanceof Portal) {
                    Portal.TeleportRequest req = ((Portal) uA).createTeleportRequest((Ball) uB);
                    if (req != null) {
                        pendingTeleports.add(req);
                        game.soundEngine.playMarbleClink();
                    }
                }

                if (uA instanceof Ball && uB instanceof Bouncer) {
                    ((Bouncer) uB).applyImpulse((Ball) uA);
                    game.soundEngine.playBubblePop();
                } else if (uB instanceof Ball && uA instanceof Bouncer) {
                    ((Bouncer) uA).applyImpulse((Ball) uB);
                    game.soundEngine.playBubblePop();
                }

                if (uA instanceof Ball && uB instanceof SpikeObstacle) {
                    popTestBall((Ball) uA);
                } else if (uB instanceof Ball && uA instanceof SpikeObstacle) {
                    popTestBall((Ball) uB);
                }
            }

            @Override public void endContact(Contact contact) {}
            @Override public void preSolve(Contact contact, Manifold oldManifold) {}
            @Override public void postSolve(Contact contact, ContactImpulse impulse) {}
        });
    }

    private void collectTestBall(Ball b) {
        if (b.isCollected() || b.isDead()) return;
        if (b.getType() == Ball.BallType.COLORED) {
            b.setCollected(true);
            testJar.incrementBalls();
            testParticles.spawnLiquidSplash(b.getPosition().x, b.getPosition().y, b.getColor());
            game.soundEngine.playBubblePop();
        } else if (b.getType() == Ball.BallType.GRAY) {
            b.setCollected(true);
        } else if (b.getType() == Ball.BallType.BOMB) {
            triggerBomb(b);
        }
    }

    private void popTestBall(Ball b) {
        if (b.isDead()) return;
        b.setDead(true);
        testParticles.spawnColorSparks(b.getPosition().x, b.getPosition().y, Color.RED);
        bodiesToDestroy.add(b.getBody());
    }

    private void triggerBomb(Ball bomb) {
        if (bomb.isDead()) return;
        bomb.setDead(true);
        testParticles.spawnExplosion(bomb.getPosition().x, bomb.getPosition().y);
        game.soundEngine.playExplosion();

        for (Ball b : testBalls) {
            if (b.isDead() || b == bomb) continue;
            if (b.getPosition().dst(bomb.getPosition()) < 2.5f) {
                b.setDead(true);
                bodiesToDestroy.add(b.getBody());
            }
        }
        bodiesToDestroy.add(bomb.getBody());
    }

    // =========================================================================
    // RENDER & EKRAN ÇİZİMİ
    // =========================================================================

    @Override
    public void render(float delta) {
        if (toastTimer > 0) toastTimer -= delta;

        // Arka Plan
        Gdx.gl.glClearColor(0.06f, 0.08f, 0.14f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // 1. DÜNYA VE NESNELER
        worldViewport.apply();
        worldCamera.update();

        ShapeRenderer sr = game.shapeRenderer;
        sr.setProjectionMatrix(worldCamera.combined);

        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        sr.begin(ShapeRenderer.ShapeType.Filled);

        // Arka Plan Lüks Studio Gradyanı
        sr.rect(0, 0, WORLD_WIDTH, WORLD_HEIGHT,
                new Color(0.04f, 0.05f, 0.10f, 1f),
                new Color(0.04f, 0.05f, 0.10f, 1f),
                new Color(0.09f, 0.13f, 0.22f, 1f),
                new Color(0.09f, 0.13f, 0.22f, 1f)
        );

        // Kavanoz altı zemin ışık aurası (Ambient Floor Glow)
        sr.setColor(0.2f, 0.6f, 1.0f, 0.07f);
        sr.circle(editorData.jar.x, editorData.jar.y, 3.4f, 32);

        // Editör Kılavuz Izgarası
        if (!isPlaytesting) {
            sr.setColor(1f, 1f, 1f, 0.035f);
            for (float x = 0.5f; x <= 8.5f; x += 0.5f) {
                sr.rectLine(x, 0, x, 16, 0.015f);
            }
            for (float y = 0.5f; y <= 15.5f; y += 0.5f) {
                sr.rectLine(0, y, 9, y, 0.015f);
            }
        }

        if (isPlaytesting) {
            renderPlaytestWorld(sr, delta);
        } else {
            renderEditorWorld(sr);
            renderGhostPreview(sr);
        }

        sr.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);

        // 2. YÜKSEK ÇÖZÜNÜRLÜKLÜ EDİTÖR ARAYÜZÜ (720x1280)
        uiViewport.apply();
        uiCamera.update();

        sr.setProjectionMatrix(uiCamera.combined);
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        sr.begin(ShapeRenderer.ShapeType.Filled);
        renderEditorUIBars(sr);
        sr.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);

        // Metinler & Bilgi Göstergeleri
        game.batch.setProjectionMatrix(uiCamera.combined);
        game.batch.begin();
        renderEditorUIText();
        if (showHelpModal) {
            renderHelpModalText();
        }
        game.batch.end();
    }

    private void renderPlaytestWorld(ShapeRenderer sr, float delta) {
        // Sıvı / İvmeölçer Dinamik Eğim
        float targetGx = 0f;
        float targetGy = -22.0f;

        if (Gdx.input.isPeripheralAvailable(com.badlogic.gdx.Input.Peripheral.Accelerometer)) {
            float accelX = Gdx.input.getAccelerometerX();
            float accelY = Gdx.input.getAccelerometerY();
            targetGx = -accelX * 3.4f;
            targetGy = -Math.max(14.0f, accelY * 2.4f);
        }

        if (Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.A) || Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.LEFT)) {
            targetGx -= 16.0f;
        }
        if (Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.D) || Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.RIGHT)) {
            targetGx += 16.0f;
        }

        testGravityX += (targetGx - testGravityX) * 12.0f * delta;
        testGravityY += (targetGy - testGravityY) * 12.0f * delta;
        testWorld.setGravity(new Vector2(testGravityX, testGravityY));
        if (testJar != null) testJar.setFluidTilt(testGravityX);

        // Portallarda temas eden veya bekleyen bilyeleri kontrol et (Sıkışmayı önler)
        for (Portal portal : testPortals) {
            for (Ball ball : testBalls) {
                if (!ball.isDead() && !ball.isCollected() && ball.canTeleport()) {
                    if (ball.getPosition().dst(portal.getInPos()) < portal.getRadius() * 1.15f) {
                        Portal.TeleportRequest req = portal.createTeleportRequest(ball);
                        if (req != null) {
                            pendingTeleports.add(req);
                            testParticles.spawnColorSparks(ball.getPosition().x, ball.getPosition().y, ball.getColor());
                            game.soundEngine.playMarbleClink();
                        }
                    }
                }
            }
        }

        float fixedTimeStep = Math.min(delta, 1 / 30f);
        testWorld.step(fixedTimeStep, 10, 8);

        // 1. Işınlanmaları Dünya Kilidi Açıkken Uygula
        if (pendingTeleports.size > 0) {
            for (Portal.TeleportRequest tr : pendingTeleports) {
                tr.execute();
            }
            pendingTeleports.clear();
        }

        // 2. Gövdeleri Dünya Kilidi Açıkken Yok Et
        if (bodiesToDestroy.size > 0) {
            for (Body b : bodiesToDestroy) if (b != null) testWorld.destroyBody(b);
            bodiesToDestroy.clear();
        }

        for (Ball b : testBalls) b.update(delta);
        for (Pin p : testPins) p.update(delta);
        for (Portal port : testPortals) port.update(delta);
        testParticles.update(delta);

        for (Portal p : testPortals) p.render(sr);
        for (Bouncer b : testBouncers) b.render(sr);
        for (SpikeObstacle s : testSpikes) s.render(sr);
        for (LevelWall w : testWalls) w.render(sr);
        testJar.render(sr);
        for (Pin p : testPins) p.render(sr);
        for (Ball b : testBalls) b.render(sr);
        testParticles.render(sr);
    }

    private void renderEditorWorld(ShapeRenderer sr) {
        // 1. Portallar
        for (int i = 0; i < editorData.portals.size; i++) {
            EditorData.PortalDef port = editorData.portals.get(i);
            boolean isSelIn = (selectedType == SelectedType.PORTAL_IN && selectedIndex == i);
            boolean isSelOut = (selectedType == SelectedType.PORTAL_OUT && selectedIndex == i);

            sr.setColor(isSelIn ? Color.CYAN : new Color(0.2f, 0.6f, 1.0f, 0.75f));
            sr.circle(port.inX, port.inY, 0.5f, 16);
            sr.setColor(isSelOut ? Color.YELLOW : new Color(1.0f, 0.55f, 0.1f, 0.75f));
            sr.circle(port.outX, port.outY, 0.5f, 16);

            sr.setColor(1f, 1f, 1f, 0.25f);
            sr.rectLine(port.inX, port.inY, port.outX, port.outY, 0.04f);
        }

        if (pendingPortalIn != null) {
            sr.setColor(0.2f, 0.6f, 1.0f, 0.9f);
            sr.circle(pendingPortalIn.x, pendingPortalIn.y, 0.5f, 16);
        }

        // 2. Trambolinler
        for (int i = 0; i < editorData.bouncers.size; i++) {
            EditorData.BouncerDef b = editorData.bouncers.get(i);
            boolean isSel = (selectedType == SelectedType.BOUNCER && selectedIndex == i);
            sr.setColor(isSel ? Color.CYAN : new Color(0.95f, 0.2f, 0.65f, 1.0f));
            sr.rect(b.x - b.width / 2f, b.y - b.height / 2f, b.width / 2f, b.height / 2f, b.width, b.height, 1f, 1f, b.angle);
        }

        // 3. Dikenler
        for (int i = 0; i < editorData.spikes.size; i++) {
            EditorData.SpikeDef s = editorData.spikes.get(i);
            boolean isSel = (selectedType == SelectedType.SPIKE && selectedIndex == i);
            sr.setColor(isSel ? Color.CYAN : new Color(0.9f, 0.2f, 0.2f, 1.0f));
            sr.rect(s.x - s.width / 2f, s.y - s.height / 2f, s.width, s.height);
        }

        // 4. Duvarlar
        for (int i = 0; i < editorData.walls.size; i++) {
            EditorData.WallDef w = editorData.walls.get(i);
            if (w.points.size >= 2) {
                sr.setColor(0.35f, 0.45f, 0.60f, 1.0f);
                for (int j = 0; j < w.points.size - 1; j++) {
                    Vector2 p1 = w.points.get(j);
                    Vector2 p2 = w.points.get(j + 1);
                    sr.rectLine(p1.x, p1.y, p2.x, p2.y, 0.22f);
                    sr.circle(p1.x, p1.y, 0.11f, 12);
                    sr.circle(p2.x, p2.y, 0.11f, 12);
                }
                // Seçili duvar noktası vurgulama
                if (selectedType == SelectedType.WALL_POINT && selectedIndex == i && selectedPointIndex >= 0 && selectedPointIndex < w.points.size) {
                    Vector2 sp = w.points.get(selectedPointIndex);
                    sr.setColor(Color.CYAN);
                    sr.circle(sp.x, sp.y, 0.25f, 16);
                }
            }
        }

        // Çizilmekte olan aktif duvar
        if (currentWallPoints.size > 0) {
            sr.setColor(1.0f, 0.85f, 0.2f, 1.0f);
            for (int i = 0; i < currentWallPoints.size - 1; i++) {
                Vector2 p1 = currentWallPoints.get(i);
                Vector2 p2 = currentWallPoints.get(i + 1);
                sr.rectLine(p1.x, p1.y, p2.x, p2.y, 0.18f);
            }
            for (Vector2 pt : currentWallPoints) {
                sr.circle(pt.x, pt.y, 0.18f, 14);
            }
        }

        // 5. Kavanoz
        boolean isJarSel = (selectedType == SelectedType.JAR);
        sr.setColor(isJarSel ? new Color(0.2f, 0.8f, 1f, 0.6f) : new Color(0.3f, 0.6f, 0.9f, 0.35f));
        sr.rect(editorData.jar.x - editorData.jar.width / 2f, editorData.jar.y - editorData.jar.height / 2f, editorData.jar.width, editorData.jar.height);
        sr.setColor(0.5f, 0.85f, 1.0f, 0.9f);
        sr.rect(editorData.jar.x - editorData.jar.width / 2f, editorData.jar.y + editorData.jar.height / 2f - 0.1f, editorData.jar.width, 0.15f);

        // 6. Pimler
        for (int i = 0; i < editorData.pins.size; i++) {
            EditorData.PinDef p = editorData.pins.get(i);
            boolean isSel = (selectedType == SelectedType.PIN && selectedIndex == i);

            sr.setColor(isSel ? Color.CYAN : new Color(0.95f, 0.78f, 0.25f, 1f));
            if (p.dir == Pin.Direction.LEFT || p.dir == Pin.Direction.RIGHT) {
                sr.rect(p.x - p.length / 2f, p.y - 0.12f, p.length, 0.24f);
            } else {
                sr.rect(p.x - 0.12f, p.y - p.length / 2f, 0.24f, p.length);
            }
            sr.setColor(0.98f, 0.38f, 0.45f, 1f);
            float hx = p.dir == Pin.Direction.RIGHT ? p.x + p.length / 2f + 0.25f :
                       (p.dir == Pin.Direction.LEFT ? p.x - p.length / 2f - 0.25f : p.x);
            float hy = p.dir == Pin.Direction.UP ? p.y + p.length / 2f + 0.25f :
                       (p.dir == Pin.Direction.DOWN ? p.y - p.length / 2f - 0.25f : p.y);
            sr.circle(hx, hy, 0.4f, 16);
        }

        // 7. Toplar
        for (int i = 0; i < editorData.balls.size; i++) {
            EditorData.BallDef b = editorData.balls.get(i);
            boolean isSel = (selectedType == SelectedType.BALL && selectedIndex == i);

            if (isSel) {
                sr.setColor(Color.CYAN);
                sr.circle(b.x, b.y, Ball.RADIUS + 0.08f, 18);
            }

            if (b.type == Ball.BallType.COLORED) {
                sr.setColor(0.25f, 0.85f, 0.65f, 1.0f);
            } else if (b.type == Ball.BallType.GRAY) {
                sr.setColor(0.72f, 0.74f, 0.80f, 1.0f);
            } else {
                sr.setColor(0.95f, 0.25f, 0.35f, 1.0f);
            }
            sr.circle(b.x, b.y, Ball.RADIUS, 18);
            sr.setColor(1f, 1f, 1f, 0.6f);
            sr.circle(b.x - 0.07f, b.y + 0.07f, 0.07f, 10);
        }
    }

    private void renderGhostPreview(ShapeRenderer sr) {
        float gx = mouseWorld.x;
        float gy = mouseWorld.y;
        if (isGridSnap) {
            gx = Math.round(gx * 2f) / 2f;
            gy = Math.round(gy * 2f) / 2f;
        }

        if (gx < 0.2f || gx > 8.8f || gy < 1.5f || gy > 14.5f) return;

        switch (activeTool) {
            case WALL:
                if (currentWallPoints.size > 0) {
                    Vector2 last = currentWallPoints.peek();
                    sr.setColor(1f, 0.85f, 0.2f, 0.5f);
                    sr.rectLine(last.x, last.y, gx, gy, 0.15f);
                }
                sr.setColor(1f, 0.85f, 0.2f, 0.6f);
                sr.circle(gx, gy, 0.15f, 12);
                break;

            case PIN:
                sr.setColor(0.95f, 0.78f, 0.25f, 0.45f);
                if (selectedPinDir == Pin.Direction.LEFT || selectedPinDir == Pin.Direction.RIGHT) {
                    sr.rect(gx - selectedPinLength / 2f, gy - 0.12f, selectedPinLength, 0.24f);
                } else {
                    sr.rect(gx - 0.12f, gy - selectedPinLength / 2f, 0.24f, selectedPinLength);
                }
                sr.setColor(0.98f, 0.38f, 0.45f, 0.55f);
                float hx = selectedPinDir == Pin.Direction.RIGHT ? gx + selectedPinLength / 2f + 0.25f :
                           (selectedPinDir == Pin.Direction.LEFT ? gx - selectedPinLength / 2f - 0.25f : gx);
                float hy = selectedPinDir == Pin.Direction.UP ? gy + selectedPinLength / 2f + 0.25f :
                           (selectedPinDir == Pin.Direction.DOWN ? gy - selectedPinLength / 2f - 0.25f : gy);
                sr.circle(hx, hy, 0.4f, 16);
                break;

            case COLOR_BALL:
                sr.setColor(0.25f, 0.85f, 0.65f, 0.45f);
                sr.circle(gx, gy, Ball.RADIUS, 16);
                break;

            case GRAY_BALL:
                sr.setColor(0.72f, 0.74f, 0.80f, 0.45f);
                sr.circle(gx, gy, Ball.RADIUS, 16);
                break;

            case BOMB:
                sr.setColor(0.95f, 0.25f, 0.35f, 0.45f);
                sr.circle(gx, gy, Ball.RADIUS, 16);
                break;

            case BOUNCER:
                sr.setColor(0.95f, 0.2f, 0.65f, 0.45f);
                sr.rect(gx - 1.1f, gy - 0.175f, 1.1f, 0.175f, 2.2f, 0.35f, 1f, 1f, selectedBouncerAngle);
                break;

            case SPIKE:
                sr.setColor(0.9f, 0.2f, 0.2f, 0.45f);
                sr.rect(gx - 1.0f, gy - 0.2f, 2.0f, 0.4f);
                break;

            case JAR:
                sr.setColor(0.3f, 0.6f, 0.9f, 0.25f);
                sr.rect(gx - editorData.jar.width / 2f, gy - editorData.jar.height / 2f, editorData.jar.width, editorData.jar.height);
                break;

            case PORTAL:
                if (pendingPortalIn == null) {
                    sr.setColor(0.2f, 0.6f, 1.0f, 0.45f);
                    sr.circle(gx, gy, 0.5f, 16);
                } else {
                    sr.setColor(1.0f, 0.55f, 0.1f, 0.45f);
                    sr.circle(gx, gy, 0.5f, 16);
                    sr.setColor(1f, 1f, 1f, 0.2f);
                    sr.rectLine(pendingPortalIn.x, pendingPortalIn.y, gx, gy, 0.04f);
                }
                break;

            case DELETE:
                sr.setColor(1.0f, 0.2f, 0.2f, 0.45f);
                sr.circle(gx, gy, 0.35f, 16);
                break;

            case SELECT:
                sr.setColor(0.2f, 0.8f, 1.0f, 0.35f);
                sr.circle(gx, gy, 0.4f, 16);
                break;
        }
    }

    private void renderEditorUIBars(ShapeRenderer sr) {
        // 1. ÜST KONTROL ÇUBUĞU (Y: 1180 - 1280)
        sr.setColor(0.08f, 0.11f, 0.18f, 0.96f);
        sr.rect(0, 1170, UI_WIDTH, 110);
        sr.setColor(0.20f, 0.60f, 1.0f, 0.7f);
        sr.rect(0, 1170, UI_WIDTH, 4);

        // Geri Butonu
        sr.setColor(0.18f, 0.24f, 0.38f, 1f);
        sr.rect(12, 1185, 80, 75);

        // Test Et / Durdur Butonu
        if (isPlaytesting) {
            sr.setColor(0.98f, 0.38f, 0.45f, 1f); // Kırmızı (Durdur)
        } else {
            sr.setColor(0.25f, 0.85f, 0.65f, 1f); // Yeşil (Test Et)
        }
        sr.rect(100, 1185, 175, 75);

        // Geri Al (Undo - Ctrl+Z) & Yinele (Redo - Ctrl+Y)
        sr.setColor(undoStack.size > 0 ? new Color(0.25f, 0.45f, 0.75f, 1f) : new Color(0.15f, 0.20f, 0.28f, 0.8f));
        sr.rect(283, 1185, 72, 75);
        sr.setColor(redoStack.size > 0 ? new Color(0.25f, 0.45f, 0.75f, 1f) : new Color(0.15f, 0.20f, 0.28f, 0.8f));
        sr.rect(363, 1185, 72, 75);

        // Kaydet (Ctrl+S)
        sr.setColor(0.20f, 0.60f, 0.95f, 1f);
        sr.rect(443, 1185, 85, 75);

        // Yükle (Ctrl+L)
        sr.setColor(0.68f, 0.45f, 0.95f, 1f);
        sr.rect(536, 1185, 85, 75);

        // Yardım (? / H)
        sr.setColor(0.95f, 0.70f, 0.15f, 1f);
        sr.rect(629, 1185, 80, 75);

        // 2. İKİNCİL BİLGİ & SEÇENEK ÇUBUĞU (Y: 1090 - 1165)
        sr.setColor(0.10f, 0.14f, 0.22f, 0.92f);
        sr.rect(0, 1090, UI_WIDTH, 75);

        // Slot Seçici (< Slot 1 >)
        sr.setColor(0.16f, 0.22f, 0.34f, 1f);
        sr.rect(12, 1100, 160, 55);

        // Hedef Top Sayacı ([-] 15 [+])
        sr.setColor(0.16f, 0.22f, 0.34f, 1f);
        sr.rect(180, 1100, 270, 55);

        // Izgara Yapışma Butonu (Ctrl+G)
        sr.setColor(isGridSnap ? new Color(0.2f, 0.7f, 0.5f, 1f) : new Color(0.3f, 0.35f, 0.45f, 1f));
        sr.rect(458, 1100, 130, 55);

        // Temizle Butonu (Ctrl+N)
        sr.setColor(0.85f, 0.25f, 0.30f, 1f);
        sr.rect(596, 1100, 112, 55);

        // 3. ALT ARAÇ PALETİ (Y: 0 - 135)
        sr.setColor(0.08f, 0.11f, 0.18f, 0.96f);
        sr.rect(0, 0, UI_WIDTH, 135);
        sr.setColor(0.20f, 0.60f, 1.0f, 0.7f);
        sr.rect(0, 133, UI_WIDTH, 4);

        Tool[] tools = Tool.values();
        float btnW = 60f;
        float btnGap = 4.5f;
        float startX = 6f;

        for (int i = 0; i < tools.length; i++) {
            float bx = startX + i * (btnW + btnGap);
            boolean isCur = (tools[i] == activeTool && !isPlaytesting);

            if (isCur) {
                sr.setColor(1.0f, 0.80f, 0.22f, 1f); // Seçili sarı çerçeve
                sr.rect(bx - 2, 8, btnW + 4, 115);
            }

            sr.setColor(0.16f, 0.22f, 0.35f, 1f);
            sr.rect(bx, 10, btnW, 111);
        }

        // 4. Toast Mesajı Arka Planı
        if (toastTimer > 0) {
            sr.setColor(0.10f, 0.14f, 0.25f, 0.96f);
            sr.rect(50, 195, 620, 65);
            sr.setColor(1.0f, 0.80f, 0.22f, 1f);
            sr.rect(50, 195, 620, 3);
        }

        // 5. Kısayollar Modal Kartı
        if (showHelpModal) {
            sr.setColor(0f, 0f, 0f, 0.75f);
            sr.rect(0, 0, UI_WIDTH, UI_HEIGHT);

            sr.setColor(0.10f, 0.14f, 0.24f, 0.98f);
            sr.rect(40, 260, 640, 760);
            sr.setColor(1.0f, 0.80f, 0.22f, 1f);
            sr.rect(40, 1016, 640, 4);

            // Kapat Butonu
            sr.setColor(0.85f, 0.25f, 0.30f, 1f);
            sr.rect(590, 960, 70, 45);
        }
    }

    private void renderEditorUIText() {
        boolean tr = com.game.pullthepin.i18n.I18N.isTurkish();

        // Üst Bar Yazıları
        drawShadowText(game.smallFont, tr ? "< GERI" : "< BACK", 20, 1235, Color.WHITE);
        drawShadowText(game.regularFont, isPlaytesting ? (tr ? "DURDUR" : "STOP") : (tr ? "TEST ET >" : "TEST >"), 112, 1238, Color.WHITE);
        drawShadowText(game.smallFont, tr ? "GERI AL" : "UNDO", 288, 1235, Color.WHITE);
        drawShadowText(game.smallFont, tr ? "YINELE" : "REDO", 370, 1235, Color.WHITE);
        drawShadowText(game.smallFont, tr ? "KAYDET" : "SAVE", 450, 1235, Color.WHITE);
        drawShadowText(game.smallFont, tr ? "YUKLE" : "LOAD", 548, 1235, Color.WHITE);
        drawShadowText(game.smallFont, tr ? "YARDIM" : "HELP", 633, 1235, Color.WHITE);

        // İkincil Bar Yazıları
        drawShadowText(game.smallFont, "< Slot " + currentSlot + " >", 25, 1138, Color.WHITE);
        drawShadowText(game.smallFont, (tr ? "Hedef: [ - ] " : "Target: [ - ] ") + editorData.jar.requiredBalls + (tr ? " Top [ + ]" : " Balls [ + ]"), 195, 1138, new Color(0.85f, 0.95f, 1.0f, 1f));
        drawShadowText(game.smallFont, isGridSnap ? (tr ? "Izgara: ACIK" : "Grid: ON") : (tr ? "Izgara: KAPALI" : "Grid: OFF"), 466, 1138, Color.WHITE);
        drawShadowText(game.smallFont, tr ? "SIFIRLA" : "CLEAR", 608, 1138, Color.WHITE);

        // Alt Araç Paleti İsimleri ve Tuş Kısayolları (1-9, 0, S)
        String[] toolNamesTR = {"Tasi", "Duvar", "Pim", "Renkli", "Gri", "Bomba", "Kavanoz", "Portal", "Ziplatici", "Diken", "Silgi"};
        String[] toolNamesEN = {"Move", "Wall", "Pin", "Colored", "Gray", "Bomb", "Jar", "Portal", "Bouncer", "Spike", "Eraser"};
        String[] toolNames = tr ? toolNamesTR : toolNamesEN;
        String[] toolKeys = {"[S]", "[1]", "[2]", "[3]", "[4]", "[5]", "[6]", "[7]", "[8]", "[9]", "[0]"};
        float btnW = 60f;
        float btnGap = 4.5f;
        float startX = 6f;

        game.smallFont.getData().setScale(0.85f);
        for (int i = 0; i < toolNames.length; i++) {
            float bx = startX + i * (btnW + btnGap);
            glyphLayout.setText(game.smallFont, toolNames[i]);
            float tx = bx + (btnW - glyphLayout.width) / 2f;
            drawShadowText(game.smallFont, toolNames[i], tx, 75, Color.WHITE);

            glyphLayout.setText(game.smallFont, toolKeys[i]);
            float kx = bx + (btnW - glyphLayout.width) / 2f;
            drawShadowText(game.smallFont, toolKeys[i], kx, 40, new Color(1f, 0.85f, 0.3f, 0.9f));
        }
        game.smallFont.getData().setScale(1.4f); // Orijinal boyuta dön

        // İstatistik & Durum Çubuğu
        if (!isPlaytesting) {
            int colored = editorData.getColoredBallCount();
            int gray = editorData.getGrayBallCount();
            int bombs = editorData.getBombCount();
            int pins = editorData.pins.size;

            String statText = tr ?
                    ("Renkli: " + colored + " | Gri: " + gray + " | Bomba: " + bombs + " | Pim: " + pins) :
                    ("Colored: " + colored + " | Gray: " + gray + " | Bomb: " + bombs + " | Pin: " + pins);
            glyphLayout.setText(game.smallFont, statText);
            drawShadowText(game.smallFont, statText, (UI_WIDTH - glyphLayout.width) / 2f, 175, new Color(0.75f, 0.9f, 1.0f, 0.95f));

            // Araç İpucu
            String hint = getToolHint(activeTool);
            glyphLayout.setText(game.smallFont, hint);
            drawShadowText(game.smallFont, hint, (UI_WIDTH - glyphLayout.width) / 2f, 148, new Color(1.0f, 0.85f, 0.35f, 1f));
        } else {
            String testInfo = (tr ? "Toplanan: " : "Collected: ") + testJar.getCollectedBalls() + " / " + testJar.getRequiredBalls() + (tr ? "  |  Yeniden Baslat: [R]" : "  |  Restart: [R]");
            glyphLayout.setText(game.regularFont, testInfo);
            drawShadowText(game.regularFont, testInfo, (UI_WIDTH - glyphLayout.width) / 2f, 175, new Color(0.25f, 0.85f, 0.65f, 1f));
        }

        // Toast Bildirimi
        if (toastTimer > 0) {
            glyphLayout.setText(game.smallFont, toastMessage);
            drawShadowText(game.smallFont, toastMessage, (UI_WIDTH - glyphLayout.width) / 2f, 238, Color.WHITE);
        }
    }

    private void renderHelpModalText() {
        boolean tr = com.game.pullthepin.i18n.I18N.isTurkish();
        drawCenteredShadowText(game.regularFont, tr ? "KLAVYE VE KONTROL KISAYOLLARI" : "KEYBOARD SHORTCUTS & CONTROLS", 985, new Color(1.0f, 0.82f, 0.22f, 1f));
        drawShadowText(game.smallFont, "X", 618, 990, Color.WHITE);

        float y = 920;
        float dy = 44;

        String[][] shortcutsTR = {
                {"Ctrl + S", "Seviyeyi aktif yuvaya hizli kaydet"},
                {"Ctrl + L / Ctrl + O", "Aktif yuvadaki seviyeyi yukle"},
                {"Ctrl + Z", "Son yapilan islemi Geri Al (Undo)"},
                {"Ctrl + Y / Ctrl+Shift+Z", "Son geri alinan islemi Yinele (Redo)"},
                {"Ctrl + N", "Sahneyi sifirla ve yeni harita ac"},
                {"Ctrl + G", "Izgara sabitlemeyi Ac / Kapat (Snap)"},
                {"SPACE / F5", "Canli fizik testini Baslat / Durdur"},
                {"R", "Fizik testini aninda bastan baslat"},
                {"TAB", "Aktif arac seceneklerini cevir (Yon, Aci, Boyut)"},
                {"1 .. 9, 0, S", "Araclar: Tasi, Duvar, Pim, Toplar, Portal vb."},
                {"[ - ] / [ + ]", "Kavanoz hedef top sayisini degistir"},
                {"H / F1", "Bu yardim ekranini Ac / Kapat"},
                {"ESC", "Secimi iptal et / Ana menuye don"}
        };

        String[][] shortcutsEN = {
                {"Ctrl + S", "Quick save level to active slot"},
                {"Ctrl + L / Ctrl + O", "Load level from active slot"},
                {"Ctrl + Z", "Undo last editor action"},
                {"Ctrl + Y / Ctrl+Shift+Z", "Redo last undone action"},
                {"Ctrl + N", "Clear canvas and start fresh"},
                {"Ctrl + G", "Toggle grid snap On / Off"},
                {"SPACE / F5", "Start / Stop live playtest physics"},
                {"R", "Instant restart playtest"},
                {"TAB", "Cycle object options (Direction, Angle, Size)"},
                {"1 .. 9, 0, S", "Palette: Move, Wall, Pin, Balls, Portal etc."},
                {"[ - ] / [ + ]", "Adjust target jar required ball count"},
                {"H / F1", "Toggle this help shortcut menu"},
                {"ESC", "Deselect object / Return to Main Menu"}
        };

        String[][] shortcuts = tr ? shortcutsTR : shortcutsEN;

        game.smallFont.getData().setScale(1.15f);
        for (String[] sc : shortcuts) {
            drawShadowText(game.smallFont, sc[0], 65, y, new Color(0.35f, 0.85f, 1.0f, 1f));
            drawShadowText(game.smallFont, sc[1], 330, y, Color.WHITE);
            y -= dy;
        }
        game.smallFont.getData().setScale(1.4f);

        drawCenteredShadowText(game.smallFont, tr ? "Kapatmak icin herhangi bir yere dokunun veya ESC'ye basin." : "Tap anywhere or press ESC to close.", 310, new Color(0.7f, 0.8f, 0.9f, 0.8f));
    }

    private String getToolHint(Tool tool) {
        switch (tool) {
            case SELECT: return "Nesneleri surukleyip yerini degistirmek icin dokunun.";
            case WALL: return currentWallPoints.size > 0 ? "Nokta ekleyin veya bitirmek icin son noktaya cift tiklayin." : "Duvar cizmek icin noktalara dokunun.";
            case PIN: return "Pim koymak icin dokunun. Yon: " + selectedPinDir + " | Boyut: " + selectedPinLength + "m [TAB: Degistir]";
            case COLOR_BALL: return "Ekrana dokunarak veya basili tutup firca gibi surukleyerek renkli bilye ekleyin.";
            case GRAY_BALL: return "Gri bilye ekleyin. (Firca gibi surukleyebilirsiniz)";
            case BOMB: return "Bomba yerlestirmek icin dokunun.";
            case JAR: return "Hedef kavanozun konumunu degistirmek icin dokunun.";
            case PORTAL: return pendingPortalIn == null ? "1. Tik: Mavi Giris Portali yerlestir." : "2. Tik: Turuncu Cikis Portali yerlestir.";
            case BOUNCER: return "Ziplatici yerlestir. Aci: " + (int)selectedBouncerAngle + "° [TAB: Aci degistir]";
            case SPIKE: return "Olumcul diken engeli yerlestir.";
            case DELETE: return "Silmek istediginiz nesneye dokunun.";
            default: return "";
        }
    }

    private void showToast(String message) {
        this.toastMessage = message;
        this.toastTimer = 2.5f;
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

    // =========================================================================
    // KLAVYE VE CTRL KISAYOLLARI (KEYBOARD SHORTCUTS)
    // =========================================================================

    @Override
    public boolean keyDown(int keycode) {
        boolean ctrl = Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT) || Gdx.input.isKeyPressed(Input.Keys.CONTROL_RIGHT);
        boolean shift = Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT) || Gdx.input.isKeyPressed(Input.Keys.SHIFT_RIGHT);

        // Kısayol Yardım Modalını Kapat / Aç (H veya F1)
        if (keycode == Input.Keys.H || keycode == Input.Keys.F1) {
            showHelpModal = !showHelpModal;
            game.soundEngine.playClick();
            return true;
        }

        if (showHelpModal) {
            if (keycode == Input.Keys.ESCAPE) {
                showHelpModal = false;
                return true;
            }
            return false;
        }

        // CTRL KOMBİNASYONLARI
        if (ctrl) {
            if (keycode == Input.Keys.S) {
                EditorData.saveToSlot(currentSlot, editorData);
                game.soundEngine.playClick();
                showToast("✅ Seviye Slot " + currentSlot + "'e Kaydedildi! (Ctrl+S)");
                return true;
            }
            if (keycode == Input.Keys.L || keycode == Input.Keys.O) {
                if (isPlaytesting) stopPlaytest();
                editorData = EditorData.loadFromSlot(currentSlot);
                undoStack.clear();
                redoStack.clear();
                game.soundEngine.playClick();
                showToast("📂 Slot " + currentSlot + " Yuklendi! (Ctrl+L)");
                return true;
            }
            if (keycode == Input.Keys.Z) {
                if (shift) redo();
                else undo();
                return true;
            }
            if (keycode == Input.Keys.Y) {
                redo();
                return true;
            }
            if (keycode == Input.Keys.N) {
                pushUndoState();
                if (isPlaytesting) stopPlaytest();
                editorData.clear();
                game.soundEngine.playClick();
                showToast("🗑️ Yeni Seviye / Tuval Sifirlandi (Ctrl+N)");
                return true;
            }
            if (keycode == Input.Keys.G) {
                isGridSnap = !isGridSnap;
                game.soundEngine.playClick();
                showToast("Izgara Sabitleme: " + (isGridSnap ? "ACIK" : "KAPALI"));
                return true;
            }
            if (keycode == Input.Keys.P) {
                if (isPlaytesting) stopPlaytest();
                else startPlaytest();
                return true;
            }
            if (keycode == Input.Keys.R) {
                restartPlaytest();
                return true;
            }
        }

        // TEK TUŞ KISAYOLLARI
        if (keycode == Input.Keys.SPACE || keycode == Input.Keys.F5) {
            if (isPlaytesting) stopPlaytest();
            else startPlaytest();
            return true;
        }

        if (keycode == Input.Keys.R) {
            restartPlaytest();
            return true;
        }

        if (keycode == Input.Keys.TAB) {
            cycleToolOption(activeTool);
            game.soundEngine.playClick();
            return true;
        }

        if (keycode == Input.Keys.ESCAPE) {
            if (isPlaytesting) {
                stopPlaytest();
            } else if (currentWallPoints.size > 0) {
                currentWallPoints.clear();
                showToast("Duvar cizimi iptal edildi.");
            } else if (pendingPortalIn != null) {
                pendingPortalIn = null;
                showToast("Portal cizimi iptal edildi.");
            } else {
                game.openMenu();
            }
            return true;
        }

        if (keycode == Input.Keys.FORWARD_DEL || keycode == Input.Keys.BACKSPACE) {
            if (currentWallPoints.size > 0) {
                currentWallPoints.pop();
                showToast("Son duvar noktasi silindi.");
                return true;
            }
        }

        // Araç Seçimleri (1-9, 0, S)
        if (!ctrl) {
            if (keycode == Input.Keys.S) { selectTool(Tool.SELECT); return true; }
            if (keycode == Input.Keys.NUM_1 || keycode == Input.Keys.NUMPAD_1) { selectTool(Tool.WALL); return true; }
            if (keycode == Input.Keys.NUM_2 || keycode == Input.Keys.NUMPAD_2) { selectTool(Tool.PIN); return true; }
            if (keycode == Input.Keys.NUM_3 || keycode == Input.Keys.NUMPAD_3) { selectTool(Tool.COLOR_BALL); return true; }
            if (keycode == Input.Keys.NUM_4 || keycode == Input.Keys.NUMPAD_4) { selectTool(Tool.GRAY_BALL); return true; }
            if (keycode == Input.Keys.NUM_5 || keycode == Input.Keys.NUMPAD_5) { selectTool(Tool.BOMB); return true; }
            if (keycode == Input.Keys.NUM_6 || keycode == Input.Keys.NUMPAD_6) { selectTool(Tool.JAR); return true; }
            if (keycode == Input.Keys.NUM_7 || keycode == Input.Keys.NUMPAD_7) { selectTool(Tool.PORTAL); return true; }
            if (keycode == Input.Keys.NUM_8 || keycode == Input.Keys.NUMPAD_8) { selectTool(Tool.BOUNCER); return true; }
            if (keycode == Input.Keys.NUM_9 || keycode == Input.Keys.NUMPAD_9) { selectTool(Tool.SPIKE); return true; }
            if (keycode == Input.Keys.NUM_0 || keycode == Input.Keys.NUMPAD_0) { selectTool(Tool.DELETE); return true; }

            // Hedef Top Arttır / Azalt
            if (keycode == Input.Keys.PLUS || keycode == Input.Keys.EQUALS || keycode == Input.Keys.RIGHT_BRACKET) {
                editorData.jar.requiredBalls = Math.min(60, editorData.jar.requiredBalls + 1);
                showToast("Hedef Top: " + editorData.jar.requiredBalls);
                return true;
            }
            if (keycode == Input.Keys.MINUS || keycode == Input.Keys.LEFT_BRACKET) {
                editorData.jar.requiredBalls = Math.max(1, editorData.jar.requiredBalls - 1);
                showToast("Hedef Top: " + editorData.jar.requiredBalls);
                return true;
            }
        }

        return false;
    }

    private void selectTool(Tool tool) {
        if (isPlaytesting) stopPlaytest();
        activeTool = tool;
        pendingPortalIn = null;
        if (activeTool != Tool.WALL) currentWallPoints.clear();
        selectedType = SelectedType.NONE;
        game.soundEngine.playClick();
        showToast("Secili Arac: " + tool.name());
    }

    private void cycleToolOption(Tool tool) {
        if (tool == Tool.PIN) {
            if (selectedPinDir == Pin.Direction.RIGHT) selectedPinDir = Pin.Direction.LEFT;
            else if (selectedPinDir == Pin.Direction.LEFT) selectedPinDir = Pin.Direction.UP;
            else if (selectedPinDir == Pin.Direction.UP) selectedPinDir = Pin.Direction.DOWN;
            else selectedPinDir = Pin.Direction.RIGHT;
            showToast("Pim Yonu: " + selectedPinDir);
        } else if (tool == Tool.BOUNCER) {
            if (selectedBouncerAngle == 45f) selectedBouncerAngle = -45f;
            else if (selectedBouncerAngle == -45f) selectedBouncerAngle = 0f;
            else selectedBouncerAngle = 45f;
            showToast("Ziplatici Acisi: " + (int)selectedBouncerAngle + "°");
        }
    }

    // =========================================================================
    // FARE VE DOKUNMA ETKİLEŞİMLERİ (MOUSE / TOUCH INPUT)
    // =========================================================================

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        mouseWorld.set(screenX, screenY, 0);
        worldViewport.unproject(mouseWorld);
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (showHelpModal) {
            // Modal içindeki X butonu veya dışına tıklanırsa kapat
            showHelpModal = false;
            game.soundEngine.playClick();
            return true;
        }

        touchUI.set(screenX, screenY, 0);
        uiViewport.unproject(touchUI);

        // 1. ÜST BAR KONTROLLERİ
        if (touchUI.y >= 1180 && touchUI.y <= 1260) {
            // Geri
            if (touchUI.x >= 12 && touchUI.x <= 92) {
                game.soundEngine.playClick();
                if (isPlaytesting) stopPlaytest();
                game.openMenu();
                return true;
            }
            // Test Et / Durdur
            if (touchUI.x >= 100 && touchUI.x <= 275) {
                game.soundEngine.playClick();
                if (isPlaytesting) stopPlaytest();
                else startPlaytest();
                return true;
            }
            // Geri Al (Undo)
            if (touchUI.x >= 283 && touchUI.x <= 355) {
                undo();
                return true;
            }
            // Yinele (Redo)
            if (touchUI.x >= 363 && touchUI.x <= 435) {
                redo();
                return true;
            }
            // Kaydet
            if (touchUI.x >= 443 && touchUI.x <= 528) {
                game.soundEngine.playClick();
                EditorData.saveToSlot(currentSlot, editorData);
                showToast("✅ Seviye Slot " + currentSlot + "'e Kaydedildi!");
                return true;
            }
            // Yükle
            if (touchUI.x >= 536 && touchUI.x <= 621) {
                game.soundEngine.playClick();
                if (isPlaytesting) stopPlaytest();
                editorData = EditorData.loadFromSlot(currentSlot);
                undoStack.clear();
                redoStack.clear();
                showToast("📂 Slot " + currentSlot + " Yuklendi!");
                return true;
            }
            // Yardım
            if (touchUI.x >= 629 && touchUI.x <= 709) {
                game.soundEngine.playClick();
                showHelpModal = true;
                return true;
            }
        }

        // 2. İKİNCİL SEÇENEK ÇUBUĞU
        if (touchUI.y >= 1090 && touchUI.y <= 1165) {
            // Slot Değiştirme
            if (touchUI.x >= 12 && touchUI.x <= 172) {
                game.soundEngine.playClick();
                if (isPlaytesting) stopPlaytest();
                currentSlot = (currentSlot % 10) + 1;
                editorData = EditorData.loadFromSlot(currentSlot);
                undoStack.clear();
                redoStack.clear();
                showToast("Slot " + currentSlot + " Secildi.");
                return true;
            }
            // Hedef Top Azalt [-]
            if (touchUI.x >= 180 && touchUI.x <= 245) {
                pushUndoState();
                game.soundEngine.playClick();
                editorData.jar.requiredBalls = Math.max(5, editorData.jar.requiredBalls - 5);
                return true;
            }
            // Hedef Top Artır [+]
            if (touchUI.x >= 385 && touchUI.x <= 450) {
                pushUndoState();
                game.soundEngine.playClick();
                editorData.jar.requiredBalls = Math.min(60, editorData.jar.requiredBalls + 5);
                return true;
            }
            // Izgara Aç/Kapa
            if (touchUI.x >= 458 && touchUI.x <= 588) {
                game.soundEngine.playClick();
                isGridSnap = !isGridSnap;
                return true;
            }
            // Sıfırla / Temizle
            if (touchUI.x >= 596 && touchUI.x <= 708) {
                pushUndoState();
                game.soundEngine.playClick();
                if (isPlaytesting) stopPlaytest();
                editorData.clear();
                showToast("🗑️ Tuval Sifirlandi.");
                return true;
            }
        }

        // 3. ALT ARAÇ PALETİ
        if (touchUI.y >= 8 && touchUI.y <= 125 && !isPlaytesting) {
            Tool[] tools = Tool.values();
            float btnW = 60f;
            float btnGap = 4.5f;
            float startX = 6f;
            for (int i = 0; i < tools.length; i++) {
                float bx = startX + i * (btnW + btnGap);
                if (touchUI.x >= bx && touchUI.x <= bx + btnW) {
                    game.soundEngine.playClick();
                    if (activeTool == tools[i]) {
                        cycleToolOption(tools[i]);
                    } else {
                        selectTool(tools[i]);
                    }
                    return true;
                }
            }
        }

        // 4. TEST MODUNDA PİM ÇEKME
        if (isPlaytesting) {
            touchWorld.set(screenX, screenY, 0);
            worldViewport.unproject(touchWorld);

            for (Pin p : testPins) {
                if (p.containsHandle(touchWorld.x, touchWorld.y) || p.containsPin(touchWorld.x, touchWorld.y)) {
                    testSelectedPin = p;
                    testSelectedPin.setSelected(true);
                    game.soundEngine.playPinPull();
                    return true;
                }
            }
            return false;
        }

        // 5. EDİTÖR TUVALİNDE ÇALIŞMA
        if (touchUI.y > 135 && touchUI.y < 1090) {
            touchWorld.set(screenX, screenY, 0);
            worldViewport.unproject(touchWorld);

            float placeX = touchWorld.x;
            float placeY = touchWorld.y;

            if (isGridSnap) {
                placeX = Math.round(placeX * 2f) / 2f;
                placeY = Math.round(placeY * 2f) / 2f;
            }

            if (activeTool == Tool.SELECT) {
                handleObjectSelection(touchWorld.x, touchWorld.y);
            } else {
                handleCanvasPlacement(placeX, placeY);
            }
            return true;
        }

        return false;
    }

    private void handleObjectSelection(float x, float y) {
        // 1. Pim seç
        for (int i = editorData.pins.size - 1; i >= 0; i--) {
            EditorData.PinDef p = editorData.pins.get(i);
            if (Vector2.dst(p.x, p.y, x, y) < 1.2f) {
                pushUndoState();
                selectedType = SelectedType.PIN;
                selectedIndex = i;
                isDraggingObject = true;
                game.soundEngine.playClick();
                showToast("Pim Secildi (Tasiyabilirsiniz)");
                return;
            }
        }

        // 2. Top seç
        for (int i = editorData.balls.size - 1; i >= 0; i--) {
            EditorData.BallDef b = editorData.balls.get(i);
            if (Vector2.dst(b.x, b.y, x, y) < 0.45f) {
                pushUndoState();
                selectedType = SelectedType.BALL;
                selectedIndex = i;
                isDraggingObject = true;
                game.soundEngine.playClick();
                return;
            }
        }

        // 3. Trambolin seç
        for (int i = editorData.bouncers.size - 1; i >= 0; i--) {
            EditorData.BouncerDef b = editorData.bouncers.get(i);
            if (Vector2.dst(b.x, b.y, x, y) < 1.0f) {
                pushUndoState();
                selectedType = SelectedType.BOUNCER;
                selectedIndex = i;
                isDraggingObject = true;
                game.soundEngine.playClick();
                return;
            }
        }

        // 4. Diken seç
        for (int i = editorData.spikes.size - 1; i >= 0; i--) {
            EditorData.SpikeDef s = editorData.spikes.get(i);
            if (Vector2.dst(s.x, s.y, x, y) < 1.0f) {
                pushUndoState();
                selectedType = SelectedType.SPIKE;
                selectedIndex = i;
                isDraggingObject = true;
                game.soundEngine.playClick();
                return;
            }
        }

        // 5. Kavanoz seç
        if (Vector2.dst(editorData.jar.x, editorData.jar.y, x, y) < 1.6f) {
            pushUndoState();
            selectedType = SelectedType.JAR;
            isDraggingObject = true;
            game.soundEngine.playClick();
            showToast("Kavanoz Secildi");
            return;
        }

        // 6. Portal ucu seç
        for (int i = editorData.portals.size - 1; i >= 0; i--) {
            EditorData.PortalDef p = editorData.portals.get(i);
            if (Vector2.dst(p.inX, p.inY, x, y) < 0.6f) {
                pushUndoState();
                selectedType = SelectedType.PORTAL_IN;
                selectedIndex = i;
                isDraggingObject = true;
                return;
            }
            if (Vector2.dst(p.outX, p.outY, x, y) < 0.6f) {
                pushUndoState();
                selectedType = SelectedType.PORTAL_OUT;
                selectedIndex = i;
                isDraggingObject = true;
                return;
            }
        }

        // 7. Duvar noktası seç
        for (int i = 0; i < editorData.walls.size; i++) {
            EditorData.WallDef w = editorData.walls.get(i);
            for (int j = 0; j < w.points.size; j++) {
                if (w.points.get(j).dst(x, y) < 0.45f) {
                    pushUndoState();
                    selectedType = SelectedType.WALL_POINT;
                    selectedIndex = i;
                    selectedPointIndex = j;
                    isDraggingObject = true;
                    return;
                }
            }
        }

        selectedType = SelectedType.NONE;
        isDraggingObject = false;
    }

    private void handleCanvasPlacement(float x, float y) {
        switch (activeTool) {
            case WALL:
                // Çift tıklama: Duvarı bitir
                if (currentWallPoints.size > 0 && currentWallPoints.peek().dst(x, y) < 0.35f) {
                    if (currentWallPoints.size >= 2) {
                        pushUndoState();
                        EditorData.WallDef w = new EditorData.WallDef();
                        for (Vector2 p : currentWallPoints) w.points.add(p.cpy());
                        editorData.walls.add(w);
                        game.soundEngine.playClick();
                        showToast("✅ Duvar Eklendi.");
                    }
                    currentWallPoints.clear();
                } else {
                    currentWallPoints.add(new Vector2(x, y));
                    game.soundEngine.playClick();
                }
                break;

            case PIN:
                pushUndoState();
                editorData.pins.add(new EditorData.PinDef(x, y, selectedPinLength, selectedPinDir));
                game.soundEngine.playClick();
                break;

            case COLOR_BALL:
                pushUndoState();
                editorData.balls.add(new EditorData.BallDef(x, y, Ball.BallType.COLORED));
                game.soundEngine.playMarbleClink();
                break;

            case GRAY_BALL:
                pushUndoState();
                editorData.balls.add(new EditorData.BallDef(x, y, Ball.BallType.GRAY));
                game.soundEngine.playMarbleClink();
                break;

            case BOMB:
                pushUndoState();
                editorData.balls.add(new EditorData.BallDef(x, y, Ball.BallType.BOMB));
                game.soundEngine.playClick();
                break;

            case JAR:
                pushUndoState();
                editorData.jar.x = x;
                editorData.jar.y = y;
                game.soundEngine.playClick();
                break;

            case PORTAL:
                if (pendingPortalIn == null) {
                    pendingPortalIn = new Vector2(x, y);
                    game.soundEngine.playClick();
                    showToast("Mavi Giris belirlendi. Simdi Cikis noktasina dokunun.");
                } else {
                    pushUndoState();
                    editorData.portals.add(new EditorData.PortalDef(pendingPortalIn.x, pendingPortalIn.y, x, y));
                    pendingPortalIn = null;
                    game.soundEngine.playClick();
                    showToast("🌀 Portal Cifti Eklendi!");
                }
                break;

            case BOUNCER:
                pushUndoState();
                editorData.bouncers.add(new EditorData.BouncerDef(x, y, selectedBouncerAngle));
                game.soundEngine.playClick();
                break;

            case SPIKE:
                pushUndoState();
                editorData.spikes.add(new EditorData.SpikeDef(x, y));
                game.soundEngine.playClick();
                break;

            case DELETE:
                pushUndoState();
                deleteNearestObject(x, y);
                break;

            case SELECT:
                break;
        }
    }

    private void deleteNearestObject(float x, float y) {
        for (int i = editorData.balls.size - 1; i >= 0; i--) {
            if (Vector2.dst(editorData.balls.get(i).x, editorData.balls.get(i).y, x, y) < 0.45f) {
                editorData.balls.removeIndex(i);
                game.soundEngine.playBubblePop();
                return;
            }
        }
        for (int i = editorData.pins.size - 1; i >= 0; i--) {
            if (Vector2.dst(editorData.pins.get(i).x, editorData.pins.get(i).y, x, y) < 1.2f) {
                editorData.pins.removeIndex(i);
                game.soundEngine.playClick();
                return;
            }
        }
        for (int i = editorData.portals.size - 1; i >= 0; i--) {
            EditorData.PortalDef p = editorData.portals.get(i);
            if (Vector2.dst(p.inX, p.inY, x, y) < 0.6f || Vector2.dst(p.outX, p.outY, x, y) < 0.6f) {
                editorData.portals.removeIndex(i);
                game.soundEngine.playClick();
                return;
            }
        }
        for (int i = editorData.bouncers.size - 1; i >= 0; i--) {
            if (Vector2.dst(editorData.bouncers.get(i).x, editorData.bouncers.get(i).y, x, y) < 1.0f) {
                editorData.bouncers.removeIndex(i);
                game.soundEngine.playClick();
                return;
            }
        }
        for (int i = editorData.spikes.size - 1; i >= 0; i--) {
            if (Vector2.dst(editorData.spikes.get(i).x, editorData.spikes.get(i).y, x, y) < 1.0f) {
                editorData.spikes.removeIndex(i);
                game.soundEngine.playClick();
                return;
            }
        }
        for (int i = editorData.walls.size - 1; i >= 0; i--) {
            EditorData.WallDef w = editorData.walls.get(i);
            for (Vector2 pt : w.points) {
                if (pt.dst(x, y) < 0.6f) {
                    editorData.walls.removeIndex(i);
                    game.soundEngine.playClick();
                    return;
                }
            }
        }
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        mouseWorld.set(screenX, screenY, 0);
        worldViewport.unproject(mouseWorld);

        if (isPlaytesting && testSelectedPin != null) {
            testSelectedPin.onDrag(mouseWorld.x, mouseWorld.y);
            return true;
        }

        // Seçilen Nesneyi Taşıma (Drag & Drop Move)
        if (!isPlaytesting && activeTool == Tool.SELECT && isDraggingObject) {
            float moveX = mouseWorld.x;
            float moveY = mouseWorld.y;
            if (isGridSnap) {
                moveX = Math.round(moveX * 2f) / 2f;
                moveY = Math.round(moveY * 2f) / 2f;
            }

            switch (selectedType) {
                case PIN:
                    if (selectedIndex >= 0 && selectedIndex < editorData.pins.size) {
                        editorData.pins.get(selectedIndex).x = moveX;
                        editorData.pins.get(selectedIndex).y = moveY;
                    }
                    break;
                case BALL:
                    if (selectedIndex >= 0 && selectedIndex < editorData.balls.size) {
                        editorData.balls.get(selectedIndex).x = moveX;
                        editorData.balls.get(selectedIndex).y = moveY;
                    }
                    break;
                case JAR:
                    editorData.jar.x = moveX;
                    editorData.jar.y = moveY;
                    break;
                case PORTAL_IN:
                    if (selectedIndex >= 0 && selectedIndex < editorData.portals.size) {
                        editorData.portals.get(selectedIndex).inX = moveX;
                        editorData.portals.get(selectedIndex).inY = moveY;
                    }
                    break;
                case PORTAL_OUT:
                    if (selectedIndex >= 0 && selectedIndex < editorData.portals.size) {
                        editorData.portals.get(selectedIndex).outX = moveX;
                        editorData.portals.get(selectedIndex).outY = moveY;
                    }
                    break;
                case BOUNCER:
                    if (selectedIndex >= 0 && selectedIndex < editorData.bouncers.size) {
                        editorData.bouncers.get(selectedIndex).x = moveX;
                        editorData.bouncers.get(selectedIndex).y = moveY;
                    }
                    break;
                case SPIKE:
                    if (selectedIndex >= 0 && selectedIndex < editorData.spikes.size) {
                        editorData.spikes.get(selectedIndex).x = moveX;
                        editorData.spikes.get(selectedIndex).y = moveY;
                    }
                    break;
                case WALL_POINT:
                    if (selectedIndex >= 0 && selectedIndex < editorData.walls.size) {
                        EditorData.WallDef w = editorData.walls.get(selectedIndex);
                        if (selectedPointIndex >= 0 && selectedPointIndex < w.points.size) {
                            w.points.get(selectedPointIndex).set(moveX, moveY);
                        }
                    }
                    break;
            }
            return true;
        }

        // Fırça gibi sürükleyerek bilye ekleme
        if (!isPlaytesting && (activeTool == Tool.COLOR_BALL || activeTool == Tool.GRAY_BALL)) {
            touchUI.set(screenX, screenY, 0);
            uiViewport.unproject(touchUI);

            if (touchUI.y > 135 && touchUI.y < 1090) {
                float bx = mouseWorld.x;
                float by = mouseWorld.y;
                if (isGridSnap) {
                    bx = Math.round(bx * 2f) / 2f;
                    by = Math.round(by * 2f) / 2f;
                }

                boolean tooClose = false;
                for (EditorData.BallDef b : editorData.balls) {
                    if (Vector2.dst(b.x, b.y, bx, by) < 0.46f) {
                        tooClose = true;
                        break;
                    }
                }
                if (!tooClose) {
                    pushUndoState();
                    editorData.balls.add(new EditorData.BallDef(
                            bx, by,
                            activeTool == Tool.COLOR_BALL ? Ball.BallType.COLORED : Ball.BallType.GRAY
                    ));
                }
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if (isPlaytesting && testSelectedPin != null) {
            testSelectedPin.setSelected(false);
            testSelectedPin = null;
            return true;
        }
        isDraggingObject = false;
        return false;
    }

    @Override
    public void resize(int width, int height) {
        worldViewport.update(width, height, true);
        uiViewport.update(width, height, true);
    }

    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}

    @Override
    public void dispose() {
        if (testWorld != null) testWorld.dispose();
    }
}
