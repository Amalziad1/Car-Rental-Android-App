package com.project.drivr.ui.profile;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.database.Cursor;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.project.drivr.DataBaseHelper;
import com.project.drivr.SharedPrefManager;

public class ProfileViewModel extends ViewModel {

    private String firstName;
    private String lastName;
    private String country;
    private String city;
    private String gender;
    private String phoneNumber;
    private String email;
    private String password;
    private String retypedPassword;
    private Application application;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRetypedPassword() {
        return retypedPassword;
    }

    public void setRetypedPassword(String retypedPassword) {
        this.retypedPassword = retypedPassword;
    }


    public ProfileViewModel(Application application) {
        this.application = application;
    }

    public void UpdateProfileInformation() {
        Context context;
        SharedPrefManager sharedPrefManager = SharedPrefManager.getInstance(application.getApplicationContext());
        String userName = sharedPrefManager.readString("userName", null);
        DataBaseHelper dataBaseHelper = DataBaseHelper.getInstance(application.getApplicationContext(), "registration", null, 1);
        Cursor cursor = dataBaseHelper.getUserByEmail(userName);
        cursor.moveToLast();
        int firstNameIndex = cursor.getColumnIndexOrThrow("FIRSTNAME");
        String name = cursor.getString(firstNameIndex) + " " + cursor.getString(firstNameIndex + 1);
        String PFPpath = cursor.getString(cursor.getColumnIndexOrThrow("PICTURE_PATH"));
    }

}