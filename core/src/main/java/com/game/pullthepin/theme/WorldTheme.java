package com.game.pullthepin.theme;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;

/**
 * 6 Farklı Eşsiz Dünya Teması ve Dinamik Arka Plan Motoru.
 * Her tema kendine has gradyanlar, animasyonlu parçacıklar, ışık huzmeleri ve görsel stiller içerir.
 */
public class WorldTheme {

    public enum ThemeType {
        CYBER_LAB(1, 20, "CYBER LAB", "SİBER LABORATUVAR", "High-Tech Neon & Circuitry", "Yüksek Teknoloji & Neon Devreler"),
        ATLANTIS_OCEAN(21, 40, "ATLANTIS DEEP", "ATLANTIS DERİNLİKLERİ", "Subaquatic Abyss & Coral Ruins", "Derin Deniz & Mercan Harabeleri"),
        MAGMA_FORGE(41, 60, "MAGMA FORGE", "LAV OCAĞI", "Volcanic Core & Molten Fire", "Volkanik Çekirdek & Erimiş Lav"),
        COSMIC_NEBULA(61, 80, "COSMIC NEBULA", "KOZMİK BULUTSU", "Starlight Astral & Quantum Void", "Yıldız Işığı & Kuantum Boşluğu"),
        EMERALD_ZEN(81, 100, "EMERALD ZEN", "ZÜMRÜT ZEN TAPINAĞI", "Ancient Bamboo & Jade Grottos", "Kadim Bambu & Yeşim Mağaraları"),
        SOLAR_MIRAGE(101, 120, "SOLAR MIRAGE", "GÜNEŞ SERABI", "Golden Dunes & Solar Zenith", "Altın Tepeler & Güneş Zirvesi"),
        TOXIC_BIOSPHERE(121, 140, "TOXIC BIOSPHERE", "TOKSİK BİYOSFER", "Acidic Lagoons & Crystal Springs", "Asit Gölleri & Kristal Pınarlar"),
        SKY_JETSTREAM(141, 160, "SKY JETSTREAM", "AERODİNAMİK GÖKYÜZÜ", "Aerodynamic Wind & Sky Jetstreams", "Aerodinamik Rüzgarlar & Gökyüzü Akımları"),
        CHAOS_REACTOR(161, 180, "CHAOS REACTOR", "KUANTUM KAOS REAKTÖRÜ", "Hybrid Mechanics & Quantum Turbulence", "Hibrit Sistemler & Kuantum Çalkantısı"),
        GRANDMASTER_APEX(181, 200, "GRANDMASTER APEX", "KOZMİK TANRI ZİRVESİ", "Mastery of the Universe & Grand Finale", "Evrenin Ustalığı & Büyük Final");

        public final int minLevel;
        public final int maxLevel;
        public final String nameEn;
        public final String nameTr;
        public final String descEn;
        public final String descTr;

        ThemeType(int minLevel, int maxLevel, String nameEn, String nameTr, String descEn, String descTr) {
            this.minLevel = minLevel;
            this.maxLevel = maxLevel;
            this.nameEn = nameEn;
            this.nameTr = nameTr;
            this.descEn = descEn;
            this.descTr = descTr;
        }

        public String getName(boolean isTurkish) {
            return isTurkish ? nameTr : nameEn;
        }

        public String getDesc(boolean isTurkish) {
            return isTurkish ? descTr : descEn;
        }
    }

    public static ThemeType getThemeForLevel(int level) {
        if (level <= 20) return ThemeType.CYBER_LAB;
        if (level <= 40) return ThemeType.ATLANTIS_OCEAN;
        if (level <= 60) return ThemeType.MAGMA_FORGE;
        if (level <= 80) return ThemeType.COSMIC_NEBULA;
        if (level <= 100) return ThemeType.EMERALD_ZEN;
        if (level <= 120) return ThemeType.SOLAR_MIRAGE;
        if (level <= 140) return ThemeType.TOXIC_BIOSPHERE;
        if (level <= 160) return ThemeType.SKY_JETSTREAM;
        if (level <= 180) return ThemeType.CHAOS_REACTOR;
        return ThemeType.GRANDMASTER_APEX;
    }

    // Arka Plan Parçacığı (Yıldızlar, baloncuklar, lav közleri, ateş böcekleri)
    public static class BackgroundElement {
        public float x, y, size, speedY, speedX, phase, alpha;
        public Color color;

