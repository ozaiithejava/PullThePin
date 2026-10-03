package com.game.pullthepin.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

/**
 * 2.5D Zehirli Asit Havuzu & Kimyasal Nötralizasyon Sistemi (Acid Pool & Neutralizer).
 * - Aktifken: İçi kaynayan neon yeşili ölümcül asit. Temas eden bilyeleri anında eritir.
 * - Su Bilyesi (Water Ball) temas ettiğinde: Devasa buhar patlamasıyla nötralize olur!
 * - Nötralize olduğunda: Berrak, masmavi, sakin kristal suya dönüşür ve bilyeler güvenle geçer.
 */
public class AcidPool {

    private Body sensorBody;
    private float x, y, width, height;
    private boolean neutralized = false;
    private float animTimer = 0f;

    // Renk Paleti - Aktif Zehirli Asit
    private final Color TANK_BORDER   = new Color(0.12f, 0.14f, 0.18f, 1.0f);
    private final Color ACID_DEEP     = new Color(0.08f, 0.45f, 0.12f, 0.90f);
    private final Color ACID_BODY     = new Color(0.18f, 0.78f, 0.22f, 0.85f);
    private final Color ACID_SURFACE  = new Color(0.35f, 1.00f, 0.35f, 0.98f);
    private final Color ACID_BUBBLE   = new Color(0.65f, 1.00f, 0.50f, 0.85f);

    // Renk Paleti - Nötralize Olmuş Temiz Su
    private final Color WATER_DEEP    = new Color(0.05f, 0.30f, 0.58f, 0.75f);
    private final Color WATER_BODY    = new Color(0.12f, 0.62f, 0.92f, 0.70f);
    private final Color WATER_SURFACE = new Color(0.55f, 0.90f, 1.00f, 0.95f);

    public AcidPool(World world, float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

        initPhysics(world);
    }

    private void initPhysics(World world) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(x, y);

        sensorBody = world.createBody(bodyDef);
        sensorBody.setUserData(this);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width / 2f, height / 2f);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.isSensor = true;

        sensorBody.createFixture(fixtureDef);
        shape.dispose();
    }

    public void update(float delta) {
        animTimer += delta;
    }

    public void neutralize() {
        this.neutralized = true;
    }

    public boolean isNeutralized() {
        return neutralized;
    }

    public float getX() { return x; }
    public float getY() { return y; }
    public float getWidth() { return width; }
    public float getHeight() { return height; }

    /**
     * 2.5D Akrilik Sıvı ve Dalga Yüzey Render Pipeline
     */
    public void render(ShapeRenderer shapeRenderer) {
        float left = x - width / 2f;
        float bottom = y - height / 2f;
        float top = y + height / 2f;

        // 1. Zemin Derinlik Gölgesi
        shapeRenderer.setColor(0f, 0f, 0f, 0.35f);
        shapeRenderer.rect(left + 0.06f, bottom - 0.08f, width, height);

        // 2. Tank Metal Tabanı & Yan Duvar Çerçeveleri
        shapeRenderer.setColor(TANK_BORDER);
        float wallThick = 0.08f;
        shapeRenderer.rect(left - wallThick, bottom - wallThick, width + 2 * wallThick, height + wallThick);

        // 3. Tank Alt Kenarındaki Tehlike Şeritleri (Hazard Stripes)
        if (!neutralized) {
            shapeRenderer.setColor(1.0f, 0.78f, 0.12f, 0.85f);
            for (float hx = left; hx < left + width; hx += 0.28f) {
                shapeRenderer.rectLine(hx, bottom - wallThick, hx + 0.12f, bottom, 0.035f);
            }
        } else {
            // Nötralize olduğunda güvenli nane yeşili LED şerit
            shapeRenderer.setColor(0.20f, 0.90f, 0.60f, 0.85f);
            shapeRenderer.rect(left, bottom - wallThick * 0.7f, width, 0.035f);
        }

        // 4. Sıvı Tabanı (Deep Shading)
        Color deepCol = neutralized ? WATER_DEEP : ACID_DEEP;
        Color bodyCol = neutralized ? WATER_BODY : ACID_BODY;
        Color surfCol = neutralized ? WATER_SURFACE : ACID_SURFACE;

        shapeRenderer.setColor(deepCol);
        shapeRenderer.rect(left, bottom, width, height * 0.45f);

        // 5. Sıvı Gövdesi (Body)
        shapeRenderer.setColor(bodyCol);
        shapeRenderer.rect(left, bottom + height * 0.35f, width, height * 0.55f);

        // 6. Dinamik Dalgalı Sıvı Yüzeyi
        int waveSegments = 16;
        float segW = width / waveSegments;
        float waveSpeed = neutralized ? 2.8f : 5.5f;
        float waveAmp = neutralized ? 0.022f : 0.045f;

        for (int i = 0; i < waveSegments; i++) {
            float x1 = left + i * segW;
            float x2 = x1 + segW;
            float y1 = top + (float) Math.sin(animTimer * waveSpeed + x1 * 4.5f) * waveAmp;
            float y2 = top + (float) Math.sin(animTimer * waveSpeed + x2 * 4.5f) * waveAmp;

            shapeRenderer.setColor(bodyCol);
            shapeRenderer.triangle(x1, top - 0.06f, x2, top - 0.06f, x2, y2);
            shapeRenderer.triangle(x1, top - 0.06f, x2, y2, x1, y1);

            // Parlak Yüzey Hattı
            shapeRenderer.setColor(surfCol);
            shapeRenderer.rectLine(x1, y1, x2, y2, 0.04f);
        }

        // 7. Sıvı İçi Kabarcıklar
        if (!neutralized) {
            // Asit Köpüren Gaz Kabarcıkları
            for (int b = 0; b < 5; b++) {
                float bx = left + width * (0.15f + b * 0.18f) + (float) Math.sin(animTimer * 3f + b) * 0.06f;
                float bProgress = (animTimer * 0.8f + b * 0.23f) % 1.0f;
                float by = bottom + bProgress * height;
                float bSize = 0.035f + 0.02f * (float) Math.sin(animTimer * 4f + b);

                shapeRenderer.setColor(ACID_BUBBLE);
                shapeRenderer.circle(bx, by, bSize, 10);
                shapeRenderer.setColor(Color.WHITE);
                shapeRenderer.circle(bx - 0.01f, by + 0.01f, bSize * 0.35f, 6);
            }

            // Asit Yüzeyinde Toksik Buhar Parıltısı
            float vaporPulse = 0.5f + 0.5f * (float) Math.sin(animTimer * 4f);
            shapeRenderer.setColor(0.35f, 1.0f, 0.35f, 0.12f + vaporPulse * 0.08f);
            shapeRenderer.rect(left, top - 0.04f, width, 0.18f);
        } else {
            // Nötralize Su Yüzeyi Parıltı Yıldızları
            for (int s = 0; s < 4; s++) {
                float sx = left + width * (0.2f + s * 0.22f);
                float sy = top + (float) Math.sin(animTimer * 2f + s) * 0.03f;
                float sAlpha = 0.4f + 0.4f * (float) Math.sin(animTimer * 4.5f + s * 1.5f);
                shapeRenderer.setColor(1.0f, 1.0f, 1.0f, sAlpha);
                shapeRenderer.circle(sx, sy, 0.025f, 8);
            }
        }
    }
}
