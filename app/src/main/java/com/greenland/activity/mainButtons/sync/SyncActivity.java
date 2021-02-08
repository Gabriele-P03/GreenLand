package com.greenland.activity.mainButtons.sync;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.greenland.MainActivity;
import com.greenland.R;
import com.greenland.databinding.SyncLayoutBinding;
import com.greenland.utils.Files;
import com.greenland.utils.STRINGS;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Random;

public class SyncActivity extends AppCompatActivity {

    private File folder;
    private static File[] files;

    private TextView countTV;
    private ListView listFiles;
    private Button syncButton;

    private Button newFile;

    SyncLayoutBinding syncLayoutBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        syncLayoutBinding = DataBindingUtil.setContentView(this, R.layout.sync_layout);
        syncLayoutBinding.setSettings(MainActivity.loadSettings);
        loadComponents();
    }

    private void loadComponents() {
        this.countTV = findViewById(R.id.countFilesTV);
        this.syncButton = findViewById(R.id.syncFilesButton);
        this.syncButton.setOnClickListener(v -> sync());

        this.syncButton = findViewById(R.id.newFileButton);
        this.syncButton.setOnClickListener(v -> newFile());


        this.listFiles = findViewById(R.id.listFilesToSync);
        folder = Files.getDir(getApplicationContext(), STRINGS.SURVEYS.getString());
        this.setList();
    }

    //DELETE. USED FOR DEBUG
    private void newFile() {
        String name = String.valueOf(new Random().nextInt(31)+1);
        name += "-"+String.valueOf(new Random().nextInt(12)+1);
        name += "-2021.txt";

        File file = new File(folder, name);
        try {
            file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file);
            String tmp = String.valueOf(new Random().nextInt(100));
            tmp += "\n"+String.valueOf(new Random().nextInt(100));
            tmp += "\n"+String.valueOf(new Random().nextInt(100));
            fos.write(tmp.getBytes());
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setList(){
        this.loadFiles();
        final ArrayList<String> arrayList = new ArrayList<>();
        for (File file : this.files) {
            arrayList.add(file.getName());
        }

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, getListViewLayout(), arrayList);
        this.listFiles.setAdapter(arrayAdapter);

        this.countTV.setText(this.files.length + " Surveys To Sync");
    }

    private int getListViewLayout() {
        return !MainActivity.loadSettings.isDarkMode() ? R.layout.listview_blacktext_layout : R.layout.listview_whitetext_layout;
    }

    public static String msg;
    private void sync() {
        Toast.makeText(getApplicationContext(), MainActivity.loadSettings.getClientIP() + ":" + MainActivity.loadSettings.getServerPORT(), Toast.LENGTH_SHORT).show();
        Sender sender = new Sender();
        sender.execute(new String[]{"Scialla"});
    }

    private void loadFiles(){
        files = new File[folder.listFiles().length];
        for(int i = 0; i < folder.listFiles().length; i++){
            files[i] = folder.listFiles()[i];
        }
    }

    public static File[] getFiles() {
        return files;
    }
}
