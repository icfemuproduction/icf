package com.example.application.iricf;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class PositionRegister {

    @SerializedName("data")
    private List<Position> positionList = new ArrayList<>();

    @SerializedName("status")
    private Integer status;


    public List<Position> getPositionList() {
        return positionList;
    }

    public void setPositionList(List<Position> positionList) {
        this.positionList = positionList;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
