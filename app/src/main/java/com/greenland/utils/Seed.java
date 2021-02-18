package com.greenland.utils;


import androidx.annotation.Nullable;

import java.io.BufferedReader;
import java.io.IOException;

/**
 * This class sets a seed as object
 *
 * LoadSettings makes an instance of a new object of this class.
 * In order of it, you can pass the buffered reader of the seed File or the range values of the surveys as string
 * @see Seed(BufferedReader)
 * @see Seed(String...)
 */
public class Seed {

    private final String namePlant;
    private final int[] temperature;
    private final int[] humidity;
    private final int[] light;

    /**
     * Constructor with buffered reader calls constructor passing the range values as string
     * @param bufferedReader
     * @throws IOException
     */
    public Seed(BufferedReader bufferedReader) throws IOException {
       this(bufferedReader.readLine(), bufferedReader.readLine(), bufferedReader.readLine(), bufferedReader.readLine());
    }

    public Seed(String namePlant, String temperature, String humidity, String light){
       this.namePlant = namePlant;
       this.temperature = getRangeSurvey(temperature);
       this.humidity = getRangeSurvey(humidity);
       this.light = getRangeSurvey(light);
    }

    /**
     * Get the index of the dash.
     * e.g.
     * "17-18" it returns 2
     *
     * Substring from 0 to @index is the min value passable
     * String from @index+1 is the max value passable
     *
     * @param string - string to divide
     * @return range values as integer array
     */
    private int[] getRangeSurvey(@Nullable String string){
        int[] range = {0, 0};
        if (string == "")
            return range;

        int i = 0;
        for (i = 0; i < string.length(); i++){
            if(string.charAt(i) == '-')
                break;
        }

        if(i < string.length()) {
            range[0] = Integer.valueOf(string.substring(0, i));
            range[1] = Integer.valueOf(string.substring(i + 1));
        }

        return range;
    }

    public String getNamePlant() {
        return namePlant;
    }

    public int[] getTemperature() {
        return temperature;
    }

    public int[] getHumidity() {
        return humidity;
    }

    public int[] getLight() {
        return light;
    }
}