        public BackgroundElement(float x, float y, float size, float speedY, float speedX, float phase, Color color) {
            this.x = x;
            this.y = y;
            this.size = size;
            this.speedY = speedY;
            this.speedX = speedX;
            this.phase = phase;
            this.color = color;
            this.alpha = color.a;
        }
    }

    private ThemeType currentType;
    private Array<BackgroundElement> bgElements = new Array<>();
    private float animTimer = 0f;

    public WorldTheme(ThemeType type) {
        setTheme(type);
    }

    public void setTheme(ThemeType type) {
        this.currentType = type;
        this.bgElements.clear();
        this.animTimer = 0f;
        initElementsForTheme(type);
    }

    public ThemeType getThemeType() {
        return currentType;
    }

    private void initElementsForTheme(ThemeType type) {
        switch (type) {
            case CYBER_LAB:
                // Siber Veri Noktaları
                for (int i = 0; i < 28; i++) {
                    bgElements.add(new BackgroundElement(
                            MathUtils.random(0.2f, 8.8f),
                            MathUtils.random(0.5f, 15.5f),
                            MathUtils.random(0.03f, 0.07f),
                            MathUtils.random(0.4f, 1.2f),
                            0f,
                            MathUtils.random(0f, 6.28f),
                            new Color(0.2f, 0.85f, 1.0f, MathUtils.random(0.15f, 0.45f))
                    ));
                }
                break;

            case ATLANTIS_OCEAN:
                // Yükselen Su Altı Baloncukları
                for (int i = 0; i < 35; i++) {
                    bgElements.add(new BackgroundElement(
                            MathUtils.random(0.2f, 8.8f),
                            MathUtils.random(0f, 16f),
                            MathUtils.random(0.04f, 0.12f),
                            MathUtils.random(0.5f, 1.5f),
                            MathUtils.random(-0.2f, 0.2f),
                            MathUtils.random(0f, 6.28f),
                            new Color(0.3f, 0.9f, 0.95f, MathUtils.random(0.12f, 0.35f))
                    ));
                }
                break;

            case MAGMA_FORGE:
                // Yükselen Lav Közleri ve Kıvılcımlar
                for (int i = 0; i < 32; i++) {
                    bgElements.add(new BackgroundElement(
                            MathUtils.random(0.2f, 8.8f),
                            MathUtils.random(0f, 16f),
                            MathUtils.random(0.04f, 0.10f),
                            MathUtils.random(0.6f, 2.0f),
                            MathUtils.random(-0.4f, 0.4f),
                            MathUtils.random(0f, 6.28f),
                            MathUtils.randomBoolean() ?
                                    new Color(1.0f, 0.45f, 0.15f, MathUtils.random(0.25f, 0.60f)) :
                                    new Color(1.0f, 0.85f, 0.20f, MathUtils.random(0.20f, 0.50f))
                    ));
                }
                break;

            case COSMIC_NEBULA:
                // Parıldayan İki Katmanlı Kozmik Yıldızlar
                for (int i = 0; i < 55; i++) {
                    bgElements.add(new BackgroundElement(
                            MathUtils.random(0.1f, 8.9f),
                            MathUtils.random(0.1f, 15.9f),
                            MathUtils.random(0.02f, 0.08f),
                            MathUtils.random(0.05f, 0.15f),
                            0f,
                            MathUtils.random(0f, 6.28f),
                            MathUtils.randomBoolean() ?
                                    new Color(0.85f, 0.70f, 1.0f, MathUtils.random(0.25f, 0.85f)) :
                                    new Color(0.40f, 0.85f, 1.0f, MathUtils.random(0.25f, 0.85f))
                    ));
                }
                break;

            case EMERALD_ZEN:
                // Salınan Parlak Ateş Böcekleri & Zen Polenleri
                for (int i = 0; i < 26; i++) {
                    bgElements.add(new BackgroundElement(
                            MathUtils.random(0.2f, 8.8f),
                            MathUtils.random(0.5f, 15.5f),
                            MathUtils.random(0.04f, 0.09f),
                            MathUtils.random(0.1f, 0.4f),
                            MathUtils.random(-0.3f, 0.3f),
                            MathUtils.random(0f, 6.28f),
                            new Color(0.55f, 1.0f, 0.45f, MathUtils.random(0.20f, 0.55f))
                    ));
                }
                break;

            case SOLAR_MIRAGE:
                // Parlayan Elmas Işık Pırıltıları
                for (int i = 0; i < 30; i++) {
                    bgElements.add(new BackgroundElement(
                            MathUtils.random(0.2f, 8.8f),
                            MathUtils.random(0.5f, 15.5f),
                            MathUtils.random(0.04f, 0.11f),
                            MathUtils.random(0.3f, 0.8f),
                            MathUtils.random(-0.15f, 0.15f),
                            MathUtils.random(0f, 6.28f),
                            new Color(1.0f, 0.90f, 0.40f, MathUtils.random(0.25f, 0.65f))
                    ));
                }
                break;

            case TOXIC_BIOSPHERE:
                // Toksik Asit Buharları & Yeşil-Turkuaz Kabarcıklar
                for (int i = 0; i < 35; i++) {
                    bgElements.add(new BackgroundElement(
                            MathUtils.random(0.2f, 8.8f),
                            MathUtils.random(0f, 16f),
                            MathUtils.random(0.04f, 0.12f),
                            MathUtils.random(0.4f, 1.2f),
                            MathUtils.random(-0.3f, 0.3f),
                            MathUtils.random(0f, 6.28f),
                            MathUtils.randomBoolean() ?
                                    new Color(0.2f, 0.95f, 0.3f, MathUtils.random(0.15f, 0.45f)) :
                                    new Color(0.15f, 0.85f, 0.85f, MathUtils.random(0.15f, 0.45f))
                    ));
                }
                break;

            case SKY_JETSTREAM:
                // Uçuşan Hava Çizgileri & Rüzgar Parçacıkları
                for (int i = 0; i < 40; i++) {
                    bgElements.add(new BackgroundElement(
                            MathUtils.random(0.1f, 8.9f),
                            MathUtils.random(0.1f, 15.9f),
                            MathUtils.random(0.03f, 0.08f),
                            MathUtils.random(1.2f, 2.8f),
                            MathUtils.random(0.2f, 0.8f),
                            MathUtils.random(0f, 6.28f),
                            new Color(0.5f, 0.85f, 1.0f, MathUtils.random(0.2f, 0.6f))
                    ));
                }
                break;

            case CHAOS_REACTOR:
                // Kuantum Kaos & Yüksek Enerji Parçacıkları
                for (int i = 0; i < 45; i++) {
                    bgElements.add(new BackgroundElement(
                            MathUtils.random(0.2f, 8.8f),
                            MathUtils.random(0.2f, 15.8f),
                            MathUtils.random(0.03f, 0.1f),
                            MathUtils.random(0.8f, 2.2f),
                            MathUtils.random(-0.5f, 0.5f),
                            MathUtils.random(0f, 6.28f),
                            MathUtils.randomBoolean() ?
                                    new Color(0.85f, 0.2f, 1.0f, MathUtils.random(0.25f, 0.75f)) :
                                    new Color(0.2f, 0.9f, 0.9f, MathUtils.random(0.25f, 0.75f))
                    ));
                }
                break;

            case GRANDMASTER_APEX:
                // Kozmik Tanrı Zirvesi Altın Yıldız Yağmuru
                for (int i = 0; i < 50; i++) {
                    bgElements.add(new BackgroundElement(
                            MathUtils.random(0.1f, 8.9f),
                            MathUtils.random(0.1f, 15.9f),
                            MathUtils.random(0.04f, 0.12f),
                            MathUtils.random(0.3f, 1.2f),
                            MathUtils.random(-0.2f, 0.2f),
                            MathUtils.random(0f, 6.28f),
                            MathUtils.randomBoolean() ?
                                    new Color(1.0f, 0.84f, 0.25f, MathUtils.random(0.35f, 0.9f)) :
                                    new Color(1.0f, 1.0f, 1.0f, MathUtils.random(0.4f, 0.95f))
                    ));
                }
                break;
        }
    }

