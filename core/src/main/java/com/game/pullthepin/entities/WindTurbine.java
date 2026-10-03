package com.game.pullthepin.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;

/**
 * 2.5D Endüstriyel Rüzgar Türbini & Aerodinamik Hava Kanalı (Wind Turbine / Jet Stream).
 * - Dönen 3 kanatlı pervane ve ağır metal gövde.
 * - Akışkan aerodinamik hava dalgaları (Cyan/Beyaz rüzgar çizgileri ve girdap parçacıkları).
 * - İçine giren bilyeleri yerçekimine meydan okuyarak yukarı veya yatay olarak fırlatır!
 */
public class WindTurbine {

    public enum Direction {
        UP,
        DOWN,
        LEFT,
        RIGHT
    }

    private Body turbineBody;
    private Body sensorBody;
    private float x, y;
    private float width = 1.0f;
    private float height = 0.65f;
    private Direction direction;
    private float streamLength;
    private float streamWidth;
    private float windForce;
    private boolean active = true;

    private float bladeAngle = 0f;
    private float streamTimer = 0f;
    private Array<Ball> ballsInStream = new Array<Ball>();

    // Renk Paleti
    private final Color HOUSING_BASE = new Color(0.14f, 0.16f, 0.22f, 1.0f);
    private final Color HOUSING_DARK = new Color(0.08f, 0.09f, 0.13f, 1.0f);
    private final Color BLADE_COLOR  = new Color(0.75f, 0.82f, 0.90f, 1.0f);
    private final Color BLADE_DARK   = new Color(0.35f, 0.40f, 0.50f, 1.0f);
    private final Color STREAM_GLOW  = new Color(0.20f, 0.85f, 1.00f, 0.10f);

    public WindTurbine(World world, float x, float y, Direction direction, float streamLength, float windForce) {
        this.x = x;
        this.y = y;
        this.direction = direction;
        this.streamLength = Math.max(1.5f, streamLength);
        this.streamWidth = 1.15f;
        this.windForce = windForce > 0 ? windForce : 38.0f;

        initPhysics(world);
    }

    private void initPhysics(World world) {
        // 1. Türbin Taban Katı Gövdesi
        BodyDef bDef = new BodyDef();
        bDef.type = BodyDef.BodyType.StaticBody;
        bDef.position.set(x, y);

        turbineBody = world.createBody(bDef);
        turbineBody.setUserData(this);

        PolygonShape baseShape = new PolygonShape();
        baseShape.setAsBox(width / 2f, height / 2f);
        turbineBody.createFixture(baseShape, 0);
        baseShape.dispose();

        // 2. Rüzgar Akımı Sensör Alanı
        BodyDef sDef = new BodyDef();
        sDef.type = BodyDef.BodyType.StaticBody;

        Vector2 sensorCenter = getStreamCenter();
        sDef.position.set(sensorCenter);

        sensorBody = world.createBody(sDef);
        sensorBody.setUserData(this);

        PolygonShape streamShape = new PolygonShape();
        if (direction == Direction.UP || direction == Direction.DOWN) {
            streamShape.setAsBox(streamWidth / 2f, streamLength / 2f);
        } else {
            streamShape.setAsBox(streamLength / 2f, streamWidth / 2f);
        }

        FixtureDef fDef = new FixtureDef();
        fDef.shape = streamShape;
        fDef.isSensor = true;
        sensorBody.createFixture(fDef);
        streamShape.dispose();
    }

    public Vector2 getStreamCenter() {
        switch (direction) {
            case UP:    return new Vector2(x, y + height / 2f + streamLength / 2f);
            case DOWN:  return new Vector2(x, y - height / 2f - streamLength / 2f);
            case RIGHT: return new Vector2(x + width / 2f + streamLength / 2f, y);
            case LEFT:
            default:    return new Vector2(x - width / 2f - streamLength / 2f, y);
        }
    }

    public void addBall(Ball ball) {
        if (!ballsInStream.contains(ball, true)) {
            ballsInStream.add(ball);
        }
    }

    public void removeBall(Ball ball) {
        ballsInStream.removeValue(ball, true);
    }

    public void update(float delta) {
        if (!active) return;
        bladeAngle += delta * 24.0f;
        streamTimer += delta;

        // Hava kanalındaki tüm bilyelere aerodinamik itiş uygula
        for (int i = ballsInStream.size - 1; i >= 0; i--) {
            Ball b = ballsInStream.get(i);
            if (b.isDead() || b.getBody() == null) {
                ballsInStream.removeIndex(i);
                continue;
            }

            Vector2 bpos = b.getPosition();
            switch (direction) {
                case UP:
                    float centeringX = (x - bpos.x) * 6.5f;
                    b.getBody().applyForceToCenter(centeringX, windForce, true);
                    break;
                case DOWN:
                    float centeringXD = (x - bpos.x) * 6.5f;
                    b.getBody().applyForceToCenter(centeringXD, -windForce, true);
                    break;
                case RIGHT:
                    float centeringYR = (y - bpos.y) * 6.5f;
                    b.getBody().applyForceToCenter(windForce, centeringYR, true);
                    break;
                case LEFT:
                    float centeringYL = (y - bpos.y) * 6.5f;
                    b.getBody().applyForceToCenter(-windForce, centeringYL, true);
                    break;
            }
        }
    }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
    public float getX() { return x; }
    public float getY() { return y; }
    public Direction getDirection() { return direction; }

