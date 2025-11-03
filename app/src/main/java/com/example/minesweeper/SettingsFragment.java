package com.example.minesweeper;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.minesweeper.R;

public class SettingsFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Spinner rowSpinner = view.findViewById(R.id.rowSpinner);
        Spinner colSpinner = view.findViewById(R.id.colSpinner);
        Spinner mineSpinner = view.findViewById(R.id.mineSpinner);
        Button playButton = view.findViewById(R.id.playButton);

        Integer[] sizes = {5,6,7,8,9,10};
        Integer[] percents = {10,15,20};

        ArrayAdapter<Integer> sizeAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, sizes);
        sizeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        rowSpinner.setAdapter(sizeAdapter);
        colSpinner.setAdapter(sizeAdapter);

        ArrayAdapter<Integer> mineAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, percents);
        mineAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mineSpinner.setAdapter(mineAdapter);

        playButton.setOnClickListener(v -> {
            int rows = (Integer) rowSpinner.getSelectedItem();
            int cols = (Integer) colSpinner.getSelectedItem();
            int minePercent = (Integer) mineSpinner.getSelectedItem();

            Bundle args = new Bundle();
            args.putInt("rows", rows);
            args.putInt("cols", cols);
            args.putInt("minePercent", minePercent);

            GameFragment gameFragment = new GameFragment();
            gameFragment.setArguments(args);

            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, gameFragment)
                    .addToBackStack(null)
                    .commit();
        });
    }
}
