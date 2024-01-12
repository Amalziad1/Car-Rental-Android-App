package com.project.drivr.ui.car_menu;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.project.drivr.databinding.FragmentCarMenuBinding;

public class CarMenuFragment extends Fragment {

    private FragmentCarMenuBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        CarMenuViewModel carMenuViewModel =
                new ViewModelProvider(this).get(CarMenuViewModel.class);

        binding = FragmentCarMenuBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // final TextView textView = binding.textGallery;
        // carMenuViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}