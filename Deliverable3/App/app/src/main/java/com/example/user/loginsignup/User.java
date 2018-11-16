package com.example.user.loginsignup;

public class User {
    public String firstName, email, phone,lastName,address,role,username,day,month,year,companyName,description,licence,date,startTime,endTime;
    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }
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
//getters

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public String getDate() {
        return date;
    }

    public String getUsername() {
        return username;
    }

    public String getLicence() {
        return licence;
    }

    public String getRole() {
        return role;
    }

    public String getAddress() {
        return address;
    }

    public String getDay() {
        return day;
    }

    public String getDescription() {
        return description;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getMonth() {
        return month;
    }

    public String getPhone() {
        return phone;
    }

    public String getYear() {
        return year;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }
//setters
    public void setAddress(String address) {
        this.address = address;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public void setLicence(String licence) {
        this.licence = licence;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
