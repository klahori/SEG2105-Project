package com.example.user.loginsignup;

public class User {
    public String firstName, email, phone,lastName,address,role,username,day,month,year;

//user class has all the information that we want from the user
    public User(String username,String name,String last, String email, String phone,String address ,String role,String day,String month,String year) {
        this.username = username;
        this.firstName = name;
        this.lastName = last;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.role =role;
        this.day =day;
        this.month=month;
        this.year=year;

    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }
}
