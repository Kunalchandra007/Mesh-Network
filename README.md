# 🌐 Mesh Network

**Decentralized Communication Network using Bluetooth Low Energy (BLE)**

Mesh Network is an Android application that creates a robust, peer-to-peer mesh communication network using Bluetooth Low Energy (BLE) technology. This enables devices to communicate directly with each other without requiring internet connectivity or centralized infrastructure, making it ideal for emergency situations, remote areas, or scenarios where traditional communication networks are unavailable.

## 📱 Features

### 🔗 **BLE Mesh Networking**
- **Peer-to-peer communication** using Bluetooth Low Energy (BLE) technology
- **Offline functionality** - works completely without internet connectivity
- **Auto-discovery** of nearby devices running Mesh Network
- **Message relay** - extends communication range through device hopping
- **Real-time messaging** with instant delivery across the mesh

### 🚨 **Emergency Communication**
- **SOS signals** for immediate help requests with GPS coordinates
- **Emergency alerts** for critical information broadcasting
- **Location sharing** for coordination and rescue operations
- **Priority messaging** for urgent communications

### 👥 **Dual Interface System**
- **User Interface** - Simple, intuitive design for general users
- **Admin Interface** - Password-protected control panel for emergency authorities
- **Role-based access** - Different permissions for users and administrators

### 🗺️ **Location Services**
- **Real-time GPS tracking** - Automatic location detection and sharing
- **Coordinate display** - Shows current position in both panels
- **Location-based alerts** - Send messages to specific geographic areas
- **Emergency location transmission** - SOS signals include precise coordinates

### 🎨 **Modern UI/UX**
- **Material Design** implementation
- **Clean, intuitive interface** optimized for emergency situations
- **Status indicators** for network connectivity and device status
- **Activity logs** for monitoring network operations

## 🏗️ **Architecture**

### **Core Components**
- **BLEMeshService** - Background service managing BLE connections and mesh operations
- **UserActivity** - User interface for general mesh network operations
- **AdminActivity** - Administrative interface for emergency management
- **MeshNetworkService** - Core mesh networking logic and message routing

### **Technology Stack**
- **Language**: Java (Android)
- **UI Framework**: Android Material Design
- **Mesh Technology**: Bluetooth Low Energy (BLE)
- **Location Services**: Android GPS and Location APIs
- **Architecture**: Service-based with Activity components

## 🔧 **BLE Mesh Implementation**

### **Bluetooth Low Energy Features**
- **BLE Advertising** - Device discovery and mesh formation
- **BLE Scanning** - Automatic detection of nearby mesh devices
- **GATT Communication** - Reliable data transmission between devices
- **Service Discovery** - Custom BLE services for mesh networking
- **Characteristic Management** - Dedicated characteristics for different message types

### **Mesh Network Capabilities**
- **Device Discovery** - Automatic detection and connection to nearby devices
- **Message Routing** - Intelligent message forwarding through the mesh
- **Network Formation** - Dynamic mesh topology creation and maintenance
- **Offline Operation** - Complete independence from internet connectivity
- **Range Extension** - Messages hop through multiple devices to reach distant nodes

## 🚀 **Getting Started**

### **Prerequisites**
- Android Studio (latest version)
- Android SDK 21+ (Android 5.0)
- Android device with Bluetooth Low Energy support
- Location services enabled on device

### **Installation**

1. **Clone the repository**
   ```bash
   git clone https://github.com/yourusername/Mesh-Network.git
   cd Mesh-Network
   ```

2. **Open in Android Studio**
   - Launch Android Studio
   - Select "Open an existing project"
   - Navigate to the cloned directory
   - Select the `android-ble-mesh-master` folder

3. **Build and Run**
   ```bash
   ./gradlew assembleDebug
   ```

### **Configuration**

#### **Admin Access**
- **Default Password**: `admin` / `admin123`
- **Location**: Admin panel login screen
- ⚠️ **Security Note**: Change default credentials before production deployment

## 📋 **Permissions Required**

### **Essential Permissions**
- `BLUETOOTH` - Basic Bluetooth functionality
- `BLUETOOTH_ADMIN` - Bluetooth administration and control
- `ACCESS_FINE_LOCATION` - For GPS coordinates and location services
- `ACCESS_COARSE_LOCATION` - For approximate location detection

### **Android 12+ Permissions**
- `BLUETOOTH_ADVERTISE` - For BLE advertising
- `BLUETOOTH_SCAN` - For BLE device scanning
- `BLUETOOTH_CONNECT` - For establishing BLE connections

