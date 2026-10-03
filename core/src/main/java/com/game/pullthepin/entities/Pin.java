package com.game.pullthepin.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

/**
 * Üst Düzey 2.5D İzometrik Fırçalanmış Altın & Metalik Silindir Pim.
 * Dokunulduğunda parlayan çift halkalı ışık aurası, 3D oluklu gövde ve pürüzsüz çekme hissi.
 */
public class Pin {

    public enum Direction {
        LEFT, RIGHT, UP, DOWN
    }

    private World world;
    private Body body;

    private float originX, originY;
    private float currentX, currentY;
    private float length;
    private float thickness = 0.28f; // Topların sızmasını önleyen sağlam kalınlık
    private float handleRadius = 0.50f;
    private Direction direction;

    private boolean isHorizontal;
    private boolean isSelected = false;
    private boolean isRemoved = false;
    private boolean isAnimatingOut = false;
    private float pullProgress = 0f;
    private float auraPulse = 0f;

    // Kilit & Anahtar Sistemi (Key & Lock System)
    private boolean isLocked = false;
    private int keyId = 0;
    private float lockedShakeTimer = 0f;

    // Altın ve Değerli Taş Metal Renk Paleti (Skinlenebilir)
    private Color GOLD_DARK = new Color(0.48f, 0.32f, 0.06f, 1f);
    private Color GOLD_BASE = new Color(0.95f, 0.78f, 0.22f, 1f);
    private Color GOLD_LIGHT = new Color(1.00f, 0.92f, 0.55f, 1f);
    private Color GOLD_SPECULAR = new Color(1.00f, 0.98f, 0.88f, 1f);

    private Color HANDLE_OUTER = new Color(0.98f, 0.35f, 0.48f, 1f);
    private Color HANDLE_INNER_GLOW = new Color(1.00f, 0.75f, 0.82f, 1f);
    private Color HANDLE_SHADOW = new Color(0.55f, 0.12f, 0.20f, 1f);
    private Color sparkColor = new Color(1.00f, 0.88f, 0.35f, 1f);
    private com.game.pullthepin.shop.SkinManager.PinSkin activeSkin = null;

    public void applySkin(com.game.pullthepin.shop.SkinManager.PinSkin skin) {
        if (skin == null) return;
        this.activeSkin = skin;
        this.GOLD_BASE = skin.shaftBase.cpy();
        this.GOLD_DARK = skin.shaftDark.cpy();
        this.GOLD_LIGHT = skin.shaftLight.cpy();
        this.GOLD_SPECULAR = new Color(Math.min(1f, skin.shaftLight.r + 0.15f), Math.min(1f, skin.shaftLight.g + 0.15f), Math.min(1f, skin.shaftLight.b + 0.15f), 1f);
        this.HANDLE_OUTER = skin.handleOuter.cpy();
        this.HANDLE_INNER_GLOW = skin.handleInner.cpy();
        this.HANDLE_SHADOW = new Color(skin.handleOuter.r * 0.4f, skin.handleOuter.g * 0.4f, skin.handleOuter.b * 0.4f, 1f);
        this.sparkColor = skin.sparkColor.cpy();
    }

    public Color getSparkColor() {
        return sparkColor;
    }

    public Pin(World world, float x, float y, float length, Direction direction) {
        this(world, x, y, length, direction, false, 0);
    }

    public Pin(World world, float x, float y, float length, Direction direction, boolean isLocked, int keyId) {
        this.world = world;
        this.originX = x;
        this.originY = y;
        this.currentX = x;
        this.currentY = y;
        this.length = length;
        this.direction = direction;
        this.isHorizontal = (direction == Direction.LEFT || direction == Direction.RIGHT);
        this.isLocked = isLocked;
        this.keyId = keyId;

        createPhysicsBody();
    }

    private void createPhysicsBody() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(originX, originY);

        body = world.createBody(bodyDef);
        body.setUserData(this);

        PolygonShape shape = new PolygonShape();
        if (isHorizontal) {
            shape.setAsBox(length / 2f, thickness / 2f);
        } else {
            shape.setAsBox(thickness / 2f, length / 2f);
        }

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.friction = 0.15f;
        fixtureDef.restitution = 0.2f;

