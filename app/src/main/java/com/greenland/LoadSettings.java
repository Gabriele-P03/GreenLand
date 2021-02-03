package com.greenland;

import android.content.Context;
import android.graphics.Color;

import com.greenland.utils.Files;
import com.greenland.utils.STRINGS;
import com.greenland.utils.Seed;

import java.io.BufferedReader;
import java.io.IOException;

/**
 * This class represents the loading settings
 * Called at the launching of app to set the background using @darkMode
 *
 * SETTINGS AVAILABLE:
 *      Dark Mode
 *      Seed
 *      IP that will receive surveys
 */
public class LoadSettings {

    private Context context;
    private BufferedReader bufferedReader;
    private BufferedReader seedReader;
    private Seed seed = null;

    private boolean darkMode;
    private String serverIP;
    private int serverPORT;

    public LoadSettings(Context context){

        this.context = context;
        loadSettings();

        try {
            seed = new Seed(seedReader);
        } catch (IOException e) {
            e.printStackTrace();
            seed = null;
        }
    }

    private void loadSettings(){
        try {
            this.bufferedReader = Files.getBufferedReader(context, STRINGS.SETTINGS_FILE.getString());
            this.seedReader = Files.getBufferedReader(context, STRINGS.SEED_FILE.getString());
            String buffer;

            if((buffer = bufferedReader.readLine()) != null ){
                darkMode = buffer.equals("1");
            }
            if((buffer = bufferedReader.readLine()) != null ){
                this.serverIP = buffer;
            }
            if((buffer = bufferedReader.readLine()) != null ){
                this.serverPORT = Integer.valueOf(buffer);
            }

            bufferedReader.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public boolean isDarkMode() {
        return darkMode;
    }

    public String getClientIP() {return serverIP;}

    public int getServerPORT() {
        return serverPORT;
    }

    /**
     * Called most of time for background
     * @return the color as theme
     */
    public int getBWMode(){
        return darkMode ? Color.BLACK : Color.WHITE;
    }

    /**
     * Called most of time for text color
     * @return the opposite color of the theme
     */
    public int getWBMode(){
        return darkMode ? Color.WHITE : Color.BLACK;
    }

    /**
     * Called by progress bar to set the drawable
     * @return circle with the opposite color to the theme
     */
    public int getProgressDrawable(){
        return isDarkMode() ? R.drawable.light_circle_drawable : R.drawable.dark_circle_drawable;
    }

    public Seed getSeed() {
        return seed;
    }
}
