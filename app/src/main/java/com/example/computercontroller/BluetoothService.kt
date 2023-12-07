package com.example.computercontroller
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.Intent
import android.util.Log
import java.io.IOException
import java.util.*
import android.app.Activity
import android.content.Context
import java.io.OutputStream

class BluetoothService(private val activity: Activity) {

    private val bluetoothAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()
    private lateinit var device: BluetoothDevice
    private var connectThread: ConnectThread? = null
    var listener: BluetoothConnectionListener? = null


    interface BluetoothConnectionListener {
        fun onBluetoothConnected(socket: BluetoothSocket)
    }

    fun enableBluetooth() {
        if (bluetoothAdapter == null) {
            // Device doesn't support Bluetooth
        } else if (!bluetoothAdapter.isEnabled) {
            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            activity.startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT)
        }
    }

    fun connectToDevice(deviceAddress: String) {
        device = bluetoothAdapter?.getRemoteDevice(deviceAddress)!!
        connectThread = ConnectThread(device)
        connectThread?.run()
    }

    fun getOutputStream(): OutputStream? {
        return connectThread?.mmSocket?.outputStream
    }

    private inner class ConnectThread(device: BluetoothDevice) : Thread() {
        val mmSocket: BluetoothSocket? by lazy(LazyThreadSafetyMode.NONE) {
            device.createRfcommSocketToServiceRecord(MY_UUID)
        }

        override fun run() {
            // Cancel discovery because it otherwise slows down the connection.
            bluetoothAdapter?.cancelDiscovery()

            mmSocket?.let { socket ->
                // Connect to the remote device through the socket. This call blocks
                // until it succeeds or throws an exception.
                socket.connect()

                // The connection attempt succeeded. Perform work associated with
                // the connection in a separate thread.
                manageMyConnectedSocket(socket)
            }
        }

        // Closes the client socket and causes the thread to finish.
        fun cancel() {
            try {
                mmSocket?.close()
            } catch (e: IOException) {
                Log.e(TAG, "Could not close the client socket", e)
            }
        }
    }

    private fun manageMyConnectedSocket(socket: BluetoothSocket) {
        // Pass the connected socket to a separate thread responsible for communication
        // Implement your data transmission logic here

        listener?.onBluetoothConnected(socket)
    }

    companion object {
        private const val TAG = "BluetoothService"
        private const val REQUEST_ENABLE_BT = 1
        private val MY_UUID: UUID = UUID.fromString("cdc3c10c-058a-4e50-a724-cb9ccb53f385") // Replace with your own UUID
    }
}

//cdc3c10c-058a-4e50-a724-cb9ccb53f385
