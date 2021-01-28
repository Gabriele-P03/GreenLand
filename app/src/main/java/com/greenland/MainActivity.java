package com.greenland;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.greenland.activity.mainButtons.SettingsActivity;
import com.greenland.activity.survey.CircularProgressBarActivity;
import com.greenland.databinding.ActivityMainBinding;
import com.greenland.utils.Files;
import com.greenland.utils.STRINGS;
import java.io.BufferedReader;
import java.io.FileNotFoundException;

public class MainActivity extends AppCompatActivity {

    private static ConstraintLayout mainLayout;
    private static TextView[] textViews = new TextView[3];
    private static ProgressBar[] progressBars = new ProgressBar[3];
    private static ImageButton[] mainButtons = new ImageButton[3];

    //Data binding
    ActivityMainBinding mainBinding;
    public static LoadSettings loadSettings;

    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        loadSettings = new LoadSettings(getApplicationContext());
        mainBinding.setSettings(loadSettings);

        loadScreenSize();
        loadComponents();
    }

    private void loadComponents() {
        textViews[0] = findViewById(R.id.TemperatureTV);
        progressBars[0] = findViewById(R.id.TemperaturePCB);
        progressBars[0].setProgressDrawable(getResources().getDrawable(loadSettings.getProgressDrawable()));

        textViews[1] = findViewById(R.id.HumidityTV);
        progressBars[1] = findViewById(R.id.HumidityPCB);
        progressBars[1].setProgressDrawable(getResources().getDrawable(loadSettings.getProgressDrawable()));

        textViews[2] = findViewById(R.id.LightTV);
        progressBars[2] = findViewById(R.id.LightPCB);
        progressBars[2].setProgressDrawable(getResources().getDrawable(loadSettings.getProgressDrawable()));

        mainButtons[0] = findViewById(R.id.settingsButton);
        mainButtons[1] = findViewById(R.id.bltButton);
        mainButtons[2] = findViewById(R.id.syncButton);

        loadButtonsEvent();

        Intent intent = new Intent(getApplicationContext(), CircularProgressBarActivity.class);
        startActivity(intent);
    }

    public static int newX;
    public static int newY;
    @RequiresApi(api = Build.VERSION_CODES.R)
    private void loadScreenSize() {
        mainLayout = findViewById(R.id.mainLayout);
        Point point = new Point();
        getWindowManager().getDefaultDisplay().getRealSize(point);

        ViewGroup.LayoutParams tmp = mainLayout.getLayoutParams();

        newX  = point.x - (point.x - tmp.width);
        newY = point.y - (point.y - tmp.height);

        mainLayout.getLayoutParams().height = newY;
        mainLayout.getLayoutParams().width = newX;
    }

    private void loadButtonsEvent(){
        mainButtons[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentSettings = new Intent(getApplicationContext(), SettingsActivity.class);
                startActivity(intentSettings);
            }
        });
    }


    public static TextView[] getTextViews() {
        return textViews;
    }

    public static ProgressBar[] getProgressBars() {
        return progressBars;
    }

    public static ImageButton[] getMainButtons() { return mainButtons; }
}