package com.example.criminalintent.app;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by Donkey on 4/27/2015.
 */
public class CrimeCameraActivity extends SingleFragmentActivity {

    @Override
    public void onCreate(Bundle savedInstance)
    {
        //hide the window title
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        //Hide the status bar amd other OS-level chrome
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstance);
    }


    @Override
    protected Fragment createFragment() {
        return new CrimeCameraFragment();
    }
}
