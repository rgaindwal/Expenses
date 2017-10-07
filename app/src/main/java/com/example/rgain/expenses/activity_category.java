package com.example.rgain.expenses;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.rgain.expenses.Adapters.ExpenseItemsAdapter;
import com.example.rgain.expenses.Adapters.ExpenseNameAdapter;
import com.example.rgain.expenses.Models.Expense;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static android.R.attr.key;
import static android.R.attr.name;
import static android.R.attr.value;

public class activity_category extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private ExpenseItemsAdapter mAdapter;
    private Context mContext;
    public static final String MyPREFERENCES = "expensePrefs" ;
    SharedPreferences sharedpreferences;

    private DatabaseHandler db;
    public String categoryname;

    @Override
    protected void onResume() {
        super.onResume();
        showListData(categoryname);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
//        getSupportActionBar().hide();
        setContentView(R.layout.activity_category);


//        getSupportActionBar().setDisplayShowHomeEnabled(true);

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        Boolean isRunOnce= sharedpreferences.getBoolean("runActivityCategoryOnce",false);
        if(!isRunOnce)
            showOverlay(this);


        Intent i = getIntent();
        final String category = i.getStringExtra("category");
        categoryname=category;
//        setTitle(category.toUpperCase());
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



        mergeList(allExpenseList,name);

    }

    public void mergeList(ArrayList<Expense> to_edit,String name)
    {
        HashMap<String,Integer> map=new HashMap<String,Integer>();
        ArrayList<Expense> to_send = new ArrayList<>();
        for (int j = 0; j < to_edit.size(); j++) {
            String item = to_edit.get(j).getDescription();
            if (to_edit.get(j).getCategory().equals(name)) {
                int cost = to_edit.get(j).getAmount();

                if (!map.containsKey(item))
                    map.put(item, cost);
                else {
                    map.put(item, map.get(item) + cost);

                }
            }
        }
        Iterator it = map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            Expense to_add = new Expense(Integer.parseInt(pair.getValue().toString()),"","",pair.getKey().toString());
            to_send.add(to_add);
            System.out.println(pair.getKey() + " = " + pair.getValue());
            it.remove(); // avoids a ConcurrentModificationException


        }
        //add
        mAdapter = new ExpenseItemsAdapter(to_send, mContext);
        mRecyclerView.setAdapter(mAdapter);
    }


    public void showOverlay(Context ctx) {
        final Dialog dialog = new Dialog(ctx, android.R.style.Theme_Translucent_NoTitleBar);
        dialog.setContentView(R.layout.second_screen_overlay);

        RelativeLayout relativeLayout = (RelativeLayout) dialog.findViewById(R.id.secondScreenOverlayLayout);
        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View arg0) {

                dialog.dismiss();

            }
        });
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putBoolean("runActivityCategoryOnce",true);
        editor.commit();


        dialog.show();
    }
}
