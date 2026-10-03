package com.game.pullthepin.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

/**
 * Üst Düzey 2.5D İzometrik Kristal Cam Kavanoz (Crystal Glass Vase with Dynamic Fluid Wave Shader).
 * Dinamik dalgalı sıvı yüzeyi, köpüren kabarcıklar, kademeli doluluk çizgileri ve 3D cam kırılma parlamaları.
 */
public class Jar {

    private World world;
    private Body body;
    private Body sensorBody;

    private float x, y;
    private float width;
    private float height;
    private float wallThickness = 0.24f;

    private int collectedBalls = 0;
    private int requiredBalls = 0;
    private float fillAnimation = 0f;
    private float waveTimer = 0f;
    private float currentTilt = 0f;

    // Şık Kristal Cam ve Sıvı Renkleri
    private final Color GLASS_OUTLINE = new Color(0.45f, 0.82f, 1.0f, 0.85f);
    private final Color GLASS_BASE_TINT = new Color(0.18f, 0.55f, 0.90f, 0.16f);
    private final Color GLASS_GLINT = new Color(1.0f, 1.0f, 1.0f, 0.28f);
    private final Color FLUID_BODY = new Color(0.20f, 0.85f, 0.65f, 0.42f);
    private final Color FLUID_SURFACE = new Color(0.55f, 1.00f, 0.85f, 0.95f);
    private final Color FLUID_FOAM = new Color(1.00f, 1.00f, 1.00f, 0.85f);

    public Jar(World world, float x, float y, float width, float height, int requiredBalls) {
        this.world = world;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.requiredBalls = requiredBalls;

        createPhysics();
    }

    private void createPhysics() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(x, y);

        body = world.createBody(bodyDef);
        body.setUserData(this);

        PolygonShape bottomShape = new PolygonShape();
        bottomShape.setAsBox(width / 2f, wallThickness / 2f, new Vector2(0, -height / 2f), 0);
        body.createFixture(bottomShape, 0);
        bottomShape.dispose();

        PolygonShape leftShape = new PolygonShape();
        leftShape.setAsBox(wallThickness / 2f, height / 2f, new Vector2(-width / 2f, 0), 0);
        body.createFixture(leftShape, 0);
        leftShape.dispose();

        PolygonShape rightShape = new PolygonShape();
        rightShape.setAsBox(wallThickness / 2f, height / 2f, new Vector2(width / 2f, 0), 0);
        body.createFixture(rightShape, 0);
        rightShape.dispose();

        // Sensör
        BodyDef sensorDef = new BodyDef();
        sensorDef.type = BodyDef.BodyType.StaticBody;
        sensorDef.position.set(x, y);

        sensorBody = world.createBody(sensorDef);
        sensorBody.setUserData("JAR_SENSOR");

        PolygonShape sensorShape = new PolygonShape();
        sensorShape.setAsBox(width / 2f - wallThickness, height / 2.2f);

        FixtureDef sensorFixture = new FixtureDef();
        sensorFixture.shape = sensorShape;
        sensorFixture.isSensor = true;

