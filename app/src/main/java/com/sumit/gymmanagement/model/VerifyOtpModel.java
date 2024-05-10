package com.sumit.gymmanagement.model;

import java.util.ArrayList;

public class VerifyOtpModel {
    public int code;
    public String message;
    public boolean status;
    public ArrayList<Otpdata> data;


    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public boolean isStatus() {
        return status;
    }

    public ArrayList<Otpdata> getData() {
        return data;
    }


}



