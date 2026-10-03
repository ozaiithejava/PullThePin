package com.game.pullthepin.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

/**
 * Zengin Görsel Efektler: Sıvı Sıçramaları, Şok Dalgaları, 3D Dönen Konfeti Yağmuru,
 * Altın Yıldız Patlamaları (Star Bursts), Havai Fişekler ve Yüzen Skor Pop-up'ları.
 */
public class ParticleSystem {

    public static class Particle {
        Vector2 position = new Vector2();
        Vector2 velocity = new Vector2();
        Color color = new Color();
        float size;
        float life;
        float maxLife;
        boolean isConfetti;
        boolean isRing;
        boolean isSplash;
        boolean isSparkle;
        boolean isAmbientMote;
        boolean isStarRay;
        float ringRadius;
        float rotation;
        float rotationSpeed;

        public boolean update(float delta) {
            life += delta;
            position.add(velocity.x * delta, velocity.y * delta);

            if (isConfetti) {
                velocity.y -= 6.5f * delta;
                velocity.x += (float) Math.sin(life * 5.5f) * 1.2f * delta;
                rotation += rotationSpeed * delta;
            } else if (isSplash) {
                velocity.y -= 16.0f * delta; // Sıvı damlası yerçekimi
            } else if (isRing) {
                ringRadius += delta * 4.8f;
            } else if (isStarRay) {
                velocity.scl(0.91f);
                size *= 0.97f;
            } else if (isAmbientMote) {
                position.x += (float) Math.sin(life * 1.5f) * 0.15f * delta;
                if (position.y > 16.5f) position.y = -0.5f;
                if (position.x < -0.5f) position.x = 9.5f;
                if (position.x > 9.5f) position.x = -0.5f;
            } else {
                velocity.scl(0.94f);
            }

            return life < maxLife;
        }
    }

    public static class FloatingText {
        public String text;
        public Vector2 position = new Vector2();
        public Vector2 velocity = new Vector2();
        public Color color = new Color();
        public float life = 0f;
        public float maxLife = 1.2f;
        public float scale = 1.0f;

        public boolean update(float delta) {
            life += delta;
            position.add(velocity.x * delta, velocity.y * delta);
            velocity.scl(0.95f);
            return life < maxLife;
        }
    }

    private Array<Particle> particles = new Array<>();
    private Array<Particle> ambientMotes = new Array<>();
    private Array<FloatingText> floatingTexts = new Array<>();
    private GlyphLayout glyphLayout = new GlyphLayout();

    public ParticleSystem() {
        initAmbientZenMotes();
    }

    private void initAmbientZenMotes() {
        ambientMotes.clear();
        for (int i = 0; i < 20; i++) {
            Particle p = new Particle();
            p.position.set(MathUtils.random(0.2f, 8.8f), MathUtils.random(0.5f, 15.5f));
            p.velocity.set(MathUtils.random(-0.08f, 0.08f), MathUtils.random(0.12f, 0.35f));
            p.color.set(0.65f, 0.85f, 1.0f, MathUtils.random(0.08f, 0.22f));
            p.size = MathUtils.random(0.04f, 0.12f);
            p.life = MathUtils.random(0f, 10f);
            p.maxLife = 99999f;
            p.isAmbientMote = true;
            ambientMotes.add(p);
        }
    }

    public void update(float delta) {
        for (Particle m : ambientMotes) {
            m.update(delta);
        }

        for (int i = particles.size - 1; i >= 0; i--) {
            Particle p = particles.get(i);
            if (!p.update(delta)) {
                particles.removeIndex(i);
            }
        }

        for (int i = floatingTexts.size - 1; i >= 0; i--) {
            FloatingText ft = floatingTexts.get(i);
            if (!ft.update(delta)) {
                floatingTexts.removeIndex(i);
            }
        }
    }

