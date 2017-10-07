package com.example.rgain.expenses.Adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.rgain.expenses.DatabaseHandler;
import com.example.rgain.expenses.DetailedViewActivity;
import com.example.rgain.expenses.Models.Expense;
import com.example.rgain.expenses.R;

import java.util.ArrayList;

import static android.R.attr.id;

/**
 * Created by Daljit on 02-10-2017.
 */

public class DescriptionItemAdapter  extends RecyclerView.Adapter<DescriptionItemAdapter.DescriptionItemHolder>  {
    private ArrayList<Expense> mList;
    private Context mContext;
    private String description;

    public DescriptionItemAdapter(ArrayList<Expense> list, Context context,String name)
    {
        mList=list;
        mContext=context;
        description=name;
    }
    @Override
    public DescriptionItemAdapter.DescriptionItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflatedView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.expense_list_model, parent, false);
        final DescriptionItemHolder holder = new DescriptionItemHolder(inflatedView);
        holder.mDeleteIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int pos= holder.getAdapterPosition();
                DatabaseHandler db = new DatabaseHandler(mContext);
                db.deleteExpense(mList.get(pos));
                mList.remove(pos);
                notifyItemRemoved(pos);
                ((DetailedViewActivity)mContext).showListData(description);


            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(DescriptionItemAdapter.DescriptionItemHolder holder, int position) {
        Expense to_send = mList.get(position);
        holder.bindItem(to_send);

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }


    //1
    public static class DescriptionItemHolder extends RecyclerView.ViewHolder {
        //2
        private TextView mItemDate;
        private TextView mItemPrice;
        private ImageView mDeleteIcon;


        //3

        //4
        public DescriptionItemHolder(View v) {
            super(v);

            mItemDate = (TextView) v.findViewById(R.id.expense_item_name);
            mItemPrice = (TextView) v.findViewById(R.id.expense_item_cost);
            mDeleteIcon=(ImageView) v.findViewById(R.id.remove_list_item);
        }

        public void bindItem(Expense expense) {
            mItemDate.setText(expense.getDate());
            mItemPrice.setText("₹ " +String.valueOf(expense.getAmount()));
        }

    }

}
