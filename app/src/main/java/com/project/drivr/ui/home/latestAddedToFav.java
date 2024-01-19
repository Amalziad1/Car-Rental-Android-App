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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.project.drivr.DataBaseHelper;
import com.project.drivr.R;
import com.project.drivr.SharedPrefManager;
import com.project.drivr.ui.car_menu.viewCarDetails;
import com.squareup.picasso.Picasso;

public class latestAddedToFav extends Fragment {
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
    private int status=0;//to know if there's favorites to show details or not
    public latestAddedToFav() {
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
        View view = inflater.inflate(R.layout.fragment_latest_added_to_fav, container, false);
        ImageView imageView=view.findViewById(R.id.imageView4);
        TextView carName=view.findViewById(R.id.textView2CarName);
        TextView priceText=view.findViewById(R.id.textView2Price);
        TextView dateText=view.findViewById(R.id.textView2Date);
        ConstraintLayout layout=view.findViewById(R.id.recFavLayout);
        //handling database
        sharedPrefManager= SharedPrefManager.getInstance(getActivity().getApplicationContext());
        String userEmail=sharedPrefManager.readString("userName","noValue");
        DataBaseHelper dataBaseHelper = DataBaseHelper.getInstance(getActivity().getApplicationContext(),"registration",null,1);
        Cursor fav=dataBaseHelper.getLatestFavorite(userEmail);
        try{
            if (fav != null && fav.getCount() > 0) {
                while (fav.moveToNext()) {
                    //Toast.makeText(getActivity().getApplicationContext(), "Yes", Toast.LENGTH_SHORT).show();
                    @SuppressLint("Range") String VIN = fav.getString(fav.getColumnIndex("VIN"));
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
                carName.setText("No Favorites yet");
                priceText.setText("");
                dateText.setText("");
                status=1;
            }
        }finally {
            if (fav != null) {
                fav.close();
            }
        }
        //handling viewing details of recent favorite
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(status==0){
                    Intent intent = new Intent(getActivity().getApplicationContext(), viewCarDetails.class);
                    intent.putExtra("model", model);
                    intent.putExtra("factory", factory);
                    intent.putExtra("type", type);
                    intent.putExtra("price", price);
                    intent.putExtra("img", img);
                    intent.putExtra("email", email);
                    intent.putExtra("VIN", VIN);
                    startActivity(intent);
                }
            }
        });
        return view;
    }
}