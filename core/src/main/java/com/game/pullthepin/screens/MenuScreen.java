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
import com.game.pullthepin.daily.DailyChallengeManager;
import com.game.pullthepin.haptics.HapticsService.HapticType;
import com.game.pullthepin.i18n.I18N;

/**
 * Üst Düzey AAA 2.5D Chill / ASMR Ana Menü Ekranı (Liquid Glassmorphism & Cyber Luxe).
 * Kusursuz piksel geometrisi, sıfır grafik hatası, pürüzsüz tipografi,
 * prosedürel altın yıldız ve kupa paneli, dokunsal yaylı buton geri bildirimi.
 */
public class MenuScreen extends InputAdapter implements Screen {

    public static final float UI_WIDTH = 720f;
    public static final float UI_HEIGHT = 1280f;

    private PullThePinGame game;
    private OrthographicCamera camera;
    private FitViewport viewport;
    private GlyphLayout glyphLayout = new GlyphLayout();
    private Vector3 touchPos = new Vector3();

    // Arka Plan Yüzen Cam Bilyeler
    private static class AmbientMarble {
        float x, y, radius, speedY, phase, driftSpeed;
        Color color;
        Color specularColor;
    }
    private AmbientMarble[] bgMarbles = new AmbientMarble[18];

    // Ortam Parçacık Tozları (Ambient Stardust Motes)
    private static class Stardust {
        float x, y, radius, alpha, speedY, pulse;
    }
    private Stardust[] stardusts = new Stardust[28];

    // Dokunma / Basma Animasyon Takibi
    // -1: Yok, 0: Oyna, 1: Seviyeler, 2: Editor, 3: Ses, 4: Haptik, 5: Dil
    private int pressedButton = -1;
    private float globalTimer = 0f;

    // Toast Bildirim
    private String toastMessage = "";
    private float toastTimer = 0f;

    public MenuScreen(PullThePinGame game) {
        this.game = game;
        camera = new OrthographicCamera();
        viewport = new FitViewport(UI_WIDTH, UI_HEIGHT, camera);
        camera.position.set(UI_WIDTH / 2f, UI_HEIGHT / 2f, 0);

        Color[] marblePalette = {
                new Color(0.98f, 0.40f, 0.55f, 0.35f), // Neon Gül
                new Color(0.18f, 0.88f, 0.65f, 0.35f), // Zümrüt Nane
                new Color(0.25f, 0.70f, 1.00f, 0.35f), // Safir Okyanus
                new Color(1.00f, 0.78f, 0.22f, 0.35f), // Altın Kehribar
                new Color(0.70f, 0.40f, 0.98f, 0.35f), // Ametist Mor
                new Color(0.20f, 0.90f, 0.90f, 0.35f)  // Kuantum Camgöbeği
        };

        for (int i = 0; i < bgMarbles.length; i++) {
            AmbientMarble m = new AmbientMarble();
            m.x = MathUtils.random(40f, UI_WIDTH - 40f);
            m.y = MathUtils.random(0f, UI_HEIGHT);
            m.radius = MathUtils.random(32f, 78f);
            m.speedY = MathUtils.random(25f, 65f);
            m.driftSpeed = MathUtils.random(0.3f, 0.8f);
            m.phase = MathUtils.random(0f, 6.28f);
            m.color = marblePalette[i % marblePalette.length].cpy();
            m.specularColor = new Color(1f, 1f, 1f, 0.38f);
            bgMarbles[i] = m;
        }

        for (int i = 0; i < stardusts.length; i++) {
            Stardust s = new Stardust();
            s.x = MathUtils.random(20f, UI_WIDTH - 20f);
            s.y = MathUtils.random(0f, UI_HEIGHT);
            s.radius = MathUtils.random(1.5f, 3.5f);
            s.speedY = MathUtils.random(15f, 40f);
            s.pulse = MathUtils.random(0f, 6.28f);
            s.alpha = MathUtils.random(0.2f, 0.7f);
            stardusts[i] = s;
        }
    }

    public void showToast(String msg) {
        this.toastMessage = msg;
        this.toastTimer = 2.6f;
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(this);
        if (game.dailyRewardManager.canClaimToday(game.saveManager)) {
            game.openDailyReward();
            return;
        }
    }

