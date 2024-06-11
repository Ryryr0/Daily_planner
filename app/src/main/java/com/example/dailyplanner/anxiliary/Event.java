package com.example.dailyplanner.anxiliary;

public class Event {
    private String imageResId;
    private String title;
    private String description;
    private int month, dayOfMonth, year;

    public Event(){}

    public Event(String imageResId, String title, String description, int month, int dayOfMonth, int year) {
        this.imageResId = imageResId;
        this.title = title;
        this.description = description;
        this.month = month;
        this.dayOfMonth = dayOfMonth;
        this.year = year;
    }

    public boolean equals(Event event) {
        return (this.imageResId.equals(event.getImageResId()) && this.title.equals(event.getTitle())
                && this.description.equals(event.getDescription()) && this.month == event.getMonth()
                && this.dayOfMonth == event.getDayOfMonth() && this.year == event.getYear());
    }

    public String getImageResId() {
        return imageResId;
    }

    public void setImageResId(String imageResId) {
        this.imageResId = imageResId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }
}
