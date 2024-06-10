package com.example.dailyplanner.anxiliary;


public class Task {
    private String userId;
    private String name;
    private String description;
    private Types type;
    private int year;
    private int month;
    private int dayOfMonth;
    private boolean isComplete;

    public Task(){}

    public Task(String name, String description, Types type, int year, int month, int dayOfMonth, boolean isComplete) {
        this.name = name;
        this.description = description;
        this.type = type;
        this.year = year;
        this.month = month;
        this.dayOfMonth = dayOfMonth;
        this.isComplete = isComplete;
        this.userId = "";
    }

    public Task(String userId, String name, String description, Types type, int year, int month, int dayOfMonth, boolean isComplete) {
        this.name = name;
        this.description = description;
        this.type = type;
        this.year = year;
        this.month = month;
        this.dayOfMonth = dayOfMonth;
        this.isComplete = isComplete;
        this.userId = userId;
    }

    public boolean equals(Task task) {
        return (this.name.equals(task.name) && this.description.equals(description)
                && this.type == task.type && this.year == task.year && this.month == task.month
                && this.dayOfMonth == task.dayOfMonth && this.userId.equals(task.userId));
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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
