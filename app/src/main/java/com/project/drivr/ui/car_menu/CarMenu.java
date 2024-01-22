package com.project.drivr.ui.car_menu;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;

import java.sql.Time;
import java.util.Date;
import java.util.List;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.project.drivr.DataBaseHelper;
import com.project.drivr.R;

import com.project.drivr.ui.reservations.Reservation;
import com.squareup.picasso.Picasso;


public class CarMenu extends Fragment {
    private List<Car> carList;
    private List<Car> filteredList;
    private String VIN;
    private String factory;
    private String type;
    private String img;
    private int model;
    private double price;
    private String email;
    private boolean exist;//

    public CarMenu() {
        // Required empty public constructor
    }

    public static CarMenu newInstance(String VIN, String factory, String type, String img, int model, double price, String email) {
        CarMenu fragment = new CarMenu();
        Bundle args = new Bundle();
        args.putString("VIN", VIN);
        args.putString("factory", factory);
        args.putString("type", type);
        args.putString("img", img);
        args.putDouble("price", price);
        args.putInt("model", model);
        args.putString("email", email);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.VIN = getArguments().getString("VIN");
            this.model = getArguments().getInt("model");
            this.price = getArguments().getDouble("price");
            this.img = getArguments().getString("img");
            this.type = getArguments().getString("type");
            this.factory = getArguments().getString("factory");
            this.email = getArguments().getString("email");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_car_menu_frag, container, false);
        TextView name = view.findViewById(R.id.menuCarName);
        name.setText(factory + " : " + type);
        ImageView imageview = view.findViewById(R.id.photoImageView);
        Picasso.get().load(img).into(imageview);

        ConstraintLayout constraintLayout = view.findViewById(R.id.carDetailsConstraint);
        constraintLayout.setOnClickListener(new View.OnClickListener() {
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
                constraintLayout.startAnimation(scaleAnimation);
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
                PopUpReservation popUpReservation = PopUpReservation.newInstance(factory, type, model, price, img, email, VIN);
                popUpReservation.show(getChildFragmentManager(), "popup_fragment");
            }
        });
        exist = dataBaseHelper.isFavoriteExist(email, VIN);
        if (exist) {
            addFav.setCompoundDrawablesWithIntrinsicBounds(R.drawable.favorite_filled, 0, 0, 0);
        } else {
            addFav.setCompoundDrawablesWithIntrinsicBounds(R.drawable.favorite, 0, 0, 0);
        }
        addFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (exist) {
                    dataBaseHelper.removeFavorite(email, VIN);
                    addFav.setCompoundDrawablesWithIntrinsicBounds(R.drawable.favorite, 0, 0, 0);
                    Toast.makeText(getActivity().getApplicationContext(), "Removed from your Favorites", Toast.LENGTH_SHORT).show();
                    exist = false;
                } else {
                    dataBaseHelper.insertFavorite(email, VIN,currentDate,currentTime);
                    addFav.setCompoundDrawablesWithIntrinsicBounds(R.drawable.favorite_filled, 0, 0, 0);
                    Toast.makeText(getActivity().getApplicationContext(), "Added to your Favorites", Toast.LENGTH_SHORT).show();
                    exist = true;
                }
            }
        });

        return view;
    }



}