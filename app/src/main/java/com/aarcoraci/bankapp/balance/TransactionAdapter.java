package com.aarcoraci.bankapp.balance;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.aarcoraci.bankapp.R;
import com.aarcoraci.bankapp.domain.Transaction;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by angel on 3/22/2018.
 */

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.ViewHolder> {

    private List<Transaction> transactionList = new ArrayList<>();
    private NumberFormat currencyFormat;
    private int incomeTextColor = 0;
    private int expendTextColor = 0;

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView amountTextView;
        public TextView descriptionTextView;
        public ImageView transactionImageView;

        public ViewHolder(View itemView) {
            super(itemView);
            amountTextView = itemView.findViewById(R.id.amountTextView);
            descriptionTextView = itemView.findViewById(R.id.descriptionTextView);
            transactionImageView = itemView.findViewById(R.id.transactionImage);
        }
    }

    public TransactionAdapter(List<Transaction> transactions, NumberFormat currencyFormat, Context context) {
        this.transactionList = transactions;
        this.incomeTextColor = context.getResources().getColor(R.color.light_green);
        this.expendTextColor = context.getResources().getColor(R.color.teal);
        this.currencyFormat = currencyFormat;
    }

    @Override
    public TransactionAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.transaction_item, parent, false);

        TransactionAdapter.ViewHolder vh = new TransactionAdapter.ViewHolder(itemView);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(TransactionAdapter.ViewHolder holder, int position) {

        Transaction transaction = transactionList.get(position);
        holder.amountTextView.setText(currencyFormat.format(transaction.amount));
        if (transaction.amount > 0) {
            holder.amountTextView.setTextColor(incomeTextColor);
        } else {
            holder.amountTextView.setTextColor(expendTextColor);
        }
    }

    @Override
    public int getItemCount() {
        return transactionList.size();
    }
}
