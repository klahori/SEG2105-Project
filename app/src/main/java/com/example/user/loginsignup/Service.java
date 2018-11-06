package com.example.user.loginsignup;

public class Service {
    public String serviceName;
    public double cost;
    private String _id;

    public Service() {
    }
    //user class has all the information that we want from the user
    public Service(String id,String serviceName,double cost){
        this._id=id;
        this.serviceName = serviceName;
        this.cost =cost;

    }
    public void setId(String id) {
        _id = id;
    }
    public String getId() {
        return _id;
    }
    public void setServiceName(String serviceName) {
    this.serviceName = serviceName;    }
    public String getServiceName() {
        return serviceName;
    }
    public void setCost(double cost) {
        this.cost = cost;
    }
    public double getCost() {
        return cost;
    }

}


