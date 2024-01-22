package com.project.drivr.ui.car_menu;

public class Car {
    private String VIN;//primary key
    private String factory;
    private String type;
    private String model;
    private int year;
    private String transmission;
    private double mileage;
    private String fuel;
    private double price;
    private String imgURL;
    public Car(){

    }
    public Car(String VIN, String factory, String type, String model, int year, String transmission, double mileage, String fuel, double price, String imgURL) {
        this.VIN = VIN;
        this.factory = factory;
        this.type = type;
        this.model = model;
        this.year = year;
        this.transmission = transmission;
        this.mileage = mileage;
        this.fuel = fuel;
        this.price = price;
        this.imgURL=imgURL;
    }
    public String getImgURL() {
        return imgURL;
    }

    public void setImgURL(String imgURL) {
        this.imgURL = imgURL;
    }
    public void setVIN(String VIN) {
        this.VIN = VIN;
    }

    public void setFactory(String factory) {
        this.factory = factory;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getVIN() {
        return VIN;
    }

    public String getFactory() {
        return factory;
    }

    public String getType() {
        return type;
    }

    public String getModel() {
        return model;
    }

    public double getPrice() {
        return price;
    }


    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getTransmission() {
        return transmission;
    }

    public void setTransmission(String trasmission) {
        this.transmission = trasmission;
    }

    public double getMileage() {
        return mileage;
    }

    public void setMileage(double mileage) {
        this.mileage = mileage;
    }

    public String getFuel() {
        return fuel;
    }

    public void setFuel(String fuel) {
        this.fuel = fuel;
    }
}
