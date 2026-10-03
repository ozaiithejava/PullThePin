package com.game.pullthepin.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

/**
 * Üst Düzey Görsel Kalite (AAA Glossy Acrylic/Crystal Marble Shader),
 * Gerçekçi Fizik (CCD Bullet, Sürtünme, Squash & Stretch) ve Dinamik Işık Yansımaları.
 */
public class Ball {

    public enum BallType {
        COLORED,  // Parlak kristal cam bilye
        GRAY,     // Mat inci & saten gri bilye
        BOMB,     // Obsidyen / Nabız atan magma çekirdekli bomba
        WATER     // Saf kristal su bilyesi (Asit nötralize edici)
    }

    public static final float RADIUS = 0.22f; // Box2D metre yarıçapı

    private Body body;
    private BallType type;
    private Color color;
    private Color darkShade;
    private Color lightShade;
    private Color rimGlowColor;
    private Color trailColor;
    private boolean isDead = false;
    private boolean isCollected = false;

    // Hareket İzi (Motion Trail) için son konumlar
    private static final int TRAIL_LENGTH = 7;
    private Vector2[] trailPositions = new Vector2[TRAIL_LENGTH];
    private int trailIndex = 0;

    // Ezilme / Esneme (Squash & Stretch)
    private float squashX = 1.0f;
    private float squashY = 1.0f;
    private float animTimer = 0f;
    private float teleportCooldown = 0f;

    // Zengin, Doygun ve Dinlendirici Chill Renk Paleti
    private static final Color[] CHILL_PALETTE = {
            new Color(0.98f, 0.38f, 0.52f, 1f), // Canlı Sakura Pembesi
            new Color(0.18f, 0.88f, 0.68f, 1f), // Parlak Zümrüt / Nane
            new Color(0.22f, 0.72f, 1.00f, 1f), // Gökyüzü / Safir Mavisi
            new Color(1.00f, 0.82f, 0.18f, 1f), // Parlak Altın Sarısı
            new Color(0.72f, 0.42f, 0.98f, 1f), // Lavanta / Ametist Moru
            new Color(1.00f, 0.52f, 0.28f, 1f)  // Canlı Mercan Turuncusu
    };

    public Ball(World world, float x, float y, BallType type) {
        this(world, x, y, type, null);
    }

    public Ball(World world, float x, float y, BallType type, Color customColor) {
        this.type = type;
        this.animTimer = MathUtils.random(0f, 6.28f);

        if (customColor != null) {
            this.color = customColor.cpy();
        } else if (type == BallType.COLORED) {
            this.color = CHILL_PALETTE[MathUtils.random(CHILL_PALETTE.length - 1)].cpy();
        } else if (type == BallType.GRAY) {
            this.color = new Color(0.68f, 0.72f, 0.78f, 1f); // Mat Saten İnci
        } else if (type == BallType.WATER) {
            this.color = new Color(0.18f, 0.85f, 1.00f, 1f); // Kristal Akuamarin Su
        } else {
            this.color = new Color(0.14f, 0.15f, 0.20f, 1f); // Obsidyen Gece Siyahı
        }

        updateShadeColors();

        for (int i = 0; i < TRAIL_LENGTH; i++) {
            trailPositions[i] = new Vector2(x, y);
        }

        createBody(world, x, y);
    }

    private void updateShadeColors() {
        if (type == BallType.WATER) {
            darkShade = new Color(0.06f, 0.40f, 0.65f, 1f);
            lightShade = new Color(0.75f, 0.95f, 1.00f, 1f);
            rimGlowColor = new Color(0.30f, 0.90f, 1.00f, 0.75f);
            trailColor = new Color(0.20f, 0.80f, 1.00f, 0.45f);
            return;
        }
        darkShade = new Color(color.r * 0.38f, color.g * 0.38f, color.b * 0.38f, 1.0f);
        lightShade = new Color(
                Math.min(1.0f, color.r * 1.35f + 0.1f),
                Math.min(1.0f, color.g * 1.35f + 0.1f),
                Math.min(1.0f, color.b * 1.35f + 0.1f),
                1.0f
        );
        rimGlowColor = new Color(color.r, color.g, color.b, 0.45f);
        trailColor = new Color(color.r, color.g, color.b, 0.25f);
    }

