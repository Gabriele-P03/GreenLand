package com.greenland.activity.survey;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;

import com.greenland.MainActivity;

public class CircularProgressBarActivity extends Activity {

    private int[] amounts = {0, 0, 0};

    private Handler handler = new Handler();
    private static Runnable runnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        runnable = new Runnable() {
            @Override
            public void run() {

                for(int i = 0; i < 3; i++){
                    MainActivity.getTextViews()[i].setText(amounts[i] + "%");
                    MainActivity.getProgressBars()[i].setProgress((int)amounts[i]);
                    if (amounts[i] < 100)
                        amounts[i]++;
                }
                handler.postDelayed(this, 1);
            }
        };
        runnable.run();

        finish();
    }

}