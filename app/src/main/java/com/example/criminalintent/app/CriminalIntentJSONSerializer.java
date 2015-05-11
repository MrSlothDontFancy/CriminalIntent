package com.example.criminalintent.app;

import android.content.Context;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONTokener;


import java.io.*;
import java.util.ArrayList;

/**
 * Created by Donkey on 4/25/2015.
 */
public class CriminalIntentJSONSerializer {

    private Context mContext;
    private String  mFileName;

    public CriminalIntentJSONSerializer(Context c , String f)
    {
        mContext  = c;
        mFileName = f;
    }

    //loading crimes to filesystem
    public ArrayList<Crime> loadCrimes() throws IOException, JSONException
    {
        ArrayList<Crime> crimes = new ArrayList<Crime>();
        BufferedReader reader = null;
        try
        {
            //open and read thr file into a StringBuilder
            InputStream in = mContext.openFileInput(mFileName);
            reader = new BufferedReader((new InputStreamReader(in)));
            StringBuilder jsonString = new StringBuilder();
            String line = null;

            while((line = reader.readLine()) != null)
            {
                //line breaks are ommited and irrelevent
                jsonString.append(line);
            }
            //Parse the JSON using JSONTokener
            JSONArray array = (JSONArray) new JSONTokener(jsonString.toString()).nextValue();
            //Build the array of crimes from JSONObjects
            for (int i = 0; i < array.length(); i++)
            {
                crimes.add(new Crime(array.getJSONObject(i)));
            }
        }
        catch(FileNotFoundException e)
        {
            //Ignorew this one ; it happens when starting afresh
        }
        finally
        {
            if(reader != null)
                reader.close();
        }
        return  crimes;
    }

    public void saveCrimes(ArrayList<Crime> crimes) throws JSONException, IOException
        {
            // build an array in JSON
            JSONArray array = new JSONArray();
            for(Crime c : crimes )
                array.put(c.toJSON());

            //write the file to disk
            Writer writer = null;
            try
            {
                OutputStream out = mContext.openFileOutput(mFileName, Context.MODE_PRIVATE);
                writer = new OutputStreamWriter(out);
                writer.write(array.toString());
            }
            finally {
                    if(writer != null)
                        writer.close();
            }

        }
    }

