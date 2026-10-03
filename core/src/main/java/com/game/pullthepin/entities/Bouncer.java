package com.game.pullthepin.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

/**
 * 2.5D Neon Yaylı Trambolin Yastığı (Bouncy Silicone Jelly Cushion).
 * Top çarptığında dinamik esneme animasyonu, LED neon ışık şeritleri ve yönlendirme okları.
 */
public class Bouncer {

    private Body body;
    private float x, y, width, height, angle;
    private float bounceForce;
    private float bounceAnim = 0f;

    // Şık Neon Pembe / Cyan Renkleri
    private final Color BASE_DARK = new Color(0.20f, 0.10f, 0.25f, 1.0f);
    private final Color CUSHION_COLOR = new Color(0.98f, 0.22f, 0.65f, 1.0f);
    private final Color NEON_GLOW = new Color(1.00f, 0.55f, 0.85f, 0.95f);
    private final Color ARROW_COLOR = new Color(1.00f, 0.95f, 0.60f, 0.90f);

    public Bouncer(World world, float x, float y, float width, float height, float angleDeg, float bounceForce) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.angle = angleDeg;
        this.bounceForce = bounceForce;

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(x, y);
        bodyDef.angle = (float) Math.toRadians(angleDeg);

        body = world.createBody(bodyDef);
        body.setUserData(this);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width / 2f, height / 2f);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.restitution = 1.35f;
        fixtureDef.friction = 0.05f;

        body.createFixture(fixtureDef);
        shape.dispose();
    }

    public void applyImpulse(Ball ball) {
        if (ball != null && !ball.isDead() && ball.getBody() != null) {
            float rad = (float) Math.toRadians(angle + 90);
            Vector2 dir = new Vector2((float) Math.cos(rad), (float) Math.sin(rad)).scl(bounceForce);
            ball.getBody().applyLinearImpulse(dir, ball.getBody().getWorldCenter(), true);
            bounceAnim = 1.0f; // Esneme animasyonunu tetikle
        }
    }

    public void render(ShapeRenderer shapeRenderer) {
        if (bounceAnim > 0) {
            bounceAnim -= 0.08f;
            if (bounceAnim < 0) bounceAnim = 0;
        }

        float scaleY = 1.0f + bounceAnim * 0.55f;

        // 1. Zemin Derinlik Gölgesi
        shapeRenderer.setColor(0f, 0f, 0f, 0.32f);
        shapeRenderer.rect(x - width / 2f + 0.06f, y - height / 2f - 0.08f, width / 2f, height / 2f, width, height, 1f, 1f, angle);

        // 2. Koyu Taban Metal Montaj Şeridi
        shapeRenderer.setColor(BASE_DARK);
        shapeRenderer.rect(x - width / 2f, y - height / 2f, width / 2f, height / 2f, width, height, 1f, 1f, angle);

        // Krom Montaj Vidaları
        shapeRenderer.setColor(0.65f, 0.75f, 0.90f, 1.0f);
        float rad = (float) Math.toRadians(angle);
        float cos = (float) Math.cos(rad);
        float sin = (float) Math.sin(rad);
        float bOff = width * 0.38f;
        shapeRenderer.circle(x - cos * bOff, y - sin * bOff, 0.045f, 8);
        shapeRenderer.circle(x + cos * bOff, y + sin * bOff, 0.045f, 8);

        // 3. Neon Silikon Yay Yastığı (Hyper-Elastic Cushion)
        shapeRenderer.setColor(CUSHION_COLOR);
        shapeRenderer.rect(x - width / 2f, y - height / 4f, width / 2f, height / 4f, width, height * 0.75f, 1f, scaleY, angle);

        // 4. Üst Neon LED Parlama Çizgisi
        shapeRenderer.setColor(NEON_GLOW);
        shapeRenderer.rect(x - width / 2f + 0.08f, y + height / 4f, width / 2f, 0.03f, width - 0.16f, 0.08f, 1f, scaleY, angle);

        // 5. Enerji Sıçrama Parlaması (Bounce Impact Glow)
        if (bounceAnim > 0.05f) {
            shapeRenderer.setColor(1.0f, 1.0f, 1.0f, bounceAnim * 0.65f);
            shapeRenderer.rect(x - width / 2f, y + height / 4f, width / 2f, 0.05f, width, 0.14f, 1f, 1f, angle);
        }

        // 6. Yön Belirteç Parlayan Çift Nokta
        shapeRenderer.setColor(ARROW_COLOR);
        float nrad = (float) Math.toRadians(angle + 90);
        float nx = (float) Math.cos(nrad) * 0.16f;
        float ny = (float) Math.sin(nrad) * 0.16f;
        shapeRenderer.circle(x + nx, y + ny, 0.07f, 12);
        shapeRenderer.setColor(Color.WHITE);
        shapeRenderer.circle(x + nx, y + ny, 0.03f, 8);
    }
}
