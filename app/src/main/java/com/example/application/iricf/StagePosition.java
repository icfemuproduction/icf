package com.example.application.iricf;

public class StagePosition {

    int stage;
    String coachNum;

    public StagePosition() {
    }

    public StagePosition(int stage, String coachNum) {
        this.stage = stage;
        this.coachNum = coachNum;
    }

    public int getStage() {
        return stage;
    }

    public void setStage(int stage) {
        this.stage = stage;
    }

    public String getCoachNum() {
        return coachNum;
    }

    public void setCoachNum(String coachNum) {
        this.coachNum = coachNum;
    }
}
