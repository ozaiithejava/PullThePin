package com.game.pullthepin.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.ObjectSet;

/**
 * 2.5D Kuantum Çoğaltıcı Kapı (Multiplier / Math Gate).
 * Toplar lazer enerji bariyerinden geçtiğinde anında sayıyı çoğaltır (x2, x3, +5 vb.)
 * ve şelale gibi fışkıran bilye kaskadı oluşturur.
 */
public class MultiplierGate {

    public enum GateType {
        MULTIPLY_X2("x2", 1, new Color(0.20f, 0.85f, 1.0f, 1.0f)),   // 1 yeni klon (+1 = 2x)
        MULTIPLY_X3("x3", 2, new Color(0.95f, 0.40f, 0.95f, 1.0f)),   // 2 yeni klon (+2 = 3x)
        ADD_5("+5", 5, new Color(0.25f, 0.95f, 0.55f, 1.0f)),         // 5 yeni top
        ADD_10("+10", 10, new Color(1.0f, 0.82f, 0.22f, 1.0f));       // 10 yeni top

        public final String label;
        public final int cloneCount;
        public final Color accentColor;

        GateType(String label, int cloneCount, Color accentColor) {
            this.label = label;
            this.cloneCount = cloneCount;
            this.accentColor = accentColor;
        }
    }

    private World world;
    private Body body;
    private float x, y, width, height;
    private GateType type;
    private ObjectSet<Ball> processedBalls = new ObjectSet<>();
    private float pulseTimer = 0f;
    private GlyphLayout glyphLayout = new GlyphLayout();

    public MultiplierGate(World world, float x, float y, float width, float height, GateType type) {
        this.world = world;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.type = type;

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
        fdef.isSensor = true; // Fiziksel çarpmayı engelle, sadece sensör algıla

        body.createFixture(fdef);
        shape.dispose();
    }

    public void update(float delta) {
        pulseTimer += delta * 4.5f;
    }

    public boolean canProcess(Ball ball) {
        if (ball == null || ball.isDead() || ball.isCollected()) return false;
        return !processedBalls.contains(ball);
    }

    public void markProcessed(Ball ball) {
        if (ball != null) {
            processedBalls.add(ball);
        }
    }

    public int getCloneCount() {
        return type.cloneCount;
    }

    public GateType getType() {
        return type;
    }

    public Vector2 getPosition() {
        return new Vector2(x, y);
    }

    public float getWidth() { return width; }
    public float getHeight() { return height; }

    /**
     * 2.5D Kuantum Lazer Bariyeri & Holografik Çoğaltıcı Rozeti Çizimi
     */
    public void render(ShapeRenderer sr) {
        Color col = type.accentColor;
        float pulse = 0.5f + 0.5f * (float) Math.sin(pulseTimer);

        // 1. Dış Enerji Alanı (Holographic Laser Field)
        sr.setColor(col.r, col.g, col.b, 0.18f + pulse * 0.12f);
        sr.rect(x - width / 2f, y - height / 2f, width, height);

        // 2. Enerji Tarama Çizgileri (Scanning Beams)
        float scanY = y - height / 2f + (float) ((pulseTimer * 0.35f) % 1.0f) * height;
        sr.setColor(1.0f, 1.0f, 1.0f, 0.45f);
        sr.rect(x - width / 2f, scanY, width, 0.06f);

        // 3. Yan İletken Direkler (Laser Emitter Pillars)
        float pillarW = 0.14f;
        sr.setColor(0.08f, 0.12f, 0.20f, 1.0f);
        sr.rect(x - width / 2f - pillarW / 2f, y - height / 2f, pillarW, height);
        sr.rect(x + width / 2f - pillarW / 2f, y - height / 2f, pillarW, height);

        // Parlayan Neon Direk Uçları
        sr.setColor(col);
        sr.circle(x - width / 2f, y - height / 2f, 0.15f, 12);
        sr.circle(x - width / 2f, y + height / 2f, 0.15f, 12);
        sr.circle(x + width / 2f, y - height / 2f, 0.15f, 12);
        sr.circle(x + width / 2f, y + height / 2f, 0.15f, 12);

        // Beyaz Çekirdek
        sr.setColor(Color.WHITE);
        sr.circle(x - width / 2f, y - height / 2f, 0.07f, 8);
        sr.circle(x - width / 2f, y + height / 2f, 0.07f, 8);
        sr.circle(x + width / 2f, y - height / 2f, 0.07f, 8);
        sr.circle(x + width / 2f, y + height / 2f, 0.07f, 8);

        // 4. Orta Holografik Kapsül Rozeti (Floating Badge)
        float badgeW = 0.90f;
        float badgeH = 0.48f;
        sr.setColor(0.05f, 0.08f, 0.16f, 0.92f);
        sr.rect(x - badgeW / 2f, y - badgeH / 2f, badgeW, badgeH);

        sr.setColor(col);
        sr.rect(x - badgeW / 2f, y - badgeH / 2f, badgeW, 0.04f);
        sr.rect(x - badgeW / 2f, y + badgeH / 2f - 0.04f, badgeW, 0.04f);
    }

    public void renderText(SpriteBatch batch, BitmapFont font) {
        glyphLayout.setText(font, type.label);
        font.setColor(0f, 0f, 0f, 0.85f);
        font.draw(batch, type.label, x - glyphLayout.width / 2f + 0.02f, y + glyphLayout.height / 2f - 0.02f);
        font.setColor(type.accentColor);
        font.draw(batch, type.label, x - glyphLayout.width / 2f, y + glyphLayout.height / 2f);
    }
}
