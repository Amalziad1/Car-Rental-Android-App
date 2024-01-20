package com.project.drivr.ui.find_us;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

        Button openInMapsButton = binding.openInMapsButton;
        Button callUs = binding.callUsButton;
        Button emailUs = binding.emailUsButton;

        openInMapsButton.setOnClickListener(v -> {
            Intent mapsIntent =new Intent();
            mapsIntent.setAction(Intent.ACTION_VIEW);
            mapsIntent.setData(Uri.parse("geo:33.513885850, 36.276516455"));
            startActivity(mapsIntent);
        });
        callUs.setOnClickListener(v -> {
            Intent dialIntent =new Intent();
            dialIntent.setAction(Intent.ACTION_DIAL);
            dialIntent.setData(Uri.parse("tel:0599000000"));
            startActivity(dialIntent);
        });
        emailUs.setOnClickListener(v -> {
            Intent gmailIntent =new Intent();
            gmailIntent.setAction(Intent.ACTION_SENDTO);
            gmailIntent.setType("message/rfc822");
            gmailIntent.setData(Uri.parse("mailto:"));
            gmailIntent.putExtra(Intent.EXTRA_EMAIL, "CarDealer@cars.com");
            startActivity(gmailIntent);
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}