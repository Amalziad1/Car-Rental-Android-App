package com.project.drivr.ui.favorites;
import android.content.Intent;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

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

import com.project.drivr.ui.car_menu.PopUpReservation;
import com.project.drivr.ui.car_menu.viewCarDetails;
import com.squareup.picasso.Picasso;


public class FavoriteFragment2 extends Fragment {
    private String VIN;
    private String factory;
    private String type;
    private double price;
    private int model;
    private String img;
    private String email;

    public FavoriteFragment2() {
        // Required empty public constructor
    }

    public static FavoriteFragment2 newInstance(String VIN,String factory, String type, int model, double price, String img,String email) {
        FavoriteFragment2 fragment = new FavoriteFragment2();
        Bundle args = new Bundle();
        args.putString("VIN", VIN);
        args.putString("Factory", factory);
        args.putString("Type", type);
        args.putInt("Model", model);
        args.putString("Image", img);
        args.putDouble("Price", price);
        args.putString("Email",email);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            VIN =getArguments().getString("VIN");
            factory = getArguments().getString("Factory");
            type = getArguments().getString("Type");
            model = getArguments().getInt("Model");
            price = getArguments().getDouble("Price");
            img=getArguments().getString("Image");
            email=getArguments().getString("Email");
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorite2, container, false);

        TextView name=view.findViewById(R.id.carNameView);
        ImageView imageview=view.findViewById(R.id.cImage);
        name.setText(factory + " : "+type);
        Picasso.get().load(img).into(imageview);//to display images
        Button unFav=view.findViewById(R.id.button3);
        Button reserve=view.findViewById(R.id.button2);
        DataBaseHelper dataBaseHelper = DataBaseHelper.getInstance(getActivity().getApplicationContext(),"registration",null,1);
        final FragmentManager fragmentManager =  getParentFragmentManager();
        unFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int f=dataBaseHelper.removeFavorite(VIN,email);
                if(f>0){
                    Toast.makeText(getActivity().getApplicationContext(), "Removed from Favorites", Toast.LENGTH_LONG).show();
                    fragmentManager.beginTransaction().remove(FavoriteFragment2.this).commit();
                    Toast.makeText(getActivity(), "Favorite removed", Toast.LENGTH_SHORT).show();
                }else if(f==-1){
                    Toast.makeText(getActivity().getApplicationContext(), "Error in deletion", Toast.LENGTH_LONG).show();
                }
            }
        });
        ConstraintLayout favCons=view.findViewById(R.id.favCons);
        favCons.setOnClickListener(new View.OnClickListener() {
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
                favCons.startAnimation(scaleAnimation);
            }
        });
        reserve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopUpReservation popUpReservation = PopUpReservation.newInstance(factory, type, model, price, img, email, VIN);
                popUpReservation.show(getChildFragmentManager(), "popup_fragment");
            }
        });

        return view;
    }
}