    public void update(float delta) {
        animTimer += delta;

        for (BackgroundElement el : bgElements) {
            el.y += el.speedY * delta;
            el.x += (float) Math.sin(animTimer * 2f + el.phase) * el.speedX * delta;

            if (el.y > 16.5f) {
                el.y = -0.5f;
                el.x = MathUtils.random(0.2f, 8.8f);
            }
            if (el.x < -0.5f) el.x = 9.5f;
            if (el.x > 9.5f) el.x = -0.5f;
        }
    }

    public void renderBackground(ShapeRenderer sr, float width, float height) {
        switch (currentType) {
            case CYBER_LAB:
                renderCyberLab(sr, width, height);
                break;
            case ATLANTIS_OCEAN:
                renderAtlantisOcean(sr, width, height);
                break;
            case MAGMA_FORGE:
                renderMagmaForge(sr, width, height);
                break;
            case COSMIC_NEBULA:
                renderCosmicNebula(sr, width, height);
                break;
            case EMERALD_ZEN:
                renderEmeraldZen(sr, width, height);
                break;
            case SOLAR_MIRAGE:
                renderSolarMirage(sr, width, height);
                break;
            case TOXIC_BIOSPHERE:
                renderToxicBiosphere(sr, width, height);
                break;
            case SKY_JETSTREAM:
                renderSkyJetstream(sr, width, height);
                break;
            case CHAOS_REACTOR:
                renderChaosReactor(sr, width, height);
                break;
            case GRANDMASTER_APEX:
                renderGrandmasterApex(sr, width, height);
                break;
        }
    }