    /**
     * 2.5D Pervane ve Hava Akımı Çizimi
     */
    public void render(ShapeRenderer sr) {
        Vector2 sc = getStreamCenter();
        float sLeft, sBottom, sW, sH;

        if (direction == Direction.UP || direction == Direction.DOWN) {
            sW = streamWidth; sH = streamLength;
            sLeft = sc.x - sW / 2f;
            sBottom = sc.y - sH / 2f;
        } else {
            sW = streamLength; sH = streamWidth;
            sLeft = sc.x - sW / 2f;
            sBottom = sc.y - sH / 2f;
        }

        // 1. Aerodinamik Hava Akımı Tüneli (Translucent Air Stream)
        if (active) {
            float streamPulse = 0.5f + 0.5f * (float) Math.sin(streamTimer * 4.5f);
            sr.setColor(0.18f, 0.85f, 1.00f, 0.08f + streamPulse * 0.04f);
            sr.rect(sLeft, sBottom, sW, sH);

            // Hareketli Rüzgar Dalga Çizgileri (Airflow Streaks)
            int numLines = 5;
            for (int i = 0; i < numLines; i++) {
                float lineOffset = ((streamTimer * 2.2f + (float) i / numLines) % 1.0f);
                float lineAlpha = (float) Math.sin(lineOffset * Math.PI) * 0.65f;
                sr.setColor(0.65f, 0.95f, 1.00f, lineAlpha);

                if (direction == Direction.UP) {
                    float lx = sLeft + sW * (0.2f + i * 0.15f);
                    float ly = sBottom + lineOffset * sH;
                    sr.rectLine(lx, ly, lx, ly + 0.35f, 0.025f);
                } else if (direction == Direction.RIGHT) {
                    float ly = sBottom + sH * (0.2f + i * 0.15f);
                    float lx = sLeft + lineOffset * sW;
                    sr.rectLine(lx, ly, lx + 0.35f, ly, 0.025f);
                } else if (direction == Direction.LEFT) {
                    float ly = sBottom + sH * (0.2f + i * 0.15f);
                    float lx = sLeft + sW - lineOffset * sW;
                    sr.rectLine(lx, ly, lx - 0.35f, ly, 0.025f);
                } else {
                    float lx = sLeft + sW * (0.2f + i * 0.15f);
                    float ly = sBottom + sH - lineOffset * sH;
                    sr.rectLine(lx, ly, lx, ly - 0.35f, 0.025f);
                }
            }
        }

        // 2. Türbin Gövdesi Gölgesi
        sr.setColor(0f, 0f, 0f, 0.35f);
        sr.rect(x - width / 2f + 0.06f, y - height / 2f - 0.06f, width, height);

        // 3. Ağır Metalik Gövde
        sr.setColor(HOUSING_BASE);
        sr.rect(x - width / 2f, y - height / 2f, width, height);
        sr.setColor(HOUSING_DARK);
        sr.rect(x - width / 2f, y - height / 2f, width, height * 0.25f);

        // 4. Hava Çıkış Izgarası
        sr.setColor(0.18f, 0.22f, 0.30f, 1.0f);
        float grillR = Math.min(width, height) * 0.45f;
        sr.circle(x, y, grillR, 24);

        // 5. Dönen 3 Kanatlı Pervane
        float bladeLen = grillR * 0.85f;
        for (int b = 0; b < 3; b++) {
            float ang = bladeAngle + b * (2.094f); // 120 derece
            float bx1 = x + MathUtils.cos(ang) * bladeLen;
            float by1 = y + MathUtils.sin(ang) * bladeLen;
            float bx2 = x + MathUtils.cos(ang + 0.35f) * (bladeLen * 0.65f);
            float by2 = y + MathUtils.sin(ang + 0.35f) * (bladeLen * 0.65f);

            sr.setColor(BLADE_DARK);
            sr.triangle(x, y, bx1, by1, bx2, by2);
            sr.setColor(BLADE_COLOR);
            sr.rectLine(x, y, bx1, by1, 0.035f);
        }

        // 6. Merkez Mil Başlığı & Mavi LED Işık
        sr.setColor(0.12f, 0.14f, 0.18f, 1f);
        sr.circle(x, y, grillR * 0.32f, 16);
        sr.setColor(active ? new Color(0.20f, 0.95f, 1.0f, 1.0f) : new Color(0.5f, 0.2f, 0.2f, 0.8f));
        sr.circle(x, y, grillR * 0.16f, 12);
    }
}
