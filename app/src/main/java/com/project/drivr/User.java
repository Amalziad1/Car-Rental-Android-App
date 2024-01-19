package com.project.drivr;

import android.content.Context;

public class User {
    private String gender;
    private String firstName;
    private String lastName;
    private String email;
    private String country;
    private String city;
    private String password;
    private long phoneNumber;
    private String picturePath;
    public User(){

    }
    public User(String firstName,String lastName,String gender,String email,String country,String city, String password,long phoneNumber, String picturePath){
        this.gender=gender;
        this.city=city;
        this.country=country;
        this.email=email;
        this.firstName=firstName;
        this.lastName=lastName;
        this.password=password;
        this.phoneNumber=phoneNumber;
        this.picturePath=picturePath;
    }

    public long getPhoneNumber() {
        return phoneNumber;
    }

    public String getCity() {
        return city;
    }
    public String getCountry() {
        return country;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPassword() {
        return password;
    }

    public String getGender() {
        return gender;
    }

    public String getPicturePath() {
        return picturePath;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPhoneNumber(long phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setPicturePath(String picturePath) {
        this.picturePath = picturePath;
    }
}
