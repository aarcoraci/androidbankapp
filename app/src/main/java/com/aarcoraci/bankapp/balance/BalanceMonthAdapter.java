package com.aarcoraci.bankapp.balance;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aarcoraci.bankapp.R;

/**
 * Created by angel on 3/21/2018.
 */

public class BalanceMonthAdapter extends RecyclerView.Adapter<BalanceMonthAdapter.ViewHolder> {

    private String[] months;
    private int selectedIndex = RecyclerView.NO_POSITION;
    private int defaultTextColor = 0;
    private int selectedTextColor = 0;
    private OnMonthClickListener onMonthClickListener;

    // interactions
    public interface OnMonthClickListener {
        void onMonthClick(int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView monthNameTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            monthNameTextView = itemView.findViewById(R.id.month_name);

            monthNameTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    notifyItemChanged(selectedIndex);
                    selectedIndex = getAdapterPosition();
                    notifyItemChanged(selectedIndex);

                    onMonthClickListener.onMonthClick(selectedIndex);
                }
            });
        }

        public void setSelected() {
            monthNameTextView.setTextColor(selectedTextColor);
        }
    }

    public BalanceMonthAdapter(String[] months, OnMonthClickListener onMonthClickListener, Context context) {
        this.months = months;
        this.onMonthClickListener = onMonthClickListener;
        this.defaultTextColor = context.getResources().getColor(R.color.text_dark);
        this.selectedTextColor = context.getResources().getColor(R.color.teal);
    }

    public void selectPosition(int position) {
        notifyItemChanged(selectedIndex);
        selectedIndex = position;
        notifyItemChanged(selectedIndex);
    }

    @Override
    public BalanceMonthAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.balancemonth_item, parent, false);

        ViewHolder vh = new ViewHolder(itemView);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.monthNameTextView.setText(months[position]);
        holder.monthNameTextView.setTextColor(defaultTextColor);

        if (position == selectedIndex)
            holder.setSelected();

    }

    @Override
    public int getItemCount() {
        return months.length;
    }
}
