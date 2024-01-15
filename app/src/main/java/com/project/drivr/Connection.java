package com.project.drivr;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.core.view.WindowCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.project.drivr.databinding.ActivityConnectionBinding;
import com.project.drivr.ui.car_menu.Car;

import java.util.List;

public class Connection extends AppCompatActivity {
    Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connection);
        setProgress(false);
        button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConnectionAsync connectionAsyncTask = new ConnectionAsync(Connection.this);
                connectionAsyncTask.execute("https://mp0364e1f95be16573ab.free.beeceptor.com/data");
            }
        });
    }
    public void setButtonText(String text) {
        button.setText(text);
    }
    public void fillCars(List<Car> car) {
        DataBaseHelper dataBaseHelper = new DataBaseHelper(Connection.this,"registration",null,1);
        for (int i = 0; i < car.size(); i++) {
            dataBaseHelper.insertCar(car.get(i));
        }
    }
    public void setProgress(boolean progress) {
        ProgressBar progressBar = (ProgressBar)
                findViewById(R.id.progressBar);
        if (progress) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }
    public void moveToNextIntent(){
        Intent intent = new Intent(Connection.this, LoginSignup.class);
        startActivity(intent);
        finish();
    }
}