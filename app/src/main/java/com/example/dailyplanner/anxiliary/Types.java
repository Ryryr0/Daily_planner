package com.example.dailyplanner.anxiliary;

import com.example.dailyplanner.R;

public enum Types {
    WORK(R.drawable.work),
    REST(R.drawable.rest),
    ROUTINE(R.drawable.routine),
    OTHER(R.drawable.other);

    private int title;
    Types(int title) {
        this.title = title;
    }

    public int getTitle() {
        return title;
    }
}
