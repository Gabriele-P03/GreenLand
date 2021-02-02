package com.greenland;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.greenland.activity.mainButtons.SettingsActivity;
import com.greenland.activity.survey.CircularProgressBarActivity;
import com.greenland.activity.mainButtons.sync.SyncActivity;
import com.greenland.databinding.ActivityMainBinding;
import com.greenland.utils.Seed;

/**
 * @author GABRIELE
 * @link https://github.com/Gabriele-P03/GreenLand
 *
 * This class represents the Main Activity
 *
 * Shows the current data which represents the plant's state.
 * Data are get via HC-05 module.
 */

public class MainActivity extends AppCompatActivity {

    private static ConstraintLayout mainLayout;
    private static TextView[] textViews = new TextView[3];
    private static ProgressBar[] progressBars = new ProgressBar[3];
    private static ImageButton[] mainButtons = new ImageButton[3];
    private static ImageButton seedViewButton;

    //Data binding
    ActivityMainBinding mainBinding;
    public static LoadSettings loadSettings;

    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /**
         * Set the content view via DataBindingUtil, then bind the loadSettings object.
         * Now in main_layout.xml, loadObject's fields can be used.
         */
        mainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        loadSettings = new LoadSettings(getApplicationContext());
        mainBinding.setSettings(loadSettings);

        loadScreenSize();
        loadComponents();
    }

    /**
     * For each survey, it get textView and progressBar, and buttons in the main layout.
     * Every progress bar is set to use the drawable with the opposite color of the theme
     * e.g
     *  if dark theme is set, light circle is used
     *
     * @see @drawable/dark_circle_drawable
     * @see @drawable/light_circle_drawable
     */
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

        seedViewButton = findViewById(R.id.seedViewButton);
        seedViewButton.setOnClickListener(v -> showSeedInfo());

        loadButtonsEvent();

        Intent intent = new Intent(getApplicationContext(), CircularProgressBarActivity.class);
        startActivity(intent);
    }

    private void showSeedInfo() {

        LayoutInflater layoutInflater = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = layoutInflater.inflate(R.layout.seed_read_popup, null);

        Seed seed = loadSettings.getSeed();

        TextView name = popupView.findViewById(R.id.namePlantView);
        name.setText(seed.getNamePlant());
        TextView temperature = popupView.findViewById(R.id.temperaturePlantView);
        temperature.setText(seed.getTemperature()[0] + " - " + seed.getTemperature()[1]);
        TextView humidity = popupView.findViewById(R.id.humidityPlantView);
        humidity.setText(seed.getHumidity()[0] + " - " + seed.getHumidity()[1]);
        TextView light = popupView.findViewById(R.id.lightPlantView);
        light.setText(seed.getLight()[0] + " - " + seed.getLight()[1]);

        int size = ViewGroup.LayoutParams.WRAP_CONTENT;
        final PopupWindow popupWindow = new PopupWindow(popupView, size, size, true);
        popupWindow.showAtLocation(popupView, Gravity.CENTER, size, size);
    }

    /**
     * Get the screen real size and subtracts the title's bar's size
     * Then the params' layout are set to the right width and height
     */
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

    private void loadButtonsEvent() {
        mainButtons[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentSettings = new Intent(getApplicationContext(), SettingsActivity.class);
                startActivity(intentSettings);
            }
        });
        mainButtons[2].setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), SyncActivity.class)));
    }

    public static TextView[] getTextViews() {
        return textViews;
    }

    public static ProgressBar[] getProgressBars() {
        return progressBars;
    }

    public static ImageButton[] getMainButtons() { return mainButtons; }
}