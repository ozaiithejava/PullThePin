package com.game.pullthepin.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

/**
 * 2.5D Parlak Altın Fiziksel Anahtar (Physics Key Entity).
 * Yerçekiminden etkilenir, bilyeler ve oluklar arasında yuvarlanır.
 * Kilitli pime dokunduğunda kilidi çözer ve altın kıvılcımlarla yok olur.
 */
public class KeyEntity {

    private World world;
    private Body body;
    private int keyId;
    private float radius = 0.28f;
    private boolean isDead = false;
    private float auraPulse = 0f;

    // Altın Renk Paleti
    private final Color GOLD_DARK = new Color(0.55f, 0.38f, 0.08f, 1f);
    private final Color GOLD_BASE = new Color(1.00f, 0.84f, 0.22f, 1f);
    private final Color GOLD_LIGHT = new Color(1.00f, 0.95f, 0.65f, 1f);
    private final Color GOLD_GLOW = new Color(1.00f, 0.85f, 0.20f, 0.35f);

    public KeyEntity(World world, float x, float y, int keyId) {
        this.world = world;
        this.keyId = keyId;

        createPhysicsBody(x, y);
    }

    private void createPhysicsBody(float x, float y) {
        BodyDef bdef = new BodyDef();
        bdef.type = BodyDef.BodyType.DynamicBody;
        bdef.position.set(x, y);

        body = world.createBody(bdef);
        body.setUserData(this);

        CircleShape shape = new CircleShape();
        shape.setRadius(radius);

        FixtureDef fdef = new FixtureDef();
        fdef.shape = shape;
        fdef.density = 1.6f;
        fdef.friction = 0.20f;
        fdef.restitution = 0.25f;

        body.createFixture(fdef);
        shape.dispose();
    }

    public void update(float delta) {
        auraPulse += delta * 6.0f;
    }

    public Vector2 getPosition() {
        return body != null ? body.getPosition() : Vector2.Zero;
    }

    public int getKeyId() { return keyId; }
    public boolean isDead() { return isDead; }
    public void setDead(boolean dead) { this.isDead = dead; }
    public Body getBody() { return body; }
    public float getRadius() { return radius; }

    /**
     * 2.5D İzometrik Altın Anahtar Render Pipeline
     */
    public void render(ShapeRenderer sr) {
        if (isDead || body == null) return;

        Vector2 pos = body.getPosition();
        float angle = body.getAngle();
        float pulse = 0.5f + 0.5f * (float) Math.sin(auraPulse);

        // 1. Dış Altın Işıma Aurası (Golden Halo Glow)
        sr.setColor(GOLD_GLOW.r, GOLD_GLOW.g, GOLD_GLOW.b, 0.20f + pulse * 0.18f);
        sr.circle(pos.x, pos.y, radius * 1.8f, 20);

        // 2. Derinlik Zemin Gölgesi
        sr.setColor(0f, 0f, 0f, 0.32f);
        sr.circle(pos.x + 0.04f, pos.y - 0.05f, radius * 0.95f, 16);

        // 3. Anahtar Yuvarlak Başlığı (Ring Handle)
        sr.setColor(GOLD_DARK);
        sr.circle(pos.x, pos.y, radius, 18);
        sr.setColor(GOLD_BASE);
        sr.circle(pos.x - 0.02f, pos.y + 0.02f, radius * 0.88f, 18);

        // İç Delik (Hollow Center)
        sr.setColor(0.08f, 0.12f, 0.20f, 1.0f);
        sr.circle(pos.x, pos.y, radius * 0.42f, 14);

        // 4. Anahtar Diş / Gövde Uzantısı (Key Teeth & Shaft)
        float cos = (float) Math.cos(angle);
        float sin = (float) Math.sin(angle);
        float tipX = pos.x + cos * radius * 1.35f;
        float tipY = pos.y + sin * radius * 1.35f;

        sr.setColor(GOLD_DARK);
        sr.rectLine(pos.x, pos.y, tipX, tipY, 0.10f);
        sr.setColor(GOLD_BASE);
        sr.rectLine(pos.x - 0.02f, pos.y + 0.02f, tipX - 0.02f, tipY + 0.02f, 0.07f);

        // Parlak Mücevher Çekirdeği
        sr.setColor(GOLD_LIGHT);
        sr.circle(pos.x - radius * 0.30f, pos.y + radius * 0.30f, radius * 0.20f, 10);
        sr.setColor(Color.WHITE);
        sr.circle(pos.x - radius * 0.30f, pos.y + radius * 0.30f, radius * 0.09f, 6);
    }
}
