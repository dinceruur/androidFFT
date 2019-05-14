package com.github.dinceruur.androidfft.Config;

import android.content.Context;
import android.content.SharedPreferences;
import com.github.dinceruur.androidfft.R;

public class Config{

    private SharedPreferences   pref;

    public Config(Context ctx){
        String sharedPrefName = "config";
        pref = ctx.getApplicationContext().getSharedPreferences(sharedPrefName, Context.MODE_PRIVATE);
    }

    public void setSampleSize(int sampleSize) {
        putInt("SAMPLE_SIZE",sampleSize);
    }

    public int getSampleSize() {
        int SAMPLE_SIZE_DEFAULT = 3074;
        return pref.getInt("SAMPLE_SIZE", SAMPLE_SIZE_DEFAULT);
    }

    public void setAccType(int accType) {
        putInt("ACC_TYPE",accType);
    }

    public int getAccType() {
        int ACC_TYPE_DEFAULT = R.id.acc_normal;
        return pref.getInt("ACC_TYPE", ACC_TYPE_DEFAULT);
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


