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

/**
 * Activity started when the user clicks on the Bluetooth button in the main layout
 * Once activated own bluetooth radio, it searches the already compared device which are available
 *
 * When the user clicks on one, the application instances a new BLTSocket to set the connection,
 * passing to the constructor the MAC address of the bluetooth device clicked
 *
 * @see BLTSocket
 */
public class BluetoothActivity extends AppCompatActivity {

    ActivityBluetoothBinding activityBluetoothBinding;

    private BluetoothAdapter adapter;
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
        //If the adapter is null, the user's phone hasn't a bluetooth radio
        if(adapter != null){
            if(!adapter.isEnabled()){
                //If the bluetooth is disabled, it asks to the user to enable it
                startActivityForResult(new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE), 1);
            }

            //The bonded devices are those ones which have already been compared
            pairedDevices = adapter.getBondedDevices();
            this.bltTV.setText(pairedDevices.size() + " Devices Found");

            if(pairedDevices.size() > 0){

                /*
                 *  Create a new list of strings which every one contains the name of one compared
                 *  bluetooth radio. The array list is given to the adapter.
                 */
                final ArrayList<String> arrayList = new ArrayList<>();
                for (BluetoothDevice device : pairedDevices){
                    arrayList.add(device.getName());
                }
                final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, getListViewLayout(), arrayList);
                this.listBLTDevice.setAdapter(adapter);

                /*
                    When an item (string that represents a bluetooth radio) has been clicked,
                    its MAC address is given to the BLTSocket constructor, which will set
                    the opportune variable, objects and connects our radio with the selected one
                 */
                this.listBLTDevice.setOnItemClickListener( (parent, v, pos, id) -> {
                    deviceSelected = (BluetoothDevice) pairedDevices.toArray()[pos];
                    Toast.makeText(getApplicationContext(), "Device Selected: " + deviceSelected.getName() + "\n" + deviceSelected.getAddress(), Toast.LENGTH_SHORT).show();
                    bltSocket = new BLTSocket(deviceSelected.getAddress());
                    startService(new Intent(this, BLTSocket.class));
                } );
            }
        }else{
            Toast.makeText(getApplicationContext(), "Seems like your device hasn't any bluetooth adapter...", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Called when setting the ArrayAdapter to chose list layout with the right color of the text
     * If the user has the dark mode enabled, this method returns the white list and vice versa
     *
     * @return the id of the opportune listview layout
     */
    private int getListViewLayout() {
        return !MainActivity.loadSettings.isDarkMode() ? R.layout.listview_blacktext_layout : R.layout.listview_whitetext_layout;
    }
}