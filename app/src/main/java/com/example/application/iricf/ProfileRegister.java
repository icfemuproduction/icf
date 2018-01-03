package com.example.application.iricf;

import com.google.gson.annotations.SerializedName;

public class ProfileRegister {

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

        @SerializedName("name")
        private String name;

        @SerializedName("username")
        private String userName;

        @SerializedName("role")
        private String role;

        @SerializedName("position")
        private String position;

        @SerializedName("email")
        private String email;

        @SerializedName("mobile")
        private String mobileNum;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }

        public String getPosition() {
            return position;
        }

        public void setPosition(String position) {
            this.position = position;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getMobileNum() {
            return mobileNum;
        }

        public void setMobileNum(String mobileNum) {
            this.mobileNum = mobileNum;
        }
    }



}
