package com.example.criminalintent.app;

import android.support.v4.app.Fragment;

/**
 * Created by Donkey on 4/18/2015.
 */
public class CrimeListActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return new  CrimeListFragment();
    }



}
