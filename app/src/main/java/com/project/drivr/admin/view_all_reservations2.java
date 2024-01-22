package com.project.drivr.admin;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.project.drivr.R;
import com.squareup.picasso.Picasso;

public class view_all_reservations2 extends Fragment {
    private String customerName;
    private String carName;
    private String time;
    private String date;
    private String img;
    public view_all_reservations2() {
        // Required empty public constructor
    }

    public static view_all_reservations2 newInstance(String customerName,String carName,String time,String date,String img) {
        view_all_reservations2 fragment = new view_all_reservations2();
        Bundle args = new Bundle();
        args.putString("customerName",customerName);
        args.putString("carName",carName);
        args.putString("time",time);
        args.putString("date",date);
        args.putString("img",img);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            customerName = getArguments().getString("customerName");
            carName=getArguments().getString("carName");
            time=getArguments().getString("time");
            date=getArguments().getString("date");
            img=getArguments().getString("img");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_view_all_reservations2, container, false);
        TextView cName=view.findViewById(R.id.cName);
        TextView cDate=view.findViewById(R.id.cDate);
        TextView cTime=view.findViewById(R.id.cTime);
        TextView carNameView=view.findViewById(R.id.carNameView);
        ImageView imageView=view.findViewById(R.id.cImage);
        cName.setText(customerName);
        cDate.setText(date);
        cTime.setText(time);
        carNameView.setText(carName);
        Picasso.get().load(img).into(imageView);

        return view;
    }
}