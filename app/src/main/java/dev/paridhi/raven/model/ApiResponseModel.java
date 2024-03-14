package dev.paridhi.raven.model;

public class ApiResponseModel {
    String Prediction;

    public ApiResponseModel(String prediction) {
        Prediction = prediction;
    }

    public String getPrediction() {
        return Prediction;
    }

    public void setPrediction(String prediction) {
        Prediction = prediction;
    }
}
