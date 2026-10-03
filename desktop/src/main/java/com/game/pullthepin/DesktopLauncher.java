package com.game.pullthepin;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

/**
 * Masaüstü Başlatıcı (Desktop Launcher)
 * Geliştirme aşamasında saniyeler içinde oyunu çalıştırmayı sağlar.
 */
public class DesktopLauncher {
    public static void main(String[] arg) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setForegroundFPS(60);
        config.setTitle("Pull The Pin - Mobil Oyun Prototipi (Java / LibGDX)");
        
        // 9:16 Mobil Ekran Simülasyonu
        config.setWindowedMode(450, 800);
        config.setResizable(true);

        new Lwjgl3Application(new PullThePinGame(), config);
    }
}
