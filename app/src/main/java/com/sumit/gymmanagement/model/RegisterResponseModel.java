package com.sumit.gymmanagement.model;

import java.util.ArrayList;

public class RegisterResponseModel {


        public int code;
        public String message;
        public boolean status;
        public ArrayList<UserData> data;

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public boolean isStatus() {
        return status;
    }

    public ArrayList<UserData> getData() {
        return data;
    }
}
