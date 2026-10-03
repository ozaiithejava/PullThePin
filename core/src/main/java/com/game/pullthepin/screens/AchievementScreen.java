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
import com.game.pullthepin.achievements.AchievementManager;
import com.game.pullthepin.i18n.I18N;

public class AchievementScreen extends InputAdapter implements Screen {

    private static final float UI_W = 720f, UI_H = 1280f;

    // Card layout constants
    private static final float CARD_W = 305f;
    private static final float CARD_H = 160f;
    private static final float CARD_GAP_X = 20f;
    private static final float CARD_GAP_Y = 18f;
    private static final float CARD_MARGIN_X = 45f;

    // Scroll region: between header and back button
    private static final float SCROLL_TOP = 1130f;   // top of scroll area (y below header)
    private static final float SCROLL_BOTTOM = 120f;  // bottom of scroll area (above back btn)
    private static final float SCROLL_HEIGHT = SCROLL_TOP - SCROLL_BOTTOM;

    private PullThePinGame game;
    private OrthographicCamera camera;
    private FitViewport viewport;
    private GlyphLayout gl = new GlyphLayout();
    private Vector3 touch = new Vector3();

    private float scrollOffset = 0f;
    private float scrollVelocity = 0f;
    private float lastTouchY = 0f;
    private boolean dragging = false;
    private float totalContentHeight = 0f;

    private float timer = 0f;
    private boolean pressedBack = false;

