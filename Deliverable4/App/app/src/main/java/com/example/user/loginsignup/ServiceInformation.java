package com.example.user.loginsignup;

public class ServiceInformation {
    private String serviceName, rating, date, StartTime, endTime, email, id;
    private String  cost;

    public ServiceInformation(String serviceName, String cost, String id) {
        this.serviceName = serviceName;
        this.cost = cost;
        this.id = id;
        this.date = "Date: Unavailable";
        this.StartTime = "Start Time: Unavailable";
        this.endTime = "End Time: Unavailable";
        this.email = "Email: Unavailable";
        this.rating = "unrated";
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStartTime() {
        return StartTime;
    }

    public void setStartTime(String startTime) {
        StartTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
