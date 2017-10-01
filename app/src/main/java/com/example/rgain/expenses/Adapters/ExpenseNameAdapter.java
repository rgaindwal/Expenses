package com.example.rgain.expenses.Adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.rgain.expenses.DatabaseHandler;
import com.example.rgain.expenses.MainActivity;
import com.example.rgain.expenses.Models.ExpenseNameModel;
import com.example.rgain.expenses.R;
import com.example.rgain.expenses.activity_category;

import java.util.ArrayList;

/**
 * Created by Gaindu on 01-10-2017.
 */

public class ExpenseNameAdapter extends RecyclerView.Adapter<ExpenseNameAdapter.ExpenseHolder> {
    private ArrayList<ExpenseNameModel> mExpensenamesList;
    private Context mContext;

    public ExpenseNameAdapter(ArrayList<ExpenseNameModel> list,Context context)

    {
        mContext=context;
        mExpensenamesList=list;
    }
    @Override
    public ExpenseNameAdapter.ExpenseHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflatedView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.expense_list_model, parent, false);
        final ExpenseHolder holder =  new ExpenseHolder(inflatedView,mContext,mExpensenamesList);

        inflatedView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("RecyclerView", "CLICK!"+ String.valueOf(holder.getAdapterPosition()));
                Intent i = new Intent(mContext,activity_category.class);
                i.putExtra("category", mExpensenamesList.get(holder.getAdapterPosition()).name);
                mContext.startActivity(i);
            }
        });

        holder.mDeleteIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos= holder.getAdapterPosition();
                String nameToDelete= mExpensenamesList.get(pos).name;
                DatabaseHandler db = new DatabaseHandler(mContext);
                db.deleteExpenseNameandExpenses(nameToDelete);
                mExpensenamesList.remove(pos);
                notifyItemRemoved(pos);
//                notifyDataSetChanged();
//                ((MainActivity)mContext).showListData();
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ExpenseNameAdapter.ExpenseHolder holder, int position) {
    ExpenseNameModel to_bind = mExpensenamesList.get(position);
        holder.bindExpense(to_bind);
    }

    @Override
    public int getItemCount() {
        return mExpensenamesList.size();
    }

    //1
    public static class ExpenseHolder extends RecyclerView.ViewHolder  {
        //2
        private ImageView mDeleteIcon;
        private TextView mName;
        private TextView mAmount;
        private Context mContext;
        private ExpenseNameModel mExpense;
        private ArrayList<ExpenseNameModel> mList;

        //3
        private static final String PHOTO_KEY = "PHOTO";

        //4
        public ExpenseHolder(View v,Context context,ArrayList<ExpenseNameModel> list) {
            super(v);
            mContext=context;
            mList=list;
            mName = (TextView) v.findViewById(R.id.expense_item_name);
            mAmount = (TextView) v.findViewById(R.id.expense_item_cost);
            mDeleteIcon = (ImageView) v.findViewById(R.id.remove_list_item) ;

//            v.setOnClickListener(this);
//            v.setOnLongClickListener(new View.OnLongClickListener() {
//                @Override
//                public boolean onLongClick(View v) {
//                    final CharSequence[] items = {"Delete"};
//
//                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
//
//                    builder.setTitle("Select The Action");
//                    builder.setItems(items, new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int item) {
//                            int pos= getAdapterPosition();
//                            String nameToDelete= mList.get(pos).name;
//                            DatabaseHandler db = new DatabaseHandler(mContext);
//                            db.deleteExpenseNameandExpenses(nameToDelete);
//                            ((MainActivity)mContext).showListData();
//
//
//                        }
//                    });
//                    builder.show();
//                    return true;
//                }
//            });
        }

        //5


        public void bindExpense(ExpenseNameModel expenseName) {
            mExpense = expenseName;
            Log.v("test",mExpense.name+mExpense.amount);

            mName.setText(mExpense.name);
            mAmount.setText(String.valueOf(mExpense.amount));
        }
    }
}
