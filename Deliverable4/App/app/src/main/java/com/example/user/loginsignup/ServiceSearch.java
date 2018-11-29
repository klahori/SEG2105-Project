package com.example.user.loginsignup;

public class ServiceSearch {

    public String serviceName, cost, rating;
    //public int cost;

    public ServiceSearch() {

    }

    public ServiceSearch(String serviceName, String cost, String rating) {
        this.serviceName = serviceName;
        this.cost = cost;
        this.rating = rating;
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
}
