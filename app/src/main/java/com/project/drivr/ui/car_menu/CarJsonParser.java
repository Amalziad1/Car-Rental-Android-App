package com.project.drivr.ui.car_menu;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CarJsonParser {
    public static List<Car> getObjectFromJson(String json) {
        List<Car> cars;
        try {
            JSONArray jsonArray = new JSONArray(json);
            cars = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = new JSONObject();
                jsonObject = (JSONObject) jsonArray.get(i);
                Car car = new Car();
                car.setVIN(jsonObject.getString("VIN"));
                car.setFactory(jsonObject.getString("car_factory"));
                car.setModel(jsonObject.getInt("model"));
                car.setType(jsonObject.getString("car_type"));
                car.setPrice(jsonObject.getDouble("price"));
                car.setImgURL(jsonObject.getString("image"));
                cars.add(car);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return cars;
    }
}
