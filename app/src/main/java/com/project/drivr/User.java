package com.project.drivr;

public class User {
    private String gender;
    private String firstName;
    private String lastName;
    private String email;
    private String country;
    private String city;
    private String password;
    private long phoneNumber;
    public User(){

    }
    public User(String firstName,String lastName,String gender,String email,String country,String city, String password,long phoneNumber){
        this.gender=gender;
        this.city=city;
        this.country=country;
        this.email=email;
        this.firstName=firstName;
        this.lastName=lastName;
        this.password=password;
        this.phoneNumber=phoneNumber;
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
}
