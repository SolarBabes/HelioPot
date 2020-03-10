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
import com.github.mikephil.charting.components.YAxis;
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
    private static float[] yMin = {0,0,0,0};
    private static float[] yMax = {40, 1000, 100, 1000};
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
    private int mode = 4;
    private int[] timeLength = {172800,86400,43200};
    private Button Button3;
    private Button Button4;
    private Button Button5;
    private Button Button6;
    private ArrayList<Entry> temperatureVal = new ArrayList<Entry>();
    private ArrayList<Entry> moistureVal = new ArrayList<Entry>();
    private ArrayList<Entry> humidityVal = new ArrayList<Entry>();
    private ArrayList<Entry> lightVal = new ArrayList<Entry>();
    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss ddMMM");
    private DatabaseReference mDatabase;

    private int GRAPH_NO = 4;
    ValueEventListener Listener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            if (mode!=4){
                return;
            }

            Log.d("TEST", dataSnapshot.getRef().toString());
            for (int i = 0; i < GRAPH_NO; i++) {
                Map<String, Long> map = (Map<String, Long>) dataSnapshot.child(allMeasurements[i]).getValue();
                ArrayList<Entry> dataVals = new ArrayList<Entry>();
                for (String s:map.keySet()){
                    dataVals.add(new Entry(Long.parseLong(s)-1580000000L,map.get(s)));
                }
                Collections.sort(dataVals,c);
                if (i==0){
                    temperatureVal= new ArrayList<Entry>(dataVals);
                }else if(i==1){
                    moistureVal= new ArrayList<Entry>(dataVals);
                }else if(i==2){
                    humidityVal= new ArrayList<Entry>(dataVals);
                }else{
                    lightVal= new ArrayList<Entry>(dataVals);
                }
                ArrayList<Entry> dataValsNew = new ArrayList<Entry>(dataVals);
                while (dataValsNew.size()>31){
                    dataValsNew.remove(0);
                }
                ArrayList<ILineDataSet> dataSets = new ArrayList<>();
                LineDataSet lineDataSet = new LineDataSet(dataValsNew,allMeasurements[i]);
                lineDataSet.setLineWidth(3);
                lineDataSet.setDrawValues(false);
                dataSets.add(lineDataSet);
                mpLineChart[i].setData(new LineData(dataSets));
//                limit line for light graph
//                if (i==3){
//                    ArrayList<ILineDataSet> dataSets1 = new ArrayList<>();
//                    LineDataSet lineDataSet1 = new LineDataSet(dataVals,allMeasurements[i]);
//                    lineDataSet1.setLineWidth(3);
//                    lineDataSet1.setDrawValues(false);
//                    dataSets1.add(lineDataSet);
//                    mpLineChart[i].setData(new LineData(dataSets1));
//                }
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
        for (int i = 0; i < GRAPH_NO; i++) {
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

            YAxis yAxisL = mpLineChart[i].getAxisLeft();
            yAxisL.setAxisMaximum(yMax[i]);
            yAxisL.setAxisMinimum(yMin[i]);

            YAxis yAxisR = mpLineChart[i].getAxisRight();
            yAxisR.setAxisMaximum(yMax[i]);
            yAxisR.setAxisMinimum(yMin[i]);


        }
//        mpLineChart[0].setTouchEnabled(false);
//        mpLineChart[1].setScaleEnabled(false);
//        mpLineChart[0].setXAxisRenderer();

        mDatabase.addValueEventListener(Listener);

        for (int i = 0; i < GRAPH_NO; i++) {
            ArrayList<Entry> dataVals = new ArrayList<Entry>();
            dataVals.add(new Entry(0,0));
            ArrayList<ILineDataSet> dataSets = new ArrayList<>();
            LineDataSet lineDataSet = new LineDataSet(dataVals,allMeasurements[i]);
            lineDataSet.setLineWidth(3);
            lineDataSet.setDrawValues(false);
            dataSets.add(lineDataSet);
            mpLineChart[i].setData(new LineData(dataSets));
            mpLineChart[i].invalidate();
        }
    }

    public void onClick3(View view){
        mode=1;
        Button3.setBackgroundResource(R.drawable.rounded_shape_palegreen);
        Button4.setBackgroundResource(R.drawable.rounded_shape_grey);
        Button5.setBackgroundResource(R.drawable.rounded_shape_grey);
        Button6.setBackgroundResource(R.drawable.rounded_shape_grey);
        Long currentTime = System.currentTimeMillis()/1000;
        Long firstTime = currentTime-timeLength[0];
        for (int i = 0; i < GRAPH_NO; i++) {
            ArrayList<Entry> dataValsNew;
            if (i==0){
                dataValsNew = new ArrayList<Entry>(temperatureVal);
            }else if(i==1){
                dataValsNew = new ArrayList<Entry>(moistureVal);
            }else if(i==2){
                dataValsNew = new ArrayList<Entry>(humidityVal);
            }else{
                dataValsNew = new ArrayList<Entry>(lightVal);
            }
            while (dataValsNew.size()>0 && dataValsNew.get(0).getX()+1580000000L<firstTime){
                dataValsNew.remove(0);
            }
            double interval = timeLength[0]/50;
            ArrayList<Entry> finalVal = new ArrayList<Entry>();
            int n = 0;
            for (Entry j : dataValsNew){
                if (j.getX()>=n*interval){
                    finalVal.add(j);
                    n++;
                    if (n==51){
                        break;
                    }
                }

            }
            ArrayList<ILineDataSet> dataSets = new ArrayList<>();
            LineDataSet lineDataSet = new LineDataSet(finalVal,allMeasurements[i]);
            lineDataSet.setLineWidth(3);
            lineDataSet.setDrawValues(false);
            dataSets.add(lineDataSet);
            mpLineChart[i].setData(new LineData(dataSets));
            mpLineChart[i].invalidate();
        }
    }

    public void onClick4(View view){
        mode=2;
        Button4.setBackgroundResource(R.drawable.rounded_shape_palegreen);
        Button3.setBackgroundResource(R.drawable.rounded_shape_grey);
        Button5.setBackgroundResource(R.drawable.rounded_shape_grey);
        Button6.setBackgroundResource(R.drawable.rounded_shape_grey);
        Long firstTime = System.currentTimeMillis()/1000-timeLength[1];
        for (int i = 0; i < GRAPH_NO; i++) {
            ArrayList<Entry> dataValsNew;
            if (i==0){
                dataValsNew = new ArrayList<Entry>(temperatureVal);
            }else if(i==1){
                dataValsNew = new ArrayList<Entry>(moistureVal);
            }else if(i==2){
                dataValsNew = new ArrayList<Entry>(humidityVal);
            }else{
                dataValsNew = new ArrayList<Entry>(lightVal);
            }
            while (dataValsNew.size()>0 && dataValsNew.get(0).getX()+1580000000L<firstTime){
                dataValsNew.remove(0);
            }
            double interval = dataValsNew.size()/30;
            ArrayList<Entry> finalVal = new ArrayList<Entry>();
            int n = 0;
            for (Entry j : dataValsNew){
                if (j.getX()>=n*interval){
                    finalVal.add(j);
                    n++;
                    if (n==51){
                        break;
                    }
                }

            }
            ArrayList<ILineDataSet> dataSets = new ArrayList<>();
            LineDataSet lineDataSet = new LineDataSet(finalVal,allMeasurements[i]);
            lineDataSet.setLineWidth(3);
            lineDataSet.setDrawValues(false);
            dataSets.add(lineDataSet);
            mpLineChart[i].setData(new LineData(dataSets));
            mpLineChart[i].invalidate();
        }
    }

    public void onClick5(View view){
        mode=3;
        Button5.setBackgroundResource(R.drawable.rounded_shape_palegreen);
        Button4.setBackgroundResource(R.drawable.rounded_shape_grey);
        Button3.setBackgroundResource(R.drawable.rounded_shape_grey);
        Button6.setBackgroundResource(R.drawable.rounded_shape_grey);
        Long firstTime = System.currentTimeMillis()/1000-timeLength[2];
        for (int i = 0; i < GRAPH_NO; i++) {
            ArrayList<Entry> dataValsNew;
            if (i==0){
                dataValsNew = new ArrayList<Entry>(temperatureVal);
            }else if(i==1){
                dataValsNew = new ArrayList<Entry>(moistureVal);
            }else if(i==2){
                dataValsNew = new ArrayList<Entry>(humidityVal);
            }else{
                dataValsNew = new ArrayList<Entry>(lightVal);
            }
            while (dataValsNew.size()>0 && dataValsNew.get(0).getX()+1580000000L<firstTime){
                dataValsNew.remove(0);
            }
            double interval = dataValsNew.size()/30;
            ArrayList<Entry> finalVal = new ArrayList<Entry>();
            int n = 0;
            for (Entry j : dataValsNew){
                if (j.getX()>=n*interval){
                    finalVal.add(j);
                    n++;
                    if (n==51){
                        break;
                    }
                }

            }
            ArrayList<ILineDataSet> dataSets = new ArrayList<>();
            LineDataSet lineDataSet = new LineDataSet(finalVal,allMeasurements[i]);
            lineDataSet.setLineWidth(3);
            lineDataSet.setDrawValues(false);
            dataSets.add(lineDataSet);
            mpLineChart[i].setData(new LineData(dataSets));
            mpLineChart[i].invalidate();
        }
    }

    public void onClick6(View view){
        mode=4;
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
