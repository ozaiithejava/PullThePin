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
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.game.pullthepin.PullThePinGame;
import com.game.pullthepin.daily.DailyChallengeManager;
import com.game.pullthepin.daily.DailyChallengeManager.DailyChallenge;
import com.game.pullthepin.haptics.HapticsService.HapticType;
import com.game.pullthepin.i18n.I18N;

/**
 * 2.5D Liquid Glassmorphism Günlük Meydan Okuma Ekranı (Daily Challenge Hub).
 * Kusursuz dikey boşluk dağılımı, sıfır metin taşması, deterministik günlük görevler,
 * seri (streak) sayacı, altın ödülleri ve canlı mutasyon kartları.
 */
public class DailyChallengeScreen extends InputAdapter implements Screen {

    public static final float UI_WIDTH = 720f;
    public static final float UI_HEIGHT = 1280f;

    private PullThePinGame game;
    private OrthographicCamera camera;
    private FitViewport viewport;
    private GlyphLayout glyphLayout = new GlyphLayout();
    private Vector3 touchPos = new Vector3();

    private Array<DailyChallenge> challenges;
    private String todayDateStr;
    private int pressedButton = -1; // -1: yok, 0: Geri, 1: Görev 0, 2: Görev 1, 3: Görev 2
    private float globalTimer = 0f;

    public DailyChallengeScreen(PullThePinGame game) {
        this.game = game;
        camera = new OrthographicCamera();
        viewport = new FitViewport(UI_WIDTH, UI_HEIGHT, camera);
        camera.position.set(UI_WIDTH / 2f, UI_HEIGHT / 2f, 0);

        todayDateStr = DailyChallengeManager.getTodayDisplayDate(I18N.isTurkish());
        challenges = DailyChallengeManager.getDailyChallenges(game.saveManager);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(this);
        todayDateStr = DailyChallengeManager.getTodayDisplayDate(I18N.isTurkish());
        challenges = DailyChallengeManager.getDailyChallenges(game.saveManager);
    }