    public void render(ShapeRenderer shapeRenderer) {
        // 1. Arka Plan Işık Noktaları
        for (Particle m : ambientMotes) {
            float pulse = 0.5f + 0.5f * (float) Math.sin(m.life * 2f);
            shapeRenderer.setColor(m.color.r, m.color.g, m.color.b, m.color.a * pulse);
            shapeRenderer.circle(m.position.x, m.position.y, m.size, 10);
            shapeRenderer.setColor(1f, 1f, 1f, m.color.a * pulse * 0.6f);
            shapeRenderer.circle(m.position.x, m.position.y, m.size * 0.4f, 6);
        }

        // 2. Dinamik Efekt Parçacıkları
        for (Particle p : particles) {
            float alpha = 1f - (p.life / p.maxLife);
            shapeRenderer.setColor(p.color.r, p.color.g, p.color.b, alpha * 0.95f);

            if (p.isConfetti) {
                shapeRenderer.rect(p.position.x, p.position.y, p.size * 1.5f, p.size * 0.8f);
            } else if (p.isRing) {
                shapeRenderer.circle(p.position.x, p.position.y, p.ringRadius, 24);
                shapeRenderer.setColor(1f, 1f, 1f, alpha * 0.55f);
                shapeRenderer.circle(p.position.x, p.position.y, Math.max(0.01f, p.ringRadius - 0.05f), 24);
            } else if (p.isSplash) {
                shapeRenderer.circle(p.position.x, p.position.y, p.size, 10);
                shapeRenderer.setColor(1f, 1f, 1f, alpha * 0.65f);
                shapeRenderer.circle(p.position.x, p.position.y, p.size * 0.45f, 8);
            } else if (p.isStarRay || p.isSparkle) {
                float sz = p.size * alpha;
                shapeRenderer.triangle(p.position.x - sz, p.position.y, p.position.x + sz, p.position.y, p.position.x, p.position.y + sz * 2.2f);
                shapeRenderer.triangle(p.position.x - sz, p.position.y, p.position.x + sz, p.position.y, p.position.x, p.position.y - sz * 2.2f);
                shapeRenderer.setColor(1f, 1f, 1f, alpha);
                shapeRenderer.circle(p.position.x, p.position.y, sz * 0.4f, 8);
            } else {
                shapeRenderer.circle(p.position.x, p.position.y, p.size, 12);
                shapeRenderer.setColor(1f, 1f, 1f, alpha * 0.7f);
                shapeRenderer.circle(p.position.x, p.position.y, p.size * 0.45f, 8);
            }
        }
    }

    public void renderFloatingTexts(SpriteBatch batch, BitmapFont font) {
        for (FloatingText ft : floatingTexts) {
            float progress = ft.life / ft.maxLife;
            float alpha = MathUtils.clamp(1f - progress, 0f, 1f);
            glyphLayout.setText(font, ft.text);

            font.setColor(0f, 0f, 0f, alpha * 0.65f);
            font.draw(batch, ft.text, ft.position.x - glyphLayout.width / 2f + 2f, ft.position.y - 2f);

            font.setColor(ft.color.r, ft.color.g, ft.color.b, alpha);
            font.draw(batch, ft.text, ft.position.x - glyphLayout.width / 2f, ft.position.y);
        }
    }

    public void spawnFloatingText(String text, float worldX, float worldY, Color color) {
        FloatingText ft = new FloatingText();
        ft.text = text;
        ft.position.set((worldX / 9.0f) * 720f, (worldY / 16.0f) * 1280f + 25f);
        ft.velocity.set(0, MathUtils.random(55f, 95f));
        ft.color.set(color != null ? color : Color.WHITE);
        ft.maxLife = 1.0f;
        floatingTexts.add(ft);
    }

    public void spawnStarBurst(float worldX, float worldY) {
        // Genişleyen Altın Işıma Halkası
        Particle ring = new Particle();
        ring.position.set(worldX, worldY);
        ring.color.set(1.0f, 0.85f, 0.25f, 1f);
        ring.isRing = true;
        ring.ringRadius = 0.1f;
        ring.life = 0;
        ring.maxLife = 0.45f;
        particles.add(ring);

        // 20 Yöne Parlayan Altın Yıldız Işınları
        for (int i = 0; i < 20; i++) {
            Particle p = new Particle();
            p.position.set(worldX, worldY);
            float angle = i * 18f + MathUtils.random(-5f, 5f);
            float speed = MathUtils.random(2.5f, 6.5f);
            p.velocity.set(MathUtils.cosDeg(angle) * speed, MathUtils.sinDeg(angle) * speed);
            p.color.set(MathUtils.randomBoolean() ? new Color(1.0f, 0.88f, 0.25f, 1f) : new Color(1.0f, 1.0f, 0.7f, 1f));
            p.size = MathUtils.random(0.08f, 0.16f);
            p.life = 0;
            p.maxLife = MathUtils.random(0.45f, 0.75f);
            p.isStarRay = true;
            particles.add(p);
        }
    }

