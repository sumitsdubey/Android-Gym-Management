package com.sumit.gymmanagement.model;

import java.util.ArrayList;

public class FeesDataResponseModel {
        public int code;
        public String message;
        public boolean status;
        public ArrayList<FeesDataModel> data;
    public ArrayList<ExtraData> extra_data;

    public ArrayList<ExtraData> getExtra_data() {
        return extra_data;
    }

    public void setExtra_data(ArrayList<ExtraData> extra_data) {
        this.extra_data = extra_data;
    }

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

    public ArrayList<FeesDataModel> getData() {
        return data;
    }

    public void setData(ArrayList<FeesDataModel> data) {
        this.data = data;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }
}
