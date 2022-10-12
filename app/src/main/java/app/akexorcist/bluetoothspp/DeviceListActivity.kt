/*
 * Copyright 2014 Akexorcist
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package app.akexorcist.bluetoothspp

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import app.akexorcist.bluetotohspp.library.BluetoothSPP
import android.os.Bundle
import app.akexorcist.bluetoothspp.R
import android.widget.Toast
import app.akexorcist.bluetotohspp.library.BluetoothSPP.OnDataReceivedListener
import app.akexorcist.bluetotohspp.library.BluetoothState
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import app.akexorcist.bluetotohspp.library.DeviceList

class DeviceListActivity() : Activity() {
    var bt: BluetoothSPP? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_devicelist)
        bt = BluetoothSPP(this)
        if (!bt!!.isBluetoothAvailable) {
            Toast.makeText(
                applicationContext, "Bluetooth is not available", Toast.LENGTH_SHORT
            ).show()
            finish()
        }
        bt!!.setOnDataReceivedListener(OnDataReceivedListener { data, message ->
            Log.i("Check", "Length : " + data.size)
            Log.i("Check", "Message : $message")
        })
        val btnConnect = findViewById<View>(R.id.btnConnect) as Button
        btnConnect.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {
                if (bt!!.serviceState == BluetoothState.STATE_CONNECTED) {
                    bt!!.disconnect()
                } else {
                    val intent = Intent(this@DeviceListActivity, DeviceList::class.java)
                    intent.putExtra("bluetooth_devices", "Bluetooth devices")
                    intent.putExtra("no_devices_found", "No devices found")
                    intent.putExtra("scanning", "Scanning")
                    intent.putExtra("scan_for_devices", "Search")
                    intent.putExtra("select_device", "Select")
                    intent.putExtra("layout_list", R.layout.device_layout_list)
                    intent.putExtra("layout_text", R.layout.device_layout_text)
                    startActivityForResult(intent, BluetoothState.REQUEST_CONNECT_DEVICE)
                }
            }
        })
    }

    public override fun onDestroy() {
        super.onDestroy()
        bt!!.stopService()
    }

    public override fun onStart() {
        super.onStart()
        if (!bt!!.isBluetoothEnabled) {
            bt!!.enable()
        } else {
            if (!bt!!.isServiceAvailable) {
                bt!!.setupService()
                bt!!.startService(BluetoothState.DEVICE_ANDROID)
                setup()
            }
        }
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        if (requestCode == BluetoothState.REQUEST_CONNECT_DEVICE) {
            if (resultCode == RESULT_OK) bt!!.connect(data)
        } else if (requestCode == BluetoothState.REQUEST_ENABLE_BT) {
            if (resultCode == RESULT_OK) {
                bt!!.setupService()
            } else {
                Toast.makeText(
                    applicationContext, "Bluetooth was not enabled.", Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
    }

    fun setup() {
        val btnSend = findViewById<View>(R.id.btnSend) as Button
        btnSend.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {
                bt!!.send("Text", true)
            }
        })
    }

}