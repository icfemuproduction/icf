package com.example.application.iricf;

import com.google.gson.annotations.SerializedName;

public class LoginRegister {


    @SerializedName("status")
    private Integer status;

    @SerializedName("data")
    private Datum datum;


    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Datum getDatum() {
        return datum;
    }

    public void setDatum(Datum datum) {
        this.datum = datum;
    }

    class Datum{
        @SerializedName("token")
        String token;

        @SerializedName("message")
        String message;

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }


}
