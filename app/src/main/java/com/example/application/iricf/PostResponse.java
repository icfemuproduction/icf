package com.example.application.iricf;

import com.google.gson.annotations.SerializedName;

public class PostResponse {

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

    class Datum {

        @SerializedName("message")
        private String message;

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}
