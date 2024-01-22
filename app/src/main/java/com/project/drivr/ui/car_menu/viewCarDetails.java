package com.project.drivr.ui.car_menu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.project.drivr.DataBaseHelper;
import com.project.drivr.R;
import com.project.drivr.ui.reservations.Reservation;
import com.squareup.picasso.Picasso;

import java.sql.Time;
import java.util.Date;

public class viewCarDetails extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_car_details);
        Intent intent = getIntent();
        String factory = intent.getStringExtra("factory");
        String type=intent.getStringExtra("type");
        String img=intent.getStringExtra("img");
        String model=intent.getStringExtra("model");
        int year=intent.getIntExtra("year",0);
        String fuel=intent.getStringExtra("fuel");
        String transmission=intent.getStringExtra("transmission");
        double mileage=intent.getDoubleExtra("mileage",0);
        double price=intent.getDoubleExtra("price",0);
        String VIN=intent.getStringExtra("VIN");
        String email=intent.getStringExtra("email");

        TextView factoryText=findViewById(R.id.dFactory);
        TextView typeText=findViewById(R.id.dType);
        TextView modelText=findViewById(R.id.dModel);
        TextView priceText=findViewById(R.id.dPrice);
        ImageView imgView=findViewById(R.id.photoImageView);

        factoryText.setText(factory);
        typeText.setText(type);
        modelText.setText(String.valueOf(model));
        priceText.setText(String.valueOf(price));
        Picasso.get().load(img).into(imgView);//to display images

        Button addFav=findViewById(R.id.dAddFav);
        Button addRes=findViewById(R.id.dAddRes);

        DataBaseHelper dataBaseHelper = DataBaseHelper.getInstance(viewCarDetails.this,"registration",null,1);
        addRes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopUpReservation popUpReservation = PopUpReservation.newInstance(factory, type, model, year, transmission, fuel, mileage, price, img, email, VIN);
                popUpReservation.show(getSupportFragmentManager(), "popup_fragment");
            }
        });
        addFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //dataBaseHelper.insertReservation(reserve);
            }
        });
    }
}