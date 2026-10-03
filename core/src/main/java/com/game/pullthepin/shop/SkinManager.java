package com.game.pullthepin.shop;

import com.badlogic.gdx.graphics.Color;
import com.game.pullthepin.SaveManager;

public class SkinManager {

    // ── 1. BİLYE KAPLAMALARI (BALL SKINS) ──────────────────────────────────
    public enum BallSkin {
        CLASSIC("classic", "Klasik", "Classic", 0, "basic",
                new Color(0.20f,0.60f,1.00f,1f), new Color(0.10f,0.30f,0.60f,1f), new Color(1f,1f,1f,0.80f)),
        CRYSTAL("crystal", "Kristal", "Crystal", 0, "basic",
                new Color(0.80f,0.90f,1.00f,0.90f), new Color(0.40f,0.60f,0.90f,1f), new Color(1f,1f,1f,0.95f)),
        MARBLE("marble", "Mermer", "Marble", 200, "basic",
                new Color(0.95f,0.95f,0.95f,1f), new Color(0.60f,0.60f,0.65f,1f), new Color(1f,1f,1f,0.70f)),
        FIRE("fire", "Ates", "Fire", 300, "elemental",
                new Color(1.00f,0.35f,0.05f,1f), new Color(0.70f,0.10f,0.02f,1f), new Color(1f,0.80f,0.20f,0.90f)),
        ICE("ice", "Buz", "Ice", 300, "elemental",
                new Color(0.50f,0.85f,1.00f,1f), new Color(0.20f,0.55f,0.85f,1f), new Color(1f,1f,1f,0.90f)),
        THUNDER("thunder", "Simsek", "Thunder", 400, "elemental",
                new Color(0.95f,0.90f,0.10f,1f), new Color(0.55f,0.45f,0.02f,1f), new Color(1f,1f,0.50f,0.90f)),
        POISON("poison", "Zehir", "Poison", 400, "elemental",
                new Color(0.35f,0.85f,0.20f,1f), new Color(0.15f,0.45f,0.05f,1f), new Color(0.70f,1f,0.30f,0.80f)),
        NEBULA("nebula", "Nebula", "Nebula", 800, "cosmic",
                new Color(0.55f,0.20f,0.90f,1f), new Color(0.25f,0.05f,0.55f,1f), new Color(0.85f,0.60f,1f,0.85f)),
        BLACKHOLE("blackhole", "Kara Delik", "Black Hole", 1200, "cosmic",
                new Color(0.08f,0.05f,0.12f,1f), new Color(0.02f,0.01f,0.04f,1f), new Color(0.50f,0.20f,0.80f,0.70f)),
        GALAXY("galaxy", "Galaksi", "Galaxy", 1500, "cosmic",
                new Color(0.25f,0.45f,0.85f,1f), new Color(0.08f,0.18f,0.48f,1f), new Color(0.80f,0.85f,1f,0.90f)),
        GOLD("gold", "Altin", "Gold", 3000, "prestige",
                new Color(1.00f,0.84f,0.22f,1f), new Color(0.65f,0.45f,0.08f,1f), new Color(1f,1f,0.80f,0.95f)),
        DIAMOND("diamond", "Elmas", "Diamond", 5000, "prestige",
                new Color(0.92f,0.98f,1.00f,1f), new Color(0.60f,0.80f,0.95f,1f), new Color(1f,1f,1f,1.00f));

        public final String id, nameTR, nameEN, category;
        public final int price;
        public final Color primary, secondary, specular;

        BallSkin(String id, String nameTR, String nameEN, int price, String category,
                 Color primary, Color secondary, Color specular) {
            this.id = id; this.nameTR = nameTR; this.nameEN = nameEN;
            this.price = price; this.category = category;
            this.primary = primary; this.secondary = secondary; this.specular = specular;
        }
    }

