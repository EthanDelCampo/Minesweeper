package com.example.minesweeper;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, new TitleFragment())
                    .commit();
        }
    }

    public void switchToSettings() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, new SettingsFragment())
                .addToBackStack(null)
                .commit();
    }

    public void switchToGame() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, new GameFragment())
                .addToBackStack(null)
                .commit();
    }
}
