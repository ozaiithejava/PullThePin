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
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.game.pullthepin.PullThePinGame;
import com.game.pullthepin.haptics.HapticsService.HapticType;
import com.game.pullthepin.i18n.I18N;
import com.game.pullthepin.shop.SkinManager;
import com.game.pullthepin.shop.SkinManager.BallSkin;
import com.game.pullthepin.shop.SkinManager.PinSkin;
import com.game.pullthepin.shop.SkinManager.TrailSkin;

import java.util.ArrayList;
import java.util.List;

/**
 * AAA Seviyesinde Çok Sekmeli 2.5D Liquid Glassmorphism Mağazası.
 * - 3 Ana Kategori: [ BİLYELER ] [ PİMLER ] [ İZLER ]
 * - Bilye Alt Kategorileri: [ Temel ] [ Elementel ] [ Kozmik ] [ Prestij ]
 * - 2.5D Dinamik Canlı Önizlemeler (Bilye Küreleri, Metal Pimler, Hareketli Kuyruk Parçacıkları)
 * - Yumuşak Inercia Kaydırma, Sabit Üst Navigasyon ve Dokunmatik Haptikler
 */
public class ShopScreen extends InputAdapter implements Screen {

    public static final float UI_WIDTH  = 720f;
    public static final float UI_HEIGHT = 1280f;

    public enum MasterTab {
        BALLS,
        PINS,
        TRAILS
    }

    // Üst Navigasyon & Bar Ölçüleri
    private static final float HEADER_X       = 30f;
    private static final float HEADER_Y       = 1170f;
    private static final float HEADER_W       = 660f;
    private static final float HEADER_H       = 95f;

    // Ana Sekme Barı (Bilyeler | Pimler | İzler)
    private static final float MASTER_BAR_X   = 30f;
    private static final float MASTER_BAR_Y   = 1104f;
    private static final float MASTER_BAR_W   = 660f;
    private static final float MASTER_BAR_H   = 52f;
    private static final float MASTER_TAB_W   = 216f;
    private static final float MASTER_TAB_GAP = 6f;

    // Alt Kategori Barı (Yalnızca Bilyeler İçin)
    private static final float SUB_BAR_X      = 30f;
    private static final float SUB_BAR_Y      = 1044f;
    private static final float SUB_BAR_W      = 660f;
    private static final float SUB_BAR_H      = 48f;
    private static final float SUB_TAB_W      = 162f;
    private static final float SUB_TAB_GAP    = 4f;

    // Kart Izgarası Ölçüleri
    private static final float CONTENT_BOTTOM = 25f;
    private static final float CARD_W         = 310f;
    private static final float CARD_H         = 225f;
    private static final float CARD_GAP       = 20f;
    private static final float SIDE_PAD       = 40f;

    private PullThePinGame game;
    private OrthographicCamera camera;
    private FitViewport viewport;
    private GlyphLayout glyphLayout = new GlyphLayout();
    private Vector3 touchPos = new Vector3();

    private float globalTimer       = 0f;
    private float scrollOffset      = 0f;
    private float scrollVelocity    = 0f;
    private float lastTouchY        = 0f;
    private float totalDragDistance = 0f;
    private boolean isDragging      = false;
    private boolean pressedBack     = false;

    private MasterTab currentMasterTab = MasterTab.BALLS;
    private String selectedCategory    = "basic";

    // Toast Bildirimi
    private String toastMessage = "";
    private float  toastTimer   = 0f;

    // Bilye Kategorileri
    private static final String[] CAT_IDS = {"basic", "elemental", "cosmic", "prestige"};

    public ShopScreen(PullThePinGame game) {
        this.game = game;
        camera = new OrthographicCamera();
        viewport = new FitViewport(UI_WIDTH, UI_HEIGHT, camera);
        camera.position.set(UI_WIDTH / 2f, UI_HEIGHT / 2f, 0);
    }

    @Override public void show()   { Gdx.input.setInputProcessor(this); }
    @Override public void hide()   {}
    @Override public void pause()  {}
    @Override public void resume() {}
    @Override public void dispose() {}

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    private float getContentTop() {
        return (currentMasterTab == MasterTab.BALLS) ? 1028f : 1088f;
    }

