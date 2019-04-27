package com.fepa.meupet.model.environment.feeder;

import java.io.Serializable;

public class FeedingTime implements Serializable {

    private int drop_amount;
    private String time;

    public FeedingTime(int drop_amount, String time) {
        this.drop_amount = drop_amount;
        this.time = time;
    }

    public int getDrop_amount() {
        return drop_amount;
    }

    public void setDrop_amount(int drop_amount) {
        this.drop_amount = drop_amount;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