    @Override
    public void render(float delta) {
        globalTimer += delta;
        if (toastTimer > 0) toastTimer -= delta;

        // Arka plan cam bilye animasyonu
        for (AmbientMarble m : bgMarbles) {
            m.y += m.speedY * delta;
            m.x += (float) Math.sin(globalTimer * m.driftSpeed + m.phase) * 0.5f;
            if (m.y > UI_HEIGHT + m.radius * 2f) {
                m.y = -m.radius * 2f;
                m.x = MathUtils.random(40f, UI_WIDTH - 40f);
            }
        }

        // Yıldız tozu animasyonu
        for (Stardust s : stardusts) {
            s.y += s.speedY * delta;
            if (s.y > UI_HEIGHT + 10f) {
                s.y = -10f;
                s.x = MathUtils.random(20f, UI_WIDTH - 20f);
            }
        }

        // Koyu Gece Mavisi Zemin Temizliği
        Gdx.gl.glClearColor(0.04f, 0.06f, 0.11f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        viewport.apply();
        camera.update();

        ShapeRenderer sr = game.shapeRenderer;
        sr.setProjectionMatrix(camera.combined);

        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        // =========================================================================
        // 1. DİNAMİK ARKA PLAN & IŞIK HALELERİ
        // =========================================================================
        sr.begin(ShapeRenderer.ShapeType.Filled);

        // Lüks Gece Yarısı Degrade Zemin
        sr.rect(0, 0, UI_WIDTH, UI_HEIGHT,
                new Color(0.03f, 0.04f, 0.08f, 1f),
                new Color(0.03f, 0.04f, 0.08f, 1f),
                new Color(0.08f, 0.12f, 0.22f, 1f),
                new Color(0.08f, 0.12f, 0.22f, 1f)
        );

        // Merkez Ambiyans Işık Halkaları
        float centerGlow = 0.5f + 0.5f * (float) Math.sin(globalTimer * 1.5f);
        sr.setColor(0.12f, 0.35f, 0.65f, 0.12f + centerGlow * 0.06f);
        sr.circle(UI_WIDTH / 2f, UI_HEIGHT * 0.55f, 340f, 36);
        sr.setColor(0.18f, 0.85f, 0.65f, 0.08f + centerGlow * 0.04f);
        sr.circle(UI_WIDTH / 2f, UI_HEIGHT * 0.65f, 220f, 32);

        // Yüzen Parlak Cam Bilyeler
        for (AmbientMarble m : bgMarbles) {
            // Gölge
            sr.setColor(0f, 0f, 0f, 0.25f);
            sr.circle(m.x + 6f, m.y - 8f, m.radius, 24);

            // Ana Cam Bilye
            sr.setColor(m.color);
            sr.circle(m.x, m.y, m.radius, 24);

            // İç Parlaklık Kemeri
            sr.setColor(m.color.r * 1.2f, m.color.g * 1.2f, m.color.b * 1.2f, m.color.a * 1.3f);
            sr.circle(m.x - m.radius * 0.15f, m.y + m.radius * 0.15f, m.radius * 0.72f, 20);

            // 3D Işık Yansıması (Specular Highlight)
            sr.setColor(m.specularColor);
            sr.circle(m.x - m.radius * 0.35f, m.y + m.radius * 0.35f, m.radius * 0.24f, 16);
            sr.circle(m.x - m.radius * 0.45f, m.y + m.radius * 0.45f, m.radius * 0.10f, 12);
        }

        // Ambiyans Yıldız Tozları
        for (Stardust s : stardusts) {
            float a = s.alpha * (0.6f + 0.4f * (float) Math.sin(globalTimer * 3f + s.pulse));
            sr.setColor(0.85f, 0.95f, 1.0f, a);
            sr.circle(s.x, s.y, s.radius, 10);
        }

        // Üst Sağ: Canlı Altın Kapsülü
        float coinPillW = 145f, coinPillH = 40f;
        float coinPillX = UI_WIDTH - coinPillW - 35f;
        float coinPillY = UI_HEIGHT - 65f;
        drawRoundedBox(sr, coinPillX, coinPillY - 3f, coinPillW, coinPillH, 20f, new Color(0f, 0f, 0f, 0.35f));
        drawRoundedBox(sr, coinPillX, coinPillY, coinPillW, coinPillH, 20f, new Color(0.08f, 0.12f, 0.22f, 0.92f));
        drawRoundedBox(sr, coinPillX + 15f, coinPillY + coinPillH - 2f, coinPillW - 30f, 2f, 1f, new Color(1.0f, 0.84f, 0.22f, 0.75f));
        // Altın sikke simgesi
        sr.setColor(1.0f, 0.84f, 0.22f, 1.0f);
        sr.circle(coinPillX + 22f, coinPillY + coinPillH / 2f, 10f, 16);
        sr.setColor(1.0f, 0.98f, 0.60f, 1.0f);
        sr.circle(coinPillX + 20f, coinPillY + coinPillH / 2f + 2f, 4f, 12);

        // =========================================================================
        // 2. BAŞLIK & LOGO KARTI (3D CRYSTAL EMBOSSED BANNER)
        // =========================================================================
        float bannerFloat = (float) Math.sin(globalTimer * 2.0f) * 4f;
        float bannerY = 970f + bannerFloat;

        // Dış Yumuşak Gölge
        drawRoundedBox(sr, 60f, bannerY - 8f, 600f, 220f, 24f, new Color(0f, 0f, 0f, 0.45f));

        // Dış Neon Altın Çevre Işıltısı
        float logoPulse = 0.5f + 0.5f * (float) Math.sin(globalTimer * 2.5f);
        drawRoundedBox(sr, 56f, bannerY - 4f, 608f, 228f, 26f, new Color(1.0f, 0.82f, 0.22f, 0.25f + logoPulse * 0.18f));

        // Ana Cam Kart Gövdesi
        drawRoundedBox(sr, 60f, bannerY, 600f, 220f, 24f, new Color(0.09f, 0.13f, 0.24f, 0.96f));

        // Üst İnce Altın Işık Çizgisi
        drawRoundedBox(sr, 80f, bannerY + 213f, 560f, 4f, 2f, new Color(1.00f, 0.84f, 0.28f, 0.90f));

        // Alt Başlık Pill Rozet Kutusu
        drawRoundedBox(sr, 195f, bannerY + 54f, 330f, 32f, 16f, new Color(0.15f, 0.22f, 0.38f, 0.90f));
        drawRoundedBox(sr, 195f, bannerY + 54f, 330f, 2f, 1f, new Color(0.35f, 0.55f, 0.90f, 0.60f));

        // =========================================================================
        // 3. 2.5D MODERN YUVARLAK KÖŞELİ BUTONLAR (FERAH & DENGELİ)
        // =========================================================================

        // 1. OYNA (PLAY) - Zümrüt Neon Yeşil (Geniş & Auralı)
        float playY = 865f;
        boolean playPressed = (pressedButton == 0);
        float playPulse = 0.5f + 0.5f * (float) Math.sin(globalTimer * 3.5f);

        // Oyna Butonu Dış Pulsing Aura
        if (!playPressed) {
            drawRoundedBox(sr, 70f, playY - 5f, 580f, 90f, 24f, new Color(0.15f, 0.90f, 0.65f, 0.22f + playPulse * 0.18f));
        }

        renderModernButton(sr, 75f, playY, 570f, 80f, 20f,
                new Color(0.16f, 0.86f, 0.62f, 1.0f),
                new Color(0.08f, 0.54f, 0.38f, 1.0f),
                new Color(0.60f, 1.00f, 0.88f, 0.70f),
                playPressed);
        if (!playPressed) {
            renderButtonShimmer(sr, 75f, playY, 570f, 80f, 20f);
        }

        // Oyna Butonu Üzerinde 3D Beyaz Üçgen İkonu
        float playTriX = 405f;
        float playTriY = playY + (playPressed ? 36f : 40f);
        sr.setColor(1f, 1f, 1f, 0.95f);
        sr.triangle(playTriX, playTriY - 11f, playTriX, playTriY + 11f, playTriX + 18f, playTriY);

        // 2. GÜNLÜK MEYDAN OKUMA (DAILY CHALLENGES) - Lüks Kehribar Alev & Altın
        float dailyY = 770f;
        boolean dailyPressed = (pressedButton == 1);
        float dailyPulse = 0.5f + 0.5f * (float) Math.sin(globalTimer * 4.0f);

        if (!dailyPressed) {
            drawRoundedBox(sr, 70f, dailyY - 4f, 580f, 84f, 22f, new Color(1.00f, 0.62f, 0.15f, 0.18f + dailyPulse * 0.15f));
        }

        renderModernButton(sr, 75f, dailyY, 570f, 76f, 18f,
                new Color(1.00f, 0.62f, 0.15f, 1.0f),
                new Color(0.68f, 0.35f, 0.05f, 1.0f),
                new Color(1.00f, 0.88f, 0.55f, 0.65f),
                dailyPressed);
        if (!dailyPressed) {
            renderButtonShimmer(sr, 75f, dailyY, 570f, 76f, 18f);
        }

        // Görev Tamamlanma Rozeti (Badge: [X/3])
        String todayKey = DailyChallengeManager.getTodayKey();
        int completedCount = game.saveManager.getCompletedDailyChallengesCount(todayKey);
        float badgeW = 74f;
        float badgeH = 32f;
        float badgeX = 550f;
        float badgeY = dailyY + (dailyPressed ? 20f : 24f);
        drawRoundedBox(sr, badgeX, badgeY, badgeW, badgeH, 12f, completedCount == 3 ? new Color(0.15f, 0.75f, 0.40f, 0.95f) : new Color(0.18f, 0.24f, 0.38f, 0.95f));

        // 3. SEVİYELER (LEVELS 1-60) - Safir Okyanus Mavisi
        float levelsY = 678f;
        boolean levelsPressed = (pressedButton == 2);
        renderModernButton(sr, 75f, levelsY, 570f, 74f, 18f,
                new Color(0.20f, 0.65f, 0.98f, 1.0f),
                new Color(0.10f, 0.38f, 0.68f, 1.0f),
                new Color(0.65f, 0.88f, 1.00f, 0.60f),
                levelsPressed);

        // 4. BÖLÜM EDİTÖRÜ - Parlak Ametist / Mor
        float editorY = 588f;
        boolean editorPressed = (pressedButton == 3);
        renderModernButton(sr, 75f, editorY, 570f, 72f, 18f,
                new Color(0.78f, 0.42f, 0.98f, 1.0f),
                new Color(0.48f, 0.20f, 0.68f, 1.0f),
                new Color(0.90f, 0.70f, 1.00f, 0.60f),
                editorPressed);

        // 5. SES & HAPTİK BUTONLARI (İkili Modern Kart Satırı)
        boolean soundOn = game.soundEngine.isSoundEnabled();
        boolean hapticOn = game.saveManager.isHapticsEnabled();
        float toggleY = 500f;

        // 5A. SES (Sound Toggle) - Sol: x=75, w=275
        boolean soundPressed = (pressedButton == 4);
        renderModernButton(sr, 75f, toggleY, 275f, 70f, 16f,
                soundOn ? new Color(0.35f, 0.55f, 0.95f, 1.0f) : new Color(0.28f, 0.26f, 0.38f, 0.95f),
                soundOn ? new Color(0.18f, 0.28f, 0.65f, 1.0f) : new Color(0.16f, 0.14f, 0.22f, 0.95f),
                soundOn ? new Color(0.70f, 0.85f, 1.00f, 0.60f) : new Color(0.50f, 0.48f, 0.60f, 0.35f),
                soundPressed);

        // Ses LED Durum Işığı
        float soundLedY = toggleY + (soundPressed ? 31f : 35f);
        sr.setColor(soundOn ? new Color(0.20f, 1.00f, 0.55f, 1f) : new Color(0.5f, 0.5f, 0.5f, 0.6f));
        sr.circle(108f, soundLedY, 6f, 12);
        if (soundOn) {
            sr.setColor(0.20f, 1.00f, 0.55f, 0.35f);
            sr.circle(108f, soundLedY, 11f, 14);
        }

        // 5B. HAPTİK (Haptic Toggle) - Sağ: x=370, w=275
        boolean hapticPressed = (pressedButton == 5);
        renderModernButton(sr, 370f, toggleY, 275f, 70f, 16f,
                hapticOn ? new Color(0.92f, 0.32f, 0.60f, 1.0f) : new Color(0.38f, 0.24f, 0.30f, 0.95f),
                hapticOn ? new Color(0.60f, 0.12f, 0.35f, 1.0f) : new Color(0.20f, 0.10f, 0.16f, 0.95f),
                hapticOn ? new Color(1.00f, 0.68f, 0.88f, 0.60f) : new Color(0.60f, 0.45f, 0.50f, 0.35f),
                hapticPressed);

        // Haptik LED Durum Işığı
        float hapticLedY = toggleY + (hapticPressed ? 31f : 35f);
        sr.setColor(hapticOn ? new Color(0.20f, 1.00f, 0.55f, 1f) : new Color(0.5f, 0.5f, 0.5f, 0.6f));
        sr.circle(403f, hapticLedY, 6f, 12);
        if (hapticOn) {
            sr.setColor(0.20f, 1.00f, 0.55f, 0.35f);
            sr.circle(403f, hapticLedY, 11f, 14);
        }

        // 6. DİL SEÇİCİ (LANGUAGE) - Kobalt & Uzay Mavisi
        float langY = 412f;
        boolean langPressed = (pressedButton == 6);
        renderModernButton(sr, 75f, langY, 570f, 70f, 16f,
                new Color(0.28f, 0.45f, 0.85f, 1.0f),
                new Color(0.14f, 0.25f, 0.55f, 1.0f),
                new Color(0.65f, 0.80f, 1.00f, 0.55f),
                langPressed);

        // 7. MAĞAZA & BAŞARIMLAR (İkili Modern Buton Satırı)
        float shopAchY = 295f;

        // 7A. MAĞAZA (SHOP) - Sol: x=75, w=275
        boolean shopPressed = (pressedButton == 7);
        renderModernButton(sr, 75f, shopAchY, 275f, 70f, 16f,
                new Color(0.12f, 0.72f, 0.85f, 1.0f),
                new Color(0.06f, 0.42f, 0.52f, 1.0f),
                new Color(0.60f, 0.95f, 1.00f, 0.65f),
                shopPressed);

        // 7B. BAŞARIMLAR (ACHIEVEMENTS) - Sağ: x=370, w=275
        boolean achPressed = (pressedButton == 8);
        renderModernButton(sr, 370f, shopAchY, 275f, 70f, 16f,
                new Color(0.75f, 0.42f, 0.95f, 1.0f),
                new Color(0.45f, 0.18f, 0.65f, 1.0f),
                new Color(0.90f, 0.70f, 1.00f, 0.60f),
                achPressed);

        // =========================================================================
        // 4. OYUNCU İLERLEME VE YILDIZ KUPASI PANELİ (KOMPAKT & MODERN)
        // =========================================================================
        float dashY = 160f;
        float dashW = 570f;
        float dashH = 105f;
        float dashX = 75f;

        // Dış Gölge
        drawRoundedBox(sr, dashX, dashY - 4f, dashW, dashH, 18f, new Color(0f, 0f, 0f, 0.35f));
        // Ana Panel
        drawRoundedBox(sr, dashX, dashY, dashW, dashH, 18f, new Color(0.08f, 0.12f, 0.22f, 0.94f));
        // Panel Üst Kenar Işığı
        drawRoundedBox(sr, dashX + 20f, dashY + dashH - 3f, dashW - 40f, 2f, 1f, new Color(0.25f, 0.45f, 0.75f, 0.45f));

        // İlerleme Çubuğu Yuvası (Track)
        int unlockedLevel = game.saveManager.getUnlockedLevel();
        float progressFrac = MathUtils.clamp((float) (unlockedLevel - 1) / (float) (com.game.pullthepin.levels.LevelLoader.TOTAL_LEVELS - 1), 0.02f, 1.0f);
        drawRoundedBox(sr, dashX + 20f, dashY + 38f, dashW - 40f, 10f, 5f, new Color(0.04f, 0.06f, 0.12f, 0.95f));

        // İlerleme Çubuğu Dolgusu
        drawRoundedBox(sr, dashX + 20f, dashY + 38f, (dashW - 40f) * progressFrac, 10f, 5f, new Color(0.18f, 0.88f, 0.65f, 1f));

        // Küçük 3D Prosedürel Altın Yıldız Çizimi
        draw5PointStar(sr, dashX + 32f, dashY + 74f, 10f, 4.5f, new Color(1.0f, 0.84f, 0.22f, 1.0f), new Color(0.65f, 0.45f, 0.08f, 1.0f));

        // Toast Bildirim Kartı
        if (toastTimer > 0) {
            float toastAlpha = Math.min(1.0f, toastTimer * 2f);
            drawRoundedBox(sr, 60f, 890f, 600f, 64f, 16f, new Color(0.08f, 0.12f, 0.22f, 0.98f * toastAlpha));
            drawRoundedBox(sr, 60f, 890f, 600f, 3f, 1.5f, new Color(1.0f, 0.82f, 0.22f, 1.0f * toastAlpha));
        }

        sr.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);

        // =========================================================================
        // 5. KRİSTAL NETLİĞİNDE YÜKSEK ÇÖZÜNÜRLÜKLÜ TİPOGRAFİ
        // =========================================================================
        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();

        // 1. Ana Başlık ("PULL THE PIN")
        drawEmbossedText(game.titleFont, I18N.get("MENU_TITLE"), bannerY + 175f,
                new Color(1.00f, 0.86f, 0.32f, 1f),
                new Color(0.45f, 0.28f, 0.04f, 1f));

        // Alt Başlık Pill Metni
        drawCenteredText(game.smallFont, "[ " + I18N.get("MENU_SUBTITLE") + " ]", bannerY + 76f, new Color(0.85f, 0.93f, 1.0f, 1f));
        // Açıklama Metni
        drawCenteredText(game.smallFont, I18N.get("MENU_DESC"), bannerY + 30f, new Color(0.65f, 0.78f, 0.95f, 0.85f));

        // 2. Buton Metinleri (Kusursuz Merkezlenmiş)
        float playTextY = playY + (playPressed ? 0f : 3f);
        drawButtonText(game.regularFont, I18N.get("BTN_PLAY"), 75f, playTextY, 540f, 80f, Color.WHITE);

        float dailyTextY = dailyY + (dailyPressed ? 0f : 3f);
        drawButtonText(game.regularFont, I18N.get("BTN_DAILY_CHALLENGE"), 75f, dailyTextY, 490f, 76f, Color.WHITE);
        drawButtonText(game.smallFont, completedCount + "/3", badgeX, badgeY - 2f, badgeW, badgeH, Color.WHITE);

        float levelsTextY = levelsY + (levelsPressed ? 0f : 3f);
        drawButtonText(game.regularFont, I18N.get("BTN_LEVELS"), 75f, levelsTextY, 570f, 74f, Color.WHITE);

        float editorTextY = editorY + (editorPressed ? 0f : 3f);
        drawButtonText(game.regularFont, I18N.get("BTN_EDITOR"), 75f, editorTextY, 570f, 72f, Color.WHITE);

        // 3. Ses & Haptik Metinleri
        float soundTextY = toggleY + (soundPressed ? 0f : 3f);
        drawButtonText(game.smallFont, I18N.get(soundOn ? "BTN_SOUND_ON" : "BTN_SOUND_OFF"), 115f, soundTextY, 230f, 70f, Color.WHITE);

        float hapticTextY = toggleY + (hapticPressed ? 0f : 3f);
        drawButtonText(game.smallFont, I18N.get(hapticOn ? "BTN_HAPTICS_ON" : "BTN_HAPTICS_OFF"), 410f, hapticTextY, 230f, 70f, Color.WHITE);

        // 4. Dil Değiştir Butonu Metni
        float langTextY = langY + (langPressed ? 0f : 3f);
        drawButtonText(game.regularFont, I18N.get("BTN_LANG"), 75f, langTextY, 570f, 70f, Color.WHITE);

        // 5. Mağaza & Başarımlar Buton Metinleri
        float shopTextY = shopAchY + (shopPressed ? 0f : 3f);
        drawButtonText(game.smallFont, I18N.get("BTN_SHOP"), 75f, shopTextY, 275f, 70f, Color.WHITE);

        float achTextY = shopAchY + (achPressed ? 0f : 3f);
        drawButtonText(game.smallFont, I18N.get("BTN_ACHIEVEMENTS"), 370f, achTextY, 275f, 70f, Color.WHITE);

        // Üst Sağ: Canlı Altın Kapsülü Metni
        int coins = game.saveManager.getCoins();
        String coinStr = String.valueOf(coins);
        glyphLayout.setText(game.smallFont, coinStr);
        float coinTextX = coinPillX + 38f + (coinPillW - 44f - glyphLayout.width) / 2f;
        float coinTextY = coinPillY + (coinPillH + glyphLayout.height) / 2f;
        drawShadowText(game.smallFont, coinStr, coinTextX, coinTextY, new Color(1.0f, 0.88f, 0.35f, 1.0f));

        // 6. İlerleme & Yıldız Dashboard Metinleri
        int totalStars = game.saveManager.getTotalStars();
        int maxStars = com.game.pullthepin.levels.LevelLoader.TOTAL_LEVELS * 3;
        String rankTitle = game.saveManager.getStarRankTitle(I18N.isTurkish());

        // Yıldız & Rütbe (Sol tarafta, küçük yıldızın yanında)
        String starInfo = String.format("%d/%d  |  %s", totalStars, maxStars, rankTitle);
        drawShadowText(game.smallFont, starInfo, 125f, dashY + 80f, new Color(1.00f, 0.84f, 0.25f, 1.0f));

        // Seviye İlerlemesi (Sağ tarafta)
        String lvlText = I18N.get("TXT_PROGRESS", unlockedLevel);
        glyphLayout.setText(game.smallFont, lvlText);
        drawShadowText(game.smallFont, lvlText, 625f - glyphLayout.width, dashY + 80f, new Color(0.78f, 0.88f, 0.98f, 1.0f));

        // Alt Footer Bilgisi (İlerleme çubuğunun altında, ince ve şık)
        drawCenteredText(game.smallFont, I18N.get("TXT_FOOTER"), dashY + 16f, new Color(0.50f, 0.65f, 0.80f, 0.70f));

        // Toast Metni
        if (toastTimer > 0) {
            glyphLayout.setText(game.smallFont, toastMessage);
            drawShadowText(game.smallFont, toastMessage, (UI_WIDTH - glyphLayout.width) / 2f, 930f, Color.WHITE);
        }

        game.batch.end();
    }

