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
import com.game.pullthepin.levels.LevelLoader;
import com.game.pullthepin.theme.WorldTheme;

/**
 * 2.5D İzometrik Seviye Seçim Ekranı (60 Seviye, Yıldız Sandığı & Rütbe Sistemi).
 * - Canlı Toplam Yıldız Sayacı (⭐ X / 180), Oyuncu Rütbe Rozeti & Dünya İlerlemesi
 * - 3 Yıldızlı Seviyelerde Parlayan Altın Halka ve Yıldız Işıltısı
 * - Sayfa 1: Seviye 1 - 30, Sayfa 2: Seviye 31 - 60
 */
public class LevelSelectScreen extends InputAdapter implements Screen {

    public static final float UI_WIDTH = 720f;
    public static final float UI_HEIGHT = 1280f;

    private PullThePinGame game;
    private OrthographicCamera camera;
    private FitViewport viewport;
    private GlyphLayout glyphLayout = new GlyphLayout();
    private Vector3 touchPos = new Vector3();

    private int currentPage = 1;
    private static final int TOTAL_PAGES = 10;
    private static final int LEVELS_PER_PAGE = 20;

    private static final int COLS = 5;
    private static final int ROWS = 4;
    private static final float BUTTON_SIZE = 95f;
    private static final float START_X = 75f;
    private static final float START_Y = 890f;
    private static final float SPACING_X = 120f;
    private static final float SPACING_Y = 135f;

