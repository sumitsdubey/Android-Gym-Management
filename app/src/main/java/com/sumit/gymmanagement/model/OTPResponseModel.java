package com.sumit.gymmanagement.model;

import java.util.ArrayList;

public class OTPResponseModel {
        public String raw;
        public int code;
        public String message;
        public boolean status;
        public ArrayList<Integer> data;
        public boolean error;

        public int getCode() {
                return code;
        }

        public String getRaw() {
                return raw;
        }

        public String getMessage() {
                return message;
        }

        public boolean isStatus() {
                return status;
        }

        public ArrayList<Integer> getData() {
                return data;
        }

        public boolean isError() {
                return error;
        }
}
