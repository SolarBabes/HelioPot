package com.solarbabes.heliopot;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;

public class MetricData extends AppCompatActivity {
    private static String plantName;
    private static String[] allMeasurements = {"temperature", "moisture", "humidity", "light"};
    private ArrayList<Entry> temperature = new ArrayList<Entry>();
    private ArrayList<Entry> moisture = new ArrayList<Entry>();
    private ArrayList<Entry> humidity = new ArrayList<Entry>();
    private ArrayList<Entry> light = new ArrayList<Entry>();
    private LineChart mpLineChart_temperature;
    private LineChart mpLineChart_moisture;
    private LineChart mpLineChart_humidity;
    private LineChart mpLineChart_light;
    ArrayList<Entry> dataVals = new ArrayList<Entry>();
    Comparator<Entry> c = new Comparator<Entry>(){
        @Override
        public int compare(Entry t1, Entry t2) {
            if (t1.getX()-t2.getX()>0){return 1; }
            else if(t1.getX()==t2.getX()){return 0;}
            else{return -1;
            }
        }
    };

    ArrayList<ILineDataSet> dataSets = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_metric_data);
        Intent intent = getIntent();
        plantName = intent.getStringExtra(PlantList.PLANT_NAME);
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("bot/"+plantName);

        ValueEventListener Listener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, Long> map = (Map<String, Long>) dataSnapshot.child("temperature").getValue();
                temperature.clear();
                for (String s:map.keySet()){
                    temperature.add(new Entry(Long.parseLong(s)-1580000000000L,map.get(s)));
                }
                Collections.sort(temperature,c);
                while (temperature.size()>31){
                    temperature.remove(0);
                }
                dataSets.clear();
                dataSets.add(new LineDataSet(temperature,"temperature"));
                mpLineChart_temperature.setData(new LineData(dataSets));
                mpLineChart_temperature.invalidate();


                Map<String, Long> map1 = (Map<String, Long>) dataSnapshot.child("moisture").getValue();
                moisture.clear();
                for (String s:map1.keySet()){
                    moisture.add(new Entry(Long.parseLong(s)-1580000000000L,map1.get(s)));
                }
                Collections.sort(moisture,c);
                while (moisture.size()>31){
                    moisture.remove(0);
                }
                dataSets.clear();
                dataSets.add(new LineDataSet(moisture,"moisture"));
                mpLineChart_moisture.setData(new LineData(dataSets));
                mpLineChart_moisture.invalidate();


                Map<String, Long> map2 = (Map<String, Long>) dataSnapshot.child("humidity").getValue();
                humidity.clear();
                for (String s:map2.keySet()){
                    humidity.add(new Entry(Long.parseLong(s)-1580000000000L,map2.get(s)));
                }
                Collections.sort(humidity,c);
                while (humidity.size()>31){
                    humidity.remove(0);
                }
                dataSets.clear();
                dataSets.add(new LineDataSet(humidity,"humidity"));
                mpLineChart_humidity.setData(new LineData(dataSets));
                mpLineChart_humidity.invalidate();


                Map<String, Long>  map3 = (Map<String, Long>) dataSnapshot.child("light").getValue();
                light.clear();
                for (String s:map3.keySet()){
                    light.add(new Entry(Long.parseLong(s)-1580000000000L,map3.get(s)));
                }
                Collections.sort(light,c);
                while (light.size()>31){
                    light.remove(0);
                }
                dataSets.clear();
                dataSets.add(new LineDataSet(light,"light"));
                mpLineChart_light.setData(new LineData(dataSets));
                mpLineChart_light.invalidate();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("123", "loadPost:onCancelled", databaseError.toException());
            }
        };
        mDatabase.addValueEventListener(Listener);


        mpLineChart_temperature = (LineChart) findViewById(R.id.line_chart_temperature);
        temperature.add(new Entry(0,0));
        dataSets.clear();
        dataSets.add(new LineDataSet(temperature,"temperature"));
        mpLineChart_temperature.setData(new LineData(dataSets));
        mpLineChart_temperature.invalidate();

        mpLineChart_moisture = (LineChart) findViewById(R.id.line_chart_moisture);
        moisture.add(new Entry(0,0));
        dataSets.clear();
        dataSets.add(new LineDataSet(moisture,"moisture"));
        mpLineChart_moisture.setData(new LineData(dataSets));
        mpLineChart_moisture.invalidate();

        mpLineChart_humidity = (LineChart) findViewById(R.id.line_chart_humidity);
        humidity.add(new Entry(0,0));
        dataSets.clear();
        dataSets.add(new LineDataSet(humidity,"humidity"));
        mpLineChart_humidity.setData(new LineData(dataSets));
        mpLineChart_humidity.invalidate();

        mpLineChart_light = (LineChart) findViewById(R.id.line_chart_light);
        light.add(new Entry(0,0));
        dataSets.clear();
        dataSets.add(new LineDataSet(light,"light"));
        mpLineChart_light.setData(new LineData(dataSets));
        mpLineChart_light.invalidate();

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


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }





}
