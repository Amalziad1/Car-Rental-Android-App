package com.project.drivr.ui.home;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.project.drivr.DataBaseHelper;
import com.project.drivr.R;
import com.project.drivr.SharedPrefManager;
import com.project.drivr.ui.car_menu.viewCarDetails;
import com.squareup.picasso.Picasso;

public class latestReservation extends Fragment {
    private String img;
    private String time;
    private String date;
    private String factory;
    private String type;
    private int model;
    private double price;
    private String VIN;
    SharedPrefManager sharedPrefManager;
    private String email;
    int status=0;//to know if there's reservations to show details
    public latestReservation() {
        // Required empty public constructor
    }
    public static latestReservation newInstance(String img,String time,String date,String factory,String type) {
        latestReservation fragment = new latestReservation();
        Bundle args = new Bundle();
        args.putString("img", img);
        args.putString("time",time);
        args.putString("date",date);
        args.putString("factory",factory);
        args.putString("type",type);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            img = getArguments().getString("img");
            time=getArguments().getString("time");
            date=getArguments().getString("date");
            factory=getArguments().getString("factory");
            type=getArguments().getString("type");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_latest_reservation, container, false);
        ImageView imageView=view.findViewById(R.id.imageView3);
        TextView carName=view.findViewById(R.id.textViewCarName);
        TextView priceText=view.findViewById(R.id.textViewPrice);
        TextView dateText=view.findViewById(R.id.textViewDate);
        ConstraintLayout layout=view.findViewById(R.id.recentResLayout);
        //handling database
        sharedPrefManager= SharedPrefManager.getInstance(getActivity().getApplicationContext());
        String userEmail=sharedPrefManager.readString("userName","noValue");
        DataBaseHelper dataBaseHelper = DataBaseHelper.getInstance(getActivity().getApplicationContext(),"registration",null,1);
        Cursor reservations=dataBaseHelper.getLatestReservation(userEmail);
        try{
            if (reservations != null && reservations.getCount() > 0) {
                while (reservations.moveToNext()) {
                    //Toast.makeText(getActivity().getApplicationContext(), "Yes", Toast.LENGTH_SHORT).show();
                    @SuppressLint("Range") String date = reservations.getString(reservations.getColumnIndex("DATE_RESERVED"));
                    @SuppressLint("Range") String time = reservations.getString(reservations.getColumnIndex("TIME_RESERVED"));
                    @SuppressLint("Range") String VIN = reservations.getString(reservations.getColumnIndex("VIN"));
                    Cursor carDetails = dataBaseHelper.getCar(VIN);
                    if (carDetails != null && carDetails.moveToFirst()) {
                        @SuppressLint("Range") int model = carDetails.getInt(carDetails.getColumnIndex("MODEL"));
                        @SuppressLint("Range") double price = carDetails.getDouble(carDetails.getColumnIndex("PRICE"));
                        @SuppressLint("Range") String factory = carDetails.getString(carDetails.getColumnIndex("FACTORY"));
                        @SuppressLint("Range") String type = carDetails.getString(carDetails.getColumnIndex("TYPE"));
                        @SuppressLint("Range") String img = carDetails.getString(carDetails.getColumnIndex("IMG"));
                        Picasso.get().load(img).into(imageView);//to display images
                        carName.setText(factory+"-"+type+"-"+String.valueOf(model));
                        priceText.setText(String.valueOf(price)+"$/day");
                        dateText.setText(date+"-"+time);
                        this.factory=factory;
                        this.img=img;
                        this.type=type;
                        this.price=price;
                        this.model=model;
                        this.VIN=VIN;
                        this.email=userEmail;
                    }
                }
            }else{
                status=1;
                carName.setText("No Reservations yet");
                priceText.setText("");
                dateText.setText("");
            }
        }finally {
            if (reservations != null) {
                reservations.close();
            }
        }
        //handling viewing details of recent reservation
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation scaleAnimation = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.scale);
                scaleAnimation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                    }
                    @Override
                    public void onAnimationEnd(Animation animation) {
                        //animation ended, start the new activity
                        Intent intent = new Intent(getActivity().getApplicationContext(), viewCarDetails.class);
                        intent.putExtra("factory", factory);
                        intent.putExtra("type", type);
                        intent.putExtra("model", model);
                        intent.putExtra("price", price);
                        intent.putExtra("img", img);
                        intent.putExtra("email", email);
                        intent.putExtra("VIN", VIN);
                        startActivity(intent);
                    }
                    @Override
                    public void onAnimationRepeat(Animation animation) {
                    }
                });
                layout.startAnimation(scaleAnimation);
            }
        });
        return view;
    }
}