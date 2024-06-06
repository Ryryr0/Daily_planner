package com.example.dailyplanner.anxiliary;


import android.os.Parcel;
import android.os.Parcelable;

import com.example.dailyplanner.R;

public class Task {
    private String name;
    private String description;
    private Types type;
    private int year;
    private int month;
    private int dayOfMonth;
    private boolean isComplete;

    public Task(String name, String description, Types type, int year, int month, int dayOfMonth, boolean isComplete) {
        this.name = name;
        this.description = description;
        this.type = type;
        this.year = year;
        this.month = month;
        this.dayOfMonth = dayOfMonth;
        this.isComplete = isComplete;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Types getType() {
        return type;
    }

    public void setType(Types type) {
        this.type = type;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDayOfMonth() {
        return dayOfMonth;
    }

    public void setDayOfMonth(int dayOfMonth) {
        this.dayOfMonth = dayOfMonth;
    }

    public boolean isComplete() {
        return isComplete;
    }

    public void setComplete(boolean complete) {
        isComplete = complete;
    }
}
