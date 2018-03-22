package com.aarcoraci.bankapp.balance;

import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.aarcoraci.bankapp.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class BalanceActivity extends AppCompatActivity {

    private LineChart balanceChart;
    private List<Entry> chartData = new ArrayList<>();

    private RecyclerView recyclerView;
    private BalanceMonthAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_balance);

        balanceChart = findViewById(R.id.balanceChart);

        setRecyclerView();
    }

    @Override
    protected void onStart() {
        super.onStart();


        // simulate data fetch, needed to get the height on the gradient shader
        balanceChart.post(new Runnable() {
            @Override
            public void run() {
                initChart();
            }
        });

        // scroll to current month
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        int month = cal.get(Calendar.MONTH);
        recyclerView.scrollToPosition(month);
        adapter.selectPosition(month);
    }

    private void setRecyclerView(){
        recyclerView = findViewById(R.id.monthRecyclerView);

        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

        recyclerView.setLayoutManager(layoutManager);
        DateFormatSymbols dfs = new DateFormatSymbols();

        adapter = new BalanceMonthAdapter(dfs.getShortMonths(), this);
        recyclerView.setAdapter(adapter);

    }

    private void initChart(){
        // prepare chart data
        chartData.clear();
        Random r = new Random();

        for (int i = 0; i < 11; i++) {
            chartData.add(new Entry(i, r.nextInt(20)));
        }

        LineDataSet lineDataSet = new LineDataSet(chartData, "");
        lineDataSet.setColor(Color.RED);
        lineDataSet.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);
        lineDataSet.setDrawValues(false);
        lineDataSet.setDrawCircles(false);
        lineDataSet.setLineWidth(4f);

        LineData lineData = new LineData((lineDataSet));
        lineData.setDrawValues(false);

        // general
        balanceChart.getDescription().setEnabled(false);
        balanceChart.setDrawBorders(false);
        balanceChart.setDrawGridBackground(false);
        // legend
        balanceChart.getLegend().setEnabled(false);
        // axis
        balanceChart.getXAxis().setEnabled(false);
        balanceChart.getAxisLeft().setEnabled(false);
        balanceChart.getAxisRight().setEnabled(false);

        //margins and padding
        balanceChart.setExtraOffsets(0, 15, 0, 15);
        balanceChart.setViewPortOffsets(0, 0, 0, 0);

        //extra effects
        int shadowColor = ContextCompat.getColor(BalanceActivity.this, R.color.green);
        int transparent = Color.argb(100, Color.red(shadowColor), Color.green(shadowColor), Color.blue(shadowColor));

        Paint paintShadow = balanceChart.getRenderer().getPaintRender();
        paintShadow.setShadowLayer(20, 0, 15, transparent);

        Paint paint = balanceChart.getRenderer().getPaintRender();
        int height = balanceChart.getHeight();
        LinearGradient linGrad = new LinearGradient(0, 0, 0, height,
                getResources().getColor(R.color.light_green),
                getResources().getColor(R.color.aqua),
                Shader.TileMode.REPEAT);
        paint.setShader(linGrad);

        balanceChart.invalidate();
        balanceChart.setData(lineData);
    }


}
