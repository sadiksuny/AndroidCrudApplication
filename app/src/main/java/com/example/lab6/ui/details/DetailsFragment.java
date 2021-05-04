package com.example.lab6.ui.details;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.lab6.R;

public class DetailsFragment extends Fragment {

    private DetailsViewModel detailsViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        detailsViewModel =
                new ViewModelProvider(this).get(DetailsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_details, container, false);
        final TextView textView = root.findViewById(R.id.text_notifications);
        detailsViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}