package com.game.pullthepin.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.game.pullthepin.PullThePinGame;
import com.game.pullthepin.i18n.I18N;

public class DailyRewardScreen extends InputAdapter implements Screen {
    private static final float UI_W = 720f, UI_H = 1280f;
    private PullThePinGame game;
    private OrthographicCamera camera;
    private FitViewport viewport;
    private GlyphLayout gl = new GlyphLayout();
    private Vector3 touch = new Vector3();
    private float timer = 0f;
    private float claimAnim = 0f; // 0=idle, >0 = animating
    private int coinsAwarded = 0;
    private boolean claimed = false;
    private boolean pressedClaim = false;
    private boolean pressedClose = false;

    public DailyRewardScreen(PullThePinGame game) {
        this.game = game;
        camera = new OrthographicCamera();
        viewport = new FitViewport(UI_W, UI_H, camera);
        camera.position.set(UI_W/2, UI_H/2, 0);
    }

    @Override public void show() { Gdx.input.setInputProcessor(this); }

    @Override
    public void render(float delta) {
        timer += delta;
        if (claimAnim > 0) { claimAnim -= delta; if (claimAnim <= 0 && claimed) game.openMenu(); }

        Gdx.gl.glClearColor(0.02f, 0.04f, 0.08f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        viewport.apply();
        camera.update();

        ShapeRenderer sr = game.shapeRenderer;
        sr.setProjectionMatrix(camera.combined);
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        sr.begin(ShapeRenderer.ShapeType.Filled);

        // Dark overlay background
        sr.setColor(0.02f, 0.04f, 0.08f, 1f);
        sr.rect(0, 0, UI_W, UI_H);

        // Card
        float cx = 50f, cy = 200f, cw = 620f, ch = 820f;
        drawRoundedBox(sr, cx+4, cy-4, cw, ch, 28f, new Color(0f,0f,0f,0.5f));
        // Gold border glow
        float pulse = 0.5f+0.5f*(float)Math.sin(timer*2.5f);
        drawRoundedBox(sr, cx-3, cy-3, cw+6, ch+6, 30f, new Color(1f,0.82f,0.22f, 0.25f+pulse*0.15f));
        drawRoundedBox(sr, cx, cy, cw, ch, 28f, new Color(0.07f,0.11f,0.20f, 0.98f));
        // Top gold line
        drawRoundedBox(sr, cx+20, cy+ch-5, cw-40, 4f, 2f, new Color(1f,0.84f,0.28f,0.9f));

        // Day boxes (7 boxes in 2 rows: 4 then 3)
        int streak = game.dailyRewardManager.getCurrentStreak(game.saveManager);
        boolean canClaim = game.dailyRewardManager.canClaimToday(game.saveManager);
        int todayDayIndex = canClaim ? (streak % 7) : ((streak-1+7) % 7);

        float boxW = 128f, boxH = 110f, boxGap = 12f;
        // Row 1: days 0-3
        float row1Y = cy + ch - 180f;
        for (int i = 0; i < 4; i++) {
            float bx = cx + 16 + i*(boxW+boxGap);
            drawDayBox(sr, bx, row1Y, boxW, boxH, i, todayDayIndex, streak, canClaim);
        }
        // Row 2: days 4-6
        float row2Y = row1Y - boxH - 18f;
        float row2StartX = cx + 16 + (boxW+boxGap)*0.5f; // centered
        for (int i = 4; i < 7; i++) {
            float bx = row2StartX + (i-4)*(boxW+boxGap);
            drawDayBox(sr, bx, row2Y, boxW, boxH, i, todayDayIndex, streak, canClaim);
        }

        // Claim button
        if (canClaim && !claimed) {
            float btnY = cy + 60f;
            float btnPulse = 0.5f+0.5f*(float)Math.sin(timer*4f);
            drawRoundedBox(sr, cx+40, btnY-6, 540f, 90f, 22f, new Color(0.15f,0.90f,0.55f, 0.20f+btnPulse*0.15f));
            drawRoundedBox(sr, cx+40+3, btnY-7, 540f, 90f, 22f, new Color(0.08f,0.52f,0.28f,1f));
            float faceY = pressedClaim ? btnY-7 : btnY;
            drawRoundedBox(sr, cx+40, faceY, 540f, 83f, 22f, pressedClaim ? new Color(0.12f,0.70f,0.40f,1f) : new Color(0.16f,0.88f,0.55f,1f));
            drawRoundedBox(sr, cx+60, faceY+77f, 500f, 3f, 1.5f, new Color(0.6f,1f,0.85f,0.65f));
        } else if (claimed && claimAnim > 0) {
            // Success animation
            float a = MathUtils.clamp(claimAnim, 0f, 1f);
            drawRoundedBox(sr, cx+40, cy+60, 540f, 83f, 22f, new Color(0.16f,0.88f,0.55f, a));
        }

        // Close button (X) top right
        float closeX = cx+cw-60, closeY = cy+ch-60;
        Color closeBg = pressedClose ? new Color(0.5f,0.1f,0.1f,0.9f) : new Color(0.25f,0.18f,0.28f,0.9f);
        drawRoundedBox(sr, closeX-2, closeY-2, 44f, 44f, 8f, closeBg);

        sr.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);

        // Text
        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();

        // Title
        String title = I18N.isTurkish() ? "GUNLUK ODUL" : "DAILY REWARD";
        drawCentered(game.titleFont, title, cy+ch-80, new Color(1f,0.84f,0.28f,1f));

        // Streak
        String streakTxt = (I18N.isTurkish() ? "Seri: " : "Streak: ") + streak + " gun";
        drawCentered(game.smallFont, streakTxt, cy+ch-120, new Color(0.75f,0.85f,0.95f,1f));

        // Day box labels
        float row1Y2 = cy + ch - 180f;
        for (int i = 0; i < 4; i++) {
            float bx = cx + 16 + i*(boxW+boxGap);
            drawDayLabel(bx, row1Y2, boxW, boxH, i);
        }
        float row2Y2 = row1Y2 - boxH - 18f;
        float row2StartX2 = cx + 16 + (boxW+boxGap)*0.5f;
        for (int i = 4; i < 7; i++) {
            float bx = row2StartX2 + (i-4)*(boxW+boxGap);
            drawDayLabel(bx, row2Y2, boxW, boxH, i);
        }

        // Claim button text
        if (!claimed) {
            if (canClaim) {
                int todayCoins = game.dailyRewardManager.getRewardForDay(streak % 7);
                String btnTxt = (I18N.isTurkish() ? "AL: " : "CLAIM: ") + todayCoins + " Jeton";
                drawCentered(game.regularFont, btnTxt, cy + 112f, Color.WHITE);
            } else {
                drawCentered(game.regularFont, I18N.isTurkish() ? "Yarin geri gel!" : "Come back tomorrow!", cy+112f, new Color(0.7f,0.8f,0.9f,1f));
            }
        } else {
            String claimTxt = "+" + coinsAwarded + " Jeton!";
            drawCentered(game.regularFont, claimTxt, cy+112f, new Color(0.2f,1f,0.6f,1f));
        }

        // Close X text
        float closeX2 = cx+cw-60;
        float closeY2 = cy+ch-60;
        game.smallFont.setColor(Color.WHITE);
        game.smallFont.draw(game.batch, "X", closeX2+10, closeY2+28);

        game.batch.end();
    }

