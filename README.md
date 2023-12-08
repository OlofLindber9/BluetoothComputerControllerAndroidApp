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
2. The directory ```ComputerControllerDesktopApp``` has to do with the Windows service and can be deleted. Preferably put it aside for the installation of the Windows Service.  
3. Open the project in Android Studio.
4. On line 40 in ```MainActivity.kt``` the String ```"00:AA:11:BB:22:CC"``` has to be replaced with your Windows computer's Bluetooth MAC address. This address can be retrieved by entering ```ipconfig /all``` in the command line. Under your Bluetooth adapter, the address should be listed next to the Physical Address. If your address is formatted something like this ```"00-AA-11-BB-22-CC"``` it can easily be converted to the correct format by just changing hyphens to colons.
5. On line 84 in ```BluetoothService``` the String ```"00000000-AAAA-1111-BBBB-222222222222"``` has to be replaced with your unique UUID (Universally unique identifier) for the pairing of your desktop and Andriod device. This UUID can easily be generated with an [UUID Generator Tool](https://www.uuidgenerator.net/).
6. Either connect your Android device to your computer and enable USB debugging or enable wireless debugging and connect via WiFi.
7. Run the project in Android Studio, and the app should install on your connected device.

### Windows Service:
1. Clone the repository to your local machine.
2. Navigate to the directory ```ComputerControllerDesktopApp``.
3. Open this directory in the IDE/code editor of your choice.
4. On line 15 in ```BluetoothServer.java``` the String ```"00000000-AAAA-1111-BBBB-222222222222"``` has to be replaced with your unique UUID.
5. Compile and run the Java code. The program should log ``` "Server Started. Waiting for clients to connect..." ```.
   
##Usage
###Setting up the Bluetooth Connection:
1. Pair your Android device with your Windows computer via Bluetooth.
2. Run the Java code on you desktop, ensure Bluetooth is enabled on both devices.
3. Open the app on your Android device. If everything is working the program on you desktop should log ```Client Connected...```.
##Using the App:
1. Use the touchpad area on the app to control the mouse cursor on the Windows screen.
2. The 'Click' button can be used to simulate mouse clicks.
