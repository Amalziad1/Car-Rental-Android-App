package com.project.drivr.ui.home;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.project.drivr.R;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class historyFragment extends Fragment {
    private String carDealerName;
    private String history;//reading from txt file

    public historyFragment() {
        // Required empty public constructor
    }
    public static historyFragment newInstance(String carDealerName, String history) {
        historyFragment fragment = new historyFragment();
        Bundle args = new Bundle();
        args.putString("carDealerName", carDealerName);
        args.putString("history", history);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            carDealerName = getArguments().getString("carDealerName");
            history = getArguments().getString("history");
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history_fragment, container, false);
        TextView carName=view.findViewById(R.id.carDealerName);
        TextView histo=view.findViewById(R.id.history);
        String str="CarDealer Motors, established in 2010, has built a legacy centered on passion, quality, and exceptional service in the automotive industry. ";
        histo.setText(str);
        carName.setText("Car Dealer Motors");
        return view;
    }
}