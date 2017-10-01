package com.example.rgain.expenses;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.example.rgain.expenses.Adapters.ExpenseItemsAdapter;
import com.example.rgain.expenses.Adapters.ExpenseNameAdapter;
import com.example.rgain.expenses.Models.Expense;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class activity_category extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private ExpenseItemsAdapter mAdapter;
    private Context mContext;
    private DatabaseHandler db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Intent i = getIntent();
        final String category = i.getStringExtra("category");
        setTitle(category.toUpperCase());
        TextView heading = (TextView) findViewById(R.id.category_name);
        heading.setText(category.toUpperCase());

        mContext=this;
        db = new DatabaseHandler(this);
        mRecyclerView=(RecyclerView) findViewById(R.id.expense_item_recycler_view) ;
        mLinearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        showListData(category);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_add_expense_item);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = getLayoutInflater();
                View alertLayout = inflater.inflate(R.layout.new_expense_in_category_adder, null);
                final EditText expenseDescriptionEditText = (EditText) alertLayout.findViewById(R.id.expense_description);
                final EditText expenseAmountEditText = (EditText) alertLayout.findViewById(R.id.expense_amount);
                final DatePicker datePicker = (DatePicker) alertLayout.findViewById(R.id.datePicker);

                AlertDialog.Builder alert = new AlertDialog.Builder(mContext);
                alert.setView(alertLayout);
                alert.setPositiveButton("Submit", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String expenseDescription = expenseDescriptionEditText.getText().toString();
                        int expenseAmount =Integer.parseInt(expenseAmountEditText.getText().toString());
                        int day = datePicker.getDayOfMonth();
                        int month = datePicker.getMonth();
                        int year = datePicker.getYear();
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(year, month, day);
                        Date date = calendar.getTime();
                        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-YYYY");
                        String dateString = sdf.format(date);
//                        String date = String.valueOf(day)+"/"+String.valueOf(month)+"/"+String.valueOf(year);
                        Log.v("test", dateString);
                        Expense to_add = new Expense(expenseAmount,category,dateString,expenseDescription);
                        db.addExpense(to_add);
                        showListData(category);

//                        db.addExpenseName(expenseName);
//                        showListData();

                    }
                });

                AlertDialog dialog = alert.create();
                dialog.show();
            }
        });

    }

    public void showListData(String name) {
        ArrayList<Expense> allExpenseList = db.getAllExpenses();

        ArrayList<Expense> to_send = new ArrayList<>();
        for (int j = 0; j < allExpenseList.size(); j++) {
            Log.v("test",allExpenseList.get(j).getCategory() + " compare with "+name);
            if (allExpenseList.get(j).getCategory().equals(name)) {
                to_send.add(allExpenseList.get(j));
                Log.v("test","found");
            }
        }


        mAdapter = new ExpenseItemsAdapter(to_send, mContext);
        mRecyclerView.setAdapter(mAdapter);

    }
}