    // 1. CYBER LAB: Neon Izgaralar & Dijital Motes
    private void renderCyberLab(ShapeRenderer sr, float w, float h) {
        sr.rect(0, 0, w, h,
                new Color(0.04f, 0.05f, 0.10f, 1f),
                new Color(0.04f, 0.05f, 0.10f, 1f),
                new Color(0.08f, 0.13f, 0.24f, 1f),
                new Color(0.08f, 0.13f, 0.24f, 1f)
        );

        // Zemin Siber Aurası
        sr.setColor(0.18f, 0.75f, 1.0f, 0.08f);
        sr.circle(w / 2f, 2.0f, 3.8f, 32);

        // Siber Izgara
        sr.setColor(0.2f, 0.6f, 1.0f, 0.04f);
        for (float x = 0.5f; x < w; x += 0.8f) {
            for (float y = 0.5f; y < h; y += 0.8f) {
                sr.circle(x, y, 0.025f, 8);
            }
        }

        // Yüzen Veri Noktaları
        for (BackgroundElement el : bgElements) {
            float pulse = 0.6f + 0.4f * (float) Math.sin(animTimer * 4f + el.phase);
            sr.setColor(el.color.r, el.color.g, el.color.b, el.alpha * pulse);
            sr.circle(el.x, el.y, el.size, 10);
            sr.setColor(1f, 1f, 1f, el.alpha * pulse * 0.8f);
            sr.circle(el.x, el.y, el.size * 0.4f, 6);
        }
    }

    // 2. ATLANTIS OCEAN: Su Altı Işık Huzmeleri (Caustics) & Baloncuklar
    private void renderAtlantisOcean(ShapeRenderer sr, float w, float h) {
        sr.rect(0, 0, w, h,
                new Color(0.02f, 0.06f, 0.12f, 1f),
                new Color(0.02f, 0.06f, 0.12f, 1f),
                new Color(0.04f, 0.18f, 0.24f, 1f),
                new Color(0.04f, 0.18f, 0.24f, 1f)
        );

        // Su Altı Işık Huzmeleri (Animated Sunlight Caustic Shafts)
        for (int i = 0; i < 4; i++) {
            float sway = (float) Math.sin(animTimer * 0.8f + i * 1.5f) * 0.6f;
            float topX = 1.8f + i * 2.0f + sway;
            sr.setColor(0.3f, 0.85f, 0.95f, 0.035f);
            sr.triangle(topX - 0.5f, h, topX + 0.5f, h, topX + sway * 1.5f - 1.2f, 0);
            sr.triangle(topX + 0.5f, h, topX + sway * 1.5f + 1.2f, 0, topX + sway * 1.5f - 1.2f, 0);
        }

        // Zemin Deniz Parıltısı
        sr.setColor(0.2f, 0.9f, 0.85f, 0.07f);
        sr.circle(w / 2f, 1.8f, 4.0f, 32);

        // Yükselen İncimsi Baloncuklar
        for (BackgroundElement el : bgElements) {
            float pulse = 0.7f + 0.3f * (float) Math.sin(animTimer * 3f + el.phase);
            sr.setColor(el.color.r, el.color.g, el.color.b, el.alpha * pulse);
            sr.circle(el.x, el.y, el.size, 12);
            sr.setColor(1f, 1f, 1f, el.alpha * pulse * 0.9f);
            sr.circle(el.x - el.size * 0.3f, el.y + el.size * 0.3f, el.size * 0.35f, 8);
        }
    }

