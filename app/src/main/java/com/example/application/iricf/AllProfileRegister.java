package com.example.application.iricf;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AllProfileRegister {

    @SerializedName("status")
    private int status;

    @SerializedName("data")
    List<Profile> data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<Profile> getData() {
        return data;
    }

    public void setData(List<Profile> data) {
        this.data = data;
    }
}
