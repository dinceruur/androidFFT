package com.github.dinceruur.androidfft.Helper;

import android.content.Context;
import android.util.Log;
import java.io.File;
import java.io.FileOutputStream;
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
            this.sendData();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendData(){
        String charset = "UTF-8";
        String requestURL = "http://35.246.151.115:3001/upload";
        String file_path = context.getFilesDir() + "/" + this.filename;

        try {
            MultipartUtility multipart = new MultipartUtility(requestURL, charset);
            multipart.addFormField("fileName", this.filename);
            multipart.addFilePart("uploadFile", new File(file_path));
            String response = multipart.finish();

            Log.wtf("MultiPartResponse", response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
