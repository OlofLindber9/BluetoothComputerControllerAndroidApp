# Bluetooth Mouse Controller

## Description
This project enables a user to control the mouse on a Windows desktop using an Android device via Bluetooth. The application consists of two parts: an Android app and a Java-based Windows service.

## Features
- Control mouse movements and MOUSE1 from an Android device.
- Connect to a Windows computer via Bluetooth.

## Prerequisites
### For the Android App:
- Android Studio
- Android device

### For the Windows Service:
- Java Development Kit (JDK)
- Bluetooth enabled on the Windows device

## Installation
### Android App:
1. Clone the repository to your local machine.
2. Open the project in Android Studio.
3. Either connect your Android device to your computer and enable USB debugging or enable wireless debugging and connect via WiFi.
4. Run the project in Android Studio, and the app should install on your connected device.

### Windows Service:
1. Clone the repository to your local machine.
2. Navigate to the directory containing the Windows service code.
3. Compile the Java code. You can use the following command:
   ```shell
   javac BluetoothServer.java

##Usage
###Setting up the Bluetooth Connection:
1. Pair your Android device with your Windows computer via Bluetooth.
2. Run the JAR file ```ComputerControllerDesktopApp.jar ``` in commandline, ensure Bluetooth is enabled on both devices.
3. Open the app on your Android device. If everything is working the commandline should print out ```Client Connected...```
##Using the App:
1. Use the touchpad area on the app to control the mouse cursor on the Windows screen.
2. The 'Click' button can be used to simulate mouse clicks.
