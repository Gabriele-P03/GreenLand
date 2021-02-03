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
import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class SyncActivity extends AppCompatActivity {

    private File folder;
    private static File[] files;

    private TextView countTV;
    private ListView listFiles;
    private Button syncButton;

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
        this.listFiles = findViewById(R.id.listFilesToSync);
        folder = Files.getDir(getApplicationContext(), STRINGS.SURVEYS.getString());
        this.setList();
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