    @Override
    public void render(float delta) {
        globalTimer += delta;
        if (toastTimer > 0) toastTimer -= delta;

        // Yumuşak kaydırma ataleti (Inertia scrolling)
        if (!isDragging) {
            scrollOffset += scrollVelocity * delta;
            scrollVelocity *= 0.88f;
            if (Math.abs(scrollVelocity) < 1f) scrollVelocity = 0f;
        }
        clampScroll();

        // 1. Zemin Temizliği (Derin Gece Mavisi)
        Gdx.gl.glClearColor(0.03f, 0.04f, 0.08f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        viewport.apply();
        camera.update();

        ShapeRenderer sr = game.shapeRenderer;
        sr.setProjectionMatrix(camera.combined);

        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        // =========================================================================
        // KATMAN 1: ARKA PLAN & AMBİYANS IŞILTI HALKALARI
        // =========================================================================
        sr.begin(ShapeRenderer.ShapeType.Filled);

        sr.rect(0, 0, UI_WIDTH, UI_HEIGHT,
                new Color(0.02f, 0.03f, 0.07f, 1f),
                new Color(0.02f, 0.03f, 0.07f, 1f),
                new Color(0.07f, 0.11f, 0.20f, 1f),
                new Color(0.07f, 0.11f, 0.20f, 1f));

        // Ambiyans Işık Küresi
        float glowPulse = 0.5f + 0.5f * (float) Math.sin(globalTimer * 1.8f);
        Color ambColor = (currentMasterTab == MasterTab.BALLS) ? new Color(0.12f, 0.35f, 0.65f, 0.10f + glowPulse * 0.05f) :
                         (currentMasterTab == MasterTab.PINS)  ? new Color(0.70f, 0.45f, 0.10f, 0.10f + glowPulse * 0.05f) :
                                                                 new Color(0.55f, 0.20f, 0.70f, 0.10f + glowPulse * 0.05f);
        sr.setColor(ambColor);
        sr.circle(UI_WIDTH / 2f, UI_HEIGHT * 0.52f, 320f, 36);

        // =========================================================================
        // KATMAN 2: SCROLL EDİLEBİLİR KARTLAR (SHAPE)
        // =========================================================================
        if (currentMasterTab == MasterTab.BALLS) {
            renderBallCards(sr);
        } else if (currentMasterTab == MasterTab.PINS) {
            renderPinCards(sr);
        } else {
            renderTrailCards(sr);
        }

        sr.end();

        // =========================================================================
        // KATMAN 3: KARTLARIN TİPOGRAFİSİ (BATCH)
        // =========================================================================
        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();
        if (currentMasterTab == MasterTab.BALLS) {
            renderBallCardTexts();
        } else if (currentMasterTab == MasterTab.PINS) {
            renderPinCardTexts();
        } else {
            renderTrailCardTexts();
        }
        game.batch.end();

        // =========================================================================
        // KATMAN 4: SABİT ÜST NAVİGASYON VE SEKME BARLARI (SHAPE)
        // =========================================================================
        sr.begin(ShapeRenderer.ShapeType.Filled);

        float barBottomY = (currentMasterTab == MasterTab.BALLS) ? SUB_BAR_Y : MASTER_BAR_Y;

        // Sekmeler altındaki derinlik gölgesi
        sr.rect(0, barBottomY - 20f, UI_WIDTH, 20f,
                new Color(0f, 0f, 0f, 0f),
                new Color(0f, 0f, 0f, 0f),
                new Color(0f, 0f, 0f, 0.45f),
                new Color(0f, 0f, 0f, 0.45f));

        // Sabit Üst Arka Plan Bloklaması
        sr.setColor(0.03f, 0.04f, 0.08f, 0.98f);
        sr.rect(0, barBottomY, UI_WIDTH, UI_HEIGHT - barBottomY);

        // 1. ÜST NAVİGASYON BAŞLIK KARTI
        drawRoundedBox(sr, HEADER_X, HEADER_Y - 5f, HEADER_W, HEADER_H, 22f, new Color(0f, 0f, 0f, 0.45f));
        drawRoundedBox(sr, HEADER_X, HEADER_Y, HEADER_W, HEADER_H, 22f, new Color(0.08f, 0.12f, 0.24f, 0.98f));
        // Üst İnce Altın Vurgu Çizgisi
        sr.setColor(1.00f, 0.84f, 0.28f, 0.85f);
        sr.rect(HEADER_X + 25f, HEADER_Y + HEADER_H - 3f, HEADER_W - 50f, 2.5f);

        // 1A. GERİ BUTONU (<)
        float backX = HEADER_X + 15f;
        float backY = HEADER_Y + (HEADER_H - 56f) / 2f;
        renderModernButton(sr, backX, backY, 56f, 56f, 16f,
                new Color(0.18f, 0.26f, 0.44f, 1.0f),
                new Color(0.10f, 0.14f, 0.25f, 1.0f),
                new Color(0.55f, 0.75f, 1.00f, 0.50f),
                pressedBack);

        // 1B. SAĞ ÜST: CANLI ALTIN KAPSÜLÜ (DEBUG: Dokununca +10.000 Altın)
        float coinW = 180f, coinH = 48f;
        float coinX = HEADER_X + HEADER_W - coinW - 15f;
        float coinY = HEADER_Y + (HEADER_H - coinH) / 2f;
        drawRoundedBox(sr, coinX, coinY - 3f, coinW, coinH, 24f, new Color(0f, 0f, 0f, 0.35f));
        drawRoundedBox(sr, coinX, coinY, coinW, coinH, 24f, new Color(0.10f, 0.15f, 0.28f, 0.98f));
        sr.setColor(1.0f, 0.84f, 0.22f, 0.80f);
        sr.rect(coinX + 16f, coinY + coinH - 2.5f, coinW - 32f, 2f);
        drawCoinIcon(sr, coinX + 22f, coinY + coinH / 2f, 13f);
        drawRoundedBox(sr, coinX + coinW - 28f, coinY + (coinH - 24f) / 2f, 22f, 24f, 8f, new Color(0.16f, 0.78f, 0.42f, 0.95f));

        // 2. ANA KATEGORİ SEKMELERİ [ BİLYELER ] [ PİMLER ] [ İZLER ]
        drawRoundedBox(sr, MASTER_BAR_X, MASTER_BAR_Y - 4f, MASTER_BAR_W, MASTER_BAR_H, 22f, new Color(0f, 0f, 0f, 0.40f));
        drawRoundedBox(sr, MASTER_BAR_X, MASTER_BAR_Y, MASTER_BAR_W, MASTER_BAR_H, 22f, new Color(0.06f, 0.09f, 0.18f, 0.98f));
        sr.setColor(0.25f, 0.40f, 0.65f, 0.40f);
        sr.rect(MASTER_BAR_X + 20f, MASTER_BAR_Y + MASTER_BAR_H - 2.5f, MASTER_BAR_W - 40f, 2f);

        MasterTab[] allMaster = MasterTab.values();
        for (int i = 0; i < allMaster.length; i++) {
            boolean active = (allMaster[i] == currentMasterTab);
            float mtx = MASTER_BAR_X + 4f + i * (MASTER_TAB_W + MASTER_TAB_GAP);
            if (active) {
                Color mTop, mBot, mRim;
                if (allMaster[i] == MasterTab.BALLS) {
                    mTop = new Color(0.20f, 0.62f, 0.98f, 1f);
                    mBot = new Color(0.10f, 0.35f, 0.65f, 1f);
                    mRim = new Color(0.65f, 0.88f, 1.00f, 0.70f);
                } else if (allMaster[i] == MasterTab.PINS) {
                    mTop = new Color(1.00f, 0.78f, 0.18f, 1f);
                    mBot = new Color(0.65f, 0.45f, 0.05f, 1f);
                    mRim = new Color(1.00f, 0.95f, 0.65f, 0.80f);
                } else {
                    mTop = new Color(0.85f, 0.32f, 0.95f, 1f);
                    mBot = new Color(0.50f, 0.12f, 0.65f, 1f);
                    mRim = new Color(0.95f, 0.70f, 1.00f, 0.75f);
                }
                renderModernButton(sr, mtx, MASTER_BAR_Y + 4f, MASTER_TAB_W, MASTER_BAR_H - 8f, 16f, mTop, mBot, mRim, false);
            }
        }

        // 3. ALT KATEGORİ SEGMENTED CONTROL BARI (Sadece Bilyeler İçin)
        if (currentMasterTab == MasterTab.BALLS) {
            drawRoundedBox(sr, SUB_BAR_X, SUB_BAR_Y - 4f, SUB_BAR_W, SUB_BAR_H, 20f, new Color(0f, 0f, 0f, 0.40f));
            drawRoundedBox(sr, SUB_BAR_X, SUB_BAR_Y, SUB_BAR_W, SUB_BAR_H, 20f, new Color(0.05f, 0.08f, 0.16f, 0.98f));
            sr.setColor(0.25f, 0.40f, 0.65f, 0.40f);
            sr.rect(SUB_BAR_X + 20f, SUB_BAR_Y + SUB_BAR_H - 2.5f, SUB_BAR_W - 40f, 2f);

            float startSubX = SUB_BAR_X + 4f;
            for (int i = 0; i < CAT_IDS.length; i++) {
                boolean active = CAT_IDS[i].equals(selectedCategory);
                float tx = startSubX + i * (SUB_TAB_W + SUB_TAB_GAP);
                if (active) {
                    Color tabTop, tabBot, tabRim;
                    if ("prestige".equals(selectedCategory)) {
                        tabTop = new Color(1.00f, 0.80f, 0.20f, 1f);
                        tabBot = new Color(0.68f, 0.45f, 0.05f, 1f);
                        tabRim = new Color(1.00f, 0.95f, 0.65f, 0.80f);
                    } else if ("cosmic".equals(selectedCategory)) {
                        tabTop = new Color(0.70f, 0.32f, 0.95f, 1f);
                        tabBot = new Color(0.40f, 0.12f, 0.62f, 1f);
                        tabRim = new Color(0.90f, 0.70f, 1.00f, 0.70f);
                    } else if ("elemental".equals(selectedCategory)) {
                        tabTop = new Color(1.00f, 0.45f, 0.12f, 1f);
                        tabBot = new Color(0.65f, 0.22f, 0.04f, 1f);
                        tabRim = new Color(1.00f, 0.85f, 0.50f, 0.70f);
                    } else {
                        tabTop = new Color(0.20f, 0.62f, 0.98f, 1f);
                        tabBot = new Color(0.10f, 0.35f, 0.65f, 1f);
                        tabRim = new Color(0.65f, 0.88f, 1.00f, 0.70f);
                    }
                    renderModernButton(sr, tx, SUB_BAR_Y + 4f, SUB_TAB_W, SUB_BAR_H - 8f, 14f, tabTop, tabBot, tabRim, false);
                }
            }
        }

        // 4. TOAST BİLDİRİM KARTI
        if (toastTimer > 0) {
            float ta = Math.min(1f, toastTimer * 2f);
            drawRoundedBox(sr, 60f, 790f, 600f, 64f, 18f, new Color(0.08f, 0.12f, 0.24f, 0.98f * ta));
            sr.setColor(1.0f, 0.82f, 0.22f, ta);
            sr.rect(75f, 790f, 570f, 2.5f);
        }

        sr.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);

        // =========================================================================
        // KATMAN 5: SABİT ÜST METİNLER (BATCH)
        // =========================================================================
        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();

        // Geri Oku (<)
        drawButtonText(game.regularFont, "<", backX, backY, 56f, 56f, Color.WHITE);

        // Başlık & Alt Başlık
        boolean tr = I18N.isTurkish();
        String titleStr = (currentMasterTab == MasterTab.BALLS) ? (tr ? "BILYE MAGAZASI" : "BALL SHOP") :
                          (currentMasterTab == MasterTab.PINS)  ? (tr ? "PIM MAGAZASI"   : "PIN SHOP")  :
                                                                  (tr ? "IZ MAGAZASI"    : "TRAIL SHOP");
        float titleX = HEADER_X + 85f;
        drawShadowText(game.regularFont, titleStr, titleX, HEADER_Y + 68f, new Color(1.00f, 0.86f, 0.32f, 1f));

        String subStr = (currentMasterTab == MasterTab.BALLS) ? (tr ? "[ 12 ESSIZ BILYE ]"  : "[ 12 UNIQUE BALLS ]") :
                        (currentMasterTab == MasterTab.PINS)  ? (tr ? "[ 6 OZEL PIM MODELI ]": "[ 6 CUSTOM PINS ]")   :
                                                                (tr ? "[ 5 PARILTI IZI ]"    : "[ 5 GLOW TRAILS ]");
        drawShadowText(game.smallFont, subStr, titleX, HEADER_Y + 36f, new Color(0.65f, 0.82f, 1.0f, 0.90f));

        // Üst Sağ Altın Metni & Debug '+' Rozeti
        int coins = game.saveManager.getCoins();
        String coinStr = String.valueOf(coins);
        glyphLayout.setText(game.smallFont, coinStr);
        float coinTextX = coinX + 38f + (coinW - 70f - glyphLayout.width) / 2f;
        float coinTextY = coinY + (coinH + glyphLayout.height) / 2f;
        drawShadowText(game.smallFont, coinStr, coinTextX, coinTextY, new Color(1.0f, 0.88f, 0.35f, 1.0f));
        drawButtonText(game.smallFont, "+", coinX + coinW - 28f, coinY + (coinH - 24f) / 2f, 22f, 24f, Color.WHITE);

        // Ana Sekme Metinleri
        String[] masterLabels = {tr ? "BILYELER" : "BALLS", tr ? "PIMLER" : "PINS", tr ? "IZLER" : "TRAILS"};
        for (int i = 0; i < allMaster.length; i++) {
            boolean active = (allMaster[i] == currentMasterTab);
            float mtx = MASTER_BAR_X + 4f + i * (MASTER_TAB_W + MASTER_TAB_GAP);
            Color labelCol = active ? Color.WHITE : new Color(0.65f, 0.75f, 0.90f, 0.85f);
            drawButtonText(game.smallFont, masterLabels[i], mtx, MASTER_BAR_Y + 4f, MASTER_TAB_W, MASTER_BAR_H - 8f, labelCol);
        }

        // Bilyeler Alt Kategori Etiketleri
        if (currentMasterTab == MasterTab.BALLS) {
            float startSubX = SUB_BAR_X + 4f;
            for (int i = 0; i < CAT_IDS.length; i++) {
                boolean active = CAT_IDS[i].equals(selectedCategory);
                float tx = startSubX + i * (SUB_TAB_W + SUB_TAB_GAP);
                String label = getCategoryLabel(CAT_IDS[i]);
                Color labelCol = active ? Color.WHITE : new Color(0.65f, 0.75f, 0.90f, 0.85f);
                drawButtonText(game.smallFont, label, tx, SUB_BAR_Y + 4f, SUB_TAB_W, SUB_BAR_H - 8f, labelCol);
            }
        }

        // Toast Metni
        if (toastTimer > 0) {
            glyphLayout.setText(game.smallFont, toastMessage);
            float tx = (UI_WIDTH - glyphLayout.width) / 2f;
            drawShadowText(game.smallFont, toastMessage, tx, 830f, Color.WHITE);
        }

        game.batch.end();
    }

