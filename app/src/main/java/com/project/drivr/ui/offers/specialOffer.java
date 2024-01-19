package com.project.drivr.ui.offers;

import android.graphics.Paint;
import android.os.Bundle;

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
import com.project.drivr.ui.car_menu.CarMenu;
import com.project.drivr.ui.car_menu.PopUpReservation;
import com.squareup.picasso.Picasso;

public class specialOffer extends Fragment {

    private String VIN;
    private String factory;
    private String type;
    private String img;
    private int model;
    private double price;
    private String email;
    private boolean exist;
    private String endDate;
    private double ratio;

    public specialOffer() {
        // Required empty public constructor
    }
    public static specialOffer newInstance(String VIN, String factory, String type, String img, int model, double price, String email,String endDate,double ratio) {
        specialOffer fragment = new specialOffer();
        Bundle args = new Bundle();
        args.putString("VIN", VIN);
        args.putString("factory", factory);
        args.putString("type", type);
        args.putString("img", img);
        args.putDouble("price", price);
        args.putInt("model", model);
        args.putString("email", email);
        args.putString("endDate",endDate);
        args.putDouble("ratio", ratio);
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
            this.endDate=getArguments().getString("endDate");
            this.ratio=getArguments().getDouble("ratio");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_special_offer, container, false);
        TextView carName=view.findViewById(R.id.SpecialCarName);
        TextView oldPrice=view.findViewById(R.id.SOriginalPrice);
        TextView newPrice=view.findViewById(R.id.SpecialOfferPrice);
        TextView datePeriod=view.findViewById(R.id.SpecialTimePeriod);
        ImageView imageView=view.findViewById(R.id.imageView7);
        Button reserve=view.findViewById(R.id.button4);
        Button fav=view.findViewById(R.id.button5);

        carName.setText(factory+"-"+type+"-"+String.valueOf(model));
        oldPrice.setText(String.valueOf(price)+"$/day");
        oldPrice.setPaintFlags(oldPrice.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
        newPrice.setText(String.valueOf(ratio*price)+"$day");
        Picasso.get().load(img).into(imageView);
        datePeriod.setText("Until "+endDate);
        reserve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopUpReservation popUpReservation = PopUpReservation.newInstance(factory, type, model, price, img, email, VIN);
                popUpReservation.show(getChildFragmentManager(), "popup_fragment");
            }
        });
        DataBaseHelper dataBaseHelper = DataBaseHelper.getInstance(getActivity().getApplicationContext(),"registration",null,1);
        exist = dataBaseHelper.isFavoriteExist(email, VIN);
        if (exist) {
            fav.setCompoundDrawablesWithIntrinsicBounds(R.drawable.favorite_filled, 0, 0, 0);
        } else {
            fav.setCompoundDrawablesWithIntrinsicBounds(R.drawable.favorite, 0, 0, 0);
        }
        fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (exist) {
                    dataBaseHelper.removeFavorite(email, VIN);
                    fav.setCompoundDrawablesWithIntrinsicBounds(R.drawable.favorite, 0, 0, 0);
                    Toast.makeText(getActivity().getApplicationContext(), "Removed from your Favorites", Toast.LENGTH_SHORT).show();
                    exist = false;
                } else {
                    dataBaseHelper.insertFavorite(email, VIN);
                    fav.setCompoundDrawablesWithIntrinsicBounds(R.drawable.favorite_filled, 0, 0, 0);
                    //Animation animation = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.scale);

                    Toast.makeText(getActivity().getApplicationContext(), "Added to your Favorites", Toast.LENGTH_SHORT).show();
                    exist = true;
                }
            }
        });
        return view;
    }
}