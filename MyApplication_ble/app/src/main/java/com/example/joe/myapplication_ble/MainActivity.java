package com.example.joe.myapplication_ble;


import android.annotation.TargetApi;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.le.AdvertiseCallback;
import android.bluetooth.le.AdvertiseData;
import android.bluetooth.le.AdvertiseSettings;
import android.bluetooth.le.BluetoothLeAdvertiser;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.ParcelUuid;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import android.support.v7.app.AppCompatActivity;

@TargetApi(21)
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int DISCOVERABLE_BT_REQUEST_CODE = 2 ;
    private static final int DISCOVERABLE_DURATION = 300 ;
    Set set = new HashSet();
    private BluetoothAdapter mBluetoothAdapter;
    private int REQUEST_ENABLE_BT = 1;
    private Handler mHandler;
    private static final long SCAN_PERIOD = 8000;
    private BluetoothLeScanner mLEScanner;
    private ScanSettings settings;
    private List<ScanFilter> filters;
    private ScanFilter filter;
    private BluetoothGatt mGatt;
    private ListView lv;
    private ArrayAdapter<String> adapter;
    private Button mAdvertiseButton;
    private Button mDiscoverButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDiscoverButton = (Button) findViewById( R.id.discover_btn );
        mAdvertiseButton = (Button) findViewById( R.id.advertise_btn );
        mDiscoverButton.setOnClickListener(this);
        mAdvertiseButton.setOnClickListener(this);
        lv =(ListView) findViewById(R.id.listView);
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1);
        adapter.clear();
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                      @Override
                                      public void onItemClick(AdapterView<?> parent, View view,
                                                              int position, long id) {
                                          String  itemValue = (String) lv.getItemAtPosition(position);
                                          String MAC = itemValue.substring(itemValue.length() - 17);
                                          Log.e("MAC",MAC);
                                          BluetoothDevice device =mBluetoothAdapter.getRemoteDevice(MAC);
                                          connectToDevice(device);

                                      }
                                  });

        mHandler = new Handler();
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, "BLE Not Supported",
                    Toast.LENGTH_SHORT).show();
            finish();
        }
        final BluetoothManager bluetoothManager =
                (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();
        mBluetoothAdapter.setName("BLE!!!");
    }

    @Override
    public void onClick(View v) {
        if( v.getId() == R.id.discover_btn ) {
            discover(true);
        } else if( v.getId() == R.id.advertise_btn ) {
            advertise();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();

        adapter.clear();

        if(mBluetoothAdapter.getScanMode() != BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE){
            Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, DISCOVERABLE_DURATION);
            startActivityForResult(discoverableIntent, DISCOVERABLE_BT_REQUEST_CODE);
        }

       if( !BluetoothAdapter.getDefaultAdapter().isMultipleAdvertisementSupported() ) {
            Toast.makeText( this, "Multiple advertisement not supported", Toast.LENGTH_LONG ).show();
            mAdvertiseButton.setEnabled( false );
        }

    }

    private void advertise(){

        BluetoothLeAdvertiser advertiser = BluetoothAdapter.getDefaultAdapter().getBluetoothLeAdvertiser();

        AdvertiseSettings settings = new AdvertiseSettings.Builder()
                .setAdvertiseMode( AdvertiseSettings.ADVERTISE_MODE_LOW_LATENCY )
                .setTxPowerLevel( AdvertiseSettings.ADVERTISE_TX_POWER_HIGH )
                .setConnectable( false )
                .build();

        ParcelUuid pUuid = new ParcelUuid( UUID.fromString( getString( R.string.ble_uuid ) ) );

        AdvertiseData data = new AdvertiseData.Builder()
                .setIncludeDeviceName( true )
                .addServiceUuid( pUuid )
                .addServiceData( pUuid, "Data".getBytes( Charset.forName( "UTF-8" ) ) )
                .build();

        AdvertiseCallback advertisingCallback = new AdvertiseCallback() {
            @Override
            public void onStartSuccess(AdvertiseSettings settingsInEffect) {
                super.onStartSuccess(settingsInEffect);
            }

            @Override
            public void onStartFailure(int errorCode) {
                Log.e( "BLE", "Advertising onStartFailure: " + errorCode );
                super.onStartFailure(errorCode);
            }
        };

        try {
            advertiser.startAdvertising(settings, data, advertisingCallback);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

            if (requestCode == DISCOVERABLE_BT_REQUEST_CODE){
                if (resultCode == DISCOVERABLE_DURATION){
                    Toast.makeText(getApplicationContext(), "Your device is now discoverable by other devices for " +
                                    DISCOVERABLE_DURATION + " seconds",
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Failed to enable discoverability on your device.",
                            Toast.LENGTH_SHORT).show();
                    finish();

                }
            }

        }

    private void discover(final boolean enable) {
        if (Build.VERSION.SDK_INT >= 21) {
            mLEScanner = mBluetoothAdapter.getBluetoothLeScanner();
            settings = new ScanSettings.Builder()
                    .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
                    .build();
            filters = new ArrayList<ScanFilter>();
        }
        if (enable) {
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (Build.VERSION.SDK_INT < 21) {
                        Log.e("Stop Scan "," Build<21");
                        mBluetoothAdapter.stopLeScan(mLeScanCallback);
                        Toast.makeText(getApplicationContext(),"Scan Stopped!! Click Scan to resume.",Toast.LENGTH_LONG).show();

                    }
                    else {
                        Log.e("Stop Scan "," Build>21");
                        mLEScanner.stopScan(mScanCallback);
                        Toast.makeText(getApplicationContext(),"Scan Stopped!! Click Scan to resume.",Toast.LENGTH_LONG).show();
                    }
                }
            }, SCAN_PERIOD);
            if (Build.VERSION.SDK_INT < 21) {
                Log.e("Start Scan "," Build<21");
                mBluetoothAdapter.startLeScan(mLeScanCallback);
            } else {
                Log.e("Start Scan "," Build>21");
                mLEScanner.startScan(filters,settings,mScanCallback);
            }
        } else {
            if (Build.VERSION.SDK_INT < 21) {
                Log.e("Stop Scan,Enable=false "," Build<21");
                mBluetoothAdapter.stopLeScan(mLeScanCallback);
            } else {
                Log.e("Stop Scan,Enable=false "," Build>21");
                mLEScanner.stopScan(mScanCallback);
            }
        }
    }

    private ScanCallback mScanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            super.onScanResult(callbackType, result);
            Log.e("callbackType", String.valueOf(callbackType));
            Log.e("result ", result.toString());
            if(!set.contains(result.getDevice())){
                if(result.getDevice().getName()==null)
                    adapter.add("Unknown Device"+"\n"+result.getDevice());
                else
                    adapter.add(result.getDevice().getName()+"\n"+result.getDevice());

                adapter.notifyDataSetChanged();
            }
            try{
                set.add(result.getDevice());
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        @Override
        public void onBatchScanResults(List<ScanResult> results) {
            for (ScanResult sr : results) {
                Log.e("ScanResult - Results", sr.toString());
            }
        }

        @Override
        public void onScanFailed(int errorCode) {
            Log.e("Scan Failed", "Error Code: " + errorCode);
        }
    };

    private BluetoothAdapter.LeScanCallback mLeScanCallback =
            new BluetoothAdapter.LeScanCallback() {
                @Override
                public void onLeScan(final BluetoothDevice device, int rssi,
                                     byte[] scanRecord) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.e("onLeScan", device.toString());
                            if(device!=null){
                                adapter.add(device.getName()+"\n"+device.getAddress());
                                adapter.notifyDataSetChanged();

                            }
                            else {
                                adapter.add("Unknown Device"+"\n"+device.getAddress());
                                adapter.notifyDataSetChanged();
                            }
                        }
                    });
                }
            };


    public void connectToDevice(BluetoothDevice device) {
        if (mGatt == null) {
            Log.e("Trying to connect ","...");
            mGatt = device.connectGatt(this, false, gattCallback);
            discover(false);// will stop after first device connects/..

        } else {
            Log.e("Mgatt not null "," Cannot connect!!");
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mBluetoothAdapter != null && mBluetoothAdapter.isEnabled()) {
            discover(false);
        }
    }

    @Override
    protected void onDestroy() {
        if (mGatt != null) {

            mGatt.close();
            mGatt = null;
        }

        super.onDestroy();
    }

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
                    mGatt=null;
                    break;

                default:
                    Log.e("gattCallback", "STATE_OTHER");
            }
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            List<BluetoothGattService> services = gatt.getServices();
            Log.e("onServicesDiscovered", services.toString());
            gatt.readCharacteristic(services.get(1).getCharacteristics().get(0));
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt,BluetoothGattCharacteristic characteristic, int status) {

            Log.e("onCharacteristicRead", characteristic.toString());
            gatt.disconnect();
            gatt.close();

        }
    };

}