## 🎯 **Usage**

### **For Regular Users**
1. **Launch the app** - Opens to user interface automatically
2. **Grant permissions** - Allow Bluetooth and location access when prompted
3. **View network status** - Check if mesh network is active
4. **Send SOS signals** - Use emergency button to send help requests with location
5. **Send messages** - Communicate with other users in the mesh network
6. **View activity logs** - Monitor network communications and device connections

### **For Administrators**
1. **Switch to Admin mode** - Access admin interface
2. **Enter credentials** - Use admin password to gain access
3. **Monitor network** - View all connected devices and their status
4. **Broadcast alerts** - Send emergency notifications to all users
5. **Manage communications** - Oversee mesh network operations
6. **Track locations** - Monitor user positions for emergency response

## 🔧 **Development**

### **Project Structure**
```
android-ble-mesh-master/
├── app/
│   ├── src/main/
│   │   ├── java/com/meshnetwork/app/
│   │   │   ├── services/
│   │   │   │   ├── BLEMeshService.java      # Core BLE mesh implementation
│   │   │   │   └── MeshNetworkService.java  # Mesh network management
│   │   │   ├── ui/
│   │   │   │   ├── UserActivity.java        # User interface
│   │   │   │   ├── AdminActivity.java       # Admin interface
│   │   │   │   └── BLEMeshActivity.java     # BLE testing interface
│   │   │   └── adapters/
│   │   ├── res/                             # UI resources and layouts
│   │   └── AndroidManifest.xml              # App configuration
│   ├── build.gradle                         # App dependencies
│   └── google-services.json                 # Firebase config (optional)
├── build.gradle                             # Project configuration
└── README.md                                # This file
```

### **Key Classes**
- `BLEMeshService` - Manages BLE advertising, scanning, and GATT communication
- `MeshNetworkService` - Handles mesh network logic and message routing
- `UserActivity` - User interface for general mesh operations
- `AdminActivity` - Administrative interface for emergency management

### **Building for Release**
```bash
./gradlew assembleRelease
```

## 🧪 **Testing**

### **BLE Mesh Testing**
1. **Install on multiple devices** - Use at least 2-3 Android devices
2. **Enable Bluetooth** - Ensure BLE is supported and enabled
3. **Grant permissions** - Allow all required permissions
4. **Test device discovery** - Verify devices can find each other
5. **Test messaging** - Send messages between devices
6. **Test SOS functionality** - Verify emergency signals work
7. **Test admin features** - Verify admin interface and controls

### **Network Range Testing**
- **Direct connection** - Test devices within ~10 meters
- **Extended range** - Test with devices acting as relays
- **Mesh formation** - Test with 3+ devices to verify mesh topology

## 🤝 **Contributing**

We welcome contributions! Please follow these steps:

1. **Fork the repository**
2. **Create a feature branch** (`git checkout -b feature/amazing-feature`)
3. **Commit your changes** (`git commit -m 'Add amazing feature'`)
4. **Push to the branch** (`git push origin feature/amazing-feature`)
5. **Open a Pull Request**

### **Code Style**
- Follow Java coding conventions
- Use meaningful variable and function names
- Add comments for complex BLE operations
- Write unit tests for new features

## 📄 **License**

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🆘 **Emergency Use Disclaimer**

**IMPORTANT**: This application is designed to assist in emergency communication but should not be relied upon as the sole means of emergency communication. Always follow official emergency procedures and contact professional emergency services when available.

## 🙏 **Acknowledgments**

- **Bluetooth SIG** for Bluetooth Low Energy specifications
- **Android BLE APIs** for providing robust BLE functionality
- **Material Design** for UI/UX guidelines
- **Open source community** for mesh networking concepts and implementations

## 📞 **Support**

- **Issues**: [GitHub Issues](https://github.com/yourusername/Mesh-Network/issues)
- **Discussions**: [GitHub Discussions](https://github.com/yourusername/Mesh-Network/discussions)
- **Email**: your.email@example.com

## 🔄 **Changelog**

### **v1.0.0** (Current)
- ✅ BLE mesh networking implementation
- ✅ User and Admin interface system
- ✅ SOS emergency functionality
- ✅ Location services integration
- ✅ Real-time messaging system
- ✅ Device discovery and connection management

---

**Built with ❤️ for reliable, offline communication and emergency preparedness**
