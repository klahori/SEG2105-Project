package com.example.user.loginsignup;

public class User {
    public String name, email, phone,last,address,role,username,day,month,year;


    public User(String username,String name,String last, String email, String phone,String address ,String role,String day,String month,String year) {
        this.username = username;
        this.name = name;
        this.last = last;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.role =role;
        this.day =day;
        this.month=month;
        this.year=year;

    }

    public String getName() {
        return name;
    }

    public String getRole() {
        return role;
    }
}
