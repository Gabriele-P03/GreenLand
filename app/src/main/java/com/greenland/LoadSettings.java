package com.greenland;

import android.content.Context;
import android.graphics.Color;

import com.greenland.utils.Files;
import com.greenland.utils.STRINGS;
import java.io.BufferedReader;
import java.io.IOException;

public class LoadSettings {

    private Context context;
    private BufferedReader bufferedReader;
    private boolean darkMode;

    public LoadSettings(Context context){
        this.context = context;
        loadSettings();
    }

    private void loadSettings(){
        try {
            this.bufferedReader = Files.getBufferedReader(context, STRINGS.SETTINGS_FILE.getString());
            String buffer;
            if((buffer = bufferedReader.readLine()) != null ){
                darkMode = buffer.equals("1");
            }

            bufferedReader.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public boolean isDarkMode() {
        return darkMode;
    }

    public int getBWMode(){
        return darkMode ? Color.BLACK : Color.WHITE;
    }

    public int getWBMode(){
        return darkMode ? Color.WHITE : Color.BLACK;
    }

    public int getProgressDrawable(){
        return isDarkMode() ? R.drawable.light_circle_drawable : R.drawable.dark_circle_drawable;
    }

}
