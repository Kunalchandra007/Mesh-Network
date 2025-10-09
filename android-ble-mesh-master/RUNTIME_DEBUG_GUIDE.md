# MeshNetwork App - Runtime Debug Guide

## 🔧 **How to Debug Runtime Crashes**

### **Step 1: Check Android Studio Logcat**

1. **Open Android Studio**
2. **Connect your device/emulator**
3. **Go to View → Tool Windows → Logcat**
4. **Filter by your app**: Type `com.meshnetwork.app` in the filter box
5. **Look for RED error messages** when the app crashes

### **Step 2: Common Crash Causes & Solutions**

#### **1. Resource Not Found Errors**
```
android.content.res.Resources$NotFoundException: Resource ID #0x7f...
```
**Solution**: Missing drawable/color/string resources
**Fixed**: ✅ Added missing resources (ic_launcher, colors.xml, rounded_button.xml)

#### **2. ClassNotFoundException**
```
java.lang.ClassNotFoundException: com.meshnetwork.app.ui.MainActivity
```
**Solution**: Package name mismatch or missing classes
**Fixed**: ✅ All classes exist in correct package

#### **3. Permission Denied**
```
java.lang.SecurityException: Permission denied
```
**Solution**: Grant required permissions
**To Fix**: 
- Go to Settings → Apps → MeshNetwork → Permissions
- Enable Location and Bluetooth permissions

#### **4. Layout Inflation Errors**
```
android.view.InflateException: Binary XML file line #X
```
**Solution**: Layout file has invalid references
**Fixed**: ✅ All layout files validated

### **Step 3: Test the App Step by Step**

#### **Test 1: Basic Launch**
1. Launch the app from app drawer
2. **Expected**: Main screen with login options appears
3. **If crashes**: Check logcat for specific error

#### **Test 2: User Mode**
1. Tap "User Mode (No Login Required)"
2. **Expected**: User panel opens
3. **If crashes**: Check logcat for location permission issues

#### **Test 3: Admin Login**
1. Enter username: `admin`
2. Enter password: `admin123`
3. Tap "Admin Login"
4. **Expected**: Admin panel opens
5. **If crashes**: Check logcat for layout issues

### **Step 4: Grant Permissions Manually**

If the app crashes due to permissions:

1. **Go to Android Settings**
2. **Apps → MeshNetwork**
3. **Permissions**
4. **Enable**:
   - ✅ Location (Fine location)
   - ✅ Bluetooth
   - ✅ Bluetooth Admin

### **Step 5: Clear App Data**

If still crashing:

1. **Go to Android Settings**
2. **Apps → MeshNetwork**
3. **Storage → Clear Data**
4. **Try launching again**

### **Step 6: Check Device Compatibility**

**Minimum Requirements**:
- ✅ Android 6.0+ (API 23+)
- ✅ Bluetooth LE support
- ✅ Location services enabled

### **Step 7: Alternative Launch Method**

Try launching from Android Studio:
1. **Open Android Studio**
2. **Open this project**
3. **Click Run button (▶️)**
4. **Select your device**
5. **Watch logcat for errors**

### **Common Error Messages & Solutions**

| Error Message | Solution |
|---------------|----------|
| `Resources$NotFoundException` | Missing resource files (✅ Fixed) |
| `ClassNotFoundException` | Wrong package name (✅ Fixed) |
| `SecurityException` | Grant permissions manually |
| `InflateException` | Layout XML error (✅ Fixed) |
| `NullPointerException` | Missing initialization |

### **If Still Crashing**

**Send me the logcat output**:
1. Filter logcat by `com.meshnetwork.app`
2. Copy the red error messages
3. Share the stack trace

**Or try this command**:
```bash
adb logcat | grep "MeshNetwork"
```

---

## 🎯 **Expected App Flow**

1. **Launch** → Main screen with "MeshNetwork" title
2. **User Mode** → User panel with SOS button
3. **Admin Login** → Enter admin/admin123 → Admin panel
4. **Grant Permissions** → Allow location when prompted
5. **Test Features** → SOS button, location display, messages

**The app should now work without crashes!** 🚀