    public LevelSelectScreen(PullThePinGame game) {
        this.game = game;
        camera = new OrthographicCamera();
        viewport = new FitViewport(UI_WIDTH, UI_HEIGHT, camera);
        camera.position.set(UI_WIDTH / 2f, UI_HEIGHT / 2f, 0);

        int unlocked = game.saveManager.getUnlockedLevel();
        currentPage = Math.min(TOTAL_PAGES, Math.max(1, (unlocked - 1) / LEVELS_PER_PAGE + 1));
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.05f, 0.07f, 0.12f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        viewport.apply();
        camera.update();

        int unlockedLevel = game.saveManager.getUnlockedLevel();
        int totalStars = game.saveManager.getTotalStars();
        int maxStars = LevelLoader.TOTAL_LEVELS * 3;

        ShapeRenderer sr = game.shapeRenderer;
        sr.setProjectionMatrix(camera.combined);

        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        sr.begin(ShapeRenderer.ShapeType.Filled);

        // Lüks Stüdyo Arka Plan Gradyanı
        sr.rect(0, 0, UI_WIDTH, UI_HEIGHT,
                new Color(0.04f, 0.05f, 0.10f, 1f),
                new Color(0.04f, 0.05f, 0.10f, 1f),
                new Color(0.09f, 0.13f, 0.22f, 1f),
                new Color(0.09f, 0.13f, 0.22f, 1f)
        );

        // 1. ÜST YILDIZ VE RÜTBE KAPSÜLÜ (Top Star Trophy Banner)
        sr.setColor(0.08f, 0.12f, 0.22f, 0.96f);
        sr.rect(40, 1140, 640, 95);
        sr.setColor(1.0f, 0.84f, 0.22f, 0.90f);
        sr.rect(40, 1140, 640, 4);

        // Yıldız Rozet Yuvası
        sr.setColor(1.0f, 0.85f, 0.25f, 0.22f);
        sr.circle(95, 1188, 32, 24);
        drawStar(sr, 95, 1188, 22f, new Color(1.0f, 0.88f, 0.22f, 1f));
        drawStar(sr, 95, 1188, 10f, Color.WHITE);

        // 2. SAYFA VE DÜNYA SEÇİCİ BAR
        sr.setColor(0.08f, 0.12f, 0.20f, 0.95f);
        sr.rect(50, 1050, 620, 60);
        sr.setColor(0.2f, 0.6f, 1.0f, 0.6f);
        sr.rect(50, 1050, 620, 3);

        // Sol Sayfa Oku (<)
        if (currentPage > 1) {
            sr.setColor(0.18f, 0.25f, 0.40f, 1.0f);
            sr.rect(65, 1058, 48, 44);
        }

        // Sağ Sayfa Oku (>)
        if (currentPage < TOTAL_PAGES) {
            sr.setColor(0.18f, 0.25f, 0.40f, 1.0f);
            sr.rect(607, 1058, 48, 44);
        }

        // 3. 30 ADET SEVİYE KARTI
        int startLevel = (currentPage - 1) * LEVELS_PER_PAGE + 1;
        int endLevel = Math.min(currentPage * LEVELS_PER_PAGE, LevelLoader.TOTAL_LEVELS);

        for (int levelNum = startLevel; levelNum <= endLevel; levelNum++) {
            int pageIdx = levelNum - startLevel;
            int col = pageIdx % COLS;
            int row = pageIdx / COLS;

            float bx = START_X + col * SPACING_X;
            float by = START_Y - row * SPACING_Y;

            boolean isUnlocked = (levelNum <= unlockedLevel);
            boolean isCurrent = (levelNum == unlockedLevel);

            WorldTheme.ThemeType theme = WorldTheme.getThemeForLevel(levelNum);
            int stars = game.saveManager.getStarsForLevel(levelNum);

            if (isUnlocked) {
                boolean isJackpot = (levelNum % 5 == 0);

                // Aktif Seviye Işıma Aurası
                if (isCurrent) {
                    float cp = 0.5f + 0.5f * (float) Math.sin(Gdx.graphics.getFrameId() * 0.08f);
                    sr.setColor(1.0f, 0.85f, 0.25f, 0.35f + cp * 0.30f);
                    sr.rect(bx - 4f, by - 4f, BUTTON_SIZE + 8f, BUTTON_SIZE + 8f);
                }

                // Jackpot Bonus Seviyesi Altın Işıltısı (Every 5th Level)
                if (isJackpot) {
                    float jpPulse = 0.5f + 0.5f * (float) Math.sin(Gdx.graphics.getFrameId() * 0.08f + levelNum);
                    sr.setColor(1.0f, 0.84f, 0.22f, 0.35f + jpPulse * 0.25f);
                    sr.rect(bx - 3f, by - 3f, BUTTON_SIZE + 6f, BUTTON_SIZE + 6f);
                }

                // 3 Yıldızlı Kusursuz Seviye Altın Tacı (3-Star Golden Aura)
                if (stars == 3) {
                    float pulse = 0.5f + 0.5f * (float) Math.sin(Gdx.graphics.getFrameId() * 0.06f + levelNum);
                    sr.setColor(1.0f, 0.85f, 0.2f, 0.20f + pulse * 0.15f);
                    sr.circle(bx + BUTTON_SIZE / 2f, by + BUTTON_SIZE / 2f, BUTTON_SIZE * 0.65f, 20);
                }

                // Zemin gölgesi
                sr.setColor(0f, 0f, 0f, 0.32f);
                sr.rect(bx + 4f, by - 5f, BUTTON_SIZE, BUTTON_SIZE);

                // Tematik Kart Rengi
                Color cardColor = isCurrent ? new Color(0.18f, 0.72f, 1.0f, 1.0f) : getThemeColor(theme);
                sr.setColor(cardColor);
                sr.rect(bx, by, BUTTON_SIZE, BUTTON_SIZE);

                // Alt Eğim Şeridi
                sr.setColor(cardColor.r * 0.65f, cardColor.g * 0.65f, cardColor.b * 0.65f, 1.0f);
                sr.rect(bx, by, BUTTON_SIZE, 9);

                // Üst Işık Çizgisi veya Jackpot Üst Şeridi
                if (isJackpot) {
                    sr.setColor(1.0f, 0.84f, 0.22f, 0.95f);
                    sr.rect(bx, by + BUTTON_SIZE - 6f, BUTTON_SIZE, 6f);
                    // Sağ üst köşede altın sikke rozeti
                    sr.setColor(1.0f, 0.84f, 0.22f, 1.0f);
                    sr.circle(bx + BUTTON_SIZE - 12f, by + BUTTON_SIZE - 12f, 6.5f, 12);
                    sr.setColor(1.0f, 0.98f, 0.60f, 1.0f);
                    sr.circle(bx + BUTTON_SIZE - 13.5f, by + BUTTON_SIZE - 10.5f, 2.5f, 8);
                } else {
                    sr.setColor(1f, 1f, 1f, 0.35f);
                    sr.rect(bx, by + BUTTON_SIZE - 4f, BUTTON_SIZE, 4);
                }

                // Yıldızlar
                for (int s = 0; s < 3; s++) {
                    float starX = bx + 18f + s * 28f;
                    float starY = by + 16f;
                    if (s < stars) {
                        sr.setColor(1.0f, 0.84f, 0.22f, 1.0f);
                        drawStar(sr, starX, starY, 9.0f, new Color(1.0f, 0.86f, 0.22f, 1.0f));
                        drawStar(sr, starX, starY, 4.5f, Color.WHITE);
                    } else {
                        drawStar(sr, starX, starY, 7.0f, new Color(0.12f, 0.18f, 0.28f, 0.9f));
                    }
                }
            } else {
                // Kilitli Kart
                sr.setColor(0f, 0f, 0f, 0.22f);
                sr.rect(bx + 3f, by - 4f, BUTTON_SIZE, BUTTON_SIZE);

                sr.setColor(0.12f, 0.15f, 0.22f, 0.96f);
                sr.rect(bx, by, BUTTON_SIZE, BUTTON_SIZE);
                sr.setColor(0.08f, 0.10f, 0.15f, 1.0f);
                sr.rect(bx, by, BUTTON_SIZE, 9);

                // Asma Kilit İkonu
                sr.setColor(0.35f, 0.42f, 0.55f, 0.6f);
                sr.rect(bx + BUTTON_SIZE / 2f - 9f, by + BUTTON_SIZE / 2f - 12f, 18f, 15f);
                sr.circle(bx + BUTTON_SIZE / 2f, by + BUTTON_SIZE / 2f + 5f, 7f, 12);
                sr.setColor(0.12f, 0.15f, 0.22f, 1.0f);
                sr.circle(bx + BUTTON_SIZE / 2f, by + BUTTON_SIZE / 2f + 5f, 4f, 10);
            }
        }

        // GERİ DÖN BUTONU
        sr.setColor(0f, 0f, 0f, 0.35f);
        sr.rect(155, 60, 410, 85);
        sr.setColor(0.98f, 0.38f, 0.48f, 1.0f);
        sr.rect(150, 65, 420, 80);
        sr.setColor(0.72f, 0.22f, 0.30f, 1.0f);
        sr.rect(150, 65, 420, 10);
        sr.setColor(1.0f, 0.75f, 0.82f, 0.5f);
        sr.rect(150, 141, 420, 4);

        sr.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);

