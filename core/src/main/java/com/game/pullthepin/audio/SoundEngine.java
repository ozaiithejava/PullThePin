package com.game.pullthepin.audio;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.AudioDevice;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Disposable;
import com.game.pullthepin.theme.WorldTheme;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Dinlendirici (Chill / ASMR) ve Zengin Ses & Dinamik Müzik Motoru.
 * - 6 Farklı Dünya Temasına Uyumlu Prosedürel Dinamik Müzik Sentezleyici
 * - Kombo Melodi Çanları (Combo Chimes), Portallar, Trambolinler, Yıldız Fanfarları ve Cam Bilye Tıkırtıları.
 */
public class SoundEngine implements Disposable {

    private Sound pinPullSound;
    private Sound marbleClinkSound;
    private Sound bubblePopSound;
    private Sound zenChimeSound;
    private Sound softExplosionSound;
    private Sound chillWinSound;
    private Sound chillFailSound;
    private Sound clickSound;
    private Sound bouncerSound;
    private Sound portalSound;
    private Sound multiplierSound;
    private Sound keyUnlockSound;
    private Sound colorGateSound;
    private Sound laserZapSound;
    private Sound gearTickSound;
    private Sound acidSizzleSound;
    private Sound steamHissSound;
    private Sound windTurbineSound;
    private Sound[] comboChimeSounds = new Sound[8];
    private Sound[] starFanfareSounds = new Sound[3];

    private boolean soundEnabled = true;
    private boolean musicEnabled = true;

    // Dinamik Müzik Sentezleyicisi
    private AudioDevice musicDevice;
    private Thread musicThread;
    private volatile boolean isMusicRunning = false;
    private volatile WorldTheme.ThemeType currentTheme = WorldTheme.ThemeType.CYBER_LAB;

