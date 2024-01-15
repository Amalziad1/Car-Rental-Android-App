package com.project.drivr.ui.favorites;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.project.drivr.R;
import com.project.drivr.ui.reservations.reserved;
import com.squareup.picasso.Picasso;


public class carFavoriteFragment extends Fragment {

    private String factory;
    private String type;
    private String date;
    private String time;
    private double price;
    private String model;
    private String img;

    public carFavoriteFragment() {
        // Required empty public constructor
    }

    public static reserved newInstance(String factory, String type, String date, String time, String model, double price, String img) {
        reserved fragment = new reserved();
        Bundle args = new Bundle();
        args.putString("Factory", factory);
        args.putString("Type", type);
        args.putString("Date", date);
        args.putString("Time", time);
        args.putString("Model", model);
        args.putString("Image", img);
        args.putDouble("Price", price);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            factory = getArguments().getString("Factory");
            type = getArguments().getString("Type");
            date = getArguments().getString("Date");
            time = getArguments().getString("Time");
            model = getArguments().getString("Model");
            price = getArguments().getDouble("Price");
            img=getArguments().getString("Image");
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_car_favorite, container, false);
        TextView name=view.findViewById(R.id.favCarName);
        ImageView imageview=view.findViewById(R.id.photoImageView);
        name.setText(factory + " : "+type);
        Picasso.get().load(img).into(imageview);//to display images\
        Button unFav=view.findViewById(R.id.unFav);
        Button reserve=view.findViewById(R.id.R);
        unFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        return view;
    }
}