        // 2. NET TİPOGRAFİ
        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();

        // Üst Başlık & Toplam Yıldız Sayacı (Solunda vektör altın yıldız çizilidir)
        String starScoreStr = String.format("%d / %d", totalStars, maxStars);
        String rankStr = game.saveManager.getStarRankTitle(com.game.pullthepin.i18n.I18N.isTurkish());
        drawShadowText(game.regularFont, starScoreStr, 145, 1210, new Color(1.0f, 0.88f, 0.25f, 1f));
        drawShadowText(game.smallFont, rankStr, 145, 1175, new Color(0.85f, 0.95f, 1.0f, 1f));

        // Sayfa Bilgisi & Dünya Başlığı
        String pageStr = com.game.pullthepin.i18n.I18N.get("PAGE_LABEL", currentPage, TOTAL_PAGES);
        WorldTheme.ThemeType theme = WorldTheme.getThemeForLevel(startLevel);
        String worldName = theme.getName(com.game.pullthepin.i18n.I18N.isTurkish());
        String rangeStr = String.format("%s: %s (%d - %d)",
                com.game.pullthepin.i18n.I18N.isTurkish() ? ("DÜNYA " + currentPage) : ("WORLD " + currentPage),
                worldName, startLevel, endLevel);
        drawCenteredText(game.regularFont, pageStr + " • " + rangeStr, 1090, new Color(0.85f, 0.95f, 1.0f, 1f));

        if (currentPage > 1) {
            drawCenteredInBox(game.regularFont, "<", 65, 1058, 48, 44, Color.WHITE);
        }
        if (currentPage < TOTAL_PAGES) {
            drawCenteredInBox(game.regularFont, ">", 607, 1058, 48, 44, Color.WHITE);
        }

        // Seviye Numaraları
        for (int levelNum = startLevel; levelNum <= endLevel; levelNum++) {
            int pageIdx = levelNum - startLevel;
            int col = pageIdx % COLS;
            int row = pageIdx / COLS;

            float bx = START_X + col * SPACING_X;
            float by = START_Y - row * SPACING_Y;

            boolean isUnlocked = (levelNum <= unlockedLevel);
            String numStr = String.valueOf(levelNum);

            if (isUnlocked) {
                glyphLayout.setText(game.regularFont, numStr);
                float tx = bx + (BUTTON_SIZE - glyphLayout.width) / 2f;
                float ty = by + BUTTON_SIZE * 0.74f;
                boolean isJackpot = (levelNum % 5 == 0);
                Color numColor = isJackpot ? new Color(1.0f, 0.88f, 0.25f, 1f) : Color.WHITE;
                drawText(game.regularFont, numStr, tx, ty, numColor);
            } else {
                glyphLayout.setText(game.regularFont, "-");
                float tx = bx + (BUTTON_SIZE - glyphLayout.width) / 2f;
                float ty = by + BUTTON_SIZE * 0.65f;
                drawText(game.regularFont, "-", tx, ty, new Color(0.40f, 0.45f, 0.55f, 0.7f));
            }
        }

        // Geri Dön Yazısı
        drawButtonText(game.regularFont, com.game.pullthepin.i18n.I18N.get("BTN_BACK_MENU"), 150, 65, 420, 80, Color.WHITE);

        game.batch.end();
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

