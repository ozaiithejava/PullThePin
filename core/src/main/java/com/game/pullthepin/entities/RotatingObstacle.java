package com.game.pullthepin.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

/**
 * 2.5D Dönen Mekanik Çark / Pervane Engeli (Rotating Paddle & Cogwheel Obstacle).
 * Box2D KinematicBody ile fiziksel tork ve açısal hız uygular. Toplar dönen
 * kollara çarpar, kanatların üzerinde yuvarlanır veya savrularak yönlendirilir.
 */
public class RotatingObstacle {

    public enum RotorType {
        CROSS_PADDLE_4(4),  // 4 Kanatlı Dönen Çark (Cross Rotor)
        TWIN_BAR_2(2),      // 2 Kanatlı Uzun Süpürücü (Dual Blade)
        TRI_ROTOR_3(3),     // 3 Kanatlı Yıldız Çarkı (Tri Blade)
        GEAR_DISC_6(6);     // 6 Dişli Dönen Mekanik Çark (Cogwheel)

        public final int bladeCount;
        RotorType(int bladeCount) {
            this.bladeCount = bladeCount;
        }
    }

    private Vector2 position;
    private RotorType type;
    private float armLength;
    private float armWidth;
    private float angularSpeed; // rad/sec
    private Color accentColor;
    private Body body;
    private float angle = 0f; // radians

    public RotatingObstacle(World world, float x, float y, RotorType type, float armLength, float armWidth, float angularSpeed, Color accentColor) {
        this.position = new Vector2(x, y);
        this.type = type;
        this.armLength = armLength;
        this.armWidth = armWidth;
        this.angularSpeed = angularSpeed;
        this.accentColor = (accentColor != null) ? accentColor : new Color(0.2f, 0.85f, 1.0f, 1.0f);

        initPhysics(world);
    }

    private void initPhysics(World world) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.KinematicBody;
        bodyDef.position.set(position);
        bodyDef.angularVelocity = angularSpeed;

        body = world.createBody(bodyDef);
        body.setUserData(this);

        int blades = type.bladeCount;
        float angleStep = (float) (2 * Math.PI / blades);

        for (int i = 0; i < blades; i++) {
            float bladeAngle = i * angleStep;
            PolygonShape shape = new PolygonShape();
            
            // Kanadın merkezden dışa doğru uzanması
            Vector2 bladeCenter = new Vector2(
                    (float) Math.cos(bladeAngle) * (armLength * 0.5f),
                    (float) Math.sin(bladeAngle) * (armLength * 0.5f)
            );
            
            shape.setAsBox(armLength * 0.5f, armWidth * 0.5f, bladeCenter, bladeAngle);

            FixtureDef fixtureDef = new FixtureDef();
            fixtureDef.shape = shape;
            fixtureDef.density = 5.0f;
            fixtureDef.friction = 0.25f;
            fixtureDef.restitution = 0.55f; // Bouncy ve dinamik çarpma

            body.createFixture(fixtureDef);
            shape.dispose();
        }

        // Merkez Şaft Çemberi (Hub Fixture)
        CircleShape hubShape = new CircleShape();
        hubShape.setRadius(armWidth * 1.3f);
        FixtureDef hubFix = new FixtureDef();
        hubFix.shape = hubShape;
        hubFix.friction = 0.3f;
        hubFix.restitution = 0.4f;
        body.createFixture(hubFix);
        hubShape.dispose();
    }

    public void update(float delta) {
        if (body != null) {
            angle = body.getAngle();
        } else {
            angle += angularSpeed * delta;
        }
    }

    public void render(ShapeRenderer sr) {
        float cx = position.x;
        float cy = position.y;
        int blades = type.bladeCount;
        float angleStep = (float) (2 * Math.PI / blades);

        // 1. DÖNME İZİ & DIŞ AMBİYANS HALKASI (Motion Glow Aura)
        float motionRadius = armLength + 0.08f;
        sr.setColor(accentColor.r, accentColor.g, accentColor.b, 0.09f);
        sr.circle(cx, cy, motionRadius, 32);

        sr.setColor(accentColor.r, accentColor.g, accentColor.b, 0.04f);
        sr.circle(cx, cy, motionRadius * 1.18f, 32);

        // 2. KANATLARIN 2.5D GÖLGESİ (Drop Shadow)
        float shadowOffX = 0.06f;
        float shadowOffY = -0.07f;
        sr.setColor(0.02f, 0.03f, 0.06f, 0.38f);
        for (int i = 0; i < blades; i++) {
            float curAngle = angle + i * angleStep;
            float endX = cx + shadowOffX + (float) Math.cos(curAngle) * armLength;
            float endY = cy + shadowOffY + (float) Math.sin(curAngle) * armLength;
            sr.rectLine(cx + shadowOffX, cy + shadowOffY, endX, endY, armWidth * 1.1f);
        }

        // 3. KANATLARIN ANA GÖVDESİ (Metallic Dark Core + Neon Edges)
        for (int i = 0; i < blades; i++) {
            float curAngle = angle + i * angleStep;
            float cosA = (float) Math.cos(curAngle);
            float sinA = (float) Math.sin(curAngle);

            float startX = cx;
            float startY = cy;
            float endX = cx + cosA * armLength;
            float endY = cy + sinA * armLength;

            // Koyu Metalik Çerçeve
            sr.setColor(0.12f, 0.16f, 0.25f, 1.0f);
            sr.rectLine(startX, startY, endX, endY, armWidth);

            // İç Neon Enerji Çizgisi
            sr.setColor(accentColor.r, accentColor.g, accentColor.b, 0.92f);
            sr.rectLine(startX + cosA * (armWidth * 0.8f), startY + sinA * (armWidth * 0.8f),
                        endX - cosA * 0.05f, endY - sinA * 0.05f, armWidth * 0.42f);

            // Kanat Ucu Parlayan Elmas / Düğüm Noktası
            sr.setColor(1.0f, 1.0f, 1.0f, 0.95f);
            sr.circle(endX, endY, armWidth * 0.32f, 10);
            sr.setColor(accentColor);
            sr.circle(endX, endY, armWidth * 0.55f, 10);
        }

        // 4. MERKEZ RULMAN & ŞAFT GÖBEĞİ (3D Metallic Bearing Hub)
        float hubR = armWidth * 1.35f;

        // Dış Şaft Gölgesi
        sr.setColor(0.04f, 0.06f, 0.10f, 0.95f);
        sr.circle(cx, cy, hubR * 1.25f, 24);

        // Metalik Krom Halka
        sr.setColor(0.35f, 0.42f, 0.55f, 1.0f);
        sr.circle(cx, cy, hubR, 20);

        // İç Neon Çekirdek
        sr.setColor(accentColor.r, accentColor.g, accentColor.b, 0.85f);
        sr.circle(cx, cy, hubR * 0.70f, 16);

        // Merkez Titanyum Vida / LED Işıltısı
        float pulse = 0.75f + 0.25f * (float) Math.sin(angle * 6.0f);
        sr.setColor(1.0f, 1.0f, 1.0f, 0.95f * pulse);
        sr.circle(cx, cy, hubR * 0.35f, 12);
    }

    public Vector2 getPosition() { return position; }
    public Body getBody() { return body; }
    public RotorType getType() { return type; }
    public float getArmLength() { return armLength; }
    public Color getAccentColor() { return accentColor; }
}
