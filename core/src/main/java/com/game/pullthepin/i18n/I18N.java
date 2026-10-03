package com.game.pullthepin.i18n;

import java.util.Locale;

/**
 * Profesyonel Çoklu Dil ve Yerelleştirme Yöneticisi (I18N).
 * 60 Seviye, 6 Dünya Teması, Menüler, HUD ve Editör için Türkçe (TR) ve İngilizce (EN) desteği.
 */
public class I18N {

    public enum Language {
        TR("Türkçe", "TR"),
        EN("English", "EN");

        private final String displayName;
        private final String code;

        Language(String displayName, String code) {
            this.displayName = displayName;
            this.code = code;
        }

        public String getDisplayName() { return displayName; }
        public String getCode() { return code; }

        public static Language fromCode(String code) {
            if ("tr".equalsIgnoreCase(code) || "TR".equalsIgnoreCase(code)) {
                return TR;
            }
            return EN;
        }
    }

    private static Language currentLanguage = Language.TR;

    public static void init(String savedLanguageCode) {
        if (savedLanguageCode != null && !savedLanguageCode.isEmpty()) {
            currentLanguage = Language.fromCode(savedLanguageCode);
        } else {
            String sysLang = Locale.getDefault().getLanguage();
            if ("tr".equalsIgnoreCase(sysLang)) {
                currentLanguage = Language.TR;
            } else {
                currentLanguage = Language.EN;
            }
        }
    }

    public static Language getLanguage() {
        return currentLanguage;
    }

    public static void setLanguage(Language language) {
        if (language != null) {
            currentLanguage = language;
        }
    }

    public static void toggleLanguage() {
        if (currentLanguage == Language.TR) {
            currentLanguage = Language.EN;
        } else {
            currentLanguage = Language.TR;
        }
    }

    public static boolean isTurkish() {
        return currentLanguage == Language.TR;
    }

    // --- METİN ÇEVİRİLERİ (DICTIONARY) ---

    public static String get(String key, Object... args) {
        String template = getRaw(key);
        if (args != null && args.length > 0) {
            try {
                return String.format(template, args);
            } catch (Exception e) {
                return template;
            }
        }
        return template;
    }

