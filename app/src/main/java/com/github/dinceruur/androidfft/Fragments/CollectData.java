package com.github.dinceruur.androidfft.Fragments;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.Series;
import java.text.NumberFormat;
import java.util.List;
import com.github.dinceruur.androidfft.AccelerometerListener;
import com.github.dinceruur.androidfft.Config.Config;
import com.github.dinceruur.androidfft.DataProcess.DoFFT;
import com.github.dinceruur.androidfft.DataProcess.MathOperators;
import com.github.dinceruur.androidfft.Helper.FileStream;
import com.github.dinceruur.androidfft.InterFace.InterFaceDataListener;
import com.github.dinceruur.androidfft.R;

public class CollectData extends Fragment implements CompoundButton.OnCheckedChangeListener,InterFaceDataListener {

    DoFFT                   doFFT;
    CollectData             collectData;
    ConstraintLayout graphContainer;
    ConstraintLayout initContainer;
    MathOperators           mathOperators;
    AccelerometerListener   fastestListener;
    SensorManager           mSensorManager;
    View                    view;
    ImageView               playButton;

    Switch switchX;
    Switch switchY;
    Switch switchZ;
    Button doFFTButton;
    Button exportButton;

    public GraphView                    graph;
    public LineGraphSeries<DataPoint>   mSeries1;
    public LineGraphSeries<DataPoint>   mSeries2;
    public LineGraphSeries<DataPoint>   mSeries3;

    public double[][] prepareFFTData;
    public long[]     timeStamp;
    int sampleSize;
    int graph2LastXValue    = 0;

    public static CollectData newInstance() {
        return new CollectData();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
        public View onCreateView(@NonNull  LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {

        collectData = this;

        mathOperators               = new MathOperators();
        Config config               = new Config(inflater.getContext());

        view                        = inflater.inflate(R.layout.fragment_collect, container, false);
        graphContainer              = view.findViewById(R.id.graphContainer);
        initContainer               = view.findViewById(R.id.initContainer);
        playButton                  = view.findViewById(R.id.playButton);

        doFFTButton                 = view.findViewById(R.id.fft);
        exportButton                = view.findViewById(R.id.export);
        playButton                  = view.findViewById(R.id.playButton);

        graph                       = view.findViewById(R.id.graphView);

        switchX                     = view.findViewById(R.id.switch1);
        switchY                     = view.findViewById(R.id.switch2);
        switchZ                     = view.findViewById(R.id.switch3);

        sampleSize      = config.getSampleSize();
        prepareFFTData  = new double[3][sampleSize];
        timeStamp       = new long[sampleSize];

        DisplayMetrics displayMetrics = view.getResources().getDisplayMetrics();
        float dpWidth   = displayMetrics.widthPixels / displayMetrics.density;
        int buttonWidth = Math.round((dpWidth - 24));

        ConstraintLayout.LayoutParams doFFTButtonLayoutParams       = (ConstraintLayout.LayoutParams) doFFTButton.getLayoutParams();
        ConstraintLayout.LayoutParams exportButtonLayoutParams      = (ConstraintLayout.LayoutParams) exportButton.getLayoutParams();
        doFFTButtonLayoutParams.width       = buttonWidth;
        exportButtonLayoutParams.width      = buttonWidth;
        doFFTButton.setLayoutParams(doFFTButtonLayoutParams);
        exportButton.setLayoutParams(exportButtonLayoutParams);

        doFFTButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                graph.setTitle("FFT");
                switchX.setChecked(true);
                switchY.setChecked(true);
                switchZ.setChecked(true);
                doFFT = new DoFFT(collectData);
                doFFT.init();
            }
        });

        exportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FileStream fileStream = new FileStream();
                fileStream.saveData(getActivity(), prepareFFTData,"rawAcc");
            }
        });

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playButton.animate()
                        .alpha(0)
                        .setDuration(300)
                        .setListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                initContainer.setVisibility(View.GONE);
                                graphContainer.setVisibility(View.VISIBLE);
                                drawGraph();
                            }
                        });
            }
        });

        return view;
    }


    public void drawGraph(){
        switchX.setVisibility(View.VISIBLE);
        switchY.setVisibility(View.VISIBLE);
        switchZ.setVisibility(View.VISIBLE);

        mSeries1 = new LineGraphSeries<>();
        mSeries2 = new LineGraphSeries<>();
        mSeries3 = new LineGraphSeries<>();

        mSeries1.setTitle("x");
        mSeries2.setTitle("y");
        mSeries3.setTitle("z");

        graph.setTranslationY(10);
        graph.getGridLabelRenderer().setLabelVerticalWidth(30);
        graph.getGridLabelRenderer().setTextSize(24);
        graph.setTitleColor(ContextCompat.getColor(view.getContext(), R.color.secondaryColor));
        graph.setTitle("Real Time Acceleration Data");
        graph.getGridLabelRenderer().setPadding(20);
        graph.getGridLabelRenderer().setHighlightZeroLines(true);
        graph.getGridLabelRenderer().setHorizontalLabelsVisible(false);
        graph.getGridLabelRenderer().setGridColor(ContextCompat.getColor(view.getContext(), R.color.secondaryDarkColor));
        graph.getGridLabelRenderer().setHorizontalLabelsColor(ContextCompat.getColor(view.getContext(), R.color.primaryTextColor));
        graph.getGridLabelRenderer().setVerticalLabelsColor(ContextCompat.getColor(view.getContext(), R.color.primaryTextColor));

        mSeries1.setColor(ContextCompat.getColor(view.getContext(), R.color.lineColor1));
        mSeries2.setColor(ContextCompat.getColor(view.getContext(), R.color.lineColor2));
        mSeries3.setColor(ContextCompat.getColor(view.getContext(), R.color.lineColor3));

        NumberFormat nf = NumberFormat.getInstance();
        nf.setMaximumFractionDigits(3);
        nf.setMaximumIntegerDigits(3);
        graph.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter(nf, nf));

        graph.addSeries(mSeries1);
        graph.addSeries(mSeries2);
        graph.addSeries(mSeries3);

        switchX.setTextColor(ContextCompat.getColor(view.getContext(), R.color.lineColor1));
        switchY.setTextColor(ContextCompat.getColor(view.getContext(), R.color.lineColor2));
        switchZ.setTextColor(ContextCompat.getColor(view.getContext(), R.color.lineColor3));

        switchX.setOnCheckedChangeListener(this);
        switchY.setOnCheckedChangeListener(this);
        switchZ.setOnCheckedChangeListener(this);

        fastestListener = new AccelerometerListener(this);

        mSensorManager = (SensorManager) view.getContext().getSystemService(Context.SENSOR_SERVICE);

        mSensorManager.registerListener(fastestListener,
                mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_FASTEST);

        mSensorManager.registerListener(fastestListener,
                mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),
                SensorManager.SENSOR_DELAY_FASTEST);

        fastestListener.init();
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {



        switch (buttonView.getId()) {

            case R.id.switch1:



                if(isChecked){
                    if(graph.getSeries().size() == 0){
                        if(mSeries1 != null)
                        graph.addSeries(mSeries1);
                    }else{
                        boolean isExist = checkListContains(graph.getSeries(),"x");

                        if(Boolean.FALSE.equals(isExist)){
                            graph.addSeries(mSeries1);
                        }
                    }

                }else{
                    graph.removeSeries(mSeries1);
                }


                break;

            case R.id.switch2:

                if(isChecked){
                    if(graph.getSeries().size() == 0){
                        if(mSeries2 != null)
                        graph.addSeries(mSeries2);
                    }else{
                        boolean isExist = checkListContains(graph.getSeries(),"y");

                        if(Boolean.FALSE.equals(isExist)){
                            graph.addSeries(mSeries2);
                        }
                    }

                }else{
                    graph.removeSeries(mSeries2);
                }

                break;

            case R.id.switch3:

                if(isChecked){
                    if(graph.getSeries().size() == 0){
                        if(mSeries3 != null)
                        graph.addSeries(mSeries3);
                    }else{
                        boolean isExist = checkListContains(graph.getSeries(),"z");

                        if(Boolean.FALSE.equals(isExist)){
                            graph.addSeries(mSeries3);
                        }
                    }
                }else{
                    graph.removeSeries(mSeries3);
                }

                break;

            default:
                break;
        }
    }

    public boolean checkListContains(List<Series> checkMe, String needle){

        boolean isExist = false;

        for (Series singleSeries: checkMe){
            isExist = singleSeries.getTitle().equals(needle);
        }
        return isExist;
    }

    @Override
    public void notifySensorChanged() {

        if(fastestListener.getSensorType() == Sensor.TYPE_ACCELEROMETER){

            float[] accelerometer           = fastestListener.getAccelerometerVector();
            long    sensorTimeStamp         = fastestListener.getTimeStamp();

            if( graph2LastXValue < sampleSize ){

                if(accelerometer[0] != 0){
                    timeStamp[graph2LastXValue] = sensorTimeStamp;

                    prepareFFTData[0][graph2LastXValue] = accelerometer[0];
                    prepareFFTData[1][graph2LastXValue] = accelerometer[1];
                    prepareFFTData[2][graph2LastXValue] = accelerometer[2];

                    mSeries1.appendData(new DataPoint(graph2LastXValue, accelerometer[0]), true,sampleSize);
                    mSeries2.appendData(new DataPoint(graph2LastXValue, accelerometer[1]), true,sampleSize);
                    mSeries3.appendData(new DataPoint(graph2LastXValue, accelerometer[2]), true,sampleSize);

                    graph.getViewport().setXAxisBoundsManual(true);
                    graph.getViewport().setMinX(0);
                    graph.getViewport().setMaxX(graph2LastXValue);
                    graph.getViewport().setScalable(true);
                    graph.getViewport().setScalableY(true);

                    graph2LastXValue++;
                }

            }else{
                mSensorManager.unregisterListener(fastestListener);

                graph.getGridLabelRenderer().setVerticalLabelsVisible(true);

                doFFTButton.setVisibility(View.VISIBLE);
                exportButton.setVisibility(View.VISIBLE);
            }
        }
    }

    public void showResults(DataPoint[][] graphData){
        // removing real-time sensor data from the graph
        graph.removeAllSeries();

        // reconstructing the graph series with fft data
        mSeries1.resetData(graphData[0]);
        mSeries2.resetData(graphData[1]);
        mSeries3.resetData(graphData[2]);

        graph.getGridLabelRenderer().setPadding(20);
        graph.getGridLabelRenderer().setHighlightZeroLines(true);
        graph.getGridLabelRenderer().setHorizontalLabelsVisible(true);
        graph.getGridLabelRenderer().setVerticalLabelsVisible(false);
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMinX(0);
        graph.getViewport().setMaxX(graphData[0][graphData[0].length-1].getX());

        graph.getViewport().setScrollable(false); // enables horizontal scrolling
        graph.getViewport().setScrollableY(false); // enables vertical scrolling
        graph.getViewport().setScalable(false); // enables horizontal zooming and scrolling
        graph.getViewport().setScalableY(false); // enables vertical zooming and scrolling

        // adding series to the graph
        graph.addSeries(mSeries1);
        graph.addSeries(mSeries2);
        graph.addSeries(mSeries3);
    }


}