    // 3. MAGMA FORGE: Volkanik Erimiş Lav & Köz Kıvılcımları
    private void renderMagmaForge(ShapeRenderer sr, float w, float h) {
        sr.rect(0, 0, w, h,
                new Color(0.08f, 0.03f, 0.05f, 1f),
                new Color(0.08f, 0.03f, 0.05f, 1f),
                new Color(0.20f, 0.06f, 0.04f, 1f),
                new Color(0.20f, 0.06f, 0.04f, 1f)
        );

        // Zemin Magma Ateş Hattı (Pulsing Molten Flare)
        float lavaPulse = 0.7f + 0.3f * (float) Math.sin(animTimer * 2.5f);
        sr.setColor(1.0f, 0.35f, 0.10f, 0.12f * lavaPulse);
        sr.circle(w / 2f, 1.5f, 4.5f, 32);
        sr.setColor(1.0f, 0.75f, 0.20f, 0.08f * lavaPulse);
        sr.circle(w / 2f, 1.0f, 3.0f, 32);

        // Yükselen Köz Parçacıkları (Ember Sparks)
        for (BackgroundElement el : bgElements) {
            float pulse = 0.5f + 0.5f * (float) Math.sin(animTimer * 5f + el.phase);
            sr.setColor(el.color.r, el.color.g, el.color.b, el.alpha * pulse);
            sr.circle(el.x, el.y, el.size, 8);
            sr.setColor(1.0f, 1.0f, 0.8f, el.alpha * pulse);
            sr.circle(el.x, el.y, el.size * 0.4f, 6);
        }
    }

    // 4. COSMIC NEBULA: Yıldızlar, Parıldayan Kozmik Toz & Galaksi
    private void renderCosmicNebula(ShapeRenderer sr, float w, float h) {
        sr.rect(0, 0, w, h,
                new Color(0.05f, 0.02f, 0.10f, 1f),
                new Color(0.05f, 0.02f, 0.10f, 1f),
                new Color(0.14f, 0.04f, 0.22f, 1f),
                new Color(0.14f, 0.04f, 0.22f, 1f)
        );

        // Bulutsu Parlamaları (Nebula Clouds)
        sr.setColor(0.7f, 0.25f, 0.95f, 0.06f);
        sr.circle(2.5f, 12.0f, 3.5f, 24);
        sr.setColor(0.2f, 0.6f, 1.0f, 0.06f);
        sr.circle(6.5f, 6.0f, 4.0f, 24);

        // Yanıp Sönen Yıldızlar
        for (BackgroundElement el : bgElements) {
            float twinkle = 0.3f + 0.7f * (float) Math.abs(Math.sin(animTimer * 2.2f + el.phase));
            sr.setColor(el.color.r, el.color.g, el.color.b, el.alpha * twinkle);
            sr.circle(el.x, el.y, el.size * twinkle, 8);
            if (el.size > 0.05f) {
                // Yıldız Haçı (4-point sparkle)
                float sz = el.size * twinkle * 1.8f;
                sr.setColor(1f, 1f, 1f, el.alpha * twinkle * 0.8f);
                sr.rect(el.x - sz, el.y - 0.01f, sz * 2f, 0.02f);
                sr.rect(el.x - 0.01f, el.y - sz, 0.02f, sz * 2f);
            }
        }
    }

    // 5. EMERALD ZEN: Bambu Silüetleri & Uçuşan Ateş Böcekleri
    private void renderEmeraldZen(ShapeRenderer sr, float w, float h) {
        sr.rect(0, 0, w, h,
                new Color(0.03f, 0.07f, 0.05f, 1f),
                new Color(0.03f, 0.07f, 0.05f, 1f),
                new Color(0.06f, 0.18f, 0.12f, 1f),
                new Color(0.06f, 0.18f, 0.12f, 1f)
        );

        // Zen Bambu Işık Huzmeleri
        sr.setColor(0.3f, 0.9f, 0.6f, 0.04f);
        for (int i = 0; i < 3; i++) {
            float bx = 1.5f + i * 2.8f + (float) Math.sin(animTimer * 0.5f + i) * 0.4f;
            sr.rect(bx, 0, 0.4f, h);
        }

        // Ateş Böcekleri (Fireflies with Soft Halo)
        for (BackgroundElement el : bgElements) {
            float pulse = 0.4f + 0.6f * (float) Math.sin(animTimer * 3.5f + el.phase);
            // Işık Haresi
            sr.setColor(0.4f, 1.0f, 0.6f, el.alpha * pulse * 0.35f);
            sr.circle(el.x, el.y, el.size * 2.4f, 12);
            // Çekirdek
            sr.setColor(0.9f, 1.0f, 0.6f, el.alpha * pulse);
            sr.circle(el.x, el.y, el.size, 8);
            sr.setColor(1f, 1f, 1f, el.alpha * pulse);
            sr.circle(el.x, el.y, el.size * 0.4f, 6);
        }
    }

