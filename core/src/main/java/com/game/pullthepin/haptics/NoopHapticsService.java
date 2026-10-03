package com.game.pullthepin.haptics;

/**
 * Masaüstü ve test ortamları için no-op Haptik Servisi.
 */
public class NoopHapticsService implements HapticsService {
    @Override
    public void trigger(HapticType type) {
        // Masaüstü ortamında donanımsal titreşim motoru bulunmadığından pas geçilir
    }

    @Override
    public boolean isAvailable() {
        return false;
    }
}