    // =========================================================================
    // ÇİZİM YARDIMCILARI (SHAPE UTILITIES - SIFIR ARTIFAKT)
    // =========================================================================

    private void renderModernButton(ShapeRenderer sr, float x, float y, float w, float h, float r,
                                    Color topColor, Color bottomColor, Color rimColor, boolean pressed) {
        float pressOffset = pressed ? -4f : 0f;
        float curY = y + pressOffset;

        // 1. Yumuşak Zemin Gölgesi (Basılmamışken)
        if (!pressed) {
            drawRoundedBox(sr, x + 3f, y - 7f, w, h, r, new Color(0f, 0f, 0f, 0.38f));
        }

        // 2. 3D Taban Gövdesi (Koyu Alt Eğim)
        drawRoundedBox(sr, x, curY, w, h, r, bottomColor);

        // 3. Yükseltilmiş Ön Plaka (Ana Canlı Renk)
        float faceH = pressed ? h : h - 6f;
        float faceY = pressed ? curY : curY + 6f;
        drawRoundedBox(sr, x, faceY, w, faceH, r, topColor);

        // 4. Üst Parlak Işık Şeridi (Specular Highlight)
        drawRoundedBox(sr, x + r, faceY + faceH - 4f, w - 2f * r, 3f, 1.5f, rimColor);
    }

