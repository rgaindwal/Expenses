package com.example.rgain.expenses;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.rgain.expenses.Models.Expense;

import java.util.ArrayList;

/**
 * Created by Gaindu on 01-10-2017.
 */

public class DatabaseHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "expensesManager";

    // Expenses table name
    private static final String TABLE_EXPENSES= "expenses";
    private static final String TABLE_LIST= "nameOfExpenses";

    // Expenses Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_AMOUNT = "amount";
    private static final String KEY_CATEGORY = "category";
    private static final String KEY_DATE = "date";
    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_EXPENSE_NAME = "expenseName";


    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_EXPENSES_TABLE = "CREATE TABLE " + TABLE_EXPENSES + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_AMOUNT + " INTEGER,"
                + KEY_CATEGORY + " TEXT,"
                + KEY_DATE + " TEXT,"
                + KEY_DESCRIPTION + " TEXT"
                + ")";
        db.execSQL(CREATE_EXPENSES_TABLE);

        String CREATE_EXPENSES_LIST_TABLE = "CREATE TABLE " + TABLE_LIST + "("

                + KEY_EXPENSE_NAME + " TEXT PRIMARY KEY"
                + ")";
        db.execSQL(CREATE_EXPENSES_LIST_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXPENSES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LIST);
        onCreate(db);

    }

    public void addExpenseName(String name) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_EXPENSE_NAME, name); // Expense category
        // Inserting Row
        db.insert(TABLE_LIST, null, values);
        db.close(); // Closing database connection
    }

    public void addExpense(Expense expense) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_AMOUNT, expense.getAmount()); // Expense amount
        values.put(KEY_CATEGORY, expense.getCategory()); // Expense category
        values.put(KEY_DATE, expense.getDate()); // Expense category
        values.put(KEY_DESCRIPTION, expense.getDescription()); // Expense category
        // Inserting Row
        db.insert(TABLE_EXPENSES, null, values);
        db.close(); // Closing database connection
    }

    // Getting single expense
    public Expense getExpense(int id) {

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_EXPENSES, new String[] { KEY_ID,
                        KEY_AMOUNT, KEY_CATEGORY, KEY_DATE,KEY_DESCRIPTION}, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Expense expense = new Expense(Integer.parseInt(cursor.getString(0)),
                Integer.parseInt(cursor.getString(1)), cursor.getString(2),cursor.getString(3),cursor.getString(4));
        // return contact
        return expense;

    }
    public ArrayList<String> getAllExpensesNames() {
        ArrayList<String> expenseList = new ArrayList<String>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_LIST;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                String nameOfExpense;
                nameOfExpense= (cursor.getString(0));

                // Adding Expense to list
                expenseList.add(nameOfExpense);
            } while (cursor.moveToNext());
        }

        // return Expense list
        return expenseList;
    }
    // Getting All Expenses
    public ArrayList<Expense> getAllExpenses() {
        ArrayList<Expense> expenseList = new ArrayList<Expense>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_EXPENSES;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Expense expense = new Expense();
                expense.setId(Integer.parseInt(cursor.getString(0)));
                expense.setAmount(Integer.parseInt(cursor.getString(1)));
                expense.setCategory(cursor.getString(2));
                expense.setDate(cursor.getString(3));
                expense.setDescription(cursor.getString(4));

                // Adding Expense to list
                expenseList.add(expense);
            } while (cursor.moveToNext());
        }

        // return Expense list
        return expenseList;
    }

    // Getting expenses Count
    public int getExpensesCount() {
        String countQuery = "SELECT  * FROM " + TABLE_EXPENSES;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }
    // Updating single expense
    public int updateExpense(Expense expense) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_AMOUNT, expense.getAmount());
        values.put(KEY_CATEGORY, expense.getCategory());
        values.put(KEY_DATE, expense.getDate());
        values.put(KEY_DESCRIPTION, expense.getDescription());

        // updating row
        return db.update(TABLE_EXPENSES, values, KEY_ID + " = ?",
                new String[] { String.valueOf(expense.getId()) });
    }

    // Deleting single expense
    public void deleteExpense(Expense expense) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_EXPENSES, KEY_ID + " = ?",
                new String[] { String.valueOf(expense.getId()) });
        db.close();
    }

    public void deleteExpenseWithDescriptionName(String description) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_EXPENSES, KEY_DESCRIPTION + " = ?",
                new String[] { description });
        db.close();
    }

    public void deleteExpenseNameandExpenses(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_LIST, KEY_EXPENSE_NAME + " = ?",
                new String[] { name });
        db.delete(TABLE_EXPENSES, KEY_CATEGORY + " = ?",
                new String[] { name});
        db.close();

    }

}