    public AchievementScreen(PullThePinGame game) {
        this.game = game;
        camera = new OrthographicCamera();
        viewport = new FitViewport(UI_W, UI_H, camera);
        camera.position.set(UI_W / 2f, UI_H / 2f, 0);

        // Compute content height: rows = ceil(10/2) = 5
        int rows = (int) Math.ceil(AchievementManager.ALL.length / 2.0);
        totalContentHeight = rows * (CARD_H + CARD_GAP_Y) + CARD_GAP_Y;
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void render(float delta) {
        timer += delta;

        // Apply scroll inertia
        if (!dragging) {
            scrollOffset += scrollVelocity * delta;
            scrollVelocity *= 0.88f;
            if (Math.abs(scrollVelocity) < 1f) scrollVelocity = 0f;
        }
        clampScroll();

        Gdx.gl.glClearColor(0.04f, 0.06f, 0.11f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        viewport.apply();
        camera.update();

        ShapeRenderer sr = game.shapeRenderer;
        sr.setProjectionMatrix(camera.combined);
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        // ---- Background ----
        sr.begin(ShapeRenderer.ShapeType.Filled);
        sr.setColor(0.03f, 0.05f, 0.09f, 1f);
        sr.rect(0, 0, UI_W, UI_H);

        // Ambient glow circles
        float glow = 0.5f + 0.5f * (float) Math.sin(timer * 1.4f);
        sr.setColor(0.10f, 0.28f, 0.55f, 0.10f + glow * 0.05f);
        sr.circle(UI_W / 2f, UI_H * 0.6f, 360f, 36);

        // Header panel
        drawRoundedBox(sr, 30f, 1170f, 660f, 100f, 20f, new Color(0.08f, 0.12f, 0.22f, 0.97f));
        drawRoundedBox(sr, 50f, 1267f, 620f, 3f, 1.5f, new Color(0.78f, 0.55f, 1.00f, 0.80f));

        // Back button
        float backY = 30f;
        Color backColor = pressedBack
                ? new Color(0.35f, 0.20f, 0.55f, 1f)
                : new Color(0.48f, 0.22f, 0.72f, 1f);
        drawRoundedBox(sr, 75f, backY, 570f, 70f, 18f, new Color(0.28f, 0.14f, 0.45f, 1f));
        drawRoundedBox(sr, 75f, pressedBack ? backY : backY + 5f, 570f, 65f, 18f, backColor);
        drawRoundedBox(sr, 95f, pressedBack ? backY + 59f : backY + 64f, 530f, 3f, 1.5f, new Color(0.85f, 0.70f, 1.00f, 0.55f));

        // Scissor / clip mask for card area
        // We simulate clipping by only drawing cards that are visible
        drawCards(sr);

        sr.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);

        // ---- Text ----
        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();

        // Header title
        int unlockedCount = game.achievementManager.getUnlockedCount();
        String headerTitle = I18N.isTurkish() ? "BASARIMLAR" : "ACHIEVEMENTS";
        drawCentered(game.titleFont, headerTitle, 1248f, new Color(0.78f, 0.55f, 1.00f, 1f));
        String countTxt = unlockedCount + "/" + AchievementManager.ALL.length;
        drawCentered(game.smallFont, countTxt, 1188f, new Color(0.85f, 0.93f, 1.00f, 1f));

        // Card texts
        drawCardTexts();

        // Back button text
        String backTxt = I18N.isTurkish() ? "GERI" : "BACK";
        drawCentered(game.regularFont, backTxt, pressedBack ? 78f : 83f, Color.WHITE);

        game.batch.end();
    }

    private void drawCards(ShapeRenderer sr) {
        AchievementManager.Achievement[] all = AchievementManager.ALL;
        for (int i = 0; i < all.length; i++) {
            AchievementManager.Achievement ach = all[i];
            float[] pos = getCardPos(i);
            float cx = pos[0];
            float cy = pos[1] + scrollOffset;

            // Cull cards outside visible area
            if (cy + CARD_H < SCROLL_BOTTOM || cy > SCROLL_TOP) continue;

            boolean unlocked = game.achievementManager.isUnlocked(ach.id);

            if (unlocked) {
                // Gold glow border
                float pulse = 0.5f + 0.5f * (float) Math.sin(timer * 2.2f + i * 0.7f);
                drawRoundedBox(sr, cx - 3, cy - 3, CARD_W + 6, CARD_H + 6, 14f,
                        new Color(1f, 0.82f, 0.22f, 0.22f + pulse * 0.13f));
                // Shadow
                drawRoundedBox(sr, cx + 3, cy - 4, CARD_W, CARD_H, 12f, new Color(0f, 0f, 0f, 0.35f));
                // Card body
                drawRoundedBox(sr, cx, cy, CARD_W, CARD_H, 12f, new Color(0.10f, 0.16f, 0.28f, 0.97f));
                // Top accent line (gold)
                drawRoundedBox(sr, cx + 10f, cy + CARD_H - 4f, CARD_W - 20f, 3f, 1.5f, new Color(1f, 0.84f, 0.28f, 0.85f));
            } else {
                // Locked card — dimmed
                drawRoundedBox(sr, cx + 2, cy - 3, CARD_W, CARD_H, 12f, new Color(0f, 0f, 0f, 0.25f));
                drawRoundedBox(sr, cx, cy, CARD_W, CARD_H, 12f, new Color(0.08f, 0.10f, 0.16f, 0.90f));
                drawRoundedBox(sr, cx + 10f, cy + CARD_H - 4f, CARD_W - 20f, 3f, 1.5f, new Color(0.40f, 0.42f, 0.50f, 0.45f));
            }

            // Progress bar (for target > 0)
            if (ach.targetProgress > 0) {
                int progress = game.achievementManager.getProgress(ach.id);
                float pct = MathUtils.clamp((float) progress / ach.targetProgress, 0f, 1f);
                float barX = cx + 10f;
                float barY = cy + 14f;
                float barW = CARD_W - 20f;
                float barH = 10f;
                // Track
                drawRoundedBox(sr, barX, barY, barW, barH, 5f, new Color(0.04f, 0.06f, 0.12f, 0.95f));
                // Fill
                if (pct > 0f) {
                    Color fillColor = unlocked
                            ? new Color(1f, 0.82f, 0.22f, 1f)
                            : new Color(0.30f, 0.55f, 0.90f, 1f);
                    drawRoundedBox(sr, barX, barY, barW * pct, barH, 5f, fillColor);
                }
            }

            // Lock icon on locked cards (small square)
            if (!unlocked) {
                sr.setColor(new Color(0.45f, 0.48f, 0.58f, 0.70f));
                sr.rect(cx + CARD_W - 26f, cy + CARD_H - 24f, 14f, 10f);
                sr.setColor(new Color(0.40f, 0.43f, 0.53f, 0.70f));
                sr.rect(cx + CARD_W - 24f, cy + CARD_H - 30f, 10f, 8f);
            }
        }
    }

    private void drawCardTexts() {
        AchievementManager.Achievement[] all = AchievementManager.ALL;
        boolean tr = I18N.isTurkish();
        for (int i = 0; i < all.length; i++) {
            AchievementManager.Achievement ach = all[i];
            float[] pos = getCardPos(i);
            float cx = pos[0];
            float cy = pos[1] + scrollOffset;

            if (cy + CARD_H < SCROLL_BOTTOM || cy > SCROLL_TOP) continue;

            boolean unlocked = game.achievementManager.isUnlocked(ach.id);
            String name = tr ? ach.nameTR : ach.nameEN;
            String desc = tr ? ach.descTR : ach.descEN;

            // Name
            Color nameColor = unlocked
                    ? new Color(1f, 0.84f, 0.28f, 1f)
                    : new Color(0.55f, 0.58f, 0.68f, 1f);
            drawCardText(game.smallFont, name, cx + 10f, cy + CARD_H - 18f, CARD_W - 20f, nameColor);

            // Description (small)
            Color descColor = unlocked
                    ? new Color(0.80f, 0.88f, 0.98f, 0.90f)
                    : new Color(0.40f, 0.43f, 0.52f, 0.80f);
            drawCardText(game.smallFont, desc, cx + 10f, cy + CARD_H - 44f, CARD_W - 20f, descColor);

            // Reward coins
            String rewardTxt = "+" + ach.rewardCoins + " Jeton";
            Color rewardColor = unlocked
                    ? new Color(0.25f, 0.95f, 0.60f, 1f)
                    : new Color(0.38f, 0.40f, 0.48f, 0.70f);
            drawCardText(game.smallFont, rewardTxt, cx + 10f, cy + CARD_H - 70f, CARD_W - 20f, rewardColor);

            // Progress text
            if (ach.targetProgress > 0) {
                int progress = game.achievementManager.getProgress(ach.id);
                String progTxt = progress + "/" + ach.targetProgress;
                Color progColor = unlocked
                        ? new Color(1f, 0.84f, 0.22f, 0.90f)
                        : new Color(0.50f, 0.55f, 0.70f, 0.80f);
                drawCardText(game.smallFont, progTxt, cx + 10f, cy + 34f, CARD_W - 20f, progColor);
            } else if (unlocked) {
                drawCardText(game.smallFont, "✔", cx + 10f, cy + 34f, CARD_W - 20f, new Color(0.25f, 1f, 0.55f, 1f));
            }
        }
    }

    /** Returns [x, y] (y is base, before scrollOffset is applied). */
    private float[] getCardPos(int index) {
        int col = index % 2;
        int row = index / 2;
        int totalRows = (int) Math.ceil(AchievementManager.ALL.length / 2.0);
        // Cards stack downward from SCROLL_TOP; first card at top
        float baseY = SCROLL_TOP - CARD_GAP_Y - (row + 1) * (CARD_H + CARD_GAP_Y);
        float x = CARD_MARGIN_X + col * (CARD_W + CARD_GAP_X);
        return new float[]{x, baseY};
    }

    private void clampScroll() {
        float maxScroll = 0f;
        // If content taller than scroll area, allow scrolling down (negative offset moves content up)
        float minScroll = -(totalContentHeight - SCROLL_HEIGHT);
        if (minScroll > 0) minScroll = 0; // content fits
        scrollOffset = MathUtils.clamp(scrollOffset, minScroll, maxScroll);
    }

    private void drawRoundedBox(ShapeRenderer sr, float x, float y, float w, float h, float r, Color color) {
        if (w <= 0 || h <= 0) return;
        sr.setColor(color);
        sr.rect(x + r, y, w - 2 * r, h);
        sr.rect(x, y + r, r, h - 2 * r);
        sr.rect(x + w - r, y + r, r, h - 2 * r);
        sr.arc(x + r, y + r, r, 180f, 90f, 14);
        sr.arc(x + w - r, y + r, r, 270f, 90f, 14);
        sr.arc(x + w - r, y + h - r, r, 0f, 90f, 14);
        sr.arc(x + r, y + h - r, r, 90f, 90f, 14);
    }

    private void drawCentered(BitmapFont font, String text, float y, Color color) {
        gl.setText(font, text);
        float x = (UI_W - gl.width) / 2f;
        font.setColor(0f, 0f, 0f, 0.45f);
        font.draw(game.batch, text, x + 2f, y - 2f);
        font.setColor(color);
        font.draw(game.batch, text, x, y);
    }

    private void drawCardText(BitmapFont font, String text, float x, float y, float maxW, Color color) {
        font.setColor(0f, 0f, 0f, 0.40f);
        font.draw(game.batch, text, x + 1f, y - 1f, maxW, -1, true);
        font.setColor(color);
        font.draw(game.batch, text, x, y, maxW, -1, true);
    }

    // ---- Touch ----

    @Override
    public boolean touchDown(int sx, int sy, int p, int b) {
        touch.set(sx, sy, 0);
        viewport.unproject(touch);
        lastTouchY = touch.y;

        // Back button
        if (touch.x >= 75 && touch.x <= 645 && touch.y >= 30 && touch.y <= 100) {
            pressedBack = true;
            return true;
        }

        // Scroll area
        if (touch.y >= SCROLL_BOTTOM && touch.y <= SCROLL_TOP) {
            dragging = true;
            scrollVelocity = 0f;
        }
        return true;
    }

    @Override
    public boolean touchDragged(int sx, int sy, int p) {
        touch.set(sx, sy, 0);
        viewport.unproject(touch);
        if (dragging) {
            float dy = touch.y - lastTouchY;
            scrollOffset += dy;
            scrollVelocity = dy / Gdx.graphics.getDeltaTime();
            lastTouchY = touch.y;
        }
        pressedBack = false;
        return true;
    }

    @Override
    public boolean touchUp(int sx, int sy, int p, int b) {
        touch.set(sx, sy, 0);
        viewport.unproject(touch);
        dragging = false;

        if (pressedBack) {
            pressedBack = false;
            game.openMenu();
            return true;
        }
        return false;
    }

    @Override public void resize(int w, int h) { viewport.update(w, h, true); }
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
    @Override public void dispose() {}
}
