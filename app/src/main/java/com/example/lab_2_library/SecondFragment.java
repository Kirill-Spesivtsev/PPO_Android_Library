package com.example.lab_2_library;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

public class SecondFragment extends Fragment {

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        return inflater.inflate(R.layout.fragment_second, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.button_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(SecondFragment.this)
                        .navigate(R.id.action_SecondFragment_to_FirstFragment);
            }
        });

        view.findViewById(R.id.button_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextInputLayout til = requireView().findViewById(R.id.textInputMinimalYear);
                try {
                    if(!til.getEditText().getText().toString().equals("")){
                        int val = Integer.parseInt(til.getEditText().getText().toString());
                        ((MainActivity) requireActivity()).filterQuery = " where "
                                + DatabaseHelper.COLUMN_YEAR + " > " + val;
                    }
                    else{
                        ((MainActivity) requireActivity()).filterQuery = "";
                    }
                    Toast.makeText(getActivity(),"Фильтр установлен", Toast.LENGTH_SHORT).show();
                }

                catch (NumberFormatException e){
                    Toast.makeText(getActivity(),"Невозможно установить фильтр", Toast.LENGTH_SHORT).show();
                }
                NavHostFragment.findNavController(SecondFragment.this)
                        .navigate(R.id.action_SecondFragment_to_FirstFragment);
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
    }
}