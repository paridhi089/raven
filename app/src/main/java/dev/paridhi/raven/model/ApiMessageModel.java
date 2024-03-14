package dev.paridhi.raven.model;

import com.google.gson.annotations.SerializedName;

public class ApiMessageModel {

    @SerializedName("Message")
    String Message;

    public ApiMessageModel(String message) {
        Message = message;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }
}
