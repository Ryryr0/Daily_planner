package com.example.dailyplanner.mainpages.taskpage;


import android.os.Parcel;
import android.os.Parcelable;

import com.example.dailyplanner.R;

public class Task implements Parcelable {
    private String name;
    private String description;
    private int icon;
    private int year;
    private int month;
    private int dayOfMonth;
    private boolean isComplete;

    public Task() {
        this.name = "Название";
        this.description = "Описание";
        this.icon = R.drawable.ic_notifications_black_24dp;
        this.year = 2024;
        this.month = 2;
        this.dayOfMonth = 8;
        this.isComplete = false;
    }

    public Task(String name, String description, int icon, int year,
                int month, int dayOfMonth, boolean isComplete) {
        this.name = name;
        this.description = description;
        this.icon = icon;
        this.year = year;
        this.month = month;
        this.dayOfMonth = dayOfMonth;
        this.isComplete = isComplete;
    }

    Task(Parcel parcel) {
        name = parcel.readString();
        description = parcel.readString();
        icon = parcel.readInt();
        year = parcel.readInt();
        month = parcel.readInt();
        dayOfMonth = parcel.readInt();
        isComplete = 0 != parcel.readInt();
    }

    public static final Creator<Task> CREATOR = new Creator<Task>() {
        @Override
        public Task createFromParcel(Parcel in) {
            return new Task(in);
        }

        @Override
        public Task[] newArray(int size) {
            return new Task[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(description);
        dest.writeInt(icon);
        dest.writeInt(year);
        dest.writeInt(month);
        dest.writeInt(dayOfMonth);
        if (isComplete)
            dest.writeInt(1);
        else
            dest.writeInt(0);
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getIcon() {
        return icon;
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public int getDayOfMonth() {
        return dayOfMonth;
    }

    public boolean isComplete() {
        return isComplete;
    }
}
