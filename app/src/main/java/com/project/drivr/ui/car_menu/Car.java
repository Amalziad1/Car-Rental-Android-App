package com.project.drivr.ui.car_menu;

public class Car {
    private String VIN;//primary key
    private String factory;
    private String type;
    private int model;
    private double price;
    private String imgURL;
    public Car(){

    }
    public Car(String VIN, String factory, String type, int model, double price, String imgURL) {
        this.VIN = VIN;
        this.factory = factory;
        this.type = type;
        this.model = model;
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

    public void setModel(int model) {
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

    public int getModel() {
        return model;
    }

    public double getPrice() {
        return price;
    }



}
