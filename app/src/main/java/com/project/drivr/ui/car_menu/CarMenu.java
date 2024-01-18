package com.project.drivr.ui.car_menu;

import android.content.Intent;
import android.os.Bundle;

import java.sql.Time;
import java.util.Date;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.project.drivr.DataBaseHelper;
import com.project.drivr.R;

import com.project.drivr.ui.reservations.Reservation;
import com.squareup.picasso.Picasso;


public class CarMenu extends Fragment {
    private String VIN;
    private String factory;
    private String type;
    private String img;
    private  int model;
    private double price;
    private String email;
    public CarMenu() {
        // Required empty public constructor
    }
    public static CarMenu newInstance(String VIN, String factory,String type,String img,int model,double price,String email) {
        CarMenu fragment = new CarMenu();
        Bundle args = new Bundle();
        args.putString("VIN", VIN);
        args.putString("factory", factory);
        args.putString("type",type);
        args.putString("img",img);
        args.putDouble("price",price);
        args.putInt("model",model);
        args.putString("email",email);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.VIN = getArguments().getString("VIN");
            this.model = getArguments().getInt("model");
            this.price=getArguments().getDouble("price");
            this.img=getArguments().getString("img");
            this.type=getArguments().getString("type");
            this.factory=getArguments().getString("factory");
            this.email=getArguments().getString("email");
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_car_menu_frag, container, false);
        TextView name=view.findViewById(R.id.menuCarName);
        name.setText(factory+" : "+type);
        ImageView imageview=view.findViewById(R.id.photoImageView);
        Picasso.get()
                .load(img)
                .fit()
                .into(imageview);
        ConstraintLayout constraintLayout=view.findViewById(R.id.carDetailsConstraint);
        constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
        });

        Button addFav=view.findViewById(R.id.AddFav);
        Button addRes=view.findViewById(R.id.Reserve);
        DataBaseHelper dataBaseHelper = DataBaseHelper.getInstance(getActivity().getApplicationContext(),"registration",null,1);
        Reservation reserve=new Reservation();
        long currentTimeMillis = System.currentTimeMillis();
        Date currentDate = new Date(currentTimeMillis);
        Time currentTime = new Time(currentDate.getTime());
        reserve.setDate(currentDate);
        reserve.setTime(currentTime);
        reserve.setEmail(email);
        reserve.setVIN(VIN);
        addRes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataBaseHelper.insertReservation(reserve);
            }
        });
        addFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataBaseHelper.insertFavorite(email,VIN);
                Toast.makeText(getActivity().getApplicationContext(), "Added to favorites", Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }
}