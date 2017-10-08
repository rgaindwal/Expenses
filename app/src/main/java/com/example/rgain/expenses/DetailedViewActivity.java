package com.example.rgain.expenses;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.rgain.expenses.Adapters.DescriptionItemAdapter;
import com.example.rgain.expenses.Adapters.ExpenseItemsAdapter;
import com.example.rgain.expenses.Models.Expense;

import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import static android.R.attr.category;
import static android.R.attr.mapViewStyle;
import static android.R.attr.name;
import static android.icu.lang.UCharacter.GraphemeClusterBreak.L;
import static com.example.rgain.expenses.R.drawable.calendar;

public class DetailedViewActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private DescriptionItemAdapter mAdapter;
    private Context mContext;
    private TextView weeklyTextView, monthlyTextView;
    private String name;
    private DatabaseHandler db;
    public static final String MyPREFERENCES = "expensePrefs" ;
    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
//        getSupportActionBar().hide();
        setContentView(R.layout.activity_detailed_view);


        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        Boolean isRunOnce= sharedpreferences.getBoolean("runActivityDetailedOnce",false);
        if(!isRunOnce)
            showOverlay(this);


        Intent i = getIntent();
        final String description = i.getStringExtra("description");
        name = description;
//        setTitle(description.toUpperCase());
        TextView heading = (TextView) findViewById(R.id.description_name);
        heading.setText(description.toUpperCase());

        weeklyTextView= (TextView) findViewById(R.id.weekly_spent_total);
        monthlyTextView= (TextView) findViewById(R.id.monthly_spent_total);
        mContext=this;
        db = new DatabaseHandler(this);
        mRecyclerView=(RecyclerView) findViewById(R.id.expense_description_recycler_view) ;
        mLinearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        showListData(description);


    }

    public void showListData(String name) {
        int monthly=0,weekly=0;
        ArrayList<Expense> allExpenseList = db.getAllExpenses();

        ArrayList<Expense> to_send = new ArrayList<>();
        for (int j = 0; j < allExpenseList.size(); j++) {
            if (allExpenseList.get(j).getDescription().equals(name)) {
                to_send.add(allExpenseList.get(j));
                Log.v("test","found");

                String dateString = allExpenseList.get(j).getDate();
                String[] parts = dateString.split("-");
                int day = Integer.parseInt(parts[0]);
                int month = Integer.parseInt(parts[1]);
                int year = Integer.parseInt(parts[2]);
                Log.v("date",String.valueOf(day)+"-"+String.valueOf(month)+"-"+String.valueOf(year));
                Calendar calendar = Calendar.getInstance();
//                calendar.set(year, month, day);
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month-1);
                calendar.set(Calendar.DAY_OF_MONTH, day);
                Date date = calendar.getTime();
                Log.v("that date " ,date.toString());
                int week = calendar.get(Calendar.WEEK_OF_YEAR);
                Log.v("week and month",String.valueOf(month)+ "   "+String.valueOf(week));


                Calendar today = Calendar.getInstance();
//                today.set(Calendar.HOUR_OF_DAY, 0); // same for minutes and seconds
//                today.set(Calendar.SECOND,0);
//                today.set(Calendar.MINUTE,0);
                Date now = today.getTime();

                Calendar cal = Calendar.getInstance();
                date = cal.getTime();
//                cal.setTime(now);
                int month_today = cal.get(Calendar.MONTH);
                month_today++;
                int week_today = cal.get(Calendar.WEEK_OF_YEAR);
                Log.v("Month today ",String.valueOf(month_today));
                Log.v("week now",String.valueOf(week_today));

                if(month==month_today)
                    monthly+=allExpenseList.get(j).getAmount();
                if(week_today==week)
                    weekly+=allExpenseList.get(j).getAmount();


            }
        }
        weeklyTextView.setText("₹ " +String.valueOf(weekly));
        monthlyTextView.setText("₹ " +String.valueOf(monthly));
        mAdapter = new DescriptionItemAdapter(to_send,mContext,name);
        mRecyclerView.setAdapter(mAdapter);


    }


    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.detailed_menu, menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.date:
                showListDataSorted(0);
                return true;
            case R.id.money:
                showListDataSorted(1);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

        //respond to menu item selection

    }

    private void showListDataSorted(int i) {
        ArrayList<Expense> allExpenseList = db.getAllExpenses();

        ArrayList<Expense> to_send = new ArrayList<>();
        for (int j = 0; j < allExpenseList.size(); j++) {
            if (allExpenseList.get(j).getDescription().equals(name)) {
                to_send.add(allExpenseList.get(j));
                Log.v("test","found");
            }
        }
        if(i==0) {
            Collections.sort(to_send, new Comparator<Expense>() {
                @Override
                public int compare(Expense lhs, Expense rhs) {
                    // -1 - less than, 1 - greater than, 0 - equal, all inversed for descending
                    SimpleDateFormat sdf = new SimpleDateFormat("ddMMYYYY");



                    String[] parts = lhs.getDate().split("-");
                    int day1 = Integer.parseInt(parts[0]);
                    int month1 = Integer.parseInt(parts[1]);
                    int year1 = Integer.parseInt(parts[2]);
                    String[] parts1 = rhs.getDate().split("-");
                    int day2 = Integer.parseInt(parts1[0]);
                    int month2 = Integer.parseInt(parts1[1]);
                    int year2= Integer.parseInt(parts1[2]);

                    Calendar calendar = Calendar.getInstance();
                    calendar.set(year1, month1-1, day1);
                    Date leftDate = calendar.getTime();
                    Calendar calendar1 = Calendar.getInstance();
                    calendar1.set(year2, month2-1, day2);
                    Date rightDate = calendar1.getTime();

                    return leftDate.after(rightDate) ? -1 : (rightDate.after(leftDate)) ? 1 : 0;
//                    try {
//                        Log.v("dates",lhs.getDate()+ "  "+rhs.getDate());
//                        Date leftDate = (Date) sdf.parse(lhs.getDate().replaceAll("-",""));
//
//                        Date rightDate = (Date) sdf.parse(rhs.getDate().replaceAll("-",""));
//
//                        Log.v("Dates",leftDate.toString()+ "  "+rightDate.toString());
//
//
//                    } catch (ParseException e) {
//                        e.printStackTrace();
//                        return 0;
//                    }


                }
            });
        }
        else
        {
            Collections.sort(to_send, new Comparator<Expense>() {
                @Override
                public int compare(Expense lhs, Expense rhs) {
                    // -1 - less than, 1 - greater than, 0 - equal, all inversed for descending
                    return lhs.getAmount() > rhs.getAmount() ? -1 : (lhs.getAmount() < rhs.getAmount()) ? 1 : 0;
                }
            });

        }
        Log.v("new swt",to_send.toString());
        DescriptionItemAdapter newAdapter = new DescriptionItemAdapter(to_send,mContext,name);
        mRecyclerView.swapAdapter(newAdapter,false);
//        mRecyclerView.setAdapter(mAdapter);
    }

    public void showOverlay(Context ctx) {
        final Dialog dialog = new Dialog(ctx, android.R.style.Theme_Translucent_NoTitleBar);
        dialog.setContentView(R.layout.third_screen_overlay);

        RelativeLayout relativeLayout = (RelativeLayout) dialog.findViewById(R.id.thirdScreenOverlayLayout);
        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View arg0) {

                dialog.dismiss();

            }
        });
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putBoolean("runActivityDetailedOnce",true);
        editor.commit();


        dialog.show();
    }
}