    // 6. SOLAR MIRAGE: Altın Güneş Işınları & Elmas Pırıltılar
    private void renderSolarMirage(ShapeRenderer sr, float w, float h) {
        sr.rect(0, 0, w, h,
                new Color(0.10f, 0.04f, 0.08f, 1f),
                new Color(0.10f, 0.04f, 0.08f, 1f),
                new Color(0.24f, 0.12f, 0.04f, 1f),
                new Color(0.24f, 0.12f, 0.04f, 1f)
        );

        // Dönen Güneş Işınları (Rotating Solar Zenith Rays)
        float cx = w / 2f;
        float cy = h + 2.0f;
        for (int i = 0; i < 14; i++) {
            float angle = animTimer * 4.0f + i * (360f / 14f);
            float rad1 = (float) Math.toRadians(angle - 4f);
            float rad2 = (float) Math.toRadians(angle + 4f);
            sr.setColor(1.0f, 0.85f, 0.3f, 0.025f);
            sr.triangle(
                    cx, cy,
                    cx + (float) Math.cos(rad1) * 22f, cy - (float) Math.sin(rad1) * 22f,
                    cx + (float) Math.cos(rad2) * 22f, cy - (float) Math.sin(rad2) * 22f
            );
        }

        // Zemin Altın Parıltı
        sr.setColor(1.0f, 0.75f, 0.2f, 0.08f);
        sr.circle(w / 2f, 2.0f, 4.0f, 32);

        // Yüzen Elmas Pırıltıları
        for (BackgroundElement el : bgElements) {
            float sparkle = 0.5f + 0.5f * (float) Math.sin(animTimer * 4.5f + el.phase);
            float sz = el.size * sparkle;
            sr.setColor(1.0f, 0.85f, 0.35f, el.alpha * sparkle);
            sr.triangle(el.x - sz, el.y, el.x + sz, el.y, el.x, el.y + sz * 1.8f);
            sr.triangle(el.x - sz, el.y, el.x + sz, el.y, el.x, el.y - sz * 1.8f);
            sr.setColor(1f, 1f, 1f, el.alpha * sparkle);
            sr.circle(el.x, el.y, sz * 0.35f, 6);
        }
    }

    // 7. QUANTUM CORE: Yüksek Voltajlı Plazma Arkları & Sinkrotron Enerji Halkaları
    private void renderQuantumCore(ShapeRenderer sr, float w, float h) {
        sr.rect(0, 0, w, h,
                new Color(0.04f, 0.02f, 0.09f, 1f),
                new Color(0.04f, 0.02f, 0.09f, 1f),
                new Color(0.12f, 0.04f, 0.22f, 1f),
                new Color(0.12f, 0.04f, 0.22f, 1f)
        );

        // Kuantum Sinkrotron Dönen Enerji Halkaları
        float cx = w / 2f;
        float cy = h * 0.55f;
        float corePulse = 0.8f + 0.2f * (float) Math.sin(animTimer * 3.5f);

        sr.setColor(0.15f, 0.85f, 1.0f, 0.06f * corePulse);
        sr.circle(cx, cy, 4.8f, 36);

        sr.setColor(0.85f, 0.25f, 1.0f, 0.07f * corePulse);
        sr.circle(cx, cy, 3.2f, 32);

        // Sinkrotron Çember Çizgisi
        sr.setColor(0.20f, 0.80f, 1.0f, 0.08f);
        sr.circle(cx, cy, 2.2f, 32);

        // Uçuşan Kuantum Parçacıkları
        for (BackgroundElement el : bgElements) {
            float pulse = 0.6f + 0.4f * (float) Math.sin(animTimer * 5.0f + el.phase);
            sr.setColor(el.color.r, el.color.g, el.color.b, el.alpha * pulse);
            sr.circle(el.x, el.y, el.size, 10);
            sr.setColor(1.0f, 1.0f, 1.0f, el.alpha * pulse * 0.8f);
            sr.circle(el.x, el.y, el.size * 0.4f, 6);
        }
    }

