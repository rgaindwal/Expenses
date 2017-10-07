package com.example.rgain.expenses;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.design.widget.FloatingActionButton;
import android.support.transition.TransitionManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.rgain.expenses.Adapters.ExpenseNameAdapter;
import com.example.rgain.expenses.Models.Expense;
import com.example.rgain.expenses.Models.ExpenseNameModel;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private ExpenseNameAdapter mAdapter;
    public static final String MyPREFERENCES = "expensePrefs" ;

    SharedPreferences sharedpreferences;
    FloatingActionButton fabFake;
    private Context mContext;
    private DatabaseHandler db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        Boolean isRunOnce= sharedpreferences.getBoolean("runMainActivityOnce",false);
        if(!isRunOnce)
            showOverlay(this);


        mContext=this;
        db = new DatabaseHandler(this);
        mRecyclerView=(RecyclerView) findViewById(R.id.expense_recycler_view) ;
        mLinearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        showListData();
        //TODO : Make the empty_expenses Invisible after adding the first element and make the expense_recycler_view Visible.


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_add_expense_name);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = getLayoutInflater();
                View alertLayout = inflater.inflate(R.layout.new_expense_adder_dialog, null);
                final EditText expenseNameEditText = (EditText) alertLayout.findViewById(R.id.expense_name_adder);
                AlertDialog.Builder alert = new AlertDialog.Builder(mContext);
                alert.setView(alertLayout);
                alert.setPositiveButton("Submit", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String expenseName = expenseNameEditText.getText().toString();
                        db.addExpenseName(expenseName);
                        showListData();

                    }
                });

                AlertDialog dialog = alert.create();
                dialog.show();
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        showListData();
    }

    public void showListData()
    {

        ArrayList<String> expenseList = db.getAllExpensesNames();
        ArrayList<Expense> allExpenseList = db.getAllExpenses();
        int sumOfExpense;
        ArrayList<ExpenseNameModel> to_send = new ArrayList<>();
        for(int i=0;i<expenseList.size();i++)
        {
            String expenseName= expenseList.get(i);
            sumOfExpense=0;
            for(int j=0; j<allExpenseList.size();j++)
            {
                if (allExpenseList.get(j).getCategory().equals(expenseName))
                {
                    sumOfExpense+= allExpenseList.get(j).getAmount();
                }
            }
            to_send.add(new ExpenseNameModel(expenseName,sumOfExpense));

        }

        mAdapter = new ExpenseNameAdapter(to_send,mContext);
        mRecyclerView.setAdapter(mAdapter);
        TextView emptyExpenseTextView=(TextView)findViewById(R.id.empty_expenses);
//        if(to_send.size()>0)
        emptyExpenseTextView.setVisibility(View.INVISIBLE);
    }

    public void showOverlay(Context ctx) {
        final Dialog dialog = new Dialog(ctx, android.R.style.Theme_Translucent_NoTitleBar);
        dialog.setContentView(R.layout.first_screen_overlay);

        LinearLayout relativeLayout = (LinearLayout) dialog.findViewById(R.id.first_screen_linear_layout);
        fabFake = (FloatingActionButton) dialog.findViewById(R.id.fab_fake);
        fabFake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                LayoutInflater inflater = getLayoutInflater();
                View alertLayout = inflater.inflate(R.layout.new_expense_adder_dialog, null);
                final EditText expenseNameEditText = (EditText) alertLayout.findViewById(R.id.expense_name_adder);
                AlertDialog.Builder alert = new AlertDialog.Builder(mContext);
                alert.setView(alertLayout);
                alert.setPositiveButton("Submit", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String expenseName = expenseNameEditText.getText().toString();
                        db.addExpenseName(expenseName);
                        showListData();

                    }
                });

                AlertDialog dialog = alert.create();
                dialog.show();
            }
        });

        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View arg0) {

                dialog.dismiss();

            }
        });
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putBoolean("runMainActivityOnce",true);
        editor.commit();


        dialog.show();
    }
}
