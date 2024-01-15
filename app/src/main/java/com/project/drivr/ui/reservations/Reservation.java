package com.project.drivr.ui.reservations;

import java.sql.Time;
import java.util.Date;

public class Reservation {
    private String email;//foreign key
    private Date date; //date reserved
    private Time time; //time reserved
    private String VIN;//foreign key, carId ---------may change to Car
    public Reservation(){

    }
    public Reservation(String email,  Date date, Time time, String VIN ){
        this.date=date;
        this.time=time;
        this.VIN=VIN;
        this.email=email;
    }

    public Date getDate() {
        return date;
    }

    public String getVIN() {
        return VIN;
    }

    public String getEmail() {
        return email;
    }

    public Time getTime() {
        return time;
    }


    public void setDate(Date date) {
        this.date = date;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setTime(Time time) {
        this.time = time;
    }

    public void setVIN(String VIN) {
        this.VIN = VIN;
    }
}
