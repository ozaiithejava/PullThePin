package com.game.pullthepin;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.game.pullthepin.levels.LevelLoader;

/**
 * Seviye ilerlemesini, yıldız puanlarını, rütbeleri ve ayarları kalıcı olarak saklar.
 */
public class SaveManager {

    private static final String PREF_NAME = "PullThePin_SaveData";
    private static final String KEY_UNLOCKED_LEVEL = "unlocked_level";
    private static final String KEY_LEVEL_STARS_PREFIX = "level_stars_";
    private static final String KEY_SOUND_ENABLED = "sound_enabled";
    private static final String KEY_HAPTICS_ENABLED = "haptics_enabled";
    private static final String KEY_LANGUAGE = "selected_language";

    private Preferences prefs;

    public SaveManager() {
        prefs = Gdx.app.getPreferences(PREF_NAME);
    }

    public String getLanguage() {
        return prefs.getString(KEY_LANGUAGE, "");
    }

    public void setLanguage(String lang) {
        prefs.putString(KEY_LANGUAGE, lang);
        prefs.flush();
    }

    public int getUnlockedLevel() {
        return prefs.getInteger(KEY_UNLOCKED_LEVEL, 1);
    }

    public void unlockLevel(int level) {
        int current = getUnlockedLevel();
        if (level > current) {
            prefs.putInteger(KEY_UNLOCKED_LEVEL, level);
            prefs.flush();
        }
    }

    public int getStarsForLevel(int level) {
        return prefs.getInteger(KEY_LEVEL_STARS_PREFIX + level, 0);
    }

    public void setStarsForLevel(int level, int stars) {
        int currentStars = getStarsForLevel(level);
        if (stars > currentStars) {
            prefs.putInteger(KEY_LEVEL_STARS_PREFIX + level, stars);
            prefs.flush();
        }
    }

    public int getTotalStars() {
        int total = 0;
        for (int i = 1; i <= LevelLoader.TOTAL_LEVELS; i++) {
            total += getStarsForLevel(i);
        }
        return total;
    }

    public int getStarsInWorld(int worldIndex) {
        int start = (worldIndex - 1) * 10 + 1;
        int end = Math.min(worldIndex * 10, LevelLoader.TOTAL_LEVELS);
        int sum = 0;
        for (int i = start; i <= end; i++) {
            sum += getStarsForLevel(i);
        }
        return sum;
    }

    public String getStarRankTitle(boolean isTurkish) {
        int stars = getTotalStars();
        if (stars >= 220) {
            return isTurkish ? "Kozmik İlah" : "Cosmic Supreme";
        } else if (stars >= 170) {
            return isTurkish ? "Efsanevi Sampiyon" : "Legendary Champion";
        } else if (stars >= 120) {
            return isTurkish ? "Kidemli Usta" : "Grandmaster";
        } else if (stars >= 75) {
            return isTurkish ? "Bilye Sihirbazi" : "Marble Wizard";
        } else if (stars >= 30) {
            return isTurkish ? "Bulmaca Ustasi" : "Puzzle Solver";
        } else {
            return isTurkish ? "Cirak" : "Apprentice";
        }
    }

    // --- COIN & DAILY CHALLENGE SİSTEMİ ---
    private static final String KEY_COINS = "player_coins";
    private static final String KEY_DAILY_STREAK = "daily_streak";
    private static final String KEY_LAST_CHALLENGE_DATE = "last_challenge_date";
    private static final String KEY_CHALLENGE_DONE_PREFIX = "challenge_done_";

    public int getCoins() {
        return prefs.getInteger(KEY_COINS, 100); // 100 başlangıç hediyesi
    }

    public void addCoins(int amount) {
        if (amount <= 0) return;
        int current = getCoins();
        prefs.putInteger(KEY_COINS, current + amount);
        prefs.flush();
    }

    public boolean spendCoins(int amount) {
        int current = getCoins();
        if (current >= amount) {
            prefs.putInteger(KEY_COINS, current - amount);
            prefs.flush();
            return true;
        }
        return false;
    }

    public int getDailyStreak() {
        String lastDate = getLastChallengeDate();
        if (lastDate.isEmpty()) return 0;
        String today = com.game.pullthepin.daily.DailyChallengeManager.getTodayKey();
        long diff = getDaysDifference(lastDate, today);
        if (diff > 1) {
            return 0; // Seri bozuldu
        }
        return prefs.getInteger(KEY_DAILY_STREAK, 0);
    }

    public String getLastChallengeDate() {
        return prefs.getString(KEY_LAST_CHALLENGE_DATE, "");
    }

    public boolean isDailyChallengeCompleted(int challengeId, String todayDate) {
        String key = KEY_CHALLENGE_DONE_PREFIX + todayDate + "_" + challengeId;
        return prefs.getBoolean(key, false);
    }

