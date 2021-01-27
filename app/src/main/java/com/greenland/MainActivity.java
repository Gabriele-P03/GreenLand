package com.greenland;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.greenland.activity.Survey.CircularProgressBarActivity;
import com.greenland.utils.Files;
import com.greenland.utils.STRINGS;
import java.io.BufferedReader;
import java.io.FileNotFoundException;

public class MainActivity extends AppCompatActivity {

    private static LinearLayout linearLayout;
    public static final String SETTINGS_FILE = STRINGS.SETTINGS_FILE.getString();
    private static BufferedReader bufferedReader;

    private static TextView[] textViews = new TextView[3];
    private static ProgressBar[] progressBars = new ProgressBar[3];


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       // loadScreenSize();
        loadComponents();
    }

    private void loadComponents() {


        textViews[0] = (TextView)findViewById(R.id.TemperatureTV);
        progressBars[0] = (ProgressBar)findViewById(R.id.TemperaturePCB);
        textViews[1] = (TextView)findViewById(R.id.HumidityTV);
        progressBars[1] = (ProgressBar)findViewById(R.id.HumidityPCB);
        textViews[2] = (TextView)findViewById(R.id.LightTV);
        progressBars[2] = (ProgressBar)findViewById(R.id.LightPCB);

        Intent intent = new Intent(getApplicationContext(), CircularProgressBarActivity.class);
        startActivity(intent);

    }

//
//    private void loadScreenSize() {
//        linearLayout = findViewById(R.id.MainLayout);
//        ViewGroup.LayoutParams params = linearLayout.getLayoutParams();
//        params.width = 500;
//        params.height = 750;
//        linearLayout.setLayoutParams(params);
//        linearLayout.requestLayout();
//    }


    private void loadSettings(){
        try {
            bufferedReader = Files.getBufferedReader(getApplicationContext(), SETTINGS_FILE);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static TextView[] getTextViews() {
        return textViews;
    }

    public static ProgressBar[] getProgressBars() {
        return progressBars;
    }
}