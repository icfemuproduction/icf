package com.example.application.iricf;

import com.google.gson.annotations.SerializedName;

public class RakeName {

    @SerializedName("railway")
    private String railway;

    @SerializedName("rake_num")
    private String rakeNum;

    public String getRailway() {
        return railway;
    }

    public void setRailway(String railway) {
        this.railway = railway;
    }

    public String getRakeNum() {
        return rakeNum;
    }

    public void setRakeNum(String rakeNum) {
        this.rakeNum = rakeNum;
    }
}
