package com.project.drivr.ui.find_us;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.project.drivr.databinding.FragmentFindUsBinding;

public class FindUsFragment extends Fragment {

    private FragmentFindUsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        FindUsViewModel FindUsViewModel =
                new ViewModelProvider(this).get(FindUsViewModel.class);

        binding = FragmentFindUsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // final TextView textView = binding.textFindUs;
        // FindUsViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}