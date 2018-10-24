package com.example.user.loginsignup;

public class User {
    public String name, email, phone,last,address,role,username;


    public User(String username,String name,String last, String email, String phone,String address ,String role) {
        this.username = username;
        this.name = name;
        this.last = last;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.role =role;

    }

    public String getName() {
        return name;
    }

    public String getRole() {
        return role;
    }
}