    @Override
    public void render(float delta) {
        globalTimer += delta;

        Gdx.gl.glClearColor(0.04f, 0.06f, 0.11f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        viewport.apply();
        camera.update();

        ShapeRenderer sr = game.shapeRenderer;
        sr.setProjectionMatrix(camera.combined);

        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        // =========================================================================
        // 1. ŞEKİLLER & KARTLAR
        // =========================================================================
        sr.begin(ShapeRenderer.ShapeType.Filled);

        // Gece Mavisi Zemin
        sr.rect(0, 0, UI_WIDTH, UI_HEIGHT,
                new Color(0.03f, 0.04f, 0.08f, 1f),
                new Color(0.03f, 0.04f, 0.08f, 1f),
                new Color(0.08f, 0.12f, 0.22f, 1f),
                new Color(0.08f, 0.12f, 0.22f, 1f)
        );

        // 1. ÜST GERİ BUTONU (< ANA MENÜ)
        boolean backPressed = (pressedButton == 0);
        renderModernButton(sr, 50f, 1170f, 180f, 60f, 16f,
                new Color(0.20f, 0.28f, 0.45f, 0.95f),
                new Color(0.10f, 0.15f, 0.28f, 0.95f),
                new Color(0.50f, 0.65f, 0.90f, 0.50f),
                backPressed);

        // 2. ÜST TARİH & BAŞLIK KARTI
        float headerY = 975f;
        drawRoundedBox(sr, 50f, headerY - 6f, 620f, 135f, 22f, new Color(0f, 0f, 0f, 0.40f));
        drawRoundedBox(sr, 50f, headerY, 620f, 135f, 22f, new Color(0.08f, 0.12f, 0.24f, 0.96f));
        drawRoundedBox(sr, 70f, headerY + 132f, 580f, 3f, 1.5f, new Color(1.00f, 0.84f, 0.28f, 0.85f));

        // Tarih Pill Rozeti
        drawRoundedBox(sr, 200f, headerY + 14f, 320f, 32f, 16f, new Color(0.14f, 0.20f, 0.35f, 0.90f));

        // 3. İSTATİSTİK ŞERİDİ (SERİ & ALTIN BAKİYESİ)
        float statsY = 880f;
        // Sol Kart: Günlük Seri
        drawRoundedBox(sr, 50f, statsY, 300f, 75f, 18f, new Color(0.10f, 0.15f, 0.28f, 0.95f));
        drawRoundedBox(sr, 60f, statsY + 72f, 280f, 3f, 1.5f, new Color(1.00f, 0.55f, 0.20f, 0.80f));

        // Sağ Kart: Toplam Altın
        drawRoundedBox(sr, 370f, statsY, 300f, 75f, 18f, new Color(0.10f, 0.15f, 0.28f, 0.95f));
        drawRoundedBox(sr, 380f, statsY + 72f, 280f, 3f, 1.5f, new Color(1.00f, 0.84f, 0.25f, 0.80f));
        // Altın Sikke Çizimi
        drawCoinIcon(sr, 405f, statsY + 38f, 16f);

        // 4. 3 ADET GÖREV KARTI (KOLAY, ORTA, ZOR)
        float cardH = 210f;
        float startCardY = 645f;
        float cardGap = 250f;

        Color[][] themeColors = {
                // Kolay: Zümrüt Nane
                { new Color(0.14f, 0.82f, 0.58f, 1f), new Color(0.08f, 0.48f, 0.35f, 1f), new Color(0.55f, 1.0f, 0.85f, 0.6f) },
                // Orta: Safir Mavi
                { new Color(0.20f, 0.65f, 0.98f, 1f), new Color(0.10f, 0.38f, 0.68f, 1f), new Color(0.65f, 0.88f, 1.0f, 0.6f) },
                // Zor: Altın Ametist
                { new Color(0.78f, 0.42f, 0.98f, 1f), new Color(0.48f, 0.20f, 0.68f, 1f), new Color(0.90f, 0.70f, 1.0f, 0.6f) }
        };

        for (int i = 0; i < challenges.size; i++) {
            DailyChallenge dc = challenges.get(i);
            float cardY = startCardY - i * cardGap;
            boolean isPressed = (pressedButton == (i + 1));

            // Dış Gölge & Kart Gövdesi
            drawRoundedBox(sr, 50f, cardY - 6f, 620f, cardH, 22f, new Color(0f, 0f, 0f, 0.40f));
            drawRoundedBox(sr, 50f, cardY, 620f, cardH, 22f, new Color(0.08f, 0.12f, 0.22f, 0.96f));

            // Sol Canlı Eşik Çizgisi
            drawRoundedBox(sr, 50f, cardY, 10f, cardH, 5f, themeColors[i][0]);

            // Üst Sol Zorluk Rozet Kutusu
            drawRoundedBox(sr, 75f, cardY + 160f, 140f, 32f, 12f, themeColors[i][0]);

            // Ödül Altın Sikkesi
            drawCoinIcon(sr, 95f, cardY + 45f, 16f);

            // Sağ Eylem Butonu (OYNA / TAMAMLANDI)
            float btnW = 205f;
            float btnH = 64f;
            float btnX = 445f;
            float btnY = cardY + 20f;

            if (dc.isCompleted) {
                // Tamamlandı Butonu (Zümrüt Yeşil Solid Bevel)
                renderModernButton(sr, btnX, btnY, btnW, btnH, 18f,
                        new Color(0.18f, 0.75f, 0.45f, 0.95f),
                        new Color(0.10f, 0.45f, 0.25f, 0.95f),
                        new Color(0.60f, 0.95f, 0.75f, 0.50f),
                        false);
            } else {
                // Oyna Butonu
                renderModernButton(sr, btnX, btnY, btnW, btnH, 18f,
                        themeColors[i][0],
                        themeColors[i][1],
                        themeColors[i][2],
                        isPressed);
            }
        }

        // 5. ALT BİLGİLENDİRME KUTUSU
        float footerY = 45f;
        drawRoundedBox(sr, 50f, footerY, 620f, 65f, 18f, new Color(0.06f, 0.09f, 0.18f, 0.90f));

        sr.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);

        // =========================================================================
        // 2. KRİSTAL NETLİĞİNDE TİPOGRAFİ
        // =========================================================================
        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();

        boolean isTr = I18N.isTurkish();

        // Geri Butonu Metni
        drawButtonText(game.smallFont, isTr ? "< ANA MENU" : "< MAIN MENU", 50f, 1170f + (backPressed ? -2f : 2f), 180f, 60f, Color.WHITE);

        // Başlık
        drawEmbossedText(game.titleFont, isTr ? "GUNLUK MEYDAN OKUMA" : "DAILY CHALLENGES", headerY + 110f,
                new Color(1.00f, 0.86f, 0.32f, 1f),
                new Color(0.45f, 0.28f, 0.04f, 1f));

        // Tarih
        drawCenteredText(game.smallFont, todayDateStr, headerY + 36f, new Color(0.85f, 0.92f, 1.0f, 1f));

        // İstatistikler (Seri & Altın)
        int streak = game.saveManager.getDailyStreak();
        int coins = game.saveManager.getCoins();
        String streakText = String.format("%s: %d %s", isTr ? "SERI" : "STREAK", streak, isTr ? "GUN" : "DAYS");
        drawShadowText(game.regularFont, streakText, 95f, statsY + 48f, new Color(1.0f, 0.65f, 0.25f, 1f));

        String coinText = String.format("%d %s", coins, isTr ? "ALTIN" : "COINS");
        drawShadowText(game.regularFont, coinText, 435f, statsY + 48f, new Color(1.0f, 0.85f, 0.25f, 1f));

        // Görev Kartları Metinleri
        for (int i = 0; i < challenges.size; i++) {
            DailyChallenge dc = challenges.get(i);
            float cardY = startCardY - i * cardGap;
            boolean isPressed = (pressedButton == (i + 1));

            // 1. Zorluk Rozet Metni
            drawButtonText(game.smallFont, dc.getDifficultyLabel(isTr), 75f, cardY + 160f, 140f, 32f, Color.WHITE);

            // 1B. Seviye Numarası
            String lvlLabel = String.format("%s %d", isTr ? "Seviye" : "Level", dc.levelNumber);
            drawShadowText(game.smallFont, lvlLabel, 230f, cardY + 182f, new Color(0.85f, 0.92f, 1.0f, 1f));

            // 2. Mutasyon Başlığı
            drawShadowText(game.regularFont, dc.modifier.getTitle(isTr), 75f, cardY + 138f, themeColors[i][0]);

            // 3. Mutasyon Açıklaması
            drawShadowText(game.smallFont, dc.modifier.getDesc(isTr), 75f, cardY + 98f, new Color(0.72f, 0.84f, 0.96f, 0.95f));

            // 4. Ödül Miktarı
            String rewardStr = String.format("+%d %s", dc.rewardCoins, isTr ? "ALTIN" : "COINS");
            drawShadowText(game.regularFont, rewardStr, 125f, cardY + 54f, new Color(1.00f, 0.86f, 0.25f, 1.0f));

            // 5. Buton Metni
            float btnX = 445f;
            float btnY = cardY + 20f + (isPressed ? -2f : 2f);
            float btnW = 205f;
            float btnH = 64f;

            if (dc.isCompleted) {
                drawButtonText(game.smallFont, isTr ? "TAMAMLANDI" : "COMPLETED", btnX, btnY, btnW, btnH, Color.WHITE);
            } else {
                drawButtonText(game.regularFont, isTr ? "OYNA  >" : "PLAY  >", btnX, btnY, btnW, btnH, Color.WHITE);
            }
        }

        // Alt Bilgilendirme Metni
        String footerDesc = isTr ? "Yeni gorevler her gece 00:00'da yenilenir!" : "New challenges refresh every midnight!";
        drawCenteredText(game.smallFont, footerDesc, footerY + 42f, new Color(0.65f, 0.78f, 0.95f, 0.90f));

        game.batch.end();
    }

