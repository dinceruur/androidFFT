package com.github.dinceruur.androidfft;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import com.github.dinceruur.androidfft.Fragments.CollectData;
import com.github.dinceruur.androidfft.Fragments.ConfigInput;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onDestroy() {
        deleteFiles(getFilesDir());
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        BottomNavigationView bottomNavigationView = findViewById(R.id.navigationView);

        bottomNavigationView.setOnNavigationItemSelectedListener
                (new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        Fragment selectedFragment = CollectData.newInstance();
                        switch (item.getItemId()) {
                            case R.id.collectData:
                                selectedFragment = CollectData.newInstance();
                                break;
                            case R.id.config:
                                selectedFragment = ConfigInput.newInstance();
                                break;
                        }

                            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                            transaction.replace(R.id.container, selectedFragment);
                            transaction.commit();

                        return true;
                    }
                });


        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, CollectData.newInstance());
        transaction.commit();

    }

    protected void onResume() {
        super.onResume();
    }

    protected void onPause() {
        super.onPause();
    }

    void deleteFiles(File file) {
        if( file.exists() ) {
            if (file.isDirectory()) {
                File[] files = file.listFiles();
                for (File singleFile : files) {
                    if (!singleFile.isDirectory()) {
                        singleFile.delete();
                    }
                }
            }
        }
    }
}