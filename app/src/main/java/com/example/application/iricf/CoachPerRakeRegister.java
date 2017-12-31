package com.example.application.iricf;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class CoachPerRakeRegister {

    @SerializedName("status")
    private Integer status;

    @SerializedName("data")
    private List<CoachPerRake> coaches = new ArrayList<>();

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public List<CoachPerRake> getCoaches() {
        return coaches;
    }

    public void setCoaches(List<CoachPerRake> coaches) {
        this.coaches = coaches;
    }
}
