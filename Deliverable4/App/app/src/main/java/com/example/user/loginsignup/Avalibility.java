package com.example.user.loginsignup;

public class Avalibility {

    public String date,endTime,startTime, serviceName;
    private String _id;

    public Avalibility() {
    }
    //user class has all the information that we want from the user
    public Avalibility(String id,String date,String startTime,String endTime){
        this._id=id;
        this.date = date;
        this.startTime=startTime;
        this.endTime =endTime;
    }
    public Avalibility(String id,String date,String startTime,String endTime, String serviceName){
        this._id=id;
        this.date = date;
        this.startTime=startTime;
        this.endTime =endTime;
        this.serviceName = serviceName;
    }

    public void setId(String id) {
        _id = id;
    }
    public String getId() {
        return _id;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getDate() {
        return date;
    }

    public String getEndTime() {
        return endTime;
    }

    public String getStartTime() {
        return startTime;
    }
}