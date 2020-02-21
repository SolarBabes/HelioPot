package com.solarbabes.heliopot;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

public class Main2Activity extends AppCompatActivity {
    LineChart mpLineChart;
    ArrayList<Entry> dataVals = new ArrayList<Entry>();
//    LineDataSet lineDataSet1;
    ArrayList<ILineDataSet> dataSets = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        mpLineChart = (LineChart) findViewById(R.id.line_chart);



        dataVals.add(new Entry(1,10));
        dataVals.add(new Entry(2,10));
        dataVals.add(new Entry(3,0));
        dataVals.add(new Entry(4,70));
        dataVals.add(new Entry(5,50));
        dataVals.add(new Entry(6,40));


//        lineDataSet1 = new LineDataSet(dataVals,"data set 1");
        dataSets.clear();
        dataSets.add(new LineDataSet(dataVals,"data set 1"));

//        mpLineChart.setBackgroundColor(Color.BLUE);
//        mpLineChart.setNoDataText("NO DATA");

//        LineData data = new LineData(dataSets);

        XAxis xAxis = mpLineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextSize(10f);
        xAxis.setTextColor(Color.BLACK);
        xAxis.setDrawAxisLine(true);
        xAxis.setDrawGridLines(true);
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return (Float.toString(value+100));
            }
        });





        mpLineChart.setData(new LineData(dataSets));
        mpLineChart.invalidate();
        Log.d("draw","1");





    }

    private ArrayList<Entry> dataValues1(){
        ArrayList<Entry> dataVals = new ArrayList<Entry>();
        dataVals.add(new Entry(1,10));
        dataVals.add(new Entry(2,10));
        dataVals.add(new Entry(3,0));
        dataVals.add(new Entry(4,70));
        dataVals.add(new Entry(5,50));
        dataVals.add(new Entry(6,40));
        return dataVals;
    }

    public void do1(View view) {
        dataVals.remove(0);

        dataVals.add(new Entry(7,10));
        dataVals.add(new Entry(8,80));


//        lineDataSet1 = new LineDataSet(dataVals,"data set 1");
        dataSets.clear();
        dataSets.add(new LineDataSet(dataVals,"data set 1"));
        mpLineChart.setData(new LineData(dataSets));
        mpLineChart.invalidate();
        Log.d("draw","2");

    }



}