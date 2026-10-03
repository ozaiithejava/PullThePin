package com.game.pullthepin.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

/**
 * 2.5D Prizmatik Renk Filtresi / Lazer Boya Geçidi (Color Filter Gate).
 * İçinden geçen renksiz (veya farklı renkli) bilyeleri anında kuantum lazerle boyar,
 * canlı parçacık halkaları saçar.
 */
public class ColorGate {

    private World world;
    private Body body;
    private float x, y, width, height;
    private Color gateColor;
    private String colorName;
    private float waveTimer = 0f;

    public ColorGate(World world, float x, float y, float width, float height, Color gateColor, String colorName) {
        this.world = world;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.gateColor = gateColor;
        this.colorName = colorName;

        createPhysicsBody();
    }

    private void createPhysicsBody() {
        BodyDef bdef = new BodyDef();
        bdef.type = BodyDef.BodyType.StaticBody;
        bdef.position.set(x, y);

        body = world.createBody(bdef);
        body.setUserData(this);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width / 2f, height / 2f);

        FixtureDef fdef = new FixtureDef();
        fdef.shape = shape;
        fdef.isSensor = true;

        body.createFixture(fdef);
        shape.dispose();
    }

    public void update(float delta) {
        waveTimer += delta * 5.0f;
    }

    public boolean canColorize(Ball ball) {
        if (ball == null || ball.isDead() || ball.isCollected()) return false;
        // Gri topları veya farklı renkteki topları boyayabilir
        return ball.getType() == Ball.BallType.GRAY || !ball.getColor().equals(gateColor);
    }

    public Color getGateColor() {
        return gateColor;
    }

    public Vector2 getPosition() {
        return new Vector2(x, y);
    }

    public float getWidth() { return width; }
    public float getHeight() { return height; }
    public String getColorName() { return colorName; }

    /**
     * 2.5D Prizmatik Lazer Renk Alanı Çizimi
     */
    public void render(ShapeRenderer sr) {
        float pulse = 0.5f + 0.5f * (float) Math.sin(waveTimer);

        // 1. Canlı Lazer Işıma Alanı (Prismatic Glow Field)
        sr.setColor(gateColor.r, gateColor.g, gateColor.b, 0.22f + pulse * 0.15f);
        sr.rect(x - width / 2f, y - height / 2f, width, height);

        // 2. Çoklu Lazer Işın Demetleri (Color Laser Rays)
        for (int i = 0; i < 4; i++) {
            float rayOffset = (float) Math.sin(waveTimer + i * 1.57f) * (height * 0.35f);
            sr.setColor(gateColor.r, gateColor.g, gateColor.b, 0.45f);
            sr.rect(x - width / 2f, y + rayOffset - 0.02f, width, 0.04f);
        }

        // 3. Prizma Uç Kristal Direkleri
        float crystalRadius = 0.18f;
        sr.setColor(0.08f, 0.12f, 0.20f, 1.0f);
        sr.circle(x - width / 2f, y, crystalRadius * 1.2f, 16);
        sr.circle(x + width / 2f, y, crystalRadius * 1.2f, 16);

        sr.setColor(gateColor);
        sr.circle(x - width / 2f, y, crystalRadius, 16);
        sr.circle(x + width / 2f, y, crystalRadius, 16);

        sr.setColor(Color.WHITE);
        sr.circle(x - width / 2f, y, crystalRadius * 0.45f, 10);
        sr.circle(x + width / 2f, y, crystalRadius * 0.45f, 10);
    }
}