    // ── 2. PİM KAPLAMALARI (PIN SKINS) ─────────────────────────────────────
    public enum PinSkin {
        CLASSIC("pin_classic", "Klasik Altin", "Classic Gold", 0, "basic",
                new Color(0.95f,0.78f,0.22f,1f), new Color(0.48f,0.32f,0.06f,1f), new Color(1.00f,0.92f,0.55f,1f),
                new Color(0.98f,0.35f,0.48f,1f), new Color(1.00f,0.75f,0.82f,1f), new Color(1.00f,0.88f,0.35f,1f)),
        CYBER("pin_cyber", "Siber Isin", "Cyber Blade", 500, "elemental",
                new Color(0.12f,0.88f,1.00f,1f), new Color(0.04f,0.30f,0.55f,1f), new Color(0.65f,0.95f,1.00f,1f),
                new Color(0.85f,0.20f,0.90f,1f), new Color(0.45f,0.90f,1.00f,1f), new Color(0.20f,0.95f,1.00f,1f)),
        CANDY("pin_candy", "Baston Seker", "Candy Cane", 600, "elemental",
                new Color(0.95f,0.22f,0.28f,1f), new Color(0.55f,0.08f,0.12f,1f), new Color(1.00f,1.00f,1.00f,1f),
                new Color(0.18f,0.85f,0.45f,1f), new Color(0.85f,1.00f,0.90f,1f), new Color(1.00f,0.45f,0.60f,1f)),
        BAMBOO("pin_bamboo", "Kadim Bambu", "Zen Bamboo", 800, "basic",
                new Color(0.38f,0.85f,0.28f,1f), new Color(0.12f,0.45f,0.08f,1f), new Color(0.75f,0.98f,0.55f,1f),
                new Color(0.85f,0.70f,0.25f,1f), new Color(0.50f,0.95f,0.35f,1f), new Color(0.45f,1.00f,0.35f,1f)),
        KATANA("pin_katana", "Ejder Katana", "Dragon Katana", 1500, "cosmic",
                new Color(0.88f,0.92f,1.00f,1f), new Color(0.28f,0.30f,0.38f,1f), new Color(1.00f,1.00f,1.00f,1f),
                new Color(0.90f,0.12f,0.18f,1f), new Color(1.00f,0.85f,0.30f,1f), new Color(0.95f,0.95f,1.00f,1f)),
        ROYAL("pin_royal", "Kraliyet Asasi", "Royal Scepter", 2500, "prestige",
                new Color(1.00f,0.84f,0.15f,1f), new Color(0.65f,0.42f,0.05f,1f), new Color(1.00f,0.98f,0.75f,1f),
                new Color(0.92f,0.10f,0.28f,1f), new Color(1.00f,0.90f,0.40f,1f), new Color(1.00f,0.92f,0.35f,1f));

        public final String id, nameTR, nameEN, category;
        public final int price;
        public final Color shaftBase, shaftDark, shaftLight, handleOuter, handleInner, sparkColor;

        PinSkin(String id, String nameTR, String nameEN, int price, String category,
                Color shaftBase, Color shaftDark, Color shaftLight,
                Color handleOuter, Color handleInner, Color sparkColor) {
            this.id = id; this.nameTR = nameTR; this.nameEN = nameEN;
            this.price = price; this.category = category;
            this.shaftBase = shaftBase; this.shaftDark = shaftDark; this.shaftLight = shaftLight;
            this.handleOuter = handleOuter; this.handleInner = handleInner; this.sparkColor = sparkColor;
        }
    }

