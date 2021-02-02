package com.greenland.activity.mainButtons.sync;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class Sender extends IntentService {

    private File[] files;
    private String IP;
    private int PORT;

    public Sender() {
        super("Sending Surveys");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        String[] filesNames = intent.getExtras().getStringArray("filesName");
        files = new File[filesNames.length];
        for (int i = 0; i < files.length; i++){
            files[i] = new File(getApplicationContext().getFilesDir(), filesNames[i]);
        }

        IP = intent.getExtras().getString("ip");
        PORT = ((Integer)intent.getExtras().getInt("port")).intValue();

        Socket clientSocket = null;
        OutputStream os = null;

        try {

            clientSocket = new Socket(InetAddress.getByName(IP), PORT);
            os = clientSocket.getOutputStream();

            InputStream is = clientSocket.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);

            byte[] buffer = new byte[4096];

            for(File file : files) {
                FileInputStream fis = new FileInputStream(file);
                BufferedInputStream bis = new BufferedInputStream(fis);
                // long BytesToSend = fileToSend.length();

                while (true) {

                    int bytesRead = bis.read(buffer, 0, buffer.length);

                    if (bytesRead == -1) {
                        break;
                    }

                    //BytesToSend = BytesToSend - bytesRead;
                    os.write(buffer, 0, bytesRead);
                    os.flush();
                }
                file.delete();
            }
            br.close();
            isr.close();
            is.close();
            os.close();
            clientSocket.close();

            Toast.makeText(getApplicationContext(), "File trasnfered", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Toast.makeText(getApplicationContext(), "File not trasnfered", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDestroy() {
        stopSelf();
    }
}