    private void drawDayBox(ShapeRenderer sr, float x, float y, float w, float h, int dayIndex, int todayIdx, int streak, boolean canClaim) {
        boolean isCompleted = dayIndex < (streak % 7 == 0 && streak > 0 ? 7 : streak % 7);
        boolean isToday = (dayIndex == todayIdx) && canClaim;
        boolean[] special = {false,false,true,false,true,false,true};

        if (isCompleted) {
            drawRoundedBox(sr, x, y, w, h, 12f, new Color(0.1f,0.55f,0.28f,0.95f));
            drawRoundedBox(sr, x+2, y+h-3, w-4, 3f, 1.5f, new Color(0.4f,1f,0.7f,0.6f));
        } else if (isToday) {
            float p = 0.5f+0.5f*(float)Math.sin(timer*3f);
            drawRoundedBox(sr, x-3, y-3, w+6, h+6, 14f, new Color(1f,0.82f,0.22f, 0.3f+p*0.2f));
            drawRoundedBox(sr, x, y, w, h, 12f, new Color(0.18f,0.22f,0.38f,0.98f));
            drawRoundedBox(sr, x+2, y+h-3, w-4, 3f, 1.5f, new Color(1f,0.84f,0.28f,0.8f));
        } else {
            drawRoundedBox(sr, x, y, w, h, 12f, new Color(0.1f,0.13f,0.20f,0.90f));
        }

        // Special day gem indicator
        if (special[dayIndex]) {
            float gemA = isCompleted ? 0.5f : (isToday ? 1f : 0.3f);
            sr.setColor(new Color(0.85f,0.3f,1f,gemA));
            float cx2 = x+w-18, cy2 = y+h-18;
            sr.triangle(cx2,cy2-8, cx2-6,cy2+4, cx2+6,cy2+4);
        }
    }

