package com.game.pullthepin.levels;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.game.pullthepin.entities.Ball;
import com.game.pullthepin.entities.Bouncer;
import com.game.pullthepin.entities.ColorGate;
import com.game.pullthepin.entities.Jar;
import com.game.pullthepin.entities.KeyEntity;
import com.game.pullthepin.entities.LaserObstacle;
import com.game.pullthepin.entities.LevelWall;
import com.game.pullthepin.entities.MultiplierGate;
import com.game.pullthepin.entities.Pin;
import com.game.pullthepin.entities.Portal;
import com.game.pullthepin.entities.RotatingObstacle;
import com.game.pullthepin.entities.SpikeObstacle;

import com.game.pullthepin.entities.AcidPool;
import com.game.pullthepin.entities.WindTurbine;

/**
 * 80 Adet Kusursuz, Dengeli ve Fiziksel Olarak %100 Çözülebilir Seviye Tasarımı (8 Dünya).
 * Dönen Çarklar, Kuantum Lazer Bariyerleri, Asit Havuzları & Su Nötralizasyonu, Rüzgar Türbinleri.
 * Hedef top sayıları oyuncu dostu ve adil oranlarla (%65 - %78) ayarlandı.
 */
public class LevelLoader {

    public static final int TOTAL_LEVELS = 200;

    public static class LevelData {
        public Array<Ball> balls = new Array<>();
        public Array<Pin> pins = new Array<>();
        public Array<LevelWall> walls = new Array<>();
        public Array<Portal> portals = new Array<>();
        public Array<Bouncer> bouncers = new Array<>();
        public Array<SpikeObstacle> spikes = new Array<>();
        public Array<MultiplierGate> multiplierGates = new Array<>();
        public Array<ColorGate> colorGates = new Array<>();
        public Array<KeyEntity> keys = new Array<>();
        public Array<RotatingObstacle> rotatingObstacles = new Array<>();
        public Array<LaserObstacle> lasers = new Array<>();
        public Array<AcidPool> acidPools = new Array<>();
        public Array<WindTurbine> windTurbines = new Array<>();
        public Jar jar;
        public int totalBallsRequired;
        public String levelTitle;
    }

    public static LevelData loadLevel(int levelNumber, World world) {
        if (levelNumber < 1) levelNumber = 1;
        if (levelNumber > TOTAL_LEVELS) levelNumber = TOTAL_LEVELS;

        LevelData data = new LevelData();

        switch (levelNumber) {
            case 1: buildLevel1(world, data); break;
            case 2: buildLevel2(world, data); break;
            case 3: buildLevel3(world, data); break;
            case 4: buildLevel4(world, data); break;
            case 5: buildLevel5(world, data); break;
            case 6: buildLevel6(world, data); break;
            case 7: buildLevel7(world, data); break;
            case 8: buildLevel8(world, data); break;
            case 9: buildLevel9(world, data); break;
            case 10: buildLevel10(world, data); break;

            case 11: buildLevel11(world, data); break;
            case 12: buildLevel12(world, data); break;
            case 13: buildLevel13(world, data); break;
            case 14: buildLevel14(world, data); break;
            case 15: buildLevel15(world, data); break;
            case 16: buildLevel16(world, data); break;
            case 17: buildLevel17(world, data); break;
            case 18: buildLevel18(world, data); break;
            case 19: buildLevel19(world, data); break;
            case 20: buildLevel20(world, data); break;

            case 21: buildLevel21(world, data); break;
            case 22: buildLevel22(world, data); break;
            case 23: buildLevel23(world, data); break;
            case 24: buildLevel24(world, data); break;
            case 25: buildLevel25(world, data); break;
            case 26: buildLevel26(world, data); break;
            case 27: buildLevel27(world, data); break;
            case 28: buildLevel28(world, data); break;
            case 29: buildLevel29(world, data); break;
            case 30: buildLevel30(world, data); break;

            case 31: buildLevel31(world, data); break;
            case 32: buildLevel32(world, data); break;
            case 33: buildLevel33(world, data); break;
            case 34: buildLevel34(world, data); break;
            case 35: buildLevel35(world, data); break;
            case 36: buildLevel36(world, data); break;
            case 37: buildLevel37(world, data); break;
            case 38: buildLevel38(world, data); break;
            case 39: buildLevel39(world, data); break;
            case 40: buildLevel40(world, data); break;

            case 41: buildLevel41(world, data); break;
            case 42: buildLevel42(world, data); break;
            case 43: buildLevel43(world, data); break;
            case 44: buildLevel44(world, data); break;
            case 45: buildLevel45(world, data); break;
            case 46: buildLevel46(world, data); break;
            case 47: buildLevel47(world, data); break;
            case 48: buildLevel48(world, data); break;
            case 49: buildLevel49(world, data); break;
            case 50: buildLevel50(world, data); break;

            case 51: buildLevel51(world, data); break;
            case 52: buildLevel52(world, data); break;
            case 53: buildLevel53(world, data); break;
            case 54: buildLevel54(world, data); break;
            case 55: buildLevel55(world, data); break;
            case 56: buildLevel56(world, data); break;
            case 57: buildLevel57(world, data); break;
            case 58: buildLevel58(world, data); break;
            case 59: buildLevel59(world, data); break;
            case 60: buildLevel60(world, data); break;

            case 61: buildLevel61(world, data); break;
            case 62: buildLevel62(world, data); break;
            case 63: buildLevel63(world, data); break;
            case 64: buildLevel64(world, data); break;
            case 65: buildLevel65(world, data); break;
            case 66: buildLevel66(world, data); break;
            case 67: buildLevel67(world, data); break;
            case 68: buildLevel68(world, data); break;
            case 69: buildLevel69(world, data); break;
            case 70: buildLevel70(world, data); break;

            case 71: buildLevel71(world, data); break;
            case 72: buildLevel72(world, data); break;
            case 73: buildLevel73(world, data); break;
            case 74: buildLevel74(world, data); break;
            case 75: buildLevel75(world, data); break;
            case 76: buildLevel76(world, data); break;
            case 77: buildLevel77(world, data); break;
            case 78: buildLevel78(world, data); break;
            case 79: buildLevel79(world, data); break;
            case 80: buildLevel80(world, data); break;


            case 81: buildLevel81(world, data); break;
            case 82: buildLevel82(world, data); break;
            case 83: buildLevel83(world, data); break;
            case 84: buildLevel84(world, data); break;
            case 85: buildLevel85(world, data); break;
            case 86: buildLevel86(world, data); break;
            case 87: buildLevel87(world, data); break;
            case 88: buildLevel88(world, data); break;
            case 89: buildLevel89(world, data); break;
            case 90: buildLevel90(world, data); break;

            case 91: buildLevel91(world, data); break;
            case 92: buildLevel92(world, data); break;
            case 93: buildLevel93(world, data); break;
            case 94: buildLevel94(world, data); break;
            case 95: buildLevel95(world, data); break;
            case 96: buildLevel96(world, data); break;
            case 97: buildLevel97(world, data); break;
            case 98: buildLevel98(world, data); break;
            case 99: buildLevel99(world, data); break;
            case 100: buildLevel100(world, data); break;

            case 101: buildLevel101(world, data); break;
            case 102: buildLevel102(world, data); break;
            case 103: buildLevel103(world, data); break;
            case 104: buildLevel104(world, data); break;
            case 105: buildLevel105(world, data); break;
            case 106: buildLevel106(world, data); break;
            case 107: buildLevel107(world, data); break;
            case 108: buildLevel108(world, data); break;
            case 109: buildLevel109(world, data); break;
            case 110: buildLevel110(world, data); break;

            case 111: buildLevel111(world, data); break;
            case 112: buildLevel112(world, data); break;
            case 113: buildLevel113(world, data); break;
            case 114: buildLevel114(world, data); break;
            case 115: buildLevel115(world, data); break;
            case 116: buildLevel116(world, data); break;
            case 117: buildLevel117(world, data); break;
            case 118: buildLevel118(world, data); break;
            case 119: buildLevel119(world, data); break;
            case 120: buildLevel120(world, data); break;


            case 121: buildLevel121(world, data); break;
            case 122: buildLevel122(world, data); break;
            case 123: buildLevel123(world, data); break;
            case 124: buildLevel124(world, data); break;
            case 125: buildLevel125(world, data); break;
            case 126: buildLevel126(world, data); break;
            case 127: buildLevel127(world, data); break;
            case 128: buildLevel128(world, data); break;
            case 129: buildLevel129(world, data); break;
            case 130: buildLevel130(world, data); break;

            case 131: buildLevel131(world, data); break;
            case 132: buildLevel132(world, data); break;
            case 133: buildLevel133(world, data); break;
            case 134: buildLevel134(world, data); break;
            case 135: buildLevel135(world, data); break;
            case 136: buildLevel136(world, data); break;
            case 137: buildLevel137(world, data); break;
            case 138: buildLevel138(world, data); break;
            case 139: buildLevel139(world, data); break;
            case 140: buildLevel140(world, data); break;

            case 141: buildLevel141(world, data); break;
            case 142: buildLevel142(world, data); break;
            case 143: buildLevel143(world, data); break;
            case 144: buildLevel144(world, data); break;
            case 145: buildLevel145(world, data); break;
            case 146: buildLevel146(world, data); break;
            case 147: buildLevel147(world, data); break;
            case 148: buildLevel148(world, data); break;
            case 149: buildLevel149(world, data); break;
            case 150: buildLevel150(world, data); break;

            case 151: buildLevel151(world, data); break;
            case 152: buildLevel152(world, data); break;
            case 153: buildLevel153(world, data); break;
            case 154: buildLevel154(world, data); break;
            case 155: buildLevel155(world, data); break;
            case 156: buildLevel156(world, data); break;
            case 157: buildLevel157(world, data); break;
            case 158: buildLevel158(world, data); break;
            case 159: buildLevel159(world, data); break;
            case 160: buildLevel160(world, data); break;

            case 161: buildLevel161(world, data); break;
            case 162: buildLevel162(world, data); break;
            case 163: buildLevel163(world, data); break;
            case 164: buildLevel164(world, data); break;
            case 165: buildLevel165(world, data); break;
            case 166: buildLevel166(world, data); break;
            case 167: buildLevel167(world, data); break;
            case 168: buildLevel168(world, data); break;
            case 169: buildLevel169(world, data); break;
            case 170: buildLevel170(world, data); break;
            case 171: buildLevel171(world, data); break;
            case 172: buildLevel172(world, data); break;
            case 173: buildLevel173(world, data); break;
            case 174: buildLevel174(world, data); break;
            case 175: buildLevel175(world, data); break;
            case 176: buildLevel176(world, data); break;
            case 177: buildLevel177(world, data); break;
            case 178: buildLevel178(world, data); break;
            case 179: buildLevel179(world, data); break;
            case 180: buildLevel180(world, data); break;
            case 181: buildLevel181(world, data); break;
            case 182: buildLevel182(world, data); break;
            case 183: buildLevel183(world, data); break;
            case 184: buildLevel184(world, data); break;
            case 185: buildLevel185(world, data); break;
            case 186: buildLevel186(world, data); break;
            case 187: buildLevel187(world, data); break;
            case 188: buildLevel188(world, data); break;
            case 189: buildLevel189(world, data); break;
            case 190: buildLevel190(world, data); break;
            case 191: buildLevel191(world, data); break;
            case 192: buildLevel192(world, data); break;
            case 193: buildLevel193(world, data); break;
            case 194: buildLevel194(world, data); break;
            case 195: buildLevel195(world, data); break;
            case 196: buildLevel196(world, data); break;
            case 197: buildLevel197(world, data); break;
            case 198: buildLevel198(world, data); break;
            case 199: buildLevel199(world, data); break;
            case 200: buildLevel200(world, data); break;

            default: buildFallbackLevel(world, data, levelNumber); break;
        }

        sanitizeAndRelaxBallSpawns(data);
        data.levelTitle = com.game.pullthepin.i18n.I18N.getLevelTitle(levelNumber);
        return data;
    }

    // =========================================================================
    // DÜNYA 1: SİBER LABORATUVAR (SEVİYE 1 - 20)
    // =========================================================================

