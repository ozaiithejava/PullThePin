package com.game.pullthepin.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.World;

/**
 * 2.5D İzometrik Duvarlar ve Oluklu Kanal Rayları (Tactile Channel Walls with 3D Bevel).
 */
public class LevelWall {

    private Body body;
    private Vector2[] vertices;

    // Şık İzometrik Duvar Tonları
    private final Color WALL_SHADOW = new Color(0.04f, 0.06f, 0.10f, 0.45f);
    private final Color WALL_BASE = new Color(0.20f, 0.25f, 0.36f, 1.0f);
    private final Color WALL_BEVEL = new Color(0.38f, 0.48f, 0.65f, 1.0f);
    private final Color WALL_HIGHLIGHT = new Color(0.65f, 0.78f, 0.95f, 0.85f);

    public LevelWall(World world, Vector2[] vertices) {
        this.vertices = vertices;

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        body = world.createBody(bodyDef);
        body.setUserData(this);

        ChainShape chainShape = new ChainShape();
        chainShape.createChain(vertices);
        body.createFixture(chainShape, 0.0f);
        chainShape.dispose();
    }

    /**
     * 2.5D Katmanlı Eğimli Duvar Çizimi
     */
    public void render(ShapeRenderer shapeRenderer) {
        // 1. Zemin Derinlik Gölgesi
        shapeRenderer.setColor(WALL_SHADOW);
        for (int i = 0; i < vertices.length - 1; i++) {
            Vector2 p1 = vertices[i];
            Vector2 p2 = vertices[i + 1];
            shapeRenderer.rectLine(p1.x + 0.07f, p1.y - 0.09f, p2.x + 0.07f, p2.y - 0.09f, 0.32f);
        }

        // 2. Ana Duvar Gövdesi (Koyu Slate Boru)
        shapeRenderer.setColor(WALL_BASE);
        for (int i = 0; i < vertices.length - 1; i++) {
            Vector2 p1 = vertices[i];
            Vector2 p2 = vertices[i + 1];
            shapeRenderer.rectLine(p1.x, p1.y, p2.x, p2.y, 0.24f);
            shapeRenderer.circle(p1.x, p1.y, 0.12f, 16);
            shapeRenderer.circle(p2.x, p2.y, 0.12f, 16);
        }

        // 3. Orta Eğim Rayı (Bevel)
        shapeRenderer.setColor(WALL_BEVEL);
        for (int i = 0; i < vertices.length - 1; i++) {
            Vector2 p1 = vertices[i];
            Vector2 p2 = vertices[i + 1];
            shapeRenderer.rectLine(p1.x - 0.02f, p1.y + 0.02f, p2.x - 0.02f, p2.y + 0.02f, 0.13f);
        }

        // 4. Üst İnce Neon Işık Hattı (Specular Neon Ridge Line)
        shapeRenderer.setColor(WALL_HIGHLIGHT);
        for (int i = 0; i < vertices.length - 1; i++) {
            Vector2 p1 = vertices[i];
            Vector2 p2 = vertices[i + 1];
            shapeRenderer.rectLine(p1.x - 0.035f, p1.y + 0.035f, p2.x - 0.035f, p2.y + 0.035f, 0.045f);
        }

        // 5. Köşe Bağlantı Perçinleri (Chrome Rivets / Joint Caps)
        shapeRenderer.setColor(0.70f, 0.82f, 0.98f, 0.95f);
        for (Vector2 v : vertices) {
            shapeRenderer.circle(v.x, v.y, 0.075f, 12);
        }
        shapeRenderer.setColor(Color.WHITE);
        for (Vector2 v : vertices) {
            shapeRenderer.circle(v.x - 0.02f, v.y + 0.02f, 0.03f, 8);
        }
    }

    public Vector2[] getVertices() {
        return vertices;
    }
}