    private void renderButtonShimmer(ShapeRenderer sr, float x, float y, float w, float h, float r) {
        float cycleTime = 2.4f;
        float phase = (globalTimer % cycleTime);
        if (phase < 0.75f) {
            float progress = phase / 0.75f;
            float shimmerW = 55f;
            float shimmerX = x - shimmerW + progress * (w + shimmerW * 2f);
            float clampedLeft = Math.max(x + r * 0.5f, shimmerX);
            float clampedRight = Math.min(x + w - r * 0.5f, shimmerX + shimmerW);
            if (clampedRight > clampedLeft) {
                sr.setColor(1f, 1f, 1f, 0.22f * (float) Math.sin(progress * Math.PI));
                sr.rect(clampedLeft, y + 4f, clampedRight - clampedLeft, h - 10f);
            }
        }
    }

    private void drawRoundedBox(ShapeRenderer sr, float x, float y, float w, float h, float r, Color color) {
        sr.setColor(color);
        // İç Gövde Dikdörtgenleri
        sr.rect(x + r, y, w - 2f * r, h);
        sr.rect(x, y + r, r, h - 2f * r);
        sr.rect(x + w - r, y + r, r, h - 2f * r);
        // 4 Köşe Yayları (Pürüzsüz 16 segment)
        sr.arc(x + r, y + r, r, 180f, 90f, 16);
        sr.arc(x + w - r, y + r, r, 270f, 90f, 16);
        sr.arc(x + w - r, y + h - r, r, 0f, 90f, 16);
        sr.arc(x + r, y + h - r, r, 90f, 90f, 16);
    }