    // 8. HYPER DIMENSION: Çok Boyutlu Prizmatik Uzay & Zaman Bükülme Işınları
    private void renderHyperDimension(ShapeRenderer sr, float w, float h) {
        sr.rect(0, 0, w, h,
                new Color(0.05f, 0.03f, 0.09f, 1f),
                new Color(0.05f, 0.03f, 0.09f, 1f),
                new Color(0.18f, 0.10f, 0.26f, 1f),
                new Color(0.18f, 0.10f, 0.26f, 1f)
        );

        // Çapraz Boyut Kırılma Işınları (Hyper-Dimensional Warp Rays)
        for (int i = 0; i < 6; i++) {
            float offset = (float) Math.sin(animTimer * 0.7f + i * 1.2f) * 1.5f;
            float topX = 0.5f + i * 1.8f + offset;
            sr.setColor(1.0f, 0.82f, 0.25f, 0.025f);
            sr.triangle(topX - 0.6f, h, topX + 0.6f, h, topX + 1.2f, 0);
            sr.setColor(0.35f, 0.75f, 1.0f, 0.025f);
            sr.triangle(topX + 0.6f, h, topX + 1.8f, h, topX - 0.4f, 0);
        }

        // Zemin Kozmik Prizma Parıltısı
        sr.setColor(1.0f, 0.85f, 0.25f, 0.07f);
        sr.circle(w / 2f, 2.0f, 4.2f, 32);

        // Yüzen Prizmatik Kristallere Benzer Parçacıklar
        for (BackgroundElement el : bgElements) {
            float tw = 0.4f + 0.6f * (float) Math.abs(Math.sin(animTimer * 3.0f + el.phase));
            float sz = el.size * tw;
            sr.setColor(el.color.r, el.color.g, el.color.b, el.alpha * tw);
            sr.triangle(el.x - sz, el.y, el.x + sz, el.y, el.x, el.y + sz * 1.6f);
            sr.triangle(el.x - sz, el.y, el.x + sz, el.y, el.x, el.y - sz * 1.6f);
            sr.setColor(1f, 1f, 1f, el.alpha * tw * 0.9f);
            sr.circle(el.x, el.y, sz * 0.35f, 6);
        }
    }

    private void renderToxicBiosphere(ShapeRenderer sr, float w, float h) {
        sr.rect(0, 0, w, h,
                new Color(0.02f, 0.08f, 0.05f, 1f),
                new Color(0.02f, 0.08f, 0.05f, 1f),
                new Color(0.06f, 0.18f, 0.10f, 1f),
                new Color(0.06f, 0.18f, 0.10f, 1f)
        );
        sr.setColor(0.2f, 0.95f, 0.35f, 0.06f);
        sr.circle(w / 2f, 1.8f, 4.2f, 32);
        for (BackgroundElement el : bgElements) {
            float pulse = 0.7f + 0.3f * (float) Math.sin(animTimer * 2.5f + el.phase);
            sr.setColor(el.color.r, el.color.g, el.color.b, el.alpha * pulse);
            sr.circle(el.x, el.y, el.size, 10);
            sr.setColor(1f, 1f, 1f, el.alpha * pulse * 0.7f);
            sr.circle(el.x - el.size * 0.25f, el.y + el.size * 0.25f, el.size * 0.3f, 6);
        }
    }

    private void renderSkyJetstream(ShapeRenderer sr, float w, float h) {
        sr.rect(0, 0, w, h,
                new Color(0.04f, 0.08f, 0.16f, 1f),
                new Color(0.04f, 0.08f, 0.16f, 1f),
                new Color(0.12f, 0.22f, 0.38f, 1f),
                new Color(0.12f, 0.22f, 0.38f, 1f)
        );
        sr.setColor(0.35f, 0.85f, 1.0f, 0.08f);
        sr.circle(w / 2f, 2.0f, 4.0f, 32);
        for (BackgroundElement el : bgElements) {
            float pulse = 0.6f + 0.4f * (float) Math.sin(animTimer * 3.5f + el.phase);
            sr.setColor(el.color.r, el.color.g, el.color.b, el.alpha * pulse);
            sr.rect(el.x, el.y, el.size * 3.5f, el.size * 0.8f);
        }
    }