    private String getCategoryLabel(String catId) {
        boolean tr = I18N.isTurkish();
        if ("basic".equals(catId)) return tr ? "TEMEL" : "BASIC";
        if ("elemental".equals(catId)) return tr ? "ELEMENT" : "ELEMENT";
        if ("cosmic".equals(catId)) return tr ? "KOZMIK" : "COSMIC";
        if ("prestige".equals(catId)) return tr ? "PRESTIJ" : "PRESTIGE";
        return catId.toUpperCase();
    }

    private float contentHeight(int count) {
        int rows = (count + 1) / 2;
        return rows * CARD_H + (rows - 1) * CARD_GAP + 40f;
    }

    private float cardScreenY(int row) {
        float firstCardTop = getContentTop() - 15f;
        return firstCardTop - row * (CARD_H + CARD_GAP) - CARD_H + scrollOffset;
    }

    // ──────────────────────────────────────────────────────────────────────────
    // 1. BİLYE KARTLARI (BALL CARDS)
    // ──────────────────────────────────────────────────────────────────────────
    private List<BallSkin> getSkinsForCategory() {
        List<BallSkin> list = new ArrayList<BallSkin>();
        for (BallSkin s : BallSkin.values()) {
            if (s.category.equals(selectedCategory)) list.add(s);
        }
        return list;
    }

    private void renderBallCards(ShapeRenderer sr) {
        List<BallSkin> skins = getSkinsForCategory();
        int count = skins.size();
        SkinManager sm = game.skinManager;
        BallSkin equipped = sm.getEquippedSkin();

        for (int i = 0; i < count; i++) {
            BallSkin skin = skins.get(i);
            int col = i % 2;
            int row = i / 2;

            float cardX = SIDE_PAD + col * (CARD_W + CARD_GAP);
            float cardY = cardScreenY(row);

            if (cardY + CARD_H < CONTENT_BOTTOM - 100f || cardY > getContentTop() + 100f) continue;

            boolean isEquipped = skin.equals(equipped);
            boolean isOwned    = sm.isSkinOwned(skin);

            // Dış Gölge
            drawRoundedBox(sr, cardX + 3f, cardY - 6f, CARD_W, CARD_H, 18f, new Color(0f, 0f, 0f, 0.40f));

            // Kuşanılmışsa Altın Parıltı Çerçevesi
            if (isEquipped) {
                float pulse = 0.5f + 0.5f * (float) Math.sin(globalTimer * 3.5f);
                drawRoundedBox(sr, cardX - 3.5f, cardY - 3.5f, CARD_W + 7f, CARD_H + 7f, 21.5f,
                        new Color(1.0f, 0.84f, 0.22f, 0.40f + pulse * 0.35f));
            }

            // Kart Ana Gövdesi
            drawRoundedBox(sr, cardX, cardY, CARD_W, CARD_H, 18f, new Color(0.06f, 0.10f, 0.20f, 0.98f));

            // Üst Vurgu Çizgisi
            sr.setColor(skin.primary.r, skin.primary.g, skin.primary.b, 0.75f);
            sr.rect(cardX + 24f, cardY + CARD_H - 3f, CARD_W - 48f, 2.5f);

            // 3D Bilye Önizleme
            float ballCX = cardX + 80f;
            float ballCY = cardY + 138f;

            if (skin == BallSkin.GOLD) {
                float haloPulse = 0.5f + 0.5f * (float) Math.sin(globalTimer * 4f);
                sr.setColor(1.0f, 0.85f, 0.22f, 0.28f + haloPulse * 0.18f);
                sr.circle(ballCX, ballCY, 58f, 28);
            } else if (skin == BallSkin.FIRE) {
                float firePulse = 0.5f + 0.5f * (float) Math.sin(globalTimer * 6f);
                sr.setColor(1.0f, 0.35f, 0.05f, 0.30f + firePulse * 0.20f);
                sr.circle(ballCX, ballCY, 56f, 28);
            } else if (skin == BallSkin.DIAMOND) {
                sr.setColor(0.85f, 0.98f, 1.0f, 0.38f);
                sr.circle(ballCX, ballCY, 58f, 28);
            } else if (skin == BallSkin.BLACKHOLE) {
                sr.setColor(0.50f, 0.15f, 0.75f, 0.40f);
                sr.circle(ballCX, ballCY, 56f, 28);
            } else {
                sr.setColor(skin.primary.r, skin.primary.g, skin.primary.b, 0.15f);
                sr.circle(ballCX, ballCY, 52f, 24);
            }

            sr.setColor(0f, 0f, 0f, 0.38f);
            sr.circle(ballCX + 4f, ballCY - 5f, 44f, 24);

            sr.setColor(skin.secondary);
            sr.circle(ballCX, ballCY, 42f, 28);

            sr.setColor(skin.primary);
            sr.circle(ballCX - 2f, ballCY + 2f, 36f, 28);

            sr.setColor(skin.specular);
            sr.circle(ballCX - 12f, ballCY + 12f, 14f, 18);
            sr.setColor(1f, 1f, 1f, 0.95f);
            sr.circle(ballCX - 14f, ballCY + 14f, 5f, 12);

            // Eylem Butonu
            float btnX = cardX + 12f;
            float btnY = cardY + 12f;
            float btnW = CARD_W - 24f;
            float btnH = 46f;

            Color btnTop, btnBot, btnRim;
            if (isEquipped) {
                btnTop = new Color(0.95f, 0.75f, 0.12f, 1f);
                btnBot = new Color(0.60f, 0.40f, 0.04f, 1f);
                btnRim = new Color(1.0f, 0.95f, 0.65f, 0.65f);
            } else if (isOwned) {
                btnTop = new Color(0.16f, 0.82f, 0.50f, 1f);
                btnBot = new Color(0.08f, 0.45f, 0.25f, 1f);
                btnRim = new Color(0.60f, 1.0f, 0.80f, 0.55f);
            } else {
                btnTop = new Color(0.20f, 0.60f, 0.98f, 1f);
                btnBot = new Color(0.10f, 0.32f, 0.65f, 1f);
                btnRim = new Color(0.65f, 0.88f, 1.0f, 0.55f);
            }
            renderModernButton(sr, btnX, btnY, btnW, btnH, 12f, btnTop, btnBot, btnRim, false);
        }
    }

