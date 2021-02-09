package com.greenland.activity.mainButtons.blt;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.greenland.MainActivity;
import com.greenland.R;
import com.greenland.databinding.ActivityBluetoothBinding;
import com.greenland.utils.Seed;

import java.util.ArrayList;
import java.util.Set;
import java.util.concurrent.ExecutionException;

public class BluetoothActivity extends AppCompatActivity {

    ActivityBluetoothBinding activityBluetoothBinding;

    private BluetoothAdapter adapter;
    private BluetoothSocket socket;
    private Set<BluetoothDevice> pairedDevices;
    private ListView listBLTDevice;
    private TextView bltTV;

    private BluetoothDevice deviceSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityBluetoothBinding = DataBindingUtil.setContentView(this, R.layout.activity_bluetooth);
        activityBluetoothBinding.setSettings(MainActivity.loadSettings);
        this.bltTV = findViewById(R.id.bltTV);
        this.listBLTDevice = findViewById(R.id.listBLTDevices);
        setBlt();
    }

    public static BLTSocket bltSocket;

    private void setBlt(){
        this.adapter = BluetoothAdapter.getDefaultAdapter();
        if(adapter != null){
            if(!adapter.isEnabled()){
                startActivityForResult(new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE), 1);
            }
            pairedDevices = adapter.getBondedDevices();
            if(pairedDevices.size() > 0){
                final ArrayList<String> arrayList = new ArrayList<>();
                for (BluetoothDevice device : pairedDevices){
                    arrayList.add(device.getName());
                }
                final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, getListViewLayout(), arrayList);
                this.listBLTDevice.setAdapter(adapter);

                this.listBLTDevice.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        deviceSelected = (BluetoothDevice) pairedDevices.toArray()[position];
                        Toast.makeText(getApplicationContext(), "Device Selected: " + deviceSelected.getName() + "\n" + deviceSelected.getAddress(), Toast.LENGTH_SHORT).show();
                        startBLT();
                    }
                });
            }

            this.bltTV.setText(pairedDevices.size() + " Devices Found");
        }else{
            Toast.makeText(getApplicationContext(), "Seems like your device hasn't any bluetooth adapter...", Toast.LENGTH_SHORT).show();
        }
    }

    private void startBLT(){
        bltSocket = new BLTSocket(deviceSelected.getAddress());
        startService(new Intent(this, BLTSocket.class));
    }

    private int getListViewLayout() {
        return !MainActivity.loadSettings.isDarkMode() ? R.layout.listview_blacktext_layout : R.layout.listview_whitetext_layout;
    }
}