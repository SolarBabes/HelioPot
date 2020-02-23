package com.solarbabes.heliopot;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Map;

public class MetricData extends AppCompatActivity {
    private static String plantName;
    private static String plantId;
    private static String[] allMeasurements = {"temperature", "moisture", "humidity", "light"};
    private LineChart[] mpLineChart = new LineChart[4];
    private Comparator<Entry> c = new Comparator<Entry>(){
        @Override
        public int compare(Entry t1, Entry t2) {
            if (t1.getX()-t2.getX()>0){return 1; }
            else if(t1.getX()==t2.getX()){return 0;}
            else{return -1;
            }
        }
    };
    private Button Button3;
    private Button Button4;
    private Button Button5;
    private Button Button6;
    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss ddMMM");
    private DatabaseReference mDatabase;
    ValueEventListener Listener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            for (int i = 0; i < 4; i++) {
                Map<String, Long> map = (Map<String, Long>) dataSnapshot.child(allMeasurements[i]).getValue();
                ArrayList<Entry> dataVals = new ArrayList<Entry>();
                for (String s:map.keySet()){
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
        plantId = intent.getStringExtra(PlantList.PLANT_ID);
        mDatabase = FirebaseDatabase.getInstance().getReference("heliopots/"+plantId+"/data");

        mpLineChart[0] = (LineChart) findViewById(R.id.line_chart_temperature);
        mpLineChart[1] = (LineChart) findViewById(R.id.line_chart_moisture);
        mpLineChart[2] = (LineChart) findViewById(R.id.line_chart_humidity);
        mpLineChart[3] = (LineChart) findViewById(R.id.line_chart_light);

        Button3 = findViewById(R.id.button3);
        Button4 = findViewById(R.id.button4);
        Button5 = findViewById(R.id.button5);
        Button6 = findViewById(R.id.button6);


        Description description = new Description();
        description.setText("");
        for (int i = 0; i < 4; i++) {
            Legend legend = mpLineChart[i].getLegend();
            legend.setEnabled(false);
            mpLineChart[i].setDescription(description);
            mpLineChart[i].setNoDataText("NO DATA");

            XAxis xAxis = mpLineChart[i].getXAxis();
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            xAxis.setTextSize(8f);
            xAxis.setLabelCount(4,true);
            xAxis.setTextColor(Color.BLACK);
            xAxis.setDrawAxisLine(true);
            xAxis.setDrawGridLines(true);
            xAxis.setValueFormatter(new IAxisValueFormatter() {
                @Override
                public String getFormattedValue(float value, AxisBase axis) {
                    Date resultdate = new Date((Math.round(value)+1580000000L)*1000);
                    return (sdf.format(resultdate));
                }
            });
        }
//        mpLineChart[0].setTouchEnabled(false);
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

    public void onClick3(View view){
        Button3.setBackgroundResource(R.drawable.rounded_shape_palegreen);
        Button4.setBackgroundResource(R.drawable.rounded_shape_grey);
        Button5.setBackgroundResource(R.drawable.rounded_shape_grey);
        Button6.setBackgroundResource(R.drawable.rounded_shape_grey);
    }

    public void onClick4(View view){
        Button4.setBackgroundResource(R.drawable.rounded_shape_palegreen);
        Button3.setBackgroundResource(R.drawable.rounded_shape_grey);
        Button5.setBackgroundResource(R.drawable.rounded_shape_grey);
        Button6.setBackgroundResource(R.drawable.rounded_shape_grey);
    }

    public void onClick5(View view){
        Button5.setBackgroundResource(R.drawable.rounded_shape_palegreen);
        Button4.setBackgroundResource(R.drawable.rounded_shape_grey);
        Button3.setBackgroundResource(R.drawable.rounded_shape_grey);
        Button6.setBackgroundResource(R.drawable.rounded_shape_grey);
    }

    public void onClick6(View view){
        Button6.setBackgroundResource(R.drawable.rounded_shape_palegreen);
        Button4.setBackgroundResource(R.drawable.rounded_shape_grey);
        Button5.setBackgroundResource(R.drawable.rounded_shape_grey);
        Button3.setBackgroundResource(R.drawable.rounded_shape_grey);
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
