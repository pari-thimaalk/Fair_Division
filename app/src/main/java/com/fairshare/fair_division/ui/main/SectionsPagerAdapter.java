package com.fairshare.fair_division.ui.main;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.fairshare.fair_division.AllocateSessionFragment;
import com.fairshare.fair_division.R;
import com.fairshare.fair_division.SessionDetailsFragment;
import com.fairshare.fair_division.SessionStatusFragment;

import org.checkerframework.checker.units.qual.A;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentStateAdapter {



    public SectionsPagerAdapter(FragmentManager fm, Lifecycle lifecycle) {
        super(fm, lifecycle);


    }




    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if(position == 0) {
            return new SessionDetailsFragment();
        }

        if(position == 2) {
            return new AllocateSessionFragment();
        }
        return new SessionStatusFragment();
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}