    private void renderBallCardTexts() {
        List<BallSkin> skins = getSkinsForCategory();
        int count = skins.size();
        SkinManager sm = game.skinManager;
        BallSkin equipped = sm.getEquippedSkin();

        for (int i = 0; i < count; i++) {
            BallSkin skin = skins.get(i);
            int col = i % 2;
            int row = i / 2;

            float cardX = SIDE_PAD + col * (CARD_W + CARD_GAP);
            float cardY = cardScreenY(row);

            if (cardY + CARD_H < CONTENT_BOTTOM - 100f || cardY > getContentTop() + 100f) continue;

            boolean isEquipped = skin.equals(equipped);
            boolean isOwned    = sm.isSkinOwned(skin);

            float nameX = cardX + 145f;
            float nameY = cardY + CARD_H - 26f;
            String displayName = I18N.isTurkish() ? skin.nameTR : skin.nameEN;
            drawShadowText(game.regularFont, displayName, nameX, nameY, Color.WHITE);

            String statusText;
            Color statusColor;
            if (isEquipped) {
                statusText  = I18N.isTurkish() ? "KUSANILDI" : "EQUIPPED";
                statusColor = new Color(1.0f, 0.84f, 0.22f, 1f);
            } else if (isOwned) {
                statusText  = I18N.isTurkish() ? "SAHIP" : "OWNED";
                statusColor = new Color(0.30f, 0.95f, 0.60f, 1f);
            } else {
                statusText  = skin.price + (I18N.isTurkish() ? " ALTIN" : " COINS");
                statusColor = new Color(1.0f, 0.85f, 0.35f, 1f);
            }
            drawShadowText(game.smallFont, statusText, nameX, nameY - 32f, statusColor);

            float btnX = cardX + 12f;
            float btnY = cardY + 12f;
            float btnW = CARD_W - 24f;
            float btnH = 46f;

            String btnLabel;
            if (isEquipped) {
                btnLabel = I18N.isTurkish() ? "KUSANILDI" : "EQUIPPED";
            } else if (isOwned) {
                btnLabel = I18N.isTurkish() ? "KUSAN" : "EQUIP";
            } else {
                btnLabel = (I18N.isTurkish() ? "SATIN AL: " : "BUY: ") + skin.price;
            }
            drawButtonText(game.smallFont, btnLabel, btnX, btnY, btnW, btnH, Color.WHITE);
        }
    }

