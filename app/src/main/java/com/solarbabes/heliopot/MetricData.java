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
    ArrayList<Entry> dataVals = new ArrayList<Entry>();
    private Comparator<Entry> c = new Comparator<Entry>(){
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
                    Log.d("first",s);
                    Log.d("second",Long.toString(map.get(s)));
                    temperature.add(new Entry(Long.parseLong(s)-1580000000000L,map.get(s)));
                }
                Collections.sort(temperature,c);
                while (temperature.size()>31){
                    temperature.remove(0);
                }
                dataSets.clear();
                dataSets.add(new LineDataSet(temperature,"data set 1"));
                mpLineChart_temperature.setData(new LineData(dataSets));
                mpLineChart_temperature.invalidate();

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
        dataSets.add(new LineDataSet(temperature,"data set 1"));
        mpLineChart_temperature.setData(new LineData(dataSets));
        mpLineChart_temperature.invalidate();

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
