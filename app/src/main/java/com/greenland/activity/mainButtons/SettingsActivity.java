package com.greenland.activity.mainButtons;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.Toast;
import com.greenland.MainActivity;
import com.greenland.R;
import com.greenland.databinding.ActivitySettingsBinding;
import com.greenland.utils.Files;
import com.greenland.utils.STRINGS;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;

/**
 * Settings Activity
 * Activity started when settings button, in the main layout, is clicked
 * This one afford the user to change the recommended values of the seed and the dark mode
 *
 * @author GABRIELE-P03
 */
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
        this.saveButton.setOnClickListener( v -> saveSettings() );
        this.seedButton = findViewById(R.id.seedButton);
        this.seedButton.setOnClickListener( v -> showPopupWindow());
        this.darkModeCB = findViewById(R.id.darkMode);
        this.darkModeCB.setChecked(MainActivity.loadSettings.isDarkMode());
    }

    /**
     * New popup layout. It is inflated when seed button is clicked
     * This layout is used to set the new recommended values of the plant
     */
    View popupView;
    private void showPopupWindow() {
        //Inflate the popupWindow's layout
        LayoutInflater layoutInflater = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
        popupView = layoutInflater.inflate(R.layout.seed_write_popup_layout, null);
        popupView.setBackgroundColor(MainActivity.loadSettings.getWBMode());
        int width_height = ViewGroup.LayoutParams.WRAP_CONTENT; //Create the popup window
        boolean focus = true; //To close the popup window when the user tap over it
        final PopupWindow popupWindow = new PopupWindow(popupView, width_height, width_height, focus);

        //View given doesn't mind, it will used only for window token
        popupWindow.showAtLocation(new View(this), Gravity.CENTER, 0, 0 );
        Button button = popupView.findViewById(R.id.saveSeedButton);
        button.setOnClickListener(v -> saveSeed());
    }

    public void saveSeed() {
        BufferedWriter seedFile;
        EditText name = popupView.findViewById(R.id.namePlant);
        EditText temperature = popupView.findViewById(R.id.temperaturePlant);
        EditText humidity = popupView.findViewById(R.id.humidityPlant);
        EditText light = popupView.findViewById(R.id.lightPlant);

        try{
            seedFile = Files.getBufferedWriter(popupView.getContext(), STRINGS.SEED_FILE.getString());
            seedFile.write(name.getText().toString() + "\n");
            seedFile.write(temperature.getText().toString() + "\n");
            seedFile.write(humidity.getText().toString() + "\n");
            seedFile.write(light.getText().toString());

            seedFile.flush();
            seedFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void saveSettings(){
        BufferedWriter bufferedWriter = null;
        try{
            bufferedWriter = Files.getBufferedWriter(getApplicationContext(), STRINGS.SETTINGS_FILE.getString());
            bufferedWriter.write( (darkModeCB.isChecked() ? "1" : "0") );
            bufferedWriter.flush();
            bufferedWriter.close();
            Toast.makeText(getApplicationContext(), "Settings saved", Toast.LENGTH_SHORT).show();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}