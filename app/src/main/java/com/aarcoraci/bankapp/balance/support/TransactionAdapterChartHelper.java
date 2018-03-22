package com.aarcoraci.bankapp.balance.support;

import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;

import java.util.ArrayList;

/**
 * Created by angel on 3/22/2018.
 */

public abstract class TransactionAdapterChartHelper {

    /**
     * Builds the pie data object that represents the impact of a transaction againts the
     * total of the type of transaction
     * @param transactionAmount amount of the transaction
     * @param total total (income or expend)
     * @return pie data to be added to the chart
     */
    public static PieData buildTransactionPieData(float transactionAmount, float total, int transactionColor, int totalColor){
        ArrayList<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(transactionAmount));
        entries.add(new PieEntry(total));

        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setDrawIcons(false);
        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(transactionColor);
        colors.add(totalColor);
        dataSet.setColors(colors);

        PieData data = new PieData(dataSet);
        data.setDrawValues(false);
        data.setHighlightEnabled(false);
        return data;
    }
}
