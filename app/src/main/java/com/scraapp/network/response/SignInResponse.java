package com.scraapp.network.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class SignInResponse extends AbstractApiResponse {

    @SerializedName("result")
    @Expose
    public Result result;

    public class Result {

        public String message;

        public String status;

        @SerializedName("user")
        private User user;

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public User getUser() {
            return user;
        }

        public void setUser(User user) {
            this.user = user;
        }
    }

    private class User {

        @SerializedName("id")
        String id;
        @SerializedName("name")
        String name;
        @SerializedName("email_id")
        String email_id;
        @SerializedName("mobile_no")
        String mobile_no;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getEmail_id() {
            return email_id;
        }

        public void setEmail_id(String email_id) {
            this.email_id = email_id;
        }

        public String getMobile_no() {
            return mobile_no;
        }

        public void setMobile_no(String mobile_no) {
            this.mobile_no = mobile_no;
        }
    }

}
