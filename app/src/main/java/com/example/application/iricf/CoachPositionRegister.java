package com.example.application.iricf;

import com.google.gson.annotations.SerializedName;

public class CoachPositionRegister {

    @SerializedName("data")
    private Position position;

    @SerializedName("status")
    private int status;

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }


}