    public void spawnLiquidSplash(float x, float y, Color liquidColor) {
        for (int i = 0; i < 8; i++) {
            Particle p = new Particle();
            p.position.set(x + MathUtils.random(-0.15f, 0.15f), y);
            float angle = MathUtils.random(60f, 120f);
            float speed = MathUtils.random(2.2f, 5.2f);
            p.velocity.set(MathUtils.cosDeg(angle) * speed, MathUtils.sinDeg(angle) * speed);
            p.color.set(liquidColor != null ? liquidColor : new Color(0.25f, 0.85f, 0.65f, 1.0f));
            p.size = MathUtils.random(0.04f, 0.09f);
            p.life = 0;
            p.maxLife = MathUtils.random(0.35f, 0.60f);
            p.isSplash = true;
            particles.add(p);
        }
    }

    public void spawnPinShockwave(float x, float y) {
        Particle ring = new Particle();
        ring.position.set(x, y);
        ring.color.set(1.0f, 0.85f, 0.35f, 0.85f);
        ring.isRing = true;
        ring.ringRadius = 0.08f;
        ring.life = 0;
        ring.maxLife = 0.28f;
        particles.add(ring);

        for (int i = 0; i < 6; i++) {
            Particle p = new Particle();
            p.position.set(x, y);
            float angle = MathUtils.random(0f, 360f);
            float speed = MathUtils.random(1.5f, 3.8f);
            p.velocity.set(MathUtils.cosDeg(angle) * speed, MathUtils.sinDeg(angle) * speed);
            p.color.set(1.0f, 0.90f, 0.45f, 1.0f);
            p.size = MathUtils.random(0.04f, 0.08f);
            p.life = 0;
            p.maxLife = 0.32f;
            p.isSparkle = true;
            particles.add(p);
        }
    }

    public void spawnBouncerShockwave(float x, float y) {
        Particle ring = new Particle();
        ring.position.set(x, y);
        ring.color.set(0.2f, 0.85f, 1.0f, 0.9f);
        ring.isRing = true;
        ring.ringRadius = 0.1f;
        ring.life = 0;
        ring.maxLife = 0.35f;
        particles.add(ring);
    }

    public void spawnColorSparks(float x, float y, Color baseColor) {
        Particle ring = new Particle();
        ring.position.set(x, y);
        ring.color.set(baseColor);
        ring.isRing = true;
        ring.ringRadius = 0.05f;
        ring.life = 0;
        ring.maxLife = 0.35f;
        particles.add(ring);

        for (int i = 0; i < 8; i++) {
            Particle p = new Particle();
            p.position.set(x, y);
            float angle = MathUtils.random(0f, 360f);
            float speed = MathUtils.random(1.2f, 3.5f);
            p.velocity.set(MathUtils.cosDeg(angle) * speed, MathUtils.sinDeg(angle) * speed);
            p.color.set(baseColor);
            p.size = MathUtils.random(0.06f, 0.11f);
            p.life = 0;
            p.maxLife = MathUtils.random(0.35f, 0.60f);
            p.isSparkle = true;
            particles.add(p);
        }
    }

    public void spawnExplosion(float x, float y) {
        Particle ring = new Particle();
        ring.position.set(x, y);
        ring.color.set(1.0f, 0.45f, 0.2f, 1f);
        ring.isRing = true;
        ring.ringRadius = 0.1f;
        ring.life = 0;
        ring.maxLife = 0.5f;
        particles.add(ring);

        for (int i = 0; i < 35; i++) {
            Particle p = new Particle();
            p.position.set(x, y);
            float angle = MathUtils.random(0f, 360f);
            float speed = MathUtils.random(2.5f, 8.5f);
            p.velocity.set(MathUtils.cosDeg(angle) * speed, MathUtils.sinDeg(angle) * speed);
            p.color.set(MathUtils.randomBoolean() ? new Color(1.0f, 0.45f, 0.2f, 1f) : new Color(0.98f, 0.25f, 0.35f, 1f));
            p.size = MathUtils.random(0.12f, 0.26f);
            p.life = 0;
            p.maxLife = MathUtils.random(0.4f, 0.85f);
            particles.add(p);
        }
    }