    // =========================================================================
    // ÇİZİM YARDIMCILARI (SHAPE UTILITIES)
    // =========================================================================

    private void renderModernButton(ShapeRenderer sr, float x, float y, float w, float h, float r,
                                    Color topColor, Color bottomColor, Color rimColor, boolean pressed) {
        float pressOffset = pressed ? -4f : 0f;
        float curY = y + pressOffset;

        if (!pressed) {
            drawRoundedBox(sr, x + 3f, y - 6f, w, h, r, new Color(0f, 0f, 0f, 0.38f));
        }

        drawRoundedBox(sr, x, curY, w, h, r, bottomColor);
        float faceH = pressed ? h : h - 6f;
        float faceY = pressed ? curY : curY + 6f;
        drawRoundedBox(sr, x, faceY, w, faceH, r, topColor);
        drawRoundedBox(sr, x + r, faceY + faceH - 4f, w - 2f * r, 3f, 1.5f, rimColor);
    }

    private void drawRoundedBox(ShapeRenderer sr, float x, float y, float w, float h, float r, Color color) {
        sr.setColor(color);
        sr.rect(x + r, y, w - 2f * r, h);
        sr.rect(x, y + r, r, h - 2f * r);
        sr.rect(x + w - r, y + r, r, h - 2f * r);
        sr.arc(x + r, y + r, r, 180f, 90f, 16);
        sr.arc(x + w - r, y + r, r, 270f, 90f, 16);
        sr.arc(x + w - r, y + h - r, r, 0f, 90f, 16);
        sr.arc(x + r, y + h - r, r, 90f, 90f, 16);
    }

