package com.example.rgain.expenses.Models;

/**
 * Created by rgain on 10/1/2017.
 */

public class Expense {
    int id;
    int amount;
    String category;
    String date;
    String description;
    public Expense()
    {}
    public Expense(int id, int amount, String category, String date, String description) {
        this.id = id;
        this.amount = amount;
        this.category = category;
        this.date = date;
        this.description = description;
    }
    public Expense( int amount, String category, String date, String description) {

        this.amount = amount;
        this.category = category;
        this.date = date;
        this.description = description;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
