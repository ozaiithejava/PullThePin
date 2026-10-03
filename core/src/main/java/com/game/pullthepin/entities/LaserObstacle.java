package com.game.pullthepin.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

/**
 * 2.5D Kuantum Enerji & Lazer Bariyeri (Pulsing / Continuous Laser Beam Obstacle).
 * Periyodik olarak yanıp sönen (veya sürekli aktif) ölümcül lazer ışını.
 * Aktifken temas eden topları anında buharlaştırır ve neon plazma patlaması yaratır.
 */
public class LaserObstacle {

    public enum LaserMode {
        PULSING,        // Belirli aralıklarla açılıp kapanan (Zamanlamalı)
        CONSTANT,       // Sürekli aktif tehlike bariyeri
        COLOR_SELECTIVE // Sadece gri topları yakan veya belirli renge duyarlı
    }

    private Vector2 startPos;
    private Vector2 endPos;
    private LaserMode mode;
    private float onDuration;
    private float offDuration;
    private float timer;
    private boolean active = true;
    private boolean warning = false;
    private float beamThickness = 0.22f;
    private Color laserColor;
    private Color warningColor = new Color(1.0f, 0.45f, 0.1f, 1.0f); // Amber / Turuncu uyarı

    private Body sensorBody;
    private float animPhase = 0f;

    public LaserObstacle(World world, float x1, float y1, float x2, float y2, LaserMode mode,
                         float onDuration, float offDuration, float startOffset, Color laserColor) {
        this.startPos = new Vector2(x1, y1);
        this.endPos = new Vector2(x2, y2);
        this.mode = mode;
        this.onDuration = Math.max(0.5f, onDuration);
        this.offDuration = Math.max(0.5f, offDuration);
        this.timer = startOffset;
        this.laserColor = (laserColor != null) ? laserColor : new Color(1.0f, 0.20f, 0.35f, 1.0f); // Crimson Lazer

        if (mode == LaserMode.CONSTANT) {
            this.active = true;
            this.warning = false;
        } else {
            updateCycle(0f);
        }

        initPhysics(world);
    }

    private void initPhysics(World world) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set((startPos.x + endPos.x) / 2f, (startPos.y + endPos.y) / 2f);

        sensorBody = world.createBody(bodyDef);
        sensorBody.setUserData(this);

        Vector2 diff = endPos.cpy().sub(startPos);
        float length = diff.len();
        float angle = diff.angleRad();

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(length * 0.5f, beamThickness * 0.4f, new Vector2(0, 0), angle);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.isSensor = true;

