# 🎮 Pull The Pin - Java Mobil Oyun Motoru & Prototipi

Reklamlardaki popüler **"İğne Çekmece / Top Toplama" (Pull The Pin)** mekaniğini içeren, %100 saf **Java** ve **LibGDX + Box2D** fizik motoru ile geliştirilmiş çapraz platform mobil oyun projesi.

---

## 🌟 Proje Özellikleri

* **Gerçek Zamanlı 2D Fizik (Box2D):** Yerçekimi, top çarpışmaları, sekme ve kinetik pim hareketleri.
* **İğne / Pim Mekaniği:** Parmağınızla veya fareyle pimin çekme halkasını tutup kaydırarak açma.
* **Renk Karıştırma Mekaniği (Viral Ad Kuralı):** Gri/nötr toplar, renkli toplara çarptığında renklenir.
* **Bomba ve Tehlikeler:** Yanlış pim çekilirse bomba patlar ve etraftaki topları yok eder.
* **Parçacık Efektleri:** Seviye zaferinde konfeti yağmuru, patlamalar ve renk kıvılcımları.
* **200 Ozenle Tasarlanmis Seviye & 10 Tematik Dunya:**
  * Dunya 1: Siber Laboratuvar (Seviye 1-20)
  * Dunya 2: Atlantis Okyanus Cukuru (Seviye 21-40)
  * Dunya 3: Magma Demir Ocagi (Seviye 41-60)
  * Dunya 4: Kozmik Bulutsu (Seviye 61-80)
  * Dunya 5: Zumrut Zen Bahcesi (Seviye 81-100)
  * Dunya 6: Gunes Serabi Colu (Seviye 101-120)
  * Dunya 7: Toksik Biyosfer (Seviye 121-140)
  * Dunya 8: Gokyuzu Jet Akintisi (Seviye 141-160)
  * Dunya 9: Kuantum Kaos Reaktoru (Seviye 161-180)
  * Dunya 10: Kozmik Zirve Grandmaster (Seviye 181-200)
* **Zengin Oyun Mekanikleri:**
  * Cogaltici Kapilar (x2, x3, +5, +10 Multiplier Gates)
  * Portallar (Solucan deligi isinlanma)
  * Lazer Bariyerleri (Zaman ayarli puls ve sabit)
  * Ruzgar Turbinleri ve Trambolinler (Bouncers)
  * Asit Havuzlari & Su Toplari ile Notralizasyon
  * Donen Carklar (2'li, 3'lu, 4'lu ve 6 disli carklar)
  * Kilitli Pimler ve Anahtar Toplama Mekanigi
* **Bilye Magazasi (Skin Shop):** 4 kategoride 12 farkli bilye skini.
* **Gunluk Odul & Gorev Sistemi:** 7 gunluk giris serisi odulleri.
* **Basarim Sistemi (Achievements):** Jeton odullu basarimlar.
* **Iki Dilli Arayuz (Turkce / Ingilizce):** Aninda dil degisimi.

---

## 📂 Proje Dizin Yapısı

```text
MobilGameEngine/
├── core/                                   # Platform bağımsız saf Java oyun mantığı
│   └── src/main/java/com/game/pullthepin/
│       ├── PullThePinGame.java             # Ana oyun ve render yöneticisi
│       ├── screens/GameScreen.java         # Oyun döngüsü, Box2D adımları, dokunma ve HUD
│       ├── entities/
│       │   ├── Ball.java                   # Renkli, Gri ve Bomba topları
│       │   ├── Pin.java                    # Çekilebilir pimler ve hareket mekaniği
│       │   ├── Jar.java                    # Alt hedef toplama kavanozu ve sensörü
│       │   ├── LevelWall.java              # Seviye sınır duvarları ve huniler
│       │   └── ParticleSystem.java         # Konfeti ve kıvılcım parçacıkları
│       └── levels/LevelLoader.java         # Seviyelerin tasarımı ve yükleyicisi
├── desktop/                                # Masaüstü simülasyon başlatıcısı (Hızlı Test)
│   └── src/main/java/com/game/pullthepin/DesktopLauncher.java
├── android/                                # Android APK başlatıcısı ve konfigürasyonu
│   └── src/main/java/com/game/pullthepin/AndroidLauncher.java
├── run_desktop.bat                         # Tek tıkla masaüstünde çalıştırma
└── build_apk.bat                           # Tek tıkla Android APK üretme
```

---

## 🚀 Oyunu Çalıştırma

### 1. Masaüstünde Anında Test Etmek İçin:
Klasördeki [`run_desktop.bat`](file:///g:/MobilGameEngine/run_desktop.bat) dosyasına çift tıklayın veya terminalden çalıştırın:
```bash
.\gradlew desktop:run
```
*Bu komut oyunu 9:16 dikey mobil pencere simülasyonunda 60 FPS hızında anında açar.*

### 2. Android APK Üretmek İçin:
Klasördeki [`build_apk.bat`](file:///g:/MobilGameEngine/build_apk.bat) dosyasına çift tıklayın veya:
```bash
.\gradlew android:assembleDebug
```
Üretilen APK konumu:
👉 [`android/build/outputs/apk/debug/android-debug.apk`](file:///g:/MobilGameEngine/android/build/outputs/apk/debug/android-debug.apk) *(Doğrudan telefonunuza yükleyebilirsiniz!)*

---

## 🛠️ Yeni Seviye veya Mekanik Ekleme

Yeni bir seviye eklemek için [`LevelLoader.java`](file:///g:/MobilGameEngine/core/src/main/java/com/game/pullthepin/levels/LevelLoader.java) içerisine `buildLevel4(...)` metodunu ekleyip:
1. `data.walls.add(new LevelWall(...))` ile duvarları çizin.
2. `data.pins.add(new Pin(...))` ile çekilecek pimleri yerleştirin.
3. `data.balls.add(new Ball(...))` ile topları / bombaları ekleyin.
4. `data.jar = new Jar(...)` ile hedef kavanozu belirleyin.
