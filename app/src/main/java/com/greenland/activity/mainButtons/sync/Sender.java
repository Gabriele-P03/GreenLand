package com.greenland.activity.mainButtons.sync;

import android.os.AsyncTask;
import android.util.Log;

import com.greenland.MainActivity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;


/**
 * This class run a new thread that send the daily average of the surveys as byte
 *
 * @param "strings" is an array of string that contains:
 *               first: address
 *               second: port
 *               third: msg
 * @see SyncActivity
 */
public class Sender extends AsyncTask<String, Void, Void> {


    @Override
    protected Void doInBackground(String... strings) {
        PrintWriter printWriter;
        BufferedReader bufferedReader;
        try {
            Socket socket = new Socket(InetAddress.getByName(MainActivity.loadSettings.getClientIP()), 2020);
            printWriter = new PrintWriter(socket.getOutputStream());

            File[] files = SyncActivity.getFiles();
            String buffer;

            for (File file : files) {
                bufferedReader = new BufferedReader(new FileReader(file));
                while((buffer = bufferedReader.readLine()) != null){
                    printWriter.write(buffer);
                }
                printWriter.write("\n");
            }

            printWriter.flush();
            printWriter.close();
            socket.close();
            //
//            for(File file : files){
//                bufferedReader = new BufferedReader(new FileReader(file));
//                bufferedWriter.write(bufferedReader.readLine());
//            }
        } catch (IOException e) {
            Log.d(Sender.class.getSimpleName(), "refused");
            e.printStackTrace();
        }

        return null;
    }
}
