package com.project.drivr.ui.car_menu;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import com.project.drivr.DataBaseHelper;
import com.project.drivr.R;
import com.project.drivr.SharedPrefManager;
import com.project.drivr.databinding.FragmentCarMenuBinding;
import com.project.drivr.ui.reservations.reserved;

public class CarMenuFragment extends Fragment {
    SharedPrefManager sharedPrefManager;
    private FragmentCarMenuBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        CarMenuViewModel carMenuViewModel =
                new ViewModelProvider(this).get(CarMenuViewModel.class);
        binding = FragmentCarMenuBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        //---------reading data
        sharedPrefManager= SharedPrefManager.getInstance(getActivity().getApplicationContext());
        String userEmail=sharedPrefManager.readString("userName","noValue");
        DataBaseHelper dataBaseHelper = new DataBaseHelper(getActivity().getApplicationContext(),"registration",null,1);
        final FragmentManager fragmentManager =  getParentFragmentManager();
        Cursor rows = dataBaseHelper.getAllCars();
        try {
            if (rows != null && rows.getCount() > 0) {
                while (rows.moveToNext()) {
                    @SuppressLint("Range") String VIN = rows.getString(rows.getColumnIndex("VIN"));
                    @SuppressLint("Range") String factory = rows.getString(rows.getColumnIndex("FACTORY"));
                    @SuppressLint("Range") String type = rows.getString(rows.getColumnIndex("TYPE"));
                    @SuppressLint("Range") int model = rows.getInt(rows.getColumnIndex("MODEL"));
                    @SuppressLint("Range") double price = rows.getDouble(rows.getColumnIndex("PRICE"));
                    @SuppressLint("Range") String img = rows.getString(rows.getColumnIndex("IMG"));
                    //CarMenu car = (CarMenu) fragmentManager.findFragmentByTag("Car");
                    CarMenu car = CarMenu.newInstance(VIN,factory,type,img,model,price,userEmail);
                    fragmentManager.beginTransaction()
                            .add(R.id.menuLayout, car, "Car")
                            .commit();
                }
            } else {
                //Toast.makeText(getActivity().getApplicationContext(), "No reservations have been made yet!", Toast.LENGTH_LONG).show();
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