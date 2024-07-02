package com.fairshare.fair_division;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.fairshare.fair_division.ui.main.SectionsPagerAdapter;
import com.fairshare.fair_division.databinding.ActivitySessionBinding;
import com.google.android.material.tabs.TabLayoutMediator;

public class SessionActivity extends AppCompatActivity {

    private ActivitySessionBinding binding;

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.session_details_text, R.string.session_status_text, R.string.allocate_items_text};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivitySessionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), getLifecycle());
        ViewPager2 viewPager = binding.viewPager;
        viewPager.setAdapter(sectionsPagerAdapter);

        TabLayout tabs = binding.tabs;
        new TabLayoutMediator(tabs, viewPager, (tab, i) -> tab.setText(TAB_TITLES[i])).attach();

    }
}