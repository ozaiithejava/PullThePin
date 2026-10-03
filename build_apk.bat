@echo off
cd /d "%~dp0"
set JAVA_HOME=C:\Users\ozaii\.jdks\temurin-17.0.16
if not exist "%JAVA_HOME%" (
    set JAVA_HOME=C:\Users\ozaii\.jdks\ms-17.0.16
)
echo [1/2] Android APK paketi olusturuluyor...
call gradlew.bat android:assembleDebug
echo.
echo [2/2] APK tamamlandi! Konum: android\build\outputs\apk\debug\android-debug.apk
pause
