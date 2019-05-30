package com.example.samplestickerapp.ui.home;

import android.os.Bundle;

import com.example.samplestickerapp.R;
import com.example.samplestickerapp.StickerApplication;
import com.example.samplestickerapp.ui.detail.AddStickerPackActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;

import android.view.MenuItem;
import android.widget.TextView;

public class MainActivity extends AddStickerPackActivity {
    static {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    navigateToChapters("home");
                    return true;
                case R.id.navigation_dashboard:
                    navigateToChapters("home");
                    return true;
                case R.id.navigation_notifications:
                    navigateToChapters("home");
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigateToChapters("home");
    }

    public void navigateToChapters(String type) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, StickerPackListFragment.newInstance(StickerApplication.getInstance().stickerPacks, type))
                .commit();

    }


}
