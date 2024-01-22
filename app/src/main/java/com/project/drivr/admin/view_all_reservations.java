package com.project.drivr.admin;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.project.drivr.DataBaseHelper;
import com.project.drivr.R;

public class view_all_reservations extends Fragment {
    private DataBaseHelper dataBaseHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_view_all_reservations, container, false);
        dataBaseHelper = DataBaseHelper.getInstance(getActivity().getApplicationContext(), "registration", null, 1);
        Cursor cursor=dataBaseHelper.getAllReservations();
        final FragmentManager fragmentManager =  getParentFragmentManager();
        if(cursor!=null && cursor.getCount()>0){
            while(cursor.moveToNext()){
                @SuppressLint("Range") String date = cursor.getString(cursor.getColumnIndex("DATE_RESERVED"));
                @SuppressLint("Range") String time = cursor.getString(cursor.getColumnIndex("TIME_RESERVED"));
                @SuppressLint("Range") String VIN = cursor.getString(cursor.getColumnIndex("VIN"));
                @SuppressLint("Range") String email = cursor.getString(cursor.getColumnIndex("EMAIL"));
                Cursor customer= dataBaseHelper.getUserByEmail(email);
                if (customer != null && customer.moveToFirst()){
                    @SuppressLint("Range") String fname = customer.getString(customer.getColumnIndex("FIRSTNAME"));
                    @SuppressLint("Range") String lname = customer.getString(customer.getColumnIndex("LASTNAME"));
                    String customerName=fname+" "+lname;
                    Cursor car= dataBaseHelper.getCar(VIN);
                    if (car != null && car.moveToFirst()){
                        @SuppressLint("Range") String factory = car.getString(car.getColumnIndex("FACTORY"));
                        @SuppressLint("Range") String type = car.getString(car.getColumnIndex("TYPE"));
                        @SuppressLint("Range") int model = car.getInt(car.getColumnIndex("MODEL"));
                        @SuppressLint("Range") String img = car.getString(car.getColumnIndex("IMG"));
                        String carName=factory+"-"+type+"-"+model;
                        view_all_reservations2 res=view_all_reservations2.newInstance(customerName,carName,time,date,img);
                        fragmentManager.beginTransaction()
                                .add(R.id.viewRLayout, res, "reserved")
                                .commit();
                    }
                }

            }
        }
        return view;
    }
}