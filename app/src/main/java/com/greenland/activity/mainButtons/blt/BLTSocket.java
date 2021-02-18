package com.greenland.activity.mainButtons.blt;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.greenland.MainActivity;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.UUID;

/**
 * This class runs a new async task for the bluetooth radio
 *
 * @author GABRIELE-P03
 */
public class BLTSocket extends Service {

    private static final UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private static boolean isBtConnected = false;
    private static BluetoothAdapter adapter = null;
    private static BluetoothSocket socket = null;
    private static InputStream is = null;
    private static OutputStream os = null;

    public BLTSocket(){

    }

    /**
     * Constructor called when a bluetooth device has been clicked in the layout
     * If the phone's bluetooth socket has not already connected , it tries to initialize
     * the new connection with the one clicked.
     * Get the stream and set @isBtConnected as true
     *
     * @param address - MAC address of the bluetooth device
     */
    public BLTSocket(String address){
        if (!isBtConnected || !socket.isConnected()) {
            try {
                adapter = BluetoothAdapter.getDefaultAdapter();
                BluetoothDevice device = adapter.getRemoteDevice(address);
                socket = device.createInsecureRfcommSocketToServiceRecord(uuid);
                socket.connect();
                is = socket.getInputStream();
                os = socket.getOutputStream();
                isBtConnected = true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onCreate() {
        HandlerThread handlerThread = new HandlerThread("SURVEYS_SERVICE", Process.THREAD_PRIORITY_BACKGROUND);
        handlerThread.start();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        survey();
        return START_STICKY;
    }

    /**
     * This handler keeps updating the three surveys reading their current value
     * from the bluetooth module's input stream.
     * Set the the circular progress bar and the own text view below as the relative value
     */
    Handler handler = new Handler();
    Runnable runnable;
    private void survey() {

        runnable = new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void run() {
                byte[] survey = new byte[12];
                try {
                    /*
                        Once got the current values of the three surveys,
                        it sets the relative progress bar and the text view
                     */
                    if(is.read(survey) > 0){
                        for(int i = 0; i < 3; i++){
                            MainActivity.getProgressBars()[i].setProgress(survey[i]);
                            MainActivity.getTextViews()[i].setText(String.valueOf(survey[i]) + (i == 0 ? "°C" : '%'));
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    return;
                }
                handler.postDelayed(this, 2000);
            }
        };
        runnable.run();

    }

    /**
     * Called when the user click on the UPDATE SEED button
     * on the popup menu of the ViewSeed layout
     *
     * Send to the HC-05 the new recommended values
     *
     * @param values
     */
    public void updateSeed(int[] values){
        try {
            handler.removeCallbacks(runnable);
            for(int value : values) {
                os.write(value);
            }
            os.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            handler.postDelayed(runnable, 2000);
        }
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /**
     * Close the socket and set @isBtConnected as false when the app is closed or crashes
     */
    @Override
    public void onDestroy() {
        try {
            this.socket.close();
            this.isBtConnected = false;
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

}