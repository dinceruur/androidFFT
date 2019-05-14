package com.github.dinceruur.androidfft.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import com.github.dinceruur.androidfft.Config.Config;
import com.github.dinceruur.androidfft.R;


public class ConfigInput extends Fragment {

    View view;

    public static ConfigInput newInstance() {
        return new ConfigInput();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_config, container, false);

        final Config config                 = new Config(view.getContext());
        final EditText  getSampleSize       = view.findViewById(R.id.sampleSize);
        final Button    getSaveButton       = view.findViewById(R.id.saveButton);


        // if is there any saved config about sample size, get and put it
        int savedSampleSize = config.getSampleSize();
        getSampleSize.setText(String.valueOf(savedSampleSize));

        getSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                config.setSampleSize(Integer.parseInt(getSampleSize.getText().toString()));
            }
        });


        return view;
    }
}
