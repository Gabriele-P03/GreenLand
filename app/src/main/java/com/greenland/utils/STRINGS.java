package com.greenland.utils;

public enum STRINGS {

    SETTINGS_FILE("settings.txt");

    private String String;

    STRINGS(String string){
        this.String = string;
    }

    public String getString() {
        return String;
    }
}
