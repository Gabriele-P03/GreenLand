package com.greenland.activity.mainButtons;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.greenland.MainActivity;
import com.greenland.R;
import com.greenland.databinding.ActivitySettingsBinding;
import com.greenland.utils.Files;
import com.greenland.utils.STRINGS;

import java.io.BufferedWriter;
import java.io.IOException;

public class SettingsActivity extends AppCompatActivity {

    private CheckBox darkModeCB;
    private Button seedButton;

    ActivitySettingsBinding settingsBinding;
    private Button saveButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        settingsBinding = DataBindingUtil.setContentView(this, R.layout.activity_settings);
        settingsBinding.setSettings(MainActivity.loadSettings);
        loadComponents();
    }

    private void loadComponents() {
        this.saveButton = findViewById(R.id.saveSettings);
        this.saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveSettings();
            }
        });
        this.seedButton = findViewById(R.id.seedButton);
        this.seedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupWindow();
            }
        });
        this.darkModeCB = findViewById(R.id.darkMode);
        this.darkModeCB.setChecked(MainActivity.loadSettings.isDarkMode());
    }

    private void showPopupWindow() {

        //Inflate the popupWindow's layout
        LayoutInflater layoutInflater = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = layoutInflater.inflate(R.layout.seed_write_popup_layout, null);
        popupView.setBackgroundColor(MainActivity.loadSettings.getWBMode());
        //Create the popup window
        int width_height = ViewGroup.LayoutParams.WRAP_CONTENT;
        boolean focus = true; //To dismiss popup window when I tap over it
        final PopupWindow popupWindow = new PopupWindow(popupView, width_height, width_height, focus);

        //View given doesn't mind, it will used only for window token
        popupWindow.showAtLocation(new View(this), Gravity.CENTER, 0, 0 );
    }


    private void saveSettings(){
        BufferedWriter bufferedWriter = null;
        try{
            bufferedWriter = Files.getBufferedWriter(getApplicationContext(), STRINGS.SETTINGS_FILE.getString());
            bufferedWriter.write( darkModeCB.isChecked() ? "1" : "0");

            bufferedWriter.flush();
            bufferedWriter.close();
            Toast.makeText(getApplicationContext(), "Settings saved", Toast.LENGTH_SHORT).show();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

}