    // ──────────────────────────────────────────────────────────────────────────
    // 2. PİM KARTLARI (PIN CARDS)
    // ──────────────────────────────────────────────────────────────────────────
    private void renderPinCards(ShapeRenderer sr) {
        PinSkin[] skins = PinSkin.values();
        int count = skins.length;
        SkinManager sm = game.skinManager;
        PinSkin equipped = sm.getEquippedPinSkin();

        for (int i = 0; i < count; i++) {
            PinSkin skin = skins[i];
            int col = i % 2;
            int row = i / 2;

            float cardX = SIDE_PAD + col * (CARD_W + CARD_GAP);
            float cardY = cardScreenY(row);

            if (cardY + CARD_H < CONTENT_BOTTOM - 100f || cardY > getContentTop() + 100f) continue;

            boolean isEquipped = skin.equals(equipped);
            boolean isOwned    = sm.isPinSkinOwned(skin);

            // Dış Gölge
            drawRoundedBox(sr, cardX + 3f, cardY - 6f, CARD_W, CARD_H, 18f, new Color(0f, 0f, 0f, 0.40f));

            // Kuşanılmışsa Altın Parıltı Çerçevesi
            if (isEquipped) {
                float pulse = 0.5f + 0.5f * (float) Math.sin(globalTimer * 3.5f);
                drawRoundedBox(sr, cardX - 3.5f, cardY - 3.5f, CARD_W + 7f, CARD_H + 7f, 21.5f,
                        new Color(1.0f, 0.84f, 0.22f, 0.40f + pulse * 0.35f));
            }

            // Kart Ana Gövdesi
            drawRoundedBox(sr, cardX, cardY, CARD_W, CARD_H, 18f, new Color(0.06f, 0.10f, 0.20f, 0.98f));

            // Üst Vurgu Çizgisi
            sr.setColor(skin.shaftBase.r, skin.shaftBase.g, skin.shaftBase.b, 0.80f);
            sr.rect(cardX + 24f, cardY + CARD_H - 3f, CARD_W - 48f, 2.5f);

            // ── 2.5D PİM ÖNİZLEMESİ ──
            float pinCX = cardX + 75f;
            float pinCY = cardY + 138f;

            // Arka Işıma Küresi
            sr.setColor(skin.sparkColor.r, skin.sparkColor.g, skin.sparkColor.b, 0.18f);
            sr.circle(pinCX, pinCY, 52f, 24);

            float shaftLen = 78f;
            float shaftThick = 15f;
            float shaftLeft = pinCX - 40f;
            float shaftBottom = pinCY - shaftThick / 2f;

            // Metalik Gövde Gölgesi
            sr.setColor(0f, 0f, 0f, 0.35f);
            sr.rect(shaftLeft + 3f, shaftBottom - 4f, shaftLen, shaftThick);

            // Metal Gövde Koyu Taban
            sr.setColor(skin.shaftDark);
            sr.rect(shaftLeft, shaftBottom, shaftLen, shaftThick);

            // Metal Gövde Birincil Renk
            sr.setColor(skin.shaftBase);
            sr.rect(shaftLeft, shaftBottom + shaftThick * 0.20f, shaftLen, shaftThick * 0.65f);

            // Metal Gövde Aynasal Parlaklık
            sr.setColor(skin.shaftLight);
            sr.rect(shaftLeft, shaftBottom + shaftThick * 0.55f, shaftLen, shaftThick * 0.25f);

            // Yuvarlak Sol Uç
            sr.setColor(skin.shaftBase);
            sr.circle(shaftLeft, pinCY, shaftThick / 2f, 16);
            sr.setColor(skin.shaftLight);
            sr.circle(shaftLeft, pinCY + 2f, shaftThick * 0.25f, 10);

            // Taktil Metal Çizgileri
            sr.setColor(skin.shaftDark);
            for (float gx = shaftLeft + 15f; gx <= shaftLeft + 55f; gx += 12f) {
                sr.rect(gx, shaftBottom, 2f, shaftThick);
            }

            // Sağ Çekme Halkası (Jeweled Eyelet Handle)
            float handleX = shaftLeft + shaftLen + 6f;
            float handleY = pinCY;
            float handleR = 19f;

            // Halka Gölgesi
            sr.setColor(0f, 0f, 0f, 0.35f);
            sr.circle(handleX + 3f, handleY - 3f, handleR, 20);

            // Halka Dış Kabuk
            sr.setColor(skin.handleOuter);
            sr.circle(handleX, handleY, handleR, 22);

            // Halka İç Parıltı
            sr.setColor(skin.handleInner);
            sr.circle(handleX, handleY, handleR * 0.65f, 18);

            // Halka Merkezi (Cam Delik)
            sr.setColor(0.06f, 0.10f, 0.20f, 1f);
            sr.circle(handleX, handleY, handleR * 0.35f, 16);

            // Kıvılcım Yıldızı (Pin Ucunda)
            float sparkPulse = 0.5f + 0.5f * (float) Math.sin(globalTimer * 6f + i);
            sr.setColor(skin.sparkColor.r, skin.sparkColor.g, skin.sparkColor.b, 0.65f + sparkPulse * 0.35f);
            sr.circle(shaftLeft - 2f, pinCY, 4f + sparkPulse * 2f, 10);

            // Eylem Butonu
            float btnX = cardX + 12f;
            float btnY = cardY + 12f;
            float btnW = CARD_W - 24f;
            float btnH = 46f;

            Color btnTop, btnBot, btnRim;
            if (isEquipped) {
                btnTop = new Color(0.95f, 0.75f, 0.12f, 1f);
                btnBot = new Color(0.60f, 0.40f, 0.04f, 1f);
                btnRim = new Color(1.0f, 0.95f, 0.65f, 0.65f);
            } else if (isOwned) {
                btnTop = new Color(0.16f, 0.82f, 0.50f, 1f);
                btnBot = new Color(0.08f, 0.45f, 0.25f, 1f);
                btnRim = new Color(0.60f, 1.0f, 0.80f, 0.55f);
            } else {
                btnTop = new Color(0.20f, 0.60f, 0.98f, 1f);
                btnBot = new Color(0.10f, 0.32f, 0.65f, 1f);
                btnRim = new Color(0.65f, 0.88f, 1.0f, 0.55f);
            }
            renderModernButton(sr, btnX, btnY, btnW, btnH, 12f, btnTop, btnBot, btnRim, false);
        }
    }

    private void renderPinCardTexts() {
        PinSkin[] skins = PinSkin.values();
        int count = skins.length;
        SkinManager sm = game.skinManager;
        PinSkin equipped = sm.getEquippedPinSkin();

        for (int i = 0; i < count; i++) {
            PinSkin skin = skins[i];
            int col = i % 2;
            int row = i / 2;

            float cardX = SIDE_PAD + col * (CARD_W + CARD_GAP);
            float cardY = cardScreenY(row);

            if (cardY + CARD_H < CONTENT_BOTTOM - 100f || cardY > getContentTop() + 100f) continue;

            boolean isEquipped = skin.equals(equipped);
            boolean isOwned    = sm.isPinSkinOwned(skin);

            float nameX = cardX + 145f;
            float nameY = cardY + CARD_H - 26f;
            String displayName = I18N.isTurkish() ? skin.nameTR : skin.nameEN;
            drawShadowText(game.regularFont, displayName, nameX, nameY, Color.WHITE);

            String statusText;
            Color statusColor;
            if (isEquipped) {
                statusText  = I18N.isTurkish() ? "KUSANILDI" : "EQUIPPED";
                statusColor = new Color(1.0f, 0.84f, 0.22f, 1f);
            } else if (isOwned) {
                statusText  = I18N.isTurkish() ? "SAHIP" : "OWNED";
                statusColor = new Color(0.30f, 0.95f, 0.60f, 1f);
            } else {
                statusText  = skin.price + (I18N.isTurkish() ? " ALTIN" : " COINS");
                statusColor = new Color(1.0f, 0.85f, 0.35f, 1f);
            }
            drawShadowText(game.smallFont, statusText, nameX, nameY - 32f, statusColor);

            float btnX = cardX + 12f;
            float btnY = cardY + 12f;
            float btnW = CARD_W - 24f;
            float btnH = 46f;

            String btnLabel;
            if (isEquipped) {
                btnLabel = I18N.isTurkish() ? "KUSANILDI" : "EQUIPPED";
            } else if (isOwned) {
                btnLabel = I18N.isTurkish() ? "KUSAN" : "EQUIP";
            } else {
                btnLabel = (I18N.isTurkish() ? "SATIN AL: " : "BUY: ") + skin.price;
            }
            drawButtonText(game.smallFont, btnLabel, btnX, btnY, btnW, btnH, Color.WHITE);
        }
    }

