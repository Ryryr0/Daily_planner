package com.example.dailyplanner.anxiliary;

public class Event {
    private int imageResId;
    private String title;
    private String description;

    public Event(int imageResId, String title, String description) {
        this.imageResId = imageResId;
        this.title = title;
        this.description = description;
    }

    public int getImageResId() {
        return imageResId;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }
}
