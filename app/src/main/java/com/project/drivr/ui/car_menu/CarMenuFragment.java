package com.project.drivr.ui.car_menu;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.project.drivr.DataBaseHelper;
import com.project.drivr.R;
import com.project.drivr.SharedPrefManager;
import com.project.drivr.databinding.FragmentCarMenuBinding;
import com.project.drivr.ui.reservations.reserved;

import java.util.Objects;

public class CarMenuFragment extends Fragment {
    SharedPrefManager sharedPrefManager;
    private FragmentCarMenuBinding binding;
    private Cursor rows;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        CarMenuViewModel carMenuViewModel =
                new ViewModelProvider(this).get(CarMenuViewModel.class);
        binding = FragmentCarMenuBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // fetch database helper
        sharedPrefManager= SharedPrefManager.getInstance(requireActivity().getApplicationContext());
        String userEmail=sharedPrefManager.readString("userName","noValue");
        DataBaseHelper dataBaseHelper = DataBaseHelper.getInstance(requireActivity().getApplicationContext(),"registration",null,1);
        final FragmentManager fragmentManager =  getParentFragmentManager();
        rows = dataBaseHelper.getAllCars();

        FloatingActionButton actionButton = binding.fab;
        View popupView = LayoutInflater.from(getActivity()).inflate(R.layout.filtering_menu, null, true);
        PopupWindow popupWindow = new PopupWindow(popupView, 1000, 210, true);
        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.setAnimationStyle(16973826);
                popupWindow.showAtLocation(root, Gravity.BOTTOM, 0, 0);
            }
        });
        // Determine filters
        EditText filterValueField = popupView.findViewById(R.id.filterValueEditText);
        Spinner filterTypeSpinner = popupView.findViewById(R.id.filterType);

        //implementing filter spinner
        ArrayAdapter<CharSequence> filterAdapter = ArrayAdapter.createFromResource(
                requireActivity().getApplicationContext(),
                R.array.car_filters,
                android.R.layout.simple_spinner_item
        );
        filterAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        filterTypeSpinner.setAdapter(filterAdapter);
        filterValueField.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    // Perform action on key press
                    removeCars(rows, fragmentManager);
                    String filter = filterTypeSpinner.getSelectedItem().toString();
                    if (filter.equals("Filter")) {
                        rows = dataBaseHelper.getAllCars();
                    }
                    else {
                        String filterValue = filterValueField.getText().toString();
                        rows = dataBaseHelper.getCarsFiltered(filter, filterValue);
                    }
                    displayCars(rows, fragmentManager, userEmail);
                    return true;
                }
                return false;

            }
        });
        displayCars(rows, fragmentManager, userEmail);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void displayCars(Cursor rows, FragmentManager fragmentManager, String userEmail) {
        if (rows != null && rows.getCount() > 0 && !rows.isClosed()) {
            rows.moveToFirst();
            do {
                @SuppressLint("Range") String VIN = rows.getString(rows.getColumnIndex("VIN"));
                @SuppressLint("Range") String factory = rows.getString(rows.getColumnIndex("FACTORY"));
                @SuppressLint("Range") String type = rows.getString(rows.getColumnIndex("TYPE"));
                @SuppressLint("Range") String model = rows.getString(rows.getColumnIndex("MODEL"));
                @SuppressLint("Range") int year = rows.getInt(rows.getColumnIndex("YEAR"));
                @SuppressLint("Range") String transmission = rows.getString(rows.getColumnIndex("TRANSMISSION"));
                @SuppressLint("Range") String fuel = rows.getString(rows.getColumnIndex("FUEL"));
                @SuppressLint("Range") double mileage = rows.getDouble(rows.getColumnIndex("MILEAGE"));
                @SuppressLint("Range") double price = rows.getDouble(rows.getColumnIndex("PRICE"));
                @SuppressLint("Range") String img = rows.getString(rows.getColumnIndex("IMG"));
                //CarMenu car = (CarMenu) fragmentManager.findFragmentByTag("Car");
                CarMenu car = CarMenu.newInstance(VIN,factory,type,img,model, year, transmission, fuel, mileage, price,userEmail);
                fragmentManager.beginTransaction()
                        .add(R.id.menuLayout, car, VIN)
                        .commit();
            } while (rows.moveToNext());
        }
    }
    private void removeCars(Cursor rows, FragmentManager fragmentManager) {
        if (rows != null && rows.getCount() > 0 && !rows.isClosed()) {
            rows.moveToFirst();
            do {
                String VIN = rows.getString(rows.getColumnIndexOrThrow("VIN"));
                fragmentManager.beginTransaction().remove(Objects.requireNonNull(fragmentManager.findFragmentByTag(VIN))).commit();
            } while (rows.moveToNext());
        }
    }
}