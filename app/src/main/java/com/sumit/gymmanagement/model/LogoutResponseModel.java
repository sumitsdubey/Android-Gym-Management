package com.sumit.gymmanagement.model;

import java.util.ArrayList;

public class LogoutResponseModel {
    public int code;
    public String message;
    public boolean status;
    public ArrayList<Object> data;
    public boolean error;

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public boolean isStatus() {
        return status;
    }

    public ArrayList<Object> getData() {
        return data;
    }

    public boolean isError() {
        return error;
    }
}
