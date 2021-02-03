package com.greenland.activity.mainButtons.sync;

import android.os.AsyncTask;
import android.util.Log;

import com.greenland.MainActivity;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
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
        BufferedWriter bufferedWriter;
        BufferedReader bufferedReader;
        try {
            Socket socket = new Socket(InetAddress.getByName(MainActivity.loadSettings.getClientIP()), MainActivity.loadSettings.getServerPORT());
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

            File[] files = SyncActivity.getFiles();
            String buffer;

            for (File file : files) {
                bufferedReader = new BufferedReader(new FileReader(file));
                bufferedWriter.write(file.getName().replace(".txt", "") + "\n");
                while((buffer = bufferedReader.readLine()) != null){
                    bufferedWriter.write(buffer + "\n");
                }
                bufferedWriter.write("\n");
                file.delete();
            }

            bufferedWriter.flush();
            bufferedWriter.close();
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