    // ──────────────────────────────────────────────────────────────────────────
    // 3. KUYRUK İZİ KARTLARI (TRAIL CARDS)
    // ──────────────────────────────────────────────────────────────────────────
    private void renderTrailCards(ShapeRenderer sr) {
        TrailSkin[] skins = TrailSkin.values();
        int count = skins.length;
        SkinManager sm = game.skinManager;
        TrailSkin equipped = sm.getEquippedTrailSkin();

        for (int i = 0; i < count; i++) {
            TrailSkin skin = skins[i];
            int col = i % 2;
            int row = i / 2;

            float cardX = SIDE_PAD + col * (CARD_W + CARD_GAP);
            float cardY = cardScreenY(row);

            if (cardY + CARD_H < CONTENT_BOTTOM - 100f || cardY > getContentTop() + 100f) continue;

            boolean isEquipped = skin.equals(equipped);
            boolean isOwned    = sm.isTrailSkinOwned(skin);

            // Dış Gölge
            drawRoundedBox(sr, cardX + 3f, cardY - 6f, CARD_W, CARD_H, 18f, new Color(0f, 0f, 0f, 0.40f));

            // Kuşanılmışsa Altın Parıltı Çerçevesi
            if (isEquipped) {
                float pulse = 0.5f + 0.5f * (float) Math.sin(globalTimer * 3.5f);
                drawRoundedBox(sr, cardX - 3.5f, cardY - 3.5f, CARD_W + 7f, CARD_H + 7f, 21.5f,
                        new Color(1.0f, 0.84f, 0.22f, 0.40f + pulse * 0.35f));
            }

            // Kart Ana Gövdesi
            drawRoundedBox(sr, cardX, cardY, CARD_W, CARD_H, 18f, new Color(0.06f, 0.10f, 0.20f, 0.98f));

            // Üst Vurgu Çizgisi
            sr.setColor(skin.primary.r, skin.primary.g, skin.primary.b, 0.80f);
            sr.rect(cardX + 24f, cardY + CARD_H - 3f, CARD_W - 48f, 2.5f);

            // ── DİNAMİK KUYRUK İZİ ÖNİZLEMESİ ──
            float trailCX = cardX + 75f;
            float trailCY = cardY + 138f;

            // Arka Işıma Küresi
            sr.setColor(skin.primary.r, skin.primary.g, skin.primary.b, 0.16f);
            sr.circle(trailCX, trailCY, 54f, 24);

            // Sağa sola salınan mini bilye simülasyonu
            float t = globalTimer * 2.8f + i * 1.5f;
            float swing = (float) Math.sin(t);
            float velX = (float) Math.cos(t);
            float leadX = trailCX + swing * 26f;
            float leadY = trailCY;

            // Kuyruk parçacıkları (ters yönde süzülen 5 nokta)
            for (int p = 1; p <= 5; p++) {
                float factor = (float) (6 - p) / 5f;
                float px = leadX - velX * p * 9f;
                float py = leadY + (float) Math.sin(t - p * 0.35f) * 4f;

                if (skin == TrailSkin.RAINBOW) {
                    float hue = (globalTimer * 2.5f + (float) p / 5f) % 1.0f;
                    float r = 0.5f + 0.5f * MathUtils.sin(hue * 6.28f);
                    float g = 0.5f + 0.5f * MathUtils.sin(hue * 6.28f + 2.09f);
                    float b = 0.5f + 0.5f * MathUtils.sin(hue * 6.28f + 4.18f);
                    sr.setColor(r, g, b, 0.70f * factor);
                    sr.circle(px, py, 6f * factor + 2f, 10);
                } else {
                    // Dış parıltı
                    sr.setColor(skin.glow.r, skin.glow.g, skin.glow.b, skin.glow.a * factor * 0.60f);
                    sr.circle(px, py, 9f * factor + 3f, 12);
                    // İç çekirdek
                    sr.setColor(skin.primary.r, skin.primary.g, skin.primary.b, skin.primary.a * factor * 0.90f);
                    sr.circle(px, py, 5f * factor + 2f, 10);
                }
            }

            // Öncü Kristal Bilye (Leading Orb)
            sr.setColor(0f, 0f, 0f, 0.35f);
            sr.circle(leadX + 2f, leadY - 3f, 16f, 18);
            sr.setColor(skin.primary);
            sr.circle(leadX, leadY, 15f, 20);
            sr.setColor(1f, 1f, 1f, 0.85f);
            sr.circle(leadX - 4f, leadY + 4f, 5f, 10);

            // Eylem Butonu
            float btnX = cardX + 12f;
            float btnY = cardY + 12f;
            float btnW = CARD_W - 24f;
            float btnH = 46f;

            Color btnTop, btnBot, btnRim;
            if (isEquipped) {
                btnTop = new Color(0.95f, 0.75f, 0.12f, 1f);
                btnBot = new Color(0.60f, 0.40f, 0.04f, 1f);
                btnRim = new Color(1.0f, 0.95f, 0.65f, 0.65f);
            } else if (isOwned) {
                btnTop = new Color(0.16f, 0.82f, 0.50f, 1f);
                btnBot = new Color(0.08f, 0.45f, 0.25f, 1f);
                btnRim = new Color(0.60f, 1.0f, 0.80f, 0.55f);
            } else {
                btnTop = new Color(0.20f, 0.60f, 0.98f, 1f);
                btnBot = new Color(0.10f, 0.32f, 0.65f, 1f);
                btnRim = new Color(0.65f, 0.88f, 1.0f, 0.55f);
            }
            renderModernButton(sr, btnX, btnY, btnW, btnH, 12f, btnTop, btnBot, btnRim, false);
        }
    }

    private void renderTrailCardTexts() {
        TrailSkin[] skins = TrailSkin.values();
        int count = skins.length;
        SkinManager sm = game.skinManager;
        TrailSkin equipped = sm.getEquippedTrailSkin();

        for (int i = 0; i < count; i++) {
            TrailSkin skin = skins[i];
            int col = i % 2;
            int row = i / 2;

            float cardX = SIDE_PAD + col * (CARD_W + CARD_GAP);
            float cardY = cardScreenY(row);

            if (cardY + CARD_H < CONTENT_BOTTOM - 100f || cardY > getContentTop() + 100f) continue;

            boolean isEquipped = skin.equals(equipped);
            boolean isOwned    = sm.isTrailSkinOwned(skin);

            float nameX = cardX + 145f;
            float nameY = cardY + CARD_H - 26f;
            String displayName = I18N.isTurkish() ? skin.nameTR : skin.nameEN;
            drawShadowText(game.regularFont, displayName, nameX, nameY, Color.WHITE);

            String statusText;
            Color statusColor;
            if (isEquipped) {
                statusText  = I18N.isTurkish() ? "KUSANILDI" : "EQUIPPED";
                statusColor = new Color(1.0f, 0.84f, 0.22f, 1f);
            } else if (isOwned) {
                statusText  = I18N.isTurkish() ? "SAHIP" : "OWNED";
                statusColor = new Color(0.30f, 0.95f, 0.60f, 1f);
            } else {
                statusText  = skin.price + (I18N.isTurkish() ? " ALTIN" : " COINS");
                statusColor = new Color(1.0f, 0.85f, 0.35f, 1f);
            }
            drawShadowText(game.smallFont, statusText, nameX, nameY - 32f, statusColor);

            float btnX = cardX + 12f;
            float btnY = cardY + 12f;
            float btnW = CARD_W - 24f;
            float btnH = 46f;

            String btnLabel;
            if (isEquipped) {
                btnLabel = I18N.isTurkish() ? "KUSANILDI" : "EQUIPPED";
            } else if (isOwned) {
                btnLabel = I18N.isTurkish() ? "KUSAN" : "EQUIP";
            } else {
                btnLabel = (I18N.isTurkish() ? "SATIN AL: " : "BUY: ") + skin.price;
            }
            drawButtonText(game.smallFont, btnLabel, btnX, btnY, btnW, btnH, Color.WHITE);
        }
    }

    // ──────────────────────────────────────────────────────────────────────────
    // DOKUNMA YÖNETİMİ (TOUCH & INPUT)
    // ──────────────────────────────────────────────────────────────────────────
    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        touchPos.set(screenX, screenY, 0);
        viewport.unproject(touchPos);
        float ux = touchPos.x, uy = touchPos.y;

        lastTouchY = uy;
        totalDragDistance = 0f;
        isDragging = false;
        scrollVelocity = 0f;

        // Geri Butonu Tıklaması
        float backX = HEADER_X + 15f;
        float backY = HEADER_Y + (HEADER_H - 56f) / 2f;
        if (ux >= backX && ux <= backX + 56f && uy >= backY && uy <= backY + 56f) {
            pressedBack = true;
            game.soundEngine.playClick();
            game.triggerHaptic(HapticType.BUTTON_CLICK);
            return true;
        }

        // Canlı Altın Kapsülü Tıklaması (DEBUG: Dokununca +10.000 Altın Verir)
        float coinW = 180f, coinH = 48f;
        float coinX = HEADER_X + HEADER_W - coinW - 15f;
        float coinY = HEADER_Y + (HEADER_H - coinH) / 2f;
        if (ux >= coinX && ux <= coinX + coinW && uy >= coinY && uy <= coinY + coinH) {
            game.saveManager.addCoins(10000);
            game.soundEngine.playWin();
            game.triggerHaptic(HapticType.VICTORY_PULSE);
            showToast("+10.000 Altin Eklendi! (DEBUG)");
            return true;
        }

        // Ana Sekmeler Tıklaması [ BİLYELER ] [ PİMLER ] [ İZLER ]
        if (uy >= MASTER_BAR_Y && uy <= MASTER_BAR_Y + MASTER_BAR_H) {
            MasterTab[] allMaster = MasterTab.values();
            for (int i = 0; i < allMaster.length; i++) {
                float mtx = MASTER_BAR_X + 4f + i * (MASTER_TAB_W + MASTER_TAB_GAP);
                if (ux >= mtx && ux <= mtx + MASTER_TAB_W) {
                    if (allMaster[i] != currentMasterTab) {
                        currentMasterTab = allMaster[i];
                        scrollOffset = 0f;
                        scrollVelocity = 0f;
                        game.soundEngine.playClick();
                        game.triggerHaptic(HapticType.BUTTON_CLICK);
                    }
                    return true;
                }
            }
        }

