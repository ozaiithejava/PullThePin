package com.game.pullthepin.daily;

import com.badlogic.gdx.utils.Array;
import com.game.pullthepin.SaveManager;
import com.game.pullthepin.levels.LevelLoader;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

/**
 * Günlük Meydan Okuma Yöneticisi (Daily Challenge System).
 * Her gün saat 00:00'da deterministik tarih tohumu (date seed) ile 3 benzersiz meydan okuma üretir.
 * Yerçekimi, hız, %100 kusursuzluk ve özel mutasyonlar içerir.
 */
public class DailyChallengeManager {

    public enum ChallengeModifier {
        LOW_GRAVITY("Ay Yercekimi", "Moon Gravity", "Bilyeler yavasca suzulur (Dusuk Cekim)", "Balls float gently (Low Gravity)", -5.0f, false),
        TURBO_SPEED("Turbo Hiz", "Turbo Speed", "2X Hizli yercekimi ve yuksek tempo!", "2X Gravity & extreme high tempo!", -26.0f, false),
        DYNAMIC_WIND("Ruzgar Akintisi", "Dynamic Wind", "Dalgalanan ruzgarlar bilyeleri savurur!", "Floating crosswinds push balls around!", -16.0f, false),
        HEAVY_GRAVITY("Agir Cekim", "Heavy Gravity", "Bilyeler cok agir ve hizla duser!", "Heavy lead physics with high gravity!", -36.0f, false),
        PRECISION_100("Kusursuz Isabet", "Perfect Aim", "Hicbir bilye kaybedilemez (%%100)", "No balls can be lost (100%%)", -22.0f, true),
        MEGA_BOUNCER("Mega Yaylanma", "Mega Bouncer", "Bilyeler ekstra esnek ve zipkir!", "Super elastic bouncy physics!", -14.0f, false);

        public final String titleTR;
        public final String titleEN;
        public final String descTR;
        public final String descEN;
        public final float gravityY;
        public final boolean requireAllBalls;

        ChallengeModifier(String titleTR, String titleEN, String descTR, String descEN, float gravityY, boolean requireAllBalls) {
            this.titleTR = titleTR;
            this.titleEN = titleEN;
            this.descTR = descTR;
            this.descEN = descEN;
            this.gravityY = gravityY;
            this.requireAllBalls = requireAllBalls;
        }

        public String getTitle(boolean isTurkish) {
            return isTurkish ? titleTR : titleEN;
        }

        public String getDesc(boolean isTurkish) {
            return isTurkish ? descTR : descEN;
        }
    }

    public static class DailyChallenge {
        public int id; // 0: Kolay, 1: Orta, 2: Zor
        public int levelNumber;
        public ChallengeModifier modifier;
        public int rewardCoins;
        public boolean isCompleted;

        public DailyChallenge(int id, int levelNumber, ChallengeModifier modifier, int rewardCoins, boolean isCompleted) {
            this.id = id;
            this.levelNumber = levelNumber;
            this.modifier = modifier;
            this.rewardCoins = rewardCoins;
            this.isCompleted = isCompleted;
        }

        public String getDifficultyLabel(boolean isTurkish) {
            if (id == 0) return isTurkish ? "KOLAY" : "EASY";
            if (id == 1) return isTurkish ? "ORTA" : "MEDIUM";
            return isTurkish ? "ZOR / USTA" : "HARD / MASTER";
        }
    }

    public static String getTodayKey() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        return sdf.format(new Date());
    }

    public static String getTodayDisplayDate(boolean isTurkish) {
        Calendar cal = Calendar.getInstance();
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);

        String[] trMonths = {"Ocak", "Subat", "Mart", "Nisan", "Mayis", "Haziran", "Temmuz", "Agustos", "Eylul", "Ekim", "Kasim", "Aralik"};
        String[] enMonths = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};

        String monthStr = isTurkish ? trMonths[month] : enMonths[month];
        return String.format("%d %s %d", day, monthStr, year);
    }

    public static Array<DailyChallenge> getDailyChallenges(SaveManager saveManager) {
        Array<DailyChallenge> list = new Array<>();
        String todayKey = getTodayKey();

        // Deterministik tohum (Tüm kullanıcılar aynı gün aynı görevleri alır)
        long seed = todayKey.hashCode();
        Random rand = new Random(seed);

        ChallengeModifier[] allMods = ChallengeModifier.values();

        // Görev 1: Kolay (Seviye 1 - 20)
        int lvl1 = 1 + rand.nextInt(20);
        ChallengeModifier mod1 = rand.nextBoolean() ? ChallengeModifier.LOW_GRAVITY : ChallengeModifier.TURBO_SPEED;
        boolean done1 = saveManager.isDailyChallengeCompleted(0, todayKey);
        list.add(new DailyChallenge(0, lvl1, mod1, 50, done1));

        // Görev 2: Orta (Seviye 21 - 40)
        int lvl2 = 21 + rand.nextInt(20);
        ChallengeModifier mod2 = rand.nextBoolean() ? ChallengeModifier.DYNAMIC_WIND : ChallengeModifier.MEGA_BOUNCER;
        boolean done2 = saveManager.isDailyChallengeCompleted(1, todayKey);
        list.add(new DailyChallenge(1, lvl2, mod2, 100, done2));

        // Görev 3: Zor / Usta (Seviye 41 - 60)
        int lvl3 = 41 + rand.nextInt(LevelLoader.TOTAL_LEVELS - 40);
        ChallengeModifier mod3 = rand.nextBoolean() ? ChallengeModifier.HEAVY_GRAVITY : ChallengeModifier.PRECISION_100;
        boolean done3 = saveManager.isDailyChallengeCompleted(2, todayKey);
        list.add(new DailyChallenge(2, lvl3, mod3, 200, done3));

        return list;
    }
}
