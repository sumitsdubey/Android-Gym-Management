package com.sumit.gymmanagement.model;

public class AddDataResponseModel {
        public int code;
        public String message;
        public boolean status;

        public AddDataResponseModel(int code, String message, boolean status) {
                this.code = code;
                this.message = message;
                this.status = status;
        }

        public int getCode() {
                return code;
        }

        public String getMessage() {
                return message;
        }

        public boolean isStatus() {
                return status;
        }
}
