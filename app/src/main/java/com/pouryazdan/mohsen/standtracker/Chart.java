package com.pouryazdan.mohsen.standtracker;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;
import java.util.List;

public class Chart extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);

        DbHandler db = new DbHandler(this);
        List<StandData> list = db.getAllStandDate();

        BarChart chart = (BarChart) findViewById(R.id.chart);
        List<BarEntry> bardata = new ArrayList<>();

        ArrayList<String> labels = new ArrayList<String>();

        for(int i = 0; i < list.size(); i ++) {
            BarEntry b = new BarEntry(i, list.get(i).get_count());
            bardata.add(b);
            labels.add(list.get(i).get_date());
        }

        BarDataSet dataSet = new BarDataSet(bardata, "Stands per day");
        BarData data = new BarData();

        chart.setData(data);

    }
}