    public void completeDailyChallenge(int challengeId, String todayDate, int rewardCoins) {
        String key = KEY_CHALLENGE_DONE_PREFIX + todayDate + "_" + challengeId;
        if (!prefs.getBoolean(key, false)) {
            prefs.putBoolean(key, true);
            addCoins(rewardCoins);

            // Streak hesaplama
            String lastDate = getLastChallengeDate();
            if (lastDate.isEmpty()) {
                prefs.putInteger(KEY_DAILY_STREAK, 1);
                prefs.putString(KEY_LAST_CHALLENGE_DATE, todayDate);
            } else if (!todayDate.equals(lastDate)) {
                long diff = getDaysDifference(lastDate, todayDate);
                if (diff == 1) {
                    int streak = prefs.getInteger(KEY_DAILY_STREAK, 0);
                    prefs.putInteger(KEY_DAILY_STREAK, streak + 1);
                } else {
                    prefs.putInteger(KEY_DAILY_STREAK, 1);
                }
                prefs.putString(KEY_LAST_CHALLENGE_DATE, todayDate);
            }
            prefs.flush();
        }
    }

    private long getDaysDifference(String dateStr1, String dateStr2) {
        try {
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.US);
            java.util.Date d1 = sdf.parse(dateStr1);
            java.util.Date d2 = sdf.parse(dateStr2);
            long diffMs = Math.abs(d2.getTime() - d1.getTime());
            return diffMs / (24 * 60 * 60 * 1000);
        } catch (Exception e) {
            return 99;
        }
    }

    public int getCompletedDailyChallengesCount(String todayDate) {
        int count = 0;
        for (int i = 0; i < 3; i++) {
            if (isDailyChallengeCompleted(i, todayDate)) count++;
        }
        return count;
    }

    public boolean isSoundEnabled() {
        return prefs.getBoolean(KEY_SOUND_ENABLED, true);
    }

    public void setSoundEnabled(boolean enabled) {
        prefs.putBoolean(KEY_SOUND_ENABLED, enabled);
        prefs.flush();
    }

    public boolean isHapticsEnabled() {
        return prefs.getBoolean(KEY_HAPTICS_ENABLED, true);
    }

    public void setHapticsEnabled(boolean enabled) {
        prefs.putBoolean(KEY_HAPTICS_ENABLED, enabled);
        prefs.flush();
    }

    // --- DAILY REWARD ---
    public String getDailyRewardLastDate() { return prefs.getString("daily_reward_last_date", ""); }
    public void setDailyRewardLastDate(String date) { prefs.putString("daily_reward_last_date", date); prefs.flush(); }
    public int getDailyRewardStreak() { return prefs.getInteger("daily_reward_streak", 0); }
    public void setDailyRewardStreak(int streak) { prefs.putInteger("daily_reward_streak", streak); prefs.flush(); }

    // --- ACHIEVEMENTS ---
    public boolean isAchievementUnlocked(String id) { return prefs.getBoolean("ach_unlocked_" + id, false); }
    public void setAchievementUnlocked(String id) { prefs.putBoolean("ach_unlocked_" + id, true); prefs.flush(); }
    public int getAchievementProgress(String id) { return prefs.getInteger("ach_progress_" + id, 0); }
    public void setAchievementProgress(String id, int value) { prefs.putInteger("ach_progress_" + id, value); prefs.flush(); }
    public int getStat(String statKey) { return prefs.getInteger("stat_" + statKey, 0); }
    public void incrementStat(String statKey, int amount) {
        int current = prefs.getInteger("stat_" + statKey, 0);
        prefs.putInteger("stat_" + statKey, current + amount);
        prefs.flush();
    }

    // --- SKIN SHOP ---
    public boolean isSkinOwned(String skinId) { return prefs.getBoolean("skin_owned_" + skinId, false); }
    public void setSkinOwned(String skinId) { prefs.putBoolean("skin_owned_" + skinId, true); prefs.flush(); }
    public String getEquippedSkin() { return prefs.getString("equipped_skin", "classic"); }
    public void setEquippedSkin(String skinId) { prefs.putString("equipped_skin", skinId); prefs.flush(); }

    public String getEquippedPinSkin() { return prefs.getString("equipped_pin_skin", "pin_classic"); }
    public void setEquippedPinSkin(String skinId) { prefs.putString("equipped_pin_skin", skinId); prefs.flush(); }

    public String getEquippedTrailSkin() { return prefs.getString("equipped_trail_skin", "trail_default"); }
    public void setEquippedTrailSkin(String skinId) { prefs.putString("equipped_trail_skin", skinId); prefs.flush(); }
}
