package com.example.application.iricf;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class RakeName {

    @SerializedName("railway")
    private String railway;

    @SerializedName("rake_num")
    private String rakeNum;

    @SerializedName("despatch")
    private Date despatchDate;

    public RakeName(String railway, String rakeNum,Date despatchDate) {
        this.railway = railway;
        this.rakeNum = rakeNum;
        this.despatchDate = despatchDate;
    }

    public Date getDespatchDate() {
        return despatchDate;
    }

    public void setDespatchDate(Date despatchDate) {
        this.despatchDate = despatchDate;
    }

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
