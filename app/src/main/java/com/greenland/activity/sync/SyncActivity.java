package com.greenland.activity.sync;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.greenland.R;
import com.greenland.utils.Files;
import com.greenland.utils.STRINGS;

import java.io.File;

public class SyncActivity extends AppCompatActivity {

    private File folder;
    private File[] files;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sync_layout);
        folder = Files.getFile(getApplicationContext(), STRINGS.SURVEYS.getString());
    }

    private void getFiles(){
        files = new File[folder.listFiles().length];
        for(int i = 0; i < folder.listFiles().length; i++){
            files[i] = folder.listFiles()[i];
        }
    }
}