        sensorBody.createFixture(fixtureDef);
        shape.dispose();
    }

    public void update(float delta) {
        animPhase += delta * 12.0f;

        if (mode == LaserMode.PULSING) {
            timer += delta;
            updateCycle(delta);
        }
    }

    private void updateCycle(float delta) {
        float cycleDuration = onDuration + offDuration;
        float curCycleTime = timer % cycleDuration;

        if (curCycleTime < onDuration) {
            active = true;
            warning = false;
        } else {
            active = false;
            // Kapanma süresinin son 0.6 saniyesinde oyuncuyu uyaran "Warning" aşaması
            float remainingOffTime = cycleDuration - curCycleTime;
            warning = (remainingOffTime <= 0.65f);
        }
    }

    public boolean isActive() {
        return active;
    }

    public boolean isWarning() {
        return warning;
    }

    public boolean checkBallIntersection(Ball ball) {
        if (!active || ball == null || ball.isDead()) return false;
        Vector2 bpos = ball.getPosition();
        float ballRadius = Ball.RADIUS;

        // Noktanın çizgi segmentine olan en yakın mesafesi
        float dist = Intersector.distanceSegmentPoint(startPos, endPos, bpos);
        return dist < (ballRadius + beamThickness * 0.45f);
    }

    public void render(ShapeRenderer sr) {
        // 1. İKİ UÇTAKİ PROJEKTÖR MONTE PODLARI (Laser Emitter Nodes)
        renderEmitterNode(sr, startPos);
        renderEmitterNode(sr, endPos);

        if (active) {
            renderActiveBeam(sr);
        } else if (warning) {
            renderWarningBeam(sr);
        }
    }

    private void renderActiveBeam(ShapeRenderer sr) {
        float pulse = 0.85f + 0.15f * (float) Math.sin(animPhase * 1.5f);

        // 1. Geniş Dış Neon Enerji Aurası (Outer Glow Halo)
        sr.setColor(laserColor.r, laserColor.g, laserColor.b, 0.18f * pulse);
        sr.rectLine(startPos.x, startPos.y, endPos.x, endPos.y, beamThickness * 2.4f);

        sr.setColor(laserColor.r, laserColor.g, laserColor.b, 0.45f * pulse);
        sr.rectLine(startPos.x, startPos.y, endPos.x, endPos.y, beamThickness * 1.4f);

        // 2. Ana Plazma Işın Gövdesi (Core Laser Beam)
        sr.setColor(laserColor.r * 1.2f, laserColor.g * 1.2f, laserColor.b * 1.2f, 0.95f);
        sr.rectLine(startPos.x, startPos.y, endPos.x, endPos.y, beamThickness * 0.75f);

        // 3. Merkez Akkor Beyaz Lazer Çekirdeği (Pure White Core Filament)
        sr.setColor(1.0f, 1.0f, 1.0f, 0.98f);
        sr.rectLine(startPos.x, startPos.y, endPos.x, endPos.y, beamThickness * 0.28f);

        // 4. Dinamik Elektrik Plazma Arkları (Voltaj Dalgaları)
        int segments = 6;
        Vector2 step = endPos.cpy().sub(startPos).scl(1.0f / segments);
        Vector2 normal = new Vector2(-step.y, step.x).nor();

        for (int i = 0; i < segments; i++) {
            float t1 = (float) i / segments;
            float t2 = (float) (i + 1) / segments;
            float offset1 = (float) Math.sin(animPhase * 2.2f + i * 1.8f) * 0.08f;
            float offset2 = (float) Math.sin(animPhase * 2.2f + (i + 1) * 1.8f) * 0.08f;

            Vector2 p1 = startPos.cpy().add(step.cpy().scl(i)).add(normal.cpy().scl(offset1));
            Vector2 p2 = startPos.cpy().add(step.cpy().scl(i + 1)).add(normal.cpy().scl(offset2));

            sr.setColor(1.0f, 1.0f, 1.0f, 0.85f);
            sr.rectLine(p1.x, p1.y, p2.x, p2.y, 0.035f);
        }
    }

    private void renderWarningBeam(ShapeRenderer sr) {
        // Hızlı yanıp sönen şarj uyarı çizgisi (Strobe Flash)
        float flash = (float) Math.abs(Math.sin(animPhase * 3.5f));
        sr.setColor(warningColor.r, warningColor.g, warningColor.b, 0.35f * flash);
        sr.rectLine(startPos.x, startPos.y, endPos.x, endPos.y, beamThickness * 0.8f);

        sr.setColor(1.0f, 0.90f, 0.50f, 0.75f * flash);
        sr.rectLine(startPos.x, startPos.y, endPos.x, endPos.y, 0.04f);
    }

    private void renderEmitterNode(ShapeRenderer sr, Vector2 pos) {
        float nodeR = 0.28f;

        // Dış Montaj Tabanı
        sr.setColor(0.05f, 0.08f, 0.14f, 0.98f);
        sr.circle(pos.x, pos.y, nodeR, 16);

        // Metalik Kenarlık
        sr.setColor(0.35f, 0.42f, 0.55f, 1.0f);
        sr.circle(pos.x, pos.y, nodeR * 0.82f, 16);

        // Durum LED'i
        if (active) {
            sr.setColor(laserColor);
            sr.circle(pos.x, pos.y, nodeR * 0.55f, 14);
            sr.setColor(1.0f, 1.0f, 1.0f, 0.90f);
            sr.circle(pos.x, pos.y, nodeR * 0.25f, 10);
        } else if (warning) {
            float blink = (float) Math.abs(Math.sin(animPhase * 4.0f));
            sr.setColor(warningColor.r, warningColor.g, warningColor.b, blink);
            sr.circle(pos.x, pos.y, nodeR * 0.55f, 14);
        } else {
            // Güvenli / Kapalı (Yeşil / Cyan LED)
            sr.setColor(0.15f, 0.85f, 0.45f, 0.80f);
            sr.circle(pos.x, pos.y, nodeR * 0.45f, 14);
        }
    }

    public Vector2 getStartPos() { return startPos; }
    public Vector2 getEndPos() { return endPos; }
    public Color getLaserColor() { return laserColor; }
    public LaserMode getMode() { return mode; }
}
