package com.example.joe.myapplication_1;

import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private ArrayAdapter<String> arrayAdapter;
    ListView pDList = (ListView) findViewById(R.id.pDList);

    private static final int REQUEST_ENABLE_BT = 1;
    BluetoothAdapter BA;

    public void toggleBluetooth(View view) {

            if (BA.isEnabled()) {
                BA.disable();
                if (BA.isEnabled()) {
                    Toast.makeText(getApplicationContext(), "Bluetooth could not be turned off!", Toast.LENGTH_LONG).show();

                }
                else {

                    Toast.makeText(getApplicationContext(), "Bluetooth turned off", Toast.LENGTH_LONG).show();
                }
            }
            else{
                Intent i= new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(i, REQUEST_ENABLE_BT);
                //if (!BA.isEnabled()) {
                  //  Toast.makeText(getApplicationContext(), "Bluetooth could not be turned on!", Toast.LENGTH_LONG).show();

                //} else {

                    Toast.makeText(getApplicationContext(), "Bluetooth turned on", Toast.LENGTH_LONG).show();
                }

                }

    public void discoverableDevices(View view){

        Intent i = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        startActivity(i);
    }

    public void pairedDevices(View view) {

        Set<BluetoothDevice> paired = BA.getBondedDevices();
        //ListView pDList = (ListView) findViewById(R.id.pDList);
        ArrayList pDAList = new ArrayList();

        if (paired.size() > 0) {
            // Loop through paired devices
            for (BluetoothDevice device : paired) {

                pDAList.add(device.getName()+ "\n" + device.getAddress());
            }

            arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, pDAList);
            pDList.setAdapter(arrayAdapter);


        }
    }

    public void Scan(View view){

        if (BA.isDiscovering()){
            BA.cancelDiscovery();
        }
        //re-start discovery
        BA.startDiscovery();

      final BroadcastReceiver bReceiver = new BroadcastReceiver() {

        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();

            // When discovery finds a device

            if (BluetoothDevice.ACTION_FOUND.equals(action)) {

                // Get the BluetoothDevice object from the Intent

                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                // add the name and the MAC address of the object to the arrayAdapter

                arrayAdapter.add(device.getName() + "\n" + device.getAddress());

                arrayAdapter.notifyDataSetChanged();
                pDList.setAdapter(arrayAdapter);

            }
        }

        }

    };




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BA = BluetoothAdapter.getDefaultAdapter();
        // Phone does not support Bluetooth so let the user know and exit.
        if (BA == null) {
            new AlertDialog.Builder(this)
                    .setTitle("Not compatible")
                    .setMessage("Your phone does not support Bluetooth")
                    .setPositiveButton("Exit", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            System.exit(0);
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }



    }

}