        body.createFixture(fixtureDef);
        shape.dispose();
    }

    public void update(float delta) {
        auraPulse += delta * 6.5f;
        if (lockedShakeTimer > 0) lockedShakeTimer -= delta;

        if (isAnimatingOut) {
            pullProgress += delta * 5.5f;
            if (pullProgress >= 1.25f) {
                pullProgress = 1.25f;
                isAnimatingOut = false;
                isRemoved = true;
            }
            updatePositionByProgress();
        }
    }

    private void updatePositionByProgress() {
        float offset = pullProgress * (length + handleRadius * 2.2f);
        switch (direction) {
            case RIGHT: currentX = originX + offset; break;
            case LEFT:  currentX = originX - offset; break;
            case UP:    currentY = originY + offset; break;
            case DOWN:  currentY = originY - offset; break;
        }

        if (body != null) {
            body.setTransform(currentX, currentY, 0);
        }
    }

    public boolean containsHandle(float touchX, float touchY) {
        if (isRemoved || isAnimatingOut) return false;
        Vector2 handlePos = getHandlePosition();
        float distSq = (touchX - handlePos.x) * (touchX - handlePos.x) + (touchY - handlePos.y) * (touchY - handlePos.y);
        return distSq <= (handleRadius * 1.7f) * (handleRadius * 1.7f);
    }

    public boolean containsPin(float touchX, float touchY) {
        if (isRemoved || isAnimatingOut) return false;

        if (isHorizontal) {
            return touchX >= currentX - length / 2f - handleRadius &&
                   touchX <= currentX + length / 2f + handleRadius &&
                   Math.abs(touchY - currentY) <= thickness + 0.35f;
        } else {
            return touchY >= currentY - length / 2f - handleRadius &&
                   touchY <= currentY + length / 2f + handleRadius &&
                   Math.abs(touchX - currentX) <= thickness + 0.35f;
        }
    }

    public Vector2 getHandlePosition() {
        switch (direction) {
            case RIGHT: return new Vector2(currentX + length / 2f + handleRadius * 0.55f, currentY);
            case LEFT:  return new Vector2(currentX - length / 2f - handleRadius * 0.55f, currentY);
            case UP:    return new Vector2(currentX, currentY + length / 2f + handleRadius * 0.55f);
            case DOWN:
            default:    return new Vector2(currentX, currentY - length / 2f - handleRadius * 0.55f);
        }
    }

    public boolean onDrag(float dragX, float dragY) {
        if (isRemoved || isAnimatingOut) return false;
        if (isLocked) {
            triggerLockedFeedback();
            return false;
        }

        float delta = 0;
        if (isHorizontal) {
            if (direction == Direction.RIGHT) {
                delta = dragX - (originX + length / 2f);
            } else {
                delta = (originX - length / 2f) - dragX;
            }
        } else {
            if (direction == Direction.UP) {
                delta = dragY - (originY + length / 2f);
            } else {
                delta = (originY - length / 2f) - dragY;
            }
        }

        if (delta < 0) delta = 0;
        pullProgress = Math.min(1.0f, delta / length);
        updatePositionByProgress();

        if (pullProgress > 0.36f) {
            return releaseAndSlideOut();
        }
        return false;
    }

    public boolean releaseAndSlideOut() {
        if (isRemoved || isAnimatingOut || isLocked) return false;
        isAnimatingOut = true;
        isSelected = false;

        if (body != null) {
            world.destroyBody(body);
            body = null;
        }
        return true;
    }

    public float getCurrentX() { return currentX; }
    public float getCurrentY() { return currentY; }
    public boolean isLocked() { return isLocked; }
    public int getKeyId() { return keyId; }
    public void triggerLockedFeedback() { lockedShakeTimer = 0.28f; }

    public boolean unlock() {
        if (!isLocked) return false;
        isLocked = false;
        lockedShakeTimer = 0f;
        return true;
    }

    /**
     * 2.5D Metalik Silindir & Parlak Mücevher Halka Render Pipeline
     */
    public void render(ShapeRenderer shapeRenderer) {
        if (isRemoved) return;

        Vector2 handle = getHandlePosition();
        if (lockedShakeTimer > 0) {
            float shakeOffset = (float) Math.sin(lockedShakeTimer * 35f) * 0.08f;
            handle.add(shakeOffset, 0);
        }

        // 1. Dokunma / Seçim Parlama Aurası (Dynamic Golden Glow Halo)
        if (isSelected) {
            float glowPulse = 1.0f + 0.12f * (float) Math.sin(auraPulse);
            shapeRenderer.setColor(1.0f, 0.85f, 0.3f, 0.22f);
            shapeRenderer.circle(handle.x, handle.y, handleRadius * 1.8f * glowPulse, 24);
            shapeRenderer.setColor(1.0f, 0.95f, 0.5f, 0.38f);
            shapeRenderer.circle(handle.x, handle.y, handleRadius * 1.4f * glowPulse, 24);
        }

        // 2. Zemin Derinlik Gölgesi (Contact Drop Shadow)
        shapeRenderer.setColor(0f, 0f, 0f, 0.26f);
        if (isHorizontal) {
            shapeRenderer.rect(currentX - length / 2f + 0.06f, currentY - thickness / 2f - 0.07f, length, thickness);
            shapeRenderer.circle(currentX - length / 2f + 0.06f, currentY - 0.07f, thickness / 2f, 16);
            shapeRenderer.circle(currentX + length / 2f + 0.06f, currentY - 0.07f, thickness / 2f, 16);
        } else {
            shapeRenderer.rect(currentX - thickness / 2f + 0.06f, currentY - length / 2f - 0.07f, thickness, length);
            shapeRenderer.circle(currentX + 0.06f, currentY - length / 2f - 0.07f, thickness / 2f, 16);
            shapeRenderer.circle(currentX + 0.06f, currentY + length / 2f - 0.07f, thickness / 2f, 16);
        }
        shapeRenderer.circle(handle.x + 0.06f, handle.y - 0.07f, handleRadius, 22);

        // 3. 2.5D Çok Katmanlı Altın Metal Gövde
        if (isHorizontal) {
            // Taban koyu metal
            shapeRenderer.setColor(GOLD_DARK);
            shapeRenderer.rect(currentX - length / 2f, currentY - thickness / 2f, length, thickness);
            // Orta altın gövde
            shapeRenderer.setColor(GOLD_BASE);
            shapeRenderer.rect(currentX - length / 2f, currentY - thickness * 0.20f, length, thickness * 0.70f);
            // Üst altın ışık yansıması
            shapeRenderer.setColor(GOLD_LIGHT);
            shapeRenderer.rect(currentX - length / 2f, currentY + thickness * 0.10f, length, thickness * 0.30f);
            // Aynasal beyaz çizgi
            shapeRenderer.setColor(GOLD_SPECULAR);
            shapeRenderer.rect(currentX - length / 2f, currentY + thickness * 0.22f, length, thickness * 0.10f);

            // Tactile Grip Ridges (Metal Oyuklar)
            shapeRenderer.setColor(GOLD_DARK);
            for (float gx = currentX - length * 0.35f; gx <= currentX + length * 0.35f; gx += 0.45f) {
                shapeRenderer.rect(gx, currentY - thickness * 0.35f, 0.05f, thickness * 0.70f);
            }

            // Yuvarlak Uç Başlıkları
            shapeRenderer.setColor(GOLD_BASE);
            shapeRenderer.circle(currentX - length / 2f, currentY, thickness / 2f, 16);
            shapeRenderer.circle(currentX + length / 2f, currentY, thickness / 2f, 16);
            shapeRenderer.setColor(GOLD_SPECULAR);
            shapeRenderer.circle(currentX - length / 2f, currentY + 0.04f, thickness * 0.25f, 10);
            shapeRenderer.circle(currentX + length / 2f, currentY + 0.04f, thickness * 0.25f, 10);
        } else {
            shapeRenderer.setColor(GOLD_DARK);
            shapeRenderer.rect(currentX - thickness / 2f, currentY - length / 2f, thickness, length);
            shapeRenderer.setColor(GOLD_BASE);
            shapeRenderer.rect(currentX - thickness * 0.20f, currentY - length / 2f, thickness * 0.70f, length);
            shapeRenderer.setColor(GOLD_LIGHT);
            shapeRenderer.rect(currentX + thickness * 0.10f, currentY - length / 2f, thickness * 0.30f, length);
            shapeRenderer.setColor(GOLD_SPECULAR);
            shapeRenderer.rect(currentX + thickness * 0.22f, currentY - length / 2f, thickness * 0.10f, length);

            // Tactile Grip Ridges (Metal Oyuklar)
            shapeRenderer.setColor(GOLD_DARK);
            for (float gy = currentY - length * 0.35f; gy <= currentY + length * 0.35f; gy += 0.45f) {
                shapeRenderer.rect(currentX - thickness * 0.35f, gy, thickness * 0.70f, 0.05f);
            }

            shapeRenderer.setColor(GOLD_BASE);
            shapeRenderer.circle(currentX, currentY - length / 2f, thickness / 2f, 16);
            shapeRenderer.circle(currentX, currentY + length / 2f, thickness / 2f, 16);
            shapeRenderer.setColor(GOLD_SPECULAR);
            shapeRenderer.circle(currentX + 0.04f, currentY - length / 2f, thickness * 0.25f, 10);
            shapeRenderer.circle(currentX + 0.04f, currentY + length / 2f, thickness * 0.25f, 10);
        }

        // 4. 3D Kristal Çekme Halkası (Jeweled Eyelet Handle)
        shapeRenderer.setColor(HANDLE_SHADOW);
        shapeRenderer.circle(handle.x, handle.y - 0.04f, handleRadius, 24);

        // Ana Mercan/Yakut Halka
        shapeRenderer.setColor(isLocked ? new Color(0.35f, 0.38f, 0.48f, 1.0f) : HANDLE_OUTER);
        shapeRenderer.circle(handle.x, handle.y, handleRadius, 24);

        // İç Işıma ve Parlama Vurgusu
        shapeRenderer.setColor(isLocked ? new Color(0.65f, 0.70f, 0.80f, 1.0f) : HANDLE_INNER_GLOW);
        shapeRenderer.circle(handle.x - handleRadius * 0.28f, handle.y + handleRadius * 0.28f, handleRadius * 0.32f, 16);
        shapeRenderer.setColor(Color.WHITE);
        shapeRenderer.circle(handle.x - handleRadius * 0.32f, handle.y + handleRadius * 0.32f, handleRadius * 0.12f, 10);

        // Halka Ortasındaki Boşluk (Derinlikli Delik)
        shapeRenderer.setColor(0.06f, 0.08f, 0.14f, 1f);
        shapeRenderer.circle(handle.x, handle.y, handleRadius * 0.45f, 20);
        shapeRenderer.setColor(GOLD_DARK);
        shapeRenderer.circle(handle.x, handle.y, handleRadius * 0.40f, 18);
        shapeRenderer.setColor(0.06f, 0.08f, 0.14f, 1f);
        shapeRenderer.circle(handle.x + 0.02f, handle.y - 0.02f, handleRadius * 0.35f, 16);

        // 5. Kilitli Pim Padlock Rozeti (Security Padlock Badge)
        if (isLocked) {
            float px = handle.x;
            float py = handle.y;

            // Kilit Gövdesi (Koyu Arka Plan + Neon Kırmızı/Altın Şerit)
            shapeRenderer.setColor(0.08f, 0.10f, 0.18f, 0.96f);
            shapeRenderer.rect(px - 0.20f, py - 0.22f, 0.40f, 0.34f);

            shapeRenderer.setColor(0.98f, 0.35f, 0.42f, 1.0f);
            shapeRenderer.rect(px - 0.20f, py - 0.22f, 0.40f, 0.05f);
            shapeRenderer.rect(px - 0.20f, py + 0.07f, 0.40f, 0.05f);

            // Kilit Kelepçe Yayı (Silver Padlock Shackle)
            shapeRenderer.setColor(0.85f, 0.88f, 0.95f, 1.0f);
            shapeRenderer.rectLine(px - 0.11f, py + 0.10f, px - 0.11f, py + 0.22f, 0.045f);
            shapeRenderer.rectLine(px + 0.11f, py + 0.10f, px + 0.11f, py + 0.22f, 0.045f);
            shapeRenderer.rectLine(px - 0.11f, py + 0.22f, px + 0.11f, py + 0.22f, 0.045f);

            // Anahtar Deliği
            shapeRenderer.setColor(0.04f, 0.06f, 0.12f, 1.0f);
            shapeRenderer.circle(px, py - 0.03f, 0.055f, 12);
            shapeRenderer.rect(px - 0.022f, py - 0.13f, 0.044f, 0.09f);
        } else if (!isRemoved && !isAnimatingOut) {
            // Yönlendirme Oku / Çekme İpucu (Directional Pull Arrow)
            float arrowPulse = 0.5f + 0.5f * (float) Math.sin(auraPulse * 1.5f);
            shapeRenderer.setColor(1f, 1f, 1f, 0.35f + arrowPulse * 0.45f);
            float arrDist = handleRadius * 1.25f + arrowPulse * 0.08f;
            float ax = handle.x; float ay = handle.y;
            float asz = 0.10f;
            switch (direction) {
                case RIGHT:
                    shapeRenderer.triangle(ax + arrDist, ay, ax + arrDist - asz, ay + asz, ax + arrDist - asz, ay - asz);
                    break;
                case LEFT:
                    shapeRenderer.triangle(ax - arrDist, ay, ax - arrDist + asz, ay + asz, ax - arrDist + asz, ay - asz);
                    break;
                case UP:
                    shapeRenderer.triangle(ax, ay + arrDist, ax - asz, ay + arrDist - asz, ax + asz, ay + arrDist - asz);
                    break;
                case DOWN:
                    shapeRenderer.triangle(ax, ay - arrDist, ax - asz, ay - arrDist + asz, ax + asz, ay - arrDist + asz);
                    break;
            }
        }
    }

    public void setSelected(boolean selected) { this.isSelected = selected; }
    public boolean isRemoved() { return isRemoved; }
    public boolean isAnimatingOut() { return isAnimatingOut; }
    public boolean isPulled() { return isRemoved || isAnimatingOut; }

    public float getOriginX() { return originX; }
    public float getOriginY() { return originY; }
    public float getLength() { return length; }
    public float getThickness() { return thickness; }
    public boolean isHorizontal() { return isHorizontal; }
    public Direction getDirection() { return direction; }
}