    private void createBody(World world, float x, float y) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(x, y);
        bodyDef.bullet = true; // CCD (Sürekli Çarpışma Tespiti): Asla sızma yapmaz!
        bodyDef.linearDamping = 0.05f; // Akışkan sıvı süzülme sürtünmesi
        bodyDef.angularDamping = 0.15f;

        body = world.createBody(bodyDef);
        body.setUserData(this);

        CircleShape circle = new CircleShape();
        circle.setRadius(RADIUS);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = circle;
        fixtureDef.density = 1.6f;
        fixtureDef.friction = 0.08f; // Süper akıcı sıvı yuvarlanma sürtünmesi
        fixtureDef.restitution = 0.26f;

        body.createFixture(fixtureDef);
        circle.dispose();
    }

    public void update(float delta) {
        if (isDead || body == null) return;
        animTimer += delta;
        if (teleportCooldown > 0) teleportCooldown -= delta;

        Vector2 pos = body.getPosition();
        Vector2 vel = body.getLinearVelocity();

        // Hareket izi kaydet
        trailPositions[trailIndex].set(pos);
        trailIndex = (trailIndex + 1) % TRAIL_LENGTH;

        // Gerçekçi Squash & Stretch (Hız vektörüne göre esneme)
        float speed = vel.len();
        if (speed > 3.5f) {
            float stretch = Math.min(1.32f, 1.0f + speed * 0.022f);
            squashY += (stretch - squashY) * 0.25f;
            squashX += ((1.0f / stretch) - squashX) * 0.25f;
        } else {
            squashX += (1.0f - squashX) * 0.2f;
            squashY += (1.0f - squashY) * 0.2f;
        }
    }

    public boolean canTeleport() {
        return teleportCooldown <= 0;
    }

    private com.game.pullthepin.shop.SkinManager.BallSkin activeSkin = null;

    public void applySkin(com.game.pullthepin.shop.SkinManager.BallSkin skin) {
        this.activeSkin = skin;
        if (type == BallType.COLORED && skin != null && skin != com.game.pullthepin.shop.SkinManager.BallSkin.CLASSIC) {
            this.color = skin.primary.cpy();
            this.darkShade = skin.secondary.cpy();
            this.lightShade = skin.specular.cpy();
            this.rimGlowColor = new Color(skin.primary.r, skin.primary.g, skin.primary.b, 0.65f);
            this.trailColor = new Color(skin.primary.r, skin.primary.g, skin.primary.b, 0.40f);
        }
    }

    private com.game.pullthepin.shop.SkinManager.TrailSkin activeTrailSkin = null;

    public void applyTrailSkin(com.game.pullthepin.shop.SkinManager.TrailSkin trailSkin) {
        this.activeTrailSkin = trailSkin;
        if (trailSkin != null && trailSkin != com.game.pullthepin.shop.SkinManager.TrailSkin.DEFAULT) {
            this.trailColor = trailSkin.primary.cpy();
        }
    }

    public void setTeleportCooldown(float cooldown) {
        this.teleportCooldown = cooldown;
    }

    public boolean colorize(Color sourceColor) {
        if (type == BallType.GRAY) {
            type = BallType.COLORED;
            if (activeSkin != null && activeSkin != com.game.pullthepin.shop.SkinManager.BallSkin.CLASSIC) {
                color = activeSkin.primary.cpy();
                darkShade = activeSkin.secondary.cpy();
                lightShade = activeSkin.specular.cpy();
                rimGlowColor = new Color(activeSkin.primary.r, activeSkin.primary.g, activeSkin.primary.b, 0.65f);
                trailColor = new Color(activeSkin.primary.r, activeSkin.primary.g, activeSkin.primary.b, 0.40f);
            } else {
                color = sourceColor != null ? sourceColor.cpy() : CHILL_PALETTE[MathUtils.random(CHILL_PALETTE.length - 1)].cpy();
                updateShadeColors();
            }
            return true;
        }
        return false;
    }

    /**
     * AAA Kalitesinde Katmanlı 2.5D Kristal Cam Render Pipeline
     */
    public void render(ShapeRenderer shapeRenderer) {
        if (isDead || body == null) return;

        Vector2 pos = body.getPosition();
        Vector2 vel = body.getLinearVelocity();
        float speed = vel.len();

        // 1. Dinamik Hareket Işık İzi (Speed Glow Motion Trail)
        if (speed > 2.2f && type != BallType.GRAY) {
            for (int i = 0; i < TRAIL_LENGTH; i++) {
                int idx = (trailIndex + i) % TRAIL_LENGTH;
                Vector2 tp = trailPositions[idx];
                float factor = (float) (i + 1) / TRAIL_LENGTH;

                if (activeTrailSkin != null && activeTrailSkin != com.game.pullthepin.shop.SkinManager.TrailSkin.DEFAULT) {
                    if (activeTrailSkin == com.game.pullthepin.shop.SkinManager.TrailSkin.RAINBOW) {
                        float hue = (animTimer * 2.2f + (float) i / TRAIL_LENGTH) % 1.0f;
                        float r = 0.5f + 0.5f * MathUtils.sin(hue * 6.28f);
                        float g = 0.5f + 0.5f * MathUtils.sin(hue * 6.28f + 2.09f);
                        float b = 0.5f + 0.5f * MathUtils.sin(hue * 6.28f + 4.18f);
                        shapeRenderer.setColor(r, g, b, 0.65f * factor);
                        shapeRenderer.circle(tp.x, tp.y, RADIUS * (0.35f + factor * 0.55f), 12);
                    } else {
                        // Dış Parıltı (Aura Glow)
                        shapeRenderer.setColor(activeTrailSkin.glow.r, activeTrailSkin.glow.g, activeTrailSkin.glow.b, activeTrailSkin.glow.a * factor * 0.50f);
                        shapeRenderer.circle(tp.x, tp.y, RADIUS * (0.45f + factor * 0.65f), 12);
                        // İç Çekirdek (Core Primary)
                        shapeRenderer.setColor(activeTrailSkin.primary.r, activeTrailSkin.primary.g, activeTrailSkin.primary.b, activeTrailSkin.primary.a * factor * 0.85f);
                        shapeRenderer.circle(tp.x, tp.y, RADIUS * (0.25f + factor * 0.40f), 12);
                    }
                } else {
                    shapeRenderer.setColor(trailColor.r, trailColor.g, trailColor.b, trailColor.a * factor * 0.7f);
                    shapeRenderer.circle(tp.x, tp.y, RADIUS * (0.35f + factor * 0.55f), 12);
                }
            }
        }

        // 2. Yumuşak Zemin Ambient Occlusion Derinlik Gölgesi
        shapeRenderer.setColor(0f, 0f, 0f, 0.28f);
        shapeRenderer.circle(pos.x + 0.045f, pos.y - 0.06f, RADIUS * 0.95f, 20);
        shapeRenderer.setColor(0f, 0f, 0f, 0.14f);
        shapeRenderer.circle(pos.x + 0.06f, pos.y - 0.075f, RADIUS * 1.15f, 20);

        // 3. Küresel Taban Gradyanı (3D Dark Base Shading)
        shapeRenderer.setColor(darkShade);
        shapeRenderer.circle(pos.x, pos.y, RADIUS * squashY, 24);

        if (type == BallType.BOMB) {
            // --- BOMBA: Obsidyen Zırh + Nabız Atan Magma Çekirdek ---
            float pulse = 0.85f + 0.15f * (float) Math.sin(animTimer * 9f);

            // Siyah Dış Kabuk
            shapeRenderer.setColor(0.10f, 0.11f, 0.15f, 1.0f);
            shapeRenderer.circle(pos.x, pos.y, RADIUS * 0.94f, 24);

            // Magma Çatlak/Işıma Halkası
            shapeRenderer.setColor(1.0f, 0.30f, 0.10f, 0.50f * pulse);
            shapeRenderer.circle(pos.x, pos.y, RADIUS * 0.76f * pulse, 20);

            // Kırmızı/Turuncu Çekirdek
            shapeRenderer.setColor(0.98f, 0.18f, 0.22f, 1.0f);
            shapeRenderer.circle(pos.x, pos.y, RADIUS * 0.48f * pulse, 16);

            // Kor Sarı Merkez
            shapeRenderer.setColor(1.0f, 0.90f, 0.40f, 1.0f);
            shapeRenderer.circle(pos.x, pos.y, RADIUS * 0.24f, 12);

            // Fitil Kıvılcımı
            shapeRenderer.setColor(1.0f, 0.95f, 0.7f, 1.0f);
            float sparkAngle = animTimer * 14f;
            float sx = pos.x + MathUtils.cos(sparkAngle) * 0.05f;
            float sy = pos.y + RADIUS + 0.04f + MathUtils.sin(sparkAngle) * 0.03f;
            shapeRenderer.circle(sx, sy, 0.045f, 8);
            return;
        }

        if (type == BallType.WATER) {
            // Su Küresi: Kristal cam dış kabuk + dalgalı saf akuamarin çekirdek
            float pulse = 0.5f + 0.5f * (float) Math.sin(animTimer * 5f);
            shapeRenderer.setColor(0.20f, 0.85f, 1.0f, 0.28f + pulse * 0.15f);
            shapeRenderer.circle(pos.x, pos.y, RADIUS * 1.25f, 20);

            shapeRenderer.setColor(0.06f, 0.40f, 0.70f, 1.0f);
            shapeRenderer.circle(pos.x, pos.y, RADIUS * 0.94f, 22);

            shapeRenderer.setColor(0.18f, 0.85f, 1.0f, 0.95f);
            shapeRenderer.circle(pos.x - 0.01f, pos.y + 0.01f, RADIUS * 0.78f, 20);

            shapeRenderer.setColor(0.60f, 0.95f, 1.0f, 0.85f);
            shapeRenderer.circle(pos.x - 0.03f, pos.y + 0.03f, RADIUS * 0.45f, 16);

            shapeRenderer.setColor(1.0f, 1.0f, 1.0f, 0.95f);
            shapeRenderer.circle(pos.x - 0.07f, pos.y + 0.07f, RADIUS * 0.25f, 12);
            shapeRenderer.circle(pos.x - 0.09f, pos.y + 0.09f, RADIUS * 0.10f, 8);
            return;
        }

        // Özel Prestij & Elementel Skin Işıltısı (Aura Glow)
        if (activeSkin != null && type == BallType.COLORED) {
            if (activeSkin == com.game.pullthepin.shop.SkinManager.BallSkin.GOLD) {
                float haloPulse = 0.5f + 0.5f * (float) Math.sin(animTimer * 4f);
                shapeRenderer.setColor(1.0f, 0.85f, 0.22f, 0.25f + haloPulse * 0.15f);
                shapeRenderer.circle(pos.x, pos.y, RADIUS * 1.35f, 20);
            } else if (activeSkin == com.game.pullthepin.shop.SkinManager.BallSkin.FIRE) {
                float firePulse = 0.5f + 0.5f * (float) Math.sin(animTimer * 7f);
                shapeRenderer.setColor(1.0f, 0.35f, 0.05f, 0.25f + firePulse * 0.18f);
                shapeRenderer.circle(pos.x, pos.y, RADIUS * 1.25f, 20);
            } else if (activeSkin == com.game.pullthepin.shop.SkinManager.BallSkin.DIAMOND) {
                shapeRenderer.setColor(0.9f, 0.98f, 1.0f, 0.32f);
                shapeRenderer.circle(pos.x, pos.y, RADIUS * 1.30f, 20);
            } else if (activeSkin == com.game.pullthepin.shop.SkinManager.BallSkin.BLACKHOLE) {
                shapeRenderer.setColor(0.45f, 0.15f, 0.70f, 0.35f);
                shapeRenderer.circle(pos.x, pos.y, RADIUS * 1.25f, 20);
            }
        }

        // 4. Dış Kristal Fresnel Işıma Halkası (Outer Glass Rim)
        shapeRenderer.setColor(rimGlowColor);
        shapeRenderer.circle(pos.x, pos.y, RADIUS * 0.98f * squashX, 24);

        // 5. Ana Kristal Renk Küresi
        shapeRenderer.setColor(color);
        shapeRenderer.circle(pos.x - 0.015f, pos.y + 0.018f, RADIUS * 0.88f * squashX, 24);

        // 6. İç Kırılma Işık Katmanı (Subsurface Light Core)
        shapeRenderer.setColor(lightShade.r, lightShade.g, lightShade.b, 0.65f);
        shapeRenderer.circle(pos.x - RADIUS * 0.16f, pos.y + RADIUS * 0.16f, RADIUS * 0.58f, 20);

        if (type == BallType.GRAY) {
            // Mat Saten İnci: Yumuşak difüz parlama
            shapeRenderer.setColor(1f, 1f, 1f, 0.60f);
            shapeRenderer.circle(pos.x - RADIUS * 0.30f, pos.y + RADIUS * 0.30f, RADIUS * 0.26f, 14);
            shapeRenderer.setColor(1f, 1f, 1f, 0.90f);
            shapeRenderer.circle(pos.x - RADIUS * 0.32f, pos.y + RADIUS * 0.32f, RADIUS * 0.12f, 10);
        } else {
            // Kristal Cam Bilye: Çift Katmanlı Aynasal Parlama (Dual Specular Glint & Caustics)
            // Alt Kaustik Yansıma (Caustic Bottom Bounce)
            shapeRenderer.setColor(lightShade.r, lightShade.g, lightShade.b, 0.45f);
            shapeRenderer.circle(pos.x + RADIUS * 0.25f, pos.y - RADIUS * 0.28f, RADIUS * 0.22f, 14);

            // Birincil Keskin Işık Parıltısı (Primary Sharp Glint)
            shapeRenderer.setColor(1f, 1f, 1f, 0.95f);
            shapeRenderer.circle(pos.x - RADIUS * 0.35f, pos.y + RADIUS * 0.35f, RADIUS * 0.26f, 16);

            // Merkez İğne Parlaması (Needle Pin-Point Light)
            shapeRenderer.setColor(Color.WHITE);
            shapeRenderer.circle(pos.x - RADIUS * 0.36f, pos.y + RADIUS * 0.36f, RADIUS * 0.11f, 10);

            // İkincil Yumuşak Yay Parıltısı (Secondary Arc)
            shapeRenderer.setColor(1f, 1f, 1f, 0.50f);
            shapeRenderer.circle(pos.x - RADIUS * 0.16f, pos.y + RADIUS * 0.52f, RADIUS * 0.12f, 10);
        }
    }

    public Vector2 getPosition() { return body.getPosition(); }
    public void setPosition(float x, float y) {
        if (body != null) {
            body.setTransform(x, y, body.getAngle());
            body.setLinearVelocity(0, 0);
            body.setAngularVelocity(0);
        }
        for (int i = 0; i < TRAIL_LENGTH; i++) {
            if (trailPositions[i] != null) {
                trailPositions[i].set(x, y);
            }
        }
    }
    public Body getBody() { return body; }
    public BallType getType() { return type; }
    public Color getColor() { return color; }
    public boolean isDead() { return isDead; }
    public void setDead(boolean dead) { isDead = dead; }
    public boolean isCollected() { return isCollected; }
    public void setCollected(boolean collected) { isCollected = collected; }
    public void setRestitution(float rest) {
        if (body != null) {
            for (Fixture f : body.getFixtureList()) {
                f.setRestitution(rest);
            }
        }
    }
}
