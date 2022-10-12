package app.akexorcist.bluetoothspp

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import app.akexorcist.bluetoothspp.R
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.view.View
import android.widget.Button
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import app.akexorcist.bluetoothspp.SimpleActivity
import app.akexorcist.bluetoothspp.ListenerActivity
import app.akexorcist.bluetoothspp.AutoConnectActivity
import app.akexorcist.bluetoothspp.DeviceListActivity
import app.akexorcist.bluetoothspp.TerminalActivity

class Main : Activity(), View.OnClickListener {
    private val REQUEST_CODE_PERMISSION = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main)
        val btnSimple = findViewById<View>(R.id.btnSimple) as Button
        btnSimple.setOnClickListener(this)
        val btnListener = findViewById<View>(R.id.btnListener) as Button
        btnListener.setOnClickListener(this)
        val btnAutoConnect = findViewById<View>(R.id.btnAutoConnect) as Button
        btnAutoConnect.setOnClickListener(this)
        val btnDeviceList = findViewById<View>(R.id.btnDeviceList) as Button
        btnDeviceList.setOnClickListener(this)
        val btnTerminal = findViewById<View>(R.id.btnTerminal) as Button
        btnTerminal.setOnClickListener(this)

        checkPermissions()
    }

    override fun onClick(v: View) {
        val id = v.id
        var intent: Intent? = null
        when (id) {
            R.id.btnSimple -> {
                intent = Intent(applicationContext, SimpleActivity::class.java)
                startActivity(intent)
            }
            R.id.btnListener -> {
                intent = Intent(applicationContext, ListenerActivity::class.java)
                startActivity(intent)
            }
            R.id.btnAutoConnect -> {
                intent = Intent(applicationContext, AutoConnectActivity::class.java)
                startActivity(intent)
            }
            R.id.btnDeviceList -> {
                intent = Intent(applicationContext, DeviceListActivity::class.java)
                startActivity(intent)
            }
            R.id.btnTerminal -> {
                intent = Intent(applicationContext, TerminalActivity::class.java)
                startActivity(intent)
            }
        }

    }


    private fun checkPermissions() {
        val permissions = mutableListOf<String>(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.BLUETOOTH,
            Manifest.permission.BLUETOOTH_ADMIN
        )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            permissions.add(Manifest.permission.BLUETOOTH_SCAN)
            permissions.add(Manifest.permission.BLUETOOTH_CONNECT)
        }

        val permissionDeniedList: MutableList<String> = ArrayList()
        for (permission in permissions) {
            val permissionCheck = ContextCompat.checkSelfPermission(this, permission)
            if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
//                onPermissionGranted(permission)
            } else {
                permissionDeniedList.add(permission)
            }
        }

        if (!permissionDeniedList.isEmpty()) {
            val deniedPermissions = permissionDeniedList.toTypedArray()
            ActivityCompat.requestPermissions(
                this,
                deniedPermissions,
                REQUEST_CODE_PERMISSION
            )
        } else {
            onPermissionGranted()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        checkPermissions()
        if (!grantResults.contains(PackageManager.PERMISSION_DENIED)) {
            onPermissionGranted()
        }
    }

    @SuppressLint("MissingPermission")
    fun onPermissionGranted() {

    }
}