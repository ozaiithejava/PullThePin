package com.game.pullthepin;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.game.pullthepin.achievements.AchievementManager;
import com.game.pullthepin.audio.SoundEngine;
import com.game.pullthepin.daily.DailyRewardManager;
import com.game.pullthepin.editor.LevelEditorScreen;
import com.game.pullthepin.screens.AchievementScreen;
import com.game.pullthepin.screens.DailyChallengeScreen;
import com.game.pullthepin.screens.DailyRewardScreen;
import com.game.pullthepin.screens.GameScreen;
import com.game.pullthepin.screens.LevelSelectScreen;
import com.game.pullthepin.screens.MenuScreen;
import com.game.pullthepin.screens.ShopScreen;
import com.game.pullthepin.shop.SkinManager;

/**
 * Pull The Pin - Ana Oyun Yöneticisi.
 * Yüksek Çözünürlüklü Kristal Netliğinde TrueType Font (FreeType) Motoru.
 */
public class PullThePinGame extends Game {

    public SpriteBatch batch;
    public ShapeRenderer shapeRenderer;
    public BitmapFont smallFont;
    public BitmapFont regularFont;
    public BitmapFont titleFont;
    public SoundEngine soundEngine;
    public SaveManager saveManager;
    public SkinManager skinManager;
    public DailyRewardManager dailyRewardManager;
    public AchievementManager achievementManager;
    public com.game.pullthepin.haptics.HapticsService haptics;

    public PullThePinGame() {
        this(new com.game.pullthepin.haptics.NoopHapticsService());
    }

    public PullThePinGame(com.game.pullthepin.haptics.HapticsService hapticsService) {
        this.haptics = (hapticsService != null) ? hapticsService : new com.game.pullthepin.haptics.NoopHapticsService();
    }

    @Override
    public void create() {
        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        soundEngine = new SoundEngine();
        saveManager = new SaveManager();
        skinManager = new SkinManager(saveManager);
        dailyRewardManager = new DailyRewardManager();
        achievementManager = new AchievementManager(saveManager);

        com.game.pullthepin.i18n.I18N.init(saveManager.getLanguage());

        initHighResFonts();

        // Debug Test Modu: Test süreci için bakiye en az 50.000 altına ayarlanır
        if (saveManager.getCoins() < 50000) {
            saveManager.addCoins(50000 - saveManager.getCoins());
        }

        // Ana Menüden Başlat
        setScreen(new MenuScreen(this));
    }

    public void triggerHaptic(com.game.pullthepin.haptics.HapticsService.HapticType type) {
        if (saveManager != null && saveManager.isHapticsEnabled() && haptics != null) {
            haptics.trigger(type);
        }
    }

    private void initHighResFonts() {
        FileHandle fontFile = null;
        String[] possiblePaths = {
                "fonts/font.ttf",
                "android/assets/fonts/font.ttf",
                "core/src/main/resources/fonts/font.ttf",
                "desktop/src/main/resources/fonts/font.ttf",
                "desktop/assets/fonts/font.ttf",
                "C:\\Windows\\Fonts\\segoeuib.ttf",
                "C:\\Windows\\Fonts\\arialbd.ttf"
        };

        for (String p : possiblePaths) {
            try {
                FileHandle fh = Gdx.files.internal(p);
                if (fh.exists()) { fontFile = fh; break; }
                fh = Gdx.files.classpath(p);
                if (fh.exists()) { fontFile = fh; break; }
                fh = Gdx.files.absolute(p);
                if (fh.exists()) { fontFile = fh; break; }
            } catch (Exception ignored) {}
        }

        if (fontFile != null && fontFile.exists()) {
            try {
                FreeTypeFontGenerator generator = new FreeTypeFontGenerator(fontFile);
                FreeTypeFontGenerator.FreeTypeFontParameter param = new FreeTypeFontGenerator.FreeTypeFontParameter();
                param.characters = FreeTypeFontGenerator.DEFAULT_CHARS + "çÇğĞıİöÖşŞüÜ<>+-[]/()!?:;*&%#@1234567890=|_~$^.,'\"\\•★⭐✦►◄▲▼✔✖🔒🔑";
                param.minFilter = Texture.TextureFilter.Linear;
                param.magFilter = Texture.TextureFilter.Linear;
                param.gamma = 1.2f;

                // Küçük Font (20px)
                param.size = 20;
                smallFont = generator.generateFont(param);

                // Buton & HUD Fontu (30px)
                param.size = 30;
                regularFont = generator.generateFont(param);

                // Başlık Fontu (48px)
                param.size = 48;
                titleFont = generator.generateFont(param);

                generator.dispose();
                return;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // Yedek Fontlar
        smallFont = new BitmapFont();
        smallFont.getData().setScale(1.2f);
        smallFont.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        regularFont = new BitmapFont();
        regularFont.getData().setScale(1.8f);
        regularFont.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        titleFont = new BitmapFont();
        titleFont.getData().setScale(2.5f);
        titleFont.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
    }

    public void startLevel(int level) {
        setScreen(new GameScreen(this, level));
    }

    public void openLevelSelect() {
        setScreen(new LevelSelectScreen(this));
    }

    public void openEditor() {
        setScreen(new LevelEditorScreen(this));
    }

    public void openDailyChallenge() {
        setScreen(new DailyChallengeScreen(this));
    }

    public void startDailyChallenge(com.game.pullthepin.daily.DailyChallengeManager.DailyChallenge challenge) {
        setScreen(new GameScreen(this, challenge.levelNumber, challenge));
    }

    public void openMenu() {
        setScreen(new MenuScreen(this));
    }

    public void openDailyReward() {
        setScreen(new DailyRewardScreen(this));
    }

    public void openAchievements() {
        setScreen(new AchievementScreen(this));
    }

    public void openShop() {
        setScreen(new ShopScreen(this));
    }

    @Override
    public void dispose() {
        if (batch != null) batch.dispose();
        if (shapeRenderer != null) shapeRenderer.dispose();
        if (smallFont != null) smallFont.dispose();
        if (regularFont != null) regularFont.dispose();
        if (titleFont != null) titleFont.dispose();
        if (soundEngine != null) soundEngine.dispose();
        if (getScreen() != null) getScreen().dispose();
    }
}