    private Color getThemeColor(WorldTheme.ThemeType theme) {
        switch (theme) {
            case CYBER_LAB:       return new Color(0.18f, 0.65f, 0.95f, 1.0f);
            case ATLANTIS_OCEAN:  return new Color(0.15f, 0.75f, 0.75f, 1.0f);
            case MAGMA_FORGE:     return new Color(0.92f, 0.45f, 0.18f, 1.0f);
            case COSMIC_NEBULA:   return new Color(0.72f, 0.35f, 0.92f, 1.0f);
            case EMERALD_ZEN:     return new Color(0.20f, 0.78f, 0.48f, 1.0f);
            case SOLAR_MIRAGE:    return new Color(0.95f, 0.75f, 0.20f, 1.0f);
            case TOXIC_BIOSPHERE:  return new Color(0.25f, 0.85f, 0.35f, 1.0f);
            case SKY_JETSTREAM:    return new Color(0.40f, 0.75f, 1.0f, 1.0f);
            case CHAOS_REACTOR:    return new Color(0.85f, 0.25f, 0.90f, 1.0f);
            case GRANDMASTER_APEX: return new Color(1.0f, 0.84f, 0.25f, 1.0f);
            default:              return new Color(0.20f, 0.60f, 0.90f, 1.0f);
        }
    }

    private void drawText(BitmapFont font, String text, float x, float y, Color color) {
        font.setColor(0f, 0f, 0f, 0.35f);
        font.draw(game.batch, text, x + 1.5f, y - 1.5f);
        font.setColor(color);
        font.draw(game.batch, text, x, y);
    }

    private void drawShadowText(BitmapFont font, String text, float x, float y, Color color) {
        font.setColor(0f, 0f, 0f, 0.85f);
        font.draw(game.batch, text, x + 2f, y - 2f);
        font.setColor(color);
        font.draw(game.batch, text, x, y);
    }

    private void drawCenteredText(BitmapFont font, String text, float y, Color color) {
        glyphLayout.setText(font, text);
        float x = (UI_WIDTH - glyphLayout.width) / 2f;
        drawText(font, text, x, y, color);
    }

    private void drawCenteredInBox(BitmapFont font, String text, float bx, float by, float bw, float bh, Color color) {
        glyphLayout.setText(font, text);
        float x = bx + (bw - glyphLayout.width) / 2f;
        float y = by + (bh + glyphLayout.height) / 2f;
        drawText(font, text, x, y, color);
    }

    private void drawButtonText(BitmapFont font, String text, float btnX, float btnY, float btnW, float btnH, Color color) {
        glyphLayout.setText(font, text);
        float x = btnX + (btnW - glyphLayout.width) / 2f;
        float y = btnY + (btnH + glyphLayout.height) / 2f;
        drawText(font, text, x, y, color);
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        touchPos.set(screenX, screenY, 0);
        viewport.unproject(touchPos);

        int unlockedLevel = game.saveManager.getUnlockedLevel();

        // Sayfa Değiştirme Butonları
        if (currentPage > 1 && touchPos.x >= 60 && touchPos.x <= 120 && touchPos.y >= 1050 && touchPos.y <= 1110) {
            currentPage--;
            game.soundEngine.playClick();
            game.triggerHaptic(HapticType.BUTTON_CLICK);
            return true;
        }
        if (currentPage < TOTAL_PAGES && touchPos.x >= 600 && touchPos.x <= 660 && touchPos.y >= 1050 && touchPos.y <= 1110) {
            currentPage++;
            game.soundEngine.playClick();
            game.triggerHaptic(HapticType.BUTTON_CLICK);
            return true;
        }

        int startLevel = (currentPage - 1) * LEVELS_PER_PAGE + 1;
        int endLevel = Math.min(currentPage * LEVELS_PER_PAGE, LevelLoader.TOTAL_LEVELS);

        for (int levelNum = startLevel; levelNum <= endLevel; levelNum++) {
            int pageIdx = levelNum - startLevel;
            int col = pageIdx % COLS;
            int row = pageIdx / COLS;

            float bx = START_X + col * SPACING_X;
            float by = START_Y - row * SPACING_Y;

            if (touchPos.x >= bx && touchPos.x <= bx + BUTTON_SIZE &&
                touchPos.y >= by && touchPos.y <= by + BUTTON_SIZE) {
                if (levelNum <= unlockedLevel) {
                    game.soundEngine.playClick();
                    game.triggerHaptic(HapticType.BUTTON_CLICK);
                    game.startLevel(levelNum);
                    return true;
                }
            }
        }

        // Geri Dön Butonu
        if (touchPos.x >= 150 && touchPos.x <= 570 && touchPos.y >= 65 && touchPos.y <= 145) {
            game.soundEngine.playClick();
            game.triggerHaptic(HapticType.BUTTON_CLICK);
            game.openMenu();
            return true;
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
