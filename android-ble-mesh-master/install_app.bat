@echo off
echo Installing MeshNetwork APK...
echo.

REM Check if ADB is available
where adb >nul 2>nul
if %ERRORLEVEL% NEQ 0 (
    echo ADB not found in PATH. Please install Android SDK Platform Tools.
    echo You can download it from: https://developer.android.com/studio/releases/platform-tools
    echo.
    echo Alternatively, manually install the APK file:
    echo %~dp0app\build\outputs\apk\debug\drone-mesh.apk
    pause
    exit /b 1
)

REM Check if device is connected
adb devices | findstr "device" >nul
if %ERRORLEVEL% NEQ 0 (
    echo No Android device found. Please connect your device and enable USB debugging.
    echo.
    echo To enable USB debugging:
    echo 1. Go to Settings ^> About Phone
    echo 2. Tap "Build Number" 7 times
    echo 3. Go back to Settings ^> Developer Options
    echo 4. Enable "USB Debugging"
    pause
    exit /b 1
)

REM Install the APK
echo Installing APK to connected device...
adb install -r "%~dp0app\build\outputs\apk\debug\drone-mesh.apk"

if %ERRORLEVEL% EQU 0 (
    echo.
    echo ✅ Installation successful! The app should now be available on your device.
    echo Look for "MeshNetwork" in your app drawer.
    echo.
    echo 🚀 BLE MESH FEATURES ENABLED:
    echo • Admin Login: admin / admin123
    echo • User Mode: Direct access (no login)
    echo • Emergency SOS with location
    echo • Real-time BLE communication
    echo • Works without internet connection
    echo.
    echo 📖 See TESTING_GUIDE.md for detailed testing instructions
) else (
    echo.
    echo ❌ Installation failed. Please check the error message above.
)

pause
