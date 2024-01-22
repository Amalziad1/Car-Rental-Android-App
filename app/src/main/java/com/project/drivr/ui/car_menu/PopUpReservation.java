package com.project.drivr.ui.car_menu;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;

import com.project.drivr.DataBaseHelper;
import com.project.drivr.R;
import com.project.drivr.ui.reservations.Reservation;
import com.squareup.picasso.Picasso;

import java.sql.Time;
import java.util.Date;

public class PopUpReservation extends DialogFragment {

    private String factory;
    private String type;
    private String model;
    private int year;
    private String transmission;
    private double mileage;
    private String fuel;
    private double price;
    private String img;
    private String email;
    private String VIN;

    public PopUpReservation() {}

    public static PopUpReservation newInstance(String factory, String type, String model, int year, String transmission, String fuel, double mileage, double price, String img, String email, String VIN) {
        PopUpReservation fragment = new PopUpReservation();
        Bundle args = new Bundle();
        args.putString("VIN", VIN);
        args.putString("factory", factory);
        args.putString("type",type);
        args.putString("img",img);
        args.putDouble("price",price);
        args.putString("model",model);
        args.putInt("year", year);
        args.putString("transmission", transmission);
        args.putString("fuel", fuel);
        args.putDouble("mileage", mileage);
        args.putString("email",email);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.VIN = getArguments().getString("VIN");
            this.model = getArguments().getString("model");
            this.year = getArguments().getInt("year");
            this.transmission = getArguments().getString("transmission");
            this.fuel = getArguments().getString("fuel");
            this.mileage = getArguments().getDouble("mileage");
            this.price=getArguments().getDouble("price");
            this.img=getArguments().getString("img");
            this.type=getArguments().getString("type");
            this.factory=getArguments().getString("factory");
            this.email=getArguments().getString("email");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pop_up_reservation, container, false);
        TextView carName=view.findViewById(R.id.textCarName);
        TextView priceText=view.findViewById(R.id.textPrice);
        Button reserve=view.findViewById(R.id.button4Reserve);
        Button cancel=view.findViewById(R.id.button5Cancel);
        ImageView imageView=view.findViewById(R.id.imageView6);
        carName.setText(factory+"-"+type+"-"+String.valueOf(model));
        priceText.setText(price+"$/day");
        Picasso.get().load(img).into(imageView);
        DataBaseHelper dataBaseHelper = DataBaseHelper.getInstance(getActivity().getApplicationContext(),"registration",null,1);
        reserve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Reservation reserve=new Reservation();
                long currentTimeMillis = System.currentTimeMillis();
                Date currentDate = new Date(currentTimeMillis);
                Time currentTime = new Time(currentDate.getTime());
                reserve.setDate(currentDate);
                reserve.setTime(currentTime);
                reserve.setEmail(email);
                reserve.setVIN(VIN);
                dataBaseHelper.insertReservation(reserve);
                Toast.makeText(getActivity().getApplicationContext(), "Congrats on your Reservation!", Toast.LENGTH_SHORT).show();
                dismiss();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        return view;
    }


}