        // Alt Kategori Sekmeleri Tıklaması (Yalnızca Bilyeler)
        if (currentMasterTab == MasterTab.BALLS && uy >= SUB_BAR_Y && uy <= SUB_BAR_Y + SUB_BAR_H) {
            float startSubX = SUB_BAR_X + 4f;
            for (int i = 0; i < CAT_IDS.length; i++) {
                float tx = startSubX + i * (SUB_TAB_W + SUB_TAB_GAP);
                if (ux >= tx && ux <= tx + SUB_TAB_W) {
                    if (!CAT_IDS[i].equals(selectedCategory)) {
                        selectedCategory = CAT_IDS[i];
                        scrollOffset = 0f;
                        scrollVelocity = 0f;
                        game.soundEngine.playClick();
                        game.triggerHaptic(HapticType.BUTTON_CLICK);
                    }
                    return true;
                }
            }
        }

        return true;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        touchPos.set(screenX, screenY, 0);
        viewport.unproject(touchPos);
        float uy = touchPos.y;

        float dy = uy - lastTouchY;
        lastTouchY = uy;
        totalDragDistance += Math.abs(dy);

        if (totalDragDistance > 12f) {
            isDragging = true;
            pressedBack = false;
        }

        scrollOffset += dy;
        scrollVelocity = dy / Gdx.graphics.getDeltaTime();
        clampScroll();
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        touchPos.set(screenX, screenY, 0);
        viewport.unproject(touchPos);
        float ux = touchPos.x, uy = touchPos.y;

        if (isDragging && totalDragDistance > 12f) {
            isDragging = false;
            return true;
        }
        isDragging = false;

        // Geri Butonu Bırakma
        if (pressedBack) {
            pressedBack = false;
            float backX = HEADER_X + 15f;
            float backY = HEADER_Y + (HEADER_H - 56f) / 2f;
            if (ux >= backX - 10f && ux <= backX + 66f && uy >= backY - 10f && uy <= backY + 66f) {
                game.openMenu();
                return true;
            }
        }

        // Ana Sekmeler Bırakma
        if (uy >= MASTER_BAR_Y && uy <= MASTER_BAR_Y + MASTER_BAR_H) {
            MasterTab[] allMaster = MasterTab.values();
            for (int i = 0; i < allMaster.length; i++) {
                float mtx = MASTER_BAR_X + 4f + i * (MASTER_TAB_W + MASTER_TAB_GAP);
                if (ux >= mtx && ux <= mtx + MASTER_TAB_W) {
                    if (allMaster[i] != currentMasterTab) {
                        currentMasterTab = allMaster[i];
                        scrollOffset = 0f;
                        scrollVelocity = 0f;
                        game.soundEngine.playClick();
                        game.triggerHaptic(HapticType.BUTTON_CLICK);
                    }
                    return true;
                }
            }
        }

        // Alt Kategori Sekmeleri Bırakma
        if (currentMasterTab == MasterTab.BALLS && uy >= SUB_BAR_Y && uy <= SUB_BAR_Y + SUB_BAR_H) {
            float startSubX = SUB_BAR_X + 4f;
            for (int i = 0; i < CAT_IDS.length; i++) {
                float tx = startSubX + i * (SUB_TAB_W + SUB_TAB_GAP);
                if (ux >= tx && ux <= tx + SUB_TAB_W) {
                    if (!CAT_IDS[i].equals(selectedCategory)) {
                        selectedCategory = CAT_IDS[i];
                        scrollOffset = 0f;
                        scrollVelocity = 0f;
                        game.soundEngine.playClick();
                        game.triggerHaptic(HapticType.BUTTON_CLICK);
                    }
                    return true;
                }
            }
        }

        // Kart Eylem Butonları Tıklaması
        if (uy >= CONTENT_BOTTOM && uy <= getContentTop()) {
            SkinManager sm = game.skinManager;

            if (currentMasterTab == MasterTab.BALLS) {
                List<BallSkin> skins = getSkinsForCategory();
                BallSkin equipped = sm.getEquippedSkin();

                for (int i = 0; i < skins.size(); i++) {
                    BallSkin skin = skins.get(i);
                    float cardX = SIDE_PAD + (i % 2) * (CARD_W + CARD_GAP);
                    float cardY = cardScreenY(i / 2);
                    float btnX = cardX + 12f, btnY = cardY + 12f, btnW = CARD_W - 24f, btnH = 46f;

                    if (ux >= btnX && ux <= btnX + btnW && uy >= btnY && uy <= btnY + btnH) {
                        boolean isOwned    = sm.isSkinOwned(skin);
                        boolean isEquipped = skin.equals(equipped);

                        if (isEquipped) {
                            game.soundEngine.playClick();
                            game.triggerHaptic(HapticType.BUTTON_CLICK);
                        } else if (isOwned) {
                            sm.equipSkin(skin);
                            game.soundEngine.playClick();
                            game.triggerHaptic(HapticType.PIN_RELEASE);
                            showToast("\"" + (I18N.isTurkish() ? skin.nameTR : skin.nameEN) + "\" " + (I18N.isTurkish() ? "giyildi!" : "equipped!"));
                        } else {
                            boolean bought = sm.purchaseSkin(skin);
                            if (bought) {
                                sm.equipSkin(skin);
                                game.soundEngine.playWin();
                                game.triggerHaptic(HapticType.VICTORY_PULSE);
                                showToast("\"" + (I18N.isTurkish() ? skin.nameTR : skin.nameEN) + "\" " + (I18N.isTurkish() ? "satin alindi!" : "unlocked!"));
                            } else {
                                game.soundEngine.playFail();
                                game.triggerHaptic(HapticType.FAILURE_BUZZ);
                                showToast(I18N.isTurkish() ? "Yetersiz altin! (" + skin.price + " gerekli)" : "Not enough coins! (" + skin.price + " needed)");
                            }
                        }
                        return true;
                    }
                }
            } else if (currentMasterTab == MasterTab.PINS) {
                PinSkin[] skins = PinSkin.values();
                PinSkin equipped = sm.getEquippedPinSkin();

                for (int i = 0; i < skins.length; i++) {
                    PinSkin skin = skins[i];
                    float cardX = SIDE_PAD + (i % 2) * (CARD_W + CARD_GAP);
                    float cardY = cardScreenY(i / 2);
                    float btnX = cardX + 12f, btnY = cardY + 12f, btnW = CARD_W - 24f, btnH = 46f;

                    if (ux >= btnX && ux <= btnX + btnW && uy >= btnY && uy <= btnY + btnH) {
                        boolean isOwned    = sm.isPinSkinOwned(skin);
                        boolean isEquipped = skin.equals(equipped);

                        if (isEquipped) {
                            game.soundEngine.playClick();
                            game.triggerHaptic(HapticType.BUTTON_CLICK);
                        } else if (isOwned) {
                            sm.equipPinSkin(skin);
                            game.soundEngine.playClick();
                            game.triggerHaptic(HapticType.PIN_RELEASE);
                            showToast("\"" + (I18N.isTurkish() ? skin.nameTR : skin.nameEN) + "\" " + (I18N.isTurkish() ? "kusandi!" : "equipped!"));
                        } else {
                            boolean bought = sm.purchasePinSkin(skin);
                            if (bought) {
                                sm.equipPinSkin(skin);
                                game.soundEngine.playWin();
                                game.triggerHaptic(HapticType.VICTORY_PULSE);
                                showToast("\"" + (I18N.isTurkish() ? skin.nameTR : skin.nameEN) + "\" " + (I18N.isTurkish() ? "satin alindi!" : "unlocked!"));
                            } else {
                                game.soundEngine.playFail();
                                game.triggerHaptic(HapticType.FAILURE_BUZZ);
                                showToast(I18N.isTurkish() ? "Yetersiz altin! (" + skin.price + " gerekli)" : "Not enough coins! (" + skin.price + " needed)");
                            }
                        }
                        return true;
                    }
                }
            } else {
                TrailSkin[] skins = TrailSkin.values();
                TrailSkin equipped = sm.getEquippedTrailSkin();

                for (int i = 0; i < skins.length; i++) {
                    TrailSkin skin = skins[i];
                    float cardX = SIDE_PAD + (i % 2) * (CARD_W + CARD_GAP);
                    float cardY = cardScreenY(i / 2);
                    float btnX = cardX + 12f, btnY = cardY + 12f, btnW = CARD_W - 24f, btnH = 46f;

                    if (ux >= btnX && ux <= btnX + btnW && uy >= btnY && uy <= btnY + btnH) {
                        boolean isOwned    = sm.isTrailSkinOwned(skin);
                        boolean isEquipped = skin.equals(equipped);

                        if (isEquipped) {
                            game.soundEngine.playClick();
                            game.triggerHaptic(HapticType.BUTTON_CLICK);
                        } else if (isOwned) {
                            sm.equipTrailSkin(skin);
                            game.soundEngine.playClick();
                            game.triggerHaptic(HapticType.PIN_RELEASE);
                            showToast("\"" + (I18N.isTurkish() ? skin.nameTR : skin.nameEN) + "\" " + (I18N.isTurkish() ? "secildi!" : "equipped!"));
                        } else {
                            boolean bought = sm.purchaseTrailSkin(skin);
                            if (bought) {
                                sm.equipTrailSkin(skin);
                                game.soundEngine.playWin();
                                game.triggerHaptic(HapticType.VICTORY_PULSE);
                                showToast("\"" + (I18N.isTurkish() ? skin.nameTR : skin.nameEN) + "\" " + (I18N.isTurkish() ? "satin alindi!" : "unlocked!"));
                            } else {
                                game.soundEngine.playFail();
                                game.triggerHaptic(HapticType.FAILURE_BUZZ);
                                showToast(I18N.isTurkish() ? "Yetersiz altin! (" + skin.price + " gerekli)" : "Not enough coins! (" + skin.price + " needed)");
                            }
                        }
                        return true;
                    }
                }
            }
        }

