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
import android.widget.TextView;

import com.aarcoraci.bankapp.R;
import com.aarcoraci.bankapp.data.TransactionStore;
import com.aarcoraci.bankapp.domain.Transaction;
import com.aarcoraci.bankapp.utils.Utils;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.text.DateFormatSymbols;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.util.AppendOnlyLinkedArrayList;
import io.reactivex.schedulers.Schedulers;

public class BalanceActivity extends AppCompatActivity {

    // chart settings and data
    private final int CHART_ANIMATION_DURATION = 400;
    private LineChart balanceChart;
    private List<Entry> chartData = new ArrayList<>();
    LineDataSet lineDataSet;

    private RecyclerView monthRecyclerView;
    private BalanceMonthAdapter adapter;

    // display data
    float totalIncome = 0f;
    float totalExpend = 0f;

    // ui
    private NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();
    private TextView balanceTextView;
    private TextView totalIncomeTextView;
    private TextView totalExpendTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_balance);

        balanceChart = findViewById(R.id.balanceChart);
        balanceTextView = findViewById(R.id.balanceTextView);
        totalExpendTextView = findViewById(R.id.totalExpendTextView);
        totalIncomeTextView = findViewById(R.id.totalIncomeTextView);

        setRecyclerView();
    }

    @Override
    protected void onStart() {
        super.onStart();

        // scroll to current month
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        final int month = cal.get(Calendar.MONTH);
        monthRecyclerView.scrollToPosition(month);
        adapter.selectPosition(month);

        // configure chart elements
        lineDataSet = new LineDataSet(chartData, "");
        lineDataSet.setColor(Color.RED);
        lineDataSet.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);
        lineDataSet.setDrawValues(false);
        lineDataSet.setDrawCircles(false);
        lineDataSet.setLineWidth(4f);

        // simulate data fetch, needed to get the height on the gradient shader
        balanceChart.post(new Runnable() {
            @Override
            public void run() {
                // configure chart look and feel
                configureChartUI();

                // first time draw
                drawChart(month);
            }
        });

    }

    private void setRecyclerView() {
        monthRecyclerView = findViewById(R.id.monthRecyclerView);

        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

        monthRecyclerView.setLayoutManager(layoutManager);
        DateFormatSymbols dfs = new DateFormatSymbols();

        adapter = new BalanceMonthAdapter(dfs.getShortMonths(), new BalanceMonthAdapter.OnMonthClickListener() {
            @Override
            public void onMonthClick(int position) {
                drawChart(position);
            }
        }, this);
        monthRecyclerView.setAdapter(adapter);

    }

    /**
     * configures the chart and effects to match the design
     */
    private void configureChartUI() {

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
        float offset = Utils.convertPixelsToDp(this.getResources().getDisplayMetrics(), 15);
        balanceChart.setExtraOffsets(0, offset, 0, offset);
        balanceChart.setViewPortOffsets(0, 0, 0, 0);

        //extra effects
        int shadowColor = ContextCompat.getColor(BalanceActivity.this, R.color.green);
        int transparent = Color.argb(100, Color.red(shadowColor), Color.green(shadowColor), Color.blue(shadowColor));

        float radius = Utils.convertPixelsToDp(this.getResources().getDisplayMetrics(), 30);
        float dy = Utils.convertPixelsToDp(this.getResources().getDisplayMetrics(), 40);
        Paint paintShadow = balanceChart.getRenderer().getPaintRender();
        paintShadow.setShadowLayer(radius, 0, dy, transparent);

        Paint paint = balanceChart.getRenderer().getPaintRender();
        int height = balanceChart.getHeight();
        LinearGradient linGrad = new LinearGradient(0, 0, 0, height,
                getResources().getColor(R.color.light_green),
                getResources().getColor(R.color.aqua),
                Shader.TileMode.REPEAT);
        paint.setShader(linGrad);
    }

    /***
     * plots the transactions of a given month
     * @param month month number
     */
    private void drawChart(final int month) {

        TransactionStore.getInstance().getTransactions()
                .subscribeOn(Schedulers.io())
                .filter(new AppendOnlyLinkedArrayList.NonThrowingPredicate<Transaction>() {

                    /**
                     * Filter elements for the desired month
                     */
                    Calendar calendar = Calendar.getInstance();

                    @Override
                    public boolean test(Transaction transaction) {
                        calendar.setTime(transaction.date);
                        return calendar.get(Calendar.MONTH) == month;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Transaction>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        // reset values
                        totalIncome = 0f;
                        totalExpend = 0f;
                        chartData.clear();
                    }

                    @Override
                    public void onNext(Transaction transaction) {
                        if (transaction.amount > 0)
                            totalIncome += transaction.amount;
                        else
                            totalExpend -= transaction.amount;

                        chartData.add(new Entry(transaction.date.getTime(), transaction.amount));
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                        totalExpendTextView.setText(currencyFormat.format(totalExpend));
                        totalIncomeTextView.setText(currencyFormat.format(totalIncome));
                        balanceTextView.setText(currencyFormat.format(totalIncome - totalExpend));


                        lineDataSet.notifyDataSetChanged();
                        LineData lineData = new LineData((lineDataSet));
                        lineData.setDrawValues(false);
                        lineData.setHighlightEnabled(false);

                        balanceChart.setData(lineData);
                        balanceChart.animateY(CHART_ANIMATION_DURATION);
                        balanceChart.invalidate();
                    }
                });

    }
}