    private void draw5PointStar(ShapeRenderer sr, float cx, float cy, float rOuter, float rInner, Color base, Color shadow) {
        float angleStep = MathUtils.PI / 5f;
        for (int i = 0; i < 5; i++) {
            float a1 = i * 2 * angleStep - MathUtils.PI / 2f;
            float a2 = a1 + angleStep;
            float a3 = a1 + 2 * angleStep;

            float x1 = cx + (float) Math.cos(a1) * rOuter;
            float y1 = cy + (float) Math.sin(a1) * rOuter;
            float x2 = cx + (float) Math.cos(a2) * rInner;
            float y2 = cy + (float) Math.sin(a2) * rInner;
            float x3 = cx + (float) Math.cos(a3) * rOuter;
            float y3 = cy + (float) Math.sin(a3) * rOuter;

            sr.setColor(shadow);
            sr.triangle(cx, cy, x1, y1, x2, y2);
            sr.setColor(base);
            sr.triangle(cx, cy, x2, y2, x3, y3);
        }
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

        // Derin 3D Zemin Gölgesi
        font.setColor(0f, 0f, 0f, 0.60f);
        font.draw(game.batch, text, x + 3f, y - 4f);
        font.setColor(shadow);
        font.draw(game.batch, text, x, y - 2.5f);

        // Ana Parlak Metin
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
    // DOKUNMA VE ETKİLEŞİM YÖNETİMİ (TOUCH & SPRING FEEDBACK)
    // =========================================================================

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        touchPos.set(screenX, screenY, 0);
        viewport.unproject(touchPos);

        // 1. OYNA
        if (touchPos.x >= 75 && touchPos.x <= 645 && touchPos.y >= 865 && touchPos.y <= 945) {
            pressedButton = 0;
            game.soundEngine.playClick();
            game.triggerHaptic(HapticType.BUTTON_CLICK);
            return true;
        }

        // 2. GÜNLÜK MEYDAN OKUMA
        if (touchPos.x >= 75 && touchPos.x <= 645 && touchPos.y >= 770 && touchPos.y <= 846) {
            pressedButton = 1;
            game.soundEngine.playClick();
            game.triggerHaptic(HapticType.BUTTON_CLICK);
            return true;
        }

        // 3. SEVİYELER
        if (touchPos.x >= 75 && touchPos.x <= 645 && touchPos.y >= 678 && touchPos.y <= 752) {
            pressedButton = 2;
            game.soundEngine.playClick();
            game.triggerHaptic(HapticType.BUTTON_CLICK);
            return true;
        }

        // 4. BÖLÜM EDİTÖRÜ
        if (touchPos.x >= 75 && touchPos.x <= 645 && touchPos.y >= 588 && touchPos.y <= 660) {
            pressedButton = 3;
            game.soundEngine.playClick();
            game.triggerHaptic(HapticType.BUTTON_CLICK);
            return true;
        }

        // 5A. SES / MÜZİK
        if (touchPos.x >= 75 && touchPos.x <= 350 && touchPos.y >= 500 && touchPos.y <= 570) {
            pressedButton = 4;
            boolean newState = !game.soundEngine.isSoundEnabled();
            game.soundEngine.setSoundEnabled(newState);
            game.soundEngine.setMusicEnabled(newState);
            game.saveManager.setSoundEnabled(newState);
            game.soundEngine.playClick();
            game.triggerHaptic(HapticType.BUTTON_CLICK);
            return true;
        }

        // 5B. HAPTİK TİTREŞİM
        if (touchPos.x >= 370 && touchPos.x <= 645 && touchPos.y >= 500 && touchPos.y <= 570) {
            pressedButton = 5;
            boolean newHaptic = !game.saveManager.isHapticsEnabled();
            game.saveManager.setHapticsEnabled(newHaptic);
            game.soundEngine.playClick();
            if (newHaptic) {
                game.triggerHaptic(HapticType.PIN_RELEASE);
            }
            return true;
        }

        // 6. DİL SEÇİCİ (LANGUAGE)
        if (touchPos.x >= 75 && touchPos.x <= 645 && touchPos.y >= 412 && touchPos.y <= 482) {
            pressedButton = 6;
            I18N.toggleLanguage();
            game.saveManager.setLanguage(I18N.getLanguage().getCode());
            game.soundEngine.playClick();
            game.triggerHaptic(HapticType.BUTTON_CLICK);
            return true;
        }

        // 7A. MAĞAZA (SHOP)
        if (touchPos.x >= 75 && touchPos.x <= 350 && touchPos.y >= 295 && touchPos.y <= 365) {
            pressedButton = 7;
            game.soundEngine.playClick();
            game.triggerHaptic(HapticType.BUTTON_CLICK);
            return true;
        }

        // 7B. BAŞARIMLAR (ACHIEVEMENTS)
        if (touchPos.x >= 370 && touchPos.x <= 645 && touchPos.y >= 295 && touchPos.y <= 365) {
            pressedButton = 8;
            game.soundEngine.playClick();
            game.triggerHaptic(HapticType.BUTTON_CLICK);
            return true;
        }

        // ÜST SAĞ: ALTIN KAPSÜLÜ (MAĞAZAYI AÇ)
        if (touchPos.x >= 530 && touchPos.x <= 695 && touchPos.y >= 1205 && touchPos.y <= 1265) {
            game.soundEngine.playClick();
            game.triggerHaptic(HapticType.BUTTON_CLICK);
            game.openShop();
            return true;
        }

        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        touchPos.set(screenX, screenY, 0);
        viewport.unproject(touchPos);

        int releaseButton = pressedButton;
        pressedButton = -1;

        if (releaseButton == 0 && touchPos.x >= 75 && touchPos.x <= 645 && touchPos.y >= 860 && touchPos.y <= 950) {
            int current = game.saveManager.getUnlockedLevel();
            game.startLevel(current);
            return true;
        }

        if (releaseButton == 1 && touchPos.x >= 75 && touchPos.x <= 645 && touchPos.y >= 765 && touchPos.y <= 852) {
            game.openDailyChallenge();
            return true;
        }

        if (releaseButton == 2 && touchPos.x >= 75 && touchPos.x <= 645 && touchPos.y >= 670 && touchPos.y <= 758) {
            game.openLevelSelect();
            return true;
        }

        if (releaseButton == 3 && touchPos.x >= 75 && touchPos.x <= 645 && touchPos.y >= 580 && touchPos.y <= 668) {
            game.openEditor();
            return true;
        }

        if (releaseButton == 7 && touchPos.x >= 75 && touchPos.x <= 350 && touchPos.y >= 290 && touchPos.y <= 370) {
            game.openShop();
            return true;
        }

        if (releaseButton == 8 && touchPos.x >= 370 && touchPos.x <= 645 && touchPos.y >= 290 && touchPos.y <= 370) {
            showToast("Basarimlar yakin zamanda gelecek!");
            return true;
        }

        return false;
    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == com.badlogic.gdx.Input.Keys.C) {
            game.saveManager.addCoins(10000);
            game.soundEngine.playWin();
            game.triggerHaptic(HapticType.VICTORY_PULSE);
            showToast("+10.000 Altın Eklendi! (DEBUG: C)");
            return true;
        }
        if (keycode == com.badlogic.gdx.Input.Keys.U) {
            game.saveManager.unlockLevel(80);
            for (int i = 1; i <= 80; i++) {
                game.saveManager.setStarsForLevel(i, 3);
            }
            game.soundEngine.playWin();
            game.triggerHaptic(HapticType.VICTORY_PULSE);
            showToast("Tüm 80 Seviye Açıldı! (DEBUG: U)");
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
