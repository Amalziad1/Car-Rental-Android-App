package com.project.drivr.ui.favorites;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.project.drivr.DataBaseHelper;
import com.project.drivr.R;
import com.project.drivr.SharedPrefManager;
import com.project.drivr.databinding.FragmentFavoritesBinding;
import com.project.drivr.ui.car_menu.CarMenu;

import java.util.List;

public class FavoritesFragment extends Fragment {
    private FragmentFavoritesBinding binding;
    SharedPrefManager sharedPrefManager;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        FavoritesViewModel reservationsViewModel =
                new ViewModelProvider(this).get(FavoritesViewModel.class);

        binding = FragmentFavoritesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        //=========================
        sharedPrefManager= SharedPrefManager.getInstance(getActivity().getApplicationContext());
        String userEmail=sharedPrefManager.readString("userName","noValue");
        DataBaseHelper dataBaseHelper = DataBaseHelper.getInstance(getActivity().getApplicationContext(),"registration",null,1);
        final FragmentManager fragmentManager =  getParentFragmentManager();
        Cursor cursor=dataBaseHelper.getFavorites(userEmail);
        int tag=0;
        try {
            if (cursor != null && cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    @SuppressLint("Range") String VIN = cursor.getString(cursor.getColumnIndex("VIN"));
                    Cursor rows=dataBaseHelper.getCar(VIN);
                    if (rows!=null && rows.moveToFirst()){
                        //Toast.makeText(getActivity().getApplicationContext(), "count="+rows.getCount(), Toast.LENGTH_SHORT).show();
                        @SuppressLint("Range") String factory = rows.getString(rows.getColumnIndex("FACTORY"));
                        @SuppressLint("Range") String type = rows.getString(rows.getColumnIndex("TYPE"));
                        @SuppressLint("Range") String model = rows.getString(rows.getColumnIndex("MODEL"));
                        @SuppressLint("Range") int year = rows.getInt(rows.getColumnIndex("YEAR"));
                        @SuppressLint("Range") String transmission = rows.getString(rows.getColumnIndex("TRANSMISSION"));
                        @SuppressLint("Range") String fuel = rows.getString(rows.getColumnIndex("FUEL"));
                        @SuppressLint("Range") double mileage = rows.getDouble(rows.getColumnIndex("MILEAGE"));
                        @SuppressLint("Range") double price = rows.getDouble(rows.getColumnIndex("PRICE"));
                        @SuppressLint("Range") String img = rows.getString(rows.getColumnIndex("IMG"));
                        FavoriteFragment2 frag = FavoriteFragment2.newInstance(VIN,factory,type,model,year, transmission, fuel, mileage, price,img,userEmail);
                        fragmentManager.beginTransaction()
                                .add(R.id.favLayout, frag, String.valueOf("FavFrag"+tag))
                                .commit();
                        tag++;
                    }else{
                        Toast.makeText(getActivity().getApplicationContext(), "????", Toast.LENGTH_SHORT).show();
                    }
                }
            } else {
                Toast.makeText(getActivity().getApplicationContext(), "No favorites have been added yet!", Toast.LENGTH_LONG).show();
            }
        } finally {
            if (cursor != null) {
                cursor.close();
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