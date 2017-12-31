package com.example.application.iricf;

import com.google.gson.annotations.SerializedName;

public class PositionRegister {

    @SerializedName("data")
    private Datum datum;

    @SerializedName("status")
    private int status;

    public Datum getDatum() {
        return datum;
    }

    public void setDatum(Datum datum) {
        this.datum = datum;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    class Datum{

        @SerializedName("line")
        private String line;

        @SerializedName("stage")
        private String stage;

        @SerializedName("coach_num")
        private String coachNum;

        @SerializedName("rake_num")
        private String rakeNum;

        public String getLine() {
            return line;
        }

        public void setLine(String line) {
            this.line = line;
        }

        public String getStage() {
            return stage;
        }

        public void setStage(String stage) {
            this.stage = stage;
        }

        public String getCoachNum() {
            return coachNum;
        }

        public void setCoachNum(String coachNum) {
            this.coachNum = coachNum;
        }

        public String getRakeNum() {
            return rakeNum;
        }

        public void setRakeNum(String rakeNum) {
            this.rakeNum = rakeNum;
        }
    }
}
