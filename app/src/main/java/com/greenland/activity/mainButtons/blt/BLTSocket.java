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
 * This class run a new async task for the bluetooth radio
 */
public class BLTSocket extends Service {

    private static final UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private static boolean isBtConnected = false;
    private static BluetoothAdapter adapter = null;
    private static BluetoothSocket socket = null;
    private static InputStream is = null;

    public BLTSocket(){

    }

    public BLTSocket(String address){
        if (!isBtConnected || !socket.isConnected()) {
            try {
                adapter = BluetoothAdapter.getDefaultAdapter();
                BluetoothDevice device = adapter.getRemoteDevice(address);
                socket = device.createInsecureRfcommSocketToServiceRecord(uuid);
                socket.connect();
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

    Handler handler = new Handler();
    Runnable runnable;
    private void survey() {

        runnable = new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void run() {
                byte[] survey = new byte[12];
                try {
                    is = socket.getInputStream();
                    if(is.read(survey) > 0);
                        setProgressTV(survey);
                } catch (IOException e) {
                    e.printStackTrace();

                    return;
                }
                handler.postDelayed(this, 2000);
            }
        };
        runnable.run();

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setProgressTV(byte[] survey) {

        MainActivity.getProgressBars()[0].setProgress(survey[0]);
        MainActivity.getTextViews()[0].setText(String.valueOf(survey[0]) + "°C");
        MainActivity.getProgressBars()[1].setProgress(survey[1]);
        MainActivity.getTextViews()[1].setText(String.valueOf(survey[1]) + "%");
//        MainActivity.getProgressBars()[2].setProgress(Byte.toUnsignedInt(survey[2]));
//        MainActivity.getTextViews()[2].setText(Byte.toString(survey[2]));
    }


    public void updateSeed(int[] values){
        OutputStream stream = null;
        try {
            handler.removeCallbacks(runnable);
            stream = socket.getOutputStream();
            for(int value : values) {
                stream.write(value);
            }
            stream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            handler.postDelayed(runnable, 5000);
        }
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        try {
            this.socket.close();
            this.isBtConnected = false;
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

/*    private final class ServiceHandler extends Handler{

        public ServiceHandler(Looper looper){
            super(looper);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            survey();
        }
    }*/
}
