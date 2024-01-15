package com.project.drivr;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;

import com.project.drivr.ui.car_menu.Car;
import com.project.drivr.ui.car_menu.CarJsonParser;

import java.util.List;

public class ConnectionAsync extends AsyncTask<String, String,String> {
    Activity activity;
    public ConnectionAsync(Activity activity) {
        this.activity = activity;
    }
    @Override
    protected void onPreExecute() {
        ((Connection) activity).setButtonText("connecting");
        super.onPreExecute();
        ((Connection) activity).setProgress(true);
    }
    @Override
    protected String doInBackground(String... params) {
        String data = HttpManager.getData(params[0]);
        return data;
    }
    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        ((Connection) activity).setProgress(false);
        ((Connection) activity).setButtonText("connected");
        List<Car> cars = CarJsonParser.getObjectFromJson(s);
        ((Connection) activity).fillCars(cars);
        ((Connection) activity).moveToNextIntent();
    }
}