        return false;
    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == com.badlogic.gdx.Input.Keys.C) {
            game.saveManager.addCoins(10000);
            game.soundEngine.playWin();
            game.triggerHaptic(HapticType.VICTORY_PULSE);
            showToast("+10.000 Altin Eklendi! (DEBUG: C)");
            return true;
        }
        if (keycode == com.badlogic.gdx.Input.Keys.U) {
            for (BallSkin s : BallSkin.values()) {
                game.saveManager.setSkinOwned(s.id);
            }
            for (PinSkin s : PinSkin.values()) {
                game.saveManager.setSkinOwned(s.id);
            }
            for (TrailSkin s : TrailSkin.values()) {
                game.saveManager.setSkinOwned(s.id);
            }
            game.soundEngine.playWin();
            game.triggerHaptic(HapticType.VICTORY_PULSE);
            showToast("Tum Skinler Acildi! (DEBUG: U)");
            return true;
        }
        if (keycode == com.badlogic.gdx.Input.Keys.ESCAPE || keycode == com.badlogic.gdx.Input.Keys.BACK) {
            game.openMenu();
            return true;
        }
        return false;
    }

    private void clampScroll() {
        int count = (currentMasterTab == MasterTab.BALLS) ? getSkinsForCategory().size() :
                    (currentMasterTab == MasterTab.PINS)  ? PinSkin.values().length :
                                                            TrailSkin.values().length;
        float ch = contentHeight(count);
        float visibleH = getContentTop() - CONTENT_BOTTOM;
        float maxScroll = Math.max(0f, ch - visibleH);
        if (scrollOffset < 0f) {
            scrollOffset = 0f;
            scrollVelocity = 0f;
        }
        if (scrollOffset > maxScroll) {
            scrollOffset = maxScroll;
            scrollVelocity = 0f;
        }
    }

    private void showToast(String msg) {
        toastMessage = msg;
        toastTimer   = 2.2f;
    }

    // ──────────────────────────────────────────────────────────────────────────
    // ÇİZİM YARDIMCILARI (SHAPE UTILITIES)
    // ──────────────────────────────────────────────────────────────────────────
    private void drawCoinIcon(ShapeRenderer sr, float cx, float cy, float radius) {
        sr.setColor(1.0f, 0.84f, 0.22f, 1.0f);
        sr.circle(cx, cy, radius, 18);
        sr.setColor(1.0f, 0.94f, 0.40f, 1.0f);
        sr.circle(cx, cy, radius * 0.72f, 16);
        sr.setColor(1.0f, 1.0f, 1.0f, 0.85f);
        sr.circle(cx - radius * 0.25f, cy + radius * 0.25f, radius * 0.28f, 12);
    }

    private void renderModernButton(ShapeRenderer sr, float x, float y, float w, float h, float r,
                                    Color topColor, Color bottomColor, Color rimColor, boolean pressed) {
        float pressOffset = pressed ? -3f : 0f;
        float curY = y + pressOffset;

        if (!pressed) {
            drawRoundedBox(sr, x + 2f, y - 5f, w, h, r, new Color(0f, 0f, 0f, 0.35f));
        }
        drawRoundedBox(sr, x, curY, w, h, r, bottomColor);

        float faceH = pressed ? h : h - 5f;
        float faceY = pressed ? curY : curY + 5f;
        drawRoundedBox(sr, x, faceY, w, faceH, r, topColor);

        if (rimColor != null && faceH > 8f && w > 2f * r + 8f) {
            sr.setColor(rimColor);
            sr.rect(x + r + 4f, faceY + faceH - 3f, w - 2f * r - 8f, 2f);
        }
    }

    private void drawRoundedBox(ShapeRenderer sr, float x, float y, float w, float h, float r, Color color) {
        if (r <= 0f || h <= 4f || w <= 4f) {
            sr.setColor(color);
            sr.rect(x, y, w, h);
            return;
        }
        r = Math.min(r, Math.min(w / 2f, h / 2f));
        sr.setColor(color);
        sr.rect(x + r, y, w - 2f * r, h);
        sr.rect(x, y + r, r, h - 2f * r);
        sr.rect(x + w - r, y + r, r, h - 2f * r);
        sr.arc(x + r,         y + r,         r, 180f, 90f, 16);
        sr.arc(x + w - r,     y + r,         r, 270f, 90f, 16);
        sr.arc(x + w - r,     y + h - r,     r,   0f, 90f, 16);
        sr.arc(x + r,         y + h - r,     r,  90f, 90f, 16);
    }

    private void drawButtonText(BitmapFont font, String text,
                                float btnX, float btnY, float btnW, float btnH, Color color) {
        glyphLayout.setText(font, text);
        float x = btnX + (btnW - glyphLayout.width) / 2f;
        float y = btnY + (btnH + glyphLayout.height) / 2f;
        font.setColor(0f, 0f, 0f, 0.45f);
        font.draw(game.batch, text, x + 2f, y - 2f);
        font.setColor(color);
        font.draw(game.batch, text, x, y);
    }

    private void drawShadowText(BitmapFont font, String text, float x, float y, Color color) {
        font.setColor(0f, 0f, 0f, 0.50f);
        font.draw(game.batch, text, x + 2f, y - 2f);
        font.setColor(color);
        font.draw(game.batch, text, x, y);
    }
}