        sensorBody.createFixture(sensorFixture);
        sensorShape.dispose();
    }

    public void incrementBalls() {
        collectedBalls++;
    }

    public boolean isTargetReached() {
        return collectedBalls >= requiredBalls && requiredBalls > 0;
    }

    /**
     * 2.5D Kristal Cam & Dinamik Sıvı Dalgası Render Pipeline
     */
    public void render(ShapeRenderer shapeRenderer) {
        waveTimer += 0.05f;
        float fillRatio = requiredBalls > 0 ? Math.min(1.0f, (float) collectedBalls / (float) requiredBalls) : 0f;
        fillAnimation += (fillRatio - fillAnimation) * 0.12f;

        float innerW = width - wallThickness;
        float innerH = height - wallThickness / 2f;
        float baseX = x - width / 2f + wallThickness / 2f;
        float baseY = y - height / 2f;

        // 1. Zemin Derinlik Gölgesi
        shapeRenderer.setColor(0f, 0f, 0f, 0.28f);
        shapeRenderer.rect(x - width / 2f + 0.08f, y - height / 2f - 0.14f, width, height * 0.35f);

        // 2. Yarı Saydam Kristal Cam İç Hacmi
        shapeRenderer.setColor(GLASS_BASE_TINT);
        shapeRenderer.rect(baseX, baseY, innerW, height);

        // 3. Sıvı Katmanı ve Dinamik Dalgalar (Dynamic Sine Wave Fluid)
        if (fillAnimation > 0.02f) {
            float fluidH = innerH * fillAnimation;

            // Sıvı Gövdesi
            shapeRenderer.setColor(FLUID_BODY);
            shapeRenderer.rect(baseX, baseY, innerW, fluidH);

            // Alt Taban Işıması
            shapeRenderer.setColor(0.35f, 1.0f, 0.75f, 0.25f);
            shapeRenderer.rect(baseX, baseY, innerW, 0.15f);

            // Dalgalı Sıvı Üst Yüzeyi (Dinamik Çift Dalga Eğim & Köpük Çizgisi)
            shapeRenderer.setColor(FLUID_SURFACE);
            int segments = 20;
            float segW = innerW / segments;
            float tiltSlope = currentTilt * 0.05f;
            for (int i = 0; i < segments; i++) {
                float sx1 = baseX + i * segW;
                float sx2 = sx1 + segW;
                float t1 = ((float) i / segments - 0.5f) * tiltSlope;
                float t2 = ((float) (i + 1) / segments - 0.5f) * tiltSlope;
                float w1 = (float) Math.sin(waveTimer * 3.5f + i * 0.55f) * 0.04f + (float) Math.cos(waveTimer * 5.0f + i * 0.8f) * 0.015f;
                float w2 = (float) Math.sin(waveTimer * 3.5f + (i + 1) * 0.55f) * 0.04f + (float) Math.cos(waveTimer * 5.0f + (i + 1) * 0.8f) * 0.015f;
                float waveY1 = baseY + fluidH + w1 + t1;
                float waveY2 = baseY + fluidH + w2 + t2;
                shapeRenderer.rectLine(sx1, waveY1, sx2, waveY2, 0.09f);
            }

            // Yüzen Mikro Köpük Kabarcıkları (Rising Foam Bubbles)
            shapeRenderer.setColor(FLUID_FOAM);
            float b1X = baseX + innerW * 0.25f + (float) Math.sin(waveTimer * 2f) * 0.12f;
            float b1Y = baseY + fluidH + (float) Math.sin(waveTimer * 3.5f + 1.2f) * 0.04f - 0.25f * tiltSlope;
            shapeRenderer.circle(b1X, b1Y, 0.04f, 8);

            float b2X = baseX + innerW * 0.65f + (float) Math.cos(waveTimer * 2.5f) * 0.12f;
            float b2Y = baseY + fluidH + (float) Math.sin(waveTimer * 3.5f + 3.5f) * 0.04f + 0.18f * tiltSlope;
            shapeRenderer.circle(b2X, b2Y, 0.045f, 8);

            float b3X = baseX + innerW * 0.48f + (float) Math.sin(waveTimer * 3f + 2f) * 0.08f;
            float b3Y = baseY + fluidH * 0.45f + (float) Math.sin(waveTimer * 4f) * 0.08f;
            shapeRenderer.circle(b3X, b3Y, 0.03f, 6);
        }

        // 4. Cam Kademe Çizgileri (Level Graduation Markers)
        shapeRenderer.setColor(1.0f, 1.0f, 1.0f, 0.40f);
        for (float f = 0.25f; f <= 0.75f; f += 0.25f) {
            float my = baseY + innerH * f;
            shapeRenderer.rectLine(baseX + 0.08f, my, baseX + 0.38f, my, 0.035f);
            shapeRenderer.rectLine(baseX + innerW - 0.38f, my, baseX + innerW - 0.08f, my, 0.035f);
        }

        // 5. 3D Kalın Kristal Cam Duvarlar
        shapeRenderer.setColor(GLASS_OUTLINE);
        // Sol Duvar
        shapeRenderer.rect(x - width / 2f - wallThickness / 2f, y - height / 2f, wallThickness, height);
        // Sağ Duvar
        shapeRenderer.rect(x + width / 2f - wallThickness / 2f, y - height / 2f, wallThickness, height);
        // Alt Taban
        shapeRenderer.rect(x - width / 2f - wallThickness / 2f, y - height / 2f - wallThickness / 2f, width + wallThickness, wallThickness);

        // 6. Cam Yüzeyinde Aynasal Işık Kırılma Şeritleri (Glass Specular Streaks)
        shapeRenderer.setColor(GLASS_GLINT);
        shapeRenderer.rect(baseX + 0.15f, baseY + 0.1f, 0.16f, height - 0.2f);
        shapeRenderer.rect(baseX + 0.38f, baseY + 0.2f, 0.07f, height - 0.4f);

        // 7. İzometrik 3D Üst Ağız Halkası (Polished Glass Rim)
        shapeRenderer.setColor(0.65f, 0.90f, 1.0f, 0.95f);
        shapeRenderer.rect(x - width / 2f - wallThickness, y + height / 2f - 0.08f, width + wallThickness * 2f, 0.16f);
        shapeRenderer.setColor(Color.WHITE);
        shapeRenderer.rect(x - width / 2f, y + height / 2f - 0.02f, width, 0.06f);
    }

    public int getCollectedBalls() { return collectedBalls; }
    public int getRequiredBalls() { return requiredBalls; }
    public void setRequiredBalls(int req) { this.requiredBalls = req; }
    public float getX() { return x; }
    public float getY() { return y; }
    public void setFluidTilt(float tilt) { this.currentTilt = tilt; }
}
