package com.project.drivr.ui.car_menu;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.fragment.app.DialogFragment;

import com.project.drivr.R;
import com.squareup.picasso.Picasso;

public class PopUpReservation extends DialogFragment {

    private String factory;
    private String type;
    private int model;
    private double price;
    private String img;
    private String email;
    private String VIN;

    public PopUpReservation() {}

    public static PopUpReservation newInstance(String factory, String type, int model, double price, String img, String email, String VIN) {
        PopUpReservation fragment = new PopUpReservation();
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
        reserve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //database to insert to reserve
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
