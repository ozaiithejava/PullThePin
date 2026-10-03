package com.game.pullthepin.achievements;

import com.game.pullthepin.SaveManager;

public class AchievementManager {
    public static final String ACH_FIRST_LEVEL = "ach_first_level";
    public static final String ACH_10_LEVELS = "ach_10_levels";
    public static final String ACH_50_LEVELS = "ach_50_levels";
    public static final String ACH_ALL_LEVELS = "ach_all_levels";
    public static final String ACH_BOMB_MASTER = "ach_bomb_master";
    public static final String ACH_PERFECT_3STAR = "ach_perfect_3star";
    public static final String ACH_COIN_100 = "ach_coin_100";
    public static final String ACH_COIN_1000 = "ach_coin_1000";
    public static final String ACH_STREAK_7 = "ach_streak_7";
    public static final String ACH_SPEED_DEMON = "ach_speed_demon";

    public static class Achievement {
        public final String id;
        public final String nameTR, nameEN;
        public final String descTR, descEN;
        public final int rewardCoins;
        public final int targetProgress; // 0 = single-trigger, >0 = progress-based
        public Achievement(String id, String nameTR, String nameEN, String descTR, String descEN, int reward, int target) {
            this.id=id; this.nameTR=nameTR; this.nameEN=nameEN;
            this.descTR=descTR; this.descEN=descEN;
            this.rewardCoins=reward; this.targetProgress=target;
        }
    }

    public static final Achievement[] ALL = {
        new Achievement(ACH_FIRST_LEVEL,"Ilk Adim","First Step","Ilk bolumu tamamla","Complete first level",50,0),
        new Achievement(ACH_10_LEVELS,"Pim Ustasi","Pin Master","10 bolum tamamla","Complete 10 levels",100,10),
        new Achievement(ACH_50_LEVELS,"Bulmaca Kahramani","Puzzle Hero","50 bolum tamamla","Complete 50 levels",300,50),
        new Achievement(ACH_ALL_LEVELS,"Kozmik Tanri","Cosmic God","Tum bolumler","Complete all levels",1000,80),
        new Achievement(ACH_BOMB_MASTER,"Bomba Ustasi","Bomb Master","10 bomba imha et","Destroy 10 bombs",150,10),
        new Achievement(ACH_PERFECT_3STAR,"Mukemmel","Perfect","10 bolumu 3 yildizla bitir","3-star 10 levels",200,10),
        new Achievement(ACH_COIN_100,"Kumbara","Piggy Bank","100 jeton kazan","Earn 100 coins",50,100),
        new Achievement(ACH_COIN_1000,"Altin Yagmuru","Gold Rain","1000 jeton kazan","Earn 1000 coins",200,1000),
        new Achievement(ACH_STREAK_7,"Sadik Oyuncu","Loyal Player","7 gunluk seri","7-day login streak",500,7),
        new Achievement(ACH_SPEED_DEMON,"Isik Hizi","Speed Demon","Bir bolumu 5 saniyede bitir","Finish a level in 5 seconds",250,0),
    };

    private SaveManager saveManager;
    private String pendingNotification = null;

    public AchievementManager(SaveManager saveManager) {
        this.saveManager = saveManager;
    }

    public boolean isUnlocked(String id) {
        return saveManager.isAchievementUnlocked(id);
    }

    public int getProgress(String id) {
        return saveManager.getAchievementProgress(id);
    }

    private void tryUnlock(String id, int rewardCoins, String name) {
        if (!isUnlocked(id)) {
            saveManager.setAchievementUnlocked(id);
            saveManager.addCoins(rewardCoins);
            pendingNotification = name;
        }
    }

    public String getPendingNotification() {
        String n = pendingNotification;
        pendingNotification = null;
        return n;
    }

    public int getUnlockedCount() {
        int count = 0;
        for (Achievement a : ALL) if (isUnlocked(a.id)) count++;
        return count;
    }

    public void onLevelComplete(int levelNumber, int stars, float timeSeconds, int ballsCollected, int bombsDestroyed) {
        // First level
        tryUnlock(ACH_FIRST_LEVEL, 50, "Ilk Adim");

        // Level count
        saveManager.incrementStat("levels_completed", 1);
        int totalLevels = saveManager.getStat("levels_completed");
        saveManager.setAchievementProgress(ACH_10_LEVELS, Math.min(totalLevels, 10));
        if (totalLevels >= 10) tryUnlock(ACH_10_LEVELS, 100, "Pim Ustasi");
        saveManager.setAchievementProgress(ACH_50_LEVELS, Math.min(totalLevels, 50));
        if (totalLevels >= 50) tryUnlock(ACH_50_LEVELS, 300, "Bulmaca Kahramani");
        saveManager.setAchievementProgress(ACH_ALL_LEVELS, Math.min(totalLevels, 80));
        if (totalLevels >= 80) tryUnlock(ACH_ALL_LEVELS, 1000, "Kozmik Tanri");

        // Bombs
        if (bombsDestroyed > 0) {
            saveManager.incrementStat("bombs_destroyed", bombsDestroyed);
            int totalBombs = saveManager.getStat("bombs_destroyed");
            saveManager.setAchievementProgress(ACH_BOMB_MASTER, Math.min(totalBombs, 10));
            if (totalBombs >= 10) tryUnlock(ACH_BOMB_MASTER, 150, "Bomba Ustasi");
        }

        // 3-star
        if (stars == 3) {
            saveManager.incrementStat("three_star_levels", 1);
            int total3 = saveManager.getStat("three_star_levels");
            saveManager.setAchievementProgress(ACH_PERFECT_3STAR, Math.min(total3, 10));
            if (total3 >= 10) tryUnlock(ACH_PERFECT_3STAR, 200, "Mukemmel");
        }

        // Speed
        if (timeSeconds <= 5f) tryUnlock(ACH_SPEED_DEMON, 250, "Isik Hizi");
    }

    public void onCoinsChanged(int totalCoins) {
        if (totalCoins >= 100) tryUnlock(ACH_COIN_100, 50, "Kumbara");
        if (totalCoins >= 1000) tryUnlock(ACH_COIN_1000, 200, "Altin Yagmuru");
    }

    public void onStreakChanged(int streak) {
        saveManager.setAchievementProgress(ACH_STREAK_7, Math.min(streak, 7));
        if (streak >= 7) tryUnlock(ACH_STREAK_7, 500, "Sadik Oyuncu");
    }
}
