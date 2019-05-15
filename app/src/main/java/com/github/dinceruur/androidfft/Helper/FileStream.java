package com.github.dinceruur.androidfft.Helper;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;

import com.github.dinceruur.androidfft.BuildConfig;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class FileStream {

    private String filename;
    private Context context;

    public void saveData(Context context, double[][] data, String prefix){

        DateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd'-'HH.mm.ss", Locale.ROOT);
        Date date = new Date();

        this.context    = context;
        this.filename   = prefix + dateFormat.format(date) + ".csv";

        FileOutputStream outputStream;

        try {
            outputStream = this.context.openFileOutput(this.filename, Context.MODE_PRIVATE);

            for (double[] singleRow : data) {
                int index = 0;
                for (double singleElement : singleRow){
                    if(index == singleRow.length - 1){
                        outputStream.write((String.valueOf(singleElement)).getBytes());
                    }else{
                        outputStream.write((String.valueOf(singleElement) + ',').getBytes());
                    }
                    index++;
                }
                outputStream.write(("\n").getBytes());
            }

            outputStream.close();
            this.share();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void share(){
        String file_path = context.getFilesDir() + "/" + this.filename;

        Intent intentShareFile = new Intent(Intent.ACTION_SEND);
        File fileWithinMyDir = new File(file_path);

        if(fileWithinMyDir.exists()) {
            intentShareFile.setType("text/csv");

            Uri csvUri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", fileWithinMyDir);

            intentShareFile.putExtra(Intent.EXTRA_STREAM, csvUri);

            intentShareFile.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            intentShareFile.putExtra(Intent.EXTRA_SUBJECT,
                    "Sharing File...");
            intentShareFile.putExtra(Intent.EXTRA_TEXT, "Sharing File...");

            context.startActivity(Intent.createChooser(intentShareFile, "Share File"));
        }
    }

}
