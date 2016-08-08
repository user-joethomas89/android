package com.example.joe.ble_22;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
//import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {


    private final static String TAG = MainActivity.class.getSimpleName();
    private BluetoothAdapter bluetoothAdapter;
    BluetoothManager bluetoothManager;
    boolean hasBleFeature = false;

    int colorId = android.R.color.holo_red_light;
    private boolean mScanning;
    private Handler handler = new Handler();
    private static final long SCAN_PERIOD = 10000;
    private static final int REQUEST_ENABLE_BT = 1209;
    private ListView lv;
    ArrayList<BluetoothDevice> listDevices;
    private ArrayAdapter adapter;
    boolean isConnected = false;
    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lv = (ListView) findViewById(R.id.listView);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                BluetoothDevice device = getDevice(position);
                Toast.makeText(getApplicationContext(),"" + device.getName(), Toast.LENGTH_SHORT).show();
                BluetoothGatt connectGatt = device.connectGatt(getBaseContext(),false,mGattCallback);

            }

        });
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1);
        adapter.clear();
        lv.setAdapter(adapter);
        initParameters();
        scanLeDevice(true);
    }
    @Override
    protected void onResume() {
        super.onResume();
        adapter.clear();
        scanLeDevice(true);
        initParameters();
    }

    @SuppressLint("NewApi")
    void initParameters() {
        hasBleFeature = getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE);
        Log.e(TAG, "hasBleFeature : " + hasBleFeature);

        if (hasBleFeature) {
           // messageId = ;
            colorId = android.R.color.holo_blue_light;
        } else {
            //messageId = R.string.doesnt_support_ble;
            colorId = android.R.color.holo_red_light;
        }
        bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        bluetoothAdapter = bluetoothManager.getAdapter();// BluetoothAdapter.getDefaultAdapter();

        if (bluetoothAdapter == null || !bluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }


        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1);
        lv.setAdapter(adapter);

    }


    @SuppressLint("NewApi")
    void scanLeDevice(final boolean enable) {
        if (enable) {
            handler.postDelayed(new Runnable() {
                @SuppressLint("NewApi")
                @Override
                public void run() {
                    mScanning = false;
                    bluetoothAdapter.stopLeScan(leScanCallback);
                }
            }, SCAN_PERIOD);

            mScanning = false;
            bluetoothAdapter.startLeScan(leScanCallback);
        } else {
            mScanning = false;
            bluetoothAdapter.stopLeScan(leScanCallback);
        }
    }

    @SuppressLint("NewApi")
    private BluetoothAdapter.LeScanCallback leScanCallback = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(final BluetoothDevice device, int rssi, byte[] scanRecord) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (device != null) {
                        adapter.add(device);
                        adapter.notifyDataSetChanged();
                    }
                }
            });
        }
    };

    BluetoothDevice getDevice(int position) {
        return (BluetoothDevice) lv.getAdapter().getItem(position);
    }

    @SuppressLint("NewApi")
    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
        BluetoothDevice device = getDevice(position);
        Toast.makeText(this, "" + device.getName(), Toast.LENGTH_SHORT).show();
        BluetoothGatt connectGatt = device.connectGatt(this, false, mGattCallback);

    }

    private static final UUID CONFIG_DESCRIPTOR = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb");


    private BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {

        /* State Machine Tracking */
        private int mState = 0;


        private final BluetoothGattCallback gattCallback = new BluetoothGattCallback() {
            @Override
            public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
                Log.e("onConnectionStateChange", "Status: " + status);
                switch (newState) {
                    case BluetoothProfile.STATE_CONNECTED:
                        Log.e("gattCallback", "STATE_CONNECTED");
                        gatt.discoverServices();
                        break;
                    case BluetoothProfile.STATE_CONNECTING:
                        Log.e("gattCallback", "STATE_CONNECTING");
                        break;
                    case BluetoothProfile.STATE_DISCONNECTED:
                        Log.e("gattCallback", "STATE_DISCONNECTED");
                        break;
                    default:
                        Log.e("gattCallback", "STATE_OTHER");
                }

            }

            @Override
            public void onServicesDiscovered(BluetoothGatt gatt, int status) {
                List<BluetoothGattService> services = gatt.getServices();
                Log.e("onServicesDiscovered", services.toString());
                gatt.readCharacteristic(services.get(1).getCharacteristics().get
                        (0));
            }

            @Override
            public void onCharacteristicRead(BluetoothGatt gatt,
                                             BluetoothGattCharacteristic
                                                     characteristic, int status) {
                Log.e("onCharacteristicRead", characteristic.toString());
                gatt.disconnect();
            }
        };
    };
}