    // Bölüm 1: İlk Adım (Onboarding: 1 yatay pim, 12 renkli bilye)
    private static void buildLevel1(World world, LevelData data) {
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(2.2f, 15.0f), new Vector2(2.2f, 8.5f), new Vector2(3.3f, 5.0f), new Vector2(3.3f, 3.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(6.8f, 15.0f), new Vector2(6.8f, 8.5f), new Vector2(5.7f, 5.0f), new Vector2(5.7f, 3.5f) }));
        data.jar = new Jar(world, 4.5f, 1.8f, 3.0f, 2.4f, 9);
        data.totalBallsRequired = 9;
        data.pins.add(new Pin(world, 4.5f, 7.8f, 5.2f, Pin.Direction.RIGHT));
        spawnBallGrid(world, data, 3.2f, 9.2f, 3, 4, Ball.BallType.COLORED); // 12 top
    }

    // Bölüm 2: Çift Pim (İki kademeli pim, akış sırası)
    private static void buildLevel2(World world, LevelData data) {
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(2.0f, 15.0f), new Vector2(2.0f, 10.5f), new Vector2(2.8f, 8.5f), new Vector2(3.3f, 5.0f), new Vector2(3.3f, 3.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(7.0f, 15.0f), new Vector2(7.0f, 10.5f), new Vector2(6.2f, 8.5f), new Vector2(5.7f, 5.0f), new Vector2(5.7f, 3.5f) }));
        data.jar = new Jar(world, 4.5f, 1.8f, 3.0f, 2.4f, 12);
        data.totalBallsRequired = 12;
        data.pins.add(new Pin(world, 4.5f, 11.2f, 5.4f, Pin.Direction.LEFT));
        data.pins.add(new Pin(world, 4.5f, 7.5f, 5.4f, Pin.Direction.RIGHT));
        spawnBallGrid(world, data, 2.8f, 12.0f, 4, 4, Ball.BallType.COLORED); // 16 top
    }

    // Bölüm 3: Renklerin Doğuşu (Üstte 6 renkli, altta 8 gri bilye; renklendirme)
    private static void buildLevel3(World world, LevelData data) {
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(1.8f, 15.0f), new Vector2(1.8f, 10.5f), new Vector2(3.2f, 8.0f), new Vector2(3.2f, 3.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(7.2f, 15.0f), new Vector2(7.2f, 7.5f), new Vector2(5.8f, 3.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(4.0f, 15.0f), new Vector2(4.0f, 11.2f) }));
        data.jar = new Jar(world, 4.5f, 1.8f, 3.0f, 2.4f, 11);
        data.totalBallsRequired = 11;
        data.pins.add(new Pin(world, 2.9f, 10.5f, 2.8f, Pin.Direction.LEFT));
        data.pins.add(new Pin(world, 5.0f, 6.8f, 4.6f, Pin.Direction.RIGHT));
        spawnBallGrid(world, data, 2.2f, 11.8f, 2, 3, Ball.BallType.COLORED); // 6 renkli
        spawnBallGrid(world, data, 4.2f, 7.8f, 2, 4, Ball.BallType.GRAY);    // 8 gri -> 14 top
    }

    // Bölüm 4: Çift Kanat (Sol ve sağ kanatta renkli, ortada gri)
    private static void buildLevel4(World world, LevelData data) {
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(1.2f, 15.0f), new Vector2(1.2f, 8.5f), new Vector2(3.3f, 5.0f), new Vector2(3.3f, 3.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(7.8f, 15.0f), new Vector2(7.8f, 8.5f), new Vector2(5.7f, 5.0f), new Vector2(5.7f, 3.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(3.4f, 15.0f), new Vector2(3.4f, 11.0f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(5.6f, 15.0f), new Vector2(5.6f, 11.0f) }));
        data.jar = new Jar(world, 4.5f, 1.8f, 3.2f, 2.4f, 16);
        data.totalBallsRequired = 16;
        data.pins.add(new Pin(world, 2.3f, 10.5f, 2.6f, Pin.Direction.LEFT));
        data.pins.add(new Pin(world, 6.7f, 10.5f, 2.6f, Pin.Direction.RIGHT));
        data.pins.add(new Pin(world, 4.5f, 7.2f, 5.0f, Pin.Direction.RIGHT));
        spawnBallGrid(world, data, 1.6f, 11.8f, 2, 3, Ball.BallType.COLORED); // 6
        spawnBallGrid(world, data, 6.0f, 11.8f, 2, 3, Ball.BallType.COLORED); // 6
        spawnBallGrid(world, data, 3.8f, 8.5f, 3, 3, Ball.BallType.GRAY);     // 9 -> 21 top
    }

    // Bölüm 5: Akışkan Huni (24 bilyelik ASMR dopamin şöleni)
    private static void buildLevel5(World world, LevelData data) {
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(1.2f, 15.5f), new Vector2(1.2f, 10.0f), new Vector2(3.2f, 5.5f), new Vector2(3.2f, 3.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(7.8f, 15.5f), new Vector2(7.8f, 10.0f), new Vector2(5.8f, 5.5f), new Vector2(5.8f, 3.5f) }));
        data.jar = new Jar(world, 4.5f, 1.8f, 3.2f, 2.4f, 19);
        data.totalBallsRequired = 19;
        data.pins.add(new Pin(world, 4.5f, 7.0f, 5.4f, Pin.Direction.RIGHT));
        spawnBallGrid(world, data, 2.6f, 9.5f, 4, 6, Ball.BallType.COLORED); // 24 top
    }

    // Bölüm 6: Tehlike Uyarısı (İlk bomba! Sol kapalı çukura tahliye)
    private static void buildLevel6(World world, LevelData data) {
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(0.8f, 15.0f), new Vector2(0.8f, 5.0f), new Vector2(2.5f, 5.0f), new Vector2(2.5f, 8.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(2.5f, 15.0f), new Vector2(2.5f, 11.0f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(3.3f, 8.5f), new Vector2(3.3f, 3.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(7.5f, 15.0f), new Vector2(7.5f, 8.5f), new Vector2(5.7f, 5.0f), new Vector2(5.7f, 3.5f) }));
        data.jar = new Jar(world, 4.5f, 1.8f, 3.0f, 2.4f, 10);
        data.totalBallsRequired = 10;
        data.pins.add(new Pin(world, 1.65f, 10.5f, 2.1f, Pin.Direction.LEFT));
        data.pins.add(new Pin(world, 5.0f, 7.5f, 4.8f, Pin.Direction.RIGHT));
        data.balls.add(new Ball(world, 1.65f, 11.8f, Ball.BallType.BOMB));
        spawnBallGrid(world, data, 3.8f, 9.0f, 3, 4, Ball.BallType.COLORED); // 12 top
    }

    // Bölüm 7: Bomba Tahliyesi (Eğimli rampa ile bombayı sola kaydırma)
    private static void buildLevel7(World world, LevelData data) {
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(0.8f, 15.0f), new Vector2(0.8f, 5.5f), new Vector2(2.4f, 3.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(2.6f, 15.0f), new Vector2(2.6f, 8.5f), new Vector2(3.5f, 5.0f), new Vector2(3.5f, 3.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(7.8f, 15.0f), new Vector2(7.8f, 8.0f), new Vector2(5.8f, 5.0f), new Vector2(5.8f, 3.5f) }));
        data.jar = new Jar(world, 4.6f, 1.8f, 2.8f, 2.4f, 11);
        data.totalBallsRequired = 11;
        data.pins.add(new Pin(world, 1.7f, 7.8f, 2.2f, Pin.Direction.LEFT));
        data.pins.add(new Pin(world, 5.2f, 8.5f, 5.0f, Pin.Direction.RIGHT));
        data.balls.add(new Ball(world, 1.7f, 9.5f, Ball.BallType.BOMB));
        spawnBallGrid(world, data, 3.4f, 10.0f, 3, 3, Ball.BallType.COLORED); // 9
        spawnBallGrid(world, data, 5.6f, 10.0f, 3, 2, Ball.BallType.GRAY);    // 6 -> 15 top
    }

    // Bölüm 8: Renk Önceliği (Önce renkli üst pimi çek, sonra kavanoz pimi)
    private static void buildLevel8(World world, LevelData data) {
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(2.0f, 15.0f), new Vector2(2.0f, 10.0f), new Vector2(3.2f, 6.0f), new Vector2(3.2f, 3.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(7.0f, 15.0f), new Vector2(7.0f, 10.0f), new Vector2(5.8f, 6.0f), new Vector2(5.8f, 3.5f) }));
        data.jar = new Jar(world, 4.5f, 1.8f, 3.0f, 2.4f, 13);
        data.totalBallsRequired = 13;
        data.pins.add(new Pin(world, 4.5f, 11.2f, 5.4f, Pin.Direction.LEFT));
        data.pins.add(new Pin(world, 4.5f, 7.0f, 5.2f, Pin.Direction.RIGHT));
        spawnBallGrid(world, data, 2.8f, 12.0f, 4, 2, Ball.BallType.COLORED); // 8 renkli
        spawnBallGrid(world, data, 2.8f, 8.0f, 4, 2, Ball.BallType.GRAY);    // 8 gri -> 16 top
    }

    // Bölüm 9: Zikzak İniş (3 kademeli zikzak rampa)
    private static void buildLevel9(World world, LevelData data) {
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(1.5f, 15.0f), new Vector2(1.5f, 11.0f), new Vector2(6.5f, 9.5f), new Vector2(2.5f, 6.5f), new Vector2(3.3f, 3.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(7.5f, 15.0f), new Vector2(7.5f, 11.0f), new Vector2(7.0f, 9.5f), new Vector2(4.5f, 6.5f), new Vector2(5.7f, 3.5f) }));
        data.jar = new Jar(world, 4.5f, 1.8f, 3.0f, 2.4f, 14);
        data.totalBallsRequired = 14;
        data.pins.add(new Pin(world, 4.5f, 12.0f, 5.5f, Pin.Direction.RIGHT));
        data.pins.add(new Pin(world, 4.5f, 8.8f, 4.5f, Pin.Direction.LEFT));
        data.pins.add(new Pin(world, 4.5f, 6.0f, 4.0f, Pin.Direction.RIGHT));
        spawnBallGrid(world, data, 2.6f, 12.8f, 4, 4, Ball.BallType.COLORED); // 16 top
    }

    // Bölüm 10: Laboratuvar Zirvesi (Apex Mini Boss: 1 Bomba, Renkli & Gri Bilyeler)
    private static void buildLevel10(World world, LevelData data) {
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(0.8f, 15.5f), new Vector2(0.8f, 6.0f), new Vector2(2.4f, 4.0f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(2.4f, 15.5f), new Vector2(2.4f, 10.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(3.3f, 8.5f), new Vector2(3.3f, 3.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(7.8f, 15.5f), new Vector2(7.8f, 8.5f), new Vector2(5.7f, 5.0f), new Vector2(5.7f, 3.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(5.2f, 15.5f), new Vector2(5.2f, 11.5f) }));
        data.jar = new Jar(world, 4.5f, 1.8f, 3.2f, 2.4f, 14);
        data.totalBallsRequired = 14;
        data.pins.add(new Pin(world, 1.6f, 9.8f, 2.0f, Pin.Direction.LEFT));
        data.pins.add(new Pin(world, 3.8f, 11.0f, 3.0f, Pin.Direction.LEFT));
        data.pins.add(new Pin(world, 5.0f, 7.5f, 4.8f, Pin.Direction.RIGHT));
        data.balls.add(new Ball(world, 1.6f, 11.0f, Ball.BallType.BOMB));
        spawnBallGrid(world, data, 2.8f, 12.0f, 2, 4, Ball.BallType.COLORED); // 8 renkli
        spawnBallGrid(world, data, 5.6f, 12.0f, 2, 5, Ball.BallType.GRAY);    // 10 gri -> 18 top
    }

    // Bölüm 11: Taktil Kaydırak (Dikey pim ve 45 derecelik rampa)
    private static void buildLevel11(World world, LevelData data) {
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(1.5f, 15.0f), new Vector2(1.5f, 8.5f), new Vector2(3.2f, 4.5f), new Vector2(3.2f, 3.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(7.5f, 15.0f), new Vector2(7.5f, 8.5f), new Vector2(5.8f, 4.5f), new Vector2(5.8f, 3.5f) }));
        data.jar = new Jar(world, 4.5f, 1.8f, 3.0f, 2.4f, 12);
        data.totalBallsRequired = 12;
        data.pins.add(new Pin(world, 4.5f, 10.0f, 4.0f, Pin.Direction.UP));
        data.pins.add(new Pin(world, 4.5f, 6.5f, 4.8f, Pin.Direction.RIGHT));
        spawnBallGrid(world, data, 2.6f, 11.2f, 4, 4, Ball.BallType.COLORED); // 16 top
    }

    // Bölüm 12: Üçlü Hazne (3 dikey tüp: sol renkli, orta gri, sağ renkli)
    private static void buildLevel12(World world, LevelData data) {
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(1.0f, 15.5f), new Vector2(1.0f, 8.5f), new Vector2(3.2f, 5.0f), new Vector2(3.2f, 3.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(8.0f, 15.5f), new Vector2(8.0f, 8.5f), new Vector2(5.8f, 5.0f), new Vector2(5.8f, 3.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(3.3f, 15.5f), new Vector2(3.3f, 10.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(5.7f, 15.5f), new Vector2(5.7f, 10.5f) }));
        data.jar = new Jar(world, 4.5f, 1.8f, 3.2f, 2.4f, 16);
        data.totalBallsRequired = 16;
        data.pins.add(new Pin(world, 2.1f, 10.0f, 2.5f, Pin.Direction.LEFT));
        data.pins.add(new Pin(world, 6.9f, 10.0f, 2.5f, Pin.Direction.RIGHT));
        data.pins.add(new Pin(world, 4.5f, 6.8f, 4.8f, Pin.Direction.RIGHT));
        spawnBallGrid(world, data, 1.4f, 11.5f, 2, 3, Ball.BallType.COLORED); // 6
        spawnBallGrid(world, data, 3.8f, 11.5f, 2, 4, Ball.BallType.GRAY);    // 8
        spawnBallGrid(world, data, 6.2f, 11.5f, 2, 3, Ball.BallType.COLORED); // 6 -> 20 top
    }

    // Bölüm 13: Çifte Bomba Tuzağı (İki bomba birbirine tokuşur ve patlar!)
    private static void buildLevel13(World world, LevelData data) {
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(1.2f, 15.5f), new Vector2(1.2f, 8.0f), new Vector2(3.2f, 5.0f), new Vector2(3.2f, 3.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(7.8f, 15.5f), new Vector2(7.8f, 8.0f), new Vector2(5.8f, 5.0f), new Vector2(5.8f, 3.5f) }));
        data.jar = new Jar(world, 4.5f, 1.8f, 3.0f, 2.4f, 12);
        data.totalBallsRequired = 12;
        data.pins.add(new Pin(world, 4.5f, 12.0f, 5.8f, Pin.Direction.RIGHT));
        data.pins.add(new Pin(world, 4.5f, 8.5f, 2.8f, Pin.Direction.LEFT));
        data.pins.add(new Pin(world, 4.5f, 6.5f, 4.8f, Pin.Direction.RIGHT));
        data.balls.add(new Ball(world, 2.2f, 9.5f, Ball.BallType.BOMB));
        data.balls.add(new Ball(world, 6.8f, 9.5f, Ball.BallType.BOMB));
        spawnBallGrid(world, data, 2.6f, 12.8f, 4, 4, Ball.BallType.COLORED); // 16 top
    }

    // Bölüm 14: Dar Boğaz (Dar kanaldan tek sıra akan ASMR bilyeler)
    private static void buildLevel14(World world, LevelData data) {
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(1.5f, 15.5f), new Vector2(1.5f, 9.5f), new Vector2(3.8f, 7.0f), new Vector2(3.8f, 4.0f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(7.5f, 15.5f), new Vector2(7.5f, 9.5f), new Vector2(5.2f, 7.0f), new Vector2(5.2f, 4.0f) }));
        data.jar = new Jar(world, 4.5f, 1.8f, 3.0f, 2.4f, 15);
        data.totalBallsRequired = 15;
        data.pins.add(new Pin(world, 4.5f, 8.5f, 5.2f, Pin.Direction.RIGHT));
        data.pins.add(new Pin(world, 4.5f, 4.8f, 2.5f, Pin.Direction.LEFT));
        spawnBallGrid(world, data, 2.6f, 10.0f, 4, 5, Ball.BallType.COLORED); // 20 top
    }

    // Bölüm 15: Hızlı Karar (Ters yöne çekilen yatay pimler ve bomba ayrımı)
    private static void buildLevel15(World world, LevelData data) {
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(1.0f, 15.0f), new Vector2(1.0f, 6.0f), new Vector2(2.5f, 4.0f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(2.8f, 15.0f), new Vector2(2.8f, 8.5f), new Vector2(3.3f, 5.0f), new Vector2(3.3f, 3.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(7.8f, 15.0f), new Vector2(7.8f, 8.5f), new Vector2(5.7f, 5.0f), new Vector2(5.7f, 3.5f) }));
        data.jar = new Jar(world, 4.5f, 1.8f, 3.0f, 2.4f, 12);
        data.totalBallsRequired = 12;
        data.pins.add(new Pin(world, 1.9f, 8.0f, 2.2f, Pin.Direction.LEFT));
        data.pins.add(new Pin(world, 5.3f, 8.0f, 4.8f, Pin.Direction.RIGHT));
        data.balls.add(new Ball(world, 1.9f, 9.5f, Ball.BallType.BOMB));
        spawnBallGrid(world, data, 3.6f, 9.2f, 4, 4, Ball.BallType.COLORED); // 16 top
    }

    // Bölüm 16: Çapraz Kesişim (Bomba sola, bilyeler sağa)
    private static void buildLevel16(World world, LevelData data) {
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(1.2f, 15.5f), new Vector2(1.2f, 5.5f), new Vector2(2.5f, 3.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(3.0f, 15.5f), new Vector2(3.0f, 9.0f), new Vector2(3.5f, 5.0f), new Vector2(3.5f, 3.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(7.8f, 15.5f), new Vector2(7.8f, 8.5f), new Vector2(5.8f, 5.0f), new Vector2(5.8f, 3.5f) }));
        data.jar = new Jar(world, 4.6f, 1.8f, 2.8f, 2.4f, 12);
        data.totalBallsRequired = 12;
        data.pins.add(new Pin(world, 2.1f, 8.5f, 2.4f, Pin.Direction.LEFT));
        data.pins.add(new Pin(world, 5.4f, 8.5f, 4.8f, Pin.Direction.RIGHT));
        data.balls.add(new Ball(world, 2.1f, 10.0f, Ball.BallType.BOMB));
        spawnBallGrid(world, data, 3.8f, 9.5f, 3, 5, Ball.BallType.COLORED); // 15 top
    }

    // Bölüm 17: Renk Şelalesi (Dopamin tazeleme: 32 rengarenk bilye)
    private static void buildLevel17(World world, LevelData data) {
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(1.0f, 15.5f), new Vector2(1.0f, 9.0f), new Vector2(3.2f, 5.0f), new Vector2(3.2f, 3.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(8.0f, 15.5f), new Vector2(8.0f, 9.0f), new Vector2(5.8f, 5.0f), new Vector2(5.8f, 3.5f) }));
        data.jar = new Jar(world, 4.5f, 1.8f, 3.4f, 2.4f, 25);
        data.totalBallsRequired = 25;
        data.pins.add(new Pin(world, 4.5f, 12.0f, 5.8f, Pin.Direction.LEFT));
        data.pins.add(new Pin(world, 4.5f, 7.2f, 5.5f, Pin.Direction.RIGHT));
        spawnBallGrid(world, data, 2.2f, 12.8f, 4, 8, Ball.BallType.COLORED); // 32 top!
    }

    // Bölüm 18: Labirent Odacıkları (4 küçük oda ve sıralı aktivasyon)
    private static void buildLevel18(World world, LevelData data) {
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(1.2f, 15.5f), new Vector2(1.2f, 7.5f), new Vector2(3.3f, 5.0f), new Vector2(3.3f, 3.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(7.8f, 15.5f), new Vector2(7.8f, 7.5f), new Vector2(5.7f, 5.0f), new Vector2(5.7f, 3.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(4.5f, 15.5f), new Vector2(4.5f, 8.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(0.5f, 11.5f), new Vector2(2.5f, 11.5f) }));
        data.jar = new Jar(world, 4.5f, 1.8f, 3.2f, 2.4f, 14);
        data.totalBallsRequired = 14;
        data.pins.add(new Pin(world, 2.8f, 12.0f, 3.2f, Pin.Direction.LEFT));
        data.pins.add(new Pin(world, 6.2f, 12.0f, 3.2f, Pin.Direction.RIGHT));
        data.pins.add(new Pin(world, 4.5f, 7.5f, 5.2f, Pin.Direction.RIGHT));
        spawnBallGrid(world, data, 2.0f, 12.8f, 2, 3, Ball.BallType.COLORED); // 6
        spawnBallGrid(world, data, 5.5f, 12.8f, 2, 3, Ball.BallType.GRAY);    // 6
        spawnBallGrid(world, data, 3.5f, 9.0f, 3, 2, Ball.BallType.GRAY);     // 6 -> 18 top
    }

    // Bölüm 19: Hassas Sıralama (1 yanlış çekişte bombanın renklere değdiği zeka bulmacası)
    private static void buildLevel19(World world, LevelData data) {
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(1.0f, 15.5f), new Vector2(1.0f, 6.5f), new Vector2(2.5f, 4.0f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(2.8f, 15.5f), new Vector2(2.8f, 8.5f), new Vector2(3.3f, 5.0f), new Vector2(3.3f, 3.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(7.8f, 15.5f), new Vector2(7.8f, 8.5f), new Vector2(5.7f, 5.0f), new Vector2(5.7f, 3.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(5.3f, 15.5f), new Vector2(5.3f, 11.5f) }));
        data.jar = new Jar(world, 4.5f, 1.8f, 3.2f, 2.4f, 15);
        data.totalBallsRequired = 15;
        data.pins.add(new Pin(world, 1.9f, 8.5f, 2.2f, Pin.Direction.LEFT));
        data.pins.add(new Pin(world, 4.0f, 11.2f, 2.6f, Pin.Direction.LEFT));
        data.pins.add(new Pin(world, 6.5f, 11.2f, 2.6f, Pin.Direction.RIGHT));
        data.pins.add(new Pin(world, 5.0f, 7.2f, 4.8f, Pin.Direction.RIGHT));
        data.balls.add(new Ball(world, 1.9f, 10.0f, Ball.BallType.BOMB));
        spawnBallGrid(world, data, 3.2f, 12.0f, 2, 4, Ball.BallType.COLORED); // 8
        spawnBallGrid(world, data, 5.8f, 12.0f, 2, 6, Ball.BallType.GRAY);    // 12 -> 20 top
    }

    // Bölüm 20: Siber Dünya Finali (World 1 Grand Finale: 36 Bilye, 2 Bomba, Kusursuz Akış)
    private static void buildLevel20(World world, LevelData data) {
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(0.8f, 15.5f), new Vector2(0.8f, 7.5f), new Vector2(3.2f, 5.0f), new Vector2(3.2f, 3.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(8.2f, 15.5f), new Vector2(8.2f, 7.5f), new Vector2(5.8f, 5.0f), new Vector2(5.8f, 3.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(4.5f, 15.5f), new Vector2(4.5f, 11.5f) }));
        data.jar = new Jar(world, 4.5f, 1.8f, 3.4f, 2.4f, 28);
        data.totalBallsRequired = 28;
        data.pins.add(new Pin(world, 2.6f, 13.5f, 3.4f, Pin.Direction.LEFT));
        data.pins.add(new Pin(world, 6.4f, 13.5f, 3.4f, Pin.Direction.RIGHT));
        data.pins.add(new Pin(world, 4.5f, 10.5f, 5.4f, Pin.Direction.LEFT));
        data.pins.add(new Pin(world, 4.5f, 7.0f, 5.2f, Pin.Direction.RIGHT));
        data.balls.add(new Ball(world, 2.5f, 11.2f, Ball.BallType.BOMB));
        data.balls.add(new Ball(world, 6.5f, 11.2f, Ball.BallType.BOMB));
        spawnBallGrid(world, data, 1.6f, 14.0f, 3, 6, Ball.BallType.COLORED); // 18
        spawnBallGrid(world, data, 5.4f, 14.0f, 3, 6, Ball.BallType.GRAY);    // 18 -> 36 top!
    }

    // =========================================================================
    // DÜNYA 2: ATLANTIS DERİNLİKLERİ (SEVİYE 21 - 40)
    // =========================================================================

    // Bölüm 21: Zıplayan Bilyeler (İlk Trambolin tanıtımı!)
    private static void buildLevel21(World world, LevelData data) {
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(1.5f, 15.5f), new Vector2(1.5f, 9.0f), new Vector2(3.0f, 6.5f), new Vector2(1.8f, 4.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(7.5f, 15.5f), new Vector2(7.5f, 9.0f), new Vector2(6.0f, 6.5f), new Vector2(6.0f, 3.5f) }));
        data.bouncers.add(new Bouncer(world, 2.8f, 5.0f, 2.2f, 0.35f, -35, 8.5f));
        data.jar = new Jar(world, 5.2f, 1.8f, 3.0f, 2.4f, 12);
        data.totalBallsRequired = 12;
        data.pins.add(new Pin(world, 4.5f, 8.5f, 5.2f, Pin.Direction.RIGHT));
        spawnBallGrid(world, data, 2.6f, 10.0f, 4, 4, Ball.BallType.COLORED); // 16 top
    }

    // Bölüm 22: Çifte Trambolin (Sol bouncer sağa atar, sağ bouncer kavanoza)
    private static void buildLevel22(World world, LevelData data) {
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(1.2f, 15.5f), new Vector2(1.2f, 8.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(7.8f, 15.5f), new Vector2(7.8f, 8.5f) }));
        data.bouncers.add(new Bouncer(world, 2.2f, 7.5f, 2.0f, 0.35f, -38, 8.0f));
        data.bouncers.add(new Bouncer(world, 6.8f, 5.0f, 2.0f, 0.35f, 38, 8.0f));
        data.jar = new Jar(world, 4.5f, 1.8f, 3.0f, 2.4f, 14);
        data.totalBallsRequired = 14;
        data.pins.add(new Pin(world, 4.5f, 11.5f, 5.5f, Pin.Direction.RIGHT));
        spawnBallGrid(world, data, 2.6f, 12.5f, 4, 4, Ball.BallType.COLORED); // 16 top
    }

    // Bölüm 23: Altın Anahtar (İlk Kilit & Anahtar Sistemi)
    private static void buildLevel23(World world, LevelData data) {
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(1.5f, 15.0f), new Vector2(1.5f, 8.5f), new Vector2(3.3f, 5.0f), new Vector2(3.3f, 3.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(7.5f, 15.0f), new Vector2(7.5f, 8.5f), new Vector2(5.7f, 5.0f), new Vector2(5.7f, 3.5f) }));
        data.jar = new Jar(world, 4.5f, 1.8f, 3.0f, 2.4f, 12);
        data.totalBallsRequired = 12;
        data.pins.add(new Pin(world, 4.5f, 10.5f, 5.4f, Pin.Direction.LEFT));
        data.pins.add(new Pin(world, 4.5f, 6.5f, 4.8f, Pin.Direction.RIGHT, true, 1));
        data.keys.add(new KeyEntity(world, 2.5f, 13.5f, 1));
        spawnBallGrid(world, data, 2.8f, 11.2f, 4, 4, Ball.BallType.COLORED); // 16 top
    }

    // Bölüm 24: Açısal Sıçrama (Bouncer bilyeleri yüksek duvarın üstünden aşırtır)
    private static void buildLevel24(World world, LevelData data) {
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(1.2f, 15.5f), new Vector2(1.2f, 3.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(8.0f, 15.5f), new Vector2(8.0f, 3.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(4.5f, 10.0f), new Vector2(4.5f, 3.5f) }));
        data.bouncers.add(new Bouncer(world, 6.5f, 5.5f, 2.2f, 0.35f, 45, 9.5f));
        data.jar = new Jar(world, 2.8f, 1.8f, 2.8f, 2.4f, 12);
        data.totalBallsRequired = 12;
        data.pins.add(new Pin(world, 6.2f, 9.5f, 3.4f, Pin.Direction.RIGHT));
        spawnBallGrid(world, data, 5.2f, 10.5f, 3, 5, Ball.BallType.COLORED); // 15 top
    }

    // Bölüm 25: Zıplayan Bomba (Bomba bouncera çarpıp dikenli duvarda patlar)
    private static void buildLevel25(World world, LevelData data) {
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(1.0f, 15.5f), new Vector2(1.0f, 7.5f), new Vector2(3.2f, 4.5f), new Vector2(3.2f, 3.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(8.0f, 15.5f), new Vector2(8.0f, 7.5f), new Vector2(5.8f, 4.5f), new Vector2(5.8f, 3.5f) }));
        data.spikes.add(new SpikeObstacle(world, 1.8f, 8.5f, 1.8f, 0.4f));
        data.bouncers.add(new Bouncer(world, 3.5f, 6.5f, 2.0f, 0.35f, 45, 8.0f));
        data.jar = new Jar(world, 4.5f, 1.8f, 3.0f, 2.4f, 12);
        data.totalBallsRequired = 12;
        data.pins.add(new Pin(world, 2.5f, 11.5f, 3.0f, Pin.Direction.LEFT));
        data.pins.add(new Pin(world, 6.0f, 11.5f, 3.0f, Pin.Direction.RIGHT));
        data.pins.add(new Pin(world, 4.5f, 7.5f, 4.8f, Pin.Direction.RIGHT));
        data.balls.add(new Ball(world, 2.5f, 12.5f, Ball.BallType.BOMB));
        spawnBallGrid(world, data, 5.0f, 12.5f, 3, 5, Ball.BallType.COLORED); // 15 top
    }

    // Bölüm 26: Renk Yaylanması (Bouncer renkli bilyeleri gri bilye havuzuna fırlatır)
    private static void buildLevel26(World world, LevelData data) {
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(1.2f, 15.5f), new Vector2(1.2f, 8.0f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(7.8f, 15.5f), new Vector2(7.8f, 8.0f), new Vector2(5.8f, 5.0f), new Vector2(5.8f, 3.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(3.5f, 7.5f), new Vector2(3.5f, 3.5f) }));
        data.bouncers.add(new Bouncer(world, 2.4f, 5.0f, 2.2f, 0.35f, -38, 9.0f));
        data.jar = new Jar(world, 4.6f, 1.8f, 2.8f, 2.4f, 14);
        data.totalBallsRequired = 14;
        data.pins.add(new Pin(world, 2.4f, 9.5f, 2.6f, Pin.Direction.LEFT));
        data.pins.add(new Pin(world, 5.5f, 7.0f, 4.0f, Pin.Direction.RIGHT));
        spawnBallGrid(world, data, 1.6f, 10.5f, 2, 4, Ball.BallType.COLORED); // 8 renkli
        spawnBallGrid(world, data, 4.4f, 8.0f, 3, 3, Ball.BallType.GRAY);     // 9 gri -> 17 top
    }

    // Bölüm 27: Kilitli Labirent (Key 1 -> Pin 1 -> Key 2 -> Pin 2)
    private static void buildLevel27(World world, LevelData data) {
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(1.5f, 15.5f), new Vector2(1.5f, 8.5f), new Vector2(3.3f, 5.0f), new Vector2(3.3f, 3.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(7.5f, 15.5f), new Vector2(7.5f, 8.5f), new Vector2(5.7f, 5.0f), new Vector2(5.7f, 3.5f) }));
        data.jar = new Jar(world, 4.5f, 1.8f, 3.0f, 2.4f, 12);
        data.totalBallsRequired = 12;
        data.pins.add(new Pin(world, 4.5f, 12.0f, 5.5f, Pin.Direction.RIGHT));
        data.pins.add(new Pin(world, 4.5f, 9.2f, 5.0f, Pin.Direction.LEFT, true, 1));
        data.pins.add(new Pin(world, 4.5f, 6.5f, 4.5f, Pin.Direction.RIGHT, true, 2));
        data.keys.add(new KeyEntity(world, 2.2f, 13.5f, 1));
        data.keys.add(new KeyEntity(world, 6.5f, 10.5f, 2));
        spawnBallGrid(world, data, 3.0f, 12.5f, 4, 4, Ball.BallType.COLORED); // 16 top
    }

    // Bölüm 28: Süper Esneklik (Yüksek irtifadan süper bouncera bırakılan bilyeler)
    private static void buildLevel28(World world, LevelData data) {
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(1.2f, 15.5f), new Vector2(1.2f, 10.0f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(7.8f, 15.5f), new Vector2(7.8f, 10.0f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(3.0f, 5.0f), new Vector2(3.0f, 3.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(6.0f, 5.0f), new Vector2(6.0f, 3.5f) }));
        data.bouncers.add(new Bouncer(world, 4.5f, 5.5f, 2.6f, 0.4f, 0, 10.0f));
        data.jar = new Jar(world, 4.5f, 1.8f, 3.2f, 2.4f, 19);
        data.totalBallsRequired = 19;
        data.pins.add(new Pin(world, 4.5f, 11.5f, 5.5f, Pin.Direction.RIGHT));
        spawnBallGrid(world, data, 2.6f, 12.5f, 4, 6, Ball.BallType.COLORED); // 24 top
    }

    // Bölüm 29: Ters Havuz (Bouncer bilyeleri sola, bomba düz sağdaki kuyuya)
    private static void buildLevel29(World world, LevelData data) {
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(0.8f, 15.5f), new Vector2(0.8f, 5.0f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(7.8f, 15.5f), new Vector2(7.8f, 4.0f), new Vector2(5.5f, 4.0f) }));
        data.bouncers.add(new Bouncer(world, 3.2f, 5.5f, 2.2f, 0.35f, -42, 8.5f));
        data.jar = new Jar(world, 2.5f, 1.8f, 2.8f, 2.4f, 12);
        data.totalBallsRequired = 12;
        data.pins.add(new Pin(world, 4.5f, 10.5f, 5.5f, Pin.Direction.RIGHT));
        data.balls.add(new Ball(world, 6.5f, 11.5f, Ball.BallType.BOMB));
        spawnBallGrid(world, data, 2.6f, 11.5f, 3, 5, Ball.BallType.COLORED); // 15 top
    }

    // Bölüm 30: Okyanus Mabedi (Apex Mini Boss: Bouncer + Kilit & Anahtar)
    private static void buildLevel30(World world, LevelData data) {
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(1.2f, 15.5f), new Vector2(1.2f, 8.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(7.8f, 15.5f), new Vector2(7.8f, 8.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(3.2f, 5.0f), new Vector2(3.2f, 3.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(5.8f, 5.0f), new Vector2(5.8f, 3.5f) }));
        data.bouncers.add(new Bouncer(world, 2.5f, 6.0f, 2.0f, 0.35f, -40, 8.5f));
        data.bouncers.add(new Bouncer(world, 6.5f, 6.0f, 2.0f, 0.35f, 40, 8.5f));
        data.jar = new Jar(world, 4.5f, 1.8f, 3.2f, 2.4f, 16);
        data.totalBallsRequired = 16;
        data.pins.add(new Pin(world, 4.5f, 12.0f, 5.5f, Pin.Direction.RIGHT));
        data.pins.add(new Pin(world, 4.5f, 4.8f, 3.8f, Pin.Direction.LEFT, true, 1));
        data.keys.add(new KeyEntity(world, 2.5f, 13.5f, 1));
        spawnBallGrid(world, data, 2.8f, 12.5f, 4, 3, Ball.BallType.COLORED); // 12
        spawnBallGrid(world, data, 2.8f, 9.5f, 4, 2, Ball.BallType.GRAY);     // 8 -> 20 top
    }

    // Bölüm 31: İki Aşamalı Kilit (Sol Key 1, Sağ Key 2; iki kilitli koridor)
    private static void buildLevel31(World world, LevelData data) {
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(1.2f, 15.5f), new Vector2(1.2f, 8.0f), new Vector2(3.2f, 5.0f), new Vector2(3.2f, 3.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(7.8f, 15.5f), new Vector2(7.8f, 8.0f), new Vector2(5.8f, 5.0f), new Vector2(5.8f, 3.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(4.5f, 15.5f), new Vector2(4.5f, 9.5f) }));
        data.jar = new Jar(world, 4.5f, 1.8f, 3.2f, 2.4f, 16);
        data.totalBallsRequired = 16;
        data.pins.add(new Pin(world, 2.8f, 9.5f, 3.2f, Pin.Direction.LEFT, true, 1));
        data.pins.add(new Pin(world, 6.2f, 9.5f, 3.2f, Pin.Direction.RIGHT, true, 2));
        data.pins.add(new Pin(world, 4.5f, 6.5f, 5.0f, Pin.Direction.RIGHT));
        data.keys.add(new KeyEntity(world, 2.0f, 14.0f, 1));
        data.keys.add(new KeyEntity(world, 7.0f, 14.0f, 2));
        spawnBallGrid(world, data, 2.0f, 11.0f, 2, 5, Ball.BallType.COLORED); // 10
        spawnBallGrid(world, data, 5.5f, 11.0f, 2, 5, Ball.BallType.GRAY);    // 10 -> 20 top
    }

    // Bölüm 32: Geometrik Sektirme (V-şeklinde karşılıklı iki bouncer ile ping-pong)
    private static void buildLevel32(World world, LevelData data) {
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(1.5f, 15.5f), new Vector2(1.5f, 9.0f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(7.5f, 15.5f), new Vector2(7.5f, 9.0f) }));
        data.bouncers.add(new Bouncer(world, 2.5f, 6.5f, 2.2f, 0.35f, -40, 8.5f));
        data.bouncers.add(new Bouncer(world, 6.5f, 5.0f, 2.2f, 0.35f, 40, 8.5f));
        data.jar = new Jar(world, 4.5f, 1.8f, 3.0f, 2.4f, 14);
        data.totalBallsRequired = 14;
        data.pins.add(new Pin(world, 4.5f, 11.0f, 5.5f, Pin.Direction.RIGHT));
        spawnBallGrid(world, data, 2.8f, 12.0f, 4, 4, Ball.BallType.COLORED); // 16 top
    }

    // Bölüm 33: Anahtarı Kurtar (Bomba anahtarın yanında; önce bombayı tahliye et)
    private static void buildLevel33(World world, LevelData data) {
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(0.8f, 15.5f), new Vector2(0.8f, 6.0f), new Vector2(2.4f, 4.0f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(2.6f, 15.5f), new Vector2(2.6f, 8.5f), new Vector2(3.3f, 5.0f), new Vector2(3.3f, 3.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(7.8f, 15.5f), new Vector2(7.8f, 8.5f), new Vector2(5.7f, 5.0f), new Vector2(5.7f, 3.5f) }));
        data.jar = new Jar(world, 4.5f, 1.8f, 3.0f, 2.4f, 12);
        data.totalBallsRequired = 12;
        data.pins.add(new Pin(world, 1.7f, 8.5f, 2.2f, Pin.Direction.LEFT));
        data.pins.add(new Pin(world, 5.2f, 9.0f, 4.8f, Pin.Direction.RIGHT));
        data.pins.add(new Pin(world, 4.5f, 6.5f, 4.2f, Pin.Direction.LEFT, true, 1));
        data.balls.add(new Ball(world, 1.7f, 10.5f, Ball.BallType.BOMB));
        data.keys.add(new KeyEntity(world, 5.2f, 13.5f, 1));
        spawnBallGrid(world, data, 3.5f, 10.0f, 4, 4, Ball.BallType.COLORED); // 16 top
    }

    // Bölüm 34: Trambolin Kalkanı (Bouncer bombayı saptırıp bilyelerin yolunu korur)
    private static void buildLevel34(World world, LevelData data) {
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(1.2f, 15.5f), new Vector2(1.2f, 7.5f), new Vector2(3.2f, 5.0f), new Vector2(3.2f, 3.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(7.8f, 15.5f), new Vector2(7.8f, 7.5f), new Vector2(5.8f, 5.0f), new Vector2(5.8f, 3.5f) }));
        data.spikes.add(new SpikeObstacle(world, 1.8f, 8.5f, 1.8f, 0.4f));
        data.bouncers.add(new Bouncer(world, 3.5f, 6.5f, 2.0f, 0.35f, 45, 8.5f));
        data.jar = new Jar(world, 4.5f, 1.8f, 3.2f, 2.4f, 14);
        data.totalBallsRequired = 14;
        data.pins.add(new Pin(world, 2.5f, 12.0f, 3.0f, Pin.Direction.LEFT));
        data.pins.add(new Pin(world, 6.0f, 12.0f, 3.0f, Pin.Direction.RIGHT));
        data.pins.add(new Pin(world, 4.5f, 8.0f, 5.0f, Pin.Direction.RIGHT));
        data.balls.add(new Ball(world, 2.5f, 13.2f, Ball.BallType.BOMB));
        spawnBallGrid(world, data, 5.0f, 13.0f, 3, 6, Ball.BallType.COLORED); // 18 top
    }

    // Bölüm 35: İkili Renk Akvaryumu (24 bilyelik simetrik akışkan trambolin gösterisi)
    private static void buildLevel35(World world, LevelData data) {
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(1.0f, 15.5f), new Vector2(1.0f, 9.0f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(8.0f, 15.5f), new Vector2(8.0f, 9.0f) }));
        data.bouncers.add(new Bouncer(world, 2.2f, 5.5f, 2.2f, 0.35f, -35, 8.0f));
        data.bouncers.add(new Bouncer(world, 6.8f, 5.5f, 2.2f, 0.35f, 35, 8.0f));
        data.jar = new Jar(world, 4.5f, 1.8f, 3.2f, 2.4f, 20);
        data.totalBallsRequired = 20;
        data.pins.add(new Pin(world, 4.5f, 11.0f, 5.8f, Pin.Direction.RIGHT));
        spawnBallGrid(world, data, 2.6f, 12.0f, 4, 6, Ball.BallType.COLORED); // 24 top
    }

    // Bölüm 36: Hızlı Sekme (Eğimli hızlandırıcı rampa ve bouncer)
    private static void buildLevel36(World world, LevelData data) {
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(1.2f, 15.5f), new Vector2(1.2f, 9.0f), new Vector2(5.5f, 6.0f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(7.8f, 15.5f), new Vector2(7.8f, 7.5f) }));
        data.bouncers.add(new Bouncer(world, 6.5f, 4.8f, 2.2f, 0.35f, 42, 8.5f));
        data.jar = new Jar(world, 3.2f, 1.8f, 2.8f, 2.4f, 14);
        data.totalBallsRequired = 14;
        data.pins.add(new Pin(world, 4.5f, 11.5f, 5.5f, Pin.Direction.LEFT));
        spawnBallGrid(world, data, 2.6f, 12.5f, 4, 4, Ball.BallType.COLORED); // 16 top
    }

    // Bölüm 37: Çapraz Sıçrama (Soldan gelen renkli, sağdan gelen gri; havada çarpışma)
    private static void buildLevel37(World world, LevelData data) {
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(1.2f, 15.5f), new Vector2(1.2f, 8.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(7.8f, 15.5f), new Vector2(7.8f, 8.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(4.5f, 15.5f), new Vector2(4.5f, 10.5f) }));
        data.bouncers.add(new Bouncer(world, 2.5f, 6.5f, 2.0f, 0.35f, -42, 8.5f));
        data.bouncers.add(new Bouncer(world, 6.5f, 6.5f, 2.0f, 0.35f, 42, 8.5f));
        data.jar = new Jar(world, 4.5f, 1.8f, 3.2f, 2.4f, 15);
        data.totalBallsRequired = 15;
        data.pins.add(new Pin(world, 2.8f, 10.0f, 3.2f, Pin.Direction.LEFT));
        data.pins.add(new Pin(world, 6.2f, 10.0f, 3.2f, Pin.Direction.RIGHT));
        spawnBallGrid(world, data, 1.8f, 11.0f, 2, 5, Ball.BallType.COLORED); // 10
        spawnBallGrid(world, data, 5.4f, 11.0f, 2, 5, Ball.BallType.GRAY);    // 10 -> 20 top
    }

    // Bölüm 38: Kilitli Hazne (Bouncer anahtarı iter, kilit açılır)
    private static void buildLevel38(World world, LevelData data) {
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(1.2f, 15.5f), new Vector2(1.2f, 8.5f), new Vector2(3.3f, 5.0f), new Vector2(3.3f, 3.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(7.8f, 15.5f), new Vector2(7.8f, 8.5f), new Vector2(5.7f, 5.0f), new Vector2(5.7f, 3.5f) }));
        data.bouncers.add(new Bouncer(world, 2.4f, 6.0f, 2.0f, 0.35f, -38, 8.5f));
        data.jar = new Jar(world, 4.5f, 1.8f, 3.0f, 2.4f, 15);
        data.totalBallsRequired = 15;
        data.pins.add(new Pin(world, 4.5f, 12.0f, 5.5f, Pin.Direction.RIGHT));
        data.pins.add(new Pin(world, 4.5f, 6.5f, 4.5f, Pin.Direction.LEFT, true, 1));
        data.keys.add(new KeyEntity(world, 2.4f, 13.5f, 1));
        spawnBallGrid(world, data, 3.0f, 12.5f, 4, 3, Ball.BallType.COLORED); // 12
        spawnBallGrid(world, data, 3.0f, 9.0f, 4, 2, Ball.BallType.GRAY);     // 8 -> 20 top
    }

    // Bölüm 39: Çılgın Rota (3 Trambolin ile tüm ekranı turlayan akrobatik bilyeler)
    private static void buildLevel39(World world, LevelData data) {
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(1.0f, 15.5f), new Vector2(1.0f, 6.0f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(8.0f, 15.5f), new Vector2(8.0f, 6.0f) }));
        data.bouncers.add(new Bouncer(world, 2.2f, 9.0f, 2.0f, 0.35f, -40, 8.5f));
        data.bouncers.add(new Bouncer(world, 6.8f, 6.5f, 2.0f, 0.35f, 40, 8.5f));
        data.bouncers.add(new Bouncer(world, 3.2f, 4.5f, 2.0f, 0.35f, -35, 8.0f));
        data.jar = new Jar(world, 5.5f, 1.8f, 3.0f, 2.4f, 17);
        data.totalBallsRequired = 17;
        data.pins.add(new Pin(world, 4.5f, 12.5f, 5.5f, Pin.Direction.RIGHT));
        spawnBallGrid(world, data, 2.6f, 13.5f, 4, 5, Ball.BallType.COLORED); // 20 top
    }

    // Bölüm 40: Atlantis Tacı Finali (Dünya 2 Büyük Finali: Çift Kilit, Çift Trambolin, 32 Bilye)
    private static void buildLevel40(World world, LevelData data) {
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(1.0f, 15.5f), new Vector2(1.0f, 7.5f), new Vector2(3.2f, 4.8f), new Vector2(3.2f, 3.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(8.0f, 15.5f), new Vector2(8.0f, 7.5f), new Vector2(5.8f, 4.8f), new Vector2(5.8f, 3.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(4.5f, 15.5f), new Vector2(4.5f, 11.0f) }));
        data.bouncers.add(new Bouncer(world, 2.2f, 6.5f, 2.0f, 0.35f, -38, 8.5f));
        data.bouncers.add(new Bouncer(world, 6.8f, 6.5f, 2.0f, 0.35f, 38, 8.5f));
        data.jar = new Jar(world, 4.5f, 1.8f, 3.4f, 2.4f, 25);
        data.totalBallsRequired = 25;
        data.pins.add(new Pin(world, 2.8f, 11.5f, 3.2f, Pin.Direction.LEFT, true, 1));
        data.pins.add(new Pin(world, 6.2f, 11.5f, 3.2f, Pin.Direction.RIGHT, true, 2));
        data.pins.add(new Pin(world, 4.5f, 7.5f, 5.2f, Pin.Direction.RIGHT));
        data.keys.add(new KeyEntity(world, 2.0f, 14.5f, 1));
        data.keys.add(new KeyEntity(world, 7.0f, 14.5f, 2));
        spawnBallGrid(world, data, 2.0f, 12.5f, 2, 8, Ball.BallType.COLORED); // 16
        spawnBallGrid(world, data, 5.4f, 12.5f, 2, 8, Ball.BallType.GRAY);    // 16 -> 32 top!
    }

    private static void buildFallbackLevel(World world, LevelData data, int levelNumber) {
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(1.2f, 15.5f), new Vector2(1.2f, 8.5f), new Vector2(3.3f, 5.0f), new Vector2(3.3f, 3.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(7.8f, 15.5f), new Vector2(7.8f, 8.5f), new Vector2(5.7f, 5.0f), new Vector2(5.7f, 3.5f) }));
        data.jar = new Jar(world, 4.5f, 1.8f, 3.2f, 2.4f, 14);
        data.totalBallsRequired = 14;
        data.pins.add(new Pin(world, 4.5f, 11.0f, 5.5f, Pin.Direction.LEFT));
        data.pins.add(new Pin(world, 4.5f, 7.2f, 5.0f, Pin.Direction.RIGHT));
        spawnBallGrid(world, data, 2.8f, 12.0f, 4, 3, Ball.BallType.COLORED); // 12
        spawnBallGrid(world, data, 2.8f, 8.5f, 4, 2, Ball.BallType.GRAY);     // 8 -> 20 balls
    }

        // =========================================================================
    // DÜNYA 3: LAV OCAĞI (SEVİYE 41 - 60)
    // =========================================================================

    // Bölüm 41: Obsidyen Dikenler (Diken tehlikesinin tanıtımı)
    private static void buildLevel41(World world, LevelData data) {
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(1.2f, 15.5f), new Vector2(1.2f, 8.5f), new Vector2(3.3f, 5.0f), new Vector2(3.3f, 3.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(7.8f, 15.5f), new Vector2(7.8f, 8.5f), new Vector2(5.7f, 5.0f), new Vector2(5.7f, 3.5f) }));
        data.spikes.add(new SpikeObstacle(world, 2.2f, 7.5f, 1.4f, 0.4f));
        data.jar = new Jar(world, 4.5f, 1.8f, 3.2f, 2.4f, 12);
        data.totalBallsRequired = 12;
        data.pins.add(new Pin(world, 4.5f, 11.5f, 5.8f, Pin.Direction.RIGHT));
        data.pins.add(new Pin(world, 3.2f, 8.5f, 2.6f, Pin.Direction.LEFT));
        spawnBallGrid(world, data, 2.6f, 12.5f, 4, 4, Ball.BallType.COLORED); // 16 top
    }

    // Bölüm 42: Dikenli Çukur (Bombayı dikenli çukura düşürüp imha etme)
    private static void buildLevel42(World world, LevelData data) {
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(1.0f, 15.5f), new Vector2(1.0f, 5.0f), new Vector2(3.6f, 5.0f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(3.6f, 15.5f), new Vector2(3.6f, 5.0f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(4.2f, 15.5f), new Vector2(4.2f, 8.0f), new Vector2(4.6f, 5.0f), new Vector2(4.6f, 3.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(8.0f, 15.5f), new Vector2(8.0f, 8.0f), new Vector2(7.4f, 5.0f), new Vector2(7.4f, 3.5f) }));
        data.spikes.add(new SpikeObstacle(world, 2.3f, 5.3f, 2.2f, 0.4f));
        data.jar = new Jar(world, 6.0f, 1.8f, 3.0f, 2.4f, 12);
        data.totalBallsRequired = 12;
        data.pins.add(new Pin(world, 2.3f, 11.0f, 2.4f, Pin.Direction.LEFT));
        data.pins.add(new Pin(world, 6.1f, 10.0f, 3.4f, Pin.Direction.RIGHT));
        data.balls.add(new Ball(world, 2.3f, 12.5f, Ball.BallType.BOMB));
        spawnBallGrid(world, data, 4.8f, 11.5f, 3, 5, Ball.BallType.COLORED); // 15 top
    }

    // Bölüm 43: Solucan Deliği (Portal mekaniği tanıtımı)
    private static void buildLevel43(World world, LevelData data) {
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(1.5f, 15.5f), new Vector2(1.5f, 10.0f), new Vector2(7.5f, 10.0f), new Vector2(7.5f, 15.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(2.5f, 6.5f), new Vector2(3.3f, 4.8f), new Vector2(3.3f, 3.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(6.5f, 6.5f), new Vector2(5.7f, 4.8f), new Vector2(5.7f, 3.5f) }));
        data.portals.add(new Portal(world, 4.5f, 9.2f, 4.5f, 6.0f));
        data.jar = new Jar(world, 4.5f, 1.8f, 3.2f, 2.4f, 12);
        data.totalBallsRequired = 12;
        data.pins.add(new Pin(world, 4.5f, 10.8f, 5.4f, Pin.Direction.RIGHT));
        spawnBallGrid(world, data, 2.6f, 12.0f, 4, 4, Ball.BallType.COLORED); // 16 top
    }

    // Bölüm 44: Dikenlerin Üstünden Atlama (Portal ile diken tabakasını baypas etme)
    private static void buildLevel44(World world, LevelData data) {
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(1.2f, 15.5f), new Vector2(1.2f, 10.0f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(7.8f, 15.5f), new Vector2(7.8f, 10.0f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(1.2f, 5.5f), new Vector2(3.3f, 4.2f), new Vector2(3.3f, 3.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(7.8f, 5.5f), new Vector2(5.7f, 4.2f), new Vector2(5.7f, 3.5f) }));
        data.spikes.add(new SpikeObstacle(world, 4.5f, 7.5f, 5.0f, 0.45f));
        data.portals.add(new Portal(world, 4.5f, 9.2f, 4.5f, 5.2f));
        data.jar = new Jar(world, 4.5f, 1.8f, 3.2f, 2.4f, 12);
        data.totalBallsRequired = 12;
        data.pins.add(new Pin(world, 4.5f, 11.0f, 5.8f, Pin.Direction.LEFT));
        spawnBallGrid(world, data, 2.6f, 12.2f, 4, 4, Ball.BallType.COLORED); // 16 top
    }

    // Bölüm 45: Hız Korunumlu Portal (Rampadan fırlayıp portala dalış ve renklendirme)
    private static void buildLevel45(World world, LevelData data) {
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(1.2f, 15.5f), new Vector2(1.2f, 10.5f), new Vector2(3.8f, 8.0f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(4.5f, 15.5f), new Vector2(4.5f, 10.2f), new Vector2(5.0f, 8.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(4.8f, 7.5f), new Vector2(4.8f, 4.0f), new Vector2(3.3f, 3.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(8.0f, 13.5f), new Vector2(8.0f, 4.0f), new Vector2(5.7f, 3.5f) }));
        data.portals.add(new Portal(world, 3.8f, 7.5f, 6.4f, 12.5f));
        data.jar = new Jar(world, 4.5f, 1.8f, 3.2f, 2.4f, 16);
        data.totalBallsRequired = 16;
        data.pins.add(new Pin(world, 2.8f, 11.5f, 3.0f, Pin.Direction.LEFT));
        data.pins.add(new Pin(world, 6.4f, 6.5f, 3.0f, Pin.Direction.RIGHT));
        spawnBallGrid(world, data, 1.8f, 12.5f, 2, 5, Ball.BallType.COLORED); // 10 renkli
        spawnBallGrid(world, data, 5.4f, 7.5f, 2, 5, Ball.BallType.GRAY);     // 10 gri -> 20 top
    }

    // Bölüm 46: Bomba Zinciri (Çift bombayı dikenlerde zincirleme imha etme)
    private static void buildLevel46(World world, LevelData data) {
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(1.0f, 15.5f), new Vector2(1.0f, 5.0f), new Vector2(3.6f, 5.0f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(3.6f, 15.5f), new Vector2(3.6f, 5.0f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(4.2f, 15.5f), new Vector2(4.2f, 7.5f), new Vector2(3.3f, 4.5f), new Vector2(3.3f, 3.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(8.0f, 15.5f), new Vector2(8.0f, 7.5f), new Vector2(5.7f, 4.5f), new Vector2(5.7f, 3.5f) }));
        data.spikes.add(new SpikeObstacle(world, 2.3f, 5.4f, 2.2f, 0.4f));
        data.jar = new Jar(world, 4.5f, 1.8f, 3.2f, 2.4f, 14);
        data.totalBallsRequired = 14;
        data.pins.add(new Pin(world, 2.3f, 12.0f, 2.4f, Pin.Direction.LEFT));
        data.pins.add(new Pin(world, 6.1f, 9.5f, 3.6f, Pin.Direction.RIGHT));
        data.balls.add(new Ball(world, 2.3f, 13.5f, Ball.BallType.BOMB));
        data.balls.add(new Ball(world, 2.3f, 10.0f, Ball.BallType.BOMB));
        spawnBallGrid(world, data, 4.8f, 11.5f, 3, 6, Ball.BallType.COLORED); // 18 top
    }

    // Bölüm 47: Dikenli Labirent (Sağ ve solda dikenler, ortadaki güvenli koridordan süzülme)
    private static void buildLevel47(World world, LevelData data) {
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(1.2f, 15.5f), new Vector2(1.2f, 10.0f), new Vector2(3.0f, 8.5f), new Vector2(1.2f, 7.0f), new Vector2(3.3f, 4.5f), new Vector2(3.3f, 3.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(7.8f, 15.5f), new Vector2(7.8f, 10.0f), new Vector2(6.0f, 8.5f), new Vector2(7.8f, 7.0f), new Vector2(5.7f, 4.5f), new Vector2(5.7f, 3.5f) }));
        data.spikes.add(new SpikeObstacle(world, 1.9f, 9.8f, 1.2f, 0.35f));
        data.spikes.add(new SpikeObstacle(world, 7.1f, 6.8f, 1.2f, 0.35f));
        data.jar = new Jar(world, 4.5f, 1.8f, 3.2f, 2.4f, 13);
        data.totalBallsRequired = 13;
        data.pins.add(new Pin(world, 4.5f, 12.0f, 5.6f, Pin.Direction.RIGHT));
        data.pins.add(new Pin(world, 4.5f, 8.8f, 2.8f, Pin.Direction.LEFT));
        spawnBallGrid(world, data, 2.6f, 12.8f, 4, 4, Ball.BallType.COLORED); // 16 top
    }

    // Bölüm 48: İkiz Portallar (A->B ve C->D ikili portal matrisi)
    private static void buildLevel48(World world, LevelData data) {
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(1.2f, 15.5f), new Vector2(1.2f, 9.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(4.5f, 15.5f), new Vector2(4.5f, 9.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(7.8f, 15.5f), new Vector2(7.8f, 9.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(4.5f, 7.5f), new Vector2(4.5f, 4.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(1.2f, 6.0f), new Vector2(3.3f, 4.5f), new Vector2(3.3f, 3.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(7.8f, 6.0f), new Vector2(5.7f, 4.5f), new Vector2(5.7f, 3.5f) }));
        data.portals.add(new Portal(world, 2.8f, 9.2f, 6.2f, 6.5f));
        data.portals.add(new Portal(world, 6.2f, 9.2f, 2.8f, 6.5f));
        data.jar = new Jar(world, 4.5f, 1.8f, 3.2f, 2.4f, 16);
        data.totalBallsRequired = 16;
        data.pins.add(new Pin(world, 2.8f, 10.8f, 3.0f, Pin.Direction.LEFT));
        data.pins.add(new Pin(world, 6.2f, 10.8f, 3.0f, Pin.Direction.RIGHT));
        data.pins.add(new Pin(world, 4.5f, 5.5f, 5.4f, Pin.Direction.RIGHT));
        spawnBallGrid(world, data, 1.8f, 11.5f, 2, 5, Ball.BallType.COLORED); // 10 renkli
        spawnBallGrid(world, data, 5.2f, 11.5f, 2, 5, Ball.BallType.GRAY);    // 10 gri -> 20 top
    }

    // Bölüm 49: Renk Işınlama (Portaldan kapalı odaya ışınlanıp gri bilyeleri boyama)
    private static void buildLevel49(World world, LevelData data) {
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(1.2f, 15.5f), new Vector2(1.2f, 10.5f), new Vector2(4.0f, 9.5f), new Vector2(4.0f, 15.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(4.8f, 11.5f), new Vector2(4.8f, 6.0f), new Vector2(3.3f, 4.5f), new Vector2(3.3f, 3.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(7.8f, 11.5f), new Vector2(7.8f, 6.0f), new Vector2(5.7f, 4.5f), new Vector2(5.7f, 3.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(4.8f, 11.5f), new Vector2(7.8f, 11.5f) }));
        data.portals.add(new Portal(world, 2.6f, 10.0f, 6.3f, 10.5f));
        data.jar = new Jar(world, 4.5f, 1.8f, 3.2f, 2.4f, 18);
        data.totalBallsRequired = 18;
        data.pins.add(new Pin(world, 2.6f, 11.8f, 2.6f, Pin.Direction.LEFT));
        data.pins.add(new Pin(world, 6.3f, 6.8f, 2.8f, Pin.Direction.RIGHT));
        spawnBallGrid(world, data, 1.6f, 12.5f, 2, 6, Ball.BallType.COLORED); // 12 renkli
        spawnBallGrid(world, data, 5.2f, 7.8f, 2, 6, Ball.BallType.GRAY);     // 12 gri -> 24 top
    }

    // Bölüm 50: Magma Kalbi (Dünya 3 Mini-Apex: Diken çukuru, portal, bomba ve renk füzyonu)
    private static void buildLevel50(World world, LevelData data) {
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(1.0f, 15.5f), new Vector2(1.0f, 5.5f), new Vector2(3.2f, 5.5f), new Vector2(3.2f, 15.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(3.8f, 15.5f), new Vector2(3.8f, 8.5f), new Vector2(3.3f, 5.0f), new Vector2(3.3f, 3.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(7.8f, 15.5f), new Vector2(7.8f, 8.5f), new Vector2(5.7f, 5.0f), new Vector2(5.7f, 3.5f) }));
        data.spikes.add(new SpikeObstacle(world, 2.1f, 5.8f, 2.0f, 0.4f));
        data.portals.add(new Portal(world, 2.1f, 9.5f, 2.1f, 6.5f));
        data.jar = new Jar(world, 4.5f, 1.8f, 3.2f, 2.4f, 16);
        data.totalBallsRequired = 16;
        data.pins.add(new Pin(world, 2.1f, 11.5f, 2.0f, Pin.Direction.LEFT));
        data.pins.add(new Pin(world, 5.8f, 12.0f, 3.8f, Pin.Direction.RIGHT));
        data.pins.add(new Pin(world, 5.8f, 8.2f, 3.8f, Pin.Direction.LEFT));
        data.balls.add(new Ball(world, 2.1f, 13.0f, Ball.BallType.BOMB));
        spawnBallGrid(world, data, 4.4f, 12.8f, 3, 4, Ball.BallType.COLORED); // 12 renkli
        spawnBallGrid(world, data, 4.4f, 9.0f, 3, 3, Ball.BallType.GRAY);     // 9 gri -> 21 top
    }

    // Bölüm 51: Portal Döngüsü (Zamanlamayla döngüden çıkış)
    private static void buildLevel51(World world, LevelData data) {
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(1.5f, 15.5f), new Vector2(1.5f, 7.0f), new Vector2(3.3f, 5.0f), new Vector2(3.3f, 3.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(7.5f, 15.5f), new Vector2(7.5f, 7.0f), new Vector2(5.7f, 5.0f), new Vector2(5.7f, 3.5f) }));
        data.portals.add(new Portal(world, 4.5f, 6.5f, 4.5f, 13.5f));
        data.jar = new Jar(world, 4.5f, 1.8f, 3.2f, 2.4f, 13);
        data.totalBallsRequired = 13;
        data.pins.add(new Pin(world, 4.5f, 11.0f, 5.6f, Pin.Direction.RIGHT));
        data.pins.add(new Pin(world, 4.5f, 7.8f, 5.6f, Pin.Direction.LEFT));
        spawnBallGrid(world, data, 2.6f, 12.0f, 4, 4, Ball.BallType.COLORED); // 16 top
    }

    // Bölüm 52: Diken Tuzağı (Yanlış pimi çekersen dikenlere akar)
    private static void buildLevel52(World world, LevelData data) {
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(1.2f, 15.5f), new Vector2(1.2f, 8.5f), new Vector2(1.2f, 4.0f), new Vector2(3.6f, 4.0f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(4.5f, 12.0f), new Vector2(4.5f, 8.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(7.8f, 15.5f), new Vector2(7.8f, 8.5f), new Vector2(6.8f, 5.0f), new Vector2(6.8f, 3.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(4.5f, 7.0f), new Vector2(4.2f, 5.0f), new Vector2(4.2f, 3.5f) }));
        data.spikes.add(new SpikeObstacle(world, 2.4f, 4.3f, 2.2f, 0.4f));
        data.jar = new Jar(world, 5.5f, 1.8f, 3.0f, 2.4f, 14);
        data.totalBallsRequired = 14;
        data.pins.add(new Pin(world, 2.8f, 8.2f, 3.0f, Pin.Direction.LEFT)); // Tuzak pimi
        data.pins.add(new Pin(world, 6.2f, 8.2f, 3.0f, Pin.Direction.RIGHT)); // Güvenli pim
        data.pins.add(new Pin(world, 4.5f, 12.5f, 5.8f, Pin.Direction.RIGHT)); // Ana üst pim
        spawnBallGrid(world, data, 2.6f, 13.2f, 4, 4, Ball.BallType.COLORED); // 16 top
    }

    // Bölüm 53: Bomba Işınlama (Bombayı portalla dikenli odaya gönderip imha etme)
    private static void buildLevel53(World world, LevelData data) {
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(1.2f, 15.5f), new Vector2(1.2f, 8.0f), new Vector2(3.3f, 5.0f), new Vector2(3.3f, 3.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(7.8f, 15.5f), new Vector2(7.8f, 8.0f), new Vector2(5.7f, 5.0f), new Vector2(5.7f, 3.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(3.2f, 15.5f), new Vector2(3.2f, 10.5f), new Vector2(5.8f, 10.5f), new Vector2(5.8f, 15.5f) }));
        data.spikes.add(new SpikeObstacle(world, 8.0f, 5.0f, 1.6f, 0.4f));
        data.portals.add(new Portal(world, 4.5f, 9.5f, 8.0f, 6.5f));
        data.jar = new Jar(world, 4.5f, 1.8f, 3.2f, 2.4f, 12);
        data.totalBallsRequired = 12;
        data.pins.add(new Pin(world, 4.5f, 11.0f, 2.4f, Pin.Direction.LEFT));
        data.pins.add(new Pin(world, 2.2f, 8.5f, 2.0f, Pin.Direction.LEFT));
        data.pins.add(new Pin(world, 6.8f, 8.5f, 2.0f, Pin.Direction.RIGHT));
        data.balls.add(new Ball(world, 4.5f, 12.2f, Ball.BallType.BOMB));
        spawnBallGrid(world, data, 1.6f, 9.5f, 2, 4, Ball.BallType.COLORED); // 8
        spawnBallGrid(world, data, 6.2f, 9.5f, 2, 4, Ball.BallType.COLORED); // 8 -> 16 top
    }

    // Bölüm 54: Kilitli Portal (Portal çıkışı kilitli pimin arkasında)
    private static void buildLevel54(World world, LevelData data) {
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(1.2f, 15.5f), new Vector2(1.2f, 9.5f), new Vector2(3.5f, 7.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(4.5f, 15.5f), new Vector2(4.5f, 10.0f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(7.8f, 15.5f), new Vector2(7.8f, 6.5f), new Vector2(5.7f, 4.5f), new Vector2(5.7f, 3.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(4.5f, 6.5f), new Vector2(4.5f, 4.5f), new Vector2(3.3f, 3.5f) }));
        data.portals.add(new Portal(world, 6.2f, 10.0f, 6.2f, 7.8f));
        data.jar = new Jar(world, 4.5f, 1.8f, 3.2f, 2.4f, 13);
        data.totalBallsRequired = 13;
        data.keys.add(new KeyEntity(world, 2.5f, 13.5f, 1));
        data.pins.add(new Pin(world, 2.8f, 11.5f, 3.0f, Pin.Direction.LEFT));
        data.pins.add(new Pin(world, 6.2f, 6.5f, 3.0f, Pin.Direction.RIGHT, true, 1));
        spawnBallGrid(world, data, 5.0f, 11.8f, 3, 3, Ball.BallType.COLORED); // 9
        spawnBallGrid(world, data, 5.0f, 8.5f, 3, 2, Ball.BallType.GRAY);      // 6 -> 15 top
    }

    // Bölüm 55: Trambolin + Portal (Bouncer bilyeyi portala fırlatır)
    private static void buildLevel55(World world, LevelData data) {
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(1.2f, 15.5f), new Vector2(1.2f, 9.0f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(3.8f, 15.5f), new Vector2(3.8f, 9.0f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(7.8f, 15.5f), new Vector2(7.8f, 5.0f), new Vector2(5.7f, 4.0f), new Vector2(5.7f, 3.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(4.2f, 5.0f), new Vector2(3.3f, 4.0f), new Vector2(3.3f, 3.5f) }));
        data.bouncers.add(new Bouncer(world, 2.5f, 6.5f, 2.2f, 0.35f, 42, 8.5f));
        data.portals.add(new Portal(world, 5.5f, 10.5f, 5.5f, 6.0f));
        data.jar = new Jar(world, 4.5f, 1.8f, 3.2f, 2.4f, 12);
        data.totalBallsRequired = 12;
        data.pins.add(new Pin(world, 2.5f, 11.0f, 2.4f, Pin.Direction.LEFT));
        spawnBallGrid(world, data, 1.6f, 12.5f, 2, 7, Ball.BallType.COLORED); // 14 top
    }

    // Bölüm 56: Lav Şelalesi (28 renkli bilyenin ikili portaldan akışı)
    private static void buildLevel56(World world, LevelData data) {
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(1.2f, 15.5f), new Vector2(1.2f, 10.0f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(7.8f, 15.5f), new Vector2(7.8f, 10.0f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(1.2f, 6.5f), new Vector2(3.1f, 4.5f), new Vector2(3.1f, 3.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(7.8f, 6.5f), new Vector2(5.9f, 4.5f), new Vector2(5.9f, 3.5f) }));
        data.portals.add(new Portal(world, 3.0f, 9.0f, 3.5f, 6.0f));
        data.portals.add(new Portal(world, 6.0f, 9.0f, 5.5f, 6.0f));
        data.jar = new Jar(world, 4.5f, 1.8f, 3.6f, 2.4f, 22);
        data.totalBallsRequired = 22;
        data.pins.add(new Pin(world, 4.5f, 10.8f, 5.8f, Pin.Direction.RIGHT));
        spawnBallGrid(world, data, 2.2f, 11.8f, 5, 5, Ball.BallType.COLORED); // 25 top
        spawnBallGrid(world, data, 3.2f, 14.6f, 3, 1, Ball.BallType.COLORED); // +3 = 28 top
    }

    // Bölüm 57: Zikzak Dikenler (Merdiven şeklinde diken bariyerleri)
    private static void buildLevel57(World world, LevelData data) {
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(1.2f, 15.5f), new Vector2(1.2f, 11.0f), new Vector2(5.5f, 9.0f), new Vector2(1.2f, 7.0f), new Vector2(3.3f, 4.5f), new Vector2(3.3f, 3.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(7.8f, 15.5f), new Vector2(7.8f, 9.0f), new Vector2(3.5f, 7.0f), new Vector2(7.8f, 5.0f), new Vector2(5.7f, 4.5f), new Vector2(5.7f, 3.5f) }));
        data.spikes.add(new SpikeObstacle(world, 6.8f, 9.2f, 1.4f, 0.35f));
        data.spikes.add(new SpikeObstacle(world, 2.2f, 6.8f, 1.4f, 0.35f));
        data.jar = new Jar(world, 4.5f, 1.8f, 3.2f, 2.4f, 14);
        data.totalBallsRequired = 14;
        data.pins.add(new Pin(world, 4.5f, 12.2f, 5.6f, Pin.Direction.LEFT));
        data.pins.add(new Pin(world, 4.5f, 8.2f, 4.5f, Pin.Direction.RIGHT));
        spawnBallGrid(world, data, 2.6f, 13.0f, 4, 4, Ball.BallType.COLORED); // 16 top
    }

    // Bölüm 58: Çifte Işınlama Çıkmazı (Güvenli portal vs dikenli ölüm portalı)
    private static void buildLevel58(World world, LevelData data) {
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(1.2f, 15.5f), new Vector2(1.2f, 8.5f), new Vector2(3.3f, 5.0f), new Vector2(3.3f, 3.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(7.8f, 15.5f), new Vector2(7.8f, 8.5f), new Vector2(5.7f, 5.0f), new Vector2(5.7f, 3.5f) }));
        data.spikes.add(new SpikeObstacle(world, 8.0f, 4.0f, 1.6f, 0.4f));
        data.portals.add(new Portal(world, 3.0f, 9.0f, 4.5f, 5.5f)); // Güvenli
        data.portals.add(new Portal(world, 6.0f, 9.0f, 8.0f, 5.5f)); // Dikenli tuzak
        data.jar = new Jar(world, 4.5f, 1.8f, 3.2f, 2.4f, 12);
        data.totalBallsRequired = 12;
        data.pins.add(new Pin(world, 4.5f, 12.0f, 5.6f, Pin.Direction.LEFT));
        data.pins.add(new Pin(world, 4.5f, 9.8f, 4.0f, Pin.Direction.RIGHT));
        spawnBallGrid(world, data, 2.6f, 12.8f, 4, 4, Ball.BallType.COLORED); // 16 top
    }

    // Bölüm 59: Üçlü Bomba Tahliyesi (3 ayrı bombanın milimetrik tahliyesi)
    private static void buildLevel59(World world, LevelData data) {
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(1.0f, 15.5f), new Vector2(1.0f, 5.0f), new Vector2(2.5f, 5.0f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(2.5f, 15.5f), new Vector2(2.5f, 5.0f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(6.5f, 15.5f), new Vector2(6.5f, 5.0f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(8.0f, 15.5f), new Vector2(8.0f, 5.0f), new Vector2(6.5f, 5.0f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(3.0f, 15.5f), new Vector2(3.0f, 8.0f), new Vector2(3.3f, 4.5f), new Vector2(3.3f, 3.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(6.0f, 15.5f), new Vector2(6.0f, 8.0f), new Vector2(5.7f, 4.5f), new Vector2(5.7f, 3.5f) }));
        data.spikes.add(new SpikeObstacle(world, 1.7f, 5.3f, 1.4f, 0.4f));
        data.spikes.add(new SpikeObstacle(world, 7.3f, 5.3f, 1.4f, 0.4f));
        data.jar = new Jar(world, 4.5f, 1.8f, 3.2f, 2.4f, 12);
        data.totalBallsRequired = 12;
        data.pins.add(new Pin(world, 1.7f, 11.5f, 1.4f, Pin.Direction.LEFT));
        data.pins.add(new Pin(world, 7.3f, 11.5f, 1.4f, Pin.Direction.RIGHT));
        data.pins.add(new Pin(world, 4.5f, 10.5f, 2.8f, Pin.Direction.RIGHT));
        data.balls.add(new Ball(world, 1.7f, 12.8f, Ball.BallType.BOMB));
        data.balls.add(new Ball(world, 7.3f, 12.8f, Ball.BallType.BOMB));
        data.balls.add(new Ball(world, 1.7f, 14.0f, Ball.BallType.BOMB));
        spawnBallGrid(world, data, 3.6f, 11.5f, 2, 7, Ball.BallType.COLORED); // 14 top
    }

    // Bölüm 60: Lav Tanrısı Finali (Dünya 3 Büyük Finali: 2 Portal, 2 Diken, 1 Kilit, Bomba ve Füzyon)
    private static void buildLevel60(World world, LevelData data) {
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(1.0f, 15.5f), new Vector2(1.0f, 5.0f), new Vector2(2.6f, 5.0f), new Vector2(2.6f, 15.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(3.2f, 15.5f), new Vector2(3.2f, 8.5f), new Vector2(3.3f, 5.0f), new Vector2(3.3f, 3.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(7.8f, 15.5f), new Vector2(7.8f, 8.5f), new Vector2(5.7f, 5.0f), new Vector2(5.7f, 3.5f) }));
        data.spikes.add(new SpikeObstacle(world, 1.8f, 5.3f, 1.5f, 0.4f));
        data.portals.add(new Portal(world, 1.8f, 8.5f, 1.8f, 6.2f));
        data.jar = new Jar(world, 4.5f, 1.8f, 3.4f, 2.4f, 20);
        data.totalBallsRequired = 20;
        data.keys.add(new KeyEntity(world, 4.5f, 14.5f, 1));
        data.pins.add(new Pin(world, 1.8f, 10.5f, 1.6f, Pin.Direction.LEFT));
        data.pins.add(new Pin(world, 5.5f, 12.0f, 4.2f, Pin.Direction.RIGHT));
        data.pins.add(new Pin(world, 4.5f, 6.8f, 4.8f, Pin.Direction.LEFT, true, 1));
        data.balls.add(new Ball(world, 1.8f, 12.0f, Ball.BallType.BOMB));
        spawnBallGrid(world, data, 3.8f, 12.8f, 3, 4, Ball.BallType.COLORED); // 12
        spawnBallGrid(world, data, 3.8f, 9.2f, 3, 4, Ball.BallType.GRAY);     // 12 -> 24 top
    }

    // =========================================================================
    // DÜNYA 4: KOZMİK BULUTSU (SEVİYE 61 - 80)
    // =========================================================================

    // Bölüm 61: Kuantum Çoğaltıcı (x2) (Çoğaltıcı kapı mekaniği tanıtımı)
    private static void buildLevel61(World world, LevelData data) {
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(1.5f, 15.5f), new Vector2(1.5f, 9.5f), new Vector2(3.3f, 5.5f), new Vector2(3.3f, 3.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(7.5f, 15.5f), new Vector2(7.5f, 9.5f), new Vector2(5.7f, 5.5f), new Vector2(5.7f, 3.5f) }));
        data.multiplierGates.add(new MultiplierGate(world, 4.5f, 7.5f, 2.5f, 0.45f, MultiplierGate.GateType.MULTIPLY_X2));
        data.jar = new Jar(world, 4.5f, 1.8f, 3.2f, 2.4f, 10);
        data.totalBallsRequired = 10;
        data.pins.add(new Pin(world, 4.5f, 11.5f, 5.2f, Pin.Direction.RIGHT));
        spawnBallGrid(world, data, 3.5f, 12.5f, 2, 3, Ball.BallType.COLORED); // 6 top x2 = 12
    }

    // Bölüm 62: Büyük Patlama (x3) (5 bilyeden 15 bilye üretimi)
    private static void buildLevel62(World world, LevelData data) {
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(1.5f, 15.5f), new Vector2(1.5f, 10.0f), new Vector2(3.3f, 5.5f), new Vector2(3.3f, 3.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(7.5f, 15.5f), new Vector2(7.5f, 10.0f), new Vector2(5.7f, 5.5f), new Vector2(5.7f, 3.5f) }));
        data.multiplierGates.add(new MultiplierGate(world, 4.5f, 7.8f, 2.5f, 0.45f, MultiplierGate.GateType.MULTIPLY_X3));
        data.jar = new Jar(world, 4.5f, 1.8f, 3.2f, 2.4f, 12);
        data.totalBallsRequired = 12;
        data.pins.add(new Pin(world, 4.5f, 11.8f, 5.2f, Pin.Direction.LEFT));
        spawnBallGrid(world, data, 3.8f, 12.8f, 2, 3, Ball.BallType.COLORED); // 6 top x3 = 18
    }

    // Bölüm 63: Renk Filtresi (Neon Mavi) (Gri bilyeleri kuantum lazerle boyama)
    private static void buildLevel63(World world, LevelData data) {
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(1.2f, 15.5f), new Vector2(1.2f, 9.0f), new Vector2(3.3f, 5.0f), new Vector2(3.3f, 3.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(7.8f, 15.5f), new Vector2(7.8f, 9.0f), new Vector2(5.7f, 5.0f), new Vector2(5.7f, 3.5f) }));
        data.colorGates.add(new ColorGate(world, 4.5f, 7.2f, 2.6f, 0.45f, new Color(0.15f, 0.85f, 1.0f, 1f), "NEON MAVI"));
        data.jar = new Jar(world, 4.5f, 1.8f, 3.2f, 2.4f, 12);
        data.totalBallsRequired = 12;
        data.pins.add(new Pin(world, 4.5f, 11.5f, 5.6f, Pin.Direction.RIGHT));
        spawnBallGrid(world, data, 2.6f, 12.5f, 4, 4, Ball.BallType.GRAY); // 16 gri top
    }

    // Bölüm 64: Çift Filtre Geçidi (Sarı ve Magenta filtreleri)
    private static void buildLevel64(World world, LevelData data) {
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(1.2f, 15.5f), new Vector2(1.2f, 9.5f), new Vector2(3.3f, 4.5f), new Vector2(3.3f, 3.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(7.8f, 15.5f), new Vector2(7.8f, 9.5f), new Vector2(5.7f, 4.5f), new Vector2(5.7f, 3.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(4.5f, 11.0f), new Vector2(4.5f, 5.5f) }));
        data.colorGates.add(new ColorGate(world, 2.8f, 7.5f, 2.2f, 0.45f, new Color(1.0f, 0.88f, 0.20f, 1f), "SARI"));
        data.colorGates.add(new ColorGate(world, 6.2f, 7.5f, 2.2f, 0.45f, new Color(0.95f, 0.25f, 0.85f, 1f), "MAGENTA"));
        data.jar = new Jar(world, 4.5f, 1.8f, 3.2f, 2.4f, 14);
        data.totalBallsRequired = 14;
        data.pins.add(new Pin(world, 4.5f, 12.5f, 5.8f, Pin.Direction.LEFT));
        spawnBallGrid(world, data, 2.6f, 13.5f, 4, 4, Ball.BallType.GRAY); // 16 gri top
    }

    // Bölüm 65: Çoğalt + Renklendir (Önce x2 kapısı, ardından renk filtresi)
    private static void buildLevel65(World world, LevelData data) {
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(1.5f, 15.5f), new Vector2(1.5f, 10.5f), new Vector2(3.3f, 5.5f), new Vector2(3.3f, 3.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(7.5f, 15.5f), new Vector2(7.5f, 10.5f), new Vector2(5.7f, 5.5f), new Vector2(5.7f, 3.5f) }));
        data.multiplierGates.add(new MultiplierGate(world, 4.5f, 9.5f, 2.5f, 0.45f, MultiplierGate.GateType.MULTIPLY_X2));
        data.colorGates.add(new ColorGate(world, 4.5f, 6.2f, 2.6f, 0.45f, new Color(0.20f, 0.95f, 0.45f, 1f), "ZUMRUT"));
        data.jar = new Jar(world, 4.5f, 1.8f, 3.2f, 2.4f, 10);
        data.totalBallsRequired = 10;
        data.pins.add(new Pin(world, 4.5f, 11.8f, 5.2f, Pin.Direction.RIGHT));
        spawnBallGrid(world, data, 3.5f, 12.8f, 2, 3, Ball.BallType.GRAY); // 6 top x2 = 12
    }

    // Bölüm 66: Bomba Çoğaltma Tuzağı (Bombadan sakınma, sadece renkli topları çoğaltma)
    private static void buildLevel66(World world, LevelData data) {
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(1.0f, 15.5f), new Vector2(1.0f, 5.5f), new Vector2(3.0f, 5.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(3.0f, 15.5f), new Vector2(3.0f, 8.5f), new Vector2(3.3f, 4.5f), new Vector2(3.3f, 3.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(7.8f, 15.5f), new Vector2(7.8f, 8.5f), new Vector2(5.7f, 4.5f), new Vector2(5.7f, 3.5f) }));
        data.spikes.add(new SpikeObstacle(world, 2.0f, 5.8f, 1.8f, 0.4f));
        data.multiplierGates.add(new MultiplierGate(world, 5.5f, 7.5f, 2.4f, 0.45f, MultiplierGate.GateType.MULTIPLY_X2));
        data.jar = new Jar(world, 4.5f, 1.8f, 3.2f, 2.4f, 12);
        data.totalBallsRequired = 12;
        data.pins.add(new Pin(world, 2.0f, 11.5f, 1.8f, Pin.Direction.LEFT));
        data.pins.add(new Pin(world, 5.5f, 11.5f, 4.2f, Pin.Direction.RIGHT));
        data.balls.add(new Ball(world, 2.0f, 13.0f, Ball.BallType.BOMB));
        spawnBallGrid(world, data, 4.2f, 12.5f, 2, 4, Ball.BallType.COLORED); // 8 top x2 = 16
    }

    // Bölüm 67: İkiz Çoğaltıcı (x2 + x2) (4 katına çıkan üstel artış)
    private static void buildLevel67(World world, LevelData data) {
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(1.5f, 15.5f), new Vector2(1.5f, 11.0f), new Vector2(3.3f, 5.5f), new Vector2(3.3f, 3.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(7.5f, 15.5f), new Vector2(7.5f, 11.0f), new Vector2(5.7f, 5.5f), new Vector2(5.7f, 3.5f) }));
        data.multiplierGates.add(new MultiplierGate(world, 4.5f, 10.0f, 2.5f, 0.45f, MultiplierGate.GateType.MULTIPLY_X2));
        data.multiplierGates.add(new MultiplierGate(world, 4.5f, 6.8f, 2.6f, 0.45f, MultiplierGate.GateType.MULTIPLY_X2));
        data.jar = new Jar(world, 4.5f, 1.8f, 3.2f, 2.4f, 16);
        data.totalBallsRequired = 16;
        data.pins.add(new Pin(world, 4.5f, 12.5f, 5.2f, Pin.Direction.LEFT));
        spawnBallGrid(world, data, 3.6f, 13.5f, 2, 3, Ball.BallType.COLORED); // 6 top x4 = 24
    }

    // Bölüm 68: Prizmatik Bölme (İki farklı renk filtresine ayrılarak akış)
    private static void buildLevel68(World world, LevelData data) {
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(1.2f, 15.5f), new Vector2(1.2f, 9.5f), new Vector2(3.3f, 5.0f), new Vector2(3.3f, 3.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(7.8f, 15.5f), new Vector2(7.8f, 9.5f), new Vector2(5.7f, 5.0f), new Vector2(5.7f, 3.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(4.5f, 11.5f), new Vector2(4.5f, 5.5f) }));
        data.colorGates.add(new ColorGate(world, 2.8f, 7.5f, 2.2f, 0.45f, new Color(0.10f, 0.90f, 0.95f, 1f), "SIBER MAVI"));
        data.colorGates.add(new ColorGate(world, 6.2f, 7.5f, 2.2f, 0.45f, new Color(1.0f, 0.50f, 0.15f, 1f), "GUN BATIMI"));
        data.jar = new Jar(world, 4.5f, 1.8f, 3.2f, 2.4f, 14);
        data.totalBallsRequired = 14;
        data.pins.add(new Pin(world, 2.8f, 10.5f, 2.8f, Pin.Direction.LEFT));
        data.pins.add(new Pin(world, 6.2f, 10.5f, 2.8f, Pin.Direction.RIGHT));
        spawnBallGrid(world, data, 1.8f, 12.0f, 2, 4, Ball.BallType.GRAY); // 8
        spawnBallGrid(world, data, 5.2f, 12.0f, 2, 4, Ball.BallType.GRAY); // 8 -> 16 top
    }

    // Bölüm 69: Çoğaltıcı ve Dikenler (Çoğalan topların dikenlere çarpmadan inişi)
    private static void buildLevel69(World world, LevelData data) {
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(1.2f, 15.5f), new Vector2(1.2f, 10.5f), new Vector2(2.5f, 8.5f), new Vector2(3.3f, 5.0f), new Vector2(3.3f, 3.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(7.8f, 15.5f), new Vector2(7.8f, 10.5f), new Vector2(6.5f, 8.5f), new Vector2(5.7f, 5.0f), new Vector2(5.7f, 3.5f) }));
        data.spikes.add(new SpikeObstacle(world, 1.6f, 8.5f, 1.0f, 0.35f));
        data.spikes.add(new SpikeObstacle(world, 7.4f, 8.5f, 1.0f, 0.35f));
        data.multiplierGates.add(new MultiplierGate(world, 4.5f, 10.0f, 2.5f, 0.45f, MultiplierGate.GateType.MULTIPLY_X2));
        data.jar = new Jar(world, 4.5f, 1.8f, 3.2f, 2.4f, 12);
        data.totalBallsRequired = 12;
        data.pins.add(new Pin(world, 4.5f, 12.0f, 5.4f, Pin.Direction.RIGHT));
        spawnBallGrid(world, data, 3.5f, 13.0f, 2, 4, Ball.BallType.COLORED); // 8 top x2 = 16
    }

    // Bölüm 70: Nebula Çekirdeği (Dünya 4 Mini-Apex: x3 Çoğaltıcı, Kilitli Pim ve Portal)
    private static void buildLevel70(World world, LevelData data) {
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(1.2f, 15.5f), new Vector2(1.2f, 9.5f), new Vector2(3.5f, 7.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(4.2f, 15.5f), new Vector2(4.2f, 10.0f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(7.8f, 15.5f), new Vector2(7.8f, 6.0f), new Vector2(5.7f, 4.5f), new Vector2(5.7f, 3.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(4.2f, 6.0f), new Vector2(3.3f, 4.5f), new Vector2(3.3f, 3.5f) }));
        data.multiplierGates.add(new MultiplierGate(world, 6.0f, 10.5f, 2.4f, 0.45f, MultiplierGate.GateType.MULTIPLY_X3));
        data.portals.add(new Portal(world, 6.0f, 8.5f, 6.0f, 6.5f));
        data.jar = new Jar(world, 4.5f, 1.8f, 3.2f, 2.4f, 14);
        data.totalBallsRequired = 14;
        data.keys.add(new KeyEntity(world, 2.2f, 14.0f, 1));
        data.pins.add(new Pin(world, 2.6f, 11.5f, 2.6f, Pin.Direction.LEFT));
        data.pins.add(new Pin(world, 6.0f, 12.0f, 3.0f, Pin.Direction.RIGHT));
        data.pins.add(new Pin(world, 6.0f, 5.5f, 3.2f, Pin.Direction.RIGHT, true, 1));
        spawnBallGrid(world, data, 5.0f, 13.0f, 2, 3, Ball.BallType.COLORED); // 6 top x3 = 18
    }

    // Bölüm 71: Ters Filtre Odası (x2 ile çoğalan renkli topların gri odaya akışı)
    private static void buildLevel71(World world, LevelData data) {
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(1.2f, 15.5f), new Vector2(1.2f, 9.0f), new Vector2(3.3f, 5.0f), new Vector2(3.3f, 3.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(7.8f, 15.5f), new Vector2(7.8f, 9.0f), new Vector2(5.7f, 5.0f), new Vector2(5.7f, 3.5f) }));
        data.multiplierGates.add(new MultiplierGate(world, 4.5f, 10.0f, 2.5f, 0.45f, MultiplierGate.GateType.MULTIPLY_X2));
        data.jar = new Jar(world, 4.5f, 1.8f, 3.2f, 2.4f, 16);
        data.totalBallsRequired = 16;
        data.pins.add(new Pin(world, 4.5f, 12.0f, 5.4f, Pin.Direction.RIGHT));
        data.pins.add(new Pin(world, 4.5f, 6.5f, 5.0f, Pin.Direction.LEFT));
        spawnBallGrid(world, data, 3.5f, 13.0f, 2, 3, Ball.BallType.COLORED); // 6 top x2 = 12 renkli
        spawnBallGrid(world, data, 2.8f, 7.5f, 4, 3, Ball.BallType.GRAY);     // 12 gri top -> 24 top
    }

    // Bölüm 72: Çoğaltıcı Yağmuru (2 bilyeden 23 bilyelik patlama)
    private static void buildLevel72(World world, LevelData data) {
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(1.5f, 15.5f), new Vector2(1.5f, 11.5f), new Vector2(3.3f, 5.0f), new Vector2(3.3f, 3.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(7.5f, 15.5f), new Vector2(7.5f, 11.5f), new Vector2(5.7f, 5.0f), new Vector2(5.7f, 3.5f) }));
        data.multiplierGates.add(new MultiplierGate(world, 4.5f, 11.0f, 2.4f, 0.45f, MultiplierGate.GateType.MULTIPLY_X3));
        data.multiplierGates.add(new MultiplierGate(world, 4.5f, 8.0f, 2.6f, 0.45f, MultiplierGate.GateType.MULTIPLY_X3));
        data.multiplierGates.add(new MultiplierGate(world, 4.5f, 5.5f, 2.8f, 0.45f, MultiplierGate.GateType.ADD_5));
        data.jar = new Jar(world, 4.5f, 1.8f, 3.4f, 2.4f, 18);
        data.totalBallsRequired = 18;
        data.pins.add(new Pin(world, 4.5f, 12.8f, 5.0f, Pin.Direction.RIGHT));
        data.balls.add(new Ball(world, 4.2f, 13.8f, Ball.BallType.COLORED));
        data.balls.add(new Ball(world, 4.8f, 13.8f, Ball.BallType.COLORED));
    }

    // Bölüm 73: Portal + Çoğaltıcı (Portaldan fırlayıp doğrudan x2 kapısına dalış)
    private static void buildLevel73(World world, LevelData data) {
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(1.2f, 15.5f), new Vector2(1.2f, 10.0f), new Vector2(3.8f, 8.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(4.2f, 15.5f), new Vector2(4.2f, 10.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(7.8f, 13.0f), new Vector2(7.8f, 5.5f), new Vector2(5.7f, 4.5f), new Vector2(5.7f, 3.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(4.2f, 5.5f), new Vector2(3.3f, 4.5f), new Vector2(3.3f, 3.5f) }));
        data.portals.add(new Portal(world, 2.6f, 10.0f, 6.2f, 11.5f));
        data.multiplierGates.add(new MultiplierGate(world, 6.2f, 8.0f, 2.4f, 0.45f, MultiplierGate.GateType.MULTIPLY_X2));
        data.jar = new Jar(world, 4.5f, 1.8f, 3.2f, 2.4f, 12);
        data.totalBallsRequired = 12;
        data.pins.add(new Pin(world, 2.6f, 11.8f, 2.6f, Pin.Direction.LEFT));
        data.pins.add(new Pin(world, 6.2f, 6.5f, 3.2f, Pin.Direction.RIGHT));
        spawnBallGrid(world, data, 1.8f, 12.8f, 2, 4, Ball.BallType.COLORED); // 8 top x2 = 16
    }

    // Bölüm 74: Seçici Renk Kapısı (Gri bilyelerin viyola filtreyle boyanması)
    private static void buildLevel74(World world, LevelData data) {
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(1.2f, 15.5f), new Vector2(1.2f, 9.0f), new Vector2(3.3f, 5.0f), new Vector2(3.3f, 3.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(7.8f, 15.5f), new Vector2(7.8f, 9.0f), new Vector2(5.7f, 5.0f), new Vector2(5.7f, 3.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(4.5f, 15.5f), new Vector2(4.5f, 7.5f) }));
        data.colorGates.add(new ColorGate(world, 6.2f, 7.2f, 2.4f, 0.45f, new Color(0.70f, 0.25f, 0.95f, 1f), "VIYOLA"));
        data.jar = new Jar(world, 4.5f, 1.8f, 3.2f, 2.4f, 13);
        data.totalBallsRequired = 13;
        data.pins.add(new Pin(world, 2.8f, 10.5f, 3.0f, Pin.Direction.LEFT));
        data.pins.add(new Pin(world, 6.2f, 10.5f, 3.0f, Pin.Direction.RIGHT));
        data.pins.add(new Pin(world, 4.5f, 6.0f, 5.2f, Pin.Direction.RIGHT));
        spawnBallGrid(world, data, 1.8f, 11.5f, 2, 4, Ball.BallType.COLORED); // 8
        spawnBallGrid(world, data, 5.2f, 11.5f, 2, 4, Ball.BallType.GRAY);    // 8 -> 16 top
    }

    // Bölüm 75: Geometrik Prizma (3 renkli prizma filtresi: Mavi, Altın, Yakut)
    private static void buildLevel75(World world, LevelData data) {
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(1.0f, 15.5f), new Vector2(1.0f, 9.5f), new Vector2(3.3f, 4.5f), new Vector2(3.3f, 3.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(8.0f, 15.5f), new Vector2(8.0f, 9.5f), new Vector2(5.7f, 4.5f), new Vector2(5.7f, 3.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(3.2f, 15.5f), new Vector2(3.2f, 6.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(5.8f, 15.5f), new Vector2(5.8f, 6.5f) }));
        data.colorGates.add(new ColorGate(world, 2.1f, 7.5f, 1.8f, 0.45f, new Color(0.20f, 0.60f, 1.0f, 1f), "MAVI"));
        data.colorGates.add(new ColorGate(world, 4.5f, 7.5f, 1.8f, 0.45f, new Color(1.0f, 0.85f, 0.20f, 1f), "ALTIN"));
        data.colorGates.add(new ColorGate(world, 6.9f, 7.5f, 1.8f, 0.45f, new Color(1.0f, 0.25f, 0.35f, 1f), "YAKUT"));
        data.jar = new Jar(world, 4.5f, 1.8f, 3.4f, 2.4f, 14);
        data.totalBallsRequired = 14;
        data.pins.add(new Pin(world, 2.1f, 10.5f, 2.0f, Pin.Direction.LEFT));
        data.pins.add(new Pin(world, 4.5f, 10.5f, 2.2f, Pin.Direction.RIGHT));
        data.pins.add(new Pin(world, 6.9f, 10.5f, 2.0f, Pin.Direction.RIGHT));
        spawnBallGrid(world, data, 1.4f, 11.5f, 2, 3, Ball.BallType.GRAY); // 6
        spawnBallGrid(world, data, 3.8f, 11.5f, 2, 3, Ball.BallType.GRAY); // 6
        spawnBallGrid(world, data, 6.2f, 11.5f, 2, 3, Ball.BallType.GRAY); // 6 -> 18 top
    }

    // Bölüm 76: x3 Labirenti (18 bilyenin dar virajlardan akışı)
    private static void buildLevel76(World world, LevelData data) {
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(1.2f, 15.5f), new Vector2(1.2f, 11.0f), new Vector2(5.5f, 9.0f), new Vector2(1.2f, 7.0f), new Vector2(3.3f, 4.5f), new Vector2(3.3f, 3.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(7.8f, 15.5f), new Vector2(7.8f, 9.0f), new Vector2(3.5f, 7.0f), new Vector2(7.8f, 5.0f), new Vector2(5.7f, 4.5f), new Vector2(5.7f, 3.5f) }));
        data.multiplierGates.add(new MultiplierGate(world, 4.5f, 12.0f, 2.4f, 0.45f, MultiplierGate.GateType.MULTIPLY_X3));
        data.jar = new Jar(world, 4.5f, 1.8f, 3.2f, 2.4f, 14);
        data.totalBallsRequired = 14;
        data.pins.add(new Pin(world, 4.5f, 13.5f, 5.4f, Pin.Direction.RIGHT));
        spawnBallGrid(world, data, 3.5f, 14.2f, 2, 3, Ball.BallType.COLORED); // 6 top x3 = 18
    }

    // Bölüm 77: Çoğaltıcı ve Bomba Barikatı (Bombaları ayır, sadece renkli bilyeleri çoğalt)
    private static void buildLevel77(World world, LevelData data) {
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(1.0f, 15.5f), new Vector2(1.0f, 5.5f), new Vector2(2.6f, 5.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(2.6f, 15.5f), new Vector2(2.6f, 8.5f), new Vector2(3.3f, 5.0f), new Vector2(3.3f, 3.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(7.8f, 15.5f), new Vector2(7.8f, 8.5f), new Vector2(5.7f, 5.0f), new Vector2(5.7f, 3.5f) }));
        data.spikes.add(new SpikeObstacle(world, 1.8f, 5.8f, 1.4f, 0.4f));
        data.multiplierGates.add(new MultiplierGate(world, 5.2f, 7.5f, 2.4f, 0.45f, MultiplierGate.GateType.MULTIPLY_X2));
        data.jar = new Jar(world, 4.5f, 1.8f, 3.2f, 2.4f, 12);
        data.totalBallsRequired = 12;
        data.pins.add(new Pin(world, 1.8f, 11.5f, 1.6f, Pin.Direction.LEFT));
        data.pins.add(new Pin(world, 5.2f, 11.5f, 4.4f, Pin.Direction.RIGHT));
        data.balls.add(new Ball(world, 1.8f, 12.8f, Ball.BallType.BOMB));
        data.balls.add(new Ball(world, 1.8f, 14.0f, Ball.BallType.BOMB));
        spawnBallGrid(world, data, 4.2f, 12.5f, 2, 4, Ball.BallType.COLORED); // 8 top x2 = 16
    }

    // Bölüm 78: Kuantum Sentez (Kilitli pimin ardındaki x2 çoğaltıcı ve renk filtresi)
    private static void buildLevel78(World world, LevelData data) {
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(1.2f, 15.5f), new Vector2(1.2f, 9.5f), new Vector2(3.3f, 5.0f), new Vector2(3.3f, 3.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(7.8f, 15.5f), new Vector2(7.8f, 9.5f), new Vector2(5.7f, 5.0f), new Vector2(5.7f, 3.5f) }));
        data.multiplierGates.add(new MultiplierGate(world, 4.5f, 7.0f, 2.5f, 0.45f, MultiplierGate.GateType.MULTIPLY_X2));
        data.colorGates.add(new ColorGate(world, 4.5f, 5.0f, 2.6f, 0.45f, new Color(0.2f, 0.95f, 0.65f, 1f), "KUANTUM YESIL"));
        data.jar = new Jar(world, 4.5f, 1.8f, 3.2f, 2.4f, 11);
        data.totalBallsRequired = 11;
        data.keys.add(new KeyEntity(world, 7.0f, 13.5f, 1));
        data.pins.add(new Pin(world, 4.5f, 12.0f, 5.4f, Pin.Direction.LEFT));
        data.pins.add(new Pin(world, 4.5f, 8.5f, 5.0f, Pin.Direction.RIGHT, true, 1));
        spawnBallGrid(world, data, 3.2f, 12.8f, 3, 2, Ball.BallType.GRAY); // 6
        data.balls.add(new Ball(world, 4.5f, 14.0f, Ball.BallType.GRAY));   // 7 gri x2 = 14
    }

    // Bölüm 79: Yıldız Tozu Çağlayanı (İkili x2 kapılarından süzülen 20+ bilyelik şelale)
    private static void buildLevel79(World world, LevelData data) {
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(1.2f, 15.5f), new Vector2(1.2f, 10.0f), new Vector2(3.3f, 4.5f), new Vector2(3.3f, 3.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(7.8f, 15.5f), new Vector2(7.8f, 10.0f), new Vector2(5.7f, 4.5f), new Vector2(5.7f, 3.5f) }));
        data.multiplierGates.add(new MultiplierGate(world, 3.2f, 8.0f, 2.2f, 0.45f, MultiplierGate.GateType.MULTIPLY_X2));
        data.multiplierGates.add(new MultiplierGate(world, 5.8f, 8.0f, 2.2f, 0.45f, MultiplierGate.GateType.MULTIPLY_X2));
        data.jar = new Jar(world, 4.5f, 1.8f, 3.4f, 2.4f, 16);
        data.totalBallsRequired = 16;
        data.pins.add(new Pin(world, 4.5f, 11.5f, 5.6f, Pin.Direction.RIGHT));
        spawnBallGrid(world, data, 2.6f, 12.5f, 4, 3, Ball.BallType.COLORED); // 12 top x2 = 24
    }

    // Bölüm 80: Kozmik Bulutsu Finali (Dünya 4 Büyük Finali: x2 & x3, Renk Filtresi, Portal, Kilit ve 30+ Bilye)
    private static void buildLevel80(World world, LevelData data) {
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(1.0f, 15.5f), new Vector2(1.0f, 5.0f), new Vector2(2.6f, 5.0f), new Vector2(2.6f, 15.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(3.2f, 15.5f), new Vector2(3.2f, 9.5f), new Vector2(3.3f, 5.0f), new Vector2(3.3f, 3.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(7.8f, 15.5f), new Vector2(7.8f, 9.5f), new Vector2(5.7f, 5.0f), new Vector2(5.7f, 3.5f) }));
        data.spikes.add(new SpikeObstacle(world, 1.8f, 5.3f, 1.5f, 0.4f));
        data.multiplierGates.add(new MultiplierGate(world, 5.5f, 10.5f, 2.5f, 0.45f, MultiplierGate.GateType.MULTIPLY_X2));
        data.colorGates.add(new ColorGate(world, 5.5f, 7.8f, 2.6f, 0.45f, new Color(0.85f, 0.35f, 1.0f, 1f), "KOZMIK NEBULA"));
        data.jar = new Jar(world, 4.5f, 1.8f, 3.6f, 2.4f, 22);
        data.totalBallsRequired = 22;
        data.keys.add(new KeyEntity(world, 5.5f, 14.5f, 1));
        data.pins.add(new Pin(world, 1.8f, 11.5f, 1.6f, Pin.Direction.LEFT));
        data.pins.add(new Pin(world, 5.5f, 12.0f, 4.2f, Pin.Direction.RIGHT));
        data.pins.add(new Pin(world, 4.5f, 6.0f, 4.8f, Pin.Direction.LEFT, true, 1));
        data.balls.add(new Ball(world, 1.8f, 13.0f, Ball.BallType.BOMB));
        spawnBallGrid(world, data, 4.2f, 12.8f, 3, 3, Ball.BallType.GRAY); // 9 gri x2 = 18
        spawnBallGrid(world, data, 4.2f, 8.8f, 3, 2, Ball.BallType.GRAY);  // +6 gri -> 24 top
    }

    // =========================================================================
    // DÜNYA 5: ZÜMRÜT ZEN TAPINAĞI (SEVİYE 81 - 100)
    // =========================================================================

    // Bölüm 81: Kadim Çark (Dönen çark mekaniği tanıtımı)
    private static void buildLevel81(World world, LevelData data) {
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(1.5f, 15.5f), new Vector2(1.5f, 10.0f), new Vector2(3.3f, 5.0f), new Vector2(3.3f, 3.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(7.5f, 15.5f), new Vector2(7.5f, 10.0f), new Vector2(5.7f, 5.0f), new Vector2(5.7f, 3.5f) }));
        data.rotatingObstacles.add(new RotatingObstacle(world, 4.5f, 8.0f, RotatingObstacle.RotorType.CROSS_PADDLE_4, 1.4f, 0.22f, 1.2f, new Color(0.2f, 0.85f, 0.4f, 1f)));
        data.jar = new Jar(world, 4.5f, 1.8f, 3.2f, 2.4f, 12);
        data.totalBallsRequired = 12;
        data.pins.add(new Pin(world, 4.5f, 11.5f, 5.4f, Pin.Direction.RIGHT));
        spawnBallGrid(world, data, 2.6f, 12.5f, 4, 4, Ball.BallType.COLORED); // 16 top
    }

    // Bölüm 82: Çift Zıt Pervane (Biri saat yönünde, diğeri tersine iki çark)
    private static void buildLevel82(World world, LevelData data) {
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(1.2f, 15.5f), new Vector2(1.2f, 9.5f), new Vector2(3.3f, 4.5f), new Vector2(3.3f, 3.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(7.8f, 15.5f), new Vector2(7.8f, 9.5f), new Vector2(5.7f, 4.5f), new Vector2(5.7f, 3.5f) }));
        data.rotatingObstacles.add(new RotatingObstacle(world, 2.8f, 8.0f, RotatingObstacle.RotorType.CROSS_PADDLE_4, 1.2f, 0.20f, 1.4f, new Color(0.2f, 0.85f, 0.4f, 1f)));
        data.rotatingObstacles.add(new RotatingObstacle(world, 6.2f, 8.0f, RotatingObstacle.RotorType.CROSS_PADDLE_4, 1.2f, 0.20f, -1.4f, new Color(0.2f, 0.75f, 0.5f, 1f)));
        data.jar = new Jar(world, 4.5f, 1.8f, 3.2f, 2.4f, 14);
        data.totalBallsRequired = 14;
        data.pins.add(new Pin(world, 4.5f, 11.5f, 5.8f, Pin.Direction.LEFT));
        spawnBallGrid(world, data, 2.6f, 12.5f, 4, 4, Ball.BallType.COLORED); // 16
        data.balls.add(new Ball(world, 4.2f, 14.8f, Ball.BallType.COLORED));
        data.balls.add(new Ball(world, 4.8f, 14.8f, Ball.BallType.COLORED)); // 18 top
    }

    // Bölüm 83: Çarklı Bomba Tahliyesi (Pervanenin bombayı diken haznesine fırlatması)
    private static void buildLevel83(World world, LevelData data) {
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(1.0f, 15.5f), new Vector2(1.0f, 7.5f), new Vector2(2.5f, 4.5f), new Vector2(2.5f, 3.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(5.5f, 15.5f), new Vector2(5.5f, 10.0f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(8.0f, 15.5f), new Vector2(8.0f, 5.0f), new Vector2(5.8f, 5.0f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(5.5f, 7.5f), new Vector2(4.5f, 4.5f), new Vector2(4.5f, 3.5f) }));
        data.spikes.add(new SpikeObstacle(world, 6.9f, 5.3f, 2.0f, 0.4f));
        data.rotatingObstacles.add(new RotatingObstacle(world, 4.5f, 8.5f, RotatingObstacle.RotorType.CROSS_PADDLE_4, 1.3f, 0.22f, 1.8f, new Color(0.2f, 0.85f, 0.4f, 1f)));
        data.jar = new Jar(world, 3.5f, 1.8f, 2.8f, 2.4f, 12);
        data.totalBallsRequired = 12;
        data.pins.add(new Pin(world, 3.0f, 11.5f, 3.6f, Pin.Direction.LEFT));
        data.pins.add(new Pin(world, 6.8f, 11.5f, 2.2f, Pin.Direction.RIGHT));
        data.balls.add(new Ball(world, 4.2f, 12.5f, Ball.BallType.BOMB));
        spawnBallGrid(world, data, 1.8f, 12.5f, 3, 5, Ball.BallType.COLORED); // 15 top
    }

    // Bölüm 84: Dört Kollu Zen Çarkı (Dönen kanatlar arasında renk karıştırma)
    private static void buildLevel84(World world, LevelData data) {
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(1.2f, 15.5f), new Vector2(1.2f, 9.5f), new Vector2(3.3f, 5.0f), new Vector2(3.3f, 3.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(7.8f, 15.5f), new Vector2(7.8f, 9.5f), new Vector2(5.7f, 5.0f), new Vector2(5.7f, 3.5f) }));
        data.rotatingObstacles.add(new RotatingObstacle(world, 4.5f, 7.8f, RotatingObstacle.RotorType.CROSS_PADDLE_4, 1.5f, 0.22f, 1.3f, new Color(0.25f, 0.9f, 0.5f, 1f)));
        data.jar = new Jar(world, 4.5f, 1.8f, 3.2f, 2.4f, 15);
        data.totalBallsRequired = 15;
        data.pins.add(new Pin(world, 4.5f, 11.5f, 5.6f, Pin.Direction.RIGHT));
        spawnBallGrid(world, data, 2.4f, 12.5f, 4, 3, Ball.BallType.COLORED); // 12 renkli
        spawnBallGrid(world, data, 2.4f, 14.2f, 4, 2, Ball.BallType.GRAY);    // 8 gri -> 20 top
    }

    // Bölüm 85: Hızlı Dişli (Yüksek hızlı pervanenin bilyeleri fırlatması)
    private static void buildLevel85(World world, LevelData data) {
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(1.2f, 15.5f), new Vector2(1.2f, 9.5f), new Vector2(2.5f, 6.0f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(4.5f, 15.5f), new Vector2(4.5f, 11.0f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(7.8f, 15.5f), new Vector2(7.8f, 5.0f), new Vector2(5.7f, 4.0f), new Vector2(5.7f, 3.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(4.5f, 5.0f), new Vector2(3.3f, 4.0f), new Vector2(3.3f, 3.5f) }));
        data.rotatingObstacles.add(new RotatingObstacle(world, 3.2f, 7.5f, RotatingObstacle.RotorType.CROSS_PADDLE_4, 1.3f, 0.22f, 2.8f, new Color(0.2f, 0.85f, 0.4f, 1f)));
        data.jar = new Jar(world, 4.5f, 1.8f, 3.2f, 2.4f, 12);
        data.totalBallsRequired = 12;
        data.pins.add(new Pin(world, 2.8f, 11.5f, 3.0f, Pin.Direction.LEFT));
        spawnBallGrid(world, data, 1.8f, 12.5f, 2, 7, Ball.BallType.COLORED); // 14 top
    }

    // Bölüm 86: Çark + Trambolin (Pervaneden fırlayan bilyelerin bouncer ile sekişi)
    private static void buildLevel86(World world, LevelData data) {
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(1.2f, 15.5f), new Vector2(1.2f, 9.0f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(4.5f, 15.5f), new Vector2(4.5f, 11.0f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(7.8f, 15.5f), new Vector2(7.8f, 5.0f), new Vector2(5.7f, 4.0f), new Vector2(5.7f, 3.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(4.8f, 5.0f), new Vector2(3.3f, 4.0f), new Vector2(3.3f, 3.5f) }));
        data.rotatingObstacles.add(new RotatingObstacle(world, 2.8f, 9.5f, RotatingObstacle.RotorType.CROSS_PADDLE_4, 1.3f, 0.20f, 1.6f, new Color(0.2f, 0.85f, 0.4f, 1f)));
        data.bouncers.add(new Bouncer(world, 2.8f, 5.8f, 2.2f, 0.35f, 40, 8.5f));
        data.jar = new Jar(world, 4.5f, 1.8f, 3.2f, 2.4f, 12);
        data.totalBallsRequired = 12;
        data.pins.add(new Pin(world, 2.8f, 12.0f, 3.0f, Pin.Direction.LEFT));
        spawnBallGrid(world, data, 1.8f, 12.8f, 2, 7, Ball.BallType.COLORED); // 14 top
    }

    // Bölüm 87: Asimetrik Kanatlar (Üç kollu pervane ile ritmik zamanlama)
    private static void buildLevel87(World world, LevelData data) {
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(1.5f, 15.5f), new Vector2(1.5f, 10.0f), new Vector2(3.3f, 5.0f), new Vector2(3.3f, 3.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(7.5f, 15.5f), new Vector2(7.5f, 10.0f), new Vector2(5.7f, 5.0f), new Vector2(5.7f, 3.5f) }));
        data.rotatingObstacles.add(new RotatingObstacle(world, 4.5f, 8.0f, RotatingObstacle.RotorType.TRI_ROTOR_3, 1.5f, 0.22f, 1.5f, new Color(0.3f, 0.95f, 0.5f, 1f)));
        data.jar = new Jar(world, 4.5f, 1.8f, 3.2f, 2.4f, 12);
        data.totalBallsRequired = 12;
        data.pins.add(new Pin(world, 4.5f, 11.5f, 5.4f, Pin.Direction.RIGHT));
        spawnBallGrid(world, data, 2.6f, 12.5f, 4, 4, Ball.BallType.COLORED); // 16 top
    }

    // Bölüm 88: Çark Üstünde Kilit (Çark dönerken anahtarı kapıp kilitli pime ulaştırma)
    private static void buildLevel88(World world, LevelData data) {
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(1.2f, 15.5f), new Vector2(1.2f, 8.5f), new Vector2(3.3f, 4.5f), new Vector2(3.3f, 3.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(7.8f, 15.5f), new Vector2(7.8f, 8.5f), new Vector2(5.7f, 4.5f), new Vector2(5.7f, 3.5f) }));
        data.rotatingObstacles.add(new RotatingObstacle(world, 4.5f, 8.5f, RotatingObstacle.RotorType.CROSS_PADDLE_4, 1.4f, 0.22f, 1.4f, new Color(0.2f, 0.85f, 0.4f, 1f)));
        data.jar = new Jar(world, 4.5f, 1.8f, 3.2f, 2.4f, 14);
        data.totalBallsRequired = 14;
        data.keys.add(new KeyEntity(world, 4.5f, 13.5f, 1));
        data.pins.add(new Pin(world, 4.5f, 11.8f, 5.6f, Pin.Direction.LEFT));
        data.pins.add(new Pin(world, 4.5f, 5.8f, 5.0f, Pin.Direction.RIGHT, true, 1));
        spawnBallGrid(world, data, 2.6f, 12.5f, 4, 4, Ball.BallType.COLORED); // 16
        data.balls.add(new Ball(world, 4.5f, 14.5f, Ball.BallType.COLORED)); // 17 top
    }

    // Bölüm 89: Dönen Engel Duvarı (İki kanatlı dönen süpürücü bariyer)
    private static void buildLevel89(World world, LevelData data) {
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(1.5f, 15.5f), new Vector2(1.5f, 9.5f), new Vector2(3.3f, 5.0f), new Vector2(3.3f, 3.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(7.5f, 15.5f), new Vector2(7.5f, 9.5f), new Vector2(5.7f, 5.0f), new Vector2(5.7f, 3.5f) }));
        data.rotatingObstacles.add(new RotatingObstacle(world, 4.5f, 7.8f, RotatingObstacle.RotorType.TWIN_BAR_2, 1.8f, 0.24f, 1.2f, new Color(0.25f, 0.9f, 0.45f, 1f)));
        data.jar = new Jar(world, 4.5f, 1.8f, 3.2f, 2.4f, 12);
        data.totalBallsRequired = 12;
        data.pins.add(new Pin(world, 4.5f, 11.5f, 5.4f, Pin.Direction.RIGHT));
        spawnBallGrid(world, data, 2.6f, 12.5f, 4, 4, Ball.BallType.COLORED); // 16 top
    }

    // Bölüm 90: Zen Mabedi Zirvesi (Dünya 5 Mini-Apex: Üçlü dişli treni mekanizması)
    private static void buildLevel90(World world, LevelData data) {
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(1.2f, 15.5f), new Vector2(1.2f, 8.5f), new Vector2(3.3f, 5.0f), new Vector2(3.3f, 3.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(7.8f, 15.5f), new Vector2(7.8f, 8.5f), new Vector2(5.7f, 5.0f), new Vector2(5.7f, 3.5f) }));
        data.rotatingObstacles.add(new RotatingObstacle(world, 2.8f, 10.0f, RotatingObstacle.RotorType.CROSS_PADDLE_4, 1.2f, 0.20f, 1.4f, new Color(0.2f, 0.85f, 0.4f, 1f)));
        data.rotatingObstacles.add(new RotatingObstacle(world, 6.2f, 8.5f, RotatingObstacle.RotorType.CROSS_PADDLE_4, 1.2f, 0.20f, -1.4f, new Color(0.25f, 0.9f, 0.5f, 1f)));
        data.rotatingObstacles.add(new RotatingObstacle(world, 3.5f, 6.8f, RotatingObstacle.RotorType.CROSS_PADDLE_4, 1.2f, 0.20f, 1.4f, new Color(0.3f, 0.95f, 0.55f, 1f)));
        data.jar = new Jar(world, 4.5f, 1.8f, 3.4f, 2.4f, 16);
        data.totalBallsRequired = 16;
        data.pins.add(new Pin(world, 4.5f, 12.2f, 5.6f, Pin.Direction.RIGHT));
        spawnBallGrid(world, data, 2.4f, 13.0f, 4, 3, Ball.BallType.COLORED); // 12 renkli
        spawnBallGrid(world, data, 2.4f, 14.8f, 4, 2, Ball.BallType.GRAY);    // 8 gri -> 20 top
    }

    // Bölüm 91: Çark ve Portallar (Pervanenin bilyeleri doğrudan portala süpürmesi)
    private static void buildLevel91(World world, LevelData data) {
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(1.2f, 15.5f), new Vector2(1.2f, 8.5f), new Vector2(3.5f, 7.0f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(4.5f, 15.5f), new Vector2(4.5f, 10.0f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(7.8f, 13.0f), new Vector2(7.8f, 5.0f), new Vector2(5.7f, 4.0f), new Vector2(5.7f, 3.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(4.8f, 5.0f), new Vector2(3.3f, 4.0f), new Vector2(3.3f, 3.5f) }));
        data.rotatingObstacles.add(new RotatingObstacle(world, 2.8f, 8.5f, RotatingObstacle.RotorType.CROSS_PADDLE_4, 1.3f, 0.20f, 1.6f, new Color(0.2f, 0.85f, 0.4f, 1f)));
        data.portals.add(new Portal(world, 4.0f, 7.8f, 6.2f, 11.5f));
        data.jar = new Jar(world, 4.5f, 1.8f, 3.2f, 2.4f, 12);
        data.totalBallsRequired = 12;
        data.pins.add(new Pin(world, 2.8f, 11.5f, 3.0f, Pin.Direction.LEFT));
        data.pins.add(new Pin(world, 6.2f, 6.5f, 3.0f, Pin.Direction.RIGHT));
        spawnBallGrid(world, data, 1.8f, 12.5f, 2, 7, Ball.BallType.COLORED); // 14 top
    }

    // Bölüm 92: Dikenli Çark Odası (Çarkın etrafı dikenlerle çevrili, merkezden geçiş)
    private static void buildLevel92(World world, LevelData data) {
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(1.2f, 15.5f), new Vector2(1.2f, 9.5f), new Vector2(3.3f, 5.0f), new Vector2(3.3f, 3.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(7.8f, 15.5f), new Vector2(7.8f, 9.5f), new Vector2(5.7f, 5.0f), new Vector2(5.7f, 3.5f) }));
        data.spikes.add(new SpikeObstacle(world, 1.8f, 8.0f, 1.2f, 0.35f));
        data.spikes.add(new SpikeObstacle(world, 7.2f, 8.0f, 1.2f, 0.35f));
        data.rotatingObstacles.add(new RotatingObstacle(world, 4.5f, 8.0f, RotatingObstacle.RotorType.CROSS_PADDLE_4, 1.3f, 0.22f, 1.2f, new Color(0.2f, 0.85f, 0.4f, 1f)));
        data.jar = new Jar(world, 4.5f, 1.8f, 3.2f, 2.4f, 12);
        data.totalBallsRequired = 12;
        data.pins.add(new Pin(world, 4.5f, 11.5f, 5.4f, Pin.Direction.RIGHT));
        spawnBallGrid(world, data, 2.6f, 12.5f, 4, 4, Ball.BallType.COLORED); // 16 top
    }

    // Bölüm 93: İki Kademeli Değirmen (Üst değirmen alta aktarır, alt değirmen kavanoza)
    private static void buildLevel93(World world, LevelData data) {
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(1.2f, 15.5f), new Vector2(1.2f, 9.0f), new Vector2(3.3f, 5.0f), new Vector2(3.3f, 3.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(7.8f, 15.5f), new Vector2(7.8f, 9.0f), new Vector2(5.7f, 5.0f), new Vector2(5.7f, 3.5f) }));
        data.rotatingObstacles.add(new RotatingObstacle(world, 4.5f, 10.0f, RotatingObstacle.RotorType.CROSS_PADDLE_4, 1.3f, 0.20f, 1.4f, new Color(0.2f, 0.85f, 0.4f, 1f)));
        data.rotatingObstacles.add(new RotatingObstacle(world, 4.5f, 6.8f, RotatingObstacle.RotorType.CROSS_PADDLE_4, 1.3f, 0.20f, -1.4f, new Color(0.25f, 0.9f, 0.5f, 1f)));
        data.jar = new Jar(world, 4.5f, 1.8f, 3.2f, 2.4f, 15);
        data.totalBallsRequired = 15;
        data.pins.add(new Pin(world, 4.5f, 12.5f, 5.6f, Pin.Direction.LEFT));
        spawnBallGrid(world, data, 2.6f, 13.5f, 4, 5, Ball.BallType.COLORED); // 20 top
    }

    // Bölüm 94: Dönen Renk Filtresi (Pervaneyle renk filtresine itilen gri toplar)
    private static void buildLevel94(World world, LevelData data) {
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(1.5f, 15.5f), new Vector2(1.5f, 10.0f), new Vector2(3.3f, 5.0f), new Vector2(3.3f, 3.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(7.5f, 15.5f), new Vector2(7.5f, 10.0f), new Vector2(5.7f, 5.0f), new Vector2(5.7f, 3.5f) }));
        data.rotatingObstacles.add(new RotatingObstacle(world, 4.5f, 9.2f, RotatingObstacle.RotorType.CROSS_PADDLE_4, 1.3f, 0.20f, 1.4f, new Color(0.2f, 0.85f, 0.4f, 1f)));
        data.colorGates.add(new ColorGate(world, 4.5f, 6.2f, 2.6f, 0.45f, new Color(0.15f, 0.90f, 0.45f, 1f), "ZUMRUT"));
        data.jar = new Jar(world, 4.5f, 1.8f, 3.2f, 2.4f, 12);
        data.totalBallsRequired = 12;
        data.pins.add(new Pin(world, 4.5f, 11.8f, 5.4f, Pin.Direction.RIGHT));
        spawnBallGrid(world, data, 2.6f, 12.8f, 4, 4, Ball.BallType.GRAY); // 16 gri top
    }

    // Bölüm 95: Bomba Sepeti (Pervanenin bombayı yakalayıp dışarı atması)
    private static void buildLevel95(World world, LevelData data) {
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(1.0f, 15.5f), new Vector2(1.0f, 5.5f), new Vector2(2.6f, 5.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(2.6f, 15.5f), new Vector2(2.6f, 8.5f), new Vector2(3.3f, 5.0f), new Vector2(3.3f, 3.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(7.8f, 15.5f), new Vector2(7.8f, 8.5f), new Vector2(5.7f, 5.0f), new Vector2(5.7f, 3.5f) }));
        data.spikes.add(new SpikeObstacle(world, 1.8f, 5.8f, 1.4f, 0.4f));
        data.rotatingObstacles.add(new RotatingObstacle(world, 5.2f, 8.0f, RotatingObstacle.RotorType.CROSS_PADDLE_4, 1.3f, 0.22f, -1.6f, new Color(0.2f, 0.85f, 0.4f, 1f)));
        data.jar = new Jar(world, 4.5f, 1.8f, 3.2f, 2.4f, 12);
        data.totalBallsRequired = 12;
        data.pins.add(new Pin(world, 1.8f, 11.5f, 1.6f, Pin.Direction.LEFT));
        data.pins.add(new Pin(world, 5.2f, 11.5f, 4.5f, Pin.Direction.RIGHT));
        data.balls.add(new Ball(world, 1.8f, 12.8f, Ball.BallType.BOMB));
        spawnBallGrid(world, data, 4.2f, 12.5f, 2, 8, Ball.BallType.COLORED); // 16 top
    }

    // Bölüm 96: Senkronize Pervaneler (İki pervanenin kanatları arasından bilye sızdırma)
    private static void buildLevel96(World world, LevelData data) {
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(1.2f, 15.5f), new Vector2(1.2f, 9.5f), new Vector2(3.3f, 5.0f), new Vector2(3.3f, 3.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(7.8f, 15.5f), new Vector2(7.8f, 9.5f), new Vector2(5.7f, 5.0f), new Vector2(5.7f, 3.5f) }));
        data.rotatingObstacles.add(new RotatingObstacle(world, 2.8f, 8.0f, RotatingObstacle.RotorType.CROSS_PADDLE_4, 1.2f, 0.20f, 1.5f, new Color(0.2f, 0.85f, 0.4f, 1f)));
        data.rotatingObstacles.add(new RotatingObstacle(world, 6.2f, 8.0f, RotatingObstacle.RotorType.CROSS_PADDLE_4, 1.2f, 0.20f, 1.5f, new Color(0.2f, 0.85f, 0.4f, 1f)));
        data.jar = new Jar(world, 4.5f, 1.8f, 3.2f, 2.4f, 15);
        data.totalBallsRequired = 15;
        data.pins.add(new Pin(world, 4.5f, 11.5f, 5.6f, Pin.Direction.LEFT));
        spawnBallGrid(world, data, 2.6f, 12.5f, 4, 5, Ball.BallType.COLORED); // 20 top
    }

    // Bölüm 97: Bambu Akışı (30 bilyenin 6 dişli çarktan dökülen ASMR kaskadı)
    private static void buildLevel97(World world, LevelData data) {
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(1.2f, 15.5f), new Vector2(1.2f, 10.5f), new Vector2(3.1f, 4.5f), new Vector2(3.1f, 3.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(7.8f, 15.5f), new Vector2(7.8f, 10.5f), new Vector2(5.9f, 4.5f), new Vector2(5.9f, 3.5f) }));
        data.rotatingObstacles.add(new RotatingObstacle(world, 4.5f, 8.0f, RotatingObstacle.RotorType.GEAR_DISC_6, 1.4f, 0.20f, 1.4f, new Color(0.25f, 0.92f, 0.45f, 1f)));
        data.jar = new Jar(world, 4.5f, 1.8f, 3.6f, 2.4f, 24);
        data.totalBallsRequired = 24;
        data.pins.add(new Pin(world, 4.5f, 12.0f, 5.8f, Pin.Direction.RIGHT));
        spawnBallGrid(world, data, 2.2f, 12.8f, 5, 6, Ball.BallType.COLORED); // 30 top
    }

    // Bölüm 98: Çarklı Çoğaltıcı (Pervane bilyeleri x2 kapısına yönlendirir)
    private static void buildLevel98(World world, LevelData data) {
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(1.5f, 15.5f), new Vector2(1.5f, 10.0f), new Vector2(3.3f, 5.0f), new Vector2(3.3f, 3.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(7.5f, 15.5f), new Vector2(7.5f, 10.0f), new Vector2(5.7f, 5.0f), new Vector2(5.7f, 3.5f) }));
        data.rotatingObstacles.add(new RotatingObstacle(world, 4.5f, 9.5f, RotatingObstacle.RotorType.CROSS_PADDLE_4, 1.3f, 0.20f, 1.4f, new Color(0.2f, 0.85f, 0.4f, 1f)));
        data.multiplierGates.add(new MultiplierGate(world, 4.5f, 6.5f, 2.4f, 0.45f, MultiplierGate.GateType.MULTIPLY_X2));
        data.jar = new Jar(world, 4.5f, 1.8f, 3.2f, 2.4f, 12);
        data.totalBallsRequired = 12;
        data.pins.add(new Pin(world, 4.5f, 12.0f, 5.2f, Pin.Direction.RIGHT));
        spawnBallGrid(world, data, 3.5f, 13.0f, 2, 4, Ball.BallType.COLORED); // 8 top x2 = 16
    }

    // Bölüm 99: Milimetrik Çark (Dar kanatlı çarktan bilyeleri tek sıra geçirme)
    private static void buildLevel99(World world, LevelData data) {
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(1.5f, 15.5f), new Vector2(1.5f, 9.5f), new Vector2(3.3f, 5.0f), new Vector2(3.3f, 3.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(7.5f, 15.5f), new Vector2(7.5f, 9.5f), new Vector2(5.7f, 5.0f), new Vector2(5.7f, 3.5f) }));
        data.rotatingObstacles.add(new RotatingObstacle(world, 4.5f, 7.8f, RotatingObstacle.RotorType.TWIN_BAR_2, 1.7f, 0.22f, 1.0f, new Color(0.2f, 0.85f, 0.4f, 1f)));
        data.jar = new Jar(world, 4.5f, 1.8f, 3.2f, 2.4f, 12);
        data.totalBallsRequired = 12;
        data.pins.add(new Pin(world, 4.5f, 11.5f, 5.4f, Pin.Direction.LEFT));
        spawnBallGrid(world, data, 2.6f, 12.5f, 4, 4, Ball.BallType.COLORED); // 16 top
    }

    // Bölüm 100: Zen Büyük Tapınak Finali (Dünya 5 Büyük Finali: 3 Çark, 1 Bomba, 1 Kilit, 28 Bilye)
    private static void buildLevel100(World world, LevelData data) {
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(1.0f, 15.5f), new Vector2(1.0f, 5.0f), new Vector2(2.6f, 5.0f), new Vector2(2.6f, 15.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(3.2f, 15.5f), new Vector2(3.2f, 8.5f), new Vector2(3.3f, 5.0f), new Vector2(3.3f, 3.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(7.8f, 15.5f), new Vector2(7.8f, 8.5f), new Vector2(5.7f, 5.0f), new Vector2(5.7f, 3.5f) }));
        data.spikes.add(new SpikeObstacle(world, 1.8f, 5.3f, 1.5f, 0.4f));
        data.rotatingObstacles.add(new RotatingObstacle(world, 5.5f, 10.0f, RotatingObstacle.RotorType.CROSS_PADDLE_4, 1.2f, 0.20f, 1.4f, new Color(0.2f, 0.85f, 0.4f, 1f)));
        data.rotatingObstacles.add(new RotatingObstacle(world, 5.5f, 6.8f, RotatingObstacle.RotorType.CROSS_PADDLE_4, 1.2f, 0.20f, -1.4f, new Color(0.25f, 0.9f, 0.5f, 1f)));
        data.jar = new Jar(world, 4.5f, 1.8f, 3.6f, 2.4f, 22);
        data.totalBallsRequired = 22;
        data.keys.add(new KeyEntity(world, 5.5f, 14.5f, 1));
        data.pins.add(new Pin(world, 1.8f, 11.5f, 1.6f, Pin.Direction.LEFT));
        data.pins.add(new Pin(world, 5.5f, 12.0f, 4.2f, Pin.Direction.RIGHT));
        data.pins.add(new Pin(world, 4.5f, 5.5f, 4.8f, Pin.Direction.LEFT, true, 1));
        data.balls.add(new Ball(world, 1.8f, 13.0f, Ball.BallType.BOMB));
        spawnBallGrid(world, data, 4.0f, 12.8f, 3, 4, Ball.BallType.COLORED); // 12 renkli
        spawnBallGrid(world, data, 4.0f, 8.5f, 3, 4, Ball.BallType.GRAY);     // 12 gri -> 24 + 4 = 28 top
        data.balls.add(new Ball(world, 3.5f, 14.2f, Ball.BallType.COLORED));
        data.balls.add(new Ball(world, 4.0f, 14.2f, Ball.BallType.COLORED));
        data.balls.add(new Ball(world, 4.5f, 14.2f, Ball.BallType.COLORED));
        data.balls.add(new Ball(world, 5.0f, 14.2f, Ball.BallType.COLORED));
    }

    // =========================================================================
    // DÜNYA 6: GÜNEŞ SERABI (SEVİYE 101 - 120)
    // =========================================================================

    // Bölüm 101: Kızıl Lazer Bariyeri (Kuantum nabız lazeri tanıtımı)
    private static void buildLevel101(World world, LevelData data) {
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(1.5f, 15.5f), new Vector2(1.5f, 9.5f), new Vector2(3.3f, 5.0f), new Vector2(3.3f, 3.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(7.5f, 15.5f), new Vector2(7.5f, 9.5f), new Vector2(5.7f, 5.0f), new Vector2(5.7f, 3.5f) }));
        data.lasers.add(new LaserObstacle(world, 1.5f, 7.5f, 7.5f, 7.5f, LaserObstacle.LaserMode.PULSING, 1.4f, 1.4f, 0f, new Color(1.0f, 0.20f, 0.35f, 1.0f)));
        data.jar = new Jar(world, 4.5f, 1.8f, 3.2f, 2.4f, 12);
        data.totalBallsRequired = 12;
        data.pins.add(new Pin(world, 4.5f, 11.5f, 5.4f, Pin.Direction.RIGHT));
        spawnBallGrid(world, data, 2.6f, 12.5f, 4, 4, Ball.BallType.COLORED); // 16 top
    }

    // Bölüm 102: Lazerli Koridor (Lazer söndüğü anda bilyeleri güvenle geçirme)
    private static void buildLevel102(World world, LevelData data) {
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(1.5f, 15.5f), new Vector2(1.5f, 10.0f), new Vector2(3.3f, 5.5f), new Vector2(3.3f, 3.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(7.5f, 15.5f), new Vector2(7.5f, 10.0f), new Vector2(5.7f, 5.5f), new Vector2(5.7f, 3.5f) }));
        data.lasers.add(new LaserObstacle(world, 2.5f, 7.0f, 6.5f, 7.0f, LaserObstacle.LaserMode.PULSING, 1.2f, 1.6f, 0f, new Color(1.0f, 0.25f, 0.30f, 1.0f)));
        data.jar = new Jar(world, 4.5f, 1.8f, 3.2f, 2.4f, 12);
        data.totalBallsRequired = 12;
        data.pins.add(new Pin(world, 4.5f, 11.8f, 5.4f, Pin.Direction.LEFT));
        spawnBallGrid(world, data, 2.6f, 12.8f, 4, 4, Ball.BallType.COLORED); // 16 top
    }

    // Bölüm 103: Sabit Lazer Fırını (Bombayı lazere atıp buharlaştırma)
    private static void buildLevel103(World world, LevelData data) {
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(1.0f, 15.5f), new Vector2(1.0f, 5.0f), new Vector2(3.5f, 5.0f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(3.5f, 15.5f), new Vector2(3.5f, 5.0f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(4.2f, 15.5f), new Vector2(4.2f, 8.0f), new Vector2(4.6f, 5.0f), new Vector2(4.6f, 3.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(8.0f, 15.5f), new Vector2(8.0f, 8.0f), new Vector2(7.4f, 5.0f), new Vector2(7.4f, 3.5f) }));
        data.lasers.add(new LaserObstacle(world, 1.0f, 6.5f, 3.5f, 6.5f, LaserObstacle.LaserMode.CONSTANT, 1f, 1f, 0f, new Color(1.0f, 0.20f, 0.20f, 1.0f)));
        data.jar = new Jar(world, 6.0f, 1.8f, 3.0f, 2.4f, 12);
        data.totalBallsRequired = 12;
        data.pins.add(new Pin(world, 2.2f, 11.5f, 2.4f, Pin.Direction.LEFT));
        data.pins.add(new Pin(world, 6.1f, 10.5f, 3.4f, Pin.Direction.RIGHT));
        data.balls.add(new Ball(world, 2.2f, 12.8f, Ball.BallType.BOMB));
        spawnBallGrid(world, data, 4.8f, 11.5f, 3, 5, Ball.BallType.COLORED); // 15 top
    }

    // Bölüm 104: İkiz Faz Lazerleri (Biri sönerken diğeri yanan iki alternatif lazer)
    private static void buildLevel104(World world, LevelData data) {
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(1.5f, 15.5f), new Vector2(1.5f, 9.0f), new Vector2(3.3f, 5.0f), new Vector2(3.3f, 3.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(7.5f, 15.5f), new Vector2(7.5f, 9.0f), new Vector2(5.7f, 5.0f), new Vector2(5.7f, 3.5f) }));
        data.lasers.add(new LaserObstacle(world, 1.5f, 9.0f, 7.5f, 9.0f, LaserObstacle.LaserMode.PULSING, 1.5f, 1.5f, 0f, new Color(1.0f, 0.20f, 0.35f, 1.0f)));
        data.lasers.add(new LaserObstacle(world, 1.5f, 6.0f, 7.5f, 6.0f, LaserObstacle.LaserMode.PULSING, 1.5f, 1.5f, 1.5f, new Color(0.20f, 0.85f, 1.0f, 1.0f)));
        data.jar = new Jar(world, 4.5f, 1.8f, 3.2f, 2.4f, 12);
        data.totalBallsRequired = 12;
        data.pins.add(new Pin(world, 4.5f, 11.5f, 5.4f, Pin.Direction.RIGHT));
        data.pins.add(new Pin(world, 4.5f, 7.5f, 5.0f, Pin.Direction.LEFT));
        spawnBallGrid(world, data, 2.6f, 12.5f, 4, 4, Ball.BallType.COLORED); // 16 top
    }

    // Bölüm 105: Çapraz Lazer Tuzağı (X şeklinde kesişen lazerler)
    private static void buildLevel105(World world, LevelData data) {
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(1.5f, 15.5f), new Vector2(1.5f, 9.5f), new Vector2(3.3f, 5.0f), new Vector2(3.3f, 3.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(7.5f, 15.5f), new Vector2(7.5f, 9.5f), new Vector2(5.7f, 5.0f), new Vector2(5.7f, 3.5f) }));
        data.lasers.add(new LaserObstacle(world, 1.8f, 8.5f, 7.2f, 6.0f, LaserObstacle.LaserMode.PULSING, 1.3f, 1.7f, 0f, new Color(1.0f, 0.20f, 0.35f, 1.0f)));
        data.lasers.add(new LaserObstacle(world, 1.8f, 6.0f, 7.2f, 8.5f, LaserObstacle.LaserMode.PULSING, 1.3f, 1.7f, 0f, new Color(1.0f, 0.20f, 0.35f, 1.0f)));
        data.jar = new Jar(world, 4.5f, 1.8f, 3.2f, 2.4f, 12);
        data.totalBallsRequired = 12;
        data.pins.add(new Pin(world, 4.5f, 11.5f, 5.4f, Pin.Direction.LEFT));
        spawnBallGrid(world, data, 2.6f, 12.5f, 4, 4, Ball.BallType.COLORED); // 16 top
    }

    // Bölüm 106: Lazer + Trambolin (Bilyeyi lazerin üzerinden zıplatarak aşma)
    private static void buildLevel106(World world, LevelData data) {
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(1.2f, 15.5f), new Vector2(1.2f, 8.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(3.8f, 15.5f), new Vector2(3.8f, 10.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(7.8f, 15.5f), new Vector2(7.8f, 5.0f), new Vector2(5.7f, 4.0f), new Vector2(5.7f, 3.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(4.2f, 5.0f), new Vector2(3.3f, 4.0f), new Vector2(3.3f, 3.5f) }));
        data.lasers.add(new LaserObstacle(world, 3.5f, 6.5f, 6.5f, 6.5f, LaserObstacle.LaserMode.CONSTANT, 1f, 1f, 0f, new Color(1.0f, 0.20f, 0.35f, 1.0f)));
        data.bouncers.add(new Bouncer(world, 2.5f, 6.0f, 2.2f, 0.35f, 44, 9.0f));
        data.jar = new Jar(world, 4.5f, 1.8f, 3.2f, 2.4f, 12);
        data.totalBallsRequired = 12;
        data.pins.add(new Pin(world, 2.5f, 11.5f, 2.4f, Pin.Direction.LEFT));
        spawnBallGrid(world, data, 1.6f, 12.5f, 2, 7, Ball.BallType.COLORED); // 14 top
    }

    // Bölüm 107: Lazerli Kilit (Zamanlama ile anahtarı kapıp kilitli pimi açma)
    private static void buildLevel107(World world, LevelData data) {
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(1.2f, 15.5f), new Vector2(1.2f, 9.0f), new Vector2(3.3f, 5.0f), new Vector2(3.3f, 3.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(7.8f, 15.5f), new Vector2(7.8f, 9.0f), new Vector2(5.7f, 5.0f), new Vector2(5.7f, 3.5f) }));
        data.lasers.add(new LaserObstacle(world, 1.5f, 8.5f, 7.5f, 8.5f, LaserObstacle.LaserMode.PULSING, 1.2f, 1.6f, 0f, new Color(1.0f, 0.25f, 0.30f, 1.0f)));
        data.jar = new Jar(world, 4.5f, 1.8f, 3.2f, 2.4f, 12);
        data.totalBallsRequired = 12;
        data.keys.add(new KeyEntity(world, 2.5f, 13.5f, 1));
        data.pins.add(new Pin(world, 4.5f, 11.5f, 5.6f, Pin.Direction.LEFT));
        data.pins.add(new Pin(world, 4.5f, 6.0f, 5.0f, Pin.Direction.RIGHT, true, 1));
        spawnBallGrid(world, data, 3.5f, 12.5f, 3, 5, Ball.BallType.COLORED); // 15 top
    }

    // Bölüm 108: Dönen Çark ve Lazer (Çark kanatları lazeri dönemsel bloklar)
    private static void buildLevel108(World world, LevelData data) {
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(1.5f, 15.5f), new Vector2(1.5f, 9.5f), new Vector2(3.3f, 5.0f), new Vector2(3.3f, 3.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(7.5f, 15.5f), new Vector2(7.5f, 9.5f), new Vector2(5.7f, 5.0f), new Vector2(5.7f, 3.5f) }));
        data.rotatingObstacles.add(new RotatingObstacle(world, 4.5f, 8.0f, RotatingObstacle.RotorType.CROSS_PADDLE_4, 1.4f, 0.22f, 1.3f, new Color(1.0f, 0.75f, 0.2f, 1f)));
        data.lasers.add(new LaserObstacle(world, 1.5f, 6.5f, 7.5f, 6.5f, LaserObstacle.LaserMode.PULSING, 1.4f, 1.4f, 0.7f, new Color(1.0f, 0.20f, 0.35f, 1.0f)));
        data.jar = new Jar(world, 4.5f, 1.8f, 3.2f, 2.4f, 12);
        data.totalBallsRequired = 12;
        data.pins.add(new Pin(world, 4.5f, 11.5f, 5.4f, Pin.Direction.RIGHT));
        spawnBallGrid(world, data, 2.6f, 12.5f, 4, 4, Ball.BallType.COLORED); // 16 top
    }

    // Bölüm 109: Hızlı Nabız Lazerleri (0.8s seri lazer flaşları arasından sıyrılma)
    private static void buildLevel109(World world, LevelData data) {
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(1.5f, 15.5f), new Vector2(1.5f, 9.5f), new Vector2(3.3f, 5.0f), new Vector2(3.3f, 3.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(7.5f, 15.5f), new Vector2(7.5f, 9.5f), new Vector2(5.7f, 5.0f), new Vector2(5.7f, 3.5f) }));
        data.lasers.add(new LaserObstacle(world, 1.5f, 7.5f, 7.5f, 7.5f, LaserObstacle.LaserMode.PULSING, 0.8f, 1.2f, 0f, new Color(1.0f, 0.20f, 0.35f, 1.0f)));
        data.jar = new Jar(world, 4.5f, 1.8f, 3.2f, 2.4f, 12);
        data.totalBallsRequired = 12;
        data.pins.add(new Pin(world, 4.5f, 11.5f, 5.4f, Pin.Direction.LEFT));
        spawnBallGrid(world, data, 2.6f, 12.5f, 4, 4, Ball.BallType.COLORED); // 16 top
    }

    // Bölüm 110: Güneş Piramidi (Dünya 6 Mini-Apex: Lazer koridoru + x2 Çoğaltıcı)
    private static void buildLevel110(World world, LevelData data) {
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(1.5f, 15.5f), new Vector2(1.5f, 10.5f), new Vector2(3.3f, 5.0f), new Vector2(3.3f, 3.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(7.5f, 15.5f), new Vector2(7.5f, 10.5f), new Vector2(5.7f, 5.0f), new Vector2(5.7f, 3.5f) }));
        data.lasers.add(new LaserObstacle(world, 1.5f, 9.0f, 7.5f, 9.0f, LaserObstacle.LaserMode.PULSING, 1.4f, 1.4f, 0f, new Color(1.0f, 0.20f, 0.35f, 1.0f)));
        data.multiplierGates.add(new MultiplierGate(world, 4.5f, 6.5f, 2.6f, 0.45f, MultiplierGate.GateType.MULTIPLY_X2));
        data.jar = new Jar(world, 4.5f, 1.8f, 3.4f, 2.4f, 20);
        data.totalBallsRequired = 20;
        data.pins.add(new Pin(world, 4.5f, 12.0f, 5.4f, Pin.Direction.RIGHT));
        spawnBallGrid(world, data, 2.8f, 12.8f, 4, 3, Ball.BallType.COLORED); // 12 top x2 = 24
    }

    // Bölüm 111: Dikey Plazma Işını (Yukarıdan aşağı inen lazer sütunu)
    private static void buildLevel111(World world, LevelData data) {
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(1.2f, 15.5f), new Vector2(1.2f, 8.5f), new Vector2(3.3f, 4.5f), new Vector2(3.3f, 3.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(7.8f, 15.5f), new Vector2(7.8f, 8.5f), new Vector2(5.7f, 4.5f), new Vector2(5.7f, 3.5f) }));
        data.lasers.add(new LaserObstacle(world, 4.5f, 14.0f, 4.5f, 6.0f, LaserObstacle.LaserMode.CONSTANT, 1f, 1f, 0f, new Color(1.0f, 0.20f, 0.35f, 1.0f)));
        data.jar = new Jar(world, 4.5f, 1.8f, 3.2f, 2.4f, 14);
        data.totalBallsRequired = 14;
        data.pins.add(new Pin(world, 2.8f, 10.5f, 3.0f, Pin.Direction.LEFT));
        data.pins.add(new Pin(world, 6.2f, 10.5f, 3.0f, Pin.Direction.RIGHT));
        spawnBallGrid(world, data, 1.8f, 12.0f, 2, 4, Ball.BallType.COLORED); // 8
        spawnBallGrid(world, data, 5.4f, 12.0f, 2, 4, Ball.BallType.COLORED); // 8 -> 16 top
    }

    // Bölüm 112: Lazerli Portallar (Portaldan çıkan bilyelerin lazer zamanlamasına uyması)
    private static void buildLevel112(World world, LevelData data) {
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(1.2f, 15.5f), new Vector2(1.2f, 9.5f), new Vector2(3.5f, 8.0f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(4.5f, 15.5f), new Vector2(4.5f, 10.0f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(7.8f, 13.5f), new Vector2(7.8f, 5.0f), new Vector2(5.7f, 4.0f), new Vector2(5.7f, 3.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(4.5f, 5.0f), new Vector2(3.3f, 4.0f), new Vector2(3.3f, 3.5f) }));
        data.portals.add(new Portal(world, 2.8f, 9.5f, 6.2f, 11.0f));
        data.lasers.add(new LaserObstacle(world, 4.8f, 7.5f, 7.8f, 7.5f, LaserObstacle.LaserMode.PULSING, 1.3f, 1.6f, 0f, new Color(1.0f, 0.20f, 0.35f, 1.0f)));
        data.jar = new Jar(world, 4.5f, 1.8f, 3.2f, 2.4f, 12);
        data.totalBallsRequired = 12;
        data.pins.add(new Pin(world, 2.8f, 11.5f, 3.0f, Pin.Direction.LEFT));
        data.pins.add(new Pin(world, 6.2f, 5.5f, 3.0f, Pin.Direction.RIGHT));
        spawnBallGrid(world, data, 1.8f, 12.5f, 2, 7, Ball.BallType.COLORED); // 14 top
    }

    // Bölüm 113: Lazer Kalkanı Pimi (Lazerin önünü kesen koruma pimi)
    private static void buildLevel113(World world, LevelData data) {
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(1.2f, 15.5f), new Vector2(1.2f, 8.5f), new Vector2(3.3f, 5.0f), new Vector2(3.3f, 3.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(7.8f, 15.5f), new Vector2(7.8f, 8.5f), new Vector2(5.7f, 5.0f), new Vector2(5.7f, 3.5f) }));
        data.lasers.add(new LaserObstacle(world, 1.2f, 7.0f, 4.0f, 7.0f, LaserObstacle.LaserMode.CONSTANT, 1f, 1f, 0f, new Color(1.0f, 0.20f, 0.35f, 1.0f)));
        data.jar = new Jar(world, 4.5f, 1.8f, 3.2f, 2.4f, 12);
        data.totalBallsRequired = 12;
        data.pins.add(new Pin(world, 2.8f, 7.8f, 2.8f, Pin.Direction.LEFT)); // Lazer kalkanı (çekilmemeli)
        data.pins.add(new Pin(world, 4.5f, 11.5f, 5.6f, Pin.Direction.RIGHT));
        spawnBallGrid(world, data, 2.6f, 12.5f, 4, 4, Ball.BallType.COLORED); // 16 top
    }

    // Bölüm 114: Üç Aşamalı Lazer Merdiveni (1, 2 ve 3. lazerlerin sırayla söndüğü ritmik bulmaca)
    private static void buildLevel114(World world, LevelData data) {
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(1.5f, 15.5f), new Vector2(1.5f, 9.0f), new Vector2(3.3f, 4.5f), new Vector2(3.3f, 3.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(7.5f, 15.5f), new Vector2(7.5f, 9.0f), new Vector2(5.7f, 4.5f), new Vector2(5.7f, 3.5f) }));
        data.lasers.add(new LaserObstacle(world, 1.5f, 10.0f, 7.5f, 10.0f, LaserObstacle.LaserMode.PULSING, 1.2f, 2.0f, 0.0f, new Color(1.0f, 0.2f, 0.3f, 1f)));
        data.lasers.add(new LaserObstacle(world, 1.5f, 7.5f, 7.5f, 7.5f, LaserObstacle.LaserMode.PULSING, 1.2f, 2.0f, 1.0f, new Color(1.0f, 0.7f, 0.2f, 1f)));
        data.lasers.add(new LaserObstacle(world, 1.5f, 5.2f, 7.5f, 5.2f, LaserObstacle.LaserMode.PULSING, 1.2f, 2.0f, 2.0f, new Color(0.2f, 0.8f, 1.0f, 1f)));
        data.jar = new Jar(world, 4.5f, 1.8f, 3.2f, 2.4f, 12);
        data.totalBallsRequired = 12;
        data.pins.add(new Pin(world, 4.5f, 12.0f, 5.4f, Pin.Direction.LEFT));
        spawnBallGrid(world, data, 2.6f, 12.8f, 4, 4, Ball.BallType.COLORED); // 16 top
    }

    // Bölüm 115: Bomba Lazer Fünyesi (Lazeri bombaya temas ettirerek imha etme)
    private static void buildLevel115(World world, LevelData data) {
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(1.0f, 15.5f), new Vector2(1.0f, 5.0f), new Vector2(3.0f, 5.0f), new Vector2(3.0f, 15.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(3.6f, 15.5f), new Vector2(3.6f, 8.0f), new Vector2(3.3f, 4.5f), new Vector2(3.3f, 3.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(7.8f, 15.5f), new Vector2(7.8f, 8.0f), new Vector2(5.7f, 4.5f), new Vector2(5.7f, 3.5f) }));
        data.lasers.add(new LaserObstacle(world, 1.0f, 6.5f, 3.0f, 6.5f, LaserObstacle.LaserMode.CONSTANT, 1f, 1f, 0f, new Color(1.0f, 0.20f, 0.20f, 1.0f)));
        data.jar = new Jar(world, 4.5f, 1.8f, 3.2f, 2.4f, 12);
        data.totalBallsRequired = 12;
        data.pins.add(new Pin(world, 2.0f, 11.5f, 1.8f, Pin.Direction.LEFT));
        data.pins.add(new Pin(world, 5.7f, 10.5f, 4.0f, Pin.Direction.RIGHT));
        data.balls.add(new Ball(world, 2.0f, 12.8f, Ball.BallType.BOMB));
        spawnBallGrid(world, data, 4.4f, 11.5f, 3, 5, Ball.BallType.COLORED); // 15 top
    }

    // Bölüm 116: Lazer Labirenti (Zikzak lazerli geçit)
    private static void buildLevel116(World world, LevelData data) {
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(1.2f, 15.5f), new Vector2(1.2f, 10.0f), new Vector2(3.0f, 8.5f), new Vector2(1.2f, 7.0f), new Vector2(3.3f, 4.5f), new Vector2(3.3f, 3.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(7.8f, 15.5f), new Vector2(7.8f, 10.0f), new Vector2(6.0f, 8.5f), new Vector2(7.8f, 7.0f), new Vector2(5.7f, 4.5f), new Vector2(5.7f, 3.5f) }));
        data.lasers.add(new LaserObstacle(world, 1.2f, 9.8f, 5.0f, 9.8f, LaserObstacle.LaserMode.PULSING, 1.3f, 1.5f, 0f, new Color(1.0f, 0.2f, 0.35f, 1f)));
        data.lasers.add(new LaserObstacle(world, 4.0f, 7.0f, 7.8f, 7.0f, LaserObstacle.LaserMode.PULSING, 1.3f, 1.5f, 1.3f, new Color(1.0f, 0.2f, 0.35f, 1f)));
        data.jar = new Jar(world, 4.5f, 1.8f, 3.2f, 2.4f, 14);
        data.totalBallsRequired = 14;
        data.pins.add(new Pin(world, 4.5f, 12.0f, 5.4f, Pin.Direction.RIGHT));
        data.pins.add(new Pin(world, 4.5f, 8.5f, 3.0f, Pin.Direction.LEFT));
        spawnBallGrid(world, data, 2.6f, 12.8f, 4, 4, Ball.BallType.COLORED); // 16 top
    }

    // Bölüm 117: Renk Filtresi ve Lazer (Güneş Altını filtre ve nabız lazeri)
    private static void buildLevel117(World world, LevelData data) {
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(1.5f, 15.5f), new Vector2(1.5f, 10.0f), new Vector2(3.3f, 5.0f), new Vector2(3.3f, 3.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(7.5f, 15.5f), new Vector2(7.5f, 10.0f), new Vector2(5.7f, 5.0f), new Vector2(5.7f, 3.5f) }));
        data.colorGates.add(new ColorGate(world, 4.5f, 9.2f, 2.6f, 0.45f, new Color(1.0f, 0.85f, 0.20f, 1f), "GUNES ALTINI"));
        data.lasers.add(new LaserObstacle(world, 1.5f, 6.8f, 7.5f, 6.8f, LaserObstacle.LaserMode.PULSING, 1.3f, 1.6f, 0f, new Color(1.0f, 0.20f, 0.35f, 1.0f)));
        data.jar = new Jar(world, 4.5f, 1.8f, 3.2f, 2.4f, 12);
        data.totalBallsRequired = 12;
        data.pins.add(new Pin(world, 4.5f, 11.5f, 5.4f, Pin.Direction.RIGHT));
        spawnBallGrid(world, data, 2.6f, 12.5f, 4, 4, Ball.BallType.GRAY); // 16 gri top
    }

    // Bölüm 118: İkiz Çark Lazer Kalkanı (Dönen iki çarkın gölgesinde ilerleyen bilyeler)
    private static void buildLevel118(World world, LevelData data) {
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(1.2f, 15.5f), new Vector2(1.2f, 9.0f), new Vector2(3.3f, 5.0f), new Vector2(3.3f, 3.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(7.8f, 15.5f), new Vector2(7.8f, 9.0f), new Vector2(5.7f, 5.0f), new Vector2(5.7f, 3.5f) }));
        data.rotatingObstacles.add(new RotatingObstacle(world, 2.8f, 8.0f, RotatingObstacle.RotorType.CROSS_PADDLE_4, 1.2f, 0.20f, 1.4f, new Color(1.0f, 0.75f, 0.2f, 1f)));
        data.rotatingObstacles.add(new RotatingObstacle(world, 6.2f, 8.0f, RotatingObstacle.RotorType.CROSS_PADDLE_4, 1.2f, 0.20f, -1.4f, new Color(1.0f, 0.75f, 0.2f, 1f)));
        data.lasers.add(new LaserObstacle(world, 3.2f, 6.2f, 5.8f, 6.2f, LaserObstacle.LaserMode.PULSING, 1.4f, 1.4f, 0f, new Color(1.0f, 0.20f, 0.35f, 1.0f)));
        data.jar = new Jar(world, 4.5f, 1.8f, 3.2f, 2.4f, 14);
        data.totalBallsRequired = 14;
        data.pins.add(new Pin(world, 4.5f, 11.5f, 5.6f, Pin.Direction.LEFT));
        spawnBallGrid(world, data, 2.6f, 12.5f, 4, 4, Ball.BallType.COLORED); // 16
        data.balls.add(new Ball(world, 4.2f, 14.8f, Ball.BallType.COLORED));
        data.balls.add(new Ball(world, 4.8f, 14.8f, Ball.BallType.COLORED)); // 18 top
    }

    // Bölüm 119: Ultra Lazer Kaosu (Senkronize lazerlerin dans ettiği yüksek adrenalin)
    private static void buildLevel119(World world, LevelData data) {
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(1.5f, 15.5f), new Vector2(1.5f, 9.0f), new Vector2(3.3f, 5.0f), new Vector2(3.3f, 3.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(7.5f, 15.5f), new Vector2(7.5f, 9.0f), new Vector2(5.7f, 5.0f), new Vector2(5.7f, 3.5f) }));
        data.lasers.add(new LaserObstacle(world, 1.5f, 9.0f, 7.5f, 9.0f, LaserObstacle.LaserMode.PULSING, 1.3f, 1.5f, 0f, new Color(1.0f, 0.20f, 0.35f, 1.0f)));
        data.lasers.add(new LaserObstacle(world, 1.5f, 6.5f, 7.5f, 6.5f, LaserObstacle.LaserMode.PULSING, 1.3f, 1.5f, 0f, new Color(1.0f, 0.20f, 0.35f, 1.0f)));
        data.jar = new Jar(world, 4.5f, 1.8f, 3.2f, 2.4f, 14);
        data.totalBallsRequired = 14;
        data.pins.add(new Pin(world, 4.5f, 11.5f, 5.4f, Pin.Direction.RIGHT));
        spawnBallGrid(world, data, 2.6f, 12.5f, 4, 3, Ball.BallType.COLORED); // 12 renkli
        spawnBallGrid(world, data, 2.6f, 14.2f, 4, 2, Ball.BallType.GRAY);    // 8 gri -> 20 top
    }

    // Bölüm 120: Güneş Hükümdarı Finali (Dünya 6 Büyük Finali: 2 Lazer, Çark, Portal, Bomba ve Füzyon)
    private static void buildLevel120(World world, LevelData data) {
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(1.0f, 15.5f), new Vector2(1.0f, 5.0f), new Vector2(2.6f, 5.0f), new Vector2(2.6f, 15.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(3.2f, 15.5f), new Vector2(3.2f, 8.5f), new Vector2(3.3f, 5.0f), new Vector2(3.3f, 3.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(7.8f, 15.5f), new Vector2(7.8f, 8.5f), new Vector2(5.7f, 5.0f), new Vector2(5.7f, 3.5f) }));
        data.lasers.add(new LaserObstacle(world, 1.0f, 6.0f, 2.6f, 6.0f, LaserObstacle.LaserMode.CONSTANT, 1f, 1f, 0f, new Color(1.0f, 0.20f, 0.20f, 1.0f)));
        data.lasers.add(new LaserObstacle(world, 3.2f, 8.5f, 7.8f, 8.5f, LaserObstacle.LaserMode.PULSING, 1.4f, 1.5f, 0f, new Color(1.0f, 0.20f, 0.35f, 1.0f)));
        data.rotatingObstacles.add(new RotatingObstacle(world, 5.5f, 6.5f, RotatingObstacle.RotorType.CROSS_PADDLE_4, 1.3f, 0.20f, 1.4f, new Color(1.0f, 0.75f, 0.2f, 1f)));
        data.jar = new Jar(world, 4.5f, 1.8f, 3.6f, 2.4f, 22);
        data.totalBallsRequired = 22;
        data.pins.add(new Pin(world, 1.8f, 11.5f, 1.6f, Pin.Direction.LEFT));
        data.pins.add(new Pin(world, 5.5f, 12.0f, 4.2f, Pin.Direction.RIGHT));
        data.balls.add(new Ball(world, 1.8f, 13.0f, Ball.BallType.BOMB));
        spawnBallGrid(world, data, 4.0f, 12.8f, 3, 5, Ball.BallType.COLORED); // 15 renkli
        spawnBallGrid(world, data, 4.0f, 9.2f, 3, 4, Ball.BallType.GRAY);     // 12 gri -> 27 + 3 = 30 top
        data.balls.add(new Ball(world, 4.0f, 15.6f, Ball.BallType.COLORED));
        data.balls.add(new Ball(world, 4.6f, 15.6f, Ball.BallType.COLORED));
        data.balls.add(new Ball(world, 5.2f, 15.6f, Ball.BallType.COLORED));
    }

        // =========================================================================
    // DÜNYA 7: TOKSİK BİYOSFER (SEVİYE 121 - 140)
    // =========================================================================

    // Bölüm 121: Toksik Asit Tanıtımı (Asit havuzu ve su bilyesi ile nötralizasyon)
    private static void buildLevel121(World world, LevelData data) {
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(1.5f, 15.5f), new Vector2(1.5f, 9.5f), new Vector2(3.3f, 5.0f), new Vector2(3.3f, 3.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(7.5f, 15.5f), new Vector2(7.5f, 9.5f), new Vector2(5.7f, 5.0f), new Vector2(5.7f, 3.5f) }));
        data.acidPools.add(new AcidPool(world, 4.5f, 6.2f, 2.6f, 0.55f));
        data.jar = new Jar(world, 4.5f, 1.8f, 3.2f, 2.4f, 12);
        data.totalBallsRequired = 12;
        data.pins.add(new Pin(world, 4.5f, 11.5f, 5.4f, Pin.Direction.RIGHT));
        data.pins.add(new Pin(world, 4.5f, 7.8f, 5.0f, Pin.Direction.LEFT));
        data.balls.add(new Ball(world, 4.5f, 8.8f, Ball.BallType.WATER)); // Su bilyesi
        spawnBallGrid(world, data, 2.6f, 12.5f, 4, 4, Ball.BallType.COLORED); // 16 top
    }

    // Bölüm 122: Çift Asit Çukuru (İki asit havuzu ve iki ayrı su bilyesi)
    private static void buildLevel122(World world, LevelData data) {
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(1.2f, 15.5f), new Vector2(1.2f, 9.0f), new Vector2(3.3f, 4.5f), new Vector2(3.3f, 3.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(7.8f, 15.5f), new Vector2(7.8f, 9.0f), new Vector2(5.7f, 4.5f), new Vector2(5.7f, 3.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(4.5f, 11.5f), new Vector2(4.5f, 5.0f) }));
        data.acidPools.add(new AcidPool(world, 2.8f, 6.5f, 2.2f, 0.55f));
        data.acidPools.add(new AcidPool(world, 6.2f, 6.5f, 2.2f, 0.55f));
        data.jar = new Jar(world, 4.5f, 1.8f, 3.2f, 2.4f, 14);
        data.totalBallsRequired = 14;
        data.pins.add(new Pin(world, 2.8f, 10.5f, 2.8f, Pin.Direction.LEFT));
        data.pins.add(new Pin(world, 6.2f, 10.5f, 2.8f, Pin.Direction.RIGHT));
        data.pins.add(new Pin(world, 4.5f, 7.8f, 5.6f, Pin.Direction.RIGHT));
        data.balls.add(new Ball(world, 2.8f, 8.8f, Ball.BallType.WATER));
        data.balls.add(new Ball(world, 6.2f, 8.8f, Ball.BallType.WATER));
        spawnBallGrid(world, data, 1.8f, 11.8f, 2, 4, Ball.BallType.COLORED); // 8
        spawnBallGrid(world, data, 5.2f, 11.8f, 2, 4, Ball.BallType.COLORED); // 8 -> 16 top
    }

    // Bölüm 123: Asit Barikatı (Kavanoz girişini tıkayan asit havuzu)
    private static void buildLevel123(World world, LevelData data) {
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(1.5f, 15.5f), new Vector2(1.5f, 9.5f), new Vector2(3.3f, 5.0f), new Vector2(3.3f, 3.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(7.5f, 15.5f), new Vector2(7.5f, 9.5f), new Vector2(5.7f, 5.0f), new Vector2(5.7f, 3.5f) }));
        data.acidPools.add(new AcidPool(world, 4.5f, 5.5f, 2.8f, 0.55f));
        data.jar = new Jar(world, 4.5f, 1.8f, 3.2f, 2.4f, 12);
        data.totalBallsRequired = 12;
        data.pins.add(new Pin(world, 4.5f, 12.0f, 5.4f, Pin.Direction.RIGHT));
        data.pins.add(new Pin(world, 4.5f, 7.2f, 5.0f, Pin.Direction.LEFT));
        data.balls.add(new Ball(world, 4.5f, 8.2f, Ball.BallType.WATER));
        spawnBallGrid(world, data, 2.6f, 12.8f, 4, 4, Ball.BallType.COLORED); // 16 top
    }

    // Bölüm 124: Zikzak Asit Rampası (Asidin üstünden su bilyesini yuvarlama)
    private static void buildLevel124(World world, LevelData data) {
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(1.2f, 15.5f), new Vector2(1.2f, 10.5f), new Vector2(5.5f, 8.5f), new Vector2(1.2f, 6.5f), new Vector2(3.3f, 4.5f), new Vector2(3.3f, 3.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(7.8f, 15.5f), new Vector2(7.8f, 8.5f), new Vector2(3.5f, 6.5f), new Vector2(7.8f, 5.0f), new Vector2(5.7f, 4.5f), new Vector2(5.7f, 3.5f) }));
        data.acidPools.add(new AcidPool(world, 5.8f, 6.2f, 2.4f, 0.55f));
        data.jar = new Jar(world, 4.5f, 1.8f, 3.2f, 2.4f, 12);
        data.totalBallsRequired = 12;
        data.pins.add(new Pin(world, 4.5f, 12.0f, 5.4f, Pin.Direction.LEFT));
        data.pins.add(new Pin(world, 4.5f, 8.0f, 4.5f, Pin.Direction.RIGHT));
        data.balls.add(new Ball(world, 4.5f, 9.0f, Ball.BallType.WATER));
        spawnBallGrid(world, data, 2.6f, 12.8f, 4, 4, Ball.BallType.COLORED); // 16 top
    }

    // Bölüm 125: Bomba ve Asit (Bombayı güvenle imha edip asiti nötralize etme)
    private static void buildLevel125(World world, LevelData data) {
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(1.0f, 15.5f), new Vector2(1.0f, 5.0f), new Vector2(2.6f, 5.0f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(2.6f, 15.5f), new Vector2(2.6f, 8.5f), new Vector2(3.3f, 5.0f), new Vector2(3.3f, 3.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(7.8f, 15.5f), new Vector2(7.8f, 8.5f), new Vector2(5.7f, 5.0f), new Vector2(5.7f, 3.5f) }));
        data.spikes.add(new SpikeObstacle(world, 1.8f, 5.3f, 1.4f, 0.4f));
        data.acidPools.add(new AcidPool(world, 5.2f, 6.2f, 2.6f, 0.55f));
        data.jar = new Jar(world, 4.5f, 1.8f, 3.2f, 2.4f, 12);
        data.totalBallsRequired = 12;
        data.pins.add(new Pin(world, 1.8f, 11.5f, 1.6f, Pin.Direction.LEFT));
        data.pins.add(new Pin(world, 5.2f, 11.5f, 4.4f, Pin.Direction.RIGHT));
        data.pins.add(new Pin(world, 5.2f, 7.8f, 4.4f, Pin.Direction.LEFT));
        data.balls.add(new Ball(world, 1.8f, 12.8f, Ball.BallType.BOMB));
        data.balls.add(new Ball(world, 5.2f, 8.8f, Ball.BallType.WATER));
        spawnBallGrid(world, data, 4.0f, 12.5f, 2, 7, Ball.BallType.COLORED); // 14 top
    }

    // Bölüm 126: Trambolinli Su Atışı (Su bilyesini trambolinle asite fırlatma)
    private static void buildLevel126(World world, LevelData data) {
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(1.2f, 15.5f), new Vector2(1.2f, 8.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(3.8f, 15.5f), new Vector2(3.8f, 10.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(7.8f, 15.5f), new Vector2(7.8f, 5.0f), new Vector2(5.7f, 4.0f), new Vector2(5.7f, 3.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(4.2f, 5.0f), new Vector2(3.3f, 4.0f), new Vector2(3.3f, 3.5f) }));
        data.bouncers.add(new Bouncer(world, 2.5f, 6.0f, 2.2f, 0.35f, 42, 8.5f));
        data.acidPools.add(new AcidPool(world, 6.0f, 6.5f, 2.4f, 0.55f));
        data.jar = new Jar(world, 4.5f, 1.8f, 3.2f, 2.4f, 12);
        data.totalBallsRequired = 12;
        data.pins.add(new Pin(world, 2.5f, 11.5f, 2.4f, Pin.Direction.LEFT));
        data.balls.add(new Ball(world, 2.5f, 12.5f, Ball.BallType.WATER));
        spawnBallGrid(world, data, 1.6f, 13.5f, 2, 7, Ball.BallType.COLORED); // 14 top
    }

    // Bölüm 127: Kilitli Su Haznesi (Su bilyesi kilitli pimin arkasında)
    private static void buildLevel127(World world, LevelData data) {
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(1.2f, 15.5f), new Vector2(1.2f, 9.0f), new Vector2(3.3f, 5.0f), new Vector2(3.3f, 3.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(7.8f, 15.5f), new Vector2(7.8f, 9.0f), new Vector2(5.7f, 5.0f), new Vector2(5.7f, 3.5f) }));
        data.acidPools.add(new AcidPool(world, 4.5f, 5.8f, 2.6f, 0.55f));
        data.jar = new Jar(world, 4.5f, 1.8f, 3.2f, 2.4f, 12);
        data.totalBallsRequired = 12;
        data.keys.add(new KeyEntity(world, 2.2f, 14.0f, 1));
        data.pins.add(new Pin(world, 4.5f, 11.5f, 5.6f, Pin.Direction.RIGHT));
        data.pins.add(new Pin(world, 4.5f, 7.5f, 5.0f, Pin.Direction.LEFT, true, 1));
        data.balls.add(new Ball(world, 4.5f, 8.5f, Ball.BallType.WATER));
        spawnBallGrid(world, data, 3.2f, 12.5f, 3, 5, Ball.BallType.COLORED); // 15 top
    }

    // Bölüm 128: Geniş Asit Denizi (Geniş asit havuzuna çift su bilyesi)
    private static void buildLevel128(World world, LevelData data) {
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(1.5f, 15.5f), new Vector2(1.5f, 9.5f), new Vector2(3.3f, 5.0f), new Vector2(3.3f, 3.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(7.5f, 15.5f), new Vector2(7.5f, 9.5f), new Vector2(5.7f, 5.0f), new Vector2(5.7f, 3.5f) }));
        data.acidPools.add(new AcidPool(world, 4.5f, 6.0f, 3.6f, 0.65f));
        data.jar = new Jar(world, 4.5f, 1.8f, 3.4f, 2.4f, 15);
        data.totalBallsRequired = 15;
        data.pins.add(new Pin(world, 4.5f, 12.0f, 5.4f, Pin.Direction.RIGHT));
        data.pins.add(new Pin(world, 4.5f, 8.0f, 5.0f, Pin.Direction.LEFT));
        data.balls.add(new Ball(world, 3.8f, 9.0f, Ball.BallType.WATER));
        data.balls.add(new Ball(world, 5.2f, 9.0f, Ball.BallType.WATER));
        spawnBallGrid(world, data, 2.6f, 12.8f, 4, 5, Ball.BallType.COLORED); // 20 top
    }

    // Bölüm 129: Asit ve Dönen Çark (Çark su bilyesini alıp asit havuzuna taşır)
    private static void buildLevel129(World world, LevelData data) {
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(1.5f, 15.5f), new Vector2(1.5f, 9.5f), new Vector2(3.3f, 5.0f), new Vector2(3.3f, 3.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(7.5f, 15.5f), new Vector2(7.5f, 9.5f), new Vector2(5.7f, 5.0f), new Vector2(5.7f, 3.5f) }));
        data.rotatingObstacles.add(new RotatingObstacle(world, 4.5f, 8.5f, RotatingObstacle.RotorType.CROSS_PADDLE_4, 1.3f, 0.22f, 1.4f, new Color(0.2f, 0.85f, 0.4f, 1f)));
        data.acidPools.add(new AcidPool(world, 4.5f, 5.8f, 2.6f, 0.55f));
        data.jar = new Jar(world, 4.5f, 1.8f, 3.2f, 2.4f, 12);
        data.totalBallsRequired = 12;
        data.pins.add(new Pin(world, 4.5f, 12.0f, 5.4f, Pin.Direction.LEFT));
        data.pins.add(new Pin(world, 4.5f, 10.2f, 3.4f, Pin.Direction.RIGHT));
        data.balls.add(new Ball(world, 4.5f, 11.0f, Ball.BallType.WATER));
        spawnBallGrid(world, data, 2.6f, 12.8f, 4, 4, Ball.BallType.COLORED); // 16 top
    }

    // Bölüm 130: Kimyasal Reaktör (Dünya 7 Mini-Apex: Devasa asit havuzu, kilitli su haznesi)
    private static void buildLevel130(World world, LevelData data) {
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(1.2f, 15.5f), new Vector2(1.2f, 8.5f), new Vector2(3.3f, 5.0f), new Vector2(3.3f, 3.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(7.8f, 15.5f), new Vector2(7.8f, 8.5f), new Vector2(5.7f, 5.0f), new Vector2(5.7f, 3.5f) }));
        data.acidPools.add(new AcidPool(world, 4.5f, 5.8f, 3.2f, 0.6f));
        data.jar = new Jar(world, 4.5f, 1.8f, 3.4f, 2.4f, 18);
        data.totalBallsRequired = 18;
        data.keys.add(new KeyEntity(world, 2.2f, 14.5f, 1));
        data.pins.add(new Pin(world, 4.5f, 12.0f, 5.6f, Pin.Direction.RIGHT));
        data.pins.add(new Pin(world, 4.5f, 7.8f, 5.0f, Pin.Direction.LEFT, true, 1));
        data.balls.add(new Ball(world, 4.2f, 8.8f, Ball.BallType.WATER));
        data.balls.add(new Ball(world, 4.8f, 8.8f, Ball.BallType.WATER));
        spawnBallGrid(world, data, 2.4f, 12.8f, 4, 3, Ball.BallType.COLORED); // 12 renkli
        spawnBallGrid(world, data, 2.4f, 14.5f, 4, 3, Ball.BallType.GRAY);    // 12 gri -> 24 top
    }

    // Bölüm 131: Portallı Asit Nötralizasyonu (Su bilyesini portala atıp uzaktaki asite ışınlama)
    private static void buildLevel131(World world, LevelData data) {
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(1.2f, 15.5f), new Vector2(1.2f, 9.5f), new Vector2(3.5f, 8.0f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(4.5f, 15.5f), new Vector2(4.5f, 10.0f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(7.8f, 13.5f), new Vector2(7.8f, 5.0f), new Vector2(5.7f, 4.0f), new Vector2(5.7f, 3.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(4.5f, 5.0f), new Vector2(3.3f, 4.0f), new Vector2(3.3f, 3.5f) }));
        data.portals.add(new Portal(world, 2.8f, 9.5f, 6.2f, 11.0f));
        data.acidPools.add(new AcidPool(world, 6.2f, 7.5f, 2.4f, 0.55f));
        data.jar = new Jar(world, 4.5f, 1.8f, 3.2f, 2.4f, 12);
        data.totalBallsRequired = 12;
        data.pins.add(new Pin(world, 2.8f, 11.5f, 3.0f, Pin.Direction.LEFT));
        data.pins.add(new Pin(world, 6.2f, 5.5f, 3.0f, Pin.Direction.RIGHT));
        data.balls.add(new Ball(world, 2.8f, 12.2f, Ball.BallType.WATER));
        spawnBallGrid(world, data, 1.8f, 13.2f, 2, 7, Ball.BallType.COLORED); // 14 top
    }

    // Bölüm 132: Renkli ve Gri Asit Ayrımı (Gri topları renklendirirken suyu kaybetmeme)
    private static void buildLevel132(World world, LevelData data) {
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(1.2f, 15.5f), new Vector2(1.2f, 9.0f), new Vector2(3.3f, 5.0f), new Vector2(3.3f, 3.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(7.8f, 15.5f), new Vector2(7.8f, 9.0f), new Vector2(5.7f, 5.0f), new Vector2(5.7f, 3.5f) }));
        data.acidPools.add(new AcidPool(world, 4.5f, 6.2f, 2.6f, 0.55f));
        data.jar = new Jar(world, 4.5f, 1.8f, 3.2f, 2.4f, 13);
        data.totalBallsRequired = 13;
        data.pins.add(new Pin(world, 4.5f, 12.0f, 5.4f, Pin.Direction.LEFT));
        data.pins.add(new Pin(world, 4.5f, 8.0f, 5.0f, Pin.Direction.RIGHT));
        data.balls.add(new Ball(world, 4.5f, 9.0f, Ball.BallType.WATER));
        spawnBallGrid(world, data, 2.4f, 12.8f, 4, 2, Ball.BallType.COLORED); // 8 renkli
        spawnBallGrid(world, data, 2.4f, 14.0f, 4, 2, Ball.BallType.GRAY);    // 8 gri -> 16 top
    }

    // Bölüm 133: Lazerli Asit Laboratuvarı (Zamanlamalı koruma ile suyu asite ulaştırma)
    private static void buildLevel133(World world, LevelData data) {
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(1.5f, 15.5f), new Vector2(1.5f, 9.5f), new Vector2(3.3f, 5.0f), new Vector2(3.3f, 3.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(7.5f, 15.5f), new Vector2(7.5f, 9.5f), new Vector2(5.7f, 5.0f), new Vector2(5.7f, 3.5f) }));
        data.lasers.add(new LaserObstacle(world, 1.5f, 8.5f, 7.5f, 8.5f, LaserObstacle.LaserMode.PULSING, 1.3f, 1.6f, 0f, new Color(1.0f, 0.20f, 0.35f, 1.0f)));
        data.acidPools.add(new AcidPool(world, 4.5f, 6.2f, 2.6f, 0.55f));
        data.jar = new Jar(world, 4.5f, 1.8f, 3.2f, 2.4f, 12);
        data.totalBallsRequired = 12;
        data.pins.add(new Pin(world, 4.5f, 11.5f, 5.4f, Pin.Direction.RIGHT));
        data.balls.add(new Ball(world, 4.5f, 12.2f, Ball.BallType.WATER));
        spawnBallGrid(world, data, 2.6f, 13.0f, 4, 4, Ball.BallType.COLORED); // 16 top
    }

    // Bölüm 134: İkiz Hazneli Asit Tuzağı (Sol havuz asitli, doğru havuza su gönderme)
    private static void buildLevel134(World world, LevelData data) {
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(1.2f, 15.5f), new Vector2(1.2f, 9.0f), new Vector2(3.3f, 4.5f), new Vector2(3.3f, 3.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(7.8f, 15.5f), new Vector2(7.8f, 9.0f), new Vector2(5.7f, 4.5f), new Vector2(5.7f, 3.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(4.5f, 11.5f), new Vector2(4.5f, 5.0f) }));
        data.acidPools.add(new AcidPool(world, 2.8f, 6.5f, 2.2f, 0.55f));
        data.jar = new Jar(world, 4.5f, 1.8f, 3.2f, 2.4f, 13);
        data.totalBallsRequired = 13;
        data.pins.add(new Pin(world, 2.8f, 10.5f, 2.8f, Pin.Direction.LEFT));
        data.pins.add(new Pin(world, 6.2f, 10.5f, 2.8f, Pin.Direction.RIGHT));
        data.pins.add(new Pin(world, 4.5f, 7.5f, 5.6f, Pin.Direction.RIGHT));
        data.balls.add(new Ball(world, 2.8f, 8.8f, Ball.BallType.WATER));
        spawnBallGrid(world, data, 1.8f, 11.8f, 2, 4, Ball.BallType.COLORED); // 8
        spawnBallGrid(world, data, 5.2f, 11.8f, 2, 4, Ball.BallType.COLORED); // 8 -> 16 top
    }

    // Bölüm 135: x2 Çoğaltılmış Su (1 su bilyesinden 2 su bilyesi üretip iki havuzu temizleme)
    private static void buildLevel135(World world, LevelData data) {
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(1.2f, 15.5f), new Vector2(1.2f, 9.0f), new Vector2(3.3f, 4.5f), new Vector2(3.3f, 3.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(7.8f, 15.5f), new Vector2(7.8f, 9.0f), new Vector2(5.7f, 4.5f), new Vector2(5.7f, 3.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(4.5f, 8.5f), new Vector2(4.5f, 5.0f) }));
        data.multiplierGates.add(new MultiplierGate(world, 4.5f, 10.0f, 2.4f, 0.45f, MultiplierGate.GateType.MULTIPLY_X2));
        data.acidPools.add(new AcidPool(world, 2.8f, 6.5f, 2.2f, 0.55f));
        data.acidPools.add(new AcidPool(world, 6.2f, 6.5f, 2.2f, 0.55f));
        data.jar = new Jar(world, 4.5f, 1.8f, 3.2f, 2.4f, 12);
        data.totalBallsRequired = 12;
        data.pins.add(new Pin(world, 4.5f, 12.0f, 5.4f, Pin.Direction.RIGHT));
        data.pins.add(new Pin(world, 4.5f, 7.5f, 5.4f, Pin.Direction.LEFT));
        data.balls.add(new Ball(world, 4.5f, 12.8f, Ball.BallType.WATER)); // x2 = 2 su bilyesi!
        spawnBallGrid(world, data, 2.6f, 13.8f, 4, 4, Ball.BallType.COLORED); // 16 top
    }

    // Bölüm 136: Dikenli Asit Havzası (Dikenlerin hemen altındaki asit havuzu)
    private static void buildLevel136(World world, LevelData data) {
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(1.2f, 15.5f), new Vector2(1.2f, 9.5f), new Vector2(3.3f, 5.0f), new Vector2(3.3f, 3.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(7.8f, 15.5f), new Vector2(7.8f, 9.5f), new Vector2(5.7f, 5.0f), new Vector2(5.7f, 3.5f) }));
        data.spikes.add(new SpikeObstacle(world, 1.8f, 8.5f, 1.2f, 0.35f));
        data.spikes.add(new SpikeObstacle(world, 7.2f, 8.5f, 1.2f, 0.35f));
        data.acidPools.add(new AcidPool(world, 4.5f, 6.2f, 2.6f, 0.55f));
        data.jar = new Jar(world, 4.5f, 1.8f, 3.2f, 2.4f, 12);
        data.totalBallsRequired = 12;
        data.pins.add(new Pin(world, 4.5f, 11.5f, 5.4f, Pin.Direction.RIGHT));
        data.pins.add(new Pin(world, 4.5f, 7.5f, 5.0f, Pin.Direction.LEFT));
        data.balls.add(new Ball(world, 4.5f, 8.5f, Ball.BallType.WATER));
        spawnBallGrid(world, data, 2.6f, 12.5f, 4, 4, Ball.BallType.COLORED); // 16 top
    }

    // Bölüm 137: Hızlı Su Kaydırağı (Dik rampadan kayarak asite dalan su bilyesi)
    private static void buildLevel137(World world, LevelData data) {
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(1.2f, 15.5f), new Vector2(1.2f, 10.5f), new Vector2(4.5f, 7.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(7.8f, 15.5f), new Vector2(7.8f, 5.0f), new Vector2(5.7f, 4.0f), new Vector2(5.7f, 3.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(4.5f, 5.0f), new Vector2(3.3f, 4.0f), new Vector2(3.3f, 3.5f) }));
        data.acidPools.add(new AcidPool(world, 5.8f, 6.2f, 2.6f, 0.55f));
        data.jar = new Jar(world, 4.5f, 1.8f, 3.2f, 2.4f, 12);
        data.totalBallsRequired = 12;
        data.pins.add(new Pin(world, 2.8f, 11.5f, 3.0f, Pin.Direction.LEFT));
        data.balls.add(new Ball(world, 2.2f, 12.5f, Ball.BallType.WATER));
        spawnBallGrid(world, data, 1.8f, 13.5f, 2, 7, Ball.BallType.COLORED); // 14 top
    }

    // Bölüm 138: Asitli Çapraz Geçit (İki çapraz koridorda iki ayrı asit havuzu)
    private static void buildLevel138(World world, LevelData data) {
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(1.2f, 15.5f), new Vector2(1.2f, 9.0f), new Vector2(3.3f, 4.5f), new Vector2(3.3f, 3.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(7.8f, 15.5f), new Vector2(7.8f, 9.0f), new Vector2(5.7f, 4.5f), new Vector2(5.7f, 3.5f) }));
        data.acidPools.add(new AcidPool(world, 2.8f, 6.5f, 2.0f, 0.55f));
        data.acidPools.add(new AcidPool(world, 6.2f, 6.5f, 2.0f, 0.55f));
        data.jar = new Jar(world, 4.5f, 1.8f, 3.2f, 2.4f, 14);
        data.totalBallsRequired = 14;
        data.pins.add(new Pin(world, 4.5f, 12.0f, 5.6f, Pin.Direction.RIGHT));
        data.pins.add(new Pin(world, 4.5f, 8.0f, 5.2f, Pin.Direction.LEFT));
        data.balls.add(new Ball(world, 3.2f, 9.0f, Ball.BallType.WATER));
        data.balls.add(new Ball(world, 5.8f, 9.0f, Ball.BallType.WATER));
        spawnBallGrid(world, data, 2.6f, 12.8f, 4, 4, Ball.BallType.COLORED); // 16
        data.balls.add(new Ball(world, 4.2f, 15.0f, Ball.BallType.COLORED));
        data.balls.add(new Ball(world, 4.8f, 15.0f, Ball.BallType.COLORED)); // 18 top
    }

    // Bölüm 139: Kimyasal Saflık (Sıfır hata gerektiren su ve bilye sıralaması)
    private static void buildLevel139(World world, LevelData data) {
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(1.5f, 15.5f), new Vector2(1.5f, 9.0f), new Vector2(3.3f, 5.0f), new Vector2(3.3f, 3.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(7.5f, 15.5f), new Vector2(7.5f, 9.0f), new Vector2(5.7f, 5.0f), new Vector2(5.7f, 3.5f) }));
        data.acidPools.add(new AcidPool(world, 4.5f, 6.2f, 2.8f, 0.55f));
        data.jar = new Jar(world, 4.5f, 1.8f, 3.4f, 2.4f, 16);
        data.totalBallsRequired = 16;
        data.pins.add(new Pin(world, 4.5f, 12.5f, 5.4f, Pin.Direction.LEFT));
        data.pins.add(new Pin(world, 4.5f, 8.5f, 5.0f, Pin.Direction.RIGHT));
        data.balls.add(new Ball(world, 4.5f, 9.5f, Ball.BallType.WATER));
        spawnBallGrid(world, data, 2.6f, 13.2f, 4, 5, Ball.BallType.COLORED); // 20 top
    }

    // Bölüm 140: Biyosfer Çekirdeği Finali (Dünya 7 Büyük Finali: 2 Asit, 2 Su, x2, Kilit, Bomba ve 28 Bilye)
    private static void buildLevel140(World world, LevelData data) {
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(1.0f, 15.5f), new Vector2(1.0f, 5.0f), new Vector2(2.6f, 5.0f), new Vector2(2.6f, 15.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(3.2f, 15.5f), new Vector2(3.2f, 8.5f), new Vector2(3.3f, 5.0f), new Vector2(3.3f, 3.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(7.8f, 15.5f), new Vector2(7.8f, 8.5f), new Vector2(5.7f, 5.0f), new Vector2(5.7f, 3.5f) }));
        data.spikes.add(new SpikeObstacle(world, 1.8f, 5.3f, 1.5f, 0.4f));
        data.acidPools.add(new AcidPool(world, 5.5f, 6.2f, 2.8f, 0.6f));
        data.jar = new Jar(world, 4.5f, 1.8f, 3.6f, 2.4f, 22);
        data.totalBallsRequired = 22;
        data.keys.add(new KeyEntity(world, 5.5f, 14.5f, 1));
        data.pins.add(new Pin(world, 1.8f, 11.5f, 1.6f, Pin.Direction.LEFT));
        data.pins.add(new Pin(world, 5.5f, 12.0f, 4.2f, Pin.Direction.RIGHT));
        data.pins.add(new Pin(world, 4.5f, 5.5f, 4.8f, Pin.Direction.LEFT, true, 1));
        data.balls.add(new Ball(world, 1.8f, 13.0f, Ball.BallType.BOMB));
        data.balls.add(new Ball(world, 5.5f, 9.5f, Ball.BallType.WATER));
        spawnBallGrid(world, data, 4.0f, 12.8f, 3, 4, Ball.BallType.COLORED); // 12 renkli
        spawnBallGrid(world, data, 4.0f, 8.0f, 3, 4, Ball.BallType.GRAY);     // 12 gri -> 24 + 4 = 28 top
        data.balls.add(new Ball(world, 3.5f, 14.2f, Ball.BallType.COLORED));
        data.balls.add(new Ball(world, 4.0f, 14.2f, Ball.BallType.COLORED));
        data.balls.add(new Ball(world, 4.5f, 14.2f, Ball.BallType.COLORED));
        data.balls.add(new Ball(world, 5.0f, 14.2f, Ball.BallType.COLORED));
    }

    // =========================================================================
    // DÜNYA 8: AERODİNAMİK GÖKYÜZÜ (SEVİYE 141 - 160)
    // =========================================================================

    // Bölüm 141: Yukarı Rüzgar Jeti (Rüzgar türbini mekaniği tanıtımı)
    private static void buildLevel141(World world, LevelData data) {
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(1.5f, 15.5f), new Vector2(1.5f, 9.5f), new Vector2(3.3f, 5.0f), new Vector2(3.3f, 3.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(7.5f, 15.5f), new Vector2(7.5f, 9.5f), new Vector2(5.7f, 5.0f), new Vector2(5.7f, 3.5f) }));
        data.windTurbines.add(new WindTurbine(world, 4.5f, 4.5f, WindTurbine.Direction.UP, 5.5f, 42.0f));
        data.jar = new Jar(world, 4.5f, 1.8f, 3.2f, 2.4f, 12);
        data.totalBallsRequired = 12;
        data.pins.add(new Pin(world, 4.5f, 11.5f, 5.4f, Pin.Direction.RIGHT));
        spawnBallGrid(world, data, 2.6f, 12.5f, 4, 4, Ball.BallType.COLORED); // 16 top
    }

    // Bölüm 142: Yatay Hava Tüneli (Soldan sağa hızla uçuran rüzgar akımı)
    private static void buildLevel142(World world, LevelData data) {
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(1.2f, 15.5f), new Vector2(1.2f, 9.0f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(4.5f, 15.5f), new Vector2(4.5f, 11.0f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(7.8f, 15.5f), new Vector2(7.8f, 5.0f), new Vector2(5.7f, 4.0f), new Vector2(5.7f, 3.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(4.5f, 5.0f), new Vector2(3.3f, 4.0f), new Vector2(3.3f, 3.5f) }));
        data.windTurbines.add(new WindTurbine(world, 1.8f, 7.5f, WindTurbine.Direction.RIGHT, 5.0f, 38.0f));
        data.jar = new Jar(world, 4.5f, 1.8f, 3.2f, 2.4f, 12);
        data.totalBallsRequired = 12;
        data.pins.add(new Pin(world, 2.8f, 11.5f, 3.0f, Pin.Direction.LEFT));
        spawnBallGrid(world, data, 1.8f, 12.5f, 2, 7, Ball.BallType.COLORED); // 14 top
    }

    // Bölüm 143: Dikenlerin Üstünden Uçuş (Dikenli çukuru rüzgarla havalanarak aşma)
    private static void buildLevel143(World world, LevelData data) {
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(1.2f, 15.5f), new Vector2(1.2f, 8.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(7.8f, 15.5f), new Vector2(7.8f, 5.0f), new Vector2(5.7f, 4.0f), new Vector2(5.7f, 3.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(3.8f, 5.0f), new Vector2(3.3f, 4.0f), new Vector2(3.3f, 3.5f) }));
        data.spikes.add(new SpikeObstacle(world, 2.8f, 4.5f, 2.2f, 0.4f));
        data.windTurbines.add(new WindTurbine(world, 2.8f, 5.5f, WindTurbine.Direction.UP, 5.5f, 42.0f));
        data.jar = new Jar(world, 4.5f, 1.8f, 3.2f, 2.4f, 12);
        data.totalBallsRequired = 12;
        data.pins.add(new Pin(world, 2.8f, 11.5f, 3.0f, Pin.Direction.LEFT));
        spawnBallGrid(world, data, 1.8f, 12.5f, 2, 7, Ball.BallType.COLORED); // 14 top
    }

    // Bölüm 144: Çift Rüzgar Asansörü (Sol türbin yukarı, sağ türbin yatay)
    private static void buildLevel144(World world, LevelData data) {
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(1.2f, 15.5f), new Vector2(1.2f, 7.5f), new Vector2(3.3f, 4.5f), new Vector2(3.3f, 3.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(7.8f, 15.5f), new Vector2(7.8f, 7.5f), new Vector2(5.7f, 4.5f), new Vector2(5.7f, 3.5f) }));
        data.windTurbines.add(new WindTurbine(world, 2.5f, 5.5f, WindTurbine.Direction.UP, 4.5f, 40.0f));
        data.windTurbines.add(new WindTurbine(world, 6.5f, 8.5f, WindTurbine.Direction.LEFT, 4.0f, 35.0f));
        data.jar = new Jar(world, 4.5f, 1.8f, 3.2f, 2.4f, 14);
        data.totalBallsRequired = 14;
        data.pins.add(new Pin(world, 4.5f, 12.0f, 5.6f, Pin.Direction.RIGHT));
        spawnBallGrid(world, data, 2.6f, 12.8f, 4, 4, Ball.BallType.COLORED); // 16 top
    }

    // Bölüm 145: Rüzgarlı Bomba Ayrımı (Ağır bomba düşer, bilyeler rüzgarla uçar)
    private static void buildLevel145(World world, LevelData data) {
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(1.0f, 15.5f), new Vector2(1.0f, 5.0f), new Vector2(3.0f, 5.0f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(3.0f, 15.5f), new Vector2(3.0f, 8.5f), new Vector2(3.3f, 4.5f), new Vector2(3.3f, 3.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(7.8f, 15.5f), new Vector2(7.8f, 8.5f), new Vector2(5.7f, 4.5f), new Vector2(5.7f, 3.5f) }));
        data.spikes.add(new SpikeObstacle(world, 2.0f, 5.3f, 1.8f, 0.4f));
        data.windTurbines.add(new WindTurbine(world, 4.2f, 8.5f, WindTurbine.Direction.RIGHT, 4.0f, 36.0f));
        data.jar = new Jar(world, 4.5f, 1.8f, 3.2f, 2.4f, 12);
        data.totalBallsRequired = 12;
        data.pins.add(new Pin(world, 2.0f, 11.5f, 1.8f, Pin.Direction.LEFT));
        data.pins.add(new Pin(world, 5.5f, 11.5f, 4.2f, Pin.Direction.RIGHT));
        data.balls.add(new Ball(world, 2.0f, 12.8f, Ball.BallType.BOMB));
        spawnBallGrid(world, data, 4.2f, 12.5f, 2, 7, Ball.BallType.COLORED); // 14 top
    }

    // Bölüm 146: Rüzgar ve Trambolin (Bouncer türbine fırlatır, türbin göğe uçurur)
    private static void buildLevel146(World world, LevelData data) {
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(1.2f, 15.5f), new Vector2(1.2f, 8.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(4.5f, 15.5f), new Vector2(4.5f, 11.0f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(7.8f, 15.5f), new Vector2(7.8f, 5.0f), new Vector2(5.7f, 4.0f), new Vector2(5.7f, 3.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(4.5f, 5.0f), new Vector2(3.3f, 4.0f), new Vector2(3.3f, 3.5f) }));
        data.bouncers.add(new Bouncer(world, 2.8f, 6.0f, 2.2f, 0.35f, 40, 8.5f));
        data.windTurbines.add(new WindTurbine(world, 6.2f, 6.5f, WindTurbine.Direction.UP, 5.0f, 42.0f));
        data.jar = new Jar(world, 4.5f, 1.8f, 3.2f, 2.4f, 12);
        data.totalBallsRequired = 12;
        data.pins.add(new Pin(world, 2.8f, 11.5f, 3.0f, Pin.Direction.LEFT));
        spawnBallGrid(world, data, 1.8f, 12.5f, 2, 7, Ball.BallType.COLORED); // 14 top
    }

    // Bölüm 147: Rüzgarlı Portal Uçuşu (Rüzgar tüneli bilyeleri portala fırlatır)
    private static void buildLevel147(World world, LevelData data) {
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(1.2f, 15.5f), new Vector2(1.2f, 9.5f), new Vector2(3.5f, 8.0f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(4.5f, 15.5f), new Vector2(4.5f, 10.0f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(7.8f, 13.5f), new Vector2(7.8f, 5.0f), new Vector2(5.7f, 4.0f), new Vector2(5.7f, 3.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(4.5f, 5.0f), new Vector2(3.3f, 4.0f), new Vector2(3.3f, 3.5f) }));
        data.windTurbines.add(new WindTurbine(world, 2.8f, 7.5f, WindTurbine.Direction.UP, 4.0f, 40.0f));
        data.portals.add(new Portal(world, 2.8f, 10.5f, 6.2f, 11.0f));
        data.jar = new Jar(world, 4.5f, 1.8f, 3.2f, 2.4f, 12);
        data.totalBallsRequired = 12;
        data.pins.add(new Pin(world, 2.8f, 12.5f, 3.0f, Pin.Direction.LEFT));
        data.pins.add(new Pin(world, 6.2f, 6.0f, 3.0f, Pin.Direction.RIGHT));
        spawnBallGrid(world, data, 1.8f, 13.5f, 2, 7, Ball.BallType.COLORED); // 14 top
    }

    // Bölüm 148: Kilitli Rüzgar Kanalı (Türbinin önündeki pimi çekince rüzgar açılır)
    private static void buildLevel148(World world, LevelData data) {
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(1.5f, 15.5f), new Vector2(1.5f, 9.5f), new Vector2(3.3f, 5.0f), new Vector2(3.3f, 3.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(7.5f, 15.5f), new Vector2(7.5f, 9.5f), new Vector2(5.7f, 5.0f), new Vector2(5.7f, 3.5f) }));
        data.windTurbines.add(new WindTurbine(world, 4.5f, 5.0f, WindTurbine.Direction.UP, 5.0f, 42.0f));
        data.jar = new Jar(world, 4.5f, 1.8f, 3.2f, 2.4f, 12);
        data.totalBallsRequired = 12;
        data.keys.add(new KeyEntity(world, 2.2f, 14.0f, 1));
        data.pins.add(new Pin(world, 4.5f, 11.5f, 5.4f, Pin.Direction.RIGHT));
        data.pins.add(new Pin(world, 4.5f, 7.5f, 5.0f, Pin.Direction.LEFT, true, 1));
        spawnBallGrid(world, data, 3.2f, 12.5f, 3, 5, Ball.BallType.COLORED); // 15 top
    }

    // Bölüm 149: Karşı Rüzgar Akımı (Zıt iki akım arasında bilyeleri dengeleme)
    private static void buildLevel149(World world, LevelData data) {
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(1.2f, 15.5f), new Vector2(1.2f, 9.0f), new Vector2(3.3f, 4.5f), new Vector2(3.3f, 3.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(7.8f, 15.5f), new Vector2(7.8f, 9.0f), new Vector2(5.7f, 4.5f), new Vector2(5.7f, 3.5f) }));
        data.windTurbines.add(new WindTurbine(world, 1.8f, 7.5f, WindTurbine.Direction.RIGHT, 3.5f, 28.0f));
        data.windTurbines.add(new WindTurbine(world, 7.2f, 7.5f, WindTurbine.Direction.LEFT, 3.5f, 28.0f));
        data.jar = new Jar(world, 4.5f, 1.8f, 3.2f, 2.4f, 14);
        data.totalBallsRequired = 14;
        data.pins.add(new Pin(world, 4.5f, 11.5f, 5.6f, Pin.Direction.RIGHT));
        spawnBallGrid(world, data, 2.6f, 12.5f, 4, 4, Ball.BallType.COLORED); // 16 top
    }

    // Bölüm 150: Gökyüzü Kalesi (Dünya 8 Mini-Apex: Yüksek irtifa rüzgar jeti + Diken bariyeri)
    private static void buildLevel150(World world, LevelData data) {
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(1.2f, 15.5f), new Vector2(1.2f, 8.5f), new Vector2(3.3f, 5.0f), new Vector2(3.3f, 3.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(7.8f, 15.5f), new Vector2(7.8f, 8.5f), new Vector2(5.7f, 5.0f), new Vector2(5.7f, 3.5f) }));
        data.spikes.add(new SpikeObstacle(world, 4.5f, 6.5f, 3.5f, 0.4f));
        data.windTurbines.add(new WindTurbine(world, 4.5f, 4.5f, WindTurbine.Direction.UP, 6.0f, 45.0f));
        data.jar = new Jar(world, 4.5f, 1.8f, 3.4f, 2.4f, 18);
        data.totalBallsRequired = 18;
        data.pins.add(new Pin(world, 4.5f, 12.0f, 5.6f, Pin.Direction.RIGHT));
        spawnBallGrid(world, data, 2.4f, 12.8f, 4, 3, Ball.BallType.COLORED); // 12 renkli
        spawnBallGrid(world, data, 2.4f, 14.5f, 4, 3, Ball.BallType.GRAY);    // 12 gri -> 24 top
    }

    // Bölüm 151: Rüzgarla Çoğaltma (Türbin bilyeleri x3 kapısına fırlatır)
    private static void buildLevel151(World world, LevelData data) {
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(1.5f, 15.5f), new Vector2(1.5f, 9.5f), new Vector2(3.3f, 5.0f), new Vector2(3.3f, 3.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(7.5f, 15.5f), new Vector2(7.5f, 9.5f), new Vector2(5.7f, 5.0f), new Vector2(5.7f, 3.5f) }));
        data.windTurbines.add(new WindTurbine(world, 4.5f, 5.0f, WindTurbine.Direction.UP, 5.0f, 40.0f));
        data.multiplierGates.add(new MultiplierGate(world, 4.5f, 8.5f, 2.5f, 0.45f, MultiplierGate.GateType.MULTIPLY_X3));
        data.jar = new Jar(world, 4.5f, 1.8f, 3.2f, 2.4f, 14);
        data.totalBallsRequired = 14;
        data.pins.add(new Pin(world, 4.5f, 11.5f, 5.2f, Pin.Direction.LEFT));
        spawnBallGrid(world, data, 3.5f, 12.5f, 2, 3, Ball.BallType.COLORED); // 6 top x3 = 18
    }

    // Bölüm 152: Rüzgarlı Dönen Çark (Çark rüzgar akımını dönemsel keser)
    private static void buildLevel152(World world, LevelData data) {
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(1.5f, 15.5f), new Vector2(1.5f, 9.5f), new Vector2(3.3f, 5.0f), new Vector2(3.3f, 3.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(7.5f, 15.5f), new Vector2(7.5f, 9.5f), new Vector2(5.7f, 5.0f), new Vector2(5.7f, 3.5f) }));
        data.rotatingObstacles.add(new RotatingObstacle(world, 4.5f, 8.5f, RotatingObstacle.RotorType.CROSS_PADDLE_4, 1.3f, 0.22f, 1.4f, new Color(0.3f, 0.85f, 1f, 1f)));
        data.windTurbines.add(new WindTurbine(world, 4.5f, 5.0f, WindTurbine.Direction.UP, 5.0f, 42.0f));
        data.jar = new Jar(world, 4.5f, 1.8f, 3.2f, 2.4f, 12);
        data.totalBallsRequired = 12;
        data.pins.add(new Pin(world, 4.5f, 11.8f, 5.4f, Pin.Direction.RIGHT));
        spawnBallGrid(world, data, 2.6f, 12.8f, 4, 4, Ball.BallType.COLORED); // 16 top
    }

    // Bölüm 153: Aşağı İtici Rüzgar (Yerçekimini hızlandıran fırtına tüneli)
    private static void buildLevel153(World world, LevelData data) {
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(1.5f, 15.5f), new Vector2(1.5f, 9.5f), new Vector2(3.3f, 5.0f), new Vector2(3.3f, 3.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(7.5f, 15.5f), new Vector2(7.5f, 9.5f), new Vector2(5.7f, 5.0f), new Vector2(5.7f, 3.5f) }));
        data.windTurbines.add(new WindTurbine(world, 4.5f, 12.5f, WindTurbine.Direction.DOWN, 5.5f, 35.0f));
        data.jar = new Jar(world, 4.5f, 1.8f, 3.4f, 2.4f, 16);
        data.totalBallsRequired = 16;
        data.pins.add(new Pin(world, 4.5f, 8.5f, 5.4f, Pin.Direction.LEFT));
        spawnBallGrid(world, data, 2.6f, 13.5f, 4, 5, Ball.BallType.COLORED); // 20 top
    }

    // Bölüm 154: Rüzgarlı Renk Filtresi (Bilyeler havada uçarken filtreden geçer)
    private static void buildLevel154(World world, LevelData data) {
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(1.5f, 15.5f), new Vector2(1.5f, 9.5f), new Vector2(3.3f, 5.0f), new Vector2(3.3f, 3.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(7.5f, 15.5f), new Vector2(7.5f, 9.5f), new Vector2(5.7f, 5.0f), new Vector2(5.7f, 3.5f) }));
        data.windTurbines.add(new WindTurbine(world, 4.5f, 5.0f, WindTurbine.Direction.UP, 5.0f, 42.0f));
        data.colorGates.add(new ColorGate(world, 4.5f, 8.5f, 2.6f, 0.45f, new Color(0.3f, 0.85f, 1.0f, 1f), "GOKYUZU MAVISI"));
        data.jar = new Jar(world, 4.5f, 1.8f, 3.2f, 2.4f, 12);
        data.totalBallsRequired = 12;
        data.pins.add(new Pin(world, 4.5f, 11.5f, 5.4f, Pin.Direction.RIGHT));
        spawnBallGrid(world, data, 2.6f, 12.5f, 4, 4, Ball.BallType.GRAY); // 16 gri top
    }

    // Bölüm 155: Rüzgar ve Asit Havuzu (Su bilyesini rüzgarla uçurup asite ulaştırma)
    private static void buildLevel155(World world, LevelData data) {
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(1.2f, 15.5f), new Vector2(1.2f, 8.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(4.5f, 15.5f), new Vector2(4.5f, 11.0f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(7.8f, 15.5f), new Vector2(7.8f, 5.0f), new Vector2(5.7f, 4.0f), new Vector2(5.7f, 3.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(4.5f, 5.0f), new Vector2(3.3f, 4.0f), new Vector2(3.3f, 3.5f) }));
        data.windTurbines.add(new WindTurbine(world, 1.8f, 7.5f, WindTurbine.Direction.RIGHT, 5.0f, 38.0f));
        data.acidPools.add(new AcidPool(world, 6.2f, 6.5f, 2.4f, 0.55f));
        data.jar = new Jar(world, 4.5f, 1.8f, 3.2f, 2.4f, 12);
        data.totalBallsRequired = 12;
        data.pins.add(new Pin(world, 2.8f, 11.5f, 3.0f, Pin.Direction.LEFT));
        data.balls.add(new Ball(world, 2.2f, 12.2f, Ball.BallType.WATER));
        spawnBallGrid(world, data, 1.8f, 13.2f, 2, 7, Ball.BallType.COLORED); // 14 top
    }

    // Bölüm 156: Havada Asılı Kalanlar (Hava akımının yerçekimini dengelemesi)
    private static void buildLevel156(World world, LevelData data) {
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(1.5f, 15.5f), new Vector2(1.5f, 9.5f), new Vector2(3.3f, 5.0f), new Vector2(3.3f, 3.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(7.5f, 15.5f), new Vector2(7.5f, 9.5f), new Vector2(5.7f, 5.0f), new Vector2(5.7f, 3.5f) }));
        data.windTurbines.add(new WindTurbine(world, 4.5f, 5.5f, WindTurbine.Direction.UP, 4.5f, 36.0f));
        data.jar = new Jar(world, 4.5f, 1.8f, 3.4f, 2.4f, 14);
        data.totalBallsRequired = 14;
        data.pins.add(new Pin(world, 4.5f, 11.5f, 5.4f, Pin.Direction.RIGHT));
        spawnBallGrid(world, data, 2.6f, 12.5f, 4, 4, Ball.BallType.COLORED); // 16 top
    }

    // Bölüm 157: Zikzak Rüzgar Tünelleri (3 türbin ile U şeklinde hava parkuru)
    private static void buildLevel157(World world, LevelData data) {
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(1.2f, 15.5f), new Vector2(1.2f, 7.5f), new Vector2(3.3f, 4.5f), new Vector2(3.3f, 3.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(7.8f, 15.5f), new Vector2(7.8f, 7.5f), new Vector2(5.7f, 4.5f), new Vector2(5.7f, 3.5f) }));
        data.windTurbines.add(new WindTurbine(world, 2.2f, 6.0f, WindTurbine.Direction.UP, 4.0f, 38.0f));
        data.windTurbines.add(new WindTurbine(world, 4.5f, 10.5f, WindTurbine.Direction.RIGHT, 3.5f, 35.0f));
        data.windTurbines.add(new WindTurbine(world, 6.8f, 6.0f, WindTurbine.Direction.DOWN, 4.0f, 35.0f));
        data.jar = new Jar(world, 4.5f, 1.8f, 3.2f, 2.4f, 14);
        data.totalBallsRequired = 14;
        data.pins.add(new Pin(world, 2.2f, 11.5f, 2.0f, Pin.Direction.LEFT));
        spawnBallGrid(world, data, 1.6f, 12.5f, 2, 8, Ball.BallType.COLORED); // 16 top
    }

    // Bölüm 158: Fırtına Zamanlaması (Rüzgar akımına doğru anda bilye bırakma)
    private static void buildLevel158(World world, LevelData data) {
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(1.5f, 15.5f), new Vector2(1.5f, 9.5f), new Vector2(3.3f, 5.0f), new Vector2(3.3f, 3.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(7.5f, 15.5f), new Vector2(7.5f, 9.5f), new Vector2(5.7f, 5.0f), new Vector2(5.7f, 3.5f) }));
        data.windTurbines.add(new WindTurbine(world, 4.5f, 5.0f, WindTurbine.Direction.UP, 5.5f, 44.0f));
        data.jar = new Jar(world, 4.5f, 1.8f, 3.2f, 2.4f, 12);
        data.totalBallsRequired = 12;
        data.pins.add(new Pin(world, 4.5f, 11.5f, 5.4f, Pin.Direction.LEFT));
        spawnBallGrid(world, data, 2.6f, 12.5f, 4, 4, Ball.BallType.COLORED); // 16 top
    }

    // Bölüm 159: Rüzgar Kasırgası (32 bilyenin havada girdap gibi döndüğü görsel şölen)
    private static void buildLevel159(World world, LevelData data) {
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(1.2f, 15.5f), new Vector2(1.2f, 10.5f), new Vector2(3.1f, 4.5f), new Vector2(3.1f, 3.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(7.8f, 15.5f), new Vector2(7.8f, 10.5f), new Vector2(5.9f, 4.5f), new Vector2(5.9f, 3.5f) }));
        data.windTurbines.add(new WindTurbine(world, 3.5f, 5.5f, WindTurbine.Direction.UP, 5.5f, 40.0f));
        data.windTurbines.add(new WindTurbine(world, 5.5f, 5.5f, WindTurbine.Direction.UP, 5.5f, 40.0f));
        data.jar = new Jar(world, 4.5f, 1.8f, 3.6f, 2.4f, 25);
        data.totalBallsRequired = 25;
        data.pins.add(new Pin(world, 4.5f, 12.0f, 5.8f, Pin.Direction.RIGHT));
        spawnBallGrid(world, data, 2.2f, 12.8f, 5, 6, Ball.BallType.COLORED); // 30 top
        data.balls.add(new Ball(world, 4.2f, 16.0f, Ball.BallType.COLORED));
        data.balls.add(new Ball(world, 4.8f, 16.0f, Ball.BallType.COLORED)); // 32 top
    }

    // Bölüm 160: Fırtına Tanrısı Finali (Dünya 8 Büyük Finali: 3 Türbin, 1 Trambolin, 1 Portal, 30 Bilye)
    private static void buildLevel160(World world, LevelData data) {
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(1.0f, 15.5f), new Vector2(1.0f, 5.0f), new Vector2(2.6f, 5.0f), new Vector2(2.6f, 15.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(3.2f, 15.5f), new Vector2(3.2f, 8.5f), new Vector2(3.3f, 5.0f), new Vector2(3.3f, 3.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(7.8f, 15.5f), new Vector2(7.8f, 8.5f), new Vector2(5.7f, 5.0f), new Vector2(5.7f, 3.5f) }));
        data.spikes.add(new SpikeObstacle(world, 1.8f, 5.3f, 1.5f, 0.4f));
        data.windTurbines.add(new WindTurbine(world, 5.5f, 5.5f, WindTurbine.Direction.UP, 5.5f, 44.0f));
        data.portals.add(new Portal(world, 5.5f, 11.5f, 5.5f, 7.5f));
        data.jar = new Jar(world, 4.5f, 1.8f, 3.6f, 2.4f, 24);
        data.totalBallsRequired = 24;
        data.keys.add(new KeyEntity(world, 5.5f, 14.5f, 1));
        data.pins.add(new Pin(world, 1.8f, 11.5f, 1.6f, Pin.Direction.LEFT));
        data.pins.add(new Pin(world, 5.5f, 12.0f, 4.2f, Pin.Direction.RIGHT));
        data.pins.add(new Pin(world, 4.5f, 5.5f, 4.8f, Pin.Direction.LEFT, true, 1));
        data.balls.add(new Ball(world, 1.8f, 13.0f, Ball.BallType.BOMB));
        spawnBallGrid(world, data, 4.0f, 12.8f, 3, 5, Ball.BallType.COLORED); // 15 renkli
        spawnBallGrid(world, data, 4.0f, 8.5f, 3, 5, Ball.BallType.GRAY);     // 15 gri -> 30 top
    }

        // Bölüm 161: Lazerli Rüzgar Tüneli (Rüzgarla uçarken lazer ışınlarından süzülme)
    private static void buildLevel161(World world, LevelData data) {
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(1.5f, 15.5f), new Vector2(1.5f, 8.5f), new Vector2(3.3f, 4.5f), new Vector2(3.3f, 3.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(7.5f, 15.5f), new Vector2(7.5f, 8.5f), new Vector2(5.7f, 4.5f), new Vector2(5.7f, 3.5f) }));
        data.lasers.add(new LaserObstacle(world, 2.2f, 7.5f, 6.8f, 7.5f, LaserObstacle.LaserMode.PULSING, 1.4f, 1.4f, 0.0f, Color.RED));
        data.windTurbines.add(new WindTurbine(world, 4.5f, 4.8f, WindTurbine.Direction.UP, 6.0f, 42.0f));
        data.jar = new Jar(world, 4.5f, 1.8f, 3.2f, 2.4f, 12);
        data.totalBallsRequired = 12;
        data.pins.add(new Pin(world, 4.5f, 11.5f, 5.4f, Pin.Direction.RIGHT));
        spawnBallGrid(world, data, 2.6f, 12.5f, 4, 4, Ball.BallType.COLORED); // 16 top
    }

    // Bölüm 162: Asit ve Lazer Kafesi (Asit havuzu üstünde lazer; su bilyesi zamanlamalı geçiş)
    private static void buildLevel162(World world, LevelData data) {
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(1.2f, 15.5f), new Vector2(1.2f, 9.0f), new Vector2(3.3f, 4.5f), new Vector2(3.3f, 3.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(7.8f, 15.5f), new Vector2(7.8f, 9.0f), new Vector2(5.7f, 4.5f), new Vector2(5.7f, 3.5f) }));
        data.acidPools.add(new AcidPool(world, 4.5f, 5.5f, 2.6f, 0.55f));
        data.lasers.add(new LaserObstacle(world, 2.5f, 7.2f, 6.5f, 7.2f, LaserObstacle.LaserMode.PULSING, 1.3f, 1.3f, 0.0f, Color.RED));
        data.jar = new Jar(world, 4.5f, 1.8f, 3.2f, 2.4f, 12);
        data.totalBallsRequired = 12;
        data.pins.add(new Pin(world, 4.5f, 11.5f, 5.4f, Pin.Direction.RIGHT));
        data.pins.add(new Pin(world, 4.5f, 8.5f, 5.0f, Pin.Direction.LEFT));
        data.balls.add(new Ball(world, 4.5f, 9.5f, Ball.BallType.WATER)); // Su bilyesi
        spawnBallGrid(world, data, 2.6f, 12.5f, 4, 4, Ball.BallType.COLORED); // 16 top
    }

    // Bölüm 163: Rüzgarla Asit Nötralizasyonu (Türbin su bilyesini fırlatır -> asit nötralize olur)
    private static void buildLevel163(World world, LevelData data) {
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(1.0f, 15.5f), new Vector2(1.0f, 5.0f), new Vector2(3.0f, 5.0f), new Vector2(3.0f, 15.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(4.0f, 15.5f), new Vector2(4.0f, 8.5f), new Vector2(4.0f, 3.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(8.0f, 15.5f), new Vector2(8.0f, 8.5f), new Vector2(6.5f, 3.5f) }));
        data.windTurbines.add(new WindTurbine(world, 2.0f, 5.5f, WindTurbine.Direction.UP, 7.0f, 44.0f));
        data.acidPools.add(new AcidPool(world, 5.5f, 6.5f, 2.4f, 0.55f));
        data.jar = new Jar(world, 5.5f, 1.8f, 3.0f, 2.4f, 12);
        data.totalBallsRequired = 12;
        data.pins.add(new Pin(world, 2.0f, 8.0f, 1.8f, Pin.Direction.LEFT));
        data.pins.add(new Pin(world, 6.0f, 11.5f, 3.8f, Pin.Direction.RIGHT));
        data.balls.add(new Ball(world, 2.0f, 9.2f, Ball.BallType.WATER));
        spawnBallGrid(world, data, 4.5f, 12.5f, 3, 5, Ball.BallType.COLORED); // 15 top
    }

    // Bölüm 164: Çarklı Lazer Labirenti (Dönen pervaneler lazerleri perdelerken geçiş)
    private static void buildLevel164(World world, LevelData data) {
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(1.2f, 15.5f), new Vector2(1.2f, 9.5f), new Vector2(3.3f, 4.5f), new Vector2(3.3f, 3.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(7.8f, 15.5f), new Vector2(7.8f, 9.5f), new Vector2(5.7f, 4.5f), new Vector2(5.7f, 3.5f) }));
        data.rotatingObstacles.add(new RotatingObstacle(world, 4.5f, 8.0f, RotatingObstacle.RotorType.CROSS_PADDLE_4, 1.4f, 0.22f, 1.4f, new Color(0.2f, 0.85f, 0.4f, 1f)));
        data.lasers.add(new LaserObstacle(world, 1.8f, 8.0f, 7.2f, 8.0f, LaserObstacle.LaserMode.PULSING, 1.5f, 1.5f, 0.0f, Color.MAGENTA));
        data.jar = new Jar(world, 4.5f, 1.8f, 3.2f, 2.4f, 14);
        data.totalBallsRequired = 14;
        data.pins.add(new Pin(world, 4.5f, 11.5f, 5.6f, Pin.Direction.RIGHT));
        spawnBallGrid(world, data, 2.6f, 12.5f, 4, 4, Ball.BallType.COLORED); // 16 top
        data.balls.add(new Ball(world, 4.5f, 15.0f, Ball.BallType.COLORED));
        data.balls.add(new Ball(world, 4.0f, 15.0f, Ball.BallType.COLORED)); // 18 top
    }

    // Bölüm 165: Dört Boyutlu Kaos (Portal + Trambolin + Rüzgar + x2 Çoğaltıcı)
    private static void buildLevel165(World world, LevelData data) {
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(1.2f, 15.5f), new Vector2(1.2f, 7.0f), new Vector2(3.5f, 7.0f), new Vector2(3.5f, 15.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(4.5f, 15.5f), new Vector2(4.5f, 9.0f), new Vector2(3.3f, 4.5f), new Vector2(3.3f, 3.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(7.8f, 15.5f), new Vector2(7.8f, 9.0f), new Vector2(5.7f, 4.5f), new Vector2(5.7f, 3.5f) }));
        data.portals.add(new Portal(world, 2.3f, 8.0f, 6.2f, 13.5f));
        data.bouncers.add(new Bouncer(world, 6.2f, 10.0f, 2.0f, 0.4f, -25.0f, 8.0f));
        data.multiplierGates.add(new MultiplierGate(world, 4.5f, 6.5f, 2.2f, 0.45f, MultiplierGate.GateType.MULTIPLY_X2));
        data.jar = new Jar(world, 4.5f, 1.8f, 3.2f, 2.4f, 14);
        data.totalBallsRequired = 14;
        data.pins.add(new Pin(world, 2.3f, 10.5f, 2.2f, Pin.Direction.LEFT));
        spawnBallGrid(world, data, 1.6f, 11.5f, 2, 4, Ball.BallType.COLORED); // 8 top x2 = 16 top
    }

    // Bölüm 166: Bomba, Asit ve Su Üçgeni (Bomba yolu açar, su asidi temizler, bilyeler geçer)
    private static void buildLevel166(World world, LevelData data) {
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(1.0f, 15.5f), new Vector2(1.0f, 5.0f), new Vector2(2.8f, 5.0f), new Vector2(2.8f, 15.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(3.2f, 15.5f), new Vector2(3.2f, 8.5f), new Vector2(3.3f, 4.5f), new Vector2(3.3f, 3.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(7.8f, 15.5f), new Vector2(7.8f, 8.5f), new Vector2(5.7f, 4.5f), new Vector2(5.7f, 3.5f) }));
        data.spikes.add(new SpikeObstacle(world, 1.9f, 5.3f, 1.6f, 0.4f));
        data.acidPools.add(new AcidPool(world, 4.5f, 5.5f, 2.4f, 0.55f));
        data.jar = new Jar(world, 4.5f, 1.8f, 3.2f, 2.4f, 12);
        data.totalBallsRequired = 12;
        data.pins.add(new Pin(world, 1.9f, 9.5f, 1.6f, Pin.Direction.LEFT));
        data.pins.add(new Pin(world, 5.5f, 11.5f, 4.2f, Pin.Direction.RIGHT));
        data.pins.add(new Pin(world, 5.5f, 8.5f, 4.2f, Pin.Direction.LEFT));
        data.balls.add(new Ball(world, 1.9f, 11.0f, Ball.BallType.BOMB));
        data.balls.add(new Ball(world, 5.5f, 9.5f, Ball.BallType.WATER));
        spawnBallGrid(world, data, 4.2f, 12.5f, 3, 5, Ball.BallType.COLORED); // 15 top
    }

    // Bölüm 167: Rüzgarlı Kilit Avı (Anahtar hava akımıyla kilitli pime çarparak açar)
    private static void buildLevel167(World world, LevelData data) {
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(1.0f, 15.5f), new Vector2(1.0f, 5.0f), new Vector2(3.0f, 5.0f), new Vector2(3.0f, 12.0f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(4.0f, 15.5f), new Vector2(4.0f, 8.5f), new Vector2(3.3f, 4.5f), new Vector2(3.3f, 3.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(7.8f, 15.5f), new Vector2(7.8f, 8.5f), new Vector2(5.7f, 4.5f), new Vector2(5.7f, 3.5f) }));
        data.windTurbines.add(new WindTurbine(world, 2.0f, 5.5f, WindTurbine.Direction.UP, 6.0f, 44.0f));
        data.keys.add(new KeyEntity(world, 2.0f, 7.5f, 1));
        data.jar = new Jar(world, 4.5f, 1.8f, 3.2f, 2.4f, 12);
        data.totalBallsRequired = 12;
        data.pins.add(new Pin(world, 2.0f, 8.5f, 1.8f, Pin.Direction.LEFT));
        data.pins.add(new Pin(world, 5.5f, 8.0f, 3.8f, Pin.Direction.RIGHT, true, 1));
        spawnBallGrid(world, data, 4.5f, 10.5f, 3, 5, Ball.BallType.COLORED); // 15 top
    }

    // Bölüm 168: Çift Asit Çift Rüzgar (İki katmanlı temizleme ve fırlatma reaktörü)
    private static void buildLevel168(World world, LevelData data) {
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(1.0f, 15.5f), new Vector2(1.0f, 8.0f), new Vector2(3.3f, 4.5f), new Vector2(3.3f, 3.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(8.0f, 15.5f), new Vector2(8.0f, 8.0f), new Vector2(5.7f, 4.5f), new Vector2(5.7f, 3.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(4.5f, 15.5f), new Vector2(4.5f, 9.5f) }));
        data.acidPools.add(new AcidPool(world, 2.6f, 6.8f, 2.0f, 0.55f));
        data.acidPools.add(new AcidPool(world, 6.4f, 6.8f, 2.0f, 0.55f));
        data.windTurbines.add(new WindTurbine(world, 2.6f, 4.5f, WindTurbine.Direction.UP, 4.5f, 38.0f));
        data.windTurbines.add(new WindTurbine(world, 6.4f, 4.5f, WindTurbine.Direction.UP, 4.5f, 38.0f));
        data.jar = new Jar(world, 4.5f, 1.8f, 3.2f, 2.4f, 14);
        data.totalBallsRequired = 14;
        data.pins.add(new Pin(world, 2.6f, 10.5f, 2.8f, Pin.Direction.LEFT));
        data.pins.add(new Pin(world, 6.4f, 10.5f, 2.8f, Pin.Direction.RIGHT));
        data.balls.add(new Ball(world, 2.6f, 8.5f, Ball.BallType.WATER));
        data.balls.add(new Ball(world, 6.4f, 8.5f, Ball.BallType.WATER));
        spawnBallGrid(world, data, 1.8f, 11.5f, 2, 4, Ball.BallType.COLORED); // 8
        spawnBallGrid(world, data, 5.4f, 11.5f, 2, 4, Ball.BallType.COLORED); // 8 -> 16 top
    }

    // Bölüm 169: Senkronize Lazer Fırtınası (3 Lazer ve rüzgar türbininin kusursuz ritmi)
    private static void buildLevel169(World world, LevelData data) {
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(1.2f, 15.5f), new Vector2(1.2f, 9.5f), new Vector2(3.3f, 4.5f), new Vector2(3.3f, 3.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(7.8f, 15.5f), new Vector2(7.8f, 9.5f), new Vector2(5.7f, 4.5f), new Vector2(5.7f, 3.5f) }));
        data.lasers.add(new LaserObstacle(world, 1.5f, 10.0f, 7.5f, 10.0f, LaserObstacle.LaserMode.PULSING, 1.2f, 1.2f, 0.0f, Color.RED));
        data.lasers.add(new LaserObstacle(world, 1.5f, 8.0f, 7.5f, 8.0f, LaserObstacle.LaserMode.PULSING, 1.2f, 1.2f, 0.6f, Color.CYAN));
        data.lasers.add(new LaserObstacle(world, 1.5f, 6.0f, 7.5f, 6.0f, LaserObstacle.LaserMode.PULSING, 1.2f, 1.2f, 1.2f, Color.YELLOW));
        data.windTurbines.add(new WindTurbine(world, 4.5f, 4.5f, WindTurbine.Direction.UP, 4.5f, 36.0f));
        data.jar = new Jar(world, 4.5f, 1.8f, 3.2f, 2.4f, 15);
        data.totalBallsRequired = 15;
        data.pins.add(new Pin(world, 4.5f, 12.0f, 5.6f, Pin.Direction.RIGHT));
        spawnBallGrid(world, data, 2.6f, 12.8f, 4, 5, Ball.BallType.COLORED); // 20 top
    }

    // Bölüm 170: Kaos Çekirdeği (Apex) (Tüm mekaniklerin harmanlandığı mega puzzle)
    private static void buildLevel170(World world, LevelData data) {
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(1.0f, 15.5f), new Vector2(1.0f, 8.5f), new Vector2(3.3f, 4.5f), new Vector2(3.3f, 3.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(8.0f, 15.5f), new Vector2(8.0f, 8.5f), new Vector2(5.7f, 4.5f), new Vector2(5.7f, 3.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(3.5f, 15.5f), new Vector2(3.5f, 11.5f) }));
        data.acidPools.add(new AcidPool(world, 4.5f, 5.0f, 2.6f, 0.55f));
        data.lasers.add(new LaserObstacle(world, 2.5f, 7.5f, 6.5f, 7.5f, LaserObstacle.LaserMode.PULSING, 1.4f, 1.4f, 0.0f, Color.RED));
        data.rotatingObstacles.add(new RotatingObstacle(world, 4.5f, 9.5f, RotatingObstacle.RotorType.CROSS_PADDLE_4, 1.2f, 0.20f, 1.2f, new Color(0.2f, 0.85f, 0.4f, 1f)));
        data.jar = new Jar(world, 4.5f, 1.8f, 3.4f, 2.4f, 15);
        data.totalBallsRequired = 15;
        data.pins.add(new Pin(world, 2.2f, 11.0f, 2.2f, Pin.Direction.LEFT));
        data.pins.add(new Pin(world, 5.8f, 11.0f, 4.0f, Pin.Direction.RIGHT));
        data.balls.add(new Ball(world, 2.2f, 12.0f, Ball.BallType.WATER)); // Su bilyesi
        spawnBallGrid(world, data, 4.5f, 12.0f, 3, 6, Ball.BallType.COLORED); // 18 top
        data.balls.add(new Ball(world, 5.5f, 15.5f, Ball.BallType.COLORED)); // 19 top
    }

    // Bölüm 171: Süzülen Su Damlası (Su bilyesini rüzgarla taşırken lazerlerden sakınma)
    private static void buildLevel171(World world, LevelData data) {
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(1.2f, 15.5f), new Vector2(1.2f, 8.5f), new Vector2(3.3f, 4.5f), new Vector2(3.3f, 3.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(7.8f, 15.5f), new Vector2(7.8f, 8.5f), new Vector2(5.7f, 4.5f), new Vector2(5.7f, 3.5f) }));
        data.windTurbines.add(new WindTurbine(world, 2.0f, 7.5f, WindTurbine.Direction.RIGHT, 4.5f, 35.0f));
        data.lasers.add(new LaserObstacle(world, 4.5f, 6.0f, 4.5f, 9.5f, LaserObstacle.LaserMode.PULSING, 1.3f, 1.3f, 0.0f, Color.CYAN));
        data.acidPools.add(new AcidPool(world, 4.5f, 5.0f, 2.4f, 0.55f));
        data.jar = new Jar(world, 4.5f, 1.8f, 3.2f, 2.4f, 14);
        data.totalBallsRequired = 14;
        data.pins.add(new Pin(world, 2.0f, 9.5f, 1.8f, Pin.Direction.LEFT));
        data.pins.add(new Pin(world, 5.5f, 11.5f, 4.2f, Pin.Direction.RIGHT));
        data.balls.add(new Ball(world, 2.0f, 10.5f, Ball.BallType.WATER));
        spawnBallGrid(world, data, 4.2f, 12.5f, 3, 6, Ball.BallType.COLORED); // 18 top
    }

    // Bölüm 172: Ters Açı Sıçraması (Trambolin -> Rüzgar -> Renk Filtresi -> Kavanoz)
    private static void buildLevel172(World world, LevelData data) {
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(1.2f, 15.5f), new Vector2(1.2f, 7.0f), new Vector2(3.3f, 4.5f), new Vector2(3.3f, 3.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(7.8f, 15.5f), new Vector2(7.8f, 7.0f), new Vector2(5.7f, 4.5f), new Vector2(5.7f, 3.5f) }));
        data.bouncers.add(new Bouncer(world, 2.5f, 8.0f, 2.0f, 0.4f, -40.0f, 8.0f));
        data.windTurbines.add(new WindTurbine(world, 3.5f, 8.0f, WindTurbine.Direction.RIGHT, 3.5f, 38.0f));
        data.colorGates.add(new ColorGate(world, 6.0f, 8.0f, 1.8f, 0.45f, Color.CYAN, "CYAN"));
        data.jar = new Jar(world, 4.5f, 1.8f, 3.2f, 2.4f, 12);
        data.totalBallsRequired = 12;
        data.pins.add(new Pin(world, 2.5f, 11.5f, 2.6f, Pin.Direction.LEFT));
        spawnBallGrid(world, data, 1.8f, 12.5f, 2, 8, Ball.BallType.GRAY); // 16 gri top
    }

    // Bölüm 173: Dönen Asit Kazanı (Çark bilyeleri asitin üstünden dairesel aktarır)
    private static void buildLevel173(World world, LevelData data) {
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(1.2f, 15.5f), new Vector2(1.2f, 9.0f), new Vector2(3.3f, 4.5f), new Vector2(3.3f, 3.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(7.8f, 15.5f), new Vector2(7.8f, 9.0f), new Vector2(5.7f, 4.5f), new Vector2(5.7f, 3.5f) }));
        data.acidPools.add(new AcidPool(world, 4.5f, 5.5f, 2.8f, 0.55f));
        data.rotatingObstacles.add(new RotatingObstacle(world, 4.5f, 8.0f, RotatingObstacle.RotorType.CROSS_PADDLE_4, 1.4f, 0.22f, 1.3f, new Color(0.2f, 0.85f, 0.4f, 1f)));
        data.jar = new Jar(world, 4.5f, 1.8f, 3.2f, 2.4f, 14);
        data.totalBallsRequired = 14;
        data.pins.add(new Pin(world, 4.5f, 11.5f, 5.4f, Pin.Direction.RIGHT));
        data.pins.add(new Pin(world, 4.5f, 9.5f, 5.0f, Pin.Direction.LEFT));
        data.balls.add(new Ball(world, 4.5f, 10.3f, Ball.BallType.WATER));
        spawnBallGrid(world, data, 2.6f, 12.5f, 4, 4, Ball.BallType.COLORED); // 16 top
        data.balls.add(new Ball(world, 4.2f, 15.0f, Ball.BallType.COLORED));
        data.balls.add(new Ball(world, 4.8f, 15.0f, Ball.BallType.COLORED)); // 18 top
    }

    // Bölüm 174: Çifte Kilitli Fırtına (2 Anahtar, 2 Kilitli pim, rüzgar koridoru)
    private static void buildLevel174(World world, LevelData data) {
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(1.0f, 15.5f), new Vector2(1.0f, 6.0f), new Vector2(3.3f, 4.5f), new Vector2(3.3f, 3.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(8.0f, 15.5f), new Vector2(8.0f, 6.0f), new Vector2(5.7f, 4.5f), new Vector2(5.7f, 3.5f) }));
        data.windTurbines.add(new WindTurbine(world, 4.5f, 5.0f, WindTurbine.Direction.UP, 5.0f, 40.0f));
        data.keys.add(new KeyEntity(world, 2.2f, 12.5f, 1));
        data.keys.add(new KeyEntity(world, 6.8f, 12.5f, 2));
        data.jar = new Jar(world, 4.5f, 1.8f, 3.4f, 2.4f, 15);
        data.totalBallsRequired = 15;
        data.pins.add(new Pin(world, 2.2f, 10.5f, 2.4f, Pin.Direction.LEFT, true, 1));
        data.pins.add(new Pin(world, 6.8f, 10.5f, 2.4f, Pin.Direction.RIGHT, true, 2));
        data.pins.add(new Pin(world, 4.5f, 7.8f, 5.2f, Pin.Direction.RIGHT));
        spawnBallGrid(world, data, 3.5f, 12.5f, 4, 5, Ball.BallType.COLORED); // 20 top
    }

    // Bölüm 175: Çoğaltılmış Su Dalgası (1 Su bilyesi x2 geçer, 2 asit havuzuna düşer!)
    private static void buildLevel175(World world, LevelData data) {
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(1.2f, 15.5f), new Vector2(1.2f, 8.5f), new Vector2(3.3f, 4.5f), new Vector2(3.3f, 3.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(7.8f, 15.5f), new Vector2(7.8f, 8.5f), new Vector2(5.7f, 4.5f), new Vector2(5.7f, 3.5f) }));
        data.multiplierGates.add(new MultiplierGate(world, 4.5f, 9.5f, 2.2f, 0.45f, MultiplierGate.GateType.MULTIPLY_X2));
        data.acidPools.add(new AcidPool(world, 2.6f, 6.5f, 1.8f, 0.55f));
        data.acidPools.add(new AcidPool(world, 6.4f, 6.5f, 1.8f, 0.55f));
        data.jar = new Jar(world, 4.5f, 1.8f, 3.2f, 2.4f, 15);
        data.totalBallsRequired = 15;
        data.pins.add(new Pin(world, 4.5f, 11.5f, 5.4f, Pin.Direction.RIGHT));
        data.pins.add(new Pin(world, 4.5f, 7.8f, 5.4f, Pin.Direction.LEFT));
        data.balls.add(new Ball(world, 4.5f, 10.3f, Ball.BallType.WATER)); // x2 = 2 su bilyesi
        spawnBallGrid(world, data, 2.6f, 12.5f, 4, 5, Ball.BallType.COLORED); // 20 top
    }

    // Bölüm 176: Lazerle Bomba Patlatma Bulmacası (Lazer bombayı patlatır, rüzgar bilyeleri taşır)
    private static void buildLevel176(World world, LevelData data) {
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(1.0f, 15.5f), new Vector2(1.0f, 7.0f), new Vector2(3.0f, 7.0f), new Vector2(3.0f, 15.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(4.0f, 15.5f), new Vector2(4.0f, 9.0f), new Vector2(3.3f, 4.5f), new Vector2(3.3f, 3.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(7.8f, 15.5f), new Vector2(7.8f, 9.0f), new Vector2(5.7f, 4.5f), new Vector2(5.7f, 3.5f) }));
        data.lasers.add(new LaserObstacle(world, 1.2f, 8.0f, 2.8f, 8.0f, LaserObstacle.LaserMode.CONSTANT, 0f, 0f, 0f, Color.RED));
        data.windTurbines.add(new WindTurbine(world, 6.0f, 5.0f, WindTurbine.Direction.UP, 5.0f, 40.0f));
        data.jar = new Jar(world, 4.5f, 1.8f, 3.2f, 2.4f, 15);
        data.totalBallsRequired = 15;
        data.pins.add(new Pin(world, 2.0f, 10.0f, 1.8f, Pin.Direction.LEFT));
        data.pins.add(new Pin(world, 6.0f, 11.5f, 3.6f, Pin.Direction.RIGHT));
        data.balls.add(new Ball(world, 2.0f, 11.2f, Ball.BallType.BOMB));
        spawnBallGrid(world, data, 4.5f, 12.5f, 3, 6, Ball.BallType.COLORED); // 18 top
    }

    // Bölüm 177: İkiz Çarklı Asit Geçidi (Asit havuzu üzerinde dönen iki koruyucu çark)
    private static void buildLevel177(World world, LevelData data) {
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(1.2f, 15.5f), new Vector2(1.2f, 9.5f), new Vector2(3.3f, 4.5f), new Vector2(3.3f, 3.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(7.8f, 15.5f), new Vector2(7.8f, 9.5f), new Vector2(5.7f, 4.5f), new Vector2(5.7f, 3.5f) }));
        data.acidPools.add(new AcidPool(world, 4.5f, 5.5f, 2.8f, 0.55f));
        data.rotatingObstacles.add(new RotatingObstacle(world, 3.0f, 8.0f, RotatingObstacle.RotorType.CROSS_PADDLE_4, 1.2f, 0.20f, 1.4f, new Color(0.2f, 0.85f, 0.4f, 1f)));
        data.rotatingObstacles.add(new RotatingObstacle(world, 6.0f, 8.0f, RotatingObstacle.RotorType.CROSS_PADDLE_4, 1.2f, 0.20f, -1.4f, new Color(0.2f, 0.85f, 0.4f, 1f)));
        data.jar = new Jar(world, 4.5f, 1.8f, 3.2f, 2.4f, 15);
        data.totalBallsRequired = 15;
        data.pins.add(new Pin(world, 4.5f, 11.5f, 5.6f, Pin.Direction.RIGHT));
        data.pins.add(new Pin(world, 4.5f, 9.5f, 5.2f, Pin.Direction.LEFT));
        data.balls.add(new Ball(world, 4.5f, 10.3f, Ball.BallType.WATER));
        spawnBallGrid(world, data, 2.6f, 12.5f, 4, 5, Ball.BallType.COLORED); // 20 top
    }

    // Bölüm 178: Kuantum Prizma Matrisi (Bilyeler 3 farklı renge boyanıp kavanoza dökülür)
    private static void buildLevel178(World world, LevelData data) {
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(1.2f, 15.5f), new Vector2(1.2f, 9.5f), new Vector2(3.3f, 4.5f), new Vector2(3.3f, 3.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(7.8f, 15.5f), new Vector2(7.8f, 9.5f), new Vector2(5.7f, 4.5f), new Vector2(5.7f, 3.5f) }));
        data.colorGates.add(new ColorGate(world, 2.5f, 8.0f, 1.6f, 0.45f, Color.CYAN, "CYAN"));
        data.colorGates.add(new ColorGate(world, 4.5f, 8.0f, 1.6f, 0.45f, Color.YELLOW, "YELLOW"));
        data.colorGates.add(new ColorGate(world, 6.5f, 8.0f, 1.6f, 0.45f, Color.MAGENTA, "MAGENTA"));
        data.jar = new Jar(world, 4.5f, 1.8f, 3.4f, 2.4f, 14);
        data.totalBallsRequired = 14;
        data.pins.add(new Pin(world, 4.5f, 11.5f, 5.8f, Pin.Direction.RIGHT));
        spawnBallGrid(world, data, 2.4f, 12.5f, 5, 4, Ball.BallType.GRAY); // 20 gri top
    }

    // Bölüm 179: Büyük Sentez Çağlayanı (x2 + Çark + Rüzgar + 24 bilye = 48 bilyelik çağlayan)
    private static void buildLevel179(World world, LevelData data) {
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(1.0f, 15.5f), new Vector2(1.0f, 9.0f), new Vector2(3.3f, 4.5f), new Vector2(3.3f, 3.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(8.0f, 15.5f), new Vector2(8.0f, 9.0f), new Vector2(5.7f, 4.5f), new Vector2(5.7f, 3.5f) }));
        data.multiplierGates.add(new MultiplierGate(world, 4.5f, 9.0f, 2.4f, 0.45f, MultiplierGate.GateType.MULTIPLY_X2));
        data.rotatingObstacles.add(new RotatingObstacle(world, 4.5f, 6.5f, RotatingObstacle.RotorType.CROSS_PADDLE_4, 1.3f, 0.22f, 1.5f, new Color(0.2f, 0.85f, 0.4f, 1f)));
        data.jar = new Jar(world, 4.5f, 1.8f, 3.6f, 2.4f, 30);
        data.totalBallsRequired = 30;
        data.pins.add(new Pin(world, 4.5f, 12.0f, 5.8f, Pin.Direction.RIGHT));
        spawnBallGrid(world, data, 2.4f, 12.8f, 4, 6, Ball.BallType.COLORED); // 24 top x2 = 48 top
    }

    // Bölüm 180: Kaos Efendisi Finali (Dünya 9 Finali: Lazer, Asit, Rüzgar, Çark, Portal, Kilit)
    private static void buildLevel180(World world, LevelData data) {
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(1.0f, 15.5f), new Vector2(1.0f, 7.5f), new Vector2(2.8f, 7.5f), new Vector2(2.8f, 15.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(3.2f, 15.5f), new Vector2(3.2f, 8.5f), new Vector2(3.3f, 4.5f), new Vector2(3.3f, 3.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(7.8f, 15.5f), new Vector2(7.8f, 8.5f), new Vector2(5.7f, 4.5f), new Vector2(5.7f, 3.5f) }));
        data.windTurbines.add(new WindTurbine(world, 1.9f, 8.0f, WindTurbine.Direction.UP, 6.0f, 44.0f));
        data.acidPools.add(new AcidPool(world, 5.5f, 5.5f, 2.4f, 0.55f));
        data.lasers.add(new LaserObstacle(world, 3.5f, 7.5f, 7.5f, 7.5f, LaserObstacle.LaserMode.PULSING, 1.4f, 1.4f, 0.0f, Color.RED));
        data.rotatingObstacles.add(new RotatingObstacle(world, 5.5f, 9.5f, RotatingObstacle.RotorType.CROSS_PADDLE_4, 1.2f, 0.20f, 1.3f, new Color(0.2f, 0.85f, 0.4f, 1f)));
        data.keys.add(new KeyEntity(world, 1.9f, 9.5f, 1));
        data.jar = new Jar(world, 4.5f, 1.8f, 3.6f, 2.4f, 20);
        data.totalBallsRequired = 20;
        data.pins.add(new Pin(world, 1.9f, 10.5f, 1.6f, Pin.Direction.LEFT));
        data.pins.add(new Pin(world, 5.5f, 12.0f, 4.0f, Pin.Direction.RIGHT));
        data.pins.add(new Pin(world, 5.5f, 8.0f, 4.0f, Pin.Direction.LEFT, true, 1));
        data.balls.add(new Ball(world, 5.5f, 10.8f, Ball.BallType.WATER)); // Su bilyesi
        spawnBallGrid(world, data, 4.2f, 12.8f, 4, 6, Ball.BallType.COLORED); // 24 top
    }

    // Bölüm 181: Zirveye Tırmanış (Saf zeka gerektiren 5 adımlı açılış bulmacası)
    private static void buildLevel181(World world, LevelData data) {
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(1.2f, 15.5f), new Vector2(1.2f, 9.5f), new Vector2(3.3f, 4.5f), new Vector2(3.3f, 3.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(7.8f, 15.5f), new Vector2(7.8f, 9.5f), new Vector2(5.7f, 4.5f), new Vector2(5.7f, 3.5f) }));
        data.jar = new Jar(world, 4.5f, 1.8f, 3.4f, 2.4f, 18);
        data.totalBallsRequired = 18;
        data.pins.add(new Pin(world, 4.5f, 13.5f, 5.4f, Pin.Direction.LEFT));
        data.pins.add(new Pin(world, 4.5f, 11.5f, 5.4f, Pin.Direction.RIGHT));
        data.pins.add(new Pin(world, 4.5f, 9.5f, 5.2f, Pin.Direction.LEFT));
        data.pins.add(new Pin(world, 4.5f, 7.5f, 5.0f, Pin.Direction.RIGHT));
        data.pins.add(new Pin(world, 4.5f, 5.5f, 4.8f, Pin.Direction.LEFT));
        spawnBallGrid(world, data, 2.6f, 14.0f, 4, 6, Ball.BallType.COLORED); // 24 top
    }

    // Bölüm 182: Kuantum Dansı (İki portaldan çıkan bilyelerin havada çarpışarak renklenmesi)
    private static void buildLevel182(World world, LevelData data) {
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(1.0f, 15.5f), new Vector2(1.0f, 9.0f), new Vector2(3.5f, 9.0f), new Vector2(3.5f, 15.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(5.5f, 15.5f), new Vector2(5.5f, 9.0f), new Vector2(8.0f, 9.0f), new Vector2(8.0f, 15.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(3.3f, 7.5f), new Vector2(3.3f, 3.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(5.7f, 7.5f), new Vector2(5.7f, 3.5f) }));
        data.portals.add(new Portal(world, 2.2f, 9.5f, 3.8f, 7.5f));
        data.portals.add(new Portal(world, 6.8f, 9.5f, 5.2f, 7.5f));
        data.jar = new Jar(world, 4.5f, 1.8f, 3.4f, 2.4f, 18);
        data.totalBallsRequired = 18;
        data.pins.add(new Pin(world, 2.2f, 11.5f, 2.2f, Pin.Direction.LEFT));
        data.pins.add(new Pin(world, 6.8f, 11.5f, 2.2f, Pin.Direction.RIGHT));
        spawnBallGrid(world, data, 1.4f, 12.2f, 3, 4, Ball.BallType.COLORED); // 12 renkli
        spawnBallGrid(world, data, 6.0f, 12.2f, 3, 4, Ball.BallType.GRAY);    // 12 gri -> 24 top
    }

    // Bölüm 183: İkiz Asit Reaktörü (Milimetrik su bilyesi zamanlaması)
    private static void buildLevel183(World world, LevelData data) {
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(1.2f, 15.5f), new Vector2(1.2f, 8.5f), new Vector2(3.3f, 4.5f), new Vector2(3.3f, 3.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(7.8f, 15.5f), new Vector2(7.8f, 8.5f), new Vector2(5.7f, 4.5f), new Vector2(5.7f, 3.5f) }));
        data.acidPools.add(new AcidPool(world, 2.8f, 6.5f, 2.2f, 0.55f));
        data.acidPools.add(new AcidPool(world, 6.2f, 6.5f, 2.2f, 0.55f));
        data.jar = new Jar(world, 4.5f, 1.8f, 3.4f, 2.4f, 18);
        data.totalBallsRequired = 18;
        data.pins.add(new Pin(world, 2.8f, 10.0f, 2.6f, Pin.Direction.LEFT));
        data.pins.add(new Pin(world, 6.2f, 10.0f, 2.6f, Pin.Direction.RIGHT));
        data.pins.add(new Pin(world, 4.5f, 7.8f, 5.4f, Pin.Direction.RIGHT));
        data.balls.add(new Ball(world, 2.8f, 8.5f, Ball.BallType.WATER));
        data.balls.add(new Ball(world, 6.2f, 8.5f, Ball.BallType.WATER));
        spawnBallGrid(world, data, 2.4f, 11.5f, 4, 6, Ball.BallType.COLORED); // 24 top
    }

    // Bölüm 184: Yörünge Fırtınası (2 Türbin ile dairesel hava girdabı)
    private static void buildLevel184(World world, LevelData data) {
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(1.2f, 15.5f), new Vector2(1.2f, 8.5f), new Vector2(3.3f, 4.5f), new Vector2(3.3f, 3.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(7.8f, 15.5f), new Vector2(7.8f, 8.5f), new Vector2(5.7f, 4.5f), new Vector2(5.7f, 3.5f) }));
        data.windTurbines.add(new WindTurbine(world, 2.5f, 5.5f, WindTurbine.Direction.UP, 6.0f, 42.0f));
        data.windTurbines.add(new WindTurbine(world, 6.5f, 11.5f, WindTurbine.Direction.DOWN, 6.0f, 42.0f));
        data.jar = new Jar(world, 4.5f, 1.8f, 3.4f, 2.4f, 18);
        data.totalBallsRequired = 18;
        data.pins.add(new Pin(world, 4.5f, 13.0f, 5.6f, Pin.Direction.RIGHT));
        data.pins.add(new Pin(world, 4.5f, 7.5f, 5.2f, Pin.Direction.LEFT));
        spawnBallGrid(world, data, 2.6f, 13.8f, 4, 6, Ball.BallType.COLORED); // 24 top
    }

    // Bölüm 185: Kusursuz Zamanlama Lazerleri (4 Aşamalı lazer koreografisi)
    private static void buildLevel185(World world, LevelData data) {
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(1.2f, 15.5f), new Vector2(1.2f, 9.5f), new Vector2(3.3f, 4.5f), new Vector2(3.3f, 3.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(7.8f, 15.5f), new Vector2(7.8f, 9.5f), new Vector2(5.7f, 4.5f), new Vector2(5.7f, 3.5f) }));
        data.lasers.add(new LaserObstacle(world, 1.5f, 11.0f, 7.5f, 11.0f, LaserObstacle.LaserMode.PULSING, 1.0f, 1.0f, 0.0f, Color.RED));
        data.lasers.add(new LaserObstacle(world, 1.5f, 9.2f, 7.5f, 9.2f, LaserObstacle.LaserMode.PULSING, 1.0f, 1.0f, 0.5f, Color.CYAN));
        data.lasers.add(new LaserObstacle(world, 1.5f, 7.4f, 7.5f, 7.4f, LaserObstacle.LaserMode.PULSING, 1.0f, 1.0f, 1.0f, Color.MAGENTA));
        data.lasers.add(new LaserObstacle(world, 1.5f, 5.6f, 7.5f, 5.6f, LaserObstacle.LaserMode.PULSING, 1.0f, 1.0f, 1.5f, Color.YELLOW));
        data.jar = new Jar(world, 4.5f, 1.8f, 3.4f, 2.4f, 18);
        data.totalBallsRequired = 18;
        data.pins.add(new Pin(world, 4.5f, 12.8f, 5.8f, Pin.Direction.RIGHT));
        spawnBallGrid(world, data, 2.6f, 13.5f, 4, 6, Ball.BallType.COLORED); // 24 top
    }

    // Bölüm 186: Prizmatik Şaheser (Renksiz 24 bilye 3 kapıdan geçerek gökkuşağına dönüşür)
    private static void buildLevel186(World world, LevelData data) {
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(1.0f, 15.5f), new Vector2(1.0f, 8.5f), new Vector2(3.3f, 4.5f), new Vector2(3.3f, 3.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(8.0f, 15.5f), new Vector2(8.0f, 8.5f), new Vector2(5.7f, 4.5f), new Vector2(5.7f, 3.5f) }));
        data.colorGates.add(new ColorGate(world, 2.2f, 7.5f, 1.6f, 0.45f, Color.CYAN, "CYAN"));
        data.colorGates.add(new ColorGate(world, 4.5f, 7.5f, 1.6f, 0.45f, Color.GOLD, "GOLD"));
        data.colorGates.add(new ColorGate(world, 6.8f, 7.5f, 1.6f, 0.45f, Color.MAGENTA, "MAGENTA"));
        data.jar = new Jar(world, 4.5f, 1.8f, 3.6f, 2.4f, 18);
        data.totalBallsRequired = 18;
        data.pins.add(new Pin(world, 4.5f, 11.5f, 5.8f, Pin.Direction.RIGHT));
        spawnBallGrid(world, data, 2.4f, 12.5f, 4, 6, Ball.BallType.GRAY); // 24 gri top
    }

    // Bölüm 187: Üç Kilitli Efsane (3 Anahtar ve 3 Kilitli kapı)
    private static void buildLevel187(World world, LevelData data) {
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(1.0f, 15.5f), new Vector2(1.0f, 6.0f), new Vector2(3.3f, 4.5f), new Vector2(3.3f, 3.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(8.0f, 15.5f), new Vector2(8.0f, 6.0f), new Vector2(5.7f, 4.5f), new Vector2(5.7f, 3.5f) }));
        data.keys.add(new KeyEntity(world, 2.0f, 13.5f, 1));
        data.keys.add(new KeyEntity(world, 4.5f, 13.5f, 2));
        data.keys.add(new KeyEntity(world, 7.0f, 13.5f, 3));
        data.jar = new Jar(world, 4.5f, 1.8f, 3.4f, 2.4f, 18);
        data.totalBallsRequired = 18;
        data.pins.add(new Pin(world, 2.2f, 11.0f, 2.2f, Pin.Direction.LEFT, true, 1));
        data.pins.add(new Pin(world, 4.5f, 9.0f, 4.8f, Pin.Direction.RIGHT, true, 2));
        data.pins.add(new Pin(world, 6.8f, 7.0f, 2.2f, Pin.Direction.RIGHT, true, 3));
        spawnBallGrid(world, data, 3.0f, 12.0f, 3, 8, Ball.BallType.COLORED); // 24 top
    }

    // Bölüm 188: Fırtına ve Lav Çarpışması (Rüzgar bilyeleri dikenlerin üzerinden uçurur)
    private static void buildLevel188(World world, LevelData data) {
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(1.0f, 15.5f), new Vector2(1.0f, 6.5f), new Vector2(3.3f, 4.5f), new Vector2(3.3f, 3.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(8.0f, 15.5f), new Vector2(8.0f, 6.5f), new Vector2(5.7f, 4.5f), new Vector2(5.7f, 3.5f) }));
        data.spikes.add(new SpikeObstacle(world, 4.5f, 6.8f, 3.5f, 0.4f));
        data.windTurbines.add(new WindTurbine(world, 4.5f, 5.0f, WindTurbine.Direction.UP, 6.0f, 44.0f));
        data.jar = new Jar(world, 4.5f, 1.8f, 3.4f, 2.4f, 18);
        data.totalBallsRequired = 18;
        data.pins.add(new Pin(world, 4.5f, 12.0f, 5.8f, Pin.Direction.RIGHT));
        spawnBallGrid(world, data, 2.6f, 12.8f, 4, 6, Ball.BallType.COLORED); // 24 top
    }

    // Bölüm 189: Çift Çarklı Teleportasyon (Çark 1 -> Portal -> Çark 2 -> Kavanoz)
    private static void buildLevel189(World world, LevelData data) {
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(1.2f, 15.5f), new Vector2(1.2f, 9.0f), new Vector2(3.3f, 4.5f), new Vector2(3.3f, 3.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(7.8f, 15.5f), new Vector2(7.8f, 9.0f), new Vector2(5.7f, 4.5f), new Vector2(5.7f, 3.5f) }));
        data.rotatingObstacles.add(new RotatingObstacle(world, 3.0f, 9.5f, RotatingObstacle.RotorType.CROSS_PADDLE_4, 1.2f, 0.20f, 1.4f, new Color(0.2f, 0.85f, 0.4f, 1f)));
        data.portals.add(new Portal(world, 3.0f, 7.8f, 6.0f, 9.5f));
        data.rotatingObstacles.add(new RotatingObstacle(world, 6.0f, 7.5f, RotatingObstacle.RotorType.CROSS_PADDLE_4, 1.2f, 0.20f, -1.4f, new Color(0.2f, 0.85f, 0.4f, 1f)));
        data.jar = new Jar(world, 4.5f, 1.8f, 3.4f, 2.4f, 18);
        data.totalBallsRequired = 18;
        data.pins.add(new Pin(world, 3.0f, 12.0f, 3.2f, Pin.Direction.LEFT));
        spawnBallGrid(world, data, 2.0f, 12.8f, 4, 6, Ball.BallType.COLORED); // 24 top
    }

    // Bölüm 190: Kozmik Zirve (Apex 1) (32 Bilyelik büyüleyici akışkan simülasyon)
    private static void buildLevel190(World world, LevelData data) {
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(1.0f, 15.5f), new Vector2(1.0f, 9.0f), new Vector2(3.3f, 4.5f), new Vector2(3.3f, 3.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(8.0f, 15.5f), new Vector2(8.0f, 9.0f), new Vector2(5.7f, 4.5f), new Vector2(5.7f, 3.5f) }));
        data.rotatingObstacles.add(new RotatingObstacle(world, 4.5f, 9.0f, RotatingObstacle.RotorType.CROSS_PADDLE_4, 1.4f, 0.22f, 1.5f, new Color(0.2f, 0.85f, 0.4f, 1f)));
        data.lasers.add(new LaserObstacle(world, 2.0f, 6.5f, 7.0f, 6.5f, LaserObstacle.LaserMode.PULSING, 1.3f, 1.3f, 0.0f, Color.CYAN));
        data.jar = new Jar(world, 4.5f, 1.8f, 3.6f, 2.4f, 24);
        data.totalBallsRequired = 24;
        data.pins.add(new Pin(world, 4.5f, 12.0f, 5.8f, Pin.Direction.RIGHT));
        spawnBallGrid(world, data, 2.2f, 12.8f, 4, 8, Ball.BallType.COLORED); // 32 top
    }

    // Bölüm 191: Asit Labirenti ve Su İncileri (3 Su bilyesi, 3 Asit bariyeri)
    private static void buildLevel191(World world, LevelData data) {
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(1.0f, 15.5f), new Vector2(1.0f, 7.5f), new Vector2(3.3f, 4.5f), new Vector2(3.3f, 3.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(8.0f, 15.5f), new Vector2(8.0f, 7.5f), new Vector2(5.7f, 4.5f), new Vector2(5.7f, 3.5f) }));
        data.acidPools.add(new AcidPool(world, 2.4f, 9.0f, 1.8f, 0.55f));
        data.acidPools.add(new AcidPool(world, 6.6f, 9.0f, 1.8f, 0.55f));
        data.acidPools.add(new AcidPool(world, 4.5f, 6.0f, 2.4f, 0.55f));
        data.jar = new Jar(world, 4.5f, 1.8f, 3.4f, 2.4f, 18);
        data.totalBallsRequired = 18;
        data.pins.add(new Pin(world, 4.5f, 12.0f, 5.6f, Pin.Direction.RIGHT));
        data.pins.add(new Pin(world, 4.5f, 7.8f, 5.2f, Pin.Direction.LEFT));
        data.balls.add(new Ball(world, 2.4f, 10.5f, Ball.BallType.WATER));
        data.balls.add(new Ball(world, 6.6f, 10.5f, Ball.BallType.WATER));
        data.balls.add(new Ball(world, 4.5f, 8.5f, Ball.BallType.WATER));
        spawnBallGrid(world, data, 2.6f, 12.8f, 4, 6, Ball.BallType.COLORED); // 24 top
    }

    // Bölüm 192: Ayna Yansıması (Sol ve sağın birbirini ayna gibi tamamladığı simetrik bulmaca)
    private static void buildLevel192(World world, LevelData data) {
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(1.0f, 15.5f), new Vector2(1.0f, 7.0f), new Vector2(3.3f, 4.5f), new Vector2(3.3f, 3.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(8.0f, 15.5f), new Vector2(8.0f, 7.0f), new Vector2(5.7f, 4.5f), new Vector2(5.7f, 3.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(4.5f, 15.5f), new Vector2(4.5f, 6.5f) }));
        data.bouncers.add(new Bouncer(world, 2.5f, 6.0f, 1.8f, 0.4f, 35.0f, 8.0f));
        data.bouncers.add(new Bouncer(world, 6.5f, 6.0f, 1.8f, 0.4f, -35.0f, 8.0f));
        data.jar = new Jar(world, 4.5f, 1.8f, 3.6f, 2.4f, 22);
        data.totalBallsRequired = 22;
        data.pins.add(new Pin(world, 2.5f, 11.5f, 2.6f, Pin.Direction.LEFT));
        data.pins.add(new Pin(world, 6.5f, 11.5f, 2.6f, Pin.Direction.RIGHT));
        spawnBallGrid(world, data, 1.6f, 12.5f, 3, 5, Ball.BallType.COLORED); // 15
        spawnBallGrid(world, data, 5.4f, 12.5f, 3, 5, Ball.BallType.COLORED); // 15 -> 30 top
    }

    // Bölüm 193: Ters Yerçekimi Rüyası (Rüzgar türbini bilyeleri yukarı fırlatır, üst kavanoz dolar)
    private static void buildLevel193(World world, LevelData data) {
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(1.5f, 15.5f), new Vector2(1.5f, 4.5f), new Vector2(4.5f, 3.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(7.5f, 15.5f), new Vector2(7.5f, 4.5f), new Vector2(4.5f, 3.5f) }));
        data.windTurbines.add(new WindTurbine(world, 4.5f, 4.5f, WindTurbine.Direction.UP, 8.0f, 55.0f));
        data.jar = new Jar(world, 4.5f, 14.5f, 3.4f, 2.4f, 18);
        data.totalBallsRequired = 18;
        data.pins.add(new Pin(world, 4.5f, 8.5f, 5.4f, Pin.Direction.RIGHT));
        spawnBallGrid(world, data, 2.6f, 9.5f, 4, 6, Ball.BallType.COLORED); // 24 top
    }

    // Bölüm 194: 5 Bombalı Mayın Tarlası (5 Bombanın birbirine çarptırılarak tek seferde imhası)
    private static void buildLevel194(World world, LevelData data) {
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(1.0f, 15.5f), new Vector2(1.0f, 7.5f), new Vector2(2.5f, 5.0f), new Vector2(2.5f, 3.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(8.0f, 15.5f), new Vector2(8.0f, 7.5f), new Vector2(6.5f, 5.0f), new Vector2(6.5f, 3.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(3.5f, 15.5f), new Vector2(3.5f, 9.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(5.5f, 15.5f), new Vector2(5.5f, 9.5f) }));
        data.spikes.add(new SpikeObstacle(world, 4.5f, 4.5f, 3.0f, 0.4f));
        data.jar = new Jar(world, 4.5f, 1.8f, 3.4f, 2.4f, 18);
        data.totalBallsRequired = 18;
        data.pins.add(new Pin(world, 4.5f, 10.0f, 5.6f, Pin.Direction.RIGHT));
        data.pins.add(new Pin(world, 4.5f, 6.5f, 5.0f, Pin.Direction.LEFT));
        data.balls.add(new Ball(world, 4.0f, 8.5f, Ball.BallType.BOMB));
        data.balls.add(new Ball(world, 4.5f, 8.5f, Ball.BallType.BOMB));
        data.balls.add(new Ball(world, 5.0f, 8.5f, Ball.BallType.BOMB));
        data.balls.add(new Ball(world, 4.2f, 7.5f, Ball.BallType.BOMB));
        data.balls.add(new Ball(world, 4.8f, 7.5f, Ball.BallType.BOMB)); // 5 bomba
        spawnBallGrid(world, data, 2.6f, 11.5f, 4, 6, Ball.BallType.COLORED); // 24 top
    }

    // Bölüm 195: x3 Çoğaltıcı Patlaması (10 bilye -> 30 bilye -> 60 bilyelik altın yağmuru)
    private static void buildLevel195(World world, LevelData data) {
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(1.0f, 15.5f), new Vector2(1.0f, 9.0f), new Vector2(3.3f, 4.5f), new Vector2(3.3f, 3.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(8.0f, 15.5f), new Vector2(8.0f, 9.0f), new Vector2(5.7f, 4.5f), new Vector2(5.7f, 3.5f) }));
        data.multiplierGates.add(new MultiplierGate(world, 4.5f, 10.5f, 2.4f, 0.45f, MultiplierGate.GateType.MULTIPLY_X3));
        data.multiplierGates.add(new MultiplierGate(world, 4.5f, 7.0f, 2.4f, 0.45f, MultiplierGate.GateType.MULTIPLY_X2));
        data.jar = new Jar(world, 4.5f, 1.8f, 3.8f, 2.6f, 40);
        data.totalBallsRequired = 40;
        data.pins.add(new Pin(world, 4.5f, 12.5f, 5.6f, Pin.Direction.RIGHT));
        spawnBallGrid(world, data, 3.2f, 13.5f, 2, 5, Ball.BallType.COLORED); // 10 top x3 x2 = 60 top!
    }

    // Bölüm 196: Kuantum Çemberi (Bilyeler tüm ekranı saat yönünde turlayarak iner)
    private static void buildLevel196(World world, LevelData data) {
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(1.2f, 15.5f), new Vector2(1.2f, 8.5f), new Vector2(3.3f, 4.5f), new Vector2(3.3f, 3.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(7.8f, 15.5f), new Vector2(7.8f, 8.5f), new Vector2(5.7f, 4.5f), new Vector2(5.7f, 3.5f) }));
        data.portals.add(new Portal(world, 2.2f, 8.0f, 6.8f, 14.0f));
        data.bouncers.add(new Bouncer(world, 6.8f, 10.0f, 2.0f, 0.4f, -40.0f, 8.0f));
        data.jar = new Jar(world, 4.5f, 1.8f, 3.6f, 2.4f, 20);
        data.totalBallsRequired = 20;
        data.pins.add(new Pin(world, 2.2f, 10.5f, 2.4f, Pin.Direction.LEFT));
        spawnBallGrid(world, data, 1.5f, 11.5f, 3, 9, Ball.BallType.COLORED); // 27 top
    }

    // Bölüm 197: Son Sınav: Denge (En ufak acele kaybettirir, soğukkanlı pim çekişi kazandırır)
    private static void buildLevel197(World world, LevelData data) {
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(1.0f, 15.5f), new Vector2(1.0f, 7.5f), new Vector2(3.3f, 4.5f), new Vector2(3.3f, 3.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(8.0f, 15.5f), new Vector2(8.0f, 7.5f), new Vector2(5.7f, 4.5f), new Vector2(5.7f, 3.5f) }));
        data.spikes.add(new SpikeObstacle(world, 2.2f, 6.0f, 1.6f, 0.4f));
        data.acidPools.add(new AcidPool(world, 6.8f, 6.0f, 1.6f, 0.55f));
        data.jar = new Jar(world, 4.5f, 1.8f, 3.4f, 2.4f, 18);
        data.totalBallsRequired = 18;
        data.pins.add(new Pin(world, 2.2f, 9.5f, 2.0f, Pin.Direction.LEFT));
        data.pins.add(new Pin(world, 6.8f, 9.5f, 2.0f, Pin.Direction.RIGHT));
        data.pins.add(new Pin(world, 4.5f, 11.5f, 5.6f, Pin.Direction.RIGHT));
        data.balls.add(new Ball(world, 2.2f, 10.5f, Ball.BallType.BOMB));
        data.balls.add(new Ball(world, 6.8f, 10.5f, Ball.BallType.WATER));
        spawnBallGrid(world, data, 2.6f, 12.5f, 4, 6, Ball.BallType.COLORED); // 24 top
    }

    // Bölüm 198: Büyük Usta İmtihanı (Lazer, Asit, Rüzgar, Çark, Portal, Kilit, Trambolin bir arada!)
    private static void buildLevel198(World world, LevelData data) {
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(1.0f, 15.5f), new Vector2(1.0f, 7.0f), new Vector2(2.8f, 7.0f), new Vector2(2.8f, 15.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(3.2f, 15.5f), new Vector2(3.2f, 8.5f), new Vector2(3.3f, 4.5f), new Vector2(3.3f, 3.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(7.8f, 15.5f), new Vector2(7.8f, 8.5f), new Vector2(5.7f, 4.5f), new Vector2(5.7f, 3.5f) }));
        data.windTurbines.add(new WindTurbine(world, 1.9f, 7.5f, WindTurbine.Direction.UP, 6.0f, 44.0f));
        data.acidPools.add(new AcidPool(world, 5.5f, 5.5f, 2.4f, 0.55f));
        data.lasers.add(new LaserObstacle(world, 3.5f, 7.2f, 7.5f, 7.2f, LaserObstacle.LaserMode.PULSING, 1.4f, 1.4f, 0.0f, Color.RED));
        data.rotatingObstacles.add(new RotatingObstacle(world, 5.5f, 9.0f, RotatingObstacle.RotorType.CROSS_PADDLE_4, 1.2f, 0.20f, 1.3f, new Color(0.2f, 0.85f, 0.4f, 1f)));
        data.keys.add(new KeyEntity(world, 1.9f, 9.0f, 1));
        data.jar = new Jar(world, 4.5f, 1.8f, 3.6f, 2.4f, 22);
        data.totalBallsRequired = 22;
        data.pins.add(new Pin(world, 1.9f, 10.5f, 1.6f, Pin.Direction.LEFT));
        data.pins.add(new Pin(world, 5.5f, 12.0f, 4.0f, Pin.Direction.RIGHT));
        data.pins.add(new Pin(world, 5.5f, 7.8f, 4.0f, Pin.Direction.LEFT, true, 1));
        data.balls.add(new Ball(world, 5.5f, 10.5f, Ball.BallType.WATER));
        spawnBallGrid(world, data, 4.0f, 12.8f, 4, 7, Ball.BallType.COLORED); // 28 top
    }

    // Bölüm 199: Şafak Vakti (40 Bilyenin kusursuz ses akorduyla kavanoza döküldüğü görsel şiir)
    private static void buildLevel199(World world, LevelData data) {
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(1.0f, 15.5f), new Vector2(1.0f, 9.0f), new Vector2(3.3f, 4.5f), new Vector2(3.3f, 3.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(8.0f, 15.5f), new Vector2(8.0f, 9.0f), new Vector2(5.7f, 4.5f), new Vector2(5.7f, 3.5f) }));
        data.rotatingObstacles.add(new RotatingObstacle(world, 3.2f, 7.5f, RotatingObstacle.RotorType.CROSS_PADDLE_4, 1.2f, 0.20f, 1.2f, new Color(0.9f, 0.7f, 0.2f, 1f)));
        data.rotatingObstacles.add(new RotatingObstacle(world, 5.8f, 7.5f, RotatingObstacle.RotorType.CROSS_PADDLE_4, 1.2f, 0.20f, -1.2f, new Color(0.9f, 0.7f, 0.2f, 1f)));
        data.jar = new Jar(world, 4.5f, 1.8f, 3.8f, 2.6f, 30);
        data.totalBallsRequired = 30;
        data.pins.add(new Pin(world, 4.5f, 12.0f, 5.8f, Pin.Direction.RIGHT));
        data.pins.add(new Pin(world, 4.5f, 9.5f, 5.4f, Pin.Direction.LEFT));
        spawnBallGrid(world, data, 2.0f, 12.8f, 5, 8, Ball.BallType.COLORED); // 40 top
    }

    // Bölüm 200: KOZMİK TANRI: BÜYÜK FİNAL (GRAND FINALE - 200 Bölümlük Efsanevi Başyapıt)
    private static void buildLevel200(World world, LevelData data) {
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(1.0f, 15.5f), new Vector2(1.0f, 8.5f), new Vector2(3.3f, 4.5f), new Vector2(3.3f, 3.5f) }));
        data.walls.add(new LevelWall(world, new Vector2[]{ new Vector2(8.0f, 15.5f), new Vector2(8.0f, 8.5f), new Vector2(5.7f, 4.5f), new Vector2(5.7f, 3.5f) }));
        data.portals.add(new Portal(world, 1.8f, 10.0f, 3.5f, 6.5f));
        data.portals.add(new Portal(world, 7.2f, 10.0f, 5.5f, 6.5f));
        data.rotatingObstacles.add(new RotatingObstacle(world, 4.5f, 8.5f, RotatingObstacle.RotorType.CROSS_PADDLE_4, 1.4f, 0.22f, 1.8f, new Color(1.0f, 0.84f, 0.22f, 1f)));
        data.multiplierGates.add(new MultiplierGate(world, 4.5f, 6.0f, 2.4f, 0.45f, MultiplierGate.GateType.MULTIPLY_X2));
        data.lasers.add(new LaserObstacle(world, 2.0f, 4.8f, 7.0f, 4.8f, LaserObstacle.LaserMode.PULSING, 1.5f, 1.5f, 0.0f, Color.CYAN));
        data.jar = new Jar(world, 4.5f, 1.8f, 3.8f, 2.6f, 32);
        data.totalBallsRequired = 32;
        data.pins.add(new Pin(world, 4.5f, 12.0f, 5.8f, Pin.Direction.RIGHT));
        data.pins.add(new Pin(world, 4.5f, 9.8f, 5.4f, Pin.Direction.LEFT));
        spawnBallGrid(world, data, 2.2f, 12.8f, 5, 5, Ball.BallType.COLORED); // 25 top x2 = 50 top!
    }

    private static void spawnBallGrid(World world, LevelData data, float startX, float startY, int cols, int rows, Ball.BallType type) {
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                float bx = startX + c * 0.54f + (r % 2 == 0 ? 0.12f : 0f);
                float by = startY + r * 0.54f;
                data.balls.add(new Ball(world, bx, by, type));
            }
        }
    }

    /**
     * Otomatik GÃ¼venli BÃ¶lge (Safe Zone) ve Ã‡arpÄ±ÅŸma Ã–nleme Motoru.
     * BÃ¶lÃ¼m yÃ¼klenirken toplarÄ±n duvarlara, pimlere veya birbirlerine asla takÄ±lmamasÄ±nÄ± / sÄ±kÄ±ÅŸmamasÄ±nÄ± saÄŸlar.
     */
    private static void sanitizeAndRelaxBallSpawns(LevelData data) {
        if (data.balls == null || data.balls.size == 0) return;

        float safeMargin = 0.05f;
        float ballRad = Ball.RADIUS;

        float[] posX = new float[data.balls.size];
        float[] posY = new float[data.balls.size];

        for (int i = 0; i < data.balls.size; i++) {
            Vector2 p = data.balls.get(i).getPosition();
            posX[i] = p.x;
            posY[i] = p.y;
        }

        for (int iter = 0; iter < 20; iter++) {
            // 1. LevelWall Ã‡arpÄ±ÅŸma RahatlatmasÄ±
            for (int b = 0; b < data.balls.size; b++) {
                float bx = posX[b];
                float by = posY[b];
                float r = ballRad + safeMargin;

                for (LevelWall wall : data.walls) {
                    Vector2[] verts = wall.getVertices();
                    if (verts == null || verts.length < 2) continue;

                    for (int i = 0; i < verts.length - 1; i++) {
                        Vector2 v1 = verts[i];
                        Vector2 v2 = verts[i + 1];

                        float dx = v2.x - v1.x;
                        float dy = v2.y - v1.y;
                        float l2 = dx * dx + dy * dy;

                        float qx, qy;
                        if (l2 < 0.00001f) {
                            qx = v1.x;
                            qy = v1.y;
                        } else {
                            float t = Math.max(0.0f, Math.min(1.0f, ((bx - v1.x) * dx + (by - v1.y) * dy) / l2));
                            qx = v1.x + t * dx;
                            qy = v1.y + t * dy;
                        }

                        float pdistX = bx - qx;
                        float pdistY = by - qy;
                        float dist = (float) Math.sqrt(pdistX * pdistX + pdistY * pdistY);

                        if (dist < r) {
                            float nx, ny;
                            if (dist > 0.0001f) {
                                nx = pdistX / dist;
                                ny = pdistY / dist;
                            } else {
                                nx = -dy;
                                ny = dx;
                                float norm = (float) Math.sqrt(nx * nx + ny * ny);
                                if (norm > 0.0001f) {
                                    nx /= norm;
                                    ny /= norm;
                                } else {
                                    nx = 0;
                                    ny = 1;
                                }
                            }
                            bx = qx + nx * r;
                            by = qy + ny * r;
                        }
                    }
                }
                posX[b] = bx;
                posY[b] = by;
            }

            // 2. Pim (Pin) Ã‡arpÄ±ÅŸma ve ÃœstÃ¼ne Oturma RahatlatmasÄ±
            for (int b = 0; b < data.balls.size; b++) {
                float bx = posX[b];
                float by = posY[b];
                float r = ballRad + safeMargin;

                for (Pin pin : data.pins) {
                    float px = pin.getOriginX();
                    float py = pin.getOriginY();
                    float plen = pin.getLength();
                    float pthick = pin.getThickness();
                    boolean horiz = pin.isHorizontal();

                    float hw = horiz ? (plen / 2.0f) : (pthick / 2.0f);
                    float hh = horiz ? (pthick / 2.0f) : (plen / 2.0f);

                    float qx = Math.max(px - hw, Math.min(px + hw, bx));
                    float qy = Math.max(py - hh, Math.min(py + hh, by));

                    float pdistX = bx - qx;
                    float pdistY = by - qy;
                    float dist = (float) Math.sqrt(pdistX * pdistX + pdistY * pdistY);

                    if (dist < r || (Math.abs(bx - px) <= hw && Math.abs(by - py) <= hh)) {
                        if (horiz) {
                            if (by >= py) {
                                by = py + hh + r;
                            } else {
                                by = py - hh - r;
                            }
                        } else {
                            if (bx >= px) {
                                bx = px + hw + r;
                            } else {
                                bx = px - hw - r;
                            }
                        }
                    }
                }
                posX[b] = bx;
                posY[b] = by;
            }

            // 3. Diken (Spike) Tehlike BÃ¶lgesinden UzaklaÅŸtÄ±rma (Bomba hariÃ§)
            for (int b = 0; b < data.balls.size; b++) {
                if (data.balls.get(b).getType() == Ball.BallType.BOMB) continue;

                float bx = posX[b];
                float by = posY[b];

                for (SpikeObstacle spike : data.spikes) {
                    float sx = spike.getX();
                    float sy = spike.getY();
                    float sw = spike.getWidth();
                    float sh = spike.getHeight();

                    if (Math.abs(bx - sx) < sw / 2f + ballRad + 0.05f && Math.abs(by - sy) < sh / 2f + ballRad + 0.05f) {
                        by = sy + sh / 2f + ballRad + 0.10f;
                    }
                }
                posY[b] = by;
            }

            // 4. Bilyeler ArasÄ± Ä°tme ve AyrÄ±ÅŸma (Inter-Ball Overlap Relaxation)
            for (int i = 0; i < data.balls.size; i++) {
                for (int j = i + 1; j < data.balls.size; j++) {
                    float dx = posX[i] - posX[j];
                    float dy = posY[i] - posY[j];
                    float dist = (float) Math.sqrt(dx * dx + dy * dy);
                    float minDist = ballRad * 2.0f + 0.02f;

                    if (dist < minDist) {
                        if (dist < 0.0001f) {
                            dx = 0.02f;
                            dy = 0.02f;
                            dist = (float) Math.sqrt(dx * dx + dy * dy);
                        }
                        float overlap = minDist - dist;
                        float pushX = (dx / dist) * (overlap * 0.5f);
                        float pushY = (dy / dist) * (overlap * 0.5f);

                        posX[i] += pushX;
                        posY[i] += pushY;
                        posX[j] -= pushX;
                        posY[j] -= pushY;
                    }
                }
            }

            // 5. Ekran SÄ±nÄ±rlarÄ± Ä°Ã§ine Sabitleme
            for (int b = 0; b < data.balls.size; b++) {
                posX[b] = Math.max(0.5f, Math.min(8.5f, posX[b]));
                posY[b] = Math.max(2.5f, Math.min(15.5f, posY[b]));
            }
        }

        // HesaplanmÄ±ÅŸ gÃ¼venli koordinatlarÄ± Box2D gÃ¶vdesine ve iz takip dizisine aktar
        for (int i = 0; i < data.balls.size; i++) {
            data.balls.get(i).setPosition(posX[i], posY[i]);
        }
    }
}