    private void drawDayLabel(float x, float y, float w, float h, int dayIndex) {
        int[] coins = {50,100,150,200,250,300,500};
        String dayTxt = (I18N.isTurkish() ? "Gun " : "Day ") + (dayIndex+1);
        String coinTxt = "+" + coins[dayIndex];
        gl.setText(game.smallFont, dayTxt);
        float tx = x + (w - gl.width)/2;
        game.smallFont.setColor(new Color(0.85f,0.90f,0.98f,1f));
        game.smallFont.draw(game.batch, dayTxt, tx, y+h-14);
        gl.setText(game.smallFont, coinTxt);
        tx = x + (w - gl.width)/2;
        game.smallFont.setColor(new Color(1f,0.84f,0.28f,1f));
        game.smallFont.draw(game.batch, coinTxt, tx, y+22);
    }

    private void drawCentered(com.badlogic.gdx.graphics.g2d.BitmapFont font, String text, float y, Color color) {
        gl.setText(font, text);
        float x = (UI_W - gl.width) / 2f;
        font.setColor(0f,0f,0f,0.45f);
        font.draw(game.batch, text, x+2, y-2);
        font.setColor(color);
        font.draw(game.batch, text, x, y);
    }

    private void drawRoundedBox(ShapeRenderer sr, float x, float y, float w, float h, float r, Color color) {
        sr.setColor(color);
        sr.rect(x+r, y, w-2*r, h);
        sr.rect(x, y+r, r, h-2*r);
        sr.rect(x+w-r, y+r, r, h-2*r);
        sr.arc(x+r, y+r, r, 180, 90, 14);
        sr.arc(x+w-r, y+r, r, 270, 90, 14);
        sr.arc(x+w-r, y+h-r, r, 0, 90, 14);
        sr.arc(x+r, y+h-r, r, 90, 90, 14);
    }

    @Override
    public boolean touchDown(int sx, int sy, int p, int b) {
        touch.set(sx,sy,0); viewport.unproject(touch);
        float cx = 50f, cy = 200f, cw = 620f, ch = 820f;
        // Close button
        if (touch.x >= cx+cw-62 && touch.x <= cx+cw-18 && touch.y >= cy+ch-62 && touch.y <= cy+ch-18) {
            pressedClose = true; return true;
        }
        // Claim button
        boolean canClaim = game.dailyRewardManager.canClaimToday(game.saveManager);
        if (canClaim && !claimed && touch.x >= cx+40 && touch.x <= cx+580 && touch.y >= cy+60 && touch.y <= cy+143) {
            pressedClaim = true; return true;
        }
        return false;
    }

    @Override
    public boolean touchUp(int sx, int sy, int p, int b) {
        touch.set(sx,sy,0); viewport.unproject(touch);
        if (pressedClose) {
            pressedClose = false;
            game.openMenu();
            return true;
        }
        if (pressedClaim) {
            pressedClaim = false;
            coinsAwarded = game.dailyRewardManager.claimTodayReward(game.saveManager);
            claimed = true;
            claimAnim = 1.8f;
            game.soundEngine.playWin();
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
