package com.sstats.footballstats;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.sstats.footballstats.ui.AccountFragment;
import com.sstats.footballstats.ui.HomeFragment;
import com.sstats.footballstats.ui.SearchFragment;

public class MainActivity extends AppCompatActivity {

    private MaterialToolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_nav);
        bottomNav.setOnItemSelectedListener(item -> {
            Fragment fragment;
            String title;
            if (item.getItemId() == R.id.nav_home) {
                fragment = new HomeFragment();
                title = getString(R.string.app_name);
            } else if (item.getItemId() == R.id.nav_search) {
                fragment = new SearchFragment();
                title = getString(R.string.nav_search);
            } else {
                fragment = new AccountFragment();
                title = getString(R.string.nav_account);
            }
            switchFragment(fragment, title);
            return true;
        });

        if (savedInstanceState == null) {
            bottomNav.setSelectedItemId(R.id.nav_home);
        }
    }

    private void switchFragment(@NonNull Fragment fragment, @NonNull String title) {
        toolbar.setTitle(title);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }
}
