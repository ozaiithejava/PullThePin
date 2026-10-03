package com.game.pullthepin;

import android.os.Bundle;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

/**
 * Android Başlatıcı (Android Launcher)
 * Telefonda veya emülatörde APK olarak çalışır.
 */
public class AndroidLauncher extends AndroidApplication {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        config.useImmersiveMode = true; // Tam ekran oyun modu (Google Play immersive sticky)
        config.useAccelerometer = true; // Gerçekçi sıvı eğim ve jiroskop fiziği
        config.useCompass = false;
        
        initialize(new PullThePinGame(new AndroidHapticsService(this)), config);
    }
}
