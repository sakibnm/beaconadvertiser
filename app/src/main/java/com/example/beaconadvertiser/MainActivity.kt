package com.example.beaconadvertiser

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.bluetooth.le.AdvertiseSettings
import android.bluetooth.le.BluetoothLeScanner
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.uriio.beacons.Beacons
import com.uriio.beacons.model.*
import java.util.*
import android.content.Intent



class MainActivity : AppCompatActivity() {

    lateinit var buttonStart:Button
    lateinit var buttonStop:Button
    lateinit var textView:TextView
    lateinit var textView2:TextView
    lateinit var buttonScan:Button

    public val REQUEST_ENABLE_BT = 0x100

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Beacons.initialize(this)

        buttonStart = findViewById(R.id.buttonStart)
        buttonStop = findViewById(R.id.buttonStop)
        textView = findViewById(R.id.textView)
        textView2 = findViewById(R.id.textView2)
        buttonScan = findViewById(R.id.buttonScan)

        val myUID = UUID.randomUUID()
        val myUID2 = UUID.randomUUID()

        print(myUID.toString().plus(" and ").plus(myUID2.toString()))

        textView.setText(myUID.toString())
        textView2.setText(myUID2.toString())

        var myUiBeacon:EddystoneUID = EddystoneUID(UuidAdapter.getBytesFromUUID(myUID2),"uncc.edu",AdvertiseSettings.ADVERTISE_MODE_BALANCED, AdvertiseSettings.ADVERTISE_TX_POWER_HIGH, "MyGadhaEddyBeacon")

        var myIBeacon:iBeacon = iBeacon(UuidAdapter.getBytesFromUUID(myUID),1,0,
            AdvertiseSettings.ADVERTISE_MODE_BALANCED,AdvertiseSettings.ADVERTISE_TX_POWER_HIGH, "MygadhaIBeacon")

        buttonStart.setOnClickListener(View.OnClickListener {


//            myUiBeacon.edit().setName("My gadha Beacon!!!").setAdvertiseTxPower(AdvertiseSettings.ADVERTISE_TX_POWER_HIGH).apply()

            val toast:Toast = Toast.makeText(this, "Starting beacons!!!",Toast.LENGTH_SHORT)
            toast.show()

            myUiBeacon.start()

            myIBeacon.start()


        })


        buttonStop.setOnClickListener {
            val toast:Toast = Toast.makeText(this, "Stopping beacons!!!",Toast.LENGTH_SHORT)
            toast.show()

            myUiBeacon.stop()
            myIBeacon.stop()
        }


        buttonScan.setOnClickListener {
            packageManager.takeIf { it.missingSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE) }?.also {
                Toast.makeText(this, R.string.ble_not_supported, Toast.LENGTH_SHORT).show()
                finish()
            }
            val bluetoothAdapter: BluetoothAdapter? by lazy(LazyThreadSafetyMode.NONE) {
                val bluetoothManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
                bluetoothManager.adapter
            }

            if(bluetoothAdapter == null || !bluetoothAdapter!!.isEnabled()){
                val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT)
            }

        }

    }
    private fun PackageManager.missingSystemFeature(name: String): Boolean = !hasSystemFeature(name)

}


