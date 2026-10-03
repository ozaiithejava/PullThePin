@echo off
cd /d "%~dp0"
set JAVA_HOME=C:\Users\ozaii\.jdks\ms-17.0.16
echo [1/2] Pull The Pin masaustu modunda baslatiliyor...
call gradlew.bat desktop:run --no-daemon
pause
