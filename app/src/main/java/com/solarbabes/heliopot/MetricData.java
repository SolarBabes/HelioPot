package com.solarbabes.heliopot;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.sip.SipSession;
import android.os.Bundle;
import android.util.Log;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
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
//    private ArrayList<Entry> dataVals = new ArrayList<Entry>();
    private LineChart[] mpLineChart = new LineChart[4];
//    ArrayList<Entry>[] dataVals = new ArrayList[4];
    private Comparator<Entry> c = new Comparator<Entry>(){
        @Override
        public int compare(Entry t1, Entry t2) {
            if (t1.getX()-t2.getX()>0){return 1; }
            else if(t1.getX()==t2.getX()){return 0;}
            else{return -1;
            }
        }
    };
    private DatabaseReference mDatabase;
    ValueEventListener Listener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            Log.d("metric","change");
            for (int i = 0; i < 4; i++) {
                Map<String, Long> map = (Map<String, Long>) dataSnapshot.child(allMeasurements[i]).getValue();
                ArrayList<Entry> dataVals = new ArrayList<Entry>();
                for (String s:map.keySet()){
//                        Log.d(allMeasurements[i],s+" "+Long.toString(map.get(s)));
                    dataVals.add(new Entry(Long.parseLong(s)-1580000000L,map.get(s)));
                }
                Collections.sort(dataVals,c);
                while (dataVals.size()>31){
                    dataVals.remove(0);
                }
                ArrayList<ILineDataSet> dataSets = new ArrayList<>();
                LineDataSet lineDataSet = new LineDataSet(dataVals,allMeasurements[i]);
                lineDataSet.setLineWidth(3);

                dataSets.add(lineDataSet);
                mpLineChart[i].setData(new LineData(dataSets));
                mpLineChart[i].invalidate();
            }
        }
        @Override
        public void onCancelled(DatabaseError databaseError) {
            Log.w("123", "loadPost:onCancelled", databaseError.toException());
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_metric_data);
        Intent intent = getIntent();
        plantName = intent.getStringExtra(PlantList.PLANT_NAME);
        mDatabase = FirebaseDatabase.getInstance().getReference("bot/"+plantName);

        mpLineChart[0] = (LineChart) findViewById(R.id.line_chart_temperature);
        mpLineChart[1] = (LineChart) findViewById(R.id.line_chart_moisture);
        mpLineChart[2] = (LineChart) findViewById(R.id.line_chart_humidity);
        mpLineChart[3] = (LineChart) findViewById(R.id.line_chart_light);


        Description description = new Description();
        description.setText("");
        for (int i = 0; i < 4; i++) {
            Legend legend = mpLineChart[i].getLegend();
            legend.setEnabled(false);
            mpLineChart[i].setDescription(description);

        }
        mpLineChart[0].setTouchEnabled(false);
//        mpLineChart[1].setScaleEnabled(false);
//        mpLineChart[0].setXAxisRenderer();

        mDatabase.addValueEventListener(Listener);

        for (int i = 0; i < 4; i++) {
            ArrayList<Entry> dataVals = new ArrayList<Entry>();
            dataVals.add(new Entry(0,0));
            ArrayList<ILineDataSet> dataSets = new ArrayList<>();
            LineDataSet lineDataSet = new LineDataSet(dataVals,allMeasurements[i]);
            lineDataSet.setLineWidth(3);
            dataSets.add(lineDataSet);
            mpLineChart[i].setData(new LineData(dataSets));
            mpLineChart[i].invalidate();
        }
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mDatabase.removeEventListener(Listener);
    }
}
