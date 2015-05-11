package com.example.criminalintent.app;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Camera;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import java.util.Date;
import java.util.UUID;
import java.util.concurrent.Callable;

/**
 * Created by Donkey on 4/14/2015.
 */
public class CrimeFragment extends Fragment {

    private Crime       mCrime;
    private EditText    mTitleField;
    private Button      mDateButton;
    private CheckBox    mSolvedCheckBox;
    private ImageButton mPhotoButton;

    public static final String EXTRA_CRIME_ID = "com.example.criminalintent.crime_id";

    private static final String DIALOG_DATE = "date";
    private static final int    REQUEST_DATE = 0;



    public static CrimeFragment newInstance(UUID crimeId)
    {
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_CRIME_ID, crimeId);

        CrimeFragment fragment = new CrimeFragment();
    fragment.setArguments(args);


        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        UUID crimeId = (UUID)getArguments().getSerializable(EXTRA_CRIME_ID);
        mCrime = CrimeLab.get(getActivity()).getCrime(crimeId);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if(resultCode != Activity.RESULT_OK)
            return;
        if(requestCode == REQUEST_DATE) {
            Date date = (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            mCrime.setDate(date);

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch(item.getItemId())
        {
            case android.R.id.home:
                //To be implemented
                if(NavUtils.getParentActivityName(getActivity()) != null)
                {
                    NavUtils.navigateUpFromSameTask(getActivity());
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onPause()
    {
        super.onPause();;
        CrimeLab.get(getActivity()).saveCrimes();
    }


    private void updateDate()
    {
        mDateButton.setText(mCrime.getDate().toString());
    }

    @TargetApi(11)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceSate)
    {


        View v = inflater.inflate(R.layout.fragment_crime, parent, false);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
        {
            if(NavUtils.getParentActivityName(getActivity())!= null) {
                getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
            }
        }

        mTitleField = (EditText)v.findViewById(R.id.crime_title);
        mTitleField.setText(mCrime.getTitle());
        mTitleField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                mCrime.setTitle(s.toString());
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //Button needs to display the date in the crime mDate field
        mDateButton = (Button)v.findViewById(R.id.crime_date); //equating whatever ID its in into variable
       //setting the variable to show text
        mDateButton.setOnClickListener(new View.OnClickListener() {
                                           @Override
                                           public void onClick(View v) {
                                               FragmentManager fm = getActivity().getSupportFragmentManager();
                                                DatePickerFragment dialog = DatePickerFragment.newInstance(mCrime.getDate());
                                               dialog.setTargetFragment(CrimeFragment.this, REQUEST_DATE);
                                               dialog.show(fm, DIALOG_DATE);
                                           }
                                       });




        //setting a listener to the checkbox
        mSolvedCheckBox = (CheckBox)v.findViewById(R.id.crime_solved);
        mSolvedCheckBox.setChecked(mCrime.isSolved());
        mSolvedCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //Set the crimes solved property
                mCrime.setSolved(isChecked);
            }
        });

        //listener for image button
        mPhotoButton = (ImageButton)v.findViewById(R.id.crime_imageButton);
        mPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 Intent i = new Intent(getActivity(), CrimeCameraActivity.class);
                startActivity(i);
            }
        });

        //if camera is not available , disable camera functionality
        PackageManager pm = getActivity().getPackageManager();
        boolean hasACamera = pm.hasSystemFeature(PackageManager.FEATURE_CAMERA)
                ||pm.hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT)
                ||(Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD && android.hardware.Camera.getNumberOfCameras() > 0);
        if(!hasACamera)
            {
                mPhotoButton.setEnabled(false);
            }



        return v;
    }
}

