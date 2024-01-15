package com.project.drivr.ui.reservations;
import static android.content.Intent.getIntent;

import com.project.drivr.MainActivity;
import com.project.drivr.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Toast;

import com.project.drivr.DataBaseHelper;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import com.project.drivr.SharedPrefManager;
import com.project.drivr.databinding.FragmentReservationsBinding;
import com.project.drivr.login;


public class ReservationsFragment extends Fragment {
    SharedPrefManager sharedPrefManager;

    private FragmentReservationsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ReservationsViewModel ReservationsViewModel =
                new ViewModelProvider(this).get(ReservationsViewModel.class);
        binding = FragmentReservationsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        //==========================
        sharedPrefManager=SharedPrefManager.getInstance(getActivity().getApplicationContext());
        String userEmail=sharedPrefManager.readString("userName","noValue");

        DataBaseHelper dataBaseHelper = new DataBaseHelper(getActivity().getApplicationContext(),"registration",null,1);
        final FragmentManager fragmentManager =  getParentFragmentManager();
        Cursor rows = dataBaseHelper.getReservation(userEmail);
        try {
            if (rows != null && rows.getCount() > 0) {
                while (rows.moveToNext()) {
                    @SuppressLint("Range") String date = rows.getString(rows.getColumnIndex("DATE_RESERVED"));
                    @SuppressLint("Range") String time = rows.getString(rows.getColumnIndex("TIME_RESERVED"));
                    @SuppressLint("Range") String VIN = rows.getString(rows.getColumnIndex("VIN"));
                    Cursor carDetails = dataBaseHelper.getCar(VIN);
                    if (carDetails != null && carDetails.moveToFirst()) {
                        @SuppressLint("Range") String model = carDetails.getString(carDetails.getColumnIndex("MODEL"));
                        @SuppressLint("Range") double price = carDetails.getDouble(carDetails.getColumnIndex("PRICE"));
                        @SuppressLint("Range") String factory = carDetails.getString(carDetails.getColumnIndex("FACTORY"));
                        @SuppressLint("Range") String type = carDetails.getString(carDetails.getColumnIndex("TYPE"));
                        @SuppressLint("Range") String img = carDetails.getString(carDetails.getColumnIndex("IMG"));
                        reserved res = reserved.newInstance(factory, type, date, time, model, price, img);
                        fragmentManager.beginTransaction()
                                .add(R.id.root, res, "reserved")
                                .commit();
                    } else {
                    }
                }
            } else {
                Toast.makeText(getActivity().getApplicationContext(), "No reservations have been made yet!", Toast.LENGTH_LONG).show();
            }
        } finally {
            if (rows != null) {
                rows.close();
            }
        }

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}