    private void drawCoinIcon(ShapeRenderer sr, float cx, float cy, float radius) {
        // Dış Altın Halkası
        sr.setColor(0.65f, 0.45f, 0.08f, 1.0f);
        sr.circle(cx, cy, radius, 20);
        sr.setColor(1.00f, 0.84f, 0.22f, 1.0f);
        sr.circle(cx, cy, radius * 0.85f, 20);
        // İç Parlama
        sr.setColor(1.00f, 0.95f, 0.65f, 0.80f);
        sr.circle(cx - radius * 0.25f, cy + radius * 0.25f, radius * 0.35f, 14);
    }

    private void drawCenteredText(BitmapFont font, String text, float y, Color color) {
        glyphLayout.setText(font, text);
        float x = (UI_WIDTH - glyphLayout.width) / 2f;
        font.setColor(0f, 0f, 0f, 0.45f);
        font.draw(game.batch, text, x + 2f, y - 2f);
        font.setColor(color);
        font.draw(game.batch, text, x, y);
    }

    private void drawEmbossedText(BitmapFont font, String text, float y, Color fill, Color shadow) {
        glyphLayout.setText(font, text);
        float x = (UI_WIDTH - glyphLayout.width) / 2f;
        font.setColor(0f, 0f, 0f, 0.60f);
        font.draw(game.batch, text, x + 3f, y - 4f);
        font.setColor(shadow);
        font.draw(game.batch, text, x, y - 2.5f);
        font.setColor(fill);
        font.draw(game.batch, text, x, y);
    }

    private void drawButtonText(BitmapFont font, String text, float btnX, float btnY, float btnW, float btnH, Color color) {
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

    // =========================================================================
    // DOKUNMA VE ETKİLEŞİM YÖNETİMİ
    // =========================================================================

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        touchPos.set(screenX, screenY, 0);
        viewport.unproject(touchPos);

        // 1. Geri Butonu
        if (touchPos.x >= 50 && touchPos.x <= 230 && touchPos.y >= 1170 && touchPos.y <= 1230) {
            pressedButton = 0;
            game.soundEngine.playClick();
            game.triggerHaptic(HapticType.BUTTON_CLICK);
            return true;
        }

        // Görev Butonları
        float startCardY = 645f;
        float cardGap = 250f;
        for (int i = 0; i < challenges.size; i++) {
            float cardY = startCardY - i * cardGap;
            float btnX = 445f;
            float btnY = cardY + 20f;
            float btnW = 205f;
            float btnH = 64f;

            if (touchPos.x >= btnX && touchPos.x <= btnX + btnW && touchPos.y >= btnY && touchPos.y <= btnY + btnH) {
                DailyChallenge dc = challenges.get(i);
                if (!dc.isCompleted) {
                    pressedButton = i + 1;
                    game.soundEngine.playClick();
                    game.triggerHaptic(HapticType.BUTTON_CLICK);
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        touchPos.set(screenX, screenY, 0);
        viewport.unproject(touchPos);

        int releaseButton = pressedButton;
        pressedButton = -1;

        if (releaseButton == 0 && touchPos.x >= 50 && touchPos.x <= 230 && touchPos.y >= 1165 && touchPos.y <= 1235) {
            game.openMenu();
            return true;
        }

        if (releaseButton >= 1 && releaseButton <= 3) {
            int challengeIndex = releaseButton - 1;
            DailyChallenge dc = challenges.get(challengeIndex);
            if (!dc.isCompleted) {
                game.startDailyChallenge(dc);
                return true;
            }
        }

        return false;
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
    @Override public void dispose() {}
}
