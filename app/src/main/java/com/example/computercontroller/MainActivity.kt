package com.example.computercontroller
import android.annotation.SuppressLint
import android.bluetooth.BluetoothSocket
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.Manifest
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.io.IOException
import java.io.OutputStream
import java.util.*

class MainActivity : AppCompatActivity(), BluetoothService.BluetoothConnectionListener {

    private var outputStream: OutputStream? = null
    private var lastX: Float = 0.0f
    private var lastY: Float = 0.0f
    private lateinit var touchPad: View
    private lateinit var pressButton: Button
    private val REQUEST_BLUETOOTH_PERMISSIONS = 1
    private lateinit var bluetoothService: BluetoothService

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        checkBluetoothPermissions()

        val bluetoothService = BluetoothService(this)
        bluetoothService.enableBluetooth()
        bluetoothService.listener = this


        val deviceAddress = "00:AA:11:BB:22:CC" // Replace with your Windows computer's Bluetooth MAC address
        bluetoothService.connectToDevice(deviceAddress)

        // Initialize android widgets
        touchPad = findViewById(R.id.touchPad)
        pressButton = findViewById(R.id.mousePressButton)

        touchPad.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    // User started touching the screen - we could use this to simulate mouse down (click and hold)
                    lastX = event.x
                    lastY = event.y
                    Log.d("Finger DOWN", "Debug message")
                }
                MotionEvent.ACTION_MOVE -> {
                    // User is dragging the finger on the screen
                    val dx = event.x - lastX
                    val dy = event.y - lastY
                    lastX = event.x
                    lastY = event.y

                    // Send the dx, dy values to the connected device
                    sendMouseMovement(dx, dy)
                    Log.d("Finger Touched", "Debug message")
                }
                MotionEvent.ACTION_UP -> {
                    // User lifted the finger - we could use this to simulate mouse up (release click)
                    Log.d("Finger UP", "Debug message")
                }
            }
            true
        }
        pressButton.setOnClickListener {
            val pressCommand = "PRESS \n"
            try{
                outputStream?.write(pressCommand.toByteArray())
                outputStream?.flush()
            } catch (e: IOException){
                e.printStackTrace()
            }
        }
    }

    override fun onBluetoothConnected(socket: BluetoothSocket) {
        setBluetoothSocket(socket)
    }

    // Call this method with the connected BluetoothSocket after successful connection
    private fun setBluetoothSocket(socket: BluetoothSocket) {
        outputStream = socket.outputStream
    }

    private fun sendMouseMovement(dx: Float, dy: Float) {
        val moveCommand = "MOVE $dx $dy\n"
        try {
            outputStream?.write(moveCommand.toByteArray())
            outputStream?.flush()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private val REQUEST_BLUETOOTH_SCAN = 101

    private fun checkBluetoothPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val requiredPermissions = mutableListOf<String>()

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
                requiredPermissions.add(Manifest.permission.BLUETOOTH_SCAN)
            }

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                requiredPermissions.add(Manifest.permission.BLUETOOTH_CONNECT)
            }

            if (requiredPermissions.isNotEmpty()) {
                ActivityCompat.requestPermissions(this, requiredPermissions.toTypedArray(), REQUEST_BLUETOOTH_PERMISSIONS)
            } else {
                // Permissions are already granted, you can proceed with your Bluetooth operations
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == REQUEST_BLUETOOTH_PERMISSIONS) {
            if (grantResults.isNotEmpty() && grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
                // All required permissions were granted
            } else {
            }
        }
    }


}