    private void renderChaosReactor(ShapeRenderer sr, float w, float h) {
        sr.rect(0, 0, w, h,
                new Color(0.08f, 0.02f, 0.12f, 1f),
                new Color(0.08f, 0.02f, 0.12f, 1f),
                new Color(0.22f, 0.08f, 0.28f, 1f),
                new Color(0.22f, 0.08f, 0.28f, 1f)
        );
        sr.setColor(0.85f, 0.25f, 1.0f, 0.07f);
        sr.circle(w / 2f, 2.0f, 4.5f, 32);
        for (BackgroundElement el : bgElements) {
            float pulse = 0.5f + 0.5f * (float) Math.sin(animTimer * 5.0f + el.phase);
            sr.setColor(el.color.r, el.color.g, el.color.b, el.alpha * pulse);
            sr.circle(el.x, el.y, el.size, 10);
        }
    }

    private void renderGrandmasterApex(ShapeRenderer sr, float w, float h) {
        sr.rect(0, 0, w, h,
                new Color(0.06f, 0.05f, 0.12f, 1f),
                new Color(0.06f, 0.05f, 0.12f, 1f),
                new Color(0.24f, 0.16f, 0.32f, 1f),
                new Color(0.24f, 0.16f, 0.32f, 1f)
        );
        sr.setColor(1.0f, 0.84f, 0.22f, 0.10f);
        sr.circle(w / 2f, 2.0f, 5.0f, 36);
        for (BackgroundElement el : bgElements) {
            float pulse = 0.6f + 0.4f * (float) Math.sin(animTimer * 3.0f + el.phase);
            sr.setColor(el.color.r, el.color.g, el.color.b, el.alpha * pulse);
            sr.circle(el.x, el.y, el.size, 12);
            sr.setColor(1f, 1f, 1f, el.alpha * pulse * 0.9f);
            sr.circle(el.x, el.y, el.size * 0.45f, 8);
        }
    }

    public Color getAccentColor() {
        switch (currentType) {
            case CYBER_LAB:        return new Color(0.18f, 0.82f, 1.0f, 1.0f);
            case ATLANTIS_OCEAN:   return new Color(0.20f, 0.90f, 0.80f, 1.0f);
            case MAGMA_FORGE:      return new Color(1.00f, 0.50f, 0.15f, 1.0f);
            case COSMIC_NEBULA:    return new Color(0.85f, 0.35f, 0.95f, 1.0f);
            case EMERALD_ZEN:      return new Color(0.25f, 0.90f, 0.55f, 1.0f);
            case SOLAR_MIRAGE:     return new Color(1.00f, 0.85f, 0.25f, 1.0f);
            case TOXIC_BIOSPHERE:  return new Color(0.20f, 0.95f, 0.40f, 1.0f);
            case SKY_JETSTREAM:    return new Color(0.35f, 0.85f, 1.00f, 1.0f);
            case CHAOS_REACTOR:    return new Color(0.90f, 0.30f, 1.00f, 1.0f);
            case GRANDMASTER_APEX: return new Color(1.00f, 0.84f, 0.22f, 1.0f);
            default:               return new Color(0.20f, 0.80f, 1.0f, 1.0f);
        }
    }

    public Color getCardColor() {
        switch (currentType) {
            case CYBER_LAB:        return new Color(0.18f, 0.65f, 0.95f, 1.0f);
            case ATLANTIS_OCEAN:   return new Color(0.15f, 0.75f, 0.75f, 1.0f);
            case MAGMA_FORGE:      return new Color(0.92f, 0.42f, 0.18f, 1.0f);
            case COSMIC_NEBULA:    return new Color(0.72f, 0.35f, 0.92f, 1.0f);
            case EMERALD_ZEN:      return new Color(0.20f, 0.78f, 0.48f, 1.0f);
            case SOLAR_MIRAGE:     return new Color(0.95f, 0.75f, 0.20f, 1.0f);
            case TOXIC_BIOSPHERE:  return new Color(0.18f, 0.80f, 0.35f, 1.0f);
            case SKY_JETSTREAM:    return new Color(0.25f, 0.70f, 0.95f, 1.0f);
            case CHAOS_REACTOR:    return new Color(0.75f, 0.25f, 0.85f, 1.0f);
            case GRANDMASTER_APEX: return new Color(0.95f, 0.75f, 0.22f, 1.0f);
            default:               return new Color(0.20f, 0.60f, 0.90f, 1.0f);
        }
    }
}
