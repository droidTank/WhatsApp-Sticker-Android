package com.example.samplestickerapp.ui.home;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;

import com.example.samplestickerapp.R;
import com.example.samplestickerapp.ui.detail.AddStickerPackActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

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
                    navigateTo(StickerPackListFragment.HOME_TYPE);
                    return true;
                case R.id.navigation_dashboard:
                    navigateTo(StickerPackListFragment.TRENDING_TYPE);
                    return true;
                case R.id.navigation_notifications:
                    navigateTo(StickerPackListFragment.FAV_TYPE);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Home");
        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigateTo(StickerPackListFragment.HOME_TYPE);
    }

    public void navigateTo(int type) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frag_container, StickerPackListFragment.newInstance(type))
                .commit();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }



}
