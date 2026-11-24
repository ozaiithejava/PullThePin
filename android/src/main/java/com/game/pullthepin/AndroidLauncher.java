package com.game.pullthepin;

import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import java.util.Collections;
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

        // BUG FIX: Pim tutamaçları ekranın sol/sağ kenarına yakın konumlanabiliyor. Android 10+
        // sistem "geri" kaydırma jesti, ekran kenarına yakın dokunuşları oyundan ÖNCE yakalayıp
        // yutabiliyor; bu da jest navigasyonu açık cihazlarda (ör. OxygenOS) kenara yakın
        // tutamaçların "tam çekilememesi" şeklinde algılanan asıl sebepti. Tüm oyun alanını
        // sistem kaydırma hareketi dışına alarak dokunuşların her zaman oyuna ulaşmasını
        // sağlıyoruz.
        excludeSystemGestures();
    }

    private void excludeSystemGestures() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) return; // API 29 altında bu API yok
        final View root = getWindow().getDecorView();
        root.post(new Runnable() {
            @Override
            public void run() {
                int w = root.getWidth();
                int h = root.getHeight();
                if (w <= 0 || h <= 0) return;
                root.setSystemGestureExclusionRects(
                    Collections.singletonList(new Rect(0, 0, w, h))
                );
            }
        });
        root.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom,
                                        int oldLeft, int oldTop, int oldRight, int oldBottom) {
                int w = right - left;
                int h = bottom - top;
                if (w <= 0 || h <= 0) return;
                v.setSystemGestureExclusionRects(
                    Collections.singletonList(new Rect(0, 0, w, h))
                );
            }
        });
    }
}
