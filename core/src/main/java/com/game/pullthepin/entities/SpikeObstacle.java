package com.game.pullthepin.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

/**
 * 2.5D Obsidyen Kristal Dikenler (Geometric Hazard Spikes).
 * Kor kırmızısı uç parıltıları, gölgelendirilmiş 3D piramit fasetleri ve taban montajı.
 */
public class SpikeObstacle {

    private Body body;
    private float x, y, width, height;
    private int numSpikes = 4;

    private final Color BASE_BAR = new Color(0.12f, 0.14f, 0.18f, 1.0f);
    private final Color SPIKE_SHADOW = new Color(0.38f, 0.08f, 0.12f, 1.0f);
    private final Color SPIKE_BODY = new Color(0.85f, 0.18f, 0.22f, 1.0f);
    private final Color SPIKE_TIP = new Color(1.00f, 0.75f, 0.45f, 1.0f);

    public SpikeObstacle(World world, float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(x, y);

        body = world.createBody(bodyDef);
        body.setUserData(this);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width / 2f, height / 2f);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.isSensor = true;

        body.createFixture(fixtureDef);
        shape.dispose();
    }

    public void render(ShapeRenderer shapeRenderer) {
        float spikeW = width / numSpikes;
        float startX = x - width / 2f;
        float baseY = y - height / 2f;

        // 1. Zemin Derinlik Gölgesi
        shapeRenderer.setColor(0f, 0f, 0f, 0.32f);
        shapeRenderer.rect(startX + 0.06f, baseY - 0.06f, width, height * 0.4f);

        // 2. Taban Metal Rayı & Tehlike İkaz Çizgileri (Hazard Stripes)
        shapeRenderer.setColor(BASE_BAR);
        shapeRenderer.rect(startX, baseY, width, 0.09f);
        shapeRenderer.setColor(1.0f, 0.78f, 0.18f, 0.85f);
        for (float hx = startX + 0.05f; hx < startX + width; hx += 0.25f) {
            shapeRenderer.rectLine(hx, baseY, hx + 0.08f, baseY + 0.09f, 0.03f);
        }

        // 3. 2.5D Piramit Kristal Dikenler
        for (int i = 0; i < numSpikes; i++) {
            float sx = startX + i * spikeW;
            float midX = sx + spikeW / 2f;
            float tipY = baseY + height;

            // Sol Gölgeli Obsidyen Yüzey
            shapeRenderer.setColor(SPIKE_SHADOW);
            shapeRenderer.triangle(sx, baseY + 0.08f, midX, baseY + 0.08f, midX, tipY);

            // Sağ Parlak Magma Yüzey
            shapeRenderer.setColor(SPIKE_BODY);
            shapeRenderer.triangle(midX, baseY + 0.08f, sx + spikeW, baseY + 0.08f, midX, tipY);

            // Orta Kristal Kesit Işıltısı
            shapeRenderer.setColor(1.0f, 0.45f, 0.35f, 0.85f);
            shapeRenderer.rectLine(midX, baseY + 0.08f, midX, tipY, 0.025f);

            // Keskin Kor Uç Parıltısı
            shapeRenderer.setColor(SPIKE_TIP);
            shapeRenderer.circle(midX, tipY - 0.015f, 0.04f, 8);
            shapeRenderer.setColor(Color.WHITE);
            shapeRenderer.circle(midX, tipY - 0.015f, 0.018f, 6);
        }
    }

    public float getX() { return x; }
    public float getY() { return y; }
    public float getWidth() { return width; }
    public float getHeight() { return height; }
}
