package com.game.pullthepin.editor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;
import com.game.pullthepin.entities.Ball;
import com.game.pullthepin.entities.Pin;

/**
 * Bölüm Editörü Veri Modeli ve JSON Kayıt / Yükleme Yöneticisi.
 */
public class EditorData {

    public static class WallDef {
        public Array<Vector2> points = new Array<>();
        public WallDef() {}
    }

    public static class PinDef {
        public float x, y;
        public float length = 3.5f;
        public Pin.Direction dir = Pin.Direction.RIGHT;
        public PinDef() {}
        public PinDef(float x, float y, float length, Pin.Direction dir) {
            this.x = x; this.y = y; this.length = length; this.dir = dir;
        }
    }

    public static class BallDef {
        public float x, y;
        public Ball.BallType type = Ball.BallType.COLORED;
        public BallDef() {}
        public BallDef(float x, float y, Ball.BallType type) {
            this.x = x; this.y = y; this.type = type;
        }
    }

    public static class JarDef {
        public float x = 4.5f, y = 1.8f;
        public float width = 3.0f, height = 2.4f;
        public int requiredBalls = 15;
        public JarDef() {}
    }

    public static class PortalDef {
        public float inX, inY, outX, outY;
        public PortalDef() {}
        public PortalDef(float inX, float inY, float outX, float outY) {
            this.inX = inX; this.inY = inY; this.outX = outX; this.outY = outY;
        }
    }

    public static class BouncerDef {
        public float x, y;
        public float width = 2.2f, height = 0.35f;
        public float angle = 45f;
        public float force = 8.5f;
        public BouncerDef() {}
        public BouncerDef(float x, float y, float angle) {
            this.x = x; this.y = y; this.angle = angle;
        }
    }

    public static class SpikeDef {
        public float x, y;
        public float width = 2.0f, height = 0.4f;
        public SpikeDef() {}
        public SpikeDef(float x, float y) {
            this.x = x; this.y = y;
        }
    }

    public String levelName = "Özel Seviye";
    public JarDef jar = new JarDef();
    public Array<WallDef> walls = new Array<>();
    public Array<PinDef> pins = new Array<>();
    public Array<BallDef> balls = new Array<>();
    public Array<PortalDef> portals = new Array<>();
    public Array<BouncerDef> bouncers = new Array<>();
    public Array<SpikeDef> spikes = new Array<>();

    public EditorData() {
        createDefaultBoundaries();
    }

    public void clear() {
        walls.clear();
        pins.clear();
        balls.clear();
        portals.clear();
        bouncers.clear();
        spikes.clear();
        jar = new JarDef();
        createDefaultBoundaries();
    }

    public void createDefaultBoundaries() {
        // Standart Sol Huni Duvarı
        WallDef leftWall = new WallDef();
        leftWall.points.add(new Vector2(1.5f, 15.0f));
        leftWall.points.add(new Vector2(1.5f, 9.0f));
        leftWall.points.add(new Vector2(3.3f, 5.0f));
        leftWall.points.add(new Vector2(3.3f, 3.5f));
        walls.add(leftWall);

        // Standart Sağ Huni Duvarı
        WallDef rightWall = new WallDef();
        rightWall.points.add(new Vector2(7.5f, 15.0f));
        rightWall.points.add(new Vector2(7.5f, 9.0f));
        rightWall.points.add(new Vector2(5.7f, 5.0f));
        rightWall.points.add(new Vector2(5.7f, 3.5f));
        walls.add(rightWall);

        // Standart Alt Tutucu Pim
        pins.add(new PinDef(4.5f, 7.5f, 5.2f, Pin.Direction.RIGHT));
    }

    public EditorData copy() {
        EditorData c = new EditorData();
        c.levelName = this.levelName;
        c.jar.x = this.jar.x;
        c.jar.y = this.jar.y;
        c.jar.width = this.jar.width;
        c.jar.height = this.jar.height;
        c.jar.requiredBalls = this.jar.requiredBalls;

        c.walls.clear();
        for (WallDef w : this.walls) {
            WallDef nw = new WallDef();
            for (Vector2 p : w.points) nw.points.add(p.cpy());
            c.walls.add(nw);
        }

        c.pins.clear();
        for (PinDef p : this.pins) {
            c.pins.add(new PinDef(p.x, p.y, p.length, p.dir));
        }

        c.balls.clear();
        for (BallDef b : this.balls) {
            c.balls.add(new BallDef(b.x, b.y, b.type));
        }

        c.portals.clear();
        for (PortalDef port : this.portals) {
            c.portals.add(new PortalDef(port.inX, port.inY, port.outX, port.outY));
        }

        c.bouncers.clear();
        for (BouncerDef b : this.bouncers) {
            BouncerDef nb = new BouncerDef(b.x, b.y, b.angle);
            nb.width = b.width; nb.height = b.height; nb.force = b.force;
            c.bouncers.add(nb);
        }

        c.spikes.clear();
        for (SpikeDef s : this.spikes) {
            SpikeDef ns = new SpikeDef(s.x, s.y);
            ns.width = s.width; ns.height = s.height;
            c.spikes.add(ns);
        }

        return c;
    }

    public int getColoredBallCount() {
        int count = 0;
        for (BallDef b : balls) if (b.type == Ball.BallType.COLORED) count++;
        return count;
    }

    public int getGrayBallCount() {
        int count = 0;
        for (BallDef b : balls) if (b.type == Ball.BallType.GRAY) count++;
        return count;
    }

    public int getBombCount() {
        int count = 0;
        for (BallDef b : balls) if (b.type == Ball.BallType.BOMB) count++;
        return count;
    }

    public Array<String> getValidationWarnings() {
        Array<String> warnings = new Array<>();
        int colored = getColoredBallCount();
        int total = balls.size;
        if (colored == 0) {
            warnings.add("UYARI: Hic renkli bilye yok! Gri bilyeler renksiz gecemez.");
        }
        if (total < jar.requiredBalls) {
            warnings.add("UYARI: Toplam bilye (" + total + ") hedef sayidan (" + jar.requiredBalls + ") az!");
        }
        if (pins.size == 0) {
            warnings.add("BILGI: Bolumde hic pim bulunmuyor.");
        }
        return warnings;
    }

    public static void saveToSlot(int slot, EditorData data) {
        Preferences prefs = Gdx.app.getPreferences("PullThePin_CustomLevels");
        Json json = new Json();
        json.setOutputType(JsonWriter.OutputType.json);
        String jsonStr = json.toJson(data);
        prefs.putString("slot_" + slot, jsonStr);
        prefs.flush();
    }

    public static EditorData loadFromSlot(int slot) {
        Preferences prefs = Gdx.app.getPreferences("PullThePin_CustomLevels");
        String jsonStr = prefs.getString("slot_" + slot, null);
        if (jsonStr != null && !jsonStr.trim().isEmpty()) {
            try {
                Json json = new Json();
                return json.fromJson(EditorData.class, jsonStr);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return new EditorData();
    }
}
