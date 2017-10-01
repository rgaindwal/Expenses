package com.example.rgain.expenses.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.rgain.expenses.DatabaseHandler;
import com.example.rgain.expenses.Models.Expense;
import com.example.rgain.expenses.R;
import com.example.rgain.expenses.activity_category;

import java.util.ArrayList;

/**
 * Created by Daljit on 01-10-2017.
 */

public class ExpenseItemsAdapter extends RecyclerView.Adapter<ExpenseItemsAdapter.ExpenseItemHolder> {
private ArrayList<Expense> mExpensesToShow;
private Context mContext;
public ExpenseItemsAdapter(ArrayList<Expense> expense,Context context)
        {
        mExpensesToShow=expense;
        mContext=context;
        }
@Override
public ExpenseItemsAdapter.ExpenseItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflatedView = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.expense_list_model, parent, false);

        final ExpenseItemHolder holder = new ExpenseItemHolder(inflatedView,mContext,mExpensesToShow);
    inflatedView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Log.d("RecyclerView", "CLICK!"+ String.valueOf(holder.getAdapterPosition()));
            Intent i = new Intent(mContext,activity_category.class);
            i.putExtra("id", mExpensesToShow.get(holder.getAdapterPosition()).getId());
            mContext.startActivity(i);
        }
    });

    holder.mDeleteIcon.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int pos= holder.getAdapterPosition();
            DatabaseHandler db = new DatabaseHandler(mContext);
            db.deleteExpense(mExpensesToShow.get(pos));
            mExpensesToShow.remove(pos);
            notifyItemRemoved(pos);
//                notifyDataSetChanged();
//                ((MainActivity)mContext).showListData();
        }
    });
        return holder;
        }

@Override
public void onBindViewHolder(ExpenseItemsAdapter.ExpenseItemHolder holder, int position) {
        holder.bindExpense(mExpensesToShow.get(position));
        }

@Override
public int getItemCount() {
        return mExpensesToShow.size();
        }
public static class ExpenseItemHolder extends RecyclerView.ViewHolder  {
    //2
    private ImageView mDeleteIcon;
    private TextView mName;
    private TextView mAmount;
    private Context mContext;
    private ArrayList<Expense> mList;
    private Expense mExpense;

    //4
    public ExpenseItemHolder(View v, Context context, ArrayList<Expense> list) {
        super(v);
        mContext = context;
        mList = list;
        mName = (TextView) v.findViewById(R.id.expense_item_name);
        mAmount = (TextView) v.findViewById(R.id.expense_item_cost);
        mDeleteIcon = (ImageView) v.findViewById(R.id.remove_list_item) ;

    }

    //5


    public void bindExpense(Expense expense) {
        mExpense = expense;
        Log.v("test", mExpense.getDescription() + mExpense.getAmount());

        mName.setText(mExpense.getDescription());
        mAmount.setText(String.valueOf(mExpense.getAmount()));
    }
}
}