    public void spawnConfetti(float x, float y) {
        Color[] pastelColors = {
                new Color(0.98f, 0.38f, 0.52f, 1f),
                new Color(0.18f, 0.88f, 0.68f, 1f),
                new Color(0.22f, 0.72f, 1.00f, 1f),
                new Color(1.00f, 0.82f, 0.18f, 1f),
                new Color(0.72f, 0.42f, 0.98f, 1f),
                new Color(1.00f, 0.52f, 0.28f, 1f)
        };

        for (int i = 0; i < 90; i++) {
            Particle p = new Particle();
            p.position.set(x + MathUtils.random(-3.8f, 3.8f), y + MathUtils.random(0f, 3.0f));
            p.velocity.set(MathUtils.random(-3.0f, 3.0f), MathUtils.random(4.0f, 9.5f));
            p.color.set(pastelColors[MathUtils.random(pastelColors.length - 1)]);
            p.size = MathUtils.random(0.12f, 0.25f);
            p.life = 0;
            p.maxLife = MathUtils.random(2.2f, 3.8f);
            p.isConfetti = true;
            p.rotation = MathUtils.random(0f, 360f);
            p.rotationSpeed = MathUtils.random(-160f, 160f);
            particles.add(p);
        }
    }

    public void spawnMultiplierBurst(float x, float y, Color gateColor) {
        Particle ring = new Particle();
        ring.position.set(x, y);
        ring.color.set(gateColor);
        ring.isRing = true;
        ring.ringRadius = 0.08f;
        ring.life = 0;
        ring.maxLife = 0.35f;
        particles.add(ring);

        for (int i = 0; i < 14; i++) {
            Particle p = new Particle();
            p.position.set(x + MathUtils.random(-0.2f, 0.2f), y);
            float angle = MathUtils.random(210f, 330f); // Aşağı doğru fışkırma
            float speed = MathUtils.random(2.0f, 6.0f);
            p.velocity.set(MathUtils.cosDeg(angle) * speed, MathUtils.sinDeg(angle) * speed);
            p.color.set(gateColor);
            p.size = MathUtils.random(0.06f, 0.12f);
            p.life = 0;
            p.maxLife = MathUtils.random(0.4f, 0.7f);
            p.isSparkle = true;
            particles.add(p);
        }
    }

    public void spawnKeyUnlockSparks(float x, float y) {
        Particle ring = new Particle();
        ring.position.set(x, y);
        ring.color.set(1.0f, 0.85f, 0.22f, 1.0f);
        ring.isRing = true;
        ring.ringRadius = 0.06f;
        ring.life = 0;
        ring.maxLife = 0.40f;
        particles.add(ring);

        for (int i = 0; i < 20; i++) {
            Particle p = new Particle();
            p.position.set(x, y);
            float angle = MathUtils.random(0f, 360f);
            float speed = MathUtils.random(2.0f, 5.5f);
            p.velocity.set(MathUtils.cosDeg(angle) * speed, MathUtils.sinDeg(angle) * speed);
            p.color.set(MathUtils.randomBoolean() ? new Color(1.0f, 0.88f, 0.25f, 1f) : Color.WHITE);
            p.size = MathUtils.random(0.06f, 0.12f);
            p.life = 0;
            p.maxLife = MathUtils.random(0.45f, 0.80f);
            p.isSparkle = true;
            particles.add(p);
        }
    }

    public void spawnColorGateSplash(float x, float y, Color col) {
        Particle ring = new Particle();
        ring.position.set(x, y);
        ring.color.set(col);
        ring.isRing = true;
        ring.ringRadius = 0.08f;
        ring.life = 0;
        ring.maxLife = 0.32f;
        particles.add(ring);

        for (int i = 0; i < 10; i++) {
            Particle p = new Particle();
            p.position.set(x + MathUtils.random(-0.15f, 0.15f), y);
            float angle = MathUtils.random(0f, 360f);
            float speed = MathUtils.random(1.5f, 4.0f);
            p.velocity.set(MathUtils.cosDeg(angle) * speed, MathUtils.sinDeg(angle) * speed);
            p.color.set(col);
            p.size = MathUtils.random(0.05f, 0.10f);
            p.life = 0;
            p.maxLife = MathUtils.random(0.35f, 0.55f);
            p.isSparkle = true;
            particles.add(p);
        }
    }

