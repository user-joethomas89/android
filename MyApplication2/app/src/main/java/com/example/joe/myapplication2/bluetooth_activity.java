package com.example.joe.myapplication2;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ToggleButton;
import java.io.IOException;
import java.util.UUID;

public class bluetooth_activity extends AppCompatActivity {

    private BluetoothAdapter bluetoothAdapter;
    private ToggleButton toggleButton;
    private ListView listview;
    private ArrayAdapter adapter;
    private static final int ENABLE_BT_REQUEST_CODE = 1;
    private static final int DISCOVERABLE_BT_REQUEST_CODE = 2;
    private static final int DISCOVERABLE_DURATION = 300;

    private final static UUID uuid = UUID.fromString("4516e827-023d-497b-bf18-05335a239387");
    // Create a BroadcastReceiver for ACTION_FOUND
    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            // Whenever a remote Bluetooth device is found
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Get the BluetoothDevice object from the Intent
                BluetoothDevice bluetoothDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                // Add the name and address to an array adapter to show in a ListView
                adapter.add(bluetoothDevice.getName() + "\n"
                        + bluetoothDevice.getAddress());
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth_activity);

        toggleButton = (ToggleButton) findViewById(R.id.toggleButton);

        listview = (ListView) findViewById(R.id.listView);
        // ListView Item Click Listener
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // ListView Clicked item value
                String  itemValue = (String) listview.getItemAtPosition(position);

                String MAC = itemValue.substring(itemValue.length() - 17);

                BluetoothDevice bluetoothDevice = bluetoothAdapter.getRemoteDevice(MAC);

                // Initiate a connection request in a separate thread
                ConnectingThread t = new ConnectingThread(bluetoothDevice);
                t.start();


            }
        });

        adapter = new ArrayAdapter
                (this,android.R.layout.simple_list_item_1);
        listview.setAdapter(adapter);

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        bluetoothAdapter.setName("Jiobit");
    }


    public void onToggleClicked(View view) {

        adapter.clear();

        ToggleButton toggleButton = (ToggleButton) view;

        if (bluetoothAdapter == null) {
            // Device does not support Bluetooth
            Toast.makeText(getApplicationContext(), "Your device does not support Bluetooth",
                    Toast.LENGTH_LONG).show();
            toggleButton.setChecked(false);
        }
        else {

            if (toggleButton.isChecked()){ // to turn on bluetooth
                if (!bluetoothAdapter.isEnabled()) {
                    // A dialog will appear requesting user permission to enable Bluetooth
                    Intent enableBluetoothIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enableBluetoothIntent, ENABLE_BT_REQUEST_CODE);
                }
                else {
                    Toast.makeText(getApplicationContext(), "Your device has already been enabled." +
                                    "\n" + "Scanning for remote Bluetooth devices...",
                            Toast.LENGTH_LONG).show();
                    // To discover remote Bluetooth devices
                    discoverDevices();
                    // Make local device discoverable by other devices
                    makeDiscoverable();
                }
            } else { // Turn off bluetooth

                bluetoothAdapter.disable();
                adapter.clear();
                Toast.makeText(getApplicationContext(), "Your device is now disabled.",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == ENABLE_BT_REQUEST_CODE) {

            // Bluetooth successfully enabled!
            if (resultCode == Activity.RESULT_OK) {
                Toast.makeText(getApplicationContext(), "Bluetooth is now enabled." +
                                "\n" + "Scanning for remote Bluetooth devices...",
                        Toast.LENGTH_SHORT).show();

                // To discover remote Bluetooth devices
                discoverDevices();

                // Make local device discoverable by other devices
                makeDiscoverable();

                // Start a thread to create a  server socket to listen
                // for connection request
                ListeningThread t = new ListeningThread();
                t.start();

            } else { // RESULT_CANCELED as user refused or failed to enable Bluetooth
                Toast.makeText(getApplicationContext(), "Bluetooth is not enabled.",
                        Toast.LENGTH_SHORT).show();

                // Turn off togglebutton
                toggleButton.setChecked(false);
            }
        } else if (requestCode == DISCOVERABLE_BT_REQUEST_CODE){

            if (resultCode == DISCOVERABLE_DURATION){
                Toast.makeText(getApplicationContext(), "Your device is now discoverable by other devices for " +
                                DISCOVERABLE_DURATION + " seconds",
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "Fail to enable discoverability on your device.",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    protected void discoverDevices(){
        // To scan for remote Bluetooth devices
        if (bluetoothAdapter.startDiscovery()) {
            Toast.makeText(getApplicationContext(), "Discovering other bluetooth devices...",
                    Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getApplicationContext(), "Discovery failed to start.",
                    Toast.LENGTH_LONG).show();
        }
    }

    protected void makeDiscoverable(){
        // Make local device discoverable
        Intent discoverableIntent = new
                Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, DISCOVERABLE_DURATION);
        startActivityForResult(discoverableIntent, DISCOVERABLE_BT_REQUEST_CODE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Register the BroadcastReceiver for ACTION_FOUND
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        this.registerReceiver(broadcastReceiver, filter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        this.unregisterReceiver(broadcastReceiver);
        adapter.clear();
    }



private class ListeningThread extends Thread {
    private final BluetoothServerSocket bluetoothServerSocket;

    public ListeningThread() {
        BluetoothServerSocket temp = null;
        try {
            temp = bluetoothAdapter.listenUsingRfcommWithServiceRecord(getString(R.string.app_name), uuid);

        } catch (IOException e) {
            e.printStackTrace();
        }
        bluetoothServerSocket = temp;
    }

    public void run() {
        BluetoothSocket bluetoothSocket;
        // This will block while listening until a BluetoothSocket is returned
        // or an exception occurs
        while (true) {
            try {
                bluetoothSocket = bluetoothServerSocket.accept();
            } catch (IOException e) {
                break;
            }
            // If a connection is accepted
            if (bluetoothSocket != null) {

                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(getApplicationContext(), "A connection has been accepted.",
                                Toast.LENGTH_SHORT).show();
                    }
                });

                // Code to manage the connection in a separate thread

                try {
                    bluetoothServerSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }
    // Cancel the listening socket and terminate the thread
    public void cancel() {
        try {
            bluetoothServerSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

private class ConnectingThread extends Thread {
    private final BluetoothSocket bluetoothSocket;
    private final BluetoothDevice bluetoothDevice;

    public ConnectingThread(BluetoothDevice device) {

        BluetoothSocket temp = null;
        bluetoothDevice = device;

        // Get a BluetoothSocket to connect with the given BluetoothDevice
        try {
            temp = bluetoothDevice.createRfcommSocketToServiceRecord(uuid);
        } catch (IOException e) {
            e.printStackTrace();
        }
        bluetoothSocket = temp;
    }
    public void run() {
        // Cancel discovery as it will slow down the connection
        bluetoothAdapter.cancelDiscovery();

        try {
            // This will block until it succeeds in connecting to the device
            // through the bluetoothSocket or throws an exception
            bluetoothSocket.connect();
        } catch (IOException connectException) {
            connectException.printStackTrace();
            try {
                bluetoothSocket.close();
            } catch (IOException closeException) {
                closeException.printStackTrace();
            }
        }

        // Code to manage the connection in a separate thread

    }

    // Cancel an open connection and terminate the thread
    public void cancel() {
        try {
            bluetoothSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
}
