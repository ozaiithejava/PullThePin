package com.game.pullthepin.daily;

import com.game.pullthepin.SaveManager;

public class DailyRewardManager {
    private static final int[] REWARD_COINS = {50, 100, 150, 200, 250, 300, 500};
    private static final boolean[] IS_SPECIAL = {false, false, true, false, true, false, true};

    public String getTodayKey() {
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.US);
        return sdf.format(new java.util.Date());
    }

    public boolean canClaimToday(SaveManager saveManager) {
        String lastDate = saveManager.getDailyRewardLastDate();
        String today = getTodayKey();
        return !today.equals(lastDate);
    }

    public int getCurrentStreak(SaveManager saveManager) {
        return saveManager.getDailyRewardStreak();
    }

    public int getRewardForDay(int dayInCycle) { // dayInCycle 0-6
        return REWARD_COINS[dayInCycle % 7];
    }

    public boolean isSpecialDay(int dayInCycle) {
        return IS_SPECIAL[dayInCycle % 7];
    }

    // Returns coins awarded
    public int claimTodayReward(SaveManager saveManager) {
        if (!canClaimToday(saveManager)) return 0;
        String today = getTodayKey();
        String lastDate = saveManager.getDailyRewardLastDate();
        int streak = saveManager.getDailyRewardStreak();
        // Check if streak continues or resets
        if (!lastDate.isEmpty()) {
            try {
                java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.US);
                java.util.Date d1 = sdf.parse(lastDate);
                java.util.Date d2 = sdf.parse(today);
                long diff = (d2.getTime() - d1.getTime()) / (24*60*60*1000L);
                if (diff > 1) streak = 0; // streak broken
            } catch (Exception e) { streak = 0; }
        }
        int dayIndex = streak % 7;
        int coins = REWARD_COINS[dayIndex];
        streak++;
        saveManager.setDailyRewardStreak(streak);
        saveManager.setDailyRewardLastDate(today);
        saveManager.addCoins(coins);
        return coins;
    }
}