    public void spawnLaserVaporize(float x, float y, Color beamColor) {
        // Genişleyen Plazma Şok Halkası
        Particle ring = new Particle();
        ring.position.set(x, y);
        ring.color.set(beamColor != null ? beamColor : new Color(1.0f, 0.2f, 0.35f, 1f));
        ring.isRing = true;
        ring.ringRadius = 0.06f;
        ring.life = 0;
        ring.maxLife = 0.38f;
        particles.add(ring);

        // 16 Yöne Fışkıran Elektrik Plazma Kıvılcımları
        for (int i = 0; i < 16; i++) {
            Particle p = new Particle();
            p.position.set(x, y);
            float angle = MathUtils.random(0f, 360f);
            float speed = MathUtils.random(2.5f, 6.5f);
            p.velocity.set(MathUtils.cosDeg(angle) * speed, MathUtils.sinDeg(angle) * speed);
            p.color.set(MathUtils.randomBoolean() ? beamColor : Color.WHITE);
            p.size = MathUtils.random(0.06f, 0.14f);
            p.life = 0;
            p.maxLife = MathUtils.random(0.30f, 0.55f);
            p.isSparkle = true;
            particles.add(p);
        }
    }

    public void spawnGearSparks(float x, float y) {
        for (int i = 0; i < 6; i++) {
            Particle p = new Particle();
            p.position.set(x, y);
            float angle = MathUtils.random(0f, 360f);
            float speed = MathUtils.random(1.2f, 3.2f);
            p.velocity.set(MathUtils.cosDeg(angle) * speed, MathUtils.sinDeg(angle) * speed);
            p.color.set(new Color(1.0f, 0.85f, 0.30f, 1.0f));
            p.size = MathUtils.random(0.04f, 0.08f);
            p.life = 0;
            p.maxLife = 0.25f;
            p.isSparkle = true;
            particles.add(p);
        }
    }

    public void spawnAcidFizz(float x, float y) {
        // Toksik Yeşil Köpüren Asit Buharı ve Gaz Kabarcıkları
        for (int i = 0; i < 14; i++) {
            Particle p = new Particle();
            p.position.set(x + MathUtils.random(-0.15f, 0.15f), y + MathUtils.random(-0.1f, 0.1f));
            float speedX = MathUtils.random(-0.8f, 0.8f);
            float speedY = MathUtils.random(1.2f, 3.5f);
            p.velocity.set(speedX, speedY);
            p.color.set(MathUtils.randomBoolean() ? new Color(0.25f, 0.95f, 0.25f, 0.9f) : new Color(0.75f, 1.0f, 0.35f, 0.8f));
            p.size = MathUtils.random(0.06f, 0.14f);
            p.life = 0;
            p.maxLife = MathUtils.random(0.40f, 0.75f);
            p.isSparkle = true;
            particles.add(p);
        }
    }

    public void spawnSteamBurst(float x, float y) {
        // Kimyasal Nötralizasyon Devasa Beyaz/Cyan Buhar Patlaması
        Particle ring = new Particle();
        ring.position.set(x, y);
        ring.color.set(0.60f, 0.92f, 1.0f, 0.85f);
        ring.isRing = true;
        ring.ringRadius = 0.08f;
        ring.life = 0;
        ring.maxLife = 0.45f;
        particles.add(ring);

        for (int i = 0; i < 22; i++) {
            Particle p = new Particle();
            p.position.set(x + MathUtils.random(-0.2f, 0.2f), y + MathUtils.random(-0.1f, 0.1f));
            float angle = MathUtils.random(40f, 140f); // Yukarı doğru patlama
            float speed = MathUtils.random(2.0f, 5.5f);
            p.velocity.set(MathUtils.cosDeg(angle) * speed, MathUtils.sinDeg(angle) * speed);
            p.color.set(MathUtils.randomBoolean() ? Color.WHITE : new Color(0.70f, 0.92f, 1.0f, 0.85f));
            p.size = MathUtils.random(0.08f, 0.18f);
            p.life = 0;
            p.maxLife = MathUtils.random(0.55f, 0.95f);
            p.isSparkle = true;
            particles.add(p);
        }
    }

    public void clear() {
        particles.clear();
        floatingTexts.clear();
        initAmbientZenMotes();
    }
}
