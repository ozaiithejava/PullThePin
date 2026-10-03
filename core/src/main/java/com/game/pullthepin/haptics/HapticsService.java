package com.game.pullthepin.haptics;

/**
 * Modern Haptik Titreşim Arayüzü (Universal Haptic Feedback Service).
 * Samsung, OnePlus, Google Pixel, Xiaomi ve tüm modern mobil cihazlarda
 * mikroskobik dokunsal geri bildirim ve zengin titreşim dalga formları sunar.
 */
public interface HapticsService {

    enum HapticType {
        LIGHT_TICK,     // Hafif pime dokunma / sürükleme tıkırtısı (Micro Tick)
        PIN_RELEASE,    // Pimin hazneden tamamen çıkış hissi (Satisfying Snap)
        BALL_COLLECT,   // Topun kavanoza düşüş pıtırtısı (Soft Bubble Bump)
        BALL_COLORIZE,  // Renksiz topun sıvıya değip renklenmesi (Liquid Pulse)
        PORTAL_WARP,    // Portal kuantum geçişi titreşimi
        BOUNCER_HIT,    // Trambolin zıplama darbesi
        EXPLOSION,      // Bomba patlaması derin sarsıntısı (Heavy Sub-Bass Rumble)
        VICTORY_PULSE,  // Ağır çekim zafer tamamlanma ritmi (Triumphant Waveform)
        FAILURE_BUZZ,   // Kaybetme çift ikaz titreşimi (Error Buzz)
        BUTTON_CLICK,   // Standart menü buton tıklaması (Crisp UI Click)
        KEY_UNLOCK,     // Anahtar kilit açılış tok mekanik hissi (Key Unlock Snap)
        MULTIPLIER_SURGE // Çoğaltıcı kapıdan geçiş enerji dalgası (Laser Pulse)
    }

    void trigger(HapticType type);

    boolean isAvailable();
}
