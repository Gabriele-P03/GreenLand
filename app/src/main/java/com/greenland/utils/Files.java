package com.greenland.utils;

import android.content.Context;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class Files {

    public static File getFile(Context context, String fileName){
        File file = new File(context.getFilesDir(), fileName);
        if(!file.exists()){
            try {
                if (file.createNewFile()) {
                    Toast.makeText(context.getApplicationContext(), "File has been created!", Toast.LENGTH_LONG).show();
                }
            } catch (IOException e) {
                Toast.makeText(context.getApplicationContext(), "File hasn't been created", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        }
        Toast.makeText(context.getApplicationContext(), "File loaded!", Toast.LENGTH_LONG).show();
        return file;
    }

    public static BufferedReader getBufferedReader(Context context, String fileName) throws FileNotFoundException {
        return new BufferedReader(new FileReader(getFile(context, fileName)));
    }
}
