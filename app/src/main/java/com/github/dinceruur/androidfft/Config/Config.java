package com.github.dinceruur.androidfft.Config;

import android.content.Context;
import android.content.SharedPreferences;

public class Config{

    private SharedPreferences   pref;

    public Config(Context ctx){
        String sharedPrefName = "config";
        pref = ctx.getApplicationContext().getSharedPreferences(sharedPrefName, Context.MODE_PRIVATE);
    }

    public void setDcRemoving(int dcRemoving) {
        putInt("DC_REMOVING",dcRemoving);
    }

    public int getDcRemoving() {
        int DC_REMOVING = 1;
        return pref.getInt("DC_REMOVING", DC_REMOVING);
    }

    public void setSampleSize(int sampleSize) {
        putInt("SAMPLE_SIZE",sampleSize);
    }

    public int getSampleSize() {
        int SAMPLE_SIZE_DEFAULT = 3074;
        return pref.getInt("SAMPLE_SIZE", SAMPLE_SIZE_DEFAULT);
    }

    public int getSampleSizeSplashScreen(){
        return 256;
    }

    private void putInt(String key, int value){
        SharedPreferences.Editor edit = pref.edit();
        edit.putInt(key, value);
        edit.apply();
    }

}