    public SoundEngine() {
        try {
            generateSFX();
            startDynamicMusicSynth();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setTheme(WorldTheme.ThemeType theme) {
        if (theme != null) {
            this.currentTheme = theme;
        }
    }

    private void generateSFX() {
        // Yumuşak ve pürüzsüz pim çekme sesi
        pinPullSound = createProceduralSound(generateSmoothSweep(280, 640, 0.13f, 0.7f));
        // Cam bilye / mermer çarpışma tıkırtısı (ASMR Glass Marble Clink)
        marbleClinkSound = createProceduralSound(generateMarbleClink());
        // Yumuşak baloncuk pıtırtısı
        bubblePopSound = createProceduralSound(generateBubblePop());
        // Renklenince çalan büyülü zen çanı
        zenChimeSound = createProceduralSound(generateZenChime());
        // Sinematik derin sub-bass patlama
        softExplosionSound = createProceduralSound(generateSoftBoom(0.5f));
        // Dinlendirici zafer melodisi
        chillWinSound = createProceduralSound(generateChillWinChord());
        // Yumuşak mağlubiyet melodisi
        chillFailSound = createProceduralSound(generateChillFailChord());
        // Modern temiz UI tık sesi
        clickSound = createProceduralSound(generateCleanClick());
        // Trambolin zıplama sesi (Resonant Rubber Boing)
        bouncerSound = createProceduralSound(generateBouncerBoing());
        // Portal ışınlanma sesi (Quantum Warp Whoosh)
        portalSound = createProceduralSound(generatePortalWarp());
        // Çoğaltıcı kapı yükselen akor sesi
        multiplierSound = createProceduralSound(generateMultiplierArpeggio());
        // Anahtar kilit açma mekanik tıkırtısı
        keyUnlockSound = createProceduralSound(generateKeyUnlockClick());
        // Prizmatik renk lazer geçit sesi
        colorGateSound = createProceduralSound(generateColorGateWhoosh());
        // Kuantum ölümcül lazer buharlaşma sesi
        laserZapSound = createProceduralSound(generateLaserZap());
        gearTickSound = createProceduralSound(generateGearTick());
        // Zehirli asit cızırtı ve erime sesi
        acidSizzleSound = createProceduralSound(generateAcidSizzle());
        // Kimyasal nötralizasyon buhar tıslaması
        steamHissSound = createProceduralSound(generateSteamHiss());
        // Rüzgar türbini aerodinamik hava akımı sesi
        windTurbineSound = createProceduralSound(generateWindWhoosh());

        // 8 Notalı Yükselen Kombo Çanları (C5, D5, E5, G5, A5, C6, D6, E6)
        float[] comboFreqs = {523.25f, 587.33f, 659.25f, 783.99f, 880.00f, 1046.50f, 1174.66f, 1318.51f};
        for (int i = 0; i < comboFreqs.length; i++) {
            comboChimeSounds[i] = createProceduralSound(generateSingleChime(comboFreqs[i], 0.28f));
        }

        // Yıldız Kazanma Fanfarları (1. Yıldız: C5-G5, 2. Yıldız: E5-C6, 3. Yıldız: G5-E6 Fanfar)
        float[][] starChords = {
                {523.25f, 783.99f},
                {659.25f, 1046.50f},
                {783.99f, 1046.50f, 1318.51f}
        };
        for (int i = 0; i < 3; i++) {
            starFanfareSounds[i] = createProceduralSound(generateChordChime(starChords[i], 0.45f));
        }
    }

    // =========================================================================
    // DİNAMİK 6-TEMA PROSEDÜREL MÜZİK SENTEZLEYİCİSİ
    // =========================================================================
    private void startDynamicMusicSynth() {
        try {
            musicDevice = Gdx.audio.newAudioDevice(22050, true);
            isMusicRunning = true;

            musicThread = new Thread(() -> {
                int sampleRate = 22050;
                short[] buffer = new short[4410]; // 0.2 saniyelik tampon

                float phase = 0f;
                float leadFreq = 440f;
                float leadPhase = 0f;
                float leadEnv = 0f;
                int tickCounter = 0;
                int chordIdx = 0;

                while (isMusicRunning) {
                    if (!musicEnabled || !soundEnabled) {
                        try { Thread.sleep(100); } catch (InterruptedException ignored) {}
                        continue;
                    }

                    // Temaya göre müzik gamı ve akor dizilimleri
                    float[][] chordProgression;
                    float[] leadScale;
                    float padVolume = 0.045f;
                    float leadVolume = 0.12f;

                    switch (currentTheme) {
                        case ATLANTIS_OCEAN:
                            // Derin Deniz & Akvaryum Huzuru (Dorian / Lydian Akorları)
                            chordProgression = new float[][]{
                                    {220.00f, 261.63f, 329.63f, 392.00f}, // Am7
                                    {174.61f, 220.00f, 261.63f, 329.63f}, // Fmaj7
                                    {196.00f, 246.94f, 293.66f, 392.00f}, // G7
                                    {164.81f, 207.65f, 246.94f, 329.63f}  // Em7
                            };
                            leadScale = new float[]{329.63f, 392.00f, 440.00f, 523.25f, 659.25f, 783.99f};
                            padVolume = 0.05f;
                            leadVolume = 0.11f;
                            break;

                        case MAGMA_FORGE:
                            // Volkanik Gizem & Derin Rezonans (Karanlık Minör Gerilim)
                            chordProgression = new float[][]{
                                    {146.83f, 220.00f, 293.66f, 349.23f}, // Dm
                                    {130.81f, 196.00f, 261.63f, 329.63f}, // C
                                    {116.54f, 174.61f, 233.08f, 293.66f}, // Bb
                                    {110.00f, 164.81f, 220.00f, 277.18f}  // A
                            };
                            leadScale = new float[]{293.66f, 349.23f, 392.00f, 440.00f, 523.25f, 587.33f};
                            padVolume = 0.055f;
                            leadVolume = 0.09f;
                            break;

                        case COSMIC_NEBULA:
                            // Kozmik Ethereal & Yıldız Tozu (Lydian Maj9 Uzay Tınıları)
                            chordProgression = new float[][]{
                                    {261.63f, 329.63f, 392.00f, 493.88f, 587.33f}, // Cmaj9
                                    {293.66f, 369.99f, 440.00f, 554.37f},          // Dmaj7
                                    {220.00f, 277.18f, 329.63f, 415.30f},          // Amaj7
                                    {196.00f, 246.94f, 293.66f, 369.99f}           // Gmaj7
                            };
                            leadScale = new float[]{523.25f, 587.33f, 659.25f, 783.99f, 880.00f, 1046.50f};
                            padVolume = 0.042f;
                            leadVolume = 0.13f;
                            break;

                        case EMERALD_ZEN:
                            // Kadim Zen & Bambu Huzuru (Pentatonik Majör / Koto Tınısı)
                            chordProgression = new float[][]{
                                    {261.63f, 329.63f, 392.00f, 523.25f}, // C
                                    {196.00f, 261.63f, 293.66f, 392.00f}, // G
                                    {220.00f, 261.63f, 329.63f, 440.00f}, // Am
                                    {174.61f, 220.00f, 261.63f, 349.23f}  // F
                            };
                            leadScale = new float[]{392.00f, 440.00f, 523.25f, 659.25f, 783.99f, 880.00f};
                            padVolume = 0.045f;
                            leadVolume = 0.14f;
                            break;

                        case SOLAR_MIRAGE:
                            // Altın Zirve & Güneş İhtişamı (Görkemli Majör Zafer Akorları)
                            chordProgression = new float[][]{
                                    {261.63f, 329.63f, 392.00f, 523.25f},
                                    {329.63f, 392.00f, 493.88f, 659.25f},
                                    {349.23f, 440.00f, 523.25f, 698.46f},
                                    {392.00f, 493.88f, 587.33f, 783.99f}
                            };
                            leadScale = new float[]{523.25f, 659.25f, 783.99f, 987.77f, 1046.50f, 1318.51f};
                            padVolume = 0.048f;
                            leadVolume = 0.13f;
                            break;

                        case TOXIC_BIOSPHERE:
                            // Toksik Asit & Prizmatik Atmosfer
                            chordProgression = new float[][]{
                                    {196.00f, 246.94f, 293.66f, 370.00f},
                                    {174.61f, 220.00f, 261.63f, 329.63f},
                                    {220.00f, 261.63f, 329.63f, 392.00f},
                                    {196.00f, 246.94f, 293.66f, 392.00f}
                            };
                            leadScale = new float[]{370.00f, 392.00f, 440.00f, 493.88f, 587.33f, 659.25f};
                            padVolume = 0.046f;
                            leadVolume = 0.12f;
                            break;

                        case SKY_JETSTREAM:
                            // Yüksek İrtifa & Aerodinamik Rüzgar
                            chordProgression = new float[][]{
                                    {261.63f, 329.63f, 392.00f, 523.25f},
                                    {293.66f, 369.99f, 440.00f, 587.33f},
                                    {329.63f, 392.00f, 493.88f, 659.25f},
                                    {349.23f, 440.00f, 523.25f, 698.46f}
                            };
                            leadScale = new float[]{523.25f, 587.33f, 659.25f, 783.99f, 880.00f, 1046.50f};
                            padVolume = 0.045f;
                            leadVolume = 0.13f;
                            break;

                        case CHAOS_REACTOR:
                            // Kuantum Kaos Reaktörü (Fütüristik Phrygian / Cyber Synth)
                            chordProgression = new float[][]{
                                    {196.00f, 261.63f, 311.13f, 392.00f}, // Gmsus
                                    {174.61f, 233.08f, 277.18f, 349.23f}, // Fsus
                                    {207.65f, 261.63f, 311.13f, 415.30f}, // Abmaj7
                                    {196.00f, 246.94f, 293.66f, 392.00f}  // G7
                            };
                            leadScale = new float[]{392.00f, 415.30f, 523.25f, 622.25f, 783.99f, 830.61f};
                            padVolume = 0.052f;
                            leadVolume = 0.13f;
                            break;

                        case GRANDMASTER_APEX:
                            // Büyük Final (Epik Master Gamı / Lydian Astral)
                            chordProgression = new float[][]{
                                    {261.63f, 329.63f, 392.00f, 493.88f, 659.25f}, // Cmaj9
                                    {329.63f, 415.30f, 493.88f, 659.25f},          // Emaj7
                                    {349.23f, 440.00f, 523.25f, 659.25f},          // Fmaj9
                                    {392.00f, 493.88f, 587.33f, 783.99f}           // G9
                            };
                            leadScale = new float[]{523.25f, 659.25f, 783.99f, 987.77f, 1046.50f, 1318.51f, 1567.98f};
                            padVolume = 0.055f;
                            leadVolume = 0.14f;
                            break;

                        case CYBER_LAB:
                        default:
                            // Siber Synthwave & Chill Lofi
                            chordProgression = new float[][]{
                                    {261.63f, 329.63f, 392.00f, 493.88f}, // Cmaj7
                                    {220.00f, 261.63f, 329.63f, 392.00f}, // Am7
                                    {174.61f, 261.63f, 329.63f, 392.00f}, // Fmaj7
                                    {196.00f, 246.94f, 293.66f, 392.00f}  // G7
                            };
                            leadScale = new float[]{261.63f, 293.66f, 329.63f, 392.00f, 440.00f, 523.25f, 587.33f};
                            padVolume = 0.045f;
                            leadVolume = 0.12f;
                            break;
                    }

                    // Her 2 saniyede bir yeni akor ve melodi notası
                    if (tickCounter % 10 == 0) {
                        chordIdx = (chordIdx + 1) % chordProgression.length;
                        leadFreq = leadScale[MathUtils.random(leadScale.length - 1)];
                        leadEnv = 1.0f;
                    }
                    tickCounter++;

                    float[] currentChord = chordProgression[chordIdx];

                    for (int i = 0; i < buffer.length; i++) {
                        // 1. Sıcak Yumuşak Ambient Pad
                        float padSample = 0f;
                        for (float freq : currentChord) {
                            padSample += (float) Math.sin(2 * Math.PI * freq * (i / (float) sampleRate) + phase) * padVolume;
                        }

                        // 2. Tatlı Zen Melodi Notaları (Pluck / Bell)
                        leadEnv *= 0.99988f;
                        float leadSample = (float) Math.sin(2 * Math.PI * leadFreq * (i / (float) sampleRate) + leadPhase)
                                         * leadEnv * leadVolume;

                        float mix = padSample + leadSample;
                        mix = MathUtils.clamp(mix, -0.85f, 0.85f);
                        buffer[i] = (short) (mix * 32767);
                    }

                    phase += 0.05f;
                    leadPhase += 0.05f;
                    musicDevice.writeSamples(buffer, 0, buffer.length);
                }
            }, "DynamicThemeMusicSynth");

            musicThread.setDaemon(true);
            musicThread.start();
        } catch (Exception ignored) {}
    }

    // =========================================================================
    // SFX SES ÜRETEÇLERİ
    // =========================================================================

    private byte[] generateSingleChime(float freq, float duration) {
        int sampleRate = 22050;
        int numSamples = (int) (sampleRate * duration);
        byte[] pcm = new byte[numSamples * 2];

        for (int i = 0; i < numSamples; i++) {
            float t = (float) i / numSamples;
            float env = (float) Math.exp(-t * 6.5f);
            float s1 = (float) Math.sin(2 * Math.PI * freq * (i / (float) sampleRate));
            float s2 = (float) Math.sin(2 * Math.PI * (freq * 2f) * (i / (float) sampleRate)) * 0.35f;
            short sample = (short) ((s1 + s2) * 32767 * 0.45f * env);
            pcm[i * 2] = (byte) (sample & 0xFF);
            pcm[i * 2 + 1] = (byte) ((sample >> 8) & 0xFF);
        }
        return pcm;
    }

    private byte[] generateChordChime(float[] freqs, float duration) {
        int sampleRate = 22050;
        int numSamples = (int) (sampleRate * duration);
        byte[] pcm = new byte[numSamples * 2];

        for (int i = 0; i < numSamples; i++) {
            float t = (float) i / numSamples;
            float env = (float) Math.exp(-t * 4.5f);
            float wave = 0f;
            for (int k = 0; k < freqs.length; k++) {
                wave += (float) Math.sin(2 * Math.PI * freqs[k] * (i / (float) sampleRate)) * (1.0f / (k + 1.2f));
            }
            short sample = (short) (wave * 32767 * 0.42f * env);
            pcm[i * 2] = (byte) (sample & 0xFF);
            pcm[i * 2 + 1] = (byte) ((sample >> 8) & 0xFF);
        }
        return pcm;
    }

    private byte[] generateBouncerBoing() {
        int sampleRate = 22050;
        float duration = 0.16f;
        int numSamples = (int) (sampleRate * duration);
        byte[] pcm = new byte[numSamples * 2];

        for (int i = 0; i < numSamples; i++) {
            float t = (float) i / numSamples;
            // Frekans önce hızla yükselir sonra yay gibi salınır
            float freq = 180f + 320f * (float) Math.sin(t * Math.PI * 0.8f);
            float env = (float) Math.exp(-t * 12.0f);
            short sample = (short) (Math.sin(2 * Math.PI * freq * (i / (float) sampleRate)) * 32767 * 0.55f * env);
            pcm[i * 2] = (byte) (sample & 0xFF);
            pcm[i * 2 + 1] = (byte) ((sample >> 8) & 0xFF);
        }
        return pcm;
    }

    private byte[] generatePortalWarp() {
        int sampleRate = 22050;
        float duration = 0.22f;
        int numSamples = (int) (sampleRate * duration);
        byte[] pcm = new byte[numSamples * 2];

        for (int i = 0; i < numSamples; i++) {
            float t = (float) i / numSamples;
            float freq = 900f - 550f * t; // Aşağı doğru süzülen kuantum frekansı
            float env = (float) Math.sin(t * Math.PI);
            short sample = (short) (Math.sin(2 * Math.PI * freq * (i / (float) sampleRate)) * 32767 * 0.50f * env);
            pcm[i * 2] = (byte) (sample & 0xFF);
            pcm[i * 2 + 1] = (byte) ((sample >> 8) & 0xFF);
        }
        return pcm;
    }

    private byte[] generateMarbleClink() {
        int sampleRate = 22050;
        float duration = 0.09f;
        int numSamples = (int) (sampleRate * duration);
        byte[] pcm = new byte[numSamples * 2];

        float freq1 = MathUtils.random(1400f, 2100f);
        float freq2 = freq1 * 1.58f;

        for (int i = 0; i < numSamples; i++) {
            float t = (float) i / numSamples;
            float env = (float) Math.exp(-t * 22.0f);
            float s1 = (float) Math.sin(2 * Math.PI * freq1 * (i / (float) sampleRate));
            float s2 = (float) Math.sin(2 * Math.PI * freq2 * (i / (float) sampleRate)) * 0.4f;
            short sample = (short) ((s1 + s2) * 32767 * 0.6f * env);
            pcm[i * 2] = (byte) (sample & 0xFF);
            pcm[i * 2 + 1] = (byte) ((sample >> 8) & 0xFF);
        }
        return pcm;
    }

    private byte[] generateBubblePop() {
        int sampleRate = 22050;
        float duration = 0.07f;
        int numSamples = (int) (sampleRate * duration);
        byte[] pcm = new byte[numSamples * 2];

        for (int i = 0; i < numSamples; i++) {
            float t = (float) i / numSamples;
            float freq = 380f + 620f * (float) Math.sin(t * Math.PI);
            float env = (float) Math.exp(-t * 18.0f);
            short sample = (short) (Math.sin(2 * Math.PI * freq * (i / (float) sampleRate)) * 32767 * 0.55f * env);
            pcm[i * 2] = (byte) (sample & 0xFF);
            pcm[i * 2 + 1] = (byte) ((sample >> 8) & 0xFF);
        }
        return pcm;
    }

    private byte[] generateZenChime() {
        int sampleRate = 22050;
        float duration = 0.35f;
        int numSamples = (int) (sampleRate * duration);
        byte[] pcm = new byte[numSamples * 2];

        float[] freqs = {523.25f, 659.25f, 783.99f, 1046.50f};

        for (int i = 0; i < numSamples; i++) {
            float t = (float) i / numSamples;
            float env = (float) Math.exp(-t * 5.5f);
            float wave = 0f;
            for (int k = 0; k < freqs.length; k++) {
                wave += (float) Math.sin(2 * Math.PI * freqs[k] * (i / (float) sampleRate)) * (1.0f / (k + 1.2f));
            }
            short sample = (short) (wave * 32767 * 0.45f * env);
            pcm[i * 2] = (byte) (sample & 0xFF);
            pcm[i * 2 + 1] = (byte) ((sample >> 8) & 0xFF);
        }
        return pcm;
    }

    private byte[] generateSoftBoom(float duration) {
        int sampleRate = 22050;
        int numSamples = (int) (sampleRate * duration);
        byte[] pcm = new byte[numSamples * 2];

        for (int i = 0; i < numSamples; i++) {
            float t = (float) i / numSamples;
            float env = (float) Math.exp(-t * 4.0f);
            float sub = (float) Math.sin(2 * Math.PI * (75f - t * 40f) * (i / (float) sampleRate));
            float noise = MathUtils.random(-0.35f, 0.35f);
            short sample = (short) ((sub * 0.7f + noise * 0.3f) * 32767 * 0.8f * env);
            pcm[i * 2] = (byte) (sample & 0xFF);
            pcm[i * 2 + 1] = (byte) ((sample >> 8) & 0xFF);
        }
        return pcm;
    }

    private byte[] generateChillWinChord() {
        int sampleRate = 22050;
        float[] notes = {523.25f, 659.25f, 783.99f, 987.77f, 1046.50f};
        float noteDur = 0.14f;
        int numSamples = (int) (sampleRate * noteDur * notes.length + sampleRate * 0.4f);
        byte[] pcm = new byte[numSamples * 2];

        for (int n = 0; n < notes.length; n++) {
            int startSample = (int) (n * noteDur * sampleRate);
            int count = numSamples - startSample;
            for (int i = 0; i < count; i++) {
                int totalIdx = startSample + i;
                float t = (float) i / (sampleRate * 0.6f);
                float env = (float) Math.exp(-t * 2.8f);
                float wave = (float) Math.sin(2 * Math.PI * notes[n] * (i / (float) sampleRate));
                short val = (short) (wave * 32767 * 0.32f * env);

                int existing = (pcm[totalIdx * 2 + 1] << 8) | (pcm[totalIdx * 2] & 0xFF);
                int combined = MathUtils.clamp(existing + val, -32767, 32767);
                pcm[totalIdx * 2] = (byte) (combined & 0xFF);
                pcm[totalIdx * 2 + 1] = (byte) ((combined >> 8) & 0xFF);
            }
        }
        return pcm;
    }

    private byte[] generateChillFailChord() {
        int sampleRate = 22050;
        float[] notes = {392.00f, 349.23f, 329.63f, 261.63f};
        float noteDur = 0.12f;
        int numSamples = (int) (sampleRate * noteDur * notes.length + sampleRate * 0.3f);
        byte[] pcm = new byte[numSamples * 2];

        for (int n = 0; n < notes.length; n++) {
            int startSample = (int) (n * noteDur * sampleRate);
            int count = numSamples - startSample;
            for (int i = 0; i < count; i++) {
                int totalIdx = startSample + i;
                float t = (float) i / (sampleRate * 0.5f);
                float env = (float) Math.exp(-t * 3.5f);
                float wave = (float) Math.sin(2 * Math.PI * notes[n] * (i / (float) sampleRate));
                short val = (short) (wave * 32767 * 0.35f * env);

                int existing = (pcm[totalIdx * 2 + 1] << 8) | (pcm[totalIdx * 2] & 0xFF);
                int combined = MathUtils.clamp(existing + val, -32767, 32767);
                pcm[totalIdx * 2] = (byte) (combined & 0xFF);
                pcm[totalIdx * 2 + 1] = (byte) ((combined >> 8) & 0xFF);
            }
        }
        return pcm;
    }

    private byte[] generateSmoothSweep(float startFreq, float endFreq, float duration, float vol) {
        int sampleRate = 22050;
        int numSamples = (int) (sampleRate * duration);
        byte[] pcm = new byte[numSamples * 2];

        for (int i = 0; i < numSamples; i++) {
            float t = (float) i / numSamples;
            float freq = startFreq + (endFreq - startFreq) * t;
            float env = (float) Math.sin(t * Math.PI);
            short sample = (short) (Math.sin(2 * Math.PI * freq * (i / (float) sampleRate)) * 32767 * vol * env);
            pcm[i * 2] = (byte) (sample & 0xFF);
            pcm[i * 2 + 1] = (byte) ((sample >> 8) & 0xFF);
        }
        return pcm;
    }

    private byte[] generateCleanClick() {
        int sampleRate = 22050;
        float duration = 0.035f;
        int numSamples = (int) (sampleRate * duration);
        byte[] pcm = new byte[numSamples * 2];

        for (int i = 0; i < numSamples; i++) {
            float t = (float) i / numSamples;
            float env = (float) Math.exp(-t * 30.0f);
            short sample = (short) (Math.sin(2 * Math.PI * 950 * (i / (float) sampleRate)) * 32767 * 0.45f * env);
            pcm[i * 2] = (byte) (sample & 0xFF);
            pcm[i * 2 + 1] = (byte) ((sample >> 8) & 0xFF);
        }
        return pcm;
    }

    private byte[] generateMultiplierArpeggio() {
        int sampleRate = 22050;
        float[] notes = {523.25f, 659.25f, 783.99f, 1046.50f};
        float noteDur = 0.045f;
        int numSamples = (int) (sampleRate * (noteDur * notes.length + 0.15f));
        byte[] pcm = new byte[numSamples * 2];

        for (int n = 0; n < notes.length; n++) {
            int startSample = (int) (n * noteDur * sampleRate);
            int count = numSamples - startSample;
            for (int i = 0; i < count; i++) {
                int totalIdx = startSample + i;
                float t = (float) i / (sampleRate * 0.25f);
                float env = (float) Math.exp(-t * 6.0f);
                float wave = (float) Math.sin(2 * Math.PI * notes[n] * (i / (float) sampleRate));
                short val = (short) (wave * 32767 * 0.35f * env);

                int existing = (pcm[totalIdx * 2 + 1] << 8) | (pcm[totalIdx * 2] & 0xFF);
                int combined = MathUtils.clamp(existing + val, -32767, 32767);
                pcm[totalIdx * 2] = (byte) (combined & 0xFF);
                pcm[totalIdx * 2 + 1] = (byte) ((combined >> 8) & 0xFF);
            }
        }
        return pcm;
    }

    private byte[] generateKeyUnlockClick() {
        int sampleRate = 22050;
        float duration = 0.14f;
        int numSamples = (int) (sampleRate * duration);
        byte[] pcm = new byte[numSamples * 2];

        for (int i = 0; i < numSamples; i++) {
            float t = (float) i / numSamples;
            float env1 = (float) Math.exp(-t * 40.0f);
            float t2 = Math.max(0, t - 0.05f);
            float env2 = (float) Math.exp(-t2 * 35.0f);

            float s1 = (float) Math.sin(2 * Math.PI * 1600f * (i / (float) sampleRate)) * env1;
            float s2 = (float) Math.sin(2 * Math.PI * 2200f * (i / (float) sampleRate)) * env2;
            short sample = (short) ((s1 * 0.5f + s2 * 0.5f) * 32767 * 0.6f);
            pcm[i * 2] = (byte) (sample & 0xFF);
            pcm[i * 2 + 1] = (byte) ((sample >> 8) & 0xFF);
        }
        return pcm;
    }

    private byte[] generateColorGateWhoosh() {
        int sampleRate = 22050;
        float duration = 0.25f;
        int numSamples = (int) (sampleRate * duration);
        byte[] pcm = new byte[numSamples * 2];

        for (int i = 0; i < numSamples; i++) {
            float t = (float) i / numSamples;
            float env = (float) Math.sin(t * Math.PI);
            float freq = 400f + 600f * (float) Math.sin(t * Math.PI);
            short sample = (short) (Math.sin(2 * Math.PI * freq * (i / (float) sampleRate)) * 32767 * 0.45f * env);
            pcm[i * 2] = (byte) (sample & 0xFF);
            pcm[i * 2 + 1] = (byte) ((sample >> 8) & 0xFF);
        }
        return pcm;
    }

    private Sound createProceduralSound(byte[] pcmData) {
        try {
            byte[] wavBytes = wrapWav(pcmData, 22050, 1, 16);
            FileHandle handle = new FileHandle("chill_sfx_" + MathUtils.random(1000000)) {
                @Override public byte[] readBytes() { return wavBytes; }
                @Override public boolean exists() { return true; }
                @Override public long length() { return wavBytes.length; }
            };
            return Gdx.audio.newSound(handle);
        } catch (Exception e) {
            return null;
        }
    }

    private byte[] wrapWav(byte[] pcmData, int sampleRate, int channels, int bitsPerSample) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        dos.writeBytes("RIFF");
        dos.writeInt(Integer.reverseBytes(36 + pcmData.length));
        dos.writeBytes("WAVE");
        dos.writeBytes("fmt ");
        dos.writeInt(Integer.reverseBytes(16));
        dos.writeShort(Short.reverseBytes((short) 1));
        dos.writeShort(Short.reverseBytes((short) channels));
        dos.writeInt(Integer.reverseBytes(sampleRate));
        dos.writeInt(Integer.reverseBytes(sampleRate * channels * (bitsPerSample / 8)));
        dos.writeShort(Short.reverseBytes((short) (channels * (bitsPerSample / 8))));
        dos.writeShort(Short.reverseBytes((short) bitsPerSample));
        dos.writeBytes("data");
        dos.writeInt(Integer.reverseBytes(pcmData.length));
        dos.write(pcmData);
        return baos.toByteArray();
    }

    public void playPinPull() {
        if (soundEnabled && pinPullSound != null) pinPullSound.play(0.85f);
    }

    public void playMarbleClink() {
        if (soundEnabled && marbleClinkSound != null) marbleClinkSound.play(0.45f);
    }

    public void playBubblePop() {
        if (soundEnabled && bubblePopSound != null) bubblePopSound.play(0.5f);
    }

    public void playZenChime() {
        if (soundEnabled && zenChimeSound != null) zenChimeSound.play(0.7f);
    }

    public void playComboChime(int comboStep) {
        if (!soundEnabled) return;
        int idx = MathUtils.clamp(comboStep, 0, comboChimeSounds.length - 1);
        if (comboChimeSounds[idx] != null) {
            comboChimeSounds[idx].play(0.65f);
        }
    }

    public void playStarEarned(int starIndex) {
        if (!soundEnabled) return;
        int idx = MathUtils.clamp(starIndex - 1, 0, starFanfareSounds.length - 1);
        if (starFanfareSounds[idx] != null) {
            starFanfareSounds[idx].play(0.85f);
        }
    }

    public void playBouncerBoing() {
        if (soundEnabled && bouncerSound != null) bouncerSound.play(0.75f);
    }

    public void playPortalWarp() {
        if (soundEnabled && portalSound != null) portalSound.play(0.70f);
    }

    public void playExplosion() {
        if (soundEnabled && softExplosionSound != null) softExplosionSound.play(0.85f);
    }

    public void playWin() {
        if (soundEnabled && chillWinSound != null) chillWinSound.play(0.85f);
    }

    public void playFail() {
        if (soundEnabled && chillFailSound != null) chillFailSound.play(0.75f);
    }

    public void playClick() {
        if (soundEnabled && clickSound != null) clickSound.play(0.5f);
    }

    public void playMultiplier() {
        if (soundEnabled && multiplierSound != null) multiplierSound.play(0.80f);
    }

    public void playKeyUnlock() {
        if (soundEnabled && keyUnlockSound != null) keyUnlockSound.play(0.85f);
    }

    public void playColorGate() {
        if (soundEnabled && colorGateSound != null) colorGateSound.play(0.70f);
    }

    public void playLaserZap() {
        if (soundEnabled && laserZapSound != null) laserZapSound.play(0.80f);
    }

    public void playGearTick() {
        if (soundEnabled && gearTickSound != null) gearTickSound.play(0.55f);
    }

    public void playAcidSizzle() {
        if (soundEnabled && acidSizzleSound != null) acidSizzleSound.play(0.80f);
    }

    public void playSteamHiss() {
        if (soundEnabled && steamHissSound != null) steamHissSound.play(0.85f);
    }

    public void playWindWhoosh() {
        if (soundEnabled && windTurbineSound != null) windTurbineSound.play(0.65f);
    }

    private byte[] generateAcidSizzle() {
        int sampleRate = 22050;
        float duration = 0.28f;
        int numSamples = (int) (sampleRate * duration);
        byte[] pcm = new byte[numSamples * 2];

        for (int i = 0; i < numSamples; i++) {
            float t = (float) i / numSamples;
            float env = (float) Math.exp(-t * 8.0f);
            float noise = MathUtils.random(-0.7f, 0.7f);
            float sizzleFreq = 2400f + MathUtils.random(-400f, 400f);
            float s1 = (float) Math.sin(2 * Math.PI * sizzleFreq * (i / (float) sampleRate));
            short sample = (short) ((noise * 0.65f + s1 * 0.35f) * 32767 * 0.70f * env);
            pcm[i * 2] = (byte) (sample & 0xFF);
            pcm[i * 2 + 1] = (byte) ((sample >> 8) & 0xFF);
        }
        return pcm;
    }

    private byte[] generateSteamHiss() {
        int sampleRate = 22050;
        float duration = 0.35f;
        int numSamples = (int) (sampleRate * duration);
        byte[] pcm = new byte[numSamples * 2];

        for (int i = 0; i < numSamples; i++) {
            float t = (float) i / numSamples;
            float env = (float) Math.sin(t * Math.PI) * (float) Math.exp(-t * 3.5f);
            float noise = MathUtils.random(-0.85f, 0.85f);
            short sample = (short) (noise * 32767 * 0.65f * env);
            pcm[i * 2] = (byte) (sample & 0xFF);
            pcm[i * 2 + 1] = (byte) ((sample >> 8) & 0xFF);
        }
        return pcm;
    }

    private byte[] generateWindWhoosh() {
        int sampleRate = 22050;
        float duration = 0.30f;
        int numSamples = (int) (sampleRate * duration);
        byte[] pcm = new byte[numSamples * 2];

        for (int i = 0; i < numSamples; i++) {
            float t = (float) i / numSamples;
            float env = (float) Math.sin(t * Math.PI);
            float lowRumble = (float) Math.sin(2 * Math.PI * (120f + 60f * (float) Math.sin(t * Math.PI)) * (i / (float) sampleRate));
            float airNoise = MathUtils.random(-0.35f, 0.35f);
            short sample = (short) ((lowRumble * 0.6f + airNoise * 0.4f) * 32767 * 0.60f * env);
            pcm[i * 2] = (byte) (sample & 0xFF);
            pcm[i * 2 + 1] = (byte) ((sample >> 8) & 0xFF);
        }
        return pcm;
    }

    private byte[] generateLaserZap() {
        int sampleRate = 22050;
        float duration = 0.20f;
        int numSamples = (int) (sampleRate * duration);
        byte[] pcm = new byte[numSamples * 2];

        for (int i = 0; i < numSamples; i++) {
            float t = (float) i / numSamples;
            float env = (float) Math.exp(-t * 14.0f);
            float freq = 1800f - 1400f * t + (float) Math.sin(t * 80f) * 200f;
            float noise = MathUtils.random(-0.4f, 0.4f);
            short sample = (short) ((Math.sin(2 * Math.PI * freq * (i / (float) sampleRate)) * 0.7f + noise * 0.3f) * 32767 * 0.75f * env);
            pcm[i * 2] = (byte) (sample & 0xFF);
            pcm[i * 2 + 1] = (byte) ((sample >> 8) & 0xFF);
        }
        return pcm;
    }

    private byte[] generateGearTick() {
        int sampleRate = 22050;
        float duration = 0.08f;
        int numSamples = (int) (sampleRate * duration);
        byte[] pcm = new byte[numSamples * 2];

        for (int i = 0; i < numSamples; i++) {
            float t = (float) i / numSamples;
            float env = (float) Math.exp(-t * 26.0f);
            float freq = 850f + 400f * (float) Math.sin(t * Math.PI);
            short sample = (short) (Math.sin(2 * Math.PI * freq * (i / (float) sampleRate)) * 32767 * 0.50f * env);
            pcm[i * 2] = (byte) (sample & 0xFF);
            pcm[i * 2 + 1] = (byte) ((sample >> 8) & 0xFF);
        }
        return pcm;
    }

    public boolean isSoundEnabled() { return soundEnabled; }
    public void setSoundEnabled(boolean enabled) { this.soundEnabled = enabled; }
    public boolean isMusicEnabled() { return musicEnabled; }
    public void setMusicEnabled(boolean enabled) { this.musicEnabled = enabled; }

    @Override
    public void dispose() {
        isMusicRunning = false;
        if (musicDevice != null) {
            try { musicDevice.dispose(); } catch (Exception ignored) {}
        }
        if (pinPullSound != null) pinPullSound.dispose();
        if (marbleClinkSound != null) marbleClinkSound.dispose();
        if (bubblePopSound != null) bubblePopSound.dispose();
        if (zenChimeSound != null) zenChimeSound.dispose();
        if (softExplosionSound != null) softExplosionSound.dispose();
        if (chillWinSound != null) chillWinSound.dispose();
        if (chillFailSound != null) chillFailSound.dispose();
        if (clickSound != null) clickSound.dispose();
        if (bouncerSound != null) bouncerSound.dispose();
        if (portalSound != null) portalSound.dispose();
        if (multiplierSound != null) multiplierSound.dispose();
        if (keyUnlockSound != null) keyUnlockSound.dispose();
        if (colorGateSound != null) colorGateSound.dispose();
        if (laserZapSound != null) laserZapSound.dispose();
        if (gearTickSound != null) gearTickSound.dispose();
        if (acidSizzleSound != null) acidSizzleSound.dispose();
        if (steamHissSound != null) steamHissSound.dispose();
        if (windTurbineSound != null) windTurbineSound.dispose();

        for (Sound s : comboChimeSounds) {
            if (s != null) s.dispose();
        }
        for (Sound s : starFanfareSounds) {
            if (s != null) s.dispose();
        }
    }
}
