package com.game.pullthepin.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

/**
 * 2.5D Kuantum Işınlama Portalı (Vortex Ring & Swirling Particle Gateway).
 * Dönen ışık halkaları, derinlikli olay ufku boşluğu ve renkli nabız aurası.
 */
public class Portal {

    private Vector2 inPos;
    private Vector2 outPos;
    private Body inSensorBody;
    private float radius = 0.52f;
    private float rotTimer = 0f;

    private Color inColor = new Color(0.15f, 0.70f, 1.0f, 1.0f);  // Cyan / Safir Giriş
    private Color outColor = new Color(1.0f, 0.55f, 0.15f, 1.0f); // Amber / Altın Çıkış

    public Portal(World world, float inX, float inY, float outX, float outY) {
        this.inPos = new Vector2(inX, inY);
        this.outPos = new Vector2(outX, outY);

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(inPos);

        inSensorBody = world.createBody(bodyDef);
        inSensorBody.setUserData(this);

        CircleShape shape = new CircleShape();
        shape.setRadius(radius);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.isSensor = true;

        inSensorBody.createFixture(fixtureDef);
        shape.dispose();
    }

    public static class TeleportRequest {
        public Ball ball;
        public Vector2 targetPos;
        public Vector2 velocity;

        public TeleportRequest(Ball ball, Vector2 targetPos, Vector2 velocity) {
            this.ball = ball;
            this.targetPos = targetPos;
            this.velocity = velocity;
        }

        public void execute() {
            if (ball != null && !ball.isDead() && ball.getBody() != null) {
                ball.getBody().setTransform(targetPos.x, targetPos.y - 0.25f, 0);
                ball.getBody().setLinearVelocity(velocity.x * 0.85f, Math.min(velocity.y, -3.0f));
            }
        }
    }

    public void update(float delta) {
        rotTimer += delta * 4.5f;
    }

    public TeleportRequest createTeleportRequest(Ball ball) {
        if (ball == null || ball.isDead() || ball.getBody() == null || !ball.canTeleport()) return null;
        ball.setTeleportCooldown(0.35f);
        return new TeleportRequest(ball, outPos.cpy(), ball.getBody().getLinearVelocity().cpy());
    }

    public void render(ShapeRenderer shapeRenderer) {
        renderVortexNode(shapeRenderer, inPos, inColor, rotTimer);
        renderVortexNode(shapeRenderer, outPos, outColor, -rotTimer);
    }

    private void renderVortexNode(ShapeRenderer sr, Vector2 pos, Color baseCol, float rot) {
        float pulse = 1.0f + 0.12f * (float) Math.sin(rot * 2.5f);

        // 1. Dış Halka Parıltı Aurası (Nebula Glow Halo)
        sr.setColor(baseCol.r, baseCol.g, baseCol.b, 0.16f);
        sr.circle(pos.x, pos.y, radius * 1.65f * pulse, 24);

        sr.setColor(baseCol.r, baseCol.g, baseCol.b, 0.38f);
        sr.circle(pos.x, pos.y, radius * 1.30f * pulse, 24);

        // 2. Ana Dönen Kuantum Halka Segmenti
        sr.setColor(baseCol);
        sr.circle(pos.x, pos.y, radius * 0.98f, 22);

        // Karşı Yönde Dönen İç Enerji Halkası
        sr.setColor(baseCol.r * 1.2f, baseCol.g * 1.2f, baseCol.b * 1.2f, 0.70f);
        int ringSegments = 8;
        for (int i = 0; i < ringSegments; i++) {
            float a1 = -rot * 1.4f + i * (6.28f / ringSegments);
            float a2 = a1 + 0.45f;
            float rx1 = pos.x + (float) Math.cos(a1) * (radius * 0.82f);
            float ry1 = pos.y + (float) Math.sin(a1) * (radius * 0.82f);
            float rx2 = pos.x + (float) Math.cos(a2) * (radius * 0.82f);
            float ry2 = pos.y + (float) Math.sin(a2) * (radius * 0.82f);
            sr.rectLine(rx1, ry1, rx2, ry2, 0.05f);
        }

        // 3. İç Olay Ufku Boşluğu (Black Hole Singularity)
        sr.setColor(0.04f, 0.05f, 0.09f, 0.98f);
        sr.circle(pos.x, pos.y, radius * 0.62f, 20);

        // 4. Dönen Yörünge Parçacıkları (6 Orbiting Quantum Sparks)
        int orbCount = 6;
        for (int i = 0; i < orbCount; i++) {
            float angle = rot + i * (6.28f / orbCount);
            float orbR = radius * (0.75f + 0.10f * (float) Math.sin(rot * 3f + i));
            float ox = pos.x + (float) Math.cos(angle) * orbR;
            float oy = pos.y + (float) Math.sin(angle) * orbR;

            sr.setColor(1.0f, 1.0f, 1.0f, 0.95f);
            sr.circle(ox, oy, 0.055f, 8);
            sr.setColor(baseCol.r, baseCol.g, baseCol.b, 0.50f);
            sr.circle(ox, oy, 0.09f, 8);
        }

        // 5. Merkez Olay Ufku Işıma Noktası
        sr.setColor(1.0f, 1.0f, 1.0f, 0.85f * pulse);
        sr.circle(pos.x, pos.y, radius * 0.22f, 12);
    }

    public Vector2 getInPos() { return inPos; }
    public Vector2 getOutPos() { return outPos; }
    public float getRadius() { return radius; }
}
