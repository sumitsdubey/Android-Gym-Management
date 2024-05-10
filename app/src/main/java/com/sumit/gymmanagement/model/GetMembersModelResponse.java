package com.sumit.gymmanagement.model;

import java.util.ArrayList;

public class GetMembersModelResponse {
    public int code;
    public String message;
    public boolean status;
    public ArrayList<MembersModel> data;
    public boolean error;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public ArrayList<MembersModel> getData() {
        return data;
    }

    public void setData(ArrayList<MembersModel> data) {
        this.data = data;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }
}


