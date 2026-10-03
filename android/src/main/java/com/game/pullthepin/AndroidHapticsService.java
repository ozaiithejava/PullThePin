package com.game.pullthepin;

import android.content.Context;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.os.VibratorManager;
import com.game.pullthepin.haptics.HapticsService;

/**
 * Üst Düzey Cihaz Uyumlu Android Haptik Motoru (OnePlus, Samsung, Google Pixel, Xiaomi, Motorola vb.).
 * Android 10+ (API 29+) Primitives (EFFECT_TICK, EFFECT_CLICK, EFFECT_HEAVY_CLICK)
 * ve hassas dalga formu genlik modülasyonu (Amplitude Modulated Waveforms) kullanır.
 */
public class AndroidHapticsService implements HapticsService {

    private final Vibrator vibrator;
    private final boolean hasVibrator;

    public AndroidHapticsService(Context context) {
        Vibrator v = null;
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                VibratorManager vm = (VibratorManager) context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE);
                if (vm != null) {
                    v = vm.getDefaultVibrator();
                }
            }
            if (v == null) {
                v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
            }
        } catch (Throwable ignored) {}

        this.vibrator = v;
        this.hasVibrator = (vibrator != null && vibrator.hasVibrator());
    }

    @Override
    public boolean isAvailable() {
        return hasVibrator;
    }

    @Override
    public void trigger(HapticType type) {
        if (!hasVibrator || vibrator == null || type == null) return;

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                // Android 10+ (API 29+) Üst Düzey Donanımsal Dokunsal Efektler
                switch (type) {
                    case LIGHT_TICK:
                        try {
                            vibrator.vibrate(VibrationEffect.createPredefined(VibrationEffect.EFFECT_TICK));
                        } catch (Throwable e) {
                            vibrateOneShot(12, 60);
                        }
                        break;

                    case BUTTON_CLICK:
                        try {
                            vibrator.vibrate(VibrationEffect.createPredefined(VibrationEffect.EFFECT_CLICK));
                        } catch (Throwable e) {
                            vibrateOneShot(18, 100);
                        }
                        break;

                    case PIN_RELEASE:
                        try {
                            vibrator.vibrate(VibrationEffect.createPredefined(VibrationEffect.EFFECT_HEAVY_CLICK));
                        } catch (Throwable e) {
                            vibrateOneShot(35, 200);
                        }
                        break;

                    case BALL_COLLECT:
                        vibrateOneShot(14, 85);
                        break;

                    case BALL_COLORIZE:
                        vibrateWaveform(new long[]{0, 16, 20, 22}, new int[]{0, 90, 0, 160});
                        break;

                    case PORTAL_WARP:
                        vibrateWaveform(new long[]{0, 18, 25, 25, 20, 35}, new int[]{0, 80, 0, 140, 0, 220});
                        break;

                    case BOUNCER_HIT:
                        vibrateWaveform(new long[]{0, 20, 15, 30}, new int[]{0, 120, 0, 240});
                        break;

                    case EXPLOSION:
                        vibrateWaveform(new long[]{0, 45, 20, 50, 25, 35}, new int[]{0, 255, 0, 190, 0, 100});
                        break;

                    case VICTORY_PULSE:
                        vibrateWaveform(new long[]{0, 30, 40, 35, 40, 45, 50, 75}, new int[]{0, 120, 0, 170, 0, 210, 0, 255});
                        break;

                    case FAILURE_BUZZ:
                        vibrateWaveform(new long[]{0, 60, 50, 70}, new int[]{0, 160, 0, 120});
                        break;

                    case KEY_UNLOCK:
                        try {
                            vibrator.vibrate(VibrationEffect.createPredefined(VibrationEffect.EFFECT_HEAVY_CLICK));
                        } catch (Throwable e) {
                            vibrateWaveform(new long[]{0, 20, 25, 45}, new int[]{0, 150, 0, 255});
                        }
                        break;

                    case MULTIPLIER_SURGE:
                        vibrateWaveform(new long[]{0, 16, 20, 25, 20, 30}, new int[]{0, 100, 0, 180, 0, 240});
                        break;
                }
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                // Android 8.0 - 9.0 (API 26-28)
                switch (type) {
                    case LIGHT_TICK:
                        vibrateOneShot(12, 60);
                        break;
                    case BUTTON_CLICK:
                        vibrateOneShot(18, 100);
                        break;
                    case PIN_RELEASE:
                    case KEY_UNLOCK:
                        vibrateOneShot(35, 200);
                        break;
                    case BALL_COLLECT:
                        vibrateOneShot(15, 80);
                        break;
                    case BALL_COLORIZE:
                        vibrateWaveform(new long[]{0, 20, 20, 25}, new int[]{0, 90, 0, 150});
                        break;
                    case PORTAL_WARP:
                    case BOUNCER_HIT:
                    case MULTIPLIER_SURGE:
                        vibrateWaveform(new long[]{0, 25, 20, 35}, new int[]{0, 120, 0, 220});
                        break;
                    case EXPLOSION:
                        vibrateWaveform(new long[]{0, 50, 25, 60}, new int[]{0, 255, 0, 150});
                        break;
                    case VICTORY_PULSE:
                        vibrateWaveform(new long[]{0, 30, 50, 40, 50, 80}, new int[]{0, 120, 0, 180, 0, 255});
                        break;
                    case FAILURE_BUZZ:
                        vibrateWaveform(new long[]{0, 60, 50, 70}, new int[]{0, 150, 0, 100});
                        break;
                }
            } else {
                // Legacy Android (< API 26)
                switch (type) {
                    case LIGHT_TICK:
                        vibrator.vibrate(10);
                        break;
                    case BUTTON_CLICK:
                    case BALL_COLLECT:
                        vibrator.vibrate(18);
                        break;
                    case PIN_RELEASE:
                    case KEY_UNLOCK:
                    case BALL_COLORIZE:
                        vibrator.vibrate(35);
                        break;
                    case PORTAL_WARP:
                    case BOUNCER_HIT:
                    case MULTIPLIER_SURGE:
                        vibrator.vibrate(new long[]{0, 20, 30, 35}, -1);
                        break;
                    case EXPLOSION:
                        vibrator.vibrate(new long[]{0, 60, 30, 80}, -1);
                        break;
                    case VICTORY_PULSE:
                        vibrator.vibrate(new long[]{0, 40, 60, 40, 60, 90}, -1);
                        break;
                    case FAILURE_BUZZ:
                        vibrator.vibrate(new long[]{0, 70, 50, 80}, -1);
                        break;
                }
            }
        } catch (Throwable ignored) {}
    }

    private void vibrateOneShot(long ms, int amplitude) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            try {
                if (vibrator.hasAmplitudeControl()) {
                    vibrator.vibrate(VibrationEffect.createOneShot(ms, Math.max(1, Math.min(255, amplitude))));
                } else {
                    vibrator.vibrate(VibrationEffect.createOneShot(ms, VibrationEffect.DEFAULT_AMPLITUDE));
                }
            } catch (Throwable e) {
                vibrator.vibrate(ms);
            }
        } else {
            vibrator.vibrate(ms);
        }
    }

    private void vibrateWaveform(long[] timings, int[] amplitudes) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            try {
                if (vibrator.hasAmplitudeControl() && amplitudes != null && amplitudes.length == timings.length) {
                    vibrator.vibrate(VibrationEffect.createWaveform(timings, amplitudes, -1));
                } else {
                    vibrator.vibrate(VibrationEffect.createWaveform(timings, -1));
                }
            } catch (Throwable e) {
                vibrator.vibrate(timings, -1);
            }
        } else {
            vibrator.vibrate(timings, -1);
        }
    }
}