    // ── 3. KUYRUK İZLERİ (TRAIL SKINS) ─────────────────────────────────────
    public enum TrailSkin {
        DEFAULT("trail_default", "Klasik Iz", "Classic Glow", 0,
                new Color(0.25f, 0.65f, 1.00f, 0.35f), new Color(0.80f, 0.95f, 1.00f, 0.70f)),
        STARDUST("trail_stardust", "Yildiz Tozu", "Stardust", 400,
                new Color(1.00f, 0.85f, 0.20f, 0.55f), new Color(1.00f, 0.98f, 0.60f, 0.90f)),
        FIRE("trail_fire", "Ates & Koz", "Meteor Fire", 750,
                new Color(1.00f, 0.38f, 0.08f, 0.60f), new Color(1.00f, 0.85f, 0.20f, 0.90f)),
        FROST("trail_frost", "Buz Firtinasi", "Frost Spark", 750,
                new Color(0.40f, 0.85f, 1.00f, 0.55f), new Color(0.85f, 1.00f, 1.00f, 0.90f)),
        RAINBOW("trail_rainbow", "Gokkusagi", "Rainbow Dust", 1500,
                new Color(0.95f, 0.40f, 0.85f, 0.60f), new Color(0.35f, 0.95f, 0.85f, 0.90f));

        public final String id, nameTR, nameEN;
        public final int price;
        public final Color primary, glow;

        TrailSkin(String id, String nameTR, String nameEN, int price, Color primary, Color glow) {
            this.id = id; this.nameTR = nameTR; this.nameEN = nameEN;
            this.price = price;
            this.primary = primary; this.glow = glow;
        }
    }

    private SaveManager saveManager;

    public SkinManager(SaveManager saveManager) {
        this.saveManager = saveManager;
    }

    // ── BİLYE YÖNETİMİ ──
    public boolean isSkinOwned(BallSkin skin) {
        if (skin.price == 0) return true;
        return saveManager.isSkinOwned(skin.id);
    }

    public boolean purchaseSkin(BallSkin skin) {
        if (isSkinOwned(skin)) return false;
        if (saveManager.spendCoins(skin.price)) {
            saveManager.setSkinOwned(skin.id);
            return true;
        }
        return false;
    }

    public BallSkin getEquippedSkin() {
        String equippedId = saveManager.getEquippedSkin();
        for (BallSkin s : BallSkin.values()) {
            if (s.id.equals(equippedId)) return s;
        }
        return BallSkin.CLASSIC;
    }

    public void equipSkin(BallSkin skin) {
        if (isSkinOwned(skin)) saveManager.setEquippedSkin(skin.id);
    }

    // ── PİM YÖNETİMİ ──
    public boolean isPinSkinOwned(PinSkin skin) {
        if (skin.price == 0) return true;
        return saveManager.isSkinOwned(skin.id);
    }

    public boolean purchasePinSkin(PinSkin skin) {
        if (isPinSkinOwned(skin)) return false;
        if (saveManager.spendCoins(skin.price)) {
            saveManager.setSkinOwned(skin.id);
            return true;
        }
        return false;
    }

    public PinSkin getEquippedPinSkin() {
        String equippedId = saveManager.getEquippedPinSkin();
        for (PinSkin s : PinSkin.values()) {
            if (s.id.equals(equippedId)) return s;
        }
        return PinSkin.CLASSIC;
    }

    public void equipPinSkin(PinSkin skin) {
        if (isPinSkinOwned(skin)) saveManager.setEquippedPinSkin(skin.id);
    }

    // ── KUYRUK İZİ YÖNETİMİ ──
    public boolean isTrailSkinOwned(TrailSkin skin) {
        if (skin.price == 0) return true;
        return saveManager.isSkinOwned(skin.id);
    }

    public boolean purchaseTrailSkin(TrailSkin skin) {
        if (isTrailSkinOwned(skin)) return false;
        if (saveManager.spendCoins(skin.price)) {
            saveManager.setSkinOwned(skin.id);
            return true;
        }
        return false;
    }

    public TrailSkin getEquippedTrailSkin() {
        String equippedId = saveManager.getEquippedTrailSkin();
        for (TrailSkin s : TrailSkin.values()) {
            if (s.id.equals(equippedId)) return s;
        }
        return TrailSkin.DEFAULT;
    }

    public void equipTrailSkin(TrailSkin skin) {
        if (isTrailSkinOwned(skin)) saveManager.setEquippedTrailSkin(skin.id);
    }
}