    private static String getRaw(String key) {
        boolean tr = (currentLanguage == Language.TR);

        switch (key) {
            // Ana Menü
            case "MENU_TITLE": return "PULL THE PIN";
            case "MENU_SUBTITLE": return tr ? "CHILL & ASMR BULMACA" : "CHILL & ASMR PUZZLE";
            case "MENU_DESC": return tr ? "200 Seviye | 10 Eşsiz Dünya | Fizik Motoru" : "200 Levels | 10 Unique Worlds | Physics Engine";
            case "BTN_PLAY": return tr ? "OYNA" : "PLAY";
            case "BTN_DAILY_CHALLENGE": return tr ? "GUNLUK MEYDAN OKUMA" : "DAILY CHALLENGES";
            case "BTN_LEVELS": return tr ? "SEVIYELER (1 - 200)" : "LEVELS (1 - 200)";
            case "BTN_EDITOR": return tr ? "BOLUM EDITORU" : "LEVEL EDITOR";
            case "BTN_COMMUNITY": return tr ? "TOPLULUK BOLUMLERI" : "COMMUNITY LEVELS";
            case "BADGE_SOON": return tr ? "YAKINDA" : "SOON";
            case "BTN_SOUND_ON": return tr ? "SES: ACIK" : "SOUND: ON";
            case "BTN_SOUND_OFF": return tr ? "SES: KAPALI" : "SOUND: OFF";
            case "BTN_HAPTICS_ON": return tr ? "HAPTIK: ACIK" : "HAPTICS: ON";
            case "BTN_HAPTICS_OFF": return tr ? "HAPTIK: KAPALI" : "HAPTICS: OFF";
            case "BTN_LANG": return tr ? "DIL: TURKCE" : "LANG: ENGLISH";
            case "BTN_SHOP": return tr ? "MAGAZA" : "SHOP";
            case "BTN_ACHIEVEMENTS": return tr ? "BASARIMLAR" : "ACHIEVEMENTS";
            case "TXT_PROGRESS": return tr ? "Seviye %d / 200" : "Level %d / 200";
            case "TXT_FOOTER": return tr ? "Kendi bolumlerini tasarla, test et ve oyna!" : "Design, test and play your own levels!";
            case "TOAST_COMING_SOON": return tr ? "Cevrimici topluluk paylasimi cok yakinda!" : "Online community sharing coming soon!";
            case "TOAST_EDITOR_CLOUD": return tr ? "Bulut bolum paylasimi cok yakinda!" : "Cloud level sharing coming soon!";

            // Günlük Meydan Okuma
            case "DAILY_CHALLENGE_TITLE": return tr ? "GUNLUK MEYDAN OKUMA" : "DAILY CHALLENGES";
            case "DAILY_VICTORY_REWARD": return tr ? "+%d ALTIN KAZANILDI!" : "+%d COINS EARNED!";
            case "BTN_BACK_CHALLENGES": return tr ? "GOREVLERE DON >" : "CHALLENGES >";

            // Seviye Seçim
            case "LEVEL_SELECT_TITLE": return tr ? "SEVIYELER" : "SELECT LEVEL";
            case "BTN_BACK_MENU": return tr ? "< ANA MENU" : "< MAIN MENU";
            case "PAGE_LABEL": return tr ? "Sayfa %d / %d" : "Page %d / %d";
            case "WORLD_LABEL": return tr ? "DUNYA %d: %s" : "WORLD %d: %s";
            case "BTN_PREV_PAGE": return "<";
            case "BTN_NEXT_PAGE": return ">";

            // Dünya İsimleri
            case "WORLD_1": return tr ? "Siber Laboratuvar" : "Cyber Lab";
            case "WORLD_2": return tr ? "Atlantis Derinlikleri" : "Atlantis Deep";
            case "WORLD_3": return tr ? "Lav Ocagi" : "Magma Forge";
            case "WORLD_4": return tr ? "Kozmik Bulutsu" : "Cosmic Nebula";
            case "WORLD_5": return tr ? "Zumrut Zen Tapinagi" : "Emerald Zen";
            case "WORLD_6": return tr ? "Gunes Serabi" : "Solar Mirage";
            case "WORLD_7": return tr ? "Kuantum Cekirdek" : "Quantum Core";
            case "WORLD_8": return tr ? "Hiper Boyut (Usta)" : "Hyper Dimension";

            // Oyun İçi HUD ve Diyaloglar
            case "HUD_COLLECTED": return tr ? "Toplanan: %d / %d (%%%d)" : "Collected: %d / %d (%d%%)";
            case "WIN_TITLE": return tr ? "HARIKA!" : "AWESOME!";
            case "WIN_LEVEL_DONE": return tr ? "Seviye %d Tamamlandi" : "Level %d Completed";
            case "BTN_NEXT_LEVEL": return tr ? "SONRAKI SEVIYE >" : "NEXT LEVEL >";
            case "LOSE_TITLE": return tr ? "KAYBEDILDI" : "LEVEL FAILED";
            case "LOSE_REASON": return tr ? "Yeterli Top Toplanamadi" : "Not Enough Colored Balls";
            case "BTN_RETRY": return tr ? "TEKRAR DENE" : "RETRY LEVEL";

            // Kombo / Skor Popupları
            case "COMBO_NICE": return tr ? "+1 GUZEL!" : "+1 NICE!";
            case "COMBO_PERFECT": return tr ? "MUKEMMEL!" : "PERFECT!";
            case "COMBO_STREAK": return tr ? "KOMBO x%d!" : "COMBO x%d!";
            case "COMBO_SUPER": return tr ? "! SUPER KOMBO x%d !" : "! SUPER COMBO x%d !";
            case "COMBO_FEVER": return tr ? ">> ULTRA FEVER x%d <<" : ">> ULTRA FEVER x%d <<";
            case "JACKPOT_TITLE": return tr ? "ALTIN YAGMURU" : "GOLD RUSH JACKPOT";
            case "JACKPOT_BONUS": return tr ? "+%d JACKPOT BONUSU!" : "+%d JACKPOT BONUS!";

            // Yıldız Eşikleri ve Rozetler
            case "STAR_MILESTONE_1": return tr ? "1. YILDIZ!" : "STAR 1!";
            case "STAR_MILESTONE_2": return tr ? "2. YILDIZ!" : "STAR 2!";
            case "STAR_MILESTONE_3": return tr ? "3. YILDIZ KUSURSUZ!" : "STAR 3 PERFECT!";
            case "PERFECT_STAR_BADGE": return tr ? "KUSURSUZ 3 YILDIZ!" : "PERFECT 3 STARS!";
            case "TOTAL_STARS_LABEL": return tr ? "Toplam Yildiz: %d / %d" : "Total Stars: %d / %d";

            // Editör
            case "EDITOR_TITLE": return tr ? "BOLUM EDITORU (LEVEL DESIGNER)" : "LEVEL DESIGNER & EDITOR";
            case "BTN_PLAYTEST": return tr ? "TEST MODU (SPACE / F5)" : "PLAYTEST (SPACE / F5)";
            case "BTN_STOP_TEST": return tr ? "TESTI DURDUR (SPACE)" : "STOP TEST (SPACE)";
            case "BTN_CLEAR_ALL": return tr ? "TEMIZLE (CTRL+N)" : "CLEAR (CTRL+N)";
            case "BTN_UNDO": return tr ? "GERI AL (CTRL+Z)" : "UNDO (CTRL+Z)";
            case "BTN_REDO": return tr ? "YINELE (CTRL+Y)" : "REDO (CTRL+Y)";
            case "BTN_SAVE_SLOT": return tr ? "KAYDET (CTRL+S)" : "SAVE (CTRL+S)";
            case "BTN_LOAD_SLOT": return tr ? "YUKLE (CTRL+O)" : "LOAD (CTRL+O)";
            case "BTN_BACK_MAIN": return tr ? "CIKIS (ESC)" : "EXIT (ESC)";

            case "TOOL_SELECT": return tr ? "Sec / Tasi (V)" : "Select / Move (V)";
            case "TOOL_WALL": return tr ? "Duvar Ciz (W)" : "Draw Wall (W)";
            case "TOOL_PIN": return tr ? "Pim Ekle (P)" : "Add Pin (P)";
            case "TOOL_BALL_COLORED": return tr ? "Renkli Top (B)" : "Colored Ball (B)";
            case "TOOL_BALL_GRAY": return tr ? "Gri Top (G)" : "Gray Ball (G)";
            case "TOOL_BOMB": return tr ? "Bomba (X)" : "Bomb (X)";
            case "TOOL_PORTAL": return tr ? "Portal (T)" : "Portal (T)";
            case "TOOL_BOUNCER": return tr ? "Ziplatici (J)" : "Bouncer (J)";
            case "TOOL_SPIKE": return tr ? "Diken (K)" : "Spike (K)";
            case "TOOL_JAR": return tr ? "Kavanoz Hedefi (H)" : "Target Jar (H)";
            case "SLOT_LABEL": return tr ? "Kayit Slotu:" : "Save Slot:";

            default: return key;
        }
    }

