package com.example.criminalintent.app;

import android.content.Context;
import android.util.Log;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by Donkey on 4/14/2015.
 */
public class CrimeLab {
    private static final String TAG = "CrimeLab";
    private static final String FILENAME = "crimes.jason";

    private ArrayList<Crime> mCrimes;
    private CriminalIntentJSONSerializer mSeralizer;


    private static  CrimeLab sCrimeLab;
    private Context mAppContext;

    private CrimeLab(Context mAppContext)
    {
            this.mAppContext = mAppContext;

            mSeralizer = new CriminalIntentJSONSerializer(mAppContext, FILENAME);
        try
        {
            mCrimes = mSeralizer.loadCrimes();
        }
        catch (Exception e)
        {
            mCrimes = new ArrayList<Crime>();
            Log.e(TAG, "Error Loading crimes : " , e);
        }

    }

    public static CrimeLab get(Context c)
    {
        if(sCrimeLab == null)
        {
            sCrimeLab = new CrimeLab(c.getApplicationContext());
        }
        return sCrimeLab;
    }

    public void addCrime(Crime c)
    {
        mCrimes.add(c);
    }

    public ArrayList<Crime> getCrime()
    {
        return mCrimes;
    }

    public Crime getCrime(UUID id)
    {
        for(Crime c : mCrimes)
        {
            if(c.getId().equals(id))
                return c;
        }
        return null;
    }

    public boolean saveCrimes()
    {
        try
        {
            mSeralizer.saveCrimes(mCrimes);
            Log.d(TAG,"crimes saved to file");
            return true;
        } catch (Exception e) {

            Log.e(TAG,"Error saving crimes : ", e);
            return false;

        }
    }

    public void deleteCrime(Crime c)
    {
        mCrimes.remove(c);
    }

}
