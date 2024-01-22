package com.project.drivr.ui.offers;

import static androidx.core.content.ContextCompat.getSystemService;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.project.drivr.DataBaseHelper;
import com.project.drivr.R;
import com.project.drivr.SharedPrefManager;
import com.project.drivr.databinding.FragmentOffersBinding;
import com.project.drivr.ui.car_menu.CarMenu;

import java.time.LocalDateTime;

public class OffersFragment extends Fragment {
    SharedPrefManager sharedPrefManager;

    private FragmentOffersBinding binding;
    private int year=2024;
    private int month=1;
    private int day=15;
    private double ratio=0.3;//30% discount


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        OffersViewModel reservationsViewModel =
                new ViewModelProvider(this).get(OffersViewModel.class);

        binding = FragmentOffersBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
//
        //
        sharedPrefManager= SharedPrefManager.getInstance(getActivity().getApplicationContext());
        String userEmail=sharedPrefManager.readString("userName","noValue");
        DataBaseHelper dataBaseHelper = DataBaseHelper.getInstance(getActivity().getApplicationContext(),"registration",null,1);
        final FragmentManager fragmentManager =  getParentFragmentManager();
        //getting current time and date
        LocalDateTime currentDateTime = LocalDateTime.now();
        String strMonth=sharedPrefManager.readString("month",null);
        if(strMonth!=null){
            this.month=Integer.parseInt(strMonth);
        }
        //specifying the end date of the offer
        LocalDateTime endDate = LocalDateTime.of(year, month, day, 0, 0);
        //checking if the current date and time are before the specified end date and time
        if (currentDateTime.isAfter(endDate)) {
            //changing the month
            this.month=this.month+1;
            sharedPrefManager.writeString("month",String.valueOf(this.month));
            removeAllFragments(R.id.offersLayout);//removing all the fragment in the layout
            Cursor randomCar= dataBaseHelper.getRandomCarCursor();
            endDate=LocalDateTime.of(year,month,day,0,0);
            try{
                if (randomCar != null && randomCar.getCount() > 0) {
                    while (randomCar.moveToNext()) {
                        @SuppressLint("Range") String VIN = randomCar.getString(randomCar.getColumnIndex("VIN"));
                        @SuppressLint("Range") String factory = randomCar.getString(randomCar.getColumnIndex("FACTORY"));
                        @SuppressLint("Range") String type = randomCar.getString(randomCar.getColumnIndex("TYPE"));
                        @SuppressLint("Range") String model = randomCar.getString(randomCar.getColumnIndex("MODEL"));
                        @SuppressLint("Range") int year = randomCar.getInt(randomCar.getColumnIndex("YEAR"));
                        @SuppressLint("Range") String transmission = randomCar.getString(randomCar.getColumnIndex("TRANSMISSION"));
                        @SuppressLint("Range") String fuel = randomCar.getString(randomCar.getColumnIndex("FUEL"));
                        @SuppressLint("Range") double mileage = randomCar.getDouble(randomCar.getColumnIndex("MILEAGE"));
                        @SuppressLint("Range") double price = randomCar.getDouble(randomCar.getColumnIndex("PRICE"));
                        @SuppressLint("Range") String img = randomCar.getString(randomCar.getColumnIndex("IMG"));
                        specialOffer car = specialOffer.newInstance(VIN,factory,type,img,model,year, transmission, fuel, mileage, price,userEmail,String.valueOf(endDate),ratio);
                        fragmentManager.beginTransaction()
                                .add(R.id.offersLayout, car, "offer")
                                .commit();
                        //using shared preference to save data for later
                        sharedPrefManager.writeString("VIN",VIN);

                    }
                } else {
                    //Toast.makeText(getActivity().getApplicationContext(), "No reservations have been made yet!", Toast.LENGTH_LONG).show();
                }
            }finally {
                if (randomCar != null) {
                    randomCar.close();
                }
            }
        }else{
            sharedPrefManager.writeString("month",null);//do not change month
            //get stored data in shared preference
            String VIN=sharedPrefManager.readString("VIN",null);
            if(VIN != null){
                Cursor car=dataBaseHelper.getCar(VIN);
                try {
                    if (car != null && car.getCount() > 0) {
                        while (car.moveToNext()) {
                            @SuppressLint("Range") String factory = car.getString(car.getColumnIndex("FACTORY"));
                            @SuppressLint("Range") String type = car.getString(car.getColumnIndex("TYPE"));
                            @SuppressLint("Range") String model = car.getString(car.getColumnIndex("MODEL"));
                            @SuppressLint("Range") int year = car.getInt(car.getColumnIndex("YEAR"));
                            @SuppressLint("Range") String transmission = car.getString(car.getColumnIndex("TRANSMISSION"));
                            @SuppressLint("Range") String fuel = car.getString(car.getColumnIndex("FUEL"));
                            @SuppressLint("Range") double mileage = car.getDouble(car.getColumnIndex("MILEAGE"));
                            @SuppressLint("Range") double price = car.getDouble(car.getColumnIndex("PRICE"));
                            @SuppressLint("Range") String img = car.getString(car.getColumnIndex("IMG"));
                            specialOffer spcialcar = specialOffer.newInstance(VIN,factory,type,img,model, year, transmission, fuel, mileage, price,userEmail,String.valueOf(endDate),ratio);
                            fragmentManager.beginTransaction()
                                    .add(R.id.offersLayout, spcialcar, "offer")
                                    .commit();
                        }
                    }
                }finally {
                    if(car !=null){
                        car.close();
                    }
                }
            }
        }
        return root;
    }
    private void removeAllFragments(int containerId) {
        FragmentManager fragmentManager = getChildFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        for (Fragment fragment : fragmentManager.getFragments()) {
            if (fragment != null && fragment.getId() == containerId) {
                fragmentTransaction.remove(fragment);
            }
        }

        // Commit the transaction to remove the fragments
        fragmentTransaction.commit();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}