    // --- SEVİYE BAŞLIKLARI (80 SEVİYE ÇEVİRİSİ) ---
    public static String getLevelTitle(int levelNumber) {
        boolean tr = (currentLanguage == Language.TR);
        switch (levelNumber) {
            // Dünya 1: Siber Laboratuvar (1-20)
            case 1:  return tr ? "SEVIYE 1: ILK ADIM" : "LEVEL 1: FIRST STEP";
            case 2:  return tr ? "SEVIYE 2: CIFT PIM" : "LEVEL 2: DUAL PINS";
            case 3:  return tr ? "SEVIYE 3: RENKLERIN DOGUSU" : "LEVEL 3: COLOR GENESIS";
            case 4:  return tr ? "SEVIYE 4: CIFT KANAT" : "LEVEL 4: TWIN WINGS";
            case 5:  return tr ? "SEVIYE 5: AKISKAN HUNI" : "LEVEL 5: FLOW FUNNEL";
            case 6:  return tr ? "SEVIYE 6: TEHLIKE UYARISI" : "LEVEL 6: HAZARD ALERT";
            case 7:  return tr ? "SEVIYE 7: BOMBA TAHLIYESI" : "LEVEL 7: BOMB DEFUSAL";
            case 8:  return tr ? "SEVIYE 8: RENK ONCELIGI" : "LEVEL 8: COLOR PRIORITY";
            case 9:  return tr ? "SEVIYE 9: ZIKZAK INIS" : "LEVEL 9: ZIGZAG CASCADE";
            case 10: return tr ? "SEVIYE 10: LABORATUVAR ZIRVESI" : "LEVEL 10: CYBER APEX";
            case 11: return tr ? "SEVIYE 11: TAKTIL KAYDIRAK" : "LEVEL 11: TACTILE SLIDE";
            case 12: return tr ? "SEVIYE 12: UCLU HAZNE" : "LEVEL 12: TRIPLE CHAMBERS";
            case 13: return tr ? "SEVIYE 13: CIFTE BOMBA TUZAGI" : "LEVEL 13: TWIN BOMB CLASH";
            case 14: return tr ? "SEVIYE 14: DAR BOGAZ" : "LEVEL 14: CHOKE POINT";
            case 15: return tr ? "SEVIYE 15: HIZLI KARAR" : "LEVEL 15: QUICK DECISION";
            case 16: return tr ? "SEVIYE 16: CAPRAZ KESISIM" : "LEVEL 16: CROSSROADS";
            case 17: return tr ? "SEVIYE 17: RENK SELALESI" : "LEVEL 17: COLOR NIAGARA";
            case 18: return tr ? "SEVIYE 18: LABIRENT ODACIKLARI" : "LEVEL 18: COMPARTMENT MAZE";
            case 19: return tr ? "SEVIYE 19: HASSAS SIRALAMA" : "LEVEL 19: PRECISE SEQUENCE";
            case 20: return tr ? "SEVIYE 20: SIBER DUNYA FINALI" : "LEVEL 20: CYBER GRAND FINALE";

            // Dünya 2: Atlantis Derinlikleri (21-40)
            case 21: return tr ? "SEVIYE 21: ZIPLAYAN BILYELER" : "LEVEL 21: NEON BOUNCER";
            case 22: return tr ? "SEVIYE 22: CIFTE TRAMBOLIN" : "LEVEL 22: DUAL BOUNCE RELAY";
            case 23: return tr ? "SEVIYE 23: ALTIN ANAHTAR" : "LEVEL 23: GOLDEN KEY";
            case 24: return tr ? "SEVIYE 24: ACISAL SICRAMA" : "LEVEL 24: ANGULAR VAULT";
            case 25: return tr ? "SEVIYE 25: ZIPLAYAN BOMBA" : "LEVEL 25: BOUNCING BOMB";
            case 26: return tr ? "SEVIYE 26: RENK YAYLANMASI" : "LEVEL 26: BOUNCE & INFUSE";
            case 27: return tr ? "SEVIYE 27: KILITLI LABIRENT" : "LEVEL 27: KEY PROGRESSION";
            case 28: return tr ? "SEVIYE 28: SUPER ESNEKLIK" : "LEVEL 28: HIGH VAULT";
            case 29: return tr ? "SEVIYE 29: TERS HAVUZ" : "LEVEL 29: TRAJECTORY SPLIT";
            case 30: return tr ? "SEVIYE 30: OKYANUS MABEDI" : "LEVEL 30: ATLANTIS APEX";
            case 31: return tr ? "SEVIYE 31: IKI ASAMALI KILIT" : "LEVEL 31: DUAL LOCK MATRIX";
            case 32: return tr ? "SEVIYE 32: GEOMETRIK SEKTIRME" : "LEVEL 32: PING-PONG BOUNCE";
            case 33: return tr ? "SEVIYE 33: ANAHTARI KURTAR" : "LEVEL 33: RESCUE THE KEY";
            case 34: return tr ? "SEVIYE 34: TRAMBOLIN KALKANI" : "LEVEL 34: BOUNCER DEFLECTOR";
            case 35: return tr ? "SEVIYE 35: IKILI RENK AKVARYUMU" : "LEVEL 35: TWIN REEF";
            case 36: return tr ? "SEVIYE 36: HIZLI SEKME" : "LEVEL 36: SLOPED ACCELERATOR";
            case 37: return tr ? "SEVIYE 37: CAPRAZ SICRAMA" : "LEVEL 37: CRISS-CROSS BOUNCE";
            case 38: return tr ? "SEVIYE 38: KILITLI HAZNE" : "LEVEL 38: LOCKED CHAMBER";
            case 39: return tr ? "SEVIYE 39: CILGIN ROTA" : "LEVEL 39: WILD TRIPLE BOUNCE";
            case 40: return tr ? "SEVIYE 40: ATLANTIS TACI FINALI" : "LEVEL 40: CROWN OF ATLANTIS";

            // Dünya 3: Lav Ocağı (41-60)
            case 41: return tr ? "SEVIYE 41: OBSIDYEN DIKENLER" : "LEVEL 41: OBSIDIAN SPIKES";
            case 42: return tr ? "SEVIYE 42: DIKENLI CUKUR" : "LEVEL 42: SPIKED PIT";
            case 43: return tr ? "SEVIYE 43: SOLUCAN DELIGI (PORTAL)" : "LEVEL 43: WORMHOLE PORTAL";
            case 44: return tr ? "SEVIYE 44: DIKENLERIN USTUNDEN ATLAMA" : "LEVEL 44: SPIKE BYPASS";
            case 45: return tr ? "SEVIYE 45: HIZ KORUNUMLU PORTAL" : "LEVEL 45: MOMENTUM PORTAL";
            case 46: return tr ? "SEVIYE 46: BOMBA ZINCIRI" : "LEVEL 46: BOMB CASCADE CHAIN";
            case 47: return tr ? "SEVIYE 47: DIKENLI LABIRENT" : "LEVEL 47: SPIKED CORRIDOR";
            case 48: return tr ? "SEVIYE 48: IKIZ PORTALLAR" : "LEVEL 48: TWIN PORTALS MATRIX";
            case 49: return tr ? "SEVIYE 49: RENK ISINLAMA" : "LEVEL 49: TELEPORT & INFUSE";
            case 50: return tr ? "SEVIYE 50: MAGMA KALBI (APEX)" : "LEVEL 50: HEART OF MAGMA (APEX)";
            case 51: return tr ? "SEVIYE 51: PORTAL DONGUSU" : "LEVEL 51: INFINITE LOOP";
            case 52: return tr ? "SEVIYE 52: DIKEN TUZAGI" : "LEVEL 52: SPIKE TRAP";
            case 53: return tr ? "SEVIYE 53: BOMBA ISINLAMA" : "LEVEL 53: BOMB TELEPORT";
            case 54: return tr ? "SEVIYE 54: KILITLI PORTAL" : "LEVEL 54: LOCKED PORTAL";
            case 55: return tr ? "SEVIYE 55: TRAMBOLIN + PORTAL" : "LEVEL 55: BOUNCE TO PORTAL";
            case 56: return tr ? "SEVIYE 56: LAV SELALESI" : "LEVEL 56: LAVA WATERFALL";
            case 57: return tr ? "SEVIYE 57: ZIKZAK DIKENLER" : "LEVEL 57: ZIGZAG SPIKES";
            case 58: return tr ? "SEVIYE 58: CIFTE ISINLAMA CIKMAZI" : "LEVEL 58: PORTAL DILEMMA";
            case 59: return tr ? "SEVIYE 59: UCLU BOMBA TAHLIYESI" : "LEVEL 59: TRIPLE BOMB DEFUSAL";
            case 60: return tr ? "SEVIYE 60: LAV TANRISI FINALI" : "LEVEL 60: MAGMA GOD GRAND FINALE";

            // Dünya 4: Kozmik Bulutsu (61-80)
            case 61: return tr ? "SEVIYE 61: KUANTUM COGALTICI (x2)" : "LEVEL 61: QUANTUM MULTIPLIER (x2)";
            case 62: return tr ? "SEVIYE 62: BUYUK PATLAMA (x3)" : "LEVEL 62: BIG BANG MULTIPLIER (x3)";
            case 63: return tr ? "SEVIYE 63: RENK FILTRESI (NEON MAVI)" : "LEVEL 63: NEON BLUE FILTER GATE";
            case 64: return tr ? "SEVIYE 64: CIFT FILTRE GECIDI" : "LEVEL 64: DUAL COLOR PRISM";
            case 65: return tr ? "SEVIYE 65: COGALT + RENKLENDIR" : "LEVEL 65: MULTIPLY & INFUSE";
            case 66: return tr ? "SEVIYE 66: BOMBA COGALTMA TUZAGI" : "LEVEL 66: BOMB DUPLICATION PERIL";
            case 67: return tr ? "SEVIYE 67: IKIZ COGALTICI (x2 + x2)" : "LEVEL 67: TWIN MULTIPLIER (x4)";
            case 68: return tr ? "SEVIYE 68: PRIZMATIK BOLME" : "LEVEL 68: PRISMATIC SPLIT";
            case 69: return tr ? "SEVIYE 69: COGALTICI VE DIKENLER" : "LEVEL 69: MULTIPLIER & SPIKES";
            case 70: return tr ? "SEVIYE 70: NEBULA CEKIRDEGI (APEX)" : "LEVEL 70: NEBULA CORE APEX";
            case 71: return tr ? "SEVIYE 71: TERS FILTRE ODASI" : "LEVEL 71: INVERTED COLOR VAULT";
            case 72: return tr ? "SEVIYE 72: COGALTICI YAGMURU" : "LEVEL 72: MULTIPLIER DELUGE";
            case 73: return tr ? "SEVIYE 73: PORTAL + COGALTICI" : "LEVEL 73: PORTAL MULTIPLIER LAUNCH";
            case 74: return tr ? "SEVIYE 74: SECICI RENK KAPISI" : "LEVEL 74: SELECTIVE COLOR SIEVE";
            case 75: return tr ? "SEVIYE 75: GEOMETRIK PRIZMA" : "LEVEL 75: GEOMETRIC RAINBOW";
            case 76: return tr ? "SEVIYE 76: x3 LABIRENTI" : "LEVEL 76: TRIPLE DENSITY MAZE";
            case 77: return tr ? "SEVIYE 77: COGALTICI VE BOMBA BARIKATI" : "LEVEL 77: MULTIPLIER VS BOMB SHIELD";
            case 78: return tr ? "SEVIYE 78: KUANTUM SENTEZ" : "LEVEL 78: LOCKED QUANTUM SYNTHESIS";
            case 79: return tr ? "SEVIYE 79: YILDIZ TOZU CAGLAYANI" : "LEVEL 79: STARDUST CASCADE";
            case 80: return tr ? "SEVIYE 80: KOZMIK BULUTSU FINALI" : "LEVEL 80: COSMIC NEBULA GRAND FINALE";

            // Dünya 5: Zümrüt Zen Tapınağı (81-100)
            case 81:  return tr ? "SEVIYE 81: KADIM CARK" : "LEVEL 81: ANCIENT COGWHEEL";
            case 82:  return tr ? "SEVIYE 82: CIFT ZIT PERVANE" : "LEVEL 82: DUAL OPPOSING ROTORS";
            case 83:  return tr ? "SEVIYE 83: CARKLI BOMBA TAHLIYESI" : "LEVEL 83: ROTOR BOMB DISPOSAL";
            case 84:  return tr ? "SEVIYE 84: DORT KOLLU ZEN CARKI" : "LEVEL 84: 4-PADDLE ZEN WHEEL";
            case 85:  return tr ? "SEVIYE 85: HIZLI DISLI" : "LEVEL 85: HIGH-SPEED GEAR";
            case 86:  return tr ? "SEVIYE 86: CARK + TRAMBOLIN" : "LEVEL 86: ROTOR & BOUNCER";
            case 87:  return tr ? "SEVIYE 87: ASIMETRIK KANATLAR" : "LEVEL 87: ASYMMETRIC BLADES";
            case 88:  return tr ? "SEVIYE 88: CARK USTUNDE KILIT" : "LEVEL 88: LOCK ON ROTOR";
            case 89:  return tr ? "SEVIYE 89: DONEN ENGEL DUVARI" : "LEVEL 89: ROTATING BARRIER";
            case 90:  return tr ? "SEVIYE 90: ZEN MABEDI ZIRVESI (APEX)" : "LEVEL 90: ZEN SANCTUARY APEX";
            case 91:  return tr ? "SEVIYE 91: CARK VE PORTALLAR" : "LEVEL 91: ROTOR & PORTALS";
            case 92:  return tr ? "SEVIYE 92: DIKENLI CARK ODASI" : "LEVEL 92: SPIKED ROTOR VAULT";
            case 93:  return tr ? "SEVIYE 93: IKI KADEMELI DEGIRMEN" : "LEVEL 93: TWO-STAGE WATERMILL";
            case 94:  return tr ? "SEVIYE 94: DONEN RENK FILTRESI" : "LEVEL 94: ROTATING COLOR FILTER";
            case 95:  return tr ? "SEVIYE 95: BOMBA SEPETI" : "LEVEL 95: ROTOR BOMB TRAP";
            case 96:  return tr ? "SEVIYE 96: SENKRONIZE PERVANELER" : "LEVEL 96: SYNCHRONIZED ROTORS";
            case 97:  return tr ? "SEVIYE 97: BAMBU AKISI" : "LEVEL 97: BAMBOO STREAM";
            case 98:  return tr ? "SEVIYE 98: CARKLI COGALTICI" : "LEVEL 98: ROTOR MULTIPLIER";
            case 99:  return tr ? "SEVIYE 99: MILIMETRIK CARK" : "LEVEL 99: PRECISION GEAR";
            case 100: return tr ? "SEVIYE 100: ZEN BUYUK TAPINAK FINALI" : "LEVEL 100: ZEN GRAND TEMPLE FINALE";

            // Dünya 6: Güneş Serabı (101-120)
            case 101: return tr ? "SEVIYE 101: KIZIL LAZER BARICATI" : "LEVEL 101: CRIMSON PULSE LASER";
            case 102: return tr ? "SEVIYE 102: LAZERLI KORIDOR" : "LEVEL 102: TIMED LASER CHUTE";
            case 103: return tr ? "SEVIYE 103: SABIT LAZER FIRINI" : "LEVEL 103: INCINERATOR BEAM";
            case 104: return tr ? "SEVIYE 104: IKIZ FAZ LAZERLERI" : "LEVEL 104: DUAL PHASE LASERS";
            case 105: return tr ? "SEVIYE 105: CAPRAZ LAZER TUZAGI" : "LEVEL 105: CROSS LASER TRAP";
            case 106: return tr ? "SEVIYE 106: LAZER + TRAMBOLIN" : "LEVEL 106: LASER BOUNCE VAULT";
            case 107: return tr ? "SEVIYE 107: LAZERLI KILIT" : "LEVEL 107: LASER-GUARDED LOCK";
            case 108: return tr ? "SEVIYE 108: DONEN CARK VE LAZER" : "LEVEL 108: ROTOR LASER SHIELD";
            case 109: return tr ? "SEVIYE 109: HIZLI NABIZ LAZERLERI" : "LEVEL 109: RAPID PULSE BEAMS";
            case 110: return tr ? "SEVIYE 110: GUNES PIRAMIDI (APEX)" : "LEVEL 110: SOLAR PYRAMID APEX";
            case 111: return tr ? "SEVIYE 111: DIKEY PLAZMA ISINI" : "LEVEL 111: VERTICAL PLASMA BEAM";
            case 112: return tr ? "SEVIYE 112: LAZERLI PORTALLAR" : "LEVEL 112: LASER PORTAL GAUNTLET";
            case 113: return tr ? "SEVIYE 113: LAZER KALKANI PIMI" : "LEVEL 113: LASER SHIELD PIN";
            case 114: return tr ? "SEVIYE 114: UC ASAMALI LAZER MERDIVENI" : "LEVEL 114: TRIPLE LASER STEP";
            case 115: return tr ? "SEVIYE 115: BOMBA LAZER FUNYESI" : "LEVEL 115: LASER BOMB DETONATOR";
            case 116: return tr ? "SEVIYE 116: LAZER LABIRENTI" : "LEVEL 116: LASER LABYRINTH";
            case 117: return tr ? "SEVIYE 117: RENK FILTRESI VE LAZER" : "LEVEL 117: PRISM FILTER & LASER";
            case 118: return tr ? "SEVIYE 118: IKIZ CARK LAZER KALKANI" : "LEVEL 118: TWIN ROTOR LASER SHIELD";
            case 119: return tr ? "SEVIYE 119: ULTRA LAZER KAOSU" : "LEVEL 119: ULTRA LASER CHAOS";
            case 120: return tr ? "SEVIYE 120: GUNES HUKUMDARI FINALI" : "LEVEL 120: SOLAR OVERLORD GRAND FINALE";

            // Dünya 7: Toksik Biyosfer (121-140)
            case 121: return tr ? "SEVIYE 121: TOKSIK ASIT TANITIMI" : "LEVEL 121: TOXIC ACID ENTRY";
            case 122: return tr ? "SEVIYE 122: CIFT ASIT CUKURU" : "LEVEL 122: DUAL ACID PITS";
            case 123: return tr ? "SEVIYE 123: ASIT BARIKATI" : "LEVEL 123: ACID BARRICADE";
            case 124: return tr ? "SEVIYE 124: ZIKZAK ASIT RAMPASI" : "LEVEL 124: ZIGZAG ACID SLIDE";
            case 125: return tr ? "SEVIYE 125: BOMBA VE ASIT" : "LEVEL 125: BOMB & ACID";
            case 126: return tr ? "SEVIYE 126: TRAMBOLINLI SU ATISI" : "LEVEL 126: BOUNCE TO NEUTRALIZE";
            case 127: return tr ? "SEVIYE 127: KILITLI SU HAZNESI" : "LEVEL 127: LOCKED WATER VAULT";
            case 128: return tr ? "SEVIYE 128: GENIS ASIT DENIZI" : "LEVEL 128: VAST ACID SEA";
            case 129: return tr ? "SEVIYE 129: ASIT VE DONEN CARK" : "LEVEL 129: ROTOR & ACID BASIN";
            case 130: return tr ? "SEVIYE 130: KIMYASAL REAKTOR (APEX)" : "LEVEL 130: CHEMICAL REACTOR APEX";
            case 131: return tr ? "SEVIYE 131: PORTALLI ASIT NOTRALIZASYONU" : "LEVEL 131: WARP NEUTRALIZATION";
            case 132: return tr ? "SEVIYE 132: RENKLI VE GRI ASIT AYRIMI" : "LEVEL 132: SELECTIVE ACID CROSSING";
            case 133: return tr ? "SEVIYE 133: LAZERLI ASIT LABORATUVARI" : "LEVEL 133: LASER ACID LAB";
            case 134: return tr ? "SEVIYE 134: IKIZ HAZNELI ASIT TUZAGI" : "LEVEL 134: DUAL CHAMBER ACID TRAP";
            case 135: return tr ? "SEVIYE 135: x2 COGALTILMIS SU" : "LEVEL 135: DUPED WATER CASCADE";
            case 136: return tr ? "SEVIYE 136: DIKENLI ASIT HAVZASI" : "LEVEL 136: SPIKED ACID BASIN";
            case 137: return tr ? "SEVIYE 137: HIZLI SU KAYDIRAGI" : "LEVEL 137: HIGH-SPEED WATER SLIDE";
            case 138: return tr ? "SEVIYE 138: ASITLI CAPRAZ GECIT" : "LEVEL 138: ACID CRISS-CROSS";
            case 139: return tr ? "SEVIYE 139: KIMYASAL SAFLIK" : "LEVEL 139: CHEMICAL PURITY";
            case 140: return tr ? "SEVIYE 140: BIYOSFER CEKIRDEGI FINALI" : "LEVEL 140: BIOSPHERE CORE FINALE";

            // Dünya 8: Aerodinamik Gökyüzü (141-160)
            case 141: return tr ? "SEVIYE 141: YUKARI RUZGAR JETI" : "LEVEL 141: VERTICAL WIND JET";
            case 142: return tr ? "SEVIYE 142: YATAY HAVA TUNELI" : "LEVEL 142: HORIZONTAL WIND TUNNEL";
            case 143: return tr ? "SEVIYE 143: DIKENLERIN USTUNDEN UCUS" : "LEVEL 143: FLIGHT OVER SPIKES";
            case 144: return tr ? "SEVIYE 144: CIFT RUZGAR ASANSORU" : "LEVEL 144: DUAL WIND ELEVATOR";
            case 145: return tr ? "SEVIYE 145: RUZGARLI BOMBA AYRIMI" : "LEVEL 145: WIND BOMB SEPARATOR";
            case 146: return tr ? "SEVIYE 146: RUZGAR VE TRAMBOLIN" : "LEVEL 146: WIND & BOUNCER";
            case 147: return tr ? "SEVIYE 147: RUZGARLI PORTAL UCUSU" : "LEVEL 147: WIND PORTAL LAUNCH";
            case 148: return tr ? "SEVIYE 148: KILITLI RUZGAR KANALI" : "LEVEL 148: LOCKED WIND DUCT";
            case 149: return tr ? "SEVIYE 149: KARSI RUZGAR AKIMI" : "LEVEL 149: HEADWIND EQUILIBRIUM";
            case 150: return tr ? "SEVIYE 150: GOKYUZU KALESI (APEX)" : "LEVEL 150: SKY CITADEL APEX";
            case 151: return tr ? "SEVIYE 151: RUZGARLA COGALTMA" : "LEVEL 151: WIND MULTIPLIER FLIGHT";
            case 152: return tr ? "SEVIYE 152: RUZGARLI DONEN CARK" : "LEVEL 152: WIND & ROTATING GEAR";
            case 153: return tr ? "SEVIYE 153: ASAGI ITICI RUZGAR" : "LEVEL 153: DOWNWARD GALE";
            case 154: return tr ? "SEVIYE 154: RUZGARLI RENK FILTRESI" : "LEVEL 154: AERIAL COLOR FILTER";
            case 155: return tr ? "SEVIYE 155: RUZGAR VE ASIT HAVUZU" : "LEVEL 155: WIND WATER INJECTION";
            case 156: return tr ? "SEVIYE 156: HAVADA ASILI KALANLAR" : "LEVEL 156: ZERO-G FLOAT";
            case 157: return tr ? "SEVIYE 157: ZIKZAK RUZGAR TUNELLERI" : "LEVEL 157: ZIGZAG WIND CORRIDORS";
            case 158: return tr ? "SEVIYE 158: FIRTINA ZAMANLAMASI" : "LEVEL 158: GALE TIMING";
            case 159: return tr ? "SEVIYE 159: RUZGAR KASIRGASI" : "LEVEL 159: AERO VORTEX";
            case 160: return tr ? "SEVIYE 160: FIRTINA TANRISI FINALI" : "LEVEL 160: TEMPEST GOD GRAND FINALE";

            // Dünya 9: Kuantum Kaos Reaktörü (161-180)
            case 161: return tr ? "SEVIYE 161: LAZERLI RUZGAR TUNELI" : "LEVEL 161: LASER WIND TUNNEL";
            case 162: return tr ? "SEVIYE 162: ASIT VE LAZER KAFESI" : "LEVEL 162: ACID & LASER CAGE";
            case 163: return tr ? "SEVIYE 163: RUZGARLA ASIT NOTRALIZASYONU" : "LEVEL 163: WIND WATER CANNON";
            case 164: return tr ? "SEVIYE 164: CARKLI LAZER LABIRENTI" : "LEVEL 164: ROTOR LASER LABYRINTH";
            case 165: return tr ? "SEVIYE 165: DORT BOYUTLU KAOS" : "LEVEL 165: 4D QUANTUM CHAOS";
            case 166: return tr ? "SEVIYE 166: BOMBA, ASIT VE SU UÇGENI" : "LEVEL 166: BOMB ACID WATER TRIANGLE";
            case 167: return tr ? "SEVIYE 167: RUZGARLI KILIT AVI" : "LEVEL 167: AERIAL KEY RECOVERY";
            case 168: return tr ? "SEVIYE 168: CIFT ASIT CIFT RUZGAR" : "LEVEL 168: DUAL ACID DUAL WIND";
            case 169: return tr ? "SEVIYE 169: SENKRONIZE LAZER FIRTINASI" : "LEVEL 169: SYNCHRONIZED LASER GALE";
            case 170: return tr ? "SEVIYE 170: KAOS CEKIRDEGI (APEX)" : "LEVEL 170: CHAOS CORE APEX";
            case 171: return tr ? "SEVIYE 171: SUZULEN SU DAMLASI" : "LEVEL 171: GLIDING WATER DROPLET";
            case 172: return tr ? "SEVIYE 172: TERS ACI SICRAMASI" : "LEVEL 172: INVERTED ANGLE REBOUND";
            case 173: return tr ? "SEVIYE 173: DONEN ASIT KAZANI" : "LEVEL 173: ROTATING ACID CAULDRON";
            case 174: return tr ? "SEVIYE 174: CIFTE KILITLI FIRTINA" : "LEVEL 174: DUAL LOCKED TEMPEST";
            case 175: return tr ? "SEVIYE 175: COGALTILMIS SU DALGASI" : "LEVEL 175: MULTIPLIED WATER WAVE";
            case 176: return tr ? "SEVIYE 176: LAZERLE BOMBA PATLATMA BULMACASI" : "LEVEL 176: LASER BOMB DETONATION";
            case 177: return tr ? "SEVIYE 177: IKIZ CARKLI ASIT GECIDI" : "LEVEL 177: TWIN ROTOR ACID CROSSING";
            case 178: return tr ? "SEVIYE 178: KUANTUM PRIZMA MATRISI" : "LEVEL 178: QUANTUM PRISM MATRIX";
            case 179: return tr ? "SEVIYE 179: BUYUK SENTEZ CAGLAYANI" : "LEVEL 179: GRAND SYNTHESIS CASCADE";
            case 180: return tr ? "SEVIYE 180: KAOS EFENDISI FINALI" : "LEVEL 180: CHAOS LORD GRAND FINALE";

            // Dünya 10: Kozmik Tanrı Zirvesi (181-200)
            case 181: return tr ? "SEVIYE 181: ZIRVEYE TIRMANIS" : "LEVEL 181: ASCENT TO THE SUMMIT";
            case 182: return tr ? "SEVIYE 182: KUANTUM DANSI" : "LEVEL 182: QUANTUM DANCE";
            case 183: return tr ? "SEVIYE 183: IKIZ ASIT REAKTORU" : "LEVEL 183: TWIN ACID REACTOR";
            case 184: return tr ? "SEVIYE 184: YORUNGE FIRTINASI" : "LEVEL 184: ORBITAL VORTEX";
            case 185: return tr ? "SEVIYE 185: KUSURSUZ ZAMANLAMA LAZERLERI" : "LEVEL 185: FLAWLESS TIMING LASERS";
            case 186: return tr ? "SEVIYE 186: PRIZMATIK SAHESER" : "LEVEL 186: PRISMATIC MASTERPIECE";
            case 187: return tr ? "SEVIYE 187: UC KILITLI EFSANE" : "LEVEL 187: TRIPLE LOCK LEGEND";
            case 188: return tr ? "SEVIYE 188: FIRTINA VE LAV CARPISMASI" : "LEVEL 188: GALE & LAVA COLLISION";
            case 189: return tr ? "SEVIYE 189: CIFT CARKLI TELEPORTASYON" : "LEVEL 189: DUAL ROTOR TELEPORTATION";
            case 190: return tr ? "SEVIYE 190: KOZMIK ZIRVE (APEX 1)" : "LEVEL 190: COSMIC PINNACLE APEX";
            case 191: return tr ? "SEVIYE 191: ASIT LABIRENTI VE SU INCILERI" : "LEVEL 191: ACID LABYRINTH & PEARLS";
            case 192: return tr ? "SEVIYE 192: AYNA YANSIMASI" : "LEVEL 192: MIRROR REFLECTION";
            case 193: return tr ? "SEVIYE 193: TERS YERCEKIMI RUYASI" : "LEVEL 193: INVERTED GRAVITY DREAM";
            case 194: return tr ? "SEVIYE 194: 5 BOMBALI MAYIN TARLASI" : "LEVEL 194: 5-BOMB MINEFIELD";
            case 195: return tr ? "SEVIYE 195: x3 COGALTICI PATLAMASI" : "LEVEL 195: x3 MULTIPLIER OUTBURST";
            case 196: return tr ? "SEVIYE 196: KUANTUM CEMBERI" : "LEVEL 196: QUANTUM CIRCLE";
            case 197: return tr ? "SEVIYE 197: SON SINAV: DENGE" : "LEVEL 197: FINAL TRIAL: EQUILIBRIUM";
            case 198: return tr ? "SEVIYE 198: BUYUK USTA IMTIHANI" : "LEVEL 198: GRANDMASTER'S ORDEAL";
            case 199: return tr ? "SEVIYE 199: SAFAK VAKTI" : "LEVEL 199: BREAK OF DAWN";
            case 200: return tr ? "SEVIYE 200: KOZMIK TANRI: BUYUK FINAL" : "LEVEL 200: COSMIC GOD: GRAND FINALE";

            default: return tr ? ("SEVIYE " + levelNumber) : ("LEVEL " + levelNumber);
        }
    }
}

